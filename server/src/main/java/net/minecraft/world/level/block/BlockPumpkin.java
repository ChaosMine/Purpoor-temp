package net.minecraft.world.level.block;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.server.MCUtil;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.MovingObjectPositionBlock;

public class BlockPumpkin extends BlockStemmed {

    protected BlockPumpkin(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.SHEARS) {
            if (!world.isClientSide) {
                // Paper start - Add PlayerShearBlockEvent
                PlayerShearBlockEvent event = new PlayerShearBlockEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), MCUtil.toBukkitBlock(world, blockposition), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack), (enumhand == EnumHand.OFF_HAND ? org.bukkit.inventory.EquipmentSlot.OFF_HAND : org.bukkit.inventory.EquipmentSlot.HAND), new ObjectArrayList<>());
                event.getDrops().add(org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(new ItemStack(Items.PUMPKIN_SEEDS, 4)));
                if (!event.callEvent()) {
                    return EnumInteractionResult.PASS;
                }
                // Paper end
                EnumDirection enumdirection = movingobjectpositionblock.getDirection();
                EnumDirection enumdirection1 = enumdirection.n() == EnumDirection.EnumAxis.Y ? entityhuman.getDirection().opposite() : enumdirection;

                world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setTypeAndData(blockposition, (IBlockData) Blocks.CARVED_PUMPKIN.getBlockData().set(BlockPumpkinCarved.a, enumdirection1), 11);
                // Paper start - Add PlayerShearBlockEvent
                for (org.bukkit.inventory.ItemStack item : event.getDrops()) {
                EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + 0.5D + (double) enumdirection1.getAdjacentX() * 0.65D, (double) blockposition.getY() + 0.1D, (double) blockposition.getZ() + 0.5D + (double) enumdirection1.getAdjacentZ() * 0.65D, org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(item));
                // Paper end

                entityitem.setMot(0.05D * (double) enumdirection1.getAdjacentX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) enumdirection1.getAdjacentZ() + world.random.nextDouble() * 0.02D);
                world.addEntity(entityitem);
                } // Paper - Add PlayerShearBlockEvent
                itemstack.damage(1, entityhuman, (entityhuman1) -> {
                    entityhuman1.broadcastItemBreak(enumhand);
                });
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return super.interact(iblockdata, world, blockposition, entityhuman, enumhand, movingobjectpositionblock);
        }
    }

    @Override
    public BlockStem c() {
        return (BlockStem) Blocks.PUMPKIN_STEM;
    }

    @Override
    public BlockStemAttached d() {
        return (BlockStemAttached) Blocks.ATTACHED_PUMPKIN_STEM;
    }
}
