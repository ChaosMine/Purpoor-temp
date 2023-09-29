package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DataConverterRiding extends DataFix {
	public DataConverterRiding(Schema schema, boolean flag) {
		super(schema, flag);
	}

	public TypeRewriteRule makeRule() {
		var schema = this.getInputSchema();
		var schema1 = this.getOutputSchema();
		var type = schema.getTypeRaw(DataConverterTypes.ENTITY_TREE);
		var type1 = schema1.getTypeRaw(DataConverterTypes.ENTITY_TREE);
		var type2 = schema.getTypeRaw(DataConverterTypes.ENTITY);

		return this.a(schema, schema1, type, type1, type2);
	}

	private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule a(Schema schema, Schema schema1, Type<OldEntityTree> type, Type<NewEntityTree> type1, Type<Entity> type2) {
		var type3 = DSL.named(DataConverterTypes.ENTITY_TREE.typeName(), DSL.and(DSL.optional(DSL.field("Riding", type)), type2));
		var type4 = DSL.named(DataConverterTypes.ENTITY_TREE.typeName(), DSL.and(DSL.optional(DSL.field("Passengers", DSL.list(type1))), type2));
		var type5 = schema.getType(DataConverterTypes.ENTITY_TREE);
		var type6 = schema1.getType(DataConverterTypes.ENTITY_TREE);

		if (!Objects.equals(type5, type3)) {
			throw new IllegalStateException("Old entity type is not what was expected.");
		} else if (!type6.equals(type4, true, true)) {
			throw new IllegalStateException("New entity type is not what was expected.");
		} else {
			var opticfinder = DSL.typeFinder(type3);
			var opticfinder1 = DSL.typeFinder(type4);
			var opticfinder2 = DSL.typeFinder(type1);
			var type7 = schema.getType(DataConverterTypes.PLAYER);
			var type8 = schema1.getType(DataConverterTypes.PLAYER);

			return TypeRewriteRule.seq(this.fixTypeEverywhere("EntityRidingToPassengerFix", type3, type4, (dynamicops) -> (pair) -> {
				Optional<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> optional = Optional.empty();
				var pair1 = pair;

				while (true) {
					Either<List<NewEntityTree>, Unit> either = DataFixUtils.orElse(optional.map((pair2) -> {
						var typed = type1.pointTyped(dynamicops).orElseThrow(() -> new IllegalStateException("Could not create new entity tree"));
						var newentitytree = typed.set(opticfinder1, pair2).getOptional(opticfinder2).orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));

						return Either.left(ImmutableList.of(newentitytree));
					}), Either.right(DSL.unit()));

					optional = Optional.of(Pair.of(DataConverterTypes.ENTITY_TREE.typeName(), Pair.of(either, pair1.getSecond().getSecond())));
					var optional1 = ((pair1.getSecond()).getFirst()).left();

					if (optional1.isEmpty()) {
						return optional.orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));
					}

					pair1 = new Typed<>(type, dynamicops, optional1.get()).getOptional(opticfinder).orElseThrow(() -> new IllegalStateException("Should always have an entity here"));
				}
			}), this.writeAndRead("player RootVehicle injecter", type7, type8));
		}
	}
}
