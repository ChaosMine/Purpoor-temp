package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;

public abstract class DataConverterEntityName extends DataFix {
	protected final String a;

	public DataConverterEntityName(String s, Schema schema, boolean flag) {
		super(schema, flag);
		this.a = s;
	}

	@SuppressWarnings("unchecked")
	public TypeRewriteRule makeRule() {
		var taggedchoicetype = (TaggedChoiceType<String>) this.getInputSchema().findChoiceType(DataConverterTypes.ENTITY);
		var taggedchoicetype1 = (TaggedChoiceType<String>) this.getOutputSchema().findChoiceType(DataConverterTypes.ENTITY);

		return this.fixTypeEverywhere(this.a, taggedchoicetype, taggedchoicetype1, dynamicops -> pair -> {
			String s = pair.getFirst();
			Type<?> type = taggedchoicetype.types().get(s);
			Pair<String, Typed<?>> pair1 = this.a(s, this.a(pair.getSecond(), dynamicops, type));
			Type<?> type1 = taggedchoicetype1.types().get(pair1.getFirst());

			if (!type1.equals(pair1.getSecond().getType(), true, true)) {
				throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", type1, pair1.getSecond().getType()));
			} else {
				return Pair.of(pair1.getFirst(), pair1.getSecond().getValue());
			}
		});
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private <A> Typed<A> a(Object object, DynamicOps<?> dynamicops, Type<A> type) {
		return new Typed(type, dynamicops, object);
	}

	protected abstract Pair<String, Typed<?>> a(String s, Typed<?> typed);
}
