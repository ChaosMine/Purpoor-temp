package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.EntityItem;

public class SensorNearestItems extends Sensor<EntityInsentient> {

    public SensorNearestItems() {}

    @Override
    public Set<MemoryModuleType<?>> a() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
    }

    protected void a(WorldServer worldserver, EntityInsentient entityinsentient) {
        BehaviorController<?> behaviorcontroller = entityinsentient.getBehaviorController();
        // Tuinity start - remove streams
        List<EntityItem> list = worldserver.a(EntityItem.class, entityinsentient.getBoundingBox().grow(8.0D, 4.0D, 8.0D), (EntityItem item) -> {
            return entityinsentient.i(item.getItemStack()) && item.a((Entity)entityinsentient, 9.0D); // copied from removed code, make sure to update - move here so we sort less
        });

        list.sort(Comparator.comparingDouble(entityinsentient::h)); // better to take the sort perf hit than using line of sight more than we need to.
        EntityItem nearest = null;
        for (int index = 0, len = list.size(); index < len; ++index) {
            EntityItem item = list.get(index);
            if (entityinsentient.hasLineOfSight(item)) {
                nearest = item;
                break;
            }
        }

        Optional<EntityItem> optional = Optional.ofNullable(nearest);
        // Tuinity end - remove streams

        behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, optional);
    }
}
