package net.minecraft.world.level.chunk.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import net.minecraft.SharedConstants;
import net.minecraft.SystemUtils;
import net.minecraft.core.SectionPosition;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class RegionFileSection<R> extends RegionFileCache implements AutoCloseable { // Paper - nuke IOWorker

    private static final Logger LOGGER = LogManager.getLogger();
    // Paper - nuke IOWorker
    private final Long2ObjectMap<Optional<R>> c = new Long2ObjectOpenHashMap(); protected final Long2ObjectMap<Optional<R>> getDataBySection() { return this.c; } // Tuinity - OBFHELPER
    public final LongLinkedOpenHashSet d = new LongLinkedOpenHashSet();  protected final LongLinkedOpenHashSet getDirtySections() { return this.d; } // Paper - private -> public // Tuinity - OBFHELPER
    private final Function<Runnable, Codec<R>> e;
    private final Function<Runnable, R> f;
    private final DataFixer g;
    private final DataFixTypes h;

    public RegionFileSection(File file, Function<Runnable, Codec<R>> function, Function<Runnable, R> function1, DataFixer datafixer, DataFixTypes datafixtypes, boolean flag) {
        super(file, flag); // Paper - nuke IOWorker
        this.e = function;
        this.f = function1;
        this.g = datafixer;
        this.h = datafixtypes;
        //this.b = new IOWorker(file, flag, file.getName()); // Paper - nuke IOWorker
    }

    protected void a(BooleanSupplier booleansupplier) {
        while (!this.d.isEmpty() && booleansupplier.getAsBoolean()) {
            ChunkCoordIntPair chunkcoordintpair = SectionPosition.a(this.d.firstLong()).r(); // Paper - conflict here to avoid obfhelpers

            this.d(chunkcoordintpair);
        }

    }

    // Tuinity start - actually unload POI data
    public void unloadData(long coordinate) {
        ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(coordinate);
        this.writeDirtyData(chunkPos);

        Long2ObjectMap<Optional<R>> data = this.getDataBySection();
        int before = data.size();

        for (int section = 0; section < 16; ++section) {
            data.remove(SectionPosition.asLong(chunkPos.x, section, chunkPos.z));
        }

        if (before != data.size()) {
            this.onUnload(coordinate);
        }
    }

    protected void onUnload(long coordinate) {}

    public boolean isEmpty(long coordinate) {
        Long2ObjectMap<Optional<R>> data = this.getDataBySection();
        int x = net.minecraft.server.MCUtil.getCoordinateX(coordinate);
        int z = net.minecraft.server.MCUtil.getCoordinateZ(coordinate);
        for (int section = 0; section < 16; ++section) {
            Optional<R> optional = data.get(SectionPosition.asLong(x, section, z));
            if (optional != null && optional.orElse(null) != null) {
                return false;
            }
        }

        return true;
    }
    // Tuinity end - actually unload POI data

    @Nullable public final Optional<R> getIfLoaded(long value) { return this.c(value); } // Tuinity - OBFHELPER // Tuinity - OBFHELPER
    @Nullable protected Optional<R> c(long i) { // Tuinity - OBFHELPER
        return (Optional) this.c.get(i);
    }

    public final Optional<R> getOrLoad(long coordinate) { return this.d(coordinate); } // Tuinity - OBFHELPER
    protected Optional<R> d(long i) {
        SectionPosition sectionposition = SectionPosition.a(i);

        if (this.b(sectionposition)) {
            return Optional.empty();
        } else {
            Optional<R> optional = this.c(i);

            if (optional != null) {
                return optional;
            } else {
                this.b(sectionposition.r());
                optional = this.c(i);
                if (optional == null) {
                    throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException()));
                } else {
                    return optional;
                }
            }
        }
    }

    protected boolean b(SectionPosition sectionposition) {
        return World.b(SectionPosition.c(sectionposition.b()));
    }

    protected R e(long i) {
        Optional<R> optional = this.d(i);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            R r0 = this.f.apply(() -> {
                this.a(i);
            });

            this.c.put(i, Optional.of(r0));
            return r0;
        }
    }

    private void b(ChunkCoordIntPair chunkcoordintpair) {
        // Paper start - load data in function
        this.loadInData(chunkcoordintpair, this.c(chunkcoordintpair));
    }
    public void loadInData(ChunkCoordIntPair chunkPos, NBTTagCompound compound) {
        this.a(chunkPos, DynamicOpsNBT.a, compound);
        // Paper end
    }

    @Nullable
    private NBTTagCompound c(ChunkCoordIntPair chunkcoordintpair) {
        try {
            return this.read(chunkcoordintpair); // Paper - nuke IOWorker
        } catch (IOException ioexception) {
            RegionFileSection.LOGGER.error("Error reading chunk {} data from disk", chunkcoordintpair, ioexception);
            return null;
        }
    }

    private <T> void a(ChunkCoordIntPair chunkcoordintpair, DynamicOps<T> dynamicops, @Nullable T t0) {
        if (t0 == null) {
            for (int i = 0; i < 16; ++i) {
                this.c.put(SectionPosition.a(chunkcoordintpair, i).s(), Optional.empty());
            }
        } else {
            Dynamic<T> dynamic = new Dynamic(dynamicops, t0);
            int j = a(dynamic);
            int k = SharedConstants.getGameVersion().getWorldVersion();
            boolean flag = j != k;
            Dynamic<T> dynamic1 = this.g.update(this.h.a(), dynamic, j, k);
            OptionalDynamic<T> optionaldynamic = dynamic1.get("Sections");

            for (int l = 0; l < 16; ++l) {
                long i1 = SectionPosition.a(chunkcoordintpair, l).s();
                Optional<R> optional = optionaldynamic.get(Integer.toString(l)).result().flatMap((dynamic2) -> {
                    DataResult dataresult = ((Codec) this.e.apply(() -> {
                        this.a(i1);
                    })).parse(dynamic2);
                    Logger logger = RegionFileSection.LOGGER;

                    logger.getClass();
                    return dataresult.resultOrPartial(logger::error);
                });

                this.c.put(i1, optional);
                optional.ifPresent((object) -> {
                    this.b(i1);
                    if (flag) {
                        this.a(i1);
                    }

                });
            }
        }
        if (this instanceof net.minecraft.world.entity.ai.village.poi.VillagePlace) { ((net.minecraft.world.entity.ai.village.poi.VillagePlace)this).queueUnload(chunkcoordintpair.pair(), net.minecraft.server.MinecraftServer.currentTickLong + 1); } // Tuinity - unload POI data

    }

    private void d(ChunkCoordIntPair chunkcoordintpair) {
        Dynamic<NBTBase> dynamic = this.a(chunkcoordintpair, DynamicOpsNBT.a); // Paper - conflict here to avoid adding obfhelpers :)
        NBTBase nbtbase = (NBTBase) dynamic.getValue();

        if (nbtbase instanceof NBTTagCompound) {
            try { this.write(chunkcoordintpair, (NBTTagCompound) nbtbase); } catch (IOException ioexception) { RegionFileSection.LOGGER.error("Error writing data to disk", ioexception); } // Paper - nuke IOWorker // TODO make this write async
        } else {
            RegionFileSection.LOGGER.error("Expected compound tag, got {}", nbtbase);
        }

    }

    // Paper start - internal get data function, copied from above
    private NBTTagCompound getDataInternal(ChunkCoordIntPair chunkcoordintpair) {
        Dynamic<NBTBase> dynamic = this.a(chunkcoordintpair, DynamicOpsNBT.a);
        NBTBase nbtbase = (NBTBase) dynamic.getValue();

        if (nbtbase instanceof NBTTagCompound) {
            return (NBTTagCompound)nbtbase;
        } else {
            RegionFileSection.LOGGER.error("Expected compound tag, got {}", nbtbase);
        }
        return null;
    }
    // Paper end

    private <T> Dynamic<T> a(ChunkCoordIntPair chunkcoordintpair, DynamicOps<T> dynamicops) {
        Map<T, T> map = Maps.newHashMap();

        for (int i = 0; i < 16; ++i) {
            long j = SectionPosition.a(chunkcoordintpair, i).s();

            this.d.remove(j);
            Optional<R> optional = (Optional) this.c.get(j);

            if (optional != null && optional.isPresent()) {
                DataResult<T> dataresult = ((Codec) this.e.apply(() -> {
                    this.a(j);
                })).encodeStart(dynamicops, optional.get());
                String s = Integer.toString(i);
                Logger logger = RegionFileSection.LOGGER;

                logger.getClass();
                dataresult.resultOrPartial(logger::error).ifPresent((object) -> {
                    map.put(dynamicops.createString(s), object);
                });
            }
        }

        return new Dynamic(dynamicops, dynamicops.createMap(ImmutableMap.of(dynamicops.createString("Sections"), dynamicops.createMap(map), dynamicops.createString("DataVersion"), dynamicops.createInt(SharedConstants.getGameVersion().getWorldVersion()))));
    }

    protected void b(long i) {}

    protected void a(long i) {
        Optional<R> optional = (Optional) this.c.get(i);

        if (optional != null && optional.isPresent()) {
            this.d.add(i);
        } else {
            RegionFileSection.LOGGER.warn("No data for position: {}", SectionPosition.a(i));
        }
    }

    private static int a(Dynamic<?> dynamic) {
        return dynamic.get("DataVersion").asInt(1945);
    }

    public final void writeDirtyData(ChunkCoordIntPair chunkcoordintpair) { this.a(chunkcoordintpair); } // Tuinity - OBFHELPER
    public void a(ChunkCoordIntPair chunkcoordintpair) {
        if (!this.d.isEmpty()) {
            for (int i = 0; i < 16; ++i) {
                long j = SectionPosition.a(chunkcoordintpair, i).s();  // Paper - conflict here to avoid obfhelpers

                if (this.d.contains(j)) { // Paper - conflict here to avoid obfhelpers
                    this.d(chunkcoordintpair);
                    return;
                }
            }
        }

    }

//    Paper start - nuke IOWorker
//    public void close() throws IOException {
//        this.b.close();
//    }
//    Paper end

    // Paper start - get data function
    public NBTTagCompound getData(ChunkCoordIntPair chunkcoordintpair) {
        // Note: Copied from above
        // This is checking if the data exists, then it builds it later in getDataInternal(ChunkCoordIntPair)
        if (!this.d.isEmpty()) {
            for (int i = 0; i < 16; ++i) {
                long j = SectionPosition.a(chunkcoordintpair, i).s();

                if (this.d.contains(j)) {
                    return this.getDataInternal(chunkcoordintpair);
                }
            }
        }
        return null;
    }
    // Paper end
}
