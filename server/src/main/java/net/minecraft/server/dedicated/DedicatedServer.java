package net.minecraft.server.dedicated;

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.util.concurrent.FastThreadLocalThread;
import net.minecraft.CrashReport;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.NonNullList;
import net.minecraft.server.DataPackResources;
import net.minecraft.server.IMinecraftServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerCommand;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.progress.WorldLoadListenerFactory;
import net.minecraft.server.network.ITextFilter;
import net.minecraft.server.network.TextFilter;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.server.players.UserCache;
import net.minecraft.server.rcon.RemoteControlCommandListener;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.DataPackConfiguration;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.SaveData;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
// CraftBukkit end

public class DedicatedServer extends MinecraftServer implements IMinecraftServer {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Pattern k = Pattern.compile("^[a-fA-F0-9]{40}$");
  private final java.util.Queue<ServerCommand> serverCommandQueue = new java.util.concurrent.ConcurrentLinkedQueue<>(); // Paper - use a proper queue
  public final RemoteControlCommandListener remoteControlCommandListener;
  public DedicatedServerSettings propertyManager;
  @Nullable
  private final TextFilter r;

  // CraftBukkit start - Signature changed
  public DedicatedServer(joptsimple.OptionSet options, DataPackConfiguration datapackconfiguration, Thread thread, IRegistryCustom.Dimension iregistrycustom_dimension, Convertable.ConversionSession convertable_conversionsession, ResourcePackRepository resourcepackrepository, DataPackResources datapackresources, SaveData savedata, DedicatedServerSettings dedicatedserversettings, DataFixer datafixer, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache, WorldLoadListenerFactory worldloadlistenerfactory) {
    super(options, datapackconfiguration, thread, iregistrycustom_dimension, convertable_conversionsession, savedata, resourcepackrepository, Proxy.NO_PROXY, datafixer, datapackresources, minecraftsessionservice, gameprofilerepository, usercache, worldloadlistenerfactory);
    // CraftBukkit end
    this.propertyManager = dedicatedserversettings;
    this.remoteControlCommandListener = new RemoteControlCommandListener(this);
    this.r = null;
  }

  @Override
  public boolean init() throws IOException {
    Thread thread = new FastThreadLocalThread("Server console handler") {
      public void run() {
        // CraftBukkit start
        if (!org.bukkit.craftbukkit.Main.useConsole) {
          return;
        }
        // Paper start - Use TerminalConsoleAppender
        new com.destroystokyo.paper.console.PaperConsole(DedicatedServer.this).start();
                /*
                jline.console.ConsoleReader bufferedreader = reader;

                // MC-33041, SPIGOT-5538: if System.in is not valid due to javaw, then return
                try {
                    System.in.available();
                } catch (IOException ex) {
                    return;
                }
                // CraftBukkit end

                String s;

                try {
                    // CraftBukkit start - JLine disabling compatibility
                    while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning()) {
                        if (org.bukkit.craftbukkit.Main.useJline) {
                            s = bufferedreader.readLine(">", null);
                        } else {
                            s = bufferedreader.readLine();
                        }

                        // SPIGOT-5220: Throttle if EOF (ctrl^d) or stdin is /dev/null
                        if (s == null) {
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                            continue;
                        }
                        if (s.trim().length() > 0) { // Trim to filter lines which are just spaces
                            DedicatedServer.this.issueCommand(s, DedicatedServer.this.getServerCommandListener());
                        }
                        // CraftBukkit end
                    }
                } catch (IOException ioexception) {
                    DedicatedServer.LOGGER.error("Exception handling console input", ioexception);
                }

                */
        // Paper end
      }
    };

    // CraftBukkit start - TODO: handle command-line logging arguments
    var global = java.util.logging.Logger.getLogger("");
    global.setUseParentHandlers(false);
    for (var handler : global.getHandlers()) {
      global.removeHandler(handler);
    }
    global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());

    // Paper start - Not needed with TerminalConsoleAppender
    var logger = LogManager.getRootLogger();
        /*
        final org.apache.logging.log4j.core.Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger());
        for (org.apache.logging.log4j.core.Appender appender : logger.getAppenders().values()) {
            if (appender instanceof org.apache.logging.log4j.core.appender.ConsoleAppender) {
                logger.removeAppender(appender);
            }
        }

        new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, this.reader).start();
        */
    // Paper end

    System.setOut(IoBuilder.forLogger(logger).setLevel(Level.INFO).buildPrintStream());
    System.setErr(IoBuilder.forLogger(logger).setLevel(Level.WARN).buildPrintStream());
    // CraftBukkit end

    thread.setDaemon(true);
    thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(DedicatedServer.LOGGER));
    thread.start();
    DedicatedServer.LOGGER.info("Starting minecraft server version " + SharedConstants.getGameVersion().getName());
    if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
      DedicatedServer.LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
    }

    DedicatedServer.LOGGER.info("Loading properties");
    var dedicatedserverproperties = this.propertyManager.getProperties();

    if (this.isEmbeddedServer()) {
      this.a_("127.0.0.1");
    } else {
      this.setOnlineMode(dedicatedserverproperties.onlineMode);
      this.e(dedicatedserverproperties.preventProxyConnections);
      this.a_(dedicatedserverproperties.serverIp);
    }
    // Spigot start
    this.a(new DedicatedPlayerList(this, this.customRegistry, this.worldNBTStorage));
    org.spigotmc.SpigotConfig.init((java.io.File) options.valueOf("spigot-settings"));
    org.spigotmc.SpigotConfig.registerCommands();
    // Spigot end
    // Paper start
    try {
      com.destroystokyo.paper.PaperConfig.init((java.io.File) options.valueOf("paper-settings"));
    } catch (Exception e) {
      DedicatedServer.LOGGER.error("Unable to load server configuration", e);
      return false;
    }
    com.destroystokyo.paper.PaperConfig.registerCommands();
    // Purpur start
    try {
      net.pl3x.purpur.PurpurConfig.init((java.io.File) options.valueOf("purpur-settings"));
    } catch (Exception e) {
      DedicatedServer.LOGGER.error("Unable to load server configuration", e);
      return false;
    }
    net.pl3x.purpur.PurpurConfig.registerCommands();
    // Purpur end
    com.destroystokyo.paper.VersionHistoryManager.INSTANCE.getClass(); // load version history now
    io.papermc.paper.brigadier.PaperBrigadierProviderImpl.INSTANCE.getClass(); // init PaperBrigadierProvider
    // Paper end
    com.tuinity.tuinity.config.TuinityConfig.init((java.io.File) options.valueOf("tuinity-settings")); // Tuinity - Server Config

    this.setPVP(dedicatedserverproperties.pvp);
    this.setAllowFlight(dedicatedserverproperties.allowFlight);
    this.setResourcePack(dedicatedserverproperties.resourcePack, this.ba());
    this.setMotd(dedicatedserverproperties.motd);
    // this.saveData.setGameType(dedicatedserverproperties.gamemode); // CraftBukkit - moved to world loading
    DedicatedServer.LOGGER.info("Default game type: {}", dedicatedserverproperties.gamemode);
    // Paper start - Unix domain socket support
    java.net.SocketAddress bindAddress;
    if (this.getServerIp().startsWith("unix:")) {
      if (!IOUring.isAvailable()) {
        DedicatedServer.LOGGER.fatal("**** INVALID CONFIGURATION!");
        DedicatedServer.LOGGER.fatal("You are trying to use a Unix domain socket but you're not on a supported OS.");
        return false;
      } else if (!com.destroystokyo.paper.PaperConfig.velocitySupport && !org.spigotmc.SpigotConfig.bungee) {
        DedicatedServer.LOGGER.fatal("**** INVALID CONFIGURATION!");
        DedicatedServer.LOGGER.fatal("Unix domain sockets require IPs to be forwarded from a proxy.");
        return false;
      }
      bindAddress = new io.netty.channel.unix.DomainSocketAddress(this.getServerIp().substring("unix:".length()));
    } else {
      InetAddress inetaddress = null;

      if (!this.getServerIp().isEmpty()) {
        inetaddress = InetAddress.getByName(this.getServerIp());
      }
      if (this.getPort() < 0) {
        this.setPort(dedicatedserverproperties.serverPort);
      }
      bindAddress = new java.net.InetSocketAddress(inetaddress, this.getPort());
    }
    // Paper end

    this.P();
    DedicatedServer.LOGGER.info("Starting Minecraft server on {}:{}", this.getServerIp().isEmpty() ? "*" : this.getServerIp(), this.getPort());

    try {
      this.getServerConnection().bind(bindAddress); // Paper - Unix domain socket support
    } catch (IOException ioexception) {
      DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
      DedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
      DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
      return false;
    }

    server.loadPlugins();
    server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);

    if (!this.getOnlineMode()) {
      DedicatedServer.LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
      DedicatedServer.LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
      // Spigot start
      if (com.destroystokyo.paper.PaperConfig.isProxyOnlineMode()) { // Purpur
        DedicatedServer.LOGGER.warn("Whilst this makes it possible to use BungeeCord or Velocity, unless access to your server is properly restricted, it also opens up the ability for hackers to connect with any username they choose."); // Purpur
        DedicatedServer.LOGGER.warn("Please see https://www.spigotmc.org/wiki/firewall-guide/ for further information.");
      } else {
        DedicatedServer.LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
        DedicatedServer.LOGGER.warn("You will not be offered any support as long as the server allows offline-mode players to join."); // Purpur
      }
      // Spigot end
      DedicatedServer.LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
    }

    this.c(dedicatedserverproperties.maxBuildHeight);
    TileEntitySkull.a(this.getUserCache());
    TileEntitySkull.a(this.getMinecraftSessionService());
    DedicatedServer.LOGGER.info("Preparing level \"{}\"", this.getWorld());
    this.loadWorld(convertable.getLevelName()); // CraftBukkit

    //DedicatedServer.LOGGER.info("Done ({})! For help, type \"help\"", s); // Paper moved to after init
    if (dedicatedserverproperties.announcePlayerAchievements != null) {
      this.getGameRules().get(GameRules.ANNOUNCE_ADVANCEMENTS).a(dedicatedserverproperties.announcePlayerAchievements, this);
    }

    Items.AIR.a(CreativeModeTab.g, NonNullList.a());

    net.pl3x.purpur.task.TPSBarTask.start(); // Purpur

    return true;
  }

  @Override
  public boolean getSpawnAnimals() {
    return this.getDedicatedServerProperties().spawnAnimals && super.getSpawnAnimals();
  }

  @Override
  public boolean getSpawnMonsters() {
    return this.propertyManager.getProperties().spawnMonsters && super.getSpawnMonsters();
  }

  @Override
  public boolean getSpawnNPCs() {
    return this.propertyManager.getProperties().spawnNpcs && super.getSpawnNPCs();
  }

  public String ba() {
    var dedicatedserverproperties = this.propertyManager.getProperties();
    String s;

    if (!dedicatedserverproperties.resourcePackSha1.isEmpty()) {
      s = dedicatedserverproperties.resourcePackSha1;
      if (!Strings.isNullOrEmpty(dedicatedserverproperties.resourcePackHash)) {
        DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
      }
    } else if (!Strings.isNullOrEmpty(dedicatedserverproperties.resourcePackHash)) {
      DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
      s = dedicatedserverproperties.resourcePackHash;
    } else {
      s = "";
    }

    if (!s.isEmpty() && !DedicatedServer.k.matcher(s).matches()) {
      DedicatedServer.LOGGER.warn("Invalid sha1 for ressource-pack-sha1");
    }

    if (!dedicatedserverproperties.resourcePack.isEmpty() && s.isEmpty()) {
      DedicatedServer.LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
    }

    return s;
  }

  @Override
  public DedicatedServerProperties getDedicatedServerProperties() {
    return this.propertyManager.getProperties();
  }

  @Override
  public void updateWorldSettings() {
    //this.a(this.getDedicatedServerProperties().difficulty, true); // Paper - Don't overwrite level.dat's difficulty, keep current
  }

  @Override
  public boolean isHardcore() {
    return this.getDedicatedServerProperties().hardcore;
  }

  @Override
  public CrashReport b(CrashReport crashreport) {
    crashreport = super.b(crashreport);
    crashreport.g().a("Is Modded", () -> {
      return this.getModded().orElse("Unknown (can't tell)");
    });
    crashreport.g().a("Type", () -> {
      return "Dedicated Server (map_server.txt)";
    });
    return crashreport;
  }

  @Override
  public Optional<String> getModded() {
    var s = this.getServerModName();

    return !"vanilla".equals(s) ? Optional.of("Definitely; Server brand changed to '" + s + "'") : Optional.empty();
  }

  @Override
  public void exit() {
    if (this.r != null) {
      this.r.close();
    }

    hasFullyShutdown = true; // Paper
    System.exit(this.abnormalExit ? 70 : 0); // CraftBukkit // Paper
  }

  @Override
  public void b(BooleanSupplier booleansupplier) {
    super.b(booleansupplier);
    this.handleCommandQueue();
  }

  @Override
  public boolean getAllowNether() {
    return this.getDedicatedServerProperties().allowNether;
  }

  public void issueCommand(String s, CommandListenerWrapper commandlistenerwrapper) {
    this.serverCommandQueue.add(new ServerCommand(s, commandlistenerwrapper));
  }

  public void handleCommandQueue() {
    // Paper start - use proper queue
    ServerCommand servercommand;
    while ((servercommand = this.serverCommandQueue.poll()) != null) {
      // Paper end

      // CraftBukkit start - ServerCommand for preprocessing
      var event = new ServerCommandEvent(console, servercommand.command);
      server.getPluginManager().callEvent(event);
      if (event.isCancelled()) continue;
      servercommand = new ServerCommand(event.getCommand(), servercommand.source);

      // this.getCommandDispatcher().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
      server.dispatchServerCommand(console, servercommand);
      // CraftBukkit end
    }
  }

  @Override
  public boolean j() {
    return true;
  }

  @Override
  public DedicatedPlayerList getPlayerList() {
    return (DedicatedPlayerList) super.getPlayerList();
  }

  @Override
  public boolean n() {
    return true;
  }

  @Override
  public String h_() {
    return this.getServerIp();
  }

  @Override
  public int p() {
    return this.getPort();
  }

  @Override
  public String i_() {
    return this.getMotd();
  }

  @Override
  public boolean ah() {
    return false;
  }

  @Override
  public boolean a(EnumGamemode enumgamemode, boolean flag, int i) {
    return false;
  }

  @Override
  public boolean getEnableCommandBlock() {
    return this.getDedicatedServerProperties().enableCommandBlock;
  }

  @Override
  public boolean a(WorldServer worldserver, BlockPosition blockposition, EntityHuman entityhuman) {
    if (worldserver.getDimensionKey() != World.OVERWORLD) {
      return false;
    } else if (this.getSpawnProtection() <= 0) {
      return false;
    } else {
      var blockposition1 = worldserver.getSpawn();
      var i = MathHelper.a(blockposition.getX() - blockposition1.getX());
      var j = MathHelper.a(blockposition.getZ() - blockposition1.getZ());
      var k = Math.max(i, j);

      return k <= this.getSpawnProtection();
    }
  }

  @Override
  public boolean am() {
    return this.getDedicatedServerProperties().enableStatus;
  }

  @Override
  public int g() {
    return this.getDedicatedServerProperties().opPermissionLevel;
  }

  @Override
  public int h() {
    return this.getDedicatedServerProperties().functionPermissionLevel;
  }

  @Override
  public int au() {
    return this.getDedicatedServerProperties().maxWorldSize;
  }

  @Override
  public int ax() {
    return this.getDedicatedServerProperties().networkCompressionThreshold;
  }

  @Override
  public String getPlugins() {
    // CraftBukkit start - Whole method
    var result = dev.kamillaova.paper.pool.StringBuilderPool.take();
    var plugins = server.getPluginManager().getPlugins();

    result.append(server.getName());
    result.append(" on Bukkit ");
    result.append(server.getBukkitVersion());

    if (plugins.length > 0 && server.getQueryPlugins()) {
      result.append(": ");

      for (var i = 0; i < plugins.length; i++) {
        if (i > 0) {
          result.append("; ");
        }

        result.append(plugins[i].getDescription().getName());
        result.append(" ");
        result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
      }
    }

    return result.toString();
    // CraftBukkit end
  }

  @Override
  public String executeRemoteCommand(String s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void stop() {
    super.stop();
    //SystemUtils.h(); // Paper - moved into super
  }

  @Override
  public boolean a(GameProfile gameprofile) {
    return false;
  }

  @Override
  public int b(int i) {
    return this.getDedicatedServerProperties().entityBroadcastRangePercentage * i / 100;
  }

  @Override
  public String getWorld() {
    return this.convertable.getLevelName();
  }

  @Override
  public boolean isSyncChunkWrites() {
    return this.propertyManager.getProperties().syncChunkWrites;
  }

  @Nullable
  @Override
  public ITextFilter a(EntityPlayer entityplayer) {
    return this.r != null ? this.r.a(entityplayer.getProfile()) : null;
  }

  // CraftBukkit start
  public boolean isDebugging() {
    return this.getDedicatedServerProperties().debug;
  }

  @Override
  public CommandSender getBukkitSender(CommandListenerWrapper wrapper) {
    return console;
  }
  // CraftBukkit end
}
