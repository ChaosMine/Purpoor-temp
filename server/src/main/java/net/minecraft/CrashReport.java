package net.minecraft;

import com.google.common.collect.Lists;
import dev.kamillaova.paper.pool.StringBuilderPool;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionException;

public class CrashReport {

	private static final Logger LOGGER = LogManager.getLogger();
	private final String b;
	private final Throwable c;
	private final CrashReportSystemDetails d = new CrashReportSystemDetails(this, "System Details");
	private final List<CrashReportSystemDetails> e = Lists.newArrayList();
	private File f;
	private boolean g = true;
	private StackTraceElement[] h = new StackTraceElement[0];

	public CrashReport(String s, Throwable throwable) {
		this.b = s;
		this.c = throwable;
		this.i();
	}

	private void i() {
		this.d.a("Minecraft Version", () -> SharedConstants.getGameVersion().getName());
		this.d.a("Minecraft Version ID", () -> SharedConstants.getGameVersion().getId());
		this.d.a("Operating System", () -> System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
		this.d.a("Java Version", () -> System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
		this.d.a("Java VM Version", () -> System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
		this.d.a("Memory", () -> {
			var runtime = Runtime.getRuntime();
			var i = runtime.maxMemory();
			var j = runtime.totalMemory();
			var k = runtime.freeMemory();
			var l = i / 1024L / 1024L;
			var i1 = j / 1024L / 1024L;
			var j1 = k / 1024L / 1024L;

			return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
		});
		this.d.a("CPUs", Runtime.getRuntime().availableProcessors());
		this.d.a("JVM Flags", () -> {
			var list = SystemUtils.j().toList();

			return String.format("%d total; %s", list.size(), String.join(" ", list));
		});
		this.d.a("CraftBukkit Information", new org.bukkit.craftbukkit.CraftCrashReport()); // CraftBukkit
	}

	public String a() {
		return this.b;
	}

	public Throwable b() {
		return this.c;
	}

	public void a(StringBuilder stringbuilder) {
		if ((this.h == null || this.h.length <= 0) && !this.e.isEmpty()) {
			this.h = ArrayUtils.subarray(this.e.get(0).a(), 0, 1);
		}

		if (this.h != null && this.h.length > 0) {
			stringbuilder.append("-- Head --\n");
			stringbuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
			stringbuilder.append("Stacktrace:\n");
			var astacktraceelement = this.h;
			var i = astacktraceelement.length;

			for (var j = 0; j < i; ++j) {
				var stacktraceelement = astacktraceelement[j];

				stringbuilder.append("\t").append("at ").append(stacktraceelement);
				stringbuilder.append("\n");
			}

			stringbuilder.append("\n");
		}

		for (var crashreportsystemdetails : this.e) {
			crashreportsystemdetails.a(stringbuilder);
			stringbuilder.append("\n\n");
		}

		this.d.a(stringbuilder);
	}

	public String d() {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		var throwable = this.c;

		if (throwable.getMessage() == null) {
			if (throwable instanceof NullPointerException) {
				throwable = new NullPointerException(this.b);
			} else if (throwable instanceof StackOverflowError) {
				throwable = new StackOverflowError(this.b);
			} else if (throwable instanceof OutOfMemoryError) {
				throwable = new OutOfMemoryError(this.b);
			}

			throwable.setStackTrace(this.c.getStackTrace());
		}

		String s;

		try {
			stringwriter = new StringWriter();
			printwriter = new PrintWriter(stringwriter);
			throwable.printStackTrace(printwriter);
			s = stringwriter.toString();
		} finally {
			IOUtils.closeQuietly(stringwriter);
			IOUtils.closeQuietly(printwriter);
		}

		return s;
	}

	public String e() {
		var sb = StringBuilderPool.take();

		sb.append("---- Minecraft Crash Report ----\n");
		sb.append("// ");
		sb.append(j());
		sb.append("\n\n");
		sb.append("Time: ");
		sb.append((new SimpleDateFormat()).format(new Date()));
		sb.append("\n");
		sb.append("Description: ");
		sb.append(this.b);
		sb.append("\n\n");
		sb.append(this.d());
		sb.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

		sb.append("-".repeat(87));

		sb.append("\n\n");
		this.a(sb);
		return sb.toString();
	}

	public boolean a(File file) {
		if (this.f != null) {
			return false;
		} else {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}

			OutputStreamWriter outputstreamwriter = null;

			try {
				outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
				outputstreamwriter.write(this.e());
				this.f = file;
				return true;
			} catch (Throwable throwable) {
				CrashReport.LOGGER.error("Could not save crash report to {}", file, throwable);
			} finally {
				IOUtils.closeQuietly(outputstreamwriter);
			}

			return false;
		}
	}

	public CrashReportSystemDetails g() {
		return this.d;
	}

	public CrashReportSystemDetails a(String s) {
		return this.a(s, 1);
	}

	public CrashReportSystemDetails a(String s, int i) {
		var crashreportsystemdetails = new CrashReportSystemDetails(this, s);

		if (this.g) {
			var j = crashreportsystemdetails.a(i);
			var astacktraceelement = this.c.getStackTrace();
			StackTraceElement stacktraceelement = null;
			StackTraceElement stacktraceelement1 = null;
			var k = astacktraceelement.length - j;

			if (k < 0) {
				System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + j + ")");
			}

			if (0 <= k && k < astacktraceelement.length) {
				stacktraceelement = astacktraceelement[k];
				if (astacktraceelement.length + 1 - j < astacktraceelement.length) {
					stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - j];
				}
			}

			this.g = crashreportsystemdetails.a(stacktraceelement, stacktraceelement1);
			if (j > 0 && !this.e.isEmpty()) {
				var crashreportsystemdetails1 = this.e.get(this.e.size() - 1);

				crashreportsystemdetails1.b(j);
			} else if (astacktraceelement != null && astacktraceelement.length >= j && 0 <= k && k < astacktraceelement.length) {
				this.h = new StackTraceElement[k];
				System.arraycopy(astacktraceelement, 0, this.h, 0, this.h.length);
			} else {
				this.g = false;
			}
		}

		this.e.add(crashreportsystemdetails);
		return crashreportsystemdetails;
	}

	private static String j() {
		var astring = new String[]{"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

		try {
			return astring[(int) (SystemUtils.getMonotonicNanos() % (long) astring.length)];
		} catch (Throwable throwable) {
			return "Witty comment unavailable :(";
		}
	}

	public static CrashReport a(Throwable throwable, String s) {
		if (throwable instanceof ThreadDeath) com.destroystokyo.paper.util.SneakyThrow.sneaky(throwable); // Paper
		while (throwable instanceof CompletionException && throwable.getCause() != null) {
			throwable = throwable.getCause();
		}

		CrashReport crashreport;

		if (throwable instanceof ReportedException) {
			crashreport = ((ReportedException) throwable).a();
		} else {
			crashreport = new CrashReport(s, throwable);
		}

		return crashreport;
	}

	public static void h() {
		(new CrashReport("Don't panic!", new Throwable())).e();
	}
}
