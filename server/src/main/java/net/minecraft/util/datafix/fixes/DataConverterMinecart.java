package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import java.util.List;
import java.util.Objects;

public class DataConverterMinecart extends DataFix {
	private static final List<String> a = Lists.newArrayList("MinecartRideable", "MinecartChest", "MinecartFurnace");

	public DataConverterMinecart(Schema schema, boolean flag) {
		super(schema, flag);
	}

	@SuppressWarnings("unchecked")
	public TypeRewriteRule makeRule() {
		var taggedchoicetype = (TaggedChoiceType<String>) this.getInputSchema().findChoiceType(DataConverterTypes.ENTITY);
		var taggedchoicetype1 = (TaggedChoiceType<String>) this.getOutputSchema().findChoiceType(DataConverterTypes.ENTITY);

		return this.fixTypeEverywhere("EntityMinecartIdentifiersFix", taggedchoicetype, taggedchoicetype1, (dynamicops) -> (pair) -> {
			if (!Objects.equals(pair.getFirst(), "Minecart")) {
				return pair;
			} else {
				Typed<? extends Pair<String, ?>> typed = taggedchoicetype.point(dynamicops, "Minecart", pair.getSecond()).orElseThrow(IllegalStateException::new);
				Dynamic<?> dynamic = typed.getOrCreate(DSL.remainderFinder());
				int i = dynamic.get("Type").asInt(0);
				String s;

				if (i > 0 && i < DataConverterMinecart.a.size()) {
					s = DataConverterMinecart.a.get(i);
				} else {
					s = "MinecartRideable";
				}

				return Pair.of(s, typed.write().map((dynamic1) -> (taggedchoicetype1.types().get(s)).read(dynamic1)).result().orElseThrow(() -> new IllegalStateException("Could not read the new minecart.")));
			}
		});
	}
}
