package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.IBlockState;

public abstract class ip {

    private final Map<iq, List<ir>> a = Maps.newHashMap();

    public ip() {}

    protected void a(iq iq, List<ir> list) {
        List<ir> list1 = (List) this.a.put(iq, list);

        if (list1 != null) {
            throw new IllegalStateException("Value " + iq + " is already defined");
        }
    }

    Map<iq, List<ir>> a() {
        this.c();
        return ImmutableMap.copyOf(this.a);
    }

    private void c() {
        List<IBlockState<?>> list = this.b();
        Stream<iq> stream = Stream.of(iq.a());

        IBlockState iblockstate;

        for (Iterator iterator = list.iterator(); iterator.hasNext();stream = stream.flatMap((iq) -> {
            Stream stream1 = iblockstate.c();

            iq.getClass();
            return stream1.map(iq::a);
        })) {
            iblockstate = (IBlockState) iterator.next();
        }

        List<iq> list1 = (List) stream.filter((iq) -> {
            return !this.a.containsKey(iq);
        }).collect(Collectors.toList());

        if (!list1.isEmpty()) {
            throw new IllegalStateException("Missing definition for properties: " + list1);
        }
    }

    abstract List<IBlockState<?>> b();

    public static <T1 extends Comparable<T1>> ip.a<T1> a(IBlockState<T1> iblockstate) {
        return new ip.a<>(iblockstate);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> ip.b<T1, T2> a(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1) {
        return new ip.b<>(iblockstate, iblockstate1);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> ip.c<T1, T2, T3> a(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2) {
        return new ip.c<>(iblockstate, iblockstate1, iblockstate2);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> ip.d<T1, T2, T3, T4> a(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2, IBlockState<T4> iblockstate3) {
        return new ip.d<>(iblockstate, iblockstate1, iblockstate2, iblockstate3);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> ip.e<T1, T2, T3, T4, T5> a(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2, IBlockState<T4> iblockstate3, IBlockState<T5> iblockstate4) {
        return new ip.e<>(iblockstate, iblockstate1, iblockstate2, iblockstate3, iblockstate4);
    }

    @FunctionalInterface
    public interface h<P1, P2, P3, R> {

        R apply(P1 p1, P2 p2, P3 p3);
    }

    public static class e<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends ip {

        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;
        private final IBlockState<T4> d;
        private final IBlockState<T5> e;

        private e(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2, IBlockState<T4> iblockstate3, IBlockState<T5> iblockstate4) {
            this.a = iblockstate;
            this.b = iblockstate1;
            this.c = iblockstate2;
            this.d = iblockstate3;
            this.e = iblockstate4;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c, this.d, this.e);
        }

        public ip.e<T1, T2, T3, T4, T5> a(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, List<ir> list) {
            iq iq = iq.a(this.a.b(t1), this.b.b(t2), this.c.b(t3), this.d.b(t4), this.e.b(t5));

            this.a(iq, list);
            return this;
        }

        public ip.e<T1, T2, T3, T4, T5> a(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, ir ir) {
            return this.a(t1, t2, t3, t4, t5, Collections.singletonList(ir));
        }
    }

    public static class d<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends ip {

        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;
        private final IBlockState<T4> d;

        private d(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2, IBlockState<T4> iblockstate3) {
            this.a = iblockstate;
            this.b = iblockstate1;
            this.c = iblockstate2;
            this.d = iblockstate3;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c, this.d);
        }

        public ip.d<T1, T2, T3, T4> a(T1 t1, T2 t2, T3 t3, T4 t4, List<ir> list) {
            iq iq = iq.a(this.a.b(t1), this.b.b(t2), this.c.b(t3), this.d.b(t4));

            this.a(iq, list);
            return this;
        }

        public ip.d<T1, T2, T3, T4> a(T1 t1, T2 t2, T3 t3, T4 t4, ir ir) {
            return this.a(t1, t2, t3, t4, Collections.singletonList(ir));
        }
    }

    public static class c<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends ip {

        private final IBlockState<T1> a;
        private final IBlockState<T2> b;
        private final IBlockState<T3> c;

        private c(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1, IBlockState<T3> iblockstate2) {
            this.a = iblockstate;
            this.b = iblockstate1;
            this.c = iblockstate2;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b, this.c);
        }

        public ip.c<T1, T2, T3> a(T1 t1, T2 t2, T3 t3, List<ir> list) {
            iq iq = iq.a(this.a.b(t1), this.b.b(t2), this.c.b(t3));

            this.a(iq, list);
            return this;
        }

        public ip.c<T1, T2, T3> a(T1 t1, T2 t2, T3 t3, ir ir) {
            return this.a(t1, t2, t3, Collections.singletonList(ir));
        }

        public ip a(ip.h<T1, T2, T3, ir> ip_h) {
            this.a.getValues().forEach((comparable) -> {
                this.b.getValues().forEach((comparable1) -> {
                    this.c.getValues().forEach((comparable2) -> {
                        this.a(comparable, comparable1, comparable2, (ir) ip_h.apply(comparable, comparable1, comparable2));
                    });
                });
            });
            return this;
        }
    }

    public static class b<T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends ip {

        private final IBlockState<T1> a;
        private final IBlockState<T2> b;

        private b(IBlockState<T1> iblockstate, IBlockState<T2> iblockstate1) {
            this.a = iblockstate;
            this.b = iblockstate1;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a, this.b);
        }

        public ip.b<T1, T2> a(T1 t1, T2 t2, List<ir> list) {
            iq iq = iq.a(this.a.b(t1), this.b.b(t2));

            this.a(iq, list);
            return this;
        }

        public ip.b<T1, T2> a(T1 t1, T2 t2, ir ir) {
            return this.a(t1, t2, Collections.singletonList(ir));
        }

        public ip a(BiFunction<T1, T2, ir> bifunction) {
            this.a.getValues().forEach((comparable) -> {
                this.b.getValues().forEach((comparable1) -> {
                    this.a(comparable, comparable1, (ir) bifunction.apply(comparable, comparable1));
                });
            });
            return this;
        }

        public ip b(BiFunction<T1, T2, List<ir>> bifunction) {
            this.a.getValues().forEach((comparable) -> {
                this.b.getValues().forEach((comparable1) -> {
                    this.a(comparable, comparable1, (List) bifunction.apply(comparable, comparable1));
                });
            });
            return this;
        }
    }

    public static class a<T1 extends Comparable<T1>> extends ip {

        private final IBlockState<T1> a;

        private a(IBlockState<T1> iblockstate) {
            this.a = iblockstate;
        }

        @Override
        public List<IBlockState<?>> b() {
            return ImmutableList.of(this.a);
        }

        public ip.a<T1> a(T1 t1, List<ir> list) {
            iq iq = iq.a(this.a.b(t1));

            this.a(iq, list);
            return this;
        }

        public ip.a<T1> a(T1 t1, ir ir) {
            return this.a(t1, Collections.singletonList(ir));
        }

        public ip a(Function<T1, ir> function) {
            this.a.getValues().forEach((comparable) -> {
                this.a(comparable, (ir) function.apply(comparable));
            });
            return this;
        }
    }
}
