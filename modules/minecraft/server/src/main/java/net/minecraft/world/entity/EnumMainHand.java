package net.minecraft.world.entity;

import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import org.jetbrains.annotations.Unmodifiable;

public enum EnumMainHand {
    LEFT(new ChatMessage("options.mainHand.left")),
    RIGHT(new ChatMessage("options.mainHand.right")),
    ;

    public static final EnumMainHand @Unmodifiable [] VALUES = values();

    private final IChatBaseComponent c;

    EnumMainHand(IChatBaseComponent ichatbasecomponent) {
        this.c = ichatbasecomponent;
    }

    public String toString() {
        return this.c.getString();
    }
}
