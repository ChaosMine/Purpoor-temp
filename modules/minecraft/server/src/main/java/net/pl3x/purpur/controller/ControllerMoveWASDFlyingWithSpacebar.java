package net.pl3x.purpur.controller;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.phys.Vec3D;

public class ControllerMoveWASDFlyingWithSpacebar extends ControllerMoveWASDFlying {
    public ControllerMoveWASDFlyingWithSpacebar(EntityInsentient entity) {
        super(entity);
    }

    public ControllerMoveWASDFlyingWithSpacebar(EntityInsentient entity, float groundSpeedModifier) {
        super(entity, groundSpeedModifier);
    }

    @Override
    public void tick(EntityHuman rider) {
        float forward = rider.getForward();
        float strafe = rider.getStrafe() * 0.5F;
        float vertical = 0;

        if (forward < 0.0F) {
            forward *= 0.5F;
            strafe *= 0.5F;
        }

        float speed = (float) entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();

        if (entity.onGround) {
            speed *= groundSpeedModifier;
        }

        if (rider.jumping && spacebarEvent(entity) && !entity.onSpacebar()) {
            entity.setNoGravity(true);
            vertical = 1.0F;
        } else {
            entity.setNoGravity(false);
        }

        if (entity.locY() >= entity.getMaxY() || --tooHighCooldown > 0) {
            tooHighCooldown = 60;
            entity.setMot(entity.getMot().add(0.0D, -0.2D, 0.0D));
            vertical = 0.0F;
        }

        setSpeed(speed);
        entity.setSpeed((float) getSpeed());
        entity.setVertical(vertical);
        entity.setStrafe(strafe);
        entity.setForward(forward);

        setForward(entity.getForward());
        setStrafe(entity.getStrafe());

        Vec3D mot = entity.getMot();
        if (mot.y > 0.2D) {
            entity.setMot(mot.x, 0.2D, mot.z);
        }
    }
}
