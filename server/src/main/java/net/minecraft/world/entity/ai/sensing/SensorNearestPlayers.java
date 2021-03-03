package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.EntityHuman;

public class SensorNearestPlayers extends Sensor<EntityLiving> {

    public SensorNearestPlayers() {}

    @Override
    public Set<MemoryModuleType<?>> a() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving) {
        // Paper start - remove streams in favour of lists
        List<EntityHuman> players = new java.util.ArrayList<>(worldserver.getPlayers());
        players.removeIf(player -> !IEntitySelector.notSpectator().test(player) || !entityliving.a(player, 16.0D)); // Paper - removeIf only re-allocates once compared to iterator
        players.sort(Comparator.comparingDouble(entityliving::h));

        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();
        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);

        EntityHuman nearest = null, nearestTargetable = null;
        for (EntityHuman player : players) {
            if (Sensor.a(entityliving, player)) {
                if (nearest == null) nearest = player;
                if (IEntitySelector.canAITarget().test(player)) {
                    nearestTargetable = player;
                    break; // Both variables are assigned, no reason to loop further
                }
            }
        }
        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, nearest);
        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, nearestTargetable);
        // Paper end
    }
}
