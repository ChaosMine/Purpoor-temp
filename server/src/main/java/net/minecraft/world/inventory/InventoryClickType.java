package net.minecraft.world.inventory;

import org.jetbrains.annotations.Unmodifiable;

public enum InventoryClickType {
    PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL,
    ;

    public static final InventoryClickType @Unmodifiable [] VALUES = values();
}
