package net.minecraft.server;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.SystemUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionEnchant;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetTag;

public class ig implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {
	public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
		biconsumer.accept(LootTables.ay, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BOOK).a(5).b((new LootItemFunctionEnchant.a()).a(Enchantments.SOUL_SPEED))).a(LootItem.a(Items.IRON_BOOTS).a(8).b((new LootItemFunctionEnchant.a()).a(Enchantments.SOUL_SPEED))).a(LootItem.a(Items.POTION).a(8).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), (nbttagcompound) -> {
			nbttagcompound.setString("Potion", "minecraft:fire_resistance");
		})))).a(LootItem.a(Items.SPLASH_POTION).a(8).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), (nbttagcompound) -> {
			nbttagcompound.setString("Potion", "minecraft:fire_resistance");
		})))).a(LootItem.a(Items.POTION).a(10).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), (nbttagcompound) -> {
			nbttagcompound.setString("Potion", "minecraft:water");
		})))).a(LootItem.a(Items.IRON_NUGGET).a(10).b(LootItemFunctionSetCount.a(LootValueBounds.a(10.0F, 36.0F)))).a(LootItem.a(Items.ENDER_PEARL).a(10).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 4.0F)))).a(LootItem.a(Items.STRING).a(20).b(LootItemFunctionSetCount.a(LootValueBounds.a(3.0F, 9.0F)))).a(LootItem.a(Items.QUARTZ).a(20).b(LootItemFunctionSetCount.a(LootValueBounds.a(5.0F, 12.0F)))).a(LootItem.a(Items.co).a(40)).a(LootItem.a(Items.rA).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F)))).a(LootItem.a(Items.FIRE_CHARGE).a(40)).a(LootItem.a(Items.LEATHER).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 4.0F)))).a(LootItem.a(Items.dl).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 8.0F)))).a(LootItem.a(Items.NETHER_BRICK).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 8.0F)))).a(LootItem.a(Items.SPECTRAL_ARROW).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(6.0F, 12.0F)))).a(LootItem.a(Items.G).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(8.0F, 16.0F)))).a(LootItem.a(Items.rB).a(40).b(LootItemFunctionSetCount.a(LootValueBounds.a(8.0F, 16.0F))))));
	}
}
