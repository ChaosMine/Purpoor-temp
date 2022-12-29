package net.minecraft.server.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler; // Paper
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkManagerServer;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.PacketPrepender;
import net.minecraft.network.PacketSplitter;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.game.PacketPlayOutKickDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LazyInitVar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.event.CraftEventFactory; // Kamillaova

public class ServerConnection {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final LazyInitVar<NioEventLoopGroup> a = new LazyInitVar<>(() -> {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
    });
    public static final LazyInitVar<EpollEventLoopGroup> b = new LazyInitVar<>(() -> {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
    });
    private final MinecraftServer e;
    public volatile boolean c;
    private final List<ChannelFuture> listeningChannels = Collections.synchronizedList(Lists.newArrayList());
    private final List<NetworkManager> connectedChannels = Collections.synchronizedList(Lists.newArrayList());
    // Paper start - prevent blocking on adding a new network manager while the server is ticking
    private final java.util.Queue<NetworkManager> pending = new java.util.concurrent.ConcurrentLinkedQueue<>();
    private static final boolean disableFlushConsolidation = Boolean.getBoolean("Paper.disableFlushConsolidate"); // Paper
    private void addPending() {
        NetworkManager manager = null;
        while ((manager = pending.poll()) != null) {
            connectedChannels.add(manager);
            manager.isPending = false;
        }
    }
    // Paper end

    public ServerConnection(MinecraftServer minecraftserver) {
        this.e = minecraftserver;
        this.c = true;
    }

    // Paper start
    public void a(@Nullable InetAddress inetaddress, int i) throws IOException {
        bind(new java.net.InetSocketAddress(inetaddress, i));
    }
    public void bind(java.net.SocketAddress address) throws IOException {
    // Paper end
        List list = this.listeningChannels;

        synchronized (this.listeningChannels) {
            Class oclass;
            LazyInitVar lazyinitvar;

            if (Epoll.isAvailable() && this.e.l()) {
                if (address instanceof io.netty.channel.unix.DomainSocketAddress) {
                    oclass = io.netty.channel.epoll.EpollServerDomainSocketChannel.class;
                } else {
                oclass = EpollServerSocketChannel.class;
                }
                lazyinitvar = ServerConnection.b;
                ServerConnection.LOGGER.info("Using epoll channel type");
            } else {
                oclass = NioServerSocketChannel.class;
                lazyinitvar = ServerConnection.a;
                ServerConnection.LOGGER.info("Using default channel type");
            }

            // Tuinity start - indicate Velocity natives in use
            ServerConnection.LOGGER.info("Tuinity: Using " + com.velocitypowered.natives.util.Natives.compress.getLoadedVariant() + " compression from Velocity.");
            ServerConnection.LOGGER.info("Tuinity: Using " + com.velocitypowered.natives.util.Natives.cipher.getLoadedVariant() + " cipher from Velocity.");
            // Tuinity end

            this.listeningChannels.add(((ServerBootstrap) ((ServerBootstrap) (new ServerBootstrap()).channel(oclass)).childHandler(new ChannelInitializer<Channel>() {
                protected void initChannel(Channel channel) throws Exception {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                    } catch (ChannelException channelexception) {
                        ;
                    }

                    if (!disableFlushConsolidation) channel.pipeline().addFirst(new FlushConsolidationHandler()); // Paper
                    channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new LegacyPingHandler(ServerConnection.this)).addLast("splitter", new PacketSplitter()).addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND)).addLast("prepender", new PacketPrepender()).addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));
                    int j = ServerConnection.this.e.k();
                    Object object = j > 0 ? new NetworkManagerServer(j) : new NetworkManager(EnumProtocolDirection.SERVERBOUND);

                    //ServerConnection.this.connectedChannels.add((NetworkManager) object); // CraftBukkit - decompile error
                    pending.add((NetworkManager) object); // Paper
                    channel.pipeline().addLast("packet_handler", (ChannelHandler) object);
                    ((NetworkManager) object).setPacketListener(new HandshakeListener(ServerConnection.this.e, (NetworkManager) object));
                    io.papermc.paper.network.ChannelInitializeListenerHolder.callListeners(channel); // Paper
                    // Kamillaova start
                    if (NetworkManager.class == object.getClass()) {
                        CraftEventFactory.callEvent(new dev.kamillaova.paper.event.PlayerNettyChannelCreateEvent(channel));
                    }
                    // Kamillaova end
                }
            }).group((EventLoopGroup) lazyinitvar.a()).localAddress(address)).option(ChannelOption.AUTO_READ, false).bind().syncUninterruptibly()); // CraftBukkit // Paper
        }
    }

    // CraftBukkit start
    public void acceptConnections() {
        synchronized (this.listeningChannels) {
            for (ChannelFuture future : this.listeningChannels) {
                future.channel().config().setAutoRead(true);
            }
        }
    }
    // CraftBukkit end

    public void b() {
        this.c = false;
        Iterator iterator = this.listeningChannels.iterator();

        while (iterator.hasNext()) {
            ChannelFuture channelfuture = (ChannelFuture) iterator.next();

            try {
                channelfuture.channel().close().sync();
            } catch (InterruptedException interruptedexception) {
                ServerConnection.LOGGER.error("Interrupted whilst closing channel");
            }
        }

    }

    public void c() {
        List list = this.connectedChannels;

        synchronized (this.connectedChannels) {
            // Spigot Start
            this.addPending(); // Paper
            // This prevents players from 'gaming' the server, and strategically relogging to increase their position in the tick order
            if ( org.spigotmc.SpigotConfig.playerShuffle > 0 && MinecraftServer.currentTick % org.spigotmc.SpigotConfig.playerShuffle == 0 )
            {
                Collections.shuffle( this.connectedChannels );
            }
            // Spigot End
            Iterator iterator = this.connectedChannels.iterator();

            while (iterator.hasNext()) {
                NetworkManager networkmanager = (NetworkManager) iterator.next();

                if (!networkmanager.i()) {
                    if (networkmanager.isConnected()) {
                        try {
                            networkmanager.a();
                        } catch (Exception exception) {
                            if (networkmanager.isLocal()) {
                                throw new ReportedException(CrashReport.a(exception, "Ticking memory connection"));
                            }

                            ServerConnection.LOGGER.warn("Failed to handle packet for {}", networkmanager.getSocketAddress(), exception);
                            ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");

                            networkmanager.sendPacket(new PacketPlayOutKickDisconnect(chatcomponenttext), (future) -> {
                                networkmanager.close(chatcomponenttext);
                            });
                            networkmanager.stopReading();
                        }
                    } else {
                        // Spigot Start
                        // Fix a race condition where a NetworkManager could be unregistered just before connection.
                        if (networkmanager.preparing) continue;
                        // Spigot End
                        iterator.remove();
                        networkmanager.handleDisconnection();
                    }
                }
            }

        }
    }

    public MinecraftServer d() {
        return this.e;
    }
}
