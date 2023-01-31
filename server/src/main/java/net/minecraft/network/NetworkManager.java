package net.minecraft.network;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import com.mojang.authlib.properties.Property;
import dev.kamillaova.paper.server.metrics.PacketMetrics;
import dev.kamillaova.paper.util.Tristate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PlayerConnectionUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.CancelledPacketHandleException;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.LoginListener;
import net.minecraft.server.network.PlayerConnection;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkManager extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Queue<QueuedPacket> packetQueue = new ConcurrentLinkedQueue<>();
	public Channel channel;
	public SocketAddress socketAddress;
	public UUID spoofedUUID;
	public Property[] spoofedProfile;
	public boolean preparing = true;
	private PacketListener packetListener;
	private IChatBaseComponent disconnectedReason;
	private boolean disconnectionHandled;
	private boolean handlingFault;
	public int protocolVersion;
	public InetSocketAddress virtualHost;
	public boolean isPending = true;
	public EnumProtocol protocol;

	volatile boolean canFlush = true;
	@SuppressWarnings("FieldMayBeFinal") // not final. changes by varhandle
	private volatile int packetWrites = 0;
	private volatile int flushPacketsStart;

	private static final VarHandle VH__packetWrites;

	static {
		try {
			var lookup = MethodHandles.lookup();
			VH__packetWrites = lookup.findVarHandle(NetworkManager.class, "packetWrites", int.class);
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	public void disableAutomaticFlush() {
		this.flushPacketsStart = this.packetWrites; // must be volatile and before canFlush = false
		this.canFlush = false;
	}

	public void enableAutomaticFlush() {
		this.canFlush = true;
		if (this.packetWrites != this.flushPacketsStart) { // must be after canFlush = true
			this.flush(); // only make the flush call if we need to
		}
	}

	private void flush() {
		var eventLoop = this.channel.eventLoop();
		if (eventLoop.inEventLoop()) {
			this.channel.flush();
		} else {
			eventLoop.execute(() -> this.channel.flush());
		}
	}

	private final Queue<Runnable> pendingTasks = new ConcurrentLinkedQueue<>();

	public void execute(Runnable run) {
		if (this.channel == null || !this.channel.isRegistered()) {
			run.run();
			return;
		}
		var queue = !this.packetQueue.isEmpty();
		if (!queue) {
			this.channel.eventLoop().execute(run);
		} else {
			this.pendingTasks.add(run);
			if (this.packetQueue.isEmpty()) {
				// something flushed async, dump tasks now
				var eventLoop = this.channel.eventLoop();
				Runnable r;
				while ((r = this.pendingTasks.poll()) != null) {
					eventLoop.execute(r);
				}
			}
		}
	}

	public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
		super.channelActive(channelhandlercontext);
		this.channel = channelhandlercontext.channel();
		this.socketAddress = this.channel.remoteAddress();
		this.preparing = false;

		try {
			this.setProtocol(EnumProtocol.HANDSHAKING);
		} catch (Throwable throwable) {
			LOGGER.fatal("Failed to change protocol to HANDSHAKING", throwable);
		}
	}

	public void setProtocol(EnumProtocol enumprotocol) {
		this.protocol = enumprotocol;
		((PacketDecoder) this.channel.pipeline().get("decoder")).setProtocol(enumprotocol);
		((PacketEncoder) this.channel.pipeline().get("encoder")).setProtocol(enumprotocol);
		this.channel.config().setAutoRead(true);
		LOGGER.debug("Enabled auto read");
	}

	public void channelInactive(ChannelHandlerContext channelhandlercontext) {
		this.close(new ChatMessage("disconnect.endOfStream"));
	}

	public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) {
		if (throwable instanceof EncoderException && throwable.getCause() instanceof PacketEncoder.PacketTooLargeException) {
			if (((PacketEncoder.PacketTooLargeException) throwable.getCause()).getPacket().packetTooLarge(this)) {
				return;
			} else {
				throwable = throwable.getCause();
			}
		}
		if (throwable instanceof SkipEncodeException) {
			LOGGER.debug("Skipping packet due to errors", throwable.getCause());
		} else {
			var notFault = !this.handlingFault;

			this.handlingFault = true;
			if (this.channel.isOpen()) {
				var player = this.getPlayer();
				if (throwable instanceof TimeoutException) {
					LOGGER.debug("Timeout", throwable);
					if (player != null) player.quitReason = PlayerQuitEvent.QuitReason.TIMED_OUT;
					this.close(new ChatMessage("disconnect.timeout"));
				} else {
					var chatmessage = new ChatMessage("disconnect.genericReason", "Internal Exception: " + throwable);

					if (player != null)
						player.quitReason = PlayerQuitEvent.QuitReason.ERRONEOUS_STATE;
					if (notFault) {
						LOGGER.debug("Failed to sent packet", throwable);
						this.sendPacket(new PacketPlayOutKickDisconnect(chatmessage), __ -> this.close(chatmessage));
						this.stopReading();
					} else {
						LOGGER.debug("Double fault", throwable);
						this.close(chatmessage);
					}
				}
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof Packet<?> packet && this.channel.isOpen()) {
			try {
				PacketMetrics.receive();
				packet.handle(this.packetListener);
			} catch (CancelledPacketHandleException ignored) {
			}
			return;
		}

		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) {
		this.channel.flush();
	}

	public void setPacketListener(PacketListener packetlistener) {
		Validate.notNull(packetlistener, "packetListener");
		this.packetListener = packetlistener;
	}

	public EntityPlayer getPlayer() {
		if (this.packetListener instanceof PlayerConnection playerConnection) {
			return playerConnection.player;
		} else {
			return null;
		}
	}

	private static class InnerUtil {
		private static List<Packet<?>> buildExtraPackets(Packet<?> packet) {
			var extra = packet.getExtraPackets();
			if (extra == null || extra.isEmpty()) {
				return null;
			}
			var ret = new ObjectArrayList<Packet<?>>(1 + extra.size());
			buildExtraPackets0(extra, ret);
			return ret;
		}

		private static void buildExtraPackets0(List<Packet<?>> extraPackets, List<Packet<?>> into) {
			for (var extra : extraPackets) {
				into.add(extra);
				var extraExtra = extra.getExtraPackets();
				if (extraExtra != null && !extraExtra.isEmpty()) {
					buildExtraPackets0(extraExtra, into);
				}
			}
		}

		private static boolean canSendImmediate(NetworkManager networkManager, Packet<?> packet) {
			return networkManager.isPending
				|| networkManager.protocol != EnumProtocol.PLAY
				|| packet instanceof PacketPlayOutKeepAlive
				|| packet instanceof PacketPlayOutChat
				|| packet instanceof PacketPlayOutTabComplete
				|| packet instanceof PacketPlayOutTitle
				|| packet instanceof PacketPlayOutBoss;
		}
	}

	public void sendPacket(Packet<?> packet) {
		this.sendPacket(packet, null);
	}

	public void sendPacket(
		Packet<?> packet,
		@Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener
	) {
		var connected = this.isConnected();
		if (!connected && !this.preparing) {
			return;
		}
		packet.onPacketDispatch(this.getPlayer());
		if (connected && InnerUtil.canSendImmediate(this, packet) || (
			this.packetQueue.isEmpty()
				&& packet.isReady()
				&& (packet.getExtraPackets() == null || packet.getExtraPackets().isEmpty())
		)) {
			this.writePacket(packet, genericfuturelistener, Tristate.UNSET);
			return;
		}

		var extraPackets = InnerUtil.buildExtraPackets(packet);
		var hasExtraPackets = extraPackets != null && !extraPackets.isEmpty();
		if (!hasExtraPackets) {
			this.packetQueue.add(new NetworkManager.QueuedPacket(packet, genericfuturelistener));
		} else {
			this.packetQueue.add(new NetworkManager.QueuedPacket(packet, null));

			for (int i = 0, len = extraPackets.size(); i < len; ) {
				var extra = extraPackets.get(i);
				var end = ++i == len;
				this.packetQueue.add(new NetworkManager.QueuedPacket(
					extra,
					end
						? genericfuturelistener
						: null
				));
			}
		}
		this.sendPacketQueue();
	}

	public void sendRawPacket(ByteBuf rawPacket) {
		var connected = this.isConnected();
		if (!connected && !this.preparing) {
			return;
		}
		if (this.packetQueue.isEmpty()) {
			this.writeRawPacket(rawPacket, Tristate.UNSET);
			return;
		}
		this.packetQueue.add(new NetworkManager.QueuedPacket(rawPacket, null));
		this.sendPacketQueue();
	}

	private void writePacket(
		Packet<?> packet,
		@Nullable GenericFutureListener<? extends Future<? super Void>> listener,
		@NotNull Tristate flushConditional
	) {
		VH__packetWrites.getAndAdd(this, 1);

		var effectiveFlush = flushConditional.isUnset()
			? this.canFlush
			: flushConditional.asBoolean();

		var flush = effectiveFlush
			|| packet instanceof PacketPlayOutKeepAlive
			|| packet instanceof PacketPlayOutKickDisconnect;

		var packetProtocol = EnumProtocol.getProtocolOfPacket(packet);
		var currentProtocol = this.protocol;

		if (currentProtocol != packetProtocol) {
			LOGGER.debug("Disabled auto read");
			this.channel.config().setAutoRead(false);
		}

		var eventLoop = this.channel.eventLoop();

		if (eventLoop.inEventLoop()) {
			this._writePacketUnsafe(packetProtocol, currentProtocol, packet, listener, flush);
		} else {
			executeWrite(
				() -> this._writePacketUnsafe(packetProtocol, currentProtocol, packet, listener, flush),
				!flush
			);
		}
	}

	private void _writePacketUnsafe(
		EnumProtocol packetProtocol,
		EnumProtocol currentProtocol,
		Packet<?> packet,
		@Nullable GenericFutureListener<? extends Future<? super Void>> listener,
		boolean flush
	) {
		if (packetProtocol != currentProtocol) {
			this.setProtocol(packetProtocol);
		}
		if (!this.isConnected()) {
			packet.onPacketDispatchFinish(this.getPlayer(), null);
			return;
		}
		try {
			var channel = this.channel;

			var noListener = listener == null;
			var noFinishListener = !packet.hasFinishListener();

			if (noListener && noFinishListener) {
				if (flush) {
					channel.writeAndFlush(packet, channel.voidPromise());
				} else {
					channel.write(packet, channel.voidPromise());
				}
			} else {
				if (!flush && !channel.isWritable()) {
					channel.flush();
				}
				var channelfuture = flush
					? channel.writeAndFlush(packet)
					: channel.write(packet);

				if (!noListener) {
					channelfuture.addListener(listener);
				}
				if (!noFinishListener) {
					channelfuture.addListener((ChannelFutureListener) future -> packet.onPacketDispatchFinish(this.getPlayer(), future));
				}
				channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			}

			PacketMetrics.transmit();
		} catch (Exception e) {
			var player = this.getPlayer();
			LOGGER.error("Network exception: {}", player, e);
			this.close(new ChatMessage("disconnect.genericReason", "Internal Exception: " + e.getMessage()));
			packet.onPacketDispatchFinish(player, null);
		}
	}

	private void writeRawPacket(
		ByteBuf rawPacket,
		@NotNull Tristate flushConditional
	) {
		VH__packetWrites.getAndAdd(this, 1);

		var flush = flushConditional.isUnset()
			? this.canFlush
			: flushConditional.asBoolean();

		var eventLoop = this.channel.eventLoop();

		if (eventLoop.inEventLoop()) {
			this._writeRawPacketUnsafe(rawPacket, flush);
		} else {
			executeWrite(() -> this._writeRawPacketUnsafe(rawPacket, flush), !flush);
		}
	}

	private void _writeRawPacketUnsafe(ByteBuf rawPacket, boolean flush) {
		if (!this.isConnected()) return;
		try {
			var channel = this.channel;

			if (flush) {
				channel.unsafe().write(rawPacket, channel.voidPromise());
				channel.flush();
			} else {
				channel.unsafe().write(rawPacket, channel.voidPromise());
			}

			PacketMetrics.transmit();
		} catch (Exception e) {
			LOGGER.error("Network exception: {}", this.getPlayer(), e);
			this.close(new ChatMessage("disconnect.genericReason", "Internal exception: " + e.getMessage()));
		}
	}

	private void executeWrite(Runnable task, boolean lazy) {
		var channel = this.channel;
		var eventLoop = channel.eventLoop();

		if (lazy || !channel.isWritable()) {
			((AbstractEventExecutor) eventLoop).lazyExecute(task);
		} else {
			eventLoop.execute(task);
		}
	}

	private void sendPacketQueue() {
		if (!this.isConnected()) {
			return;
		}
		if (MCUtil.isMainThread()) {
			this.processQueue();
		} else if (this.isPending) {
			// Should only happen during login/status stages
			synchronized (this.packetQueue) {
				this.processQueue();
			}
		}
	}

	private void processQueue() {
		try {
			if (this.packetQueue.isEmpty()) return;
			var hasWrotePacket = false;
			// If we are on main, we are safe here in that nothing else should be processing queue off main anymore
			// But if we are not on main due to login/status, the parent is synchronized on packetQueue
			var iterator = this.packetQueue.iterator();
			while (iterator.hasNext()) {
				var queued = iterator.next();

				var packet = queued.packet;

				if (packet == null) {
					iterator.remove();
					this.writeRawPacket(
						queued.rawPacket,
						(!iterator.hasNext() && (this.canFlush))
							? Tristate.TRUE
							: Tristate.FALSE
					);
					hasWrotePacket = true;
					continue;
				}

				if (!packet.isReady()) {
					if (hasWrotePacket && (this.canFlush)) {
						this.flush();
					}
					return;
				} else {
					iterator.remove();
					this.writePacket(
						packet,
						queued.genericFutureListener,
						(!iterator.hasNext() && this.canFlush)
							? Tristate.TRUE
							: Tristate.FALSE
					);
					hasWrotePacket = true;
				}
			}
		} finally {
			Runnable r;
			while ((r = this.pendingTasks.poll()) != null) {
				this.channel.eventLoop().execute(r);
			}
		}
	}

	private static final int MAX_PER_TICK = com.destroystokyo.paper.PaperConfig.maxJoinsPerTick;
	private static int joinAttemptsThisTick;
	private static int currTick;

	public void tick() {
		this.sendPacketQueue();
		if (currTick != MinecraftServer.currentTick) {
			currTick = MinecraftServer.currentTick;
			joinAttemptsThisTick = 0;
		}
		if (this.packetListener instanceof LoginListener) {
			if (((LoginListener) this.packetListener).getLoginState() != LoginListener.EnumProtocolState.READY_TO_ACCEPT
				|| (joinAttemptsThisTick++ < MAX_PER_TICK)) {
				((LoginListener) this.packetListener).tick();
			}
		}

		if (this.packetListener instanceof PlayerConnection) {
			PlayerConnectionUtils.packetProcessing.push(this.packetListener);
			try {
				((PlayerConnection) this.packetListener).tick();
			} finally {
				PlayerConnectionUtils.packetProcessing.pop();
			}
		}
	}

	public SocketAddress getSocketAddress() {
		return this.socketAddress;
	}

	public void clearPacketQueue() {
		var player = this.getPlayer();
		for (var queuedPacket : this.packetQueue) {
			var packet = queuedPacket.packet;
			if (packet != null && packet.hasFinishListener()) {
				packet.onPacketDispatchFinish(player, null);
			}
		}
		this.packetQueue.clear();
	}

	public void close(IChatBaseComponent ichatbasecomponent) {
		this.preparing = false;
		this.clearPacketQueue();
		if (this.channel.isOpen()) {
			this.channel.close(); // We can't wait as this may be called from an event loop.
			this.disconnectedReason = ichatbasecomponent;
		}
	}

	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen();
	}

	public boolean isConnecting() {
		return this.channel == null;
	}

	public PacketListener packetListener() {
		return this.packetListener;
	}

	@Nullable
	public IChatBaseComponent disconnectedReason() {
		return this.disconnectedReason;
	}

	public void stopReading() {
		this.channel.config().setAutoRead(false);
	}

	public void setCompressionLevel(int i) {
		throw new UnsupportedOperationException("Compression is not supported: tired level: " + i);
	}

	public void handleDisconnection() {
		if (this.channel != null && !this.channel.isOpen()) {
			if (!this.disconnectionHandled) {
				this.disconnectionHandled = true;
				if (this.disconnectedReason() != null) {
					this.packetListener.a(this.disconnectedReason());
				} else if (this.packetListener != null) {
					this.packetListener.a(new ChatMessage("multiplayer.disconnect.generic"));
				}
				this.clearPacketQueue();
				var packetListener = this.packetListener;
				if (packetListener instanceof PlayerConnection playerConnection) {
					new PlayerConnectionCloseEvent(
						playerConnection.player.getUniqueID(),
						playerConnection.player.getName(),
						((InetSocketAddress) this.socketAddress).getAddress(),
						false
					).callEvent();
				} else if (packetListener instanceof LoginListener loginListener) {
					switch (loginListener.getLoginState()) {
						case READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED -> {
							var profile = loginListener.getGameProfile();
							new PlayerConnectionCloseEvent(
								profile.getId(),
								profile.getName(),
								((InetSocketAddress) this.socketAddress).getAddress(),
								false
							).callEvent();
						}
					}
				}
			}
		}
	}

	public SocketAddress getRawAddress() {
		if (this.channel.remoteAddress() == null) {
			return new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);
		}
		return this.channel.remoteAddress();
	}

	static class QueuedPacket {
		final Packet<?> packet;
		final ByteBuf rawPacket;

		@Nullable
		final GenericFutureListener<? extends Future<? super Void>> genericFutureListener;

		public QueuedPacket(
			Packet<?> packet,
			@Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener
		) {
			this.packet = packet;
			this.rawPacket = null;
			this.genericFutureListener = genericfuturelistener;
		}

		public QueuedPacket(
			ByteBuf rawPacket,
			@Nullable GenericFutureListener<? extends Future<? super Void>> genericFutureListener
		) {
			this.packet = null;
			this.rawPacket = rawPacket;
			this.genericFutureListener = genericFutureListener;
		}
	}
}
