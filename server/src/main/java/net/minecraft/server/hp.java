package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionConditionDamage;
import net.minecraft.advancements.critereon.CriterionConditionDamageSource;
import net.minecraft.advancements.critereon.CriterionConditionDistance;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionEntityEquipment;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.advancements.critereon.CriterionSlideDownBlock;
import net.minecraft.advancements.critereon.CriterionTriggerChanneledLightning;
import net.minecraft.advancements.critereon.CriterionTriggerKilled;
import net.minecraft.advancements.critereon.CriterionTriggerKilledByCrossbow;
import net.minecraft.advancements.critereon.CriterionTriggerLocation;
import net.minecraft.advancements.critereon.CriterionTriggerPlayerHurtEntity;
import net.minecraft.advancements.critereon.CriterionTriggerShotCrossbow;
import net.minecraft.advancements.critereon.CriterionTriggerSummonedEntity;
import net.minecraft.advancements.critereon.CriterionTriggerTargetHit;
import net.minecraft.advancements.critereon.CriterionTriggerUsedTotem;
import net.minecraft.advancements.critereon.CriterionTriggerVillagerTrade;
import net.minecraft.core.IRegistry;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class hp implements Consumer<Consumer<Advancement>> {

    private static final List<ResourceKey<BiomeBase>> a = ImmutableList.of(Biomes.BIRCH_FOREST_HILLS, Biomes.RIVER, Biomes.SWAMP, Biomes.DESERT, Biomes.WOODED_HILLS, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.BADLANDS, Biomes.FOREST, Biomes.STONE_SHORE, Biomes.SNOWY_TUNDRA, Biomes.TAIGA_HILLS, new ResourceKey[]{Biomes.SNOWY_MOUNTAINS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.SAVANNA, Biomes.PLAINS, Biomes.FROZEN_RIVER, Biomes.GIANT_TREE_TAIGA, Biomes.SNOWY_BEACH, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.MUSHROOM_FIELD_SHORE, Biomes.MOUNTAINS, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.BEACH, Biomes.SAVANNA_PLATEAU, Biomes.SNOWY_TAIGA_HILLS, Biomes.BADLANDS_PLATEAU, Biomes.DARK_FOREST, Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.MUSHROOM_FIELDS, Biomes.WOODED_MOUNTAINS, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE_HILLS});
    private static final EntityTypes<?>[] b = new EntityTypes[]{EntityTypes.BLAZE, EntityTypes.CAVE_SPIDER, EntityTypes.CREEPER, EntityTypes.DROWNED, EntityTypes.ELDER_GUARDIAN, EntityTypes.ENDER_DRAGON, EntityTypes.ENDERMAN, EntityTypes.ENDERMITE, EntityTypes.EVOKER, EntityTypes.GHAST, EntityTypes.GUARDIAN, EntityTypes.HOGLIN, EntityTypes.HUSK, EntityTypes.MAGMA_CUBE, EntityTypes.PHANTOM, EntityTypes.PIGLIN, EntityTypes.PIGLIN_BRUTE, EntityTypes.PILLAGER, EntityTypes.RAVAGER, EntityTypes.SHULKER, EntityTypes.SILVERFISH, EntityTypes.SKELETON, EntityTypes.SLIME, EntityTypes.SPIDER, EntityTypes.STRAY, EntityTypes.VEX, EntityTypes.VINDICATOR, EntityTypes.WITCH, EntityTypes.WITHER_SKELETON, EntityTypes.WITHER, EntityTypes.ZOGLIN, EntityTypes.ZOMBIE_VILLAGER, EntityTypes.ZOMBIE, EntityTypes.ZOMBIFIED_PIGLIN};

    public hp() {}

    public void accept(Consumer<Advancement> consumer) {
        Advancement advancement = Advancement.SerializedAdvancement.a().a((IMaterial) Items.MAP, new ChatMessage("advancements.adventure.root.title"), new ChatMessage("advancements.adventure.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/adventure.png"), AdvancementFrameType.TASK, false, false, false).a(AdvancementRequirements.OR).a("killed_something", (CriterionInstance) CriterionTriggerKilled.a.c()).a("killed_by_something", (CriterionInstance) CriterionTriggerKilled.a.d()).a(consumer, "adventure/root");
        Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Blocks.RED_BED, new ChatMessage("advancements.adventure.sleep_in_bed.title"), new ChatMessage("advancements.adventure.sleep_in_bed.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("slept_in_bed", (CriterionInstance) CriterionTriggerLocation.a.c()).a(consumer, "adventure/sleep_in_bed");

        a(Advancement.SerializedAdvancement.a(), hp.a).a(advancement1).a((IMaterial) Items.DIAMOND_BOOTS, new ChatMessage("advancements.adventure.adventuring_time.title"), new ChatMessage("advancements.adventure.adventuring_time.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(500)).a(consumer, "adventure/adventuring_time");
        Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.EMERALD, new ChatMessage("advancements.adventure.trade.title"), new ChatMessage("advancements.adventure.trade.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("traded", (CriterionInstance) CriterionTriggerVillagerTrade.a.c()).a(consumer, "adventure/trade");
        Advancement advancement3 = this.a(Advancement.SerializedAdvancement.a()).a(advancement).a((IMaterial) Items.IRON_SWORD, new ChatMessage("advancements.adventure.kill_a_mob.title"), new ChatMessage("advancements.adventure.kill_a_mob.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a(consumer, "adventure/kill_a_mob");

        this.a(Advancement.SerializedAdvancement.a()).a(advancement3).a((IMaterial) Items.DIAMOND_SWORD, new ChatMessage("advancements.adventure.kill_all_mobs.title"), new ChatMessage("advancements.adventure.kill_all_mobs.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(100)).a(consumer, "adventure/kill_all_mobs");
        Advancement advancement4 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.BOW, new ChatMessage("advancements.adventure.shoot_arrow.title"), new ChatMessage("advancements.adventure.shoot_arrow.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("shot_arrow", (CriterionInstance) CriterionTriggerPlayerHurtEntity.a.a(CriterionConditionDamage.a.a().a(CriterionConditionDamageSource.a.a().a(true).a(CriterionConditionEntity.a.a().a((Tag) TagsEntity.ARROWS))))).a(consumer, "adventure/shoot_arrow");
        Advancement advancement5 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.TRIDENT, new ChatMessage("advancements.adventure.throw_trident.title"), new ChatMessage("advancements.adventure.throw_trident.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("shot_trident", (CriterionInstance) CriterionTriggerPlayerHurtEntity.a.a(CriterionConditionDamage.a.a().a(CriterionConditionDamageSource.a.a().a(true).a(CriterionConditionEntity.a.a().a(EntityTypes.TRIDENT))))).a(consumer, "adventure/throw_trident");

        Advancement.SerializedAdvancement.a().a(advancement5).a((IMaterial) Items.TRIDENT, new ChatMessage("advancements.adventure.very_very_frightening.title"), new ChatMessage("advancements.adventure.very_very_frightening.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("struck_villager", (CriterionInstance) CriterionTriggerChanneledLightning.a.a(CriterionConditionEntity.a.a().a(EntityTypes.VILLAGER).b())).a(consumer, "adventure/very_very_frightening");
        Advancement.SerializedAdvancement.a().a(advancement2).a((IMaterial) Blocks.CARVED_PUMPKIN, new ChatMessage("advancements.adventure.summon_iron_golem.title"), new ChatMessage("advancements.adventure.summon_iron_golem.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("summoned_golem", (CriterionInstance) CriterionTriggerSummonedEntity.a.a(CriterionConditionEntity.a.a().a(EntityTypes.IRON_GOLEM))).a(consumer, "adventure/summon_iron_golem");
        Advancement.SerializedAdvancement.a().a(advancement4).a((IMaterial) Items.ARROW, new ChatMessage("advancements.adventure.sniper_duel.title"), new ChatMessage("advancements.adventure.sniper_duel.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(50)).a("killed_skeleton", (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a(EntityTypes.SKELETON).a(CriterionConditionDistance.a(CriterionConditionValue.FloatRange.b(50.0F))), CriterionConditionDamageSource.a.a().a(true))).a(consumer, "adventure/sniper_duel");
        Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.TOTEM_OF_UNDYING, new ChatMessage("advancements.adventure.totem_of_undying.title"), new ChatMessage("advancements.adventure.totem_of_undying.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("used_totem", (CriterionInstance) CriterionTriggerUsedTotem.a.a((IMaterial) Items.TOTEM_OF_UNDYING)).a(consumer, "adventure/totem_of_undying");
        Advancement advancement6 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.CROSSBOW, new ChatMessage("advancements.adventure.ol_betsy.title"), new ChatMessage("advancements.adventure.ol_betsy.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("shot_crossbow", (CriterionInstance) CriterionTriggerShotCrossbow.a.a((IMaterial) Items.CROSSBOW)).a(consumer, "adventure/ol_betsy");

        Advancement.SerializedAdvancement.a().a(advancement6).a((IMaterial) Items.CROSSBOW, new ChatMessage("advancements.adventure.whos_the_pillager_now.title"), new ChatMessage("advancements.adventure.whos_the_pillager_now.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("kill_pillager", (CriterionInstance) CriterionTriggerKilledByCrossbow.a.a(CriterionConditionEntity.a.a().a(EntityTypes.PILLAGER))).a(consumer, "adventure/whos_the_pillager_now");
        Advancement.SerializedAdvancement.a().a(advancement6).a((IMaterial) Items.CROSSBOW, new ChatMessage("advancements.adventure.two_birds_one_arrow.title"), new ChatMessage("advancements.adventure.two_birds_one_arrow.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(65)).a("two_birds", (CriterionInstance) CriterionTriggerKilledByCrossbow.a.a(CriterionConditionEntity.a.a().a(EntityTypes.PHANTOM), CriterionConditionEntity.a.a().a(EntityTypes.PHANTOM))).a(consumer, "adventure/two_birds_one_arrow");
        Advancement.SerializedAdvancement.a().a(advancement6).a((IMaterial) Items.CROSSBOW, new ChatMessage("advancements.adventure.arbalistic.title"), new ChatMessage("advancements.adventure.arbalistic.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, true).a(AdvancementRewards.a.a(85)).a("arbalistic", (CriterionInstance) CriterionTriggerKilledByCrossbow.a.a(CriterionConditionValue.IntegerRange.a(5))).a(consumer, "adventure/arbalistic");
        Advancement advancement7 = Advancement.SerializedAdvancement.a().a(advancement).a(Raid.s(), new ChatMessage("advancements.adventure.voluntary_exile.title"), new ChatMessage("advancements.adventure.voluntary_exile.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, true).a("voluntary_exile", (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a((Tag) TagsEntity.RADIERS).a(CriterionConditionEntityEquipment.b))).a(consumer, "adventure/voluntary_exile");

        Advancement.SerializedAdvancement.a().a(advancement7).a(Raid.s(), new ChatMessage("advancements.adventure.hero_of_the_village.title"), new ChatMessage("advancements.adventure.hero_of_the_village.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, true).a(AdvancementRewards.a.a(100)).a("hero_of_the_village", (CriterionInstance) CriterionTriggerLocation.a.d()).a(consumer, "adventure/hero_of_the_village");
        Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Blocks.HONEY_BLOCK.getItem(), new ChatMessage("advancements.adventure.honey_block_slide.title"), new ChatMessage("advancements.adventure.honey_block_slide.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("honey_block_slide", (CriterionInstance) CriterionSlideDownBlock.a.a(Blocks.HONEY_BLOCK)).a(consumer, "adventure/honey_block_slide");
        Advancement.SerializedAdvancement.a().a(advancement4).a((IMaterial) Blocks.TARGET.getItem(), new ChatMessage("advancements.adventure.bullseye.title"), new ChatMessage("advancements.adventure.bullseye.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(50)).a("bullseye", (CriterionInstance) CriterionTriggerTargetHit.a.a(CriterionConditionValue.IntegerRange.a(15), CriterionConditionEntity.b.a(CriterionConditionEntity.a.a().a(CriterionConditionDistance.a(CriterionConditionValue.FloatRange.b(30.0F))).b()))).a(consumer, "adventure/bullseye");
    }

    private Advancement.SerializedAdvancement a(Advancement.SerializedAdvancement advancement_serializedadvancement) {
        EntityTypes[] aentitytypes = hp.b;
        int i = aentitytypes.length;

        for (int j = 0; j < i; ++j) {
            EntityTypes<?> entitytypes = aentitytypes[j];

            advancement_serializedadvancement.a(IRegistry.ENTITY_TYPE.getKey(entitytypes).toString(), (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a(entitytypes)));
        }

        return advancement_serializedadvancement;
    }

    protected static Advancement.SerializedAdvancement a(Advancement.SerializedAdvancement advancement_serializedadvancement, List<ResourceKey<BiomeBase>> list) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ResourceKey<BiomeBase> resourcekey = (ResourceKey) iterator.next();

            advancement_serializedadvancement.a(resourcekey.a().toString(), (CriterionInstance) CriterionTriggerLocation.a.a(CriterionConditionLocation.a(resourcekey)));
        }

        return advancement_serializedadvancement;
    }
}
