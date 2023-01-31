package net.minecraft.server.dedicated;

import joptsimple.OptionSet;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.levelgen.GeneratorSettings;

import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
// CraftBukkit end

public class DedicatedServerProperties extends PropertyManager<DedicatedServerProperties> {
	public final boolean debug = this.getBoolean("debug", false); // CraftBukkit
	public final boolean onlineMode = this.getBoolean("online-mode", true);
	public final boolean preventProxyConnections = this.getBoolean("prevent-proxy-connections", false);
	public final String serverIp = this.getString("server-ip", "");
	public final String serverName = this.getString("server-name", "Unknown Server"); // Purpur
	public final boolean spawnAnimals = this.getBoolean("spawn-animals", true);
	public final boolean spawnNpcs = this.getBoolean("spawn-npcs", true);
	public final boolean pvp = this.getBoolean("pvp", true);
	public final boolean allowFlight = this.getBoolean("allow-flight", false);
	public final String resourcePack = this.getString("resource-pack", "");
	public final String motd = this.getString("motd", "A Minecraft Server");
	public final EnumDifficulty difficulty;
	public final EnumGamemode gamemode;
	public final String levelName;
	public final int serverPort;
	public final int maxBuildHeight;
	public final Boolean announcePlayerAchievements;
	public final String resourcePackHash;
	public final String resourcePackSha1;
	public final boolean hardcore;
	public final boolean allowNether;
	public final boolean spawnMonsters;
	public final boolean enableCommandBlock;
	public final int opPermissionLevel;
	public final int functionPermissionLevel;
	public final long maxTickTime;
	public final int viewDistance;
	public final int maxPlayers;
	public final int networkCompressionThreshold;
	public final int maxWorldSize;
	public final boolean syncChunkWrites;
	public final boolean enableStatus;
	public final int entityBroadcastRangePercentage;
	public final String textFilteringConfig;
	public final GeneratorSettings generatorSettings;

	// CraftBukkit start
	public DedicatedServerProperties(Properties properties, IRegistryCustom iregistrycustom, OptionSet optionset) {
		super(properties, optionset);
		// CraftBukkit end
		this.difficulty = this.a(
			"difficulty",
			a(EnumDifficulty::getById, EnumDifficulty::a),
			EnumDifficulty::c,
			EnumDifficulty.EASY
		);
		this.gamemode = this.a(
			"gamemode",
			a(EnumGamemode::getById, EnumGamemode::a),
			EnumGamemode::b,
			EnumGamemode.SURVIVAL
		);
		this.levelName = this.getString("level-name", "world");
		this.serverPort = this.getInt("server-port", 25565);
		this.maxBuildHeight = this.a(
			"max-build-height",
			val -> MathHelper.clamp((val + 8) / 16 * 16, 64, 256),
			256
		);
		this.announcePlayerAchievements = this.b("announce-player-achievements");
		this.resourcePackHash = this.a("resource-pack-hash");
		this.resourcePackSha1 = this.getString("resource-pack-sha1", "");
		this.hardcore = this.getBoolean("hardcore", false);
		this.allowNether = this.getBoolean("allow-nether", true);
		this.spawnMonsters = this.getBoolean("spawn-monsters", true);
		this.enableCommandBlock = this.getBoolean("enable-command-block", false);
		this.opPermissionLevel = this.getInt("op-permission-level", 4);
		this.functionPermissionLevel = this.getInt("function-permission-level", 2);
		this.maxTickTime = this.getLong("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
		this.viewDistance = this.getInt("view-distance", 10);
		this.maxPlayers = this.getInt("max-players", 20);
		this.networkCompressionThreshold = this.getInt("network-compression-threshold", 256);
		this.maxWorldSize = this.a("max-world-size", sz -> MathHelper.clamp(sz, 1, 29999984), 29999984);
		this.syncChunkWrites = this.getBoolean("sync-chunk-writes", true) && Boolean.getBoolean("Paper.enable-sync-chunk-writes"); // Paper - hide behind flag
		this.enableStatus = this.getBoolean("enable-status", true);
		this.entityBroadcastRangePercentage = this.a("entity-broadcast-range-percentage", range -> MathHelper.clamp(range, 10, 1000), 100);
		this.textFilteringConfig = this.getString("text-filtering-config", "");
		this.generatorSettings = GeneratorSettings.a(iregistrycustom, properties);
	}

	public static DedicatedServerProperties load(IRegistryCustom iregistrycustom, Path path, OptionSet optionset) {
		return new DedicatedServerProperties(loadPropertiesFile(path), iregistrycustom, optionset);
	}

	@Override
	protected DedicatedServerProperties reload(IRegistryCustom iregistrycustom, Properties properties, OptionSet optionset) {
		return new DedicatedServerProperties(properties, iregistrycustom, optionset);
	}
}
