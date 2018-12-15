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
        // Tuinity start - remove streams
        List<EntityHuman> nearby = (List)worldserver.getNearbyPlayers(entityliving, entityliving.locX(), entityliving.locY(), entityliving.locZ(),
                16.0, IEntitySelector.g);
        nearby.sort((e1, e2) -> Double.compare(entityliving.getDistanceSquared(e1), entityliving.getDistanceSquared(e2)));
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_PLAYERS, nearby);
        EntityHuman first = null;
        EntityHuman firstNonSpectator = null;
        for (int index = 0, len = nearby.size(); index < len; ++index) {
            EntityHuman entity = nearby.get(index);
            if (!Sensor.a(entityliving, (EntityLiving)entity)) { // copied from removed code, make sure to update
                continue;
            }
            if (first == null) {
                first = entity;
            }
            if (IEntitySelector.f.test(entity)) { // copied from removed code, make sure to update
                firstNonSpectator = entity;
                break;
            }
        }

        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, first);
        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, Optional.ofNullable(firstNonSpectator));
        // Tuinity end - remove streams
    }
}
