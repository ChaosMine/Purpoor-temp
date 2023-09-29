package net.minecraft.world.inventory;

import org.jetbrains.annotations.Unmodifiable;

public enum RecipeBookType {
    CRAFTING,
    FURNACE,
    BLAST_FURNACE,
    SMOKER,
    ;

    public static final RecipeBookType @Unmodifiable [] VALUES = values();
}
