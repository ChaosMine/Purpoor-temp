package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DataConverterEquipment extends DataFix {
	public DataConverterEquipment(Schema var0, boolean var1) {
		super(var0, var1);
	}

	@Override
	public TypeRewriteRule makeRule() {
		return this.a(this.getInputSchema().getTypeRaw(DataConverterTypes.ITEM_STACK));
	}

	private <IS> TypeRewriteRule a(Type<IS> var0) {
		var equipment = DSL.and(
			DSL.optional(DSL.field("Equipment", DSL.list(var0))),
			DSL.remainderType()
		);
		var items = DSL.and(
			DSL.optional(DSL.field("ArmorItems", DSL.list(var0))),
			DSL.optional(DSL.field("HandItems", DSL.list(var0))),
			DSL.remainderType()
		);
		var equipmentType = DSL.typeFinder(equipment);
		var equipmentField = DSL.fieldFinder("Equipment", DSL.list(var0));
		return this.fixTypeEverywhereTyped(
			"EntityEquipmentToArmorAndHandFix",
			this.getInputSchema().getType(DataConverterTypes.ENTITY),
			this.getOutputSchema().getType(DataConverterTypes.ENTITY),
			var4x -> {
				Either<List<IS>, com.mojang.datafixers.util.Unit> var5x = Either.right(DSL.unit());
				Either<List<IS>, com.mojang.datafixers.util.Unit> var6 = Either.right(DSL.unit());
				var var7 = var4x.getOrCreate(DSL.remainderFinder());
				var var8 = var4x.getOptional(equipmentField);
				if (var8.isPresent()) {
					var var9 = var8.get();
					var var10 = var0.read(var7.emptyMap())
						.result()
						.orElseThrow(() -> new IllegalStateException("Could not parse newly created empty itemstack."))
						.getFirst();

					if (!var9.isEmpty()) {
						var5x = Either.left(List.of(var9.get(0), var10));
					}

					if (var9.size() > 1) {
						List<IS> var11 = Lists.newArrayList(var10, var10, var10, var10);

						for (var var12 = 1; var12 < Math.min(var9.size(), 5); ++var12) {
							var11.set(var12 - 1, var9.get(var12));
						}

						var6 = Either.left(var11);
					}
				}

				var var9 = var7;
				Optional<? extends Stream<? extends Dynamic<?>>> var10 = var7.get("DropChances").asStreamOpt().result();
				if (var10.isPresent()) {
					Iterator<? extends Dynamic<?>> var11 = Stream.concat(var10.get(), Stream.generate(() -> var9.createInt(0))).iterator();
					var var12 = (var11.next()).asFloat(0.0F);
					if (var7.get("HandDropChances").result().isEmpty()) {
						var var13 = var7.createList(Stream.of(var12, 0.0F).map(var7::createFloat));
						var7 = var7.set("HandDropChances", var13);
					}

					if (var7.get("ArmorDropChances").result().isEmpty()) {
						var var13 = var7.createList(
							Stream.of(
								var11.next().asFloat(0.0F),
								var11.next().asFloat(0.0F),
								var11.next().asFloat(0.0F),
								var11.next().asFloat(0.0F)
							).map(var7::createFloat)
						);
						var7 = var7.set("ArmorDropChances", var13);
					}

					var7 = var7.remove("DropChances");
				}

				return var4x.set(equipmentType, items, Pair.of(var5x, Pair.of(var6, var7)));
			}
		);
	}
}
