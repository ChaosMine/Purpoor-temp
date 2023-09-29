package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.storage.loot.JsonRegistry;
import net.minecraft.world.level.storage.loot.LootSerializer;

import java.util.function.Predicate;

public class LootItemConditions {
	public static final LootItemConditionType a = a("inverted", new LootItemConditionInverted.a());
	public static final LootItemConditionType b = a("alternative", new LootItemConditionAlternative.b());
	public static final LootItemConditionType c = a("random_chance", new LootItemConditionRandomChance.a());
	public static final LootItemConditionType d = a("random_chance_with_looting", new LootItemConditionRandomChanceWithLooting.a());
	public static final LootItemConditionType e = a("entity_properties", new LootItemConditionEntityProperty.a());
	public static final LootItemConditionType f = a("killed_by_player", new LootItemConditionKilledByPlayer.a());
	public static final LootItemConditionType g = a("entity_scores", new LootItemConditionEntityScore.b());
	public static final LootItemConditionType h = a("block_state_property", new LootItemConditionBlockStateProperty.b());
	public static final LootItemConditionType i = a("match_tool", new LootItemConditionMatchTool.a());
	public static final LootItemConditionType j = a("table_bonus", new LootItemConditionTableBonus.a());
	public static final LootItemConditionType k = a("survives_explosion", new LootItemConditionSurvivesExplosion.a());
	public static final LootItemConditionType l = a("damage_source_properties", new LootItemConditionDamageSourceProperties.a());
	public static final LootItemConditionType m = a("location_check", new LootItemConditionLocationCheck.a());
	public static final LootItemConditionType n = a("weather_check", new LootItemConditionWeatherCheck.b());
	public static final LootItemConditionType o = a("reference", new LootItemConditionReference.a());
	public static final LootItemConditionType p = a("time_check", new LootItemConditionTimeCheck.b());

	private static LootItemConditionType a(String s, LootSerializer<? extends LootItemCondition> lootserializer) {
		return IRegistry.a(IRegistry.aq, new MinecraftKey(s), new LootItemConditionType(lootserializer));
	}

	public static Object a() {
		return JsonRegistry.a(IRegistry.aq, "condition", "condition", LootItemCondition::b).a();
	}

	public static <T> Predicate<T> a(Predicate<T>[] apredicate) {
		return switch (apredicate.length) {
			case 0 -> object -> true;
			case 1 -> apredicate[0];
			case 2 -> apredicate[0].and(apredicate[1]);
			default -> object -> {
				int i = apredicate.length;

				for (int j = 0; j < i; ++j) {
					Predicate<T> predicate = apredicate[j];

					if (!predicate.test(object)) {
						return false;
					}
				}

				return true;
			};
		};
	}

	public static <T> Predicate<T> b(Predicate<T>[] apredicate) {
		return switch (apredicate.length) {
			case 0 -> object -> false;
			case 1 -> apredicate[0];
			case 2 -> apredicate[0].or(apredicate[1]);
			default -> object -> {
				int i = apredicate.length;

				for (int j = 0; j < i; ++j) {
					Predicate<T> predicate = apredicate[j];

					if (predicate.test(object)) {
						return true;
					}
				}

				return false;
			};
		};
	}
}
