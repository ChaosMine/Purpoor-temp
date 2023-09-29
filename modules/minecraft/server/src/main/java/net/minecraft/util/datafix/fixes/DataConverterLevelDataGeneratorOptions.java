package net.minecraft.util.datafix.fixes;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.SystemUtils;
import net.minecraft.util.ChatDeserializer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DataConverterLevelDataGeneratorOptions extends DataFix {
	static final Map<String, String> a = SystemUtils.a(new HashMap<>(), map -> {
		map.put("0", "minecraft:ocean");
		map.put("1", "minecraft:plains");
		map.put("2", "minecraft:desert");
		map.put("3", "minecraft:mountains");
		map.put("4", "minecraft:forest");
		map.put("5", "minecraft:taiga");
		map.put("6", "minecraft:swamp");
		map.put("7", "minecraft:river");
		map.put("8", "minecraft:nether");
		map.put("9", "minecraft:the_end");
		map.put("10", "minecraft:frozen_ocean");
		map.put("11", "minecraft:frozen_river");
		map.put("12", "minecraft:snowy_tundra");
		map.put("13", "minecraft:snowy_mountains");
		map.put("14", "minecraft:mushroom_fields");
		map.put("15", "minecraft:mushroom_field_shore");
		map.put("16", "minecraft:beach");
		map.put("17", "minecraft:desert_hills");
		map.put("18", "minecraft:wooded_hills");
		map.put("19", "minecraft:taiga_hills");
		map.put("20", "minecraft:mountain_edge");
		map.put("21", "minecraft:jungle");
		map.put("22", "minecraft:jungle_hills");
		map.put("23", "minecraft:jungle_edge");
		map.put("24", "minecraft:deep_ocean");
		map.put("25", "minecraft:stone_shore");
		map.put("26", "minecraft:snowy_beach");
		map.put("27", "minecraft:birch_forest");
		map.put("28", "minecraft:birch_forest_hills");
		map.put("29", "minecraft:dark_forest");
		map.put("30", "minecraft:snowy_taiga");
		map.put("31", "minecraft:snowy_taiga_hills");
		map.put("32", "minecraft:giant_tree_taiga");
		map.put("33", "minecraft:giant_tree_taiga_hills");
		map.put("34", "minecraft:wooded_mountains");
		map.put("35", "minecraft:savanna");
		map.put("36", "minecraft:savanna_plateau");
		map.put("37", "minecraft:badlands");
		map.put("38", "minecraft:wooded_badlands_plateau");
		map.put("39", "minecraft:badlands_plateau");
		map.put("40", "minecraft:small_end_islands");
		map.put("41", "minecraft:end_midlands");
		map.put("42", "minecraft:end_highlands");
		map.put("43", "minecraft:end_barrens");
		map.put("44", "minecraft:warm_ocean");
		map.put("45", "minecraft:lukewarm_ocean");
		map.put("46", "minecraft:cold_ocean");
		map.put("47", "minecraft:deep_warm_ocean");
		map.put("48", "minecraft:deep_lukewarm_ocean");
		map.put("49", "minecraft:deep_cold_ocean");
		map.put("50", "minecraft:deep_frozen_ocean");
		map.put("127", "minecraft:the_void");
		map.put("129", "minecraft:sunflower_plains");
		map.put("130", "minecraft:desert_lakes");
		map.put("131", "minecraft:gravelly_mountains");
		map.put("132", "minecraft:flower_forest");
		map.put("133", "minecraft:taiga_mountains");
		map.put("134", "minecraft:swamp_hills");
		map.put("140", "minecraft:ice_spikes");
		map.put("149", "minecraft:modified_jungle");
		map.put("151", "minecraft:modified_jungle_edge");
		map.put("155", "minecraft:tall_birch_forest");
		map.put("156", "minecraft:tall_birch_hills");
		map.put("157", "minecraft:dark_forest_hills");
		map.put("158", "minecraft:snowy_taiga_mountains");
		map.put("160", "minecraft:giant_spruce_taiga");
		map.put("161", "minecraft:giant_spruce_taiga_hills");
		map.put("162", "minecraft:modified_gravelly_mountains");
		map.put("163", "minecraft:shattered_savanna");
		map.put("164", "minecraft:shattered_savanna_plateau");
		map.put("165", "minecraft:eroded_badlands");
		map.put("166", "minecraft:modified_wooded_badlands_plateau");
		map.put("167", "minecraft:modified_badlands_plateau");
	});

	public DataConverterLevelDataGeneratorOptions(Schema schema, boolean flag) {
		super(schema, flag);
	}

	private static <T> Dynamic<T> a(String s, DynamicOps<T> dynamicops) {
		Iterator<String> iterator = Splitter.on(';').split(s).iterator();
		String s1 = "minecraft:plains";
		Map<String, Map<String, String>> map = Maps.newHashMap();
		List<Pair<Integer, String>> list;

		if (!s.isEmpty() && iterator.hasNext()) {
			list = b(iterator.next());
			if (!list.isEmpty()) {
				if (iterator.hasNext()) {
					s1 = DataConverterLevelDataGeneratorOptions.a.getOrDefault(iterator.next(), "minecraft:plains");
				}

				if (iterator.hasNext()) {
					String[] astring = iterator.next().toLowerCase(Locale.ROOT).split(",");
					int i = astring.length;

					for (int j = 0; j < i; ++j) {
						String s2 = astring[j];
						String[] astring2 = s2.split("\\(", 2);

						if (!astring2[0].isEmpty()) {
							map.put(astring2[0], Maps.newHashMap());
							if (astring2.length > 1 && astring2[1].endsWith(")") && astring2[1].length() > 1) {
								String[] astring3 = astring2[1].substring(0, astring2[1].length() - 1).split(" ");
								int k = astring3.length;

								for (int l = 0; l < k; ++l) {
									String s3 = astring3[l];
									String[] astring5 = s3.split("=", 2);

									if (astring5.length == 2) {
										map.get(astring2[0]).put(astring5[0], astring5[1]);
									}
								}
							}
						}
					}
				} else {
					map.put("village", Maps.newHashMap());
				}
			}
		} else {
			list = new ArrayList<>();
			list.add(Pair.of(1, "minecraft:bedrock"));
			list.add(Pair.of(2, "minecraft:dirt"));
			list.add(Pair.of(1, "minecraft:grass_block"));
			map.put("village", Maps.newHashMap());
		}

		T t0 = dynamicops.createList(list.stream().map(pair -> dynamicops.createMap(ImmutableMap.of(dynamicops.createString("height"), dynamicops.createInt(pair.getFirst()), dynamicops.createString("block"), dynamicops.createString(pair.getSecond())))));
		T t1 = dynamicops.createMap(map.entrySet().stream().map(entry -> Pair.of(dynamicops.createString(entry.getKey().toLowerCase(Locale.ROOT)), dynamicops.createMap(entry.getValue().entrySet().stream().map(entry1 -> Pair.of(dynamicops.createString(entry1.getKey()), dynamicops.createString(entry1.getValue()))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));

		return new Dynamic<>(dynamicops, dynamicops.createMap(ImmutableMap.of(dynamicops.createString("layers"), t0, dynamicops.createString("biome"), dynamicops.createString(s1), dynamicops.createString("structures"), t1)));
	}

	@Nullable
	private static Pair<Integer, String> a(String s) {
		String[] astring = s.split("\\*", 2);
		int i;

		if (astring.length == 2) {
			try {
				i = Integer.parseInt(astring[0]);
			} catch (NumberFormatException numberformatexception) {
				return null;
			}
		} else {
			i = 1;
		}

		String s1 = astring[astring.length - 1];

		return Pair.of(i, s1);
	}

	private static List<Pair<Integer, String>> b(String s) {
		List<Pair<Integer, String>> list = Lists.newArrayList();
		String[] astring = s.split(",");
		int i = astring.length;

		for (int j = 0; j < i; ++j) {
			String s1 = astring[j];
			Pair<Integer, String> pair = a(s1);

			if (pair == null) {
				return Collections.emptyList();
			}

			list.add(pair);
		}

		return list;
	}

	protected TypeRewriteRule makeRule() {
		Type<?> type = this.getOutputSchema().getType(DataConverterTypes.LEVEL);

		return this.fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", this.getInputSchema().getType(DataConverterTypes.LEVEL), type, typed -> typed.write().flatMap(dynamic -> {
			Optional<String> optional = dynamic.get("generatorOptions").asString().result();
			Dynamic<?> dynamic1;

			if ("flat".equalsIgnoreCase(dynamic.get("generatorName").asString(""))) {
				String s = optional.orElse("");

				dynamic1 = dynamic.set("generatorOptions", a(s, dynamic.getOps()));
			} else if ("buffet".equalsIgnoreCase(dynamic.get("generatorName").asString("")) && optional.isPresent()) {
				Dynamic<JsonElement> dynamic2 = new Dynamic<>(JsonOps.INSTANCE, ChatDeserializer.a(optional.get(), true));

				dynamic1 = dynamic.set("generatorOptions", dynamic2.convert(dynamic.getOps()));
			} else {
				dynamic1 = dynamic;
			}

			return type.readTyped(dynamic1);
		}).map(Pair::getFirst).result().orElseThrow(() -> new IllegalStateException("Could not read new level type.")));
	}
}
