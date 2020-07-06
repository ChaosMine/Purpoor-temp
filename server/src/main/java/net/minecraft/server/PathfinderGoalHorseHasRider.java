package net.minecraft.server;

import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;

public class PathfinderGoalHorseHasRider extends PathfinderGoalHasRider {
    private final EntityHorseAbstract horse;

    public PathfinderGoalHorseHasRider(EntityHorseAbstract entity) {
        super(entity);
        this.horse = entity;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return super.a() && horse.hasSaddle();
    }
}
