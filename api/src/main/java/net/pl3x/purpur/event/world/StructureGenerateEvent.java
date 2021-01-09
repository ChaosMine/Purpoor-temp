package net.pl3x.purpur.event.world;

import org.bukkit.Bukkit;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

public class StructureGenerateEvent extends WorldEvent implements Cancellable {
  private static final HandlerList handlers = new HandlerList();

  @NotNull
  private final StructureType structureType;
  private final int chunkX;
  private final int chunkZ;

  private boolean cancel = false;

  public StructureGenerateEvent(@NotNull World world,
      @NotNull StructureType structureType, int chunkX, int chunkZ) {
    super(!Bukkit.isPrimaryThread(), world); // Structure generation is not necessarily on the main thread as of 1.16.
    this.structureType = structureType;
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
  }

  @NotNull
  @Override
  public World getWorld() {
    return super.getWorld();
  }

  @NotNull
  public StructureType getStructureType() {
    return structureType;
  }

  public int getChunkX() {
    return chunkX;
  }

  public int getChunkZ() {
    return chunkZ;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }

  @Override
  public boolean isCancelled() {
    return this.cancel;
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
