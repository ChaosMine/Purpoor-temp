package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;
import java.util.Set;

public class SensorVillagerBabies extends Sensor<EntityLiving> {

    public SensorVillagerBabies() {}

    @Override
    public Set<MemoryModuleType<?>> a() {
        return ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving) {
        entityliving.getBehaviorController().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, this.a(entityliving)); // Tuinity - decompile fix
    }

    private List<EntityLiving> a(EntityLiving entityliving) {
        // Tuinity start - remove streams
        List<EntityLiving> nearby = this.c(entityliving); // copied from removed code, make sure to update
        List<EntityLiving> ret = new ObjectArrayList<>();

        for (int index = 0, len = nearby.size(); index < len; ++index) {
            EntityLiving entity = nearby.get(index);
            if (this.b(entity)) { // copied from removed code, make sure to update
                ret.add(entity);
            }
        }

        return ret;
        // Tuinity end - remove streams
    }

    private boolean b(EntityLiving entityliving) {
        return entityliving.getEntityType() == EntityTypes.VILLAGER && entityliving.isBaby();
    }

    private List<EntityLiving> c(EntityLiving entityliving) {
        return entityliving.getBehaviorController().getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(Lists.newArrayList());
    }
}
