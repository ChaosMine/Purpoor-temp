package net.minecraft.tags;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.Set;

public class TagSet<T> implements Tag<T> {

    private final List<T> b;
    private final Set<T> c;
    @VisibleForTesting
    protected final Class<?> a;

    protected TagSet(Set<T> set, Class<?> oclass) {
        this.a = oclass;
        this.c = set;
        this.b = List.copyOf(set);
    }

    protected TagSet(Class<?> oclass) {
        this.a = oclass;
        this.c = Set.of();
        this.b = List.of();
    }

    public static <T> TagSet<T> a() {
        return new TagSet<>(Void.class);
    }

    public static <T> TagSet<T> a(Set<T> set) {
        return new TagSet<>(set, c(set));
    }

    @Override
    public boolean isTagged(T t0) {
        return this.a.isInstance(t0) && this.c.contains(t0);
    }

    @Override
    public List<T> getTagged() {
        return this.b;
    }

    private static <T> Class<?> c(Set<T> set) {
        if (set.isEmpty()) {
            return Void.class;
        } else {
            Class<?> oclass = null;

            for (T t0 : set) {
                if (oclass == null) {
                    oclass = t0.getClass();
                } else {
                    oclass = a(oclass, t0.getClass());
                }
            }

            return oclass;
        }
    }

    private static Class<?> a(Class<?> oclass, Class<?> oclass1) {
        while (!oclass.isAssignableFrom(oclass1)) {
            oclass = oclass.getSuperclass();
        }

        return oclass;
    }
}
