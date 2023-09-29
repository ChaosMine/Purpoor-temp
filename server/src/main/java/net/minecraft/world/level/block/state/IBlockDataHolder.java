package net.minecraft.world.level.block.state;

import com.google.common.collect.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.properties.IBlockState;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class IBlockDataHolder<O, S> {

    public static final Function<Entry<IBlockState<?>, Comparable<?>>, String> STATE_TO_VALUE = new Function<Entry<IBlockState<?>, Comparable<?>>, String>() {
        public String apply(@Nullable Entry<IBlockState<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            } else {
                IBlockState<?> iblockstate = entry.getKey();

                return iblockstate.getName() + "=" + this.a((IBlockState) iblockstate, (Comparable) entry.getValue()); // Paper - decompile fix
            }
        }

        private <T extends Comparable<T>> String a(IBlockState<T> iblockstate, T comparable) { // Paper - decompile error
            return iblockstate.a(comparable);
        }
    };
    protected final O c;
    private final ImmutableMap<IBlockState<?>, Comparable<?>> b;
    private Table<IBlockState<?>, Comparable<?>, S> e;
    protected final MapCodec<S> d;
    protected com.tuinity.tuinity.util.table.ZeroCollidingReferenceStateTable optimisedTable; // Tuinity - optimise state lookup

    protected IBlockDataHolder(O o0, ImmutableMap<IBlockState<?>, Comparable<?>> immutablemap, MapCodec<S> mapcodec) {
        this.c = o0;
        this.b = immutablemap;
        this.d = mapcodec;
        this.optimisedTable = new com.tuinity.tuinity.util.table.ZeroCollidingReferenceStateTable(this, immutablemap); // Tuinity - optimise state lookup
    }

    public <T extends Comparable<T>> S a(IBlockState<T> iblockstate) {
        return this.set(iblockstate, a(iblockstate.getValues(), this.get(iblockstate))); // Paper - decompile error
    }

    protected static <T> T a(Collection<T> collection, T t0) {
        Iterator<T> iterator = collection.iterator(); // Paper

        do {
            if (!iterator.hasNext()) {
                return iterator.next();
            }
        } while (!iterator.next().equals(t0));

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return collection.iterator().next();
        }
    }

    public String toString() {
        StringBuilder stringbuilder = dev.kamillaova.paper.pool.StringBuilderPool.take();

        stringbuilder.append(this.c);
        if (!this.getStateMap().isEmpty()) {
            stringbuilder.append('[');
            stringbuilder.append(this.getStateMap().entrySet().stream().map(IBlockDataHolder.STATE_TO_VALUE).collect(Collectors.joining(",")));
            stringbuilder.append(']');
        }

        return stringbuilder.toString();
    }

    public Collection<IBlockState<?>> r() {
        return Collections.unmodifiableCollection(this.b.keySet());
    }

    public <T extends Comparable<T>> boolean contains(IBlockState<T> iblockstate) { return this.b(iblockstate); } // Paper - OBFHELPER
    public <T extends Comparable<T>> boolean b(IBlockState<T> iblockstate) {
        return this.optimisedTable.get(iblockstate) != null; // Tuinity - optimise state lookup
    }

    public <T extends Comparable<T>> T get(IBlockState<T> iblockstate) {
        final Comparable<?> comparable = this.optimisedTable.get(iblockstate); // Tuinity - optimise state lookup

        if (comparable == null) {
            throw new IllegalArgumentException("Cannot get property " + iblockstate + " as it does not exist in " + this.c);
        } else {
            return iblockstate.getType().cast(comparable); // Paper - decompile error
        }
    }

    public <T extends Comparable<T>> Optional<T> d(IBlockState<T> iblockstate) {
        final Comparable<?> comparable = this.optimisedTable.get(iblockstate); // Tuinity - optimise state lookup

        return comparable == null ? Optional.empty() : Optional.of(iblockstate.getType().cast(comparable));
    }

    public <T extends Comparable<T>, V extends T> S set(IBlockState<T> iblockstate, V v0) {
        // Tuinity start - optimise state lookup
        final S ret = (S)this.optimisedTable.get(iblockstate, v0);

        if (ret == null) {
            throw new IllegalArgumentException("Cannot set property " + iblockstate + " to " + v0 + " on " + this.c + ", it is not an allowed value");
        }

        return ret;
        // Tuinity end - optimise state lookup
    }

    public void a(Map<Map<IBlockState<?>, Comparable<?>>, S> map) {
        if (this.e != null) {
            throw new IllegalStateException();
        } else {
            Table<IBlockState<?>, Comparable<?>, S> table = HashBasedTable.create();
            UnmodifiableIterator unmodifiableiterator = this.b.entrySet().iterator();

            while (unmodifiableiterator.hasNext()) {
                Entry<IBlockState<?>, Comparable<?>> entry = (Entry) unmodifiableiterator.next();
                IBlockState<?> iblockstate = entry.getKey();
                Iterator iterator = iblockstate.getValues().iterator();

                while (iterator.hasNext()) {
                    Comparable<?> comparable = (Comparable) iterator.next();

                    if (comparable != entry.getValue()) {
                        table.put(iblockstate, comparable, map.get(this.b(iblockstate, comparable)));
                    }
                }
            }

            this.e = table.isEmpty() ? table : ArrayTable.create(table); this.optimisedTable.loadInTable((Table)this.e, this.b); // Tuinity - optimise state lookup

        }
    }

    private Map<IBlockState<?>, Comparable<?>> b(IBlockState<?> iblockstate, Comparable<?> comparable) {
        Map<IBlockState<?>, Comparable<?>> map = Maps.newHashMap(this.b);

        map.put(iblockstate, comparable);
        return map;
    }

    public ImmutableMap<IBlockState<?>, Comparable<?>> getStateMap() {
        return this.b;
    }

    protected static <O, S extends IBlockDataHolder<O, S>> Codec<S> a(Codec<O> codec, Function<O, S> function) {
        return codec.dispatch("Name", (iblockdataholder) -> {
            return iblockdataholder.c;
        }, (object) -> {
            S s0 = function.apply(object); // Paper - decompile error

            return s0.getStateMap().isEmpty() ? Codec.unit(s0) : s0.d.fieldOf("Properties").codec();
        });
    }
}
