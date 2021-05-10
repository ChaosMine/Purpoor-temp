package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntitySkeletonAbstract;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

public class CraftSkeleton extends CraftMonster implements Skeleton, com.destroystokyo.paper.entity.CraftRangedEntity<EntitySkeletonAbstract> { // Paper

    public CraftSkeleton(CraftServer server, EntitySkeletonAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntitySkeletonAbstract getHandle() {
        return (EntitySkeletonAbstract) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
    public SkeletonType getSkeletonType() {
       return SkeletonType.NORMAL;
    }

    @Override
    public void setSkeletonType(SkeletonType type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    // Paper start
    @Override
    public boolean shouldBurnInDay() {
        return getHandle().shouldBurnInDay();
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        getHandle().setShouldBurnInDay(shouldBurnInDay);
    }
    // Paper end
}
