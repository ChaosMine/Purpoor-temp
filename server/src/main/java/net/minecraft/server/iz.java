package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class iz {

    private final Map<ja, MinecraftKey> a = Maps.newHashMap();
    private final Set<ja> b = Sets.newHashSet();

    public iz() {}

    public iz a(ja ja, MinecraftKey minecraftkey) {
        this.a.put(ja, minecraftkey);
        return this;
    }

    public Stream<ja> a() {
        return this.b.stream();
    }

    public iz b(ja ja, ja ja1) {
        this.a.put(ja1, this.a.get(ja));
        this.b.add(ja1);
        return this;
    }

    public MinecraftKey a(ja ja) {
        for (ja ja1 = ja; ja1 != null; ja1 = ja1.b()) {
            MinecraftKey minecraftkey = (MinecraftKey) this.a.get(ja1);

            if (minecraftkey != null) {
                return minecraftkey;
            }
        }

        throw new IllegalStateException("Can't find texture for slot " + ja);
    }

    public iz c(ja ja, MinecraftKey minecraftkey) {
        iz iz = new iz();

        iz.a.putAll(this.a);
        iz.b.addAll(this.b);
        iz.a(ja, minecraftkey);
        return iz;
    }

    public static iz a(Block block) {
        MinecraftKey minecraftkey = C(block);

        return b(minecraftkey);
    }

    public static iz b(Block block) {
        MinecraftKey minecraftkey = C(block);

        return a(minecraftkey);
    }

    public static iz a(MinecraftKey minecraftkey) {
        return (new iz()).a(ja.b, minecraftkey);
    }

    public static iz b(MinecraftKey minecraftkey) {
        return (new iz()).a(ja.a, minecraftkey);
    }

    public static iz c(Block block) {
        return d(ja.p, C(block));
    }

    public static iz c(MinecraftKey minecraftkey) {
        return d(ja.p, minecraftkey);
    }

    public static iz d(Block block) {
        return d(ja.q, C(block));
    }

    public static iz d(MinecraftKey minecraftkey) {
        return d(ja.q, minecraftkey);
    }

    public static iz e(Block block) {
        return d(ja.s, C(block));
    }

    public static iz e(MinecraftKey minecraftkey) {
        return d(ja.s, minecraftkey);
    }

    public static iz f(Block block) {
        return d(ja.t, C(block));
    }

    public static iz g(Block block) {
        return d(ja.y, C(block));
    }

    public static iz a(Block block, Block block1) {
        return (new iz()).a(ja.y, C(block)).a(ja.z, C(block1));
    }

    public static iz h(Block block) {
        return d(ja.u, C(block));
    }

    public static iz i(Block block) {
        return d(ja.x, C(block));
    }

    public static iz g(MinecraftKey minecraftkey) {
        return d(ja.A, minecraftkey);
    }

    public static iz b(Block block, Block block1) {
        return (new iz()).a(ja.v, C(block)).a(ja.w, a(block1, "_top"));
    }

    public static iz d(ja ja, MinecraftKey minecraftkey) {
        return (new iz()).a(ja, minecraftkey);
    }

    public static iz j(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.d, a(block, "_top"));
    }

    public static iz k(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.f, a(block, "_top"));
    }

    public static iz l(Block block) {
        return (new iz()).a(ja.i, C(block)).a(ja.d, a(block, "_top"));
    }

    public static iz a(MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
        return (new iz()).a(ja.i, minecraftkey).a(ja.d, minecraftkey1);
    }

    public static iz m(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.f, a(block, "_top")).a(ja.e, a(block, "_bottom"));
    }

    public static iz n(Block block) {
        MinecraftKey minecraftkey = C(block);

        return (new iz()).a(ja.r, minecraftkey).a(ja.i, minecraftkey).a(ja.f, a(block, "_top")).a(ja.e, a(block, "_bottom"));
    }

    public static iz o(Block block) {
        MinecraftKey minecraftkey = C(block);

        return (new iz()).a(ja.r, minecraftkey).a(ja.i, minecraftkey).a(ja.d, a(block, "_top"));
    }

    public static iz p(Block block) {
        return (new iz()).a(ja.f, a(block, "_top")).a(ja.e, a(block, "_bottom"));
    }

    public static iz q(Block block) {
        return (new iz()).a(ja.c, C(block));
    }

    public static iz h(MinecraftKey minecraftkey) {
        return (new iz()).a(ja.c, minecraftkey);
    }

    public static iz r(Block block) {
        return (new iz()).a(ja.C, a(block, "_0"));
    }

    public static iz s(Block block) {
        return (new iz()).a(ja.C, a(block, "_1"));
    }

    public static iz t(Block block) {
        return (new iz()).a(ja.D, C(block));
    }

    public static iz u(Block block) {
        return (new iz()).a(ja.G, C(block));
    }

    public static iz i(MinecraftKey minecraftkey) {
        return (new iz()).a(ja.G, minecraftkey);
    }

    public static iz a(Item item) {
        return (new iz()).a(ja.c, c(item));
    }

    public static iz v(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.g, a(block, "_front")).a(ja.h, a(block, "_back"));
    }

    public static iz w(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.g, a(block, "_front")).a(ja.f, a(block, "_top")).a(ja.e, a(block, "_bottom"));
    }

    public static iz x(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.g, a(block, "_front")).a(ja.f, a(block, "_top"));
    }

    public static iz y(Block block) {
        return (new iz()).a(ja.i, a(block, "_side")).a(ja.g, a(block, "_front")).a(ja.d, a(block, "_end"));
    }

    public static iz z(Block block) {
        return (new iz()).a(ja.f, a(block, "_top"));
    }

    public static iz c(Block block, Block block1) {
        return (new iz()).a(ja.c, a(block, "_front")).a(ja.o, C(block1)).a(ja.n, a(block, "_top")).a(ja.j, a(block, "_front")).a(ja.l, a(block, "_side")).a(ja.k, a(block, "_side")).a(ja.m, a(block, "_front"));
    }

    public static iz d(Block block, Block block1) {
        return (new iz()).a(ja.c, a(block, "_front")).a(ja.o, C(block1)).a(ja.n, a(block, "_top")).a(ja.j, a(block, "_front")).a(ja.k, a(block, "_front")).a(ja.l, a(block, "_side")).a(ja.m, a(block, "_side"));
    }

    public static iz A(Block block) {
        return (new iz()).a(ja.I, a(block, "_log_lit")).a(ja.C, a(block, "_fire"));
    }

    public static iz b(Item item) {
        return (new iz()).a(ja.H, c(item));
    }

    public static iz B(Block block) {
        return (new iz()).a(ja.H, C(block));
    }

    public static iz j(MinecraftKey minecraftkey) {
        return (new iz()).a(ja.H, minecraftkey);
    }

    public static MinecraftKey C(Block block) {
        MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(block);

        return new MinecraftKey(minecraftkey.getNamespace(), "block/" + minecraftkey.getKey());
    }

    public static MinecraftKey a(Block block, String s) {
        MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(block);

        return new MinecraftKey(minecraftkey.getNamespace(), "block/" + minecraftkey.getKey() + s);
    }

    public static MinecraftKey c(Item item) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(item);

        return new MinecraftKey(minecraftkey.getNamespace(), "item/" + minecraftkey.getKey());
    }

    public static MinecraftKey a(Item item, String s) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(item);

        return new MinecraftKey(minecraftkey.getNamespace(), "item/" + minecraftkey.getKey() + s);
    }
}
