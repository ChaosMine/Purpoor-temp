package net.minecraft.server;

import com.google.gson.JsonElement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;

public class jb {

    public static final jb.a a = a(iz::a, iy.c);
    public static final jb.a b = a(iz::a, iy.d);
    public static final jb.a c = a(iz::j, iy.e);
    public static final jb.a d = a(iz::j, iy.f);
    public static final jb.a e = a(iz::m, iy.h);
    public static final jb.a f = a(iz::k, iy.g);
    public static final jb.a g = a(iz::x, iy.i);
    public static final jb.a h = a(iz::w, iy.j);
    public static final jb.a i = a(iz::f, iy.aa);
    public static final jb.a j = a(iz::h, iy.ad);
    public static final jb.a k = a(iz::i, iy.ab);
    public static final jb.a l = a(iz::q, iy.F);
    public static final jb.a m = a(iz::z, iy.am);
    public static final jb.a n = a(iz::a, iy.I);
    public static final jb.a o = a(iz::t, iy.ax);
    public static final jb.a p = a(iz::t, iy.ay);
    public static final jb.a q = a(iz::b, iy.aE);
    public static final jb.a r = a(iz::l, iy.e);
    public static final jb.a s = a(iz::l, iy.f);
    public static final jb.a t = a(iz::n, iy.h);
    public static final jb.a u = a(iz::o, iy.e);
    private final iz v;
    private final ix w;

    private jb(iz iz, ix ix) {
        this.v = iz;
        this.w = ix;
    }

    public ix a() {
        return this.w;
    }

    public iz b() {
        return this.v;
    }

    public jb a(Consumer<iz> consumer) {
        consumer.accept(this.v);
        return this;
    }

    public MinecraftKey a(Block block, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
        return this.w.a(block, this.v, biconsumer);
    }

    public MinecraftKey a(Block block, String s, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
        return this.w.a(block, s, this.v, biconsumer);
    }

    private static jb.a a(Function<Block, iz> function, ix ix) {
        return (block) -> {
            return new jb((iz) function.apply(block), ix);
        };
    }

    public static jb a(MinecraftKey minecraftkey) {
        return new jb(iz.b(minecraftkey), iy.c);
    }

    @FunctionalInterface
    public interface a {

        jb get(Block block);

        default MinecraftKey a(Block block, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
            return this.get(block).a(block, biconsumer);
        }

        default MinecraftKey a(Block block, String s, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
            return this.get(block).a(block, s, biconsumer);
        }

        default jb.a a(Consumer<iz> consumer) {
            return (block) -> {
                return this.get(block).a(consumer);
            };
        }
    }
}
