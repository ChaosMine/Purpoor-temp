package com.tuinity.tuinity.util.table;

import com.google.common.collect.Table;
import net.minecraft.world.level.block.state.IBlockDataHolder;
import net.minecraft.world.level.block.state.properties.IBlockState;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ZeroCollidingReferenceStateTable {

    // upper 32 bits: starting index
    // lower 32 bits: bitset for contained ids
    protected final long[] this_index_table;
    protected final Comparable<?>[] this_table;
    protected final IBlockDataHolder<?, ?> this_state;

    protected long[] index_table;
    protected IBlockDataHolder<?, ?>[][] value_table;

    public ZeroCollidingReferenceStateTable(final IBlockDataHolder<?, ?> state, final Map<IBlockState<?>, Comparable<?>> this_map) {
        this.this_state = state;
        this.this_index_table = this.create_table(this_map.keySet());

        int max_id = -1;
        for (final IBlockState<?> property : this_map.keySet()) {
            final int id = lookup_vindex(property, this.this_index_table);
            if (id > max_id) {
                max_id = id;
            }
        }

        this.this_table = new Comparable[max_id + 1];
        for (final Map.Entry<IBlockState<?>, Comparable<?>> entry : this_map.entrySet()) {
            this.this_table[lookup_vindex(entry.getKey(), this.this_index_table)] = entry.getValue();
        }
    }

    public void loadInTable(final Table<IBlockState<?>, Comparable<?>, IBlockDataHolder<?, ?>> table,
                            final Map<IBlockState<?>, Comparable<?>> this_map) {
        final Set<IBlockState<?>> combined = new HashSet<>(table.rowKeySet());
        combined.addAll(this_map.keySet());

        this.index_table = this.create_table(combined);

        int max_id = -1;
        for (final IBlockState<?> property : combined) {
            final int id = lookup_vindex(property, this.index_table);
            if (id > max_id) {
                max_id = id;
            }
        }

        this.value_table = new IBlockDataHolder[max_id + 1][];

        final Map<IBlockState<?>, Map<Comparable<?>, IBlockDataHolder<?, ?>>> map = table.rowMap();
        for (final IBlockState<?> property : map.keySet()) {
            final Map<Comparable<?>, IBlockDataHolder<?, ?>> propertyMap = map.get(property);

            final int id = lookup_vindex(property, this.index_table);
            final IBlockDataHolder<?, ?>[] states = this.value_table[id] = new IBlockDataHolder[property.getValues().size()];

            for (final Map.Entry<Comparable<?>, IBlockDataHolder<?, ?>> entry : propertyMap.entrySet()) {
                if (entry.getValue() == null) {
                    // TODO what
                    continue;
                }

                states[((IBlockState)property).getIdFor(entry.getKey())] = entry.getValue();
            }
        }


        for (final Map.Entry<IBlockState<?>, Comparable<?>> entry : this_map.entrySet()) {
            final IBlockState<?> property = entry.getKey();
            final int index = lookup_vindex(property, this.index_table);

            if (this.value_table[index] == null) {
                this.value_table[index] = new IBlockDataHolder[property.getValues().size()];
            }

            this.value_table[index][((IBlockState)property).getIdFor(entry.getValue())] = this.this_state;
        }
    }


    protected long[] create_table(final Collection<IBlockState<?>> collection) {
        int max_id = -1;
        for (final IBlockState<?> property : collection) {
            final int id = property.getId();
            if (id > max_id) {
                max_id = id;
            }
        }

        final long[] ret = new long[((max_id + 1) + 31) >>> 5]; // ceil((max_id + 1) / 32)

        for (final IBlockState<?> property : collection) {
            final int id = property.getId();

            ret[id >>> 5] |= (1L << (id & 31));
        }

        int total = 0;
        for (int i = 1, len = ret.length; i < len; ++i) {
            ret[i] |= (long)(total += Long.bitCount(ret[i - 1] & 0xFFFFFFFFL)) << 32;
        }

        return ret;
    }

    public Comparable<?> get(final IBlockState<?> state) {
        final Comparable<?>[] table = this.this_table;
        final int index = lookup_vindex(state, this.this_index_table);

        if (index < 0 || index >= table.length) {
            return null;
        }
        return table[index];
    }

    public IBlockDataHolder<?, ?> get(final IBlockState<?> property, final Comparable<?> with) {
        final int withId = ((IBlockState)property).getIdFor(with);
        if (withId < 0) {
            return null;
        }

        final int index = lookup_vindex(property, this.index_table);
        final IBlockDataHolder<?, ?>[][] table = this.value_table;
        if (index < 0 || index >= table.length) {
            return null;
        }

        final IBlockDataHolder<?, ?>[] values = table[index];

        if (withId >= values.length) {
            return null;
        }

        return values[withId];
    }

    protected static int lookup_vindex(final IBlockState<?> property, final long[] index_table) {
        final int id = property.getId();
        final long bitset_mask = (1L << (id & 31));
        final long lower_mask = bitset_mask - 1;
        final int index = id >>> 5;
        if (index >= index_table.length) {
            return -1;
        }
        final long index_value = index_table[index];
        final long contains_check = ((index_value & bitset_mask) - 1) >> (Long.SIZE - 1); // -1L if doesn't contain

        // index = total bits set in lower table values (upper 32 bits of index_value) plus total bits set in lower indices below id
        // contains_check is 0 if the bitset had id set, else it's -1: so index is unaffected if contains_check == 0,
        // otherwise it comes out as -1.
        return (int)(((index_value >>> 32) + Long.bitCount(index_value & lower_mask)) | contains_check);
    }
}
