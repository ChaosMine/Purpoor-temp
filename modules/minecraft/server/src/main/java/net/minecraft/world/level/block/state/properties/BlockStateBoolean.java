package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Optional;

public class BlockStateBoolean extends IBlockState<Boolean> {

    private final ImmutableSet<Boolean> a = ImmutableSet.of(true, false);

    protected BlockStateBoolean(String s) {
        super(s, Boolean.class);
    }

    // Tuinity start - optimise iblockdata state lookup
    @Override
    public final int getIdFor(final Boolean value) {
        return value.booleanValue() ? 1 : 0;
    }
    // Tuinity end - optimise iblockdata state lookup

    @Override
    public Collection<Boolean> getValues() {
        return this.a;
    }

    public static BlockStateBoolean of(String s) {
        return new BlockStateBoolean(s);
    }

    @Override
    public Optional<Boolean> b(String s) {
        return !"true".equals(s) && !"false".equals(s) ? Optional.empty() : Optional.of(Boolean.valueOf(s));
    }

    public String a(Boolean obool) {
        return obool.toString();
    }

    public boolean equals_unused(Object object) { // Paper
        if (this == object) {
            return true;
        } else if (object instanceof BlockStateBoolean && super.equals(object)) {
            BlockStateBoolean blockstateboolean = (BlockStateBoolean) object;

            return this.a.equals(blockstateboolean.a);
        } else {
            return false;
        }
    }

    @Override
    public int b() {
        return 31 * super.b() + this.a.hashCode();
    }
}
