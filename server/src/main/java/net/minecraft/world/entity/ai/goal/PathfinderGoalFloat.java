package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.entity.EntityInsentient;

public class PathfinderGoalFloat extends PathfinderGoal {

    private final EntityInsentient a;

    public PathfinderGoalFloat(EntityInsentient entityinsentient) {
        this.a = entityinsentient;
        if (entityinsentient.getWorld().paperConfig.nerfedMobsShouldJump) entityinsentient.goalFloat = this; // Paper
        this.a(EnumSet.of(PathfinderGoal.Type.JUMP));
        entityinsentient.getNavigation().d(true);
    }

    public final boolean validConditions() { return this.a(); } // Paper - OBFHELPER
    @Override
    public boolean a() {
        return this.a.isInWater() && this.a.b((Tag) TagsFluid.WATER) > this.a.cx() || this.a.aQ();
    }

    public void update() { this.e(); } // Paper - OBFHELPER
    @Override
    public void e() {
        if (this.a.getRandom().nextFloat() < 0.8F) {
            this.a.getControllerJump().jump();
        }

    }
}
