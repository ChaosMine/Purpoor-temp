package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.ChatDeserializer;
import org.apache.commons.lang3.StringUtils;

public class DataConverterBook extends DataFix {
	public DataConverterBook(Schema schema, boolean flag) {
		super(schema, flag);
	}

	public Dynamic<?> a(Dynamic<?> dynamic) {
		return dynamic.update("pages", (dynamic1) -> {
			var dataresult = dynamic1.asStreamOpt().map((stream) -> stream.map((dynamic2) -> {
				if (dynamic2.asString().result().isEmpty()) {
					return dynamic2;
				} else {
					String s = dynamic2.asString("");
					Object object = null;

					if (!"null".equals(s) && !StringUtils.isEmpty(s)) {
						if ((s.charAt(0) != '"' || s.charAt(s.length() - 1) != '"') && (s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}')) {
							object = new ChatComponentText(s);
						} else {
							try {
								object = ChatDeserializer.a(DataConverterSignText.a, s, IChatBaseComponent.class, true);
								if (object == null) {
									object = ChatComponentText.d;
								}
							} catch (JsonParseException ignored) {
							}

							if (object == null) {
								try {
									object = IChatBaseComponent.ChatSerializer.a(s);
								} catch (JsonParseException ignored) {
								}
							}

							if (object == null) {
								try {
									object = IChatBaseComponent.ChatSerializer.b(s);
								} catch (JsonParseException ignored) {
								}
							}

							if (object == null) {
								object = new ChatComponentText(s);
							}
						}
					} else {
						object = ChatComponentText.d;
					}

					return dynamic2.createString(IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) object));
				}
			}));

			return DataFixUtils.orElse(dataresult.map(dynamic::createList).result(), dynamic.emptyList());
		});
	}

	public TypeRewriteRule makeRule() {
		Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
		OpticFinder<?> opticfinder = type.findField("tag");

		return this.fixTypeEverywhereTyped("ItemWrittenBookPagesStrictJsonFix", type, (typed) -> typed.updateTyped(opticfinder, (typed1) -> typed1.update(DSL.remainderFinder(), this::a)));
	}
}
