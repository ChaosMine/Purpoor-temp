package net.minecraft.server;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import it.unimi.dsi.fastutil.longs.LongIterator;
import joptsimple.OptionSet;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICommandListener;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.data.worldgen.BiomeDecoratorGroups;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutServerDifficulty;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateTime;
import net.minecraft.network.protocol.status.ServerPing;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.bossevents.BossBattleCustomData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldProviderNormal;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.progress.WorldLoadListener;
import net.minecraft.server.level.progress.WorldLoadListenerFactory;
import net.minecraft.server.network.ITextFilter;
import net.minecraft.server.network.ServerConnection;
import net.minecraft.server.packs.repository.ResourcePackLoader;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserCache;
import net.minecraft.tags.ITagRegistry;
import net.minecraft.tags.TagsBlock;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.thread.IAsyncTaskHandlerReentrant;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.MobSpawnerCat;
import net.minecraft.world.entity.npc.MobSpawnerTrader;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntityHopper;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.levelgen.GeneratorSettings;
import net.minecraft.world.level.levelgen.MobSpawnerPatrol;
import net.minecraft.world.level.levelgen.MobSpawnerPhantom;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
import net.minecraft.world.level.saveddata.RunnableSaveScoreboard;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.level.storage.loot.LootPredicateManager;
import net.minecraft.world.level.storage.loot.LootTableRegistry;
import net.minecraft.world.phys.Vec2F;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.scores.PersistentScoreboard;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.server.ServerLoadEvent;
import org.spigotmc.SlackActivityAccountant;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class MinecraftServer extends IAsyncTaskHandlerReentrant<TickTask> implements ICommandListener, AutoCloseable {
	public static MinecraftServer SERVER; // Paper
	public static final Logger LOGGER = LogManager.getLogger();
	public static final WorldSettings c = new WorldSettings("Demo World", EnumGamemode.SURVIVAL, false, EnumDifficulty.NORMAL, false, new GameRules(), DataPackConfiguration.a);
	public Convertable.ConversionSession convertable;
	public final WorldNBTStorage worldNBTStorage;
	private final List<Runnable> tickables = Lists.newArrayList();
	private ServerConnection serverConnection;
	public final WorldLoadListenerFactory worldLoadListenerFactory;
	private final ServerPing serverPing;
	private final Random r;
	public final DataFixer dataConverterManager;
	private String serverIp;
	private int serverPort;
	public final IRegistryCustom.Dimension customRegistry;
	public final Map<ResourceKey<World>, WorldServer> worldServer;
	private PlayerList playerList;
	private volatile boolean isRunning;
	private volatile boolean isRestarting = false; // Paper - flag to signify we're attempting to restart
	private boolean isStopped;
	private int ticks;
	protected final Proxy proxy;
	private boolean onlineMode;
	private boolean B;
	private boolean pvpMode;
	private boolean allowFlight;
	@Nullable
	private String motd;
	private int F;
	public final long[] h;

	public long[] getTickTimes() {
		return h;
	} // Paper - OBFHELPER

	// Paper start
	public final TickTimes tickTimes5s = new TickTimes(100);
	public final TickTimes tickTimes10s = new TickTimes(200);
	public final TickTimes tickTimes60s = new TickTimes(1200);
	// Paper end
	@Nullable
	private KeyPair H;
	@Nullable
	private String I;
	private boolean demoMode;
	private String K;
	private String L;
	private volatile boolean hasTicked;
	private long lastOverloadTime;
	private boolean P;
	private final MinecraftSessionService minecraftSessionService;
	private final GameProfileRepository gameProfileRepository;
	private final UserCache userCache;
	private long T;
	public final Thread serverThread;
	private long nextTick;
	private long W;

	final long getTickOversleepMaxTime() {
		return this.W;
	} // Paper - OBFHELPER

	private boolean X;

	final boolean hasExecutedTask() {
		return this.X;
	} // Paper - OBFHELPER

	private final ResourcePackRepository resourcePackRepository;
	private final ScoreboardServer scoreboardServer;
	@Nullable
	private PersistentCommandStorage persistentCommandStorage;
	private final BossBattleCustomData bossBattleCustomData;
	private final CustomFunctionData customFunctionData;
	private final CircularTimer circularTimer;
	private float ag;
	public final Executor executorService;
	@Nullable
	private String ai;
	public DataPackResources dataPackResources;
	private final DefinedStructureManager ak;
	protected SaveData saveData;

	// CraftBukkit start
	public DataPackConfiguration datapackconfiguration;
	public org.bukkit.craftbukkit.CraftServer server;
	public OptionSet options;
	public org.bukkit.command.ConsoleCommandSender console;
	//public ConsoleReader reader; // Paper
	public static int currentTick = 0; // Paper - Further improve tick loop
	public static long currentTickLong = 0L; // Tuinity
	public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
	public int autosavePeriod;
	public boolean serverAutoSave = false; // Paper
	public CommandDispatcher vanillaCommandDispatcher;
	public boolean forceTicks; // Paper
	// CraftBukkit end
	// Spigot start
	public static final int TPS = 20;
	public static final int TICK_TIME = 1000000000 / TPS;
	private static final int SAMPLE_INTERVAL = 20; // Paper
	public final double[] recentTps = new double[4]; // Purpur
	public boolean lagging = false; // Purpur
	public final SlackActivityAccountant slackActivityAccountant = new SlackActivityAccountant();
	// Spigot end

	public volatile Thread shutdownThread; // Paper
	public volatile boolean abnormalExit = false; // Paper

	public static <S extends MinecraftServer> S a(Function<Thread, S> function) {
		AtomicReference<S> atomicreference = new AtomicReference();
		var thread = new FastThreadLocalThread(() -> {
			atomicreference.get().w();
		}, "Server thread");

		thread.setPriority(Thread.MAX_PRIORITY - 2);
		thread.setUncaughtExceptionHandler((thread1, throwable) -> {
			MinecraftServer.LOGGER.error(throwable);
		});
		var s0 = function.apply(thread); // CraftBukkit - decompile error

		atomicreference.set(s0);
		thread.start();
		return s0;
	}

	public MinecraftServer(OptionSet options, DataPackConfiguration datapackconfiguration, Thread thread, IRegistryCustom.Dimension iregistrycustom_dimension, Convertable.ConversionSession convertable_conversionsession, SaveData savedata, ResourcePackRepository resourcepackrepository, Proxy proxy, DataFixer datafixer, DataPackResources datapackresources, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache, WorldLoadListenerFactory worldloadlistenerfactory) {
		super("Server");
		SERVER = this;
		this.serverPing = new ServerPing();
		this.r = new Random();
		this.serverPort = -1;
		this.worldServer = new LinkedHashMap<>();
		this.isRunning = true;
		this.h = new long[100];
		this.K = "";
		this.L = "";
		this.nextTick = SystemUtils.getMonotonicMillis();
		this.scoreboardServer = new ScoreboardServer(this);
		this.bossBattleCustomData = new BossBattleCustomData();
		this.circularTimer = new CircularTimer();
		this.customRegistry = iregistrycustom_dimension;
		this.saveData = savedata;
		this.proxy = proxy;
		this.resourcePackRepository = resourcepackrepository;
		this.dataPackResources = datapackresources;
		this.minecraftSessionService = minecraftsessionservice;
		this.gameProfileRepository = gameprofilerepository;
		this.userCache = usercache;
		this.worldLoadListenerFactory = worldloadlistenerfactory;
		this.convertable = convertable_conversionsession;
		this.worldNBTStorage = convertable_conversionsession.b();
		this.dataConverterManager = datafixer;
		this.customFunctionData = new CustomFunctionData(this, datapackresources.a());
		this.ak = new DefinedStructureManager(datapackresources.h(), convertable_conversionsession, datafixer);
		this.serverThread = thread;
		this.executorService = SystemUtils.f();
		this.options = options;
		this.datapackconfiguration = datapackconfiguration;
		this.vanillaCommandDispatcher = datapackresources.commandDispatcher; // CraftBukkit
		Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));
	}

	private void initializeScoreboards(WorldPersistentData worldpersistentdata) {
		var persistentscoreboard = worldpersistentdata.a(PersistentScoreboard::new, "scoreboard");

		persistentscoreboard.a(this.getScoreboard());
		this.getScoreboard().a(new RunnableSaveScoreboard(persistentscoreboard));
	}

	protected abstract boolean init() throws IOException;

	public static void convertWorld(Convertable.ConversionSession convertable_conversionsession) {
		if (convertable_conversionsession.isConvertable()) {
			MinecraftServer.LOGGER.info("Converting map! {}", convertable_conversionsession.getLevelName()); // CraftBukkit
			convertable_conversionsession.convert(new IProgressUpdate() {
				private long a = SystemUtils.getMonotonicMillis();

				@Override
				public void a(IChatBaseComponent ichatbasecomponent) {
				}

				@Override
				public void a(int i) {
					if (SystemUtils.getMonotonicMillis() - this.a >= 1000L) {
						this.a = SystemUtils.getMonotonicMillis();
						MinecraftServer.LOGGER.info("Converting... {}%", i);
					}

				}

				@Override
				public void c(IChatBaseComponent ichatbasecomponent) {
				}
			});
		}

	}

	protected void loadWorld(String s) {
		// CraftBukkit start
		var worldSession = this.convertable;
		var iregistrycustom_dimension = this.customRegistry;
		RegistryReadOps<NBTBase> registryreadops = RegistryReadOps.a((DynamicOps) DynamicOpsNBT.a, this.dataPackResources.h(), iregistrycustom_dimension);
		var overworldData = (WorldDataServer) worldSession.a(registryreadops, datapackconfiguration);
		if (overworldData == null) {
			WorldSettings worldsettings;
			GeneratorSettings generatorsettings;

			if (this.isDemoMode()) {
				worldsettings = MinecraftServer.c;
				generatorsettings = GeneratorSettings.a(iregistrycustom_dimension);
			} else {
				var dedicatedserverproperties = ((DedicatedServer) this).getDedicatedServerProperties();

				worldsettings = new WorldSettings(dedicatedserverproperties.levelName, dedicatedserverproperties.gamemode, dedicatedserverproperties.hardcore, dedicatedserverproperties.difficulty, false, new GameRules(), datapackconfiguration);
				generatorsettings = options.has("bonusChest") ? dedicatedserverproperties.generatorSettings.j() : dedicatedserverproperties.generatorSettings;
			}

			overworldData = new WorldDataServer(worldsettings, generatorsettings, Lifecycle.stable());
		}

		var overworldSettings = overworldData.getGeneratorSettings();
		var registrymaterials = overworldSettings.d();
		for (var entry : registrymaterials.d()) {
			var dimensionKey = entry.getKey();

			WorldServer world;
			var dimension = 0;

			if (dimensionKey == WorldDimension.THE_NETHER) {
				if (getAllowNether()) {
					dimension = -1;
				} else {
					continue;
				}
			} else if (dimensionKey == WorldDimension.THE_END) {
				if (server.getAllowEnd()) {
					dimension = 1;
				} else {
					continue;
				}
			} else if (dimensionKey != WorldDimension.OVERWORLD) {
				dimension = -999;
			}

			var worldType = (dimension == -999) ? dimensionKey.a().getNamespace() + "_" + dimensionKey.a().getKey() : org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
			var name = (dimensionKey == WorldDimension.OVERWORLD) ? s : s + "_" + worldType;
			if (dimension != 0) {
				var newWorld = Convertable.getFolder(new File(name), dimensionKey);
				var oldWorld = Convertable.getFolder(new File(s), dimensionKey);
				var oldLevelDat = new File(new File(s), "level.dat"); // The data folders exist on first run as they are created in the PersistentCollection constructor above, but the level.dat won't

				if (!newWorld.isDirectory() && oldWorld.isDirectory() && oldLevelDat.isFile()) {
					MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
					MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
					MinecraftServer.LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
					MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

					if (newWorld.exists()) {
						MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
						MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
					} else if (newWorld.getParentFile().mkdirs()) {
						if (oldWorld.renameTo(newWorld)) {
							MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
							// Migrate world data too.
							try {
								com.google.common.io.Files.copy(oldLevelDat, new File(new File(name), "level.dat"));
								org.apache.commons.io.FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
							} catch (IOException exception) {
								MinecraftServer.LOGGER.warn("Unable to migrate world data.");
							}
							MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
						} else {
							MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
							MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
						}
					} else {
						MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
						MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
					}
				}

				try {
					worldSession = Convertable.a(server.getWorldContainer().toPath()).c(name, dimensionKey);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				MinecraftServer.convertWorld(worldSession); // Run conversion now
			}

			var gen = this.server.getGenerator(name);

			var worlddata = (WorldDataServer) worldSession.a(registryreadops, datapackconfiguration);
			if (worlddata == null) {
				WorldSettings worldsettings;
				GeneratorSettings generatorsettings;

				if (this.isDemoMode()) {
					worldsettings = MinecraftServer.c;
					generatorsettings = GeneratorSettings.a(iregistrycustom_dimension);
				} else {
					var dedicatedserverproperties = ((DedicatedServer) this).getDedicatedServerProperties();

					worldsettings = new WorldSettings(dedicatedserverproperties.levelName, dedicatedserverproperties.gamemode, dedicatedserverproperties.hardcore, dedicatedserverproperties.difficulty, false, new GameRules(), datapackconfiguration);
					generatorsettings = options.has("bonusChest") ? dedicatedserverproperties.generatorSettings.j() : dedicatedserverproperties.generatorSettings;
				}

				worlddata = new WorldDataServer(worldsettings, generatorsettings, Lifecycle.stable());
			}
			worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
			// Paper - move down

			IWorldDataServer iworlddataserver = worlddata;
			var generatorsettings = worlddata.getGeneratorSettings();
			var flag = generatorsettings.isDebugWorld();
			var i = generatorsettings.getSeed();
			var j = BiomeManager.a(i);
			List<MobSpawner> list = ImmutableList.of(new MobSpawnerPhantom(), new MobSpawnerPatrol(), new MobSpawnerCat(), new VillageSiege(), new MobSpawnerTrader(iworlddataserver));
			// Paper start - Use correct WorldDimension registry
			var worlddimension = generatorsettings.dimensions().get(dimensionKey);
			if (worlddimension == null) {
				worlddimension = registrymaterials.get(dimensionKey);
			}
			// Paper end
			DimensionManager dimensionmanager;
			ChunkGenerator chunkgenerator;

			if (worlddimension == null) {
				dimensionmanager = this.customRegistry.a().d(DimensionManager.OVERWORLD);
				chunkgenerator = GeneratorSettings.a(customRegistry.b(IRegistry.ay), customRegistry.b(IRegistry.ar), (new Random()).nextLong());
			} else {
				dimensionmanager = worlddimension.b();
				chunkgenerator = worlddimension.c();
			}

			// Paper start - fix and optimise world upgrading
			if (options.has("forceUpgrade")) {
				net.minecraft.server.Main.convertWorldButItWorks(
					dimensionKey, World.getDimensionKey(dimensionmanager), worldSession.getLevelName(), DataConverterRegistry.getDataFixer(), options.has("eraseCache")
				);
			}
			// Paper end - fix and optimise world upgrading

			var worldKey = ResourceKey.a(IRegistry.L, dimensionKey.a());

			if (dimensionKey == WorldDimension.OVERWORLD) {
				this.saveData = worlddata;
				this.saveData.setGameType(((DedicatedServer) this).getDedicatedServerProperties().gamemode); // From DedicatedServer.init

				var worldloadlistener = this.worldLoadListenerFactory.create(11);

				world = new WorldServer(this, this.executorService, worldSession, iworlddataserver, worldKey, dimensionmanager, worldloadlistener, chunkgenerator, flag, j, list, true, org.bukkit.World.Environment.getEnvironment(dimension), gen);
				var worldpersistentdata = world.getWorldPersistentData();
				this.initializeScoreboards(worldpersistentdata);
				this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this, world.getScoreboard());
				this.persistentCommandStorage = new PersistentCommandStorage(worldpersistentdata);
			} else {
				var worldloadlistener = this.worldLoadListenerFactory.create(11);
				world = new WorldServer(this, this.executorService, worldSession, iworlddataserver, worldKey, dimensionmanager, worldloadlistener, chunkgenerator, flag, j, ImmutableList.of(), true, org.bukkit.World.Environment.getEnvironment(dimension), gen);
			}

			worlddata.a(this.getServerModName(), this.getModded().isPresent());
			this.initWorld(world, worlddata, saveData, worlddata.getGeneratorSettings());

			this.worldServer.put(world.getDimensionKey(), world);
			this.getPlayerList().setPlayerFileData(world);

			if (worlddata.getCustomBossEvents() != null) {
				this.getBossBattleCustomData().load(worlddata.getCustomBossEvents());
			}
		}
		this.updateWorldSettings();
		for (var worldserver : this.getWorlds()) {
			this.loadSpawn(worldserver.getChunkProvider().playerChunkMap.worldLoadListener, worldserver);
			this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(worldserver.getWorld()));
		}

		// Paper start - Handle collideRule team for player collision toggle
		Scoreboard scoreboard = this.getScoreboard();
		java.util.Collection<String> toRemove = scoreboard.getTeams().stream().filter(team -> team.getName().startsWith("collideRule_")).map(ScoreboardTeam::getName).collect(java.util.stream.Collectors.toList());
		for (var teamName : toRemove) {
			scoreboard.removeTeam(scoreboard.getTeam(teamName)); // Clean up after ourselves
		}

		if (!com.destroystokyo.paper.PaperConfig.enablePlayerCollisions) {
			this.getPlayerList().collideRuleTeamName = org.apache.commons.lang3.StringUtils.left("collideRule_" + java.util.concurrent.ThreadLocalRandom.current().nextInt(), 16);
			var collideTeam = scoreboard.createTeam(this.getPlayerList().collideRuleTeamName);
			collideTeam.setCanSeeFriendlyInvisibles(false); // Because we want to mimic them not being on a team at all
		}
		// Paper end

		this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
		this.server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		this.serverConnection.acceptConnections();
		// CraftBukkit end
	}

	protected void updateWorldSettings() {
	}

	// CraftBukkit start
	public void initWorld(WorldServer worldserver, IWorldDataServer iworlddataserver, SaveData saveData, GeneratorSettings generatorsettings) {
		var flag = generatorsettings.isDebugWorld();
		// CraftBukkit start
		if (worldserver.generator != null) {
			worldserver.getWorld().getPopulators().addAll(worldserver.generator.getDefaultPopulators(worldserver.getWorld()));
		}
		var worldborder = worldserver.getWorldBorder();

		worldborder.a(iworlddataserver.r());
		this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(worldserver.getWorld())); // CraftBukkit - SPIGOT-5569
		if (!iworlddataserver.p()) {
			try {
				a(worldserver, iworlddataserver, generatorsettings.c(), flag, true);
				iworlddataserver.c(true);
				if (flag) {
					this.a(this.saveData);
				}
			} catch (Throwable throwable) {
				var crashreport = CrashReport.a(throwable, "Exception initializing level");

				try {
					worldserver.a(crashreport);
				} catch (Throwable throwable1) {
        }

				throw new ReportedException(crashreport);
			}

			iworlddataserver.c(true);
		}
	}
	// CraftBukkit end

	private static void a(WorldServer worldserver, IWorldDataServer iworlddataserver, boolean flag, boolean flag1, boolean flag2) {
		var chunkgenerator = worldserver.getChunkProvider().getChunkGenerator();

		if (!flag2) {
			iworlddataserver.setSpawn(BlockPosition.ZERO.up(chunkgenerator.getSpawnHeight()), 0.0F);
		} else if (flag1) {
			iworlddataserver.setSpawn(BlockPosition.ZERO.up(), 0.0F);
		} else {
			// Paper start - moved down
			// CraftBukkit start
			if (worldserver.generator != null) {
				Globals.setupGlobals(worldserver);
				var rand = new SeededRandom(0);
				rand.secureseed_setSeed(Globals.worldSeed, 0, 0, Globals.dimension.get(), Globals.SPAWN_POINT_SALT, 0);
				var spawn = worldserver.generator.getFixedSpawnLocation(worldserver.getWorld(), rand);

				if (spawn != null) {
					if (spawn.getWorld() != worldserver.getWorld()) {
						throw new IllegalStateException("Cannot set spawn point for " + iworlddataserver.getName() + " to be in another world (" + spawn.getWorld().getName() + ")");
					} else {
						iworlddataserver.setSpawn(new BlockPosition(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()), spawn.getYaw());
						return;
					}
				}
			}
			// CraftBukkit end
			// Paper start - if the generator created a spawn for us, then there is no need for us to also create a spawn -
			// only do it if the generator did not
			var worldchunkmanager = chunkgenerator.getWorldChunkManager();
			Globals.setupGlobals(worldserver);
			var random = new SeededRandom(0);
			random.secureseed_setSeed(Globals.worldSeed, 0, 0, Globals.dimension.get(), Globals.SPAWN_POINT_SALT, 0);
			var blockposition = worldchunkmanager.a(0, worldserver.getSeaLevel(), 0, 256, (biomebase) -> {
				return biomebase.b().b();
			}, random);
			var chunkcoordintpair = blockposition == null ? new ChunkCoordIntPair(0, 0) : new ChunkCoordIntPair(blockposition);
			// Paper end

			if (blockposition == null) {
				MinecraftServer.LOGGER.warn("Unable to find spawn biome");
			}

			var flag3 = false;
			Iterator iterator = TagsBlock.VALID_SPAWN.getTagged().iterator();

			while (iterator.hasNext()) {
				var block = (Block) iterator.next();

				if (worldchunkmanager.c().contains(block.getBlockData())) {
					flag3 = true;
					break;
				}
			}

			iworlddataserver.setSpawn(chunkcoordintpair.l().b(8, chunkgenerator.getSpawnHeight(), 8), 0.0F);
			var i = 0;
			var j = 0;
			var k = 0;
			var l = -1;
			var flag4 = true;

			for (var i1 = 0; i1 < 1024; ++i1) {
				if (i > -16 && i <= 16 && j > -16 && j <= 16) {
					var blockposition1 = WorldProviderNormal.a(worldserver, new ChunkCoordIntPair(chunkcoordintpair.x + i, chunkcoordintpair.z + j), flag3);

					if (blockposition1 != null) {
						iworlddataserver.setSpawn(blockposition1, 0.0F);
						break;
					}
				}

				if (i == j || i < 0 && i == -j || i > 0 && i == 1 - j) {
					var j1 = k;

					k = -l;
					l = j1;
				}

				i += k;
				j += l;
			}

			if (flag) {
				var worldgenfeatureconfigured = BiomeDecoratorGroups.BONUS_CHEST;

				worldgenfeatureconfigured.a(worldserver, chunkgenerator, worldserver.random, new BlockPosition(iworlddataserver.a(), iworlddataserver.b(), iworlddataserver.c()));
			}

		}
	}

	private void a(SaveData savedata) {
		savedata.setDifficulty(EnumDifficulty.PEACEFUL);
		savedata.d(true);
		var iworlddataserver = savedata.H();

		iworlddataserver.setStorm(false);
		iworlddataserver.setThundering(false);
		iworlddataserver.setClearWeatherTime(1000000000);
		iworlddataserver.setDayTime(6000L);
		iworlddataserver.setGameType(EnumGamemode.SPECTATOR);
	}

	// CraftBukkit start
	public void loadSpawn(WorldLoadListener worldloadlistener, WorldServer worldserver) {
		var chunkproviderserver = worldserver.getChunkProvider(); // Paper

		// WorldServer worldserver = this.E();
		this.forceTicks = true;
		// CraftBukkit end
		if (worldserver.getWorld().getKeepSpawnInMemory()) { // Paper

			MinecraftServer.LOGGER.info("Preparing start region for dimension {}", worldserver.getDimensionKey().a());
			var blockposition = worldserver.getSpawn();

			worldloadlistener.a(new ChunkCoordIntPair(blockposition));
			//ChunkProviderServer chunkproviderserver = worldserver.getChunkProvider(); // Paper - move up

			chunkproviderserver.getLightEngine().a(500);
			this.nextTick = SystemUtils.getMonotonicMillis();
			// Paper start - configurable spawn reason
			int radiusBlocks = worldserver.paperConfig.keepLoadedRange;
			var radiusChunks = radiusBlocks / 16 + ((radiusBlocks & 15) != 0 ? 1 : 0);
			var totalChunks = ((radiusChunks) * 2 + 1);
			totalChunks *= totalChunks;
			worldloadlistener.setChunkRadius(radiusBlocks / 16);

			worldserver.addTicketsForSpawn(radiusBlocks, blockposition);
			//LOGGER.info("Loaded " + chunkproviderserver.b() + " spawn chunks for world " + worldserver.getWorld().getName()); // Paper
			// Paper end
			// CraftBukkit start
			// this.nextTick = SystemUtils.getMonotonicMillis() + 10L;
			this.executeModerately();
			// Iterator iterator = this.worldServer.values().iterator();
		}

		if (true) {
			var worldserver1 = worldserver;
			var forcedchunk = worldserver.getWorldPersistentData().b(ForcedChunk::new, "chunks");
			// CraftBukkit end

			if (forcedchunk != null) {
				var longiterator = forcedchunk.a().iterator();

				while (longiterator.hasNext()) {
					var i = longiterator.nextLong();
					var chunkcoordintpair = new ChunkCoordIntPair(i);

					worldserver1.getChunkProvider().a(chunkcoordintpair, true);
				}
			}
		}

		// CraftBukkit start
		// this.nextTick = SystemUtils.getMonotonicMillis() + 10L;
		this.executeModerately();
		// CraftBukkit end
		if (worldserver.getWorld().getKeepSpawnInMemory()) worldloadlistener.b(); // Paper
		chunkproviderserver.getLightEngine().a(worldserver.paperConfig.lightQueueSize); // Paper - increase light queue size
		// CraftBukkit start
		// this.updateSpawnFlags();
		worldserver.setSpawnFlags(this.getSpawnMonsters(), this.getSpawnAnimals());

		this.forceTicks = false;
		// CraftBukkit end
	}

	protected void loadResourcesZip() {
		var file = this.convertable.getWorldFolder(SavedFile.RESOURCES_ZIP).toFile();

		if (file.isFile()) {
			var s = this.convertable.getLevelName();

        this.setResourcePack("level://" + URLEncoder.encode(s, StandardCharsets.UTF_8) + "/" + "resources.zip", "");
    }

	}

	public EnumGamemode getGamemode() {
		return this.saveData.getGameType();
	}

	public boolean isHardcore() {
		return this.saveData.isHardcore();
	}

	public abstract int g();

	public abstract int h();

	public boolean saveChunks(boolean flag, boolean flag1, boolean flag2) {
		var flag3 = false;

		for (Iterator iterator = this.getWorlds().iterator(); iterator.hasNext(); flag3 = true) {
			var worldserver = (WorldServer) iterator.next();

			if (!flag) {
				MinecraftServer.LOGGER.info("Saving chunks for level '{}'/{}", worldserver, worldserver.getDimensionKey().a());
			}

			worldserver.save(null, flag1, worldserver.savingDisabled && !flag2);
		}

		// CraftBukkit start - moved to WorldServer.save
        /*
        WorldServer worldserver1 = this.E();
        IWorldDataServer iworlddataserver = this.saveData.H();

        iworlddataserver.a(worldserver1.getWorldBorder().t());
        this.saveData.setCustomBossEvents(this.getBossBattleCustomData().save());
        this.convertable.a(this.customRegistry, this.saveData, this.getPlayerList().save());
        */
		// CraftBukkit end
		return flag3;
	}

	@Override
	public void close() {
		this.stop();
	}

	// CraftBukkit start
	private boolean hasStopped = false;
	public volatile boolean hasFullyShutdown = false; // Paper
	private boolean hasLoggedStop = false; // Paper
	private final Object stopLock = new Object();

	public final boolean hasStopped() {
		synchronized (stopLock) {
			return hasStopped;
		}
	}
	// CraftBukkit end

	protected void stop() {
		// CraftBukkit start - prevent double stopping on multiple threads
		synchronized (stopLock) {
			if (hasStopped) return;
			hasStopped = true;
		}
		// Paper start - kill main thread, and kill it hard
		shutdownThread = Thread.currentThread();
		org.spigotmc.WatchdogThread.doStop(); // Paper
		if (!isMainThread()) {
			MinecraftServer.LOGGER.info("Stopping main thread (Ignore any thread death message you see! - DO NOT REPORT THREAD DEATH TO PAPER)");
			var start = System.nanoTime(); // Tuinity - really try hard to kill the main thread
			while (this.getThread().isAlive()) {
				this.getThread().stop();
				try {
					if ((System.nanoTime() - start) <= (15L * 1000L * 1000L * 1000L))
						Thread.sleep(1); // Tuinity - really try hard to kill the main thread - if we're past 15s we're probably in a terrible loop, spam it to really kill it
				} catch (InterruptedException e) {
				}
			}
			// We've just obliterated the main thread, this will prevent stop from dying when removing players
			MinecraftServer.getServer().getWorlds().forEach(world -> {
				world.tickingEntities = false;
			});
		}
		// Paper end
		// CraftBukkit end
		MinecraftServer.LOGGER.info("Stopping server");
		// CraftBukkit start
		if (this.server != null) {
			this.server.disablePlugins();
			this.server.waitForAsyncTasksShutdown(); // Paper
		}
		// CraftBukkit end
		if (this.serverConnection != null) {
			this.serverConnection.close();
		}

		if (this.playerList != null) {
			MinecraftServer.LOGGER.info("Saving players");
			this.playerList.savePlayers();
			this.playerList.shutdown(this.isRestarting);
		}

		MinecraftServer.LOGGER.info("Saving worlds");
		Iterator iterator = this.getWorlds().iterator();

		WorldServer worldserver;
		while (iterator.hasNext()) {
			worldserver = (WorldServer) iterator.next();
			if (worldserver != null) {
				worldserver.savingDisabled = false;
			}
		}

		this.saveChunks(false, true, false);
		iterator = this.getWorlds().iterator();

		while (iterator.hasNext()) {
			worldserver = (WorldServer) iterator.next();
			if (worldserver != null) {
				try {
					worldserver.close();
				} catch (IOException ioexception) {
					MinecraftServer.LOGGER.error("Exception closing the level", ioexception);
				}
			}
		}

		this.dataPackResources.close();

		try {
			this.convertable.close();
		} catch (IOException ioexception1) {
			MinecraftServer.LOGGER.error("Failed to unlock level {}", this.convertable.getLevelName(), ioexception1);
		}
		// Spigot start
		MCUtil.asyncExecutor.shutdown(); // Paper
		try {
			MCUtil.asyncExecutor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS); // Paper
		} catch (java.lang.InterruptedException ignored) {
		} // Paper
		// Spigot end
		// Paper start - move final shutdown items here
		LOGGER.info("Flushing Chunk IO");
		com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.close(true, true); // Paper
		LOGGER.info("Closing Thread Pool");
		SystemUtils.shutdownServerThreadPool(); // Paper
		LOGGER.info("Closing Server");
		try {
			net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Use TerminalConsoleAppender
		} catch (Exception e) {
		}
		this.exit();
		// Paper end
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void a_(String s) {
		this.serverIp = s;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	// Paper start - allow passing of the intent to restart
	public void safeShutdown(boolean flag) {
		this.safeShutdown(flag, false);
	}

	public void safeShutdown(boolean flag, boolean isRestarting) {
		net.pl3x.purpur.task.TPSBarTask.stop(); // Purpur
		this.isRunning = false;
		this.isRestarting = isRestarting;
		this.hasLoggedStop = true; // Paper
		if (flag) {
			try {
				this.serverThread.join();
			} catch (InterruptedException interruptedexception) {
				MinecraftServer.LOGGER.error("Error while shutting down", interruptedexception);
			}
		}

	}
	// Paper end

	// Spigot Start
	private static double calcTps(double avg, double exp, double tps) {
		return (avg * exp) + (tps * (1 - exp));
	}

	// Paper start - Further improve server tick loop
	private static final long SEC_IN_NANO = 1000000000;
	private static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L;
	private long lastTick = 0;
	private final long catchupTime = 0;
	public final RollingAverage tps5s = new RollingAverage(5); // Purpur
	public final RollingAverage tps1 = new RollingAverage(60);
	public final RollingAverage tps5 = new RollingAverage(60 * 5);
	public final RollingAverage tps15 = new RollingAverage(60 * 15);

	public static class RollingAverage {
		private final int size;
		private long time;
		private java.math.BigDecimal total;
		private int index = 0;
		private final java.math.BigDecimal[] samples;
		private final long[] times;

		RollingAverage(int size) {
			this.size = size;
			this.time = size * SEC_IN_NANO;
			this.total = dec(TPS).multiply(dec(SEC_IN_NANO)).multiply(dec(size));
			this.samples = new java.math.BigDecimal[size];
			this.times = new long[size];
			for (var i = 0; i < size; i++) {
				this.samples[i] = dec(TPS);
				this.times[i] = SEC_IN_NANO;
			}
		}

		private static java.math.BigDecimal dec(long t) {
			return new java.math.BigDecimal(t);
		}

		public void add(java.math.BigDecimal x, long t) {
			time -= times[index];
			total = total.subtract(samples[index].multiply(dec(times[index])));
			samples[index] = x;
			times[index] = t;
			time += t;
			total = total.add(x.multiply(dec(t)));
			if (++index == size) {
				index = 0;
			}
		}

		public double getAverage() {
			return total.divide(dec(time), 30, java.math.RoundingMode.HALF_UP).doubleValue();
		}
	}

	private static final java.math.BigDecimal TPS_BASE = new java.math.BigDecimal("1E9").multiply(new java.math.BigDecimal(SAMPLE_INTERVAL));
	// Paper End
	// Spigot End

	protected void w() {
		try {
			var serverStartTime = SystemUtils.getMonotonicNanos(); // Paper
			if (this.init()) {
				this.nextTick = SystemUtils.getMonotonicMillis();
				this.serverPing.setMOTD(new ChatComponentText(this.motd));
				this.serverPing.setServerInfo(new ServerPing.ServerData(SharedConstants.getGameVersion().getName(), SharedConstants.getGameVersion().getProtocolVersion()));

				// Spigot start
				// Paper start - move done tracking
				LOGGER.info("Running delayed init tasks");
				this.server.getScheduler().mainThreadHeartbeat(this.ticks); // run all 1 tick delay tasks during init,
				// this is going to be the first thing the tick process does anyways, so move done and run it after
				// everything is init before watchdog tick.
				// anything at 3+ won't be caught here but also will trip watchdog....
				// tasks are default scheduled at -1 + delay, and first tick will tick at 1
				var doneTime = String.format(java.util.Locale.ROOT, "%.3fs", (double) (SystemUtils.getMonotonicNanos() - serverStartTime) / 1.0E9D);
				LOGGER.info("Done ({})! For help, type \"help\"", doneTime);
				// Paper end

				com.tuinity.tuinity.config.TuinityConfig.createWorldSections = false; // Tuinity - don't let plugin created worlds fill our config
				org.spigotmc.WatchdogThread.tick(); // Paper
				org.spigotmc.WatchdogThread.hasStarted = true; // Paper
				Arrays.fill(recentTps, 20);
				long start = System.nanoTime(), curTime, tickSection = start; // Paper - Further improve server tick loop
				lastTick = start - TICK_TIME; // Paper
				while (this.isRunning) {
					var i = ((curTime = System.nanoTime()) / (1000L * 1000L)) - this.nextTick; // Paper

					if (i > 5000L && this.nextTick - this.lastOverloadTime >= 30000L) { // CraftBukkit
						var j = i / 50L;

						if (server.getWarnOnOverload()) // CraftBukkit
							MinecraftServer.LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", i, j);
						this.nextTick += j * 50L;
						this.lastOverloadTime = this.nextTick;
					}

					++MinecraftServer.currentTickLong; // Tuinity
					if (++MinecraftServer.currentTick % SAMPLE_INTERVAL == 0) {
						var diff = curTime - tickSection;
						var currentTps = TPS_BASE.divide(new java.math.BigDecimal(diff), 30, java.math.RoundingMode.HALF_UP);
						tps5s.add(currentTps, diff); // Purpur
						tps1.add(currentTps, diff);
						tps5.add(currentTps, diff);
						tps15.add(currentTps, diff);
						// Backwards compat with bad plugins
						// Purpur start
						recentTps[0] = tps5s.getAverage();
						recentTps[1] = tps1.getAverage();
						recentTps[2] = tps5.getAverage();
						recentTps[3] = tps15.getAverage();
						// Purpur end
						// Paper end
						lagging = recentTps[0] < net.pl3x.purpur.PurpurConfig.laggingThreshold; // Purpur
						tickSection = curTime;
					}
					// Tuinity - replace logic
					// Spigot end

					//MinecraftServer.currentTick = (int) (SystemUtils.getMonotonicMillis() / 50); // CraftBukkit // Paper - don't overwrite current tick time
					lastTick = curTime;
					this.nextTick += 50L;
					this.a(this::canSleepForTick);
					this.X = true;
					// Purpur start - tps catchup
					if (net.pl3x.purpur.PurpurConfig.tpsCatchup) {
						this.W = Math.max(SystemUtils.getMonotonicMillis() + 50L, this.nextTick);
					} else {
						this.W = this.nextTick = curTime / 1000000L + 50L;
					}
					// Purpur end - tps catchup
					this.sleepForTick();
					this.hasTicked = true;
				}
			} else {
				this.a((CrashReport) null);
			}
		} catch (Throwable throwable) {
			// Paper start
			if (throwable instanceof ThreadDeath) {
				MinecraftServer.LOGGER.error("Main thread terminated by WatchDog due to hard crash", throwable);
				return;
			}
			// Paper end
			MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
			// Spigot Start
			if (throwable.getCause() != null) {
				MinecraftServer.LOGGER.error("\tCause of unexpected exception was", throwable.getCause());
			}
			// Spigot End
			CrashReport crashreport;

			if (throwable instanceof ReportedException) {
				crashreport = this.b(((ReportedException) throwable).a());
			} else {
				crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
			}

			var file = new File(new File(this.B(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

			if (crashreport.a(file)) {
				MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
			} else {
				MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
			}

			this.a(crashreport);
		} finally {
			try {
				this.isStopped = true;
				this.stop();
			} catch (Throwable throwable1) {
				MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
			} finally {
				//org.spigotmc.WatchdogThread.doStop(); // Spigot // Paper - move into stop
				// CraftBukkit start - Restore terminal to original settings
				try {
					//net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Move into stop
				} catch (Exception ignored) {
				}
				// CraftBukkit end
				//this.exit(); // Paper - moved into stop
			}

		}

	}

	public boolean canSleepForTick() { // Paper
		// CraftBukkit start
		if (isOversleep) return canOversleep();// Paper - because of our changes, this logic is broken
		return this.forceTicks || this.isEntered() || SystemUtils.getMonotonicMillis() < (this.X ? this.W : this.nextTick);
	}

	// Paper start
	boolean isOversleep = false;

	private boolean canOversleep() {
		return this.hasExecutedTask() && SystemUtils.getMonotonicMillis() < this.getTickOversleepMaxTime();
	}

	private boolean canSleepForTickNoOversleep() {
		return this.forceTicks || this.isEntered() || SystemUtils.getMonotonicMillis() < this.nextTick;
	}
	// Paper end

	// Tuinity start - execute chunk tasks mid tick
	static final long CHUNK_TASK_QUEUE_BACKOFF_MIN_TIME = 25L * 1000L; // 25us
	static final long MAX_CHUNK_EXEC_TIME = 1000L; // 1us

	static final long TASK_EXECUTION_FAILURE_BACKOFF = 5L * 1000L; // 5us

	private static long lastMidTickExecute;
	private static long lastMidTickExecuteFailure;

	private boolean tickMidTickTasks() {
		// give all worlds a fair chance at by targetting them all.
		// if we execute too many tasks, that's fine - we have logic to correctly handle overuse of allocated time.
		var executed = false;
		for (var world : this.getWorlds()) {
			var currTime = System.nanoTime();
			if (currTime - world.lastMidTickExecuteFailure <= TASK_EXECUTION_FAILURE_BACKOFF) {
				continue;
			}
			if (!world.getChunkProvider().runTasks()) {
				// we need to back off if this fails
				world.lastMidTickExecuteFailure = currTime;
			} else {
				executed = true;
			}
		}

		return executed;
	}

	public final void executeMidTickTasks() {
		var startTime = System.nanoTime();
		if ((startTime - lastMidTickExecute) <= CHUNK_TASK_QUEUE_BACKOFF_MIN_TIME || (startTime - lastMidTickExecuteFailure) <= TASK_EXECUTION_FAILURE_BACKOFF) {
			// it's shown to be bad to constantly hit the queue (chunk loads slow to a crawl), even if no tasks are executed.
			// so, backoff to prevent this
			return;
		}

		for (; ; ) {
			var moreTasks = this.tickMidTickTasks();
			var currTime = System.nanoTime();
			var diff = currTime - startTime;

			if (!moreTasks || diff >= MAX_CHUNK_EXEC_TIME) {
				if (!moreTasks) {
					lastMidTickExecuteFailure = currTime;
				}

				// note: negative values reduce the time
				var overuse = diff - MAX_CHUNK_EXEC_TIME;
				if (overuse >= (10L * 1000L * 1000L)) { // 10ms
					// make sure something like a GC or dumb plugin doesn't screw us over...
					overuse = 10L * 1000L * 1000L; // 10ms
				}

				var overuseCount = (double) overuse / (double) MAX_CHUNK_EXEC_TIME;
				var extraSleep = Math.round(overuseCount * CHUNK_TASK_QUEUE_BACKOFF_MIN_TIME);

				lastMidTickExecute = currTime + extraSleep;
				return;
			}
		}
	}
	// Tuinity end - execute chunk tasks mid tick

	private void executeModerately() {
		this.executeAll();
		java.util.concurrent.locks.LockSupport.parkNanos("executing tasks", 1000L);
	}
	// CraftBukkit end

	protected void sleepForTick() {
		//this.executeAll(); // Paper - move this into the tick method for timings
		this.awaitTasks(() -> {
			return !this.canSleepForTickNoOversleep(); // Paper - move oversleep into full server tick
		});
	}

	// Tuinity - replace logic

	@Override
	public TickTask postToMainThread(Runnable runnable) {
		// Paper start - anything that does try to post to main during watchdog crash, run on watchdog
		if (this.hasStopped && Thread.currentThread().equals(shutdownThread)) {
			runnable.run();
			runnable = () -> {
			};
		}
		// Paper end
		return new TickTask(this.ticks, runnable);
	}

	protected boolean canExecute(TickTask ticktask) {
		return ticktask.a() + 3 < this.ticks || this.canSleepForTick();
	}

	@Override
	public boolean executeNext() {
		var flag = this.bb();

		this.X = flag;
		return flag;
	}

	private boolean bb() {
		if (super.executeNext()) {
			this.executeMidTickTasks(); // Tuinity - execute chunk tasks mid tick
			return true;
		} else {
			if (this.canSleepForTick()) {
				Iterator iterator = this.getWorlds().iterator();

				while (iterator.hasNext()) {
					var worldserver = (WorldServer) iterator.next();

					if (worldserver.getChunkProvider().runTasks()) {
						return true;
					}
				}
			}

			return false;
		}
	}

	protected void c(TickTask ticktask) {
		super.executeTask(ticktask);
	}

	public File B() {
		return new File(".");
	}

	protected void a(CrashReport crashreport) {
	}

	protected void exit() {
	}

	protected void a(BooleanSupplier booleansupplier) {
		this.slackActivityAccountant.tickStarted(); // Spigot
		var i = SystemUtils.getMonotonicNanos();

		// Paper start - move oversleep into full server tick
		isOversleep = true;
		this.awaitTasks(() -> {
			// Tuinity - replace logic
			return !this.canOversleep();
		});
		isOversleep = false;
		// Paper end
		new com.destroystokyo.paper.event.server.ServerTickStartEvent(this.ticks + 1).callEvent(); // Paper

		++this.ticks;
		this.b(booleansupplier);
		if (i - this.T >= 5000000000L) {
			this.T = i;
			this.serverPing.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.getMaxPlayers(), this.getPlayerCount()));
			var agameprofile = new GameProfile[Math.min(this.getPlayerCount(), org.spigotmc.SpigotConfig.playerSample)]; // Paper
			var j = MathHelper.nextInt(this.r, 0, this.getPlayerCount() - agameprofile.length);

			for (var k = 0; k < agameprofile.length; ++k) {
				agameprofile[k] = this.playerList.getPlayers().get(j + k).getProfile();
			}

			Collections.shuffle(Arrays.asList(agameprofile));
			this.serverPing.b().a(agameprofile);
		}

		serverAutoSave = (autosavePeriod > 0 && this.ticks % autosavePeriod == 0); // Paper
		var playerSaveInterval = com.destroystokyo.paper.PaperConfig.playerAutoSaveRate;
		if (playerSaveInterval < 0) {
			playerSaveInterval = autosavePeriod;
		}
		if (playerSaveInterval > 0) { // Paper
			this.playerList.savePlayers(playerSaveInterval); // Paper
		}
		for (var world : getWorlds()) {
			if (world.paperConfig.autoSavePeriod > 0) {
				world.saveIncrementally(serverAutoSave);
			}
		}

		this.executeAll();

		com.tuinity.tuinity.util.CachedLists.reset(); // Tuinity

		// Paper start
		var endTime = System.nanoTime();
		var remaining = (TICK_TIME - (endTime - lastTick)) - catchupTime;
		new com.destroystokyo.paper.event.server.ServerTickEndEvent(this.ticks, ((double) (endTime - lastTick) / 1000000D), remaining).callEvent();
		// Paper end

		var l = this.h[this.ticks % 100] = SystemUtils.getMonotonicNanos() - i;

		this.ag = this.ag * 0.8F + (float) l / 1000000.0F * 0.19999999F;
		var i1 = SystemUtils.getMonotonicNanos();

		// Paper start
		tickTimes5s.add(this.ticks, l);
		tickTimes10s.add(this.ticks, l);
		tickTimes60s.add(this.ticks, l);
		// Paper end

		this.circularTimer.a(i1 - i);
		org.spigotmc.WatchdogThread.tick(); // Spigot
		this.slackActivityAccountant.tickEnded(l); // Spigot
	}

	protected void b(BooleanSupplier booleansupplier) {
		// Tuinity - replace logic
		this.server.getScheduler().mainThreadHeartbeat(this.ticks); // CraftBukkit
		// Tuinity - replace logic
		this.getFunctionData().tick();
		// Tuinity - replace logic
		Iterator iterator = this.getWorlds().iterator();

		// CraftBukkit start
		// Run tasks that are waiting on processing
		while (!processQueue.isEmpty()) {
			processQueue.remove().run();
		}
		// Tuinity - replace logic
		// Send time updates to everyone, it will get the right time from the world the player is in.
		// Paper start - optimize time updates
		for (var world : this.getWorlds()) {
			var doDaylight = world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
			var dayTime = world.getDayTime();
			var worldTime = world.getTime();
			var worldPacket = new PacketPlayOutUpdateTime(worldTime, dayTime, doDaylight);
			for (EntityHuman entityhuman : world.getPlayers()) {
				if (!(entityhuman instanceof EntityPlayer entityplayer) || (ticks + entityhuman.getId()) % 20 != 0) {
					continue;
				}
				var playerTime = entityplayer.getPlayerTime();
				var packet = (playerTime == dayTime) ? worldPacket :
					new PacketPlayOutUpdateTime(worldTime, playerTime, doDaylight);
				entityplayer.playerConnection.sendPacket(packet); // Add support for per player time
			}
		}
		// Paper end

		while (iterator.hasNext()) {
			var worldserver = (WorldServer) iterator.next();
			worldserver.hasPhysicsEvent = org.bukkit.event.block.BlockPhysicsEvent.getHandlerList().getRegisteredListeners().length > 0; // Paper
			worldserver.hasEntityMoveEvent = EntityMoveEvent.getHandlerList().getRegisteredListeners().length > 0; // Paper
			worldserver.hasRidableMoveEvent = net.pl3x.purpur.event.entity.RidableMoveEvent.getHandlerList().getRegisteredListeners().length > 0; // Purpur
			TileEntityHopper.skipHopperEvents = worldserver.paperConfig.disableHopperMoveEvents || org.bukkit.event.inventory.InventoryMoveItemEvent.getHandlerList().getRegisteredListeners().length == 0; // Paper

            /* Drop global time updates
            if (this.ticks % 20 == 0) {
                this.methodProfiler.enter("timeSync");
                this.playerList.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))), worldserver.getDimensionKey());
                this.methodProfiler.exit();
            }
            // CraftBukkit end */

			try {
				// Tuinity - replace logic
				worldserver.doTick(booleansupplier);
				// Tuinity start
				for (var regionManager : worldserver.getChunkProvider().playerChunkMap.regionManagers) {
					regionManager.recalculateRegions();
				}
				// Tuinity end
				// Tuinity - replace logic
			} catch (Throwable throwable) {
				// Spigot Start
				CrashReport crashreport;
				try {
					crashreport = CrashReport.a(throwable, "Exception ticking world");
				} catch (Throwable t) {
					if (throwable instanceof ThreadDeath) {
						throw (ThreadDeath) throwable;
					} // Paper
					throw new RuntimeException("Error generating crash report", t);
				}
				// Spigot End

				worldserver.a(crashreport);
				throw new ReportedException(crashreport);
			}

			worldserver.explosionDensityCache.clear(); // Paper - Optimize explosions
		}

		this.getServerConnection().c();
		this.playerList.tick();

		var tickables = this.tickables;
		for (var i = 0; i < tickables.size(); ++i) {
			tickables.get(i).run();
		}
	}

	public boolean getAllowNether() {
		return true;
	}

	public void b(Runnable runnable) {
		this.tickables.add(runnable);
	}

	protected void b(String s) {
		this.ai = s;
	}

	public File c(String s) {
		return new File(this.B(), s);
	}

	public final WorldServer E() {
		return this.worldServer.get(World.OVERWORLD);
	}

	@Nullable
	public WorldServer getWorldServer(ResourceKey<World> resourcekey) {
		return this.worldServer.get(resourcekey);
	}

	public Set<ResourceKey<World>> F() {
		return this.worldServer.keySet();
	}

	public Iterable<WorldServer> getWorlds() {
		return this.worldServer.values();
	}

	public String getVersion() {
		return SharedConstants.getGameVersion().getName();
	}

	public int getPlayerCount() {
		return this.playerList.getPlayerCount();
	}

	public int getMaxPlayers() {
		return this.playerList.getMaxPlayers();
	}

	public String[] getPlayers() {
		return this.playerList.e();
	}

	public String getServerModName() {
		return net.pl3x.purpur.PurpurConfig.serverModName; // Purpur // Tuinity // Paper // Spigot // CraftBukkit
	}

	public CrashReport b(CrashReport crashreport) {
		if (this.playerList != null) {
			crashreport.g().a("Player Count", () -> {
				return this.playerList.getPlayerCount() + " / " + this.playerList.getMaxPlayers() + "; " + this.playerList.getPlayers();
			});
		}

		crashreport.g().a("Data Packs", () -> {
			var stringbuilder = dev.kamillaova.paper.pool.StringBuilderPool.take();
			Iterator iterator = this.resourcePackRepository.e().iterator();

			while (iterator.hasNext()) {
				var resourcepackloader = (ResourcePackLoader) iterator.next();

				if (stringbuilder.length() > 0) {
					stringbuilder.append(", ");
				}

				stringbuilder.append(resourcepackloader.e());
				if (!resourcepackloader.c().a()) {
					stringbuilder.append(" (incompatible)");
				}
			}

			return stringbuilder.toString();
		});
		if (this.ai != null) {
			crashreport.g().a("Server Id", () -> {
				return this.ai;
			});
		}

		return crashreport;
	}

	public abstract Optional<String> getModded();

	@Override
	public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {
		MinecraftServer.LOGGER.info(PaperAdventure.LEGACY_SECTION_UXRC.serialize(PaperAdventure.asAdventure(ichatbasecomponent))); // Paper - Log message with colors
	}

	public KeyPair getKeyPair() {
		return this.H;
	}

	public int getPort() {
		return this.serverPort;
	}

	public void setPort(int i) {
		this.serverPort = i;
	}

	public String getSinglePlayerName() {
		return this.I;
	}

	public void d(String s) {
		this.I = s;
	}

	public boolean isEmbeddedServer() {
		return this.I != null;
	}

	protected void P() {
		MinecraftServer.LOGGER.info("Generating keypair");

		try {
			this.H = MinecraftEncryption.b();
		} catch (CryptographyException cryptographyexception) {
			throw new IllegalStateException("Failed to generate key pair", cryptographyexception);
		}
	}

	// Paper start - fix per world difficulty
	public void setWorldDifficulty(WorldServer world, EnumDifficulty enumdifficulty, boolean forcefullySet) {
		this.a(world, enumdifficulty, forcefullySet);
	}

	public void a(WorldServer world, EnumDifficulty enumdifficulty, boolean flag) {
		var worldData = world.worldDataServer;
		if (flag || !worldData.isDifficultyLocked()) {
			worldData.setDifficulty(worldData.isHardcore() ? EnumDifficulty.HARD : enumdifficulty);
			world.setSpawnFlags(worldData.getDifficulty() != EnumDifficulty.PEACEFUL && ((DedicatedServer) this).propertyManager.getProperties().spawnMonsters, this.getSpawnAnimals());
			//this.getPlayerList().getPlayers().forEach(this::b); // Commented: WorldDataServer#setDifficulty handles updating players' difficulties
			// Paper end
		}
	}

	public final int applyTrackingRangeScale(int value) {
		return this.b(value);
	} // Paper - OBFHELPER

	public int b(int i) {
		return i;
	}

	private void updateSpawnFlags() {
		Iterator iterator = this.getWorlds().iterator();

		while (iterator.hasNext()) {
			var worldserver = (WorldServer) iterator.next();

			worldserver.setSpawnFlags(this.getSpawnMonsters(), this.getSpawnAnimals());
		}

	}

	public void b(boolean flag) {
		this.saveData.d(flag);
		this.getPlayerList().getPlayers().forEach(this::b);
	}

	private void b(EntityPlayer entityplayer) {
		var worlddata = entityplayer.getWorldServer().getWorldData();

		entityplayer.playerConnection.sendPacket(new PacketPlayOutServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
	}

	protected boolean getSpawnMonsters() {
		return this.saveData.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	public boolean isDemoMode() {
		return this.demoMode;
	}

	public void c(boolean flag) {
		this.demoMode = flag;
	}

	public String getResourcePack() {
		return this.K;
	}

	public String getResourcePackHash() {
		return this.L;
	}

	public void setResourcePack(String s, String s1) {
		this.K = s;
		this.L = s1;
	}

	public abstract boolean j();

	public boolean getOnlineMode() {
		return this.onlineMode;
	}

	public void setOnlineMode(boolean flag) {
		this.onlineMode = flag;
	}

	public boolean W() {
		return this.B;
	}

	public void e(boolean flag) {
		this.B = flag;
	}

	public boolean getSpawnAnimals() {
		return true;
	}

	public boolean getSpawnNPCs() {
		return true;
	}

	public boolean getPVP() {
		return this.pvpMode;
	}

	public void setPVP(boolean flag) {
		this.pvpMode = flag;
	}

	public boolean getAllowFlight() {
		return this.allowFlight;
	}

	public void setAllowFlight(boolean flag) {
		this.allowFlight = flag;
	}

	public abstract boolean getEnableCommandBlock();

	public String getMotd() {
		return this.motd;
	}

	public void setMotd(String s) {
		this.motd = s;
	}

	public int getMaxBuildHeight() {
		return this.F;
	}

	public void c(int i) {
		this.F = i;
	}

	public boolean isStopped() {
		return this.isStopped;
	}

	public PlayerList getPlayerList() {
		return this.playerList;
	}

	public void a(PlayerList playerlist) {
		this.playerList = playerlist;
	}

	public abstract boolean n();

	public void a(EnumGamemode enumgamemode) {
		this.saveData.setGameType(enumgamemode);
	}

	@Nullable
	public ServerConnection getServerConnection() {
		return this.serverConnection == null
			? this.serverConnection = new ServerConnection(this)
			: this.serverConnection;
	}

	public boolean ah() {
		return false;
	}

	public abstract boolean a(EnumGamemode enumgamemode, boolean flag, int i);

	public int ai() {
		return this.ticks;
	}

	public int getSpawnProtection() {
		return 16;
	}

	public boolean a(WorldServer worldserver, BlockPosition blockposition, EntityHuman entityhuman) {
		return false;
	}

	public void setForceGamemode(boolean flag) {
		this.P = flag;
	}

	public boolean getForceGamemode() {
		return this.P;
	}

	public boolean am() {
		return true;
	}

	public MinecraftSessionService getMinecraftSessionService() {
		return this.minecraftSessionService;
	}

	public GameProfileRepository getGameProfileRepository() {
		return this.gameProfileRepository;
	}

	public UserCache getUserCache() {
		return this.userCache;
	}

	public ServerPing getServerPing() {
		return this.serverPing;
	}

	public void invalidatePingSample() {
		this.T = 0L;
	}

	public int au() {
		return 29999984;
	}

	@Override
	public boolean isNotMainThread() {
		return super.isNotMainThread() && !this.isStopped();
	}

	@Override
	public Thread getThread() {
		return this.serverThread;
	}

	public int ax() {
		return 256;
	}

	public long ay() {
		return this.nextTick;
	}

	public DataFixer getDataFixer() {
		return this.dataConverterManager;
	}

	public int a(@Nullable WorldServer worldserver) {
		return worldserver != null ? worldserver.getGameRules().getInt(GameRules.SPAWN_RADIUS) : 10;
	}

	public AdvancementDataWorld getAdvancementData() {
		return this.dataPackResources.g();
	}

	public CustomFunctionData getFunctionData() {
		return this.customFunctionData;
	}

	// Paper start - add cause
	@Deprecated
	public CompletableFuture<Void> a(Collection<String> collection) {
		return this.reloadServerResources(collection, ServerResourcesReloadedEvent.Cause.PLUGIN);
	}

	public CompletableFuture<Void> reloadServerResources(Collection<String> collection, ServerResourcesReloadedEvent.Cause cause) {
		// Paper end
		var completablefuture = CompletableFuture.supplyAsync(() -> {
			var stream = collection.stream(); // CraftBukkit - decompile error
			var resourcepackrepository = this.resourcePackRepository;

			this.resourcePackRepository.getClass();
			return stream.map(resourcepackrepository::a).filter(Objects::nonNull).map(ResourcePackLoader::d).collect(ImmutableList.toImmutableList()); // CraftBukkit - decompile error // Paper - decompile error
		}, this).thenCompose((immutablelist) -> {
			return DataPackResources.a(immutablelist, this.j() ? CommandDispatcher.ServerType.DEDICATED : CommandDispatcher.ServerType.INTEGRATED, this.h(), this.executorService, this); // Paper - decompile error
		}).thenAcceptAsync((datapackresources) -> {
			this.dataPackResources.close();
			this.dataPackResources = datapackresources;
			this.server.syncCommands(); // SPIGOT-5884: Lost on reload
			this.resourcePackRepository.a(collection);
			this.saveData.a(a(this.resourcePackRepository));
			datapackresources.i();
			new ServerResourcesReloadedEvent(cause).callEvent(); // Paper
			if (Thread.currentThread() != this.serverThread) return; // Paper
			//this.getPlayerList().savePlayers(); // Paper - we don't need to do this
			this.getPlayerList().reload();
			this.customFunctionData.a(this.dataPackResources.a());
			this.ak.a(this.dataPackResources.h());
			org.bukkit.craftbukkit.block.data.CraftBlockData.reloadCache(); // Paper - cache block data strings, they can be defined by datapacks so refresh it here
		}, this);

		if (this.isMainThread()) {
			this.awaitTasks(completablefuture::isDone);
		}

		return completablefuture;
	}

	public static DataPackConfiguration a(ResourcePackRepository resourcepackrepository, DataPackConfiguration datapackconfiguration, boolean flag) {
		resourcepackrepository.a();
		if (flag) {
			resourcepackrepository.a(Collections.singleton("vanilla"));
			return new DataPackConfiguration(ImmutableList.of("vanilla"), ImmutableList.of());
		} else {
			Set<String> set = Sets.newLinkedHashSet();
			Iterator iterator = datapackconfiguration.a().iterator();

			while (iterator.hasNext()) {
				var s = (String) iterator.next();

				if (resourcepackrepository.b(s)) {
					set.add(s);
				} else {
					MinecraftServer.LOGGER.warn("Missing data pack {}", s);
				}
			}

			iterator = resourcepackrepository.c().iterator();

			while (iterator.hasNext()) {
				var resourcepackloader = (ResourcePackLoader) iterator.next();
				var s1 = resourcepackloader.e();

				if (!datapackconfiguration.b().contains(s1) && !set.contains(s1)) {
					MinecraftServer.LOGGER.info("Found new data pack {}, loading it automatically", s1);
					set.add(s1);
				}
			}

			if (set.isEmpty()) {
				MinecraftServer.LOGGER.info("No datapacks selected, forcing vanilla");
				set.add("vanilla");
			}

			resourcepackrepository.a(set);
			return a(resourcepackrepository);
		}
	}

	private static DataPackConfiguration a(ResourcePackRepository resourcepackrepository) {
		var collection = resourcepackrepository.d();
		List<String> list = ImmutableList.copyOf(collection);
		List<String> list1 = resourcepackrepository.b().stream().filter((s) -> {
			return !collection.contains(s);
		}).collect(ImmutableList.toImmutableList());

		return new DataPackConfiguration(list, list1);
	}

	public ResourcePackRepository getResourcePackRepository() {
		return this.resourcePackRepository;
	}

	public CommandDispatcher getCommandDispatcher() {
		return this.dataPackResources.f();
	}

	public CommandListenerWrapper getServerCommandListener() {
		var worldserver = this.E();

		return new CommandListenerWrapper(this, worldserver == null ? Vec3D.ORIGIN : Vec3D.b(worldserver.getSpawn()), Vec2F.a, worldserver, 4, "Server", new ChatComponentText("Server"), this, null);
	}

	@Override
	public boolean shouldSendSuccess() {
		return true;
	}

	@Override
	public boolean shouldSendFailure() {
		return true;
	}

	public CraftingManager getCraftingManager() {
		return this.dataPackResources.e();
	}

	public ITagRegistry getTagRegistry() {
		return this.dataPackResources.d();
	}

	public ScoreboardServer getScoreboard() {
		return this.scoreboardServer;
	}

	public PersistentCommandStorage aI() {
		if (this.persistentCommandStorage == null) {
			throw new NullPointerException("Called before server init");
		} else {
			return this.persistentCommandStorage;
		}
	}

	public LootTableRegistry getLootTableRegistry() {
		return this.dataPackResources.c();
	}

	public LootPredicateManager getLootPredicateManager() {
		return this.dataPackResources.b();
	}

	public GameRules getGameRules() {
		return this.E().getGameRules();
	}

	public BossBattleCustomData getBossBattleCustomData() {
		return this.bossBattleCustomData;
	}

	public float aO() {
		return this.ag;
	}

	public int b(GameProfile gameprofile) {
		return 0;
	}

	public abstract boolean a(GameProfile gameprofile);

	public void a(Path path) throws IOException {
		var path1 = path.resolve("levels");
		Iterator iterator = this.worldServer.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<ResourceKey<World>, WorldServer> entry = (Entry) iterator.next();
			var minecraftkey = entry.getKey().a();
			var path2 = path1.resolve(minecraftkey.getNamespace()).resolve(minecraftkey.getKey());

			Files.createDirectories(path2);
			entry.getValue().a(path2);
		}

		this.d(path.resolve("gamerules.txt"));
		this.e(path.resolve("classpath.txt"));
		this.c(path.resolve("example_crash.txt"));
		this.b(path.resolve("stats.txt"));
		this.f(path.resolve("threads.txt"));
	}

	private void b(Path path) throws IOException {
		var bufferedwriter = Files.newBufferedWriter(path);
		Throwable throwable = null;

		try {
			bufferedwriter.write(String.format("pending_tasks: %d\n", this.bi()));
			bufferedwriter.write(String.format("average_tick_time: %f\n", this.aO()));
			bufferedwriter.write(String.format("tick_times: %s\n", Arrays.toString(this.h)));
			bufferedwriter.write(String.format("queue: %s\n", SystemUtils.f()));
		} catch (Throwable throwable1) {
			throwable = throwable1;
			throw throwable1;
		} finally {
			if (bufferedwriter != null) {
				if (throwable != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable2) {
						throwable.addSuppressed(throwable2);
					}
				} else {
					bufferedwriter.close();
				}
			}

		}

	}

	private void c(Path path) throws IOException {
		var crashreport = new CrashReport("Server dump", new Exception("dummy"));

		this.b(crashreport);
		var bufferedwriter = Files.newBufferedWriter(path);
		Throwable throwable = null;

		try {
			bufferedwriter.write(crashreport.e());
		} catch (Throwable throwable1) {
			throwable = throwable1;
			throw throwable1;
		} finally {
			if (bufferedwriter != null) {
				if (throwable != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable2) {
						throwable.addSuppressed(throwable2);
					}
				} else {
					bufferedwriter.close();
				}
			}

		}

	}

	private void d(Path path) throws IOException {
		var bufferedwriter = Files.newBufferedWriter(path);
		Throwable throwable = null;

		try {
			List<String> list = Lists.newArrayList();
			var gamerules = this.getGameRules();

			GameRules.a(new GameRules.GameRuleVisitor() {
				@Override
				public <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
					list.add(String.format("%s=%s\n", gamerules_gamerulekey.a(), gamerules.get(gamerules_gamerulekey).toString()));
				}
			});
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				var s = (String) iterator.next();

				bufferedwriter.write(s);
			}
		} catch (Throwable throwable1) {
			throwable = throwable1;
			throw throwable1;
		} finally {
			if (bufferedwriter != null) {
				if (throwable != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable2) {
						throwable.addSuppressed(throwable2);
					}
				} else {
					bufferedwriter.close();
				}
			}

		}

	}

	private void e(Path path) throws IOException {
		var bufferedwriter = Files.newBufferedWriter(path);
		Throwable throwable = null;

		try {
			var s = System.getProperty("java.class.path");
			var s1 = System.getProperty("path.separator");
			Iterator iterator = Splitter.on(s1).split(s).iterator();

			while (iterator.hasNext()) {
				var s2 = (String) iterator.next();

				bufferedwriter.write(s2);
				bufferedwriter.write("\n");
			}
		} catch (Throwable throwable1) {
			throwable = throwable1;
			throw throwable1;
		} finally {
			if (bufferedwriter != null) {
				if (throwable != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable2) {
						throwable.addSuppressed(throwable2);
					}
				} else {
					bufferedwriter.close();
				}
			}

		}

	}

	private void f(Path path) throws IOException {
		var threadmxbean = ManagementFactory.getThreadMXBean();
		var athreadinfo = threadmxbean.dumpAllThreads(true, true);

		Arrays.sort(athreadinfo, Comparator.comparing(ThreadInfo::getThreadName));
		var bufferedwriter = Files.newBufferedWriter(path);
		Throwable throwable = null;

		try {
			var athreadinfo1 = athreadinfo;
			var i = athreadinfo.length;

			for (var j = 0; j < i; ++j) {
				var threadinfo = athreadinfo1[j];

				bufferedwriter.write(threadinfo.toString());
				bufferedwriter.write(10);
			}
		} catch (Throwable throwable1) {
			throwable = throwable1;
			throw throwable1;
		} finally {
			if (bufferedwriter != null) {
				if (throwable != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable2) {
						throwable.addSuppressed(throwable2);
					}
				} else {
					bufferedwriter.close();
				}
			}

		}

	}

	// CraftBukkit start
	@Override
	public boolean isMainThread() {
		return super.isMainThread() /*|| this.isStopped()*/; // CraftBukkit - MC-142590 // Paper - causes issues elsewhere
	}

	public boolean isDebugging() {
		return false;
	}

	@Deprecated
	public static MinecraftServer getServer() {
		return SERVER; // Paper
	}
	// CraftBukkit end

	public Path a(SavedFile savedfile) {
		return this.convertable.getWorldFolder(savedfile);
	}

	public boolean isSyncChunkWrites() {
		return true;
	}

	public DefinedStructureManager getDefinedStructureManager() {
		return this.ak;
	}

	public SaveData getSaveData() {
		return this.saveData;
	}

	public IRegistryCustom getCustomRegistry() {
		return this.customRegistry;
	}

	@Nullable
	public ITextFilter a(EntityPlayer entityplayer) {
		return null;
	}

	// Paper start
	public static class TickTimes {
		private final long[] times;

		public TickTimes(int length) {
			times = new long[length];
		}

		void add(int index, long time) {
			times[index % times.length] = time;
		}

		public long[] getTimes() {
			return times.clone();
		}

		public double getAverage() {
			var total = 0L;
			for (var value : times) {
				total += value;
			}
			return ((double) total / (double) times.length) * 1.0E-6D;
		}
	}
	// Paper end
}
