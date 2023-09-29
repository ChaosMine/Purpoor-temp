package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootCollector;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class LootSelectorLootTable extends LootSelectorEntry {
	private final MinecraftKey g;

	private LootSelectorLootTable(MinecraftKey minecraftkey, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
		super(i, j, alootitemcondition, alootitemfunction);
		this.g = minecraftkey;
	}

	public static LootSelectorEntry.a<?> a(MinecraftKey minecraftkey) {
		return a((i, j, alootitemcondition, alootitemfunction) -> {
			return new LootSelectorLootTable(minecraftkey, i, j, alootitemcondition, alootitemfunction);
		});
	}

	@Override
	public LootEntryType a() {
		return LootEntries.c;
	}

	@Override
	public void a(Consumer<ItemStack> consumer, LootTableInfo loottableinfo) {
		LootTable loottable = loottableinfo.a(this.g);

		loottable.populateLootDirect(loottableinfo, consumer);
	}

	@Override
	public void a(LootCollector lootcollector) {
		if (lootcollector.a(this.g)) {
			lootcollector.a("Table " + this.g + " is recursively called");
		} else {
			super.a(lootcollector);
			LootTable loottable = lootcollector.c(this.g);

			if (loottable == null) {
				lootcollector.a("Unknown loot table called " + this.g);
			} else {
				loottable.a(lootcollector.a("->{" + this.g + "}", this.g));
			}
		}
	}

	public static class a extends LootSelectorEntry.e<LootSelectorLootTable> {
		public a() {
		}

		public void a(JsonObject jsonobject, LootSelectorLootTable lootselectorloottable, JsonSerializationContext jsonserializationcontext) {
			super.a(jsonobject, lootselectorloottable, jsonserializationcontext);
			jsonobject.addProperty("name", lootselectorloottable.g.toString());
		}

		@Override
		public void serializeType(JsonObject jsonobject, LootSelectorLootTable t0, JsonSerializationContext jsonserializationcontext) {
			throw new UnsupportedOperationException("what the fuck LootSelectorLootTable#L70");
		}

		@Override
		protected LootSelectorLootTable b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
			MinecraftKey minecraftkey = new MinecraftKey(ChatDeserializer.h(jsonobject, "name"));

			return new LootSelectorLootTable(minecraftkey, i, j, alootitemcondition, alootitemfunction);
		}
	}
}
