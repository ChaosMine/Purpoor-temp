package net.minecraft.server.network;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.login.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import javax.crypto.SecretKey;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginListener implements PacketLoginInListener {

	private static final AtomicInteger b = new AtomicInteger(0);
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Random random = new Random();
	private final byte[] e = new byte[4];
	private final MinecraftServer server;
	public final NetworkManager networkManager;
	private LoginListener.EnumProtocolState g;

	public final LoginListener.EnumProtocolState getLoginState() {
		return this.g;
	}

	// Paper - OBFHELPER
	private int h;
	private GameProfile i;

	private void setGameProfile(GameProfile profile) {
		this.i = profile;
	}

	public GameProfile getGameProfile() {
		return this.i;
	} // Paper - OBFHELPER

	private final String j;
	private SecretKey loginKey;
	private EntityPlayer l;
	public String hostname = ""; // CraftBukkit - add field
	private int velocityLoginMessageId = -1; // Paper - Velocity support

	public LoginListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
		this.g = LoginListener.EnumProtocolState.HELLO;
		this.j = "";
		this.server = minecraftserver;
		this.networkManager = networkmanager;
		LoginListener.random.nextBytes(this.e);
	}

	public void tick() {
		// Paper start - Do not allow logins while the server is shutting down
		if (!MinecraftServer.getServer().isRunning()) {
			this.disconnect(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(org.spigotmc.SpigotConfig.restartMessage)[0]);
			return;
		}
		// Paper end
		if (this.g == LoginListener.EnumProtocolState.READY_TO_ACCEPT) {
			// Paper start - prevent logins to be processed even though disconnect was called
			if (networkManager.isConnected()) {
				this.c();
			}
			// Paper end
		} else if (this.g == LoginListener.EnumProtocolState.DELAY_ACCEPT) {
			var entityplayer = this.server.getPlayerList().getActivePlayer(this.i.getId()); // Paper

			if (entityplayer == null) {
				this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
				this.server.getPlayerList().a(this.networkManager, this.l);
				this.l = null;
			}
		}

		if (this.h++ == 600) {
			this.disconnect(new ChatMessage("multiplayer.disconnect.slow_login"));
		}

	}

	// CraftBukkit start
	@Deprecated
	public void disconnect(String s) {
		disconnect(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(s, true)[0]); // Paper - Fix hex colors not working in some kick messages
	}
	// CraftBukkit end

	@Override
	public NetworkManager a() {
		return this.networkManager;
	}

	public void disconnect(IChatBaseComponent ichatbasecomponent) {
		try {
			LoginListener.LOGGER.info("Disconnecting {}: {}", this.d(), ichatbasecomponent.getString());
			this.networkManager.sendPacket(new PacketLoginOutDisconnect(ichatbasecomponent));
			this.networkManager.close(ichatbasecomponent);
		} catch (Exception exception) {
			LoginListener.LOGGER.error("Error whilst disconnecting player", exception);
		}

	}

	// Paper start - Cache authenticator threads
	private static final ExecutorService authenticatorPool;

	static {
		var tpe = new ThreadPoolExecutor(
			1, 8,
			1, TimeUnit.MINUTES,
			new LinkedBlockingQueue<>(),
			new ThreadFactoryBuilder()
				.setThreadFactory(FastThreadLocalThread::new)
				.setNameFormat("authenticator-pool-%d")
				.build()
		);
		tpe.prestartAllCoreThreads();
		tpe.allowCoreThreadTimeOut(false);
		authenticatorPool = tpe;
	}

	// Paper end
	// Spigot start
	public void initUUID() {
		var uuid = networkManager.spoofedUUID == null
			? EntityHuman.getOfflineUUID(this.i.getName())
			: networkManager.spoofedUUID;

		this.i = new GameProfile(uuid, this.i.getName());

		if (networkManager.spoofedProfile != null) {
			for (var property : networkManager.spoofedProfile) {
				if (!HandshakeListener.PROP_PATTERN.matcher(property.getName()).matches()) continue;
				this.i.getProperties().put(property.getName(), property);
			}
		}
	}
	// Spigot end

	public void c() {
		// Spigot start - Moved to initUUID
        /*
        if (!this.i.isComplete()) {
            this.i = this.a(this.i);
        }
        */
		// Spigot end

		// CraftBukkit start - fire PlayerLoginEvent
		var s = this.server.getPlayerList().attemptLogin(this, this.i, hostname);

		if (s != null) {
			this.g = LoginListener.EnumProtocolState.ACCEPTED;
			if (this.server.ax() >= 0) {
				this.networkManager.sendPacket(new PacketLoginOutSetCompression(this.server.ax()), (channelfuture) -> {
					this.networkManager.setCompressionLevel(this.server.ax());
				});
			}

			this.networkManager.sendPacket(new PacketLoginOutSuccess(this.i));
			var entityplayer = this.server.getPlayerList().getActivePlayer(this.i.getId()); // Paper

			if (entityplayer != null) {
				this.g = LoginListener.EnumProtocolState.DELAY_ACCEPT;
				this.l = this.server.getPlayerList().processLogin(this.i, s); // CraftBukkit - add player reference
			} else {
				this.server.getPlayerList().a(this.networkManager, this.server.getPlayerList().processLogin(this.i, s)); // CraftBukkit - add player reference
			}
		}

	}

	@Override
	public void a(IChatBaseComponent ichatbasecomponent) {
		LoginListener.LOGGER.info("{} lost connection: {}", this.d(), ichatbasecomponent.getString());
	}

	public String d() {
		return this.i != null ? this.i + " (" + this.networkManager.getSocketAddress() + ")" : String.valueOf(this.networkManager.getSocketAddress());
	}

	@Override
	public void a(PacketLoginInStart packetlogininstart) {
		Validate.validState(this.g == LoginListener.EnumProtocolState.HELLO, "Unexpected hello packet");
		this.i = packetlogininstart.b();
		if (this.server.getOnlineMode()) {
			this.g = LoginListener.EnumProtocolState.KEY;
			this.networkManager.sendPacket(new PacketLoginOutEncryptionBegin("", this.server.getKeyPair().getPublic().getEncoded(), this.e));
		} else {
			// Paper start - Velocity support
			if (com.destroystokyo.paper.PaperConfig.velocitySupport) {
				this.velocityLoginMessageId = java.util.concurrent.ThreadLocalRandom.current().nextInt();
				var packet = new PacketLoginOutCustomPayload(this.velocityLoginMessageId, com.destroystokyo.paper.proxy.VelocityProxy.PLAYER_INFO_CHANNEL, new PacketDataSerializer(Unpooled.EMPTY_BUFFER));
				this.networkManager.sendPacket(packet);
				return;
			}
			// Paper end
			// Spigot start
			// Paper start - Cache authenticator threads
			authenticatorPool.execute(() -> {
				try {
					initUUID();
					new LoginHandler().fireEvents();
				} catch (Exception ex) {
					disconnect("Failed to verify username!");
					server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + i.getName(), ex);
				}
			});
			// Paper end
			// Spigot end
		}

	}

	@Override
	public void a(PacketLoginInEncryptionBegin packetlogininencryptionbegin) {
		this.disconnect("Encryption is not supported");
	}

	// Spigot start
	public class LoginHandler {

		public void fireEvents() throws Exception {
			// Paper start - Velocity support
			if (LoginListener.this.velocityLoginMessageId == -1 && com.destroystokyo.paper.PaperConfig.velocitySupport) {
				disconnect("This server requires you to connect with Velocity.");
				return;
			}
			// Paper end
			var playerName = i.getName();
			var address = ((java.net.InetSocketAddress) networkManager.getSocketAddress()).getAddress();
			var rawAddress = ((java.net.InetSocketAddress) networkManager.getRawAddress()).getAddress(); // Paper
			var uniqueId = i.getId();
			var server = LoginListener.this.server.server;

			// Paper start
			var profile = CraftPlayerProfile.asBukkitMirror(getGameProfile());
			var asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, rawAddress, uniqueId, profile);
			server.getPluginManager().callEvent(asyncEvent);
			profile = asyncEvent.getPlayerProfile();
			profile.complete(true);
			setGameProfile(CraftPlayerProfile.asAuthlib(profile));
			playerName = i.getName();
			uniqueId = i.getId();
			// Paper end

			if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
				var event = new PlayerPreLoginEvent(playerName, address, uniqueId);
				if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
					event.disallow(asyncEvent.getResult(), asyncEvent.kickMessage()); // Paper - Adventure
				}
				var waitable = new Waitable<PlayerPreLoginEvent.Result>() {
					@Override
					protected PlayerPreLoginEvent.Result evaluate() {
						server.getPluginManager().callEvent(event);
						return event.getResult();
					}
				};

				LoginListener.this.server.processQueue.add(waitable);
				if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
					disconnect(PaperAdventure.asVanilla(event.kickMessage())); // Paper - Adventure
					return;
				}
			} else {
				if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
					disconnect(PaperAdventure.asVanilla(asyncEvent.kickMessage())); // Paper - Adventure
					return;
				}
			}
			// CraftBukkit end
			LoginListener.LOGGER.info("UUID of player {} is {}", LoginListener.this.i.getName(), LoginListener.this.i.getId());
			LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
		}
	}
	// Spigot end

	public void a(PacketLoginInCustomPayload packetloginincustompayload) {
		// Paper start - Velocity support
		if (com.destroystokyo.paper.PaperConfig.velocitySupport && packetloginincustompayload.getId() == this.velocityLoginMessageId) {
			var buf = packetloginincustompayload.getBuf();
			if (buf == null) {
				this.disconnect("This server requires you to connect with Velocity.");
				return;
			}

			if (!com.destroystokyo.paper.proxy.VelocityProxy.checkIntegrity(buf)) {
				this.disconnect("Unable to verify player details");
				return;
			}

			var listening = this.networkManager.getSocketAddress();
			var port = 0;
			if (listening instanceof java.net.InetSocketAddress) {
				port = ((java.net.InetSocketAddress) listening).getPort();
			}
			this.networkManager.socketAddress = new java.net.InetSocketAddress(com.destroystokyo.paper.proxy.VelocityProxy.readAddress(buf), port);

			this.setGameProfile(com.destroystokyo.paper.proxy.VelocityProxy.createProfile(buf));

			// Proceed with login
			authenticatorPool.execute(() -> {
				try {
					new LoginHandler().fireEvents();
				} catch (Exception ex) {
					disconnect("Failed to verify username!");
					server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + i.getName(), ex);
				}
			});
			return;
		}
		// Paper end
		this.disconnect(new ChatMessage("multiplayer.disconnect.unexpected_query_response"));
	}

	protected GameProfile a(GameProfile gameprofile) {
		var uuid = EntityHuman.getOfflineUUID(gameprofile.getName());

		return new GameProfile(uuid, gameprofile.getName());
	}

	public enum EnumProtocolState { // Paper - package private -> public

		HELLO, KEY, AUTHENTICATING, NEGOTIATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;

		EnumProtocolState() {
		}
	}
}
