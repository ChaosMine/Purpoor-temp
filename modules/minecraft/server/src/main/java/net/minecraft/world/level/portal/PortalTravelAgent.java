package net.minecraft.world.level.portal;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.BlockUtil;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.ai.village.poi.VillagePlace;
import net.minecraft.world.entity.ai.village.poi.VillagePlaceRecord;
import net.minecraft.world.entity.ai.village.poi.VillagePlaceType;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.block.BlockPortal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.HeightMap;

import java.util.Iterator;
import java.util.Optional;

public class PortalTravelAgent {

    private final WorldServer world;

    public PortalTravelAgent(WorldServer worldserver) {
        this.world = worldserver;
    }

    public Optional<BlockUtil.Rectangle> findPortal(BlockPosition blockposition, boolean flag) {
        // CraftBukkit start
        return findPortal(blockposition, flag ? world.paperConfig.portalCreateRadius : world.paperConfig.portalSearchRadius); // Paper - search Radius
    }

    public Optional<BlockUtil.Rectangle> findPortal(BlockPosition blockposition, int i) {
        VillagePlace villageplace = this.world.y();
        // int i = flag ? 16 : 128;
        // CraftBukkit end

        // Tuinity start - optimise portals
        //villageplace.a(this.world, blockposition, i);
        Optional<VillagePlaceRecord> optional;
        java.util.List<VillagePlaceRecord> records = new ObjectArrayList<>();
        com.tuinity.tuinity.util.PoiAccess.findClosestPoiDataRecords(villageplace,
                (VillagePlaceType villageplacetype) -> {
                    return villageplacetype == VillagePlaceType.v; // this should break this entire diff on update if it changes, so TODO check that on diff break
                },
                (BlockPosition pos) -> {
                    net.minecraft.world.level.chunk.IChunkAccess lowest = this.world.getChunkAt(pos.getX() >> 4, pos.getZ() >> 4, net.minecraft.world.level.chunk.ChunkStatus.EMPTY);
                    if (!lowest.getChunkStatus().isAtLeastStatus(net.minecraft.world.level.chunk.ChunkStatus.FULL)) {
                        // why would we generate the chunk?
                        return false;
                    }
                    return lowest.getType(pos).contains(BlockProperties.E); // this should break this entire diff on update if it changes, so TODO check that on diff break
                }, blockposition, i, Double.MAX_VALUE, VillagePlace.Occupancy.ANY, true, records);
        // this gets us most of the way there, but we bias towards lower y values.
        VillagePlaceRecord lowestYRecord = null;
        for (VillagePlaceRecord record : records) {
            if (lowestYRecord == null) {
                lowestYRecord = record;
            } else if (lowestYRecord.getPosition().getY() > record.getPosition().getY()) {
                lowestYRecord = record;
            }
        }
        // now we're done
        optional = Optional.ofNullable(lowestYRecord);
        // Tuinity end - optimise portals

        return optional.map((villageplacerecord) -> {
            BlockPosition blockposition1 = villageplacerecord.f();

            this.world.getChunkProvider().addTicket(TicketType.PORTAL, new ChunkCoordIntPair(blockposition1), 3, blockposition1);
            IBlockData iblockdata = this.world.getType(blockposition1);

            return BlockUtil.a(blockposition1, iblockdata.get(BlockProperties.E), 21, EnumDirection.EnumAxis.Y, 21, (blockposition2) -> {
                return this.world.getType(blockposition2) == iblockdata;
            });
        });
    }

    public Optional<BlockUtil.Rectangle> createPortal(BlockPosition blockposition, EnumDirection.EnumAxis enumdirection_enumaxis) {
        // CraftBukkit start
        return this.createPortal(blockposition, enumdirection_enumaxis, null, 16);
    }

    public Optional<BlockUtil.Rectangle> createPortal(BlockPosition blockposition, EnumDirection.EnumAxis enumdirection_enumaxis, net.minecraft.world.entity.Entity entity, int createRadius) {
        // CraftBukkit end
        EnumDirection enumdirection = EnumDirection.a(EnumDirection.EnumAxisDirection.POSITIVE, enumdirection_enumaxis);
        double d0 = -1.0D;
        BlockPosition blockposition1 = null;
        double d1 = -1.0D;
        BlockPosition blockposition2 = null;
        WorldBorder worldborder = this.world.getWorldBorder();
        int i = this.world.getHeight() - 1;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = blockposition.i();
        Iterator iterator = BlockPosition.a(blockposition, createRadius, EnumDirection.EAST, EnumDirection.SOUTH).iterator(); // CraftBukkit

        int j;

        while (iterator.hasNext()) {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition1 = (BlockPosition.MutableBlockPosition) iterator.next();

            j = Math.min(i, this.world.a(HeightMap.Type.MOTION_BLOCKING, blockposition_mutableblockposition1.getX(), blockposition_mutableblockposition1.getZ()));
            boolean flag = true;

            if (worldborder.a(blockposition_mutableblockposition1) && worldborder.a(blockposition_mutableblockposition1.c(enumdirection, 1))) {
                blockposition_mutableblockposition1.c(enumdirection.opposite(), 1);

                for (int k = j; k >= 0; --k) {
                    blockposition_mutableblockposition1.p(k);
                    if (this.world.isEmpty(blockposition_mutableblockposition1)) {
                        int l;

                        for (l = k; k > 0 && this.world.isEmpty(blockposition_mutableblockposition1.c(EnumDirection.DOWN)); --k) {
                            ;
                        }

                        if (k + 4 <= i) {
                            int i1 = l - k;

                            if (i1 <= 0 || i1 >= 3) {
                                blockposition_mutableblockposition1.p(k);
                                if (this.a(blockposition_mutableblockposition1, blockposition_mutableblockposition, enumdirection, 0)) {
                                    double d2 = blockposition.j(blockposition_mutableblockposition1);

                                    if (this.a(blockposition_mutableblockposition1, blockposition_mutableblockposition, enumdirection, -1) && this.a(blockposition_mutableblockposition1, blockposition_mutableblockposition, enumdirection, 1) && (d0 == -1.0D || d0 > d2)) {
                                        d0 = d2;
                                        blockposition1 = blockposition_mutableblockposition1.immutableCopy();
                                    }

                                    if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
                                        d1 = d2;
                                        blockposition2 = blockposition_mutableblockposition1.immutableCopy();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == -1.0D && d1 != -1.0D) {
            blockposition1 = blockposition2;
            d0 = d1;
        }

        int j1;

        org.bukkit.craftbukkit.util.BlockStateListPopulator blockList = new org.bukkit.craftbukkit.util.BlockStateListPopulator(this.world); // CraftBukkit - Use BlockStateListPopulator
        if (d0 == -1.0D) {
            blockposition1 = (new BlockPosition(blockposition.getX(), MathHelper.clamp(blockposition.getY(), 70, this.world.getHeight() - 10), blockposition.getZ())).immutableCopy();
            EnumDirection enumdirection1 = enumdirection.g();

            if (!worldborder.a(blockposition1)) {
                return Optional.empty();
            }

            for (j1 = -1; j1 < 2; ++j1) {
                for (j = 0; j < 2; ++j) {
                    for (int k1 = -1; k1 < 3; ++k1) {
                        IBlockData iblockdata = k1 < 0 ? Blocks.OBSIDIAN.getBlockData() : Blocks.AIR.getBlockData();

                        blockposition_mutableblockposition.a((BaseBlockPosition) blockposition1, j * enumdirection.getAdjacentX() + j1 * enumdirection1.getAdjacentX(), k1, j * enumdirection.getAdjacentZ() + j1 * enumdirection1.getAdjacentZ());
                        blockList.setTypeAndData(blockposition_mutableblockposition, iblockdata, 3); // CraftBukkit
                    }
                }
            }
        }

        for (int l1 = -1; l1 < 3; ++l1) {
            for (j1 = -1; j1 < 4; ++j1) {
                if (l1 == -1 || l1 == 2 || j1 == -1 || j1 == 3) {
                    blockposition_mutableblockposition.a((BaseBlockPosition) blockposition1, l1 * enumdirection.getAdjacentX(), j1, l1 * enumdirection.getAdjacentZ());
                    blockList.setTypeAndData(blockposition_mutableblockposition, Blocks.OBSIDIAN.getBlockData(), 3); // CraftBukkit
                }
            }
        }

        IBlockData iblockdata1 = Blocks.NETHER_PORTAL.getBlockData().set(BlockPortal.AXIS, enumdirection_enumaxis);

        for (j1 = 0; j1 < 2; ++j1) {
            for (j = 0; j < 3; ++j) {
                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition1, j1 * enumdirection.getAdjacentX(), j, j1 * enumdirection.getAdjacentZ());
                blockList.setTypeAndData(blockposition_mutableblockposition, iblockdata1, 18); // CraftBukkit
            }
        }

        // CraftBukkit start
        org.bukkit.World bworld = this.world.getWorld();
        org.bukkit.event.world.PortalCreateEvent event = new org.bukkit.event.world.PortalCreateEvent((java.util.List<org.bukkit.block.BlockState>) (java.util.List) blockList.getList(), bworld, (entity == null) ? null : entity.getBukkitEntity(), org.bukkit.event.world.PortalCreateEvent.CreateReason.NETHER_PAIR);

        this.world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return Optional.empty();
        }
        blockList.updateList();
        // CraftBukkit end
        return Optional.of(new BlockUtil.Rectangle(blockposition1.immutableCopy(), 2, 3));
    }

    private boolean a(BlockPosition blockposition, BlockPosition.MutableBlockPosition blockposition_mutableblockposition, EnumDirection enumdirection, int i) {
        EnumDirection enumdirection1 = enumdirection.g();

        for (int j = -1; j < 3; ++j) {
            for (int k = -1; k < 4; ++k) {
                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection.getAdjacentX() * j + enumdirection1.getAdjacentX() * i, k, enumdirection.getAdjacentZ() * j + enumdirection1.getAdjacentZ() * i);
                if (k < 0 && !this.world.getType(blockposition_mutableblockposition).getMaterial().isBuildable()) {
                    return false;
                }

                if (k >= 0 && !this.world.isEmpty(blockposition_mutableblockposition)) {
                    return false;
                }
            }
        }

        return true;
    }
}
