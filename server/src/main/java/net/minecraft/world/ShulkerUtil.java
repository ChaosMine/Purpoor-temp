package net.minecraft.world;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.shapes.VoxelShapes;

public class ShulkerUtil {

    public static AxisAlignedBB a(BlockPosition blockposition, EnumDirection enumdirection) {
        return VoxelShapes.b().getBoundingBox().b(0.5F * (float) enumdirection.getAdjacentX(), 0.5F * (float) enumdirection.getAdjacentY(), 0.5F * (float) enumdirection.getAdjacentZ()).a(enumdirection.getAdjacentX(), enumdirection.getAdjacentY(), enumdirection.getAdjacentZ()).a(blockposition.shift(enumdirection));
    }
}
