package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DataConverterTrappedChest extends DataFix {
	private static final Logger LOGGER = LogManager.getLogger();

	public DataConverterTrappedChest(Schema schema, boolean flag) {
		super(schema, flag);
	}

	@SuppressWarnings("unchecked")
	public TypeRewriteRule makeRule() {
		Type<?> type = this.getOutputSchema().getType(DataConverterTypes.CHUNK);
		Type<?> type1 = type.findFieldType("Level");
		Type<?> type2 = type1.findFieldType("TileEntities");

		if (!(type2 instanceof ListType<?> listtype)) {
			throw new IllegalStateException("Tile entity type is not a list type.");
		} else {
			OpticFinder<? extends List<?>> opticfinder = DSL.fieldFinder("TileEntities", listtype);
			Type<?> type3 = this.getInputSchema().getType(DataConverterTypes.CHUNK);
			OpticFinder<?> opticfinder1 = type3.findField("Level");
			OpticFinder<?> opticfinder2 = opticfinder1.type().findField("Sections");
			Type<?> type4 = opticfinder2.type();

			if (!(type4 instanceof ListType<?> type5)) {
				throw new IllegalStateException("Expecting sections to be a list.");
			} else {
				OpticFinder<?> opticfinder3 = DSL.typeFinder(type5);

				return TypeRewriteRule.seq(new DataConverterAddChoices(this.getOutputSchema(), "AddTrappedChestFix", DataConverterTypes.BLOCK_ENTITY).makeRule(), this.fixTypeEverywhereTyped("Trapped Chest fix", type3, typed -> {
					return typed.updateTyped(opticfinder1, typed1 -> {
						Optional<? extends Typed<?>> optional = typed1.getOptionalTyped(opticfinder2);

						if (optional.isEmpty()) {
							return typed1;
						} else {
							List<? extends Typed<?>> list = optional.get().getAllTyped(opticfinder3);
							IntOpenHashSet intopenhashset = new IntOpenHashSet();

							for (Typed<?> typed2 : list) {
								a dataconvertertrappedchest_a = new a(typed2, this.getInputSchema());

								if (!dataconvertertrappedchest_a.b()) {
									for (int i = 0; i < 4096; ++i) {
										int j = dataconvertertrappedchest_a.c(i);

										if (dataconvertertrappedchest_a.a(j)) {
											intopenhashset.add(dataconvertertrappedchest_a.c() << 12 | i);
										}
									}
								}
							}

							Dynamic<?> dynamic = typed1.get(DSL.remainderFinder());
							int k = dynamic.get("xPos").asInt(0);
							int l = dynamic.get("zPos").asInt(0);
							var taggedchoicetype = (TaggedChoiceType<String>) this.getInputSchema().findChoiceType(DataConverterTypes.BLOCK_ENTITY);

							return typed1.updateTyped(opticfinder, typed3 -> {
								return typed3.updateTyped(taggedchoicetype.finder(), typed4 -> {
									Dynamic<?> dynamic1 = typed4.getOrCreate(DSL.remainderFinder());
									int i1 = dynamic1.get("x").asInt(0) - (k << 4);
									int j1 = dynamic1.get("y").asInt(0);
									int k1 = dynamic1.get("z").asInt(0) - (l << 4);

									return intopenhashset.contains(DataConverterLeaves.a(i1, j1, k1)) ? typed4.update(taggedchoicetype.finder(), pair -> {
										return pair.mapFirst(s -> {
											if (!Objects.equals(s, "minecraft:chest")) {
												DataConverterTrappedChest.LOGGER.warn("Block Entity was expected to be a chest");
											}

											return "minecraft:trapped_chest";
										});
									}) : typed4;
								});
							});
						}
					});
				}));
			}
		}
	}

	public static final class a extends DataConverterLeaves.b_ {
		@Nullable
		private IntSet e;

		public a(Typed<?> typed, Schema schema) {
			super(typed, schema);
		}

		@Override
		protected boolean a() {
			this.e = new IntOpenHashSet();

			for (int i = 0; i < this.b.size(); ++i) {
				Dynamic<?> dynamic = this.b.get(i);
				String s = dynamic.get("Name").asString("");

				if (Objects.equals(s, "minecraft:trapped_chest")) {
					this.e.add(i);
				}
			}

			return this.e.isEmpty();
		}

		public boolean a(int i) {
			return this.e.contains(i);
		}
	}
}
