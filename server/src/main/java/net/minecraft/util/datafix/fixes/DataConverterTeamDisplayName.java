package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;

import java.util.Objects;

public class DataConverterTeamDisplayName extends DataFix {
	public DataConverterTeamDisplayName(Schema schema, boolean flag) {
		super(schema, flag);
	}

	protected TypeRewriteRule makeRule() {
		Type<Pair<String, Dynamic<?>>> type = DSL.named(DataConverterTypes.TEAM.typeName(), DSL.remainderType());

		if (!Objects.equals(type, this.getInputSchema().getType(DataConverterTypes.TEAM))) {
			throw new IllegalStateException("Team type is not what was expected.");
		} else {
			return this.fixTypeEverywhere("TeamDisplayNameFix", type, (dynamicops) -> (pair) -> pair.mapSecond((dynamic) -> dynamic.update("DisplayName", (dynamic1) -> {
				var dataresult = dynamic1.asString().map((s) -> IChatBaseComponent.ChatSerializer.a(new ChatComponentText(s)));

				return DataFixUtils.orElse(dataresult.map(dynamic::createString).result(), dynamic1);
			})));
		}
	}
}
