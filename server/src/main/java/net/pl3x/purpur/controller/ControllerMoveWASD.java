package net.pl3x.purpur.controller;

import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.pl3x.purpur.event.entity.RidableSpacebarEvent;

public class ControllerMoveWASD extends ControllerMove {
    protected final EntityInsentient entity;
    private final double speedModifier;

    public ControllerMoveWASD(EntityInsentient entity) {
        this(entity, 1.0D);
    }

    public ControllerMoveWASD(EntityInsentient entity, double speedModifier) {
        super(entity);
        this.entity = entity;
        this.speedModifier = speedModifier;
    }

    // isUpdating
    @Override
    public boolean b() {
        return entity.hasPurpurRider() ? getForward() != 0 || getStrafe() != 0 : super.b();
    }

    // tick
    @Override
    public void a() {
        if (entity.hasPurpurRider()) {
            tick(entity.getPurpurRider());
        } else {
            tick();
        }
    }

    public void tick() {
        super.a(); // tick
    }

    public void tick(EntityHuman rider) {
        float forward = rider.getForward() * 0.5F;
        float strafe = rider.getStrafe() * 0.25F;

        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        float yawOffset = 0;
        if (strafe != 0) {
            if (forward == 0) {
                yawOffset += strafe > 0 ? -90 : 90;
                forward = Math.abs(strafe * 2);
            } else {
                yawOffset += strafe > 0 ? -30 : 30;
                strafe /= 2;
                if (forward < 0) {
                    yawOffset += strafe > 0 ? -110 : 110;
                    forward *= -1;
                }
            }
        } else if (forward < 0) {
            yawOffset -= 180;
            forward *= -1;
        }

        ((ControllerLookWASD) entity.getControllerLook()).setOffsets(yawOffset, 0);

        if (rider.jumping && spacebarEvent(entity) && !entity.onSpacebar() && entity.onGround) {
            entity.jump();
        }

        setSpeed(entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * speedModifier);

        entity.setSpeed((float) getSpeed());
        entity.setForward(forward);

        setForward(entity.getForward());
        setStrafe(entity.getStrafe());
    }

    public static boolean spacebarEvent(Entity entity) {
        if (RidableSpacebarEvent.getHandlerList().getRegisteredListeners().length > 0) {
            return new RidableSpacebarEvent(entity.getBukkitEntity()).callEvent();
        } else {
            return true;
        }
    }
}
