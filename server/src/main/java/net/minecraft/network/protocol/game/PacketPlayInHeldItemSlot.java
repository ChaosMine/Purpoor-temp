package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

import java.io.IOException;

public class PacketPlayInHeldItemSlot implements Packet<PacketListenerPlayIn> {

    private int itemInHandIndex;

    public PacketPlayInHeldItemSlot() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.itemInHandIndex = packetdataserializer.readShort();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeShort(this.itemInHandIndex);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public int getItemInHandIndex() { return b(); } // Paper - OBFHELPER
    public int b() {
        return this.itemInHandIndex;
    }
}
