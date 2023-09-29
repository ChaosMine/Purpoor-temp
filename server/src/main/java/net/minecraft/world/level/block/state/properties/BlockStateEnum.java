package net.minecraft.world.level.block.state.properties;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.INamable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BlockStateEnum<T extends Enum<T> & INamable> extends IBlockState<T> {

    private final ImmutableSet<T> a;
    private final Map<String, T> b = Maps.newHashMap();

    // Tuinity start - optimise iblockdata state lookup
    private int[] idLookupTable;

    @Override
    public final int getIdFor(final T value) {
        return this.idLookupTable[value.ordinal()];
    }
    // Tuinity end - optimise iblockdata state lookup

    protected BlockStateEnum(String s, Class<T> oclass, Collection<T> collection) {
        super(s, oclass);
        this.a = ImmutableSet.copyOf(collection);
        Iterator<T> iterator = collection.iterator(); // Paper - decompile fix

        while (iterator.hasNext()) {
            T t0 = iterator.next(); // Paper - Decompile fix
            String s1 = t0.getName();

            if (this.b.containsKey(s1)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + s1 + "'");
            }

            this.b.put(s1, t0);
        }
        // Tuinity start - optimise iblockdata state lookup
        int id = 0;
        this.idLookupTable = new int[oclass.getEnumConstants().length];
        java.util.Arrays.fill(this.idLookupTable, -1);
        for (final T value : this.getValues()) {
            this.idLookupTable[value.ordinal()] = id++;
        }
        // Tuinity end - optimise iblockdata state lookup

    }

    @Override
    public Collection<T> getValues() {
        return this.a;
    }

    @Override
    public Optional<T> b(String s) {
        return Optional.ofNullable(this.b.get(s));
    }

    public String a(T t0) {
        return t0.getName();
    }

    public boolean equals_unused(Object object) { // Paper
        if (this == object) {
            return true;
        } else if (object instanceof BlockStateEnum && super.equals(object)) {
            BlockStateEnum<?> blockstateenum = (BlockStateEnum) object;

            return this.a.equals(blockstateenum.a) && this.b.equals(blockstateenum.b);
        } else {
            return false;
        }
    }

    @Override
    public int b() {
        int i = super.b();

        i = 31 * i + this.a.hashCode();
        i = 31 * i + this.b.hashCode();
        return i;
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String s, Class<T> oclass) {
        return a(s, oclass, Predicates.alwaysTrue());
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String s, Class<T> oclass, Predicate<T> predicate) {
        return a(s, oclass, Arrays.stream(oclass.getEnumConstants()).filter(predicate).toList());
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String s, Class<T> oclass, T... at) {
        return a(s, oclass, Lists.newArrayList(at));
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String s, Class<T> oclass, Collection<T> collection) {
        return new BlockStateEnum<>(s, oclass, collection);
    }
}
