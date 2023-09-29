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
	public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
		biconsumer.accept(LootTables.ak, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.RABBIT_HIDE).a(10)).a(LootItem.a(Items.RABBIT_FOOT).a(10)).a(LootItem.a(Items.CHICKEN).a(10)).a(LootItem.a(Items.FEATHER).a(10)).a(LootItem.a(Items.ROTTEN_FLESH).a(10)).a(LootItem.a(Items.STRING).a(10)).a(LootItem.a(Items.PHANTOM_MEMBRANE).a(2))));
		biconsumer.accept(LootTables.al, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.CHAINMAIL_HELMET)).a(LootItem.a(Items.CHAINMAIL_CHESTPLATE)).a(LootItem.a(Items.CHAINMAIL_LEGGINGS)).a(LootItem.a(Items.CHAINMAIL_BOOTS))));
		biconsumer.accept(LootTables.am, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COOKED_RABBIT)).a(LootItem.a(Items.COOKED_CHICKEN)).a(LootItem.a(Items.COOKED_PORKCHOP)).a(LootItem.a(Items.COOKED_BEEF)).a(LootItem.a(Items.COOKED_MUTTON))));
		biconsumer.accept(LootTables.an, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.MAP)).a(LootItem.a(Items.PAPER))));
		biconsumer.accept(LootTables.ao, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.REDSTONE)).a(LootItem.a(Items.LAPIS_LAZULI))));
		biconsumer.accept(LootTables.ap, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BREAD)).a(LootItem.a(Items.PUMPKIN_PIE)).a(LootItem.a(Items.COOKIE))));
		biconsumer.accept(LootTables.aq, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD)).a(LootItem.a(Items.SALMON))));
		biconsumer.accept(LootTables.ar, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ARROW).a(26)).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:swiftness");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:slowness");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:strength");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:healing");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:harming");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:leaping");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:regeneration");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:fire_resistance");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:water_breathing");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:invisibility");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:night_vision");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:weakness");
		})))).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), nbt -> {
			nbt.setString("Potion", "minecraft:poison");
		}))))));
		biconsumer.accept(LootTables.as, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER))));
		biconsumer.accept(LootTables.at, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BOOK))));
		biconsumer.accept(LootTables.au, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.cY))));
		biconsumer.accept(LootTables.av, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.aR)).a(LootItem.a(Items.aS)).a(LootItem.a(Items.aT)).a(LootItem.a(Items.aU)).a(LootItem.a(Items.aV)).a(LootItem.a(Items.aW)).a(LootItem.a(Items.aX)).a(LootItem.a(Items.aY)).a(LootItem.a(Items.aZ)).a(LootItem.a(Items.ba)).a(LootItem.a(Items.bb)).a(LootItem.a(Items.bc)).a(LootItem.a(Items.bd)).a(LootItem.a(Items.be)).a(LootItem.a(Items.bf)).a(LootItem.a(Items.bg))));
		biconsumer.accept(LootTables.aw, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STONE_PICKAXE)).a(LootItem.a(Items.STONE_AXE)).a(LootItem.a(Items.STONE_HOE)).a(LootItem.a(Items.STONE_SHOVEL))));
		biconsumer.accept(LootTables.ax, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STONE_AXE)).a(LootItem.a(Items.GOLDEN_AXE)).a(LootItem.a(Items.IRON_AXE))));
	}
}
