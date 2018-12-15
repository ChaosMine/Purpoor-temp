package com.tuinity.tuinity.world;

import com.tuinity.tuinity.util.CoordinateUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.WorldServer;
import java.util.concurrent.locks.StampedLock;

public final class EntitySliceManager {

    protected static final int REGION_SHIFT = 5;
    protected static final int REGION_MASK = (1 << REGION_SHIFT) - 1;
    protected static final int REGION_SIZE = 1 << REGION_SHIFT;

    public final WorldServer world;

    private final StampedLock stateLock = new StampedLock();
    protected final Long2ObjectOpenHashMap<ChunkSlicesRegion> regions = new Long2ObjectOpenHashMap<>(32, 0.7f);

    public EntitySliceManager(final WorldServer world) {
        this.world = world;
    }

    public ChunkSlicesRegion getRegion(final int regionX, final int regionZ) {
        final long key = CoordinateUtils.getChunkKey(regionX, regionZ);
        final long attempt = this.stateLock.tryOptimisticRead();
        if (attempt != 0L) {
            try {
                final ChunkSlicesRegion ret = this.regions.get(key);

                if (this.stateLock.validate(attempt)) {
                    return ret;
                }
            } catch (final Error error) {
                throw error;
            } catch (final Throwable thr) {
                // ignore
            }
        }

        this.stateLock.readLock();
        try {
            return this.regions.get(key);
        } finally {
            this.stateLock.tryUnlockRead();
        }
    }

    public synchronized void removeChunk(final int chunkX, final int chunkZ) {
        final long key = CoordinateUtils.getChunkKey(chunkX >> REGION_SHIFT, chunkZ >> REGION_SHIFT);
        final int relIndex = (chunkX & REGION_MASK) | ((chunkZ & REGION_MASK) << REGION_SHIFT);

        final ChunkSlicesRegion region = this.regions.get(key);
        final int remaining = region.remove(relIndex);

        if (remaining == 0) {
            this.stateLock.writeLock();
            try {
                this.regions.remove(key);
            } finally {
                this.stateLock.tryUnlockWrite();
            }
        }
    }

    public synchronized void addChunk(final int chunkX, final int chunkZ, final ChunkEntitySlices slices) {
        final long key = CoordinateUtils.getChunkKey(chunkX >> REGION_SHIFT, chunkZ >> REGION_SHIFT);
        final int relIndex = (chunkX & REGION_MASK) | ((chunkZ & REGION_MASK) << REGION_SHIFT);

        ChunkSlicesRegion region = this.regions.get(key);
        if (region != null) {
            region.add(relIndex, slices);
        } else {
            region = new ChunkSlicesRegion();
            region.add(relIndex, slices);
            this.stateLock.writeLock();
            try {
                this.regions.put(key, region);
            } finally {
                this.stateLock.tryUnlockWrite();
            }
        }
    }

    public static final class ChunkSlicesRegion {

        protected final ChunkEntitySlices[] slices = new ChunkEntitySlices[REGION_SIZE * REGION_SIZE];
        protected int sliceCount;

        public ChunkEntitySlices get(final int index) {
            return this.slices[index];
        }

        public int remove(final int index) {
            final ChunkEntitySlices slices = this.slices[index];
            if (slices == null) {
                throw new IllegalStateException();
            }

            this.slices[index] = null;

            return --this.sliceCount;
        }

        public void add(final int index, final ChunkEntitySlices slices) {
            final ChunkEntitySlices curr = this.slices[index];
            if (curr != null) {
                throw new IllegalStateException();
            }

            this.slices[index] = slices;

            ++this.sliceCount;
        }
    }
}
