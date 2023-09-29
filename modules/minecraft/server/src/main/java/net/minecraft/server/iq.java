package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.properties.IBlockState;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class iq {
	private static final iq a = new iq(ImmutableList.of());
	private static final Comparator<IBlockState.a<?>> b = Comparator.comparing(var0 -> var0.a().getName());
	private final List<IBlockState.a<?>> c;

	private iq(List<IBlockState.a<?>> var0) {
		this.c = var0;
	}

	public static iq a() {
		return a;
	}

	public static iq a(IBlockState.a<?>... var0) {
		return new iq(ImmutableList.copyOf(var0));
	}

	public iq a(IBlockState.a<?> var0) {
		return new iq((List) ImmutableList.builder().addAll(this.c).add(var0).build());
	}

	public iq a(iq var0) {
		return new iq((List) ImmutableList.builder().addAll(this.c).addAll(var0.c).build());
	}

	public boolean equals(Object var0) {
		return this == var0 || var0 instanceof iq && this.c.equals(((iq) var0).c);
	}

	public int hashCode() {
		return this.c.hashCode();
	}

	public String b() {
		return this.c.stream().sorted(b).map(IBlockState.a::toString).collect(Collectors.joining(","));
	}

	public String toString() {
		return this.b();
	}
}
