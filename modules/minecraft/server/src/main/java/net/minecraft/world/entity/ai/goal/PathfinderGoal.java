package net.minecraft.world.entity.ai.goal;

import com.destroystokyo.paper.util.set.OptimizedSmallEnumSet;

import java.util.EnumSet;

public abstract class PathfinderGoal {

    private final OptimizedSmallEnumSet<Type> goalTypes = new OptimizedSmallEnumSet<>(PathfinderGoal.Type.class); // Paper - remove streams from pathfindergoalselector

    // Paper start make sure goaltypes is never empty
    public PathfinderGoal() {
        if (this.goalTypes.size() == 0) {
            this.goalTypes.addUnchecked(Type.UNKNOWN_BEHAVIOR);
        }
    }
    // paper end

    public boolean a() { return this.shouldActivate(); } public boolean shouldActivate() { return false;} public boolean shouldActivate2() { return a(); } // Paper - OBFHELPER, for both directions...

    public boolean b() { return this.shouldStayActive(); } public boolean shouldStayActive2() { return b(); } public boolean shouldStayActive() { // Paper - OBFHELPER, for both directions...
        return this.a();
    }

    public boolean C_() {
        return true;
    }

    public void c() { this.start(); } public void start() {} // Paper - OBFHELPER

    public final void onTaskResetObfHelper() { d(); } // Paper - OBFHELPER
    public void d() {
        onTaskReset(); // Paper
    }
    public void onTaskReset() {} // Paper

    public void e() { this.tick(); } public void tick() {} // Paper OBFHELPER

    public void a(EnumSet<PathfinderGoal.Type> enumset) { this.setTypes(enumset); } public void setTypes(EnumSet<PathfinderGoal.Type> enumset) { // Paper - OBFHELPER
        // Paper start - remove streams from pathfindergoalselector
        this.goalTypes.clear();
        this.goalTypes.addAllUnchecked(enumset);
        // make sure its never empty
        if (this.goalTypes.size() == 0) {
            this.goalTypes.addUnchecked(Type.UNKNOWN_BEHAVIOR);
        }
        // Paper end - remove streams from pathfindergoalselector
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    // Paper start - remove streams from pathfindergoalselector
    public com.destroystokyo.paper.util.set.OptimizedSmallEnumSet<PathfinderGoal.Type> getGoalTypes() {
        return this.goalTypes;
        // Paper end - remove streams from pathfindergoalselector
    }

    public static enum Type {

        MOVE, LOOK, JUMP, TARGET, UNKNOWN_BEHAVIOR; // Paper - add unknown

        public static Type[] VALUES = values();

        private Type() {}
    }
}
