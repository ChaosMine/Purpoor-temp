package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityEnderChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<TileEntityEnderChest> implements EnderChest {

    public CraftEnderChest(final Block block) {
        super(block, TileEntityEnderChest.class);
    }

    public CraftEnderChest(final Material material, final TileEntityEnderChest te) {
        super(material, te);
    }

    // Paper start - More Lidded Block API
    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            net.minecraft.world.level.World world = getTileEntity().getWorld();
            world.playBlockAction(getTileEntity().getPosition(), getTileEntity().getBlock().getBlock(), 1, getTileEntity().getViewerCount() + 1);
            world.playSound(null, getPosition(), net.minecraft.sounds.SoundEffects.BLOCK_ENDER_CHEST_OPEN, net.minecraft.sounds.SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            net.minecraft.world.level.World world = getTileEntity().getWorld();
            world.playBlockAction(getTileEntity().getPosition(), getTileEntity().getBlock().getBlock(), 1, 0);
            world.playSound(null, getPosition(), net.minecraft.sounds.SoundEffects.BLOCK_ENDER_CHEST_CLOSE, net.minecraft.sounds.SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }

    @Override
    public boolean isOpen() {
        return getTileEntity().opened;
    }
    // Paper end - More Lidded Block API
}
