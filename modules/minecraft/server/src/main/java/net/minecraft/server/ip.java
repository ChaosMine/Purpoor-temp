package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.IBlockState;

public abstract class ip {
    private final Map<iq, List<ir>> a = Maps.newHashMap();

    protected void a(iq var0, List<ir> var1) {
        List<ir> var2 = this.a.put(var0, var1);
        if (var2 != null) {
            throw new IllegalStateException("Value " + var0 + " is already defined");
        }
    }

    Map<iq, List<ir>> a() {
        this.c();
        return ImmutableMap.copyOf(this.a);
    }

    private void c() {
        List<IBlockState<?>> var02 = this.b();
        Stream<iq> var12 = Stream.of(iq.a());
        for (IBlockState<?> var3 : var02) {
            var12 = var12.flatMap(var1 -> var3.c().map(var1::a));
        }
        var var2 = var12.filter(var0 -> !this.a.containsKey(var0)).toList();
        if (!var2.isEmpty()) {
            throw new IllegalStateException("Missing definition for properties: " + var2);
        }
    }

    abstract List<IBlockState<?>> b();

    public static <T1 extends Comparable<T1>> a<T1> a(IBlockState<T1> var0) {
        return new a<>(var0);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> b<T1, T2> a(IBlockState<T1> var0, IBlockState<T2> var1) {
        return new b<>(var0, var1);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> c<T1, T2, T3> a(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2) {
        return new c<>(var0, var1, var2);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> d<T1, T2, T3, T4> a(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2, IBlockState<T4> var3) {
        return new d<>(var0, var1, var2, var3);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> e<T1, T2, T3, T4, T5> a(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2, IBlockState<T4> var3, IBlockState<T5> var4) {
        return new e<>(var0, var1, var2, var3, var4);
    }

    @FunctionalInterface
    public interface h<P1, P2, P3, R> {
        R apply(P1 var1, P2 var2, P3 var3);
    }

    public static class e<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>>
      extends ip {
        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;
        private final IBlockState<T4> d;
        private final IBlockState<T5> e;

        private e(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2, IBlockState<T4> var3, IBlockState<T5> var4) {
            this.a = var0;
            this.b = var1;
            this.c = var2;
            this.d = var3;
            this.e = var4;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c, this.d, this.e);
        }

        public e<T1, T2, T3, T4, T5> a(T1 var0, T2 var1, T3 var2, T4 var3, T5 var4, List<ir> var5) {
            iq var6 = iq.a(this.a.b(var0), this.b.b(var1), this.c.b(var2), this.d.b(var3), this.e.b(var4));
            this.a(var6, var5);
            return this;
        }

        public e<T1, T2, T3, T4, T5> a(T1 var0, T2 var1, T3 var2, T4 var3, T5 var4, ir var5) {
            return this.a(var0, var1, var2, var3, var4, Collections.singletonList(var5));
        }
    }

    public static class d<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>>
      extends ip {
        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;
        private final IBlockState<T4> d;

        private d(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2, IBlockState<T4> var3) {
            this.a = var0;
            this.b = var1;
            this.c = var2;
            this.d = var3;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c, this.d);
        }

        public d<T1, T2, T3, T4> a(T1 var0, T2 var1, T3 var2, T4 var3, List<ir> var4) {
            iq var5 = iq.a(this.a.b(var0), this.b.b(var1), this.c.b(var2), this.d.b(var3));
            this.a(var5, var4);
            return this;
        }

        public d<T1, T2, T3, T4> a(T1 var0, T2 var1, T3 var2, T4 var3, ir var4) {
            return this.a(var0, var1, var2, var3, Collections.singletonList(var4));
        }
    }

    public static class c<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>>
      extends ip {
        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;

        private c(IBlockState<T1> var0, IBlockState<T2> var1, IBlockState<T3> var2) {
            this.a = var0;
            this.b = var1;
            this.c = var2;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c);
        }

        public c<T1, T2, T3> a(T1 var0, T2 var1, T3 var2, List<ir> var3) {
            iq var4 = iq.a(this.a.b(var0), this.b.b(var1), this.c.b(var2));
            this.a(var4, var3);
            return this;
        }

        public c<T1, T2, T3> a(T1 var0, T2 var1, T3 var2, ir var3) {
            return this.a(var0, var1, var2, Collections.singletonList(var3));
        }

        public ip a(h<T1, T2, T3, ir> var0) {
            this.a.getValues().forEach(var1 -> this.b.getValues().forEach(var2 -> this.c.getValues().forEach(var3 -> this.a(var1, var2, var3, var0.apply(var1, var2, var3)))));
            return this;
        }
    }

    public static class b<T1 extends Comparable<T1>, T2 extends Comparable<T2>>
      extends ip {
        private final IBlockState<T1> a;
        private final IBlockState<T2> b;

        private b(IBlockState<T1> var0, IBlockState<T2> var1) {
            this.a = var0;
            this.b = var1;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b);
        }

        public b<T1, T2> a(T1 var0, T2 var1, List<ir> var2) {
            iq var3 = iq.a(this.a.b(var0), this.b.b(var1));
            this.a(var3, var2);
            return this;
        }

        public b<T1, T2> a(T1 var0, T2 var1, ir var2) {
            return this.a(var0, var1, Collections.singletonList(var2));
        }

        public ip a(BiFunction<T1, T2, ir> var0) {
            this.a.getValues().forEach(var1 -> this.b.getValues().forEach(var2 -> this.a(var1, var2, var0.apply(var1, var2))));
            return this;
        }

        public ip b(BiFunction<T1, T2, List<ir>> var0) {
            this.a.getValues().forEach(var1 -> this.b.getValues().forEach(var2 -> this.a(var1, var2, var0.apply(var1, var2))));
            return this;
        }
    }

    public static class a<T1 extends Comparable<T1>>
      extends ip {
        private final IBlockState<T1> a;

        private a(IBlockState<T1> var0) {
            this.a = var0;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a);
        }

        public a<T1> a(T1 var0, List<ir> var1) {
            iq var2 = iq.a(new IBlockState.a[]{this.a.b(var0)});
            this.a(var2, var1);
            return this;
        }

        public a<T1> a(T1 var0, ir var1) {
            return this.a(var0, Collections.singletonList(var1));
        }

        public ip a(Function<T1, ir> var0) {
            this.a.getValues().forEach(var1 -> this.a(var1, var0.apply(var1)));
            return this;
        }
    }
}
