package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class DataConverterSavedDataUUID extends DataConverterUUIDBase {
	public DataConverterSavedDataUUID(Schema schema) {
		super(schema, DataConverterTypes.SAVED_DATA);
	}

	protected TypeRewriteRule makeRule() {
		return this.fixTypeEverywhereTyped(
			"SavedDataUUIDFix",
			this.getInputSchema().getType(this.b),
			typed -> typed.updateTyped(
				typed.getType().findField("data"),
				typed1 -> typed1.update(
					DSL.remainderFinder(),
					dynamic -> dynamic.update(
						"Raids",
						dynamic1 -> dynamic1.createList(
							dynamic1.asStream().map(dynamic2 -> dynamic2.update(
								"HeroesOfTheVillage",
								dynamic3 -> dynamic3.createList(dynamic3.asStream().map(dynamic4 -> d(dynamic4, "UUIDMost", "UUIDLeast").orElseGet(() -> {
									DataConverterSavedDataUUID.LOGGER.warn("HeroesOfTheVillage contained invalid UUIDs.");
									return dynamic4;
								})))
							))
						)
					)
				)
			)
		);
	}
}
