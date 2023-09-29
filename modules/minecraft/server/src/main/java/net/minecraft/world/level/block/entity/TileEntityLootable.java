package net.minecraft.world.level.block.entity;

import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.ContainerUtil;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.phys.Vec3D;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TileEntityLootable extends TileEntityContainer {

    @Nullable
    public MinecraftKey lootTable;
    public long lootTableSeed;
    public final com.destroystokyo.paper.loottable.PaperLootableInventoryData lootableData = new com.destroystokyo.paper.loottable.PaperLootableInventoryData(new com.destroystokyo.paper.loottable.PaperTileEntityLootableInventory(this)); // Paper

    protected TileEntityLootable(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
    }

    public static void a(IBlockAccess iblockaccess, Random random, BlockPosition blockposition, MinecraftKey minecraftkey) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityLootable) {
            ((TileEntityLootable) tileentity).setLootTable(minecraftkey, random.nextLong());
        }

    }

    protected boolean b(NBTTagCompound nbttagcompound) {
        this.lootableData.loadNbt(nbttagcompound); // Paper
        if (nbttagcompound.hasKeyOfType("LootTable", 8)) {
            this.lootTable = new MinecraftKey(nbttagcompound.getString("LootTable"));
            try { org.bukkit.craftbukkit.util.CraftNamespacedKey.fromMinecraft(this.lootTable); } catch (IllegalArgumentException ex) { this.lootTable = null; } // Paper - validate
            this.lootTableSeed = nbttagcompound.getLong("LootTableSeed");
            return false; // Paper - always load the items, table may still remain
        } else {
            return false;
        }
    }

    protected boolean c(NBTTagCompound nbttagcompound) {
        this.lootableData.saveNbt(nbttagcompound); // Paper
        if (this.lootTable == null) {
            return false;
        } else {
            nbttagcompound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("LootTableSeed", this.lootTableSeed);
            }

            return false; // Paper - always save the items, table may still remain
        }
    }

    public void d(@Nullable EntityHuman entityhuman) {
        if (this.lootableData.shouldReplenish(entityhuman) && this.world.getMinecraftServer() != null) { // Paper
            LootTable loottable = this.world.getMinecraftServer().getLootTableRegistry().getLootTable(this.lootTable);

            if (entityhuman instanceof EntityPlayer) {
                CriterionTriggers.N.a((EntityPlayer) entityhuman, this.lootTable);
            }

            //this.lootTable = null; // Paper
            this.lootableData.processRefill(entityhuman); // Paper
            LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).set(LootContextParameters.ORIGIN, Vec3D.a(this.position)).a(this.lootTableSeed);

            if (entityhuman != null) {
                loottableinfo_builder.a(entityhuman.eU()).set(LootContextParameters.THIS_ENTITY, entityhuman);
            }

            loottable.fillInventory(this, loottableinfo_builder.build(LootContextParameterSets.CHEST));
        }

    }

    public void setLootTable(MinecraftKey minecraftkey, long i) {
        this.lootTable = minecraftkey;
        this.lootTableSeed = i;
    }

    @Override
    public boolean isEmpty() {
        this.d(null);
        // Paper start
        for (ItemStack itemStack : this.f()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        // Paper end
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i == 0) this.d(null); // Paper
        return this.f().get(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        this.d(null);
        ItemStack itemstack = ContainerUtil.a(this.f(), i, j);

        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        this.d(null);
        return ContainerUtil.a(this.f(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.d(null);
        this.f().set(i, itemstack);
        if (itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        this.update();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) != this ? false : entityhuman.h((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear() {
        this.f().clear();
    }

    protected abstract NonNullList<ItemStack> f();

    protected abstract void a(NonNullList<ItemStack> nonnulllist);

    @Override
    public boolean e(EntityHuman entityhuman) {
        return super.e(entityhuman) && (this.lootTable == null || !entityhuman.isSpectator());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        if (this.e(entityhuman)) {
            this.d(playerinventory.player);
            return this.createContainer(i, playerinventory);
        } else {
            return null;
        }
    }
}
