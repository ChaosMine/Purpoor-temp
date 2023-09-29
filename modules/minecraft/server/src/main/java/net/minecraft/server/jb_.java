package net.minecraft.server;

import com.google.gson.JsonElement;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class jb_ {

	public static final jb_.a a = a(iz_::a, iy.c);
	public static final jb_.a b = a(iz_::a, iy.d);
	public static final jb_.a c = a(iz_::j, iy.e);
	public static final jb_.a d = a(iz_::j, iy.f);
	public static final jb_.a e = a(iz_::m, iy.h);
	public static final jb_.a f = a(iz_::k, iy.g);
	public static final jb_.a g = a(iz_::x, iy.i);
	public static final jb_.a h = a(iz_::w, iy.j);
	public static final jb_.a i = a(iz_::f, iy.aa);
	public static final jb_.a j = a(iz_::h, iy.ad);
	public static final jb_.a k = a(iz_::i, iy.ab);
	public static final jb_.a l = a(iz_::q, iy.F);
	public static final jb_.a m = a(iz_::z, iy.am);
	public static final jb_.a n = a(iz_::a, iy.I);
	public static final jb_.a o = a(iz_::t, iy.ax);
	public static final jb_.a p = a(iz_::t, iy.ay);
	public static final jb_.a q = a(iz_::b, iy.aE);
	public static final jb_.a r = a(iz_::l, iy.e);
	public static final jb_.a s = a(iz_::l, iy.f);
	public static final jb_.a t = a(iz_::n, iy.h);
	public static final jb_.a u = a(iz_::o, iy.e);
	private final iz_ v;
	private final ix w;

	private jb_(iz_ iz, ix ix) {
		this.v = iz;
		this.w = ix;
	}

	private static jb_.a a(Function<Block, iz_> function, ix ix) {
		return (block) -> new jb_(function.apply(block), ix);
	}

	public static jb_ a(MinecraftKey minecraftkey) {
		return new jb_(iz_.b(minecraftkey), iy.c);
	}

	public ix a() {
		return this.w;
	}

	public iz_ b() {
		return this.v;
	}

	public jb_ a(Consumer<iz_> consumer) {
		consumer.accept(this.v);
		return this;
	}

	public MinecraftKey a(Block block, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		return this.w.a(block, this.v, biconsumer);
	}

	public MinecraftKey a(Block block, String s, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		return this.w.a(block, s, this.v, biconsumer);
	}

	@FunctionalInterface
	public interface a {

		jb_ get(Block block);

		default MinecraftKey a(Block block, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
			return this.get(block).a(block, biconsumer);
		}

		default MinecraftKey a(Block block, String s, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
			return this.get(block).a(block, s, biconsumer);
		}

		default jb_.a a(Consumer<iz_> consumer) {
			return (block) -> this.get(block).a(consumer);
		}
	}
}
