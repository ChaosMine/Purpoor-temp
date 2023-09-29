package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.datafix.schemas.DataConverterSchemaNamed;

import java.util.Objects;

public abstract class DataConverterEntityRenameAbstract extends DataFix {
	private final String a;

	public DataConverterEntityRenameAbstract(String s, Schema schema, boolean flag) {
		super(schema, flag);
		this.a = s;
	}

	@SuppressWarnings("unchecked")
	public TypeRewriteRule makeRule() {
		var taggedchoicetype = (TaggedChoiceType<String>) this.getInputSchema().findChoiceType(DataConverterTypes.ENTITY);
		var taggedchoicetype1 = (TaggedChoiceType<String>) this.getOutputSchema().findChoiceType(DataConverterTypes.ENTITY);
		Type<Pair<String, String>> type = DSL.named(DataConverterTypes.ENTITY_NAME.typeName(), DataConverterSchemaNamed.a());

		if (!Objects.equals(this.getOutputSchema().getType(DataConverterTypes.ENTITY_NAME), type)) {
			throw new IllegalStateException("Entity name type is not what was expected.");
		} else {
			return TypeRewriteRule.seq(this.fixTypeEverywhere(this.a, taggedchoicetype, taggedchoicetype1, (dynamicops) -> {
				return (pair) -> {
					return pair.mapFirst((s) -> {
						String s1 = this.a(s);
						Type<?> type1 = taggedchoicetype.types().get(s);
						Type<?> type2 = taggedchoicetype1.types().get(s1);

						if (!type2.equals(type1, true, true)) {
							throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", type2, type1));
						} else {
							return s1;
						}
					});
				};
			}), this.fixTypeEverywhere(this.a + " for entity name", type, (dynamicops) -> {
				return (pair) -> {
					return pair.mapSecond(this::a);
				};
			}));
		}
	}

	protected abstract String a(String s);
}
