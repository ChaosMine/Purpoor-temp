package net.pl3x.purpur.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player takes the result item out of an anvil
 */
public class AnvilTakeResultEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack result;

    public AnvilTakeResultEvent(@NotNull HumanEntity player, @NotNull InventoryView view, @NotNull ItemStack result) {
        super(view);
        this.player = (Player) player;
        this.result = result;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ItemStack getResult() {
        return result;
    }

    @NotNull
    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
