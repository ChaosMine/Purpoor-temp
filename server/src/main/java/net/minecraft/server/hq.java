package net.minecraft.server;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.IRegistry;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class hq implements Consumer<Consumer<Advancement>> {

	private static final EntityTypes<?>[] a = new EntityTypes[]{EntityTypes.HORSE, EntityTypes.DONKEY, EntityTypes.MULE, EntityTypes.SHEEP, EntityTypes.COW, EntityTypes.MOOSHROOM, EntityTypes.PIG, EntityTypes.CHICKEN, EntityTypes.WOLF, EntityTypes.OCELOT, EntityTypes.RABBIT, EntityTypes.LLAMA, EntityTypes.CAT, EntityTypes.PANDA, EntityTypes.FOX, EntityTypes.BEE, EntityTypes.HOGLIN, EntityTypes.STRIDER};
	private static final Item[] b = new Item[]{Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON};
	private static final Item[] c = new Item[]{Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET};
	private static final Item[] d = new Item[]{Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE};

	public hq() {
	}

	public void accept(Consumer<Advancement> consumer) {
		Advancement advancement = Advancement.SerializedAdvancement.a().a(Blocks.HAY_BLOCK, new ChatMessage("advancements.husbandry.root.title"), new ChatMessage("advancements.husbandry.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/husbandry.png"), AdvancementFrameType.TASK, false, false, false).a("consumed_item", CriterionTriggerConsumeItem.a.c()).a(consumer, "husbandry/root");
		Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a(Items.WHEAT, new ChatMessage("advancements.husbandry.plant_seed.title"), new ChatMessage("advancements.husbandry.plant_seed.description"), null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("wheat", CriterionTriggerPlacedBlock.a.a(Blocks.WHEAT)).a("pumpkin_stem", CriterionTriggerPlacedBlock.a.a(Blocks.PUMPKIN_STEM)).a("melon_stem", CriterionTriggerPlacedBlock.a.a(Blocks.MELON_STEM)).a("beetroots", CriterionTriggerPlacedBlock.a.a(Blocks.BEETROOTS)).a("nether_wart", CriterionTriggerPlacedBlock.a.a(Blocks.NETHER_WART)).a(consumer, "husbandry/plant_seed");
		Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement).a(Items.WHEAT, new ChatMessage("advancements.husbandry.breed_an_animal.title"), new ChatMessage("advancements.husbandry.breed_an_animal.description"), null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("bred", CriterionTriggerBredAnimals.a.c()).a(consumer, "husbandry/breed_an_animal");

		this.a(Advancement.SerializedAdvancement.a()).a(advancement1).a(Items.APPLE, new ChatMessage("advancements.husbandry.balanced_diet.title"), new ChatMessage("advancements.husbandry.balanced_diet.description"), null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a_.a(100)).a(consumer, "husbandry/balanced_diet");
		Advancement.SerializedAdvancement.a().a(advancement1).a(Items.NETHERITE_HOE, new ChatMessage("advancements.husbandry.netherite_hoe.title"), new ChatMessage("advancements.husbandry.netherite_hoe.description"), null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a_.a(100)).a("netherite_hoe", CriterionTriggerInventoryChanged.a.a(Items.NETHERITE_HOE)).a(consumer, "husbandry/obtain_netherite_hoe");
		Advancement advancement3 = Advancement.SerializedAdvancement.a().a(advancement).a(Items.LEAD, new ChatMessage("advancements.husbandry.tame_an_animal.title"), new ChatMessage("advancements.husbandry.tame_an_animal.description"), null, AdvancementFrameType.TASK, true, true, false).a("tamed_animal", CriterionTriggerTamedAnimal.a.c()).a(consumer, "husbandry/tame_an_animal");

		this.b(Advancement.SerializedAdvancement.a()).a(advancement2).a(Items.GOLDEN_CARROT, new ChatMessage("advancements.husbandry.breed_all_animals.title"), new ChatMessage("advancements.husbandry.breed_all_animals.description"), null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a_.a(100)).a(consumer, "husbandry/bred_all_animals");
		Advancement advancement4 = this.d(Advancement.SerializedAdvancement.a()).a(advancement).a(AdvancementRequirements.OR).a(Items.FISHING_ROD, new ChatMessage("advancements.husbandry.fishy_business.title"), new ChatMessage("advancements.husbandry.fishy_business.description"), null, AdvancementFrameType.TASK, true, true, false).a(consumer, "husbandry/fishy_business");

		this.c(Advancement.SerializedAdvancement.a()).a(advancement4).a(AdvancementRequirements.OR).a(Items.PUFFERFISH_BUCKET, new ChatMessage("advancements.husbandry.tactical_fishing.title"), new ChatMessage("advancements.husbandry.tactical_fishing.description"), null, AdvancementFrameType.TASK, true, true, false).a(consumer, "husbandry/tactical_fishing");
		this.e(Advancement.SerializedAdvancement.a()).a(advancement3).a(Items.COD, new ChatMessage("advancements.husbandry.complete_catalogue.title"), new ChatMessage("advancements.husbandry.complete_catalogue.description"), null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a_.a(50)).a(consumer, "husbandry/complete_catalogue");
		Advancement.SerializedAdvancement.a().a(advancement).a("safely_harvest_honey", CriterionTriggerInteractBlock.a.a(CriterionConditionLocation.a_.a().a(CriterionConditionBlock.a_.a().a(TagsBlock.BEEHIVES).b()).a(true), CriterionConditionItem.a_.a().a(Items.GLASS_BOTTLE))).a(Items.HONEY_BOTTLE, new ChatMessage("advancements.husbandry.safely_harvest_honey.title"), new ChatMessage("advancements.husbandry.safely_harvest_honey.description"), null, AdvancementFrameType.TASK, true, true, false).a(consumer, "husbandry/safely_harvest_honey");
		Advancement.SerializedAdvancement.a().a(advancement).a("silk_touch_nest", CriterionTriggerBeeNestDestroyed.a.a(Blocks.BEE_NEST, CriterionConditionItem.a_.a().a(new CriterionConditionEnchantments(Enchantments.SILK_TOUCH, CriterionConditionValue.IntegerRange.b(1))), CriterionConditionValue.IntegerRange.a(3))).a(Blocks.BEE_NEST, new ChatMessage("advancements.husbandry.silk_touch_nest.title"), new ChatMessage("advancements.husbandry.silk_touch_nest.description"), null, AdvancementFrameType.TASK, true, true, false).a(consumer, "husbandry/silk_touch_nest");
	}

	private Advancement.SerializedAdvancement a(Advancement.SerializedAdvancement advancement_serializedadvancement) {
		Item[] aitem = hq.d;
		int i = aitem.length;

		for (int j = 0; j < i; ++j) {
			Item item = aitem[j];

			advancement_serializedadvancement.a(IRegistry.ITEM.getKey(item).getKey(), CriterionTriggerConsumeItem.a.a(item));
		}

		return advancement_serializedadvancement;
	}

	private Advancement.SerializedAdvancement b(Advancement.SerializedAdvancement advancement_serializedadvancement) {
		EntityTypes[] aentitytypes = hq.a;
		int i = aentitytypes.length;

		for (int j = 0; j < i; ++j) {
			EntityTypes<?> entitytypes = aentitytypes[j];

			advancement_serializedadvancement.a(EntityTypes.getName(entitytypes).toString(), CriterionTriggerBredAnimals.a.a(CriterionConditionEntity.a_.a().a(entitytypes)));
		}

		advancement_serializedadvancement.a(EntityTypes.getName(EntityTypes.TURTLE).toString(), CriterionTriggerBredAnimals.a.a(CriterionConditionEntity.a_.a().a(EntityTypes.TURTLE).b(), CriterionConditionEntity.a_.a().a(EntityTypes.TURTLE).b(), CriterionConditionEntity.a));
		return advancement_serializedadvancement;
	}

	private Advancement.SerializedAdvancement c(Advancement.SerializedAdvancement advancement_serializedadvancement) {
		Item[] aitem = hq.c;
		int i = aitem.length;

		for (int j = 0; j < i; ++j) {
			Item item = aitem[j];

			advancement_serializedadvancement.a(IRegistry.ITEM.getKey(item).getKey(), CriterionTriggerFilledBucket.a.a(CriterionConditionItem.a_.a().a(item).b()));
		}

		return advancement_serializedadvancement;
	}

	private Advancement.SerializedAdvancement d(Advancement.SerializedAdvancement advancement_serializedadvancement) {
		Item[] aitem = hq.b;
		int i = aitem.length;

		for (int j = 0; j < i; ++j) {
			Item item = aitem[j];

			advancement_serializedadvancement.a(IRegistry.ITEM.getKey(item).getKey(), CriterionTriggerFishingRodHooked.a.a(CriterionConditionItem.a, CriterionConditionEntity.a, CriterionConditionItem.a_.a().a(item).b()));
		}

		return advancement_serializedadvancement;
	}

	private Advancement.SerializedAdvancement e(Advancement.SerializedAdvancement advancement_serializedadvancement) {
		EntityCat.bq.forEach((integer, minecraftkey) -> {
			advancement_serializedadvancement.a(minecraftkey.getKey(), CriterionTriggerTamedAnimal.a.a(CriterionConditionEntity.a_.a().a(minecraftkey).b()));
		});
		return advancement_serializedadvancement;
	}
}
