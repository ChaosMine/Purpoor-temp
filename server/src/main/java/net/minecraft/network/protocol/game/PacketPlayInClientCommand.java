package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Unmodifiable;

public class PacketPlayInClientCommand implements Packet<PacketListenerPlayIn> {

    private PacketPlayInClientCommand.EnumClientCommand a;

    public PacketPlayInClientCommand() {}

    public PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand packetplayinclientcommand_enumclientcommand) {
        this.a = packetplayinclientcommand_enumclientcommand;
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readEnum(EnumClientCommand.VALUES);
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeEnum(this.a);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public PacketPlayInClientCommand.EnumClientCommand b() {
        return this.a;
    }

    public static enum EnumClientCommand {

        PERFORM_RESPAWN, REQUEST_STATS;
        public static final EnumClientCommand @Unmodifiable [] VALUES = values();

        private EnumClientCommand() {}
    }
}
