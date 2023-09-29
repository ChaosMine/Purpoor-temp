package net.minecraft.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.IRegistry;

import java.util.List;
import java.util.function.Supplier;

public final class RegistryFileCodec<E> implements Codec<Supplier<E>> {
	private final ResourceKey<? extends IRegistry<E>> a;
	private final Codec<E> b;
	private final boolean c;

	private RegistryFileCodec(ResourceKey<? extends IRegistry<E>> var0, Codec<E> var1, boolean var2) {
		this.a = var0;
		this.b = var1;
		this.c = var2;
	}

	public static <E> RegistryFileCodec<E> a(ResourceKey<? extends IRegistry<E>> var0, Codec<E> var1) {
		return RegistryFileCodec.a(var0, var1, true);
	}

	public static <E> Codec<List<Supplier<E>>> b(ResourceKey<? extends IRegistry<E>> var03, Codec<E> var1) {
		return Codec.either(RegistryFileCodec.a(var03, var1, false).listOf(), var1.xmap(var0 -> (Supplier<E>) () -> var0, Supplier::get).listOf()).xmap(var02 -> var02.map(var0 -> var0, var0 -> var0), Either::left);
	}

	private static <E> RegistryFileCodec<E> a(ResourceKey<? extends IRegistry<E>> var0, Codec<E> var1, boolean var2) {
		return new RegistryFileCodec<E>(var0, var1, var2);
	}

	@Override
	public <T> DataResult<T> encode(Supplier<E> var0, DynamicOps<T> var1, T var2) {
		if (var1 instanceof RegistryWriteOps<T> readOps) {
			return readOps.a(var0.get(), var2, this.a, this.b);
		}
		return this.b.encode(var0.get(), var1, var2);
	}

	@Override
	public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> var0, T var1) {
		if (var0 instanceof RegistryReadOps<T> readOps) {
			return readOps.a(var1, this.a, this.b, this.c);
		}
		return this.b.decode(var0, var1).map(var02 -> var02.mapFirst(var0x -> () -> var0x));
	}

	public String toString() {
		return "RegistryFileCodec[" + this.a + " " + this.b + "]";
	}
}
