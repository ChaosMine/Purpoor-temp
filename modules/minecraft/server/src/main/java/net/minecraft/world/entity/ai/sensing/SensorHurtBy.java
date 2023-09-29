package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Set;

public class SensorHurtBy extends Sensor<EntityLiving> {
	@Override
	public Set<MemoryModuleType<?>> a() {
		return ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
	}

	@Override
	protected void a(WorldServer worldserver, EntityLiving entityliving) {
		BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();
		DamageSource damagesource = entityliving.dm();

		if (damagesource != null) {
			behaviorcontroller.setMemory(MemoryModuleType.HURT_BY, entityliving.dm());
			Entity entity = damagesource.getEntity();

			if (entity instanceof EntityLiving el) {
				behaviorcontroller.setMemory(MemoryModuleType.HURT_BY_ENTITY, el);
			}
		} else {
			behaviorcontroller.removeMemory(MemoryModuleType.HURT_BY);
		}

		behaviorcontroller.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent((entityliving1) -> {
			if (!entityliving1.isAlive() || entityliving1.world != worldserver) {
				behaviorcontroller.removeMemory(MemoryModuleType.HURT_BY_ENTITY);
			}
		});
	}
}
