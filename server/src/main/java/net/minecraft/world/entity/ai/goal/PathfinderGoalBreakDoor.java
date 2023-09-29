package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.IPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

import java.util.function.Predicate;

public class PathfinderGoalBreakDoor extends PathfinderGoalDoorInteract {

    private final Predicate<EnumDifficulty> g;
    protected int a;
    protected int b;
    protected int c;

    public PathfinderGoalBreakDoor(EntityInsentient entityinsentient, Predicate<EnumDifficulty> predicate) {
        super(entityinsentient);
        this.b = -1;
        this.c = -1;
        this.g = entityinsentient instanceof EntityZombie ? difficulty -> testDifficulty(entity) : predicate; // Purpur
    }

    public PathfinderGoalBreakDoor(EntityInsentient entityinsentient, int i, Predicate<EnumDifficulty> predicate) {
        this(entityinsentient, predicate);
        this.c = i;
    }

    protected int f() {
        return Math.max(240, this.c);
    }

    @Override
    public boolean a() {
        return !super.a() ? false : ((!this.entity.world.purpurConfig.zombieBypassMobGriefing && !this.entity.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) ? false : this.a(this.entity.world.getDifficulty()) && !this.g()); // Purpur
    }

    @Override
    public void c() {
        super.c();
        this.a = 0;
    }

    @Override
    public boolean b() {
        return this.a <= this.f() && !this.g() && this.door.a(this.entity.getPositionVector(), 2.0D) && this.a(this.entity.world.getDifficulty());
    }

    @Override
    public void d() {
        super.d();
        this.entity.world.a(this.entity.getId(), this.door, -1);
    }

    @Override
    public void e() {
        super.e();
        if (this.entity.getRandom().nextInt(20) == 0) {
            this.entity.world.triggerEffect(1019, this.door, 0);
            if (!this.entity.ai) {
                this.entity.swingHand(this.entity.getRaisedHand());
            }
        }

        ++this.a;
        int i = (int) ((float) this.a / (float) this.f() * 10.0F);

        if (i != this.b) {
            this.entity.world.a(this.entity.getId(), this.door, i);
            this.b = i;
        }

        if (this.a == this.f() && this.a(this.entity.world.getDifficulty())) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityBreakDoorEvent(this.entity, this.door).isCancelled()) {
                this.c();
                return;
            }
            // CraftBukkit end
            this.entity.world.a(this.door, false);
            this.entity.world.triggerEffect(1021, this.door, 0);
            this.entity.world.triggerEffect(2001, this.door, Block.getCombinedId(this.entity.world.getType(this.door)));
        }

    }

    private boolean a(EnumDifficulty enumdifficulty) {
        return this.g.test(enumdifficulty);
    }

    // Purpur start
    private boolean testDifficulty(Entity entity) {
        EnumDifficulty difficulty = entity.world.getDifficulty();
        switch (entity.world.purpurConfig.zombieBreakDoorMinDifficulty) {
            case PEACEFUL:
                return difficulty == EnumDifficulty.HARD || difficulty == EnumDifficulty.NORMAL || difficulty == EnumDifficulty.EASY || difficulty == EnumDifficulty.PEACEFUL;
            case EASY:
                return difficulty == EnumDifficulty.HARD || difficulty == EnumDifficulty.NORMAL || difficulty == EnumDifficulty.EASY;
            case NORMAL:
                return difficulty == EnumDifficulty.HARD || difficulty == EnumDifficulty.NORMAL;
            case HARD:
            default:
                return difficulty == EnumDifficulty.HARD;
        }
    }
    // Purpur end
}
