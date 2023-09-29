package net.minecraft.world.item;

import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundEffectType;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.event.block.BlockCanBuildEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
// CraftBukkit end

public class ItemBlock extends Item {

    @Deprecated
    private final Block a;

    public ItemBlock(Block block, Item.Info item_info) {
        super(item_info);
        this.a = block;
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        var enuminteractionresult = this.a(new BlockActionContext(itemactioncontext));

        return !enuminteractionresult.a() && this.isFood() ? this.a(itemactioncontext.getWorld(), itemactioncontext.getEntity(), itemactioncontext.getHand()).a() : enuminteractionresult;
    }

    public EnumInteractionResult a(BlockActionContext blockactioncontext) {
        if (!blockactioncontext.b()) {
            return EnumInteractionResult.FAIL;
        } else {
            var blockactioncontext1 = this.b(blockactioncontext);

            if (blockactioncontext1 == null) {
                return EnumInteractionResult.FAIL;
            } else {
                var iblockdata = this.c(blockactioncontext1);
                // CraftBukkit start - special case for handling block placement with water lilies
                org.bukkit.block.BlockState blockstate = null;
                if (this instanceof ItemWaterLily) {
                    blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(blockactioncontext1.getWorld(), blockactioncontext1.getClickPosition());
                }
                // CraftBukkit end

                if (iblockdata == null) {
                    return EnumInteractionResult.FAIL;
                } else if (!this.a(blockactioncontext1, iblockdata)) {
                    return EnumInteractionResult.FAIL;
                } else {
                    var blockposition = blockactioncontext1.getClickPosition();
                    var world = blockactioncontext1.getWorld();
                    var entityhuman = blockactioncontext1.getEntity();
                    var itemstack = blockactioncontext1.getItemStack();
                    var iblockdata1 = world.getType(blockposition);
                    var block = iblockdata1.getBlock();

                    if (block == iblockdata.getBlock()) {
                        iblockdata1 = this.a(blockposition, world, itemstack, iblockdata1);
                        this.a(blockposition, world, entityhuman, itemstack, iblockdata1);
                        block.postPlace(world, blockposition, iblockdata1, entityhuman, itemstack);
                        // CraftBukkit start
                        if (blockstate != null) {
                            var placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent((WorldServer) world, entityhuman, blockactioncontext1.getHand(), blockstate, blockposition.getX(), blockposition.getY(), blockposition.getZ());
                            if (placeEvent.isCancelled() || !placeEvent.canBuild()) {
                                blockstate.update(true, false);
                                return EnumInteractionResult.FAIL;
                            }
                        }
                        // CraftBukkit end
                        if (entityhuman instanceof EntityPlayer) {
                            CriterionTriggers.y.a((EntityPlayer) entityhuman, blockposition, itemstack);
                        }
                    }

                    var soundeffecttype = iblockdata1.getStepSound();

                    if (entityhuman == null) world.playSound(null, blockposition, this.a(iblockdata1), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F); // Paper - reintroduce this for the dispenser (i.e the shulker)
                    if ((entityhuman == null || !entityhuman.abilities.canInstantlyBuild) && itemstack != ItemStack.b) { // CraftBukkit
                        itemstack.subtract(1);
                    }

                    return EnumInteractionResult.a(world.isClientSide);
                }
            }
        }
    }

    protected SoundEffect a(IBlockData iblockdata) {
        return iblockdata.getStepSound().getPlaceSound();
    }

    @Nullable
    public BlockActionContext b(BlockActionContext blockactioncontext) {
        return blockactioncontext;
    }

    protected boolean a(BlockPosition blockposition, World world, @Nullable EntityHuman entityhuman, ItemStack itemstack, IBlockData iblockdata) {
        // Purpur start
        var handled = a(world, entityhuman, blockposition, itemstack);
        if (world.purpurConfig.persistentTileEntityDisplayNames && itemstack.hasTag()) {
            var display = itemstack.getSubTag("display");
            if (display != null) {
                var tile = world.getTileEntity(blockposition);
                if (tile != null) {
                    if (display.hasKeyOfType("Name", 8)) {
                        tile.setPersistentDisplayName(display.getString("Name"));
                    }
                    if (display.hasKeyOfType("Lore", 9)) {
                        tile.setPersistentLore(display.getList("Lore", 8));
                    }
                }
            }
        }
        return handled;
        // Purpur end
    }

    @Nullable
    protected IBlockData c(BlockActionContext blockactioncontext) {
        var iblockdata = this.getBlock().getPlacedState(blockactioncontext);

        return iblockdata != null && this.b(blockactioncontext, iblockdata) ? iblockdata : null;
    }

    private IBlockData a(BlockPosition blockposition, World world, ItemStack itemstack, IBlockData iblockdata) {
        var iblockdata1 = iblockdata;
        var nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null) {
            var nbttagcompound1 = nbttagcompound.getCompound("BlockStateTag");
            // CraftBukkit start
            iblockdata1 = getBlockState(iblockdata1, nbttagcompound1);
        }

        if (iblockdata1 != iblockdata) {
            world.setTypeAndData(blockposition, iblockdata1, 2);
        }

        return iblockdata1;
    }

    public static IBlockData getBlockState(IBlockData iblockdata, NBTTagCompound nbttagcompound1) {
        var iblockdata1 = iblockdata;
        {
            // CraftBukkit end
            var blockstatelist = iblockdata.getBlock().getStates();
            Iterator iterator = nbttagcompound1.getKeys().iterator();

            while (iterator.hasNext()) {
                var s = (String) iterator.next();
                var iblockstate = blockstatelist.a(s);

                if (iblockstate != null) {
                    var s1 = nbttagcompound1.get(s).asString();

                    iblockdata1 = a(iblockdata1, iblockstate, s1);
                }
            }
        }
        return iblockdata1;
    }

    private static <T extends Comparable<T>> IBlockData a(IBlockData iblockdata, IBlockState<T> iblockstate, String s) {
        return iblockstate.b(s).map((comparable) -> {
            return iblockdata.set(iblockstate, comparable);
        }).orElse(iblockdata);
    }

    protected boolean b(BlockActionContext blockactioncontext, IBlockData iblockdata) {
        var entityhuman = blockactioncontext.getEntity();
        var voxelshapecollision = entityhuman == null ? VoxelShapeCollision.a() : VoxelShapeCollision.a(entityhuman);
        // CraftBukkit start - store default return
        var world = blockactioncontext.getWorld(); // Paper
        var defaultReturn = (!this.isCheckCollisions() || iblockdata.canPlace(blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) && world.checkEntityCollision(iblockdata, entityhuman, voxelshapecollision, blockactioncontext.getClickPosition(), true); // Paper
        var player = (blockactioncontext.getEntity() instanceof EntityPlayer) ? (org.bukkit.entity.Player) blockactioncontext.getEntity().getBukkitEntity() : null;

        var event = new BlockCanBuildEvent(CraftBlock.at(blockactioncontext.getWorld(), blockactioncontext.getClickPosition()), player, CraftBlockData.fromData(iblockdata), defaultReturn);
        blockactioncontext.getWorld().getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    protected boolean isCheckCollisions() {
        return true;
    }

    protected boolean a(BlockActionContext blockactioncontext, IBlockData iblockdata) {
        return blockactioncontext.getWorld().setTypeAndData(blockactioncontext.getClickPosition(), iblockdata, 11);
    }

    public static boolean a(World world, @Nullable EntityHuman entityhuman, BlockPosition blockposition, ItemStack itemstack) {
        var minecraftserver = world.getMinecraftServer();

        if (minecraftserver == null) {
            return false;
        } else {
            var nbttagcompound = itemstack.b("BlockEntityTag");

            if (nbttagcompound != null) {
                var tileentity = world.getTileEntity(blockposition);

                if (tileentity != null) {
                    if (!world.isClientSide && tileentity.isFilteredNBT() && (entityhuman == null || !(entityhuman.isCreativeAndOp() || (entityhuman.abilities.canInstantlyBuild && entityhuman.getBukkitEntity().hasPermission("minecraft.nbt.place"))))) { // Spigot - add permission
                        return false;
                    }

                    var nbttagcompound1 = tileentity.save(new NBTTagCompound());
                    var nbttagcompound2 = nbttagcompound1.clone();

                    nbttagcompound1.a(nbttagcompound);
                    nbttagcompound1.setInt("x", blockposition.getX());
                    nbttagcompound1.setInt("y", blockposition.getY());
                    nbttagcompound1.setInt("z", blockposition.getZ());
                    if (!nbttagcompound1.equals(nbttagcompound2)) {
                        tileentity.load(world.getType(blockposition), nbttagcompound1);
                        tileentity.update();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public String getName() {
        return this.getBlock().i();
    }

    @Override
    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.a(creativemodetab)) {
            this.getBlock().a(creativemodetab, nonnulllist);
        }

    }

    public Block getBlock() {
        return this.a;
    }

    public void a(Map<Block, Item> map, Item item) {
        map.put(this.getBlock(), item);
    }
}
