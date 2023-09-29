package net.minecraft.world.level.block;

import com.mojang.math.PointGroupO;
import net.minecraft.core.EnumDirection;
import org.jetbrains.annotations.Unmodifiable;

public enum EnumBlockMirror {
	NONE(PointGroupO.IDENTITY),
	LEFT_RIGHT(PointGroupO.INVERT_Z),
	FRONT_BACK(PointGroupO.INVERT_X),
	;

	public static final EnumBlockMirror @Unmodifiable [] VALUES = values();

	private final PointGroupO d;

	EnumBlockMirror(PointGroupO pointgroupo) {
		this.d = pointgroupo;
	}

	public int a(int i, int j) {
		int k = j / 2;
		int l = i > k ? i - j : i;

		return switch (this) {
			case FRONT_BACK -> (j - l) % j;
			case LEFT_RIGHT -> (k - l + j) % j;
			default -> i;
		};
	}

	public EnumBlockRotation a(EnumDirection enumdirection) {
		EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();

		var o = (this != EnumBlockMirror.LEFT_RIGHT || enumdirection_enumaxis != EnumDirection.EnumAxis.Z);
		var b = (this != EnumBlockMirror.FRONT_BACK || enumdirection_enumaxis != EnumDirection.EnumAxis.X);

		return o && b
			? EnumBlockRotation.NONE
			: EnumBlockRotation.CLOCKWISE_180;
	}

	public EnumDirection b(EnumDirection enumdirection) {
		return this == EnumBlockMirror.FRONT_BACK && enumdirection.n() == EnumDirection.EnumAxis.X ? enumdirection.opposite() : (this == EnumBlockMirror.LEFT_RIGHT && enumdirection.n() == EnumDirection.EnumAxis.Z ? enumdirection.opposite() : enumdirection);
	}

	public PointGroupO a() {
		return this.d;
	}
}
