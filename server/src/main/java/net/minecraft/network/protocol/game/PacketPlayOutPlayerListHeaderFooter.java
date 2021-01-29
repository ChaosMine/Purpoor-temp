package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;

public class PacketPlayOutPlayerListHeaderFooter implements Packet<PacketListenerPlayOut> {

    public IChatBaseComponent header;
    public IChatBaseComponent footer;
    // Paper start
    public net.kyori.adventure.text.Component adventure$header;
    public net.kyori.adventure.text.Component adventure$footer;
    // Paper end

    public PacketPlayOutPlayerListHeaderFooter() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.header = packetdataserializer.h();
        this.footer = packetdataserializer.h();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        // Paper start
        if (this.adventure$header != null && this.adventure$footer != null) {
            packetdataserializer.writeComponent(this.adventure$header);
            packetdataserializer.writeComponent(this.adventure$footer);
            return;
        }
        // Paper end
        packetdataserializer.a(this.header);
        packetdataserializer.a(this.footer);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
