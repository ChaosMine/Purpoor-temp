package net.minecraft.world.entity.ai.village.poi;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.SectionPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.LightEngineGraphSection;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.storage.RegionFileSection;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VillagePlace extends RegionFileSection<VillagePlaceSection> {

    // Tuinity start - unload poi data
    // the vanilla tracker needs to be replaced because it does not support level removes
    private final com.tuinity.tuinity.util.misc.Delayed26WayDistancePropagator3D villageDistanceTracker = new com.tuinity.tuinity.util.misc.Delayed26WayDistancePropagator3D();
    static final int POI_DATA_SOURCE = 7;
    public static int convertBetweenLevels(final int level) {
        return POI_DATA_SOURCE - level;
    }

    protected void updateDistanceTracking(long section) {
        if (this.isSectionDistanceTrackerSource(section)) {
            this.villageDistanceTracker.setSource(section, POI_DATA_SOURCE);
        } else {
            this.villageDistanceTracker.removeSource(section);
        }
    }
    // Tuinity end - unload poi data

    private final LongSet b = new LongOpenHashSet(); private final LongSet getLoadedChunks() { return this.b; } // Tuinity - OBFHELPER

    private final WorldServer world; // Paper

    public VillagePlace(File file, DataFixer datafixer, boolean flag) {
        // Paper start - add world parameter
        this(file, datafixer, flag, null);
    }
    public VillagePlace(File file, DataFixer datafixer, boolean flag, WorldServer world) {
        super(file, VillagePlaceSection::a, VillagePlaceSection::new, datafixer, DataFixTypes.POI_CHUNK, flag);
        this.world = world;
        if (world == null) { throw new IllegalStateException("world must be non-null"); }// Tuinity - require non-null
        // Paper end - add world parameter
    }

    // Tuinity start - actually unload POI data
    private final java.util.TreeSet<QueuedUnload> queuedUnloads = new java.util.TreeSet<>();
    private final Long2ObjectOpenHashMap<QueuedUnload> queuedUnloadsByCoordinate = new Long2ObjectOpenHashMap<>();

    static final class QueuedUnload implements Comparable<QueuedUnload> {

        private final long unloadTick;
        private final long coordinate;

        public QueuedUnload(long unloadTick, long coordinate) {
            this.unloadTick = unloadTick;
            this.coordinate = coordinate;
        }

        @Override
        public int compareTo(QueuedUnload other) {
            if (other.unloadTick == this.unloadTick) {
                return Long.compare(this.coordinate, other.coordinate);
            } else {
                return Long.compare(this.unloadTick, other.unloadTick);
            }
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = hash * 31 + Long.hashCode(this.unloadTick);
            hash = hash * 31 + Long.hashCode(this.coordinate);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != QueuedUnload.class) {
                return false;
            }
            QueuedUnload other = (QueuedUnload)obj;
            return other.unloadTick == this.unloadTick && other.coordinate == this.coordinate;
        }
    }

    long determineDelay(long coordinate) {
        if (this.isEmpty(coordinate)) {
            return 5 * 60 * 20;
        } else {
            return 60 * 20;
        }
    }

    public void queueUnload(long coordinate, long minTarget) {
        QueuedUnload unload = new QueuedUnload(minTarget + this.determineDelay(coordinate), coordinate);
        QueuedUnload existing = this.queuedUnloadsByCoordinate.put(coordinate, unload);
        if (existing != null) {
            this.queuedUnloads.remove(existing);
        }
        this.queuedUnloads.add(unload);
    }

    public void dequeueUnload(long coordinate) {
        QueuedUnload unload = this.queuedUnloadsByCoordinate.remove(coordinate);
        if (unload != null) {
            this.queuedUnloads.remove(unload);
        }
    }

    public void pollUnloads(BooleanSupplier canSleepForTick) {
        long currentTick = net.minecraft.server.MinecraftServer.currentTickLong;
        net.minecraft.server.level.ChunkProviderServer chunkProvider = this.world.getChunkProvider();
        net.minecraft.server.level.PlayerChunkMap playerChunkMap = chunkProvider.playerChunkMap;
        // copied target determination from PlayerChunkMap
        int target = Math.min(this.queuedUnloads.size() - 100,  (int) (this.queuedUnloads.size() * net.minecraft.server.level.PlayerChunkMap.UNLOAD_QUEUE_RESIZE_FACTOR)); // Paper - Make more aggressive
        for (java.util.Iterator<QueuedUnload> iterator = this.queuedUnloads.iterator();
             iterator.hasNext() && (this.queuedUnloads.size() > target || canSleepForTick.getAsBoolean());) {
            QueuedUnload unload = iterator.next();
            if (unload.unloadTick > currentTick) {
                break;
            }

            long coordinate = unload.coordinate;

            iterator.remove();
            this.queuedUnloadsByCoordinate.remove(coordinate);

            if (playerChunkMap.getUnloadingPlayerChunk(net.minecraft.server.MCUtil.getCoordinateX(coordinate), net.minecraft.server.MCUtil.getCoordinateZ(coordinate)) != null
                || playerChunkMap.getUpdatingChunk(coordinate) != null) {
                continue;
            }

            this.unloadData(coordinate);
        }
    }

    @Override
    public void unloadData(long coordinate) {
        super.unloadData(coordinate);
    }

    @Override
    protected void onUnload(long coordinate) {
        this.getLoadedChunks().remove(coordinate);
        int chunkX = net.minecraft.server.MCUtil.getCoordinateX(coordinate);
        int chunkZ = net.minecraft.server.MCUtil.getCoordinateZ(coordinate);
        for (int section = 0; section < 16; ++section) {
            long sectionPos = SectionPosition.asLong(chunkX, section, chunkZ);
            this.updateDistanceTracking(sectionPos);
        }
    }
    // Tuinity end - actually unload POI data

    public void a(BlockPosition blockposition, VillagePlaceType villageplacetype) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).a(blockposition, villageplacetype);
    }

    public void remove(BlockPosition blockposition) { this.a(blockposition); } // Paper - OBFHELPER
    public void a(BlockPosition blockposition) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).a(blockposition);
    }

    public long count(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) { return a(predicate, blockposition, i, villageplace_occupancy); } // Purpur - OBFHELPER
    public long a(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.c(predicate, blockposition, i, villageplace_occupancy).count();
    }

    public boolean a(VillagePlaceType villageplacetype, BlockPosition blockposition) {
        Optional<VillagePlaceType> optional = ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).d(blockposition);

        return optional.isPresent() && ((VillagePlaceType) optional.get()).equals(villageplacetype);
    }

    public Stream<VillagePlaceRecord> b(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        int j = Math.floorDiv(i, 16) + 1;

        return ChunkCoordIntPair.a(new ChunkCoordIntPair(blockposition), j).flatMap((chunkcoordintpair) -> {
            return this.a(predicate, chunkcoordintpair, villageplace_occupancy);
        }).filter((villageplacerecord) -> {
            BlockPosition blockposition1 = villageplacerecord.f();

            return Math.abs(blockposition1.getX() - blockposition.getX()) <= i && Math.abs(blockposition1.getZ() - blockposition.getZ()) <= i;
        });
    }

    public Stream<VillagePlaceRecord> c(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        int j = i * i;

        return this.b(predicate, blockposition, i, villageplace_occupancy).filter((villageplacerecord) -> {
            return villageplacerecord.f().j(blockposition) <= (double) j;
        });
    }

    public Stream<VillagePlaceRecord> a(Predicate<VillagePlaceType> predicate, ChunkCoordIntPair chunkcoordintpair, VillagePlace.Occupancy villageplace_occupancy) {
        return IntStream.range(0, 16).boxed().map((integer) -> {
            return this.d(SectionPosition.a(chunkcoordintpair, integer).s());
        }).filter(Optional::isPresent).flatMap((optional) -> {
            return ((VillagePlaceSection) optional.get()).a(predicate, villageplace_occupancy);
        });
    }

    public Stream<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.c(predicate, blockposition, i, villageplace_occupancy).map(VillagePlaceRecord::f).filter(predicate1);
    }

    public Stream<BlockPosition> b(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.a(predicate, predicate1, blockposition, i, villageplace_occupancy).sorted(Comparator.comparingDouble((blockposition1) -> {
            return blockposition1.j(blockposition);
        }));
    }

    public Optional<BlockPosition> c(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        // Tuinity start - re-route to faster logic
        BlockPosition ret = com.tuinity.tuinity.util.PoiAccess.findAnyPoiPosition(this, predicate, predicate1, blockposition, i, villageplace_occupancy, false);
        return Optional.ofNullable(ret);
        // Tuinity end - re-route to faster logic
    }

    public Optional<BlockPosition> d(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        // Tuinity start - re-route to faster logic
        BlockPosition ret = com.tuinity.tuinity.util.PoiAccess.findClosestPoiDataPosition(this, predicate, null, blockposition, i, i*i, villageplace_occupancy, false);
        return Optional.ofNullable(ret);
        // Tuinity end - re-route to faster logic
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i) {
        // Tuinity start - re-route to faster logic
        VillagePlaceRecord ret = com.tuinity.tuinity.util.PoiAccess.findAnyPoiRecord(this, predicate, predicate1, blockposition, i,
                VillagePlace.Occupancy.HAS_SPACE, false);
        if (ret == null) {
            return Optional.empty();
        }
        // copy from the map() from the old code
        ret.b();
        return Optional.ofNullable(ret.f());
        // Tuinity end - re-route to faster logic
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, VillagePlace.Occupancy villageplace_occupancy, BlockPosition blockposition, int i, Random random) {
        // Tuinity start - re-route to faster logic
        List<VillagePlaceRecord> list = new ObjectArrayList<>();
        com.tuinity.tuinity.util.PoiAccess.findAnyPoiRecords(this, predicate, predicate1, blockposition, i, villageplace_occupancy, false, Integer.MAX_VALUE, list);
        // Tuinity end - re-route to faster logic

        // Tuinity start - rewrite this to use improved logic
        // the old method shuffled the list and then tried to find the first element in it that
        // matched predicate1, however we moved predicate1 into the poi search. This means we can avoid a shuffle
        // entirely, and just pick a random element from list
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(random.nextInt(list.size())).f());
        // Tuinity end - rewrite this to use improved logic
    }

    public boolean b(BlockPosition blockposition) {
        return ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).c(blockposition);
    }

    public final boolean test(BlockPosition blockposition, Predicate<VillagePlaceType> predicate) { return this.a(blockposition, predicate); } // Paper - OBFHELPER
    public boolean a(BlockPosition blockposition, Predicate<VillagePlaceType> predicate) {
        return (Boolean) this.d(SectionPosition.a(blockposition).s()).map((villageplacesection) -> {
            return villageplacesection.a(blockposition, predicate);
        }).orElse(false);
    }

    public Optional<VillagePlaceType> c(BlockPosition blockposition) {
        VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(SectionPosition.a(blockposition).s());

        return villageplacesection.d(blockposition);
    }

    public int a(SectionPosition sectionposition) {
        this.villageDistanceTracker.propagateUpdates(); // Tuinity - replace distance tracking util
        return convertBetweenLevels(this.villageDistanceTracker.getLevel(net.minecraft.server.MCUtil.getSectionKey(sectionposition))); // Tuinity - replace distance tracking util
    }

    private boolean isSectionDistanceTrackerSource(long section) { return this.f(section); } // Tuinity - OBFHELPER
    private boolean f(long i) {
        Optional<VillagePlaceSection> optional = this.c(i);

        return optional == null ? false : (Boolean) optional.map((villageplacesection) -> {
            return villageplacesection.a(VillagePlaceType.b, VillagePlace.Occupancy.IS_OCCUPIED).count() > 0L;
        }).orElse(false);
    }

    @Override
    public void a(BooleanSupplier booleansupplier) {
        // Paper start - async chunk io
        if (this.world == null) {
            super.a(booleansupplier);
        } else {
            //super.a(booleansupplier); // re-implement below
            while (!((RegionFileSection)this).d.isEmpty() && booleansupplier.getAsBoolean() && !this.world.isSavingDisabled()) { // Tuinity - unload POI data - don't write to disk if saving is disabled
                ChunkCoordIntPair chunkcoordintpair = SectionPosition.a(((RegionFileSection)this).d.firstLong()).r();

                NBTTagCompound data;
                    data = this.getData(chunkcoordintpair);
                com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.scheduleSave(this.world,
                    chunkcoordintpair.x, chunkcoordintpair.z, data, null, com.destroystokyo.paper.io.PrioritizedTaskQueue.NORMAL_PRIORITY); // Tuinity - use normal priority
            }
        }
        // Tuinity start - unload POI data
        if (!this.world.isSavingDisabled()) { // don't write to disk if saving is disabled
            this.pollUnloads(booleansupplier);
        }
        // Tuinity end - unload POI data
        // Paper end
        this.villageDistanceTracker.propagateUpdates(); // Tuinity - replace distance tracking until
    }

    @Override
    protected void a(long i) {
        super.a(i);
        this.updateDistanceTracking(i); // Tuinity - move to new distance tracking util
    }

    @Override
    protected void b(long i) {
        this.updateDistanceTracking(i); // Tuinity - move to new distance tracking util
    }

    public void a(ChunkCoordIntPair chunkcoordintpair, ChunkSection chunksection) {
        SectionPosition sectionposition = SectionPosition.a(chunkcoordintpair, chunksection.getYPosition() >> 4);

        SystemUtils.a(this.d(sectionposition.s()), (villageplacesection) -> {
            villageplacesection.a((biconsumer) -> {
                if (a(chunksection)) {
                    this.a(chunksection, sectionposition, biconsumer);
                }

            });
        }, () -> {
            if (a(chunksection)) {
                VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(sectionposition.s());

                this.a(chunksection, sectionposition, villageplacesection::a);
            }

        });
    }

    private static boolean a(ChunkSection chunksection) {
        Set<IBlockData> set = VillagePlaceType.x; // Paper - decompile error

        //set.getClass(); // Paper - decompile error
        return chunksection.a(set::contains);
    }

    private void a(ChunkSection chunksection, SectionPosition sectionposition, BiConsumer<BlockPosition, VillagePlaceType> biconsumer) {
        sectionposition.t().forEach((blockposition) -> {
            IBlockData iblockdata = chunksection.getType(SectionPosition.b(blockposition.getX()), SectionPosition.b(blockposition.getY()), SectionPosition.b(blockposition.getZ()));

            VillagePlaceType.b(iblockdata).ifPresent((villageplacetype) -> {
                biconsumer.accept(blockposition, villageplacetype);
            });
        });
    }

    public void a(IWorldReader iworldreader, BlockPosition blockposition, int i) {
        SectionPosition.b(new ChunkCoordIntPair(blockposition), Math.floorDiv(i, 16)).map((sectionposition) -> {
            return Pair.of(sectionposition, this.d(sectionposition.s()));
        }).filter((pair) -> {
            return !(Boolean) (pair.getSecond()).map(VillagePlaceSection::a).orElse(false); // Paper - decompile fix
        }).map((pair) -> {
            return ((SectionPosition) pair.getFirst()).r();
        }).filter((chunkcoordintpair) -> {
            return this.b.add(chunkcoordintpair.pair());
        }).forEach((chunkcoordintpair) -> {
            iworldreader.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z, ChunkStatus.EMPTY);
        });
    }

    final class a extends LightEngineGraphSection {

        private final Long2ByteMap b = new Long2ByteOpenHashMap();

        protected a() {
            super(7, 16, 256);
            this.b.defaultReturnValue((byte) 7);
        }

        @Override
        protected int b(long i) {
            return VillagePlace.this.f(i) ? 0 : 7; // Tuinity - unload poi data - diff on change, this specifies the source level to use for distance tracking
        }

        @Override
        protected int c(long i) {
            return this.b.get(i);
        }

        @Override
        protected void a(long i, int j) {
            if (j > 6) {
                this.b.remove(i);
            } else {
                this.b.put(i, (byte) j);
            }

        }

        public void a() {
            super.b(Integer.MAX_VALUE);
        }
    }

    // Paper start - Asynchronous chunk io
    @javax.annotation.Nullable
    @Override
    public NBTTagCompound read(ChunkCoordIntPair chunkcoordintpair) throws java.io.IOException {
        if (this.world != null && Thread.currentThread() != com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE) {
            NBTTagCompound ret = com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE
                .loadChunkDataAsyncFuture(this.world, chunkcoordintpair.x, chunkcoordintpair.z, com.destroystokyo.paper.io.IOUtil.getPriorityForCurrentThread(),
                    true, false, true).join().poiData;

            if (ret == com.destroystokyo.paper.io.PaperFileIOThread.FAILURE_VALUE) {
                throw new java.io.IOException("See logs for further detail");
            }
            return ret;
        }
        return super.read(chunkcoordintpair);
    }

    @Override
    public void write(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) throws java.io.IOException {
        if (this.world != null && Thread.currentThread() != com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE) {
            com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.scheduleSave(
                this.world, chunkcoordintpair.x, chunkcoordintpair.z, nbttagcompound, null,
                com.destroystokyo.paper.io.PrioritizedTaskQueue.NORMAL_PRIORITY); // Tuinity - writes are async, no need for priority
            return;
        }
        super.write(chunkcoordintpair, nbttagcompound);
    }
    // Paper end

    public static enum Occupancy {

        HAS_SPACE(VillagePlaceRecord::d), IS_OCCUPIED(VillagePlaceRecord::e), ANY((villageplacerecord) -> {
            return true;
        });

        private final Predicate<? super VillagePlaceRecord> d;

        private Occupancy(Predicate<? super VillagePlaceRecord> predicate) { // Paper - decompile fix
            this.d = predicate;
        }

        public final Predicate<? super VillagePlaceRecord> getPredicate() { return this.a(); } // Tuinity - OBFHELPER
        public Predicate<? super VillagePlaceRecord> a() {
            return this.d;
        }
    }
}
