package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.MinecraftKey;
import org.jetbrains.annotations.Unmodifiable;

public class PacketPlayInAdvancements implements Packet<PacketListenerPlayIn> {

    private PacketPlayInAdvancements.Status a;
    private MinecraftKey b;

    public PacketPlayInAdvancements() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readEnum(Status.VALUES);
        if (this.a == PacketPlayInAdvancements.Status.OPENED_TAB) {
            this.b = packetdataserializer.p();
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeEnum(this.a);
        if (this.a == PacketPlayInAdvancements.Status.OPENED_TAB) {
            packetdataserializer.a(this.b);
        }

    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public PacketPlayInAdvancements.Status c() {
        return this.a;
    }

    public MinecraftKey d() {
        return this.b;
    }

    public static enum Status {

        OPENED_TAB, CLOSED_SCREEN;

        public static final Status @Unmodifiable [] VALUES = values();

        private Status() {}
    }
}
