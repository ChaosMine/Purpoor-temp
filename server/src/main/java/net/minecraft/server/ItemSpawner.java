package net.minecraft.server;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import net.minecraft.world.level.block.state.IBlockData;

public class ItemSpawner extends ItemBlock {
    public ItemSpawner(Block block, Info info) {
        super(block, info);
    }

    @Override
    protected boolean a(BlockPosition blockposition, World world, EntityHuman entityhuman, ItemStack itemstack, IBlockData iblockdata) {
        boolean handled = super.a(blockposition, world, entityhuman, itemstack, iblockdata);
        if (world.purpurConfig.silkTouchEnabled && entityhuman.getBukkitEntity().hasPermission("purpur.place.spawners")) {
            TileEntity spawner = world.getTileEntity(blockposition);
            if (spawner instanceof TileEntityMobSpawner && itemstack.hasTag()) {
                NBTTagCompound tag = itemstack.getTag();
                if (tag.hasKey("Purpur.mob_type")) {
                    EntityTypes.getByName(tag.getString("Purpur.mob_type")).ifPresent(type ->
                            ((TileEntityMobSpawner) spawner).getSpawner().setMobName(type));
                }
            }
        }
        return handled;
    }
}
