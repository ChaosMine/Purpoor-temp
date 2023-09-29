package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;

import java.util.Map;

public class DataConverterAttributes extends DataFix {
	private static final Map<String, String> a = ImmutableMap.<String, String>builder()
		.put("generic.maxHealth", "generic.max_health")
		.put("Max Health", "generic.max_health")
		.put("zombie.spawnReinforcements", "zombie.spawn_reinforcements")
		.put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements")
		.put("horse.jumpStrength", "horse.jump_strength")
		.put("Jump Strength", "horse.jump_strength")
		.put("generic.followRange", "generic.follow_range")
		.put("Follow Range", "generic.follow_range")
		.put("generic.knockbackResistance", "generic.knockback_resistance")
		.put("Knockback Resistance", "generic.knockback_resistance")
		.put("generic.movementSpeed", "generic.movement_speed")
		.put("Movement Speed", "generic.movement_speed")
		.put("generic.flyingSpeed", "generic.flying_speed")
		.put("Flying Speed", "generic.flying_speed")
		.put("generic.attackDamage", "generic.attack_damage")
		.put("generic.attackKnockback", "generic.attack_knockback")
		.put("generic.attackSpeed", "generic.attack_speed")
		.put("generic.armorToughness", "generic.armor_toughness").build();

	public DataConverterAttributes(Schema schema) {
		super(schema, false);
	}

	private static Dynamic<?> a(Dynamic<?> dynamic) {
		var optional = dynamic.asString().result().map((s) -> DataConverterAttributes.a.getOrDefault(s, s));
		return DataFixUtils.orElse(optional.map(dynamic::createString), dynamic);
	}

	private static Typed<?> a(Typed<?> typed) {
		return typed.update(DSL.remainderFinder(), (dynamic) -> dynamic.update("AttributeModifiers", (dynamic1) -> {
			var optional = dynamic1.asStreamOpt().result().map((stream) -> stream.map((dynamic2) -> dynamic2.update("AttributeName", DataConverterAttributes::a)));
			return DataFixUtils.orElse(optional.map(dynamic1::createList), dynamic1);
		}));
	}

	private static Typed<?> b(Typed<?> typed) {
		return typed.update(DSL.remainderFinder(), (dynamic) -> dynamic.update("Attributes", (dynamic1) -> {
			var optional = dynamic1.asStreamOpt().result().map((stream) -> stream.map((dynamic2) -> dynamic2.update("Name", DataConverterAttributes::a)));
			return DataFixUtils.orElse(optional.map(dynamic1::createList), dynamic1);
		}));
	}

	protected TypeRewriteRule makeRule() {
		Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
		OpticFinder<?> opticfinder = type.findField("tag");
		return TypeRewriteRule.seq(this.fixTypeEverywhereTyped("Rename ItemStack Attributes", type, (typed) -> typed.updateTyped(opticfinder, DataConverterAttributes::a)), this.fixTypeEverywhereTyped("Rename Entity Attributes", this.getInputSchema().getType(DataConverterTypes.ENTITY), DataConverterAttributes::b), this.fixTypeEverywhereTyped("Rename Player Attributes", this.getInputSchema().getType(DataConverterTypes.PLAYER), DataConverterAttributes::b));
	}
}
