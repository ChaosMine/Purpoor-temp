package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Unmodifiable;

public class PacketPlayInResourcePackStatus implements Packet<PacketListenerPlayIn> {

    public PacketPlayInResourcePackStatus.EnumResourcePackStatus status;

    public PacketPlayInResourcePackStatus() {}

    public PacketPlayInResourcePackStatus(PacketPlayInResourcePackStatus.EnumResourcePackStatus packetplayinresourcepackstatus_enumresourcepackstatus) {
        this.status = packetplayinresourcepackstatus_enumresourcepackstatus;
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.status = packetdataserializer.readEnum(EnumResourcePackStatus.VALUES);
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeEnum(this.status);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public static enum EnumResourcePackStatus {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;

        public static final EnumResourcePackStatus @Unmodifiable [] VALUES = values();

        private EnumResourcePackStatus() {}
    }
}
