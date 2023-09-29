package net.minecraft.world.entity.player;

import org.jetbrains.annotations.Unmodifiable;

public enum EnumChatVisibility {
    FULL(0),
    SYSTEM(1),
    HIDDEN(2),
    ;

    public static final EnumChatVisibility @Unmodifiable [] VALUES = values();

    private final int e;

    EnumChatVisibility(int i) {
        this.e = i;
    }

    public int a() {
        return this.e;
    }
}
