package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.FluidType;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public interface IChunkAccess extends IBlockAccess, IStructureAccess {

    // Paper start
    default boolean generateFlatBedrock() {
        if (this instanceof ProtoChunk) {
            return ((ProtoChunk)this).world.paperConfig.generateFlatBedrock;
        } else if (this instanceof Chunk) {
            return ((Chunk)this).world.paperConfig.generateFlatBedrock;
        } else {
            return false;
        }
    }
    // Paper end

    // Tuinity start
    default com.tuinity.tuinity.chunk.light.SWMRNibbleArray[] getBlockNibbles() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    default void setBlockNibbles(com.tuinity.tuinity.chunk.light.SWMRNibbleArray[] nibbles) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    default com.tuinity.tuinity.chunk.light.SWMRNibbleArray[] getSkyNibbles() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    default void setSkyNibbles(com.tuinity.tuinity.chunk.light.SWMRNibbleArray[] nibbles) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    public default boolean[] getSkyEmptinessMap() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    public default void setSkyEmptinessMap(final boolean[] emptinessMap) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public default boolean[] getBlockEmptinessMap() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public default void setBlockEmptinessMap(final boolean[] emptinessMap) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    // Tuinity end

    IBlockData getType(final int x, final int y, final int z); // Paper
    @Nullable
    IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean flag);

    void setTileEntity(BlockPosition blockposition, TileEntity tileentity);

    void a(Entity entity);

    @Nullable
    default ChunkSection a() {
        ChunkSection[] achunksection = this.getSections();

        for (int i = achunksection.length - 1; i >= 0; --i) {
            ChunkSection chunksection = achunksection[i];

            if (!ChunkSection.a(chunksection)) {
                return chunksection;
            }
        }

        return null;
    }

    default int b() {
        ChunkSection chunksection = this.a();

        return chunksection == null ? 0 : chunksection.getYPosition();
    }

    Set<BlockPosition> c();

    ChunkSection[] getSections();

    Collection<Entry<HeightMap.Type, HeightMap>> f();

    void a(HeightMap.Type heightmap_type, long[] along);

    HeightMap a(HeightMap.Type heightmap_type);

    int getHighestBlock(HeightMap.Type heightmap_type, int i, int j);

    ChunkCoordIntPair getPos();

    void setLastSaved(long i);

    Map<StructureGenerator<?>, StructureStart<?>> h();

    void a(Map<StructureGenerator<?>, StructureStart<?>> map);

    default boolean a(int i, int j) {
        if (i < 0) {
            i = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        for (int k = i; k <= j; k += 16) {
            if (!ChunkSection.a(this.getSections()[k >> 4])) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    BiomeStorage getBiomeIndex();

    void setNeedsSaving(boolean flag);

    boolean isNeedsSaving();

    ChunkStatus getChunkStatus();

    void removeTileEntity(BlockPosition blockposition);

    default void e(BlockPosition blockposition) {
        LogManager.getLogger().warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", blockposition);
    }

    ShortList[] l();

    default void a(short short0, int i) {
        a(this.l(), i).add(short0);
    }

    default void a(NBTTagCompound nbttagcompound) {
        LogManager.getLogger().warn("Trying to set a BlockEntity, but this operation is not supported.");
    }

    @Nullable
    NBTTagCompound i(BlockPosition blockposition);

    @Nullable
    NBTTagCompound j(BlockPosition blockposition);

    default Stream<BlockPosition> getLightSources() { return this.m(); } // Tuinity - OBFHELPER
    Stream<BlockPosition> m();

    TickList<Block> n();

    TickList<FluidType> o();

    ChunkConverter p();

    void setInhabitedTime(long i);

    long getInhabitedTime();

    static ShortList a(ShortList[] ashortlist, int i) {
        if (ashortlist[i] == null) {
            ashortlist[i] = new ShortArrayList();
        }

        return ashortlist[i];
    }

    default boolean isLit() { return this.r(); } // Tuinity - OBFHELPER
    boolean r();

    default void setLit(boolean lit) { this.b(lit); } // Tuinity - OBFHELPER
    void b(boolean flag);

    boolean secureseed_isSlimeChunk();
}
