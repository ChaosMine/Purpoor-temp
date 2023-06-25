package net.minecraft.server;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.SystemUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.storage.loot.LootSelector;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.LootValue;
import net.minecraft.world.level.storage.loot.LootValueBounds;
import net.minecraft.world.level.storage.loot.LootValueConstant;
import net.minecraft.world.level.storage.loot.entries.LootEntryAbstract;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetTag;

public class id implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {

    public id() {}

    public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
        biconsumer.accept(LootTables.ak, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.RABBIT_HIDE).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.RABBIT_FOOT).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CHICKEN).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.FEATHER).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.ROTTEN_FLESH).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STRING).a(10)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.PHANTOM_MEMBRANE).a(2))));
        biconsumer.accept(LootTables.al, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CHAINMAIL_HELMET)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CHAINMAIL_CHESTPLATE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CHAINMAIL_LEGGINGS)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CHAINMAIL_BOOTS))));
        biconsumer.accept(LootTables.am, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKED_RABBIT)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKED_CHICKEN)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKED_PORKCHOP)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKED_BEEF)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKED_MUTTON))));
        biconsumer.accept(LootTables.an, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.MAP)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.PAPER))));
        biconsumer.accept(LootTables.ao, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.REDSTONE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.LAPIS_LAZULI))));
        biconsumer.accept(LootTables.ap, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BREAD)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.PUMPKIN_PIE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COOKIE))));
        biconsumer.accept(LootTables.aq, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.COD)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.SALMON))));
        biconsumer.accept(LootTables.ar, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.ARROW).a(26)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:swiftness");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:slowness");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:strength");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:healing");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:harming");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:leaping");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:regeneration");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:fire_resistance");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:water_breathing");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:invisibility");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:night_vision");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:weakness");
        })))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.TIPPED_ARROW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 1.0F))).b((LootItemFunction.a) LootItemFunctionSetTag.a((NBTTagCompound) SystemUtils.a((Object) (new NBTTagCompound()), (nbttagcompound) -> {
            nbttagcompound.setString("Potion", "minecraft:poison");
        }))))));
        biconsumer.accept(LootTables.as, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.LEATHER))));
        biconsumer.accept(LootTables.at, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BOOK))));
        biconsumer.accept(LootTables.au, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.cY))));
        biconsumer.accept(LootTables.av, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aR)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aS)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aT)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aU)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aV)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aW)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aX)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aY)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.aZ)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.ba)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.bb)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.bc)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.bd)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.be)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.bf)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.bg))));
        biconsumer.accept(LootTables.aw, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STONE_PICKAXE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STONE_AXE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STONE_HOE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STONE_SHOVEL))));
        biconsumer.accept(LootTables.ax, LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.STONE_AXE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.GOLDEN_AXE)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.IRON_AXE))));
    }
}
