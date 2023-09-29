package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Function;

public class StructureSettingsFeature {
	public static final Codec<StructureSettingsFeature> a = RecordCodecBuilder.<StructureSettingsFeature>create(var02 -> var02.group((Codec.intRange(0, 4096).fieldOf("spacing")).forGetter(var0 -> var0.b), (Codec.intRange(0, 4096).fieldOf("separation")).forGetter(var0 -> var0.c), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("salt").forGetter(var0 -> var0.d)).apply(var02, StructureSettingsFeature::new)).comapFlatMap(var0 -> {
		if (var0.b <= var0.c) {
			return DataResult.error("Spacing has to be smaller than separation");
		}
		return DataResult.success(var0);
	}, Function.identity());
	private final int b;
	private final int c;
	private final int d;

	public StructureSettingsFeature(int var0, int var1, int var2) {
		this.b = var0;
		this.c = var1;
		this.d = var2;
	}

	public int a() {
		return this.b;
	}

	public int b() {
		return this.c;
	}

	public int c() {
		return this.d;
	}
}
