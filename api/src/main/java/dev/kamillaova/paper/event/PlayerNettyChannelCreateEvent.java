package dev.kamillaova.paper.event;

import io.netty.channel.Channel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerNettyChannelCreateEvent extends Event {
  private static final HandlerList handlers = new HandlerList();

  private final @NotNull Channel channel;

  public PlayerNettyChannelCreateEvent(@NotNull Channel channel) {
    super(true);
    this.channel = channel;
  }

  public @NotNull Channel getChannel() {
    return channel;
  }
  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }

  public static @NotNull HandlerList getHandlerList() {
    return handlers;
  }
}
