package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.AxisAlignedBB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SensorNearestLivingEntities extends Sensor<EntityLiving> {

    public SensorNearestLivingEntities() {}

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving) {
        AxisAlignedBB axisalignedbb = entityliving.getBoundingBox().grow(16.0D, 16.0D, 16.0D);
        List<EntityLiving> list = worldserver.a(EntityLiving.class, axisalignedbb, (entityliving1) -> {
            return entityliving1 != entityliving && entityliving1.isAlive();
        });

        entityliving.getClass();
        list.sort(Comparator.comparingDouble(entityliving::h));
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        behaviorcontroller.setMemory(MemoryModuleType.MOBS, list); // Paper - decompile error
        // Tuinity start - remove streams
        List<EntityLiving> visible = new ObjectArrayList<>(list.size());
        for (int index = 0, len = list.size(); index < len; ++index) {
            EntityLiving nearby = list.get(index);
            if (Sensor.a(entityliving, nearby)) { // copied from removed code, make sure to update
                visible.add(nearby);
            }
        }
        behaviorcontroller.setMemory(MemoryModuleType.VISIBLE_MOBS, visible);
        // Tuinity end - remove streams
    }

    @Override
    public Set<MemoryModuleType<?>> a() {
        return ImmutableSet.of(MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS);
    }
}
