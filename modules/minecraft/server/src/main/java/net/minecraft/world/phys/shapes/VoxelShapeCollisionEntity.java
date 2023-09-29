package net.minecraft.world.phys.shapes;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.material.FluidTypeFlowing;

import java.util.function.Predicate;

public class VoxelShapeCollisionEntity implements VoxelShapeCollision {
	protected static final VoxelShapeCollision a = new VoxelShapeCollisionEntity(false, -Double.MAX_VALUE, Items.AIR, var0 -> false) {
		@Override
		public boolean a(VoxelShape var0, BlockPosition var1, boolean var2) {
			return var2;
		}
	};
	private final boolean b;
	private final double c;
	private final Item d;
	private final Predicate<FluidType> e;

	protected VoxelShapeCollisionEntity(boolean var0, double var1, Item var3, Predicate<FluidType> var4) {
		this.b = var0;
		this.c = var1;
		this.d = var3;
		this.e = var4;
	}

	@Deprecated
	protected VoxelShapeCollisionEntity(Entity var0) {
		this(
			var0.by(),
			var0.locY(),
			var0 instanceof EntityLiving ? ((EntityLiving) var0).getItemInMainHand().getItem() : Items.AIR,
			var0 instanceof EntityLiving ? ((EntityLiving) var0)::a : var0x -> false
		);
	}

	@Override
	public boolean a(Item var0) {
		return this.d == var0;
	}

	@Override
	public boolean a(Fluid var0, FluidTypeFlowing var1) {
		return this.e.test(var1) && !var0.getType().a(var1);
	}

	@Override
	public boolean b() {
		return this.b;
	}

	@Override
	public boolean a(VoxelShape var0, BlockPosition var1, boolean var2) {
		return this.c > (double) var1.getY() + var0.c(EnumDirection.EnumAxis.Y) - 1.0E-5F;
	}
}
