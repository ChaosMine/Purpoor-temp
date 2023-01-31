package org.bukkit.craftbukkit;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.storage.WorldNBTStorage;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SerializableAs("Player")
public class CraftOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {
    private final GameProfile profile;
    private final CraftServer server;
    private final WorldNBTStorage storage;

    protected CraftOfflinePlayer(CraftServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = server.console.worldNBTStorage;
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public boolean isOnline() {
        return getPlayer() != null;
    }

    @Override
    public String getName() {
        Player player = getPlayer();
        if (player != null) {
            return player.getName();
        }

        // This might not match lastKnownName but if not it should be more correct
        if (profile.getName() != null) {
            return profile.getName();
        }

        NBTTagCompound data = getBukkitData();

        if (data != null) {
            if (data.hasKey("lastKnownName")) {
                return data.getString("lastKnownName");
            }
        }

        return null;
    }

    @Override
    public UUID getUniqueId() {
        return profile.getId();
    }

    public Server getServer() {
        return server;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {}

    @Override
    public boolean isBanned() {
        throw new UnsupportedOperationException();
    }

    public void setBanned(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWhitelisted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("UUID", profile.getId().toString());

        return result;
    }

    public static OfflinePlayer deserialize(Map<String, Object> args) {
        // Backwards comparability
        if (args.get("name") != null) {
            return Bukkit.getServer().getOfflinePlayer((String) args.get("name"));
        }

        return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String) args.get("UUID")));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[UUID=" + profile.getId() + "]";
    }

    @Override
    public Player getPlayer() {
        return server.getPlayer(getUniqueId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer other)) {
            return false;
        }

        return this.getUniqueId().equals(other.getUniqueId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.getUniqueId().hashCode();
        return hash;
    }

    private NBTTagCompound getData() {
        return storage.getPlayerData(getUniqueId().toString());
    }

    private NBTTagCompound getBukkitData() {
        NBTTagCompound result = getData();

        if (result != null) {
            if (!result.hasKey("bukkit")) {
                result.set("bukkit", new NBTTagCompound());
            }
            result = result.getCompound("bukkit");
        }

        return result;
    }

    private File getDataFile() {
        return new File(storage.getPlayerDir(), getUniqueId() + ".dat");
    }

    @Override
    public long getFirstPlayed() {
        Player player = getPlayer();
        if (player != null) return player.getFirstPlayed();

        NBTTagCompound data = getBukkitData();

        if (data != null) {
            if (data.hasKey("firstPlayed")) {
                return data.getLong("firstPlayed");
            } else {
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getLastPlayed() {
        Player player = getPlayer();
        if (player != null) return player.getLastPlayed();

        NBTTagCompound data = getBukkitData();

        if (data != null) {
            if (data.hasKey("lastPlayed")) {
                return data.getLong("lastPlayed");
            } else {
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasPlayedBefore() {
        return getData() != null;
    }

    // Paper start
    @Override
    public long getLastLogin() {
        Player player = getPlayer();
        if (player != null) return player.getLastLogin();

        NBTTagCompound data = getPaperData();

        if (data != null) {
            if (data.hasKey("LastLogin")) {
                return data.getLong("LastLogin");
            } else {
                // if the player file cannot provide accurate data, this is probably the closest we can approximate
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getLastSeen() {
        Player player = getPlayer();
        if (player != null) return player.getLastSeen();

        NBTTagCompound data = getPaperData();

        if (data != null) {
            if (data.hasKey("LastSeen")) {
                return data.getLong("LastSeen");
            } else {
                // if the player file cannot provide accurate data, this is probably the closest we can approximate
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    private NBTTagCompound getPaperData() {
        NBTTagCompound result = getData();

        if (result != null) {
            if (!result.hasKey("Paper")) {
                result.set("Paper", new NBTTagCompound());
            }
            result = result.getCompound("Paper");
        }

        return result;
    }
    // Paper end

    @Override
    public Location getBedSpawnLocation() {
        NBTTagCompound data = getData();
        if (data == null) return null;

        if (data.hasKey("SpawnX") && data.hasKey("SpawnY") && data.hasKey("SpawnZ")) {
            String spawnWorld = data.getString("SpawnWorld");
            if (spawnWorld.equals("")) {
                spawnWorld = server.getWorlds().get(0).getName();
            }
            return new Location(server.getWorld(spawnWorld), data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"));
        }
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue metadataValue) {
        server.getPlayerMetadata().setMetadata(this, metadataKey, metadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin plugin) {
        server.getPlayerMetadata().removeMetadata(this, metadataKey, plugin);
    }

    @Override
    public void incrementStatistic(Statistic statistic) {}

    @Override
    public void decrementStatistic(Statistic statistic) {}

    @Override
    public int getStatistic(Statistic statistic) {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {}

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {}

    @Override
    public void setStatistic(Statistic statistic, int newValue) {}

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {}

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {}

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {}

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {}

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {}

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {}

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {}

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {}

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {}

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {}
}
