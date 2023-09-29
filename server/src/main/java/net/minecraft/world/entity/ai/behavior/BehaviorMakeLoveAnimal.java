package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.EntityAnimal;

import java.util.Optional;

public class BehaviorMakeLoveAnimal extends Behavior<EntityAnimal> {
	private final EntityTypes<? extends EntityAnimal> b;
	private final float c;
	private long d;

	public BehaviorMakeLoveAnimal(EntityTypes<? extends EntityAnimal> var0, float var1) {
		super(
			ImmutableMap.of(
				MemoryModuleType.VISIBLE_MOBS,
				MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.BREED_TARGET,
				MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.WALK_TARGET,
				MemoryStatus.REGISTERED,
				MemoryModuleType.LOOK_TARGET,
				MemoryStatus.REGISTERED
			),
			325
		);
		this.b = var0;
		this.c = var1;
	}

	protected boolean a(WorldServer var0, EntityAnimal var1) {
		return var1.isInLove() && this.c(var1).isPresent();
	}

	protected void a(WorldServer var0, EntityAnimal var1, long var2) {
		EntityAnimal var4 = this.c(var1).get();
		var1.getBehaviorController().setMemory(MemoryModuleType.BREED_TARGET, var4);
		var4.getBehaviorController().setMemory(MemoryModuleType.BREED_TARGET, var1);
		BehaviorUtil.a(var1, var4, this.c);
		int var5 = 275 + var1.getRandom().nextInt(50);
		this.d = var2 + (long) var5;
	}

	protected boolean b(WorldServer var0, EntityAnimal var1, long var2) {
		if (!this.b(var1)) {
			return false;
		} else {
			EntityAnimal var4 = this.a(var1);
			return var4.isAlive() && var1.mate(var4) && BehaviorUtil.a(var1.getBehaviorController(), var4) && var2 <= this.d;
		}
	}

	protected void d(WorldServer var0, EntityAnimal var1, long var2) {
		EntityAnimal var4 = this.a(var1);
		BehaviorUtil.a(var1, var4, this.c);
		if (var1.a(var4, 3.0)) {
			if (var2 >= this.d) {
				var1.a(var0, var4);
				var1.getBehaviorController().removeMemory(MemoryModuleType.BREED_TARGET);
				var4.getBehaviorController().removeMemory(MemoryModuleType.BREED_TARGET);
			}
		}
	}

	protected void c(WorldServer var0, EntityAnimal var1, long var2) {
		var1.getBehaviorController().removeMemory(MemoryModuleType.BREED_TARGET);
		var1.getBehaviorController().removeMemory(MemoryModuleType.WALK_TARGET);
		var1.getBehaviorController().removeMemory(MemoryModuleType.LOOK_TARGET);
		this.d = 0L;
	}

	private EntityAnimal a(EntityAnimal var0) {
		return (EntityAnimal) var0.getBehaviorController().getMemory(MemoryModuleType.BREED_TARGET).get();
	}

	private boolean b(EntityAnimal var0) {
		BehaviorController<?> var1 = var0.getBehaviorController();
		return var1.hasMemory(MemoryModuleType.BREED_TARGET) && var1.getMemory(MemoryModuleType.BREED_TARGET).get().getEntityType() == this.b;
	}

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantCast"}) // perf :D
	private Optional<? extends EntityAnimal> c(EntityAnimal var0) {
		return (Optional<? extends EntityAnimal>) (Optional) var0.getBehaviorController().getMemory(MemoryModuleType.VISIBLE_MOBS).get()
			.stream()
			.filter(var0x -> var0x.getEntityType() == this.b)
			.filter(x -> var0.mate((EntityAnimal) x))
			.findFirst();
	}
}
