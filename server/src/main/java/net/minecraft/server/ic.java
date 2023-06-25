package net.minecraft.server;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.SystemUtils;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionInOpenWater;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootSelector;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.LootValue;
import net.minecraft.world.level.storage.loot.LootValueBounds;
import net.minecraft.world.level.storage.loot.LootValueConstant;
import net.minecraft.world.level.storage.loot.entries.LootEntryAbstract;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootSelectorEntry;
import net.minecraft.world.level.storage.loot.entries.LootSelectorLootTable;
import net.minecraft.world.level.storage.loot.functions.LootEnchantLevel;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetDamage;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetTag;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionEntityProperty;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionLocationCheck;

public class ic implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {

    public static final LootItemCondition.a a = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.JUNGLE));
    public static final LootItemCondition.a b = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.JUNGLE_HILLS));
    public static final LootItemCondition.a c = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.JUNGLE_EDGE));
    public static final LootItemCondition.a d = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.BAMBOO_JUNGLE));
    public static final LootItemCondition.a e = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.MODIFIED_JUNGLE));
    public static final LootItemCondition.a f = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.MODIFIED_JUNGLE_EDGE));
    public static final LootItemCondition.a g = LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(Biomes.BAMBOO_JUNGLE_HILLS));

    public ic() {}

    public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
        biconsumer.accept(LootTables.ag, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootSelectorLootTable.a(LootTables.ah).a(10).b(-2)).a(LootSelectorLootTable.a(LootTables.ai).a(5).b(2).b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, CriterionConditionEntity.a.a().a(CriterionConditionInOpenWater.a(true))))).a((LootEntryAbstract.a) LootSelectorLootTable.a(LootTables.aj).a(85).b(-1))));
        biconsumer.accept(LootTables.aj, LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COD).a(60)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.SALMON).a(25)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TROPICAL_FISH).a(2)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.PUFFERFISH).a(13))));
        biconsumer.accept(LootTables.ah, LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Blocks.LILY_PAD).a(17)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.LEATHER_BOOTS).a(10).b((LootItemFunction.a) LootItemFunctionSetDamage.a(LootValueBounds.a(0.0F, 0.9F)))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.LEATHER).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BONE).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.POTION).a(10).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:water");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STRING).a(5)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.FISHING_ROD).a(2).b((LootItemFunction.a) LootItemFunctionSetDamage.a(LootValueBounds.a(0.0F, 0.9F)))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BOWL).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STICK).a(5)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.INK_SAC).a(1).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(10)))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Blocks.TRIPWIRE_HOOK).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.ROTTEN_FLESH).a(10)).a((LootEntryAbstract.a) ((LootSelectorEntry.a) LootItem.a((IMaterial) Blocks.BAMBOO).b((LootItemCondition.a) ic.a.a(ic.b).a(ic.c).a(ic.d).a(ic.e).a(ic.f).a(ic.g))).a(10))));
        biconsumer.accept(LootTables.ai, LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.NAME_TAG)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.SADDLE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BOW).b((LootItemFunction.a) LootItemFunctionSetDamage.a(LootValueBounds.a(0.0F, 0.25F))).b((LootItemFunction.a) LootEnchantLevel.a((LootValue) LootValueConstant.a(30)).e())).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.FISHING_ROD).b((LootItemFunction.a) LootItemFunctionSetDamage.a(LootValueBounds.a(0.0F, 0.25F))).b((LootItemFunction.a) LootEnchantLevel.a((LootValue) LootValueConstant.a(30)).e())).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BOOK).b((LootItemFunction.a) LootEnchantLevel.a((LootValue) LootValueConstant.a(30)).e())).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.NAUTILUS_SHELL))));
    }
}
