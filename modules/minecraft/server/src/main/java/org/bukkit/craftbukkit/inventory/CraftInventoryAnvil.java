package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.world.IInventory;
import net.minecraft.world.inventory.ContainerAnvil;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil extends CraftResultInventory implements AnvilInventory {

    private final Location location;
    public final ContainerAnvil container; // Purpur - private -> public

    public CraftInventoryAnvil(Location location, IInventory inventory, IInventory resultInventory, ContainerAnvil container) {
        super(inventory, resultInventory);
        this.location = location;
        this.container = container;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getRenameText() {
        return container.renameText;
    }

    @Override
    public int getRepairCost() {
        return container.levelCost.get();
    }

    @Override
    public void setRepairCost(int i) {
        container.levelCost.set(i);
    }

    @Override
    public int getMaximumRepairCost() {
        return container.maximumRepairCost;
    }

    @Override
    public void setMaximumRepairCost(int levels) {
        Preconditions.checkArgument(levels >= 0, "Maximum repair cost must be positive (or 0)");
        container.maximumRepairCost = levels;
    }

    // Purpur start
    @Override
    public boolean canBypassCost() {
        return container.bypassCost;
    }

    @Override
    public void setBypassCost(boolean bypassCost) {
        container.bypassCost = bypassCost;
    }

    @Override
    public boolean canDoUnsafeEnchants() {
        return container.canDoUnsafeEnchants;
    }

    @Override
    public void setDoUnsafeEnchants(boolean canDoUnsafeEnchants) {
        container.canDoUnsafeEnchants = canDoUnsafeEnchants;
    }
    // Purpur end
}
