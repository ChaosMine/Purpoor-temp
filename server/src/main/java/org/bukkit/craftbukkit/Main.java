package org.bukkit.craftbukkit;

import dev.kamillaova.paper.server.config.BootstrapConfig;
import io.netty.util.ResourceLeakDetector;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.util.ExceptionSuppressor;
import net.minecraft.world.level.lighting.LightEngineLayerEventListener;
import net.minecrell.terminalconsole.TerminalConsoleAppender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main {
	public static boolean useJline = true;
	public static boolean useConsole = true;

	public static void main(String... args) {
		Thread.currentThread().setName("Bootstrap thread");

		var bootstrapConfig = BootstrapConfig.load(Path.of("bootstrap-config.json"));

		// Set netty threads count for minecraft listener
		if (System.getProperty("purpoor.nettyThreads") == null) {
			System.setProperty("purpoor.nettyThreads", String.valueOf(bootstrapConfig.nettyThreads()));
		}

		// By default, Netty allocates 16MiB arenas for the PooledByteBufAllocator. This is too much
		// memory for Minecraft, which imposes a maximum packet size of 2MiB! We'll use 4MiB as a more
		// sane default.
		//
		// Note: io.netty.allocator.pageSize << io.netty.allocator.maxOrder is the formula used to
		// compute the chunk size. We lower maxOrder from its default of 11 to 9. (We also use a null
		// check, so that the user is free to choose another setting if need be.)
		if (System.getProperty("io.netty.allocator.maxOrder") == null) {
			System.setProperty("io.netty.allocator.maxOrder", "9");
		}

		// We allocate one arena per a worker thread + 2 (for other threads) to reduce contention.
		//
		// NOTE: Before overriding it, we check whether or not it was set before. This way users
		// will have a chance to tune it.
		if (System.getProperty("io.netty.allocator.numDirectArenas") == null) {
			System.setProperty("io.netty.allocator.numDirectArenas", String.valueOf(bootstrapConfig.nettyThreads() + 2));
		}

		// Set the number of heap arenas the number of worker thread count + 2 (for other threads)
		if (System.getProperty("io.netty.allocator.numHeapArenas") == null) {
			System.setProperty("io.netty.allocator.numHeapArenas", String.valueOf(bootstrapConfig.nettyThreads() + 2));
		}

		// We're disabling Netty 4 recyclers (lightweight object pools) as we found out they
		// come at the non-trivial cost of CPU overhead.
		//
		// NOTE: Before overriding it, we check whether or not it was set before. This way users
		// will have a chance to tune it.
		if (System.getProperty("io.netty.recycler.maxCapacity") == null
			&& System.getProperty("io.netty.recycler.maxCapacityPerThread") == null
		) {
			System.setProperty("io.netty.recycler.maxCapacity", "0");
			System.setProperty("io.netty.recycler.maxCapacityPerThread", "0");
		}

		// Disable the resource leak detector by default as it reduces performance.
		// Allow the user to override this if desired.
		if (System.getProperty("io.netty.leakDetection.level") == null) {
			ResourceLeakDetector.setLevel(bootstrapConfig.nettyLeakDetectorLevel());
		}

		// Cap per-thread NIO cache size
		if (System.getProperty("jdk.nio.maxCachedBufferSize") == null) {
			System.setProperty("jdk.nio.maxCachedBufferSize", "262144");
		}

		// We set a sane default and reject client initiated TLS/SSL session
		// renegotiation's (for security reasons).
		//
		// NOTE: This property affects both JDK SSL (Java 8+) and Netty 4 OpenSSL
		// implementations.
		if (System.getProperty("jdk.tls.rejectClientInitiatedRenegotiation") == null) {
			System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true");
		}

		var parser = new OptionParser() {{
			acceptsAll(List.of("?", "help"), "Show the help");

			acceptsAll(List.of("c", "config"), "Properties file to use")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("server.properties"))
				.describedAs("Properties file");

			acceptsAll(List.of("P", "plugins"), "Plugin directory to use")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("plugins"))
				.describedAs("Plugin directory");

			acceptsAll(List.of("h", "host", "server-ip"), "Host to listen on")
				.withRequiredArg()
				.ofType(String.class)
				.describedAs("Hostname or IP");

			acceptsAll(List.of("W", "world-dir", "universe", "world-container"), "World container")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("."))
				.describedAs("Directory containing worlds");

			acceptsAll(List.of("w", "world", "level-name"), "World name")
				.withRequiredArg()
				.ofType(String.class)
				.describedAs("World name");

			acceptsAll(List.of("p", "port", "server-port"), "Port to listen on")
				.withRequiredArg()
				.ofType(Integer.class)
				.describedAs("Port");

			acceptsAll(List.of("o", "online-mode"), "Whether to use online authentication")
				.withRequiredArg()
				.ofType(Boolean.class)
				.describedAs("Authentication");

			acceptsAll(List.of("s", "size", "max-players"), "Maximum amount of players")
				.withRequiredArg()
				.ofType(Integer.class)
				.describedAs("Server size");

			acceptsAll(List.of("d", "date-format"), "Format of the date to display in the console (for log entries)")
				.withRequiredArg()
				.ofType(SimpleDateFormat.class)
				.describedAs("Log date format");

			acceptsAll(List.of("log-pattern"), "Specfies the log filename pattern")
				.withRequiredArg()
				.ofType(String.class)
				.defaultsTo("server.log")
				.describedAs("Log filename");

			acceptsAll(List.of("log-limit"), "Limits the maximum size of the log file (0 = unlimited)")
				.withRequiredArg()
				.ofType(Integer.class)
				.defaultsTo(0)
				.describedAs("Max log size");

			acceptsAll(List.of("log-count"), "Specified how many log files to cycle through")
				.withRequiredArg()
				.ofType(Integer.class)
				.defaultsTo(1)
				.describedAs("Log count");

			acceptsAll(List.of("log-append"), "Whether to append to the log file")
				.withRequiredArg()
				.ofType(Boolean.class)
				.defaultsTo(true)
				.describedAs("Log append");

			acceptsAll(List.of("log-strip-color"), "Strips color codes from log file");

			acceptsAll(List.of("b", "bukkit-settings"), "File for bukkit settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("bukkit.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("C", "commands-settings"), "File for command settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("commands.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("forceUpgrade"), "Whether to force a world upgrade");
			acceptsAll(List.of("eraseCache"), "Whether to force cache erase during world upgrade");
			acceptsAll(List.of("nogui"), "Disables the graphical console");

			acceptsAll(List.of("nojline"), "Disables jline and emulates the vanilla console");

			acceptsAll(List.of("noconsole"), "Disables the console");

			acceptsAll(List.of("v", "version"), "Show the CraftBukkit Version");

			acceptsAll(List.of("S", "spigot-settings"), "File for spigot settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("spigot.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("paper", "paper-settings"), "File for paper settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("paper.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("tuinity", "tuinity-settings"), "File for tuinity settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("tuinity.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("purpur", "purpur-settings"), "File for purpur settings")
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File("purpur.yml"))
				.describedAs("Yml file");

			acceptsAll(List.of("server-name"), "Name of the server")
				.withRequiredArg()
				.ofType(String.class)
				.defaultsTo("Unknown Server")
				.describedAs("Name");

			acceptsAll(List.of("add-plugin", "add-extra-plugin-jar"))
				.withRequiredArg()
				.ofType(File.class)
				.defaultsTo(new File[]{})
				.describedAs("Specify paths to extra plugin jars to be loaded in addition to those in the plugins folder. This argument can be specified multiple times, once for each extra plugin jar path.");
		}};

		OptionSet options = null;

		tryPreloadClass("com.destroystokyo.paper.log.LogFullPolicy");
		tryPreloadClass("org.apache.logging.log4j.core.Core");
		tryPreloadClass("org.apache.logging.log4j.core.Appender");
		tryPreloadClass("org.apache.logging.log4j.core.ContextDataInjector");
		tryPreloadClass("org.apache.logging.log4j.core.Filter");
		tryPreloadClass("org.apache.logging.log4j.core.ErrorHandler");
		tryPreloadClass("org.apache.logging.log4j.core.LogEvent");
		tryPreloadClass("org.apache.logging.log4j.core.Logger");
		tryPreloadClass("org.apache.logging.log4j.core.LoggerContext");
		tryPreloadClass("org.apache.logging.log4j.core.LogEventListener");
		tryPreloadClass("org.apache.logging.log4j.core.AbstractLogEvent");
		tryPreloadClass("org.apache.logging.log4j.message.AsynchronouslyFormattable");
		tryPreloadClass("org.apache.logging.log4j.message.FormattedMessage");
		tryPreloadClass("org.apache.logging.log4j.message.ParameterizedMessage");
		tryPreloadClass("org.apache.logging.log4j.message.Message");
		tryPreloadClass("org.apache.logging.log4j.message.MessageFactory");
		tryPreloadClass("org.apache.logging.log4j.message.TimestampMessage");
		tryPreloadClass("org.apache.logging.log4j.message.SimpleMessage");
		tryPreloadClass("org.apache.logging.log4j.core.async.AsyncLogger");
		tryPreloadClass("org.apache.logging.log4j.core.async.AsyncLoggerContext");
		tryPreloadClass("org.apache.logging.log4j.core.async.AsyncQueueFullPolicy");
		tryPreloadClass("org.apache.logging.log4j.core.async.AsyncLoggerDisruptor");
		tryPreloadClass("org.apache.logging.log4j.core.async.RingBufferLogEvent");
		tryPreloadClass("org.apache.logging.log4j.core.async.DisruptorUtil");
		tryPreloadClass("org.apache.logging.log4j.core.async.RingBufferLogEventHandler");
		tryPreloadClass("org.apache.logging.log4j.core.impl.ThrowableProxy");
		tryPreloadClass("org.apache.logging.log4j.core.impl.ExtendedClassInfo");
		tryPreloadClass("org.apache.logging.log4j.core.impl.ExtendedStackTraceElement");
		tryPreloadClass("org.jline.terminal.impl.DumbTerminal");
		tryPreloadClass("com.google.common.collect.Maps$Values");
		tryPreloadClass("org.apache.logging.log4j.core.async.InternalAsyncUtil");
		tryPreloadClass("io.netty.channel.DefaultChannelProgressivePromise");
		tryPreloadClass("io.netty.util.concurrent.GenericProgressiveFutureListener");

		try {
			options = parser.parse(args);
		} catch (joptsimple.OptionException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
		}

		if ((options == null) || (options.has("?"))) {
			try {
				parser.printHelpOn(System.out);
			} catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (options.has("v")) {
			System.out.println(CraftServer.class.getPackage().getImplementationVersion());
		} else {
			// Do you love Java using + and ! as string based identifiers? I sure do!
			var path = new File(".").getAbsolutePath();
			if (path.contains("!") || path.contains("+")) {
				System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
				return;
			}
			try {
				// Paper start - Handled by TerminalConsoleAppender
				if (options.has("nojline")) {
					System.setProperty(TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false");
					useJline = false;
				}
				// Paper end

				if (options.has("noconsole")) {
					useConsole = false;
					useJline = false; // Paper
					System.setProperty(TerminalConsoleAppender.JLINE_OVERRIDE_PROPERTY, "false"); // Paper
				}

				// Paper start - Log Java and OS versioning to help with debugging plugin issues
				var runtimeMX = java.lang.management.ManagementFactory.getRuntimeMXBean();
				var osMX = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
				if (runtimeMX != null && osMX != null) {
					var javaInfo = "Java " + runtimeMX.getSpecVersion() + " (" + runtimeMX.getVmName() + " " + runtimeMX.getVmVersion() + ")";
					var osInfo = "Host: " + osMX.getName() + " " + osMX.getVersion() + " (" + osMX.getArch() + ")";

					System.out.println("System Info: " + javaInfo + " " + osInfo);
				} else {
					System.out.println("Unable to read system info");
				}
				// Paper end
				System.setProperty("library.jansi.version", "Paper"); // Paper - set meaningless jansi version to prevent git builds from crashing on Windows
				System.out.println("Loading libraries, please wait...");
				net.minecraft.server.Main.main(options);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			// Paper start
			// load some required classes to avoid errors during shutdown if jar is replaced
			// also to guarantee our version loads over plugins
			tryPreloadClass("com.destroystokyo.paper.util.SneakyThrow");
			tryPreloadClass("com.google.common.collect.Iterators$PeekingImpl");
			tryPreloadClass("com.google.common.collect.MapMakerInternalMap$Values");
			tryPreloadClass("com.google.common.collect.MapMakerInternalMap$ValueIterator");
			tryPreloadClass("com.google.common.collect.MapMakerInternalMap$WriteThroughEntry");
			tryPreloadClass("com.google.common.collect.Iterables");
			for (var i = 1; i <= 15; i++) {
				tryPreloadClass("com.google.common.collect.Iterables$" + i, false);
			}
			tryPreloadClass("org.apache.commons.lang3.mutable.MutableBoolean");
			tryPreloadClass("org.apache.commons.lang3.mutable.MutableInt");
			tryPreloadClass("org.jline.terminal.impl.MouseSupport");
			tryPreloadClass("org.jline.terminal.impl.MouseSupport$1");
			tryPreloadClass("org.jline.terminal.Terminal$MouseTracking");
			tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext");
			tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext$11");
			tryPreloadClass("io.netty.channel.AbstractChannelHandlerContext$12");
			tryPreloadClass("io.netty.channel.AbstractChannel$AbstractUnsafe$8");
			tryPreloadClass("io.netty.util.concurrent.DefaultPromise");
			tryPreloadClass("io.netty.util.concurrent.DefaultPromise$1");
			tryPreloadClass("io.netty.util.internal.PromiseNotificationUtil");
			tryPreloadClass("io.netty.util.internal.SystemPropertyUtil");
			tryPreloadClass("org.bukkit.craftbukkit.scheduler.CraftScheduler");
			tryPreloadClass("org.bukkit.craftbukkit.scheduler.CraftScheduler$1");
			tryPreloadClass("org.bukkit.craftbukkit.scheduler.CraftScheduler$2");
			tryPreloadClass("org.bukkit.craftbukkit.scheduler.CraftScheduler$3");
			tryPreloadClass("org.slf4j.helpers.MessageFormatter");
			tryPreloadClass("org.slf4j.helpers.FormattingTuple");
			tryPreloadClass("org.slf4j.helpers.BasicMarker");
			tryPreloadClass("org.slf4j.helpers.Util");
			tryPreloadClass("com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent");
			tryPreloadClass("com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent");
			// Minecraft, seen during saving
			tryPreloadClass(LightEngineLayerEventListener.Void.class.getName());
			tryPreloadClass(LightEngineLayerEventListener.class.getName());
			tryPreloadClass(ExceptionSuppressor.class.getName());
			// Paper end
		}
	}

	private static void tryPreloadClass(String className) {
		tryPreloadClass(className, true);
	}

	private static void tryPreloadClass(String className, boolean printError) {
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			if (printError){
				System.err.println("An expected class  " + className + " was not found for preloading: " + e.getMessage());
			}
		}
	}
}
