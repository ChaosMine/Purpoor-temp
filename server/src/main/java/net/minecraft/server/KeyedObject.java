package net.minecraft.server;

import net.minecraft.resources.MinecraftKey;

public interface KeyedObject {
    MinecraftKey getMinecraftKey();
    default String getMinecraftKeyString() {
        MinecraftKey key = getMinecraftKey();
        return key != null ? key.toString() : null;
    }
}
