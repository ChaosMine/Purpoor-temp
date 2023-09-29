package net.minecraft.world.level.storage.loot.functions;

import java.util.function.BiFunction;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.JsonRegistry;
import net.minecraft.world.level.storage.loot.LootSerializer;
import net.minecraft.world.level.storage.loot.LootTableInfo;

public class LootItemFunctions {
	public static final BiFunction<ItemStack, LootTableInfo, ItemStack> a = (itemstack, loottableinfo) -> {
		return itemstack;
	};
	public static final LootItemFunctionType b = a("set_count", new LootItemFunctionSetCount.a());
	public static final LootItemFunctionType c = a("enchant_with_levels", new LootEnchantLevel.b());
	public static final LootItemFunctionType d = a("enchant_randomly", new LootItemFunctionEnchant.b());
	public static final LootItemFunctionType e = a("set_nbt", new LootItemFunctionSetTag.a());
	public static final LootItemFunctionType f = a("furnace_smelt", new LootItemFunctionSmelt.a());
	public static final LootItemFunctionType g = a("looting_enchant", new LootEnchantFunction.b());
	public static final LootItemFunctionType h = a("set_damage", new LootItemFunctionSetDamage.a());
	public static final LootItemFunctionType i = a("set_attributes", new LootItemFunctionSetAttribute.d());
	public static final LootItemFunctionType j = a("set_name", new LootItemFunctionSetName.a());
	public static final LootItemFunctionType k = a("exploration_map", new LootItemFunctionExplorationMap.b());
	public static final LootItemFunctionType l = a("set_stew_effect", new LootItemFunctionSetStewEffect.b());
	public static final LootItemFunctionType m = a("copy_name", new LootItemFunctionCopyName.b());
	public static final LootItemFunctionType n = a("set_contents", new LootItemFunctionSetContents.b());
	public static final LootItemFunctionType o = a("limit_count", new LootItemFunctionLimitCount.a());
	public static final LootItemFunctionType p = a("apply_bonus", new LootItemFunctionApplyBonus.e_());
	public static final LootItemFunctionType q = a("set_loot_table", new LootItemFunctionSetTable.a());
	public static final LootItemFunctionType r = a("explosion_decay", new LootItemFunctionExplosionDecay.a());
	public static final LootItemFunctionType s = a("set_lore", new LootItemFunctionSetLore.b());
	public static final LootItemFunctionType t = a("fill_player_head", new LootItemFunctionFillPlayerHead.a());
	public static final LootItemFunctionType u = a("copy_nbt", new LootItemFunctionCopyNBT.e());
	public static final LootItemFunctionType v = a("copy_state", new LootItemFunctionCopyState.b());

	private static LootItemFunctionType a(String s, LootSerializer<? extends LootItemFunction> lootserializer) {
		return IRegistry.a(IRegistry.ap, new MinecraftKey(s), new LootItemFunctionType(lootserializer));
	}

	public static Object a() {
		return JsonRegistry.a(IRegistry.ap, "function", "function", LootItemFunction::b).a();
	}

	public static BiFunction<ItemStack, LootTableInfo, ItemStack> a(BiFunction<ItemStack, LootTableInfo, ItemStack>[] abifunction) {
		switch (abifunction.length) {
			case 0 -> {
				return LootItemFunctions.a;
			}
			case 1 -> {
				return abifunction[0];
			}
			case 2 -> {
				BiFunction<ItemStack, LootTableInfo, ItemStack> bifunction = abifunction[0];
				BiFunction<ItemStack, LootTableInfo, ItemStack> bifunction1 = abifunction[1];
				return (itemstack, loottableinfo) -> {
					return (ItemStack) bifunction1.apply(bifunction.apply(itemstack, loottableinfo), loottableinfo);
				};
			}
			default -> {
				return (itemstack, loottableinfo) -> {
					int i = abifunction.length;

					for (int j = 0; j < i; ++j) {
						BiFunction<ItemStack, LootTableInfo, ItemStack> bifunction2 = abifunction[j];

						itemstack = bifunction2.apply(itemstack, loottableinfo);
					}

					return itemstack;
				};
			}
		}
	}
}
