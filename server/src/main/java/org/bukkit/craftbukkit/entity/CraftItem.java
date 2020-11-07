package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem extends CraftEntity implements Item {

    // Paper start
    private final static int NO_AGE_TIME = (int) Short.MIN_VALUE;
    private final static int NO_PICKUP_TIME = (int) Short.MAX_VALUE;
    // Paper end

    private final EntityItem item;

    public CraftItem(CraftServer server, Entity entity, EntityItem item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, EntityItem entity) {
        this(server, entity, entity);
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.getItemStack());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        item.setItemStack(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return item.pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        item.pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityItem
        item.age = value;
    }

    // Paper Start
    public boolean canMobPickup() {
        return item.canMobPickup;
    }

    public void setCanMobPickup(boolean canMobPickup) {
        item.canMobPickup = canMobPickup;
    }

     @Override
     public boolean canPlayerPickup() {
        return item.pickupDelay != NO_PICKUP_TIME;
     }

     @Override
     public void setCanPlayerPickup(boolean canPlayerPickup) {
        item.pickupDelay = canPlayerPickup ? 0 : NO_PICKUP_TIME;
     }

     @Override
     public boolean willAge() {
        return item.age != NO_AGE_TIME;
     }

     @Override
     public void setWillAge(boolean willAge) {
        item.age = willAge ? 0 : NO_AGE_TIME;
     }
    // Paper End

    @Override
    public void setOwner(UUID uuid) {
        item.setOwner(uuid);
    }

    @Override
    public UUID getOwner() {
        return item.getOwner();
    }

    @Override
    public void setThrower(UUID uuid) {
        item.setThrower(uuid);
    }

    @Override
    public UUID getThrower() {
        return item.getThrower();
    }

    @Override
    public String toString() {
        return "CraftItem";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
