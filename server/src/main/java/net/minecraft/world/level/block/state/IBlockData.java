package net.minecraft.world.level.block.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IBlockState;

public class IBlockData extends BlockBase.BlockData {

    public static final Codec<IBlockData> b = a((Codec) IRegistry.BLOCK, Block::getBlockData).stable();


    // Paper start - optimise getType calls
    org.bukkit.Material cachedMaterial;

    public final org.bukkit.Material getBukkitMaterial() {
        if (this.cachedMaterial == null) {
            this.cachedMaterial = org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(this.getBlock());
        }

        return this.cachedMaterial;
    }
    // Paper end - optimise getType calls

    public IBlockData(Block block, ImmutableMap<IBlockState<?>, Comparable<?>> immutablemap, MapCodec<IBlockData> mapcodec) {
        super(block, immutablemap, mapcodec);
    }

    @Override
    protected IBlockData p() {
        return this;
    }
}
