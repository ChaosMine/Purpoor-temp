package net.pl3x.purpur.controller;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;

public class ControllerMoveWASDFlying extends ControllerMoveWASD {
    protected final float groundSpeedModifier;
    protected int tooHighCooldown = 0;
    protected boolean setGravityFlag = true;

    public ControllerMoveWASDFlying(EntityInsentient entity) {
        this(entity, 1.0F);
    }

    public ControllerMoveWASDFlying(EntityInsentient entity, float groundSpeedModifier) {
        this(entity, groundSpeedModifier, true);
    }

    public ControllerMoveWASDFlying(EntityInsentient entity, float groundSpeedModifier, boolean setGravityFlag) {
        super(entity);
        this.groundSpeedModifier = groundSpeedModifier;
        this.setGravityFlag = setGravityFlag;
    }

    @Override
    public void tick(EntityHuman rider) {
        float forward = Math.max(0.0F, rider.getForward());
        float vertical = forward == 0.0F ? 0.0F : -(rider.pitch / 45.0F);
        float strafe = rider.getStrafe();

        if (rider.jumping && spacebarEvent(entity)) {
            entity.onSpacebar();
        }

        if (entity.locY() >= entity.getMaxY() || --tooHighCooldown > 0) {
            tooHighCooldown = 60;
            entity.setMot(entity.getMot().add(0.0D, -0.05D, 0.0D));
            vertical = 0.0F;
        }

        setSpeed(entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        float speed = (float) getSpeed();

        if (entity.onGround) {
            speed *= groundSpeedModifier; // TODO = fix this!
        }

        if (setGravityFlag) {
            entity.setNoGravity(forward > 0);
        }

        entity.setSpeed(speed);
        entity.setVertical(vertical);
        entity.setStrafe(strafe);
        entity.setForward(forward);

        setForward(entity.getForward());
        setStrafe(entity.getStrafe());
    }
}
