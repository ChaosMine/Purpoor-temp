package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class LootSelectorDynamic extends LootSelectorEntry {
	private final MinecraftKey g;

	private LootSelectorDynamic(MinecraftKey minecraftkey, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
		super(i, j, alootitemcondition, alootitemfunction);
		this.g = minecraftkey;
	}

	public static LootSelectorEntry.a<?> a(MinecraftKey minecraftkey) {
		return a((i, j, alootitemcondition, alootitemfunction) -> {
			return new LootSelectorDynamic(minecraftkey, i, j, alootitemcondition, alootitemfunction);
		});
	}

	@Override
	public LootEntryType a() {
		return LootEntries.d;
	}

	@Override
	public void a(Consumer<ItemStack> consumer, LootTableInfo loottableinfo) {
		loottableinfo.a(this.g, consumer);
	}

	public static class a extends LootSelectorEntry.e<LootSelectorDynamic> {
		public void a(JsonObject jsonobject, LootSelectorDynamic lootselectordynamic, JsonSerializationContext jsonserializationcontext) {
			super.a(jsonobject, lootselectordynamic, jsonserializationcontext);
			jsonobject.addProperty("name", lootselectordynamic.g.toString());
		}

		@Override
		public void serializeType(JsonObject jsonobject, LootSelectorDynamic t0, JsonSerializationContext jsonserializationcontext) {
			throw new UnsupportedOperationException("what the fuck LootSelectorDynamic#L47");
		}

		@Override
		protected LootSelectorDynamic b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
			MinecraftKey minecraftkey = new MinecraftKey(ChatDeserializer.h(jsonobject, "name"));

			return new LootSelectorDynamic(minecraftkey, i, j, alootitemcondition, alootitemfunction);
		}
	}
}
