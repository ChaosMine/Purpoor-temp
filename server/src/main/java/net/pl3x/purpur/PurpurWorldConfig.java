package net.pl3x.purpur;

import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import static net.pl3x.purpur.PurpurConfig.log;

public class PurpurWorldConfig {

    private final String worldName;

    public PurpurWorldConfig(String worldName) {
        this.worldName = worldName;
        init();
    }

    public void init() {
        log("-------- World Settings For [" + worldName + "] --------");
        PurpurConfig.readConfig(PurpurWorldConfig.class, this);
    }

    private void set(String path, Object val) {
        PurpurConfig.config.addDefault("world-settings.default." + path, val);
        PurpurConfig.config.set("world-settings.default." + path, val);
        if (PurpurConfig.config.get("world-settings." + worldName + "." + path) != null) {
            PurpurConfig.config.addDefault("world-settings." + worldName + "." + path, val);
            PurpurConfig.config.set("world-settings." + worldName + "." + path, val);
        }
    }

    private ConfigurationSection getConfigurationSection(String path) {
        ConfigurationSection section = PurpurConfig.config.getConfigurationSection("world-settings." + worldName + "." + path);
        return section != null ? section : PurpurConfig.config.getConfigurationSection("world-settings.default." + path);
    }

    private boolean getBoolean(String path, boolean def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getBoolean("world-settings." + worldName + "." + path, PurpurConfig.config.getBoolean("world-settings.default." + path));
    }

    private double getDouble(String path, double def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getDouble("world-settings." + worldName + "." + path, PurpurConfig.config.getDouble("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getInt("world-settings." + worldName + "." + path, PurpurConfig.config.getInt("world-settings.default." + path));
    }

    private <T> List<?> getList(String path, T def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getList("world-settings." + worldName + "." + path, PurpurConfig.config.getList("world-settings.default." + path));
    }

    private String getString(String path, String def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getString("world-settings." + worldName + "." + path, PurpurConfig.config.getString("world-settings.default." + path));
    }

    public boolean idleTimeoutKick = true;
    public boolean idleTimeoutTickNearbyEntities = true;
    public boolean idleTimeoutCountAsSleeping = false;
    public boolean idleTimeoutUpdateTabList = false;
    private void playerIdleTimeoutSettings() {
        idleTimeoutKick = getBoolean("gameplay-mechanics.player.idle-timeout.kick-if-idle", idleTimeoutKick);
        idleTimeoutTickNearbyEntities = getBoolean("gameplay-mechanics.player.idle-timeout.tick-nearby-entities", idleTimeoutTickNearbyEntities);
        idleTimeoutCountAsSleeping = getBoolean("gameplay-mechanics.player.idle-timeout.count-as-sleeping", idleTimeoutCountAsSleeping);
        idleTimeoutUpdateTabList = getBoolean("gameplay-mechanics.player.idle-timeout.update-tab-list", idleTimeoutUpdateTabList);
    }
}
