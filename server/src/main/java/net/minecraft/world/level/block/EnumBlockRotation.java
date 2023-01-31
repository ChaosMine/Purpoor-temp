package net.minecraft.world.level.block;

import com.google.common.collect.Lists;
import com.mojang.math.PointGroupO;
import net.minecraft.SystemUtils;
import net.minecraft.core.EnumDirection;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EnumBlockRotation {
	NONE(PointGroupO.IDENTITY),
	CLOCKWISE_90(PointGroupO.ROT_90_Y_NEG),
	CLOCKWISE_180(PointGroupO.ROT_180_FACE_XZ),
	COUNTERCLOCKWISE_90(PointGroupO.ROT_90_Y_POS),
	;

	public static final EnumBlockRotation @Unmodifiable [] VALUES = values();

	private final PointGroupO e;

	EnumBlockRotation(PointGroupO pointgroupo) {
		this.e = pointgroupo;
	}

	public EnumBlockRotation a(EnumBlockRotation enumblockrotation) {
		return switch (enumblockrotation) {
			case CLOCKWISE_180 -> switch (this) {
				case NONE -> EnumBlockRotation.CLOCKWISE_180;
				case CLOCKWISE_90 -> EnumBlockRotation.COUNTERCLOCKWISE_90;
				case CLOCKWISE_180 -> EnumBlockRotation.NONE;
				case COUNTERCLOCKWISE_90 -> EnumBlockRotation.CLOCKWISE_90;
			};
			case COUNTERCLOCKWISE_90 -> switch (this) {
				case NONE -> EnumBlockRotation.COUNTERCLOCKWISE_90;
				case CLOCKWISE_90 -> EnumBlockRotation.NONE;
				case CLOCKWISE_180 -> EnumBlockRotation.CLOCKWISE_90;
				case COUNTERCLOCKWISE_90 -> EnumBlockRotation.CLOCKWISE_180;
			};
			case CLOCKWISE_90 -> switch (this) {
				case NONE -> EnumBlockRotation.CLOCKWISE_90;
				case CLOCKWISE_90 -> EnumBlockRotation.CLOCKWISE_180;
				case CLOCKWISE_180 -> EnumBlockRotation.COUNTERCLOCKWISE_90;
				case COUNTERCLOCKWISE_90 -> EnumBlockRotation.NONE;
			};
			default -> this;
		};
	}

	public PointGroupO a() {
		return this.e;
	}

	public EnumDirection a(EnumDirection enumdirection) {
		if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
			return enumdirection;
		} else {
			return switch (this) {
				case CLOCKWISE_90 -> enumdirection.g();
				case CLOCKWISE_180 -> enumdirection.opposite();
				case COUNTERCLOCKWISE_90 -> enumdirection.h();
				default -> enumdirection;
			};
		}
	}

	public int a(int i, int j) {
		return switch (this) {
			case CLOCKWISE_90 -> (i + j / 4) % j;
			case CLOCKWISE_180 -> (i + j / 2) % j;
			case COUNTERCLOCKWISE_90 -> (i + j * 3 / 4) % j;
			default -> i;
		};
	}

	public static EnumBlockRotation a(Random random) {
		return (EnumBlockRotation) SystemUtils.a((Object[]) values(), random);
	}

	public static List<EnumBlockRotation> b(Random random) {
		List<EnumBlockRotation> list = Lists.newArrayList(values());

		Collections.shuffle(list, random);
		return list;
	}
}
