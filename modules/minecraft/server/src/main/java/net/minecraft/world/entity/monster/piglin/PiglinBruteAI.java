package net.minecraft.world.entity.monster.piglin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class PiglinBruteAI {
    protected static BehaviorController<?> a(EntityPiglinBrute var0, BehaviorController<EntityPiglinBrute> var1) {
        b(var0, var1);
        c(var0, var1);
        d(var0, var1);
        var1.a(ImmutableSet.of(Activity.CORE));
        var1.b(Activity.IDLE);
        var1.e();
        return var1;
    }

    protected static void a(EntityPiglinBrute var0) {
        GlobalPos var1 = GlobalPos.create(var0.world.getDimensionKey(), var0.getChunkCoordinates());
        var0.getBehaviorController().setMemory(MemoryModuleType.HOME, var1);
    }

    private static void b(EntityPiglinBrute var0, BehaviorController<EntityPiglinBrute> var1) {
        var1.a(Activity.CORE, 0, ImmutableList.of(new BehaviorLook(45, 90), new BehavorMove(), new BehaviorInteractDoor(), new BehaviorForgetAnger<>()));
    }

    private static void c(EntityPiglinBrute var0, BehaviorController<EntityPiglinBrute> var1) {
        var1.a(Activity.IDLE, 10, ImmutableList.of(new BehaviorAttackTargetSet<>(PiglinBruteAI::a___), a(), b(), new BehaviorLookInteract(EntityTypes.PLAYER, 4)));
    }

    private static void d(EntityPiglinBrute var0, BehaviorController<EntityPiglinBrute> var12) {
        var12.a(Activity.FLIGHT, 10, ImmutableList.of(new BehaviorAttackTargetForget<>(var1 -> !PiglinBruteAI.a((EntityPiglinAbstract)var0, var1)), new BehaviorWalkAwayOutOfRange(1.0f), new BehaviorAttack(20)), MemoryModuleType.ATTACK_TARGET);
    }


    private static BehaviorGateSingle<EntityPiglinBrute> a() {
        return new BehaviorGateSingle<>(
          ImmutableList.of(
            Pair.of(new BehaviorLookTarget(EntityTypes.PLAYER, 8.0F), 1),
            Pair.of(new BehaviorLookTarget(EntityTypes.PIGLIN, 8.0F), 1),
            Pair.of(new BehaviorLookTarget(EntityTypes.PIGLIN_BRUTE, 8.0F), 1),
            Pair.of(new BehaviorLookTarget(8.0F), 1),
            Pair.of(new BehaviorNop(30, 60), 1)
          )
        );
    }

    private static BehaviorGateSingle<EntityPiglinBrute> b() {
        return new BehaviorGateSingle<>(
          ImmutableList.of(
            Pair.of(new BehaviorStrollRandomUnconstrained(0.6F), 2),
            Pair.of(BehaviorInteract.a(EntityTypes.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
            Pair.of(BehaviorInteract.a(EntityTypes.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
            Pair.of(new BehaviorStrollPlace(MemoryModuleType.HOME, 0.6F, 2, 100), 2),
            Pair.of(new BehaviorStrollPosition(MemoryModuleType.HOME, 0.6F, 5), 2),
            Pair.of(new BehaviorNop(30, 60), 1)
          )
        );
    }

    protected static void b(EntityPiglinBrute var0) {
        BehaviorController<EntityPiglinBrute> var1 = var0.getBehaviorController();
        Activity var2 = var1.f().orElse(null);
        var1.a(ImmutableList.of(Activity.FLIGHT, Activity.IDLE));
        Activity var3 = var1.f().orElse(null);
        if (var2 != var3) {
            d(var0);
        }

        var0.setAggressive(var1.hasMemory(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean a(EntityPiglinAbstract var0, EntityLiving var1) {
        return a(var0).filter(var1x -> var1x == var1).isPresent();
    }

    private static Optional<? extends EntityLiving> a(EntityPiglinAbstract var0) {
        Optional<EntityLiving> var1 = BehaviorUtil.a(var0, MemoryModuleType.ANGRY_AT);
        if (var1.isPresent() && a(var1.get())) {
            return var1;
        } else {
            Optional<? extends EntityLiving> var2 = a(var0, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
            return var2.isPresent() ? var2 : var0.getBehaviorController().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMSIS);
        }
    }

    private static Optional<? extends EntityLiving> a___(EntityPiglinAbstract piglin) {
        return a(piglin);
    }

    private static boolean a(EntityLiving var0) {
        return IEntitySelector.f.test(var0);
    }

    private static Optional<? extends EntityLiving> a(EntityPiglinAbstract var0, MemoryModuleType<? extends EntityLiving> var1) {
        return var0.getBehaviorController().getMemory(var1).filter(var1x -> var1x.a(var0, 12.0));
    }

    protected static void a(EntityPiglinBrute var0, EntityLiving var1) {
        if (!(var1 instanceof EntityPiglinAbstract)) {
            PiglinAI.a(var0, var1);
        }
    }

    protected static void c(EntityPiglinBrute var0) {
        if ((double)var0.world.random.nextFloat() < 0.0125) {
            d(var0);
        }
    }

    private static void d(EntityPiglinBrute var0) {
        var0.getBehaviorController().f().ifPresent(var1 -> {
            if (var1 == Activity.FLIGHT) {
                var0.eT();
            }
        });
    }
}
