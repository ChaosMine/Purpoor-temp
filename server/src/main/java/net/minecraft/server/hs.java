package net.minecraft.server;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;

import java.util.function.Consumer;

public class hs implements Consumer<Consumer<Advancement>> {

	public hs() {
	}

	public void accept(Consumer<Advancement> consumer) {
		Advancement advancement = Advancement.SerializedAdvancement.a().a(Blocks.GRASS_BLOCK, new ChatMessage("advancements.story.root.title"), new ChatMessage("advancements.story.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/stone.png"), AdvancementFrameType.TASK, false, false, false).a("crafting_table", CriterionTriggerInventoryChanged.a.a(Blocks.CRAFTING_TABLE)).a(consumer, "story/root");
		Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a(Items.WOODEN_PICKAXE, new ChatMessage("advancements.story.mine_stone.title"), new ChatMessage("advancements.story.mine_stone.description"), null, AdvancementFrameType.TASK, true, true, false).a("get_stone", CriterionTriggerInventoryChanged.a.a(CriterionConditionItem.a_.a().a(TagsItem.STONE_TOOL_MATERIALS).b())).a(consumer, "story/mine_stone");
		Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement1).a(Items.STONE_PICKAXE, new ChatMessage("advancements.story.upgrade_tools.title"), new ChatMessage("advancements.story.upgrade_tools.description"), null, AdvancementFrameType.TASK, true, true, false).a("stone_pickaxe", CriterionTriggerInventoryChanged.a.a(Items.STONE_PICKAXE)).a(consumer, "story/upgrade_tools");
		Advancement advancement3 = Advancement.SerializedAdvancement.a().a(advancement2).a(Items.IRON_INGOT, new ChatMessage("advancements.story.smelt_iron.title"), new ChatMessage("advancements.story.smelt_iron.description"), null, AdvancementFrameType.TASK, true, true, false).a("iron", CriterionTriggerInventoryChanged.a.a(Items.IRON_INGOT)).a(consumer, "story/smelt_iron");
		Advancement advancement4 = Advancement.SerializedAdvancement.a().a(advancement3).a(Items.IRON_PICKAXE, new ChatMessage("advancements.story.iron_tools.title"), new ChatMessage("advancements.story.iron_tools.description"), null, AdvancementFrameType.TASK, true, true, false).a("iron_pickaxe", CriterionTriggerInventoryChanged.a.a(Items.IRON_PICKAXE)).a(consumer, "story/iron_tools");
		Advancement advancement5 = Advancement.SerializedAdvancement.a().a(advancement4).a(Items.DIAMOND, new ChatMessage("advancements.story.mine_diamond.title"), new ChatMessage("advancements.story.mine_diamond.description"), null, AdvancementFrameType.TASK, true, true, false).a("diamond", CriterionTriggerInventoryChanged.a.a(Items.DIAMOND)).a(consumer, "story/mine_diamond");
		Advancement advancement6 = Advancement.SerializedAdvancement.a().a(advancement3).a(Items.LAVA_BUCKET, new ChatMessage("advancements.story.lava_bucket.title"), new ChatMessage("advancements.story.lava_bucket.description"), null, AdvancementFrameType.TASK, true, true, false).a("lava_bucket", CriterionTriggerInventoryChanged.a.a(Items.LAVA_BUCKET)).a(consumer, "story/lava_bucket");
		Advancement advancement7 = Advancement.SerializedAdvancement.a().a(advancement3).a(Items.IRON_CHESTPLATE, new ChatMessage("advancements.story.obtain_armor.title"), new ChatMessage("advancements.story.obtain_armor.description"), null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("iron_helmet", CriterionTriggerInventoryChanged.a.a(Items.IRON_HELMET)).a("iron_chestplate", CriterionTriggerInventoryChanged.a.a(Items.IRON_CHESTPLATE)).a("iron_leggings", CriterionTriggerInventoryChanged.a.a(Items.IRON_LEGGINGS)).a("iron_boots", CriterionTriggerInventoryChanged.a.a(Items.IRON_BOOTS)).a(consumer, "story/obtain_armor");

		Advancement.SerializedAdvancement.a().a(advancement5).a(Items.ENCHANTED_BOOK, new ChatMessage("advancements.story.enchant_item.title"), new ChatMessage("advancements.story.enchant_item.description"), null, AdvancementFrameType.TASK, true, true, false).a("enchanted_item", CriterionTriggerEnchantedItem.a.c()).a(consumer, "story/enchant_item");
		Advancement advancement8 = Advancement.SerializedAdvancement.a().a(advancement6).a(Blocks.OBSIDIAN, new ChatMessage("advancements.story.form_obsidian.title"), new ChatMessage("advancements.story.form_obsidian.description"), null, AdvancementFrameType.TASK, true, true, false).a("obsidian", CriterionTriggerInventoryChanged.a.a(Blocks.OBSIDIAN)).a(consumer, "story/form_obsidian");

		Advancement.SerializedAdvancement.a().a(advancement7).a(Items.SHIELD, new ChatMessage("advancements.story.deflect_arrow.title"), new ChatMessage("advancements.story.deflect_arrow.description"), null, AdvancementFrameType.TASK, true, true, false).a("deflected_projectile", CriterionTriggerEntityHurtPlayer.a.a(CriterionConditionDamage.a_.a().a(CriterionConditionDamageSource.a_.a().a(true)).a(true))).a(consumer, "story/deflect_arrow");
		Advancement.SerializedAdvancement.a().a(advancement5).a(Items.DIAMOND_CHESTPLATE, new ChatMessage("advancements.story.shiny_gear.title"), new ChatMessage("advancements.story.shiny_gear.description"), null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("diamond_helmet", CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_HELMET)).a("diamond_chestplate", CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_CHESTPLATE)).a("diamond_leggings", CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_LEGGINGS)).a("diamond_boots", CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_BOOTS)).a(consumer, "story/shiny_gear");
		Advancement advancement9 = Advancement.SerializedAdvancement.a().a(advancement8).a(Items.FLINT_AND_STEEL, new ChatMessage("advancements.story.enter_the_nether.title"), new ChatMessage("advancements.story.enter_the_nether.description"), null, AdvancementFrameType.TASK, true, true, false).a("entered_nether", CriterionTriggerChangedDimension.a.a(World.THE_NETHER)).a(consumer, "story/enter_the_nether");

		Advancement.SerializedAdvancement.a().a(advancement9).a(Items.GOLDEN_APPLE, new ChatMessage("advancements.story.cure_zombie_villager.title"), new ChatMessage("advancements.story.cure_zombie_villager.description"), null, AdvancementFrameType.GOAL, true, true, false).a("cured_zombie", CriterionTriggerCuredZombieVillager.a.c()).a(consumer, "story/cure_zombie_villager");
		Advancement advancement10 = Advancement.SerializedAdvancement.a().a(advancement9).a(Items.ENDER_EYE, new ChatMessage("advancements.story.follow_ender_eye.title"), new ChatMessage("advancements.story.follow_ender_eye.description"), null, AdvancementFrameType.TASK, true, true, false).a("in_stronghold", CriterionTriggerLocation.a.a(CriterionConditionLocation.a(StructureGenerator.STRONGHOLD))).a(consumer, "story/follow_ender_eye");

		Advancement.SerializedAdvancement.a().a(advancement10).a(Blocks.END_STONE, new ChatMessage("advancements.story.enter_the_end.title"), new ChatMessage("advancements.story.enter_the_end.description"), null, AdvancementFrameType.TASK, true, true, false).a("entered_end", CriterionTriggerChangedDimension.a.a(World.THE_END)).a(consumer, "story/enter_the_end");
	}
}
