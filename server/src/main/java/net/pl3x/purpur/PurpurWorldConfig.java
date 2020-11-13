package net.pl3x.purpur;

import net.minecraft.core.IRegistry;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.MinecraftKey;
import org.apache.commons.lang3.BooleanUtils;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;

import static net.pl3x.purpur.PurpurConfig.log;

public class PurpurWorldConfig {

    private final String worldName;
    private final World.Environment environment;

    public PurpurWorldConfig(String worldName, World.Environment environment) {
        this.worldName = worldName;
        this.environment = environment;
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

    private boolean getBoolean(String path, Predicate<Boolean> predicate) {
        String val = getString(path, "default").toLowerCase();
        Boolean bool = BooleanUtils.toBooleanObject(val, "true", "false", "default");
        return predicate.test(bool);
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

    public double minecartMaxSpeed = 0.4D;
    public boolean minecartPlaceAnywhere = false;
    public boolean minecartControllable = false;
    public float minecartControllableStepHeight = 1.0F;
    public double minecartControllableHopBoost = 0.5D;
    public boolean minecartControllableFallDamage = true;
    public double minecartControllableBaseSpeed = 0.1D;
    public Map<Block, Double> minecartControllableBlockSpeeds = new HashMap<>();
    private void minecartSettings() {
        if (PurpurConfig.version < 12) {
            boolean oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.place-anywhere", minecartPlaceAnywhere);
            set("gameplay-mechanics.controllable-minecarts.place-anywhere", null);
            set("gameplay-mechanics.minecart.place-anywhere", oldBool);
            oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.enabled", minecartControllable);
            set("gameplay-mechanics.controllable-minecarts.enabled", null);
            set("gameplay-mechanics.minecart.controllable.enabled", oldBool);
            double oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.step-height", minecartControllableStepHeight);
            set("gameplay-mechanics.controllable-minecarts.step-height", null);
            set("gameplay-mechanics.minecart.controllable.step-height", oldDouble);
            oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.hop-boost", minecartControllableHopBoost);
            set("gameplay-mechanics.controllable-minecarts.hop-boost", null);
            set("gameplay-mechanics.minecart.controllable.hop-boost", oldDouble);
            oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.fall-damage", minecartControllableFallDamage);
            set("gameplay-mechanics.controllable-minecarts.fall-damage", null);
            set("gameplay-mechanics.minecart.controllable.fall-damage", oldBool);
            oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.base-speed", minecartControllableBaseSpeed);
            set("gameplay-mechanics.controllable-minecarts.base-speed", null);
            set("gameplay-mechanics.minecart.controllable.base-speed", oldDouble);
            ConfigurationSection section = getConfigurationSection("gameplay-mechanics.controllable-minecarts.block-speed");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    if ("grass-block".equals(key)) key = "grass_block"; // oopsie
                    oldDouble = section.getDouble(key, minecartControllableBaseSpeed);
                    set("gameplay-mechanics.controllable-minecarts.block-speed." + key, null);
                    set("gameplay-mechanics.minecart.controllable.block-speed." + key, oldDouble);
                }
                set("gameplay-mechanics.controllable-minecarts.block-speed", null);
            }
            set("gameplay-mechanics.controllable-minecarts", null);
        }

        minecartMaxSpeed = getDouble("gameplay-mechanics.minecart.max-speed", minecartMaxSpeed);
        minecartPlaceAnywhere = getBoolean("gameplay-mechanics.minecart.place-anywhere", minecartPlaceAnywhere);
        minecartControllable = getBoolean("gameplay-mechanics.minecart.controllable.enabled", minecartControllable);
        minecartControllableStepHeight = (float) getDouble("gameplay-mechanics.minecart.controllable.step-height", minecartControllableStepHeight);
        minecartControllableHopBoost = getDouble("gameplay-mechanics.minecart.controllable.hop-boost", minecartControllableHopBoost);
        minecartControllableFallDamage = getBoolean("gameplay-mechanics.minecart.controllable.fall-damage", minecartControllableFallDamage);
        minecartControllableBaseSpeed = getDouble("gameplay-mechanics.minecart.controllable.base-speed", minecartControllableBaseSpeed);
        ConfigurationSection section = getConfigurationSection("gameplay-mechanics.minecart.controllable.block-speed");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Block block = IRegistry.BLOCK.get(new MinecraftKey(key));
                if (block != Blocks.AIR) {
                    minecartControllableBlockSpeeds.put(block, section.getDouble(key, minecartControllableBaseSpeed));
                }
            }
        } else {
            set("gameplay-mechanics.minecart.controllable.block-speed.grass_block", 0.3D);
            set("gameplay-mechanics.minecart.controllable.block-speed.stone", 0.5D);
        }
    }

    public int daytimeTicks = 12000;
    public int nighttimeTicks = 12000;
    private void daytimeCycleSettings() {
        daytimeTicks = getInt("gameplay-mechanics.daylight-cycle-ticks.daytime", daytimeTicks);
        nighttimeTicks = getInt("gameplay-mechanics.daylight-cycle-ticks.nighttime", nighttimeTicks);
    }

    public int entityLifeSpan = 0;
    private void entitySettings() {
        entityLifeSpan = getInt("gameplay-mechanics.entity-lifespan", entityLifeSpan);
    }

    public List<Item> itemImmuneToCactus = new ArrayList<>();
    public List<Item> itemImmuneToExplosion = new ArrayList<>();
    public List<Item> itemImmuneToFire = new ArrayList<>();
    public List<Item> itemImmuneToLightning = new ArrayList<>();
    private void itemSettings() {
        itemImmuneToCactus.clear();
        getList("gameplay-mechanics.item.immune.cactus", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                IRegistry.ITEM.g().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToCactus.add(item));
                return;
            }
            Item item = IRegistry.ITEM.get(new MinecraftKey(key.toString()));
            if (item != Items.AIR) itemImmuneToCactus.add(item);
        });
        itemImmuneToExplosion.clear();
        getList("gameplay-mechanics.item.immune.explosion", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                IRegistry.ITEM.g().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToExplosion.add(item));
                return;
            }
            Item item = IRegistry.ITEM.get(new MinecraftKey(key.toString()));
            if (item != Items.AIR) itemImmuneToExplosion.add(item);
        });
        itemImmuneToFire.clear();
        getList("gameplay-mechanics.item.immune.fire", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                IRegistry.ITEM.g().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToFire.add(item));
                return;
            }
            Item item = IRegistry.ITEM.get(new MinecraftKey(key.toString()));
            if (item != Items.AIR) itemImmuneToFire.add(item);
        });
        itemImmuneToLightning.clear();
        getList("gameplay-mechanics.item.immune.lightning", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                IRegistry.ITEM.g().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToLightning.add(item));
                return;
            }
            Item item = IRegistry.ITEM.get(new MinecraftKey(key.toString()));
            if (item != Items.AIR) itemImmuneToLightning.add(item);
        });
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

    public boolean infinityWorksWithNormalArrows = true;
    public boolean infinityWorksWithSpectralArrows = false;
    public boolean infinityWorksWithTippedArrows = false;
    private void infinityArrowsSettings() {
        infinityWorksWithNormalArrows = getBoolean("gameplay-mechanics.infinity-bow.normal-arrows", infinityWorksWithNormalArrows);
        infinityWorksWithSpectralArrows = getBoolean("gameplay-mechanics.infinity-bow.spectral-arrows", infinityWorksWithSpectralArrows);
        infinityWorksWithTippedArrows = getBoolean("gameplay-mechanics.infinity-bow.tipped-arrows", infinityWorksWithTippedArrows);
    }

    public int dragonFireballDespawnRate = -1;
    public int eggDespawnRate = -1;
    public int enderPearlDespawnRate = -1;
    public int expBottleDespawnRate = -1;
    public int fireworkDespawnRate = -1;
    public int fishingHookDespawnRate = -1;
    public int largeFireballDespawnRate = -1;
    public int llamaSpitDespawnRate = -1;
    public int potionDespawnRate = -1;
    public int shulkerBulletDespawnRate = -1;
    public int smallFireballDespawnRate = -1;
    public int snowballDespawnRate = -1;
    public int witherSkullDespawnRate = -1;
    private void projectileDespawnRateSettings() {
        dragonFireballDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.dragon_fireball", dragonFireballDespawnRate);
        eggDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.egg", eggDespawnRate);
        enderPearlDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.ender_pearl", enderPearlDespawnRate);
        expBottleDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.experience_bottle", expBottleDespawnRate);
        fireworkDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.firework_rocket", fireworkDespawnRate);
        fishingHookDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.fishing_bobber", fishingHookDespawnRate);
        largeFireballDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.fireball", largeFireballDespawnRate);
        llamaSpitDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.llama_spit", llamaSpitDespawnRate);
        potionDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.potion", potionDespawnRate);
        shulkerBulletDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.shulker_bullet", shulkerBulletDespawnRate);
        smallFireballDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.small_fireball", smallFireballDespawnRate);
        snowballDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.snowball", snowballDespawnRate);
        witherSkullDespawnRate = getInt("gameplay-mechanics.projectile-despawn-rates.wither_skull", witherSkullDespawnRate);
    }

    public boolean useBetterMending = false;
    public boolean boatEjectPlayersOnLand = false;
    public boolean disableDropsOnCrammingDeath = false;
    public boolean entitiesPickUpLootBypassMobGriefing = false;
    public boolean entitiesCanUsePortals = true;
    public boolean milkCuresBadOmen = true;
    public boolean persistentTileEntityDisplayNames = false;
    public double tridentLoyaltyVoidReturnHeight = 0.0D;
    public double voidDamageHeight = -64.0D;
    public double voidDamageDealt = 4.0D;
    public int raidCooldownSeconds = 0;
    public int animalBreedingCooldownSeconds = 0;
    private void miscGameplayMechanicsSettings() {
        useBetterMending = getBoolean("gameplay-mechanics.use-better-mending", useBetterMending);
        boatEjectPlayersOnLand = getBoolean("gameplay-mechanics.boat.eject-players-on-land", boatEjectPlayersOnLand);
        disableDropsOnCrammingDeath = getBoolean("gameplay-mechanics.disable-drops-on-cramming-death", disableDropsOnCrammingDeath);
        entitiesPickUpLootBypassMobGriefing = getBoolean("gameplay-mechanics.entities-pick-up-loot-bypass-mob-griefing", entitiesPickUpLootBypassMobGriefing);
        entitiesCanUsePortals = getBoolean("gameplay-mechanics.entities-can-use-portals", entitiesCanUsePortals);
        milkCuresBadOmen = getBoolean("gameplay-mechanics.milk-cures-bad-omen", milkCuresBadOmen);
        persistentTileEntityDisplayNames = getBoolean("gameplay-mechanics.persistent-tileentity-display-names-and-lore", persistentTileEntityDisplayNames);
        tridentLoyaltyVoidReturnHeight = getDouble("gameplay-mechanics.trident-loyalty-void-return-height", tridentLoyaltyVoidReturnHeight);
        voidDamageHeight = getDouble("gameplay-mechanics.void-damage-height", voidDamageHeight);
        voidDamageDealt = getDouble("gameplay-mechanics.void-damage-dealt", voidDamageDealt);
        raidCooldownSeconds = getInt("gameplay-mechanics.raid-cooldown-seconds", raidCooldownSeconds);
        animalBreedingCooldownSeconds = getInt("gameplay-mechanics.animal-breeding-cooldown-seconds", animalBreedingCooldownSeconds);
    }

    public boolean catSpawning;
    public boolean patrolSpawning;
    public boolean phantomSpawning;
    public boolean villagerTraderSpawning;
    public boolean villageSiegeSpawning;
    private void mobSpawnerSettings() {
        // values of "default" or null will default to true only if the world environment is normal (aka overworld)
        Predicate<Boolean> predicate = (bool) -> (bool != null && bool) || (bool == null && environment == World.Environment.NORMAL);
        catSpawning = getBoolean("gameplay-mechanics.mob-spawning.village-cats", predicate);
        patrolSpawning = getBoolean("gameplay-mechanics.mob-spawning.raid-patrols", predicate);
        phantomSpawning = getBoolean("gameplay-mechanics.mob-spawning.phantoms", predicate);
        villagerTraderSpawning = getBoolean("gameplay-mechanics.mob-spawning.wandering-traders", predicate);
        villageSiegeSpawning = getBoolean("gameplay-mechanics.mob-spawning.village-sieges", predicate);
    }

    public int elytraDamagePerSecond = 1;
    public double elytraDamageMultiplyBySpeed = 0;
    public boolean elytraIgnoreUnbreaking = false;
    public int elytraDamagePerFireworkBoost = 0;
    public int elytraDamagePerTridentBoost = 0;
    private void elytraSettings() {
        elytraDamagePerSecond = getInt("gameplay-mechanics.elytra.damage-per-second", elytraDamagePerSecond);
        elytraDamageMultiplyBySpeed = getDouble("gameplay-mechanics.elytra.damage-multiplied-by-speed", elytraDamageMultiplyBySpeed);
        elytraIgnoreUnbreaking = getBoolean("gameplay-mechanics.elytra.ignore-unbreaking", elytraIgnoreUnbreaking);
        elytraDamagePerFireworkBoost = getInt("gameplay-mechanics.elytra.damage-per-boost.firework", elytraDamagePerFireworkBoost);
        elytraDamagePerTridentBoost = getInt("gameplay-mechanics.elytra.damage-per-boost.trident", elytraDamagePerTridentBoost);
    }

    public String playerDeathExpDropEquation = "expLevel * 7";
    public int playerDeathExpDropMax = 100;
    private void playerDeathExpSettings() {
        playerDeathExpDropEquation = getString("gameplay-mechanics.player.exp-dropped-on-death.equation", playerDeathExpDropEquation);
        playerDeathExpDropMax = getInt("gameplay-mechanics.player.exp-dropped-on-death.maximum", playerDeathExpDropMax);
    }

    public int playerSpawnInvulnerableTicks = 60;
    public boolean playerInvulnerableWhileAcceptingResourcePack = false;
    private void playerInvulnerabilities() {
        playerSpawnInvulnerableTicks = getInt("gameplay-mechanics.player.spawn-invulnerable-ticks", playerSpawnInvulnerableTicks);
        playerInvulnerableWhileAcceptingResourcePack = getBoolean("gameplay-mechanics.player.invulnerable-while-accepting-resource-pack", playerInvulnerableWhileAcceptingResourcePack);
    }

    public Set<Block> noRandomTickBlocks = new HashSet<>();
    private void noRandomTickBlocks() {
        if (PurpurConfig.version < 11) {
            List<String> oldList = PurpurConfig.config.getStringList("world-settings." + worldName + ".blocks.no-tick");
            if (!oldList.isEmpty()) {
                PurpurConfig.config.set("world-settings." + worldName + ".blocks.no-random-tick", oldList);
                PurpurConfig.config.set("world-settings." + worldName + ".blocks.no-tick", null);
            }
            oldList = PurpurConfig.config.getStringList("world-settings.default.blocks.no-tick");
            if (!oldList.isEmpty()) {
                PurpurConfig.config.set("world-settings.default.blocks.no-random-tick", oldList);
                PurpurConfig.config.set("world-settings.default.blocks.no-tick", null);
            }
        }
        getList("blocks.no-random-tick", new ArrayList<>()).forEach(key -> {
            Block block = IRegistry.BLOCK.get(new MinecraftKey(key.toString()));
            if (!block.getBlockData().isAir()) {
                noRandomTickBlocks.add(block);
            }
        });
    }

    public boolean teleportIfOutsideBorder = false;
    private void teleportIfOutsideBorder() {
        teleportIfOutsideBorder = getBoolean("gameplay-mechanics.player.teleport-if-outside-border", teleportIfOutsideBorder);
    }

    public boolean totemOfUndyingWorksInInventory = false;
    private void totemOfUndyingWorksInInventory() {
        totemOfUndyingWorksInInventory = getBoolean("gameplay-mechanics.player.totem-of-undying-works-in-inventory", totemOfUndyingWorksInInventory);
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

    public boolean anvilAllowColors = false;
    private void anvilSettings() {
        anvilAllowColors = getBoolean("blocks.anvil.allow-colors", anvilAllowColors);
    }

    public boolean bedExplode = true;
    public double bedExplosionPower = 5.0D;
    public boolean bedExplosionFire = true;
    public Explosion.Effect bedExplosionEffect = Explosion.Effect.DESTROY;
    private void bedSettings() {
        bedExplode = getBoolean("blocks.bed.explode", bedExplode);
        bedExplosionPower = getDouble("blocks.bed.explosion-power", bedExplosionPower);
        bedExplosionFire = getBoolean("blocks.bed.explosion-fire", bedExplosionFire);
        try {
            bedExplosionEffect = Explosion.Effect.valueOf(getString("blocks.bed.explosion-effect", bedExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.bed.explosion-effect`! Using default of `DESTROY`");
            bedExplosionEffect = Explosion.Effect.DESTROY;
        }
    }

    public boolean dispenserApplyCursedArmor = true;
    public boolean dispenserPlaceAnvils = false;
    private void dispenserSettings() {
        dispenserApplyCursedArmor = getBoolean("blocks.dispenser.apply-cursed-to-armor-slots", dispenserApplyCursedArmor);
        dispenserPlaceAnvils = getBoolean("blocks.dispenser.place-anvils", dispenserPlaceAnvils);
    }

    public boolean farmlandGetsMoistFromBelow = false;
    public boolean farmlandAlpha = false;
    private void farmlandSettings() {
        farmlandGetsMoistFromBelow = getBoolean("blocks.farmland.gets-moist-from-below", farmlandGetsMoistFromBelow);
        farmlandAlpha = getBoolean("blocks.farmland.use-alpha-farmland", farmlandAlpha);
    }

    public boolean furnaceInfiniteFuel = false;
    private void furnaceSettings() {
        furnaceInfiniteFuel = getBoolean("blocks.furnace.infinite-fuel", furnaceInfiniteFuel);
    }

    public boolean lavaInfinite = false;
    public int lavaInfiniteRequiredSources = 2;
    public int lavaSpeedNether = 10;
    public int lavaSpeedNotNether = 30;
    private void lavaSettings() {
        lavaInfinite = getBoolean("blocks.lava.infinite-source", lavaInfinite);
        lavaInfiniteRequiredSources = getInt("blocks.lava.infinite-required-sources", lavaInfiniteRequiredSources);
        lavaSpeedNether = getInt("blocks.lava.speed.nether", lavaSpeedNether);
        lavaSpeedNotNether = getInt("blocks.lava.speed.not-nether", lavaSpeedNotNether);
    }

    public boolean respawnAnchorExplode = true;
    public double respawnAnchorExplosionPower = 5.0D;
    public boolean respawnAnchorExplosionFire = true;
    public Explosion.Effect respawnAnchorExplosionEffect = Explosion.Effect.DESTROY;
    private void respawnAnchorSettings() {
        respawnAnchorExplode = getBoolean("blocks.respawn_anchor.explode", respawnAnchorExplode);
        respawnAnchorExplosionPower = getDouble("blocks.respawn_anchor.explosion-power", respawnAnchorExplosionPower);
        respawnAnchorExplosionFire = getBoolean("blocks.respawn_anchor.explosion-fire", respawnAnchorExplosionFire);
        try {
            respawnAnchorExplosionEffect = Explosion.Effect.valueOf(getString("blocks.respawn_anchor.explosion-effect", respawnAnchorExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.respawn_anchor.explosion-effect`! Using default of `DESTROY`");
            respawnAnchorExplosionEffect = Explosion.Effect.DESTROY;
        }
    }

    public boolean signAllowColors = false;
    public boolean signRightClickEdit = false;
    private void signSettings() {
        signAllowColors = getBoolean("blocks.sign.allow-colors", signAllowColors);
        signRightClickEdit = getBoolean("blocks.sign.right-click-edit", signRightClickEdit);
    }

    public boolean spawnerDeactivateByRedstone = false;
    private void spawnerSettings() {
        spawnerDeactivateByRedstone = getBoolean("blocks.spawner.deactivate-by-redstone", spawnerDeactivateByRedstone);
    }

    public float stonecutterDamage = 0.0F;
    private void stonecutterSettings() {
        stonecutterDamage = (float) getDouble("blocks.stonecutter.damage", stonecutterDamage);
    }

    public boolean turtleEggsBreakFromExpOrbs = true;
    public boolean turtleEggsBreakFromItems = true;
    public boolean turtleEggsBreakFromMinecarts = true;
    private void turtleEggSettings() {
        turtleEggsBreakFromExpOrbs = getBoolean("blocks.turtle_egg.break-from-exp-orbs", turtleEggsBreakFromExpOrbs);
        turtleEggsBreakFromItems = getBoolean("blocks.turtle_egg.break-from-items", turtleEggsBreakFromItems);
        turtleEggsBreakFromMinecarts = getBoolean("blocks.turtle_egg.break-from-minecarts", turtleEggsBreakFromMinecarts);
    }

    public boolean babiesAreRidable = true;
    public boolean untamedTamablesAreRidable = true;
    public boolean useNightVisionWhenRiding = false;
    private void ridableSettings() {
        babiesAreRidable = getBoolean("ridable-settings.babies-are-ridable", babiesAreRidable);
        untamedTamablesAreRidable = getBoolean("ridable-settings.untamed-tamables-are-ridable", untamedTamablesAreRidable);
        useNightVisionWhenRiding = getBoolean("ridable-settings.use-night-vision", useNightVisionWhenRiding);
    }

    public boolean batRidable = false;
    public boolean batRidableInWater = false;
    public double batMaxY = 256D;
    private void batSettings() {
        batRidable = getBoolean("mobs.bat.ridable", batRidable);
        batRidableInWater = getBoolean("mobs.bat.ridable-in-water", batRidableInWater);
        batMaxY = getDouble("mobs.bat.ridable-max-y", batMaxY);
    }

    public boolean beeRidable = false;
    public boolean beeRidableInWater = false;
    public double beeMaxY = 256D;
    private void beeSettings() {
        beeRidable = getBoolean("mobs.bee.ridable", beeRidable);
        beeRidableInWater = getBoolean("mobs.bee.ridable-in-water", beeRidableInWater);
        beeMaxY = getDouble("mobs.bee.ridable-max-y", beeMaxY);
    }

    public boolean blazeRidable = false;
    public boolean blazeRidableInWater = false;
    public double blazeMaxY = 256D;
    private void blazeSettings() {
        blazeRidable = getBoolean("mobs.blaze.ridable", blazeRidable);
        blazeRidableInWater = getBoolean("mobs.blaze.ridable-in-water", blazeRidableInWater);
        blazeMaxY = getDouble("mobs.blaze.ridable-max-y", blazeMaxY);
    }

    public boolean catRidable = false;
    public boolean catRidableInWater = false;
    public int catSpawnDelay = 1200;
    public int catSpawnSwampHutScanRange = 16;
    public int catSpawnVillageScanRange = 48;
    private void catSettings() {
        catRidable = getBoolean("mobs.cat.ridable", catRidable);
        catRidableInWater = getBoolean("mobs.cat.ridable-in-water", catRidableInWater);
        catSpawnDelay = getInt("mobs.cat.spawn-delay", catSpawnDelay);
        catSpawnSwampHutScanRange = getInt("mobs.cat.scan-range-for-other-cats.swamp-hut", catSpawnSwampHutScanRange);
        catSpawnVillageScanRange = getInt("mobs.cat.scan-range-for-other-cats.village", catSpawnVillageScanRange);
    }

    public boolean caveSpiderRidable = false;
    public boolean caveSpiderRidableInWater = false;
    private void caveSpiderSettings() {
        caveSpiderRidable = getBoolean("mobs.cave_spider.ridable", caveSpiderRidable);
        caveSpiderRidableInWater = getBoolean("mobs.cave_spider.ridable-in-water", caveSpiderRidableInWater);
    }

    public boolean chickenRidable = false;
    public boolean chickenRidableInWater = false;
    public boolean chickenRetaliate = false;
    private void chickenSettings() {
        chickenRidable = getBoolean("mobs.chicken.ridable", chickenRidable);
        chickenRidableInWater = getBoolean("mobs.chicken.ridable-in-water", chickenRidableInWater);
        chickenRetaliate = getBoolean("mobs.chicken.retaliate", chickenRetaliate);
    }

    public boolean codRidable = false;
    private void codSettings() {
        codRidable = getBoolean("mobs.cod.ridable", codRidable);
    }

    public boolean cowRidable = false;
    public boolean cowRidableInWater = false;
    public int cowFeedMushrooms = 0;
    private void cowSettings() {
        cowRidable = getBoolean("mobs.cow.ridable", cowRidable);
        cowRidableInWater = getBoolean("mobs.cow.ridable-in-water", cowRidableInWater);
        cowFeedMushrooms = getInt("mobs.cow.feed-mushrooms-for-mooshroom", cowFeedMushrooms);
    }

    public boolean creeperRidable = false;
    public boolean creeperRidableInWater = false;
    public boolean creeperAllowGriefing = true;
    public double creeperChargedChance = 0.0D;
    private void creeperSettings() {
        creeperRidable = getBoolean("mobs.creeper.ridable", creeperRidable);
        creeperRidableInWater = getBoolean("mobs.creeper.ridable-in-water", creeperRidableInWater);
        creeperAllowGriefing = getBoolean("mobs.creeper.allow-griefing", creeperAllowGriefing);
        creeperChargedChance = getDouble("mobs.creeper.naturally-charged-chance", creeperChargedChance);
    }

    public boolean dolphinRidable = false;
    public int dolphinSpitCooldown = 20;
    public float dolphinSpitSpeed = 1.0F;
    public float dolphinSpitDamage = 2.0F;
    public boolean dolphinDisableTreasureSearching = false;
    private void dolphinSettings() {
        dolphinRidable = getBoolean("mobs.dolphin.ridable", dolphinRidable);
        dolphinSpitCooldown = getInt("mobs.dolphin.spit.cooldown", dolphinSpitCooldown);
        dolphinSpitSpeed = (float) getDouble("mobs.dolphin.spit.speed", dolphinSpitSpeed);
        dolphinSpitDamage = (float) getDouble("mobs.dolphin.spit.damage", dolphinSpitDamage);
        dolphinDisableTreasureSearching = getBoolean("mobs.dolphin.disable-treasure-searching", dolphinDisableTreasureSearching);
    }

    public boolean donkeyRidableInWater = false;
    private void donkeySettings() {
        donkeyRidableInWater = getBoolean("mobs.donkey.ridable-in-water", donkeyRidableInWater);
    }

    public boolean drownedRidable = false;
    public boolean drownedRidableInWater = false;
    public boolean drownedJockeyOnlyBaby = true;
    public double drownedJockeyChance = 0.05D;
    public boolean drownedJockeyTryExistingChickens = true;
    private void drownedSettings() {
        drownedRidable = getBoolean("mobs.drowned.ridable", drownedRidable);
        drownedRidableInWater = getBoolean("mobs.drowned.ridable-in-water", drownedRidableInWater);
        drownedJockeyOnlyBaby = getBoolean("mobs.drowned.jockey.only-babies", drownedJockeyOnlyBaby);
        drownedJockeyChance = getDouble("mobs.drowned.jockey.chance", drownedJockeyChance);
        drownedJockeyTryExistingChickens = getBoolean("mobs.drowned.jockey.try-existing-chickens", drownedJockeyTryExistingChickens);
    }

    public boolean elderGuardianRidable = false;
    private void elderGuardianSettings() {
        elderGuardianRidable = getBoolean("mobs.elder_guardian.ridable", elderGuardianRidable);
    }

    public boolean enderDragonRidable = false;
    public boolean enderDragonRidableInWater = false;
    public double enderDragonMaxY = 256D;
    public boolean enderDragonAlwaysDropsFullExp = false;
    private void enderDragonSettings() {
        enderDragonRidable = getBoolean("mobs.ender_dragon.ridable", enderDragonRidable);
        enderDragonRidableInWater = getBoolean("mobs.ender_dragon.ridable-in-water", enderDragonRidableInWater);
        enderDragonMaxY = getDouble("mobs.ender_dragon.ridable-max-y", enderDragonMaxY);
        enderDragonAlwaysDropsFullExp = getBoolean("mobs.ender_dragon.always-drop-full-exp", enderDragonAlwaysDropsFullExp);
    }

    public boolean endermanRidable = false;
    public boolean endermanRidableInWater = false;
    public boolean endermanAllowGriefing = true;
    private void endermanSettings() {
        endermanRidable = getBoolean("mobs.enderman.ridable", endermanRidable);
        endermanRidableInWater = getBoolean("mobs.enderman.ridable-in-water", endermanRidableInWater);
        endermanAllowGriefing = getBoolean("mobs.enderman.allow-griefing", endermanAllowGriefing);
    }

    public boolean endermiteRidable = false;
    public boolean endermiteRidableInWater = false;
    private void endermiteSettings() {
        endermiteRidable = getBoolean("mobs.endermite.ridable", endermiteRidable);
        endermiteRidableInWater = getBoolean("mobs.endermite.ridable-in-water", endermiteRidableInWater);
    }

    public boolean evokerRidable = false;
    public boolean evokerRidableInWater = false;
    private void evokerSettings() {
        evokerRidable = getBoolean("mobs.evoker.ridable", evokerRidable);
        evokerRidableInWater = getBoolean("mobs.evoker.ridable-in-water", evokerRidableInWater);
    }

    public boolean foxRidable = false;
    public boolean foxRidableInWater = false;
    public boolean foxTypeChangesWithTulips = false;
    private void foxSettings() {
        foxRidable = getBoolean("mobs.fox.ridable", foxRidable);
        foxRidableInWater = getBoolean("mobs.fox.ridable-in-water", foxRidableInWater);
        foxTypeChangesWithTulips = getBoolean("mobs.fox.tulips-change-type", foxTypeChangesWithTulips);
    }

    public boolean ghastRidable = false;
    public boolean ghastRidableInWater = false;
    public double ghastMaxY = 256D;
    private void ghastSettings() {
        ghastRidable = getBoolean("mobs.ghast.ridable", ghastRidable);
        ghastRidableInWater = getBoolean("mobs.ghast.ridable-in-water", ghastRidableInWater);
        ghastMaxY = getDouble("mobs.ghast.ridable-max-y", ghastMaxY);
    }

    public boolean giantRidable = false;
    public boolean giantRidableInWater = false;
    public float giantStepHeight = 2.0F;
    public float giantJumpHeight = 1.0F;
    public double giantMovementSpeed = 0.5D;
    public double giantAttackDamage = 50.0D;
    public boolean giantHaveAI = false;
    public boolean giantHaveHostileAI = false;
    public double giantMaxHealth = 100.0D;
    private void giantSettings() {
        giantRidable = getBoolean("mobs.giant.ridable", giantRidable);
        giantRidableInWater = getBoolean("mobs.giant.ridable-in-water", giantRidableInWater);
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

    public boolean guardianRidable = false;
    private void guardianSettings() {
        guardianRidable = getBoolean("mobs.guardian.ridable", guardianRidable);
    }

    public boolean hoglinRidable = false;
    public boolean hoglinRidableInWater = false;
    private void hoglinSettings() {
        hoglinRidable = getBoolean("mobs.hoglin.ridable", hoglinRidable);
        hoglinRidableInWater = getBoolean("mobs.hoglin.ridable-in-water", hoglinRidableInWater);
    }

    public boolean horseRidableInWater = false;
    private void horseSettings() {
        horseRidableInWater = getBoolean("mobs.horse.ridable-in-water", horseRidableInWater);
    }

    public boolean huskRidable = false;
    public boolean huskRidableInWater = false;
    public boolean huskJockeyOnlyBaby = true;
    public double huskJockeyChance = 0.05D;
    public boolean huskJockeyTryExistingChickens = true;
    private void huskSettings() {
        huskRidable = getBoolean("mobs.husk.ridable", huskRidable);
        huskRidableInWater = getBoolean("mobs.husk.ridable-in-water", huskRidableInWater);
        huskJockeyOnlyBaby = getBoolean("mobs.husk.jockey.only-babies", huskJockeyOnlyBaby);
        huskJockeyChance = getDouble("mobs.husk.jockey.chance", huskJockeyChance);
        huskJockeyTryExistingChickens = getBoolean("mobs.husk.jockey.try-existing-chickens", huskJockeyTryExistingChickens);
    }

    public boolean illusionerRidable = false;
    public boolean illusionerRidableInWater = false;
    public double illusionerMovementSpeed = 0.5D;
    public double illusionerFollowRange = 18.0D;
    public double illusionerMaxHealth = 32.0D;
    private void illusionerSettings() {
        illusionerRidable = getBoolean("mobs.illusioner.ridable", illusionerRidable);
        illusionerRidableInWater = getBoolean("mobs.illusioner.ridable-in-water", illusionerRidableInWater);
        illusionerMovementSpeed = getDouble("mobs.illusioner.movement-speed", illusionerMovementSpeed);
        illusionerFollowRange = getDouble("mobs.illusioner.follow-range", illusionerFollowRange);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.illusioner.max-health", illusionerMaxHealth);
            set("mobs.illusioner.attributes.max-health", oldValue);
            set("mobs.illusioner.max-health", null);
        }
        illusionerMaxHealth = getDouble("mobs.illusioner.attributes.max-health", illusionerMaxHealth);
    }

    public boolean ironGolemRidable = false;
    public boolean ironGolemRidableInWater = false;
    public boolean ironGolemCanSwim = false;
    private void ironGolemSettings() {
        ironGolemRidable = getBoolean("mobs.iron_golem.ridable", ironGolemRidable);
        ironGolemRidableInWater = getBoolean("mobs.iron_golem.ridable-in-water", ironGolemRidableInWater);
        ironGolemCanSwim = getBoolean("mobs.iron_golem.can-swim", ironGolemCanSwim);
    }

    public boolean llamaRidable = false;
    public boolean llamaRidableInWater = false;
    private void llamaSettings() {
        llamaRidable = getBoolean("mobs.llama.ridable", llamaRidable);
        llamaRidableInWater = getBoolean("mobs.llama.ridable-in-water", llamaRidableInWater);
    }

    public boolean llamaTraderRidable = false;
    public boolean llamaTraderRidableInWater = false;
    private void llamaTraderSettings() {
        llamaTraderRidable = getBoolean("mobs.trader_llama.ridable", llamaTraderRidable);
        llamaTraderRidableInWater = getBoolean("mobs.trader_llama.ridable-in-water", llamaTraderRidableInWater);
    }

    public boolean magmaCubeRidable = false;
    public boolean magmaCubeRidableInWater = false;
    private void magmaCubeSettings() {
        magmaCubeRidable = getBoolean("mobs.magma_cube.ridable", magmaCubeRidable);
        magmaCubeRidableInWater = getBoolean("mobs.magma_cube.ridable-in-water", magmaCubeRidableInWater);
    }

    public boolean mooshroomRidable = false;
    public boolean mooshroomRidableInWater = false;
    private void mooshroomSettings() {
        mooshroomRidable = getBoolean("mobs.mooshroom.ridable", mooshroomRidable);
        mooshroomRidableInWater = getBoolean("mobs.mooshroom.ridable-in-water", mooshroomRidableInWater);
    }

    public boolean muleRidableInWater = false;
    private void muleSettings() {
        muleRidableInWater = getBoolean("mobs.mule.ridable-in-water", muleRidableInWater);
    }

    public boolean ocelotRidable = false;
    public boolean ocelotRidableInWater = false;
    private void ocelotSettings() {
        ocelotRidable = getBoolean("mobs.ocelot.ridable", ocelotRidable);
        ocelotRidableInWater = getBoolean("mobs.ocelot.ridable-in-water", ocelotRidableInWater);
    }

    public boolean pandaRidable = false;
    public boolean pandaRidableInWater = false;
    private void pandaSettings() {
        pandaRidable = getBoolean("mobs.panda.ridable", pandaRidable);
        pandaRidableInWater = getBoolean("mobs.panda.ridable-in-water", pandaRidableInWater);
    }

    public boolean parrotRidable = false;
    public boolean parrotRidableInWater = false;
    public double parrotMaxY = 256D;
    private void parrotSettings() {
        parrotRidable = getBoolean("mobs.parrot.ridable", parrotRidable);
        parrotRidableInWater = getBoolean("mobs.parrot.ridable-in-water", parrotRidableInWater);
        parrotMaxY = getDouble("mobs.parrot.ridable-max-y", parrotMaxY);
    }

    public boolean phantomRidable = false;
    public boolean phantomRidableInWater = false;
    public double phantomMaxY = 256D;
    public float phantomFlameDamage = 1.0F;
    public int phantomFlameFireTime = 8;
    public double phantomAttackedByCrystalRadius = 0.0D;
    public float phantomAttackedByCrystalDamage = 1.0F;
    public double phantomOrbitCrystalRadius = 0.0D;
    public int phantomSpawnDelayMin = 1200;
    public int phantomSpawnDelayMax = 2400;
    public int phantomSpawnMinSkyDarkness = 5;
    public boolean phantomSpawnOnlyAboveSeaLevel = true;
    public boolean phantomSpawnOnlyWithVisibleSky = true;
    public double phantomSpawnLocalDifficultyChance = 3.0D;
    public int phantomSpawnMinTimeSinceSlept = 72000;
    public int phantomSpawnMinOverhead = 20;
    public int phantomSpawnMaxOverhead = 35;
    public int phantomSpawnOverheadRadius = 10;
    public int phantomSpawnMinPerAttempt = 1;
    public int phantomSpawnMaxPerAttempt = -1;
    public int phantomBurnInLight = 0;
    public boolean phantomIgnorePlayersWithTorch = false;
    public boolean phantomBurnInDaylight = true;
    public boolean phantomAllowGriefing = false;
    private void phantomSettings() {
        phantomRidable = getBoolean("mobs.phantom.ridable", phantomRidable);
        phantomRidableInWater = getBoolean("mobs.phantom.ridable-in-water", phantomRidableInWater);
        phantomMaxY = getDouble("mobs.phantom.ridable-max-y", phantomMaxY);
        phantomFlameDamage = (float) getDouble("mobs.phantom.flames.damage", phantomFlameDamage);
        phantomFlameFireTime = getInt("mobs.phantom.flames.fire-time", phantomFlameFireTime);
        phantomAttackedByCrystalRadius = getDouble("mobs.phantom.attacked-by-crystal-range", phantomAttackedByCrystalRadius);
        phantomAttackedByCrystalDamage = (float) getDouble("mobs.phantom.attacked-by-crystal-damage", phantomAttackedByCrystalDamage);
        phantomOrbitCrystalRadius = getDouble("mobs.phantom.orbit-crystal-radius", phantomOrbitCrystalRadius);
        phantomSpawnDelayMin = getInt("mobs.phantom.spawn.delay.min", phantomSpawnDelayMin);
        phantomSpawnDelayMax = getInt("mobs.phantom.spawn.delay.max", phantomSpawnDelayMax);
        phantomSpawnMinSkyDarkness = getInt("mobs.phantom.spawn.min-sky-darkness", phantomSpawnMinSkyDarkness);
        phantomSpawnOnlyAboveSeaLevel = getBoolean("mobs.phantom.spawn.only-above-sea-level", phantomSpawnOnlyAboveSeaLevel);
        phantomSpawnOnlyWithVisibleSky = getBoolean("mobs.phantom.spawn.only-with-visible-sky", phantomSpawnOnlyWithVisibleSky);
        phantomSpawnLocalDifficultyChance = getDouble("mobs.phantom.spawn.local-difficulty-chance", phantomSpawnLocalDifficultyChance);
        phantomSpawnMinTimeSinceSlept = getInt("mobs.phantom.spawn.min-time-since-slept", phantomSpawnMinTimeSinceSlept);
        phantomSpawnMinOverhead = getInt("mobs.phantom.spawn.overhead.min", phantomSpawnMinOverhead);
        phantomSpawnMaxOverhead = getInt("mobs.phantom.spawn.overhead.max", phantomSpawnMaxOverhead);
        phantomSpawnOverheadRadius = getInt("mobs.phantom.spawn.overhead.radius", phantomSpawnOverheadRadius);
        phantomSpawnMinPerAttempt = getInt("mobs.phantom.spawn.per-attempt.min", phantomSpawnMinPerAttempt);
        phantomSpawnMaxPerAttempt = getInt("mobs.phantom.spawn.per-attempt.max", phantomSpawnMaxPerAttempt);
        phantomBurnInLight = getInt("mobs.phantom.burn-in-light", phantomBurnInLight);
        phantomBurnInDaylight = getBoolean("mobs.phantom.burn-in-daylight", phantomBurnInDaylight);
        phantomIgnorePlayersWithTorch = getBoolean("mobs.phantom.ignore-players-with-torch", phantomIgnorePlayersWithTorch);
        phantomAllowGriefing = getBoolean("mobs.phantom.allow-griefing", phantomAllowGriefing);
    }

    public boolean pigRidable = false;
    public boolean pigRidableInWater = false;
    public boolean pigGiveSaddleBack = false;
    private void pigSettings() {
        pigRidable = getBoolean("mobs.pig.ridable", pigRidable);
        pigRidableInWater = getBoolean("mobs.pig.ridable-in-water", pigRidableInWater);
        pigGiveSaddleBack = getBoolean("mobs.pig.give-saddle-back", pigGiveSaddleBack);
    }

    public boolean piglinRidable = false;
    public boolean piglinRidableInWater = false;
    private void piglinSettings() {
        piglinRidable = getBoolean("mobs.piglin.ridable", piglinRidable);
        piglinRidableInWater = getBoolean("mobs.piglin.ridable-in-water", piglinRidableInWater);
    }

    public boolean piglinBruteRidable = false;
    public boolean piglinBruteRidableInWater = false;
    private void piglinBruteSettings() {
        piglinBruteRidable = getBoolean("mobs.piglin_brute.ridable", piglinBruteRidable);
        piglinBruteRidableInWater = getBoolean("mobs.piglin_brute.ridable-in-water", piglinBruteRidableInWater);
    }

    public boolean pillagerRidable = false;
    public boolean pillagerRidableInWater = false;
    private void pillagerSettings() {
        pillagerRidable = getBoolean("mobs.pillager.ridable", pillagerRidable);
        pillagerRidableInWater = getBoolean("mobs.pillager.ridable-in-water", pillagerRidableInWater);
    }

    public boolean polarBearRidable = false;
    public boolean polarBearRidableInWater = false;
    public String polarBearBreedableItemString = "";
    public Item polarBearBreedableItem = null;
    private void polarBearSettings() {
        polarBearRidable = getBoolean("mobs.polar_bear.ridable", polarBearRidable);
        polarBearRidableInWater = getBoolean("mobs.polar_bear.ridable-in-water", polarBearRidableInWater);
        polarBearBreedableItemString = getString("mobs.polar_bear.breedable-item", polarBearBreedableItemString);
        Item item = IRegistry.ITEM.get(new MinecraftKey(polarBearBreedableItemString));
        if (item != Items.AIR) polarBearBreedableItem = item;
    }

    public boolean pufferfishRidable = false;
    private void pufferfishSettings() {
        pufferfishRidable = getBoolean("mobs.pufferfish.ridable", pufferfishRidable);
    }

    public boolean rabbitRidable = false;
    public boolean rabbitRidableInWater = false;
    public double rabbitNaturalToast = 0.0D;
    public double rabbitNaturalKiller = 0.0D;
    private void rabbitSettings() {
        rabbitRidable = getBoolean("mobs.rabbit.ridable", rabbitRidable);
        rabbitRidableInWater = getBoolean("mobs.rabbit.ridable-in-water", rabbitRidableInWater);
        rabbitNaturalToast = getDouble("mobs.rabbit.spawn-toast-chance", rabbitNaturalToast);
        rabbitNaturalKiller = getDouble("mobs.rabbit.spawn-killer-rabbit-chance", rabbitNaturalKiller);
    }

    public boolean ravagerRidable = false;
    public boolean ravagerRidableInWater = false;
    private void ravagerSettings() {
        ravagerRidable = getBoolean("mobs.ravager.ridable", ravagerRidable);
        ravagerRidableInWater = getBoolean("mobs.ravager.ridable-in-water", ravagerRidableInWater);
    }

    public boolean salmonRidable = false;
    private void salmonSettings() {
        salmonRidable = getBoolean("mobs.salmon.ridable", salmonRidable);
    }

    public boolean sheepRidable = false;
    public boolean sheepRidableInWater = false;
    private void sheepSettings() {
        sheepRidable = getBoolean("mobs.sheep.ridable", sheepRidable);
        sheepRidableInWater = getBoolean("mobs.sheep.ridable-in-water", sheepRidableInWater);
    }

    public boolean shulkerRidable = false;
    public boolean shulkerRidableInWater = false;
    private void shulkerSettings() {
        shulkerRidable = getBoolean("mobs.shulker.ridable", shulkerRidable);
        shulkerRidableInWater = getBoolean("mobs.shulker.ridable-in-water", shulkerRidableInWater);
    }

    public boolean silverfishRidable = false;
    public boolean silverfishRidableInWater = false;
    private void silverfishSettings() {
        silverfishRidable = getBoolean("mobs.silverfish.ridable", silverfishRidable);
        silverfishRidableInWater = getBoolean("mobs.silverfish.ridable-in-water", silverfishRidableInWater);
    }

    public boolean skeletonRidable = false;
    public boolean skeletonRidableInWater = false;
    private void skeletonSettings() {
        skeletonRidable = getBoolean("mobs.skeleton.ridable", skeletonRidable);
        skeletonRidableInWater = getBoolean("mobs.skeleton.ridable-in-water", skeletonRidableInWater);
    }

    public boolean skeletonHorseCanSwim = false;
    public boolean skeletonHorseRidableInWater = true;
    private void skeletonHorseSettings() {
        skeletonHorseCanSwim = getBoolean("mobs.skeleton_horse.can-swim", skeletonHorseCanSwim);
        skeletonHorseRidableInWater = getBoolean("mobs.skeleton_horse.ridable-in-water", skeletonHorseRidableInWater);
    }

    public boolean slimeRidable = false;
    public boolean slimeRidableInWater = false;
    private void slimeSettings() {
        slimeRidable = getBoolean("mobs.slime.ridable", slimeRidable);
        slimeRidableInWater = getBoolean("mobs.slime.ridable-in-water", slimeRidableInWater);
    }

    public boolean snowGolemRidable = false;
    public boolean snowGolemRidableInWater = false;
    public boolean snowGolemLeaveTrailWhenRidden = false;
    public boolean snowGolemDropsPumpkin = true;
    public boolean snowGolemPutPumpkinBack = false;
    private void snowGolemSettings() {
        snowGolemRidable = getBoolean("mobs.snow_golem.ridable", snowGolemRidable);
        snowGolemRidableInWater = getBoolean("mobs.snow_golem.ridable-in-water", snowGolemRidableInWater);
        snowGolemLeaveTrailWhenRidden = getBoolean("mobs.snow_golem.leave-trail-when-ridden", snowGolemLeaveTrailWhenRidden);
        snowGolemDropsPumpkin = getBoolean("mobs.snow_golem.drop-pumpkin-when-sheared", snowGolemDropsPumpkin);
        snowGolemPutPumpkinBack = getBoolean("mobs.snow_golem.pumpkin-can-be-added-back", snowGolemPutPumpkinBack);
    }

    public boolean squidRidable = false;
    public boolean squidImmuneToEAR = true;
    public double squidOffsetWaterCheck = 0.0D;
    public boolean squidsCanFly = false;
    private void squidSettings() {
        squidRidable = getBoolean("mobs.squid.ridable", squidRidable);
        squidImmuneToEAR = getBoolean("mobs.squid.immune-to-EAR", squidImmuneToEAR);
        squidOffsetWaterCheck = getDouble("mobs.squid.water-offset-check", squidOffsetWaterCheck);
        squidsCanFly = getBoolean("mobs.squid.can-fly", squidsCanFly);
    }

    public boolean spiderRidable = false;
    public boolean spiderRidableInWater = false;
    private void spiderSettings() {
        spiderRidable = getBoolean("mobs.spider.ridable", spiderRidable);
        spiderRidableInWater = getBoolean("mobs.spider.ridable-in-water", spiderRidableInWater);
    }

    public boolean strayRidable = false;
    public boolean strayRidableInWater = false;
    private void straySettings() {
        strayRidable = getBoolean("mobs.stray.ridable", strayRidable);
        strayRidableInWater = getBoolean("mobs.stray.ridable-in-water", strayRidableInWater);
    }

    public boolean striderRidable = false;
    public boolean striderRidableInWater = false;
    private void striderSettings() {
        striderRidable = getBoolean("mobs.strider.ridable", striderRidable);
        striderRidableInWater = getBoolean("mobs.strider.ridable-in-water", striderRidableInWater);
    }

    public boolean tropicalFishRidable = false;
    private void tropicalFishSettings() {
        tropicalFishRidable = getBoolean("mobs.tropical_fish.ridable", tropicalFishRidable);
    }

    public boolean turtleRidable = false;
    public boolean turtleRidableInWater = false;
    private void turtleSettings() {
        turtleRidable = getBoolean("mobs.turtle.ridable", turtleRidable);
        turtleRidableInWater = getBoolean("mobs.turtle.ridable-in-water", turtleRidableInWater);
    }

    public boolean vexRidable = false;
    public boolean vexRidableInWater = false;
    public double vexMaxY = 256D;
    private void vexSettings() {
        vexRidable = getBoolean("mobs.vex.ridable", vexRidable);
        vexRidableInWater = getBoolean("mobs.vex.ridable-in-water", vexRidableInWater);
        vexMaxY = getDouble("mobs.vex.ridable-max-y", vexMaxY);
    }

    public boolean villagerRidable = false;
    public boolean villagerRidableInWater = false;
    public int villagerBrainTicks = 1;
    public boolean villagerUseBrainTicksOnlyWhenLagging = true;
    public boolean villagerCanBeLeashed = false;
    public boolean villagerFarmingBypassMobGriefing = false;
    public boolean villagerFollowEmeraldBlock = false;
    public int villagerSpawnIronGolemRadius = 0;
    public int villagerSpawnIronGolemLimit = 0;
    public boolean villagerCanBreed = true;
    private void villagerSettings() {
        villagerRidable = getBoolean("mobs.villager.ridable", villagerRidable);
        villagerRidableInWater = getBoolean("mobs.villager.ridable-in-water", villagerRidableInWater);
        villagerBrainTicks = getInt("mobs.villager.brain-ticks", villagerBrainTicks);
        villagerUseBrainTicksOnlyWhenLagging = getBoolean("mobs.villager.use-brain-ticks-only-when-lagging", villagerUseBrainTicksOnlyWhenLagging);
        villagerCanBeLeashed = getBoolean("mobs.villager.can-be-leashed", villagerCanBeLeashed);
        villagerFarmingBypassMobGriefing = getBoolean("mobs.villager.bypass-mob-griefing", villagerFarmingBypassMobGriefing);
        villagerFollowEmeraldBlock = getBoolean("mobs.villager.follow-emerald-blocks", villagerFollowEmeraldBlock);
        villagerSpawnIronGolemRadius = getInt("mobs.villager.spawn-iron-golem.radius", villagerSpawnIronGolemRadius);
        villagerSpawnIronGolemLimit = getInt("mobs.villager.spawn-iron-golem.limit", villagerSpawnIronGolemLimit);
        villagerCanBreed = getBoolean("mobs.villager.can-breed", villagerCanBreed);
    }

    public boolean villagerTraderRidable = false;
    public boolean villagerTraderRidableInWater = false;
    public boolean villagerTraderCanBeLeashed = false;
    public boolean villagerTraderFollowEmeraldBlock = false;
    private void villagerTraderSettings() {
        villagerTraderRidable = getBoolean("mobs.wandering_trader.ridable", villagerTraderRidable);
        villagerTraderRidableInWater = getBoolean("mobs.wandering_trader.ridable-in-water", villagerTraderRidableInWater);
        villagerTraderCanBeLeashed = getBoolean("mobs.wandering_trader.can-be-leashed", villagerTraderCanBeLeashed);
        villagerTraderFollowEmeraldBlock = getBoolean("mobs.wandering_trader.follow-emerald-blocks", villagerTraderFollowEmeraldBlock);
    }

    public boolean vindicatorRidable = false;
    public boolean vindicatorRidableInWater = false;
    public double vindicatorJohnnySpawnChance = 0D;
    private void vindicatorSettings() {
        vindicatorRidable = getBoolean("mobs.vindicator.ridable", vindicatorRidable);
        vindicatorRidableInWater = getBoolean("mobs.vindicator.ridable-in-water", vindicatorRidableInWater);
        vindicatorJohnnySpawnChance = getDouble("mobs.vindicator.johnny.spawn-chance", vindicatorJohnnySpawnChance);
    }

    public boolean witchRidable = false;
    public boolean witchRidableInWater = false;
    private void witchSettings() {
        witchRidable = getBoolean("mobs.witch.ridable", witchRidable);
        witchRidableInWater = getBoolean("mobs.witch.ridable-in-water", witchRidableInWater);
    }

    public boolean witherRidable = false;
    public boolean witherRidableInWater = false;
    public double witherMaxY = 256D;
    public float witherHealthRegenAmount = 1.0f;
    public int witherHealthRegenDelay = 20;
    public double witherMaxHealth = 300.0D;
    private void witherSettings() {
        witherRidable = getBoolean("mobs.wither.ridable", witherRidable);
        witherRidableInWater = getBoolean("mobs.wither.ridable-in-water", witherRidableInWater);
        witherMaxY = getDouble("mobs.wither.ridable-max-y", witherMaxY);
        witherHealthRegenAmount = (float) getDouble("mobs.wither.health-regen-amount", witherHealthRegenAmount);
        witherHealthRegenDelay = getInt("mobs.wither.health-regen-delay", witherHealthRegenDelay);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.wither.max-health", witherMaxHealth);
            set("mobs.wither.attributes.max-health", oldValue);
            set("mobs.wither.max-health", null);
        }
        witherMaxHealth = getDouble("mobs.wither.attributes.max-health", witherMaxHealth);
    }

    public boolean witherSkeletonRidable = false;
    public boolean witherSkeletonRidableInWater = false;
    public boolean witherSkeletonTakesWitherDamage = false;
    private void witherSkeletonSettings() {
        witherSkeletonRidable = getBoolean("mobs.wither_skeleton.ridable", witherSkeletonRidable);
        witherSkeletonRidableInWater = getBoolean("mobs.wither_skeleton.ridable-in-water", witherSkeletonRidableInWater);
        witherSkeletonTakesWitherDamage = getBoolean("mobs.wither_skeleton.takes-wither-damage", witherSkeletonTakesWitherDamage);
    }

    public boolean wolfRidable = false;
    public boolean wolfRidableInWater = false;
    private void wolfSettings() {
        wolfRidable = getBoolean("mobs.wolf.ridable", wolfRidable);
        wolfRidableInWater = getBoolean("mobs.wolf.ridable-in-water", wolfRidableInWater);
    }

    public boolean zoglinRidable = false;
    public boolean zoglinRidableInWater = false;
    private void zoglinSettings() {
        zoglinRidable = getBoolean("mobs.zoglin.ridable", zoglinRidable);
        zoglinRidableInWater = getBoolean("mobs.zoglin.ridable-in-water", zoglinRidableInWater);
    }

    public boolean zombieRidable = false;
    public boolean zombieRidableInWater = false;
    public boolean zombieJockeyOnlyBaby = true;
    public double zombieJockeyChance = 0.05D;
    public boolean zombieJockeyTryExistingChickens = true;
    public boolean zombieAggressiveTowardsVillagerWhenLagging = true;
    private void zombieSettings() {
        zombieRidable = getBoolean("mobs.zombie.ridable", zombieRidable);
        zombieRidableInWater = getBoolean("mobs.zombie.ridable-in-water", zombieRidableInWater);
        zombieJockeyOnlyBaby = getBoolean("mobs.zombie.jockey.only-babies", zombieJockeyOnlyBaby);
        zombieJockeyChance = getDouble("mobs.zombie.jockey.chance", zombieJockeyChance);
        zombieJockeyTryExistingChickens = getBoolean("mobs.zombie.jockey.try-existing-chickens", zombieJockeyTryExistingChickens);
        zombieAggressiveTowardsVillagerWhenLagging = getBoolean("mobs.zombie.aggressive-towards-villager-when-lagging", zombieAggressiveTowardsVillagerWhenLagging);
    }

    public boolean zombieHorseCanSwim = false;
    public boolean zombieHorseRidableInWater = false;
    public double zombieHorseSpawnChance = 0.0D;
    private void zombieHorseSettings() {
        zombieHorseCanSwim = getBoolean("mobs.zombie_horse.can-swim", zombieHorseCanSwim);
        zombieHorseRidableInWater = getBoolean("mobs.zombie_horse.ridable-in-water", zombieHorseRidableInWater);
        zombieHorseSpawnChance = getDouble("mobs.zombie_horse.spawn-chance", zombieHorseSpawnChance);
    }

    public boolean zombifiedPiglinRidable = false;
    public boolean zombifiedPiglinRidableInWater = false;
    public boolean zombifiedPiglinJockeyOnlyBaby = true;
    public double zombifiedPiglinJockeyChance = 0.05D;
    public boolean zombifiedPiglinJockeyTryExistingChickens = true;
    private void zombifiedPiglinSettings() {
        zombifiedPiglinRidable = getBoolean("mobs.zombified_piglin.ridable", zombifiedPiglinRidable);
        zombifiedPiglinRidableInWater = getBoolean("mobs.zombified_piglin.ridable-in-water", zombifiedPiglinRidableInWater);
        zombifiedPiglinJockeyOnlyBaby = getBoolean("mobs.zombified_piglin.jockey.only-babies", zombifiedPiglinJockeyOnlyBaby);
        zombifiedPiglinJockeyChance = getDouble("mobs.zombified_piglin.jockey.chance", zombifiedPiglinJockeyChance);
        zombifiedPiglinJockeyTryExistingChickens = getBoolean("mobs.zombified_piglin.jockey.try-existing-chickens", zombifiedPiglinJockeyTryExistingChickens);
    }

    public boolean zombieVillagerRidable = false;
    public boolean zombieVillagerRidableInWater = false;
    public boolean zombieVillagerJockeyOnlyBaby = true;
    public double zombieVillagerJockeyChance = 0.05D;
    public boolean zombieVillagerJockeyTryExistingChickens = true;
    private void zombieVillagerSettings() {
        zombieVillagerRidable = getBoolean("mobs.zombie_villager.ridable", zombieVillagerRidable);
        zombieVillagerRidableInWater = getBoolean("mobs.zombie_villager.ridable-in-water", zombieVillagerRidableInWater);
        zombieVillagerJockeyOnlyBaby = getBoolean("mobs.zombie_villager.jockey.only-babies", zombieVillagerJockeyOnlyBaby);
        zombieVillagerJockeyChance = getDouble("mobs.zombie_villager.jockey.chance", zombieVillagerJockeyChance);
        zombieVillagerJockeyTryExistingChickens = getBoolean("mobs.zombie_villager.jockey.try-existing-chickens", zombieVillagerJockeyTryExistingChickens);
    }
}
