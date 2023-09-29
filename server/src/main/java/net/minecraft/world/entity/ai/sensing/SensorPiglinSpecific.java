package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.monster.piglin.EntityPiglinAbstract;
import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinAI;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.block.BlockCampfire;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;

public class SensorPiglinSpecific extends Sensor<EntityLiving> {
	private static Optional<BlockPosition> c(WorldServer worldserver, EntityLiving entityliving) {
		return BlockPosition.a(entityliving.getChunkCoordinates(), 8, 4, (blockposition) -> {
			return a(worldserver, blockposition);
		});
	}

	private static boolean a(WorldServer worldserver, BlockPosition blockposition) {
		IBlockData iblockdata = worldserver.getType(blockposition);
		boolean flag = iblockdata.a(TagsBlock.PIGLIN_REPELLENTS);

		return flag && iblockdata.a(Blocks.SOUL_CAMPFIRE) ? BlockCampfire.g(iblockdata) : flag;
	}

	@Override
	public Set<MemoryModuleType<?>> a() {
		return ImmutableSet.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.MOBS, MemoryModuleType.NEAREST_VISIBLE_NEMSIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT);
	}

	@Override
	protected void a(WorldServer worldserver, EntityLiving entityliving) {
		BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_REPELLENT, c(worldserver, entityliving));
		Optional<EntityInsentient> optional = Optional.empty();
		Optional<EntityHoglin> optional1 = Optional.empty();
		Optional<EntityHoglin> optional2 = Optional.empty();
		Optional<EntityPiglin> optional3 = Optional.empty();
		Optional<EntityLiving> optional4 = Optional.empty();
		Optional<EntityHuman> optional5 = Optional.empty();
		Optional<EntityHuman> optional6 = Optional.empty();
		int i = 0;
		List<EntityPiglinAbstract> list = Lists.newArrayList();
		List<EntityPiglinAbstract> list1 = Lists.newArrayList();
		List<EntityLiving> list2 = behaviorcontroller.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(ImmutableList.of());

		for (EntityLiving entityliving1 : list2) {
			if (entityliving1 instanceof EntityHoglin entityhoglin) {

				if (entityhoglin.isBaby() && optional2.isEmpty()) {
					optional2 = Optional.of(entityhoglin);
				} else if (entityhoglin.eL()) {
					++i;
					if (optional1.isEmpty() && entityhoglin.eO()) {
						optional1 = Optional.of(entityhoglin);
					}
				}
			} else if (entityliving1 instanceof EntityPiglinBrute) {
				list.add((EntityPiglinBrute) entityliving1);
			} else if (entityliving1 instanceof EntityPiglin entitypiglin) {

				if (entitypiglin.isBaby() && optional3.isEmpty()) {
					optional3 = Optional.of(entitypiglin);
				} else if (entitypiglin.eM()) {
					list.add(entitypiglin);
				}
			} else if (entityliving1 instanceof EntityHuman entityhuman) {

				if (optional5.isEmpty() && IEntitySelector.f.test(entityliving1) && !PiglinAI.a(entityhuman)) {
					optional5 = Optional.of(entityhuman);
				}

				if (optional6.isEmpty() && !entityhuman.isSpectator() && PiglinAI.b(entityhuman)) {
					optional6 = Optional.of(entityhuman);
				}
			} else if (optional.isEmpty() && (entityliving1 instanceof EntitySkeletonWither || entityliving1 instanceof EntityWither)) {
				optional = Optional.of((EntityInsentient) entityliving1);
			} else if (optional4.isEmpty() && PiglinAI.a(entityliving1.getEntityType())) {
				optional4 = Optional.of(entityliving1);
			}
		}

		List<EntityLiving> list3 = behaviorcontroller.getMemory(MemoryModuleType.MOBS).orElse(ImmutableList.of());

		for (EntityLiving entityliving2 : list3) {
			if (entityliving2 instanceof EntityPiglinAbstract && ((EntityPiglinAbstract) entityliving2).eM()) {
				list1.add((EntityPiglinAbstract) entityliving2);
			}
		}

		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMSIS, optional);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, optional1);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, optional2);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional4);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, optional5);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional6);
		behaviorcontroller.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list1);
		behaviorcontroller.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
		behaviorcontroller.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list.size());
		behaviorcontroller.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, i);
	}
}
