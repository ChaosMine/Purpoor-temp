package net.minecraft.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySlice<T> extends AbstractCollection<T> {

    private final Map<Class<?>, List<T>> a = Maps.newHashMap();
    private final Class<T> b;
    private final List<T> c = Lists.newArrayList();

    public EntitySlice(Class<T> oclass) {
        this.b = oclass;
        this.a.put(oclass, this.c);
    }

    public boolean add(T t0) {
        boolean flag = false;
        Iterator iterator = this.a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Class<?>, List<T>> entry = (Entry) iterator.next();

            if (entry.getKey().isInstance(t0)) {
                flag |= entry.getValue().add(t0);
            }
        }

        return flag;
    }

    public boolean remove(Object object) {
        boolean flag = false;

			for (Entry<Class<?>, List<T>> classListEntry : this.a.entrySet()) {
				if (classListEntry.getKey().isInstance(object)) {
					List<T> list = classListEntry.getValue();

					flag |= list.remove(object);
				}
			}

        return flag;
    }

    public boolean contains(Object object) {
        return this.a(object.getClass()).contains(object);
    }

    public <S> Collection<S> a(Class<S> oclass) {
        if (!this.b.isAssignableFrom(oclass)) {
            throw new IllegalArgumentException("Don't know how to search for " + oclass);
        } else {
					return (Collection<S>) this.a.computeIfAbsent(oclass, (oclass1) -> {
                return this.c.stream().filter(oclass1::isInstance).toList();
            });
        }
    }

    public Iterator<T> iterator() {
        return this.c.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.c.iterator());
    }

    public List<T> a() {
        return ImmutableList.copyOf(this.c);
    }

    public int size() {
        return this.c.size();
    }
}
