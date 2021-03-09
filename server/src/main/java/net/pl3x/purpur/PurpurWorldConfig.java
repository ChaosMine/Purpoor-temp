package net.pl3x.purpur;

import com.destroystokyo.paper.PaperConfig;
import net.minecraft.core.IRegistry;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.item.EnumColor;
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

    private void migrateDisableProjectileSaving() {
        if (PurpurConfig.version < 6) {
            final boolean saveProjectilesToDisk = PurpurConfig.config.getBoolean("world-settings." + worldName + ".gameplay-mechanics.save-projectiles-to-disk", true);
            PurpurConfig.config.set("world-settings." + worldName + ".gameplay-mechanics.save-projectiles-to-disk", null);
            if (!saveProjectilesToDisk) {
                PaperConfig.config.set("world-settings." + worldName + ".projectile-load-save-per-chunk-limit", 0);
                PaperConfig.saveConfig();
            }
        }
    }

    private void migrateClimbingCrammingFix() {
        if (PurpurConfig.version < 7) {
            final boolean climbingCrammingFix = PurpurConfig.config.getBoolean("world-settings." + worldName + ".gameplay-mechanics.fix-climbing-bypassing-cramming-rule", false);
            PurpurConfig.config.set("world-settings." + worldName + ".gameplay-mechanics.fix-climbing-bypassing-cramming-rule", null);
            if (climbingCrammingFix) {
                PaperConfig.config.set("world-settings." + worldName + ".fix-climbing-bypassing-cramming-rule", true);
                PaperConfig.saveConfig();
            }
        }
    }

    public boolean armorstandSetNameVisible = false;
    public boolean armorstandFixNametags = false;
    public float armorstandStepHeight = 0.0F;
    public boolean armorstandMovement = true;
    public boolean armorstandWaterMovement = true;
    public boolean armorstandWaterFence = true;
    private void armorstandSettings() {
        armorstandSetNameVisible = getBoolean("gameplay-mechanics.armorstand.set-name-visible-when-placing-with-custom-name", armorstandSetNameVisible);
        armorstandFixNametags = getBoolean("gameplay-mechanics.armorstand.fix-nametags", armorstandFixNametags);
        armorstandStepHeight = (float) getDouble("gameplay-mechanics.armorstand.step-height", armorstandStepHeight);
        armorstandMovement = getBoolean("gameplay-mechanics.armorstand.can-movement-tick", armorstandMovement);
        armorstandWaterMovement = getBoolean("gameplay-mechanics.armorstand.can-move-in-water", armorstandWaterMovement);
        armorstandWaterFence = getBoolean("gameplay-mechanics.armorstand.can-move-in-water-over-fence", armorstandWaterFence);
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
    public float entityLeftHandedChance = 0.05f;
    private void entitySettings() {
        entityLifeSpan = getInt("gameplay-mechanics.entity-lifespan", entityLifeSpan);
        entityLeftHandedChance = (float) getDouble("gameplay-mechanics.entity-left-handed-chance", entityLeftHandedChance);
    }

    public List<Item> itemImmuneToCactus = new ArrayList<>();
    public List<Item> itemImmuneToExplosion = new ArrayList<>();
    public List<Item> itemImmuneToFire = new ArrayList<>();
    public List<Item> itemImmuneToLightning = new ArrayList<>();
    public boolean dontRunWithScissors = false;
    public double scissorsRunningDamage = 1D;
    public float enderPearlDamage = 5.0F;
    public int enderPearlCooldown = 20;
    public int enderPearlCooldownCreative = 20;
    public float enderPearlEndermiteChance = 0.05F;
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
        dontRunWithScissors = getBoolean("gameplay-mechanics.item.shears.damage-if-sprinting", dontRunWithScissors);
        scissorsRunningDamage = getDouble("gameplay-mechanics.item.shears.sprinting-damage", scissorsRunningDamage);
        enderPearlDamage = (float) getDouble("gameplay-mechanics.item.ender-pearl.damage", enderPearlDamage);
        enderPearlCooldown = getInt("gameplay-mechanics.item.ender-pearl.cooldown", enderPearlCooldown);
        enderPearlCooldownCreative = getInt("gameplay-mechanics.item.ender-pearl.creative-cooldown", enderPearlCooldownCreative);
        enderPearlEndermiteChance = (float) getDouble("gameplay-mechanics.item.ender-pearl.endermite-spawn-chance", enderPearlEndermiteChance);
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

    public boolean infinityWorksWithoutArrows = false;
    public boolean infinityWorksWithNormalArrows = true;
    public boolean infinityWorksWithSpectralArrows = false;
    public boolean infinityWorksWithTippedArrows = false;
    private void infinityArrowsSettings() {
        infinityWorksWithoutArrows = getBoolean("gameplay-mechanics.infinity-bow.works-without-arrows", infinityWorksWithoutArrows);
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

    public int snowballDamage = -1;
    private void snowballSettings() {
        snowballDamage = getInt("gameplay-mechanics.projectile-damage.snowball", snowballDamage);
    }

    public boolean useBetterMending = false;
    public boolean alwaysTameInCreative = false;
    public boolean boatEjectPlayersOnLand = false;
    public boolean boatsDoFallDamage = true;
    public boolean disableDropsOnCrammingDeath = false;
    public boolean entitiesPickUpLootBypassMobGriefing = false;
    public boolean entitiesCanUsePortals = true;
    public boolean fireballsBypassMobGriefing = false;
    public boolean milkCuresBadOmen = true;
    public boolean noteBlockIgnoreAbove = false;
    public boolean persistentTileEntityDisplayNames = false;
    public boolean persistentDroppableEntityDisplayNames = false;
    public double tridentLoyaltyVoidReturnHeight = 0.0D;
    public double voidDamageHeight = -64.0D;
    public double voidDamageDealt = 4.0D;
    public int raidCooldownSeconds = 0;
    public int animalBreedingCooldownSeconds = 0;
    public boolean creativeOnePunch = false;
    public boolean playerSleepNearMonsters = false;
    private void miscGameplayMechanicsSettings() {
        useBetterMending = getBoolean("gameplay-mechanics.use-better-mending", useBetterMending);
        alwaysTameInCreative = getBoolean("gameplay-mechanics.always-tame-in-creative", alwaysTameInCreative);
        boatEjectPlayersOnLand = getBoolean("gameplay-mechanics.boat.eject-players-on-land", boatEjectPlayersOnLand);
        boatsDoFallDamage = getBoolean("gameplay-mechanics.boat.do-fall-damage", boatsDoFallDamage);
        disableDropsOnCrammingDeath = getBoolean("gameplay-mechanics.disable-drops-on-cramming-death", disableDropsOnCrammingDeath);
        entitiesPickUpLootBypassMobGriefing = getBoolean("gameplay-mechanics.entities-pick-up-loot-bypass-mob-griefing", entitiesPickUpLootBypassMobGriefing);
        entitiesCanUsePortals = getBoolean("gameplay-mechanics.entities-can-use-portals", entitiesCanUsePortals);
        fireballsBypassMobGriefing = getBoolean("gameplay-mechanics.fireballs-bypass-mob-griefing", fireballsBypassMobGriefing);
        milkCuresBadOmen = getBoolean("gameplay-mechanics.milk-cures-bad-omen", milkCuresBadOmen);
        noteBlockIgnoreAbove = getBoolean("gameplay-mechanics.note-block-ignore-above", noteBlockIgnoreAbove);
        persistentTileEntityDisplayNames = getBoolean("gameplay-mechanics.persistent-tileentity-display-names-and-lore", persistentTileEntityDisplayNames);
        persistentDroppableEntityDisplayNames = getBoolean("gameplay-mechanics.persistent-droppable-entity-display-names", persistentDroppableEntityDisplayNames);
        tridentLoyaltyVoidReturnHeight = getDouble("gameplay-mechanics.trident-loyalty-void-return-height", tridentLoyaltyVoidReturnHeight);
        voidDamageHeight = getDouble("gameplay-mechanics.void-damage-height", voidDamageHeight);
        voidDamageDealt = getDouble("gameplay-mechanics.void-damage-dealt", voidDamageDealt);
        raidCooldownSeconds = getInt("gameplay-mechanics.raid-cooldown-seconds", raidCooldownSeconds);
        animalBreedingCooldownSeconds = getInt("gameplay-mechanics.animal-breeding-cooldown-seconds", animalBreedingCooldownSeconds);
        creativeOnePunch = getBoolean("gameplay-mechanics.player.one-punch-in-creative", creativeOnePunch);
        playerSleepNearMonsters = getBoolean("gameplay-mechanics.player.sleep-ignore-nearby-mobs", playerSleepNearMonsters);
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

    public int playerNetheriteFireResistanceDuration = 0;
    public int playerNetheriteFireResistanceAmplifier = 0;
    public boolean playerNetheriteFireResistanceAmbient = false;
    public boolean playerNetheriteFireResistanceShowParticles = false;
    public boolean playerNetheriteFireResistanceShowIcon = true;
    private void playerNetheriteFireResistance() {
        playerNetheriteFireResistanceDuration = getInt("gameplay-mechanics.player.netherite-fire-resistance.duration", playerNetheriteFireResistanceDuration);
        playerNetheriteFireResistanceAmplifier = getInt("gameplay-mechanics.player.netherite-fire-resistance.amplifier", playerNetheriteFireResistanceAmplifier);
        playerNetheriteFireResistanceAmbient = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.ambient", playerNetheriteFireResistanceAmbient);
        playerNetheriteFireResistanceShowParticles = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.show-particles", playerNetheriteFireResistanceShowParticles);
        playerNetheriteFireResistanceShowIcon = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.show-icon", playerNetheriteFireResistanceShowIcon);
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

    public boolean playerFixStuckPortal = false;
    private void playerFixStuckPortal() {
        playerFixStuckPortal = getBoolean("gameplay-mechanics.player.fix-stuck-in-portal", playerFixStuckPortal);
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

    public boolean snowOnBlueIce = true;
    private void iceSettings() {
        snowOnBlueIce = getBoolean("blocks.blue_ice.allow-snow-formation", snowOnBlueIce);
    }

    public boolean chestOpenWithBlockOnTop = false;
    private void chestSettings() {
        chestOpenWithBlockOnTop = getBoolean("blocks.chest.open-with-solid-block-on-top", chestOpenWithBlockOnTop);
    }

    public boolean dispenserApplyCursedArmor = true;
    public boolean dispenserPlaceAnvils = false;
    private void dispenserSettings() {
        dispenserApplyCursedArmor = getBoolean("blocks.dispenser.apply-cursed-to-armor-slots", dispenserApplyCursedArmor);
        dispenserPlaceAnvils = getBoolean("blocks.dispenser.place-anvils", dispenserPlaceAnvils);
    }

    public boolean farmlandBypassMobGriefing = false;
    public boolean farmlandGetsMoistFromBelow = false;
    public boolean farmlandAlpha = false;
    public boolean farmlandTramplingDisabled = false;
    public boolean farmlandTramplingOnlyPlayers = false;
    public boolean farmlandTramplingFeatherFalling = false;
    private void farmlandSettings() {
        farmlandBypassMobGriefing = getBoolean("blocks.farmland.bypass-mob-griefing", farmlandBypassMobGriefing);
        farmlandGetsMoistFromBelow = getBoolean("blocks.farmland.gets-moist-from-below", farmlandGetsMoistFromBelow);
        farmlandAlpha = getBoolean("blocks.farmland.use-alpha-farmland", farmlandAlpha);
        farmlandTramplingDisabled = getBoolean("blocks.farmland.disable-trampling", farmlandTramplingDisabled);
        farmlandTramplingOnlyPlayers = getBoolean("blocks.farmland.only-players-trample", farmlandTramplingOnlyPlayers);
        farmlandTramplingFeatherFalling = getBoolean("blocks.farmland.feather-fall-distance-affects-trampling", farmlandTramplingFeatherFalling);
    }

    public boolean furnaceInfiniteFuel = false;
    private void furnaceSettings() {
        furnaceInfiniteFuel = getBoolean("blocks.furnace.infinite-fuel", furnaceInfiniteFuel);
    }

    public int kelpMaxGrowthAge = 25;
    private void kelpSettings() {
        kelpMaxGrowthAge = getInt("blocks.kelp.max-growth-age", kelpMaxGrowthAge);
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

    public boolean turtleEggsBypassMobGriefing = false;
    public boolean turtleEggsBreakFromExpOrbs = true;
    public boolean turtleEggsBreakFromItems = true;
    public boolean turtleEggsBreakFromMinecarts = true;
    private void turtleEggSettings() {
        turtleEggsBypassMobGriefing = getBoolean("blocks.turtle_egg.bypass-mob-griefing", turtleEggsBypassMobGriefing);
        turtleEggsBreakFromExpOrbs = getBoolean("blocks.turtle_egg.break-from-exp-orbs", turtleEggsBreakFromExpOrbs);
        turtleEggsBreakFromItems = getBoolean("blocks.turtle_egg.break-from-items", turtleEggsBreakFromItems);
        turtleEggsBreakFromMinecarts = getBoolean("blocks.turtle_egg.break-from-minecarts", turtleEggsBreakFromMinecarts);
    }

    public double twistingVinesGrowthModifier = 0.10D;
    public int twistingVinesMaxGrowthAge = 25;
    private void twistingVinesSettings() {
        twistingVinesGrowthModifier = getDouble("blocks.twisting_vines.growth-modifier", twistingVinesGrowthModifier);
        twistingVinesMaxGrowthAge = getInt("blocks.twisting_vines.max-growth-age", twistingVinesMaxGrowthAge);
    }

    public double weepingVinesGrowthModifier = 0.10D;
    public int weepingVinesMaxGrowthAge = 25;
    private void weepingVinesSettings() {
        weepingVinesGrowthModifier = getDouble("blocks.weeping_vines.growth-modifier", weepingVinesGrowthModifier);
        weepingVinesMaxGrowthAge = getInt("blocks.weeping_vines.max-growth-age", weepingVinesMaxGrowthAge);
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
    public double batMaxHealth = 6.0D;
    public double batFollowRange = 16.0D;
    public double batKnockbackResistance = 0.0D;
    public double batMovementSpeed = 0.6D;
    public double batFlyingSpeed = 0.6D;
    public double batArmor = 0.0D;
    public double batArmorToughness = 0.0D;
    public double batAttackKnockback = 0.0D;
    private void batSettings() {
        batRidable = getBoolean("mobs.bat.ridable", batRidable);
        batRidableInWater = getBoolean("mobs.bat.ridable-in-water", batRidableInWater);
        batMaxY = getDouble("mobs.bat.ridable-max-y", batMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.bat.attributes.max-health", batMaxHealth);
            set("mobs.bat.attributes.max-health", null);
            set("mobs.bat.attributes.max_health", oldValue);
        }
        batMaxHealth = getDouble("mobs.bat.attributes.max_health", batMaxHealth);
    }

    public boolean beeRidable = false;
    public boolean beeRidableInWater = false;
    public double beeMaxY = 256D;
    public int beeBreedingTicks = 6000;
    public double beeMaxHealth = 10.0D;
    private void beeSettings() {
        beeRidable = getBoolean("mobs.bee.ridable", beeRidable);
        beeRidableInWater = getBoolean("mobs.bee.ridable-in-water", beeRidableInWater);
        beeMaxY = getDouble("mobs.bee.ridable-max-y", beeMaxY);
        beeBreedingTicks = getInt("mobs.bee.breeding-delay-ticks", beeBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.bee.attributes.max-health", beeMaxHealth);
            set("mobs.bee.attributes.max-health", null);
            set("mobs.bee.attributes.max_health", oldValue);
        }
        beeMaxHealth = getDouble("mobs.bee.attributes.max_health", beeMaxHealth);
    }

    public boolean blazeRidable = false;
    public boolean blazeRidableInWater = false;
    public double blazeMaxY = 256D;
    public double blazeMaxHealth = 20.0D;
    public boolean blazeTakeDamageFromWater = true;
    private void blazeSettings() {
        blazeRidable = getBoolean("mobs.blaze.ridable", blazeRidable);
        blazeRidableInWater = getBoolean("mobs.blaze.ridable-in-water", blazeRidableInWater);
        blazeMaxY = getDouble("mobs.blaze.ridable-max-y", blazeMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.blaze.attributes.max-health", blazeMaxHealth);
            set("mobs.blaze.attributes.max-health", null);
            set("mobs.blaze.attributes.max_health", oldValue);
        }
        blazeMaxHealth = getDouble("mobs.blaze.attributes.max_health", blazeMaxHealth);
        blazeTakeDamageFromWater = getBoolean("mobs.blaze.takes-damage-from-water", blazeTakeDamageFromWater);
    }

    public boolean catRidable = false;
    public boolean catRidableInWater = false;
    public int catSpawnDelay = 1200;
    public int catSpawnSwampHutScanRange = 16;
    public int catSpawnVillageScanRange = 48;
    public int catBreedingTicks = 6000;
    public double catMaxHealth = 10.0D;
    private void catSettings() {
        catRidable = getBoolean("mobs.cat.ridable", catRidable);
        catRidableInWater = getBoolean("mobs.cat.ridable-in-water", catRidableInWater);
        catSpawnDelay = getInt("mobs.cat.spawn-delay", catSpawnDelay);
        catSpawnSwampHutScanRange = getInt("mobs.cat.scan-range-for-other-cats.swamp-hut", catSpawnSwampHutScanRange);
        catSpawnVillageScanRange = getInt("mobs.cat.scan-range-for-other-cats.village", catSpawnVillageScanRange);
        catBreedingTicks = getInt("mobs.cat.breeding-delay-ticks", catBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cat.attributes.max-health", catMaxHealth);
            set("mobs.cat.attributes.max-health", null);
            set("mobs.cat.attributes.max_health", oldValue);
        }
        catMaxHealth = getDouble("mobs.cat.attributes.max_health", catMaxHealth);
    }

    public boolean caveSpiderRidable = false;
    public boolean caveSpiderRidableInWater = false;
    public double caveSpiderMaxHealth = 12.0D;
    private void caveSpiderSettings() {
        caveSpiderRidable = getBoolean("mobs.cave_spider.ridable", caveSpiderRidable);
        caveSpiderRidableInWater = getBoolean("mobs.cave_spider.ridable-in-water", caveSpiderRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cave_spider.attributes.max-health", caveSpiderMaxHealth);
            set("mobs.cave_spider.attributes.max-health", null);
            set("mobs.cave_spider.attributes.max_health", oldValue);
        }
        caveSpiderMaxHealth = getDouble("mobs.cave_spider.attributes.max_health", caveSpiderMaxHealth);
    }

    public boolean chickenRidable = false;
    public boolean chickenRidableInWater = false;
    public boolean chickenRetaliate = false;
    public int chickenBreedingTicks = 6000;
    public double chickenMaxHealth = 4.0D;
    private void chickenSettings() {
        chickenRidable = getBoolean("mobs.chicken.ridable", chickenRidable);
        chickenRidableInWater = getBoolean("mobs.chicken.ridable-in-water", chickenRidableInWater);
        chickenRetaliate = getBoolean("mobs.chicken.retaliate", chickenRetaliate);
        chickenBreedingTicks = getInt("mobs.chicken.breeding-delay-ticks", chickenBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.chicken.attributes.max-health", chickenMaxHealth);
            set("mobs.chicken.attributes.max-health", null);
            set("mobs.chicken.attributes.max_health", oldValue);
        }
        chickenMaxHealth = getDouble("mobs.chicken.attributes.max_health", chickenMaxHealth);
    }

    public boolean codRidable = false;
    public double codMaxHealth = 3.0D;
    private void codSettings() {
        codRidable = getBoolean("mobs.cod.ridable", codRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cod.attributes.max-health", codMaxHealth);
            set("mobs.cod.attributes.max-health", null);
            set("mobs.cod.attributes.max_health", oldValue);
        }
        codMaxHealth = getDouble("mobs.cod.attributes.max_health", codMaxHealth);
    }

    public boolean cowRidable = false;
    public boolean cowRidableInWater = false;
    public int cowFeedMushrooms = 0;
    public int cowBreedingTicks = 6000;
    public double cowMaxHealth = 10.0D;
    private void cowSettings() {
        cowRidable = getBoolean("mobs.cow.ridable", cowRidable);
        cowRidableInWater = getBoolean("mobs.cow.ridable-in-water", cowRidableInWater);
        cowFeedMushrooms = getInt("mobs.cow.feed-mushrooms-for-mooshroom", cowFeedMushrooms);
        cowBreedingTicks = getInt("mobs.cow.breeding-delay-ticks", cowBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cow.attributes.max-health", cowMaxHealth);
            set("mobs.cow.attributes.max-health", null);
            set("mobs.cow.attributes.max_health", oldValue);
        }
        cowMaxHealth = getDouble("mobs.cow.attributes.max_health", cowMaxHealth);
    }

    public boolean creeperRidable = false;
    public boolean creeperRidableInWater = false;
    public boolean creeperAllowGriefing = true;
    public double creeperChargedChance = 0.0D;
    public double creeperMaxHealth = 20.0D;
    private void creeperSettings() {
        creeperRidable = getBoolean("mobs.creeper.ridable", creeperRidable);
        creeperRidableInWater = getBoolean("mobs.creeper.ridable-in-water", creeperRidableInWater);
        creeperAllowGriefing = getBoolean("mobs.creeper.allow-griefing", creeperAllowGriefing);
        creeperChargedChance = getDouble("mobs.creeper.naturally-charged-chance", creeperChargedChance);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.creeper.attributes.max-health", creeperMaxHealth);
            set("mobs.creeper.attributes.max-health", null);
            set("mobs.creeper.attributes.max_health", oldValue);
        }
        creeperMaxHealth = getDouble("mobs.creeper.attributes.max_health", creeperMaxHealth);
    }

    public boolean dolphinRidable = false;
    public int dolphinSpitCooldown = 20;
    public float dolphinSpitSpeed = 1.0F;
    public float dolphinSpitDamage = 2.0F;
    public boolean dolphinDisableTreasureSearching = false;
    public double dolphinMaxHealth = 10.0D;
    private void dolphinSettings() {
        dolphinRidable = getBoolean("mobs.dolphin.ridable", dolphinRidable);
        dolphinSpitCooldown = getInt("mobs.dolphin.spit.cooldown", dolphinSpitCooldown);
        dolphinSpitSpeed = (float) getDouble("mobs.dolphin.spit.speed", dolphinSpitSpeed);
        dolphinSpitDamage = (float) getDouble("mobs.dolphin.spit.damage", dolphinSpitDamage);
        dolphinDisableTreasureSearching = getBoolean("mobs.dolphin.disable-treasure-searching", dolphinDisableTreasureSearching);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.dolphin.attributes.max-health", dolphinMaxHealth);
            set("mobs.dolphin.attributes.max-health", null);
            set("mobs.dolphin.attributes.max_health", oldValue);
        }
        dolphinMaxHealth = getDouble("mobs.dolphin.attributes.max_health", dolphinMaxHealth);
    }

    public boolean donkeyRidableInWater = false;
    public int donkeyBreedingTicks = 6000;
    public double donkeyMaxHealthMin = 15.0D;
    public double donkeyMaxHealthMax = 30.0D;
    public double donkeyJumpStrengthMin = 0.5D;
    public double donkeyJumpStrengthMax = 0.5D;
    public double donkeyMovementSpeedMin = 0.175D;
    public double donkeyMovementSpeedMax = 0.175D;
    private void donkeySettings() {
        donkeyRidableInWater = getBoolean("mobs.donkey.ridable-in-water", donkeyRidableInWater);
        donkeyBreedingTicks = getInt("mobs.donkey.breeding-delay-ticks", donkeyBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.donkey.attributes.max-health.min", donkeyMaxHealthMin);
            double oldMax = getDouble("mobs.donkey.attributes.max-health.max", donkeyMaxHealthMax);
            set("mobs.donkey.attributes.max-health", null);
            set("mobs.donkey.attributes.max_health.min", oldMin);
            set("mobs.donkey.attributes.max_health.max", oldMax);
        }
        donkeyMaxHealthMin = getDouble("mobs.donkey.attributes.max_health.min", donkeyMaxHealthMin);
        donkeyMaxHealthMax = getDouble("mobs.donkey.attributes.max_health.max", donkeyMaxHealthMax);
        donkeyJumpStrengthMin = getDouble("mobs.donkey.attributes.jump_strength.min", donkeyJumpStrengthMin);
        donkeyJumpStrengthMax = getDouble("mobs.donkey.attributes.jump_strength.max", donkeyJumpStrengthMax);
        donkeyMovementSpeedMin = getDouble("mobs.donkey.attributes.movement_speed.min", donkeyMovementSpeedMin);
        donkeyMovementSpeedMax = getDouble("mobs.donkey.attributes.movement_speed.max", donkeyMovementSpeedMax);
    }

    public boolean drownedRidable = false;
    public boolean drownedRidableInWater = false;
    public boolean drownedJockeyOnlyBaby = true;
    public double drownedJockeyChance = 0.05D;
    public boolean drownedJockeyTryExistingChickens = true;
    public double drownedMaxHealth = 20.0D;
    public double drownedSpawnReinforcements = 0.1D;
    private void drownedSettings() {
        drownedRidable = getBoolean("mobs.drowned.ridable", drownedRidable);
        drownedRidableInWater = getBoolean("mobs.drowned.ridable-in-water", drownedRidableInWater);
        drownedJockeyOnlyBaby = getBoolean("mobs.drowned.jockey.only-babies", drownedJockeyOnlyBaby);
        drownedJockeyChance = getDouble("mobs.drowned.jockey.chance", drownedJockeyChance);
        drownedJockeyTryExistingChickens = getBoolean("mobs.drowned.jockey.try-existing-chickens", drownedJockeyTryExistingChickens);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.drowned.attributes.max-health", drownedMaxHealth);
            set("mobs.drowned.attributes.max-health", null);
            set("mobs.drowned.attributes.max_health", oldValue);
        }
        drownedMaxHealth = getDouble("mobs.drowned.attributes.max_health", drownedMaxHealth);
        drownedSpawnReinforcements = getDouble("mobs.drowned.attributes.spawn_reinforcements", drownedSpawnReinforcements);
    }

    public boolean elderGuardianRidable = false;
    public double elderGuardianMaxHealth = 80.0D;
    private void elderGuardianSettings() {
        elderGuardianRidable = getBoolean("mobs.elder_guardian.ridable", elderGuardianRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.elder_guardian.attributes.max-health", elderGuardianMaxHealth);
            set("mobs.elder_guardian.attributes.max-health", null);
            set("mobs.elder_guardian.attributes.max_health", oldValue);
        }
        elderGuardianMaxHealth = getDouble("mobs.elder_guardian.attributes.max_health", elderGuardianMaxHealth);
    }

    public boolean enderDragonRidable = false;
    public boolean enderDragonRidableInWater = false;
    public double enderDragonMaxY = 256D;
    public boolean enderDragonAlwaysDropsFullExp = false;
    public boolean enderDragonBypassMobGriefing = false;
    public double enderDragonMaxHealth = 200.0D;
    public boolean enderDragonCanRideVehicles = false;
    private void enderDragonSettings() {
        enderDragonRidable = getBoolean("mobs.ender_dragon.ridable", enderDragonRidable);
        enderDragonRidableInWater = getBoolean("mobs.ender_dragon.ridable-in-water", enderDragonRidableInWater);
        enderDragonMaxY = getDouble("mobs.ender_dragon.ridable-max-y", enderDragonMaxY);
        enderDragonAlwaysDropsFullExp = getBoolean("mobs.ender_dragon.always-drop-full-exp", enderDragonAlwaysDropsFullExp);
        enderDragonBypassMobGriefing = getBoolean("mobs.ender_dragon.bypass-mob-griefing", enderDragonBypassMobGriefing);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.ender_dragon.max-health", enderDragonMaxHealth);
            set("mobs.ender_dragon.max-health", null);
            set("mobs.ender_dragon.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ender_dragon.attributes.max-health", enderDragonMaxHealth);
            set("mobs.ender_dragon.attributes.max-health", null);
            set("mobs.ender_dragon.attributes.max_health", oldValue);
        }
        enderDragonMaxHealth = getDouble("mobs.ender_dragon.attributes.max_health", enderDragonMaxHealth);
        enderDragonCanRideVehicles = getBoolean("mobs.ender_dragon.can-ride-vehicles", enderDragonCanRideVehicles);
    }

    public boolean endermanRidable = false;
    public boolean endermanRidableInWater = false;
    public boolean endermanAllowGriefing = true;
    public boolean endermanBypassMobGriefing = false;
    public boolean endermanDespawnEvenWithBlock = false;
    public double endermanMaxHealth = 40.0D;
    public boolean endermanTakeDamageFromWater = true;
    public boolean endermanAggroSpawnedEndermites = false;
    private void endermanSettings() {
        endermanRidable = getBoolean("mobs.enderman.ridable", endermanRidable);
        endermanRidableInWater = getBoolean("mobs.enderman.ridable-in-water", endermanRidableInWater);
        endermanAllowGriefing = getBoolean("mobs.enderman.allow-griefing", endermanAllowGriefing);
        endermanBypassMobGriefing = getBoolean("mobs.enderman.bypass-mob-griefing", endermanBypassMobGriefing);
        endermanDespawnEvenWithBlock = getBoolean("mobs.enderman.can-despawn-with-held-block", endermanDespawnEvenWithBlock);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.enderman.attributes.max-health", endermanMaxHealth);
            set("mobs.enderman.attributes.max-health", null);
            set("mobs.enderman.attributes.max_health", oldValue);
        }
        endermanMaxHealth = getDouble("mobs.enderman.attributes.max_health", endermanMaxHealth);
        endermanTakeDamageFromWater = getBoolean("mobs.enderman.takes-damage-from-water", endermanTakeDamageFromWater);
        endermanAggroSpawnedEndermites = getBoolean("mobs.enderman.aggressive-towards-spawned-endermites", endermanAggroSpawnedEndermites);
    }

    public boolean endermiteRidable = false;
    public boolean endermiteRidableInWater = false;
    public double endermiteMaxHealth = 8.0D;
    private void endermiteSettings() {
        endermiteRidable = getBoolean("mobs.endermite.ridable", endermiteRidable);
        endermiteRidableInWater = getBoolean("mobs.endermite.ridable-in-water", endermiteRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.endermite.attributes.max-health", endermiteMaxHealth);
            set("mobs.endermite.attributes.max-health", null);
            set("mobs.endermite.attributes.max_health", oldValue);
        }
        endermiteMaxHealth = getDouble("mobs.endermite.attributes.max_health", endermiteMaxHealth);
    }

    public boolean evokerRidable = false;
    public boolean evokerRidableInWater = false;
    public boolean evokerBypassMobGriefing = false;
    public double evokerMaxHealth = 24.0D;
    private void evokerSettings() {
        evokerRidable = getBoolean("mobs.evoker.ridable", evokerRidable);
        evokerRidableInWater = getBoolean("mobs.evoker.ridable-in-water", evokerRidableInWater);
        evokerBypassMobGriefing = getBoolean("mobs.evoker.bypass-mob-griefing", evokerBypassMobGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.evoker.attributes.max-health", evokerMaxHealth);
            set("mobs.evoker.attributes.max-health", null);
            set("mobs.evoker.attributes.max_health", oldValue);
        }
        evokerMaxHealth = getDouble("mobs.evoker.attributes.max_health", evokerMaxHealth);
    }

    public boolean foxRidable = false;
    public boolean foxRidableInWater = false;
    public boolean foxBypassMobGriefing = false;
    public boolean foxTypeChangesWithTulips = false;
    public int foxBreedingTicks = 6000;
    public double foxMaxHealth = 10.0D;
    private void foxSettings() {
        foxRidable = getBoolean("mobs.fox.ridable", foxRidable);
        foxRidableInWater = getBoolean("mobs.fox.ridable-in-water", foxRidableInWater);
        foxBypassMobGriefing = getBoolean("mobs.fox.bypass-mob-griefing", foxBypassMobGriefing);
        foxTypeChangesWithTulips = getBoolean("mobs.fox.tulips-change-type", foxTypeChangesWithTulips);
        foxBreedingTicks = getInt("mobs.fox.breeding-delay-ticks", foxBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.fox.attributes.max-health", foxMaxHealth);
            set("mobs.fox.attributes.max-health", null);
            set("mobs.fox.attributes.max_health", oldValue);
        }
        foxMaxHealth = getDouble("mobs.fox.attributes.max_health", foxMaxHealth);
    }

    public boolean ghastRidable = false;
    public boolean ghastRidableInWater = false;
    public double ghastMaxY = 256D;
    public boolean ghastAllowGriefing = true;
    public double ghastMaxHealth = 10.0D;
    private void ghastSettings() {
        ghastRidable = getBoolean("mobs.ghast.ridable", ghastRidable);
        ghastRidableInWater = getBoolean("mobs.ghast.ridable-in-water", ghastRidableInWater);
        ghastMaxY = getDouble("mobs.ghast.ridable-max-y", ghastMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ghast.attributes.max-health", ghastMaxHealth);
            set("mobs.ghast.attributes.max-health", null);
            set("mobs.ghast.attributes.max_health", oldValue);
        }
        ghastAllowGriefing = getBoolean("mobs.ghast.allow-griefing", ghastAllowGriefing);
        ghastMaxHealth = getDouble("mobs.ghast.attributes.max_health", ghastMaxHealth);
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
            set("mobs.giant.max-health", null);
            set("mobs.giant.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.giant.attributes.max-health", giantMaxHealth);
            set("mobs.giant.attributes.max-health", null);
            set("mobs.giant.attributes.max_health", oldValue);
        }
        giantMaxHealth = getDouble("mobs.giant.attributes.max_health", giantMaxHealth);
    }

    public boolean guardianRidable = false;
    public double guardianMaxHealth = 30.0D;
    private void guardianSettings() {
        guardianRidable = getBoolean("mobs.guardian.ridable", guardianRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.guardian.attributes.max-health", guardianMaxHealth);
            set("mobs.guardian.attributes.max-health", null);
            set("mobs.guardian.attributes.max_health", oldValue);
        }
        guardianMaxHealth = getDouble("mobs.guardian.attributes.max_health", guardianMaxHealth);
    }

    public boolean hoglinRidable = false;
    public boolean hoglinRidableInWater = false;
    public int hoglinBreedingTicks = 6000;
    public double hoglinMaxHealth = 40.0D;
    private void hoglinSettings() {
        hoglinRidable = getBoolean("mobs.hoglin.ridable", hoglinRidable);
        hoglinRidableInWater = getBoolean("mobs.hoglin.ridable-in-water", hoglinRidableInWater);
        hoglinBreedingTicks = getInt("mobs.hoglin.breeding-delay-ticks", hoglinBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.hoglin.attributes.max-health", hoglinMaxHealth);
            set("mobs.hoglin.attributes.max-health", null);
            set("mobs.hoglin.attributes.max_health", oldValue);
        }
        hoglinMaxHealth = getDouble("mobs.hoglin.attributes.max_health", hoglinMaxHealth);
    }

    public boolean horseRidableInWater = false;
    public int horseBreedingTicks = 6000;
    public double horseMaxHealthMin = 15.0D;
    public double horseMaxHealthMax = 30.0D;
    public double horseJumpStrengthMin = 0.4D;
    public double horseJumpStrengthMax = 1.0D;
    public double horseMovementSpeedMin = 0.1125D;
    public double horseMovementSpeedMax = 0.3375D;
    private void horseSettings() {
        horseRidableInWater = getBoolean("mobs.horse.ridable-in-water", horseRidableInWater);
        horseBreedingTicks = getInt("mobs.horse.breeding-delay-ticks", horseBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.horse.attributes.max-health.min", horseMaxHealthMin);
            double oldMax = getDouble("mobs.horse.attributes.max-health.max", horseMaxHealthMax);
            set("mobs.horse.attributes.max-health", null);
            set("mobs.horse.attributes.max_health.min", oldMin);
            set("mobs.horse.attributes.max_health.max", oldMax);
        }
        horseMaxHealthMin = getDouble("mobs.horse.attributes.max_health.min", horseMaxHealthMin);
        horseMaxHealthMax = getDouble("mobs.horse.attributes.max_health.max", horseMaxHealthMax);
        horseJumpStrengthMin = getDouble("mobs.horse.attributes.jump_strength.min", horseJumpStrengthMin);
        horseJumpStrengthMax = getDouble("mobs.horse.attributes.jump_strength.max", horseJumpStrengthMax);
        horseMovementSpeedMin = getDouble("mobs.horse.attributes.movement_speed.min", horseMovementSpeedMin);
        horseMovementSpeedMax = getDouble("mobs.horse.attributes.movement_speed.max", horseMovementSpeedMax);
    }

    public boolean huskRidable = false;
    public boolean huskRidableInWater = false;
    public boolean huskJockeyOnlyBaby = true;
    public double huskJockeyChance = 0.05D;
    public boolean huskJockeyTryExistingChickens = true;
    public double huskMaxHealth = 20.0D;
    public double huskSpawnReinforcements = 0.1D;
    private void huskSettings() {
        huskRidable = getBoolean("mobs.husk.ridable", huskRidable);
        huskRidableInWater = getBoolean("mobs.husk.ridable-in-water", huskRidableInWater);
        huskJockeyOnlyBaby = getBoolean("mobs.husk.jockey.only-babies", huskJockeyOnlyBaby);
        huskJockeyChance = getDouble("mobs.husk.jockey.chance", huskJockeyChance);
        huskJockeyTryExistingChickens = getBoolean("mobs.husk.jockey.try-existing-chickens", huskJockeyTryExistingChickens);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.husk.attributes.max-health", huskMaxHealth);
            set("mobs.husk.attributes.max-health", null);
            set("mobs.husk.attributes.max_health", oldValue);
        }
        huskMaxHealth = getDouble("mobs.husk.attributes.max_health", huskMaxHealth);
        huskSpawnReinforcements = getDouble("mobs.husk.attributes.spawn_reinforcements", huskSpawnReinforcements);
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
            set("mobs.illusioner.max-health", null);
            set("mobs.illusioner.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.illusioner.attributes.max-health", illusionerMaxHealth);
            set("mobs.illusioner.attributes.max-health", null);
            set("mobs.illusioner.attributes.max_health", oldValue);
        }
        illusionerMaxHealth = getDouble("mobs.illusioner.attributes.max_health", illusionerMaxHealth);
    }

    public boolean ironGolemRidable = false;
    public boolean ironGolemRidableInWater = false;
    public boolean ironGolemCanSwim = false;
    public double ironGolemMaxHealth = 100.0D;
    private void ironGolemSettings() {
        ironGolemRidable = getBoolean("mobs.iron_golem.ridable", ironGolemRidable);
        ironGolemRidableInWater = getBoolean("mobs.iron_golem.ridable-in-water", ironGolemRidableInWater);
        ironGolemCanSwim = getBoolean("mobs.iron_golem.can-swim", ironGolemCanSwim);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.iron_golem.attributes.max-health", ironGolemMaxHealth);
            set("mobs.iron_golem.attributes.max-health", null);
            set("mobs.iron_golem.attributes.max_health", oldValue);
        }
        ironGolemMaxHealth = getDouble("mobs.iron_golem.attributes.max_health", ironGolemMaxHealth);
    }

    public boolean llamaRidable = false;
    public boolean llamaRidableInWater = false;
    public int llamaBreedingTicks = 6000;
    public double llamaMaxHealthMin = 15.0D;
    public double llamaMaxHealthMax = 30.0D;
    public double llamaJumpStrengthMin = 0.5D;
    public double llamaJumpStrengthMax = 0.5D;
    public double llamaMovementSpeedMin = 0.175D;
    public double llamaMovementSpeedMax = 0.175D;
    private void llamaSettings() {
        llamaRidable = getBoolean("mobs.llama.ridable", llamaRidable);
        llamaRidableInWater = getBoolean("mobs.llama.ridable-in-water", llamaRidableInWater);
        llamaBreedingTicks = getInt("mobs.llama.breeding-delay-ticks", llamaBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.llama.attributes.max-health.min", llamaMaxHealthMin);
            double oldMax = getDouble("mobs.llama.attributes.max-health.max", llamaMaxHealthMax);
            set("mobs.llama.attributes.max-health", null);
            set("mobs.llama.attributes.max_health.min", oldMin);
            set("mobs.llama.attributes.max_health.max", oldMax);
        }
        llamaMaxHealthMin = getDouble("mobs.llama.attributes.max_health.min", llamaMaxHealthMin);
        llamaMaxHealthMax = getDouble("mobs.llama.attributes.max_health.max", llamaMaxHealthMax);
        llamaJumpStrengthMin = getDouble("mobs.llama.attributes.jump_strength.min", llamaJumpStrengthMin);
        llamaJumpStrengthMax = getDouble("mobs.llama.attributes.jump_strength.max", llamaJumpStrengthMax);
        llamaMovementSpeedMin = getDouble("mobs.llama.attributes.movement_speed.min", llamaMovementSpeedMin);
        llamaMovementSpeedMax = getDouble("mobs.llama.attributes.movement_speed.max", llamaMovementSpeedMax);
    }

    public boolean llamaTraderRidable = false;
    public boolean llamaTraderRidableInWater = false;
    public double llamaTraderMaxHealthMin = 15.0D;
    public double llamaTraderMaxHealthMax = 30.0D;
    public double llamaTraderJumpStrengthMin = 0.5D;
    public double llamaTraderJumpStrengthMax = 0.5D;
    public double llamaTraderMovementSpeedMin = 0.175D;
    public double llamaTraderMovementSpeedMax = 0.175D;
    private void llamaTraderSettings() {
        llamaTraderRidable = getBoolean("mobs.trader_llama.ridable", llamaTraderRidable);
        llamaTraderRidableInWater = getBoolean("mobs.trader_llama.ridable-in-water", llamaTraderRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.trader_llama.attributes.max-health.min", llamaTraderMaxHealthMin);
            double oldMax = getDouble("mobs.trader_llama.attributes.max-health.max", llamaTraderMaxHealthMax);
            set("mobs.trader_llama.attributes.max-health", null);
            set("mobs.trader_llama.attributes.max_health.min", oldMin);
            set("mobs.trader_llama.attributes.max_health.max", oldMax);
        }
        llamaTraderMaxHealthMin = getDouble("mobs.trader_llama.attributes.max_health.min", llamaTraderMaxHealthMin);
        llamaTraderMaxHealthMax = getDouble("mobs.trader_llama.attributes.max_health.max", llamaTraderMaxHealthMax);
        llamaTraderJumpStrengthMin = getDouble("mobs.trader_llama.attributes.jump_strength.min", llamaTraderJumpStrengthMin);
        llamaTraderJumpStrengthMax = getDouble("mobs.trader_llama.attributes.jump_strength.max", llamaTraderJumpStrengthMax);
        llamaTraderMovementSpeedMin = getDouble("mobs.trader_llama.attributes.movement_speed.min", llamaTraderMovementSpeedMin);
        llamaTraderMovementSpeedMax = getDouble("mobs.trader_llama.attributes.movement_speed.max", llamaTraderMovementSpeedMax);
    }

    public boolean magmaCubeRidable = false;
    public boolean magmaCubeRidableInWater = false;
    public String magmaCubeMaxHealth = "size * size";
    private void magmaCubeSettings() {
        magmaCubeRidable = getBoolean("mobs.magma_cube.ridable", magmaCubeRidable);
        magmaCubeRidableInWater = getBoolean("mobs.magma_cube.ridable-in-water", magmaCubeRidableInWater);
        if (PurpurConfig.version < 10) {
            String oldValue = getString("mobs.magma_cube.attributes.max-health", magmaCubeMaxHealth);
            set("mobs.magma_cube.attributes.max-health", null);
            set("mobs.magma_cube.attributes.max_health", oldValue);
        }
        magmaCubeMaxHealth = getString("mobs.magma_cube.attributes.max_health", magmaCubeMaxHealth);
    }

    public boolean mooshroomRidable = false;
    public boolean mooshroomRidableInWater = false;
    public int mooshroomBreedingTicks = 6000;
    public double mooshroomMaxHealth = 10.0D;
    private void mooshroomSettings() {
        mooshroomRidable = getBoolean("mobs.mooshroom.ridable", mooshroomRidable);
        mooshroomRidableInWater = getBoolean("mobs.mooshroom.ridable-in-water", mooshroomRidableInWater);
        mooshroomBreedingTicks = getInt("mobs.mooshroom.breeding-delay-ticks", mooshroomBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.mooshroom.attributes.max-health", mooshroomMaxHealth);
            set("mobs.mooshroom.attributes.max-health", null);
            set("mobs.mooshroom.attributes.max_health", oldValue);
        }
        mooshroomMaxHealth = getDouble("mobs.mooshroom.attributes.max_health", mooshroomMaxHealth);
    }

    public boolean muleRidableInWater = false;
    public int muleBreedingTicks = 6000;
    public double muleMaxHealthMin = 15.0D;
    public double muleMaxHealthMax = 30.0D;
    public double muleJumpStrengthMin = 0.5D;
    public double muleJumpStrengthMax = 0.5D;
    public double muleMovementSpeedMin = 0.175D;
    public double muleMovementSpeedMax = 0.175D;
    private void muleSettings() {
        muleRidableInWater = getBoolean("mobs.mule.ridable-in-water", muleRidableInWater);
        muleBreedingTicks = getInt("mobs.mule.breeding-delay-ticks", muleBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.mule.attributes.max-health.min", muleMaxHealthMin);
            double oldMax = getDouble("mobs.mule.attributes.max-health.max", muleMaxHealthMax);
            set("mobs.mule.attributes.max-health", null);
            set("mobs.mule.attributes.max_health.min", oldMin);
            set("mobs.mule.attributes.max_health.max", oldMax);
        }
        muleMaxHealthMin = getDouble("mobs.mule.attributes.max_health.min", muleMaxHealthMin);
        muleMaxHealthMax = getDouble("mobs.mule.attributes.max_health.max", muleMaxHealthMax);
        muleJumpStrengthMin = getDouble("mobs.mule.attributes.jump_strength.min", muleJumpStrengthMin);
        muleJumpStrengthMax = getDouble("mobs.mule.attributes.jump_strength.max", muleJumpStrengthMax);
        muleMovementSpeedMin = getDouble("mobs.mule.attributes.movement_speed.min", muleMovementSpeedMin);
        muleMovementSpeedMax = getDouble("mobs.mule.attributes.movement_speed.max", muleMovementSpeedMax);
    }

    public boolean ocelotRidable = false;
    public boolean ocelotRidableInWater = false;
    public int ocelotBreedingTicks = 6000;
    public double ocelotMaxHealth = 10.0D;
    private void ocelotSettings() {
        ocelotRidable = getBoolean("mobs.ocelot.ridable", ocelotRidable);
        ocelotRidableInWater = getBoolean("mobs.ocelot.ridable-in-water", ocelotRidableInWater);
        ocelotBreedingTicks = getInt("mobs.ocelot.breeding-delay-ticks", ocelotBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ocelot.attributes.max-health", ocelotMaxHealth);
            set("mobs.ocelot.attributes.max-health", null);
            set("mobs.ocelot.attributes.max_health", oldValue);
        }
        ocelotMaxHealth = getDouble("mobs.ocelot.attributes.max_health", ocelotMaxHealth);
    }

    public boolean pandaRidable = false;
    public boolean pandaRidableInWater = false;
    public int pandaBreedingTicks = 6000;
    public double pandaMaxHealth = 20.0D;
    private void pandaSettings() {
        pandaRidable = getBoolean("mobs.panda.ridable", pandaRidable);
        pandaRidableInWater = getBoolean("mobs.panda.ridable-in-water", pandaRidableInWater);
        pandaBreedingTicks = getInt("mobs.panda.breeding-delay-ticks", pandaBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.panda.attributes.max-health", pandaMaxHealth);
            set("mobs.panda.attributes.max-health", null);
            set("mobs.panda.attributes.max_health", oldValue);
        }
        pandaMaxHealth = getDouble("mobs.panda.attributes.max_health", pandaMaxHealth);
    }

    public boolean parrotRidable = false;
    public boolean parrotRidableInWater = false;
    public double parrotMaxY = 256D;
    public double parrotMaxHealth = 6.0D;
    private void parrotSettings() {
        parrotRidable = getBoolean("mobs.parrot.ridable", parrotRidable);
        parrotRidableInWater = getBoolean("mobs.parrot.ridable-in-water", parrotRidableInWater);
        parrotMaxY = getDouble("mobs.parrot.ridable-max-y", parrotMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.parrot.attributes.max-health", parrotMaxHealth);
            set("mobs.parrot.attributes.max-health", null);
            set("mobs.parrot.attributes.max_health", oldValue);
        }
        parrotMaxHealth = getDouble("mobs.parrot.attributes.max_health", parrotMaxHealth);
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
    public boolean phantomFlamesOnSwoop = false;
    public double phantomMaxHealth = 20.0D;
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
        phantomFlamesOnSwoop = getBoolean("mobs.phantom.flames-on-swoop", phantomFlamesOnSwoop);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.phantom.attributes.max-health", phantomMaxHealth);
            set("mobs.phantom.attributes.max-health", null);
            set("mobs.phantom.attributes.max_health", oldValue);
        }
        phantomMaxHealth = getDouble("mobs.phantom.attributes.max_health", phantomMaxHealth);
    }

    public boolean pigRidable = false;
    public boolean pigRidableInWater = false;
    public boolean pigGiveSaddleBack = false;
    public int pigBreedingTicks = 6000;
    public double pigMaxHealth = 10.0D;
    private void pigSettings() {
        pigRidable = getBoolean("mobs.pig.ridable", pigRidable);
        pigRidableInWater = getBoolean("mobs.pig.ridable-in-water", pigRidableInWater);
        pigGiveSaddleBack = getBoolean("mobs.pig.give-saddle-back", pigGiveSaddleBack);
        pigBreedingTicks = getInt("mobs.pig.breeding-delay-ticks", pigBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pig.attributes.max-health", pigMaxHealth);
            set("mobs.pig.attributes.max-health", null);
            set("mobs.pig.attributes.max_health", oldValue);
        }
        pigMaxHealth = getDouble("mobs.pig.attributes.max_health", pigMaxHealth);
    }

    public boolean piglinRidable = false;
    public boolean piglinRidableInWater = false;
    public double piglinMaxHealth = 16.0D;
    private void piglinSettings() {
        piglinRidable = getBoolean("mobs.piglin.ridable", piglinRidable);
        piglinRidableInWater = getBoolean("mobs.piglin.ridable-in-water", piglinRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.piglin.attributes.max-health", piglinMaxHealth);
            set("mobs.piglin.attributes.max-health", null);
            set("mobs.piglin.attributes.max_health", oldValue);
        }
        piglinMaxHealth = getDouble("mobs.piglin.attributes.max_health", piglinMaxHealth);
    }

    public boolean piglinBruteRidable = false;
    public boolean piglinBruteRidableInWater = false;
    public double piglinBruteMaxHealth = 50.0D;
    private void piglinBruteSettings() {
        piglinBruteRidable = getBoolean("mobs.piglin_brute.ridable", piglinBruteRidable);
        piglinBruteRidableInWater = getBoolean("mobs.piglin_brute.ridable-in-water", piglinBruteRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.piglin_brute.attributes.max-health", piglinBruteMaxHealth);
            set("mobs.piglin_brute.attributes.max-health", null);
            set("mobs.piglin_brute.attributes.max_health", oldValue);
        }
        piglinBruteMaxHealth = getDouble("mobs.piglin_brute.attributes.max_health", piglinBruteMaxHealth);
    }

    public boolean pillagerRidable = false;
    public boolean pillagerRidableInWater = false;
    public boolean pillagerBypassMobGriefing = false;
    public double pillagerMaxHealth = 24.0D;
    private void pillagerSettings() {
        pillagerRidable = getBoolean("mobs.pillager.ridable", pillagerRidable);
        pillagerRidableInWater = getBoolean("mobs.pillager.ridable-in-water", pillagerRidableInWater);
        pillagerBypassMobGriefing = getBoolean("mobs.pillager.bypass-mob-griefing", pillagerBypassMobGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pillager.attributes.max-health", pillagerMaxHealth);
            set("mobs.pillager.attributes.max-health", null);
            set("mobs.pillager.attributes.max_health", oldValue);
        }
        pillagerMaxHealth = getDouble("mobs.pillager.attributes.max_health", pillagerMaxHealth);
    }

    public boolean polarBearRidable = false;
    public boolean polarBearRidableInWater = false;
    public String polarBearBreedableItemString = "";
    public Item polarBearBreedableItem = null;
    public int polarBearBreedingTicks = 6000;
    public double polarBearMaxHealth = 30.0D;
    private void polarBearSettings() {
        polarBearRidable = getBoolean("mobs.polar_bear.ridable", polarBearRidable);
        polarBearRidableInWater = getBoolean("mobs.polar_bear.ridable-in-water", polarBearRidableInWater);
        polarBearBreedableItemString = getString("mobs.polar_bear.breedable-item", polarBearBreedableItemString);
        Item item = IRegistry.ITEM.get(new MinecraftKey(polarBearBreedableItemString));
        if (item != Items.AIR) polarBearBreedableItem = item;
        polarBearBreedingTicks = getInt("mobs.polar_bear.breeding-delay-ticks", polarBearBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.polar_bear.attributes.max-health", polarBearMaxHealth);
            set("mobs.polar_bear.attributes.max-health", null);
            set("mobs.polar_bear.attributes.max_health", oldValue);
        }
        polarBearMaxHealth = getDouble("mobs.polar_bear.attributes.max_health", polarBearMaxHealth);
    }

    public boolean pufferfishRidable = false;
    public double pufferfishMaxHealth = 3.0D;
    private void pufferfishSettings() {
        pufferfishRidable = getBoolean("mobs.pufferfish.ridable", pufferfishRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pufferfish.attributes.max-health", pufferfishMaxHealth);
            set("mobs.pufferfish.attributes.max-health", null);
            set("mobs.pufferfish.attributes.max_health", oldValue);
        }
        pufferfishMaxHealth = getDouble("mobs.pufferfish.attributes.max_health", pufferfishMaxHealth);
    }

    public boolean rabbitRidable = false;
    public boolean rabbitRidableInWater = false;
    public boolean rabbitBypassMobGriefing = false;
    public double rabbitNaturalToast = 0.0D;
    public double rabbitNaturalKiller = 0.0D;
    public int rabbitBreedingTicks = 6000;
    public double rabbitMaxHealth = 3.0D;
    private void rabbitSettings() {
        rabbitRidable = getBoolean("mobs.rabbit.ridable", rabbitRidable);
        rabbitRidableInWater = getBoolean("mobs.rabbit.ridable-in-water", rabbitRidableInWater);
        rabbitBypassMobGriefing = getBoolean("mobs.rabbit.bypass-mob-griefing", rabbitBypassMobGriefing);
        rabbitNaturalToast = getDouble("mobs.rabbit.spawn-toast-chance", rabbitNaturalToast);
        rabbitNaturalKiller = getDouble("mobs.rabbit.spawn-killer-rabbit-chance", rabbitNaturalKiller);
        rabbitBreedingTicks = getInt("mobs.rabbit.breeding-delay-ticks", rabbitBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.rabbit.attributes.max-health", rabbitMaxHealth);
            set("mobs.rabbit.attributes.max-health", null);
            set("mobs.rabbit.attributes.max_health", oldValue);
        }
        rabbitMaxHealth = getDouble("mobs.rabbit.attributes.max_health", rabbitMaxHealth);
    }

    public boolean ravagerRidable = false;
    public boolean ravagerRidableInWater = false;
    public boolean ravagerBypassMobGriefing = false;
    public double ravagerMaxHealth = 100.0D;
    private void ravagerSettings() {
        ravagerRidable = getBoolean("mobs.ravager.ridable", ravagerRidable);
        ravagerRidableInWater = getBoolean("mobs.ravager.ridable-in-water", ravagerRidableInWater);
        ravagerBypassMobGriefing = getBoolean("mobs.ravager.bypass-mob-griefing", ravagerBypassMobGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ravager.attributes.max-health", ravagerMaxHealth);
            set("mobs.ravager.attributes.max-health", null);
            set("mobs.ravager.attributes.max_health", oldValue);
        }
        ravagerMaxHealth = getDouble("mobs.ravager.attributes.max_health", ravagerMaxHealth);
    }

    public boolean salmonRidable = false;
    public double salmonMaxHealth = 3.0D;
    private void salmonSettings() {
        salmonRidable = getBoolean("mobs.salmon.ridable", salmonRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.salmon.attributes.max-health", salmonMaxHealth);
            set("mobs.salmon.attributes.max-health", null);
            set("mobs.salmon.attributes.max_health", oldValue);
        }
        salmonMaxHealth = getDouble("mobs.salmon.attributes.max_health", salmonMaxHealth);
    }

    public boolean sheepRidable = false;
    public boolean sheepRidableInWater = false;
    public int sheepBreedingTicks = 6000;
    public boolean sheepBypassMobGriefing = false;
    public double sheepMaxHealth = 8.0D;
    private void sheepSettings() {
        sheepRidable = getBoolean("mobs.sheep.ridable", sheepRidable);
        sheepRidableInWater = getBoolean("mobs.sheep.ridable-in-water", sheepRidableInWater);
        sheepBreedingTicks = getInt("mobs.sheep.breeding-delay-ticks", sheepBreedingTicks);
        sheepBypassMobGriefing = getBoolean("mobs.sheep.bypass-mob-griefing", sheepBypassMobGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.sheep.attributes.max-health", sheepMaxHealth);
            set("mobs.sheep.attributes.max-health", null);
            set("mobs.sheep.attributes.max_health", oldValue);
        }
        sheepMaxHealth = getDouble("mobs.sheep.attributes.max_health", sheepMaxHealth);
    }

    public boolean shulkerRidable = false;
    public boolean shulkerRidableInWater = false;
    public double shulkerMaxHealth = 30.0D;
    private void shulkerSettings() {
        shulkerRidable = getBoolean("mobs.shulker.ridable", shulkerRidable);
        shulkerRidableInWater = getBoolean("mobs.shulker.ridable-in-water", shulkerRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.shulker.attributes.max-health", shulkerMaxHealth);
            set("mobs.shulker.attributes.max-health", null);
            set("mobs.shulker.attributes.max_health", oldValue);
        }
        shulkerMaxHealth = getDouble("mobs.shulker.attributes.max_health", shulkerMaxHealth);
    }

    public boolean silverfishRidable = false;
    public boolean silverfishRidableInWater = false;
    public boolean silverfishBypassMobGriefing = false;
    public double silverfishMaxHealth = 8.0D;
    private void silverfishSettings() {
        silverfishRidable = getBoolean("mobs.silverfish.ridable", silverfishRidable);
        silverfishRidableInWater = getBoolean("mobs.silverfish.ridable-in-water", silverfishRidableInWater);
        silverfishBypassMobGriefing = getBoolean("mobs.silverfish.bypass-mob-griefing", silverfishBypassMobGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.silverfish.attributes.max-health", silverfishMaxHealth);
            set("mobs.silverfish.attributes.max-health", null);
            set("mobs.silverfish.attributes.max_health", oldValue);
        }
        silverfishMaxHealth = getDouble("mobs.silverfish.attributes.max_health", silverfishMaxHealth);
    }

    public boolean skeletonRidable = false;
    public boolean skeletonRidableInWater = false;
    public double skeletonMaxHealth = 20.0D;
    private void skeletonSettings() {
        skeletonRidable = getBoolean("mobs.skeleton.ridable", skeletonRidable);
        skeletonRidableInWater = getBoolean("mobs.skeleton.ridable-in-water", skeletonRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.skeleton.attributes.max-health", skeletonMaxHealth);
            set("mobs.skeleton.attributes.max-health", null);
            set("mobs.skeleton.attributes.max_health", oldValue);
        }
        skeletonMaxHealth = getDouble("mobs.skeleton.attributes.max_health", skeletonMaxHealth);
    }

    public boolean skeletonHorseCanSwim = false;
    public boolean skeletonHorseRidableInWater = true;
    public double skeletonHorseMaxHealthMin = 15.0D;
    public double skeletonHorseMaxHealthMax = 15.0D;
    public double skeletonHorseJumpStrengthMin = 0.4D;
    public double skeletonHorseJumpStrengthMax = 1.0D;
    public double skeletonHorseMovementSpeedMin = 0.2D;
    public double skeletonHorseMovementSpeedMax = 0.2D;
    private void skeletonHorseSettings() {
        skeletonHorseCanSwim = getBoolean("mobs.skeleton_horse.can-swim", skeletonHorseCanSwim);
        skeletonHorseRidableInWater = getBoolean("mobs.skeleton_horse.ridable-in-water", skeletonHorseRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.skeleton_horse.attributes.max-health", skeletonHorseMaxHealthMin);
            set("mobs.skeleton_horse.attributes.max-health", null);
            set("mobs.skeleton_horse.attributes.max_health.min", oldValue);
            set("mobs.skeleton_horse.attributes.max_health.max", oldValue);
        }
        skeletonHorseMaxHealthMin = getDouble("mobs.skeleton_horse.attributes.max_health.min", skeletonHorseMaxHealthMin);
        skeletonHorseMaxHealthMax = getDouble("mobs.skeleton_horse.attributes.max_health.max", skeletonHorseMaxHealthMax);
        skeletonHorseJumpStrengthMin = getDouble("mobs.skeleton_horse.attributes.jump_strength.min", skeletonHorseJumpStrengthMin);
        skeletonHorseJumpStrengthMax = getDouble("mobs.skeleton_horse.attributes.jump_strength.max", skeletonHorseJumpStrengthMax);
        skeletonHorseMovementSpeedMin = getDouble("mobs.skeleton_horse.attributes.movement_speed.min", skeletonHorseMovementSpeedMin);
        skeletonHorseMovementSpeedMax = getDouble("mobs.skeleton_horse.attributes.movement_speed.max", skeletonHorseMovementSpeedMax);
    }

    public boolean slimeRidable = false;
    public boolean slimeRidableInWater = false;
    public String slimeMaxHealth = "size * size";
    public Map<Integer, Double> slimeMaxHealthCache = new HashMap<>();
    private void slimeSettings() {
        slimeRidable = getBoolean("mobs.slime.ridable", slimeRidable);
        slimeRidableInWater = getBoolean("mobs.slime.ridable-in-water", slimeRidableInWater);
        if (PurpurConfig.version < 10) {
            String oldValue = getString("mobs.slime.attributes.max-health", slimeMaxHealth);
            set("mobs.slime.attributes.max-health", null);
            set("mobs.slime.attributes.max_health", oldValue);
        }
        slimeMaxHealth = getString("mobs.slime.attributes.max_health", slimeMaxHealth);
        slimeMaxHealthCache.clear();
    }

    public boolean snowGolemRidable = false;
    public boolean snowGolemRidableInWater = false;
    public boolean snowGolemLeaveTrailWhenRidden = false;
    public boolean snowGolemBypassMobGriefing = false;
    public boolean snowGolemDropsPumpkin = true;
    public boolean snowGolemPutPumpkinBack = false;
    public int snowGolemSnowBallMin = 20;
    public int snowGolemSnowBallMax = 20;
    public float snowGolemSnowBallModifier = 10.0F;
    public double snowGolemAttackDistance = 1.25D;
    public double snowGolemMaxHealth = 4.0D;
    public boolean snowGolemTakeDamageFromWater = true;
    private void snowGolemSettings() {
        snowGolemRidable = getBoolean("mobs.snow_golem.ridable", snowGolemRidable);
        snowGolemRidableInWater = getBoolean("mobs.snow_golem.ridable-in-water", snowGolemRidableInWater);
        snowGolemLeaveTrailWhenRidden = getBoolean("mobs.snow_golem.leave-trail-when-ridden", snowGolemLeaveTrailWhenRidden);
        snowGolemBypassMobGriefing = getBoolean("mobs.snow_golem.bypass-mob-griefing", snowGolemBypassMobGriefing);
        snowGolemDropsPumpkin = getBoolean("mobs.snow_golem.drop-pumpkin-when-sheared", snowGolemDropsPumpkin);
        snowGolemPutPumpkinBack = getBoolean("mobs.snow_golem.pumpkin-can-be-added-back", snowGolemPutPumpkinBack);
        snowGolemSnowBallMin = getInt("mobs.snow_golem.min-shoot-interval-ticks", snowGolemSnowBallMin);
        snowGolemSnowBallMax = getInt("mobs.snow_golem.max-shoot-interval-ticks", snowGolemSnowBallMax);
        snowGolemSnowBallModifier = (float) getDouble("mobs.snow_golem.snow-ball-modifier", snowGolemSnowBallModifier);
        snowGolemAttackDistance = getDouble("mobs.snow_golem.attack-distance", snowGolemAttackDistance);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.snow_golem.attributes.max-health", snowGolemMaxHealth);
            set("mobs.snow_golem.attributes.max-health", null);
            set("mobs.snow_golem.attributes.max_health", oldValue);
        }
        snowGolemMaxHealth = getDouble("mobs.snow_golem.attributes.max_health", snowGolemMaxHealth);
        snowGolemTakeDamageFromWater = getBoolean("mobs.snow_golem.takes-damage-from-water", snowGolemTakeDamageFromWater);
    }

    public boolean squidRidable = false;
    public boolean squidImmuneToEAR = true;
    public double squidOffsetWaterCheck = 0.0D;
    public boolean squidsCanFly = false;
    public double squidMaxHealth = 10.0D;
    private void squidSettings() {
        squidRidable = getBoolean("mobs.squid.ridable", squidRidable);
        squidImmuneToEAR = getBoolean("mobs.squid.immune-to-EAR", squidImmuneToEAR);
        squidOffsetWaterCheck = getDouble("mobs.squid.water-offset-check", squidOffsetWaterCheck);
        squidsCanFly = getBoolean("mobs.squid.can-fly", squidsCanFly);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.squid.attributes.max-health", squidMaxHealth);
            set("mobs.squid.attributes.max-health", null);
            set("mobs.squid.attributes.max_health", oldValue);
        }
        squidMaxHealth = getDouble("mobs.squid.attributes.max_health", squidMaxHealth);
    }

    public boolean spiderRidable = false;
    public boolean spiderRidableInWater = false;
    public double spiderMaxHealth = 16.0D;
    private void spiderSettings() {
        spiderRidable = getBoolean("mobs.spider.ridable", spiderRidable);
        spiderRidableInWater = getBoolean("mobs.spider.ridable-in-water", spiderRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.spider.attributes.max-health", spiderMaxHealth);
            set("mobs.spider.attributes.max-health", null);
            set("mobs.spider.attributes.max_health", oldValue);
        }
        spiderMaxHealth = getDouble("mobs.spider.attributes.max_health", spiderMaxHealth);
    }

    public boolean strayRidable = false;
    public boolean strayRidableInWater = false;
    public double strayMaxHealth = 20.0D;
    private void straySettings() {
        strayRidable = getBoolean("mobs.stray.ridable", strayRidable);
        strayRidableInWater = getBoolean("mobs.stray.ridable-in-water", strayRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.stray.attributes.max-health", strayMaxHealth);
            set("mobs.stray.attributes.max-health", null);
            set("mobs.stray.attributes.max_health", oldValue);
        }
        strayMaxHealth = getDouble("mobs.stray.attributes.max_health", strayMaxHealth);
    }

    public boolean striderRidable = false;
    public boolean striderRidableInWater = false;
    public int striderBreedingTicks = 6000;
    public boolean striderGiveSaddleBack = false;
    public double striderMaxHealth = 20.0D;
    public boolean striderTakeDamageFromWater = true;
    private void striderSettings() {
        striderRidable = getBoolean("mobs.strider.ridable", striderRidable);
        striderRidableInWater = getBoolean("mobs.strider.ridable-in-water", striderRidableInWater);
        striderBreedingTicks = getInt("mobs.strider.breeding-delay-ticks", striderBreedingTicks);
        striderGiveSaddleBack = getBoolean("mobs.strider.give-saddle-back", striderGiveSaddleBack);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.strider.attributes.max-health", striderMaxHealth);
            set("mobs.strider.attributes.max-health", null);
            set("mobs.strider.attributes.max_health", oldValue);
        }
        striderMaxHealth = getDouble("mobs.strider.attributes.max_health", striderMaxHealth);
        striderTakeDamageFromWater = getBoolean("mobs.strider.takes-damage-from-water", striderTakeDamageFromWater);
    }

    public boolean tropicalFishRidable = false;
    public double tropicalFishMaxHealth = 3.0D;
    private void tropicalFishSettings() {
        tropicalFishRidable = getBoolean("mobs.tropical_fish.ridable", tropicalFishRidable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.tropical_fish.attributes.max-health", tropicalFishMaxHealth);
            set("mobs.tropical_fish.attributes.max-health", null);
            set("mobs.tropical_fish.attributes.max_health", oldValue);
        }
        tropicalFishMaxHealth = getDouble("mobs.tropical_fish.attributes.max_health", tropicalFishMaxHealth);
    }

    public boolean turtleRidable = false;
    public boolean turtleRidableInWater = false;
    public int turtleBreedingTicks = 6000;
    public double turtleMaxHealth = 30.0D;
    private void turtleSettings() {
        turtleRidable = getBoolean("mobs.turtle.ridable", turtleRidable);
        turtleRidableInWater = getBoolean("mobs.turtle.ridable-in-water", turtleRidableInWater);
        turtleBreedingTicks = getInt("mobs.turtle.breeding-delay-ticks", turtleBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.turtle.attributes.max-health", turtleMaxHealth);
            set("mobs.turtle.attributes.max-health", null);
            set("mobs.turtle.attributes.max_health", oldValue);
        }
        turtleMaxHealth = getDouble("mobs.turtle.attributes.max_health", turtleMaxHealth);
    }

    public boolean vexRidable = false;
    public boolean vexRidableInWater = false;
    public double vexMaxY = 256D;
    public double vexMaxHealth = 14.0D;
    private void vexSettings() {
        vexRidable = getBoolean("mobs.vex.ridable", vexRidable);
        vexRidableInWater = getBoolean("mobs.vex.ridable-in-water", vexRidableInWater);
        vexMaxY = getDouble("mobs.vex.ridable-max-y", vexMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.vex.attributes.max-health", vexMaxHealth);
            set("mobs.vex.attributes.max-health", null);
            set("mobs.vex.attributes.max_health", oldValue);
        }
        vexMaxHealth = getDouble("mobs.vex.attributes.max_health", vexMaxHealth);
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
    public int villagerBreedingTicks = 6000;
    public boolean villagerLobotomizeEnabled = false;
    public int villagerLobotomizeCheck = 60;
    public boolean villagerClericsFarmWarts = false;
    public boolean villagerClericFarmersThrowWarts = true;
    public double villagerMaxHealth = 20.0D;
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
        villagerBreedingTicks = getInt("mobs.villager.breeding-delay-ticks", villagerBreedingTicks);
        if (PurpurConfig.version < 9) {
            boolean oldValue = getBoolean("mobs.villager.lobotomize-1x1", villagerLobotomizeEnabled);
            set("mobs.villager.lobotomize.enabled", oldValue);
            set("mobs.villager.lobotomize-1x1", null);
        }
        villagerLobotomizeEnabled = getBoolean("mobs.villager.lobotomize.enabled", villagerLobotomizeEnabled);
        villagerLobotomizeCheck = getInt("mobs.villager.lobotomize.check-interval", villagerLobotomizeCheck);
        villagerClericsFarmWarts = getBoolean("mobs.villager.clerics-farm-warts", villagerClericsFarmWarts);
        villagerClericFarmersThrowWarts = getBoolean("mobs.villager.cleric-wart-farmers-throw-warts-at-villagers", villagerClericFarmersThrowWarts);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.villager.attributes.max-health", villagerMaxHealth);
            set("mobs.villager.attributes.max-health", null);
            set("mobs.villager.attributes.max_health", oldValue);
        }
        villagerMaxHealth = getDouble("mobs.villager.attributes.max_health", villagerMaxHealth);
    }

    public boolean villagerTraderRidable = false;
    public boolean villagerTraderRidableInWater = false;
    public boolean villagerTraderCanBeLeashed = false;
    public boolean villagerTraderFollowEmeraldBlock = false;
    public double villagerTraderMaxHealth = 20.0D;
    private void villagerTraderSettings() {
        villagerTraderRidable = getBoolean("mobs.wandering_trader.ridable", villagerTraderRidable);
        villagerTraderRidableInWater = getBoolean("mobs.wandering_trader.ridable-in-water", villagerTraderRidableInWater);
        villagerTraderCanBeLeashed = getBoolean("mobs.wandering_trader.can-be-leashed", villagerTraderCanBeLeashed);
        villagerTraderFollowEmeraldBlock = getBoolean("mobs.wandering_trader.follow-emerald-blocks", villagerTraderFollowEmeraldBlock);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wandering_trader.attributes.max-health", villagerTraderMaxHealth);
            set("mobs.wandering_trader.attributes.max-health", null);
            set("mobs.wandering_trader.attributes.max_health", oldValue);
        }
        villagerTraderMaxHealth = getDouble("mobs.wandering_trader.attributes.max_health", villagerTraderMaxHealth);
    }

    public boolean vindicatorRidable = false;
    public boolean vindicatorRidableInWater = false;
    public double vindicatorJohnnySpawnChance = 0D;
    public double vindicatorMaxHealth = 24.0D;
    private void vindicatorSettings() {
        vindicatorRidable = getBoolean("mobs.vindicator.ridable", vindicatorRidable);
        vindicatorRidableInWater = getBoolean("mobs.vindicator.ridable-in-water", vindicatorRidableInWater);
        vindicatorJohnnySpawnChance = getDouble("mobs.vindicator.johnny.spawn-chance", vindicatorJohnnySpawnChance);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.vindicator.attributes.max-health", vindicatorMaxHealth);
            set("mobs.vindicator.attributes.max-health", null);
            set("mobs.vindicator.attributes.max_health", oldValue);
        }
        vindicatorMaxHealth = getDouble("mobs.vindicator.attributes.max_health", vindicatorMaxHealth);
    }

    public boolean witchRidable = false;
    public boolean witchRidableInWater = false;
    public double witchMaxHealth = 26.0D;
    private void witchSettings() {
        witchRidable = getBoolean("mobs.witch.ridable", witchRidable);
        witchRidableInWater = getBoolean("mobs.witch.ridable-in-water", witchRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.witch.attributes.max-health", witchMaxHealth);
            set("mobs.witch.attributes.max-health", null);
            set("mobs.witch.attributes.max_health", oldValue);
        }
        witchMaxHealth = getDouble("mobs.witch.attributes.max_health", witchMaxHealth);
    }

    public boolean witherRidable = false;
    public boolean witherRidableInWater = false;
    public double witherMaxY = 256D;
    public boolean witherBypassMobGriefing = false;
    public float witherHealthRegenAmount = 1.0f;
    public int witherHealthRegenDelay = 20;
    public double witherMaxHealth = 300.0D;
    public boolean witherCanRideVehicles = false;
    private void witherSettings() {
        witherRidable = getBoolean("mobs.wither.ridable", witherRidable);
        witherRidableInWater = getBoolean("mobs.wither.ridable-in-water", witherRidableInWater);
        witherMaxY = getDouble("mobs.wither.ridable-max-y", witherMaxY);
        witherBypassMobGriefing = getBoolean("mobs.wither.bypass-mob-griefing", witherBypassMobGriefing);
        witherHealthRegenAmount = (float) getDouble("mobs.wither.health-regen-amount", witherHealthRegenAmount);
        witherHealthRegenDelay = getInt("mobs.wither.health-regen-delay", witherHealthRegenDelay);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.wither.max-health", witherMaxHealth);
            set("mobs.wither.max_health", null);
            set("mobs.wither.attributes.max-health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wither.attributes.max-health", witherMaxHealth);
            set("mobs.wither.attributes.max-health", null);
            set("mobs.wither.attributes.max_health", oldValue);
        }
        witherMaxHealth = getDouble("mobs.wither.attributes.max_health", witherMaxHealth);
        witherCanRideVehicles = getBoolean("mobs.wither.can-ride-vehicles", witherCanRideVehicles);
    }

    public boolean witherSkeletonRidable = false;
    public boolean witherSkeletonRidableInWater = false;
    public boolean witherSkeletonTakesWitherDamage = false;
    public double witherSkeletonMaxHealth = 20.0D;
    private void witherSkeletonSettings() {
        witherSkeletonRidable = getBoolean("mobs.wither_skeleton.ridable", witherSkeletonRidable);
        witherSkeletonRidableInWater = getBoolean("mobs.wither_skeleton.ridable-in-water", witherSkeletonRidableInWater);
        witherSkeletonTakesWitherDamage = getBoolean("mobs.wither_skeleton.takes-wither-damage", witherSkeletonTakesWitherDamage);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wither_skeleton.attributes.max-health", witherSkeletonMaxHealth);
            set("mobs.wither_skeleton.attributes.max-health", null);
            set("mobs.wither_skeleton.attributes.max_health", oldValue);
        }
        witherSkeletonMaxHealth = getDouble("mobs.wither_skeleton.attributes.max_health", witherSkeletonMaxHealth);
    }

    public boolean wolfRidable = false;
    public boolean wolfRidableInWater = false;
    public EnumColor wolfDefaultCollarColor = EnumColor.RED;
    public boolean wolfMilkCuresRabies = true;
    public double wolfNaturalRabid = 0.0D;
    public int wolfBreedingTicks = 6000;
    public double wolfMaxHealth = 8.0D;
    private void wolfSettings() {
        wolfRidable = getBoolean("mobs.wolf.ridable", wolfRidable);
        wolfRidableInWater = getBoolean("mobs.wolf.ridable-in-water", wolfRidableInWater);
        try {
            wolfDefaultCollarColor = EnumColor.valueOf(getString("mobs.wolf.default-collar-color", wolfDefaultCollarColor.name()));
        } catch (IllegalArgumentException ignore) {
            wolfDefaultCollarColor = EnumColor.RED;
        }
        wolfMilkCuresRabies = getBoolean("mobs.wolf.milk-cures-rabid-wolves", wolfMilkCuresRabies);
        wolfNaturalRabid = getDouble("mobs.wolf.spawn-rabid-chance", wolfNaturalRabid);
        wolfBreedingTicks = getInt("mobs.wolf.breeding-delay-ticks", wolfBreedingTicks);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wolf.attributes.max-health", wolfMaxHealth);
            set("mobs.wolf.attributes.max-health", null);
            set("mobs.wolf.attributes.max_health", oldValue);
        }
        wolfMaxHealth = getDouble("mobs.wolf.attributes.max_health", wolfMaxHealth);
    }

    public boolean zoglinRidable = false;
    public boolean zoglinRidableInWater = false;
    public double zoglinMaxHealth = 40.0D;
    private void zoglinSettings() {
        zoglinRidable = getBoolean("mobs.zoglin.ridable", zoglinRidable);
        zoglinRidableInWater = getBoolean("mobs.zoglin.ridable-in-water", zoglinRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zoglin.attributes.max-health", zoglinMaxHealth);
            set("mobs.zoglin.attributes.max-health", null);
            set("mobs.zoglin.attributes.max_health", oldValue);
        }
        zoglinMaxHealth = getDouble("mobs.zoglin.attributes.max_health", zoglinMaxHealth);
    }

    public boolean zombieRidable = false;
    public boolean zombieRidableInWater = false;
    public boolean zombieBypassMobGriefing = false;
    public boolean zombieJockeyOnlyBaby = true;
    public double zombieJockeyChance = 0.05D;
    public boolean zombieJockeyTryExistingChickens = true;
    public boolean zombieAggressiveTowardsVillagerWhenLagging = true;
    public EnumDifficulty zombieBreakDoorMinDifficulty = EnumDifficulty.HARD;
    public double zombieMaxHealth = 20.0D;
    public double zombieSpawnReinforcements = 0.1D;
    private void zombieSettings() {
        zombieRidable = getBoolean("mobs.zombie.ridable", zombieRidable);
        zombieRidableInWater = getBoolean("mobs.zombie.ridable-in-water", zombieRidableInWater);
        zombieBypassMobGriefing = getBoolean("mobs.zombie.bypass-mob-griefing", zombieBypassMobGriefing);
        zombieJockeyOnlyBaby = getBoolean("mobs.zombie.jockey.only-babies", zombieJockeyOnlyBaby);
        zombieJockeyChance = getDouble("mobs.zombie.jockey.chance", zombieJockeyChance);
        zombieJockeyTryExistingChickens = getBoolean("mobs.zombie.jockey.try-existing-chickens", zombieJockeyTryExistingChickens);
        zombieAggressiveTowardsVillagerWhenLagging = getBoolean("mobs.zombie.aggressive-towards-villager-when-lagging", zombieAggressiveTowardsVillagerWhenLagging);
        try {
            zombieBreakDoorMinDifficulty = EnumDifficulty.valueOf(getString("mobs.zombie.break-door-minimum-difficulty", zombieBreakDoorMinDifficulty.name()));
        } catch (IllegalArgumentException ignore) {
            zombieBreakDoorMinDifficulty = EnumDifficulty.HARD;
        }
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie.attributes.max-health", zombieMaxHealth);
            set("mobs.zombie.attributes.max-health", null);
            set("mobs.zombie.attributes.max_health", oldValue);
        }
        zombieMaxHealth = getDouble("mobs.zombie.attributes.max_health", zombieMaxHealth);
        zombieSpawnReinforcements = getDouble("mobs.zombie.attributes.spawn_reinforcements", zombieSpawnReinforcements);
    }

    public boolean zombieHorseCanSwim = false;
    public boolean zombieHorseRidableInWater = false;
    public double zombieHorseSpawnChance = 0.0D;
    public double zombieHorseMaxHealthMin = 15.0D;
    public double zombieHorseMaxHealthMax = 15.0D;
    public double zombieHorseJumpStrengthMin = 0.4D;
    public double zombieHorseJumpStrengthMax = 1.0D;
    public double zombieHorseMovementSpeedMin = 0.2D;
    public double zombieHorseMovementSpeedMax = 0.2D;
    private void zombieHorseSettings() {
        zombieHorseCanSwim = getBoolean("mobs.zombie_horse.can-swim", zombieHorseCanSwim);
        zombieHorseRidableInWater = getBoolean("mobs.zombie_horse.ridable-in-water", zombieHorseRidableInWater);
        zombieHorseSpawnChance = getDouble("mobs.zombie_horse.spawn-chance", zombieHorseSpawnChance);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie_horse.attributes.max-health", zombieHorseMaxHealthMin);
            set("mobs.zombie_horse.attributes.max-health", null);
            set("mobs.zombie_horse.attributes.max_health.min", oldValue);
            set("mobs.zombie_horse.attributes.max_health.max", oldValue);
        }
        zombieHorseMaxHealthMin = getDouble("mobs.zombie_horse.attributes.max_health.min", zombieHorseMaxHealthMin);
        zombieHorseMaxHealthMax = getDouble("mobs.zombie_horse.attributes.max_health.max", zombieHorseMaxHealthMax);
        zombieHorseJumpStrengthMin = getDouble("mobs.zombie_horse.attributes.jump_strength.min", zombieHorseJumpStrengthMin);
        zombieHorseJumpStrengthMax = getDouble("mobs.zombie_horse.attributes.jump_strength.max", zombieHorseJumpStrengthMax);
        zombieHorseMovementSpeedMin = getDouble("mobs.zombie_horse.attributes.movement_speed.min", zombieHorseMovementSpeedMin);
        zombieHorseMovementSpeedMax = getDouble("mobs.zombie_horse.attributes.movement_speed.max", zombieHorseMovementSpeedMax);
    }

    public boolean zombifiedPiglinRidable = false;
    public boolean zombifiedPiglinRidableInWater = false;
    public boolean zombifiedPiglinJockeyOnlyBaby = true;
    public double zombifiedPiglinJockeyChance = 0.05D;
    public boolean zombifiedPiglinJockeyTryExistingChickens = true;
    public boolean zombifiedPiglinCountAsPlayerKillWhenAngry = true;
    public double zombifiedPiglinMaxHealth = 20.0D;
    public double zombifiedPiglinSpawnReinforcements = 0.0D;
    private void zombifiedPiglinSettings() {
        zombifiedPiglinRidable = getBoolean("mobs.zombified_piglin.ridable", zombifiedPiglinRidable);
        zombifiedPiglinRidableInWater = getBoolean("mobs.zombified_piglin.ridable-in-water", zombifiedPiglinRidableInWater);
        zombifiedPiglinJockeyOnlyBaby = getBoolean("mobs.zombified_piglin.jockey.only-babies", zombifiedPiglinJockeyOnlyBaby);
        zombifiedPiglinJockeyChance = getDouble("mobs.zombified_piglin.jockey.chance", zombifiedPiglinJockeyChance);
        zombifiedPiglinJockeyTryExistingChickens = getBoolean("mobs.zombified_piglin.jockey.try-existing-chickens", zombifiedPiglinJockeyTryExistingChickens);
        zombifiedPiglinCountAsPlayerKillWhenAngry = getBoolean("mobs.zombified_piglin.count-as-player-kill-when-angry", zombifiedPiglinCountAsPlayerKillWhenAngry);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombified_piglin.attributes.max-health", zombifiedPiglinMaxHealth);
            set("mobs.zombified_piglin.attributes.max-health", null);
            set("mobs.zombified_piglin.attributes.max_health", oldValue);
        }
        zombifiedPiglinMaxHealth = getDouble("mobs.zombified_piglin.attributes.max_health", zombifiedPiglinMaxHealth);
        zombifiedPiglinSpawnReinforcements = getDouble("mobs.zombified_piglin.attributes.spawn_reinforcements", zombifiedPiglinSpawnReinforcements);
    }

    public boolean zombieVillagerRidable = false;
    public boolean zombieVillagerRidableInWater = false;
    public boolean zombieVillagerJockeyOnlyBaby = true;
    public double zombieVillagerJockeyChance = 0.05D;
    public boolean zombieVillagerJockeyTryExistingChickens = true;
    public double zombieVillagerMaxHealth = 20.0D;
    public double zombieVillagerSpawnReinforcements = 0.1D;
    private void zombieVillagerSettings() {
        zombieVillagerRidable = getBoolean("mobs.zombie_villager.ridable", zombieVillagerRidable);
        zombieVillagerRidableInWater = getBoolean("mobs.zombie_villager.ridable-in-water", zombieVillagerRidableInWater);
        zombieVillagerJockeyOnlyBaby = getBoolean("mobs.zombie_villager.jockey.only-babies", zombieVillagerJockeyOnlyBaby);
        zombieVillagerJockeyChance = getDouble("mobs.zombie_villager.jockey.chance", zombieVillagerJockeyChance);
        zombieVillagerJockeyTryExistingChickens = getBoolean("mobs.zombie_villager.jockey.try-existing-chickens", zombieVillagerJockeyTryExistingChickens);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie_villager.attributes.max-health", zombieVillagerMaxHealth);
            set("mobs.zombie_villager.attributes.max-health", null);
            set("mobs.zombie_villager.attributes.max_health", oldValue);
        }
        zombieVillagerMaxHealth = getDouble("mobs.zombie_villager.attributes.max_health", zombieVillagerMaxHealth);
        zombieVillagerSpawnReinforcements = getDouble("mobs.zombie_villager.attributes.spawn_reinforcements", zombieVillagerSpawnReinforcements);
    }

    public boolean imposeTeleportRestrictionsOnGateways = false;
    private void imposeTeleportRestrictionsOnGateways() {
        imposeTeleportRestrictionsOnGateways = getBoolean("gameplay-mechanics.impose-teleport-restrictions-on-gateways", imposeTeleportRestrictionsOnGateways);
    }

    public boolean baselessCrystalExplode = true;
    public double baselessCrystalExplosionPower = 6.0D;
    public boolean baselessCrystalExplosionFire = false;
    public Explosion.Effect baselessCrystalExplosionEffect = Explosion.Effect.DESTROY;
    public boolean baseCrystalExplode = true;
    public double baseCrystalExplosionPower = 6.0D;
    public boolean baseCrystalExplosionFire = false;
    public Explosion.Effect baseCrystalExplosionEffect = Explosion.Effect.DESTROY;
    private void crystalSettings() {
        baselessCrystalExplode = getBoolean("blocks.end-crystal.baseless.explode", baselessCrystalExplode);
        baselessCrystalExplosionPower = getDouble("blocks.end-crystal.baseless.explosion-power", baselessCrystalExplosionPower);
        baselessCrystalExplosionFire = getBoolean("blocks.end-crystal.baseless.explosion-fire", baselessCrystalExplosionFire);
        try {
            baselessCrystalExplosionEffect = Explosion.Effect.valueOf(getString("blocks.end-crystal.baseless.explosion-effect", baselessCrystalExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.end-crystal.baseless.explosion-effect`! Using default of `DESTROY`");
            baselessCrystalExplosionEffect = Explosion.Effect.DESTROY;
        }
        baseCrystalExplode = getBoolean("blocks.end-crystal.base.explode", baseCrystalExplode);
        baseCrystalExplosionPower = getDouble("blocks.end-crystal.base.explosion-power", baseCrystalExplosionPower);
        baseCrystalExplosionFire = getBoolean("blocks.end-crystal.base.explosion-fire", baseCrystalExplosionFire);
        try {
            baseCrystalExplosionEffect = Explosion.Effect.valueOf(getString("blocks.end-crystal.base.explosion-effect", baseCrystalExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.end-crystal.base.explosion-effect`! Using default of `DESTROY`");
            baseCrystalExplosionEffect = Explosion.Effect.DESTROY;
        }
    }
}
