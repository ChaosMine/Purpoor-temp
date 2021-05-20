package io.papermc.paper.world;

import com.mojang.datafixers.DataFixer;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.storage.IChunkLoader;
import net.minecraft.world.level.chunk.storage.RegionFileCache;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.WorldPersistentData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class ThreadedWorldUpgrader {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ResourceKey<WorldDimension> dimensionType;
    private final ResourceKey<DimensionManager> worldKey;
    private final String worldName;
    private final ExecutorService threadPool;
    private final DataFixer dataFixer;
    private final boolean removeCaches;

    public ThreadedWorldUpgrader(final ResourceKey<WorldDimension> dimensionType, final ResourceKey<DimensionManager> worldKey, final String worldName, final int threads,
                                 final DataFixer dataFixer, final boolean removeCaches) {
        this.dimensionType = dimensionType;
        this.worldKey = worldKey;
        this.worldName = worldName;
        this.threadPool = Executors.newFixedThreadPool(Math.max(1, threads), new ThreadFactory() {
            private final AtomicInteger threadCounter = new AtomicInteger();

            @Override
            public Thread newThread(final Runnable run) {
                final Thread ret = new Thread(run);

                ret.setName("World upgrader thread for world " + ThreadedWorldUpgrader.this.worldName + " #" + this.threadCounter.getAndIncrement());
                ret.setUncaughtExceptionHandler((thread, throwable) -> {
                    LOGGER.fatal("Error upgrading world", throwable);
                });

                return ret;
            }
        });
        this.dataFixer = dataFixer;
        this.removeCaches = removeCaches;
    }

    public void convert() {
        final File worldFolder = Convertable.getFolder(new File(this.worldName), this.dimensionType);
        final WorldPersistentData worldPersistentData = new WorldPersistentData(new File(worldFolder, "data"), this.dataFixer);

        final File regionFolder = new File(worldFolder, "region");

        LOGGER.info("Force upgrading " + this.worldName);
        LOGGER.info("Counting regionfiles for " + this.worldName);
        final File[] regionFiles = regionFolder.listFiles((final File dir, final String name) -> {
            return WorldUpgrader.getRegionfileRegex().matcher(name).matches();
        });
        if (regionFiles == null) {
            LOGGER.info("Found no regionfiles to convert for world " + this.worldName);
            return;
        }
        LOGGER.info("Found " + regionFiles.length + " regionfiles to convert");
        LOGGER.info("Starting conversion now for world " + this.worldName);

        final WorldInfo info = new WorldInfo(() -> worldPersistentData,
                new IChunkLoader(regionFolder, this.dataFixer, false), this.removeCaches, this.worldKey);

        long expectedChunks = (long)regionFiles.length * (32L * 32L);

        for (final File regionFile : regionFiles) {
            final ChunkCoordIntPair regionPos = RegionFileCache.getRegionFileCoordinates(regionFile);
            if (regionPos == null) {
                expectedChunks -= (32L * 32L);
                continue;
            }

            this.threadPool.execute(new ConvertTask(info, regionPos.x >> 5, regionPos.z >> 5));
        }
        this.threadPool.shutdown();

        final DecimalFormat format = new DecimalFormat("#0.00");

        final long start = System.nanoTime();

        while (!this.threadPool.isTerminated()) {
            final long current = info.convertedChunks.get();

            LOGGER.info("{}% completed ({} / {} chunks)...", format.format((double)current / (double)expectedChunks * 100.0), current, expectedChunks);

            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException ignore) {}
        }

        final long end = System.nanoTime();

        try {
            info.loader.close();
        } catch (final IOException ex) {
            LOGGER.fatal("Failed to close chunk loader", ex);
        }
        LOGGER.info("Completed conversion. Took {}s, {} out of {} chunks needed to be converted/modified ({}%)",
                (int)Math.ceil((end - start) * 1.0e-9), info.modifiedChunks.get(), expectedChunks, format.format((double)info.modifiedChunks.get() / (double)expectedChunks * 100.0));
    }

    private static final class WorldInfo {

        public final Supplier<WorldPersistentData> persistentDataSupplier;
        public final IChunkLoader loader;
        public final boolean removeCaches;
        public final ResourceKey<DimensionManager> worldKey;
        public final AtomicLong convertedChunks = new AtomicLong();
        public final AtomicLong modifiedChunks = new AtomicLong();

        private WorldInfo(final Supplier<WorldPersistentData> persistentDataSupplier, final IChunkLoader loader, final boolean removeCaches,
                          final ResourceKey<DimensionManager> worldKey) {
            this.persistentDataSupplier = persistentDataSupplier;
            this.loader = loader;
            this.removeCaches = removeCaches;
            this.worldKey = worldKey;
        }
    }

    private static final class ConvertTask implements Runnable {

        private final WorldInfo worldInfo;
        private final int regionX;
        private final int regionZ;

        public ConvertTask(final WorldInfo worldInfo, final int regionX, final int regionZ) {
            this.worldInfo = worldInfo;
            this.regionX = regionX;
            this.regionZ = regionZ;
        }

        @Override
        public void run() {
            final int regionCX = this.regionX << 5;
            final int regionCZ = this.regionZ << 5;

            final Supplier<WorldPersistentData> persistentDataSupplier = this.worldInfo.persistentDataSupplier;
            final IChunkLoader loader = this.worldInfo.loader;
            final boolean removeCaches = this.worldInfo.removeCaches;
            final ResourceKey<DimensionManager> worldKey = this.worldInfo.worldKey;

            for (int cz = regionCZ; cz < (regionCZ + 32); ++cz) {
                for (int cx = regionCX; cx < (regionCX + 32); ++cx) {
                    final ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(cx, cz);
                    try {
                        // no need to check the coordinate of the chunk, the regionfilecache does that for us

                        NBTTagCompound chunkNBT = loader.read(chunkPos);

                        if (chunkNBT == null) {
                            continue;
                        }

                        final int versionBefore = IChunkLoader.getVersion(chunkNBT);

                        chunkNBT = loader.getChunkData(worldKey, persistentDataSupplier, chunkNBT, chunkPos, null);

                        boolean modified = versionBefore < SharedConstants.getGameVersion().getWorldVersion();

                        if (removeCaches) {
                            final NBTTagCompound level = chunkNBT.getCompound("Level");
                            modified |= level.hasKey("Heightmaps");
                            level.remove("Heightmaps");
                            modified |= level.hasKey("isLightOn");
                            level.remove("isLightOn");
                        }

                        if (modified) {
                            this.worldInfo.modifiedChunks.getAndIncrement();
                            loader.write(chunkPos, chunkNBT);
                        }
                    } catch (final Exception ex) {
                        LOGGER.error("Error upgrading chunk {}", chunkPos, ex);
                    } finally {
                        this.worldInfo.convertedChunks.getAndIncrement();
                    }
                }
            }
        }
    }
}
