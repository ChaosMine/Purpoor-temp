package net.pl3x.purpur.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MonsterEggSpawnEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    private final Player player;
    private Entity entity;
    private final ItemStack item;

    public MonsterEggSpawnEvent(@Nullable HumanEntity player, @NotNull Entity entity, @NotNull ItemStack item) {
        this.player = (Player) player;
        this.entity = entity;
        this.item = item;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Entity getEntity() {
        return entity;
    }

    public void setEntity(@Nullable Entity entity) {
        if (entity == null) {
            canceled = true;
            return;
        }
        this.entity = entity;
    }

    @NotNull
    public ItemStack getItem() {
        return item;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
