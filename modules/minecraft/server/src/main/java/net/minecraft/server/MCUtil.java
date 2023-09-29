package net.minecraft.server;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.lang.ref.Cleaner;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MCUtil {
	public static final double COLLISION_EPSILON = 1.0E-7; // Tuinity - Just in case mojang changes this...
	public static final ThreadPoolExecutor asyncExecutor = new ThreadPoolExecutor(
		0, 2, 60L, TimeUnit.SECONDS,
		new LinkedBlockingQueue<>(),
		new ThreadFactoryBuilder().setNameFormat("Paper Async Task Handler Thread - %1$d").build()
	);
	public static final ThreadPoolExecutor cleanerExecutor = new ThreadPoolExecutor(
		1, 1, 0L, TimeUnit.SECONDS,
		new LinkedBlockingQueue<>(),
		new ThreadFactoryBuilder().setNameFormat("Paper Object Cleaner").build()
	);

	public static final long INVALID_CHUNK_KEY = getCoordinateKey(Integer.MAX_VALUE, Integer.MAX_VALUE);
	public static final java.util.concurrent.Executor MAIN_EXECUTOR = (run) -> {
		if (!isMainThread()) {
			MinecraftServer.SERVER.execute(run);
		} else {
			run.run();
		}
	};
	static final int SECTION_X_BITS = 22;
	static final long SECTION_X_MASK = (1L << SECTION_X_BITS) - 1;
	static final int SECTION_Y_BITS = 20;
	static final long SECTION_Y_MASK = (1L << SECTION_Y_BITS) - 1;
	static final int SECTION_Z_BITS = 22;
	static final long SECTION_Z_MASK = (1L << SECTION_Z_BITS) - 1;
	// format is y,z,x (in order of LSB to MSB)
	static final int SECTION_Y_SHIFT = 0;
	static final int SECTION_Z_SHIFT = SECTION_Y_SHIFT + SECTION_Y_BITS;
	static final int SECTION_X_SHIFT = SECTION_Z_SHIFT + SECTION_X_BITS;
	static final int SECTION_TO_BLOCK_SHIFT = 4;

	private MCUtil() {
	}

	public static Runnable once(Runnable run) {
		var ran = new AtomicBoolean(false);
		return () -> {
			if (ran.compareAndSet(false, true)) {
				run.run();
			}
		};
	}

	public static <T> Runnable once(List<T> list, Consumer<T> cb) {
		return once(() -> {
			list.forEach(cb);
		});
	}

	private static Runnable makeCleanerCallback(Runnable run) {
		return once(() -> cleanerExecutor.execute(run));
	}

	/**
	 * DANGER WILL ROBINSON: Be sure you do not use a lambda that lives in the object being monitored, or leaky leaky!
	 *
	 * @param obj
	 * @param run
	 * @return
	 */
	public static Runnable registerCleaner(Object obj, Runnable run) {
		// Wrap callback in its own method above or the lambda will leak object
		var callback = makeCleanerCallback(run);
		Cleaner.create().register(obj, callback);
		return callback;
	}

	/**
	 * DANGER WILL ROBINSON: Be sure you do not use a lambda that lives in the object being monitored, or leaky leaky!
	 *
	 * @param obj
	 * @param list
	 * @param cleaner
	 * @param <T>
	 * @return
	 */
	public static <T> Runnable registerListCleaner(Object obj, List<T> list, Consumer<T> cleaner) {
		return registerCleaner(obj, () -> {
			list.forEach(cleaner);
			list.clear();
		});
	}

	/**
	 * DANGER WILL ROBINSON: Be sure you do not use a lambda that lives in the object being monitored, or leaky leaky!
	 *
	 * @param obj
	 * @param resource
	 * @param cleaner
	 * @param <T>
	 * @return
	 */
	public static <T> Runnable registerCleaner(Object obj, T resource, java.util.function.Consumer<T> cleaner) {
		return registerCleaner(obj, () -> cleaner.accept(resource));
	}

	public static List<ChunkCoordIntPair> getSpiralOutChunks(BlockPosition blockposition, int radius) {
		List<ChunkCoordIntPair> list = com.google.common.collect.Lists.newArrayList();

		list.add(new ChunkCoordIntPair(blockposition.getX() >> 4, blockposition.getZ() >> 4));
		for (var r = 1; r <= radius; r++) {
			var x = -r;
			var z = r;

			// Iterates the edge of half of the box; then negates for other half.
			while (x <= r && z > -r) {
				list.add(new ChunkCoordIntPair((blockposition.getX() + (x << 4)) >> 4, (blockposition.getZ() + (z << 4)) >> 4));
				list.add(new ChunkCoordIntPair((blockposition.getX() - (x << 4)) >> 4, (blockposition.getZ() - (z << 4)) >> 4));

				if (x < r) {
					x++;
				} else {
					z--;
				}
			}
		}
		return list;
	}

	public static int fastFloor(double x) {
		var truncated = (int) x;
		return x < (double) truncated ? truncated - 1 : truncated;
	}

	public static int fastFloor(float x) {
		var truncated = (int) x;
		return x < (double) truncated ? truncated - 1 : truncated;
	}

	public static float normalizeYaw(float f) {
		var f1 = f % 360.0F;

		if (f1 >= 180.0F) {
			f1 -= 360.0F;
		}

		if (f1 < -180.0F) {
			f1 += 360.0F;
		}

		return f1;
	}

	/**
	 * Quickly generate a stack trace for current location
	 *
	 * @return Stacktrace
	 */
	public static String stack() {
		return ExceptionUtils.getFullStackTrace(new Throwable());
	}

	// Tuinity start

	/**
	 * Quickly generate a stack trace for current location with message
	 *
	 * @param str
	 * @return Stacktrace
	 */
	public static String stack(String str) {
		return ExceptionUtils.getFullStackTrace(new Throwable(str));
	}

	public static long getCoordinateKey(BlockPosition blockPos) {
		return ((long) (blockPos.getZ() >> 4) << 32) | ((blockPos.getX() >> 4) & 0xFFFFFFFFL);
	}

	public static long getCoordinateKey(Entity entity) {
		return ((long) (MCUtil.fastFloor(entity.locZ()) >> 4) << 32) | ((MCUtil.fastFloor(entity.locX()) >> 4) & 0xFFFFFFFFL);
	}

	public static long getCoordinateKey(ChunkCoordIntPair pair) {
		return ((long) pair.z << 32) | (pair.x & 0xFFFFFFFFL);
	}

	public static long getCoordinateKey(int x, int z) {
		return ((long) z << 32) | (x & 0xFFFFFFFFL);
	}

	public static int getCoordinateX(long key) {
		return (int) key;
	}

	public static int getCoordinateZ(long key) {
		return (int) (key >>> 32);
	}

	public static int getChunkCoordinate(double coordinate) {
		return MCUtil.fastFloor(coordinate) >> 4;
	}

	public static int getBlockCoordinate(double coordinate) {
		return MCUtil.fastFloor(coordinate);
	}

	public static long getBlockKey(int x, int y, int z) {
		return ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27) | ((long) y << 54);
	}

	public static long getBlockKey(BlockPosition pos) {
		return ((long) pos.getX() & 0x7FFFFFF) | (((long) pos.getZ() & 0x7FFFFFF) << 27) | ((long) pos.getY() << 54);
	}

	public static long getBlockKey(Entity entity) {
		return getBlockKey(getBlockCoordinate(entity.locX()), getBlockCoordinate(entity.locY()), getBlockCoordinate(entity.locZ()));
	}

	public static long getSectionKey(int x, int y, int z) {
		return ((x & SECTION_X_MASK) << SECTION_X_SHIFT)
			| ((y & SECTION_Y_MASK) << SECTION_Y_SHIFT)
			| ((z & SECTION_Z_MASK) << SECTION_Z_SHIFT);
	}

	public static long getSectionKey(net.minecraft.core.SectionPosition pos) {
		return ((pos.getX() & SECTION_X_MASK) << SECTION_X_SHIFT)
			| ((pos.getY() & SECTION_Y_MASK) << SECTION_Y_SHIFT)
			| ((pos.getZ() & SECTION_Z_MASK) << SECTION_Z_SHIFT);
	}

	public static long getSectionKey(ChunkCoordIntPair pos, int y) {
		return ((pos.x & SECTION_X_MASK) << SECTION_X_SHIFT)
			| ((y & SECTION_Y_MASK) << SECTION_Y_SHIFT)
			| ((pos.z & SECTION_Z_MASK) << SECTION_Z_SHIFT);
	}

	public static long getSectionKey(BlockPosition pos) {
		return (((long) pos.getX() << (SECTION_X_SHIFT - SECTION_TO_BLOCK_SHIFT)) & (SECTION_X_MASK << SECTION_X_SHIFT)) |
			((pos.getY() >> SECTION_TO_BLOCK_SHIFT) & (SECTION_Y_MASK << SECTION_Y_SHIFT)) |
			(((long) pos.getZ() << (SECTION_Z_SHIFT - SECTION_TO_BLOCK_SHIFT)) & (SECTION_Z_MASK << SECTION_Z_SHIFT));
	}

	public static long getSectionKey(Entity entity) {
		return ((MCUtil.fastFloor(entity.locX()) & SECTION_X_MASK) << SECTION_X_SHIFT)
			| ((MCUtil.fastFloor(entity.locY()) & SECTION_Y_MASK) << SECTION_Y_SHIFT)
			| ((MCUtil.fastFloor(entity.locZ()) & SECTION_Z_MASK) << SECTION_Z_SHIFT);
	}

	public static int getSectionX(long key) {
		return (int) (key << (Long.SIZE - (SECTION_X_SHIFT + SECTION_X_BITS)) >> (Long.SIZE - SECTION_X_BITS));
	}
	// Tuinity end

	public static int getSectionY(long key) {
		return (int) (key << (Long.SIZE - (SECTION_Y_SHIFT + SECTION_Y_BITS)) >> (Long.SIZE - SECTION_Y_BITS));
	}

	public static int getSectionZ(long key) {
		return (int) (key << (Long.SIZE - (SECTION_Z_SHIFT + SECTION_Z_BITS)) >> (Long.SIZE - SECTION_Z_BITS));
	}

	// assumes the sets have the same comparator, and if this comparator is null then assume T is Comparable
	public static <T> void mergeSortedSets(java.util.function.Consumer<T> consumer, java.util.Comparator<? super T> comparator, java.util.SortedSet<T>... sets) {
		var all = new ObjectRBTreeSet<T>(comparator);
		// note: this is done in log(n!) ~ nlogn time. It could be improved if it were to mimic what mergesort does.
		for (var set : sets) {
			if (set != null) {
				all.addAll(set);
			}
		}
		all.forEach(consumer);
	}

	public static <T> CompletableFuture<T> ensureMain(CompletableFuture<T> future) {
		return future.thenApplyAsync(r -> r, MAIN_EXECUTOR);
	}

	public static <T> void thenOnMain(CompletableFuture<T> future, Consumer<T> consumer) {
		future.thenAcceptAsync(consumer, MAIN_EXECUTOR);
	}

	public static <T> void thenOnMain(CompletableFuture<T> future, BiConsumer<T, Throwable> consumer) {
		future.whenCompleteAsync(consumer, MAIN_EXECUTOR);
	}

	public static boolean isMainThread() {
		return MinecraftServer.SERVER.isMainThread();
	}

	public static org.bukkit.scheduler.BukkitTask scheduleTask(int ticks, Runnable runnable) {
		return scheduleTask(ticks, runnable, null);
	}

	public static org.bukkit.scheduler.BukkitTask scheduleTask(int ticks, Runnable runnable, String taskName) {
		return MinecraftServer.SERVER.server.getScheduler().scheduleInternalTask(runnable, ticks, taskName);
	}

	public static void processQueue() {
		Runnable runnable;
		var processQueue = getProcessQueue();
		while ((runnable = processQueue.poll()) != null) {
			try {
				runnable.run();
			} catch (Exception e) {
				MinecraftServer.LOGGER.error("Error executing task", e);
			}
		}
	}

	public static <T> T processQueueWhileWaiting(CompletableFuture<T> future) {
		try {
			if (isMainThread()) {
				while (!future.isDone()) {
					try {
						return future.get(1, TimeUnit.MILLISECONDS);
					} catch (TimeoutException ignored) {
						processQueue();
					}
				}
			}
			return future.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void ensureMain(Runnable run) {
		ensureMain(null, run);
	}

	/**
	 * Ensures the target code is running on the main thread
	 *
	 * @param reason
	 * @param run
	 * @return
	 */
	public static void ensureMain(String reason, Runnable run) {
		run.run();
	}

	private static Queue<Runnable> getProcessQueue() {
		return MinecraftServer.SERVER.processQueue;
	}

	public static <T> T ensureMain(Supplier<T> run) {
		return ensureMain(null, run);
	}

	/**
	 * Ensures the target code is running on the main thread
	 *
	 * @param reason
	 * @param run
	 * @param <T>
	 * @return
	 */
	public static <T> T ensureMain(String reason, Supplier<T> run) {
		return run.get();
	}

	public static PlayerProfile toBukkit(GameProfile profile) {
		return CraftPlayerProfile.asBukkitMirror(profile);
	}

	/**
	 * Calculates distance between 2 entities
	 *
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static double distance(Entity e1, Entity e2) {
		return Math.sqrt(distanceSq(e1, e2));
	}


	/**
	 * Calculates distance between 2 block positions
	 *
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static double distance(BlockPosition e1, BlockPosition e2) {
		return Math.sqrt(distanceSq(e1, e2));
	}

	/**
	 * Gets the distance between 2 positions
	 *
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
	}

	/**
	 * Get's the distance squared between 2 entities
	 *
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static double distanceSq(Entity e1, Entity e2) {
		return distanceSq(e1.locX(), e1.locY(), e1.locZ(), e2.locX(), e2.locY(), e2.locZ());
	}

	/**
	 * Gets the distance sqaured between 2 block positions
	 *
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	public static double distanceSq(BlockPosition pos1, BlockPosition pos2) {
		return distanceSq(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
	}

	/**
	 * Gets the distance squared between 2 positions
	 *
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
	}

	/**
	 * Converts a NMS World/BlockPosition to Bukkit Location
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Location toLocation(World world, double x, double y, double z) {
		return new Location(world.getWorld(), x, y, z);
	}

	/**
	 * Converts a NMS World/BlockPosition to Bukkit Location
	 *
	 * @param world
	 * @param pos
	 * @return
	 */
	public static Location toLocation(World world, BlockPosition pos) {
		return new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Converts an NMS entity's current location to a Bukkit Location
	 *
	 * @param entity
	 * @return
	 */
	public static Location toLocation(Entity entity) {
		return new Location(entity.getWorld().getWorld(), entity.locX(), entity.locY(), entity.locZ());
	}

	public static org.bukkit.block.Block toBukkitBlock(World world, BlockPosition pos) {
		return world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
	}

	public static BlockPosition toBlockPosition(Location loc) {
		return new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static boolean isEdgeOfChunk(BlockPosition pos) {
		var modX = pos.getX() & 15;
		var modZ = pos.getZ() & 15;
		return (modX == 0 || modX == 15 || modZ == 0 || modZ == 15);
	}

	/**
	 * Posts a task to be executed asynchronously
	 *
	 * @param run
	 */
	public static void scheduleAsyncTask(Runnable run) {
		asyncExecutor.execute(run);
	}

	@Nonnull
	public static WorldServer getNMSWorld(@Nonnull org.bukkit.World world) {
		return ((CraftWorld) world).getHandle();
	}

	public static WorldServer getNMSWorld(@Nonnull org.bukkit.entity.Entity entity) {
		return getNMSWorld(entity.getWorld());
	}

	public static RayTrace.FluidCollisionOption getNMSFluidCollisionOption(TargetBlockInfo.FluidMode fluidMode) {
		if (fluidMode == TargetBlockInfo.FluidMode.NEVER) {
			return RayTrace.FluidCollisionOption.NONE;
		}
		if (fluidMode == TargetBlockInfo.FluidMode.SOURCE_ONLY) {
			return RayTrace.FluidCollisionOption.SOURCE_ONLY;
		}
		if (fluidMode == TargetBlockInfo.FluidMode.ALWAYS) {
			return RayTrace.FluidCollisionOption.ANY;
		}
		return null;
	}

	public static BlockFace toBukkitBlockFace(EnumDirection enumDirection) {
		switch (enumDirection) {
			case DOWN:
				return BlockFace.DOWN;
			case UP:
				return BlockFace.UP;
			case NORTH:
				return BlockFace.NORTH;
			case SOUTH:
				return BlockFace.SOUTH;
			case WEST:
				return BlockFace.WEST;
			case EAST:
				return BlockFace.EAST;
			default:
				return null;
		}
	}

	@Nullable
	public static IChatBaseComponent getBaseComponentFromNbt(String key, NBTTagCompound compound) {
		if (!compound.hasKey(key)) {
			return null;
		}
		var string = compound.getString(key);
		try {
			return IChatBaseComponent.ChatSerializer.jsonToComponent(string);
		} catch (com.google.gson.JsonParseException e) {
			org.bukkit.Bukkit.getLogger().warning("Unable to parse " + key + " from " + compound + ": " + e.getMessage());
		}

		return null;
	}

	public static ChunkStatus getChunkStatus(PlayerChunk chunk) {
		var statuses = ChunkProviderServer.getPossibleChunkStatuses();
		for (var i = statuses.size() - 1; i >= 0; --i) {
			var curr = statuses.get(i);
			var future = chunk.getStatusFutureUnchecked(curr);
			if (future != PlayerChunk.UNLOADED_CHUNK_ACCESS_FUTURE) {
				return curr;
			}
		}
		return null; // unloaded
	}

	public static void dumpChunks(File file) throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();
		/*
		 * Json format:
		 *
		 * Main data format:
		 *  -server-version:<string>
		 *  -data-version:<int>
		 *  -worlds:
		 *    -name:<world name>
		 *    -view-distance:<int>
		 *    -keep-spawn-loaded:<boolean>
		 *    -keep-spawn-loaded-range:<int>
		 *    -visible-chunk-count:<int>
		 *    -loaded-chunk-count:<int>
		 *    -verified-fully-loaded-chunks:<int>
		 *    -players:<array of player>
		 *    -chunk-data:<array of chunks>
		 *
		 * Player format:
		 *  -name:<string>
		 *  -x:<double>
		 *  -y:<double>
		 *  -z:<double>
		 *
		 * Chunk Format:
		 *  -x:<integer>
		 *  -z:<integer>
		 *  -ticket-level:<integer>
		 *  -state:<string>
		 *  -queued-for-unload:<boolean>
		 *  -status:<string>
		 *  -tickets:<array of tickets>
		 *
		 *
		 * Ticket format:
		 *  -ticket-type:<string>
		 *  -ticket-level:<int>
		 *  -add-tick:<long>
		 *  -object-reason:<string> // This depends on the type of ticket. ie POST_TELEPORT -> entity id
		 */
		var worlds = org.bukkit.Bukkit.getWorlds();
		var data = new JsonObject();

		data.addProperty("server-version", org.bukkit.Bukkit.getVersion());
		data.addProperty("data-version", 0);

		var worldsData = new JsonArray();

		for (var bukkitWorld : worlds) {
			var worldData = new JsonObject();

			var world = ((org.bukkit.craftbukkit.CraftWorld) bukkitWorld).getHandle();
			var chunkMap = world.getChunkProvider().playerChunkMap;
			var visibleChunks = chunkMap.getVisibleChunks();
			ChunkMapDistance chunkMapDistance = chunkMap.chunkDistanceManager;
			List<PlayerChunk> allChunks = new ObjectArrayList<>(visibleChunks.values());
			var players = world.players;

			var fullLoadedChunks = 0;

			for (var chunk : allChunks) {
				if (chunk.getFullChunkIfCached() != null) {
					++fullLoadedChunks;
				}
			}

			// sorting by coordinate makes the log easier to read
			allChunks.sort((PlayerChunk v1, PlayerChunk v2) -> {
				if (v1.location.x != v2.location.x) {
					return Integer.compare(v1.location.x, v2.location.x);
				}
				return Integer.compare(v1.location.z, v2.location.z);
			});

			worldData.addProperty("name", world.getWorld().getName());
			worldData.addProperty("view-distance", world.getChunkProvider().playerChunkMap.getEffectiveViewDistance());
			worldData.addProperty("no-view-distance", world.getChunkProvider().playerChunkMap.playerChunkManager.getTargetNoTickViewDistance()); // Tuinity - replace old player chunk management
			worldData.addProperty("keep-spawn-loaded", world.keepSpawnInMemory);
			worldData.addProperty("keep-spawn-loaded-range", world.paperConfig.keepLoadedRange);
			worldData.addProperty("visible-chunk-count", visibleChunks.size());
			worldData.addProperty("loaded-chunk-count", chunkMap.loadedChunks.size());
			worldData.addProperty("verified-fully-loaded-chunks", fullLoadedChunks);

			var playersData = new JsonArray();

			for (var player : players) {
				var playerData = new JsonObject();

				playerData.addProperty("name", player.getName());
				playerData.addProperty("x", player.locX());
				playerData.addProperty("y", player.locY());
				playerData.addProperty("z", player.locZ());

				playersData.add(playerData);

			}

			worldData.add("players", playersData);

			var chunksData = new JsonArray();

			for (var playerChunk : allChunks) {
				var chunkData = new JsonObject();

				Set<Ticket<?>> tickets = chunkMapDistance.tickets.get(playerChunk.location.pair());
				var status = getChunkStatus(playerChunk);

				chunkData.addProperty("x", playerChunk.location.x);
				chunkData.addProperty("z", playerChunk.location.z);
				chunkData.addProperty("ticket-level", playerChunk.getTicketLevel());
				chunkData.addProperty("priority", playerChunk.getCurrentPriority());
				chunkData.addProperty("state", PlayerChunk.getChunkState(playerChunk.getTicketLevel()).toString());
				chunkData.addProperty("queued-for-unload", chunkMap.unloadQueue.contains(playerChunk.location.pair()));
				chunkData.addProperty("status", status == null ? "unloaded" : status.toString());

				var ticketsData = new JsonArray();

				if (tickets != null) {
					for (var ticket : tickets) {
						var ticketData = new JsonObject();

						ticketData.addProperty("ticket-type", ticket.getTicketType().toString());
						ticketData.addProperty("ticket-level", ticket.getTicketLevel());
						ticketData.addProperty("object-reason", String.valueOf(ticket.getObjectReason()));
						ticketData.addProperty("add-tick", ticket.getCreationTick());

						ticketsData.add(ticketData);
					}
				}

				chunkData.add("tickets", ticketsData);
				chunksData.add(chunkData);
			}


			worldData.add("chunk-data", chunksData);
			worldsData.add(worldData);
		}

		data.add("worlds", worldsData);

		var stringWriter = new StringWriter();
		var jsonWriter = new JsonWriter(stringWriter);
		jsonWriter.setIndent(" ");
		jsonWriter.setLenient(false);
		Streams.write(data, jsonWriter);

		var fileData = stringWriter.toString();

		try (var out = new PrintStream(new FileOutputStream(file), false, StandardCharsets.UTF_8)) {
			out.print(fileData);
		}
	}

	public static int getTicketLevelFor(ChunkStatus status) {
		// TODO make sure the constant `33` is correct on future updates. See getChunkAt(int, int, ChunkStatus, boolean)
		return 33 + ChunkStatus.getTicketLevelOffset(status);
	}
}
