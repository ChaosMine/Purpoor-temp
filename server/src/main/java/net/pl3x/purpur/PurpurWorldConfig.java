package net.pl3x.purpur;

import net.minecraft.core.IRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
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

    public float armorstandStepHeight = 0.0F;
    private void armorstandSettings() {
        armorstandStepHeight = (float) getDouble("gameplay-mechanics.armorstand.step-height", armorstandStepHeight);
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

    public int playerSpawnInvulnerableTicks = 60;
    public boolean playerInvulnerableWhileAcceptingResourcePack = false;
    private void playerInvulnerabilities() {
        playerSpawnInvulnerableTicks = getInt("gameplay-mechanics.player.spawn-invulnerable-ticks", playerSpawnInvulnerableTicks);
        playerInvulnerableWhileAcceptingResourcePack = getBoolean("gameplay-mechanics.player.invulnerable-while-accepting-resource-pack", playerInvulnerableWhileAcceptingResourcePack);
    }

    public boolean silkTouchEnabled = false;
    public String silkTouchSpawnerName = "Spawner";
    public List<String> silkTouchSpawnerLore = new ArrayList<>();
    public List<Item> silkTouchTools = new ArrayList<>();
    private void silkTouchSettings() {
        silkTouchEnabled = getBoolean("gameplay-mechanics.silk-touch.enabled", silkTouchEnabled);
        silkTouchSpawnerName = getString("gameplay-mechanics.silk-touch.spawner-name", silkTouchSpawnerName);
        silkTouchSpawnerLore.clear();
        getList("gameplay-mechanics.silk-touch.spawner-lore", new ArrayList<String>(){{
            add("Spawns a {mob}");
        }}).forEach(line -> silkTouchSpawnerLore.add(line.toString()));
        silkTouchTools.clear();
        getList("gameplay-mechanics.silk-touch.tools", new ArrayList<String>(){{
            add("minecraft:iron_pickaxe");
            add("minecraft:golden_pickaxe");
            add("minecraft:diamond_pickaxe");
            add("minecraft:netherite_pickaxe");
        }}).forEach(key -> {
            Item item = IRegistry.ITEM.get(new MinecraftKey(key.toString()));
            if (item != Items.AIR) silkTouchTools.add(item);
        });
    }

    public boolean turtleEggsBreakFromExpOrbs = true;
    public boolean turtleEggsBreakFromItems = true;
    public boolean turtleEggsBreakFromMinecarts = true;
    private void turtleEggSettings() {
        turtleEggsBreakFromExpOrbs = getBoolean("blocks.turtle_egg.break-from-exp-orbs", turtleEggsBreakFromExpOrbs);
        turtleEggsBreakFromItems = getBoolean("blocks.turtle_egg.break-from-items", turtleEggsBreakFromItems);
        turtleEggsBreakFromMinecarts = getBoolean("blocks.turtle_egg.break-from-minecarts", turtleEggsBreakFromMinecarts);
    }

    public boolean chickenRetaliate = false;
    private void chickenSettings() {
        chickenRetaliate = getBoolean("mobs.chicken.retaliate", chickenRetaliate);
    }

    public double creeperChargedChance = 0.0D;
    private void creeperSettings() {
        creeperChargedChance = getDouble("mobs.creeper.naturally-charged-chance", creeperChargedChance);
    }

    public boolean foxTypeChangesWithTulips = false;
    private void foxSettings() {
        foxTypeChangesWithTulips = getBoolean("mobs.fox.tulips-change-type", foxTypeChangesWithTulips);
    }

    public float giantStepHeight = 2.0F;
    public float giantJumpHeight = 1.0F;
    public double giantMovementSpeed = 0.5D;
    public double giantAttackDamage = 50.0D;
    public boolean giantHaveAI = false;
    public boolean giantHaveHostileAI = false;
    public double giantMaxHealth = 100.0D;
    private void giantSettings() {
        giantStepHeight = (float) getDouble("mobs.giant.step-height", giantStepHeight);
        giantJumpHeight = (float) getDouble("mobs.giant.jump-height", giantJumpHeight);
        giantMovementSpeed = getDouble("mobs.giant.movement-speed", giantMovementSpeed);
        giantAttackDamage = getDouble("mobs.giant.attack-damage", giantAttackDamage);
        giantHaveAI = getBoolean("mobs.giant.have-ai", giantHaveAI);
        giantHaveHostileAI = getBoolean("mobs.giant.have-hostile-ai", giantHaveHostileAI);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.giant.max-health", giantMaxHealth);
            set("mobs.giant.attributes.max-health", oldValue);
            set("mobs.giant.max-health", null);
        }
        giantMaxHealth = getDouble("mobs.giant.attributes.max-health", giantMaxHealth);
    }

    public double illusionerMovementSpeed = 0.5D;
    public double illusionerFollowRange = 18.0D;
    public double illusionerMaxHealth = 32.0D;
    private void illusionerSettings() {
        illusionerMovementSpeed = getDouble("mobs.illusioner.movement-speed", illusionerMovementSpeed);
        illusionerFollowRange = getDouble("mobs.illusioner.follow-range", illusionerFollowRange);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.illusioner.max-health", illusionerMaxHealth);
            set("mobs.illusioner.attributes.max-health", oldValue);
            set("mobs.illusioner.max-health", null);
        }
        illusionerMaxHealth = getDouble("mobs.illusioner.attributes.max-health", illusionerMaxHealth);
    }

    public boolean ironGolemCanSwim = false;
    private void ironGolemSettings() {
        ironGolemCanSwim = getBoolean("mobs.iron_golem.can-swim", ironGolemCanSwim);
    }

    public String polarBearBreedableItemString = "";
    public Item polarBearBreedableItem = null;
    private void polarBearSettings() {
        polarBearBreedableItemString = getString("mobs.polar_bear.breedable-item", polarBearBreedableItemString);
        Item item = IRegistry.ITEM.get(new MinecraftKey(polarBearBreedableItemString));
        if (item != Items.AIR) polarBearBreedableItem = item;
    }

    public double rabbitNaturalToast = 0.0D;
    public double rabbitNaturalKiller = 0.0D;
    private void rabbitSettings() {
        rabbitNaturalToast = getDouble("mobs.rabbit.spawn-toast-chance", rabbitNaturalToast);
        rabbitNaturalKiller = getDouble("mobs.rabbit.spawn-killer-rabbit-chance", rabbitNaturalKiller);
    }

    public int villagerBrainTicks = 1;
    public boolean villagerUseBrainTicksOnlyWhenLagging = true;
    private void villagerSettings() {
        villagerBrainTicks = getInt("mobs.villager.brain-ticks", villagerBrainTicks);
        villagerUseBrainTicksOnlyWhenLagging = getBoolean("mobs.villager.use-brain-ticks-only-when-lagging", villagerUseBrainTicksOnlyWhenLagging);
    }

    public double zombieHorseSpawnChance = 0.0D;
    private void zombieHorseSettings() {
        zombieHorseSpawnChance = getDouble("mobs.zombie_horse.spawn-chance", zombieHorseSpawnChance);
    }
}
