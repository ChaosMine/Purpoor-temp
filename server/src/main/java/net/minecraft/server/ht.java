package net.minecraft.server;

import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionConditionDistance;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.advancements.critereon.CriterionTriggerChangedDimension;
import net.minecraft.advancements.critereon.CriterionTriggerEnterBlock;
import net.minecraft.advancements.critereon.CriterionTriggerInventoryChanged;
import net.minecraft.advancements.critereon.CriterionTriggerKilled;
import net.minecraft.advancements.critereon.CriterionTriggerLevitation;
import net.minecraft.advancements.critereon.CriterionTriggerLocation;
import net.minecraft.advancements.critereon.CriterionTriggerSummonedEntity;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;

public class ht implements Consumer<Consumer<Advancement>> {

    public ht() {}

    public void accept(Consumer<Advancement> consumer) {
        Advancement advancement = Advancement.SerializedAdvancement.a().a((IMaterial) Blocks.END_STONE, new ChatMessage("advancements.end.root.title"), new ChatMessage("advancements.end.root.description"), new MinecraftKey("textures/gui/advancements/backgrounds/end.png"), AdvancementFrameType.TASK, false, false, false).a("entered_end", (CriterionInstance) CriterionTriggerChangedDimension.a.a(World.THE_END)).a(consumer, "end/root");
        Advancement advancement1 = Advancement.SerializedAdvancement.a().a(advancement).a((IMaterial) Blocks.DRAGON_HEAD, new ChatMessage("advancements.end.kill_dragon.title"), new ChatMessage("advancements.end.kill_dragon.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("killed_dragon", (CriterionInstance) CriterionTriggerKilled.a.a(CriterionConditionEntity.a.a().a(EntityTypes.ENDER_DRAGON))).a(consumer, "end/kill_dragon");
        Advancement advancement2 = Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Items.ENDER_PEARL, new ChatMessage("advancements.end.enter_end_gateway.title"), new ChatMessage("advancements.end.enter_end_gateway.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("entered_end_gateway", (CriterionInstance) CriterionTriggerEnterBlock.a.a(Blocks.END_GATEWAY)).a(consumer, "end/enter_end_gateway");

        Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Items.END_CRYSTAL, new ChatMessage("advancements.end.respawn_dragon.title"), new ChatMessage("advancements.end.respawn_dragon.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("summoned_dragon", (CriterionInstance) CriterionTriggerSummonedEntity.a.a(CriterionConditionEntity.a.a().a(EntityTypes.ENDER_DRAGON))).a(consumer, "end/respawn_dragon");
        Advancement advancement3 = Advancement.SerializedAdvancement.a().a(advancement2).a((IMaterial) Blocks.PURPUR_BLOCK, new ChatMessage("advancements.end.find_end_city.title"), new ChatMessage("advancements.end.find_end_city.description"), (MinecraftKey) null, AdvancementFrameType.TASK, true, true, false).a("in_city", (CriterionInstance) CriterionTriggerLocation.a.a(CriterionConditionLocation.a(StructureGenerator.ENDCITY))).a(consumer, "end/find_end_city");

        Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Items.DRAGON_BREATH, new ChatMessage("advancements.end.dragon_breath.title"), new ChatMessage("advancements.end.dragon_breath.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("dragon_breath", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.DRAGON_BREATH)).a(consumer, "end/dragon_breath");
        Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.SHULKER_SHELL, new ChatMessage("advancements.end.levitate.title"), new ChatMessage("advancements.end.levitate.description"), (MinecraftKey) null, AdvancementFrameType.CHALLENGE, true, true, false).a(AdvancementRewards.a.a(50)).a("levitated", (CriterionInstance) CriterionTriggerLevitation.a.a(CriterionConditionDistance.b(CriterionConditionValue.FloatRange.b(50.0F)))).a(consumer, "end/levitate");
        Advancement.SerializedAdvancement.a().a(advancement3).a((IMaterial) Items.ELYTRA, new ChatMessage("advancements.end.elytra.title"), new ChatMessage("advancements.end.elytra.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("elytra", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Items.ELYTRA)).a(consumer, "end/elytra");
        Advancement.SerializedAdvancement.a().a(advancement1).a((IMaterial) Blocks.DRAGON_EGG, new ChatMessage("advancements.end.dragon_egg.title"), new ChatMessage("advancements.end.dragon_egg.description"), (MinecraftKey) null, AdvancementFrameType.GOAL, true, true, false).a("dragon_egg", (CriterionInstance) CriterionTriggerInventoryChanged.a.a(Blocks.DRAGON_EGG)).a(consumer, "end/dragon_egg");
    }
}
