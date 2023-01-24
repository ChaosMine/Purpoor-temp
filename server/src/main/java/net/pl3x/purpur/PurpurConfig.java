package net.pl3x.purpur;

import com.destroystokyo.paper.PaperConfig;
import com.google.common.base.Throwables;
import net.minecraft.locale.LocaleLanguage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.pl3x.purpur.command.PurpurCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PurpurConfig {
    private static final String HEADER = "This is the main configuration file for Purpur.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Purpur,\n"
            + "join us in our Discord guild.\n"
            + "\n"
            + "Website: https://github.com/pl3xgaming/Purpur \n"
            + "Wiki: https://github.com/pl3xgaming/Purpur/wiki \n";
    private static File CONFIG_FILE;
    public static YamlConfiguration config;

    private static Map<String, Command> commands;

    static int version;
    static boolean verbose;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load purpur.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);
        verbose = getBoolean("verbose", false);

        commands = new HashMap<>();
        commands.put("purpur", new PurpurCommand("purpur"));

        version = getInt("config-version", 13);
        set("config-version", 13);

        readConfig(PurpurConfig.class, null);
    }

    protected static void log(String s) {
        if (verbose) {
            log(Level.INFO, s);
        }
    }

    protected static void log(Level level, String s) {
        Bukkit.getLogger().log(level, s);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Purpur", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.addDefault(path, val);
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static void migrateDisableProjectileSaving() {
        if (PurpurConfig.version < 6) {
            final boolean saveProjectilesToDisk = getBoolean("world-settings.default.gameplay-mechanics.save-projectiles-to-disk", true);
            set("world-settings.default.gameplay-mechanics.save-projectiles-to-disk", null);
            if (!saveProjectilesToDisk) {
                PaperConfig.config.set("world-settings.default.projectile-load-save-per-chunk-limit", 0);
                PaperConfig.saveConfig();
            }
        }
    }

    private static void migrateClimbingCrammingFix() {
        if (PurpurConfig.version < 7) {
            final boolean climbingCrammingFix = getBoolean("world-settings.default.gameplay-mechanics.fix-climbing-bypassing-cramming-rule", false);
            set("world-settings.default.gameplay-mechanics.fix-climbing-bypassing-cramming-rule", null);
            if (climbingCrammingFix) {
                PaperConfig.config.set("world-settings.default.fix-climbing-bypassing-cramming-rule", true);
                PaperConfig.saveConfig();
            }
        }
    }

    public static String afkBroadcastAway = "§e§o%s is now AFK";
    public static String afkBroadcastBack = "§e§o%s is no longer AFK";
    public static String afkTabListPrefix = "[AFK] ";
    public static String afkTabListSuffix = "";
    public static String demoCommandOutput = "§a%s has been shown the demo screen";
    public static String creditsCommandOutput = "§a%s has been shown the end credits";
    public static String pingCommandOutput = "§a%s's ping is %sms";
    public static String cannotRideMob = "§cYou cannot mount that mob";
    public static String unverifiedUsername = "default";
    private static void messages() {
        afkBroadcastAway = getString("settings.messages.afk-broadcast-away", afkBroadcastAway);
        afkBroadcastBack = getString("settings.messages.afk-broadcast-back", afkBroadcastBack);
        afkTabListPrefix = getString("settings.messages.afk-tab-list-prefix", afkTabListPrefix);
        afkTabListSuffix = getString("settings.messages.afk-tab-list-suffix", afkTabListSuffix);
        demoCommandOutput = getString("settings.messages.demo-command-output", demoCommandOutput);
        creditsCommandOutput = getString("settings.messages.credits-command-output", creditsCommandOutput);
        pingCommandOutput = getString("settings.messages.ping-command-output", pingCommandOutput);
        cannotRideMob = getString("settings.messages.cannot-ride-mob", cannotRideMob);
        unverifiedUsername = getString("settings.messages.unverified-username", unverifiedUsername);
    }

    public static int dungeonSeed = -1;
    private static void seedSettings() {
        dungeonSeed = getInt("settings.seed.dungeon", dungeonSeed);
    }

    public static String serverModName = "Purpur";
    private static void serverModName() {
        serverModName = getString("settings.server-mod-name", serverModName);
    }

    public static double laggingThreshold = 19.0D;
    private static void tickLoopSettings() {
        laggingThreshold = getDouble("settings.lagging-threshold", laggingThreshold);
    }

    public static boolean useAlternateKeepAlive = false;
    private static void useAlternateKeepAlive() {
        useAlternateKeepAlive = getBoolean("settings.use-alternate-keepalive", useAlternateKeepAlive);
    }

    public static boolean disableGiveCommandDrops = false;
    private static void disableGiveCommandDrops() {
        disableGiveCommandDrops = getBoolean("settings.disable-give-dropping", disableGiveCommandDrops);
    }

    public static boolean commandGamemodeRequiresPermission = false;
    public static boolean hideHiddenPlayersFromEntitySelector = false;
    private static void commandSettings() {
        commandGamemodeRequiresPermission = getBoolean("settings.command.gamemode.requires-specific-permission", commandGamemodeRequiresPermission);
        hideHiddenPlayersFromEntitySelector = getBoolean("settings.command.hide-hidden-players-from-entity-selector", hideHiddenPlayersFromEntitySelector);
    }

    public static boolean barrelSixRows = false;
    public static boolean enderChestSixRows = false;
    public static boolean enderChestPermissionRows = false;
    public static boolean cryingObsidianValidForPortalFrame = false;
    public static int beeInsideBeeHive = 3;
    public static boolean anvilCumulativeCost = true;
    private static void blockSettings() {
        if (version < 3) {
            boolean oldValue = getBoolean("settings.barrel.packed-barrels", true);
            set("settings.blocks.barrel.six-rows", oldValue);
            set("settings.packed-barrels", null);
            oldValue = getBoolean("settings.large-ender-chests", true);
            set("settings.blocks.ender_chest.six-rows", oldValue);
            set("settings.large-ender-chests", null);
        }
        anvilCumulativeCost = getBoolean("settings.blocks.anvil.cumulative-cost", anvilCumulativeCost);
        barrelSixRows = getBoolean("settings.blocks.barrel.six-rows", barrelSixRows);
        InventoryType.BARREL.setDefaultSize(barrelSixRows ? 54 : 27);
        enderChestSixRows = getBoolean("settings.blocks.ender_chest.six-rows", enderChestSixRows);
        InventoryType.ENDER_CHEST.setDefaultSize(enderChestSixRows ? 54 : 27);
        enderChestPermissionRows = getBoolean("settings.blocks.ender_chest.use-permissions-for-rows", enderChestPermissionRows);
        cryingObsidianValidForPortalFrame = getBoolean("settings.blocks.crying_obsidian.valid-for-portal-frame", cryingObsidianValidForPortalFrame);
        beeInsideBeeHive = getInt("settings.blocks.beehive.max-bees-inside", beeInsideBeeHive);
    }

    public static boolean allowInfinityMending = false;
    public static boolean allowCrossbowInfinity = false;
    public static boolean allowUnsafeEnchants = false;
    private static void enchantmentSettings() {
        if (version < 5) {
            boolean oldValue = getBoolean("settings.enchantment.allow-infinite-and-mending-together", false);
            set("settings.enchantment.allow-infinity-and-mending-together", oldValue);
            set("settings.enchantment.allow-infinite-and-mending-together", null);
        }
        allowInfinityMending = getBoolean("settings.enchantment.allow-infinity-and-mending-together", allowInfinityMending);
        allowCrossbowInfinity = getBoolean("settings.enchantment.allow-infinity-on-crossbow", allowCrossbowInfinity);
        allowUnsafeEnchants = getBoolean("settings.enchantment.allow-unsafe-enchants", allowUnsafeEnchants);
    }

    public static boolean endermanShortHeight = false;
    private static void entitySettings() {
        endermanShortHeight = getBoolean("settings.entity.enderman.short-height", endermanShortHeight);
        if (endermanShortHeight) EntityTypes.ENDERMAN.setEntitySize(EntitySize.b(0.6F, 1.9F));
    }

    public static boolean dontSendUselessEntityPackets = false;
    private static void dontSendUselessEntityPackets() {
        dontSendUselessEntityPackets = getBoolean("settings.dont-send-useless-entity-packets", dontSendUselessEntityPackets);
    }

    public static boolean allowWaterPlacementInTheEnd = true;
    private static void allowWaterPlacementInEnd() {
        allowWaterPlacementInTheEnd = getBoolean("settings.allow-water-placement-in-the-end", allowWaterPlacementInTheEnd);
    }

    public static boolean disableMushroomBlockUpdates = false;
    public static boolean disableNoteBlockUpdates = false;
    public static boolean disableChorusPlantUpdates = false;
    private static void blockUpdatesSettings() {
        disableMushroomBlockUpdates = getBoolean("settings.blocks.disable-mushroom-updates", disableMushroomBlockUpdates);
        disableNoteBlockUpdates = getBoolean("settings.blocks.disable-note-block-updates", disableNoteBlockUpdates);
        disableChorusPlantUpdates = getBoolean("settings.blocks.disable-chorus-plant-updates", disableChorusPlantUpdates);
    }

    public static boolean loggerSuppressInitLegacyMaterialError = false;
    public static boolean loggerSuppressIgnoredAdvancementWarnings = false;
    private static void loggerSettings() {
        loggerSuppressInitLegacyMaterialError = getBoolean("settings.logger.suppress-init-legacy-material-errors", loggerSuppressInitLegacyMaterialError);
        loggerSuppressIgnoredAdvancementWarnings = getBoolean("settings.logger.suppress-ignored-advancement-warnings", loggerSuppressIgnoredAdvancementWarnings);
    }

    public static boolean tpsCatchup = true;
    private static void tpsCatchup() {
        tpsCatchup = getBoolean("settings.tps-catchup", tpsCatchup);
    }

    public static boolean advancementOnlyBroadcastToAffectedPlayer = false;
    public static boolean deathMessageOnlyBroadcastToAffectedPlayer = false;
    private static void broadcastSettings() {
        if (version < 13) {
            boolean oldValue = getBoolean("settings.advancement.only-broadcast-to-affected-player", false);
            set("settings.broadcasts.advancement.only-broadcast-to-affected-player", oldValue);
            set("settings.advancement.only-broadcast-to-affected-player", null);
        }
        advancementOnlyBroadcastToAffectedPlayer  = getBoolean("settings.broadcasts.advancement.only-broadcast-to-affected-player", advancementOnlyBroadcastToAffectedPlayer);
        deathMessageOnlyBroadcastToAffectedPlayer = getBoolean("settings.broadcasts.death.only-broadcast-to-affected-player", deathMessageOnlyBroadcastToAffectedPlayer);
    }
}
