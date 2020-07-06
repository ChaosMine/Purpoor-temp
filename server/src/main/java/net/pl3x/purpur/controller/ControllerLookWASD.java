package net.pl3x.purpur.controller;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.world.entity.ai.control.ControllerLook;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityHuman;

public class ControllerLookWASD extends ControllerLook {
    protected final EntityInsentient entity;
    private float yawOffset = 0;
    private float pitchOffset = 0;

    public ControllerLookWASD(EntityInsentient entity) {
        super(entity);
        this.entity = entity;
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

    protected void tick() {
        super.a(); // tick
    }

    protected void tick(EntityHuman rider) {
        setYawPitch(rider.yaw, rider.pitch);
    }

    public void setYawPitch(float yaw, float pitch) {
        entity.yaw = normalizeYaw(yaw + yawOffset);
        entity.lastYaw = entity.yaw;
        entity.setBodyYaw(entity.yaw);
        entity.setRenderYawOffset(entity.yaw);
        entity.setHeadRotation(entity.yaw);
        entity.pitch = normalizePitch(pitch + pitchOffset);

        entity.tracker.broadcast(new PacketPlayOutEntity
                .PacketPlayOutRelEntityMoveLook(entity.getId(),
                (short) 0, (short) 0, (short) 0,
                (byte) MathHelper.d(entity.yaw * 256.0F / 360.0F),
                (byte) MathHelper.d(entity.pitch * 256.0F / 360.0F),
                entity.onGround));
    }

    public void setOffsets(float yaw, float pitch) {
        yawOffset = yaw;
        pitchOffset = pitch;
    }

    public float normalizeYaw(float yaw) {
        yaw %= 360.0f;
        if (yaw >= 180.0f) {
            yaw -= 360.0f;
        } else if (yaw < -180.0f) {
            yaw += 360.0f;
        }
        return yaw;
    }

    public float normalizePitch(float pitch) {
        if (pitch > 90.0f) {
            pitch = 90.0f;
        } else if (pitch < -90.0f) {
            pitch = -90.0f;
        }
        return pitch;
    }
}
