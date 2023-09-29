package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import java.util.Objects;
import java.util.Optional;

public abstract class DataConverterPOIRename extends DataFix {
	public DataConverterPOIRename(Schema schema, boolean flag) {
		super(schema, flag);
	}

	protected TypeRewriteRule makeRule() {
		Type<Pair<String, Dynamic<?>>> type = DSL.named(DataConverterTypes.POI_CHUNK.typeName(), DSL.remainderType());

		if (!Objects.equals(type, this.getInputSchema().getType(DataConverterTypes.POI_CHUNK))) {
			throw new IllegalStateException("Poi type is not what was expected.");
		} else {
			return this.fixTypeEverywhere("POI rename", type, dynamicops -> pair -> pair.mapSecond(this::a));
		}
	}

	private <T> Dynamic<T> a(Dynamic<T> dynamic) {
		return dynamic.update("Sections", dynamic1 -> dynamic1.updateMapValues(pair -> pair.mapSecond(dynamic2 -> dynamic2.update("Records", dynamic3 -> DataFixUtils.orElse(this.b(dynamic3), dynamic3)))));
	}

	private <T> Optional<Dynamic<T>> b(Dynamic<T> dynamic) {
		return dynamic.asStreamOpt().map(stream ->
			dynamic.createList(stream.map(dynamic1 ->
					dynamic1.update("type", dynamic2 ->
						DataFixUtils.orElse(
							dynamic2.asString().map(this::a).map(dynamic2::createString).result(),
							dynamic2
						)
					)
				)
			)
		).result();
	}

	protected abstract String a(String s);
}
