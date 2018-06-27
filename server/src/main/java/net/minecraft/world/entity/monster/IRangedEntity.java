package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.EntityLiving;

public interface IRangedEntity {

    void a(EntityLiving entityliving, float f); default void rangedAttack(EntityLiving entityliving, float f) { a(entityliving, f); } // Paper - OBFHELPER

    // - see EntitySkeletonAbstract melee goal
    void setAggressive(boolean flag); default void setChargingAttack(boolean charging) { setAggressive(charging); }; // Paper
}
