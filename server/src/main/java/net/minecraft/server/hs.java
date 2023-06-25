package net.minecraft.server;

import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionConditionDamage;
import net.minecraft.advancements.critereon.CriterionConditionDamageSource;
import net.minecraft.advancements.critereon.CriterionConditionItem;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.advancements.critereon.CriterionTriggerChangedDimension;
import net.minecraft.advancements.critereon.CriterionTriggerCuredZombieVillager;
import net.minecraft.advancements.critereon.CriterionTriggerEnchantedItem;
import net.minecraft.advancements.critereon.CriterionTriggerEntityHurtPlayer;
import net.minecraft.advancements.critereon.CriterionTriggerInventoryChanged;
import net.minecraft.advancements.critereon.CriterionTriggerLocation;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;

public class hs implements Consumer<Consumer<Advancement>> {

    public hs() {}

    public void accept(Consumer<Advancement> consumer) {
        Advancement advancement = Advancement.SerializedAdvancement.a().a((IMaterial) Blocks.GRASS_BLOCK, new ChatMessage("advancements.story.root.title"), new ChatMessage("advancements.story.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/stone.png"), AdvancementFrameType.TASK, false, false, false).a("crafting_table", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Blocks.CRAFTING_TABLE)).a(consumer, "story/root");
        Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Items.WOODEN_PICKAXE, new ChatMessage("advancements.story.mine_stone.title"), new ChatMessage("advancements.story.mine_stone.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("get_stone", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(CriterionConditionItem.a.a().a((Tag) TagsItem.STONE_TOOL_MATERIALS).b())).a(consumer, "story/mine_stone");
        Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Items.STONE_PICKAXE, new ChatMessage("advancements.story.upgrade_tools.title"), new ChatMessage("advancements.story.upgrade_tools.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("stone_pickaxe", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.STONE_PICKAXE)).a(consumer, "story/upgrade_tools");
        Advancement advancement3 = Advancement.SerializedAdvancement.a().a(advancement2).a((IMaterial) Items.IRON_INGOT, new ChatMessage("advancements.story.smelt_iron.title"), new ChatMessage("advancements.story.smelt_iron.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("iron", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_INGOT)).a(consumer, "story/smelt_iron");
        Advancement advancement4 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.IRON_PICKAXE, new ChatMessage("advancements.story.iron_tools.title"), new ChatMessage("advancements.story.iron_tools.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("iron_pickaxe", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_PICKAXE)).a(consumer, "story/iron_tools");
        Advancement advancement5 = Advancement.SerializedAdvancement.a().a(advancement4).a((IMaterial) Items.DIAMOND, new ChatMessage("advancements.story.mine_diamond.title"), new ChatMessage("advancements.story.mine_diamond.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("diamond", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DIAMOND)).a(consumer, "story/mine_diamond");
        Advancement advancement6 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.LAVA_BUCKET, new ChatMessage("advancements.story.lava_bucket.title"), new ChatMessage("advancements.story.lava_bucket.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("lava_bucket", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.LAVA_BUCKET)).a(consumer, "story/lava_bucket");
        Advancement advancement7 = Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.IRON_CHESTPLATE, new ChatMessage("advancements.story.obtain_armor.title"), new ChatMessage("advancements.story.obtain_armor.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("iron_helmet", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_HELMET)).a("iron_chestplate", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_CHESTPLATE)).a("iron_leggings", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_LEGGINGS)).a("iron_boots", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.IRON_BOOTS)).a(consumer, "story/obtain_armor");

        Advancement.SerializedAdvancement.a().a(advancement5).a((IMaterial) Items.ENCHANTED_BOOK, new ChatMessage("advancements.story.enchant_item.title"), new ChatMessage("advancements.story.enchant_item.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("enchanted_item", (CriterionInstance) CriterionTriggerEnchantedItem.a.c()).a(consumer, "story/enchant_item");
        Advancement advancement8 = Advancement.SerializedAdvancement.a().a(advancement6).a((IMaterial) Blocks.OBSIDIAN, new ChatMessage("advancements.story.form_obsidian.title"), new ChatMessage("advancements.story.form_obsidian.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("obsidian", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Blocks.OBSIDIAN)).a(consumer, "story/form_obsidian");

        Advancement.SerializedAdvancement.a().a(advancement7).a((IMaterial) Items.SHIELD, new ChatMessage("advancements.story.deflect_arrow.title"), new ChatMessage("advancements.story.deflect_arrow.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("deflected_projectile", (CriterionInstance) CriterionTriggerEntityHurtPlayer.a.a(CriterionConditionDamage.a.a().a(CriterionConditionDamageSource.a.a().a(true)).a(true))).a(consumer, "story/deflect_arrow");
        Advancement.SerializedAdvancement.a().a(advancement5).a((IMaterial) Items.DIAMOND_CHESTPLATE, new ChatMessage("advancements.story.shiny_gear.title"), new ChatMessage("advancements.story.shiny_gear.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a(AdvancementRequirements.OR).a("diamond_helmet", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_HELMET)).a("diamond_chestplate", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_CHESTPLATE)).a("diamond_leggings", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_LEGGINGS)).a("diamond_boots", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DIAMOND_BOOTS)).a(consumer, "story/shiny_gear");
        Advancement advancement9 = Advancement.SerializedAdvancement.a().a(advancement8).a((IMaterial) Items.FLINT_AND_STEEL, new ChatMessage("advancements.story.enter_the_nether.title"), new ChatMessage("advancements.story.enter_the_nether.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("entered_nether", (CriterionInstance) CriterionTriggerChangedDimension.a.a(World.THE_NETHER)).a(consumer, "story/enter_the_nether");

        Advancement.SerializedAdvancement.a().a(advancement9).a((IMaterial) Items.GOLDEN_APPLE, new ChatMessage("advancements.story.cure_zombie_villager.title"), new ChatMessage("advancements.story.cure_zombie_villager.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("cured_zombie", (CriterionInstance) CriterionTriggerCuredZombieVillager.a.c()).a(consumer, "story/cure_zombie_villager");
        Advancement advancement10 = Advancement.SerializedAdvancement.a().a(advancement9).a((IMaterial) Items.ENDER_EYE, new ChatMessage("advancements.story.follow_ender_eye.title"), new ChatMessage("advancements.story.follow_ender_eye.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("in_stronghold", (CriterionInstance) CriterionTriggerLocation.a.a(CriterionConditionLocation.a(StructureGenerator.STRONGHOLD))).a(consumer, "story/follow_ender_eye");

        Advancement.SerializedAdvancement.a().a(advancement10).a((IMaterial) Blocks.END_STONE, new ChatMessage("advancements.story.enter_the_end.title"), new ChatMessage("advancements.story.enter_the_end.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("entered_end", (CriterionInstance) CriterionTriggerChangedDimension.a.a(World.THE_END)).a(consumer, "story/enter_the_end");
    }
}
