package net.pl3x.purpur.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Called when anvil slots change, triggering the result slot to be updated
 */
public class AnvilUpdateResultEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();

    public AnvilUpdateResultEvent(@NotNull InventoryView view) {
        super(view);
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
