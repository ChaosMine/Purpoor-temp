package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

import java.io.IOException;

public class PacketPlayOutUpdateTime implements Packet<PacketListenerPlayOut> {

    private long a; private final void setWorldAge(final long age) { this.a = age; } private final long getWorldAge() { return this.a; } // Paper - OBFHELPER
    private long b; public void setPlayerTime(long time) { this.b = time; } // Purpur

    public PacketPlayOutUpdateTime() {}

    public PacketPlayOutUpdateTime(long i, long j, boolean flag) {
        this.a = i;
        this.b = j;
        if (!flag) {
            this.b = -this.b;
            if (this.b == 0L) {
                this.b = -1L;
            }
        }

        // Paper start
        this.setWorldAge(this.getWorldAge() % 192000);
        // Paper end
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readLong();
        this.b = packetdataserializer.readLong();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.a);
        packetdataserializer.writeLong(this.b);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
