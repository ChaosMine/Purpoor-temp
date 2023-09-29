package net.minecraft.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class IntSpread {
	public static final Codec<IntSpread> a = Codec.either(
		Codec.INT,
		RecordCodecBuilder.<IntSpread>create(instance ->
			instance.group(
				Codec.INT.fieldOf("base").forGetter(spread -> spread.b),
				Codec.INT.fieldOf("spread").forGetter((intspread) -> intspread.c)
			).apply(instance, IntSpread::new)
		).comapFlatMap(spread -> spread.c < 0
			? DataResult.error("Spread must be non-negative, got: " + spread.c)
			: DataResult.success(spread), Function.identity()
		)
	).xmap(
		either -> either.map(IntSpread::a, Function.identity()),
		spread -> spread.c == 0 ? Either.left(spread.b) : Either.right(spread)
	);
	private final int b;
	private final int c;

	private IntSpread(int i, int j) {
		this.b = i;
		this.c = j;
	}

	public static Codec<IntSpread> a(int i, int j, int k) {
		Function<IntSpread, DataResult<IntSpread>> function = (intspread) -> {
			return intspread.b >= i && intspread.b <= j ? (intspread.c <= k ? DataResult.success(intspread) : DataResult.error("Spread too big: " + intspread.c + " > " + k)) : DataResult.error("Base value out of range: " + intspread.b + " [" + i + "-" + j + "]");
		};

		return IntSpread.a.flatXmap(function, function);
	}

	public static IntSpread a(int i) {
		return new IntSpread(i, 0);
	}

	public static IntSpread a(int i, int j) {
		return new IntSpread(i, j);
	}

	public int a(Random random) {
		return this.c == 0 ? this.b : this.b + random.nextInt(this.c + 1);
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			IntSpread intspread = (IntSpread) object;

			return this.b == intspread.b && this.c == intspread.c;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return Objects.hash(this.b, this.c);
	}

	public String toString() {
		return "[" + this.b + '-' + (this.b + this.c) + ']';
	}
}
