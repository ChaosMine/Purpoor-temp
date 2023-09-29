package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataConverterWorldGenSettingsBuilding extends DataFix {
	private static final ImmutableMap<String, a_> a = ImmutableMap.<String, a_>builder()
		.put("minecraft:village", new a_(32, 8, 10387312))
		.put("minecraft:desert_pyramid", new a_(32, 8, 14357617))
		.put("minecraft:igloo", new a_(32, 8, 14357618))
		.put("minecraft:jungle_pyramid", new a_(32, 8, 14357619))
		.put("minecraft:swamp_hut", new a_(32, 8, 14357620))
		.put("minecraft:pillager_outpost", new a_(32, 8, 165745296))
		.put("minecraft:monument", new a_(32, 5, 10387313))
		.put("minecraft:endcity", new a_(20, 11, 10387313))
		.put("minecraft:mansion", new a_(80, 20, 10387319)).build();

	public DataConverterWorldGenSettingsBuilding(Schema schema) {
		super(schema, true);
	}

	private static <T> Dynamic<T> a(long i, DynamicLike<T> dynamiclike, Dynamic<T> dynamic, Dynamic<T> dynamic1) {
		return dynamiclike.createMap(ImmutableMap.of(dynamiclike.createString("type"), dynamiclike.createString("minecraft:noise"), dynamiclike.createString("biome_source"), dynamic1, dynamiclike.createString("seed"), dynamiclike.createLong(i), dynamiclike.createString("settings"), dynamic));
	}

	private static <T> Dynamic<T> a(Dynamic<T> dynamic, long i, boolean flag, boolean flag1) {
		var builder = ImmutableMap.<Dynamic<T>, Dynamic<T>>builder()
			.put(dynamic.createString("type"), dynamic.createString("minecraft:vanilla_layered"))
			.put(dynamic.createString("seed"), dynamic.createLong(i))
			.put(dynamic.createString("large_biomes"), dynamic.createBoolean(flag1));

		if (flag) {
			builder.put(dynamic.createString("legacy_biome_init_layer"), dynamic.createBoolean(flag));
		}

		return dynamic.createMap(builder.build());
	}

	private static <T> Dynamic<T> a(Dynamic<T> dynamic) {
		DynamicOps<T> dynamicops = dynamic.getOps();
		long i = dynamic.get("RandomSeed").asLong(0L);
		Optional<String> optional = dynamic.get("generatorName").asString().map((s) -> {
			return s.toLowerCase(Locale.ROOT);
		}).result();
		Optional<String> optional1 = dynamic.get("legacy_custom_options").asString().result().or(() -> optional.equals(Optional.of("customized")) ? dynamic.get("generatorOptions").asString().result() : Optional.empty());
		boolean flag = false;
		Dynamic dynamic1;

		if (optional.equals(Optional.of("customized"))) {
			dynamic1 = a(dynamic, i);
		} else if (optional.isEmpty()) {
			dynamic1 = a(dynamic, i);
		} else {
			String s = optional.get();
			byte b0 = -1;

			switch (s.hashCode()) {
				case -1378118590 -> {
					if (s.equals("buffet")) {
						b0 = 2;
					}
				}
				case 3145593 -> {
					if (s.equals("flat")) {
						b0 = 0;
					}
				}
				case 1045526590 -> {
					if (s.equals("debug_all_block_states")) {
						b0 = 1;
					}
				}
			}

			switch (b0) {
				case 0 -> {
					OptionalDynamic<T> optionaldynamic = dynamic.get("generatorOptions");
					Map<Dynamic<T>, Dynamic<T>> map = a(dynamicops, optionaldynamic);
					dynamic1 = dynamic.createMap(ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:flat"), dynamic.createString("settings"), dynamic.createMap(ImmutableMap.of(dynamic.createString("structures"), dynamic.createMap(map), dynamic.createString("layers"), optionaldynamic.get("layers").result().orElseGet(() -> {
						return dynamic.createList(Stream.of(dynamic.createMap(ImmutableMap.of(dynamic.createString("height"), dynamic.createInt(1), dynamic.createString("block"), dynamic.createString("minecraft:bedrock"))), dynamic.createMap(ImmutableMap.of(dynamic.createString("height"), dynamic.createInt(2), dynamic.createString("block"), dynamic.createString("minecraft:dirt"))), dynamic.createMap(ImmutableMap.of(dynamic.createString("height"), dynamic.createInt(1), dynamic.createString("block"), dynamic.createString("minecraft:grass_block")))));
					}), dynamic.createString("biome"), dynamic.createString(optionaldynamic.get("biome").asString("minecraft:plains"))))));
				}
				case 1 ->
					dynamic1 = dynamic.createMap(ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:debug")));
				case 2 -> {
					OptionalDynamic<T> optionaldynamic1 = dynamic.get("generatorOptions");
					OptionalDynamic<?> optionaldynamic2 = optionaldynamic1.get("chunk_generator");
					Optional<String> optional2 = optionaldynamic2.get("type").asString().result();
					Dynamic dynamic2;
					if (Objects.equals(optional2, Optional.of("minecraft:caves"))) {
						dynamic2 = dynamic.createString("minecraft:caves");
						flag = true;
					} else if (Objects.equals(optional2, Optional.of("minecraft:floating_islands"))) {
						dynamic2 = dynamic.createString("minecraft:floating_islands");
					} else {
						dynamic2 = dynamic.createString("minecraft:overworld");
					}
					Dynamic<T> dynamic3 = optionaldynamic1.get("biome_source").result().orElseGet(() -> {
						return dynamic.createMap(ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:fixed")));
					});
					Dynamic dynamic4;
					if (dynamic3.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
						String s1 = dynamic3.get("options").get("biomes").asStream().findFirst().flatMap((dynamic5) -> {
							return dynamic5.asString().result();
						}).orElse("minecraft:ocean");

						dynamic4 = dynamic3.remove("options").set("biome", dynamic.createString(s1));
					} else {
						dynamic4 = dynamic3;
					}
					dynamic1 = a(i, dynamic, dynamic2, dynamic4);
				}
				default -> {
					boolean flag1 = optional.get().equals("default");
					boolean flag2 = optional.get().equals("default_1_1") || flag1 && dynamic.get("generatorVersion").asInt(0) == 0;
					boolean flag3 = optional.get().equals("amplified");
					boolean flag4 = optional.get().equals("largebiomes");
					dynamic1 = a(i, dynamic, dynamic.createString(flag3 ? "minecraft:amplified" : "minecraft:overworld"), a(dynamic, i, flag2, flag4));
				}
			}
		}

		boolean flag5 = dynamic.get("MapFeatures").asBoolean(true);
		boolean flag6 = dynamic.get("BonusChest").asBoolean(false);
		Builder<T, T> builder = ImmutableMap.builder();

		builder.put(dynamicops.createString("seed"), dynamicops.createLong(i));
		builder.put(dynamicops.createString("generate_features"), dynamicops.createBoolean(flag5));
		builder.put(dynamicops.createString("bonus_chest"), dynamicops.createBoolean(flag6));
		builder.put(dynamicops.createString("dimensions"), (T) a(dynamic, i, dynamic1, flag));
		optional1.ifPresent((s2) -> {
			builder.put(dynamicops.createString("legacy_custom_options"), dynamicops.createString(s2));
		});
		return new Dynamic(dynamicops, dynamicops.createMap(builder.build()));
	}

	protected static <T> Dynamic<T> a(Dynamic<T> dynamic, long i) {
		return a(i, dynamic, dynamic.createString("minecraft:overworld"), a(dynamic, i, false, false));
	}

	protected static <T> T a(Dynamic<T> dynamic, long i, Dynamic<T> dynamic1, boolean flag) {
		DynamicOps<T> dynamicops = dynamic.getOps();

		return dynamicops.createMap(ImmutableMap.of(dynamicops.createString("minecraft:overworld"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:overworld" + (flag ? "_caves" : "")), dynamicops.createString("generator"), dynamic1.getValue())), dynamicops.createString("minecraft:the_nether"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:the_nether"), dynamicops.createString("generator"), a(i, dynamic, dynamic.createString("minecraft:nether"), dynamic.createMap(ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:multi_noise"), dynamic.createString("seed"), dynamic.createLong(i), dynamic.createString("preset"), dynamic.createString("minecraft:nether")))).getValue())), dynamicops.createString("minecraft:the_end"), dynamicops.createMap(ImmutableMap.of(dynamicops.createString("type"), dynamicops.createString("minecraft:the_end"), dynamicops.createString("generator"), a(i, dynamic, dynamic.createString("minecraft:end"), dynamic.createMap(ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:the_end"), dynamic.createString("seed"), dynamic.createLong(i)))).getValue()))));
	}

	private static <T> Map<Dynamic<T>, Dynamic<T>> a(DynamicOps<T> dynamicops, OptionalDynamic<T> optionaldynamic) {
		MutableInt mutableint = new MutableInt(32);
		MutableInt mutableint1 = new MutableInt(3);
		MutableInt mutableint2 = new MutableInt(128);
		MutableBoolean mutableboolean = new MutableBoolean(false);
		Map<String, a_> map = Maps.newHashMap();

		if (optionaldynamic.result().isEmpty()) {
			mutableboolean.setTrue();
			map.put("minecraft:village", DataConverterWorldGenSettingsBuilding.a.get("minecraft:village"));
		}

		optionaldynamic.get("structures").flatMap(Dynamic::getMapValues).result().ifPresent((map1) -> {
			map1.forEach((dynamic, dynamic1) -> {
				dynamic1.getMapValues().result().ifPresent((map2) -> {
					map2.forEach((dynamic2, dynamic3) -> {
						String s = dynamic.asString("");
						String s1 = dynamic2.asString("");
						String s2 = dynamic3.asString("");
						byte b0;

						if ("stronghold".equals(s)) {
							mutableboolean.setTrue();
							b0 = -1;
							switch (s1.hashCode()) {
								case -895684237 -> {
									if (s1.equals("spread")) {
										b0 = 1;
									}
								}
								case 94851343 -> {
									if (s1.equals("count")) {
										b0 = 2;
									}
								}
								case 288459765 -> {
									if (s1.equals("distance")) {
										b0 = 0;
									}
								}
							}

							switch (b0) {
								case 0 -> {
									mutableint.setValue(a(s2, mutableint.getValue(), 1));
								}
								case 1 -> {
									mutableint1.setValue(a(s2, mutableint1.getValue(), 1));
								}
								case 2 -> {
									mutableint2.setValue(a(s2, mutableint2.getValue(), 1));
								}
								default -> {
								}
							}
						} else {
							b0 = -1;
							switch (s1.hashCode()) {
								case -2116852922 -> {
									if (s1.equals("separation")) {
										b0 = 1;
									}
								}
								case -2012158909 -> {
									if (s1.equals("spacing")) {
										b0 = 2;
									}
								}
								case 288459765 -> {
									if (s1.equals("distance")) {
										b0 = 0;
									}
								}
							}

							switch (b0) {
								case 0 -> {
									byte b1 = -1;
									switch (s.hashCode()) {
										case -1606796090 -> {
											if (s.equals("endcity")) {
												b1 = 2;
											}
										}
										case -107033518 -> {
											if (s.equals("biome_1")) {
												b1 = 1;
											}
										}
										case 460367020 -> {
											if (s.equals("village")) {
												b1 = 0;
											}
										}
										case 835798799 -> {
											if (s.equals("mansion")) {
												b1 = 3;
											}
										}
									}
									switch (b1) {
										case 0 -> {
											a(map, "minecraft:village", s2, 9);
										}
										case 1 -> {
											a(map, "minecraft:desert_pyramid", s2, 9);
											a(map, "minecraft:igloo", s2, 9);
											a(map, "minecraft:jungle_pyramid", s2, 9);
											a(map, "minecraft:swamp_hut", s2, 9);
											a(map, "minecraft:pillager_outpost", s2, 9);
										}
										case 2 -> {
											a(map, "minecraft:endcity", s2, 1);
										}
										case 3 -> {
											a(map, "minecraft:mansion", s2, 1);
										}
										default -> {
										}
									}
								}
								case 1 -> {
									if ("oceanmonument".equals(s)) {
										a_ dataconverterworldgensettingsbuilding_a = map.getOrDefault("minecraft:monument", DataConverterWorldGenSettingsBuilding.a.get("minecraft:monument"));
										int i = a(s2, dataconverterworldgensettingsbuilding_a.c, 1);

										map.put("minecraft:monument", new a_(i, dataconverterworldgensettingsbuilding_a.c, dataconverterworldgensettingsbuilding_a.d));
									}
								}
								case 2 -> {
									if ("oceanmonument".equals(s)) {
										a(map, "minecraft:monument", s2, 1);
									}
								}
								default -> {
								}
							}
						}
					});
				});
			});
		});
		Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder();

		builder.put(optionaldynamic.createString("structures"), optionaldynamic.createMap(map.entrySet().stream().collect(Collectors.toMap((entry) -> {
			return optionaldynamic.createString(entry.getKey());
		}, (entry) -> {
			return entry.getValue().a(dynamicops);
		}))));
		if (mutableboolean.isTrue()) {
			builder.put(optionaldynamic.createString("stronghold"), optionaldynamic.createMap(ImmutableMap.of(optionaldynamic.createString("distance"), optionaldynamic.createInt(mutableint.getValue()), optionaldynamic.createString("spread"), optionaldynamic.createInt(mutableint1.getValue()), optionaldynamic.createString("count"), optionaldynamic.createInt(mutableint2.getValue()))));
		}

		return builder.build();
	}

	private static int a(String s, int i) {
		return NumberUtils.toInt(s, i);
	}

	private static int a(String s, int i, int j) {
		return Math.max(j, a(s, i));
	}

	private static void a(Map<String, a_> map, String s, String s1, int i) {
		a_ dataconverterworldgensettingsbuilding_a = map.getOrDefault(s, DataConverterWorldGenSettingsBuilding.a.get(s));
		int j = a(s1, dataconverterworldgensettingsbuilding_a.b, i);

		map.put(s, new a_(j, dataconverterworldgensettingsbuilding_a.c, dataconverterworldgensettingsbuilding_a.d));
	}

	protected TypeRewriteRule makeRule() {
		return this.fixTypeEverywhereTyped("WorldGenSettings building", this.getInputSchema().getType(DataConverterTypes.WORLD_GEN_SETTINGS), (typed) -> {
			return typed.update(DSL.remainderFinder(), DataConverterWorldGenSettingsBuilding::a);
		});
	}

	static final class a_ {
		public static final Codec<a_> a = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.INT.fieldOf("spacing").forGetter((dataconverterworldgensettingsbuilding_a) -> {
				return dataconverterworldgensettingsbuilding_a.b;
			}), Codec.INT.fieldOf("separation").forGetter((dataconverterworldgensettingsbuilding_a) -> {
				return dataconverterworldgensettingsbuilding_a.c;
			}), Codec.INT.fieldOf("salt").forGetter((dataconverterworldgensettingsbuilding_a) -> {
				return dataconverterworldgensettingsbuilding_a.d;
			})).apply(instance, a_::new);
		});
		private final int b;
		private final int c;
		private final int d;

		public a_(int i, int j, int k) {
			this.b = i;
			this.c = j;
			this.d = k;
		}

		public <T> Dynamic<T> a(DynamicOps<T> dynamicops) {
			return new Dynamic(dynamicops, DataConverterWorldGenSettingsBuilding.a_.a.encodeStart(dynamicops, this).result().orElse(dynamicops.emptyMap()));
		}
	}
}
