package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import java.util.Objects;

public class DataConverterHorse extends DataConverterEntityName {
	public DataConverterHorse(Schema schema, boolean flag) {
		super("EntityHorseSplitFix", schema, flag);
	}

	@Override
	protected Pair<String, Typed<?>> a(String s, Typed<?> typed) {
		Dynamic<?> dynamic = typed.get(DSL.remainderFinder());

		if (Objects.equals("EntityHorse", s)) {
			int i = dynamic.get("Type").asInt(0);
			String s1 = switch (i) {
				default -> "Horse";
				case 1 -> "Donkey";
				case 2 -> "Mule";
				case 3 -> "ZombieHorse";
				case 4 -> "SkeletonHorse";
			};

			dynamic.remove("Type");
			Type<?> type = this.getOutputSchema().findChoiceType(DataConverterTypes.ENTITY).types().get(s1);
			var dataresult = typed.write();

			return Pair.of(s1, (dataresult.flatMap(type::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());
		} else {
			return Pair.of(s, typed);
		}
	}
}
