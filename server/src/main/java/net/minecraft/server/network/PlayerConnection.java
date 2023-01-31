package net.minecraft.server.network;

import com.destroystokyo.paper.PaperConfig;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.papermc.paper.adventure.ChatProcessor;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.*;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PlayerConnectionUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.UtilColor;
import net.minecraft.util.thread.IAsyncTaskHandler;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.EntityFish;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EnumChatVisibility;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.vehicle.EntityBoat;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BlockCommand;
import net.minecraft.world.level.block.BlockSign;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.OperatorBoolean;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapes;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
// CraftBukkit end

public class PlayerConnection implements PacketListenerPlayIn {

	private static final Logger LOGGER = LogManager.getLogger();
	public final NetworkManager networkManager;
	private final MinecraftServer minecraftServer;
	public Runnable playerJoinReady; // Paper
	public EntityPlayer player;
	private int e;
	private long lastKeepAlive = SystemUtils.getMonotonicMillis();

	private void setLastPing(long lastPing) {
		this.lastKeepAlive = lastPing;
	}

	private long getLastPing() {
		return this.lastKeepAlive;
	}

	// Paper - OBFHELPER
	private boolean awaitingKeepAlive;

	private void setPendingPing(boolean isPending) {
		this.awaitingKeepAlive = isPending;
	}

	private boolean isPendingPing() {
		return this.awaitingKeepAlive;
	}

	// Paper - OBFHELPER
	private long h;

	private void setKeepAliveID(long keepAliveID) {
		this.h = keepAliveID;
	}

	private long getKeepAliveID() {
		return this.h;
	}

	// Paper - OBFHELPER
	private final LongArrayList keepAlives = new LongArrayList(); // Purpur
	@SuppressWarnings("unused")
	private int chatThrottle;
	@SuppressWarnings("FieldMayBeFinal")
	private int tabSpamLimiter = 0;
	@SuppressWarnings("FieldMayBeFinal")
	private int recipeSpamPackets = 0;

	private static final VarHandle VH__CHAT_THROTTLE;
	private static final VarHandle VH__TAB_SPAM_LIMITER;
	private static final VarHandle VH__RECIPE_SPAM_LIMITER;

	static {
		try {
			var lookup = MethodHandles.lookup();
			var clazz = PlayerConnection.class;

			VH__CHAT_THROTTLE = lookup.findVarHandle(clazz, "chatThrottle", int.class);
			VH__TAB_SPAM_LIMITER = lookup.findVarHandle(clazz, "tabSpamLimiter", int.class);
			VH__RECIPE_SPAM_LIMITER = lookup.findVarHandle(clazz, "recipeSpamPackets", int.class);
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	private int j;
	private final Int2ShortMap k = new Int2ShortOpenHashMap();
	private double l;
	private double m;
	private double n;
	private double o;
	private double p;
	private double q;
	private Entity r;
	private double s;
	private double t;
	private double u;
	private double v;
	private double w;
	private double x;
	private Vec3D teleportPos;
	private int teleportAwait;
	private int A;
	private boolean B;
	private int C;
	private boolean D;
	private int E;
	private int receivedMovePackets;
	private int processedMovePackets;
	private static final int MAX_SIGN_LINE_LENGTH = Integer.getInteger("Paper.maxSignLength", 80);
	private static final long KEEPALIVE_LIMIT = Long.getLong("paper.playerconnection.keepalive", 30) * 1000; // Paper - provide property to set keepalive limit

	private String clientBrandName = null; // Paper - Brand name

	public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
		this.minecraftServer = minecraftserver;
		this.networkManager = networkmanager;
		networkmanager.setPacketListener(this);
		this.player = entityplayer;
		entityplayer.playerConnection = this;
		var itextfilter = entityplayer.Q();

		if (itextfilter != null) {
			itextfilter.a();
		}

		// CraftBukkit start - add fields and methods
		this.server = minecraftserver.server;
	}

	private final org.bukkit.craftbukkit.CraftServer server;
	public boolean processedDisconnect;
	private int lastTick = MinecraftServer.currentTick;
	private int allowedPlayerTicks = 1;
	private int lastDropTick = MinecraftServer.currentTick;
	private int lastBookTick = MinecraftServer.currentTick;
	private int dropCount = 0;
	private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 6 * 6;
	private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 7 * 7;

	// Get position of last block hit for BlockDamageLevel.STOPPED
	private double lastPosX = Double.MAX_VALUE;
	private double lastPosY = Double.MAX_VALUE;
	private double lastPosZ = Double.MAX_VALUE;
	private float lastPitch = Float.MAX_VALUE;
	private float lastYaw = Float.MAX_VALUE;
	private boolean justTeleported = false;
	private boolean hasMoved; // Spigot

	public CraftPlayer getPlayer() {
		return (this.player == null) ? null : this.player.getBukkitEntity();
	}
	// CraftBukkit end

	public void tick() {
		if (this.grim$E$noReminderTicks > 20) {
			kickDoNot(6666);
			return;
		}

		// Paper start - login async
		var playerJoinReady = this.playerJoinReady;
		if (playerJoinReady != null) {
			this.playerJoinReady = null;
			playerJoinReady.run();
		}
		// Don't tick if not valid (dead), otherwise we load chunks below
		if (this.player.valid) {
			// Paper end
			this.syncPosition();
			this.player.lastX = this.player.locX();
			this.player.lastY = this.player.locY();
			this.player.lastZ = this.player.locZ();
			this.player.playerTick();
			this.player.setLocation(this.l, this.m, this.n, this.player.yaw, this.player.pitch);
			++this.e;
			this.processedMovePackets = this.receivedMovePackets;
			if (this.B && !this.player.isSleeping()) {
				if (++this.C > 80) {
					PlayerConnection.LOGGER.warn("{} was kicked for floating too long!", this.player.getDisplayName().getString());
					this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickPlayerMessage, org.bukkit.event.player.PlayerKickEvent.Cause.FLYING_PLAYER); // Paper - use configurable kick message & kick event cause
					return;
				}
			} else {
				this.B = false;
				this.C = 0;
			}

			this.r = this.player.getRootVehicle();
			if (this.r != this.player && this.r.getRidingPassenger() == this.player) {
				this.s = this.r.locX();
				this.t = this.r.locY();
				this.u = this.r.locZ();
				this.v = this.r.locX();
				this.w = this.r.locY();
				this.x = this.r.locZ();
				if (this.D && this.player.getRootVehicle().getRidingPassenger() == this.player) {
					if (++this.E > 80) {
						PlayerConnection.LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getDisplayName().getString());
						this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickVehicleMessage, org.bukkit.event.player.PlayerKickEvent.Cause.FLYING_VEHICLE); // Paper - use configurable kick message & kick event cause
						return;
					}
				} else {
					this.D = false;
					this.E = 0;
				}
			} else {
				this.r = null;
				this.D = false;
				this.E = 0;
			}
		} // Paper - end if (valid)

		// Paper Start - give clients a longer time to respond to pings as per pre 1.12.2 timings
		// This should effectively place the keepalive handling back to "as it was" before 1.12.2
		var currentTime = SystemUtils.getMonotonicMillis();
		var elapsedTime = currentTime - this.getLastPing();

		// Purpur start
		if (net.pl3x.purpur.PurpurConfig.useAlternateKeepAlive) {
			if (elapsedTime >= 1000L) { // 1 second
				if (!processedDisconnect && keepAlives.size() > KEEPALIVE_LIMIT) {
					PlayerConnection.LOGGER.warn("{} was kicked due to keepalive timeout!", player.getName());
					disconnect(new ChatMessage("disconnect.timeout"));
				} else {
					setLastPing(currentTime); // hijack this field for 1 second intervals
					keepAlives.add(currentTime); // currentTime is ID
					sendPacket(new PacketPlayOutKeepAlive(currentTime));
				}
			}
		} else
			// Purpur end

			if (this.isPendingPing()) {
				if (!this.processedDisconnect && elapsedTime >= KEEPALIVE_LIMIT) { // check keepalive limit, don't fire if already disconnected
					PlayerConnection.LOGGER.warn("{} was kicked due to keepalive timeout!", this.player.getName()); // more info
					this.disconnect(new ChatMessage("disconnect.timeout", new Object[0]), org.bukkit.event.player.PlayerKickEvent.Cause.TIMEOUT); // Paper - kick event cause
				}
			} else {
				if (elapsedTime >= 15000L) { // 15 seconds
					this.setPendingPing(true);
					this.setLastPing(currentTime);
					this.setKeepAliveID(currentTime);
					this.sendPacket(new PacketPlayOutKeepAlive(this.getKeepAliveID()));
				}
			}
		// Paper end

		// CraftBukkit start
		for (int spam; (spam = this.chatThrottle) > 0 && !VH__CHAT_THROTTLE.compareAndSet(this, spam, spam - 1); ) ;
		if (tabSpamLimiter > 0) VH__TAB_SPAM_LIMITER.getAndAdd(this, -1); // Paper - split to seperate variable
		if (recipeSpamPackets > 0) VH__RECIPE_SPAM_LIMITER.getAndAdd(this, -1); // Paper
        /* Use thread-safe field access instead
        if (this.chatThrottle > 0) {
            --this.chatThrottle;
        }
        */
		// CraftBukkit end

		if (this.j > 0) {
			--this.j;
		}
	}

	public void syncPosition() {
		this.l = this.player.locX();
		this.m = this.player.locY();
		this.n = this.player.locZ();
		this.o = this.player.locX();
		this.p = this.player.locY();
		this.q = this.player.locZ();
	}

	@Override
	public NetworkManager a() {
		return this.networkManager;
	}

	private boolean isExemptPlayer() {
		return this.minecraftServer.a(this.player.getProfile());
	}

	public void disconnect(String s) {
		// Paper start
		this.disconnect(PaperAdventure.LEGACY_SECTION_UXRC.deserialize(s), org.bukkit.event.player.PlayerKickEvent.Cause.UNKNOWN);
	}

	public void disconnect(String s, PlayerKickEvent.Cause cause) {
		this.disconnect(PaperAdventure.LEGACY_SECTION_UXRC.deserialize(s), cause);
	}

	public void disconnect(IChatBaseComponent reason) {
		this.disconnect(PaperAdventure.asAdventure(reason), org.bukkit.event.player.PlayerKickEvent.Cause.UNKNOWN);
	}

	public void disconnect(IChatBaseComponent reason, PlayerKickEvent.Cause cause) {
		this.disconnect(PaperAdventure.asAdventure(reason), cause);
	}

	public void disconnect(net.kyori.adventure.text.Component reason, org.bukkit.event.player.PlayerKickEvent.Cause cause) {
		// Paper end
		// CraftBukkit start - fire PlayerKickEvent
		if (this.processedDisconnect) {
			return;
		}
		net.kyori.adventure.text.Component leaveMessage = net.kyori.adventure.text.Component.translatable("multiplayer.player.left", net.kyori.adventure.text.format.NamedTextColor.YELLOW, this.player.getBukkitEntity().displayName()); // Paper - Adventure

		var event = new PlayerKickEvent(this.server.getPlayer(this.player), reason, leaveMessage, cause); // Paper - Adventure & kick event reason

		if (this.server.getServer().isRunning()) {
			this.server.getPluginManager().callEvent(event);
		}

		if (event.isCancelled()) {
			// Do not kick the player
			return;
		}
		// Send the possibly modified leave message
		var ichatbasecomponent = PaperAdventure.asVanilla(event.reason()); // Paper - Adventure
		// CraftBukkit end

		this.player.quitReason = org.bukkit.event.player.PlayerQuitEvent.QuitReason.KICKED; // Paper
		this.networkManager.sendPacket(new PacketPlayOutKickDisconnect(ichatbasecomponent), (future) -> {
			this.networkManager.close(ichatbasecomponent);
		});
		this.a(ichatbasecomponent); // CraftBukkit - fire quit instantly
		this.networkManager.stopReading();
		var minecraftserver = this.minecraftServer;
		var networkmanager = this.networkManager;

		this.networkManager.getClass();
		// CraftBukkit - Don't wait
		minecraftserver.scheduleOnMain(networkmanager::handleDisconnection); // Paper
	}

	private <T> void a(T t0, Consumer<T> consumer, BiFunction<ITextFilter, T, CompletableFuture<Optional<T>>> bifunction) {
		IAsyncTaskHandler<?> iasynctaskhandler = this.player.getWorldServer().getMinecraftServer();
		Consumer<T> consumer1 = (object) -> {
			if (this.a().isConnected()) {
				consumer.accept(object);
			} else {
				PlayerConnection.LOGGER.debug("Ignoring packet due to disconnection");
			}

		};
		var itextfilter = this.player.Q();

		if (itextfilter != null) {
			(bifunction.apply(itextfilter, t0)).thenAcceptAsync((optional) -> { // CraftBukkit - decompile error
				optional.ifPresent(consumer1);
			}, iasynctaskhandler);
		} else {
			iasynctaskhandler.execute(() -> {
				consumer1.accept(t0);
			});
		}

	}

	private void a(String s, Consumer<String> consumer) {
		this.a(s, consumer, ITextFilter::a);
	}

	private void a(List<String> list, Consumer<List<String>> consumer) {
		this.a(list, consumer, ITextFilter::a);
	}

	@Override
	public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsteervehicle, this, this.player.getWorldServer());
		if (!this.player.isPassenger()) {
			kickDoNot(305);
			return;
		}
		var forwards = Math.abs(packetplayinsteervehicle.c());
		var sideways = Math.abs(packetplayinsteervehicle.b());

		if (forwards > 0.98f || sideways > 0.98f) {
			kickDoNot(444);
			return;
		}

		this.grim$E$noReminderTicks = 0;
		this.player.a(
			packetplayinsteervehicle.b(),
			packetplayinsteervehicle.c(),
			packetplayinsteervehicle.d(),
			packetplayinsteervehicle.e()
		);
	}

	private static boolean b(PacketPlayInFlying packetplayinflying) {
		return !Doubles.isFinite(packetplayinflying.a(0.0D)) || !Doubles.isFinite(packetplayinflying.b(0.0D)) || !Doubles.isFinite(packetplayinflying.c(0.0D)) || !Floats.isFinite(packetplayinflying.b(0.0F)) || !Floats.isFinite(packetplayinflying.a(0.0F)) || Math.abs(packetplayinflying.a(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.b(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.c(0.0D)) > 3.0E7D;
	}

	private static boolean b(PacketPlayInVehicleMove packetplayinvehiclemove) {
		return !Doubles.isFinite(packetplayinvehiclemove.getX()) || !Doubles.isFinite(packetplayinvehiclemove.getY()) || !Doubles.isFinite(packetplayinvehiclemove.getZ()) || !Floats.isFinite(packetplayinvehiclemove.getPitch()) || !Floats.isFinite(packetplayinvehiclemove.getYaw());
	}

	@Override
	public void a(PacketPlayInVehicleMove packetplayinvehiclemove) {
		PlayerConnectionUtils.ensureMainThread(packetplayinvehiclemove, this, this.player.getWorldServer());
		if (b(packetplayinvehiclemove)) {
			this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_vehicle_movement"), org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_VEHICLE_MOVEMENT); // Paper - kick event cause
		} else {
			var entity = this.player.getRootVehicle();

			if (entity != this.player && entity.getRidingPassenger() == this.player && entity == this.r) {
				var worldserver = this.player.getWorldServer();
				var d0 = entity.locX();
				var fromX = d0; // Paper - OBFHELPER
				var d1 = entity.locY();
				var fromY = d1; // Paper - OBFHELPER
				var d2 = entity.locZ();
				var fromZ = d2; // Paper - OBFHELPER
				var d3 = packetplayinvehiclemove.getX();
				var toX = d3; // Paper - OBFHELPER
				var d4 = packetplayinvehiclemove.getY();
				var toY = d4; // Paper - OBFHELPER
				var d5 = packetplayinvehiclemove.getZ();
				var toZ = d5; // Paper - OBFHELPER
				var f = packetplayinvehiclemove.getYaw();
				var f1 = packetplayinvehiclemove.getPitch();
				var d6 = d3 - this.s;
				var d7 = d4 - this.t;
				var d8 = d5 - this.u;
				var d9 = entity.getMot().g();
				// Paper start - fix large move vectors killing the server
				var currDeltaX = toX - fromX;
				var currDeltaY = toY - fromY;
				var currDeltaZ = toZ - fromZ;
				var d10 = Math.max(d6 * d6 + d7 * d7 + d8 * d8, (currDeltaX * currDeltaX + currDeltaY * currDeltaY + currDeltaZ * currDeltaZ) - 1);
				// Paper end - fix large move vectors killing the server
				// Tuinity start - fix large move vectors killing the server
				var otherFieldX = d3 - this.v;
				var otherFieldY = d4 - this.w - 1.0E-6D;
				var otherFieldZ = d5 - this.x;
				d10 = Math.max(d10, (otherFieldX * otherFieldX + otherFieldY * otherFieldY + otherFieldZ * otherFieldZ) - 1);
				// Tuinity end - fix large move vectors killing the server

				// CraftBukkit start - handle custom speeds and skipped ticks
				this.allowedPlayerTicks += (SystemUtils.getMonotonicMillis() / 50) - this.lastTick;
				this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
				this.lastTick = (int) (SystemUtils.getMonotonicMillis() / 50);

				++this.receivedMovePackets;
				var i = this.receivedMovePackets - this.processedMovePackets;
				if (i > Math.max(this.allowedPlayerTicks, 5)) {
					PlayerConnection.LOGGER.debug(this.player.getName() + " is sending move packets too frequently (" + i + " packets since last tick)");
					i = 1;
				}

				if (d10 > 0) {
					allowedPlayerTicks -= 1;
				} else {
					allowedPlayerTicks = 20;
				}
				double speed;
				if (player.abilities.isFlying) {
					speed = player.abilities.flySpeed * 20f;
				} else {
					speed = player.abilities.walkSpeed * 10f;
				}
				speed *= 2f; // TODO: Get the speed of the vehicle instead of the player

				// Paper start - Prevent moving into unloaded chunks
				if (player.world.paperConfig.preventMovingIntoUnloadedChunks // Tuinity - improve this check
					&& (!worldserver.areChunksLoadedForMove(this.player.getBoundingBoxAt(this.player.locX(), this.player.locY(), this.player.locZ()).expand(toX - this.player.locX(), toY - this.player.locY(), toZ - this.player.locZ()))) // Tuinity - improve this check
					|| !worldserver.areChunksLoadedForMove(entity.getBoundingBoxAt(entity.locX(), entity.locY(), entity.locZ()).expand(toX - entity.locX(), toY - entity.locY(), toZ - entity.locZ()))) { // Tuinity - improve this check
					this.networkManager.sendPacket(new PacketPlayOutVehicleMove(entity));
					return;
				}
				// Paper end

				if (d10 - d9 > Math.max(100.0D, Math.pow(org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed, 2)) && !this.isExemptPlayer()) {
					// CraftBukkit end
					PlayerConnection.LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getDisplayName().getString(), this.player.getDisplayName().getString(), d6, d7, d8);
					this.networkManager.sendPacket(new PacketPlayOutVehicleMove(entity));
					return;
				}

				//boolean flag = worldserver.getCubes(entity, entity.getBoundingBox().shrink(0.0625D)); // Tuinity - replace with different checks
				var oldBox = entity.getBoundingBox(); // Tuinity - copy from player movement packet

				d6 = d3 - this.v; // Tuinity - diff on change, used for checking large move vectors above
				d7 = d4 - this.w - 1.0E-6D; // Tuinity - diff on change, used for checking large move vectors above
				d8 = d5 - this.x; // Tuinity - diff on change, used for checking large move vectors above
				entity.move(EnumMoveType.PLAYER, new Vec3D(d6, d7, d8));
				var didCollide = toX != entity.locX() || toY != entity.locY() || toZ != entity.locZ(); // Tuinity - needed here as the difference in Y can be reset - also note: this is only a guess at whether collisions took place, floating point errors can make this true when it shouldn't be...
				var d11 = d7;

				d6 = d3 - entity.locX();
				d7 = d4 - entity.locY();
				if (d7 > -0.5D || d7 < 0.5D) {
					d7 = 0.0D;
				}

				d8 = d5 - entity.locZ();
				d10 = d6 * d6 + d7 * d7 + d8 * d8;
				var flag1 = false;

				if (d10 > org.spigotmc.SpigotConfig.movedWronglyThreshold) { // Spigot
					flag1 = true; // Tuinity - diff on change, this should be moved wrongly
					PlayerConnection.LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", entity.getDisplayName().getString(), this.player.getDisplayName().getString(), Math.sqrt(d10));
				}
				var curPos = this.getPlayer().getLocation(); // Spigot

				entity.setLocation(d3, d4, d5, f, f1);
				player.setLocation(d3, d4, d5, this.player.yaw, this.player.pitch); // CraftBukkit
				//boolean flag2 = worldserver.getCubes(entity, entity.getBoundingBox().shrink(0.0625D)); // Tuinity - replace with different checks

				// Tuinity start - optimise out extra getCubes
				var teleportBack = flag1; // violating this is always a fail
				if (!teleportBack) {
					// note: only call after setLocation, or else getBoundingBox is wrong
					var newBox = entity.getBoundingBox();
					if (didCollide || !oldBox.equals(newBox)) {
						teleportBack = this.hasNewCollision(worldserver, entity, oldBox, newBox);
					} // else: no collision at all detected, why do we care?
				}
				if (teleportBack) { // Tuinity end - optimise out extra getCubes
					entity.setLocation(d0, d1, d2, f, f1);
					player.setLocation(d0, d1, d2, this.player.yaw, this.player.pitch); // CraftBukkit
					this.networkManager.sendPacket(new PacketPlayOutVehicleMove(entity));
					return;
				}

				// CraftBukkit start - fire PlayerMoveEvent
				Player player = this.getPlayer();
				// Spigot Start
				if (!hasMoved) {
					lastPosX = curPos.getX();
					lastPosY = curPos.getY();
					lastPosZ = curPos.getZ();
					lastYaw = curPos.getYaw();
					lastPitch = curPos.getPitch();
					hasMoved = true;
				}
				// Spigot End
				var from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
				var to = player.getLocation().clone(); // Start off the To location as the Players current location.

				// If the packet contains movement information then we update the To location with the correct XYZ.
				to.setX(packetplayinvehiclemove.getX());
				to.setY(packetplayinvehiclemove.getY());
				to.setZ(packetplayinvehiclemove.getZ());


				// If the packet contains look information then we update the To location with the correct Yaw & Pitch.
				to.setYaw(packetplayinvehiclemove.getYaw());
				to.setPitch(packetplayinvehiclemove.getPitch());

				// Prevent 40 event-calls for less than a single pixel of movement >.>
				var delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
				var deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

				if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isFrozen()) {
					this.lastPosX = to.getX();
					this.lastPosY = to.getY();
					this.lastPosZ = to.getZ();
					this.lastYaw = to.getYaw();
					this.lastPitch = to.getPitch();

					if (!to.getWorld().getUID().equals(from.getWorld().getUID()) || to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ() || to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch())
						this.player.resetIdleTimer(); // Purpur

					// Skip the first time we do this
					// Spigot - don't skip any move events
					var oldTo = to.clone();
					var event = new PlayerMoveEvent(player, from, to);
					this.server.getPluginManager().callEvent(event);

					// If the event is cancelled we move the player back to their old location.
					if (event.isCancelled()) {
						teleport(from);
						return;
					}

					// If a Plugin has changed the To destination then we teleport the Player
					// there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
					// We only do this if the Event was not cancelled.
					if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
						this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
						return;
					}

					// Check to see if the Players Location has some how changed during the call of the event.
					// This can happen due to a plugin teleporting the player instead of using .setTo()
					if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
						this.justTeleported = false;
						return;
					}
				}
				// CraftBukkit end

				this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
				this.player.checkMovement(this.player.locX() - d0, this.player.locY() - d1, this.player.locZ() - d2);
				this.D = d11 >= -0.03125D && !this.minecraftServer.getAllowFlight() && this.a(entity);
				this.v = entity.locX();
				this.w = entity.locY();
				this.x = entity.locZ();
			}

		}
	}

	private boolean a(Entity entity) {
		// Tuinity start - stop using streams, this is already a known fixed problem in Entity#move
		var box = entity.getBoundingBox().g(0.0625D).b(0.0D, -0.55D, 0.0D);
		var minX = MathHelper.floor(box.minX);
		var minY = MathHelper.floor(box.minY);
		var minZ = MathHelper.floor(box.minZ);
		var maxX = MathHelper.floor(box.maxX);
		var maxY = MathHelper.floor(box.maxY);
		var maxZ = MathHelper.floor(box.maxZ);

		var world = entity.world;
		var pos = new BlockPosition.MutableBlockPosition();

		for (var y = minY; y <= maxY; ++y) {
			for (var z = minZ; z <= maxZ; ++z) {
				for (var x = minX; x <= maxX; ++x) {
					pos.setValues(x, y, z);
					var type = world.getTypeIfLoaded(pos);
					if (type != null && !type.isAir()) {
						return false;
					}
				}
			}
		}

		return true;
		// Tuinity end - stop using streams, this is already a known fixed problem in Entity#move
	}

	@Override
	public void a(PacketPlayInTeleportAccept packetplayinteleportaccept) {
		PlayerConnectionUtils.ensureMainThread(packetplayinteleportaccept, this, this.player.getWorldServer());
		if (packetplayinteleportaccept.b() == this.teleportAwait && this.teleportPos != null) { // CraftBukkit
			this.player.setPositionRotation(this.teleportPos.x, this.teleportPos.y, this.teleportPos.z, this.player.yaw, this.player.pitch); // Paper - use proper setPositionRotation for teleportation
			this.o = this.teleportPos.x;
			this.p = this.teleportPos.y;
			this.q = this.teleportPos.z;
			if (this.player.H()) {
				this.player.I();
			}

			this.teleportPos = null;
			this.player.getWorldServer().getChunkProvider().movePlayer(this.player); // CraftBukkit
		}

	}

	@Override
	public void a(PacketPlayInRecipeDisplayed packetplayinrecipedisplayed) {
		PlayerConnectionUtils.ensureMainThread(packetplayinrecipedisplayed, this, this.player.getWorldServer());
		var optional = this.minecraftServer.getCraftingManager().getRecipe(packetplayinrecipedisplayed.b()); // CraftBukkit - decompile error
		var recipebookserver = this.player.getRecipeBook();

		optional.ifPresent(recipebookserver::e);
	}

	@Override
	public void a(PacketPlayInRecipeSettings packetplayinrecipesettings) {
		PlayerConnectionUtils.ensureMainThread(packetplayinrecipesettings, this, this.player.getWorldServer());
		this.player.getRecipeBook().a(packetplayinrecipesettings.b(), packetplayinrecipesettings.c(), packetplayinrecipesettings.d());
	}

	@Override
	public void a(PacketPlayInAdvancements packetplayinadvancements) {
		PlayerConnectionUtils.ensureMainThread(packetplayinadvancements, this, this.player.getWorldServer());
		if (packetplayinadvancements.c() == PacketPlayInAdvancements.Status.OPENED_TAB) {
			var minecraftkey = packetplayinadvancements.d();
			var advancement = this.minecraftServer.getAdvancementData().a(minecraftkey);

			if (advancement != null) {
				this.player.getAdvancementData().a(advancement);
			}
		}

	}

	@Override
	public void a(PacketPlayInTabComplete packetplayintabcomplete) {
		// PlayerConnectionUtils.ensureMainThread(packetplayintabcomplete, this, this.player.getWorldServer()); // Paper - run this async
		// CraftBukkit start
		var tabSpamIncrement = com.destroystokyo.paper.PaperConfig.tabSpamIncrement;
		if (((int) VH__TAB_SPAM_LIMITER.getAndAdd(this, tabSpamIncrement)) + tabSpamIncrement > com.destroystokyo.paper.PaperConfig.tabSpamLimit && !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) { // Paper start - split and make configurable
			minecraftServer.scheduleOnMain(() -> this.disconnect(new ChatMessage("disconnect.spam", new Object[0]), org.bukkit.event.player.PlayerKickEvent.Cause.SPAM)); // Paper - kick event cause
			return;
		}
		// Paper start
		var str = packetplayintabcomplete.c();
		var index = -1;
		if (str.length() > 64 && ((index = str.indexOf(' ')) == -1 || index >= 64)) {
			minecraftServer.scheduleOnMain(() -> this.disconnect(new ChatMessage("disconnect.spam", new Object[0]), org.bukkit.event.player.PlayerKickEvent.Cause.SPAM)); // Paper - kick event cause
			return;
		}
		// Paper end
		// CraftBukkit end
		var stringreader = new StringReader(packetplayintabcomplete.c());

		if (stringreader.canRead() && stringreader.peek() == '/') {
			stringreader.skip();
		}

		// Paper start - async tab completion
		com.destroystokyo.paper.event.server.AsyncTabCompleteEvent event;
		var buffer = packetplayintabcomplete.c();
		event = new com.destroystokyo.paper.event.server.AsyncTabCompleteEvent(this.getPlayer(),
			buffer, true, null);
		event.callEvent();
		java.util.List<com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion> completions = event.isCancelled() ? com.google.common.collect.ImmutableList.of() : event.completions();
		// If the event isn't handled, we can assume that we have no completions, and so we'll ask the server
		if (!event.isHandled()) {
			if (!event.isCancelled()) {

				this.minecraftServer.scheduleOnMain(() -> { // Paper - This needs to be on main
					var parseresults = this.minecraftServer.getCommandDispatcher().a().parse(stringreader, this.player.getCommandListener());

					this.minecraftServer.getCommandDispatcher().a().getCompletionSuggestions(parseresults).thenAccept((suggestions) -> {
						// Paper start
						var suggestEvent = new com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent(this.getPlayer(), suggestions, buffer);
						suggestEvent.setCancelled(suggestions.isEmpty());
						if (!suggestEvent.callEvent()) return;
						this.networkManager.sendPacket(new PacketPlayOutTabComplete(packetplayintabcomplete.b(), suggestEvent.getSuggestions())); // CraftBukkit - decompile error // Paper
						// Paper end
					});
				});
			}
		} else if (!completions.isEmpty()) {
			var builder0 = new com.mojang.brigadier.suggestion.SuggestionsBuilder(packetplayintabcomplete.c(), stringreader.getTotalLength());

			var builder = builder0.createOffset(builder0.getInput().lastIndexOf(' ') + 1);
			completions.forEach(completion -> {
				if (completion.tooltip() == null) {
					builder.suggest(completion.suggestion());
				} else {
					builder.suggest(completion.suggestion(), PaperAdventure.asVanilla(completion.tooltip()));
				}
			});
			var suggestions = builder.buildFuture().join();
			var suggestEvent = new com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent(this.getPlayer(), suggestions, buffer);
			suggestEvent.setCancelled(suggestions.isEmpty());
			if (!suggestEvent.callEvent()) return;
			this.networkManager.sendPacket(new PacketPlayOutTabComplete(packetplayintabcomplete.b(), suggestEvent.getSuggestions()));
		}
		// Paper end - async tab completion
	}

	@Override
	public void a(PacketPlayInSetCommandBlock packetplayinsetcommandblock) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsetcommandblock, this, this.player.getWorldServer());
		if (!this.minecraftServer.getEnableCommandBlock()) {
			this.player.sendMessage(new ChatMessage("advMode.notEnabled"), SystemUtils.b);
		} else if (!this.player.isCreativeAndOp() && !this.player.isCreative() && !this.player.getBukkitEntity().hasPermission("minecraft.commandblock")) { // Paper - command block permission
			this.player.sendMessage(new ChatMessage("advMode.notAllowed"), SystemUtils.b);
		} else {
			CommandBlockListenerAbstract commandblocklistenerabstract = null;
			TileEntityCommand tileentitycommand = null;
			var blockposition = packetplayinsetcommandblock.b();
			var tileentity = this.player.world.getTileEntity(blockposition);

			if (tileentity instanceof TileEntityCommand) {
				tileentitycommand = (TileEntityCommand) tileentity;
				commandblocklistenerabstract = tileentitycommand.getCommandBlock();
			}

			var s = packetplayinsetcommandblock.c();
			var flag = packetplayinsetcommandblock.d();

			if (commandblocklistenerabstract != null) {
				var tileentitycommand_type = tileentitycommand.m();
				var enumdirection = this.player.world.getType(blockposition).get(BlockCommand.a);
				IBlockData iblockdata;

				switch (packetplayinsetcommandblock.g()) {
					case SEQUENCE -> {
						iblockdata = Blocks.CHAIN_COMMAND_BLOCK.getBlockData();
						this.player.world.setTypeAndData(blockposition, iblockdata.set(BlockCommand.a, enumdirection).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
					}
					case AUTO -> {
						iblockdata = Blocks.REPEATING_COMMAND_BLOCK.getBlockData();
						this.player.world.setTypeAndData(blockposition, iblockdata.set(BlockCommand.a, enumdirection).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
					}
					default -> {
						iblockdata = Blocks.COMMAND_BLOCK.getBlockData();
						this.player.world.setTypeAndData(blockposition, iblockdata.set(BlockCommand.a, enumdirection).set(BlockCommand.b, packetplayinsetcommandblock.e()), 2);
					}
				}

				tileentity.r();
				this.player.world.setTileEntity(blockposition, tileentity);
				commandblocklistenerabstract.setCommand(s);
				commandblocklistenerabstract.a(flag);
				if (!flag) {
					commandblocklistenerabstract.b((IChatBaseComponent) null);
				}

				tileentitycommand.b(packetplayinsetcommandblock.f());
				if (tileentitycommand_type != packetplayinsetcommandblock.g()) {
					tileentitycommand.h();
				}

				commandblocklistenerabstract.e();
				if (!UtilColor.b(s)) {
					this.player.sendMessage(new ChatMessage("advMode.setCommand.success", s), SystemUtils.b);
				}
			}

		}
	}

	@Override
	public void a(PacketPlayInSetCommandMinecart packetplayinsetcommandminecart) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsetcommandminecart, this, this.player.getWorldServer());
		if (!this.minecraftServer.getEnableCommandBlock()) {
			this.player.sendMessage(new ChatMessage("advMode.notEnabled"), SystemUtils.b);
		} else if (!this.player.isCreativeAndOp() && !this.player.isCreative() && !this.player.getBukkitEntity().hasPermission("minecraft.commandblock")) { // Paper - command block permission
			this.player.sendMessage(new ChatMessage("advMode.notAllowed"), SystemUtils.b);
		} else {
			var commandblocklistenerabstract = packetplayinsetcommandminecart.a(this.player.world);

			if (commandblocklistenerabstract != null) {
				commandblocklistenerabstract.setCommand(packetplayinsetcommandminecart.b());
				commandblocklistenerabstract.a(packetplayinsetcommandminecart.c());
				if (!packetplayinsetcommandminecart.c()) {
					commandblocklistenerabstract.b((IChatBaseComponent) null);
				}

				commandblocklistenerabstract.e();
				this.player.sendMessage(new ChatMessage("advMode.setCommand.success", packetplayinsetcommandminecart.b()), SystemUtils.b);
			}

		}
	}

	@Override
	public void a(PacketPlayInPickItem packetplayinpickitem) {
		PlayerConnectionUtils.ensureMainThread(packetplayinpickitem, this, this.player.getWorldServer());
		// Paper start - validate pick item position
		if (!(packetplayinpickitem.b() >= 0 && packetplayinpickitem.b() < this.player.inventory.items.size())) {
			PlayerConnection.LOGGER.warn("{} tried to set an invalid carried item", this.player.getDisplayName().getString());
			this.disconnect("Invalid hotbar selection (Hacking?)", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION); // Paper - kick event cause
			return;
		}
		this.player.inventory.c(packetplayinpickitem.b()); // Paper - Diff above if changed
		// Paper end
		this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-2, this.player.inventory.itemInHandIndex, this.player.inventory.getItem(this.player.inventory.itemInHandIndex)));
		this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-2, packetplayinpickitem.b(), this.player.inventory.getItem(packetplayinpickitem.b())));
		this.player.playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
	}

	@Override
	public void a(PacketPlayInItemName packetplayinitemname) {
		PlayerConnectionUtils.ensureMainThread(packetplayinitemname, this, this.player.getWorldServer());
		if (this.player.activeContainer instanceof ContainerAnvil containeranvil) {
			var s = SharedConstants.a(packetplayinitemname.b());

			if (s.length() <= 35) {
				containeranvil.a(s);
			}
		}

	}

	@Override
	public void a(PacketPlayInBeacon packetplayinbeacon) {
		PlayerConnectionUtils.ensureMainThread(packetplayinbeacon, this, this.player.getWorldServer());
		if (this.player.activeContainer instanceof ContainerBeacon) {
			((ContainerBeacon) this.player.activeContainer).c(packetplayinbeacon.b(), packetplayinbeacon.c());
		}

	}

	@Override
	public void a(PacketPlayInStruct packetplayinstruct) {
		PlayerConnectionUtils.ensureMainThread(packetplayinstruct, this, this.player.getWorldServer());
		if (this.player.isCreativeAndOp()) {
			var blockposition = packetplayinstruct.b();
			var iblockdata = this.player.world.getType(blockposition);
			var tileentity = this.player.world.getTileEntity(blockposition);

			if (tileentity instanceof TileEntityStructure tileentitystructure) {

				tileentitystructure.setUsageMode(packetplayinstruct.d());
				tileentitystructure.setStructureName(packetplayinstruct.e());
				tileentitystructure.b(packetplayinstruct.f());
				tileentitystructure.c(packetplayinstruct.g());
				tileentitystructure.b(packetplayinstruct.h());
				tileentitystructure.b(packetplayinstruct.i());
				tileentitystructure.b(packetplayinstruct.j());
				tileentitystructure.a(packetplayinstruct.k());
				tileentitystructure.d(packetplayinstruct.l());
				tileentitystructure.e(packetplayinstruct.m());
				tileentitystructure.a(packetplayinstruct.n());
				tileentitystructure.a(packetplayinstruct.o());
				if (tileentitystructure.g()) {
					var s = tileentitystructure.getStructureName();

					if (packetplayinstruct.c() == TileEntityStructure.UpdateType.SAVE_AREA) {
						if (tileentitystructure.D()) {
							this.player.a(new ChatMessage("structure_block.save_success", s), false);
						} else {
							this.player.a(new ChatMessage("structure_block.save_failure", s), false);
						}
					} else if (packetplayinstruct.c() == TileEntityStructure.UpdateType.LOAD_AREA) {
						if (!tileentitystructure.F()) {
							this.player.a(new ChatMessage("structure_block.load_not_found", s), false);
						} else if (tileentitystructure.a(this.player.getWorldServer())) {
							this.player.a(new ChatMessage("structure_block.load_success", s), false);
						} else {
							this.player.a(new ChatMessage("structure_block.load_prepare", s), false);
						}
					} else if (packetplayinstruct.c() == TileEntityStructure.UpdateType.SCAN_AREA) {
						if (tileentitystructure.C()) {
							this.player.a(new ChatMessage("structure_block.size_success", s), false);
						} else {
							this.player.a(new ChatMessage("structure_block.size_failure"), false);
						}
					}
				} else {
					this.player.a(new ChatMessage("structure_block.invalid_structure_name", packetplayinstruct.e()), false);
				}

				tileentitystructure.update();
				this.player.world.notify(blockposition, iblockdata, iblockdata, 3);
			}

		}
	}

	@Override
	public void a(PacketPlayInSetJigsaw packetplayinsetjigsaw) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsetjigsaw, this, this.player.getWorldServer());
		if (this.player.isCreativeAndOp()) {
			var blockposition = packetplayinsetjigsaw.b();
			var iblockdata = this.player.world.getType(blockposition);
			var tileentity = this.player.world.getTileEntity(blockposition);

			if (tileentity instanceof TileEntityJigsaw tileentityjigsaw) {

				tileentityjigsaw.a(packetplayinsetjigsaw.c());
				tileentityjigsaw.b(packetplayinsetjigsaw.d());
				tileentityjigsaw.c(packetplayinsetjigsaw.e());
				tileentityjigsaw.a(packetplayinsetjigsaw.f());
				tileentityjigsaw.a(packetplayinsetjigsaw.g());
				tileentityjigsaw.update();
				this.player.world.notify(blockposition, iblockdata, iblockdata, 3);
			}

		}
	}

	@Override
	public void a(PacketPlayInJigsawGenerate packetplayinjigsawgenerate) {
		PlayerConnectionUtils.ensureMainThread(packetplayinjigsawgenerate, this, this.player.getWorldServer());
		if (this.player.isCreativeAndOp()) {
			var blockposition = packetplayinjigsawgenerate.b();
			var tileentity = this.player.world.getTileEntity(blockposition);

			if (tileentity instanceof TileEntityJigsaw tileentityjigsaw) {

				tileentityjigsaw.a(this.player.getWorldServer(), packetplayinjigsawgenerate.c(), packetplayinjigsawgenerate.d());
			}

		}
	}

	@Override
	public void a(PacketPlayInTrSel packetplayintrsel) {
		PlayerConnectionUtils.ensureMainThread(packetplayintrsel, this, this.player.getWorldServer());
		var i = packetplayintrsel.b();
		var container = this.player.activeContainer;

		if (container instanceof ContainerMerchant containermerchant) {
			CraftEventFactory.callTradeSelectEvent(this.player, i, containermerchant); // CraftBukkit

			containermerchant.d(i);
			containermerchant.g(i);
		}

	}

	@Override
	public void a(PacketPlayInBEdit packetplayinbedit) {
		// PandaSpigot start - Cooldown on book edit
		if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
			this.disconnect("Book edited too quickly!");
			return;
		}
		this.lastBookTick = MinecraftServer.currentTick;
		// PandaSpigot end
		// Paper start
		var testStack = packetplayinbedit.getBook();
		if (!server.isPrimaryThread() && !testStack.isEmpty() && testStack.getTag() != null) {
			var pageList = testStack.getTag().getList("pages", 8);
			if (pageList.size() > 100) {
				PlayerConnection.LOGGER.warn(this.player.getName() + " tried to send a book with too many pages");
				var event = new net.pl3x.purpur.event.player.PlayerBookTooLargeEvent(player.getBukkitEntity(), testStack.asBukkitCopy());
				if (event.shouldKickPlayer()) // Purpur
					minecraftServer.scheduleOnMain(() -> this.disconnect("Book too large!", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION)); // Paper - kick event cause
				return;
			}
			long byteTotal = 0;
			var maxBookPageSize = com.destroystokyo.paper.PaperConfig.maxBookPageSize;
			var multiplier = Math.max(0.3D, Math.min(1D, com.destroystokyo.paper.PaperConfig.maxBookTotalSizeMultiplier));
			long byteAllowed = maxBookPageSize;
			for (var i = 0; i < pageList.size(); ++i) {
				var testString = pageList.getString(i);
				var byteLength = testString.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
				if (byteLength > 256 * 4) {
					PlayerConnection.LOGGER.warn(this.player.getName() + " tried to send a book with with a page too large!");
					var event = new net.pl3x.purpur.event.player.PlayerBookTooLargeEvent(player.getBukkitEntity(), testStack.asBukkitCopy());
					if (event.shouldKickPlayer()) // Purpur
						minecraftServer.scheduleOnMain(() -> this.disconnect("Book too large!", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION)); // Paper - kick event cause
					return;
				}
				byteTotal += byteLength;
				var length = testString.length();
				var multibytes = 0;
				if (byteLength != length) {
					for (var c : testString.toCharArray()) {
						if (c > 127) {
							multibytes++;
						}
					}
				}
				byteAllowed += (maxBookPageSize * Math.min(1, Math.max(0.1D, (double) length / 255D))) * multiplier;

				if (multibytes > 1) {
					// penalize MB
					byteAllowed -= multibytes;
				}
			}

			if (byteTotal > byteAllowed) {
				PlayerConnection.LOGGER.warn(this.player.getName() + " tried to send too large of a book. Book Size: " + byteTotal + " - Allowed:  " + byteAllowed + " - Pages: " + pageList.size());
				var event = new net.pl3x.purpur.event.player.PlayerBookTooLargeEvent(player.getBukkitEntity(), testStack.asBukkitCopy());
				if (event.shouldKickPlayer()) // Purpur
					minecraftServer.scheduleOnMain(() -> this.disconnect("Book too large!", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION)); // Paper - kick event cause
				return;
			}
		}
		// Paper end
		// CraftBukkit start
		if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
			this.disconnect("Book edited too quickly!", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION); // Paper - kick event cause
			return;
		}
		this.lastBookTick = MinecraftServer.currentTick;
		// CraftBukkit end
		var itemstack = packetplayinbedit.b();

		if (itemstack.getItem() == Items.WRITABLE_BOOK) {
			var nbttagcompound = itemstack.getTag();

			if (ItemBookAndQuill.a(nbttagcompound)) {
				List<String> list = Lists.newArrayList();
				var flag = packetplayinbedit.c();

				if (flag) {
					list.add(nbttagcompound.getString("title"));
				}

				var nbttaglist = nbttagcompound.getList("pages", 8);

				// CraftBukkit start - decompile error
				// int i;

				for (var i = 0; i < nbttaglist.size(); ++i) {
					list.add(nbttaglist.getString(i));
				}

				var i = packetplayinbedit.d();
				// CraftBukkit end
				if (PlayerInventory.d(i) || i == 40) {
					this.a(list, flag ? (list1) -> {
						this.a(list1.get(0), list1.subList(1, list1.size()), i);
					} : (list1) -> {
						this.a(list1, i);
					});
				}
			}
		}
	}

	private void a(List<String> list, int i) {
		var itemstack = this.player.inventory.getItem(i);

		if (itemstack.getItem() == Items.WRITABLE_BOOK) {
			var nbttaglist = new NBTTagList();

			var hasPerm = getPlayer().hasPermission("purpur.book.color.edit"); // Purpur - edit book
			list.stream().map(s -> s = color(s, hasPerm, false)).map(NBTTagString::a).forEach(nbttaglist::add); // Purpur - edit book
			var old = itemstack.cloneItemStack(); // CraftBukkit
			itemstack.a("pages", nbttaglist);
			this.player.inventory.setItem(i, CraftEventFactory.handleEditBookEvent(player, i, old, itemstack)); // CraftBukkit // Paper - Don't ignore result (see other callsite for handleEditBookEvent)
		}
	}

	private void a(String s, List<String> list, int i) {
		var itemstack = this.player.inventory.getItem(i);

		if (itemstack.getItem() == Items.WRITABLE_BOOK) {
			var itemstack1 = new ItemStack(Items.WRITTEN_BOOK);
			var nbttagcompound = itemstack.getTag();

			if (nbttagcompound != null) {
				itemstack1.setTag(nbttagcompound.clone());
			}

			var hasPerm = getPlayer().hasPermission("purpur.book.color.edit") || getPlayer().hasPermission("purpur.book.color.sign"); // Purpur
			itemstack1.a("author", NBTTagString.a(this.player.getDisplayName().getString()));
			itemstack1.a("title", NBTTagString.a(color(s, hasPerm))); // Purpur - sign book
			var nbttaglist = new NBTTagList();
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				var s1 = color((String) iterator.next(), hasPerm);// Purpur - sign book
				var chatcomponenttext = new ChatComponentText(s1);
				var s2 = IChatBaseComponent.ChatSerializer.a(chatcomponenttext);

				nbttaglist.add(NBTTagString.a(s2));
			}

			itemstack1.a("pages", nbttaglist);
			this.player.inventory.setItem(i, CraftEventFactory.handleEditBookEvent(player, i, itemstack, itemstack1)); // CraftBukkit
		}
	}

	// Purpur start
	private String color(String str, boolean hasPerm) {
		return color(str, hasPerm, true);
	}

	private String color(String str, boolean hasPerm, boolean parseHex) {
		return hasPerm ? org.bukkit.ChatColor.color(str, parseHex) : str;
	}
	// Purpur end

	@Override
	public void a(PacketPlayInEntityNBTQuery packetplayinentitynbtquery) {
		PlayerConnectionUtils.ensureMainThread(packetplayinentitynbtquery, this, this.player.getWorldServer());
		if (this.player.k(2)) {
			var entity = this.player.getWorldServer().getEntity(packetplayinentitynbtquery.c());

			if (entity != null) {
				var nbttagcompound = entity.save(new NBTTagCompound());

				this.player.playerConnection.sendPacket(new PacketPlayOutNBTQuery(packetplayinentitynbtquery.b(), nbttagcompound));
			}

		}
	}

	@Override
	public void a(PacketPlayInTileNBTQuery packetplayintilenbtquery) {
		PlayerConnectionUtils.ensureMainThread(packetplayintilenbtquery, this, this.player.getWorldServer());
		if (this.player.k(2)) {
			var tileentity = this.player.getWorldServer().getTileEntity(packetplayintilenbtquery.c());
			var nbttagcompound = tileentity != null ? tileentity.save(new NBTTagCompound()) : null;

			this.player.playerConnection.sendPacket(new PacketPlayOutNBTQuery(packetplayintilenbtquery.b(), nbttagcompound));
		}
	}

	private static final double HARD_CODED_BORDER = 2.9999999E7D;

	@Override
	public void a(PacketPlayInFlying packetplayinflying) {
		PlayerConnectionUtils.ensureMainThread(packetplayinflying, this, this.player.getWorldServer());

		if (b(packetplayinflying)) {
			this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_player_movement"), org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_PLAYER_MOVEMENT); // Paper - kick event cause
		} else {
			++this.grim$actions$totalFlyingPacketsSent;
			if (!(packetplayinflying instanceof PacketPlayInFlying.PacketPlayInLook)) {
				this.grim$E$noReminderTicks = 0;
			} else {
				this.grim$E$noReminderTicks++;
			}
			if (packetplayinflying.hasPos) {
				if (Math.abs(packetplayinflying.x) > HARD_CODED_BORDER
					|| Math.abs(packetplayinflying.z) > HARD_CODED_BORDER
					|| Math.abs(packetplayinflying.y) > Integer.MAX_VALUE
				) {
					kickDoNot(-1); // TODO: BAN NAXUI
					return;
				}
				if (Double.isNaN(packetplayinflying.x)
					|| Double.isNaN(packetplayinflying.y)
					|| Double.isNaN(packetplayinflying.z)
					|| Double.isInfinite(packetplayinflying.x)
					|| Double.isInfinite(packetplayinflying.y)
					|| Double.isInfinite(packetplayinflying.z)
					|| Float.isNaN(packetplayinflying.yaw)
					|| Float.isNaN(packetplayinflying.pitch)
					|| Float.isInfinite(packetplayinflying.yaw)
					|| Float.isInfinite(packetplayinflying.pitch)
				) {
					kickDoNot(-2); // TODO: BANHAMMER
					return;
				}
			}
			if (packetplayinflying.hasLook) {
				if (packetplayinflying.pitch > 90 || packetplayinflying.pitch < -90) {
					kickDoNot(404);
					return;
				}
			}
			var worldserver = this.player.getWorldServer();

			if (!this.player.viewingCredits && !this.player.isFrozen()) { // CraftBukkit
				if (this.e == 0) {
					this.syncPosition();
				}

				if (this.teleportPos != null) {
					this.allowedPlayerTicks = 20; // CraftBukkit
				} else {
					this.A = this.e;
					if (this.player.isPassenger()) {
						this.player.setLocation(this.player.locX(), this.player.locY(), this.player.locZ(), packetplayinflying.a(this.player.yaw), packetplayinflying.b(this.player.pitch));
						this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
						this.allowedPlayerTicks = 20; // CraftBukkit
					} else {
						// CraftBukkit - Make sure the move is valid but then reset it for plugins to modify
						var prevX = player.locX();
						var prevY = player.locY();
						var prevZ = player.locZ();
						var prevYaw = player.yaw;
						var prevPitch = player.pitch;
						// CraftBukkit end
						var d0 = this.player.locX();
						var d1 = this.player.locY();
						var d2 = this.player.locZ();
						var d3 = this.player.locY();
						var d4 = packetplayinflying.a(this.player.locX());
						var toX = d4; // Paper - OBFHELPER
						var d5 = packetplayinflying.b(this.player.locY());
						var toY = d5; // Paper - OBFHELPER
						var d6 = packetplayinflying.c(this.player.locZ());
						var toZ = d6; // Paper - OBFHELPER
						var f = packetplayinflying.a(this.player.yaw);
						var f1 = packetplayinflying.b(this.player.pitch);
						var d7 = d4 - this.l;
						var d8 = d5 - this.m;
						var d9 = d6 - this.n;
						var d10 = this.player.getMot().g();
						// Paper start - fix large move vectors killing the server
						var currDeltaX = toX - prevX;
						var currDeltaY = toY - prevY;
						var currDeltaZ = toZ - prevZ;
						var d11 = Math.max(d7 * d7 + d8 * d8 + d9 * d9, (currDeltaX * currDeltaX + currDeltaY * currDeltaY + currDeltaZ * currDeltaZ) - 1);
						// Paper end - fix large move vectors killing the server
						// Tuinity start - fix large move vectors killing the server
						var otherFieldX = d4 - this.o;
						var otherFieldY = d5 - this.p;
						var otherFieldZ = d6 - this.q;
						d11 = Math.max(d11, (otherFieldX * otherFieldX + otherFieldY * otherFieldY + otherFieldZ * otherFieldZ) - 1);
						// Tuinity end - fix large move vectors killing the server

						if (this.player.isSleeping()) {
							if (d11 > 1.0D) {
								this.a(this.player.locX(), this.player.locY(), this.player.locZ(), packetplayinflying.a(this.player.yaw), packetplayinflying.b(this.player.pitch));
							}

						} else {
							++this.receivedMovePackets;
							var i = this.receivedMovePackets - this.processedMovePackets;

							// CraftBukkit start - handle custom speeds and skipped ticks
							this.allowedPlayerTicks += (SystemUtils.getMonotonicMillis() / 50) - this.lastTick;
							this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
							this.lastTick = (int) (SystemUtils.getMonotonicMillis() / 50);

							if (i > Math.max(this.allowedPlayerTicks, 5)) {
								PlayerConnection.LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getDisplayName().getString(), i);
								i = 1;
							}

							if (packetplayinflying.hasLook || d11 > 0) {
								allowedPlayerTicks -= 1;
							} else {
								allowedPlayerTicks = 20;
							}
							double speed;
							if (player.abilities.isFlying) {
								speed = player.abilities.flySpeed * 20f;
							} else {
								speed = player.abilities.walkSpeed * 10f;
							}
							// Paper start - Prevent moving into unloaded chunks
							if (player.world.paperConfig.preventMovingIntoUnloadedChunks && !((WorldServer) this.player.world).areChunksLoadedForMove(this.player.getBoundingBoxAt(this.player.locX(), this.player.locY(), this.player.locZ()).expand(toX - this.player.locX(), toY - this.player.locY(), toZ - this.player.locZ()))) { // Paper - use getIfLoadedImmediately // Tuinity - improve this check
								this.internalTeleport(this.player.locX(), this.player.locY(), this.player.locZ(), this.player.yaw, this.player.pitch, Collections.emptySet());
								return;
							}
							// Paper end

							if (!this.player.H() && (!this.player.getWorldServer().getGameRules().getBoolean(GameRules.DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.isGliding())) {
								var f2 = this.player.isGliding() ? 300.0F : 100.0F;

								if (d11 - d10 > Math.max(f2, Math.pow(org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed, 2)) && !this.isExemptPlayer()) {
									// CraftBukkit end
									PlayerConnection.LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getDisplayName().getString(), d7, d8, d9);
									this.a(this.player.locX(), this.player.locY(), this.player.locZ(), this.player.yaw, this.player.pitch);
									return;
								}
							}

							var axisalignedbb = this.player.getBoundingBox(); // Tuinity - diff on change, should be old AABB

							d7 = d4 - this.o; // Tuinity - diff on change, used for checking large move vectors above
							d8 = d5 - this.p; // Tuinity - diff on change, used for checking large move vectors above
							d9 = d6 - this.q; // Tuinity - diff on change, used for checking large move vectors above
							var flag = d8 > 0.0D;

							if (this.player.isOnGround() && !packetplayinflying.b() && flag) {
								// Paper start - Add player jump event
								Player player = this.getPlayer();
								var from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
								var to = player.getLocation().clone(); // Start off the To location as the Players current location.

								// If the packet contains movement information then we update the To location with the correct XYZ.
								if (packetplayinflying.hasPos) {
									to.setX(packetplayinflying.x);
									to.setY(packetplayinflying.y);
									to.setZ(packetplayinflying.z);
								}

								// If the packet contains look information then we update the To location with the correct Yaw & Pitch.
								if (packetplayinflying.hasLook) {
									to.setYaw(packetplayinflying.yaw);
									to.setPitch(packetplayinflying.pitch);
								}

								var event = new com.destroystokyo.paper.event.player.PlayerJumpEvent(player, from, to);

								if (event.callEvent()) {
									this.player.jump();
								} else {
									from = event.getFrom();
									this.internalTeleport(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), Collections.emptySet());
									return;
								}
								// Paper end
							}

							this.player.move(EnumMoveType.PLAYER, new Vec3D(d7, d8, d9));
							var didCollide = toX != this.player.locX() || toY != this.player.locY() || toZ != this.player.locZ(); // Tuinity - needed here as the difference in Y can be reset - also note: this is only a guess at whether collisions took place, floating point errors can make this true when it shouldn't be...
							this.player.setOnGround(packetplayinflying.b()); // CraftBukkit - SPIGOT-5810, SPIGOT-5835: reset by this.player.move
							// Paper start - prevent position desync
							if (this.teleportPos != null) {
								return; // ... thanks Mojang for letting move calls teleport across dimensions.
							}
							// Paper end - prevent position desync
							var d12 = d8;

							d7 = d4 - this.player.locX();
							d8 = d5 - this.player.locY();
							if (d8 > -0.5D || d8 < 0.5D) {
								d8 = 0.0D;
							}

							d9 = d6 - this.player.locZ();
							d11 = d7 * d7 + d8 * d8 + d9 * d9;
							var flag1 = false;

							if (!this.player.H() && d11 > org.spigotmc.SpigotConfig.movedWronglyThreshold && !this.player.isSleeping() && !this.player.playerInteractManager.isCreative() && this.player.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR) { // Spigot
								flag1 = true; // Tuinity - diff on change, this should be moved wrongly
								PlayerConnection.LOGGER.warn("{} moved wrongly! ({})", this.player.getDisplayName().getString(), d11); // Purpur
							}

							this.player.setLocation(d4, d5, d6, f, f1);
							// Tuinity start - optimise out extra getCubes
							// Original for reference:
							// boolean teleportBack = flag1 && worldserver.getCubes(this.player, axisalignedbb) || (didCollide && this.a((IWorldReader) worldserver, axisalignedbb));
							var teleportBack = flag1; // violating this is always a fail
							if (!this.player.noclip && !this.player.isSleeping() && !teleportBack) {
								var newBox = this.player.getBoundingBox();
								if (didCollide || !axisalignedbb.equals(newBox)) {
									// note: only call after setLocation, or else getBoundingBox is wrong
									teleportBack = this.hasNewCollision(worldserver, this.player, axisalignedbb, newBox);
								} // else: no collision at all detected, why do we care?
							}
							if (!this.player.noclip && !this.player.isSleeping() && teleportBack) { // Tuinity end - optimise out extra getCubes
								this.a(d0, d1, d2, f, f1);
							} else {
								// CraftBukkit start - fire PlayerMoveEvent
								// Rest to old location first
								this.player.setLocation(prevX, prevY, prevZ, prevYaw, prevPitch);

								Player player = this.getPlayer();
								var from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
								var to = player.getLocation().clone(); // Start off the To location as the Players current location.

								// If the packet contains movement information then we update the To location with the correct XYZ.
								if (packetplayinflying.hasPos) {
									to.setX(packetplayinflying.x);
									to.setY(packetplayinflying.y);
									to.setZ(packetplayinflying.z);
								}

								// If the packet contains look information then we update the To location with the correct Yaw & Pitch.
								if (packetplayinflying.hasLook) {
									to.setYaw(packetplayinflying.yaw);
									to.setPitch(packetplayinflying.pitch);
								}

								// Prevent 40 event-calls for less than a single pixel of movement >.>
								var delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
								var deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

								if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isFrozen()) {
									this.lastPosX = to.getX();
									this.lastPosY = to.getY();
									this.lastPosZ = to.getZ();
									this.lastYaw = to.getYaw();
									this.lastPitch = to.getPitch();

									if (!to.getWorld().getUID().equals(from.getWorld().getUID()) || to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ() || to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch())
										this.player.resetIdleTimer(); // Purpur

									// Skip the first time we do this
									if (from.getX() != Double.MAX_VALUE) {
										var oldTo = to.clone();
										var event = new PlayerMoveEvent(player, from, to);
										this.server.getPluginManager().callEvent(event);

										// If the event is cancelled we move the player back to their old location.
										if (event.isCancelled()) {
											teleport(from);
											return;
										}

										// If a Plugin has changed the To destination then we teleport the Player
										// there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
										// We only do this if the Event was not cancelled.
										if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
											this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
											return;
										}

										// Check to see if the Players Location has some how changed during the call of the event.
										// This can happen due to a plugin teleporting the player instead of using .setTo()
										if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
											this.justTeleported = false;
											return;
										}
									}
								}
								this.player.setLocation(d4, d5, d6, f, f1); // Copied from above

								// MC-135989, SPIGOT-5564: isRiptiding
								this.B = d12 >= -0.03125D && this.player.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR && !this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly && !this.player.hasEffect(MobEffects.LEVITATION) && !this.player.isGliding() && this.a(this.player) && !this.player.isRiptiding();
								// CraftBukkit end
								this.player.getWorldServer().getChunkProvider().movePlayer(this.player);
								this.player.a(this.player.locY() - d3, packetplayinflying.b());
								// this.player.setOnGround(packetplayinflying.b()); // CraftBukkit - moved up
								if (flag) {
									this.player.fallDistance = 0.0F;
								}

								// Purpur Start
								if (this.player.isSprinting() && this.player.getWorldServer().purpurConfig.dontRunWithScissors && (this.player.getItemInHand(EnumHand.MAIN_HAND).getItem() == Items.SHEARS || this.player.getItemInHand(EnumHand.OFF_HAND).getItem() == Items.SHEARS) && (int) (Math.random() * 10) == 0) {
									this.player.damageEntity(net.minecraft.world.damagesource.DamageSource.MAGIC, (float) this.player.getWorldServer().purpurConfig.scissorsRunningDamage);
								}
								// Purpur End

								this.player.checkMovement(this.player.locX() - d0, this.player.locY() - d1, this.player.locZ() - d2);
								this.o = this.player.locX();
								this.p = this.player.locY();
								this.q = this.player.locZ();
							}
						}
					}
				}
			}
		}
	}

	// Tuinity start - optimise out extra getCubes
	private boolean hasNewCollision(WorldServer world, Entity entity, AxisAlignedBB oldBox, AxisAlignedBB newBox) {
		List<AxisAlignedBB> collisions = com.tuinity.tuinity.util.CachedLists.getTempCollisionList();
		try {
			world.getCollisions(entity, newBox, collisions, true);

			for (int i = 0, len = collisions.size(); i < len; ++i) {
				var box = collisions.get(i);
				if (!box.voxelShapeIntersect(oldBox)) {
					return true;
				}
			}

			return false;
		} finally {
			com.tuinity.tuinity.util.CachedLists.returnTempCollisionList(collisions);
		}
	}
	// Tuinity end - optimise out extra getCubes

	private boolean a(IWorldReader iworldreader, AxisAlignedBB axisalignedbb) {
		var stream = iworldreader.d(this.player, this.player.getBoundingBox().shrink(9.999999747378752E-6D), (entity) -> {
			return true;
		});
		var voxelshape = VoxelShapes.a(axisalignedbb.shrink(9.999999747378752E-6D));

		return stream.anyMatch((voxelshape1) -> {
			return !VoxelShapes.c(voxelshape1, voxelshape, OperatorBoolean.AND);
		});
	}

	public void a(double d0, double d1, double d2, float f, float f1) {
		this.a(d0, d1, d2, f, f1, Collections.emptySet());
	}

	// CraftBukkit start - Delegate to teleport(Location)
	public final void teleport(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {
		this.a(d0, d1, d2, f, f1, cause);
	} // Paper - OBFHELPER

	public void a(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {
		this.a(d0, d1, d2, f, f1, Collections.emptySet(), cause);
	}

	public void a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set) {
		this.a(d0, d1, d2, f, f1, set, PlayerTeleportEvent.TeleportCause.UNKNOWN);
	}

	public boolean a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set, PlayerTeleportEvent.TeleportCause cause) { // CraftBukkit - Return event status
		Player player = this.getPlayer();
		var from = player.getLocation();

		var x = d0;
		var y = d1;
		var z = d2;
		var yaw = f;
		var pitch = f1;

		var to = new Location(this.getPlayer().getWorld(), x, y, z, yaw, pitch);
		// SPIGOT-5171: Triggered on join
		if (from.equals(to)) {
			this.internalTeleport(d0, d1, d2, f, f1, set);
			return false; // CraftBukkit - Return event status
		}

		var event = new PlayerTeleportEvent(player, from.clone(), to.clone(), cause);
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled() || !to.equals(event.getTo())) {
			set.clear(); // Can't relative teleport
			to = event.isCancelled() ? event.getFrom() : event.getTo();
			d0 = to.getX();
			d1 = to.getY();
			d2 = to.getZ();
			f = to.getYaw();
			f1 = to.getPitch();
		}

		this.internalTeleport(d0, d1, d2, f, f1, set);
		return event.isCancelled(); // CraftBukkit - Return event status
	}

	public void teleport(Location dest) {
		internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.emptySet());
	}

	private void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set) {
		if (player.dead) {
			LOGGER.info("Attempt to teleport dead player {} restricted", player.getName());
			return;
		}
		// CraftBukkit start
		if (Float.isNaN(f)) {
			f = 0;
		}
		if (Float.isNaN(f1)) {
			f1 = 0;
		}

		this.justTeleported = true;
		// CraftBukkit end
		var d3 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X) ? this.player.locX() : 0.0D;
		var d4 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y) ? this.player.locY() : 0.0D;
		var d5 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z) ? this.player.locZ() : 0.0D;
		var f2 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT) ? this.player.yaw : 0.0F;
		var f3 = set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT) ? this.player.pitch : 0.0F;

		this.teleportPos = new Vec3D(d0, d1, d2);
		if (++this.teleportAwait == Integer.MAX_VALUE) {
			this.teleportAwait = 0;
		}

		// CraftBukkit start - update last location
		this.lastPosX = this.teleportPos.x;
		this.lastPosY = this.teleportPos.y;
		this.lastPosZ = this.teleportPos.z;
		this.lastYaw = f;
		this.lastPitch = f1;
		// CraftBukkit end

		this.A = this.e;
		this.player.setPositionRotation(d0, d1, d2, f, f1); // Paper - use proper setPositionRotation for teleportation
		this.player.forceCheckHighPriority(); // Paper
		this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0 - d3, d1 - d4, d2 - d5, f - f2, f1 - f3, set, this.teleportAwait));
	}

	@Override
	public void a(PacketPlayInBlockDig packetplayinblockdig) {
		PlayerConnectionUtils.ensureMainThread(packetplayinblockdig, this, this.player.getWorldServer());

		if (packetplayinblockdig.c == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
			var _pos = packetplayinblockdig.a;
			if (packetplayinblockdig.b != EnumDirection.DOWN
				|| _pos.getX() != 0
				|| _pos.getY() != 0
				|| _pos.getZ() != 0) {
				kickDoNot(303);
				return;
			}
		}

		if (this.player.isFrozen()) return; // CraftBukkit
		var blockposition = packetplayinblockdig.b();

		this.player.resetIdleTimer();
		var packetplayinblockdig_enumplayerdigtype = packetplayinblockdig.d();

		switch (packetplayinblockdig_enumplayerdigtype) {
			case SWAP_ITEM_WITH_OFFHAND -> {
				if (!this.player.isSpectator()) {
					var itemstack = this.player.b(EnumHand.OFF_HAND);

					// CraftBukkit start - inspiration taken from DispenserRegistry (See SpigotCraft#394)
					var mainHand = CraftItemStack.asCraftMirror(itemstack);
					var offHand = CraftItemStack.asCraftMirror(this.player.b(EnumHand.MAIN_HAND));
					var swapItemsEvent = new PlayerSwapHandItemsEvent(getPlayer(), mainHand.clone(), offHand.clone());
					this.server.getPluginManager().callEvent(swapItemsEvent);
					if (swapItemsEvent.isCancelled()) {
						return;
					}
					if (swapItemsEvent.getOffHandItem().equals(offHand)) {
						this.player.a(EnumHand.OFF_HAND, this.player.b(EnumHand.MAIN_HAND));
					} else {
						this.player.a(EnumHand.OFF_HAND, CraftItemStack.asNMSCopy(swapItemsEvent.getOffHandItem()));
					}
					if (swapItemsEvent.getMainHandItem().equals(mainHand)) {
						this.player.a(EnumHand.MAIN_HAND, itemstack);
					} else {
						this.player.a(EnumHand.MAIN_HAND, CraftItemStack.asNMSCopy(swapItemsEvent.getMainHandItem()));
					}
					// CraftBukkit end
					this.player.clearActiveItem();
				}
			}
			case DROP_ITEM -> {
				if (!this.player.isSpectator()) {
					// limit how quickly items can be dropped
					// If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
					if (this.lastDropTick != MinecraftServer.currentTick) {
						this.dropCount = 0;
						this.lastDropTick = MinecraftServer.currentTick;
					} else {
						// Else we increment the drop count and check the amount.
						this.dropCount++;
						if (this.dropCount >= 20) {
							LOGGER.warn(this.player.getName() + " dropped their items too quickly!");
							this.disconnect("You dropped your items too quickly (Hacking?)", PlayerKickEvent.Cause.ILLEGAL_ACTION); // Paper - kick event cause
							return;
						}
					}
					// CraftBukkit end
					this.player.dropItem(false);
				}
			}
			case DROP_ALL_ITEMS -> {
				if (!this.player.isSpectator()) {
					this.player.dropItem(true);
				}
			}
			case RELEASE_USE_ITEM -> {
				this.player.releaseActiveItem();
			}
			case START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK -> {
				// Paper start - Don't allow digging in unloaded chunks
				if (this.player.world.getChunkIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4) == null) {
					return;
				}
				// Paper end - Don't allow digging in unloaded chunks
				this.player.playerInteractManager.a(blockposition, packetplayinblockdig_enumplayerdigtype, packetplayinblockdig.c(), this.minecraftServer.getMaxBuildHeight());
			}
			default -> throw new IllegalArgumentException("Invalid player action");
		}
	}

	private static boolean a(EntityPlayer entityplayer, ItemStack itemstack) {
		if (itemstack.isEmpty()) {
			return false;
		} else {
			var item = itemstack.getItem();

			return (item instanceof ItemBlock || item instanceof ItemBucket) && !entityplayer.getCooldownTracker().hasCooldown(item);
		}
	}

	// Spigot start - limit place/interactions
	private int limitedPackets;
	private long lastLimitedPacket = -1;
	private static final int THRESHOLD = com.destroystokyo.paper.PaperConfig.packetInSpamThreshold; // Paper - Configurable threshold

	private boolean checkLimit(long timestamp) {
		if (lastLimitedPacket != -1 && timestamp - lastLimitedPacket < THRESHOLD && limitedPackets++ >= 8) { // Paper - Use threshold, raise packet limit to 8
			return false;
		}

		if (lastLimitedPacket == -1 || timestamp - lastLimitedPacket >= THRESHOLD) { // Paper
			lastLimitedPacket = timestamp;
			limitedPackets = 0;
			return true;
		}

		return true;
	}
	// Spigot end

	@Override
	public void a(PacketPlayInUseItem packetplayinuseitem) {
		PlayerConnectionUtils.ensureMainThread(packetplayinuseitem, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		if (!checkLimit(packetplayinuseitem.timestamp)) return; // Spigot - check limit
		var worldserver = this.player.getWorldServer();
		var enumhand = packetplayinuseitem.b();
		var itemstack = this.player.b(enumhand);
		var movingobjectpositionblock = packetplayinuseitem.c();
		var blockposition = movingobjectpositionblock.getBlockPosition();
		var enumdirection = movingobjectpositionblock.getDirection();

		// Paper start - move check up
		var eyeLoc = this.getPlayer().getEyeLocation();
		var reachDistance = NumberConversions.square(eyeLoc.getX() - blockposition.getX()) + NumberConversions.square(eyeLoc.getY() - blockposition.getY()) + NumberConversions.square(eyeLoc.getZ() - blockposition.getZ());
		if (reachDistance > (this.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE ? CREATIVE_PLACE_DISTANCE_SQUARED : SURVIVAL_PLACE_DISTANCE_SQUARED)) {
			return;
		}
		// Paper end - move check up

		this.player.resetIdleTimer();
		if (blockposition.getY() < this.minecraftServer.getMaxBuildHeight()) {
			if (this.teleportPos == null && this.player.h((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D) < 64.0D && (worldserver.a(this.player, blockposition) || (worldserver.paperConfig.allowUsingSignsInsideSpawnProtection && worldserver.getType(blockposition).getBlock() instanceof BlockSign))) { // Paper
				// CraftBukkit start - Check if we can actually do something over this large a distance
				// Paper - move check up
				this.player.clearActiveItem(); // SPIGOT-4706
				// CraftBukkit end
				var enuminteractionresult = this.player.playerInteractManager.a(this.player, worldserver, itemstack, enumhand, movingobjectpositionblock);

				if (enumdirection == EnumDirection.UP && !enuminteractionresult.a() && blockposition.getY() >= this.minecraftServer.getMaxBuildHeight() - 1 && a(this.player, itemstack)) {
					var ichatmutablecomponent = (new ChatMessage("build.tooHigh", new Object[]{this.minecraftServer.getMaxBuildHeight()})).a(EnumChatFormat.RED);

					this.player.playerConnection.sendPacket(new PacketPlayOutChat(ichatmutablecomponent, ChatMessageType.GAME_INFO, SystemUtils.b));
				} else if (enuminteractionresult.b() && !this.player.playerInteractManager.interactResult) {
					this.player.swingHand(enumhand, true);
				}
			}
		} else {
			var ichatmutablecomponent1 = (new ChatMessage("build.tooHigh", new Object[]{this.minecraftServer.getMaxBuildHeight()})).a(EnumChatFormat.RED);

			this.player.playerConnection.sendPacket(new PacketPlayOutChat(ichatmutablecomponent1, ChatMessageType.GAME_INFO, SystemUtils.b));
		}

		this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
		this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection)));
	}

	@Override
	public void a(PacketPlayInBlockPlace packetplayinblockplace) {
		PlayerConnectionUtils.ensureMainThread(packetplayinblockplace, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		if (!checkLimit(packetplayinblockplace.timestamp)) return; // Spigot - check limit
		var worldserver = this.player.getWorldServer();
		var enumhand = packetplayinblockplace.b();
		var itemstack = this.player.b(enumhand);

		this.player.resetIdleTimer();
		if (!itemstack.isEmpty()) {
			// CraftBukkit start
			// Raytrace to look for 'rogue armswings'
			var f1 = this.player.pitch;
			var f2 = this.player.yaw;
			var d0 = this.player.locX();
			var d1 = this.player.locY() + (double) this.player.getHeadHeight();
			var d2 = this.player.locZ();
			var vec3d = new Vec3D(d0, d1, d2);

			var f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
			var f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
			var f5 = -MathHelper.cos(-f1 * 0.017453292F);
			var f6 = MathHelper.sin(-f1 * 0.017453292F);
			var f7 = f4 * f5;
			var f8 = f3 * f5;
			var d3 = player.playerInteractManager.getGameMode() == EnumGamemode.CREATIVE ? 5.0D : 4.5D;
			var vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
			MovingObjectPosition movingobjectposition = this.player.world.rayTrace(new RayTrace(vec3d, vec3d1, RayTrace.BlockCollisionOption.OUTLINE, RayTrace.FluidCollisionOption.NONE, player));

			boolean cancelled;
			if (movingobjectposition == null || movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.BLOCK) {
				var event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack, enumhand);
				cancelled = event.useItemInHand() == Event.Result.DENY;
			} else {
				var movingobjectpositionblock = (MovingObjectPositionBlock) movingobjectposition;
				if (player.playerInteractManager.firedInteract && player.playerInteractManager.interactPosition.equals(movingobjectpositionblock.getBlockPosition()) && player.playerInteractManager.interactHand == enumhand && ItemStack.equals(player.playerInteractManager.interactItemStack, itemstack)) {
					cancelled = player.playerInteractManager.interactResult;
				} else {
					var event = CraftEventFactory.callPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, movingobjectpositionblock.getBlockPosition(), movingobjectpositionblock.getDirection(), itemstack, true, enumhand);
					cancelled = event.useItemInHand() == Event.Result.DENY;
				}
				player.playerInteractManager.firedInteract = false;
			}

			if (cancelled) {
				this.player.getBukkitEntity().updateInventory(); // SPIGOT-2524
				return;
			}
			// Paper start
			itemstack = this.player.getItemInHand(enumhand);
			if (itemstack.isEmpty()) return;
			// Paper end
			var enuminteractionresult = this.player.playerInteractManager.a(this.player, worldserver, itemstack, enumhand);

			if (enuminteractionresult.b()) {
				this.player.swingHand(enumhand, true);
			}

		}
	}

	@Override
	public void a(PacketPlayInSpectate packetplayinspectate) {
		PlayerConnectionUtils.ensureMainThread(packetplayinspectate, this, this.player.getWorldServer());
		if (this.player.isSpectator()) {
			for (var worldserver : this.minecraftServer.getWorlds()) {
				var entity = packetplayinspectate.a(worldserver);

				if (entity != null) {
					this.player.a(
						worldserver,
						entity.locX(),
						entity.locY(),
						entity.locZ(),
						entity.yaw,
						entity.pitch,
						PlayerTeleportEvent.TeleportCause.SPECTATE
					); // CraftBukkit
					return;
				}
			}
		} else {
			kickDoNot(304);
		}
	}

	@Override
	// CraftBukkit start
	public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {
		PlayerConnectionUtils.ensureMainThread(packetplayinresourcepackstatus, this, this.player.getWorldServer());
		// Paper start
		var packStatus = PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.status.ordinal()];
		if (player.world.purpurConfig.playerInvulnerableWhileAcceptingResourcePack)
			player.setFrozen(packStatus == PlayerResourcePackStatusEvent.Status.ACCEPTED); // Purpur
		player.getBukkitEntity().setResourcePackStatus(packStatus);
		this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), packStatus));
		// Paper end
	}
	// CraftBukkit end

	@Override
	public void a(PacketPlayInBoatMove packetplayinboatmove) {
		PlayerConnectionUtils.ensureMainThread(packetplayinboatmove, this, this.player.getWorldServer());
		var entity = this.player.getVehicle();

		if (entity instanceof EntityBoat) {
			((EntityBoat) entity).a(packetplayinboatmove.b(), packetplayinboatmove.c());
		}

	}

	@Override
	public void a(IChatBaseComponent ichatbasecomponent) {
		// CraftBukkit start - Rarely it would send a disconnect line twice
		if (this.processedDisconnect) {
			return;
		} else {
			this.processedDisconnect = true;
		}
		// CraftBukkit end
		PlayerConnection.LOGGER.info("{} lost connection: {}", this.player.getDisplayName().getString(), ichatbasecomponent.getString());

		this.player.p();
		// Paper start - Adventure
		var quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
		if ((quitMessage != null) && !quitMessage.equals(net.kyori.adventure.text.Component.empty())) {
			this.minecraftServer.getPlayerList().sendMessage(PaperAdventure.asVanilla(quitMessage));
			// Paper end
		}
		// CraftBukkit end
		var itextfilter = this.player.Q();

		if (itextfilter != null) {
			itextfilter.b();
		}

		if (this.isExemptPlayer()) {
			PlayerConnection.LOGGER.info("Stopping singleplayer server as player logged out");
			this.minecraftServer.safeShutdown(false);
		}

	}

	public void sendRawPacket(ByteBuf buf) {
		networkManager.sendRawPacket(buf);
	}

	public void sendPacket(Packet<?> packet) {
		this.a(packet, null);
	}

	public void a(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {
		if (packet instanceof PacketPlayOutChat packetplayoutchat) {
			var enumchatvisibility = this.player.getChatFlags();

			if (enumchatvisibility == EnumChatVisibility.HIDDEN && packetplayoutchat.d() != ChatMessageType.GAME_INFO) {
				return;
			}

			if (enumchatvisibility == EnumChatVisibility.SYSTEM && !packetplayoutchat.c()) {
				return;
			}
		}

		// CraftBukkit start
		if (packet == null || this.processedDisconnect) { // Spigot
			return;
		} else if (packet instanceof PacketPlayOutSpawnPosition packet6) {
			this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.position.getX(), packet6.position.getY(), packet6.position.getZ());
		}
		// CraftBukkit end

		try {
			this.networkManager.sendPacket(packet, genericfuturelistener);
		} catch (Throwable throwable) {
			var crashreport = CrashReport.a(throwable, "Sending packet");
			var crashreportsystemdetails = crashreport.a("Packet being sent");

			crashreportsystemdetails.a("Packet class", () -> {
				return packet.getClass().getCanonicalName();
			});
			throw new ReportedException(crashreport);
		}
	}

	@Override
	public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
		PlayerConnectionUtils.ensureMainThread(packetplayinhelditemslot, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		if (packetplayinhelditemslot.b() >= 0 && packetplayinhelditemslot.b() < PlayerInventory.getHotbarSize()) {
			if (packetplayinhelditemslot.getItemInHandIndex() == this.player.inventory.itemInHandIndex) {
				return;
			} // Paper - don't fire itemheldevent when there wasn't a slot change
			var event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.itemInHandIndex, packetplayinhelditemslot.b());
			this.server.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				this.sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
				this.player.resetIdleTimer();
				return;
			}
			// CraftBukkit end
			if (this.player.inventory.itemInHandIndex != packetplayinhelditemslot.b() && this.player.getRaisedHand() == EnumHand.MAIN_HAND) {
				this.player.clearActiveItem();
			}

			this.player.inventory.itemInHandIndex = packetplayinhelditemslot.b();
			this.player.resetIdleTimer();
		} else {
			PlayerConnection.LOGGER.warn("{} tried to set an invalid carried item", this.player.getDisplayName().getString());
			this.disconnect("Invalid hotbar selection (Hacking?)", org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_ACTION); // CraftBukkit // Paper - kick event cause
		}
	}

	@Override
	public void a(PacketPlayInChat packetplayinchat) {
		// CraftBukkit start - async chat
		// SPIGOT-3638
		if (this.minecraftServer.isStopped()) {
			return;
		}
		// CraftBukkit end
		var s = StringUtils.normalizeSpace(packetplayinchat.b());

		if (s.startsWith("/")) {
			PlayerConnectionUtils.ensureMainThread(packetplayinchat, this, this.player.getWorldServer());
			this.c(s);
		} else {
			this.c(s); // CraftBukkit - filter NYI
		}

	}

	private void c(String s) {
		if (this.player.dead || this.player.getChatFlags() == EnumChatVisibility.HIDDEN) { // CraftBukkit - dead men tell no tales
			this.sendPacket(new PacketPlayOutChat((new ChatMessage("chat.cannotSend")).a(EnumChatFormat.RED), ChatMessageType.SYSTEM, SystemUtils.b));
		} else {
			this.player.resetIdleTimer();

			var isSync = s.startsWith("/"); // CraftBukkit
			for (var i = 0; i < s.length(); ++i) {
				if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
					// CraftBukkit start - threadsafety
					if (!s.startsWith("/")) {
						var waitable = new Waitable() {
							@Override
							protected Object evaluate() {
								PlayerConnection.this.disconnect(new ChatMessage("multiplayer.disconnect.illegal_characters"), org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_CHARACTERS); // Paper - kick event cause
								return null;
							}
						};

						this.minecraftServer.processQueue.add(waitable);

						try {
							waitable.get();
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {
							throw new RuntimeException(e);
						}
					} else {
						this.disconnect(new ChatMessage("multiplayer.disconnect.illegal_characters"), org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_CHARACTERS); // Paper - kick event cause
					}
					// CraftBukkit end
					return;
				}
			}

			// CraftBukkit start
			if (isSync) {
				try {
					this.minecraftServer.server.playerCommandState = true;
					this.handleCommand(s);
				} finally {
					this.minecraftServer.server.playerCommandState = false;
				}
			} else if (s.isEmpty()) {
				LOGGER.warn(this.player.getName() + " tried to send an empty message");
			} else if (getPlayer().isConversing()) {
				var conversationInput = s;
				this.minecraftServer.processQueue.add(() -> getPlayer().acceptConversationInput(conversationInput));
			} else if (this.player.getChatFlags() == EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
				this.sendPacket(new PacketPlayOutChat((new ChatMessage("chat.cannotSend")).a(EnumChatFormat.RED), ChatMessageType.SYSTEM, SystemUtils.b));
			} else {
				this.chat(s, true);
				// CraftBukkit end - the below is for reference. :)
			}

			// Spigot start - spam exclusions
			var counted = true;
			for (var exclude : org.spigotmc.SpigotConfig.spamExclusions) {
				if (exclude != null && s.startsWith(exclude)) {
					counted = false;
					break;
				}
			}
			// Spigot end
			// CraftBukkit start - replaced with thread safe throttle
			// this.chatThrottle += 20;
			if (counted && ((int) VH__CHAT_THROTTLE.getAndAdd(this, 20)) + 20 > 200 && !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) { // Spigot
				if (!isSync) {
					var waitable = new Waitable<>() {
						@Override
						protected Object evaluate() {
							PlayerConnection.this.disconnect(new ChatMessage("disconnect.spam"), org.bukkit.event.player.PlayerKickEvent.Cause.SPAM); // Paper - kick event cause
							return null;
						}
					};

					this.minecraftServer.processQueue.add(waitable);

					try {
						waitable.get();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (ExecutionException e) {
						throw new RuntimeException(e);
					}
				} else {
					this.disconnect(new ChatMessage("disconnect.spam"), org.bukkit.event.player.PlayerKickEvent.Cause.SPAM); // Paper - kick event cause
				}
				// CraftBukkit end
			}

		}
	}

	// CraftBukkit start - add method
	public void chat(String s, boolean async) {
		if (s.isEmpty() || this.player.getChatFlags() == EnumChatVisibility.HIDDEN) {
			return;
		}

		if (!async && s.startsWith("/")) {
			this.handleCommand(s);
		} else if (this.player.getChatFlags() == EnumChatVisibility.SYSTEM) {
			// Do nothing, this is coming from a plugin
			// Paper start
		} else {
			var cp = new ChatProcessor(this.minecraftServer, this.player, s, async);
			cp.process();
			// Paper end
		}
	}
	// CraftBukkit end

	private void handleCommand(String s) {
		// CraftBukkit start - whole method
		if (org.spigotmc.SpigotConfig.logCommands) // Spigot
			LOGGER.info(this.player.getName() + " issued server command: " + s);

		var player = this.getPlayer();

		var event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet(minecraftServer));
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		try {
			if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
			}
		} catch (org.bukkit.command.CommandException ex) {
			player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
			java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// this.minecraftServer.getCommandDispatcher().a(this.player.getCommandListener(), s);
		// CraftBukkit end
	}

	@Override
	public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
		PlayerConnectionUtils.ensureMainThread(packetplayinarmanimation, this, this.player.getWorldServer());
		this.grim$H$lastSent = this.grim$actions$totalFlyingPacketsSent;
		if (this.player.isFrozen()) return; // CraftBukkit
		this.player.resetIdleTimer();
		// CraftBukkit start - Raytrace to look for 'rogue armswings'
		var f1 = this.player.pitch;
		var f2 = this.player.yaw;
		var d0 = this.player.locX();
		var d1 = this.player.locY() + (double) this.player.getHeadHeight();
		var d2 = this.player.locZ();
		var vec3d = new Vec3D(d0, d1, d2);

		var f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
		var f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
		var f5 = -MathHelper.cos(-f1 * 0.017453292F);
		var f6 = MathHelper.sin(-f1 * 0.017453292F);
		var f7 = f4 * f5;
		var f8 = f3 * f5;
		var d3 = player.playerInteractManager.getGameMode() == EnumGamemode.CREATIVE ? 5.0D : 4.5D;
		var vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
		MovingObjectPosition movingobjectposition = this.player.world.rayTrace(new RayTrace(vec3d, vec3d1, RayTrace.BlockCollisionOption.OUTLINE, RayTrace.FluidCollisionOption.NONE, player));

		if (movingobjectposition == null || movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.BLOCK || this.player.playerInteractManager.getGameMode() == EnumGamemode.ADVENTURE) { // Paper - call PlayerInteractEvent when left-clicking on a block in adventure mode
			CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getItemInHand(), EnumHand.MAIN_HAND);
		}

		// Arm swing animation
		var event = new io.papermc.paper.event.player.PlayerArmSwingEvent(this.getPlayer(), packetplayinarmanimation.getHand() == EnumHand.MAIN_HAND ? org.bukkit.inventory.EquipmentSlot.HAND : org.bukkit.inventory.EquipmentSlot.OFF_HAND); // Paper
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled()) return;
		// CraftBukkit end
		this.player.swingHand(packetplayinarmanimation.b());
	}

	@Override
	public void a(PacketPlayInEntityAction packetplayinentityaction) {
		PlayerConnectionUtils.ensureMainThread(packetplayinentityaction, this, this.player.getWorldServer());

		if (packetplayinentityaction.animation == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) {
			if (this.grim$F$lastSprinting) {
				if (!this.grim$F$thanksMojang) {
					this.grim$F$thanksMojang = true;
				} else {
					kickDoNot(999);
				}
				this.grim$F$lastSprinting = true;
			}
		} else if (packetplayinentityaction.animation == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
			if (!this.grim$F$lastSprinting) {
				if (!this.grim$F$thanksMojang) {
					this.grim$F$thanksMojang = true;
				} else {
					kickDoNot(998);
				}
				this.grim$F$lastSprinting = false;
			}
		}

		// CraftBukkit start
		if (this.player.dead) return;
		switch (packetplayinentityaction.c()) {
			case PRESS_SHIFT_KEY, RELEASE_SHIFT_KEY -> {
				var event = new PlayerToggleSneakEvent(this.getPlayer(), packetplayinentityaction.c() == PacketPlayInEntityAction.EnumPlayerAction.PRESS_SHIFT_KEY);
				this.server.getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					return;
				}
			}
			case START_SPRINTING, STOP_SPRINTING -> {
				var e2 = new PlayerToggleSprintEvent(this.getPlayer(), packetplayinentityaction.c() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING);
				this.server.getPluginManager().callEvent(e2);
				if (e2.isCancelled()) {
					return;
				}
			}
		}
		// CraftBukkit end
		this.player.resetIdleTimer();
		IJumpable ijumpable;

		switch (packetplayinentityaction.c()) {
			case PRESS_SHIFT_KEY -> {
				this.player.setSneaking(true);

				// Paper start - Hang on!
				if (this.player.world.paperConfig.parrotsHangOnBetter) {
					this.player.releaseShoulderEntities();
				}
			}
			// Paper end

			case RELEASE_SHIFT_KEY -> this.player.setSneaking(false);
			case START_SPRINTING -> this.player.setSprinting(true);
			case STOP_SPRINTING -> this.player.setSprinting(false);
			case STOP_SLEEPING -> {
				if (this.player.isSleeping()) {
					this.player.wakeup(false, true);
					this.teleportPos = this.player.getPositionVector();
				}
			}
			case START_RIDING_JUMP -> {
				if (this.player.getVehicle() instanceof IJumpable) {
					ijumpable = (IJumpable) this.player.getVehicle();
					var i = packetplayinentityaction.d();

					if (ijumpable.P_()) {
						if (i > 0 && i <= 100) {
							ijumpable.b(i);
						} else {
							kickDoNot(201);
						}
					}
				}
			}
			case STOP_RIDING_JUMP -> {
				if (this.player.getVehicle() instanceof IJumpable) {
					ijumpable = (IJumpable) this.player.getVehicle();
					ijumpable.c();
				}
			}
			case OPEN_INVENTORY -> {
				if (this.player.getVehicle() instanceof EntityHorseAbstract) {
					((EntityHorseAbstract) this.player.getVehicle()).f(this.player);
				}
			}
			case START_FALL_FLYING -> {
				if (!this.player.eD()) {
					this.player.stopGliding();
				}
			}
			default -> {
				kickDoNot(666);
			}
		}
	}

	@Override
	public void a(PacketPlayInUseEntity packetplayinuseentity) {
		PlayerConnectionUtils.ensureMainThread(packetplayinuseentity, this, this.player.getWorldServer());
		if (packetplayinuseentity.action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
			if (this.grim$actions$totalFlyingPacketsSent - this.grim$H$lastSent > 1) {
				kickDoNot(667);
				return;
			}
		}
		if (this.player.isFrozen()) return; // CraftBukkit
		var worldserver = this.player.getWorldServer();
		// Spigot Start
		if (packetplayinuseentity.getEntityId() == player.getId() && !player.isSpectator()) {
			disconnect("Ёбу дал", org.bukkit.event.player.PlayerKickEvent.Cause.SELF_INTERACTION); // Paper - kick event cause
			return;
		}
		// Spigot End
		var entity = packetplayinuseentity.a(worldserver);

		if (entity != null) {
			this.player.resetIdleTimer();
			this.player.setSneaking(packetplayinuseentity.e());

			if (this.player.h(entity) < 36.0D) {
				var enumhand = packetplayinuseentity.c();
				var itemstack = enumhand != null ? this.player.b(enumhand).cloneItemStack() : ItemStack.b;
				Optional<EnumInteractionResult> optional = Optional.empty();

				var itemInHand = this.player.b(packetplayinuseentity.c() == null ? EnumHand.MAIN_HAND : packetplayinuseentity.c()); // CraftBukkit

				if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT
					|| packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
					// CraftBukkit start
					var triggerLeashUpdate = itemInHand != null && itemInHand.getItem() == Items.LEAD && entity instanceof EntityInsentient;
					var origItem = this.player.inventory.getItemInHand() == null ? null : this.player.inventory.getItemInHand().getItem();
					PlayerInteractEntityEvent event;
					if (entity instanceof EntityInsentient)
						((EntityInsentient) entity).ticksSinceLastInteraction = 0; // Purpur
					if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
						event = new PlayerInteractEntityEvent(this.getPlayer(), entity.getBukkitEntity(), (packetplayinuseentity.c() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
					} else {
						var target = packetplayinuseentity.d();
						event = new PlayerInteractAtEntityEvent(this.getPlayer(), entity.getBukkitEntity(), new org.bukkit.util.Vector(target.x, target.y, target.z), (packetplayinuseentity.c() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
					}
					this.server.getPluginManager().callEvent(event);

					player.processClick(enumhand); // Purpur

					// Fish bucket - SPIGOT-4048
					if ((entity instanceof EntityFish && origItem != null && origItem.getItem() == Items.WATER_BUCKET) && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != origItem)) {
						this.sendPacket(new PacketPlayOutSpawnEntityLiving((EntityFish) entity));
						this.player.updateInventory(this.player.activeContainer);
					}

					if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != origItem)) {
						// Refresh the current leash state
						this.sendPacket(new PacketPlayOutAttachEntity(entity, ((EntityInsentient) entity).getLeashHolder()));
					}

					if (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != origItem) {
						// Refresh the current entity metadata
						// Paper start - update entity for all players
						var packet = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true);
						if (entity.tracker != null) {
							entity.tracker.broadcast(packet);
						} else {
							this.sendPacket(packet);
						}
						// Paper end
					}

					if (event.isCancelled()) {
						this.player.updateInventory(this.player.activeContainer); // Paper - Refresh player inventory
						return;
					}
					// CraftBukkit end
				}

				if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
					optional = Optional.of(this.player.a(entity, enumhand));
					// CraftBukkit start
					if (itemInHand != null && !itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					// CraftBukkit end
				} else if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
					optional = Optional.of(entity.a(this.player, packetplayinuseentity.d(), enumhand));
					// CraftBukkit start
					if (itemInHand != null && !itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					// CraftBukkit end
				} else if (packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
					if (entity instanceof EntityItem || entity instanceof EntityExperienceOrb || entity instanceof EntityArrow || (entity == this.player && !player.isSpectator())) { // CraftBukkit
						this.disconnect(new ChatMessage("multiplayer.disconnect.invalid_entity_attacked"), org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_ENTITY_ATTACKED); // Paper - kick event cause
						PlayerConnection.LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getDisplayName().getString());
						return;
					}

					this.player.attack(entity);

					// CraftBukkit start
					if (itemInHand != null && !itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					// CraftBukkit end
				}

				if (optional.isPresent() && optional.get().a()) {
					CriterionTriggers.P.a(this.player, itemstack, entity);
					if (optional.get().b()) {
						this.player.swingHand(enumhand, true);
					}
				}
			}
		} else {
			this.server.getPluginManager().callEvent(new com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent(
				this.getPlayer(),
				packetplayinuseentity.getEntityId(),
				packetplayinuseentity.b() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK,
				packetplayinuseentity.c() == EnumHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND
			));
		}
	}

	@Override
	public void a(PacketPlayInClientCommand packetplayinclientcommand) {
		PlayerConnectionUtils.ensureMainThread(packetplayinclientcommand, this, this.player.getWorldServer());
		this.player.resetIdleTimer();
		var packetplayinclientcommand_enumclientcommand = packetplayinclientcommand.b();

		if (packetplayinclientcommand_enumclientcommand == PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN) {
			if (this.player.viewingCredits) {
				this.player.viewingCredits = false;
				this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, this.minecraftServer.getWorldServer(this.player.getSpawnDimension()), true, null, true, PlayerRespawnEvent.RespawnFlag.END_PORTAL); // Paper - add isEndCreditsRespawn argument
				CriterionTriggers.v.a(this.player, World.THE_END, World.OVERWORLD);
			} else {
				if (this.player.getHealth() > 0.0F) {
					return;
				}

				this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, false);
				if (this.minecraftServer.isHardcore()) {
					this.player.setGamemode(EnumGamemode.SPECTATOR, PlayerGameModeChangeEvent.Cause.HARDCORE_DEATH, null); // Paper
					this.player.getWorldServer().getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS).a(false, this.minecraftServer);
				}
			}
		} else if (packetplayinclientcommand_enumclientcommand == PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS) {
			this.player.getStatisticManager().a(this.player);
		}
	}

	@Override
	public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
		// Paper start
		handleContainerClose(packetplayinclosewindow, InventoryCloseEvent.Reason.PLAYER);
	}

	public void handleContainerClose(PacketPlayInCloseWindow packetplayinclosewindow, InventoryCloseEvent.Reason reason) {
		// Paper end
		PlayerConnectionUtils.ensureMainThread(packetplayinclosewindow, this, this.player.getWorldServer());

		if (this.player.isFrozen()) return; // CraftBukkit
		CraftEventFactory.handleInventoryCloseEvent(this.player, reason); // CraftBukkit // Paper

		this.player.o();
	}

	private void kickDoNot(int n) {
		this.disconnect(
			new ChatMessage("do not " + n),
			PlayerKickEvent.Cause.ILLEGAL_ACTION
		);
	}

	@Override
	public void a(PacketPlayInWindowClick packetplayinwindowclick) {
		PlayerConnectionUtils.ensureMainThread(packetplayinwindowclick, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		this.player.resetIdleTimer();
		if (this.player.activeContainer instanceof ContainerLectern) {
			kickDoNot(-1337); // TODO NAXER BANIM LECTERNCRASH
			return;
		}
		if (this.player.activeContainer.windowId == packetplayinwindowclick.b() && this.player.activeContainer.c(this.player) && this.player.activeContainer.canUse(this.player)) { // CraftBukkit
			var _slot = packetplayinwindowclick.c();
			if ((_slot < -1 && _slot != -999) || _slot >= this.player.activeContainer.getBukkitView().countSlots()) {
				kickDoNot(0);
				return;
			}

			if (_slot == -1) return; // ig this is correct

			var _button = packetplayinwindowclick.d();
			if ((_button < 0 || _button > 10) && _button != 40) {
				kickDoNot(1);
				return;
			} else {
				var mode = packetplayinwindowclick.g();
				switch (mode) {
					case PICKUP, QUICK_MOVE, THROW -> {
						if (_button != 0 && _button != 1) {
							kickDoNot(2);
							return;
						}
					}
					case SWAP -> {
						if (_button > 8 && _button != 40) {
							kickDoNot(3);
							return;
						}
					}
					case CLONE -> {
						if (_button != 2) {
							kickDoNot(4);
							return;
						}
					}
					case QUICK_CRAFT -> {
						if (_slot == -999 && (
							_button != 0 && _button != 4 && _button != 8 && _button != 2 && _button != 6 && _button != 10
						)) {
							kickDoNot(5);
							return;
						} else if (_button != 0
							&& _button != 1
							&& _button != 2
							&& _button != 4
							&& _button != 5
							&& _button != 6
							&& _button != 8
							&& _button != 9
							&& _button != 10 // TODO: Rewrite
						) {
							kickDoNot(6);
							return;
						}
					}
					case PICKUP_ALL -> {
						if (_button != 0) {
							kickDoNot(7);
							return;
						}
					}
				}
			}

			var cancelled = this.player.isSpectator(); // CraftBukkit - see below if

			var inventory = this.player.activeContainer.getBukkitView();
			var type = inventory.getSlotType(packetplayinwindowclick.c());

			InventoryClickEvent event;
			var click = ClickType.UNKNOWN;
			var action = InventoryAction.UNKNOWN;

			var itemstack = ItemStack.b;

			switch (packetplayinwindowclick.g()) {
				case PICKUP -> {
					if (packetplayinwindowclick.d() == 0) {
						click = ClickType.LEFT;
					} else if (packetplayinwindowclick.d() == 1) {
						click = ClickType.RIGHT;
					}
					if (packetplayinwindowclick.d() == 0 || packetplayinwindowclick.d() == 1) {
						action = InventoryAction.NOTHING; // Don't want to repeat ourselves
						if (packetplayinwindowclick.c() == -999) {
							if (!player.inventory.getCarried().isEmpty()) {
								action = packetplayinwindowclick.d() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
							}
						} else if (packetplayinwindowclick.c() < 0) {
						} else {
							var slot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
							if (slot != null) {
								var clickedItem = slot.getItem();
								var cursor = player.inventory.getCarried();
								if (clickedItem.isEmpty()) {
									if (!cursor.isEmpty()) {
										action = packetplayinwindowclick.d() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
									}
								} else if (slot.isAllowed(player)) {
									if (cursor.isEmpty()) {
										action = packetplayinwindowclick.d() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
									} else if (slot.isAllowed(cursor)) {
										if (clickedItem.doMaterialsMatch(cursor) && ItemStack.equals(clickedItem, cursor)) {
											var toPlace = packetplayinwindowclick.d() == 0 ? cursor.getCount() : 1;
											toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.getCount());
											toPlace = Math.min(toPlace, slot.inventory.getMaxStackSize() - clickedItem.getCount());
											if (toPlace == 1) {
												action = InventoryAction.PLACE_ONE;
											} else if (toPlace == cursor.getCount()) {
												action = InventoryAction.PLACE_ALL;
											} else if (toPlace < 0) {
												action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE; // this happens with oversized stacks
											} else if (toPlace != 0) {
												action = InventoryAction.PLACE_SOME;
											}
										} else if (cursor.getCount() <= slot.getMaxStackSize()) {
											action = InventoryAction.SWAP_WITH_CURSOR;
										}
									} else if (cursor.getItem() == clickedItem.getItem() && ItemStack.equals(cursor, clickedItem)) {
										if (clickedItem.getCount() >= 0) {
											if (clickedItem.getCount() + cursor.getCount() <= cursor.getMaxStackSize()) {
												// As of 1.5, this is result slots only
												action = InventoryAction.PICKUP_ALL;
											}
										}
									}
								}
							}
						}
					}
				}
				case QUICK_MOVE -> {
					if (packetplayinwindowclick.d() == 0) {
						click = ClickType.SHIFT_LEFT;
					} else if (packetplayinwindowclick.d() == 1) {
						click = ClickType.SHIFT_RIGHT;
					}
					if (packetplayinwindowclick.d() == 0 || packetplayinwindowclick.d() == 1) {
						if (packetplayinwindowclick.c() < 0) {
							action = InventoryAction.NOTHING;
						} else {
							var slot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
							if (slot != null && slot.isAllowed(this.player) && slot.hasItem()) {
								action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					}
				}
				case SWAP -> {
					if ((packetplayinwindowclick.d() >= 0 && packetplayinwindowclick.d() < 9) || packetplayinwindowclick.d() == 40) {
						click = (packetplayinwindowclick.d() == 40) ? ClickType.SWAP_OFFHAND : ClickType.NUMBER_KEY;
						var clickedSlot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
						if (clickedSlot.isAllowed(player)) {
							var hotbar = this.player.inventory.getItem(packetplayinwindowclick.d());
							var canCleanSwap = hotbar.isEmpty() || (clickedSlot.inventory == player.inventory && clickedSlot.isAllowed(hotbar)); // the slot will accept the hotbar item
							if (clickedSlot.hasItem()) {
								if (canCleanSwap) {
									action = InventoryAction.HOTBAR_SWAP;
								} else {
									action = InventoryAction.HOTBAR_MOVE_AND_READD;
								}
							} else if (!clickedSlot.hasItem() && !hotbar.isEmpty() && clickedSlot.isAllowed(hotbar)) {
								action = InventoryAction.HOTBAR_SWAP;
							} else {
								action = InventoryAction.NOTHING;
							}
						} else {
							action = InventoryAction.NOTHING;
						}
					}
				}
				case CLONE -> {
					if (packetplayinwindowclick.d() == 2) {
						click = ClickType.MIDDLE;
						if (packetplayinwindowclick.c() < 0) {
							action = InventoryAction.NOTHING;
						} else {
							var slot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
							if (slot != null && slot.hasItem() && player.abilities.canInstantlyBuild && player.inventory.getCarried().isEmpty()) {
								action = InventoryAction.CLONE_STACK;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					} else {
						click = ClickType.UNKNOWN;
						action = InventoryAction.UNKNOWN;
					}
				}
				case THROW -> {
					if (packetplayinwindowclick.c() >= 0) {
						if (packetplayinwindowclick.d() == 0) {
							click = ClickType.DROP;
							var slot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
							if (slot != null && slot.hasItem() && slot.isAllowed(player) && !slot.getItem().isEmpty() && slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
								action = InventoryAction.DROP_ONE_SLOT;
							} else {
								action = InventoryAction.NOTHING;
							}
						} else if (packetplayinwindowclick.d() == 1) {
							click = ClickType.CONTROL_DROP;
							var slot = this.player.activeContainer.getSlot(packetplayinwindowclick.c());
							if (slot != null && slot.hasItem() && slot.isAllowed(player) && !slot.getItem().isEmpty() && slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
								action = InventoryAction.DROP_ALL_SLOT;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					} else {
						// Sane default (because this happens when they are holding nothing. Don't ask why.)
						click = ClickType.LEFT;
						if (packetplayinwindowclick.d() == 1) {
							click = ClickType.RIGHT;
						}
						action = InventoryAction.NOTHING;
					}
				}
				case QUICK_CRAFT ->
					itemstack = this.player.activeContainer.a(packetplayinwindowclick.c(), packetplayinwindowclick.d(), packetplayinwindowclick.g(), this.player);
				case PICKUP_ALL -> {
					click = ClickType.DOUBLE_CLICK;
					action = InventoryAction.NOTHING;
					if (packetplayinwindowclick.c() >= 0 && !this.player.inventory.getCarried().isEmpty()) {
						var cursor = this.player.inventory.getCarried();
						action = InventoryAction.NOTHING;
						// Quick check for if we have any of the item
						if (inventory.getTopInventory().contains(CraftMagicNumbers.getMaterial(cursor.getItem())) || inventory.getBottomInventory().contains(CraftMagicNumbers.getMaterial(cursor.getItem()))) {
							action = InventoryAction.COLLECT_TO_CURSOR;
						}
					}
				}
				default -> {
				}
			}

			if (packetplayinwindowclick.g() != InventoryClickType.QUICK_CRAFT) {
				if (click == ClickType.NUMBER_KEY) {
					event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.c(), click, action, packetplayinwindowclick.d());
				} else {
					event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.c(), click, action);
				}

				var top = inventory.getTopInventory();
				if (packetplayinwindowclick.c() == 0 && top instanceof CraftingInventory) {
					var recipe = ((CraftingInventory) top).getRecipe();
					if (recipe != null) {
						if (click == ClickType.NUMBER_KEY) {
							event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.c(), click, action, packetplayinwindowclick.d());
						} else {
							event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.c(), click, action);
						}
					}
				}

				if (packetplayinwindowclick.c() == 2 && top instanceof SmithingInventory) {
					var result = ((SmithingInventory) top).getResult();
					if (result != null) {
						if (click == ClickType.NUMBER_KEY) {
							event = new SmithItemEvent(inventory, type, packetplayinwindowclick.c(), click, action, packetplayinwindowclick.d());
						} else {
							event = new SmithItemEvent(inventory, type, packetplayinwindowclick.c(), click, action);
						}
					}
				}

				event.setCancelled(cancelled);
				var oldContainer = this.player.activeContainer; // SPIGOT-1224
				server.getPluginManager().callEvent(event);
				if (this.player.activeContainer != oldContainer) {
					return;
				}

				switch (event.getResult()) {
					case ALLOW, DEFAULT ->
						itemstack = this.player.activeContainer.a(packetplayinwindowclick.c(), packetplayinwindowclick.d(), packetplayinwindowclick.g(), this.player);
					case DENY -> {
						switch (action) {
							// Modified other slots
							case PICKUP_ALL:
							case MOVE_TO_OTHER_INVENTORY:
							case HOTBAR_MOVE_AND_READD:
							case HOTBAR_SWAP:
							case COLLECT_TO_CURSOR:
							case UNKNOWN:
								this.player.updateInventory(this.player.activeContainer);
								break;
							// Modified cursor and clicked
							case PICKUP_SOME:
							case PICKUP_HALF:
							case PICKUP_ONE:
							case PLACE_ALL:
							case PLACE_SOME:
							case PLACE_ONE:
							case SWAP_WITH_CURSOR:
								this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
								this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.c(), this.player.activeContainer.getSlot(packetplayinwindowclick.c()).getItem()));
								break;
							// Modified clicked only
							case DROP_ALL_SLOT:
							case DROP_ONE_SLOT:
								this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.c(), this.player.activeContainer.getSlot(packetplayinwindowclick.c()).getItem()));
								break;
							// Modified cursor only
							case DROP_ALL_CURSOR:
							case DROP_ONE_CURSOR:
							case CLONE_STACK:
								this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
								break;
							// Nothing
							case NOTHING:
								break;
						}
						return;
					}
				}

				if (event instanceof CraftItemEvent || event instanceof SmithItemEvent) {
					// Need to update the inventory on crafting to
					// correctly support custom recipes
					player.updateInventory(player.activeContainer);
				}
			}
			// CraftBukkit end
			if (ItemStack.matches(packetplayinwindowclick.f(), itemstack)) {
				this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.b(), packetplayinwindowclick.e(), true));
				this.player.e = true;
				this.player.activeContainer.c();
				this.player.broadcastCarriedItem();
				this.player.e = false;
			} else {
				this.k.put(this.player.activeContainer.windowId, packetplayinwindowclick.e());
				this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.b(), packetplayinwindowclick.e(), false));
				this.player.activeContainer.a(this.player, false);
				NonNullList<ItemStack> nonnulllist1 = NonNullList.a();

				for (var j = 0; j < this.player.activeContainer.slots.size(); ++j) {
					var itemstack1 = this.player.activeContainer.slots.get(j).getItem();

					nonnulllist1.add(itemstack1.isEmpty() ? ItemStack.b : itemstack1);
				}

				this.player.a(this.player.activeContainer, nonnulllist1);
			}
		}
	}

	@Override
	public void a(PacketPlayInAutoRecipe packetplayinautorecipe) {
		// Paper start
		if (!Bukkit.isPrimaryThread()) {
			var autoRecipeIncrement = PaperConfig.autoRecipeIncrement;
			if (((int) VH__RECIPE_SPAM_LIMITER.getAndAdd(this, PaperConfig.autoRecipeIncrement)) + autoRecipeIncrement > PaperConfig.autoRecipeLimit) {
				minecraftServer.scheduleOnMain(() -> this.disconnect(new ChatMessage("disconnect.spam", new Object[0]), org.bukkit.event.player.PlayerKickEvent.Cause.SPAM)); // Paper - kick event cause
				return;
			}
		}
		// Paper end
		PlayerConnectionUtils.ensureMainThread(packetplayinautorecipe, this, this.player.getWorldServer());
		this.player.resetIdleTimer();
		if (!this.player.isSpectator() && this.player.activeContainer.windowId == packetplayinautorecipe.b() && this.player.activeContainer.c(this.player) && this.player.activeContainer instanceof ContainerRecipeBook) {
			// Paper start - fire event for clicking recipes in the recipe book
			var event = new com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent(
				player.getBukkitEntity(), org.bukkit.craftbukkit.util.CraftNamespacedKey.fromMinecraft(packetplayinautorecipe.c()), packetplayinautorecipe.d());
			if (event.callEvent()) {
				this.minecraftServer.getCraftingManager().getRecipe(org.bukkit.craftbukkit.util.CraftNamespacedKey.toMinecraft(event.getRecipe())).ifPresent((irecipe) -> {
					((ContainerRecipeBook) this.player.activeContainer).a(event.isMakeAll(), irecipe, this.player);
				});
			}
			// Paper end
		}
	}

	@Override
	public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
		PlayerConnectionUtils.ensureMainThread(packetplayinenchantitem, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		this.player.resetIdleTimer();
		if (this.player.activeContainer.windowId == packetplayinenchantitem.b() && this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
			this.player.activeContainer.a(this.player, packetplayinenchantitem.c());
			this.player.activeContainer.c();
		}
	}

	@Override
	public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsetcreativeslot, this, this.player.getWorldServer());
		if (this.player.playerInteractManager.isCreative()) {
			var flag = packetplayinsetcreativeslot.b() < 0;
			var itemstack = packetplayinsetcreativeslot.getItemStack();
			var nbttagcompound = itemstack.b("BlockEntityTag");

			if (!itemstack.isEmpty() && nbttagcompound != null && nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z") && this.player.getBukkitEntity().hasPermission("minecraft.nbt.copy")) { // Spigot
				var blockposition = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
				var tileentity = this.player.world.getTileEntity(blockposition);

				if (tileentity != null) {
					var nbttagcompound1 = tileentity.save(new NBTTagCompound());

					nbttagcompound1.remove("x");
					nbttagcompound1.remove("y");
					nbttagcompound1.remove("z");
					itemstack.a("BlockEntityTag", nbttagcompound1);
				}
			}

			var flag1 = packetplayinsetcreativeslot.b() >= 1 && packetplayinsetcreativeslot.b() <= 45;
			var flag2 = itemstack.isEmpty() || itemstack.getDamage() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();
			if (flag || (flag1 && !ItemStack.matches(this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.b()).getItem(), packetplayinsetcreativeslot.getItemStack()))) { // Insist on valid slot
				// CraftBukkit start - Call click event
				InventoryView inventory = this.player.defaultContainer.getBukkitView();
				var item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.getItemStack());

				var type = SlotType.QUICKBAR;
				if (flag) {
					type = SlotType.OUTSIDE;
				} else if (packetplayinsetcreativeslot.b() < 36) {
					if (packetplayinsetcreativeslot.b() >= 5 && packetplayinsetcreativeslot.b() < 9) {
						type = SlotType.ARMOR;
					} else {
						type = SlotType.CONTAINER;
					}
				}
				var event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetplayinsetcreativeslot.b(), item);
				server.getPluginManager().callEvent(event);

				itemstack = CraftItemStack.asNMSCopy(event.getCursor());

				switch (event.getResult()) {
					case ALLOW:
						// Plugin cleared the id / stacksize checks
						flag2 = true;
						break;
					case DEFAULT:
						break;
					case DENY:
						// Reset the slot
						if (packetplayinsetcreativeslot.b() >= 0) {
							this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.defaultContainer.windowId, packetplayinsetcreativeslot.b(), this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.b()).getItem()));
							this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, ItemStack.b));
						}
						return;
				}
			}
			// CraftBukkit end

			if (flag1 && flag2) {
				if (itemstack.isEmpty()) {
					this.player.defaultContainer.setItem(packetplayinsetcreativeslot.b(), ItemStack.b);
				} else {
					this.player.defaultContainer.setItem(packetplayinsetcreativeslot.b(), itemstack);
				}

				this.player.defaultContainer.a(this.player, true);
				this.player.defaultContainer.c();
			} else if (flag && flag2 && this.j < 200) {
				this.j += 20;
				this.player.drop(itemstack, true);
			}
		} else {
			kickDoNot(-666); // TODO: Instaban
			return;
		}
	}

	@Override
	public void a(PacketPlayInTransaction packetplayintransaction) {
		PlayerConnectionUtils.ensureMainThread(packetplayintransaction, this, this.player.getWorldServer());
		if (this.player.isFrozen()) return; // CraftBukkit
		var i = this.player.activeContainer.windowId;

		if (i == packetplayintransaction.b() && this.k.getOrDefault(i, (short) (packetplayintransaction.c() + 1)) == packetplayintransaction.c() && !this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
			this.player.activeContainer.a(this.player, true);
		}

	}

	@Override
	public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
		var list = Stream.of(packetplayinupdatesign.c()).map(EnumChatFormat::a).collect(Collectors.toList());

		this.a(list, (list1) -> {
			this.a(packetplayinupdatesign, list1);
		});
	}

	private void a(PacketPlayInUpdateSign packetplayinupdatesign, List<String> list) {
		if (this.player.isFrozen()) return; // CraftBukkit
		this.player.resetIdleTimer();
		var worldserver = this.player.getWorldServer();
		var blockposition = packetplayinupdatesign.b();

		if (worldserver.isLoaded(blockposition)) {
			var iblockdata = worldserver.getType(blockposition);
			var tileentity = worldserver.getTileEntity(blockposition);

			if (!(tileentity instanceof TileEntitySign tileentitysign)) {
				return;
			}

			if (!tileentitysign.d() || tileentitysign.signEditor == null || !tileentitysign.signEditor.equals(this.player.getUniqueID())) {
				PlayerConnection.LOGGER.warn("Player {} just tried to change non-editable sign", this.player.getDisplayName().getString());
				this.sendPacket(tileentity.getUpdatePacket()); // CraftBukkit
				return;
			}

			// CraftBukkit start // Paper start - Adventure
			List<net.kyori.adventure.text.Component> lines = new ObjectArrayList<>();

			for (var i = 0; i < list.size(); ++i) {
				// Paper start - cap line length - modified clients can send longer data than normal
				var currentLine = list.get(i);
				if (MAX_SIGN_LINE_LENGTH > 0 && currentLine.length() > MAX_SIGN_LINE_LENGTH) {
					// This handles multibyte characters as 1
					var offset = currentLine.codePoints().limit(MAX_SIGN_LINE_LENGTH).map(Character::charCount).sum();
					if (offset < currentLine.length()) {
						list.set(i, currentLine = currentLine.substring(0, offset));
					}
				}
				// Paper end
				// Purpur start
				if (worldserver.purpurConfig.signAllowColors) {
					org.bukkit.entity.Player bukkitPlayer = player.getBukkitEntity();
					if (bukkitPlayer.hasPermission("purpur.sign.color"))
						currentLine = currentLine.replaceAll("(?i)&([0-9a-fr])", "\u00a7$1");
					if (bukkitPlayer.hasPermission("purpur.sign.style"))
						currentLine = currentLine.replaceAll("(?i)&([l-or])", "\u00a7$1");
					if (bukkitPlayer.hasPermission("purpur.sign.magic"))
						currentLine = currentLine.replaceAll("(?i)&([kr])", "\u00a7$1");
					lines.add(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(currentLine));
				} else
					// Purpur end
					lines.add(net.kyori.adventure.text.Component.text(SharedConstants.filterAllowedChatCharacters(currentLine))); // Paper - Replaced with anvil color stripping method to stop exploits that allow colored signs to be created.
			}
			var event = new SignChangeEvent(org.bukkit.craftbukkit.block.CraftBlock.at(worldserver, blockposition), this.getPlayer(), lines);
			this.server.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				for (var i = 0; i < 4; i++) {
					tileentitysign.a(i, PaperAdventure.asVanilla(event.line(i)));
				}
				// Paper end
				tileentitysign.isEditable = false;
			}
			// CraftBukkit end

			tileentitysign.update();
			worldserver.notify(blockposition, iblockdata, iblockdata, 3);
		}

	}

	@Override
	public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
		// Purpur start
		if (net.pl3x.purpur.PurpurConfig.useAlternateKeepAlive) {
			var id = packetplayinkeepalive.getId();
			if (keepAlives.size() > 0 && keepAlives.contains(id)) {
				var ping = (int) (SystemUtils.getMonotonicMillis() - id);
				player.ping = (player.ping * 3 + ping) / 4;
				keepAlives.clear(); // we got a valid response, lets roll with it and forget the rest
			}
		} else
			// Purpur end
			//PlayerConnectionUtils.ensureMainThread(packetplayinkeepalive, this, this.player.getWorldServer()); // CraftBukkit // Paper - This shouldn't be on the main thread
			if (this.awaitingKeepAlive && packetplayinkeepalive.b() == this.h) {
				var i = (int) (SystemUtils.getMonotonicMillis() - this.lastKeepAlive);

				this.player.ping = (this.player.ping * 3 + i) / 4;
				this.awaitingKeepAlive = false;
			} else if (!this.isExemptPlayer()) {
				// Paper start - This needs to be handled on the main thread for plugins
				minecraftServer.scheduleOnMain(() -> {
					this.disconnect(new ChatMessage("disconnect.timeout"), org.bukkit.event.player.PlayerKickEvent.Cause.TIMEOUT); // Paper - kick event cause
				});
				// Paper end
			}

	}

	@Override
	public void a(PacketPlayInAbilities packetplayinabilities) {
		PlayerConnectionUtils.ensureMainThread(packetplayinabilities, this, this.player.getWorldServer());
		if (packetplayinabilities.isFlying() && !this.player.abilities.canFly) {
			kickDoNot(306);
			return;
		}
		// CraftBukkit start
		if (this.player.abilities.canFly && this.player.abilities.isFlying != packetplayinabilities.isFlying()) {
			var event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetplayinabilities.isFlying());
			this.server.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.player.abilities.isFlying = packetplayinabilities.isFlying(); // Actually set the player's flying status
			} else {
				this.player.updateAbilities(); // Tell the player their ability was reverted
			}
		}
		// CraftBukkit end
	}

	@Override
	public void a(PacketPlayInSettings packetplayinsettings) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsettings, this, this.player.getWorldServer());
		this.player.a(packetplayinsettings);
	}

	// CraftBukkit start
	private static final MinecraftKey CUSTOM_REGISTER = new MinecraftKey("register");
	private static final MinecraftKey CUSTOM_UNREGISTER = new MinecraftKey("unregister");

	private static final MinecraftKey MINECRAFT_BRAND = new MinecraftKey("brand"); // Paper - Brand support

	@Override
	public void a(PacketPlayInCustomPayload packetplayincustompayload) {
		PlayerConnectionUtils.ensureMainThread(packetplayincustompayload, this, this.player.getWorldServer());
		if (packetplayincustompayload.tag.equals(CUSTOM_REGISTER)) {
			try {
				var channels = packetplayincustompayload.data.toString(com.google.common.base.Charsets.UTF_8);
				for (var channel : channels.split("\0")) {
					getPlayer().addChannel(channel);
				}
			} catch (Exception ex) {
				PlayerConnection.LOGGER.error("Couldn't register custom payload", ex);
				this.disconnect("Invalid payload REGISTER!", org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_PAYLOAD); // Paper - kick event cause
			}
		} else if (packetplayincustompayload.tag.equals(CUSTOM_UNREGISTER)) {
			try {
				var channels = packetplayincustompayload.data.toString(com.google.common.base.Charsets.UTF_8);
				for (var channel : channels.split("\0")) {
					getPlayer().removeChannel(channel);
				}
			} catch (Exception ex) {
				PlayerConnection.LOGGER.error("Couldn't unregister custom payload", ex);
				this.disconnect("Invalid payload UNREGISTER!", org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_PAYLOAD); // Paper - kick event cause
			}
		} else {
			try {
				var data = new byte[packetplayincustompayload.data.readableBytes()];
				packetplayincustompayload.data.readBytes(data);

				// Paper start - Brand support
				if (packetplayincustompayload.tag.equals(MINECRAFT_BRAND)) {
					try {
						this.clientBrandName = new PacketDataSerializer(Unpooled.copiedBuffer(data)).readUTF(256);
					} catch (StringIndexOutOfBoundsException ex) {
						this.clientBrandName = "illegal";
					}
				}
				// Paper end
				server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packetplayincustompayload.tag.toString(), data);
			} catch (Exception ex) {
				PlayerConnection.LOGGER.error("Couldn't dispatch custom payload", ex);
				this.disconnect("Invalid custom payload!", org.bukkit.event.player.PlayerKickEvent.Cause.INVALID_PAYLOAD); // Paper - kick event cause
			}
		}

	}

	// Paper start - brand support
	public String getClientBrandName() {
		return clientBrandName;
	}
	// Paper end

	public final boolean isDisconnected() {
		return (!this.player.joining && !this.networkManager.isConnected()) || this.processedDisconnect; // Paper
	}
	// CraftBukkit end

	@Override
	public void a(PacketPlayInDifficultyChange packetplayindifficultychange) {
	}

	@Override
	public void a(PacketPlayInDifficultyLock packetplayindifficultylock) {
		PlayerConnectionUtils.ensureMainThread(packetplayindifficultylock, this, this.player.getWorldServer());
		if (this.player.k(2) || this.isExemptPlayer()) {
			this.minecraftServer.b(packetplayindifficultylock.b());
		}
	}

	//// BadPackets checks fields
	private int grim$actions$totalFlyingPacketsSent = 0;
	private int grim$H$lastSent = 0;
	private boolean grim$F$lastSprinting;
	private boolean grim$F$thanksMojang;
	public int grim$E$noReminderTicks;
}
