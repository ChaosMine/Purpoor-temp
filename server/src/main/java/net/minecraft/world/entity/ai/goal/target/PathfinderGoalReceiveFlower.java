package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;
import java.util.UUID;

public class PathfinderGoalReceiveFlower extends PathfinderGoal {
    private final EntityIronGolem entity;
    private EntityPlayer target;
    private int cooldown;

    public PathfinderGoalReceiveFlower(EntityIronGolem entity) {
        this.entity = entity;
        this.setTypes(EnumSet.of(Type.MOVE, Type.LOOK));
    }

    @Override
    public boolean shouldActivate() {
        if (this.entity.getHoldingFlowerTick() > 0) {
            return false;
        }
        if (!this.entity.isAngry()) {
            return false;
        }
        UUID uuid = this.entity.getAngerTarget();
        if (uuid == null) {
            return false;
        }
        Entity target = ((WorldServer) this.entity.world).getEntity(uuid);
        if (!(target instanceof EntityPlayer)) {
            return false;
        }
        EntityPlayer player = (EntityPlayer) target;
        if (!isHoldingFlower(player)) {
            return false;
        }
        this.target = player;
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return this.cooldown > 0;
    }

    @Override
    public void start() {
        this.cooldown = 100;
        this.entity.pacify();
        this.entity.holdFlower(true);
    }

    @Override
    public void onTaskReset() {
        this.entity.holdFlower(false);
        this.target = null;
    }

    @Override
    public void tick() {
        this.entity.getControllerLook().lookAt(this.target, 30.0F, 30.0F);
        --this.cooldown;
    }

    private boolean isHoldingFlower(EntityPlayer player) {
        return isPoppy(player.getItemInMainHand()) || isPoppy(player.getItemInOffHand());
    }

    private boolean isPoppy(ItemStack item) {
        return item.getItem() == Blocks.POPPY.getItem();
    }
}
