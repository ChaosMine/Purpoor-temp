package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.function.Predicate;

public class BehaviorLookInteract extends Behavior<EntityLiving> {

    private final EntityTypes<?> b;
    private final int c; private final int getMaxRange() { return this.c; } // Tuinity - OBFHELPER
    private final Predicate<EntityLiving> d;
    private final Predicate<EntityLiving> e;

    public BehaviorLookInteract(EntityTypes<?> entitytypes, int i, Predicate<EntityLiving> predicate, Predicate<EntityLiving> predicate1) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_MOBS, MemoryStatus.VALUE_PRESENT));
        this.b = entitytypes;
        this.c = i * i;
        this.d = predicate1;
        this.e = predicate;
    }

    public BehaviorLookInteract(EntityTypes<?> entitytypes, int i) {
        this(entitytypes, i, (entityliving) -> {
            return true;
        }, (entityliving) -> {
            return true;
        });
    }

    @Override
    public boolean a(WorldServer worldserver, EntityLiving entityliving) {
        // Tuinity start - remove streams
        if (!this.e.test(entityliving)) {
            return false;
        }

        List<EntityLiving> list = this.b(entityliving);
        for (int index = 0, len = list.size(); index < len; ++index) {
            if (this.a(list.get(index))) {
                return true;
            }
        }

        return false;
        // Tuinity end - remove streams
    }

    @Override
    public void a(WorldServer worldserver, EntityLiving entityliving, long i) {
        super.a(worldserver, entityliving, i);
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        // Tuinity start - remove streams
        List<EntityLiving> inLOS = behaviorcontroller.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(null);
        if (inLOS != null) {
            double maxRangeSquared = this.getMaxRange();
            for (int index = 0, len = inLOS.size(); index < len; ++index) {
                EntityLiving entity = inLOS.get(index);
                if (!this.canTarget(entity)) {
                    continue;
                }
                double distance = entity.getDistanceSquared(entityliving.locX(), entityliving.locY(), entityliving.locZ());
                if (distance > maxRangeSquared) {
                    continue;
                }
                behaviorcontroller.setMemory(MemoryModuleType.INTERACTION_TARGET, entity); // Tuinity - decompile fix
                behaviorcontroller.setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorPositionEntity(entity, true))); // Tuinity - decompile fix
                break;
            }
        }
        // Tuinity end - remove streams
    }

    private final boolean canTarget(EntityLiving entityliving) { return this.a(entityliving); } // Tuinity - OBFHELPER
    private boolean a(EntityLiving entityliving) {
        return this.b.equals(entityliving.getEntityType()) && this.d.test(entityliving);
    }

    private List<EntityLiving> b(EntityLiving entityliving) {
        return (List) entityliving.getBehaviorController().getMemory(MemoryModuleType.VISIBLE_MOBS).get();
    }
}
