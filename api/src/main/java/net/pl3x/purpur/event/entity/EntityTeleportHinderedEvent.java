package net.pl3x.purpur.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fired when an entity is hindered from teleporting.
 */
public class EntityTeleportHinderedEvent extends EntityEvent {
  private static final HandlerList handlers = new HandlerList();

  @NotNull
  private final Reason reason;

  @Nullable
  private final TeleportCause teleportCause;

  private boolean retry = false;

  public EntityTeleportHinderedEvent(@NotNull Entity what, @NotNull Reason reason,
      @Nullable TeleportCause teleportCause) {
    super(what);
    this.reason = reason;
    this.teleportCause = teleportCause;
  }

  /**
   * @return why the teleport was hindered.
   */
  @NotNull
  public Reason getReason() {
    return reason;
  }

  /**
   * @return why the teleport occurred if cause was given, otherwise {@code null}.
   */
  @Nullable
  public TeleportCause getTeleportCause() {
    return teleportCause;
  }

  /**
   * Whether the teleport should be retried.
   * <p>
   * Note that this can put the server in a never-ending loop of trying to teleport someone resulting in a stack
   * overflow. Do not retry more than necessary.
   * </p>
   *
   * @return whether the teleport should be retried.
   */
  public boolean shouldRetry() {
    return retry;
  }

  /**
   * Sets whether the teleport should be retried.
   * <p>
   * Note that this can put the server in a never-ending loop of trying to teleport someone resulting in a stack
   * overflow. Do not retry more than necessary.
   * </p>
   *
   * @param retry whether the teleport should be retried.
   */
  public void setShouldRetry(boolean retry) {
    this.retry = retry;
  }

  /**
   * Calls the event and tests if should retry.
   *
   * @return whether the teleport should be retried.
   */
  @Override
  public boolean callEvent() {
    super.callEvent();
    return shouldRetry();
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

  /**
   * Reason for hindrance in teleports.
   */
  public enum Reason {
    /**
     * The teleported entity is a passenger of another entity.
     */
    IS_PASSENGER,

    /**
     * The teleported entity has passengers.
     */
    IS_VEHICLE,

    /**
     * The teleport event was cancelled.
     * <p>
     * This is only caused by players teleporting.
     * </p>
     */
    EVENT_CANCELLED,
  }
}
