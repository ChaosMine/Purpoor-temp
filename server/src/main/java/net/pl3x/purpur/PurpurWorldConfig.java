package net.pl3x.purpur;

import net.minecraft.core.IRegistry;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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

    public double vindicatorJohnnySpawnChance = 0D;
    private void vindicatorSettings() {
        vindicatorJohnnySpawnChance = getDouble("mobs.vindicator.johnny.spawn-chance", vindicatorJohnnySpawnChance);
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

    public boolean useBetterMending = false;
    public boolean boatEjectPlayersOnLand = false;
    public boolean disableDropsOnCrammingDeath = false;
    public boolean entitiesPickUpLootBypassMobGriefing = false;
    public boolean milkCuresBadOmen = true;
    public double tridentLoyaltyVoidReturnHeight = 0.0D;
    public double voidDamageHeight = -64.0D;
    public double voidDamageDealt = 4.0D;
    private void miscGameplayMechanicsSettings() {
        useBetterMending = getBoolean("gameplay-mechanics.use-better-mending", useBetterMending);
        boatEjectPlayersOnLand = getBoolean("gameplay-mechanics.boat.eject-players-on-land", boatEjectPlayersOnLand);
        disableDropsOnCrammingDeath = getBoolean("gameplay-mechanics.disable-drops-on-cramming-death", disableDropsOnCrammingDeath);
        entitiesPickUpLootBypassMobGriefing = getBoolean("gameplay-mechanics.entities-pick-up-loot-bypass-mob-griefing", entitiesPickUpLootBypassMobGriefing);
        milkCuresBadOmen = getBoolean("gameplay-mechanics.milk-cures-bad-omen", milkCuresBadOmen);
        tridentLoyaltyVoidReturnHeight = getDouble("gameplay-mechanics.trident-loyalty-void-return-height", tridentLoyaltyVoidReturnHeight);
        voidDamageHeight = getDouble("gameplay-mechanics.void-damage-height", voidDamageHeight);
        voidDamageDealt = getDouble("gameplay-mechanics.void-damage-dealt", voidDamageDealt);
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
    private void farmlandSettings() {
        farmlandGetsMoistFromBelow = getBoolean("blocks.farmland.gets-moist-from-below", farmlandGetsMoistFromBelow);
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

    public boolean turtleEggsBreakFromExpOrbs = true;
    public boolean turtleEggsBreakFromItems = true;
    public boolean turtleEggsBreakFromMinecarts = true;
    private void turtleEggSettings() {
        turtleEggsBreakFromExpOrbs = getBoolean("blocks.turtle_egg.break-from-exp-orbs", turtleEggsBreakFromExpOrbs);
        turtleEggsBreakFromItems = getBoolean("blocks.turtle_egg.break-from-items", turtleEggsBreakFromItems);
        turtleEggsBreakFromMinecarts = getBoolean("blocks.turtle_egg.break-from-minecarts", turtleEggsBreakFromMinecarts);
    }

    public int catSpawnDelay = 1200;
    public int catSpawnSwampHutScanRange = 16;
    public int catSpawnVillageScanRange = 48;
    private void catSettings() {
        catSpawnDelay = getInt("mobs.cat.spawn-delay", catSpawnDelay);
        catSpawnSwampHutScanRange = getInt("mobs.cat.scan-range-for-other-cats.swamp-hut", catSpawnSwampHutScanRange);
        catSpawnVillageScanRange = getInt("mobs.cat.scan-range-for-other-cats.village", catSpawnVillageScanRange);
    }

    public boolean chickenRetaliate = false;
    private void chickenSettings() {
        chickenRetaliate = getBoolean("mobs.chicken.retaliate", chickenRetaliate);
    }

    public int cowFeedMushrooms = 0;
    private void cowSettings() {
        cowFeedMushrooms = getInt("mobs.cow.feed-mushrooms-for-mooshroom", cowFeedMushrooms);
    }

    public boolean creeperAllowGriefing = true;
    public double creeperChargedChance = 0.0D;
    private void creeperSettings() {
        creeperAllowGriefing = getBoolean("mobs.creeper.allow-griefing", creeperAllowGriefing);
        creeperChargedChance = getDouble("mobs.creeper.naturally-charged-chance", creeperChargedChance);
    }

    public boolean drownedJockeyOnlyBaby = true;
    public double drownedJockeyChance = 0.05D;
    public boolean drownedJockeyTryExistingChickens = true;
    private void drownedSettings() {
        drownedJockeyOnlyBaby = getBoolean("mobs.drowned.jockey.only-babies", drownedJockeyOnlyBaby);
        drownedJockeyChance = getDouble("mobs.drowned.jockey.chance", drownedJockeyChance);
        drownedJockeyTryExistingChickens = getBoolean("mobs.drowned.jockey.try-existing-chickens", drownedJockeyTryExistingChickens);
    }

    public boolean enderDragonAlwaysDropsFullExp = false;
    private void enderDragonSettings() {
        enderDragonAlwaysDropsFullExp = getBoolean("mobs.ender_dragon.always-drop-full-exp", enderDragonAlwaysDropsFullExp);
    }

    public boolean endermanAllowGriefing = true;
    private void endermanSettings() {
        endermanAllowGriefing = getBoolean("mobs.enderman.allow-griefing", endermanAllowGriefing);
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

    public boolean huskJockeyOnlyBaby = true;
    public double huskJockeyChance = 0.05D;
    public boolean huskJockeyTryExistingChickens = true;
    private void huskSettings() {
        huskJockeyOnlyBaby = getBoolean("mobs.husk.jockey.only-babies", huskJockeyOnlyBaby);
        huskJockeyChance = getDouble("mobs.husk.jockey.chance", huskJockeyChance);
        huskJockeyTryExistingChickens = getBoolean("mobs.husk.jockey.try-existing-chickens", huskJockeyTryExistingChickens);
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
    private void phantomSettings() {
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
    }

    public boolean pigGiveSaddleBack = false;
    private void pigSettings() {
        pigGiveSaddleBack = getBoolean("mobs.pig.give-saddle-back", pigGiveSaddleBack);
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

    public boolean snowGolemDropsPumpkin = true;
    public boolean snowGolemPutPumpkinBack = false;
    private void snowGolemSettings() {
        snowGolemDropsPumpkin = getBoolean("mobs.snow_golem.drop-pumpkin-when-sheared", snowGolemDropsPumpkin);
        snowGolemPutPumpkinBack = getBoolean("mobs.snow_golem.pumpkin-can-be-added-back", snowGolemPutPumpkinBack);
    }

    public boolean squidImmuneToEAR = true;
    private void squidSettings() {
        squidImmuneToEAR = getBoolean("mobs.squid.immune-to-EAR", squidImmuneToEAR);
    }

    public int villagerBrainTicks = 1;
    public boolean villagerUseBrainTicksOnlyWhenLagging = true;
    public boolean villagerCanBeLeashed = false;
    public boolean villagerFarmingBypassMobGriefing = false;
    public boolean villagerFollowEmeraldBlock = false;
    public int villagerSpawnIronGolemRadius = 0;
    public int villagerSpawnIronGolemLimit = 0;
    public boolean villagerCanBreed = true;
    private void villagerSettings() {
        villagerBrainTicks = getInt("mobs.villager.brain-ticks", villagerBrainTicks);
        villagerUseBrainTicksOnlyWhenLagging = getBoolean("mobs.villager.use-brain-ticks-only-when-lagging", villagerUseBrainTicksOnlyWhenLagging);
        villagerCanBeLeashed = getBoolean("mobs.villager.can-be-leashed", villagerCanBeLeashed);
        villagerFarmingBypassMobGriefing = getBoolean("mobs.villager.bypass-mob-griefing", villagerFarmingBypassMobGriefing);
        villagerFollowEmeraldBlock = getBoolean("mobs.villager.follow-emerald-blocks", villagerFollowEmeraldBlock);
        villagerSpawnIronGolemRadius = getInt("mobs.villager.spawn-iron-golem.radius", villagerSpawnIronGolemRadius);
        villagerSpawnIronGolemLimit = getInt("mobs.villager.spawn-iron-golem.limit", villagerSpawnIronGolemLimit);
        villagerCanBreed = getBoolean("mobs.villager.can-breed", villagerCanBreed);
    }

    public boolean villagerTraderCanBeLeashed = false;
    public boolean villagerTraderFollowEmeraldBlock = false;
    private void villagerTraderSettings() {
        villagerTraderCanBeLeashed = getBoolean("mobs.wandering_trader.can-be-leashed", villagerTraderCanBeLeashed);
        villagerTraderFollowEmeraldBlock = getBoolean("mobs.wandering_trader.follow-emerald-blocks", villagerTraderFollowEmeraldBlock);
    }

    public boolean witherSkeletonTakesWitherDamage = false;
    private void witherSkeletonSettings() {
        witherSkeletonTakesWitherDamage = getBoolean("mobs.wither_skeleton.takes-wither-damage", witherSkeletonTakesWitherDamage);
    }

    public boolean zombieJockeyOnlyBaby = true;
    public double zombieJockeyChance = 0.05D;
    public boolean zombieJockeyTryExistingChickens = true;
    private void zombieSettings() {
        zombieJockeyOnlyBaby = getBoolean("mobs.zombie.jockey.only-babies", zombieJockeyOnlyBaby);
        zombieJockeyChance = getDouble("mobs.zombie.jockey.chance", zombieJockeyChance);
        zombieJockeyTryExistingChickens = getBoolean("mobs.zombie.jockey.try-existing-chickens", zombieJockeyTryExistingChickens);
    }

    public double zombieHorseSpawnChance = 0.0D;
    private void zombieHorseSettings() {
        zombieHorseSpawnChance = getDouble("mobs.zombie_horse.spawn-chance", zombieHorseSpawnChance);
    }

    public boolean zombifiedPiglinJockeyOnlyBaby = true;
    public double zombifiedPiglinJockeyChance = 0.05D;
    public boolean zombifiedPiglinJockeyTryExistingChickens = true;
    private void zombifiedPiglinSettings() {
        zombifiedPiglinJockeyOnlyBaby = getBoolean("mobs.zombified_piglin.jockey.only-babies", zombifiedPiglinJockeyOnlyBaby);
        zombifiedPiglinJockeyChance = getDouble("mobs.zombified_piglin.jockey.chance", zombifiedPiglinJockeyChance);
        zombifiedPiglinJockeyTryExistingChickens = getBoolean("mobs.zombified_piglin.jockey.try-existing-chickens", zombifiedPiglinJockeyTryExistingChickens);
    }

    public boolean zombieVillagerJockeyOnlyBaby = true;
    public double zombieVillagerJockeyChance = 0.05D;
    public boolean zombieVillagerJockeyTryExistingChickens = true;
    private void zombieVillagerSettings() {
        zombieVillagerJockeyOnlyBaby = getBoolean("mobs.zombie_villager.jockey.only-babies", zombieVillagerJockeyOnlyBaby);
        zombieVillagerJockeyChance = getDouble("mobs.zombie_villager.jockey.chance", zombieVillagerJockeyChance);
        zombieVillagerJockeyTryExistingChickens = getBoolean("mobs.zombie_villager.jockey.try-existing-chickens", zombieVillagerJockeyTryExistingChickens);
    }
}
