package net.minecraft.server.level;

import com.destroystokyo.paper.PaperWorldConfig;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.*;
import dev.kamillaova.paper.server.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.*;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.CrashReport;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.SectionPosition;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.progress.WorldLoadListener;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.tags.ITagRegistry;
import net.minecraft.util.CSVWriter;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.ai.village.ReputationEvent;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.ai.village.poi.VillagePlace;
import net.minecraft.world.entity.ai.village.poi.VillagePlaceType;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.animal.horse.EntityHorseSkeleton;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntityDrowned;
import net.minecraft.world.entity.npc.MobSpawnerCat;
import net.minecraft.world.entity.npc.MobSpawnerTrader;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.raid.PersistentRaid;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ITileEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.dimension.end.EnderDragonBattle;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.MobSpawnerPatrol;
import net.minecraft.world.level.levelgen.MobSpawnerPhantom;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.material.FluidTypes;
import net.minecraft.world.level.portal.PortalTravelAgent;
import net.minecraft.world.level.saveddata.maps.PersistentIdCounts;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.IWorldDataServer;
import net.minecraft.world.level.storage.WorldDataServer;
import net.minecraft.world.level.storage.WorldPersistentData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.OperatorBoolean;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.WorldUUID;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.TimeSkipEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldServer extends World implements GeneratorAccessSeed {

	public static final BlockPosition a = new BlockPosition(100, 50, 0);
	private static final Logger LOGGER = LogManager.getLogger();
	public final Int2ObjectMap<Entity> entitiesById = new Int2ObjectLinkedOpenHashMap();
	final com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet<Entity> entitiesForIteration = new com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet<>(2048, 0.5f, 2048, 0.2, true); // Tuinity - make removing entities while ticking safe
	private final Map<UUID, Entity> entitiesByUUID = Maps.newHashMap();
	private final Queue<Entity> entitiesToAdd = Queues.newArrayDeque();
	public final List<EntityPlayer> players = Lists.newArrayList(); // Paper - private -> public
	public final ChunkProviderServer chunkProvider; // Paper - public
	public boolean tickingEntities; // Paper - expose for watchdog
	// Paper start
	List<java.lang.Runnable> afterEntityTickingTasks = Lists.newArrayList();

	public void doIfNotEntityTicking(java.lang.Runnable run) {
		if (tickingEntities) {
			afterEntityTickingTasks.add(run);
		} else {
			run.run();
		}
	}

	// Paper end
	private final MinecraftServer server;
	public final WorldDataServer worldDataServer; // CraftBukkit - type
	public boolean savingDisabled;
	private boolean everyoneSleeping;
	private int emptyTime;
	private final PortalTravelAgent portalTravelAgent;
	private final TickListServer<Block> nextTickListBlock;
	private final TickListServer<FluidType> nextTickListFluid;
	private final Set<NavigationAbstract> navigators;
	final com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet<NavigationAbstract> navigatorsForIteration = new com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet<>(2048, 0.5f, 2048, 0.2, true); // Tuinity - make removing entities while ticking safe
	protected final PersistentRaid persistentRaid;
	private final ObjectLinkedOpenHashSet<BlockActionData> L;
	private boolean ticking;
	private final List<MobSpawner> mobSpawners;
	@Nullable
	private final EnderDragonBattle dragonBattle;
	private final StructureManager structureManager;
	private final boolean Q;
	private double fakeTime; // Purpur


	// CraftBukkit start
	private int tickPosition;
	public final Convertable.ConversionSession convertable;
	public final UUID uuid;
	public boolean hasPhysicsEvent = true; // Paper
	public boolean hasEntityMoveEvent = false; // Paper
	public boolean hasRidableMoveEvent = false; // Purpur

	private static Throwable getAddToWorldStackTrace(Entity entity) {
		return new Throwable(entity + " Added to world at " + new java.util.Date());
	}

	@Override
	public Chunk getChunkIfLoaded(int x, int z) { // Paper - this was added in world too but keeping here for NMS ABI
		return this.chunkProvider.getChunkAtIfLoadedImmediately(x, z); // Paper
	}

	// Paper start - Asynchronous IO
	public final com.destroystokyo.paper.io.PaperFileIOThread.ChunkDataController poiDataController = new com.destroystokyo.paper.io.PaperFileIOThread.ChunkDataController() {
		@Override
		public void writeData(int x, int z, NBTTagCompound compound) throws java.io.IOException {
			WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace().write(new ChunkCoordIntPair(x, z), compound);
		}

		@Override
		public NBTTagCompound readData(int x, int z) throws java.io.IOException {
			return WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace().read(new ChunkCoordIntPair(x, z));
		}

		@Override
		public <T> T computeForRegionFile(int chunkX, int chunkZ, java.util.function.Function<RegionFile, T> function) {
			synchronized (WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace()) {
				RegionFile file;

				try {
					file = WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace().getFile(new ChunkCoordIntPair(chunkX, chunkZ), false);
				} catch (java.io.IOException ex) {
					throw new RuntimeException(ex);
				}

				return function.apply(file);
			}
		}

		@Override
		public <T> T computeForRegionFileIfLoaded(int chunkX, int chunkZ, java.util.function.Function<RegionFile, T> function) {
			synchronized (WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace()) {
				RegionFile file = WorldServer.this.getChunkProvider().playerChunkMap.getVillagePlace().getRegionFileIfLoaded(new ChunkCoordIntPair(chunkX, chunkZ));
				return function.apply(file);
			}
		}
	};

	public final com.destroystokyo.paper.io.PaperFileIOThread.ChunkDataController chunkDataController = new com.destroystokyo.paper.io.PaperFileIOThread.ChunkDataController() {
		@Override
		public void writeData(int x, int z, NBTTagCompound compound) throws java.io.IOException {
			WorldServer.this.getChunkProvider().playerChunkMap.write(new ChunkCoordIntPair(x, z), compound);
		}

		@Override
		public NBTTagCompound readData(int x, int z) throws java.io.IOException {
			return WorldServer.this.getChunkProvider().playerChunkMap.read(new ChunkCoordIntPair(x, z));
		}

		@Override
		public <T> T computeForRegionFile(int chunkX, int chunkZ, java.util.function.Function<RegionFile, T> function) {
			synchronized (WorldServer.this.getChunkProvider().playerChunkMap) {
				RegionFile file;

				try {
					file = WorldServer.this.getChunkProvider().playerChunkMap.regionFileCache.getFile(new ChunkCoordIntPair(chunkX, chunkZ), false);
				} catch (java.io.IOException ex) {
					throw new RuntimeException(ex);
				}

				return function.apply(file);
			}
		}

		@Override
		public <T> T computeForRegionFileIfLoaded(int chunkX, int chunkZ, java.util.function.Function<RegionFile, T> function) {
			synchronized (WorldServer.this.getChunkProvider().playerChunkMap) {
				RegionFile file = WorldServer.this.getChunkProvider().playerChunkMap.regionFileCache.getRegionFileIfLoaded(new ChunkCoordIntPair(chunkX, chunkZ));
				return function.apply(file);
			}
		}
	};
	public final com.destroystokyo.paper.io.chunk.ChunkTaskManager asyncChunkTaskManager;

	// Paper end
	// Paper start
	@Override
	public boolean isChunkLoaded(int x, int z) {
		return this.getChunkProvider().getChunkAtIfLoadedImmediately(x, z) != null;
	}
	// Paper end

	// Paper start - optimise getPlayerByUUID
	@Nullable
	@Override
	public EntityHuman getPlayerByUUID(UUID uuid) {
		Entity player = this.entitiesByUUID.get(uuid);
		return (player instanceof EntityHuman) ? (EntityHuman) player : null;
	}
	// Paper end

	// Paper start - rewrite ticklistserver
	void onChunkSetTicking(int chunkX, int chunkZ) {
		if (com.destroystokyo.paper.PaperConfig.useOptimizedTickList) {
			((com.destroystokyo.paper.server.ticklist.PaperTickList) this.nextTickListBlock).onChunkSetTicking(chunkX, chunkZ);
			((com.destroystokyo.paper.server.ticklist.PaperTickList) this.nextTickListFluid).onChunkSetTicking(chunkX, chunkZ);
		}
	}
	// Paper end - rewrite ticklistserver

	// Tuinity start
	public final boolean areChunksLoadedForMove(AxisAlignedBB axisalignedbb) {
		// copied code from collision methods, so that we can guarantee that they wont load chunks (we don't override
		// ICollisionAccess methods for VoxelShapes)
		// be more strict too, add a block (dumb plugins in move events?)
		int minBlockX = MathHelper.floor(axisalignedbb.minX - 1.0E-7D) - 3;
		int maxBlockX = MathHelper.floor(axisalignedbb.maxX + 1.0E-7D) + 3;

		int minBlockZ = MathHelper.floor(axisalignedbb.minZ - 1.0E-7D) - 3;
		int maxBlockZ = MathHelper.floor(axisalignedbb.maxZ + 1.0E-7D) + 3;

		int minChunkX = minBlockX >> 4;
		int maxChunkX = maxBlockX >> 4;

		int minChunkZ = minBlockZ >> 4;
		int maxChunkZ = maxBlockZ >> 4;

		ChunkProviderServer chunkProvider = this.getChunkProvider();

		for (int cx = minChunkX; cx <= maxChunkX; ++cx) {
			for (int cz = minChunkZ; cz <= maxChunkZ; ++cz) {
				if (chunkProvider.getChunkAtIfLoadedImmediately(cx, cz) == null) {
					return false;
				}
			}
		}

		return true;
	}

	public final void loadChunksForMoveAsync(AxisAlignedBB axisalignedbb, double toX, double toZ,
																					 java.util.function.Consumer<List<IChunkAccess>> onLoad) {
		if (Thread.currentThread() != this.serverThread) {
			this.getChunkProvider().serverThreadQueue.execute(() -> {
				this.loadChunksForMoveAsync(axisalignedbb, toX, toZ, onLoad);
			});
			return;
		}
		List<IChunkAccess> ret = new ObjectArrayList<>();
		IntArrayList ticketLevels = new IntArrayList();

		int minBlockX = MathHelper.floor(axisalignedbb.minX - 1.0E-7D) - 3;
		int maxBlockX = MathHelper.floor(axisalignedbb.maxX + 1.0E-7D) + 3;

		int minBlockZ = MathHelper.floor(axisalignedbb.minZ - 1.0E-7D) - 3;
		int maxBlockZ = MathHelper.floor(axisalignedbb.maxZ + 1.0E-7D) + 3;

		int minChunkX = minBlockX >> 4;
		int maxChunkX = maxBlockX >> 4;

		int minChunkZ = minBlockZ >> 4;
		int maxChunkZ = maxBlockZ >> 4;

		ChunkProviderServer chunkProvider = this.getChunkProvider();

		int requiredChunks = (maxChunkX - minChunkX + 1) * (maxChunkZ - minChunkZ + 1);
		int[] loadedChunks = new int[1];

		Long holderIdentifier = Long.valueOf(chunkProvider.chunkFutureAwaitCounter++);

		java.util.function.Consumer<IChunkAccess> consumer = (IChunkAccess chunk) -> {
			if (chunk != null) {
				int ticketLevel = Math.max(33, chunkProvider.playerChunkMap.getUpdatingChunk(chunk.getPos().pair()).getTicketLevel());
				ret.add(chunk);
				ticketLevels.add(ticketLevel);
				chunkProvider.addTicketAtLevel(TicketType.FUTURE_AWAIT, chunk.getPos(), ticketLevel, holderIdentifier);
			}
			if (++loadedChunks[0] == requiredChunks) {
				try {
					onLoad.accept(java.util.Collections.unmodifiableList(ret));
				} finally {
					for (int i = 0, len = ret.size(); i < len; ++i) {
						ChunkCoordIntPair chunkPos = ret.get(i).getPos();
						int ticketLevel = ticketLevels.getInt(i);

						chunkProvider.addTicketAtLevel(TicketType.UNKNOWN, chunkPos, ticketLevel, chunkPos);
						chunkProvider.removeTicketAtLevel(TicketType.FUTURE_AWAIT, chunkPos, ticketLevel, holderIdentifier);
					}
				}
			}
		};

		for (int cx = minChunkX; cx <= maxChunkX; ++cx) {
			for (int cz = minChunkZ; cz <= maxChunkZ; ++cz) {
				chunkProvider.getChunkAtAsynchronously(cx, cz, ChunkStatus.FULL, true, false, consumer);
			}
		}
	}
	// Tuinity end

	// Tuinity start - execute chunk tasks mid tick
	public long lastMidTickExecuteFailure;
	// Tuinity end - execute chunk tasks mid tick

	// Tuinity start - optimise checkDespawn
	public final List<EntityPlayer> playersAffectingSpawning = new ObjectArrayList<>();

	// Tuinity end - optimise checkDespawn
	// Tuinity start - optimise get nearest players for entity AI
	public final EntityPlayer getNearestPlayer(net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition condition, @Nullable EntityLiving source,
																						 double centerX, double centerY, double centerZ) {
		com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> nearby;
		if (source != null) {
			Chunk chunk = source.getCurrentChunk();
			if (chunk != null && (MathHelper.floor(centerX) >> 4) == chunk.locX &&
				(MathHelper.floor(centerZ) >> 4) == chunk.locZ) {
				nearby = chunk.getPlayerGeneralAreaCache();
			} else {
				nearby = this.getChunkProvider().playerChunkMap.playerGeneralAreaMap.getObjectsInRange(MathHelper.floor(centerX) >> 4, MathHelper.floor(centerZ) >> 4);
			}
		} else {
			nearby = this.getChunkProvider().playerChunkMap.playerGeneralAreaMap.getObjectsInRange(MathHelper.floor(centerX) >> 4, MathHelper.floor(centerZ) >> 4);
		}

		if (nearby == null) {
			return null;
		}

		Object[] backingSet = nearby.getBackingSet();

		double closestDistanceSquared = Double.MAX_VALUE;
		EntityPlayer closest = null;

		for (int i = 0, len = backingSet.length; i < len; ++i) {
			Object _player = backingSet[i];
			if (!(_player instanceof EntityPlayer player)) {
				continue;
			}

			double distanceSquared = player.getDistanceSquared(centerX, centerY, centerZ);
			if (distanceSquared < closestDistanceSquared && condition.test(source, player)) {
				closest = player;
				closestDistanceSquared = distanceSquared;
			}
		}

		return closest;
	}

	@Override
	public EntityHuman a(net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving) {
		return this.getNearestPlayer(pathfindertargetcondition, entityliving, entityliving.locX(), entityliving.locY(), entityliving.locZ());
	}

	@Override
	public EntityHuman a(net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2) {
		return this.getNearestPlayer(pathfindertargetcondition, entityliving, d0, d1, d2);
	}

	@Override
	public EntityHuman a(net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition pathfindertargetcondition, double d0, double d1, double d2) {
		return this.getNearestPlayer(pathfindertargetcondition, null, d0, d1, d2);
	}

	@Override
	public List<EntityHuman> a(net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition condition, EntityLiving source, AxisAlignedBB axisalignedbb) {
		com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> nearby;
		double centerX = (axisalignedbb.maxX + axisalignedbb.minX) * 0.5;
		double centerZ = (axisalignedbb.maxZ + axisalignedbb.minZ) * 0.5;
		if (source != null) {
			Chunk chunk = source.getCurrentChunk();
			if (chunk != null && (MathHelper.floor(centerX) >> 4) == chunk.locX &&
				(MathHelper.floor(centerZ) >> 4) == chunk.locZ) {
				nearby = chunk.getPlayerGeneralAreaCache();
			} else {
				nearby = this.getChunkProvider().playerChunkMap.playerGeneralAreaMap.getObjectsInRange(MathHelper.floor(centerX) >> 4, MathHelper.floor(centerZ) >> 4);
			}
		} else {
			nearby = this.getChunkProvider().playerChunkMap.playerGeneralAreaMap.getObjectsInRange(MathHelper.floor(centerX) >> 4, MathHelper.floor(centerZ) >> 4);
		}

		List<EntityHuman> ret = new ObjectArrayList<>();

		if (nearby == null) {
			return ret;
		}

		Object[] backingSet = nearby.getBackingSet();

		for (int i = 0, len = backingSet.length; i < len; ++i) {
			Object _player = backingSet[i];
			if (!(_player instanceof EntityPlayer player)) {
				continue;
			}

			if (axisalignedbb.contains(player.locX(), player.locY(), player.locZ()) && condition.test(source, player)) {
				ret.add(player);
			}
		}

		return ret;
	}
	// Tuinity end - optimise get nearest players for entity AI

	// Tuinity start - rewrite light engine
	/**
	 * Cannot be modified during light updates.
	 */
	public com.tuinity.tuinity.chunk.light.VariableBlockLightHandler customBlockLightHandlers;
	// Tuinity end - rewrite light engine

	// Add env and gen to constructor, WorldData -> WorldDataServer
	public WorldServer(MinecraftServer minecraftserver, Executor executor, Convertable.ConversionSession convertable_conversionsession, IWorldDataServer iworlddataserver, ResourceKey<World> resourcekey, DimensionManager dimensionmanager, WorldLoadListener worldloadlistener, ChunkGenerator chunkgenerator, boolean flag, long i, List<MobSpawner> list, boolean flag1, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
		super(iworlddataserver, resourcekey, dimensionmanager, false, flag, i, gen, env, executor); // Paper pass executor
		this.pvpMode = minecraftserver.getPVP();
		convertable = convertable_conversionsession;
		uuid = WorldUUID.getUUID(convertable_conversionsession.folder.toFile());
		// CraftBukkit end
		if (com.destroystokyo.paper.PaperConfig.useOptimizedTickList) {
			this.nextTickListBlock = new com.destroystokyo.paper.server.ticklist.PaperTickList<>(this, (block) -> {
				return block == null || block.getBlockData().isAir() || purpurConfig.noRandomTickBlocks.contains(block); // Purpur
			}, IRegistry.BLOCK::getKey, this::b, "Blocks"); // Paper - Timings
			this.nextTickListFluid = new com.destroystokyo.paper.server.ticklist.PaperTickList<>(this, (fluidtype) -> {
				return fluidtype == null || fluidtype == FluidTypes.EMPTY;
			}, IRegistry.FLUID::getKey, this::a, "Fluids"); // Paper - Timings
		} else {
			this.nextTickListBlock = new TickListServer<>(this, (block) -> {
				return block == null || block.getBlockData().isAir() || purpurConfig.noRandomTickBlocks.contains(block); // Purpur
			}, IRegistry.BLOCK::getKey, this::b, "Blocks"); // Paper - Timings
			this.nextTickListFluid = new TickListServer<>(this, (fluidtype) -> {
				return fluidtype == null || fluidtype == FluidTypes.EMPTY;
			}, IRegistry.FLUID::getKey, this::a, "Fluids"); // Paper - Timings
		}
		this.navigators = Sets.newHashSet();
		this.L = new ObjectLinkedOpenHashSet();
		this.Q = flag1;
		this.server = minecraftserver;
		// Purpur start - enable/disable MobSpawners per world
		this.mobSpawners = new ObjectArrayList<>();
		if (purpurConfig.phantomSpawning) {
			mobSpawners.add(new MobSpawnerPhantom());
		}
		if (purpurConfig.patrolSpawning) {
			mobSpawners.add(new MobSpawnerPatrol());
		}
		if (purpurConfig.catSpawning) {
			mobSpawners.add(new MobSpawnerCat());
		}
		if (purpurConfig.villageSiegeSpawning) {
			mobSpawners.add(new VillageSiege());
		}
		if (purpurConfig.villagerTraderSpawning) {
			mobSpawners.add(new MobSpawnerTrader(iworlddataserver));
		}
		// Purpur end
		// CraftBukkit start
		this.worldDataServer = (WorldDataServer) iworlddataserver;
		worldDataServer.world = this;
		if (gen != null) {
			chunkgenerator = new org.bukkit.craftbukkit.generator.CustomChunkGenerator(this, chunkgenerator, gen);
		}

		this.chunkProvider = new ChunkProviderServer(this, convertable_conversionsession, minecraftserver.getDataFixer(), minecraftserver.getDefinedStructureManager(), executor, chunkgenerator, this.spigotConfig.viewDistance, minecraftserver.isSyncChunkWrites(), worldloadlistener, () -> { // Spigot
			return minecraftserver.E().getWorldPersistentData();
		});
		// CraftBukkit end
		this.portalTravelAgent = new PortalTravelAgent(this);
		this.Q();
		this.R();
		this.getWorldBorder().a(minecraftserver.au());
		this.persistentRaid = this.getWorldPersistentData().a(() -> {
			return new PersistentRaid(this);
		}, PersistentRaid.a(this.getDimensionManager()));
		if (!minecraftserver.isEmbeddedServer()) {
			Globals.setupGlobals(this);
			iworlddataserver.setGameType(minecraftserver.getGamemode());
		}

		this.structureManager = new StructureManager(this, this.worldDataServer.getGeneratorSettings()); // CraftBukkit
		if (this.getDimensionManager().isCreateDragonBattle()) {
			this.dragonBattle = new EnderDragonBattle(this, this.worldDataServer.getGeneratorSettings().getSeed(), this.worldDataServer.C()); // CraftBukkit
		} else {
			this.dragonBattle = null;
		}
		this.getServer().addWorld(this.getWorld()); // CraftBukkit

		this.asyncChunkTaskManager = new com.destroystokyo.paper.io.chunk.ChunkTaskManager(this); // Paper
		this.fakeTime = this.worldDataServer.getDayTime(); // Purpur
	}

	// Tuinity start - optimise collision
	public boolean collidesWithAnyBlockOrWorldBorder(@Nullable Entity entity, AxisAlignedBB axisalignedbb, boolean loadChunks,
																									 boolean collidesWithUnloaded) {
		return this.getCollisionsForBlocksOrWorldBorder(entity, axisalignedbb, null, loadChunks, collidesWithUnloaded, true, null);
	}

	public boolean collidesWithAnyBlockOrWorldBorder(@Nullable Entity entity, AxisAlignedBB axisalignedbb,
																									 boolean loadChunks, boolean collidesWithUnloaded,
																									 java.util.function.BiPredicate<IBlockData, BlockPosition> predicate) {
		return this.getCollisionsForBlocksOrWorldBorder(entity, axisalignedbb, null, loadChunks, collidesWithUnloaded, true, predicate);
	}

	public final boolean hardCollidesWithAnyEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<Entity> predicate) {
		if (axisalignedbb.isEmpty()) {
			return false;
		}

		// to comply with vanilla intersection rules, expand by -epsilon so we only get stuff we definitely collide with.
		// Vanilla for hard collisions has this backwards, and they expand by +epsilon but this causes terrible problems
		// specifically with boat collisions.
		axisalignedbb = axisalignedbb.grow(-MCUtil.COLLISION_EPSILON, -MCUtil.COLLISION_EPSILON, -MCUtil.COLLISION_EPSILON);
		List<Entity> entities = com.tuinity.tuinity.util.CachedLists.getTempGetEntitiesList();
		try {
			if (entity != null && entity.hardCollides()) {
				this.getEntities(entity, axisalignedbb, predicate, entities);
			} else {
				this.getHardCollidingEntities(entity, axisalignedbb, predicate, entities);
			}

			for (int i = 0, len = entities.size(); i < len; ++i) {
				Entity otherEntity = entities.get(i);

				if ((entity == null || otherEntity.collisionBoxIsHard()) || entity.hardCollidesWith(otherEntity)) {
					return true;
				}
			}

			return false;
		} finally {
			com.tuinity.tuinity.util.CachedLists.returnTempGetEntitiesList(entities);
		}
	}

	public final boolean hasAnyCollisions(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
		return this.hasAnyCollisions(entity, axisalignedbb, true);
	}

	public final boolean hasAnyCollisions(@Nullable Entity entity, AxisAlignedBB axisalignedbb, boolean loadChunks) {
		return this.collidesWithAnyBlockOrWorldBorder(entity, axisalignedbb, loadChunks, true)
			|| this.hardCollidesWithAnyEntities(entity, axisalignedbb, null);
	}

	// returns whether any collisions were detected
	public boolean getCollisionsForBlocksOrWorldBorder(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list,
																										 boolean loadChunks, boolean collidesWithUnloaded, boolean checkOnly,
																										 java.util.function.BiPredicate<IBlockData, BlockPosition> predicate) {
		boolean ret = false;

		if (entity != null) {
			if (this.getWorldBorder().isAlmostCollidingOnBorder(axisalignedbb)) {
				if (checkOnly) {
					return true;
				} else {
					VoxelShapes.addBoxesTo(this.getWorldBorder().getCollisionShape(), list);
					ret = true;
				}
			}
		}

		// Purpur Start Rebase - Calculate when needed only
		int minBlockY = MathHelper.floor(axisalignedbb.minY - MCUtil.COLLISION_EPSILON) - 1;
		int maxBlockY = MathHelper.floor(axisalignedbb.maxY + MCUtil.COLLISION_EPSILON) + 1;

		BlockPosition.MutableBlockPosition mutablePos = new BlockPosition.MutableBlockPosition();
		net.minecraft.world.phys.shapes.VoxelShapeCollision collisionShape = null;

		// special cases:
		if (minBlockY > 255 || maxBlockY < 0) {
			// no point in checking
			return ret;
		}

		int minBlockX = MathHelper.floor(axisalignedbb.minX - MCUtil.COLLISION_EPSILON) - 1;
		int maxBlockX = MathHelper.floor(axisalignedbb.maxX + MCUtil.COLLISION_EPSILON) + 1;

		int minBlockZ = MathHelper.floor(axisalignedbb.minZ - MCUtil.COLLISION_EPSILON) - 1;
		int maxBlockZ = MathHelper.floor(axisalignedbb.maxZ + MCUtil.COLLISION_EPSILON) + 1;
		// Purpur End

		int minYIterate = Math.max(0, minBlockY);
		int maxYIterate = Math.min(255, maxBlockY);

		int minChunkX = minBlockX >> 4;
		int maxChunkX = maxBlockX >> 4;

		int minChunkZ = minBlockZ >> 4;
		int maxChunkZ = maxBlockZ >> 4;

		ChunkProviderServer chunkProvider = this.chunkProvider;
		// TODO special case single chunk?

		for (int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
			int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0; // coordinate in chunk
			int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15; // coordinate in chunk

			for (int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
				int minX = currChunkX == minChunkX ? minBlockX & 15 : 0; // coordinate in chunk
				int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15; // coordinate in chunk

				int chunkXGlobalPos = currChunkX << 4;
				int chunkZGlobalPos = currChunkZ << 4;
				Chunk chunk = loadChunks ? chunkProvider.getChunkAt(currChunkX, currChunkZ, true) : chunkProvider.getChunkAtIfLoadedImmediately(currChunkX, currChunkZ);

				if (chunk == null) {
					if (collidesWithUnloaded) {
						if (checkOnly) {
							return true;
						} else {
							list.add(AxisAlignedBB.getBoxForChunk(currChunkX, currChunkZ));
							ret = true;
						}
					}
					continue;
				}

				ChunkSection[] sections = chunk.getSections();

				// bound y

				for (int currY = minYIterate; currY <= maxYIterate; ++currY) {
					ChunkSection section = sections[currY >>> 4];
					if (section == null || section.isFullOfAir()) {
						// empty
						// skip to next section
						currY = (currY & ~(15)) + 15; // increment by 15: iterator loop increments by the extra one
						continue;
					}

					net.minecraft.world.level.chunk.DataPaletteBlock<IBlockData> blocks = section.blockIds;

					for (int currZ = minZ; currZ <= maxZ; ++currZ) {
						for (int currX = minX; currX <= maxX; ++currX) {
							int localBlockIndex = (currX) | (currZ << 4) | ((currY & 15) << 8);
							int blockX = currX | chunkXGlobalPos;
							int blockY = currY;
							int blockZ = currZ | chunkZGlobalPos;

							int edgeCount = ((blockX == minBlockX || blockX == maxBlockX) ? 1 : 0) +
								((blockY == minBlockY || blockY == maxBlockY) ? 1 : 0) +
								((blockZ == minBlockZ || blockZ == maxBlockZ) ? 1 : 0);
							if (edgeCount == 3) {
								continue;
							}

							IBlockData blockData = blocks.rawGet(localBlockIndex);

							if ((edgeCount != 1 || blockData.shapeExceedsCube()) && (edgeCount != 2 || blockData.getBlock() == Blocks.MOVING_PISTON)) {
								mutablePos.setValues(blockX, blockY, blockZ);
								if (collisionShape == null) {
									collisionShape = entity == null ? net.minecraft.world.phys.shapes.VoxelShapeCollision.a() : net.minecraft.world.phys.shapes.VoxelShapeCollision.a(entity);
								}
								VoxelShape voxelshape2 = blockData.getCollisionShape(this, mutablePos, collisionShape);
								if (voxelshape2 != VoxelShapes.getEmptyShape()) {
									VoxelShape voxelshape3 = voxelshape2.offset(blockX, blockY, blockZ);

									if (predicate != null && !predicate.test(blockData, mutablePos)) {
										continue;
									}

									if (checkOnly) {
										if (voxelshape3.intersects(axisalignedbb)) {
											return true;
										}
									} else {
										ret |= VoxelShapes.addBoxesToIfIntersects(voxelshape3, axisalignedbb, list);
									}
								}
							}
						}
					}
				}
			}
		}

		return ret;
	}

	public final void getEntityHardCollisions(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<Entity> predicate, List<AxisAlignedBB> list) {
		if (axisalignedbb.isEmpty()) {
			return;
		}

		// to comply with vanilla intersection rules, expand by -epsilon so we only get stuff we definitely collide with.
		// Vanilla for hard collisions has this backwards, and they expand by +epsilon but this causes terrible problems
		// specifically with boat collisions.
		axisalignedbb = axisalignedbb.grow(-MCUtil.COLLISION_EPSILON, -MCUtil.COLLISION_EPSILON, -MCUtil.COLLISION_EPSILON);
		List<Entity> entities = com.tuinity.tuinity.util.CachedLists.getTempGetEntitiesList();
		try {
			if (entity != null && entity.hardCollides()) {
				this.getEntities(entity, axisalignedbb, predicate, entities);
			} else {
				this.getHardCollidingEntities(entity, axisalignedbb, predicate, entities);
			}

			for (int i = 0, len = entities.size(); i < len; ++i) {
				Entity otherEntity = entities.get(i);

				if ((entity == null || otherEntity.collisionBoxIsHard()) || entity.hardCollidesWith(otherEntity)) {
					if (!otherEntity.getBoundingBox().isEmpty()) {
						list.add(otherEntity.getBoundingBox());
					}
				}
			}
		} finally {
			com.tuinity.tuinity.util.CachedLists.returnTempGetEntitiesList(entities);
		}
	}

	public final void getCollisions(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, boolean loadChunks) {
		this.getCollisionsForBlocksOrWorldBorder(entity, axisalignedbb, list, loadChunks, true, false, null);
		this.getEntityHardCollisions(entity, axisalignedbb, null, list);
	}

	@Override
	public boolean getCubes(AxisAlignedBB axisalignedbb) {
		return !this.hasAnyCollisions(null, axisalignedbb);
	}

	@Override
	public boolean getCubes(Entity entity) {
		return !this.hasAnyCollisions(entity, entity.getBoundingBox());
	}

	@Override
	public boolean getCubes(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
		if (entity instanceof net.minecraft.world.entity.decoration.EntityArmorStand && !entity.world.paperConfig.armorStandEntityLookups)
			return false;
		return !this.hasAnyCollisions(entity, axisalignedbb);
	}

	@Override
	public boolean getCubes(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
		if (entity instanceof net.minecraft.world.entity.decoration.EntityArmorStand && !entity.world.paperConfig.armorStandEntityLookups)
			return false;
		return !this.collidesWithAnyBlockOrWorldBorder(entity, axisalignedbb, false, false) && !this.hardCollidesWithAnyEntities(entity, axisalignedbb, predicate);
	}
	// Tuinity end - optimise collision

	// CraftBukkit start
	@Override
	public TileEntity getTileEntity(BlockPosition pos, boolean validate) {
		TileEntity result = super.getTileEntity(pos, validate);
		if (!validate || Thread.currentThread() != this.serverThread) {
			// SPIGOT-5378: avoid deadlock, this can be called in loading logic (i.e lighting) but getType() will block on chunk load
			return result;
		}
		Block type = getType(pos).getBlock();

		if (result != null && type != Blocks.AIR) {
			if (!result.getTileType().isValidBlock(type)) {
				result = fixTileEntity(pos, type, result);
			}
		}

		return result;
	}

	private TileEntity fixTileEntity(BlockPosition pos, Block type, TileEntity found) {
		this.getServer().getLogger().log(Level.SEVERE, "Block at {0}, {1}, {2} is {3} but has {4}" + ". "
			+ "Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", new Object[]{pos.getX(), pos.getY(), pos.getZ(), type, found});

		if (type instanceof ITileEntity) {
			TileEntity replacement = ((ITileEntity) type).createTile(this);
			if (replacement != null) {
				replacement.setLocation(this, pos);
				this.setTileEntity(pos, replacement);
			}
			return replacement;
		} else {
			return found;
		}
	}
	// CraftBukkit end

	public void a(int i, int j, boolean flag, boolean flag1) {
		this.worldDataServer.setClearWeatherTime(i);
		this.worldDataServer.setWeatherDuration(j);
		this.worldDataServer.setThunderDuration(j);
		this.worldDataServer.setStorm(flag, org.bukkit.event.weather.WeatherChangeEvent.Cause.COMMAND); // Paper
		this.worldDataServer.setThundering(flag1, org.bukkit.event.weather.ThunderChangeEvent.Cause.COMMAND); // Paper
	}

	public BiomeBase getBiomeBySeed(int i, int j, int k) {
		return a(i, j, k);
	} // Paper - OBFHELPER

	@Override
	public BiomeBase a(int i, int j, int k) {
		return this.getChunkProvider().getChunkGenerator().getWorldChunkManager().getBiome(i, j, k);
	}

	public StructureManager getStructureManager() {
		return this.structureManager;
	}

	public void doTick(BooleanSupplier booleansupplier) {
		// Tuinity start - optimise checkDespawn
		this.playersAffectingSpawning.clear();
		for (EntityPlayer player : this.getPlayers()) {
			if (net.minecraft.world.entity.IEntitySelector.affectsSpawning.test(player)) {
				this.playersAffectingSpawning.add(player);
			}
		}
		// Tuinity end - optimise checkDespawn

		this.ticking = true;
		this.getWorldBorder().s();
		boolean flag = this.isRaining();

		if (this.getDimensionManager().hasSkyLight()) {
			if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
				int i = this.worldDataServer.getClearWeatherTime();
				int j = this.worldDataServer.getThunderDuration();
				int k = this.worldDataServer.getWeatherDuration();
				boolean flag1 = this.worldData.isThundering();
				boolean flag2 = this.worldData.hasStorm();

				if (i > 0) {
					--i;
					j = flag1 ? 0 : 1;
					k = flag2 ? 0 : 1;
					flag1 = false;
					flag2 = false;
				} else {
					if (j > 0) {
						--j;
						if (j == 0) {
							flag1 = !flag1;
						}
					} else if (flag1) {
						j = this.random.nextInt(12000) + 3600;
					} else {
						j = this.random.nextInt(168000) + 12000;
					}

					if (k > 0) {
						--k;
						if (k == 0) {
							flag2 = !flag2;
						}
					} else if (flag2) {
						k = this.random.nextInt(12000) + 12000;
					} else {
						k = this.random.nextInt(168000) + 12000;
					}
				}

				this.worldDataServer.setThunderDuration(j);
				this.worldDataServer.setWeatherDuration(k);
				this.worldDataServer.setClearWeatherTime(i);
				this.worldDataServer.setThundering(flag1, org.bukkit.event.weather.ThunderChangeEvent.Cause.NATURAL); // Paper
				this.worldDataServer.setStorm(flag2, org.bukkit.event.weather.WeatherChangeEvent.Cause.NATURAL); // Paper
			}

			this.lastThunderLevel = this.thunderLevel;
			if (this.worldData.isThundering()) {
				this.thunderLevel = (float) ((double) this.thunderLevel + 0.01D);
			} else {
				this.thunderLevel = (float) ((double) this.thunderLevel - 0.01D);
			}

			this.thunderLevel = MathHelper.a(this.thunderLevel, 0.0F, 1.0F);
			this.lastRainLevel = this.rainLevel;
			if (this.worldData.hasStorm()) {
				this.rainLevel = (float) ((double) this.rainLevel + 0.01D);
			} else {
				this.rainLevel = (float) ((double) this.rainLevel - 0.01D);
			}

			this.rainLevel = MathHelper.a(this.rainLevel, 0.0F, 1.0F);
		}

        /* CraftBukkit start
        if (this.lastRainLevel != this.rainLevel) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.h, this.rainLevel)), this.getDimensionKey());
        }

        if (this.lastThunderLevel != this.thunderLevel) {
            this.server.getPlayerList().a((Packet) (new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.i, this.thunderLevel)), this.getDimensionKey());
        }

        if (flag != this.isRaining()) {
            if (flag) {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.c, 0.0F));
            } else {
                this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.b, 0.0F));
            }

            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.h, this.rainLevel));
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.i, this.thunderLevel));
        }
        // */
		for (int idx = 0; idx < this.players.size(); ++idx) {
			if (this.players.get(idx).world == this) {
				this.players.get(idx).tickWeather();
			}
		}

		if (flag != this.isRaining()) {
			// Only send weather packets to those affected
			for (int idx = 0; idx < this.players.size(); ++idx) {
				if (this.players.get(idx).world == this) {
					this.players.get(idx).setPlayerWeather((!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR), false);
				}
			}
		}
		for (int idx = 0; idx < this.players.size(); ++idx) {
			if (this.players.get(idx).world == this) {
				this.players.get(idx).updateWeather(this.lastRainLevel, this.rainLevel, this.lastThunderLevel, this.thunderLevel);
			}
		}
		// CraftBukkit end

		if (this.purpurConfig.playersSkipNight && this.everyoneSleeping && this.players.stream().noneMatch((entityplayer) -> { // Purpur
			return !entityplayer.isSpectator() && !entityplayer.isDeeplySleeping() && !entityplayer.fauxSleeping && !(purpurConfig.idleTimeoutCountAsSleeping && entityplayer.isAfk()); // CraftBukkit // Purpur
		})) {
			// CraftBukkit start
			long l = this.worldData.getDayTime() + 24000L;
			TimeSkipEvent event = new TimeSkipEvent(this.getWorld(), TimeSkipEvent.SkipReason.NIGHT_SKIP, (l - l % 24000L) - this.getDayTime());
			if (this.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
				getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					this.setDayTime(this.getDayTime() + event.getSkipAmount());
				}

			}

			if (!event.isCancelled()) {
				this.everyoneSleeping = false;
				this.wakeupPlayers();
			}
			// CraftBukkit end
			if (this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
				this.clearWeather();
			}
		}

		this.Q();
		this.b();
		this.getChunkProvider().tick(booleansupplier);
		if (!this.isDebugWorld()) {
			this.nextTickListBlock.b();
			this.nextTickListFluid.b();
		}

		// Tuinity - replace logic
		this.persistentRaid.a();
		this.ak();
		// Tuinity - replace logic
		this.ticking = false;
		boolean flag3 = true; // CraftBukkit - this prevents entity cleanup, other issues on servers with no players

		if (flag3) {
			this.resetEmptyTime();
		}

		if (flag3 || this.emptyTime++ < 300) {
			if (this.dragonBattle != null) {
				this.dragonBattle.b();
			}

			this.tickingEntities = true;
			com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet.Iterator<Entity> objectiterator = this.entitiesForIteration.iterator(); // Tuinity

			org.spigotmc.ActivationRange.activateEntities(this); // Spigot
			while (objectiterator.hasNext()) {
				Entity entity = objectiterator.next(); // Tuinity
				Entity entity1 = entity.getVehicle();

                /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
                if (!this.server.getSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
                    entity.die();
                }

                if (!this.server.getSpawnNPCs() && entity instanceof NPC) {
                    entity.die();
                }
                // CraftBukkit end */

				if (!entity.dead) {
					entity.checkDespawn();
					// Tuinity start - optimise notify()
					if (entity.inChunk && entity.valid) {
						if (this.getChunkProvider().isInEntityTickingChunk(entity)) {
							this.updateNavigatorsInRegion(entity);
						}
					} else {
						this.removeNavigatorsFromData(entity);
					}
					// Tuinity end - optimise notify()
				}

				if (entity1 != null) {
					if (!entity1.dead && entity1.w(entity)) {
						continue;
					}

					entity.stopRiding();
				}

				if (!entity.dead && !(entity instanceof EntityComplexPart)) {
					this.a(this::entityJoinedWorld, entity);
				}

				if (entity.dead) {
					this.removeEntityFromChunk(entity);
					this.entitiesById.remove(entity.getId()); // Tuinity
					this.unregisterEntity(entity);
				} else if (entity.inChunk && entity.valid) { // Tuinity start - optimise notify()
					if (this.getChunkProvider().isInEntityTickingChunk(entity)) {
						this.updateNavigatorsInRegion(entity);
					}
				} else {
					this.removeNavigatorsFromData(entity);
				}
				// Tuinity end - optimise notify()
			}

			objectiterator.finishedIterating(); // Tuinity
			this.tickingEntities = false;
			// Paper start
			for (java.lang.Runnable run : this.afterEntityTickingTasks) {
				try {
					run.run();
				} catch (Exception e) {
					LOGGER.error("Error in After Entity Ticking Task", e);
				}
			}
			this.afterEntityTickingTasks.clear();
			// Paper end
			// Tuinity - replace logic

			Entity entity2;

			while ((entity2 = this.entitiesToAdd.poll()) != null) {
				if (!entity2.isQueuedForRegister) continue; // Paper - ignore cancelled registers
				this.registerEntity(entity2);
			}

			// Tuinity - replace logic
			this.tickBlockEntities();
		}
	}

	protected void b() {
		if (this.Q) {
			long i = this.worldData.getTime() + 1L;

			this.worldDataServer.setTime(i); // Paper - diff on change, we want the below to be ran right after this
			this.nextTickListBlock.nextTick(); // Paper
			this.nextTickListFluid.nextTick(); // Paper
			this.worldDataServer.u().a(this.server, i);
			// Purpur start
			WorldServer world = this.worldDataServer.world;
			if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
				double incrementTimeBy = 12000.0D / (double) (world.isDay() ? world.purpurConfig.daytimeTicks : world.purpurConfig.nighttimeTicks);
				if (incrementTimeBy != 1.0D) {
					this.fakeTime += incrementTimeBy;
					this.setDayTime(this.fakeTime);
					PacketPlayOutUpdateTime packet = new PacketPlayOutUpdateTime(world.getTime(), world.getDayTime(), true);
					for (EntityHuman entityhuman : world.players) {
						EntityPlayer player = (EntityPlayer) entityhuman;
						packet.setPlayerTime(player.getPlayerTime());
						player.playerConnection.sendPacket(packet);
					}
				} else
					// Purpur end
					this.setDayTime(this.worldData.getDayTime() + 1L);
			}

		}
	}

	public void setDayTime(long i) {
		this.worldDataServer.setDayTime(i);
		// Purpur start
		this.fakeTime = i;
	}

	public void setDayTime(double i) {
		this.worldDataServer.setDayTime((long) i);
		// Purpur end
	}

	public void doMobSpawning(boolean flag, boolean flag1) {
		Iterator iterator = this.mobSpawners.iterator();

		while (iterator.hasNext()) {
			MobSpawner mobspawner = (MobSpawner) iterator.next();

			mobspawner.a(this, flag, flag1);
		}

	}

	private void wakeupPlayers() {
		(this.players.stream().filter(EntityLiving::isSleeping).collect(Collectors.toList())).forEach((entityplayer) -> { // CraftBukkit - decompile error
			entityplayer.wakeup(false, false);
		});
	}

	// Paper start - optimise random block ticking
	private final BlockPosition.MutableBlockPosition chunkTickMutablePosition = new BlockPosition.MutableBlockPosition();
	private final com.destroystokyo.paper.util.math.ThreadUnsafeRandom randomTickRandom = new com.destroystokyo.paper.util.math.ThreadUnsafeRandom();
	// Paper end
	// Tuinity start - optimise chunk ice snow ticking
	private final BiomeBase[] biomeBaseCache = new BiomeBase[1];
	// Tuinity end - optimise chunk ice snow ticking

	public void a(Chunk chunk, int i) {
		final int randomTickSpeed = i; // Paper
		ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
		boolean flag = this.isRaining();
		int j = chunkcoordintpair.d();
		int k = chunkcoordintpair.e();

		final BlockPosition.MutableBlockPosition blockposition = this.chunkTickMutablePosition; // Paper - use mutable to reduce allocation rate, final to force compile fail on change

		if (!this.paperConfig.disableThunder && flag && this.W() && this.random.nextInt(100000) == 0) { // Paper - Disable thunder
			blockposition.setValues(this.a(this.a(j, 0, k, 15))); // Paper
			if (this.isRainingAt(blockposition)) {
				DifficultyDamageScaler difficultydamagescaler = this.getDamageScaler(blockposition);
				boolean flag1 = this.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && this.random.nextDouble() < (double) difficultydamagescaler.b() * paperConfig.skeleHorseSpawnChance; // Paper

				if (flag1) {
					// Purpur start
					EntityHorseAbstract horse;
					if (purpurConfig.zombieHorseSpawnChance > 0D && random.nextDouble() <= purpurConfig.zombieHorseSpawnChance) {
						horse = EntityTypes.ZOMBIE_HORSE.create(this);
					} else {
						horse = EntityTypes.SKELETON_HORSE.create(this);
						((EntityHorseSkeleton) horse).setTrap(true);
					}
					horse.setAgeRaw(0);
					horse.setPosition(blockposition.getX(), blockposition.getY(), blockposition.getZ());
					addEntity(horse, CreatureSpawnEvent.SpawnReason.LIGHTNING); // CraftBukkit
					// Purpur end
				}

				EntityLightning entitylightning = EntityTypes.LIGHTNING_BOLT.a(this);

				entitylightning.d(Vec3D.c(blockposition));
				entitylightning.setEffect(flag1);
				this.strikeLightning(entitylightning, org.bukkit.event.weather.LightningStrikeEvent.Cause.WEATHER); // CraftBukkit
			}
		}

		if (!this.paperConfig.disableIceAndSnow && this.randomTickRandom.nextInt(16) == 0) { // Paper - Disable ice and snow // Paper - optimise random ticking
			// Paper start - optimise chunk ticking
			// Tuinity start - optimise chunk ice snow ticking
			BiomeBase[] biomeCache = this.biomeBaseCache;
			biomeCache[0] = null;
			// Tuinity start - optimise chunk ice snow ticking
			this.getRandomBlockPosition(j, 0, k, 15, blockposition);
			int normalY = chunk.getHighestBlockY(HeightMap.Type.MOTION_BLOCKING, blockposition.getX() & 15, blockposition.getZ() & 15);
			int downY = normalY - 1;
			blockposition.setY(normalY);
			// Paper end
			//BiomeBase biomebase = this.getBiome(blockposition); // Tuinity - lazily-get biome

			// Paper start - optimise chunk ticking
			blockposition.setY(downY);
			if (BiomeBase.canTurnWaterIntoIce(this, blockposition, true, chunk, biomeCache)) { // Tuinity - add chunk parameter, avoid biome lookup
				org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition, Blocks.ICE.getBlockData(), null); // CraftBukkit
				// Paper end
			}

			blockposition.setY(normalY); // Paper
			if (flag && BiomeBase.canTurnAirIntoSnow(this, blockposition, chunk, biomeCache)) {
				org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this, blockposition, Blocks.SNOW.getBlockData(), null); // CraftBukkit
			}

			// Paper start - optimise chunk ticking
			blockposition.setY(downY);
			if (flag && chunk.getType(blockposition).getBlock() instanceof net.minecraft.world.level.block.BlockCauldron && this.getBiome(blockposition).c() == BiomeBase.Precipitation.RAIN) { // Tuinity - only cauldron uses that method, and it's not likely to hit. So, avoid the expensive biome lookup and replace it with a block lookup TODO check on update for overrides
				chunk.getType(blockposition).getBlock().c(this, blockposition);
				// Paper end
			}
		}

		// Paper start - optimise random block ticking
		if (i > 0) {
			ChunkSection[] sections = chunk.getSections();

			for (int sectionIndex = 0; sectionIndex < 16; ++sectionIndex) {
				ChunkSection section = sections[sectionIndex];
				if (section == null || section.tickingList.size() == 0) {
					continue;
				}

				int yPos = sectionIndex << 4;

				for (int a = 0; a < randomTickSpeed; ++a) {
					int tickingBlocks = section.tickingList.size();
					int index = this.randomTickRandom.nextInt(16 * 16 * 16);
					if (index >= tickingBlocks) {
						continue;
					}

					long raw = section.tickingList.getRaw(index);
					int location = com.destroystokyo.paper.util.maplist.IBlockDataList.getLocationFromRaw(raw);
					int randomX = location & 15;
					int randomY = ((location >>> (4 + 4)) & 255) | yPos;
					int randomZ = (location >>> 4) & 15;

					BlockPosition blockposition2 = blockposition.setValues(j + randomX, randomY, k + randomZ);
					IBlockData iblockdata = com.destroystokyo.paper.util.maplist.IBlockDataList.getBlockDataFromRaw(raw);

					iblockdata.b(this, blockposition2, this.randomTickRandom);

					// We drop the fluid tick since LAVA is ALREADY TICKED by the above method.
					// TODO CHECK ON UPDATE
				}
			}
			// Paper end
		}
	}

	protected BlockPosition a(BlockPosition blockposition) {
		BlockPosition blockposition1 = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, blockposition);
		AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition1, new BlockPosition(blockposition1.getX(), this.getBuildHeight(), blockposition1.getZ()))).g(3.0D);
		List<EntityLiving> list = this.a(EntityLiving.class, axisalignedbb, (entityliving) -> { // CraftBukkit - decompile error
			return entityliving != null && entityliving.isAlive() && this.e(entityliving.getChunkCoordinates());
		});

		if (!list.isEmpty()) {
			return list.get(this.random.nextInt(list.size())).getChunkCoordinates();
		} else {
			if (blockposition1.getY() == -1) {
				blockposition1 = blockposition1.up(2);
			}

			return blockposition1;
		}
	}

	public boolean m_() {
		return this.ticking;
	}

	public void everyoneSleeping() {
		this.everyoneSleeping = false;
		if (!this.players.isEmpty()) {
			int i = 0;
			int j = 0;
			Iterator iterator = this.players.iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();

				if (entityplayer.isSpectator() || (entityplayer.fauxSleeping && !entityplayer.isSleeping()) || (purpurConfig.idleTimeoutCountAsSleeping && entityplayer.isAfk())) { // CraftBukkit // Purpur
					++i;
				} else if (entityplayer.isSleeping()) {
					++j;
				}
			}

			this.everyoneSleeping = j > 0 && j >= this.players.size() - i;
		}

	}

	@Override
	public ScoreboardServer getScoreboard() {
		return this.server.getScoreboard();
	}

	private void clearWeather() {
		// CraftBukkit start
		this.worldDataServer.setStorm(false, org.bukkit.event.weather.WeatherChangeEvent.Cause.SLEEP); // Paper - when passing the night
		// If we stop due to everyone sleeping we should reset the weather duration to some other random value.
		// Not that everyone ever manages to get the whole server to sleep at the same time....
		if (!this.worldDataServer.hasStorm()) {
			this.worldDataServer.setWeatherDuration(0);
		}
		// CraftBukkit end
		this.worldDataServer.setThundering(false, org.bukkit.event.weather.ThunderChangeEvent.Cause.SLEEP); // Paper - when passing the night
		// CraftBukkit start
		// If we stop due to everyone sleeping we should reset the weather duration to some other random value.
		// Not that everyone ever manages to get the whole server to sleep at the same time....
		if (!this.worldDataServer.isThundering()) {
			this.worldDataServer.setThunderDuration(0);
		}
		// CraftBukkit end
	}

	public void resetEmptyTime() {
		this.emptyTime = 0;
	}

	private void a(NextTickListEntry<FluidType> nextticklistentry) {
		Fluid fluid = this.getFluid(nextticklistentry.a);

		if (fluid.getType() == nextticklistentry.b()) {
			fluid.a(this, nextticklistentry.a);
		}

	}

	private void b(NextTickListEntry<Block> nextticklistentry) {
		IBlockData iblockdata = this.getType(nextticklistentry.a);

		if (iblockdata.a(nextticklistentry.b())) {
			iblockdata.a(this, nextticklistentry.a, this.random);
		}

	}

	// Tuinity start - log detailed entity tick information
	// TODO replace with varhandle
	static final java.util.concurrent.atomic.AtomicReference<Entity> currentlyTickingEntity = new java.util.concurrent.atomic.AtomicReference<>();

	public static List<Entity> getCurrentlyTickingEntities() {
		Entity ticking = currentlyTickingEntity.get();
		List<Entity> ret = java.util.Arrays.asList(ticking == null ? new Entity[0] : new Entity[]{ticking});

		return ret;
	}
	// Tuinity end - log detailed entity tick information

	public void entityJoinedWorld(Entity entity) {
		// Tuinity start - log detailed entity tick information
		try {
			if (currentlyTickingEntity.get() == null) {
				currentlyTickingEntity.lazySet(entity);
			}
			// Tuinity end - log detailed entity tick information
			if (!(entity instanceof EntityHuman) && !this.getChunkProvider().a(entity)) {
				this.chunkCheck(entity);
			} else {
				// Spigot start
            /*if (!org.spigotmc.ActivationRange.checkIfActive(entity)) { // Paper - comment out - EAR 2, reimplement below
                entity.ticksLived++;
                timer =  entity.getEntityType().inactiveTickTimer.startTiming(); try { // Paper - timings
                entity.inactiveTick();
                } finally { timer.stopTiming(); } // Paper
                return;
            }*/ // Paper - comment out EAR 2
				// Spigot end
				// Paper start- timings
				final boolean isActive = org.spigotmc.ActivationRange.checkIfActive(entity);
				// Paper end - timings
				entity.g(entity.locX(), entity.locY(), entity.locZ());
				entity.lastYaw = entity.yaw;
				entity.lastPitch = entity.pitch;
				if (entity.inChunk) {
					++entity.ticksLived;

					if (isActive) { // Paper - EAR 2
						entity.tick();
						entity.postTick(); // CraftBukkit
					} else {
						entity.inactiveTick();
					} // Paper - EAR 2
				}

				this.chunkCheck(entity);
				if (entity.inChunk) {
					Iterator iterator = entity.getPassengers().iterator();

					while (iterator.hasNext()) {
						Entity entity1 = (Entity) iterator.next();

						this.a(entity, entity1);
					}
				}
				//} finally { timer.stopTiming(); } // Paper - timings - move up

			}
			// Tuinity start - log detailed entity tick information
		} finally {
			if (currentlyTickingEntity.get() == entity) {
				currentlyTickingEntity.lazySet(null);
			}
		}
		// Tuinity end - log detailed entity tick information
	}

	public void a(Entity entity, Entity entity1) {
		if (!entity1.dead && entity1.getVehicle() == entity) {
			if (entity1 instanceof EntityHuman || this.getChunkProvider().a(entity1)) {
				// Paper - EAR 2
				final boolean isActive = org.spigotmc.ActivationRange.checkIfActive(entity1);
				// Paper end
				entity1.g(entity1.locX(), entity1.locY(), entity1.locZ());
				entity1.lastYaw = entity1.yaw;
				entity1.lastPitch = entity1.pitch;
				if (entity1.inChunk) {
					++entity1.ticksLived;

					// Paper start - EAR 2
					if (isActive) {
						entity1.passengerTick();
						entity1.postTick(); // CraftBukkit
					} else {
						entity1.setMot(Vec3D.ORIGIN);
						entity1.inactiveTick();
						// copied from inside of if (isPassenger()) of passengerTick, but that ifPassenger is unnecessary
						entity.syncPositionOf(entity1);
					}
					// Paper end - EAR 2
				}

				this.chunkCheck(entity1);
				if (entity1.inChunk) {
					Iterator iterator = entity1.getPassengers().iterator();

					while (iterator.hasNext()) {
						Entity entity2 = (Entity) iterator.next();

						this.a(entity1, entity2);
					}
				}

			}
		} else {
			entity1.stopRiding();
		}
	}

	public void chunkCheck(Entity entity) {
		if (entity.cl()) {
			int i = MathHelper.floor(entity.locX() / 16.0D);
			int j = Math.min(15, Math.max(0, MathHelper.floor(entity.locY() / 16.0D))); // Paper - stay consistent with chunk add/remove behavior
			int k = MathHelper.floor(entity.locZ() / 16.0D);
			// Tuinity start
			int oldRegionX = entity.chunkX >> this.getChunkProvider().playerChunkMap.dataRegionManager.regionChunkShift;
			int oldRegionZ = entity.chunkZ >> this.getChunkProvider().playerChunkMap.dataRegionManager.regionChunkShift;
			int newRegionX = i >> this.getChunkProvider().playerChunkMap.dataRegionManager.regionChunkShift;
			int newRegionZ = k >> this.getChunkProvider().playerChunkMap.dataRegionManager.regionChunkShift;
			// Tuinity end

			if (!entity.inChunk || entity.chunkX != i || entity.chunkY != j || entity.chunkZ != k) {
				// Paper start - remove entity if its in a chunk more correctly.
				Chunk currentChunk = entity.getCurrentChunk();
				if (currentChunk != null) {
					currentChunk.removeEntity(entity);
				}
				// Paper end

				// Tuinity start
				if (oldRegionX != newRegionX || oldRegionZ != newRegionZ) {
					this.removeNavigatorsFromData(entity);
				}
				// Tuinity end

				if (entity.inChunk && this.isChunkLoaded(entity.chunkX, entity.chunkZ)) {
					this.getChunkAt(entity.chunkX, entity.chunkZ).a(entity, entity.chunkY);
				}

				if (!entity.valid && !entity.ck() && !this.isChunkLoaded(i, k)) { // Paper - always load chunks to register valid entities location
					if (entity.inChunk) {
						WorldServer.LOGGER.warn("Entity {} left loaded chunk area", entity);
					}

					entity.inChunk = false;
				} else {
					// Tuinity start - gotta be careful here, sync load can teleport entity.
					Chunk chunk = this.getChunkIfLoaded(i, k);
					if (chunk == null) {
						// might teleport the entity, so be prepared.
						// at this point, we are NOT in a chunk. so CLEARLY mark that for a recursive call to see.
						entity.inChunk = false;
						// now load
						chunk = this.getChunkAt(i, k);
						// are we in a chunk now?
						if (entity.inChunk) {
							// yup we teleported and were added to another chunk. do not add to the current one!
							return;
						}
						// did we teleport but did not add to a chunk?
						// copied from start
						int in = MathHelper.floor(entity.locX() / 16.0D);
						int jn = Math.min(15, Math.max(0, MathHelper.floor(entity.locY() / 16.0D))); // Paper - stay consistent with chunk add/remove behavior
						int kn = MathHelper.floor(entity.locZ() / 16.0D);
						if (in != i || jn != j || kn != k) {
							// teleported but were not added to a chunk. try now, let the new call try to add again
							this.chunkCheck(entity);
							return;
						}

						// only now at this point have we verified the new load did not screw up our position, so
						// now we can add to the chunk
					}
					chunk.a(entity);
					// Tuinity end - gotta be careful here, sync load can teleport entity.
				}
				// Tuinity start
				if (entity.inChunk && (oldRegionX != newRegionX || oldRegionZ != newRegionZ)) {
					this.addNavigatorsIfPathingToRegion(entity);
				}
				// Tuinity end
			}
		}
	}

	@Override
	public boolean a(EntityHuman entityhuman, BlockPosition blockposition) {
		return !this.server.a(this, blockposition, entityhuman) && this.getWorldBorder().a(blockposition);
	}

	// Paper start - derived from below
	public void saveIncrementally(boolean doFull) {
		ChunkProviderServer chunkproviderserver = this.getChunkProvider();

		if (doFull) {
			org.bukkit.Bukkit.getPluginManager().callEvent(new org.bukkit.event.world.WorldSaveEvent(getWorld()));
		}

		if (doFull) {
			this.saveData();
		}

		if (!this.isSavingDisabled()) chunkproviderserver.saveIncrementally();


		// Copied from save()
		// CraftBukkit start - moved from MinecraftServer.saveChunks
		if (doFull) { // Paper
			WorldServer worldserver1 = this;

			worldDataServer.a(worldserver1.getWorldBorder().t());
			worldDataServer.setCustomBossEvents(this.server.getBossBattleCustomData().save());
			convertable.a(this.server.customRegistry, this.worldDataServer, this.server.getPlayerList().save());
		}
		// CraftBukkit end
	}
	// Paper end

	public void save(@Nullable IProgressUpdate iprogressupdate, boolean flag, boolean flag1) {
		ChunkProviderServer chunkproviderserver = this.getChunkProvider();

		if (!flag1) {
			org.bukkit.Bukkit.getPluginManager().callEvent(new org.bukkit.event.world.WorldSaveEvent(getWorld())); // CraftBukkit
			if (iprogressupdate != null) {
				iprogressupdate.a(new ChatMessage("menu.savingLevel"));
			}

			this.aj();
			if (iprogressupdate != null) {
				iprogressupdate.c(new ChatMessage("menu.savingChunks"));
			}

			chunkproviderserver.save(flag);
		}

		// CraftBukkit start - moved from MinecraftServer.saveChunks
		WorldServer worldserver1 = this;

		worldDataServer.a(worldserver1.getWorldBorder().t());
		worldDataServer.setCustomBossEvents(this.server.getBossBattleCustomData().save());
		convertable.a(this.server.customRegistry, this.worldDataServer, this.server.getPlayerList().save());
		// CraftBukkit end
	}

	private void saveData() {
		this.aj();
	} // Paper - OBFHELPER

	private void aj() {
		if (this.dragonBattle != null) {
			this.worldDataServer.a(this.dragonBattle.a()); // CraftBukkit
		}

		this.getChunkProvider().getWorldPersistentData().a();
	}

	public List<Entity> a(@Nullable EntityTypes<?> entitytypes, Predicate<? super Entity> predicate) {
		List<Entity> list = Lists.newArrayList();
		ChunkProviderServer chunkproviderserver = this.getChunkProvider();
		ObjectIterator objectiterator = this.entitiesById.values().iterator();

		while (objectiterator.hasNext()) {
			Entity entity = (Entity) objectiterator.next();

			if ((entitytypes == null || entity.getEntityType() == entitytypes) && chunkproviderserver.isLoaded(MathHelper.floor(entity.locX()) >> 4, MathHelper.floor(entity.locZ()) >> 4) && predicate.test(entity)) {
				list.add(entity);
			}
		}

		return list;
	}

	public List<EntityEnderDragon> g() {
		List<EntityEnderDragon> list = Lists.newArrayList();
		ObjectIterator objectiterator = this.entitiesById.values().iterator();

		while (objectiterator.hasNext()) {
			Entity entity = (Entity) objectiterator.next();

			if (entity instanceof EntityEnderDragon && entity.isAlive()) {
				list.add((EntityEnderDragon) entity);
			}
		}

		return list;
	}

	public List<EntityPlayer> a(Predicate<? super EntityPlayer> predicate) {
		List<EntityPlayer> list = Lists.newArrayList();
		Iterator iterator = this.players.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (predicate.test(entityplayer)) {
				list.add(entityplayer);
			}
		}

		return list;
	}

	@Nullable
	public EntityPlayer q_() {
		List<EntityPlayer> list = this.a(EntityLiving::isAlive);

		return list.isEmpty() ? null : list.get(this.random.nextInt(list.size()));
	}

	@Override
	public boolean addEntity(Entity entity) {
		// CraftBukkit start
		return this.addEntity0(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
	}

	@Override
	public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		return this.addEntity0(entity, reason);
		// CraftBukkit end
	}

	public boolean addEntitySerialized(Entity entity) {
		// CraftBukkit start
		return this.addEntitySerialized(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
	}

	public boolean addEntitySerialized(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
		return this.addEntity0(entity, reason);
		// CraftBukkit end
	}

	public void addEntityTeleport(Entity entity) {
		boolean flag = entity.attachedToPlayer;

		entity.attachedToPlayer = true;
		this.addEntitySerialized(entity);
		entity.attachedToPlayer = flag;
		this.chunkCheck(entity);
	}

	public void addPlayerCommand(EntityPlayer entityplayer) {
		this.addPlayer0(entityplayer);
		this.chunkCheck(entityplayer);
	}

	public void addPlayerPortal(EntityPlayer entityplayer) {
		this.addPlayer0(entityplayer);
		this.chunkCheck(entityplayer);
	}

	public void addPlayerJoin(EntityPlayer entityplayer) {
		this.addPlayer0(entityplayer);
	}

	public void addPlayerRespawn(EntityPlayer entityplayer) {
		this.addPlayer0(entityplayer);
	}

	private void addPlayer0(EntityPlayer entityplayer) {
		Entity entity = this.entitiesByUUID.get(entityplayer.getUniqueID());

		if (entity != null) {
			WorldServer.LOGGER.warn("Force-added player with duplicate UUID {}", entityplayer.getUniqueID().toString());
			entity.decouple();
			this.removePlayer((EntityPlayer) entity);
		}

		this.players.add(entityplayer);
		this.everyoneSleeping();
		IChunkAccess ichunkaccess = this.getChunkAt(MathHelper.floor(entityplayer.locX() / 16.0D), MathHelper.floor(entityplayer.locZ() / 16.0D), ChunkStatus.FULL, true);

		if (ichunkaccess instanceof Chunk) {
			ichunkaccess.a(entityplayer);
		}

		this.registerEntity(entityplayer);
	}

	// CraftBukkit start
	private boolean addEntity0(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason) {
		if (entity.spawnReason == null) entity.spawnReason = spawnReason; // Paper
		// Paper start
		if (entity.valid) {
			MinecraftServer.LOGGER.error("Attempted Double World add on " + entity, new Throwable());

			if (DEBUG_ENTITIES) {
				Throwable thr = entity.addedToWorldStack;
				if (thr == null) {
					MinecraftServer.LOGGER.error("Double add entity has no add stacktrace");
				} else {
					MinecraftServer.LOGGER.error("Double add stacktrace: ", thr);
				}
			}
			return true;
		}
		// Paper end
		if (entity.dead) {
			// Paper start
			if (DEBUG_ENTITIES) {
				new Throwable("Tried to add entity " + entity + " but it was marked as removed already").printStackTrace(); // CraftBukkit
				getAddToWorldStackTrace(entity).printStackTrace();
			}
			// Paper end
			// WorldServer.LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityTypes.getName(entity.getEntityType())); // CraftBukkit
			return false;
		} else if (this.isUUIDTaken(entity)) {
			return false;
		} else {
			// Paper start - capture all item additions to the world
			if (captureDrops != null && entity instanceof EntityItem) {
				captureDrops.add((EntityItem) entity);
				return true;
			}
			// Paper end

			if (!CraftEventFactory.doEntityAddEventCalling(this, entity, spawnReason)) {
				return false;
			}
			// CraftBukkit end
			IChunkAccess ichunkaccess = this.getChunkAt(MathHelper.floor(entity.locX() / 16.0D), MathHelper.floor(entity.locZ() / 16.0D), ChunkStatus.FULL, true); // Paper - always load chunks for entity adds

			if (!(ichunkaccess instanceof Chunk)) {
				return false;
			} else {
				ichunkaccess.a(entity);
				this.registerEntity(entity);
				return true;
			}
		}
	}

	public boolean addEntityChunk(Entity entity) {
		if (this.isUUIDTaken(entity)) {
			return false;
		} else {
			this.registerEntity(entity);
			return true;
		}
	}

	private boolean isUUIDTaken(Entity entity) {
		UUID uuid = entity.getUniqueID();
		Entity entity1 = this.c(uuid);

		if (entity1 == null) {
			return false;
		} else {
			// Paper start
			if (entity1.dead) {
				unregisterEntity(entity1); // remove the existing entity
				return false;
			}
			// Paper end
			WorldServer.LOGGER.warn("Trying to add entity with duplicated UUID {}. Existing {}#{}, new: {}#{}", uuid, EntityTypes.getName(entity1.getEntityType()), entity1.getId(), EntityTypes.getName(entity.getEntityType()), entity.getId()); // CraftBukkit // Paper
			// Paper start
			if (DEBUG_ENTITIES && entity.world.paperConfig.duplicateUUIDMode != PaperWorldConfig.DuplicateUUIDMode.NOTHING) {
				if (entity1.addedToWorldStack != null) {
					entity1.addedToWorldStack.printStackTrace();
				}

				getAddToWorldStackTrace(entity).printStackTrace();
			}
			// Paper end
			return true;
		}
	}

	@Nullable
	private Entity c(UUID uuid) {
		Entity entity = this.entitiesByUUID.get(uuid);

		if (entity != null) {
			return entity;
		} else {
			if (this.tickingEntities) {
				Iterator iterator = this.entitiesToAdd.iterator();

				while (iterator.hasNext()) {
					Entity entity1 = (Entity) iterator.next();

					if (entity1.getUniqueID().equals(uuid)) {
						return entity1;
					}
				}
			}

			return null;
		}
	}

	public boolean addAllEntitiesSafely(Entity entity) {
		// CraftBukkit start
		return this.addAllEntitiesSafely(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT);
	}

	public boolean addAllEntitiesSafely(Entity entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason reason) {
		// CraftBukkit end
		if (entity.recursiveStream().anyMatch(this::isUUIDTaken)) {
			return false;
		} else {
			this.addAllEntities(entity, reason); // CraftBukkit
			return true;
		}
	}

	public void unloadChunk(Chunk chunk) {
		// Spigot Start
		for (TileEntity tileentity : chunk.getTileEntities().values()) {
			if (tileentity instanceof net.minecraft.world.IInventory) {
				// Tuinity start - this area looks like it can load chunks, change the behavior
				// chests for example can apply physics to the world
				// so instead we just change the active container and call the event
				for (org.bukkit.entity.HumanEntity h : Lists.newArrayList(((net.minecraft.world.IInventory) tileentity).getViewers())) {
					((org.bukkit.craftbukkit.entity.CraftHumanEntity) h).getHandle().closeUnloadedInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason.UNLOADED); // Paper
				}
			}
			// Tuiniy end
		}
		// Spigot End
		this.tileEntityListUnload.addAll(chunk.getTileEntities().values());
		List[] aentityslice = chunk.getEntitySlices(); // Spigot
		int i = aentityslice.length;

		java.util.List<Entity> toMoveChunks = new ObjectArrayList<>(); // Paper
		for (int j = 0; j < i; ++j) {
			List<Entity> entityslice = aentityslice[j]; // Spigot
			Iterator iterator = entityslice.iterator();

			while (iterator.hasNext()) {
				Entity entity = (Entity) iterator.next();

				if (!(entity instanceof EntityPlayer)) {
					if (false) { // Tuinity
						throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Removing entity while ticking!")));
					}

					// Paper start - move out entities that shouldn't be in this chunk before it unloads
					if (!entity.dead && (int) Math.floor(entity.locX()) >> 4 != chunk.getPos().x || (int) Math.floor(entity.locZ()) >> 4 != chunk.getPos().z) {
						toMoveChunks.add(entity);
						continue;
					}
					// Paper end

					this.entitiesById.remove(entity.getId());
					this.unregisterEntity(entity);

					if (entity.dead) iterator.remove(); // Paper - don't save dead entities during unload
				}
			}
		}
		// Paper start - move out entities that shouldn't be in this chunk before it unloads
		for (Entity entity : toMoveChunks) {
			this.chunkCheck(entity);
		}
		// Paper end

	}

	public void unregisterEntity(Entity entity) {
		this.entitiesForIteration.remove(entity); // Tuinity
		// Paper start - fix entity registration issues
		if (entity instanceof EntityComplexPart) {
			// Usually this is a no-op for complex parts, and ID's should be removed, but go ahead and remove it anyways
			// Dragon parts are handled special in register. they don't receive a valid = true or register by UUID etc.
			this.entitiesById.remove(entity.getId(), entity);
			return;
		}
		if (!entity.valid) {
			// Someone called remove before we ever got added, cancel the add.
			entity.isQueuedForRegister = false;
			return;
		}
		// Paper end
		// Spigot start
		if (entity instanceof EntityHuman) {
			this.getMinecraftServer().worldServer.values().stream().map(WorldServer::getWorldPersistentData).forEach((worldData) ->
			{
				for (Object o : worldData.data.values()) {
					if (o instanceof WorldMap map) {
						map.humans.remove((EntityHuman) entity);
						for (Iterator<WorldMap.WorldMapHumanTracker> iter = map.i.iterator(); iter.hasNext(); ) {
							if (iter.next().trackee == entity) {
								map.decorations.remove(entity.getDisplayName().getString()); // Paper
								iter.remove();
							}
						}
					}
				}
			});
		}
		// Spigot end
		// Spigot Start
		if (entity.getBukkitEntity() instanceof org.bukkit.inventory.InventoryHolder) {
			for (org.bukkit.entity.HumanEntity h : Lists.newArrayList(((org.bukkit.inventory.InventoryHolder) entity.getBukkitEntity()).getInventory().getViewers())) {
				h.closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason.UNLOADED); // Paper
			}
		}
		// Spigot End
		if (entity instanceof EntityEnderDragon) {
			EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity).eJ();
			int i = aentitycomplexpart.length;

			for (int j = 0; j < i; ++j) {
				EntityComplexPart entitycomplexpart = aentitycomplexpart[j];

				entitycomplexpart.die();
			}
		}

		this.entitiesByUUID.remove(entity.getUniqueID());
		this.getChunkProvider().removeEntity(entity);
		if (entity instanceof EntityPlayer entityplayer) {

			this.players.remove(entityplayer);
		}

		this.getScoreboard().a(entity);
		// CraftBukkit start - SPIGOT-5278
		if (entity instanceof EntityDrowned) {
			// Tuinity start
			this.navigators.remove(((EntityDrowned) entity).navigationWater);
			this.navigatorsForIteration.remove(((EntityDrowned) entity).navigationWater);
			this.navigators.remove(((EntityDrowned) entity).navigationLand);
			this.navigatorsForIteration.remove(((EntityDrowned) entity).navigationLand);
			// Tuinity end
		} else
			// CraftBukkit end
			if (entity instanceof EntityInsentient) {
				// Tuinity start
				this.navigators.remove(((EntityInsentient) entity).getNavigation());
				this.navigatorsForIteration.remove(((EntityInsentient) entity).getNavigation());
				// Tuinity end
			}
		new com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
		this.removeNavigatorsFromData(entity); // Tuinity - optimise notify()
		entity.valid = false; // CraftBukkit
	}

	// Tuinity start - optimise notify()
	void removeNavigatorsIfNotPathingFromRegion(Entity entity) {
		com.tuinity.tuinity.chunk.SingleThreadChunkRegionManager.RegionSection section =
			this.getChunkProvider().playerChunkMap.dataRegionManager.getRegionSection(entity.chunkX, entity.chunkZ);
		if (section != null) {
			PlayerChunkMap.DataRegionSectionData sectionData = (PlayerChunkMap.DataRegionSectionData) section.sectionData;
			if (entity instanceof EntityInsentient) {
				if (!((EntityInsentient) entity).getNavigation().isViableForPathRecalculationChecking()) {
					sectionData.removeFromNavigators(section, (EntityInsentient) entity);
				}
			}
		}
	}

	void removeNavigatorsFromData(Entity entity) {
		com.tuinity.tuinity.chunk.SingleThreadChunkRegionManager.RegionSection section =
			this.getChunkProvider().playerChunkMap.dataRegionManager.getRegionSection(entity.chunkX, entity.chunkZ);
		if (section != null) {
			PlayerChunkMap.DataRegionSectionData sectionData = (PlayerChunkMap.DataRegionSectionData) section.sectionData;
			if (entity instanceof EntityInsentient) {
				sectionData.removeFromNavigators(section, ((EntityInsentient) entity));
			}
		}
	}

	void addNavigatorsIfPathingToRegion(Entity entity) {
		com.tuinity.tuinity.chunk.SingleThreadChunkRegionManager.RegionSection section =
			this.getChunkProvider().playerChunkMap.dataRegionManager.getRegionSection(entity.chunkX, entity.chunkZ);
		if (section != null) {
			PlayerChunkMap.DataRegionSectionData sectionData = (PlayerChunkMap.DataRegionSectionData) section.sectionData;
			if (entity instanceof EntityInsentient) {
				if (((EntityInsentient) entity).getNavigation().isViableForPathRecalculationChecking()) {
					sectionData.addToNavigators(section, ((EntityInsentient) entity));
				}
			}
		}
	}

	void updateNavigatorsInRegion(Entity entity) {
		com.tuinity.tuinity.chunk.SingleThreadChunkRegionManager.RegionSection section =
			this.getChunkProvider().playerChunkMap.dataRegionManager.getRegionSection(entity.chunkX, entity.chunkZ);
		if (section != null) {
			PlayerChunkMap.DataRegionSectionData sectionData = (PlayerChunkMap.DataRegionSectionData) section.sectionData;
			if (entity instanceof EntityInsentient) {
				if (((EntityInsentient) entity).getNavigation().isViableForPathRecalculationChecking()) {
					sectionData.addToNavigators(section, ((EntityInsentient) entity));
				} else {
					sectionData.removeFromNavigators(section, ((EntityInsentient) entity));
				}
			}
		}
	}
	// Tuinity end - optimise notify()

	private void registerEntity(Entity entity) {
		// Paper start - don't double enqueue entity registration
		//noinspection ObjectEquality
		if (this.entitiesById.get(entity.getId()) == entity) {
			LOGGER.error(entity + " was already registered!");
			new Throwable().printStackTrace();
			return;
		}
		// Paper end
		if (false) { // Tuinity
			if (!entity.isQueuedForRegister) { // Paper
				this.entitiesToAdd.add(entity);
				entity.isQueuedForRegister = true; // Paper
			}
		} else {
			entity.isQueuedForRegister = false; // Paper
			this.entitiesById.put(entity.getId(), entity);
			this.entitiesForIteration.add(entity); // Tuinity
			if (entity instanceof EntityEnderDragon) {
				EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity).eJ();
				int i = aentitycomplexpart.length;

				for (int j = 0; j < i; ++j) {
					EntityComplexPart entitycomplexpart = aentitycomplexpart[j];

					this.entitiesById.put(entitycomplexpart.getId(), entitycomplexpart);
					this.entitiesForIteration.add(entitycomplexpart); // Tuinity
				}
			}

			if (DEBUG_ENTITIES) {
				entity.addedToWorldStack = getAddToWorldStackTrace(entity);
			}

			Entity old = this.entitiesByUUID.put(entity.getUniqueID(), entity);
			if (old != null && old.getId() != entity.getId() && old.valid) {
				Logger logger = LogManager.getLogger();
				logger.error("Overwrote an existing entity " + old + " with " + entity);
				if (DEBUG_ENTITIES) {
					if (old.addedToWorldStack != null) {
						old.addedToWorldStack.printStackTrace();
					} else {
						logger.error("Oddly, the old entity was not added to the world in the normal way. Plugins?");
					}
					entity.addedToWorldStack.printStackTrace();
				}
			}

			// this.getChunkProvider().addEntity(entity); // Paper - moved down below valid=true
			// CraftBukkit start - SPIGOT-5278
			if (entity instanceof EntityDrowned) {
				// Tuinity start
				this.navigators.add(((EntityDrowned) entity).navigationWater);
				this.navigatorsForIteration.add(((EntityDrowned) entity).navigationWater);
				this.navigators.add(((EntityDrowned) entity).navigationLand);
				this.navigatorsForIteration.add(((EntityDrowned) entity).navigationLand);
				// Tuinity end
			} else
				// CraftBukkit end
				if (entity instanceof EntityInsentient) {
					// Tuinity start
					this.navigators.add(((EntityInsentient) entity).getNavigation());
					this.navigatorsForIteration.add(((EntityInsentient) entity).getNavigation());
					// Tuinity end
				}
			entity.valid = true; // CraftBukkit
			this.getChunkProvider().addEntity(entity); // Paper - from above to be below valid=true
			// Paper start - Set origin location when the entity is being added to the world
			if (entity.origin == null) {
				entity.origin = entity.getBukkitEntity().getLocation();
			}
			// Paper end
			entity.shouldBeRemoved = false; // Paper - shouldn't be removed after being re-added
			new com.destroystokyo.paper.event.entity.EntityAddToWorldEvent(entity.getBukkitEntity()).callEvent(); // Paper - fire while valid
		}

	}

	public void removeEntity(Entity entity) {
		if (false) {  // Tuinity
			throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Removing entity while ticking!")));
		} else {
			this.removeEntityFromChunk(entity);
			this.entitiesById.remove(entity.getId());
			this.unregisterEntity(entity);
			entity.shouldBeRemoved = true; // Paper
		}
	}

	private void removeEntityFromChunk(Entity entity) {
		Chunk ichunkaccess = entity.getCurrentChunk(); // Paper - getChunkAt(x,z,full,false) is broken by CraftBukkit as it won't return an unloading chunk. Use our current chunk reference as this points to what chunk they need to be removed from anyways

		if (ichunkaccess != null) { // Paper
			ichunkaccess.b(entity);
		}

	}

	public void removePlayer(EntityPlayer entityplayer) {
		entityplayer.die();
		this.removeEntity(entityplayer);
		this.everyoneSleeping();
	}

	// CraftBukkit start
	public boolean strikeLightning(Entity entitylightning) {
		return this.strikeLightning(entitylightning, LightningStrikeEvent.Cause.UNKNOWN);
	}

	public boolean strikeLightning(Entity entitylightning, LightningStrikeEvent.Cause cause) {
		LightningStrikeEvent lightning = CraftEventFactory.callLightningStrikeEvent((org.bukkit.entity.LightningStrike) entitylightning.getBukkitEntity(), cause);

		if (lightning.isCancelled()) {
			return false;
		}

		return this.addEntity(entitylightning);
	}
	// CraftBukkit end

	@Override
	public void a(int i, BlockPosition blockposition, int j) {
		ByteBuf packet = null;

		try {
			// CraftBukkit start
			EntityHuman entityhuman = null;
			Entity entity = this.getEntity(i);
			if (entity instanceof EntityHuman) entityhuman = (EntityHuman) entity;
			// CraftBukkit end

			var bp_x = (double) blockposition.getX();
			var bp_y = (double) blockposition.getY();
			var bp_z = (double) blockposition.getZ();

			var players = this.server.getPlayerList().getPlayers();
			var playersSize = players.size();
			var count = 0;
			for (var players_i = 0; players_i < playersSize; players_i++) {
				EntityPlayer entityplayer = players.get(players_i);

				if (entityplayer != null && entityplayer.world == this && entityplayer.getId() != i) {
					double d0 = bp_x - entityplayer.locX();
					double d1 = bp_y - entityplayer.locY();
					double d2 = bp_z - entityplayer.locZ();

					// CraftBukkit start
					if (entityhuman != null && entityhuman instanceof EntityPlayer && !entityplayer.getBukkitEntity().canSee(((EntityPlayer) entityhuman).getBukkitEntity())) {
						continue;
					}
					// CraftBukkit end

					if (d0 * d0 + d1 * d1 + d2 * d2 < 256.0) {
						if (count++ > 8) break;
						if (packet == null) {
							packet = PacketUtils.bakePacket(
								entityplayer.playerConnection.networkManager.channel.alloc(),
								EnumProtocol.PLAY,
								new PacketPlayOutBlockBreakAnimation(i, blockposition, j)
							);
						}
						entityplayer.playerConnection.sendRawPacket(packet.retain());
					}
				}
			}
		} finally {
			if (packet != null) {
				packet.release();
			}
		}
	}

	@Override
	public void playSound(@Nullable EntityHuman entityhuman, double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
		this.server.getPlayerList().sendPacketNearby(entityhuman, d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D, this.getDimensionKey(), new PacketPlayOutNamedSoundEffect(soundeffect, soundcategory, d0, d1, d2, f, f1));
	}

	@Override
	public void playSound(@Nullable EntityHuman entityhuman, Entity entity, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
		this.server.getPlayerList().sendPacketNearby(entityhuman, entity.locX(), entity.locY(), entity.locZ(), f > 1.0F ? (double) (16.0F * f) : 16.0D, this.getDimensionKey(), new PacketPlayOutEntitySound(soundeffect, soundcategory, entity, f, f1));
	}

	@Override
	public void b(int i, BlockPosition blockposition, int j) {
		this.server.getPlayerList().sendAll(new PacketPlayOutWorldEvent(i, blockposition, j, true));
	}

	@Override
	public void a(@Nullable EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
		this.server.getPlayerList().sendPacketNearby(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 64.0D, this.getDimensionKey(), new PacketPlayOutWorldEvent(i, blockposition, j, false));
	}

	@Override
	public void notify(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, int i) {
		this.getChunkProvider().flagDirty(blockposition);
		if (this.paperConfig.updatePathfindingOnBlockUpdate) { // Paper - option to disable pathfinding updates
			VoxelShape voxelshape = iblockdata.getCollisionShape(this, blockposition);
			VoxelShape voxelshape1 = iblockdata1.getCollisionShape(this, blockposition);

			if (VoxelShapes.c(voxelshape, voxelshape1, OperatorBoolean.NOT_SAME)) {
				// Tuinity start - optimise notify()
				com.tuinity.tuinity.chunk.SingleThreadChunkRegionManager.Region region = this.getChunkProvider().playerChunkMap.dataRegionManager.getRegion(blockposition.getX() >> 4, blockposition.getZ() >> 4);
				if (region == null) {
					return;
				}
				com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet<EntityInsentient> navigatorsFromRegion = ((PlayerChunkMap.DataRegionData) region.regionData).getNavigators();
				if (navigatorsFromRegion == null) {
					return;
				}
				boolean wasTicking = this.tickingEntities;
				this.tickingEntities = true; // Paper
				// Tuinity start
				com.tuinity.tuinity.util.maplist.IteratorSafeOrderedReferenceSet.Iterator<EntityInsentient> iterator = navigatorsFromRegion.iterator();
				// Tuinity end - optimise notify()
				try { // Tuinity end

					while (iterator.hasNext()) {
						// CraftBukkit start - fix SPIGOT-6362
						NavigationAbstract navigationabstract;
						try {
							navigationabstract = iterator.next().getNavigation(); // Tuinity - optimise notify
						} catch (java.util.ConcurrentModificationException ex) {
							// This can happen because the pathfinder update below may trigger a chunk load, which in turn may cause more navigators to register
							// In this case we just run the update again across all the iterators as the chunk will then be loaded
							// As this is a relative edge case it is much faster than copying navigators (on either read or write)
							notify(blockposition, iblockdata, iblockdata1, i);
							return;
						}
						// CraftBukkit end

						if (!navigationabstract.i()) {
							navigationabstract.b(blockposition);
						}
					}
				} finally { // Tuinity start
					iterator.finishedIterating();
				} // Tuinity end

				this.tickingEntities = wasTicking; // Paper
			}
		} // Paper
	}

	@Override
	public void broadcastEntityEffect(Entity entity, byte b0) {
		this.getChunkProvider().broadcastIncludingSelf(entity, new PacketPlayOutEntityStatus(entity, b0));
	}

	@Override
	public ChunkProviderServer getChunkProvider() {
		return this.chunkProvider;
	}

	@Override
	public Explosion createExplosion(@Nullable Entity entity, @Nullable DamageSource damagesource, @Nullable ExplosionDamageCalculator explosiondamagecalculator, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
		// CraftBukkit start
		Explosion explosion = super.createExplosion(entity, damagesource, explosiondamagecalculator, d0, d1, d2, f, flag, explosion_effect);

		if (explosion.wasCanceled) {
			return explosion;
		}

        /* Remove
        Explosion explosion = new Explosion(this, entity, damagesource, explosiondamagecalculator, d0, d1, d2, f, flag, explosion_effect);

        explosion.a();
        explosion.a(false);
        */
		// CraftBukkit end - TODO: Check if explosions are still properly implemented
		if (explosion_effect == Explosion.Effect.NONE) {
			explosion.clearBlocks();
		}

		Iterator iterator = this.players.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.h(d0, d1, d2) < 4096.0D) {
				entityplayer.playerConnection.sendPacket(new PacketPlayOutExplosion(d0, d1, d2, f, explosion.getBlocks(), explosion.c().get(entityplayer)));
			}
		}

		return explosion;
	}

	@Override
	public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
		this.L.add(new BlockActionData(blockposition, block, i, j));
	}

	private void ak() {
		while (!this.L.isEmpty()) {
			BlockActionData blockactiondata = this.L.removeFirst();

			if (this.a(blockactiondata)) {
				this.server.getPlayerList().sendPacketNearby(null, blockactiondata.a().getX(), blockactiondata.a().getY(), blockactiondata.a().getZ(), 64.0D, this.getDimensionKey(), new PacketPlayOutBlockAction(blockactiondata.a(), blockactiondata.b(), blockactiondata.c(), blockactiondata.d()));
			}
		}

	}

	private boolean a(BlockActionData blockactiondata) {
		IBlockData iblockdata = this.getType(blockactiondata.a());

		return iblockdata.a(blockactiondata.b()) && iblockdata.a(this, blockactiondata.a(), blockactiondata.c(), blockactiondata.d());
	}

	@Override
	public TickListServer<Block> getBlockTickList() {
		return this.nextTickListBlock;
	}

	@Override
	public TickListServer<FluidType> getFluidTickList() {
		return this.nextTickListFluid;
	}

	@Nonnull
	@Override
	public MinecraftServer getMinecraftServer() {
		return this.server;
	}

	public PortalTravelAgent getTravelAgent() {
		return this.portalTravelAgent;
	}

	public DefinedStructureManager n() {
		return this.server.getDefinedStructureManager();
	}

	public <T extends ParticleParam> int a(T t0, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6) {
		// CraftBukkit - visibility api support
		return sendParticles(null, t0, d0, d1, d2, i, d3, d4, d5, d6, false);
	}

	public <T extends ParticleParam> int sendParticles(EntityPlayer sender, T t0, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, boolean force) {
		// Paper start - Particle API Expansion
		return sendParticles(players, sender, t0, d0, d1, d2, i, d3, d4, d5, d6, force);
	}

	public <T extends ParticleParam> int sendParticles(List<EntityPlayer> receivers, EntityPlayer sender, T t0, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, boolean force) {
		// Paper end
		PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(t0, force, d0, d1, d2, (float) d3, (float) d4, (float) d5, (float) d6, i);
		ByteBuf baked = null;
		try {
			int j = 0;

			for (EntityHuman entityhuman : receivers) {
				if (!(entityhuman instanceof EntityPlayer player)) continue;
				if (sender != null && !player.getBukkitEntity().canSee(sender.getBukkitEntity())) continue; // CraftBukkit

				if (baked == null) {
					baked = PacketUtils.bakePacket(
						player.networkManager.channel.alloc(),
						EnumProtocol.PLAY,
						packetplayoutworldparticles
					);
				}

				if (this.a(player, force, d0, d1, d2, baked)) { // CraftBukkit
					++j;
				}
			}

			return j;
		} finally {
			if (baked != null) {
				baked.release();
			}
		}
	}

	public <T extends ParticleParam> boolean a(EntityPlayer entityplayer, T t0, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6) {
		Packet<?> packet = new PacketPlayOutWorldParticles(t0, flag, d0, d1, d2, (float) d3, (float) d4, (float) d5, (float) d6, i);
		var baked = PacketUtils.bakePacket(entityplayer.networkManager.channel.alloc(), EnumProtocol.PLAY, packet);
		try {
			return this.a(entityplayer, flag, d0, d1, d2, baked);
		} finally {
			baked.release();
		}
	}

	private boolean a(EntityPlayer entityplayer, boolean flag, double d0, double d1, double d2, ByteBuf bakedPacket) {
		if (entityplayer.getWorldServer() != this) {
			return false;
		} else {
			BlockPosition blockposition = entityplayer.getChunkCoordinates();

			if (blockposition.a(new Vec3D(d0, d1, d2), flag ? 512.0D : 32.0D)) {
				entityplayer.playerConnection.sendRawPacket(bakedPacket.retain());
				return true;
			} else {
				return false;
			}
		}
	}

	@Nullable
	@Override
	public Entity getEntity(int i) {
		return this.entitiesById.get(i);
	}

	@Nullable
	public Entity getEntity(UUID uuid) {
		return this.entitiesByUUID.get(uuid);
	}

	@Nullable
	public BlockPosition a(StructureGenerator<?> structuregenerator, BlockPosition blockposition, int i, boolean flag) {
		return !this.worldDataServer.getGeneratorSettings().shouldGenerateMapFeatures() ? null : this.getChunkProvider().getChunkGenerator().findNearestMapFeature(this, structuregenerator, blockposition, i, flag); // CraftBukkit
	}

	public BlockPosition getNearestBiome(BiomeBase biomeBase, BlockPosition blockPosition, int radius, int step) {
		return this.a(biomeBase, blockPosition, radius, step);
	} // Paper - OBFHELPER

	@Nullable
	public BlockPosition a(BiomeBase biomebase, BlockPosition blockposition, int i, int j) {
		return this.getChunkProvider().getChunkGenerator().getWorldChunkManager().a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), i, j, (biomebase1) -> {
			return biomebase1 == biomebase;
		}, this.random, true);
	}

	@Override
	public CraftingManager getCraftingManager() {
		return this.server.getCraftingManager();
	}

	@Override
	public ITagRegistry p() {
		return this.server.getTagRegistry();
	}

	@Override
	public boolean isSavingDisabled() {
		return this.savingDisabled;
	}

	public IRegistryCustom getWorldCustomRegistry() {
		return r();
	} // Paper - OBFHELPER

	@Override
	public IRegistryCustom r() {
		return this.server.getCustomRegistry();
	}

	public WorldPersistentData getWorldPersistentData() {
		return this.getChunkProvider().getWorldPersistentData();
	}

	@Nullable
	@Override
	public WorldMap a(String s) {
		return this.getMinecraftServer().E().getWorldPersistentData().b(() -> {
			// CraftBukkit start
			// We only get here when the data file exists, but is not a valid map
			WorldMap newMap = new WorldMap(s);
			MapInitializeEvent event = new MapInitializeEvent(newMap.mapView);
			Bukkit.getServer().getPluginManager().callEvent(event);
			return newMap;
			// CraftBukkit end
		}, s);
	}

	@Override
	public void a(WorldMap worldmap) {
		this.getMinecraftServer().E().getWorldPersistentData().a(worldmap);
	}

	@Override
	public int getWorldMapCount() {
		return this.getMinecraftServer().E().getWorldPersistentData().a(PersistentIdCounts::new, "idcounts").a();
	}

	// Paper start - helper function for configurable spawn radius
	public void addTicketsForSpawn(int radiusInBlocks, BlockPosition spawn) {
		// In order to respect vanilla behavior, which is ensuring everything but the spawn border can tick, we add tickets
		// with level 31 for the non-border spawn chunks
		ChunkProviderServer chunkproviderserver = this.getChunkProvider();
		int tickRadius = radiusInBlocks - 16;

		// add ticking chunks
		for (int x = -tickRadius; x <= tickRadius; x += 16) {
			for (int z = -tickRadius; z <= tickRadius; z += 16) {
				// radius of 2 will have the current chunk be level 31
				chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, z)), 2, Unit.INSTANCE);
			}
		}

		// add border chunks

		// add border along x axis (including corner chunks)
		for (int x = -radiusInBlocks; x <= radiusInBlocks; x += 16) {
			// top
			chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, radiusInBlocks)), 1, Unit.INSTANCE); // level 32
			// bottom
			chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, -radiusInBlocks)), 1, Unit.INSTANCE); // level 32
		}

		// add border along z axis (excluding corner chunks)
		for (int z = -radiusInBlocks + 16; z < radiusInBlocks; z += 16) {
			// right
			chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(radiusInBlocks, 0, z)), 1, Unit.INSTANCE); // level 32
			// left
			chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(-radiusInBlocks, 0, z)), 1, Unit.INSTANCE); // level 32
		}

		MCUtil.getSpiralOutChunks(spawn, radiusInBlocks >> 4).forEach(pair -> {
			getChunkProvider().getChunkAtAsynchronously(pair.x, pair.z, true, false).exceptionally((ex) -> {
				ex.printStackTrace();
				return null;
			});
		});
	}

	public void removeTicketsForSpawn(int radiusInBlocks, BlockPosition spawn) {
		// In order to respect vanilla behavior, which is ensuring everything but the spawn border can tick, we added tickets
		// with level 31 for the non-border spawn chunks
		ChunkProviderServer chunkproviderserver = this.getChunkProvider();
		int tickRadius = radiusInBlocks - 16;

		// remove ticking chunks
		for (int x = -tickRadius; x <= tickRadius; x += 16) {
			for (int z = -tickRadius; z <= tickRadius; z += 16) {
				// radius of 2 will have the current chunk be level 31
				chunkproviderserver.removeTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, z)), 2, Unit.INSTANCE);
			}
		}

		// remove border chunks

		// remove border along x axis (including corner chunks)
		for (int x = -radiusInBlocks; x <= radiusInBlocks; x += 16) {
			// top
			chunkproviderserver.removeTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, radiusInBlocks)), 1, Unit.INSTANCE); // level 32
			// bottom
			chunkproviderserver.removeTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(x, 0, -radiusInBlocks)), 1, Unit.INSTANCE); // level 32
		}

		// remove border along z axis (excluding corner chunks)
		for (int z = -radiusInBlocks + 16; z < radiusInBlocks; z += 16) {
			// right
			chunkproviderserver.removeTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(radiusInBlocks, 0, z)), 1, Unit.INSTANCE); // level 32
			// left
			chunkproviderserver.removeTicket(TicketType.START, new ChunkCoordIntPair(spawn.add(-radiusInBlocks, 0, z)), 1, Unit.INSTANCE); // level 32
		}
	}
	// Paper end

	public final void setSpawn(BlockPosition blockposition, float f) {
		this.a(blockposition, f);
	} // Paper - OBFHELPER

	public void a(BlockPosition blockposition, float f) {
		// Paper - configurable spawn radius
		BlockPosition prevSpawn = this.getSpawn();
		//ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(new BlockPosition(this.worldData.a(), 0, this.worldData.c()));

		this.worldData.setSpawn(blockposition, f);
		new org.bukkit.event.world.SpawnChangeEvent(getWorld(), MCUtil.toLocation(this, prevSpawn)).callEvent(); // Paper
		if (this.keepSpawnInMemory) {
			// if this keepSpawnInMemory is false a plugin has already removed our tickets, do not re-add
			this.removeTicketsForSpawn(this.paperConfig.keepLoadedRange, prevSpawn);
			this.addTicketsForSpawn(this.paperConfig.keepLoadedRange, blockposition);
		}
		this.getMinecraftServer().getPlayerList().sendAll(new PacketPlayOutSpawnPosition(blockposition, f));
	}

	// Paper - moved up to World
	//public BlockPosition getSpawn() {
	//    BlockPosition blockposition = new BlockPosition(this.worldData.a(), this.worldData.b(), this.worldData.c());
	//
	//    if (!this.getWorldBorder().a(blockposition)) {
	//        blockposition = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, new BlockPosition(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
	//    }
	//
	//    return blockposition;
	//}
	// Paper end

	public float v() {
		return this.worldData.d();
	}

	public LongSet getForceLoadedChunks() {
		ForcedChunk forcedchunk = this.getWorldPersistentData().b(ForcedChunk::new, "chunks");

		return forcedchunk != null ? LongSets.unmodifiable(forcedchunk.a()) : LongSets.EMPTY_SET;
	}

	public boolean setForceLoaded(int i, int j, boolean flag) {
		ForcedChunk forcedchunk = this.getWorldPersistentData().a(ForcedChunk::new, "chunks");
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
		long k = chunkcoordintpair.pair();
		boolean flag1;

		if (flag) {
			flag1 = forcedchunk.a().add(k);
			if (flag1) {
				this.getChunkAt(i, j);
			}
		} else {
			flag1 = forcedchunk.a().remove(k);
		}

		forcedchunk.a(flag1);
		if (flag1) {
			this.getChunkProvider().a(chunkcoordintpair, flag);
		}

		return flag1;
	}

	@Override
	public List<EntityPlayer> getPlayers() {
		return this.players;
	}

	@Override
	public void a(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1) {
		Optional<VillagePlaceType> optional = VillagePlaceType.b(iblockdata);
		Optional<VillagePlaceType> optional1 = VillagePlaceType.b(iblockdata1);

		if (!Objects.equals(optional, optional1)) {
			BlockPosition blockposition1 = blockposition.immutableCopy();

			optional.ifPresent((villageplacetype) -> {
				this.getMinecraftServer().execute(() -> {
					this.y().a(blockposition1);
					PacketDebug.b(this, blockposition1);
				});
			});
			optional1.ifPresent((villageplacetype) -> {
				this.getMinecraftServer().execute(() -> {
					// Paper start
					if (!optional.isPresent() && this.getPoiStorage().test(blockposition1, com.google.common.base.Predicates.alwaysTrue())) {
						this.getPoiStorage().remove(blockposition1);
					}
					// Paper end
					this.y().a(blockposition1, villageplacetype);
					PacketDebug.a(this, blockposition1);
				});
			});
		}
	}

	public final VillagePlace getPoiStorage() {
		return this.y();
	} // Paper - OBFHELPER

	public VillagePlace y() {
		return this.getChunkProvider().j();
	}

	public boolean a_(BlockPosition blockposition) {
		return this.a(blockposition, 1);
	}

	public boolean a(SectionPosition sectionposition) {
		return this.a_(sectionposition.q());
	}

	public boolean a(BlockPosition blockposition, int i) {
		return i <= 6 && this.b(SectionPosition.a(blockposition)) <= i;
	}

	public int b(SectionPosition sectionposition) {
		return this.y().a(sectionposition);
	}

	public PersistentRaid getPersistentRaid() {
		return this.persistentRaid;
	}

	@Nullable
	public Raid b_(BlockPosition blockposition) {
		return this.persistentRaid.getNearbyRaid(blockposition, 9216);
	}

	public boolean c_(BlockPosition blockposition) {
		return this.b_(blockposition) != null;
	}

	public void a(ReputationEvent reputationevent, Entity entity, ReputationHandler reputationhandler) {
		reputationhandler.a(reputationevent, entity);
	}

	public void a(java.nio.file.Path java_nio_file_path) throws IOException {
		PlayerChunkMap playerchunkmap = this.getChunkProvider().playerChunkMap;
		BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path.resolve("stats.txt"));
		Throwable throwable = null;

		try {
			bufferedwriter.write(String.format("spawning_chunks: %d\n", playerchunkmap.e().b()));
			SpawnerCreature.d spawnercreature_d = this.getChunkProvider().k();

			if (spawnercreature_d != null) {
				ObjectIterator objectiterator = spawnercreature_d.b().object2IntEntrySet().iterator();

				while (objectiterator.hasNext()) {
					Object2IntMap.Entry<EnumCreatureType> it_unimi_dsi_fastutil_objects_object2intmap_entry = (Object2IntMap.Entry) objectiterator.next(); // Paper - decompile fix

					bufferedwriter.write(String.format("spawn_count.%s: %d\n", it_unimi_dsi_fastutil_objects_object2intmap_entry.getKey().b(), it_unimi_dsi_fastutil_objects_object2intmap_entry.getIntValue()));
				}
			}

			bufferedwriter.write(String.format("entities: %d\n", this.entitiesById.size()));
			bufferedwriter.write(String.format("block_entities: %d\n", this.tileEntityListTick.size())); // Paper - remove unused list
			bufferedwriter.write(String.format("block_ticks: %d\n", this.getBlockTickList().a()));
			bufferedwriter.write(String.format("fluid_ticks: %d\n", this.getFluidTickList().a()));
			bufferedwriter.write("distance_manager: " + playerchunkmap.e().c() + "\n");
			bufferedwriter.write(String.format("pending_tasks: %d\n", this.getChunkProvider().f()));
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

		CrashReport crashreport = new CrashReport("Level dump", new Exception("dummy"));

		this.a(crashreport);
		BufferedWriter bufferedwriter1 = Files.newBufferedWriter(java_nio_file_path.resolve("example_crash.txt"));
		Throwable throwable3 = null;

		try {
			bufferedwriter1.write(crashreport.e());
		} catch (Throwable throwable4) {
			throwable3 = throwable4;
			throw throwable4;
		} finally {
			if (bufferedwriter1 != null) {
				if (throwable3 != null) {
					try {
						bufferedwriter1.close();
					} catch (Throwable throwable5) {
						throwable3.addSuppressed(throwable5);
					}
				} else {
					bufferedwriter1.close();
				}
			}

		}

		java.nio.file.Path java_nio_file_path1 = java_nio_file_path.resolve("chunks.csv");
		BufferedWriter bufferedwriter2 = Files.newBufferedWriter(java_nio_file_path1);
		Throwable throwable6 = null;

		try {
			playerchunkmap.a(bufferedwriter2);
		} catch (Throwable throwable7) {
			throwable6 = throwable7;
			throw throwable7;
		} finally {
			if (bufferedwriter2 != null) {
				if (throwable6 != null) {
					try {
						bufferedwriter2.close();
					} catch (Throwable throwable8) {
						throwable6.addSuppressed(throwable8);
					}
				} else {
					bufferedwriter2.close();
				}
			}

		}

		java.nio.file.Path java_nio_file_path2 = java_nio_file_path.resolve("entities.csv");
		BufferedWriter bufferedwriter3 = Files.newBufferedWriter(java_nio_file_path2);
		Throwable throwable9 = null;

		try {
			a(bufferedwriter3, this.entitiesById.values());
		} catch (Throwable throwable10) {
			throwable9 = throwable10;
			throw throwable10;
		} finally {
			if (bufferedwriter3 != null) {
				if (throwable9 != null) {
					try {
						bufferedwriter3.close();
					} catch (Throwable throwable11) {
						throwable9.addSuppressed(throwable11);
					}
				} else {
					bufferedwriter3.close();
				}
			}

		}

		java.nio.file.Path java_nio_file_path3 = java_nio_file_path.resolve("block_entities.csv");
		BufferedWriter bufferedwriter4 = Files.newBufferedWriter(java_nio_file_path3);
		Throwable throwable12 = null;

		try {
			this.a(bufferedwriter4);
		} catch (Throwable throwable13) {
			throwable12 = throwable13;
			throw throwable13;
		} finally {
			if (bufferedwriter4 != null) {
				if (throwable12 != null) {
					try {
						bufferedwriter4.close();
					} catch (Throwable throwable14) {
						throwable12.addSuppressed(throwable14);
					}
				} else {
					bufferedwriter4.close();
				}
			}

		}

	}

	private static void a(Writer writer, Iterable<Entity> iterable) throws IOException {
		CSVWriter csvwriter = CSVWriter.a().a("x").a("y").a("z").a("uuid").a("type").a("alive").a("display_name").a("custom_name").a(writer);
		Iterator iterator = iterable.iterator();

		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			IChatBaseComponent ichatbasecomponent = entity.getCustomName();
			IChatBaseComponent ichatbasecomponent1 = entity.getScoreboardDisplayName();

			csvwriter.a(entity.locX(), entity.locY(), entity.locZ(), entity.getUniqueID(), IRegistry.ENTITY_TYPE.getKey(entity.getEntityType()), entity.isAlive(), ichatbasecomponent1.getString(), ichatbasecomponent != null ? ichatbasecomponent.getString() : null);
		}

	}

	private void a(Writer writer) throws IOException {
		CSVWriter csvwriter = CSVWriter.a().a("x").a("y").a("z").a("type").a(writer);
		Iterator iterator = this.tileEntityListTick.iterator(); // Paper - remove unused list

		while (iterator.hasNext()) {
			TileEntity tileentity = (TileEntity) iterator.next();
			BlockPosition blockposition = tileentity.getPosition();

			csvwriter.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), IRegistry.BLOCK_ENTITY_TYPE.getKey(tileentity.getTileType()));
		}

	}

	@VisibleForTesting
	public void a(StructureBoundingBox structureboundingbox) {
		this.L.removeIf((blockactiondata) -> {
			return structureboundingbox.b(blockactiondata.a());
		});
	}

	@Override
	public void update(BlockPosition blockposition, Block block) {
		if (!this.isDebugWorld()) {
			// CraftBukkit start
			if (populating) {
				return;
			}
			// CraftBukkit end
			this.applyPhysics(blockposition, block);
		}

	}

	public Iterable<Entity> A() {
		return Iterables.unmodifiableIterable(this.entitiesById.values());
	}

	public String toString() {
		return "ServerLevel[" + this.worldDataServer.getName() + "]";
	}

	public boolean isFlatWorld() {
		return this.worldDataServer.getGeneratorSettings().isFlatWorld(); // CraftBukkit
	}

	@Override
	public long getSeed() {
		return this.worldDataServer.getGeneratorSettings().getSeed(); // CraftBukkit
	}

	@Nullable
	public EnderDragonBattle getDragonBattle() {
		return this.dragonBattle;
	}

	@Override
	public Stream<? extends StructureStart<?>> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator) {
		return this.getStructureManager().a(sectionposition, structuregenerator);
	}

	@Override
	public WorldServer getMinecraftWorld() {
		return this;
	}

	@VisibleForTesting
	public String F() {
		return String.format("players: %s, entities: %d [%s], block_entities: %d [%s], block_ticks: %d, fluid_ticks: %d, chunk_source: %s", this.players.size(), this.entitiesById.size(), a(this.entitiesById.values(), (entity) -> { // CraftBukkit - decompile error
			return IRegistry.ENTITY_TYPE.getKey(entity.getEntityType());
		}), this.tileEntityListTick.size(), a(this.tileEntityListTick, (tileentity) -> { // CraftBukkit - decompile error
			return IRegistry.BLOCK_ENTITY_TYPE.getKey(tileentity.getTileType());
		}), this.getBlockTickList().a(), this.getFluidTickList().a(), this.P());
	}

	private static <T> String a(Collection<T> collection, Function<T, MinecraftKey> function) {
		try {
			Object2IntOpenHashMap<MinecraftKey> object2intopenhashmap = new Object2IntOpenHashMap();
			Iterator<T> iterator = collection.iterator(); // CraftBukkit - decompile error

			while (iterator.hasNext()) {
				T t0 = iterator.next();
				MinecraftKey minecraftkey = function.apply(t0);

				object2intopenhashmap.addTo(minecraftkey, 1);
			}

			// CraftBukkit - decompile error
			return object2intopenhashmap.object2IntEntrySet().stream().sorted(Comparator.comparing(Object2IntMap.Entry<MinecraftKey>::getIntValue).reversed()).limit(5L).map((it_unimi_dsi_fastutil_objects_object2intmap_entry) -> { // Paper - decompile fix
				return it_unimi_dsi_fastutil_objects_object2intmap_entry.getKey() + ":" + it_unimi_dsi_fastutil_objects_object2intmap_entry.getIntValue();
			}).collect(Collectors.joining(","));
		} catch (Exception exception) {
			return "";
		}
	}

	public static void a(WorldServer worldserver) {
		// CraftBukkit start
		WorldServer.a(worldserver, null);
	}

	public static void a(WorldServer worldserver, Entity entity) {
		// CraftBukkit end
		BlockPosition blockposition = WorldServer.a;
		int i = blockposition.getX();
		int j = blockposition.getY() - 2;
		int k = blockposition.getZ();

		// CraftBukkit start
		org.bukkit.craftbukkit.util.BlockStateListPopulator blockList = new org.bukkit.craftbukkit.util.BlockStateListPopulator(worldserver);
		BlockPosition.b(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach((blockposition1) -> {
			blockList.setTypeAndData(blockposition1, Blocks.AIR.getBlockData(), 3);
		});
		BlockPosition.b(i - 2, j, k - 2, i + 2, j, k + 2).forEach((blockposition1) -> {
			blockList.setTypeAndData(blockposition1, Blocks.OBSIDIAN.getBlockData(), 3);
		});
		org.bukkit.World bworld = worldserver.getWorld();
		org.bukkit.event.world.PortalCreateEvent portalEvent = new org.bukkit.event.world.PortalCreateEvent((List<org.bukkit.block.BlockState>) (List) blockList.getList(), bworld, (entity == null) ? null : entity.getBukkitEntity(), org.bukkit.event.world.PortalCreateEvent.CreateReason.END_PLATFORM);

		worldserver.getServer().getPluginManager().callEvent(portalEvent);
		if (!portalEvent.isCancelled()) {
			blockList.updateList();
		}
		// CraftBukkit end
	}
}
