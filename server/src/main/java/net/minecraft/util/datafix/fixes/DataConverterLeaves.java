package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.datafix.DataBitsPacked;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class DataConverterLeaves extends DataFix {
	private static final int[][] a = new int[][]{{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};
	private static final Object2IntMap<String> b = DataFixUtils.make(new Object2IntOpenHashMap<>(), map -> {
		map.put("minecraft:acacia_leaves", 0);
		map.put("minecraft:birch_leaves", 1);
		map.put("minecraft:dark_oak_leaves", 2);
		map.put("minecraft:jungle_leaves", 3);
		map.put("minecraft:oak_leaves", 4);
		map.put("minecraft:spruce_leaves", 5);
	});
	private static final Set<String> c = ImmutableSet.of(
		"minecraft:acacia_bark",
		"minecraft:birch_bark",
		"minecraft:dark_oak_bark",
		"minecraft:jungle_bark",
		"minecraft:oak_bark",
		"minecraft:spruce_bark",
		"minecraft:acacia_log",
		"minecraft:birch_log",
		"minecraft:dark_oak_log",
		"minecraft:jungle_log",
		"minecraft:oak_log",
		"minecraft:spruce_log",
		"minecraft:stripped_acacia_log",
		"minecraft:stripped_birch_log",
		"minecraft:stripped_dark_oak_log",
		"minecraft:stripped_jungle_log",
		"minecraft:stripped_oak_log",
		"minecraft:stripped_spruce_log"
	);

	public DataConverterLeaves(Schema var0, boolean var1) {
		super(var0, var1);
	}

	public static int a(int var0, int var1, int var2) {
		return var1 << 8 | var2 << 4 | var0;
	}

	public static int a(boolean var0, boolean var1, boolean var2, boolean var3) {
		int var4 = 0;
		if (var2) {
			if (var1) {
				var4 |= 2;
			} else if (var0) {
				var4 |= 128;
			} else {
				var4 |= 1;
			}
		} else if (var3) {
			if (var0) {
				var4 |= 32;
			} else if (var1) {
				var4 |= 8;
			} else {
				var4 |= 16;
			}
		} else if (var1) {
			var4 |= 4;
		} else if (var0) {
			var4 |= 64;
		}

		return var4;
	}

	@Override
	protected TypeRewriteRule makeRule() {
		Type<?> var0 = this.getInputSchema().getType(DataConverterTypes.CHUNK);
		OpticFinder<?> var1 = var0.findField("Level");
		OpticFinder<?> var2 = var1.type().findField("Sections");
		Type<?> var3 = var2.type();
		if (!(var3 instanceof ListType<?> listType)) {
			throw new IllegalStateException("Expecting sections to be a list.");
		} else {
			Type<?> var4 = listType.getElement();
			OpticFinder<?> var5 = DSL.typeFinder(var4);
			return this.fixTypeEverywhereTyped(
				"Leaves fix",
				var0,
				var3x -> var3x.updateTyped(
					var1,
					var2xx -> {
						int[] var3xx = new int[]{0};
						Typed<?> var4x = var2xx.updateTyped(
							var2,
							var2xxx -> {
								Int2ObjectMap<DataConverterLeaves.a> var3xxx = new Int2ObjectOpenHashMap<>(
									var2xxx.getAllTyped(var5)
										.stream()
										.map(var0xxxx -> new DataConverterLeaves.a(var0xxxx, this.getInputSchema()))
										.collect(Collectors.toMap(DataConverterLeaves.b_::c, var0xxxx -> var0xxxx))
								);
								if (var3xxx.values().stream().allMatch(DataConverterLeaves.b_::b)) {
									return var2xxx;
								} else {
									List<IntSet> var4xx = Lists.newArrayList();

									for (int var5x = 0; var5x < 7; ++var5x) {
										var4xx.add(new IntOpenHashSet());
									}

									for (DataConverterLeaves.a var6x : var3xxx.values()) {
										if (!var6x.b()) {
											for (int var7 = 0; var7 < 4096; ++var7) {
												int var8 = var6x.c(var7);
												if (var6x.a(var8)) {
													var4xx.get(0).add(var6x.c() << 12 | var7);
												} else if (var6x.b(var8)) {
													int var9 = this.a(var7);
													int var10 = this.c(var7);
													var3xx[0] |= a(var9 == 0, var9 == 15, var10 == 0, var10 == 15);
												}
											}
										}
									}

									for (int var5ax = 1; var5ax < 7; ++var5ax) {
										IntSet var6 = var4xx.get(var5ax - 1);
										IntSet var7 = var4xx.get(var5ax);
										IntIterator var8 = var6.iterator();

										while (var8.hasNext()) {
											int var9 = var8.nextInt();
											int var10 = this.a(var9);
											int var11 = this.b(var9);
											int var12 = this.c(var9);

											for (int[] var16 : a) {
												int var17 = var10 + var16[0];
												int var18 = var11 + var16[1];
												int var19 = var12 + var16[2];
												if (var17 >= 0 && var17 <= 15 && var19 >= 0 && var19 <= 15 && var18 >= 0 && var18 <= 255) {
													DataConverterLeaves.a var20 = var3xxx.get(var18 >> 4);
													if (var20 != null && !var20.b()) {
														int var21 = a(var17, var18 & 15, var19);
														int var22 = var20.c(var21);
														if (var20.b(var22)) {
															int var23 = var20.d(var22);
															if (var23 > var5ax) {
																var20.a(var21, var22, var5ax);
																var7.add(a(var17, var18, var19));
															}
														}
													}
												}
											}
										}
									}

									return var2xxx.updateTyped(
										var5,
										var1xxxx -> var3xxx.get(var1xxxx.get(DSL.remainderFinder()).get("Y").asInt(0)).a(var1xxxx)
									);
								}
							}
						);
						if (var3xx[0] != 0) {
							var4x = var4x.update(
								DSL.remainderFinder(),
								var1xxx -> {
									Dynamic<?> var2xxx = DataFixUtils.orElse(var1xxx.get("UpgradeData").result(), var1xxx.emptyMap());
									return var1xxx.set(
										"UpgradeData", var2xxx.set("Sides", var1xxx.createByte((byte) (var2xxx.get("Sides").asByte((byte) 0) | var3xx[0])))
									);
								}
							);
						}

						return var4x;
					}
				)
			);
		}
	}

	private int a(int var0) {
		return var0 & 15;
	}

	private int b(int var0) {
		return var0 >> 8 & 0xFF;
	}

	private int c(int var0) {
		return var0 >> 4 & 15;
	}

	public static final class a extends b_ {
		@Nullable
		private IntSet e;
		@Nullable
		private IntSet f;
		@Nullable
		private Int2IntMap g;

		public a(Typed<?> var0, Schema var1) {
			super(var0, var1);
		}

		@Override
		protected boolean a() {
			this.e = new IntOpenHashSet();
			this.f = new IntOpenHashSet();
			this.g = new Int2IntOpenHashMap();

			for (int var0 = 0; var0 < this.b.size(); ++var0) {
				Dynamic<?> var1 = this.b.get(var0);
				String var2 = var1.get("Name").asString("");
				if (DataConverterLeaves.b.containsKey(var2)) {
					boolean var3 = Objects.equals(var1.get("Properties").get("decayable").asString(""), "false");
					this.e.add(var0);
					this.g.put(this.a(var2, var3, 7), var0);
					this.b.set(var0, this.a(var1, var2, var3, 7));
				}

				if (DataConverterLeaves.c.contains(var2)) {
					this.f.add(var0);
				}
			}

			return this.e.isEmpty() && this.f.isEmpty();
		}

		private Dynamic<?> a(Dynamic<?> var0, String var1, boolean var2, int var3) {
			Dynamic<?> var4 = var0.emptyMap();
			var4 = var4.set("persistent", var4.createString(var2 ? "true" : "false"));
			var4 = var4.set("distance", var4.createString(Integer.toString(var3)));
			Dynamic<?> var5 = var0.emptyMap();
			var5 = var5.set("Properties", var4);
			return var5.set("Name", var5.createString(var1));
		}

		public boolean a(int var0) {
			return this.f.contains(var0);
		}

		public boolean b(int var0) {
			return this.e.contains(var0);
		}

		private int d(int var0) {
			return this.a(var0) ? 0 : Integer.parseInt(((Dynamic) this.b.get(var0)).get("Properties").get("distance").asString(""));
		}

		private void a(int var0, int var1, int var2) {
			Dynamic<?> var3 = this.b.get(var1);
			String var4 = var3.get("Name").asString("");
			boolean var5 = Objects.equals(var3.get("Properties").get("persistent").asString(""), "true");
			int var6 = this.a(var4, var5, var2);
			if (!this.g.containsKey(var6)) {
				int var7 = this.b.size();
				this.e.add(var7);
				this.g.put(var6, var7);
				this.b.add(this.a(var3, var4, var5, var2));
			}

			int var7 = this.g.get(var6);
			if (1 << this.d.b() <= var7) {
				DataBitsPacked var8 = new DataBitsPacked(this.d.b() + 1, 4096);

				for (int var9 = 0; var9 < 4096; ++var9) {
					var8.a(var9, this.d.a(var9));
				}

				this.d = var8;
			}

			this.d.a(var0, var7);
		}
	}

	public abstract static class b_ {
		protected final List<Dynamic<?>> b;
		protected final int c;
		private final Type<Pair<String, Dynamic<?>>> e = DSL.named(DataConverterTypes.BLOCK_STATE.typeName(), DSL.remainderType());
		protected final OpticFinder<List<Pair<String, Dynamic<?>>>> a = DSL.fieldFinder("Palette", DSL.list(this.e));
		@Nullable
		protected DataBitsPacked d;

		@SuppressWarnings({"unchecked", "rawtypes"})
		public b_(Typed<?> var0, Schema var1) {
			if (!Objects.equals(var1.getType(DataConverterTypes.BLOCK_STATE), this.e)) {
				throw new IllegalStateException("Block state type is not what was expected.");
			} else {
				Optional<List<Pair<String, Dynamic<?>>>> var2 = var0.getOptional(this.a);
				this.b = (List) var2.map(var0x -> var0x.stream().map(Pair::getSecond).toList()).orElse(List.of());
				Dynamic<?> var3 = var0.get(DSL.remainderFinder());
				this.c = var3.get("Y").asInt(0);
				this.a(var3);
			}
		}

		protected void a(Dynamic<?> var0) {
			if (this.a()) {
				this.d = null;
			} else {
				long[] var1 = var0.get("BlockStates").asLongStream().toArray();
				int var2 = Math.max(4, DataFixUtils.ceillog2(this.b.size()));
				this.d = new DataBitsPacked(var2, 4096, var1);
			}
		}

		public Typed<?> a(Typed<?> var0) {
			return this.b()
				? var0
				: var0.update(DSL.remainderFinder(), var0x -> var0x.set("BlockStates", var0x.createLongList(Arrays.stream(this.d.a()))))
				.set(
					this.a,
					(List) this.b.stream()
						.map(var0x -> Pair.of(DataConverterTypes.BLOCK_STATE.typeName(), var0x))
						.toList()
				);
		}

		public boolean b() {
			return this.d == null;
		}

		public int c(int var0) {
			return this.d.a(var0);
		}

		protected int a(String var0, boolean var1, int var2) {
			return DataConverterLeaves.b.get(var0) << 5 | (var1 ? 16 : 0) | var2;
		}

		int c() {
			return this.c;
		}

		protected abstract boolean a();
	}
}
