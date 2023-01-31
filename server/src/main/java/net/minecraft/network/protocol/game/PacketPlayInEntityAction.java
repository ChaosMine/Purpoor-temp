package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Unmodifiable;

public class PacketPlayInEntityAction implements Packet<PacketListenerPlayIn> {
    public int a;
    public PacketPlayInEntityAction.EnumPlayerAction animation;
    public int c;

    public PacketPlayInEntityAction() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.animation = packetdataserializer.readEnum(EnumPlayerAction.VALUES);
        this.c = packetdataserializer.i();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.writeEnum(this.animation);
        packetdataserializer.d(this.c);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public PacketPlayInEntityAction.EnumPlayerAction c() {
        return this.animation;
    }

    public int d() {
        return this.c;
    }

    public static enum EnumPlayerAction {

        PRESS_SHIFT_KEY, RELEASE_SHIFT_KEY, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, START_RIDING_JUMP, STOP_RIDING_JUMP, OPEN_INVENTORY, START_FALL_FLYING;

        public static final EnumPlayerAction @Unmodifiable [] VALUES = values();

        private EnumPlayerAction() {}
    }
}
