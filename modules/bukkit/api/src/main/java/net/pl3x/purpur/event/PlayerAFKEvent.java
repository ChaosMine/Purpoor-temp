package net.pl3x.purpur.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAFKEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean setAfk;
    private boolean shouldKick;
    private String broadcast;
    private boolean cancel;

    public PlayerAFKEvent(@NotNull Player player, boolean setAfk, boolean shouldKick, @Nullable String broadcast, boolean async) {
        super(player, async);
        this.setAfk = setAfk;
        this.shouldKick = shouldKick;
        this.broadcast = broadcast;
    }

    /**
     * Whether player is going afk or coming back
     *
     * @return True if going afk. False is coming back
     */
    public boolean isGoingAfk() {
        return setAfk;
    }

    public boolean shouldKick() {
        return shouldKick;
    }

    public void setShouldKick(boolean shouldKick) {
        this.shouldKick = shouldKick;
    }

    @Nullable
    public String getBroadcastMsg() {
        return broadcast;
    }

    public void setBroadcastMsg(@Nullable String broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
