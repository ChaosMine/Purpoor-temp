package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import java.util.function.Consumer;

import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootSelectorEmpty extends LootSelectorEntry {
	private LootSelectorEmpty(int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
		super(i, j, alootitemcondition, alootitemfunction);
	}

	public static LootSelectorEntry.a<?> b() {
		return a(LootSelectorEmpty::new);
	}

	@Override
	public LootEntryType a() {
		return LootEntries.a;
	}

	@Override
	public void a(Consumer<ItemStack> consumer, LootTableInfo loottableinfo) {
	}

	public static class a extends LootSelectorEntry.e<LootSelectorEmpty> {
		@Override
		public void serializeType(JsonObject jsonobject, LootSelectorEmpty t0, JsonSerializationContext jsonserializationcontext) {
			throw new UnsupportedOperationException("What the fuck LootSelectorEmpty#L35");
		}

		@Override
		public LootSelectorEmpty b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
			return new LootSelectorEmpty(i, j, alootitemcondition, alootitemfunction);
		}
	}
}
