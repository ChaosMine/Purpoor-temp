package net.minecraft.network.protocol;

import io.netty.channel.ChannelFuture;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.EntityPlayer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.List;

public interface Packet<T extends PacketListener> {
    default void deserialize(PacketDataSerializer buf) throws IOException { a(buf); }
    void a(PacketDataSerializer packetdataserializer) throws IOException;

    default void serialize(PacketDataSerializer buf) throws IOException { b(buf); }
    void b(PacketDataSerializer packetdataserializer) throws IOException;

    @SuppressWarnings("unchecked")
    default void handle(PacketListener listener) { a((T) listener); }

    void a(T t0);

    /**
     * @param player Null if not at PLAY stage yet
     */
    default void onPacketDispatch(@Nullable EntityPlayer player) {}

    /**
     * @param player Null if not at PLAY stage yet
     * @param future Can be null if packet was cancelled
     */
    default void onPacketDispatchFinish(@Nullable EntityPlayer player, @Nullable ChannelFuture future) {}

    default boolean hasFinishListener() { return false; }

    default boolean isReady() { return true; }

    default List<Packet<?>> getExtraPackets() { return null; }

    default boolean packetTooLarge(NetworkManager manager) {
        return false;
    }

    default boolean a() {
        return false;
    }
}
