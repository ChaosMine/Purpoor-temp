package net.minecraft.world.level.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.material.FluidTypes;
import net.minecraft.world.level.pathfinder.PathMode;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import net.minecraft.world.phys.shapes.VoxelShapes;

import javax.annotation.Nullable;

public class BlockStepAbstract extends Block implements IBlockWaterlogged {

    public static final BlockStateEnum<BlockPropertySlabType> a = BlockProperties.aK; public static BlockStateEnum<BlockPropertySlabType> slabType() { return a; } // Purpur - OBFHELPER
    public static final BlockStateBoolean b = BlockProperties.C;
    protected static final VoxelShape c = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape d = Block.a(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public BlockStepAbstract(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j(this.getBlockData().set(BlockStepAbstract.a, BlockPropertySlabType.BOTTOM).set(BlockStepAbstract.b, false));
    }

    @Override
    public boolean c_(IBlockData iblockdata) {
        return iblockdata.get(BlockStepAbstract.a) != BlockPropertySlabType.DOUBLE;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockStepAbstract.a, BlockStepAbstract.b);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        BlockPropertySlabType blockpropertyslabtype = iblockdata.get(BlockStepAbstract.a);

        switch (blockpropertyslabtype) {
            case DOUBLE:
                return VoxelShapes.b();
            case TOP:
                return BlockStepAbstract.d;
            default:
                return BlockStepAbstract.c;
        }
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        IBlockData iblockdata = blockactioncontext.getWorld().getType(blockposition);

        if (iblockdata.a(this)) {
            return iblockdata.set(BlockStepAbstract.a, BlockPropertySlabType.DOUBLE).set(BlockStepAbstract.b, false);
        } else {
            Fluid fluid = blockactioncontext.getWorld().getFluid(blockposition);
            IBlockData iblockdata1 = this.getBlockData().set(BlockStepAbstract.a, BlockPropertySlabType.BOTTOM).set(BlockStepAbstract.b, fluid.getType() == FluidTypes.WATER);
            EnumDirection enumdirection = blockactioncontext.getClickedFace();

            return enumdirection != EnumDirection.DOWN && (enumdirection == EnumDirection.UP || blockactioncontext.getPos().y - (double) blockposition.getY() <= 0.5D) ? iblockdata1 : iblockdata1.set(BlockStepAbstract.a, BlockPropertySlabType.TOP);
        }
    }

    @Override
    public boolean a(IBlockData iblockdata, BlockActionContext blockactioncontext) {
        ItemStack itemstack = blockactioncontext.getItemStack();
        BlockPropertySlabType blockpropertyslabtype = iblockdata.get(BlockStepAbstract.a);

        if (blockpropertyslabtype != BlockPropertySlabType.DOUBLE && itemstack.getItem() == this.getItem()) {
            if (blockactioncontext.c()) {
                boolean flag = blockactioncontext.getPos().y - (double) blockactioncontext.getClickPosition().getY() > 0.5D;
                EnumDirection enumdirection = blockactioncontext.getClickedFace();

                return blockpropertyslabtype == BlockPropertySlabType.BOTTOM ? enumdirection == EnumDirection.UP || flag && enumdirection.n().d() : enumdirection == EnumDirection.DOWN || !flag && enumdirection.n().d();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public Fluid d(IBlockData iblockdata) {
        return iblockdata.get(BlockStepAbstract.b) ? FluidTypes.WATER.a(false) : super.d(iblockdata);
    }

    @Override
    public boolean place(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid) {
        return iblockdata.get(BlockStepAbstract.a) != BlockPropertySlabType.DOUBLE ? IBlockWaterlogged.super.place(generatoraccess, blockposition, iblockdata, fluid) : false;
    }

    @Override
    public boolean canPlace(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, FluidType fluidtype) {
        return iblockdata.get(BlockStepAbstract.a) != BlockPropertySlabType.DOUBLE ? IBlockWaterlogged.super.canPlace(iblockaccess, blockposition, iblockdata, fluidtype) : false;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (iblockdata.get(BlockStepAbstract.b)) {
            generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a(generatoraccess));
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
            case LAND:
                return false;
            case WATER:
                return iblockaccess.getFluid(blockposition).a(TagsFluid.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    // Purpur start
    public boolean halfBreak(IBlockData iblockdata, BlockPosition pos, net.minecraft.server.level.EntityPlayer player) {
        if (iblockdata.get(BlockStepAbstract.slabType()) != BlockPropertySlabType.DOUBLE) {
            return false;
        }
        net.minecraft.world.phys.MovingObjectPosition result = player.getRayTrace(16);
        if (result == null) {
            return false;
        }
        double hitY = result.getPos().getY();
        int blockY = org.bukkit.util.NumberConversions.floor(hitY);
        player.world.setTypeAndData(pos, iblockdata.set(BlockStepAbstract.slabType(), (hitY - blockY > 0.5 || blockY - pos.getY() == 1) ? BlockPropertySlabType.BOTTOM : BlockPropertySlabType.TOP), 3);
        if (!player.abilities.canInstantlyBuild) {
            net.minecraft.world.entity.item.EntityItem item = new net.minecraft.world.entity.item.EntityItem(player.world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getItem()));
            item.defaultPickupDelay();
            player.world.addEntity(item);
        }
        return true;
    }
    // Purpur end
}
