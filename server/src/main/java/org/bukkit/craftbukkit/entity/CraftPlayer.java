package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.ClientOption.ChatVisibility;
import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.Title;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.SystemUtils;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.AdvancementDataPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.saveddata.maps.MapIcon;
import net.minecraft.world.phys.Vec3D;
import net.pl3x.purpur.PurpurConfig;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new ObjectOpenHashSet<>();
    private final Map<UUID, Set<WeakReference<Plugin>>> hiddenPlayers = new Object2ObjectOpenHashMap<>();
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;
    // Paper start
    private org.bukkit.event.player.PlayerResourcePackStatusEvent.Status resourcePackStatus;
    private String resourcePackHash;
    private static final boolean DISABLE_CHANNEL_LIMIT = System.getProperty("paper.disableChannelLimit") != null; // Paper - add a flag to disable the channel limit
    private long lastSaveTime;
    // Paper end

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);

        firstPlayed = SystemUtils.getMonotonicMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getProfile();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {}

    @Override
    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    @Override
    public InetSocketAddress getAddress() {
        var handle = getHandle();
        if (handle.playerConnection == null) return null;

        var addr = handle.playerConnection.networkManager.getSocketAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    // Paper start - Implement NetworkClient
    @Override
    public int getProtocolVersion() {
        var handle = getHandle();
        if (handle.playerConnection == null) return -1;
        return handle.playerConnection.networkManager.protocolVersion;
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        var handle = getHandle();
        if (handle.playerConnection == null) return null;
        return handle.playerConnection.networkManager.virtualHost;
    }
    // Paper end

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62D;
        } else {
            return getEyeHeight();
        }
    }

    @Override
    public void sendRawMessage(String message) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        for (var component : CraftChatMessage.fromString(message)) {
            handle.playerConnection.sendPacket(new PacketPlayOutChat(component, ChatMessageType.SYSTEM, SystemUtils.b));
        }
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        for (var component : CraftChatMessage.fromString(message)) {
            handle.playerConnection.sendPacket(new PacketPlayOutChat(component, ChatMessageType.CHAT, (sender == null) ? SystemUtils.b : sender));
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (var message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String[] messages) {
        for (var message : messages) {
            sendMessage(sender, message);
        }
    }

    // Paper start
    @Override
    public void sendActionBar(BaseComponent[] message) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;
        handle.playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, message, -1, -1, -1));
    }

    @Override
    public void sendActionBar(String message) {
        var handle = getHandle();
        if (handle.playerConnection == null || message == null || message.isEmpty()) return;
        handle.playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, CraftChatMessage.fromStringOrNull(message)));
    }

    @Override
    public void sendActionBar(char alternateChar, String message) {
        if (message == null || message.isEmpty()) return;
        sendActionBar(org.bukkit.ChatColor.translateAlternateColorCodes(alternateChar, message));
    }

    @Override
    public void setPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer) {
         if (header != null) {
             var headerJson = net.md_5.bungee.chat.ComponentSerializer.toString(header);
             playerListHeader = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(headerJson);
         } else {
             playerListHeader = null;
         }

        if (footer != null) {
            var footerJson = net.md_5.bungee.chat.ComponentSerializer.toString(footer);
             playerListFooter = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(footerJson);
        } else {
             playerListFooter = null;
         }

         updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(BaseComponent header, BaseComponent footer) {
        this.setPlayerListHeaderFooter(header == null ? null : new BaseComponent[]{header},
                footer == null ? null : new BaseComponent[]{footer});
    }


    @Override
    public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (BaseComponent[]) null, fadeInTicks, stayTicks, fadeOutTicks));
    }

    @Override
    public void setSubtitle(BaseComponent[] subtitle) {
        getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle, 0, 0, 0));
    }

    @Override
    public void setSubtitle(BaseComponent subtitle) {
        setSubtitle(new BaseComponent[]{subtitle});
    }

    @Override
    public void showTitle(BaseComponent[] title) {
        getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, title, 0, 0, 0));
    }

    @Override
    public void showTitle(BaseComponent title) {
        showTitle(new BaseComponent[]{title});
    }

    @Override
    public void showTitle(BaseComponent[] title, BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        setTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
        setSubtitle(subtitle);
        showTitle(title);
    }

    @Override
    public void showTitle(BaseComponent title, BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        setTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
        setSubtitle(subtitle);
        showTitle(title);
    }

    @Override
    public void sendTitle(Title title) {
        Preconditions.checkNotNull(title, "Title is null");
        setTitleTimes(title.getFadeIn(), title.getStay(), title.getFadeOut());
        setSubtitle(title.getSubtitle() == null ? new BaseComponent[0] : title.getSubtitle());
        showTitle(title.getTitle());
    }

    @Override
    public void updateTitle(Title title) {
        Preconditions.checkNotNull(title, "Title is null");
        setTitleTimes(title.getFadeIn(), title.getStay(), title.getFadeOut());
        if (title.getSubtitle() != null) {
            setSubtitle(title.getSubtitle());
        }
        showTitle(title.getTitle());
    }

    @Override
    public void hideTitle() {
        getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, (BaseComponent[]) null, 0, 0, 0));
    }
    // Paper end

    @Override
    public String getDisplayName() {
        return io.papermc.paper.adventure.DisplayNames.getLegacy(this); // Paper
    }

    @Override
    public void setDisplayName(String name) {
        var handle = getHandle();
        handle.adventure$displayName = name != null ? io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(name) : net.kyori.adventure.text.Component.text(this.getName()); // Paper
        handle.displayName = name == null ? getName() : name;
    }

    // Paper start
    @Override
    public void playerListName(net.kyori.adventure.text.Component name) {
        var handle = getHandle();
        handle.listName = name == null ? null : io.papermc.paper.adventure.PaperAdventure.asVanilla(name);
        for (var player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, handle));
            }
        }
    }
    @Override
    public net.kyori.adventure.text.Component playerListName() {
        var handle = getHandle();
        return handle.listName == null ? net.kyori.adventure.text.Component.text(getName()) : io.papermc.paper.adventure.PaperAdventure.asAdventure(handle.listName);
    }
    @Override
    public net.kyori.adventure.text.Component playerListHeader() {
        return playerListHeader;
    }
    @Override
    public net.kyori.adventure.text.Component playerListFooter() {
        return playerListFooter;
    }
    // Paper end
    @Override
    public String getPlayerListName() {
        var handle = getHandle();
        return handle.listName == null ? getName() : CraftChatMessage.fromComponent(handle.listName);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = getName();
        }
        var handle = getHandle();
        handle.listName = name.equals(getName()) ? null : CraftChatMessage.fromStringOrNull(name);
        for (var player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, handle));
            }
        }
    }

    private net.kyori.adventure.text.Component playerListHeader; // Paper - Adventure
    private net.kyori.adventure.text.Component playerListFooter; // Paper - Adventure

    @Override
    public String getPlayerListHeader() {
        return (playerListHeader == null) ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.serialize(playerListHeader); // Paper - Adventure
    }

    @Override
    public String getPlayerListFooter() {
        return (playerListFooter == null) ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.serialize(playerListFooter); // Paper - Adventure
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = header == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(header); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = footer == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(footer); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = header == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(header); // Paper - Adventure
        this.playerListFooter = footer == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(footer); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.header = (this.playerListHeader == null) ? new ChatComponentText("") : io.papermc.paper.adventure.PaperAdventure.asVanilla(this.playerListHeader); // Paper - Adventure
        packet.footer = (this.playerListFooter == null) ? new ChatComponentText("") : io.papermc.paper.adventure.PaperAdventure.asVanilla(this.playerListFooter); // Paper - Adventure
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        var other = (OfflinePlayer) obj;
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        var uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        var idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        handle.playerConnection.disconnect(message == null ? "" : message, org.bukkit.event.player.PlayerKickEvent.Cause.PLUGIN); // Paper - kick event cause
    }

    // Paper start
    @Override
    public void kick(net.kyori.adventure.text.Component message) {
        kick(message, org.bukkit.event.player.PlayerKickEvent.Cause.PLUGIN);
    }

    @Override
    public void kick(net.kyori.adventure.text.Component message, org.bukkit.event.player.PlayerKickEvent.Cause cause) {
        var connection = getHandle().playerConnection;
        if (connection != null) {
            connection.disconnect(message == null ? net.kyori.adventure.text.Component.empty() : message, cause);
        }
    }
    // Paper end

    @Override
    public void setCompassTarget(Location loc) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        handle.playerConnection.sendPacket(new PacketPlayOutSpawnPosition(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), loc.getYaw()));
    }

    @Override
    public Location getCompassTarget() {
        var handle = getHandle();
        return handle.compassTarget;
    }

    @Override
    public void chat(String msg) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        handle.playerConnection.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var instrumentName = switch (instrument) {
            case 0 -> "harp";
            case 1 -> "basedrum";
            case 2 -> "snare";
            case 3 -> "hat";
            case 4 -> "bass";
            case 5 -> "flute";
            case 6 -> "bell";
            case 7 -> "guitar";
            case 8 -> "chime";
            case 9 -> "xylophone";
            default -> null;
        };

        var f = (float) Math.pow(2.0D, (note - 12.0D) / 12.0D);
        handle.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.sounds.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var instrumentName = switch (instrument.ordinal()) {
            case 0 -> "harp";
            case 1 -> "basedrum";
            case 2 -> "snare";
            case 3 -> "hat";
            case 4 -> "bass";
            case 5 -> "flute";
            case 6 -> "bell";
            case 7 -> "guitar";
            case 8 -> "chime";
            case 9, 16 -> "xylophone";
            case 10 -> "iron_xylophone";
            case 11 -> "cow_bell";
            case 12 -> "didgeridoo";
            case 13 -> "bit";
            case 14 -> "banjo";
            case 15 -> "pling";
            default -> null;
        };
        var f = (float) Math.pow(2.0D, (note.getId() - 12.0D) / 12.0D);
        handle.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.sounds.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        var handle = getHandle();
        if (loc == null || sound == null || category == null || handle.playerConnection == null) return;

        var packet = new PacketPlayOutNamedSoundEffect(CraftSound.getSoundEffect(sound), net.minecraft.sounds.SoundCategory.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        var handle = getHandle();
        if (loc == null || sound == null || category == null || handle.playerConnection == null) return;

        var packet = new PacketPlayOutCustomSoundEffect(new MinecraftKey(sound), net.minecraft.sounds.SoundCategory.valueOf(category.name()), new Vec3D(loc.getX(), loc.getY(), loc.getZ()), volume, pitch);
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, org.bukkit.SoundCategory category) {
        stopSound(sound.getKey().getKey(), category);
    }

    @Override
    public void stopSound(String sound, org.bukkit.SoundCategory category) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        handle.playerConnection.sendPacket(new PacketPlayOutStopSound(new MinecraftKey(sound), category == null ? net.minecraft.sounds.SoundCategory.MASTER : net.minecraft.sounds.SoundCategory.valueOf(category.name())));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var packetData = effect.getId();
        var packet = new PacketPlayOutWorldEvent(packetData, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), data, false);
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        var datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var packet = new PacketPlayOutBlockChange(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), CraftMagicNumbers.getBlock(material, data));
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var packet = new PacketPlayOutBlockChange(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), ((CraftBlockData) block).getState());
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void sendBlockDamage(Location loc, float progress) {
        Preconditions.checkArgument(loc != null, "loc must not be null");
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "progress must be between 0.0 and 1.0 (inclusive)");
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var stage = (int) (9 * progress); // There are 0 - 9 damage states
        var packet = new PacketPlayOutBlockBreakAnimation(handle.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), stage);
        handle.playerConnection.sendPacket(packet);
    }

    // Paper start
    @Override
    public void sendSignChange(Location loc, List<net.kyori.adventure.text.Component> lines) {
        this.sendSignChange(loc, lines, org.bukkit.DyeColor.BLACK);
    }
    @Override
    public void sendSignChange(Location loc, List<net.kyori.adventure.text.Component> lines, DyeColor dyeColor) {
        if (getHandle().playerConnection == null) {
            return;
        }
        if (lines == null) {
            lines = new ObjectArrayList<>(4);
        }
        Validate.notNull(loc, "Location cannot be null");
        Validate.notNull(dyeColor, "DyeColor cannot be null");
        if (lines.size() < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }
        var components = CraftSign.sanitizeLines(lines);
        this.sendSignChange0(components, loc, dyeColor);
    }

    private void sendSignChange0(IChatBaseComponent[] components, Location loc, DyeColor dyeColor) {
        var sign = new TileEntitySign();
        sign.setPosition(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        sign.setColor(EnumColor.fromColorIndex(dyeColor.getWoolData()));
        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);

        getHandle().playerConnection.sendPacket(sign.getUpdatePacket());
    }
    // Paper end
    @Override
    public void sendSignChange(Location loc, String[] lines) {
       sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) {
        if (getHandle().playerConnection == null) {
            return;
        }

        if (lines == null) {
            lines = new String[4];
        }

        Validate.notNull(loc, "Location can not be null");
        Validate.notNull(dyeColor, "DyeColor can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }

        var components = CraftSign.sanitizeLines(lines);
        /*TileEntitySign sign = new TileEntitySign(); // Paper
        sign.setPosition(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        sign.setColor(EnumColor.fromColorIndex(dyeColor.getWoolData()));
        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);

        handle.playerConnection.sendPacket(sign.getUpdatePacket());*/ // Paper
        this.sendSignChange0(components, loc, dyeColor); // Paper
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().playerConnection == null) return false;

        /*
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int cx = x >> 4;
        int cz = z >> 4;

        if (sx <= 0 || sy <= 0 || sz <= 0) {
            return false;
        }

        if ((x + sx - 1) >> 4 != cx || (z + sz - 1) >> 4 != cz || y < 0 || y + sy > 128) {
            return false;
        }

        if (data.length != (sx * sy * sz * 5) / 2) {
            return false;
        }

        Packet51MapChunk packet = new Packet51MapChunk(x, y, z, sx, sy, sz, data);

        handle.playerConnection.sendPacket(packet);

        return true;
        */

        throw new NotImplementedException("Chunk changes do not yet work"); // TODO: Chunk changes.
    }

    @Override
    public void sendMap(MapView map) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        var data = ((CraftMapView) map).render(this);
        Collection<MapIcon> icons = new ObjectArrayList<MapIcon>();
        for (var cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapIcon(MapIcon.Type.a(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection(), CraftChatMessage.fromStringOrNull(cursor.getCaption())));
            }
        }

        var packet = new PacketPlayOutMap(map.getId(), map.getScale().getValue(), true, map.isLocked(), icons, data.buffer, 0, 0, 128, 128);
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new UnsupportedOperationException("Cannot set rotation of players. Consider teleporting instead.");
    }

    // Paper start
    @Override
    public java.util.concurrent.CompletableFuture<Boolean> teleportAsync(Location loc, @javax.annotation.Nonnull PlayerTeleportEvent.TeleportCause cause) {
        ((CraftWorld)loc.getWorld()).getHandle().getChunkProvider().markAreaHighPriority(new ChunkCoordIntPair(MathHelper.floor(loc.getX()) >> 4, MathHelper.floor(loc.getZ()) >> 4), 28, 3); // Paper - load area high priority
        return super.teleportAsync(loc, cause);
    }
    // Paper end

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        location.checkFinite();

        var entity = getHandle();

        if (getHealth() == 0 || entity.dead) {
            return false;
        }

        if (entity.playerConnection == null) {
           return false;
        }

        if (entity.isVehicle()) {
            // Purpur start
            if (new net.pl3x.purpur.event.entity.EntityTeleportHinderedEvent(entity.getBukkitEntity(), net.pl3x.purpur.event.entity.EntityTeleportHinderedEvent.Reason.IS_VEHICLE, cause).callEvent())
                return teleport(location, cause);
            // Purpur end
            return false;
        }

        // From = Players current Location
        var from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        var to = location;
        // Create & Call the Teleport Event.
        var event = new PlayerTeleportEvent(this, from, to, cause);
        server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // SPIGOT-5509: Wakeup, similar to riding
        if (this.isSleeping()) {
            this.wakeup(false);
        }

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        var fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        var toWorld = ((CraftWorld) to.getWorld()).getHandle();

        var handle = getHandle();
        // Close any foreign inventory
        if (handle.activeContainer != handle.defaultContainer) {
            handle.closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason.TELEPORT); // Paper
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.playerConnection.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld, true, to, !toWorld.paperConfig.disableTeleportationSuffocationCheck); // Paper
        }
        return true;
    }

    // Paper start - Ugly workaround for SPIGOT-1915 & GH-114
    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        var wasSet = super.setPassenger(passenger);
        if (wasSet) {
            var handle = getHandle();
            handle.playerConnection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutMount(handle));
        }
        return wasSet;
    }
    // Paper end

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        server.getHandle().playerFileData.load(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().playerFileData.save(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        var handle = getHandle();
        handle.updateInventory(handle.activeContainer);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        var handle = getHandle();
        handle.fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().everyoneSleeping();
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public Location getBedSpawnLocation() {
        var handle = getHandle();
        var world = handle.server.getWorldServer(handle.getSpawnDimension());
        var bed = handle.getSpawn();

        if (world != null && bed != null) {
            var spawnLoc = EntityHuman.getBed(world, bed, handle.getSpawnAngle(), handle.isSpawnForced(), true);
            if (spawnLoc.isPresent()) {
                var vec = spawnLoc.get();
                return new Location(world.getWorld(), vec.x, vec.y, vec.z);
            }
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        var handle = getHandle();
        if (location == null) {
            handle.setRespawnPosition(null, null, 0.0F, override, false);
        } else {
            handle.setRespawnPosition(((CraftWorld) location.getWorld()).getHandle().getDimensionKey(), new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), location.getYaw(), override, false);
        }
    }

    @Override
    public Location getBedLocation() {
        Preconditions.checkState(isSleeping(), "Not sleeping");

        var bed = getHandle().getSpawn();
        return new Location(getWorld(), bed.getX(), bed.getY(), bed.getZ());
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        return getHandle().getRecipeBook().hasDiscoveredRecipe(CraftNamespacedKey.toMinecraft(recipe));
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        ImmutableSet.Builder<NamespacedKey> bukkitRecipeKeys = ImmutableSet.builder();
        getHandle().getRecipeBook().recipes.forEach(key -> bukkitRecipeKeys.add(CraftNamespacedKey.fromMinecraft(key)));
        return bukkitRecipeKeys.build();
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        return CraftStatistic.getStatistic(getHandle().getStatisticManager(), statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatisticManager(), statistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return CraftStatistic.getStatistic(getHandle().getStatisticManager(), statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic, material, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic, material, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatisticManager(), statistic, material, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return CraftStatistic.getStatistic(getHandle().getStatisticManager(), statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatisticManager(), statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatisticManager(), statistic, entityType, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatisticManager(), statistic, entityType, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        var handle = getHandle();
        handle.timeOffset = time;
        handle.relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getHandle().getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {}

    @Override
    public void setGameMode(GameMode mode) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        handle.setGamemode(EnumGamemode.getById(mode.getValue()), org.bukkit.event.player.PlayerGameModeChangeEvent.Cause.PLUGIN, null); // Paper
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().playerInteractManager.getGameMode().getId());
    }

    // Paper start
    @Override
    public int applyMending(int amount) {
        var handle = getHandle();
        // Logic copied from EntityExperienceOrb and remapped to unobfuscated methods/properties
        var itemstack = EnchantmentManager.getRandomEquippedItemWithEnchant(Enchantments.MENDING, handle);
        if (!itemstack.isEmpty() && itemstack.getItem().usesDurability()) {

            var orb = EntityTypes.EXPERIENCE_ORB.create(handle.world);
            orb.value = amount;
            orb.spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.CUSTOM;
            orb.setPositionRaw(handle.locX(), handle.locY(), handle.locZ());

            var i = Math.min(orb.xpToDur(amount), itemstack.getDamage());
            var event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemMendEvent(handle, orb, itemstack, i);
            i = event.getRepairAmount();
            orb.dead = true;
            if (!event.isCancelled()) {
                amount -= orb.durToXp(i);
                itemstack.setDamage(itemstack.getDamage() - i);
            }
        }
        return amount;
    }

    @Override
    public void giveExp(int exp, boolean applyMending) {
        if (applyMending) {
            exp = this.applyMending(exp);
        }
        // Paper end
        getHandle().giveExp(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        getHandle().levelDown(levels);
    }

    @Override
    public float getExp() {
        return getHandle().exp;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        var handle = getHandle();
        handle.exp = exp;
        handle.lastSentExp = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().expLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        var handle = getHandle();
        handle.expLevel = level;
        handle.lastSentExp = -1;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().expTotal;
    }

    @Override
    public void setTotalExperience(int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience points must not be negative (%s)", exp);
        getHandle().expTotal = exp;
    }

    @Override
    public void sendExperienceChange(float progress) {
        sendExperienceChange(progress, getLevel());
    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        var handle = getHandle();
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", progress);
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);

        if (handle.playerConnection == null) {
            return;
        }

        var packet = new PacketPlayOutExperience(progress, getTotalExperience(), level);
        handle.playerConnection.sendPacket(packet);
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return (plugin == null) ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    @Override
    @Deprecated
    public void hidePlayer(Player player) {
        hidePlayer0(null, player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        Validate.isTrue(plugin.isEnabled(), "Plugin attempted to hide player while disabled");

        hidePlayer0(plugin, player);
    }

    private void hidePlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (getHandle().playerConnection == null) return;
        if (equals(player)) return;

        var hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins != null) {
            // Some plugins are already hiding the player. Just mark that this
            // plugin wants the player hidden too and end.
            hidingPlugins.add(getPluginWeakReference(plugin));
            return;
        }
        hidingPlugins = new HashSet<>();
        hidingPlugins.add(getPluginWeakReference(plugin));
        hiddenPlayers.put(player.getUniqueId(), hidingPlugins);

        // Remove this player from the hidden player's EntityTrackerEntry
        // Paper start
        var other = ((CraftPlayer) player).getHandle();
        unregisterPlayer(other);
    }
    private void unregisterPlayer(EntityPlayer other) {
        var handle = getHandle();
        var tracker = ((WorldServer) entity.world).getChunkProvider().playerChunkMap;
        // Paper end
        var entry = tracker.trackedEntities.get(other.getId());
        if (entry != null) {
            entry.clear(handle);
        }

        // Remove the hidden player from this player user list, if they're on it
        if (other.sentListPacket) {
            handle.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, other));
        }
    }

    @Override
    @Deprecated
    public void showPlayer(Player player) {
        showPlayer0(null, player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        // Don't require that plugin be enabled. A plugin must be allowed to call
        // showPlayer during its onDisable() method.
        showPlayer0(plugin, player);
    }

    private void showPlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (getHandle().playerConnection == null) return;
        if (equals(player)) return;

        var hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins == null) {
            return; // Player isn't hidden
        }
        hidingPlugins.remove(getPluginWeakReference(plugin));
        if (!hidingPlugins.isEmpty()) {
            return; // Some other plugins still want the player hidden
        }
        hiddenPlayers.remove(player.getUniqueId());

        // Paper start
        var other = ((CraftPlayer) player).getHandle();
        registerPlayer(other);
    }
    private void registerPlayer(EntityPlayer other) {
        var tracker = ((WorldServer) entity.world).getChunkProvider().playerChunkMap;
        // Paper end

        var handle = getHandle();
        handle.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, other));

        var entry = tracker.trackedEntities.get(other.getId());
        if (entry != null && !entry.trackedPlayers.contains(handle)) {
            entry.updatePlayer(handle);
        }
    }
    // Paper start
    private void reregisterPlayer(EntityPlayer player) {
        if (!hiddenPlayers.containsKey(player.getUniqueID())) {
            unregisterPlayer(player);
            registerPlayer(player);
        }
    }
    public void setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile profile) {
        var self = getHandle();
        self.setProfile(com.destroystokyo.paper.profile.CraftPlayerProfile.asAuthlibCopy(profile));
        if (!self.sentListPacket) {
            return;
        }
        var players = server.getServer().getPlayerList().players;
        for (var player : players) {
            player.getBukkitEntity().reregisterPlayer(self);
        }
        refreshPlayer();
    }
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return new com.destroystokyo.paper.profile.CraftPlayerProfile(this).clone();
    }

    private void refreshPlayer() {
        var handle = getHandle();

        var loc = getLocation();

        var connection = handle.playerConnection;
        reregisterPlayer(handle);

        //Respawn the player then update their position and selected slot
        var worldserver = handle.getWorldServer();
        connection.grim$E$noReminderTicks = 0;
        connection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutRespawn(worldserver.getDimensionManager(), worldserver.getDimensionKey(), BiomeManager.a(worldserver.getSeed()), handle.playerInteractManager.getGameMode(), handle.playerInteractManager.c(), worldserver.isDebugWorld(), worldserver.isFlatWorld(), true));
        handle.updateAbilities();
        connection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), new HashSet<>(), 0));
        net.minecraft.server.MinecraftServer.SERVER.getPlayerList().updateClient(handle);

        if (this.isOp()) {
            this.setOp(false);
            this.setOp(true);
        }
    }
    // Paper end

    public void removeDisconnectingPlayer(Player player) {
        hiddenPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canSee(Player player) {
        // Purpur start
        return canSee(player.getUniqueId());
    }

    public boolean canSee(UUID uuid) {
        return !hiddenPlayers.containsKey(uuid);
        // Purpur end
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public void setHandle(EntityPlayer entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return hash;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    // Paper start
    @Override
    public long getLastLogin() {
        return getHandle().loginTime;
    }

    @Override
    public long getLastSeen() {
        return isOnline() ? SystemUtils.getMonotonicMillis() : this.lastSaveTime;
    }
    // Paper end

    public void readExtraData(NBTTagCompound nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            var data = nbttagcompound.getCompound("bukkit");

            if (data.hasKey("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.hasKey("newExp")) {
                var handle = getHandle();
                handle.newExp = data.getInt("newExp");
                handle.newTotalExp = data.getInt("newTotalExp");
                handle.newLevel = data.getInt("newLevel");
                handle.expToDrop = data.getInt("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(NBTTagCompound nbttagcompound) {
        this.lastSaveTime = SystemUtils.getMonotonicMillis(); // Paper

        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.set("bukkit", new NBTTagCompound());
        }

        var data = nbttagcompound.getCompound("bukkit");
        var handle = getHandle();
        data.setInt("newExp", handle.newExp);
        data.setInt("newTotalExp", handle.newTotalExp);
        data.setInt("newLevel", handle.newLevel);
        data.setInt("expToDrop", handle.expToDrop);
        data.setBoolean("keepLevel", handle.keepLevel);
        data.setLong("firstPlayed", getFirstPlayed());
        data.setLong("lastPlayed", SystemUtils.getMonotonicMillis());
        data.setString("lastKnownName", handle.getName());

        // Paper start - persist for use in offline save data
        if (!nbttagcompound.hasKey("Paper")) {
            nbttagcompound.set("Paper", new NBTTagCompound());
        }

        var paper = nbttagcompound.getCompound("Paper");
        paper.setLong("LastLogin", handle.loginTime);
        paper.setLong("LastSeen", SystemUtils.getMonotonicMillis());
        // Paper end
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        if (channels.contains(channel)) {
            channel = StandardMessenger.validateAndCorrectChannel(channel);
            var packet = new PacketPlayOutCustomPayload(new MinecraftKey(channel), new PacketDataSerializer(Unpooled.wrappedBuffer(message)));
            handle.playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().setResourcePack(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        Validate.notNull(hash, "Resource pack hash cannot be null");
        Validate.isTrue(hash.length == 20, "Resource pack hash should be 20 bytes long but was " + hash.length);

        getHandle().setResourcePack(url, BaseEncoding.base16().lowerCase().encode(hash));
    }

    public void addChannel(String channel) {
        Preconditions.checkState(DISABLE_CHANNEL_LIMIT || channels.size() < 128, "Cannot register channel '%s'. Too many channels registered!", channel); // Paper - flag to disable channel limit
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        var handle = getHandle();
        if (handle.playerConnection == null) return;
        var listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            var stream = new ByteArrayOutputStream();

            for (var channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            handle.playerConnection.sendPacket(new PacketPlayOutCustomPayload(new MinecraftKey("register"), new PacketDataSerializer(Unpooled.wrappedBuffer(stream.toByteArray()))));
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        var handle = getHandle();
        var container = handle.activeContainer;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        handle.setContainerData(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return getHandle().abilities.isFlying;
    }

    @Override
    public void setFlying(boolean value) {
        var handle = getHandle();
        var needsUpdate = handle.abilities.isFlying != value; // Paper - Only refresh abilities if needed
        if (!getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }

        handle.abilities.isFlying = value;
        if (needsUpdate) handle.updateAbilities(); // Paper - Only refresh abilities if needed
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().abilities.canFly;
    }

    @Override
    public void setAllowFlight(boolean value) {
        var handle = getHandle();
        if (isFlying() && !value) {
            handle.abilities.isFlying = false;
        }

        handle.abilities.canFly = value;
        if (!handle.abilities.isFlying) {
            handle.updateAbilities();
        }
    }

    @Override
    public int getNoDamageTicks() {
        var handle = getHandle();
        if (handle.invulnerableTicks > 0) {
            return Math.max(handle.invulnerableTicks, handle.noDamageTicks);
        } else {
            return handle.noDamageTicks;
        }
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        super.setNoDamageTicks(ticks);
        getHandle().invulnerableTicks = ticks; // SPIGOT-5921: Update both for players, like the getter above
    }

    @Override
    public void setFlySpeed(float value) {
        validateSpeed(value);
        var player = getHandle();
        player.abilities.flySpeed = value / 2f;
        player.updateAbilities();

    }

    @Override
    public void setWalkSpeed(float value) {
        validateSpeed(value);
        var player = getHandle();
        player.abilities.walkSpeed = value / 2f;
        player.updateAbilities();
        player.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(player.abilities.walkSpeed); // SPIGOT-5833: combination of the two in 1.16+
    }

    @Override
    public float getFlySpeed() {
        return getHandle().abilities.flySpeed * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return getHandle().abilities.walkSpeed * 2f;
    }

    private void validateSpeed(float value) {
        if (value < 0) {
            if (value < -1f) {
                throw new IllegalArgumentException(value + " is too low");
            }
        } else {
            if (value > 1f) {
                throw new IllegalArgumentException(value + " is too high");
            }
        }
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = amount;
        getHandle().triggerHealthUpdate();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().triggerHealthUpdate();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        var playerConnection = getHandle().playerConnection;
        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (playerConnection.isDisconnected()) {
            // throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer"); // Spigot - remove this as Mojang's semi asynchronous Netty implementation can lead to races
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
    }

    @Override
    public double getHealth() {
        return health;
    }

    public void setRealHealth(double health) {
        if (Double.isNaN(health)) {return;} // Paper
        this.health = health;
    }

    public void updateScaledHealth() {
        updateScaledHealth(true);
    }

    public void updateScaledHealth(boolean sendHealth) {
        var handle = getHandle();
        var attributemapserver = handle.getAttributeMap();
        var set = attributemapserver.b(); // PAIL: Rename

        injectScaledMaxHealth(set, true);

        // SPIGOT-3813: Attributes before health
        if (handle.playerConnection != null) {
            handle.playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(handle.getId(), set));
            if (sendHealth) {
                sendHealthUpdate();
            }
        }
        handle.getDataWatcher().set(EntityLiving.HEALTH, getScaledHealth());

        handle.maxHealthCache = getMaxHealth();
    }

    public void sendHealthUpdate() {
        var handle = getHandle();
        // Paper start - cancellable death event
        //handle.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(getScaledHealth(), handle.getFoodData().getFoodLevel(), handle.getFoodData().getSaturationLevel()));
        var packet = new PacketPlayOutUpdateHealth(getScaledHealth(), handle.getFoodData().getFoodLevel(), handle.getFoodData().getSaturationLevel());
        if (handle.queueHealthUpdatePacket) {
            handle.queuedHealthUpdatePacket = packet;
        } else {
            handle.playerConnection.sendPacket(packet);
        }
        // Paper end
    }

    public void injectScaledMaxHealth(Collection<AttributeModifiable> collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (var genericInstance : collection) {
            if (genericInstance.getAttribute() == GenericAttributes.MAX_HEALTH) {
                collection.remove(genericInstance);
                break;
            }
        }
        var dummy = new AttributeModifiable(GenericAttributes.MAX_HEALTH, (attribute) -> { });
        // Spigot start
        var healthMod = scaledHealth ? healthScale : getMaxHealth();
        if ( healthMod >= Float.MAX_VALUE || healthMod <= 0 )
        {
            healthMod = 20; // Reset health
            getServer().getLogger().warning( getName() + " tried to crash the server with a large health attribute" );
        }
        dummy.setValue(healthMod);
        // Spigot end
        collection.add(dummy);
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        var handle = getHandle();
        var followed = handle.getSpecatorTarget();
        return followed == handle ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().setSpectatorTarget((entity == null) ? null : ((CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        var times = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        var handle = getHandle();
        handle.playerConnection.sendPacket(times);

        if (title != null) {
            var packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, CraftChatMessage.fromStringOrNull(title));
            handle.playerConnection.sendPacket(packetTitle);
        }

        if (subtitle != null) {
            var packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, CraftChatMessage.fromStringOrNull(subtitle));
            handle.playerConnection.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        var packetReset = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
        getHandle().playerConnection.sendPacket(packetReset);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        var packetplayoutworldparticles = new PacketPlayOutWorldParticles(CraftParticle.toNMS(particle, data), true, x, y, z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count); // Paper - Fix x/y/z coordinate precision loss
        var handle = getHandle();
        handle.playerConnection.sendPacket(packetplayoutworldparticles);
    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");
        var handle = getHandle();

        var craft = (CraftAdvancement) advancement;
        var data = handle.getAdvancementData();
        var progress = data.getProgress(craft.getHandle());

        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public int getClientViewDistance() {
        var handle = getHandle();
        return (handle.clientViewDistance == null) ? Bukkit.getViewDistance() : handle.clientViewDistance;
    }

    // Paper start
    @Override
    public java.util.Locale locale() {
        return getHandle().adventure$locale;
    }
    // Paper end
    @Override
    public int getPing() {
        return getHandle().ping;
    }

    @Override
    public String getLocale() {
        // Paper start - Locale change event
        var locale = getHandle().locale;
        return locale != null ? locale : "en_us";
        // Paper end
    }

    // Paper start
    public void setAffectsSpawning(boolean affects) {
        this.getHandle().affectsSpawning = affects;
    }

    @Override
    public boolean getAffectsSpawning() {
        return this.getHandle().affectsSpawning;
    }

    @Override
    public void setResourcePack(String url, String hash) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        Validate.notNull(hash, "Hash cannot be null");
        this.getHandle().setResourcePack(url, hash);
    }

    @Override
    public org.bukkit.event.player.PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
        return this.resourcePackStatus;
    }

    @Override
    public String getResourcePackHash() {
        return this.resourcePackHash;
    }

    @Override
    public boolean hasResourcePack() {
        return this.resourcePackStatus == org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED;
    }

    public void setResourcePackStatus(org.bukkit.event.player.PlayerResourcePackStatusEvent.Status status) {
        this.resourcePackStatus = status;
    }
    // Paper end

    @Override
    public void updateCommands() {
        var handle = getHandle();
        if (handle.playerConnection == null) return;

        handle.server.getCommandDispatcher().a(handle);
    }

    @Override
    public void openBook(ItemStack book) {
        Validate.isTrue(book != null, "book == null");
        Validate.isTrue(book.getType() == Material.WRITTEN_BOOK, "Book must be Material.WRITTEN_BOOK");

        var hand = getInventory().getItemInMainHand();
        getInventory().setItemInMainHand(book);
        getHandle().openBook(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(book), net.minecraft.world.EnumHand.MAIN_HAND);
        getInventory().setItemInMainHand(hand);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component displayName() {
        return this.getHandle().adventure$displayName;
    }

    @Override
    public void displayName(net.kyori.adventure.text.Component displayName) {
        var handle = getHandle();
        handle.adventure$displayName = displayName != null ? displayName : net.kyori.adventure.text.Component.text(this.getName());
        handle.displayName = null;
    }

    @Override
    public void sendMessage(net.kyori.adventure.identity.Identity identity, net.kyori.adventure.text.Component message, net.kyori.adventure.audience.MessageType type) {
        var handle = getHandle();
        if (handle.playerConnection == null) return;
        var packet = new PacketPlayOutChat(
          null,
          type == net.kyori.adventure.audience.MessageType.CHAT
            ? net.minecraft.network.chat.ChatMessageType.CHAT
            : net.minecraft.network.chat.ChatMessageType.SYSTEM,
          identity.uuid()
        );
        packet.adventure$message = message;
        handle.playerConnection.sendPacket(packet);
    }

    @Override
    public void sendActionBar(net.kyori.adventure.text.Component message) {
        var packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, null);
        packet.adventure$text = message;
        this.getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendPlayerListHeader(net.kyori.adventure.text.Component header) {
        this.playerListHeader = header;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListFooter(net.kyori.adventure.text.Component footer) {
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListHeaderAndFooter(net.kyori.adventure.text.Component header, net.kyori.adventure.text.Component footer) {
        this.playerListHeader = header;
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    private void adventure$sendPlayerListHeaderAndFooter() {
        var connection = this.getHandle().playerConnection;
        if (connection == null) return;
        var packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.adventure$header = (this.playerListHeader == null) ? net.kyori.adventure.text.Component.empty() : this.playerListHeader;
        packet.adventure$footer = (this.playerListFooter == null) ? net.kyori.adventure.text.Component.empty() : this.playerListFooter;
        connection.sendPacket(packet);
    }

    @Override
    public void showTitle(net.kyori.adventure.title.Title title) {
        var connection = this.getHandle().playerConnection;
        var times = title.times();
        if (times != null) {
            connection.sendPacket(new PacketPlayOutTitle(ticks(times.fadeIn()), ticks(times.stay()), ticks(times.fadeOut())));
        }
        var sp = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, null);
        sp.adventure$text = title.subtitle();
        connection.sendPacket(sp);
        var tp = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, null);
        tp.adventure$text = title.title();
        connection.sendPacket(tp);
    }

    private static int ticks(java.time.Duration duration) {
        if (duration == null) {
            return -1;
        }
        return (int) (duration.toMillis() / 50L);
    }

    @Override
    public void clearTitle() {
        this.getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null));
    }

    // resetTitle implemented above

    @Override
    public void showBossBar(net.kyori.adventure.bossbar.BossBar bar) {
        ((net.kyori.adventure.bossbar.HackyBossBarPlatformBridge) bar).paper$playerShow(this);
    }

    @Override
    public void hideBossBar(net.kyori.adventure.bossbar.BossBar bar) {
        ((net.kyori.adventure.bossbar.HackyBossBarPlatformBridge) bar).paper$playerHide(this);
    }

    @Override
    public void playSound(net.kyori.adventure.sound.Sound sound) {
        var handle = getHandle();
        var pos = handle.getPositionVector();
        this.playSound(sound, pos.x, pos.y, pos.z);
    }

    @Override
    public void playSound(net.kyori.adventure.sound.Sound sound, double x, double y, double z) {
        var name = io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.name());
        var event = net.minecraft.core.IRegistry.SOUND_EVENT.getOptional(name);
        var handle = getHandle();
        if (event.isPresent()) {
            handle.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(event.get(), io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.source()), x, y, z, sound.volume(), sound.pitch()));
        } else {
            handle.playerConnection.sendPacket(new PacketPlayOutCustomSoundEffect(name, io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.source()), new Vec3D(x, y, z), sound.volume(), sound.pitch()));
        }
    }

    @Override
    public void stopSound(net.kyori.adventure.sound.SoundStop stop) {
        this.getHandle().playerConnection.sendPacket(new PacketPlayOutStopSound(
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.sound()),
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.source())
        ));
    }

    @Override
    public void openBook(net.kyori.adventure.inventory.Book book) {
        var handle = getHandle();
        var locale = handle.adventure$locale;
        var item = io.papermc.paper.adventure.PaperAdventure.asItemStack(book, locale);
        var player = handle;
        var connection = player.playerConnection;
        var inventory = player.inventory;
        var slot = inventory.items.size() + inventory.itemInHandIndex;
        connection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutSetSlot(0, slot, item));
        connection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutOpenBook(net.minecraft.world.EnumHand.MAIN_HAND));
        connection.sendPacket(new net.minecraft.network.protocol.game.PacketPlayOutSetSlot(0, slot, inventory.getItemInHand()));
    }

    public float getCooldownPeriod() {
        return getHandle().getCooldownPeriod();
    }

    public float getCooledAttackStrength(float adjustTicks) {
        return getHandle().getAttackCooldown(adjustTicks);
    }

    public void resetCooldown() {
        getHandle().resetAttackCooldown();
    }

    @Override
    public void remove() {
        if (this.getHandle().getClass().equals(EntityPlayer.class)) { // special case for NMS plugins inheriting
            throw new UnsupportedOperationException("Calling Entity#remove on players produces undefined (bad) behavior");
        } else {
            super.remove();
        }
    }

    // Tuinity start - implement view distances
    @Override
    public int getSendViewDistance() {
        var handle = getHandle();
        var chunkMap = handle.getWorldServer().getChunkProvider().playerChunkMap;
        var data = chunkMap.playerChunkManager.getData(handle);
        if (data == null) {
            return chunkMap.playerChunkManager.getTargetSendDistance();
        }
        return data.getTargetSendViewDistance();
    }

    @Override
    public void setSendViewDistance(int viewDistance) {
        var handle = getHandle();
        var chunkMap = handle.getWorldServer().getChunkProvider().playerChunkMap;
        var data = chunkMap.playerChunkManager.getData(handle);
        if (data == null) {
            throw new IllegalStateException("Player is not attached to world");
        }

        data.setTargetSendViewDistance(viewDistance);
    }

    @Override
    public int getNoTickViewDistance() {
        var handle = getHandle();
        var chunkMap = handle.getWorldServer().getChunkProvider().playerChunkMap;
        var data = chunkMap.playerChunkManager.getData(handle);
        if (data == null) {
            return chunkMap.playerChunkManager.getTargetNoTickViewDistance();
        }
        return data.getTargetNoTickViewDistance();
    }

    @Override
    public void setNoTickViewDistance(int viewDistance) {
        var handle = getHandle();
        var chunkMap = handle.getWorldServer().getChunkProvider().playerChunkMap;
        var data = chunkMap.playerChunkManager.getData(handle);
        if (data == null) {
            throw new IllegalStateException("Player is not attached to world");
        }

        data.setTargetNoTickViewDistance(viewDistance);
    }

    @Override
    public int getViewDistance() {
        return PurpurConfig.hardcodedViewDistance;
    }

    @Override
    public void setViewDistance(int viewDistance) {}
    // Tuinity end - implement view distances

    @Override
    public <T> T getClientOption(ClientOption<T> type) {
        var handle = getHandle();
        if(ClientOption.SKIN_PARTS.equals(type)) {
            return type.getType().cast(new PaperSkinParts(handle.getDataWatcher().get(EntityHuman.getSkinPartsWatcher())));
        } else if(ClientOption.CHAT_COLORS_ENABLED.equals(type)) {
            return type.getType().cast(handle.hasChatColorsEnabled());
        } else if(ClientOption.CHAT_VISIBILITY.equals(type)) {
            return type.getType().cast(handle.getChatFlags() == null ? ChatVisibility.UNKNOWN : ChatVisibility.valueOf(handle.getChatFlags().name()));
        } else if(ClientOption.LOCALE.equals(type)) {
            return type.getType().cast(getLocale());
        } else if(ClientOption.MAIN_HAND.equals(type)) {
            return type.getType().cast(getMainHand());
        } else if(ClientOption.VIEW_DISTANCE.equals(type)) {
            return type.getType().cast(getClientViewDistance());
        }
        throw new RuntimeException("Unknown settings type");
    }

    @Override
    public org.bukkit.entity.Firework boostElytra(ItemStack firework) {
        Validate.isTrue(isGliding(), "Player must be gliding");
        Validate.isTrue(firework != null, "firework == null");
        Validate.isTrue(firework.getType() == Material.FIREWORK_ROCKET, "Firework must be Material.FIREWORK_ROCKET");

        var item = org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(firework);
        World world = ((CraftWorld) getWorld()).getHandle();
        var entity = new EntityFireworks(world, item, getHandle());
        return world.addEntity(entity)
            ? (org.bukkit.entity.Firework) entity.getBukkitEntity()
            : null;
    }

    @Override
    public void sendOpLevel(byte level) {
        Preconditions.checkArgument(level >= 0 && level <= 4, "Level must be within [0, 4]");
        var handle = getHandle();
        handle.getMinecraftServer().getPlayerList().sendPlayerOperatorStatus(handle, level, false);
    }
    // Paper end

    // Paper start
    @Override
    public Set<Player> getTrackedPlayers() {
        if (entity.tracker == null) {
            return Collections.emptySet();
        }

        Set<Player> set = new HashSet<>(entity.tracker.trackedPlayers.size());
        for (var entityPlayer : entity.tracker.trackedPlayers) {
            set.add(entityPlayer.getBukkitEntity().getPlayer());
        }
        return set;
    }
    // Paper end

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot()
    {

        @Override
        public InetSocketAddress getRawAddress()
        {
            return (InetSocketAddress) getHandle().playerConnection.networkManager.getRawAddress();
        }

        @Override
        public boolean getCollidesWithEntities() {
            return CraftPlayer.this.isCollidable();
        }

        @Override
        public void setCollidesWithEntities(boolean collides) {
            CraftPlayer.this.setCollidable(collides);
        }

        @Override
        public void respawn()
        {
            if ( getHealth() <= 0 && isOnline() )
            {
                server.getServer().getPlayerList().moveToWorld( getHandle(), false );
            }
        }

        @Override
        public Set<Player> getHiddenPlayers()
        {
            Set<Player> ret = new HashSet<Player>();
            for (var u : hiddenPlayers.keySet() )
            {
                ret.add( getServer().getPlayer( u ) );
            }

            return java.util.Collections.unmodifiableSet( ret );
        }

        @Override
        public void sendMessage(BaseComponent component) {
          sendMessage( new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(BaseComponent... components) {
            var handle = getHandle();
           if ( handle.playerConnection == null ) return;

            var packet = new PacketPlayOutChat(null, net.minecraft.network.chat.ChatMessageType.SYSTEM, SystemUtils.b);
            packet.components = components;
            handle.playerConnection.sendPacket(packet);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent component) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, component);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent... components) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, components);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent component) {
            sendMessage( position, new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent... components) {
            var handle = getHandle();
            if ( handle.playerConnection == null ) return;

            var packet = new PacketPlayOutChat(null, net.minecraft.network.chat.ChatMessageType.a((byte) position.ordinal()), SystemUtils.b);
            packet.components = components;
            handle.playerConnection.sendPacket(packet);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent component) {
            sendMessage( position, sender, new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent... components) {
            var handle = getHandle();
            if ( handle.playerConnection == null ) return;

            var packet = new PacketPlayOutChat(null, net.minecraft.network.chat.ChatMessageType.a((byte) position.ordinal()), sender == null ? SystemUtils.b : sender);
            packet.components = components;
            handle.playerConnection.sendPacket(packet);
        }

        // Paper start
        @Override
        public int getPing()
        {
            return getHandle().ping;
        }
        // Paper end
    };

    // Paper start - brand support
    @Override
    public String getClientBrandName() {
        var handle = getHandle();
        return handle.playerConnection != null ? handle.playerConnection.getClientBrandName() : null;
    }
    // Paper end

    public Player.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end

    // Purpur start
    @Override
    public boolean isAfk() {
        return getHandle().isAfk();
    }

    @Override
    public void setAfk(boolean setAfk) {
        getHandle().setAfk(setAfk);
    }

    @Override
    public void resetIdleTimer() {
        getHandle().resetIdleTimer();
    }

    @Override
    public boolean isSpawnInvulnerable() {
        return getHandle().isSpawnInvulnerable();
    }

    @Override
    public int getSpawnInvulnerableTicks() {
        return getHandle().invulnerableTicks;
    }

    @Override
    public void setSpawnInvulnerableTicks(int invulnerableTicks) {
        getHandle().invulnerableTicks = invulnerableTicks;
    }

    @Override
    public void setFlyingFallDamage(boolean flyingFallDamage) {
        getHandle().flyingFallDamage = flyingFallDamage;
    }

    @Override
    public boolean hasFlyingFallDamage() {
        return getHandle().flyingFallDamage;
    }
    // Purpur end
}
