package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public class DataConverterItemFrame extends DataConverterNamedEntity {
	public DataConverterItemFrame(Schema schema, boolean flag) {
		super(schema, flag, "EntityItemFrameDirectionFix", DataConverterTypes.ENTITY, "minecraft:item_frame");
	}

	private static byte a(byte b0) {
		return switch (b0) {
			case 0 -> 3;
			case 1 -> 4;
			default -> 2;
			case 3 -> 5;
		};
	}

	public Dynamic<?> a(Dynamic<?> dynamic) {
		return dynamic.set("Facing", dynamic.createByte(a(dynamic.get("Facing").asByte((byte) 0))));
	}

	@Override
	protected Typed<?> a(Typed<?> typed) {
		return typed.update(DSL.remainderFinder(), this::a);
	}
}
