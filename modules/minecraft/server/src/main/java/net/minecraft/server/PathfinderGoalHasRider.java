package net.minecraft.server;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

import java.util.EnumSet;

public class PathfinderGoalHasRider extends PathfinderGoal {
    public final EntityInsentient entity;

    public PathfinderGoalHasRider(EntityInsentient entity) {
        this.entity = entity;
        setTypes(EnumSet.of(Type.JUMP, Type.MOVE, Type.LOOK, Type.TARGET, Type.UNKNOWN_BEHAVIOR));
    }

    // shouldExecute
    @Override
    public boolean a() {
        return entity.hasPurpurRider();
    }
}
