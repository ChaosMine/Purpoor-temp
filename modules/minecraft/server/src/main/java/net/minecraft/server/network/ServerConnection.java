package net.minecraft.server.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.SystemPropertyUtil;
import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.minecraft.network.*;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.game.PacketPlayOutKickDisconnect;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.SpigotConfig;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ServerConnection {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final WriteBufferWaterMark SERVER_WRITE_MARK = new WriteBufferWaterMark(
		1_048_576, // 1M
		2_097_152 // 2M
	);

	public final EventLoopGroup boss, worker;
	private final MinecraftServer server;
	public volatile boolean open;
	private ChannelFuture listeningChannel;
	private final List<NetworkManager> connectedChannels = new ArrayList<>();
	private final Queue<NetworkManager> pending = new ConcurrentLinkedQueue<>();

	private void addPending() {
		NetworkManager manager;
		while ((manager = pending.poll()) != null) {
			connectedChannels.add(manager);
			manager.isPending = false;
		}
	}

	public ServerConnection(MinecraftServer minecraftserver) {
		this.server = minecraftserver;
		this.open = true;

		boss = new IOUringEventLoopGroup(
			1,
			new ThreadFactoryBuilder()
				.setThreadFactory(FastThreadLocalThread::new)
				.setNameFormat("Netty IOUring boss")
				.setPriority(Thread.MIN_PRIORITY + 2)
				.setDaemon(true)
				.build()
		);

		worker = new IOUringEventLoopGroup(
			SystemPropertyUtil.getInt("purpoor.nettyThreads", 4),
			new ThreadFactoryBuilder()
				.setThreadFactory(FastThreadLocalThread::new)
				.setNameFormat("Netty IOUring IO worker #%d")
				.setPriority(Thread.MAX_PRIORITY - 1)
				.setDaemon(true)
				.build()
		);
	}

	// Paper start
	public void a(@Nullable InetAddress inetaddress, int i) throws IOException {
		bind(new InetSocketAddress(inetaddress, i));
	}

	public void bind(SocketAddress address) throws IOException {
		if (IOUring.isAvailable()) {
			ServerConnection.LOGGER.info("Using io_uring channel type");
		} else {
			throw new RuntimeException("IOUring is unavailable", IOUring.unavailabilityCause());
		}

		if (address instanceof DomainSocketAddress) {
			throw new RuntimeException("FUCKING");
		}

		var bootstrap = new ServerBootstrap()
			.channelFactory(IOUringServerSocketChannel::new)
			.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, SERVER_WRITE_MARK)
			.childOption(ChannelOption.IP_TOS, 0x18)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childHandler(new ChannelInitializer<>() {
				protected void initChannel(Channel channel) {
					var nm = new NetworkManager();

					channel.pipeline()
						.addLast("timeout", new ReadTimeoutHandler(15, TimeUnit.SECONDS))
						.addLast("splitter", new PacketSplitter())
						.addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND))
						.addLast("prepender", PacketPrepender.INSTANCE)
						.addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND))
						.addLast("packet_handler", nm);

					pending.add(nm);
					nm.setPacketListener(new HandshakeListener(ServerConnection.this.server, nm));
					ChannelInitializeListenerHolder.callListeners(channel);
				}
			}).group(boss, worker)
			.localAddress(address)
			.option(ChannelOption.AUTO_READ, false);

		this.listeningChannel = bootstrap.bind().syncUninterruptibly();
	}

	public void acceptConnections() {
		this.listeningChannel.channel().config().setAutoRead(true);
	}

	public void close() {
		this.open = false;

		try {
			this.listeningChannel.channel().close().sync();
		} catch (InterruptedException interruptedexception) {
			ServerConnection.LOGGER.error("Interrupted whilst closing channel");
		}
	}

	public void c() {
		this.addPending();
		if (SpigotConfig.playerShuffle > 0 && MinecraftServer.currentTick % SpigotConfig.playerShuffle == 0) {
			Collections.shuffle(this.connectedChannels, ThreadLocalRandom.current());
		}
		var iterator = this.connectedChannels.iterator();

		while (iterator.hasNext()) {
			var nm = iterator.next();

			if (!nm.isConnecting()) {
				if (nm.isConnected()) {
					try {
						nm.tick();
					} catch (Exception exception) {
						ServerConnection.LOGGER.warn("Failed to handle packet for {}", nm.getSocketAddress(), exception);
						var chatcomponenttext = new ChatComponentText("Internal server error");

						nm.sendPacket(
							new PacketPlayOutKickDisconnect(chatcomponenttext),
							__ -> nm.close(chatcomponenttext)
						);
						nm.stopReading();
					}
				} else {
					if (nm.preparing) continue;
					iterator.remove();
					nm.handleDisconnection();
				}
			}
		}
	}

	public MinecraftServer d() {
		return this.server;
	}
}
