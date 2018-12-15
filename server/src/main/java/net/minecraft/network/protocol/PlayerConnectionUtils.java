package net.minecraft.network.protocol;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutKickDisconnect;
import net.minecraft.server.CancelledPacketHandleException;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.thread.IAsyncTaskHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import co.aikar.timings.MinecraftTimings; // Paper
import co.aikar.timings.Timing; // Paper

// CraftBukkit start
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.PlayerConnection;
// CraftBukkit end

public class PlayerConnectionUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static <T extends PacketListener> void ensureMainThread(Packet<T> packet, T t0, WorldServer worldserver) throws CancelledPacketHandleException {
        ensureMainThread(packet, t0, (IAsyncTaskHandler) worldserver.getMinecraftServer());
    }

    // Tuinity start - detailed watchdog information
    public static final java.util.concurrent.ConcurrentLinkedDeque<PacketListener> packetProcessing = new java.util.concurrent.ConcurrentLinkedDeque<>();
    static final java.util.concurrent.atomic.AtomicLong totalMainThreadPacketsProcessed = new java.util.concurrent.atomic.AtomicLong();

    public static long getTotalProcessedPackets() {
        return totalMainThreadPacketsProcessed.get();
    }

    public static java.util.List<PacketListener> getCurrentPacketProcessors() {
        java.util.List<PacketListener> ret = new java.util.ArrayList<>(4);
        for (PacketListener listener : packetProcessing) {
            ret.add(listener);
        }

        return ret;
    }
    // Tuinity end - detailed watchdog information

    public static <T extends PacketListener> void ensureMainThread(Packet<T> packet, T t0, IAsyncTaskHandler<?> iasynctaskhandler) throws CancelledPacketHandleException {
        if (!iasynctaskhandler.isMainThread()) {
            Timing timing = MinecraftTimings.getPacketTiming(packet); // Paper - timings
            iasynctaskhandler.execute(() -> {
                packetProcessing.push(t0); // Tuinity - detailed watchdog information
                try { // Tuinity - detailed watchdog information
                if (MinecraftServer.getServer().hasStopped() || (t0 instanceof PlayerConnection && ((PlayerConnection) t0).processedDisconnect)) return; // CraftBukkit, MC-142590
                if (t0.a().isConnected()) {
                    try (Timing ignored = timing.startTiming()) { // Paper - timings
                    packet.a(t0);
                    } // Paper - timings
                    // Paper start
                    catch (Exception e) {
                        NetworkManager networkmanager = t0.a();
                        if (networkmanager.getPlayer() != null) {
                            LOGGER.error("Error whilst processing packet {} for {}[{}]", packet, networkmanager.getPlayer().getName(), networkmanager.getSocketAddress(), e);
                        } else {
                            LOGGER.error("Error whilst processing packet {} for connection from {}", packet, networkmanager.getSocketAddress(), e);
                        }
                        ChatComponentText error = new ChatComponentText("Packet processing error");
                        networkmanager.sendPacket(new PacketPlayOutKickDisconnect(error), (future) -> {
                            networkmanager.close(error);
                        });
                        networkmanager.stopReading();
                    }
                    // Paper end
                } else {
                    PlayerConnectionUtils.LOGGER.debug("Ignoring packet due to disconnection: " + packet);
                }
                // Tuinity start - detailed watchdog information
                } finally {
                    totalMainThreadPacketsProcessed.getAndIncrement();
                    packetProcessing.pop();
                }
                // Tuinity end - detailed watchdog information

            });
            throw CancelledPacketHandleException.INSTANCE;
            // CraftBukkit start - SPIGOT-5477, MC-142590
        } else if (MinecraftServer.getServer().hasStopped() || (t0 instanceof PlayerConnection && ((PlayerConnection) t0).processedDisconnect)) {
            throw CancelledPacketHandleException.INSTANCE;
            // CraftBukkit end
        }
    }
}
