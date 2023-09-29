package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Optional;

public class DataConverterItemStackEnchantment extends DataFix {
	private static final Int2ObjectMap<String> a = DataFixUtils.make(new Int2ObjectOpenHashMap<>(), map -> {
		map.put(0, "minecraft:protection");
		map.put(1, "minecraft:fire_protection");
		map.put(2, "minecraft:feather_falling");
		map.put(3, "minecraft:blast_protection");
		map.put(4, "minecraft:projectile_protection");
		map.put(5, "minecraft:respiration");
		map.put(6, "minecraft:aqua_affinity");
		map.put(7, "minecraft:thorns");
		map.put(8, "minecraft:depth_strider");
		map.put(9, "minecraft:frost_walker");
		map.put(10, "minecraft:binding_curse");
		map.put(16, "minecraft:sharpness");
		map.put(17, "minecraft:smite");
		map.put(18, "minecraft:bane_of_arthropods");
		map.put(19, "minecraft:knockback");
		map.put(20, "minecraft:fire_aspect");
		map.put(21, "minecraft:looting");
		map.put(22, "minecraft:sweeping");
		map.put(32, "minecraft:efficiency");
		map.put(33, "minecraft:silk_touch");
		map.put(34, "minecraft:unbreaking");
		map.put(35, "minecraft:fortune");
		map.put(48, "minecraft:power");
		map.put(49, "minecraft:punch");
		map.put(50, "minecraft:flame");
		map.put(51, "minecraft:infinity");
		map.put(61, "minecraft:luck_of_the_sea");
		map.put(62, "minecraft:lure");
		map.put(65, "minecraft:loyalty");
		map.put(66, "minecraft:impaling");
		map.put(67, "minecraft:riptide");
		map.put(68, "minecraft:channeling");
		map.put(70, "minecraft:mending");
		map.put(71, "minecraft:vanishing_curse");
	});

	public DataConverterItemStackEnchantment(Schema schema, boolean flag) {
		super(schema, flag);
	}

	protected TypeRewriteRule makeRule() {
		Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
		OpticFinder<?> opticfinder = type.findField("tag");

		return this.fixTypeEverywhereTyped("ItemStackEnchantmentFix", type, (typed) -> {
			return typed.updateTyped(opticfinder, (typed1) -> {
				return typed1.update(DSL.remainderFinder(), this::a);
			});
		});
	}

	private Dynamic<?> a(Dynamic<?> dynamic) {
		var dataresult = dynamic.get("ench").asStreamOpt().map((stream) -> {
			return stream.map((dynamic1) -> {
				return dynamic1.set("id", dynamic1.createString(DataConverterItemStackEnchantment.a.getOrDefault(dynamic1.get("id").asInt(0), "null")));
			});
		});

		Optional<? extends Dynamic<?>> optional = dataresult.map(dynamic::createList).result();

		if (optional.isPresent()) {
			dynamic = dynamic.remove("ench").set("Enchantments", optional.get());
		}

		return dynamic.update("StoredEnchantments", (dynamic1) -> {
			var dataresult1 = dynamic1.asStreamOpt().map((stream) -> {
				return stream.map((dynamic2) -> {
					return dynamic2.set("id", dynamic2.createString(DataConverterItemStackEnchantment.a.getOrDefault(dynamic2.get("id").asInt(0), "null")));
				});
			});

			return DataFixUtils.orElse(dataresult1.map(dynamic1::createList).result(), dynamic1);
		});
	}
}
