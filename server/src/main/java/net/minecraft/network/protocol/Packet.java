package net.minecraft.network.protocol;

import java.io.IOException;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.PacketListener;

public interface Packet<T extends PacketListener> {

    void a(PacketDataSerializer packetdataserializer) throws IOException;

    void b(PacketDataSerializer packetdataserializer) throws IOException;

    void a(T t0);

    // Paper start
    default boolean packetTooLarge(NetworkManager manager) {
        return false;
    }
    // Paper end

    default boolean a() {
        return false;
    }
}
