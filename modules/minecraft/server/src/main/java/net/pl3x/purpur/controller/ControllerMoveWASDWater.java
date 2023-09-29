package net.pl3x.purpur.controller;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;

public class ControllerMoveWASDWater extends ControllerMoveWASD {
    private final double speedModifier;

    public ControllerMoveWASDWater(EntityInsentient entity) {
        this(entity, 1.0D);
    }

    public ControllerMoveWASDWater(EntityInsentient entity, double speedModifier) {
        super(entity);
        this.speedModifier = speedModifier;
    }

    @Override
    public void tick(EntityHuman rider) {
        float forward = rider.getForward();
        float strafe = rider.getStrafe() * 0.5F; // strafe slower by default
        float vertical = -(rider.pitch / 90);

        if (forward == 0.0F) {
            // strafe slower if not moving forward
            strafe *= 0.5F;
            // do not move vertically if not moving forward
            vertical = 0.0F;
        } else if (forward < 0.0F) {
            // water animals can't swim backwards
            forward = 0.0F;
            vertical = 0.0F;
        }

        if (rider.jumping && spacebarEvent(entity)) {
            entity.onSpacebar();
        }

        setSpeed(entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * speedModifier);
        entity.setSpeed((float) getSpeed() * 0.1F);

        entity.setForward(forward * (float) speedModifier);
        entity.setStrafe(strafe * (float) speedModifier);
        entity.setVertical(vertical * (float) speedModifier);

        setForward(entity.getForward());
        setStrafe(entity.getStrafe());
    }
}
