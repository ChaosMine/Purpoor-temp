package net.minecraft.world.level.levelgen;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.MCUtil;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.stats.ServerStatisticManager;
import net.minecraft.stats.StatisticList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.monster.EntityPhantom;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.MobSpawner;
import net.minecraft.world.level.SpawnerCreature;
import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnerPhantom implements MobSpawner {
    private int spawnDelay;

    public MobSpawnerPhantom() {}

    @Override
    public int a(WorldServer world, boolean allowMonsters, boolean allowAnimals) {
        // Purpur start - rewrite entire thing
        if (!allowMonsters) {
            return 0;
        }

        if (!world.getGameRules().getBoolean(GameRules.DO_INSOMNIA)) {
            return 0;
        }

        --this.spawnDelay;
        if (this.spawnDelay > 0) {
            return 0;
        }
        this.spawnDelay += world.purpurConfig.phantomSpawnDelayMin + world.random.nextInt(world.purpurConfig.phantomSpawnDelayMax - world.purpurConfig.phantomSpawnDelayMin);

        if (!world.getDimensionManager().hasSkyLight() || world.getSkyDarkness() < world.purpurConfig.phantomSpawnMinSkyDarkness) {
            return 0;
        }

        int numberSpawnsAttempted = 0;
        for (EntityPlayer player : world.getPlayers()) {
            if (player.isSpectator() || (player.isCreative() && world.paperConfig.phantomIgnoreCreative)) { // Paper
                continue;
            }

            BlockPosition playerPos = player.getChunkCoordinates();
            if (playerPos.getY() < world.getSeaLevel() && world.purpurConfig.phantomSpawnOnlyAboveSeaLevel) {
                continue;
            }

            if (!world.isSkyVisible(playerPos) && world.purpurConfig.phantomSpawnOnlyWithVisibleSky) {
                continue;
            }

            DifficultyDamageScaler dmgScaler = world.getDamageScaler(playerPos);
            if (!dmgScaler.isHarderThan(world.random.nextFloat() * (float) world.purpurConfig.phantomSpawnLocalDifficultyChance)) {
                continue;
            }

            ServerStatisticManager stats = player.getStatisticManager();
            int timeSinceRest = MathHelper.clamp(stats.getStatisticValue(StatisticList.CUSTOM.get(StatisticList.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
            if (world.random.nextInt(timeSinceRest) < world.purpurConfig.phantomSpawnMinTimeSinceSlept) {
                continue;
            }

            BlockPosition spawnPos = playerPos
                    .up(world.purpurConfig.phantomSpawnMinOverhead + world.random.nextInt(world.purpurConfig.phantomSpawnMaxOverhead - world.purpurConfig.phantomSpawnMinOverhead))
                    .east(-world.purpurConfig.phantomSpawnOverheadRadius + world.random.nextInt(world.purpurConfig.phantomSpawnOverheadRadius + 1))
                    .south(-world.purpurConfig.phantomSpawnOverheadRadius + world.random.nextInt(world.purpurConfig.phantomSpawnOverheadRadius + 1));

            if (!SpawnerCreature.canSpawn(world, spawnPos, world.getType(spawnPos), world.getFluid(spawnPos), EntityTypes.PHANTOM)) {
                continue;
            }

            int difficulty = dmgScaler.getGlobalDifficulty().getId();
            int spawnAttempts = world.purpurConfig.phantomSpawnMinPerAttempt + world.random.nextInt((world.purpurConfig.phantomSpawnMaxPerAttempt < 0 ? difficulty : world.purpurConfig.phantomSpawnMaxPerAttempt) + world.purpurConfig.phantomSpawnMinPerAttempt);

            GroupDataEntity groupData = null;
            for (int count = 0; count < spawnAttempts; ++count) {
                // Paper start
                PhantomPreSpawnEvent event = new PhantomPreSpawnEvent(MCUtil.toLocation(world, spawnPos), player.getBukkitEntity(), CreatureSpawnEvent.SpawnReason.NATURAL);
                if (!event.callEvent()) {
                    if (event.shouldAbortSpawn()) {
                        break;
                    }
                    continue;
                }
                // Paper end

                EntityPhantom phantom = EntityTypes.PHANTOM.create(world);
                if (phantom == null) {
                    continue;
                }

                phantom.setSpawningEntity(player.getUniqueID()); // Paper
                phantom.setPositionRotation(spawnPos, 0.0F, 0.0F);
                groupData = phantom.prepare(world, dmgScaler, EnumMobSpawn.NATURAL, groupData, null);
                world.addEntity(phantom, CreatureSpawnEvent.SpawnReason.NATURAL); // CraftBukkit
            }

            numberSpawnsAttempted += spawnAttempts;
        }

        return numberSpawnsAttempted;
        // Purpur end - rewrite entire thing
    }
}
