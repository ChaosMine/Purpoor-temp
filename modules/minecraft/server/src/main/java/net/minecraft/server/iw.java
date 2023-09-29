package net.minecraft.server;

import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class iw {

    @Deprecated
    public static MinecraftKey a(String s) {
        return new MinecraftKey("minecraft", "block/" + s);
    }

    public static MinecraftKey b(String s) {
        return new MinecraftKey("minecraft", "item/" + s);
    }

    public static MinecraftKey a(Block block, String s) {
        MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(block);

        return new MinecraftKey(minecraftkey.getNamespace(), "block/" + minecraftkey.getKey() + s);
    }

    public static MinecraftKey a(Block block) {
        MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(block);

        return new MinecraftKey(minecraftkey.getNamespace(), "block/" + minecraftkey.getKey());
    }

    public static MinecraftKey a(Item item) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(item);

        return new MinecraftKey(minecraftkey.getNamespace(), "item/" + minecraftkey.getKey());
    }

    public static MinecraftKey a(Item item, String s) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(item);

        return new MinecraftKey(minecraftkey.getNamespace(), "item/" + minecraftkey.getKey() + s);
    }
}
