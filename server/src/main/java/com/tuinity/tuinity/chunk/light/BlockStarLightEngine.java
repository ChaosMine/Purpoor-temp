package com.tuinity.tuinity.chunk.light;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapes;

import java.util.*;
import java.util.stream.Collectors;

public final class BlockStarLightEngine extends StarLightEngine {

    public BlockStarLightEngine(final World world) {
        super(false, world);
    }

    @Override
    protected boolean[] getEmptinessMap(final IChunkAccess chunk) {
        return chunk.getBlockEmptinessMap();
    }

    @Override
    protected void setEmptinessMap(final IChunkAccess chunk, final boolean[] to) {
        chunk.setBlockEmptinessMap(to);
    }

    @Override
    protected SWMRNibbleArray[] getNibblesOnChunk(final IChunkAccess chunk) {
        return chunk.getBlockNibbles();
    }

    @Override
    protected void setNibbles(final IChunkAccess chunk, final SWMRNibbleArray[] to) {
        chunk.setBlockNibbles(to);
    }

    @Override
    protected boolean canUseChunk(final IChunkAccess chunk) {
        return chunk.getChunkStatus().isAtLeastStatus(ChunkStatus.LIGHT) && (this.isClientSide || chunk.isLit());
    }

    @Override
    protected void initNibble(final int chunkX, final int chunkY, final int chunkZ, final boolean extrude, final boolean initRemovedNibbles) {
        if (chunkY < this.minLightSection || chunkY > this.maxLightSection || this.getChunkInCache(chunkX, chunkZ) == null) {
            return;
        }

        SWMRNibbleArray nibble = this.getNibbleFromCache(chunkX, chunkY, chunkZ);
        if (nibble == null) {
            if (!initRemovedNibbles) {
                throw new IllegalStateException();
            } else {
                this.setNibbleInCache(chunkX, chunkY, chunkZ, new SWMRNibbleArray());
            }
        } else {
            nibble.setNonNull();
        }
    }

    @Override
    protected final void checkBlock(final ILightAccess lightAccess, final int worldX, final int worldY, final int worldZ) {
        // blocks can change opacity
        // blocks can change emitted light
        // blocks can change direction of propagation

        final int encodeOffset = this.coordinateOffset;
        final int emittedMask = this.emittedLightMask;

        final VariableBlockLightHandler customBlockHandler = ((WorldServer)lightAccess.getWorld()).customBlockLightHandlers;
        final int currentLevel = this.getLightLevel(worldX, worldY, worldZ);
        final IBlockData blockState = this.getBlockState(worldX, worldY, worldZ);
        final int emittedLevel = (customBlockHandler != null ? this.getCustomLightLevel(customBlockHandler, worldX, worldY, worldZ, blockState.getEmittedLight()) : blockState.getEmittedLight()) & emittedMask;

        this.setLightLevel(worldX, worldY, worldZ, emittedLevel);
        // this accounts for change in emitted light that would cause an increase
        if (emittedLevel != 0) {
            this.appendToIncreaseQueue(
                    ((worldX + (worldZ << 6) + (worldY << (6 + 6)) + encodeOffset) & ((1L << (6 + 6 + 16)) - 1))
                            | (emittedLevel & 0xFL) << (6 + 6 + 16)
                            | (((long)ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                            | (blockState.isConditionallyFullOpaque() ? FLAG_HAS_SIDED_TRANSPARENT_BLOCKS : 0)
            );
        }
        // this also accounts for a change in emitted light that would cause a decrease
        // this also accounts for the change of direction of propagation (i.e old block was full transparent, new block is full opaque or vice versa)
        // as it checks all neighbours (even if current level is 0)
        this.appendToDecreaseQueue(
                ((worldX + (worldZ << 6) + (worldY << (6 + 6)) + encodeOffset) & ((1L << (6 + 6 + 16)) - 1))
                        | (currentLevel & 0xFL) << (6 + 6 + 16)
                        | (((long)ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                        // always keep sided transparent false here, new block might be conditionally transparent which would
                        // prevent us from decreasing sources in the directions where the new block is opaque
                        // if it turns out we were wrong to de-propagate the source, the re-propagate logic WILL always
                        // catch that and fix it.
        );
        // re-propagating neighbours (done by the decrease queue) will also account for opacity changes in this block
    }

    protected final BlockPosition.MutableBlockPosition recalcCenterPos = new BlockPosition.MutableBlockPosition();
    protected final BlockPosition.MutableBlockPosition recalcNeighbourPos = new BlockPosition.MutableBlockPosition();

    @Override
    protected int calculateLightValue(final ILightAccess lightAccess, final int worldX, final int worldY, final int worldZ,
                                      final int expect, final VariableBlockLightHandler customBlockLight) {
        final IBlockData centerState = this.getBlockState(worldX, worldY, worldZ);
        int level = centerState.getEmittedLight() & 0xFF;
        if (customBlockLight != null) {
            level = this.getCustomLightLevel(customBlockLight, worldX, worldY, worldZ, level);
        }

        if (level >= (15 - 1) || level > expect) {
            return level;
        }

        final int sectionOffset = this.chunkSectionIndexOffset;
        final IBlockData conditionallyOpaqueState;
        int opacity = centerState.getOpacityIfCached();

        if (opacity == -1) {
            this.recalcCenterPos.setValues(worldX, worldY, worldZ);
            opacity = centerState.getOpacity(lightAccess.getWorld(), this.recalcCenterPos);
            if (centerState.isConditionallyFullOpaque()) {
                conditionallyOpaqueState = centerState;
            } else {
                conditionallyOpaqueState = null;
            }
        } else if (opacity >= 15) {
            return level;
        } else {
            conditionallyOpaqueState = null;
        }
        opacity = Math.max(1, opacity);

        for (final AxisDirection direction : AXIS_DIRECTIONS) {
            final int offX = worldX + direction.x;
            final int offY = worldY + direction.y;
            final int offZ = worldZ + direction.z;

            final int sectionIndex = (offX >> 4) + 5 * (offZ >> 4) + (5 * 5) * (offY >> 4) + sectionOffset;

            final int neighbourLevel = this.getLightLevel(sectionIndex, (offX & 15) | ((offZ & 15) << 4) | ((offY & 15) << 8));

            if ((neighbourLevel - 1) <= level) {
                // don't need to test transparency, we know it wont affect the result.
                continue;
            }

            final IBlockData neighbourState = this.getBlockState(offX, offY ,offZ);

            if (neighbourState.isConditionallyFullOpaque()) {
                // here the block can be conditionally opaque (i.e light cannot propagate from it), so we need to test that
                // we don't read the blockstate because most of the time this is false, so using the faster
                // known transparency lookup results in a net win
                this.recalcNeighbourPos.setValues(offX, offY, offZ);
                final VoxelShape neighbourFace = neighbourState.getCullingFace(lightAccess.getWorld(), this.recalcNeighbourPos, direction.opposite.nms);
                final VoxelShape thisFace = conditionallyOpaqueState == null ? VoxelShapes.empty() : conditionallyOpaqueState.getCullingFace(lightAccess.getWorld(), this.recalcCenterPos, direction.nms);
                if (VoxelShapes.combinationOccludes(thisFace, neighbourFace)) {
                    // not allowed to propagate
                    continue;
                }
            }

            // passed transparency,

            final int calculated = neighbourLevel - opacity;
            level = Math.max(calculated, level);
            if (level > expect) {
                return level;
            }
        }

        return level;
    }

    @Override
    protected void propagateBlockChanges(final ILightAccess lightAccess, final IChunkAccess atChunk, final Set<BlockPosition> positions) {
        for (final BlockPosition pos : positions) {
            this.checkBlock(lightAccess, pos.getX(), pos.getY(), pos.getZ());
        }

        this.performLightDecrease(lightAccess);
    }

    protected Iterator<BlockPosition> getSources(final ILightAccess lightAccess, final IChunkAccess chunk) {
        if (chunk instanceof ProtoChunkExtension || chunk instanceof Chunk) {
            // implementation on Chunk is pretty awful, so write our own here. The big optimisation is
            // skipping empty sections, and the far more optimised reading of types.
            List<BlockPosition> sources = new ObjectArrayList<>();

            int offX = chunk.getPos().x << 4;
            int offZ = chunk.getPos().z << 4;

            final ChunkSection[] sections = chunk.getSections();
            for (int sectionY = 0; sectionY <= 15; ++sectionY) {
                if (sections[sectionY] == null || sections[sectionY].isFullOfAir()) {
                    // no sources in empty sections
                    continue;
                }
                final DataPaletteBlock<IBlockData> section = sections[sectionY].blockIds;
                final int offY = sectionY << 4;

                for (int index = 0; index < (16 * 16 * 16); ++index) {
                    final IBlockData state = section.rawGet(index);
                    if (state.getEmittedLight() <= 0) {
                        continue;
                    }

                    // index = x | (z << 4) | (y << 8)
                    sources.add(new BlockPosition(offX | (index & 15), offY | (index >>> 8), offZ | ((index >>> 4) & 15)));
                }
            }

            final VariableBlockLightHandler customBlockHandler = ((WorldServer)lightAccess.getWorld()).customBlockLightHandlers;
            if (customBlockHandler == null) {
                return sources.iterator();
            }

            final Set<BlockPosition> ret = new HashSet<>(sources);
            ret.addAll(customBlockHandler.getCustomLightPositions(chunk.getPos().x, chunk.getPos().z));

            return ret.iterator();
        } else {
            // world gen and lighting run in parallel, and if lighting keeps up it can be lighting chunks that are
            // being generated. In the nether, lava will add a lot of sources. This resulted in quite a few CME crashes.
            // So all we do spinloop until we can collect a list of sources, and even if it is out of date we will pick up
            // the missing sources from checkBlock.
            for (;;) {
                try {
                    return chunk.getLightSources().collect(Collectors.toList()).iterator();
                } catch (final Exception cme) {
                    continue;
                }
            }
        }
    }

    @Override
    public void lightChunk(final ILightAccess lightAccess, final IChunkAccess chunk, final boolean needsEdgeChecks) {
        // setup sources
        final int emittedMask = this.emittedLightMask;
        final VariableBlockLightHandler customBlockHandler = ((WorldServer)lightAccess.getWorld()).customBlockLightHandlers;
        for (final Iterator<BlockPosition> positions = this.getSources(lightAccess, chunk); positions.hasNext();) {
            final BlockPosition pos = positions.next();
            final IBlockData blockState = this.getBlockState(pos.getX(), pos.getY(), pos.getZ());
            final int emittedLight = (customBlockHandler != null ? this.getCustomLightLevel(customBlockHandler, pos.getX(), pos.getY(), pos.getZ(), blockState.getEmittedLight()) : blockState.getEmittedLight()) & emittedMask;

            if (emittedLight <= this.getLightLevel(pos.getX(), pos.getY(), pos.getZ())) {
                // some other source is brighter
                continue;
            }

            this.appendToIncreaseQueue(
                    ((pos.getX() + (pos.getZ() << 6) + (pos.getY() << (6 + 6)) + this.coordinateOffset) & ((1L << (6 + 6 + 16)) - 1))
                            | (emittedLight & 0xFL) << (6 + 6 + 16)
                            | (((long)ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                            | (blockState.isConditionallyFullOpaque() ? FLAG_HAS_SIDED_TRANSPARENT_BLOCKS : 0)
            );


            // propagation wont set this for us
            this.setLightLevel(pos.getX(), pos.getY(), pos.getZ(), emittedLight);
        }

        if (needsEdgeChecks) {
            // not required to propagate here, but this will reduce the hit of the edge checks
            this.performLightIncrease(lightAccess);

            // verify neighbour edges
            this.checkChunkEdges(lightAccess, chunk, this.minLightSection, this.maxLightSection);
        } else {
            this.propagateNeighbourLevels(lightAccess, chunk, this.minLightSection, this.maxLightSection);

            this.performLightIncrease(lightAccess);
        }
    }
}
