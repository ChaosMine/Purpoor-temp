package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.properties.IBlockState;

public final class iq {

    private static final iq a = new iq(ImmutableList.of());
    private static final Comparator<IBlockState.a<?>> b = Comparator.comparing((iblockstate_a) -> {
        return iblockstate_a.a().getName();
    });
    private final List<IBlockState.a<?>> c;

    public iq a(IBlockState.a<?> iblockstate_a) {
        return new iq(ImmutableList.builder().addAll(this.c).add(iblockstate_a).build());
    }

    public iq a(iq iq) {
        return new iq(ImmutableList.builder().addAll(this.c).addAll(iq.c).build());
    }

    private iq(List<IBlockState.a<?>> list) {
        this.c = list;
    }

    public static iq a() {
        return iq.a;
    }

    public static iq a(IBlockState.a<?>... aiblockstate_a) {
        return new iq(ImmutableList.copyOf(aiblockstate_a));
    }

    public boolean equals(Object object) {
        return this == object || object instanceof iq && this.c.equals(((iq) object).c);
    }

    public int hashCode() {
        return this.c.hashCode();
    }

    public String b() {
        return (String) this.c.stream().sorted(iq.b).map(IBlockState.a::toString).collect(Collectors.joining(","));
    }

    public String toString() {
        return this.b();
    }
}
