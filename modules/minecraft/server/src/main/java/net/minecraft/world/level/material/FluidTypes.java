package net.minecraft.world.level.material;

import net.minecraft.core.IRegistry;

public class FluidTypes {
	public static final FluidType EMPTY = a("empty", new FluidTypeEmpty());
	public static final FluidTypeFlowing FLOWING_WATER = a("flowing_water", new FluidTypeWater.a());
	public static final FluidTypeFlowing WATER = a("water", new FluidTypeWater.b());
	public static final FluidTypeFlowing FLOWING_LAVA = a("flowing_lava", new FluidTypeLava.a());
	public static final FluidTypeFlowing LAVA = a("lava", new FluidTypeLava.b());

	static {
		for (FluidType fluidtype : IRegistry.FLUID) {
			for (Fluid fluid : fluidtype.g().a()) {
				FluidType.c.b(fluid);
			}
		}
	}

	private static <T extends FluidType> T a(String s, T t0) {
		return IRegistry.a(IRegistry.FLUID, s, t0);
	}
}
