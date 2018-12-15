package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.EncoderException; // Paper
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutBoss;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.network.protocol.game.PacketPlayOutKeepAlive;
import net.minecraft.network.protocol.game.PacketPlayOutKickDisconnect;
import net.minecraft.network.protocol.game.PacketPlayOutTabComplete;
import net.minecraft.network.protocol.game.PacketPlayOutTitle;
import net.minecraft.server.CancelledPacketHandleException;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.LoginListener;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.util.LazyInitVar;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import io.netty.util.concurrent.AbstractEventExecutor; // Tuinity

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Marker a = MarkerManager.getMarker("NETWORK");
    public static final Marker b = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.a);
    public static final AttributeKey<EnumProtocol> c = AttributeKey.valueOf("protocol");
    public static final LazyInitVar<NioEventLoopGroup> d = new LazyInitVar<>(() -> {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    });
    public static final LazyInitVar<EpollEventLoopGroup> e = new LazyInitVar<>(() -> {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
    });
    public static final LazyInitVar<DefaultEventLoopGroup> f = new LazyInitVar<>(() -> {
        return new DefaultEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
    });
    private final EnumProtocolDirection h;
    private final Queue<NetworkManager.QueuedPacket> packetQueue = Queues.newConcurrentLinkedQueue();
    public Channel channel;
    public SocketAddress socketAddress;
    // Spigot Start
    public java.util.UUID spoofedUUID;
    public com.mojang.authlib.properties.Property[] spoofedProfile;
    public boolean preparing = true;
    // Spigot End
    private PacketListener packetListener;
    private IChatBaseComponent m;
    private boolean n;
    private boolean o;
    private int p;
    private int q;
    private float r;
    private float s;
    private int t;
    private boolean u;
    // Paper start - NetworkClient implementation
    public int protocolVersion;
    public java.net.InetSocketAddress virtualHost;
    private static boolean enableExplicitFlush = Boolean.getBoolean("paper.explicit-flush");
    // Optimize network
    public boolean isPending = true;
    public boolean queueImmunity = false;
    public EnumProtocol protocol;
    // Paper end

    // Tuinity start - allow controlled flushing
    volatile boolean canFlush = true;
    private final java.util.concurrent.atomic.AtomicInteger packetWrites = new java.util.concurrent.atomic.AtomicInteger();
    private int flushPacketsStart;
    private final Object flushLock = new Object();

    public void disableAutomaticFlush() {
        synchronized (this.flushLock) {
            this.flushPacketsStart = this.packetWrites.get(); // must be volatile and before canFlush = false
            this.canFlush = false;
        }
    }

    public void enableAutomaticFlush() {
        synchronized (this.flushLock) {
            this.canFlush = true;
            if (this.packetWrites.get() != this.flushPacketsStart) { // must be after canFlush = true
                this.flush(); // only make the flush call if we need to
            }
        }
    }

    private final void flush() {
        if (this.channel.eventLoop().inEventLoop()) {
            this.channel.flush();
        } else {
            this.channel.eventLoop().execute(() -> {
                this.channel.flush();
            });
        }
    }
    // Tuinity end - allow controlled flushing
    // Tuinity start - add pending task queue
    private final Queue<Runnable> pendingTasks = new java.util.concurrent.ConcurrentLinkedQueue<>();
    public void execute(final Runnable run) {
        if (this.channel == null || !this.channel.isRegistered()) {
            run.run();
            return;
        }
        final boolean queue = !this.packetQueue.isEmpty();
        if (!queue) {
            this.channel.eventLoop().execute(run);
        } else {
            this.pendingTasks.add(run);
            if (this.packetQueue.isEmpty()) {
                // something flushed async, dump tasks now
                Runnable r;
                while ((r = this.pendingTasks.poll()) != null) {
                    this.channel.eventLoop().execute(r);
                }
            }
        }
    }
    // Tuinity end - add pending task queue

    public NetworkManager(EnumProtocolDirection enumprotocoldirection) {
        this.h = enumprotocoldirection;
    }

    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        this.channel = channelhandlercontext.channel();
        this.socketAddress = this.channel.remoteAddress();
        // Spigot Start
        this.preparing = false;
        // Spigot End

        try {
            this.setProtocol(EnumProtocol.HANDSHAKING);
        } catch (Throwable throwable) {
            NetworkManager.LOGGER.fatal(throwable);
        }

    }

    public void setProtocol(EnumProtocol enumprotocol) {
        protocol = enumprotocol; // Paper
        this.channel.attr(NetworkManager.c).set(enumprotocol);
        this.channel.config().setAutoRead(true);
        NetworkManager.LOGGER.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception {
        this.close(new ChatMessage("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) {
        // Paper start
        if (throwable instanceof EncoderException && throwable.getCause() instanceof PacketEncoder.PacketTooLargeException) {
            if (((PacketEncoder.PacketTooLargeException) throwable.getCause()).getPacket().packetTooLarge(this)) {
                return;
            } else {
                throwable = throwable.getCause();
            }
        }
        // Paper end
        if (throwable instanceof SkipEncodeException) {
            NetworkManager.LOGGER.debug("Skipping packet due to errors", throwable.getCause());
        } else {
            boolean flag = !this.u;

            this.u = true;
            if (this.channel.isOpen()) {
                EntityPlayer player = this.getPlayer(); // Paper
                if (throwable instanceof TimeoutException) {
                    NetworkManager.LOGGER.debug("Timeout", throwable);
                    if (player != null) player.quitReason = org.bukkit.event.player.PlayerQuitEvent.QuitReason.TIMED_OUT; // Paper
                    this.close(new ChatMessage("disconnect.timeout"));
                } else {
                    ChatMessage chatmessage = new ChatMessage("disconnect.genericReason", new Object[]{"Internal Exception: " + throwable});

                    if (player != null) player.quitReason = org.bukkit.event.player.PlayerQuitEvent.QuitReason.ERRONEOUS_STATE; // Paper
                    if (flag) {
                        NetworkManager.LOGGER.debug("Failed to sent packet", throwable);
                        this.sendPacket(new PacketPlayOutKickDisconnect(chatmessage), (future) -> {
                            this.close(chatmessage);
                        });
                        this.stopReading();
                    } else {
                        NetworkManager.LOGGER.debug("Double fault", throwable);
                        this.close(chatmessage);
                    }
                }

            }
        }
        if (net.minecraft.server.MinecraftServer.getServer().isDebugging()) throwable.printStackTrace(); // Spigot
    }

    // Tuinity start - packet limiter
    protected final Object PACKET_LIMIT_LOCK = new Object();
    protected final com.tuinity.tuinity.util.IntervalledCounter allPacketCounts = com.tuinity.tuinity.config.TuinityConfig.allPacketsLimit != null ? new com.tuinity.tuinity.util.IntervalledCounter(
            (long)(com.tuinity.tuinity.config.TuinityConfig.allPacketsLimit.packetLimitInterval * 1.0e9)
    ) : null;
    protected final java.util.Map<Class<? extends net.minecraft.network.protocol.Packet<?>>, com.tuinity.tuinity.util.IntervalledCounter> packetSpecificLimits = new java.util.HashMap<>();

    private boolean stopReadingPackets;
    private void killForPacketSpam() {
        this.sendPacket(new PacketPlayOutKickDisconnect(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(com.tuinity.tuinity.config.TuinityConfig.kickMessage, true)[0]), (future) -> {
            this.close(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(com.tuinity.tuinity.config.TuinityConfig.kickMessage, true)[0]);
        });
        this.stopReading();
        this.stopReadingPackets = true;
    }
    // Tuinity end - packet limiter

    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet<?> packet) throws Exception {
        if (this.channel.isOpen()) {
            // Tuinity start - packet limiter
            if (this.stopReadingPackets) {
                return;
            }
            if (this.allPacketCounts != null ||
                    com.tuinity.tuinity.config.TuinityConfig.packetSpecificLimits.containsKey(packet.getClass())) {
                long time = System.nanoTime();
                synchronized (PACKET_LIMIT_LOCK) {
                    if (this.allPacketCounts != null) {
                        this.allPacketCounts.updateAndAdd(1, time);
                        if (this.allPacketCounts.getRate() >= com.tuinity.tuinity.config.TuinityConfig.allPacketsLimit.maxPacketRate) {
                            this.killForPacketSpam();
                            return;
                        }
                    }

                    for (Class<?> check = packet.getClass(); check != Object.class; check = check.getSuperclass()) {
                        com.tuinity.tuinity.config.TuinityConfig.PacketLimit packetSpecificLimit =
                                com.tuinity.tuinity.config.TuinityConfig.packetSpecificLimits.get(check);
                        if (packetSpecificLimit == null) {
                            continue;
                        }
                        com.tuinity.tuinity.util.IntervalledCounter counter = this.packetSpecificLimits.computeIfAbsent((Class)check, (clazz) -> {
                            return new com.tuinity.tuinity.util.IntervalledCounter((long)(packetSpecificLimit.packetLimitInterval * 1.0e9));
                        });
                        counter.updateAndAdd(1, time);
                        if (counter.getRate() >= packetSpecificLimit.maxPacketRate) {
                            switch (packetSpecificLimit.violateAction) {
                                case DROP:
                                    return;
                                case KICK:
                                    this.killForPacketSpam();
                                    return;
                            }
                        }
                    }
                }
            }
            // Tuinity end - packet limiter
            try {
                a(packet, this.packetListener);
            } catch (CancelledPacketHandleException cancelledpackethandleexception) {
                ;
            }

            ++this.p;
        }

    }

    private static <T extends PacketListener> void a(Packet<T> packet, PacketListener packetlistener) {
        packet.a((T) packetlistener); // CraftBukkit - decompile error
    }

    public void setPacketListener(PacketListener packetlistener) {
        Validate.notNull(packetlistener, "packetListener", new Object[0]);
        this.packetListener = packetlistener;
    }
    // Paper start
    public EntityPlayer getPlayer() {
        if (packetListener instanceof PlayerConnection) {
            return ((PlayerConnection) packetListener).player;
        } else {
            return null;
        }
    }
    private static class InnerUtil { // Attempt to hide these methods from ProtocolLib so it doesn't accidently pick them up.
        private static java.util.List<Packet> buildExtraPackets(Packet packet) {
            java.util.List<Packet> extra = packet.getExtraPackets();
            if (extra == null || extra.isEmpty()) {
                return null;
            }
            java.util.List<Packet> ret = new java.util.ArrayList<>(1 + extra.size());
            buildExtraPackets0(extra, ret);
            return ret;
        }

        private static void buildExtraPackets0(java.util.List<Packet> extraPackets, java.util.List<Packet> into) {
            for (Packet extra : extraPackets) {
                into.add(extra);
                java.util.List<Packet> extraExtra = extra.getExtraPackets();
                if (extraExtra != null && !extraExtra.isEmpty()) {
                    buildExtraPackets0(extraExtra, into);
                }
            }
        }
        // Paper start
        private static boolean canSendImmediate(NetworkManager networkManager, Packet<?> packet) {
            return networkManager.isPending || networkManager.protocol != EnumProtocol.PLAY ||
                packet instanceof PacketPlayOutKeepAlive ||
                packet instanceof PacketPlayOutChat ||
                packet instanceof PacketPlayOutTabComplete ||
                packet instanceof PacketPlayOutTitle ||
                packet instanceof PacketPlayOutBoss;
        }
        // Paper end
    }
    // Paper end

    public void sendPacket(Packet<?> packet) {
        this.sendPacket(packet, (GenericFutureListener) null);
    }

    public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
        // Paper start - handle oversized packets better
        boolean connected = this.isConnected();
        if (!connected && !preparing) {
            return; // Do nothing
        }
        packet.onPacketDispatch(getPlayer());
        if (connected && (InnerUtil.canSendImmediate(this, packet) || (
            MCUtil.isMainThread() && packet.isReady() && this.packetQueue.isEmpty() &&
            (packet.getExtraPackets() == null || packet.getExtraPackets().isEmpty())
        ))) {
            this.writePacket(packet, genericfuturelistener, null); // Tuinity
            return;
        }
        // write the packets to the queue, then flush - antixray hooks there already
        java.util.List<Packet> extraPackets = InnerUtil.buildExtraPackets(packet);
        boolean hasExtraPackets = extraPackets != null && !extraPackets.isEmpty();
        if (!hasExtraPackets) {
            this.packetQueue.add(new NetworkManager.QueuedPacket(packet, genericfuturelistener));
        } else {
            java.util.List<NetworkManager.QueuedPacket> packets = new java.util.ArrayList<>(1 + extraPackets.size());
            packets.add(new NetworkManager.QueuedPacket(packet, null)); // delay the future listener until the end of the extra packets

            for (int i = 0, len = extraPackets.size(); i < len;) {
                Packet extra = extraPackets.get(i);
                boolean end = ++i == len;
                packets.add(new NetworkManager.QueuedPacket(extra, end ? genericfuturelistener : null)); // append listener to the end
            }

            this.packetQueue.addAll(packets); // atomic
        }
        this.sendPacketQueue();
        // Paper end
    }

    private void dispatchPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericFutureListener) { this.b(packet, genericFutureListener); } // Paper - OBFHELPER
    private void b(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
        // Tuinity start - add flush parameter
        this.writePacket(packet, genericfuturelistener, Boolean.TRUE);
    }
    private void writePacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener, Boolean flushConditional) {
        this.packetWrites.getAndIncrement(); // must be befeore using canFlush
        boolean effectiveFlush = flushConditional == null ? this.canFlush : flushConditional.booleanValue();
        final boolean flush = effectiveFlush || packet instanceof PacketPlayOutKeepAlive || packet instanceof PacketPlayOutKickDisconnect; // no delay for certain packets
        // Tuinity end - add flush parameter
        EnumProtocol enumprotocol = EnumProtocol.a(packet);
        EnumProtocol enumprotocol1 = (EnumProtocol) this.channel.attr(NetworkManager.c).get();

        ++this.q;
        if (enumprotocol1 != enumprotocol) {
            NetworkManager.LOGGER.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        EntityPlayer player = getPlayer(); // Paper
        if (this.channel.eventLoop().inEventLoop()) {
            if (enumprotocol != enumprotocol1) {
                this.setProtocol(enumprotocol);
            }
            // Paper start
            if (!isConnected()) {
                packet.onPacketDispatchFinish(player, null);
                return;
            }
            try {
                // Paper end

            ChannelFuture channelfuture = (flush) ? this.channel.writeAndFlush(packet) : this.channel.write(packet); // Tuinity - add flush parameter

            if (genericfuturelistener != null) {
                channelfuture.addListener(genericfuturelistener);
            }
            // Paper start
            if (packet.hasFinishListener()) {
                channelfuture.addListener((ChannelFutureListener) channelFuture -> packet.onPacketDispatchFinish(player, channelFuture));
            }
            // Paper end

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            // Paper start
            } catch (Exception e) {
                LOGGER.error("NetworkException: " + player, e);
                close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));;
                packet.onPacketDispatchFinish(player, null);
            }
            // Paper end
        } else {
            // Tuinity start - optimise packets that are not flushed
            Runnable choice1 = null;
            AbstractEventExecutor.LazyRunnable choice2 = null;
            // note: since the type is not dynamic here, we need to actually copy the old executor code
            // into two branches. On conflict, just re-copy - no changes were made inside the executor code.
            if (flush) {
                choice1 = () -> {
                    if (enumprotocol != enumprotocol1) {
                        this.setProtocol(enumprotocol);
                    }

                    // Paper start
                    if (!isConnected()) {
                        packet.onPacketDispatchFinish(player, null);
                        return;
                    }
                    try {
                        // Paper end
                        ChannelFuture channelfuture1 = (flush) ? this.channel.writeAndFlush(packet) : this.channel.write(packet); // Tuinity - add flush parameter


                        if (genericfuturelistener != null) {
                            channelfuture1.addListener(genericfuturelistener);
                        }
                        // Paper start
                        if (packet.hasFinishListener()) {
                            channelfuture1.addListener((ChannelFutureListener) channelFuture -> packet.onPacketDispatchFinish(player, channelFuture));
                        }
                        // Paper end

                        channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        // Paper start
                    } catch (Exception e) {
                        LOGGER.error("NetworkException: " + player, e);
                        close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));;
                        packet.onPacketDispatchFinish(player, null);
                    }
                    // Paper end
                };
            } else {
                // explicitly declare a variable to make the lambda use the type
                choice2 = () -> {
                    if (enumprotocol != enumprotocol1) {
                        this.setProtocol(enumprotocol);
                    }

                    // Paper start
                    if (!isConnected()) {
                        packet.onPacketDispatchFinish(player, null);
                        return;
                    }
                    try {
                        // Paper end
                        ChannelFuture channelfuture1 = (flush) ? this.channel.writeAndFlush(packet) : this.channel.write(packet); // Tuinity - add flush parameter


                        if (genericfuturelistener != null) {
                            channelfuture1.addListener(genericfuturelistener);
                        }
                        // Paper start
                        if (packet.hasFinishListener()) {
                            channelfuture1.addListener((ChannelFutureListener) channelFuture -> packet.onPacketDispatchFinish(player, channelFuture));
                        }
                        // Paper end

                        channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        // Paper start
                    } catch (Exception e) {
                        LOGGER.error("NetworkException: " + player, e);
                        close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));;
                        packet.onPacketDispatchFinish(player, null);
                    }
                    // Paper end
                };
            }
            this.channel.eventLoop().execute(choice1 != null ? choice1 : choice2);
            // Tuinity end - optimise packets that are not flushed
        }

    }

    // Paper start - rewrite this to be safer if ran off main thread
    private boolean sendPacketQueue() { return this.p(); } // OBFHELPER // void -> boolean
    private boolean p() { // void -> boolean
        if (!isConnected()) {
            return true;
        }
        if (MCUtil.isMainThread()) {
            return processQueue();
        } else if (isPending) {
            // Should only happen during login/status stages
            synchronized (this.packetQueue) {
                return this.processQueue();
            }
        }
        return false;
    }
    private boolean processQueue() {
        try { // Tuinity - add pending task queue
        if (this.packetQueue.isEmpty()) return true;
        final boolean needsFlush = this.canFlush; // Tuinity - make only one flush call per sendPacketQueue() call
        boolean hasWrotePacket = false;
        // If we are on main, we are safe here in that nothing else should be processing queue off main anymore
        // But if we are not on main due to login/status, the parent is synchronized on packetQueue
        java.util.Iterator<QueuedPacket> iterator = this.packetQueue.iterator();
        while (iterator.hasNext()) {
            NetworkManager.QueuedPacket queued = iterator.next(); // poll -> peek

            // Fix NPE (Spigot bug caused by handleDisconnection())
            if (false && queued == null) { // Tuinity - diff on change, this logic is redundant: iterator guarantees ret of an element - on change, hook the flush logic here
                return true;
            }

            Packet<?> packet = queued.getPacket();
            if (!packet.isReady()) {
                // Tuinity start - make only one flush call per sendPacketQueue() call
                if (hasWrotePacket && (needsFlush || this.canFlush)) {
                    this.flush();
                }
                // Tuinity end - make only one flush call per sendPacketQueue() call
                return false;
            } else {
                iterator.remove();
                this.writePacket(packet, queued.getGenericFutureListener(), (!iterator.hasNext() && (needsFlush || this.canFlush)) ? Boolean.TRUE : Boolean.FALSE); // Tuinity - make only one flush call per sendPacketQueue() call
                hasWrotePacket = true; // Tuinity - make only one flush call per sendPacketQueue() call
            }
        }
        return true;
        } finally { // Tuinity start - add pending task queue
            Runnable r;
            while ((r = this.pendingTasks.poll()) != null) {
                this.channel.eventLoop().execute(r);
            }
        } // Tuinity end - add pending task queue
    }
    // Paper end

    private static final int MAX_PER_TICK = com.destroystokyo.paper.PaperConfig.maxJoinsPerTick; // Paper
    private static int joinAttemptsThisTick; // Paper
    private static int currTick; // Paper
    public void a() {
        this.p();
        // Paper start
        if (currTick != MinecraftServer.currentTick) {
            currTick = MinecraftServer.currentTick;
            joinAttemptsThisTick = 0;
        }
        // Paper end
        if (this.packetListener instanceof LoginListener) {
            if ( ((LoginListener) this.packetListener).getLoginState() != LoginListener.EnumProtocolState.READY_TO_ACCEPT // Paper
                     || (joinAttemptsThisTick++ < MAX_PER_TICK)) { // Paper - limit the number of joins which can be processed each tick
            ((LoginListener) this.packetListener).tick();
            } // Paper
        }

        if (this.packetListener instanceof PlayerConnection) {
            // Tuinity start - detailed watchdog information
            net.minecraft.network.protocol.PlayerConnectionUtils.packetProcessing.push(this.packetListener);
            try {
            // Tuinity end - detailed watchdog information
            ((PlayerConnection) this.packetListener).tick();
            } finally { // Tuinity start - detailed watchdog information
                net.minecraft.network.protocol.PlayerConnectionUtils.packetProcessing.pop();
            } // Tuinity start - detailed watchdog information
        }

        if (this.channel != null) {
            if (enableExplicitFlush) this.channel.eventLoop().execute(() -> this.channel.flush()); // Paper - we don't need to explicit flush here, but allow opt in incase issues are found to a better version
        }

        if (this.t++ % 20 == 0) {
            this.b();
        }

    }

    protected void b() {
        this.s = MathHelper.g(0.75F, (float) this.q, this.s);
        this.r = MathHelper.g(0.75F, (float) this.p, this.r);
        this.q = 0;
        this.p = 0;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    // Paper start
    public void clearPacketQueue() {
        EntityPlayer player = getPlayer();
        packetQueue.forEach(queuedPacket -> {
            Packet<?> packet = queuedPacket.getPacket();
            if (packet.hasFinishListener()) {
                packet.onPacketDispatchFinish(player, null);
            }
        });
        packetQueue.clear();
    } // Paper end
    public void close(IChatBaseComponent ichatbasecomponent) {
        // Spigot Start
        this.preparing = false;
        clearPacketQueue(); // Paper
        // Spigot End
        if (this.channel.isOpen()) {
            this.channel.close(); // We can't wait as this may be called from an event loop.
            this.m = ichatbasecomponent;
        }

    }

    public boolean isLocal() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public void a(javax.crypto.SecretKey secretkey) { // Tuinity
        this.n = true;
        // Tuinity start
        try {
        this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(/*MinecraftEncryption.a(2, secretkey)*/ secretkey));
        this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(/*MinecraftEncryption.a(1, secretkey)*/ secretkey));
        } catch (java.security.GeneralSecurityException e) {
            throw new RuntimeException("Couldn't enable encryption", e);
        }
        // Tuinity end
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen();
    }

    public boolean i() {
        return this.channel == null;
    }

    public PacketListener j() {
        return this.packetListener;
    }

    @Nullable
    public IChatBaseComponent k() {
        return this.m;
    }

    public void stopReading() {
        this.channel.config().setAutoRead(false);
    }

    public void setCompressionLevel(int i) {
        if (i >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
                ((PacketDecompressor) this.channel.pipeline().get("decompress")).a(i);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new PacketDecompressor(i));
            }

            if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
                ((PacketCompressor) this.channel.pipeline().get("compress")).a(i);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new PacketCompressor(i));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
                this.channel.pipeline().remove("decompress");
            }

            if (this.channel.pipeline().get("compress") instanceof PacketCompressor) {
                this.channel.pipeline().remove("compress");
            }
        }

    }

    public void handleDisconnection() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (this.o) {
                //NetworkManager.LOGGER.warn("handleDisconnection() called twice"); // Paper - Do not log useless message
            } else {
                this.o = true;
                if (this.k() != null) {
                    this.j().a(this.k());
                } else if (this.j() != null) {
                    this.j().a(new ChatMessage("multiplayer.disconnect.generic"));
                }
                clearPacketQueue(); // Paper
                // Paper start - Add PlayerConnectionCloseEvent
                final PacketListener packetListener = this.j();
                if (packetListener instanceof PlayerConnection) {
                    /* Player was logged in */
                    final PlayerConnection playerConnection = (PlayerConnection) packetListener;
                    new com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent(playerConnection.player.getUniqueID(),
                        playerConnection.player.getName(), ((java.net.InetSocketAddress)socketAddress).getAddress(), false).callEvent();
                } else if (packetListener instanceof LoginListener) {
                    /* Player is login stage */
                    final LoginListener loginListener = (LoginListener) packetListener;
                    switch (loginListener.getLoginState()) {
                        case READY_TO_ACCEPT:
                        case DELAY_ACCEPT:
                        case ACCEPTED:
                            final com.mojang.authlib.GameProfile profile = loginListener.getGameProfile(); /* Should be non-null at this stage */
                            new com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent(profile.getId(), profile.getName(),
                                ((java.net.InetSocketAddress)socketAddress).getAddress(), false).callEvent();
                    }
                }
                // Paper end
            }

        }
    }

    public float n() {
        return this.r;
    }

    static class QueuedPacket {

        private final Packet<?> a; private final Packet<?> getPacket() { return this.a; } // Paper - OBFHELPER
        @Nullable
        private final GenericFutureListener<? extends Future<? super Void>> b; private final GenericFutureListener<? extends Future<? super Void>> getGenericFutureListener() { return this.b; } // Paper - OBFHELPER

        public QueuedPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
            this.a = packet;
            this.b = genericfuturelistener;
        }
    }

    // Spigot Start
    public SocketAddress getRawAddress()
    {
        // Paper start - this can be nullable in the case of a Unix domain socket, so if it is, fake something
        if (this.channel.remoteAddress() == null) {
            return new java.net.InetSocketAddress(java.net.InetAddress.getLoopbackAddress(), 0);
        }
        // Paper end
        return this.channel.remoteAddress();
    }
    // Spigot End
}
