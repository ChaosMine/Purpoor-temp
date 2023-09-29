package net.minecraft.world.entity.npc;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityPositionTypes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.ai.village.poi.VillagePlace;
import net.minecraft.world.entity.ai.village.poi.VillagePlaceType;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.phys.AxisAlignedBB;

import java.util.List;
import java.util.Random;

public class MobSpawnerCat implements MobSpawner {

    private int a;

    public MobSpawnerCat() {}

    @Override
    public int a(WorldServer worldserver, boolean flag, boolean flag1) {
        if (flag1 && worldserver.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            --this.a;
            if (this.a > 0) {
                return 0;
            } else {
                this.a = worldserver.purpurConfig.catSpawnDelay; // Purpur;
                EntityPlayer entityplayer = worldserver.q_();

                if (entityplayer == null) {
                    return 0;
                } else {
                    Random random = worldserver.random;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPosition blockposition = entityplayer.getChunkCoordinates().b(i, 0, j);

                    if (!worldserver.isAreaLoaded(blockposition.getX() - 10, blockposition.getY() - 10, blockposition.getZ() - 10, blockposition.getX() + 10, blockposition.getY() + 10, blockposition.getZ() + 10)) {
                        return 0;
                    } else {
                        if (SpawnerCreature.a(EntityPositionTypes.Surface.ON_GROUND, worldserver, blockposition, EntityTypes.CAT)) {
                            if (worldserver.a(blockposition, 2)) {
                                return this.a(worldserver, blockposition);
                            }

                            if (worldserver.getStructureManager().a(blockposition, true, StructureGenerator.SWAMP_HUT).e()) {
                                return this.b(worldserver, blockposition);
                            }
                        }

                        return 0;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    private int a(WorldServer worldserver, BlockPosition blockposition) {
        // Purpur start
        int range = worldserver.purpurConfig.catSpawnVillageScanRange;
        if (range <= 0) return 0;
        if (worldserver.getPoiStorage().count(VillagePlaceType.home().predicate(), blockposition, range, VillagePlace.Occupancy.IS_OCCUPIED) > 4L) {
            List<EntityCat> list = worldserver.getEntitiesInAABB(EntityCat.class, (new AxisAlignedBB(blockposition)).grow(range, 8.0D, range));
            // Purpur end

            if (list.size() < 5) {
                return this.a(blockposition, worldserver);
            }
        }

        return 0;
    }

    private int b(WorldServer worldserver, BlockPosition blockposition) {
        // Purpur start
        int range = worldserver.purpurConfig.catSpawnSwampHutScanRange;
        if (range <= 0) return 0;
        List<EntityCat> list = worldserver.getEntitiesInAABB(EntityCat.class, (new AxisAlignedBB(blockposition)).grow(range, 8.0D, range));
        // Purpur end
        return list.size() < 1 ? this.a(blockposition, worldserver) : 0;
    }

    private int a(BlockPosition blockposition, WorldServer worldserver) {
        EntityCat entitycat = EntityTypes.CAT.a(worldserver);

        if (entitycat == null) {
            return 0;
        } else {
            entitycat.setPositionRotation(blockposition, 0.0F, 0.0F); // Purpur
            entitycat.prepare(worldserver, worldserver.getDamageScaler(blockposition), EnumMobSpawn.NATURAL, null, null);
            //entitycat.setPositionRotation(blockposition, 0.0F, 0.0F); // Purpur - move up - fixes non black cat types spawning inside swamp huts
            worldserver.addAllEntities(entitycat);
            return 1;
        }
    }
}
