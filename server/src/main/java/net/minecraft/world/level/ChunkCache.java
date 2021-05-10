package net.minecraft.world.level;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkEmpty;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidTypes;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChunkCache implements IBlockAccess, ICollisionAccess {

    protected final int a;
    protected final int b;
    protected final IChunkAccess[][] c;
    protected boolean d;
    protected final World e; protected final World getWorld() { return e; } // Paper - OBFHELPER

    // Tuinity start - optimise pathfinder collision detection
    @Override
    public boolean getCubes(Entity entity) {
        return !this.getCollisionsForBlocksOrWorldBorder(entity, entity.getBoundingBox(), null, true, null);
    }

    @Override
    public boolean getCubes(Entity entity, AxisAlignedBB axisalignedbb) {
        return !this.getCollisionsForBlocksOrWorldBorder(entity, axisalignedbb, null, true, null);
    }

    @Override
    public boolean getCubes(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        return !this.getCollisionsForBlocksOrWorldBorder(entity, axisalignedbb, null, true, null);
    }

    public boolean getCollisionsForBlocksOrWorldBorder(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list,
                                                    boolean collidesWithUnloaded,
                                                    java.util.function.BiPredicate<IBlockData, BlockPosition> predicate) {
        boolean ret = false;
        final boolean checkOnly = true;

        if (entity != null) {
            if (this.getWorldBorder().isAlmostCollidingOnBorder(axisalignedbb)) {
                if (checkOnly) {
                    return true;
                } else {
                    net.minecraft.world.phys.shapes.VoxelShapes.addBoxesTo(this.getWorldBorder().getCollisionShape(), list);
                    ret = true;
                }
            }
        }

        // Purpur Rebase - Calculate when needed only
        int minBlockY = net.minecraft.util.MathHelper.floor(axisalignedbb.minY - net.minecraft.server.MCUtil.COLLISION_EPSILON) - 1;
        int maxBlockY = net.minecraft.util.MathHelper.floor(axisalignedbb.maxY + net.minecraft.server.MCUtil.COLLISION_EPSILON) + 1;
        
        BlockPosition.MutableBlockPosition mutablePos = new BlockPosition.MutableBlockPosition();
        net.minecraft.world.phys.shapes.VoxelShapeCollision collisionShape = null;

        // special cases:
        if (minBlockY > 255 || maxBlockY < 0) {
            // no point in checking
            return ret;
        }

        int minBlockX = net.minecraft.util.MathHelper.floor(axisalignedbb.minX - net.minecraft.server.MCUtil.COLLISION_EPSILON) - 1;
        int maxBlockX = net.minecraft.util.MathHelper.floor(axisalignedbb.maxX + net.minecraft.server.MCUtil.COLLISION_EPSILON) + 1;

        int minBlockZ = net.minecraft.util.MathHelper.floor(axisalignedbb.minZ - net.minecraft.server.MCUtil.COLLISION_EPSILON) - 1;
        int maxBlockZ = net.minecraft.util.MathHelper.floor(axisalignedbb.maxZ + net.minecraft.server.MCUtil.COLLISION_EPSILON) + 1;
        // Purpur End

        int minYIterate = Math.max(0, minBlockY);
        int maxYIterate = Math.min(255, maxBlockY);

        int minChunkX = minBlockX >> 4;
        int maxChunkX = maxBlockX >> 4;

        int minChunkZ = minBlockZ >> 4;
        int maxChunkZ = maxBlockZ >> 4;

        // TODO special case single chunk?

        for (int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
            int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0; // coordinate in chunk
            int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15; // coordinate in chunk

            for (int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
                int minX = currChunkX == minChunkX ? minBlockX & 15 : 0; // coordinate in chunk
                int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15; // coordinate in chunk

                int chunkXGlobalPos = currChunkX << 4;
                int chunkZGlobalPos = currChunkZ << 4;
                net.minecraft.world.level.chunk.Chunk chunk = (net.minecraft.world.level.chunk.Chunk)this.getChunkIfLoaded(currChunkX, currChunkZ);

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

                net.minecraft.world.level.chunk.ChunkSection[] sections = chunk.getSections();

                // bound y

                for (int currY = minYIterate; currY <= maxYIterate; ++currY) {
                    net.minecraft.world.level.chunk.ChunkSection section = sections[currY >>> 4];
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
                                if (voxelshape2 != net.minecraft.world.phys.shapes.VoxelShapes.getEmptyShape()) {
                                    VoxelShape voxelshape3 = voxelshape2.offset((double)blockX, (double)blockY, (double)blockZ);

                                    if (predicate != null && !predicate.test(blockData, mutablePos)) {
                                        continue;
                                    }

                                    if (checkOnly) {
                                        if (voxelshape3.intersects(axisalignedbb)) {
                                            return true;
                                        }
                                    } else {
                                        ret |= net.minecraft.world.phys.shapes.VoxelShapes.addBoxesToIfIntersects(voxelshape3, axisalignedbb, list);
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
    // Tuinity end - optimise pathfinder collision detection

    public ChunkCache(World world, BlockPosition blockposition, BlockPosition blockposition1) {
        this.e = world;
        this.a = blockposition.getX() >> 4;
        this.b = blockposition.getZ() >> 4;
        int i = blockposition1.getX() >> 4;
        int j = blockposition1.getZ() >> 4;

        this.c = new IChunkAccess[i - this.a + 1][j - this.b + 1];
        IChunkProvider ichunkprovider = world.getChunkProvider();

        this.d = true;

        int k;
        int l;

        for (k = this.a; k <= i; ++k) {
            for (l = this.b; l <= j; ++l) {
                this.c[k - this.a][l - this.b] = ((WorldServer)world).getChunkProvider().getChunkAtIfLoadedMainThreadNoCache(k, l); // Paper
            }
        }

        for (k = blockposition.getX() >> 4; k <= blockposition1.getX() >> 4; ++k) {
            for (l = blockposition.getZ() >> 4; l <= blockposition1.getZ() >> 4; ++l) {
                IChunkAccess ichunkaccess = this.c[k - this.a][l - this.b];

                if (ichunkaccess != null && !ichunkaccess.a(blockposition.getY(), blockposition1.getY())) {
                    this.d = false;
                    return;
                }
            }
        }

    }

    private IChunkAccess d(BlockPosition blockposition) {
        return this.a(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    private IChunkAccess a(int i, int j) {
        int k = i - this.a;
        int l = j - this.b;

        if (k >= 0 && k < this.c.length && l >= 0 && l < this.c[k].length) { // Paper - if this changes, update getChunkIfLoaded below
            IChunkAccess ichunkaccess = this.c[k][l];

            return (IChunkAccess) (ichunkaccess != null ? ichunkaccess : new ChunkEmpty(this.e, new ChunkCoordIntPair(i, j)));
        } else {
            return new ChunkEmpty(this.e, new ChunkCoordIntPair(i, j));
        }
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.e.getWorldBorder();
    }

    @Override
    public IBlockAccess c(int i, int j) {
        return this.a(i, j);
    }

    // Paper start - if loaded util
    private IChunkAccess getChunkIfLoaded(int x, int z) {
        int k = x - this.a;
        int l = z - this.b;

        if (k >= 0 && k < this.c.length && l >= 0 && l < this.c[k].length) {
            return this.c[k][l];
        }
        return null;
    }
    @Override
    public Fluid getFluidIfLoaded(BlockPosition blockposition) {
        IChunkAccess chunk = getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        return chunk == null ? null : chunk.getFluid(blockposition);
    }

    @Override
    public IBlockData getTypeIfLoaded(BlockPosition blockposition) {
        IChunkAccess chunk = getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        return chunk == null ? null : chunk.getType(blockposition);
    }
    // Paper end

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        IChunkAccess ichunkaccess = this.d(blockposition);

        return ichunkaccess.getTileEntity(blockposition);
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        if (World.isOutsideWorld(blockposition)) {
            return Blocks.AIR.getBlockData();
        } else {
            IChunkAccess ichunkaccess = this.d(blockposition);

            return ichunkaccess.getType(blockposition);
        }
    }

    @Override
    public Stream<VoxelShape> c(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        return Stream.empty();
    }

    @Override
    public Stream<VoxelShape> d(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        return this.b(entity, axisalignedbb);
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        if (World.isOutsideWorld(blockposition)) {
            return FluidTypes.EMPTY.h();
        } else {
            IChunkAccess ichunkaccess = this.d(blockposition);

            return ichunkaccess.getFluid(blockposition);
        }
    }
}
