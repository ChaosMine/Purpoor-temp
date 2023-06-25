package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionConditionBlock;
import net.minecraft.advancements.critereon.CriterionConditionDamageSource;
import net.minecraft.advancements.critereon.CriterionConditionDistance;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionEntityEquipment;
import net.minecraft.advancements.critereon.CriterionConditionEntityFlags;
import net.minecraft.advancements.critereon.CriterionConditionItem;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.advancements.critereon.CriterionConditionMobEffect;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.advancements.critereon.CriterionTriggerBrewedPotion;
import net.minecraft.advancements.critereon.CriterionTriggerChangedDimension;
import net.minecraft.advancements.critereon.CriterionTriggerConstructBeacon;
import net.minecraft.advancements.critereon.CriterionTriggerEffectsChanged;
import net.minecraft.advancements.critereon.CriterionTriggerInteractBlock;
import net.minecraft.advancements.critereon.CriterionTriggerInventoryChanged;
import net.minecraft.advancements.critereon.CriterionTriggerItemDurabilityChanged;
import net.minecraft.advancements.critereon.CriterionTriggerKilled;
import net.minecraft.advancements.critereon.CriterionTriggerLocation;
import net.minecraft.advancements.critereon.CriterionTriggerNetherTravel;
import net.minecraft.advancements.critereon.CriterionTriggerPlayerGeneratesContainerLoot;
import net.minecraft.advancements.critereon.CriterionTriggerPlayerInteractedWithEntity;
import net.minecraft.advancements.critereon.CriterionTriggerProperties;
import net.minecraft.advancements.critereon.CriterionTriggerSummonedEntity;
import net.minecraft.advancements.critereon.CriterionTriggerThrownItemPickedUpByEntity;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.piglin.PiglinAI;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BlockRespawnAnchor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionEntityProperty;

public class hr implements Consumer<Consumer<Advancement>> {

    private static final List<ResourceKey<BiomeBase>> a = ImmutableList.of(Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST, Biomes.BASALT_DELTAS);
    private static final CriterionConditionEntity.b b = CriterionConditionEntity.b.a(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, CriterionConditionEntity.a.a().a(CriterionConditionEntityEquipment.a.a().a(CriterionConditionItem.a.a().a((IMaterial) Items.GOLDEN_HELMET).b()).b())).a().build(), LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, CriterionConditionEntity.a.a().a(CriterionConditionEntityEquipment.a.a().b(CriterionConditionItem.a.a().a((IMaterial) Items.GOLDEN_CHESTPLATE).b()).b())).a().build(), LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, CriterionConditionEntity.a.a().a(CriterionConditionEntityEquipment.a.a().c(CriterionConditionItem.a.a().a((IMaterial) Items.GOLDEN_LEGGINGS).b()).b())).a().build(), LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, CriterionConditionEntity.a.a().a(CriterionConditionEntityEquipment.a.a().d(CriterionConditionItem.a.a().a((IMaterial) Items.GOLDEN_BOOTS).b()).b())).a().build());

    public hr() {}

    public void accept(Consumer<Advancement> consumer) {
        Advancement advancement = Advancement.SerializedAdvancement.a().a((IMaterial) Blocks.RED_NETHER_BRICKS, new ChatMessage("advancements.nether.root.title"), new ChatMessage("advancements.nether.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/nether.png"), AdvancementFrameType.TASK, false, false, false).a("entered_nether", (CriterionInstance) CriterionTriggerChangedDimension.a.a(World.THE_NETHER)).a(consumer, "nether/root");
        Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.FIRE_CHARGE, new ChatMessage("advancements.nether.return_to_sender.title"), new ChatMessage("advancements.nether.return_to_sender.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(50)).a("killed_ghast", (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a(EntityTypes.GHAST), CriterionConditionDamageSource.a.a().a(true).a(CriterionConditionEntity.a.a().a(EntityTypes.FIREBALL)))).a(consumer, "nether/return_to_sender");
        Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Blocks.NETHER_BRICKS, new ChatMessage("advancements.nether.find_fortress.title"), new ChatMessage("advancements.nether.find_fortress.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("fortress", (CriterionInstance) CriterionTriggerLocation.a.a(CriterionConditionLocation.a(StructureGenerator.FORTRESS))).a(consumer, "nether/find_fortress");

        Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.MAP, new ChatMessage("advancements.nether.fast_travel.title"), new ChatMessage("advancements.nether.fast_travel.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(100)).a("travelled", (CriterionInstance) CriterionTriggerNetherTravel.a.a(CriterionConditionDistance.a(CriterionConditionValue.FloatRange.b(7000.0F)))).a(consumer, "nether/fast_travel");
        Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Items.GHAST_TEAR, new ChatMessage("advancements.nether.uneasy_alliance.title"), new ChatMessage("advancements.nether.uneasy_alliance.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(100)).a("killed_ghast", (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a(EntityTypes.GHAST).a(CriterionConditionLocation.b(World.OVERWORLD)))).a(consumer, "nether/uneasy_alliance");
        Advancement advancement3 = Advancement.SerializedAdvancement.a().a(advancement2).a((IMaterial) Blocks.WITHER_SKELETON_SKULL, new ChatMessage("advancements.nether.get_wither_skull.title"), new ChatMessage("advancements.nether.get_wither_skull.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("wither_skull", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Blocks.WITHER_SKELETON_SKULL)).a(consumer, "nether/get_wither_skull");
        Advancement advancement4 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.NETHER_STAR, new ChatMessage("advancements.nether.summon_wither.title"), new ChatMessage("advancements.nether.summon_wither.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("summoned", (CriterionInstance) CriterionTriggerSummonedEntity.a.a(CriterionConditionEntity.a.a().a(EntityTypes.WITHER))).a(consumer, "nether/summon_wither");
        Advancement advancement5 = Advancement.SerializedAdvancement.a().a(advancement2).a((IMaterial) Items.BLAZE_ROD, new ChatMessage("advancements.nether.obtain_blaze_rod.title"), new ChatMessage("advancements.nether.obtain_blaze_rod.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("blaze_rod", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.BLAZE_ROD)).a(consumer, "nether/obtain_blaze_rod");
        Advancement advancement6 = Advancement.SerializedAdvancement.a().a(advancement4).a((IMaterial) Blocks.BEACON, new ChatMessage("advancements.nether.create_beacon.title"), new ChatMessage("advancements.nether.create_beacon.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("beacon", (CriterionInstance) CriterionTriggerConstructBeacon.a.a(CriterionConditionValue.IntegerRange.b(1))).a(consumer, "nether/create_beacon");

        Advancement.SerializedAdvancement.a().a(advancement6).a((IMaterial) Blocks.BEACON, new ChatMessage("advancements.nether.create_full_beacon.title"), new ChatMessage("advancements.nether.create_full_beacon.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("beacon", (CriterionInstance) CriterionTriggerConstructBeacon.a.a(CriterionConditionValue.IntegerRange.a(4))).a(consumer, "nether/create_full_beacon");
        Advancement advancement7 = Advancement.SerializedAdvancement.a().a(advancement5).a((IMaterial) Items.POTION, new ChatMessage("advancements.nether.brew_potion.title"), new ChatMessage("advancements.nether.brew_potion.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("potion", (CriterionInstance) CriterionTriggerBrewedPotion.a.c()).a(consumer, "nether/brew_potion");
        Advancement advancement8 = Advancement.SerializedAdvancement.a().a(advancement7).a((IMaterial) Items.MILK_BUCKET, new ChatMessage("advancements.nether.all_potions.title"), new ChatMessage("advancements.nether.all_potions.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(100)).a("all_effects", (CriterionInstance) CriterionTriggerEffectsChanged.a.a(CriterionConditionMobEffect.a().a(MobEffects.FASTER_MOVEMENT).a(MobEffects.SLOWER_MOVEMENT).a(MobEffects.INCREASE_DAMAGE).a(MobEffects.JUMP).a(MobEffects.REGENERATION).a(MobEffects.FIRE_RESISTANCE).a(MobEffects.WATER_BREATHING).a(MobEffects.INVISIBILITY).a(MobEffects.NIGHT_VISION).a(MobEffects.WEAKNESS).a(MobEffects.POISON).a(MobEffects.SLOW_FALLING).a(MobEffects.RESISTANCE))).a(consumer, "nether/all_potions");

        Advancement.SerializedAdvancement.a().a(advancement8).a((IMaterial) Items.BUCKET, new ChatMessage("advancements.nether.all_effects.title"), new ChatMessage("advancements.nether.all_effects.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, true).a(AdvancementRewards.a.a(1000)).a("all_effects", (CriterionInstance) CriterionTriggerEffectsChanged.a.a(CriterionConditionMobEffect.a().a(MobEffects.FASTER_MOVEMENT).a(MobEffects.SLOWER_MOVEMENT).a(MobEffects.INCREASE_DAMAGE).a(MobEffects.JUMP).a(MobEffects.REGENERATION).a(MobEffects.FIRE_RESISTANCE).a(MobEffects.WATER_BREATHING).a(MobEffects.INVISIBILITY).a(MobEffects.NIGHT_VISION).a(MobEffects.WEAKNESS).a(MobEffects.POISON).a(MobEffects.WITHER).a(MobEffects.FASTER_DIG).a(MobEffects.SLOWER_DIG).a(MobEffects.LEVITATION).a(MobEffects.GLOWING).a(MobEffects.ABSORBTION).a(MobEffects.HUNGER).a(MobEffects.CONFUSION).a(MobEffects.RESISTANCE).a(MobEffects.SLOW_FALLING).a(MobEffects.CONDUIT_POWER).a(MobEffects.DOLPHINS_GRACE).a(MobEffects.BLINDNESS).a(MobEffects.BAD_OMEN).a(MobEffects.HERO_OF_THE_VILLAGE))).a(consumer, "nether/all_effects");
        Advancement advancement9 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.ry, new ChatMessage("advancements.nether.obtain_ancient_debris.title"), new ChatMessage("advancements.nether.obtain_ancient_debris.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("ancient_debris", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.ry)).a(consumer, "nether/obtain_ancient_debris");

        Advancement.SerializedAdvancement.a().a(advancement9).a((IMaterial) Items.NETHERITE_CHESTPLATE, new ChatMessage("advancements.nether.netherite_armor.title"), new ChatMessage("advancements.nether.netherite_armor.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(100)).a("netherite_armor", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)).a(consumer, "nether/netherite_armor");
        Advancement.SerializedAdvancement.a().a(advancement9).a((IMaterial) Items.rw, new ChatMessage("advancements.nether.use_lodestone.title"), new ChatMessage("advancements.nether.use_lodestone.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("use_lodestone", (CriterionInstance) CriterionTriggerInteractBlock.a.a(CriterionConditionLocation.a.a().a(CriterionConditionBlock.a.a().a(Blocks.LODESTONE).b()), CriterionConditionItem.a.a().a((IMaterial) Items.COMPASS))).a(consumer, "nether/use_lodestone");
        Advancement advancement10 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.rA, new ChatMessage("advancements.nether.obtain_crying_obsidian.title"), new ChatMessage("advancements.nether.obtain_crying_obsidian.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("crying_obsidian", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.rA)).a(consumer, "nether/obtain_crying_obsidian");

        Advancement.SerializedAdvancement.a().a(advancement10).a((IMaterial) Items.rN, new ChatMessage("advancements.nether.charge_respawn_anchor.title"), new ChatMessage("advancements.nether.charge_respawn_anchor.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("charge_respawn_anchor", (CriterionInstance) CriterionTriggerInteractBlock.a.a(CriterionConditionLocation.a.a().a(CriterionConditionBlock.a.a().a(Blocks.RESPAWN_ANCHOR).a(CriterionTriggerProperties.a.a().a(BlockRespawnAnchor.a, 4).b()).b()), CriterionConditionItem.a.a().a((IMaterial) Blocks.GLOWSTONE))).a(consumer, "nether/charge_respawn_anchor");
        Advancement advancement11 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.WARPED_FUNGUS_ON_A_STICK, new ChatMessage("advancements.nether.ride_strider.title"), new ChatMessage("advancements.nether.ride_strider.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("used_warped_fungus_on_a_stick", (CriterionInstance) CriterionTriggerItemDurabilityChanged.a.a(CriterionConditionEntity.b.a(CriterionConditionEntity.a.a().a(CriterionConditionEntity.a.a().a(EntityTypes.STRIDER).b()).b()), CriterionConditionItem.a.a().a((IMaterial) Items.WARPED_FUNGUS_ON_A_STICK).b(), CriterionConditionValue.IntegerRange.e)).a(consumer, "nether/ride_strider");

        hp.a(Advancement.SerializedAdvancement.a(), hr.a).a(advancement11).a((IMaterial) Items.NETHERITE_BOOTS, new ChatMessage("advancements.nether.explore_nether.title"), new ChatMessage("advancements.nether.explore_nether.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(500)).a(consumer, "nether/explore_nether");
        Advancement advancement12 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.rJ, new ChatMessage("advancements.nether.find_bastion.title"), new ChatMessage("advancements.nether.find_bastion.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("bastion", (CriterionInstance) CriterionTriggerLocation.a.a(CriterionConditionLocation.a(StructureGenerator.BASTION_REMNANT))).a(consumer, "nether/find_bastion");

        Advancement.SerializedAdvancement.a().a(advancement12).a((IMaterial) Blocks.CHEST, new ChatMessage("advancements.nether.loot_bastion.title"), new ChatMessage("advancements.nether.loot_bastion.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("loot_bastion_other", (CriterionInstance) CriterionTriggerPlayerGeneratesContainerLoot.a.a(new MinecraftKey("minecraft:chests/bastion_other"))).a("loot_bastion_treasure", (CriterionInstance) CriterionTriggerPlayerGeneratesContainerLoot.a.a(new MinecraftKey("minecraft:chests/bastion_treasure"))).a("loot_bastion_hoglin_stable", (CriterionInstance) CriterionTriggerPlayerGeneratesContainerLoot.a.a(new MinecraftKey("minecraft:chests/bastion_hoglin_stable"))).a("loot_bastion_bridge", (CriterionInstance) CriterionTriggerPlayerGeneratesContainerLoot.a.a(new MinecraftKey("minecraft:chests/bastion_bridge"))).a(consumer, "nether/loot_bastion");
        Advancement.SerializedAdvancement.a().a(advancement).a(AdvancementRequirements.OR).a((IMaterial) Items.GOLD_INGOT, new ChatMessage("advancements.nether.distract_piglin.title"), new ChatMessage("advancements.nether.distract_piglin.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("distract_piglin", (CriterionInstance) CriterionTriggerThrownItemPickedUpByEntity.a.a(hr.b, CriterionConditionItem.a.a().a((Tag) TagsItem.PIGLIN_LOVED), CriterionConditionEntity.b.a(CriterionConditionEntity.a.a().a(EntityTypes.PIGLIN).a(CriterionConditionEntityFlags.a.a().e(false).b()).b()))).a("distract_piglin_directly", (CriterionInstance) CriterionTriggerPlayerInteractedWithEntity.a.a(hr.b, CriterionConditionItem.a.a().a((IMaterial) PiglinAI.a), CriterionConditionEntity.b.a(CriterionConditionEntity.a.a().a(EntityTypes.PIGLIN).a(CriterionConditionEntityFlags.a.a().e(false).b()).b()))).a(consumer, "nether/distract_piglin");
    }
}
