package net.minecraft.world.level.chunk;

import net.minecraft.core.SectionPosition;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.IBlockAccess;

import javax.annotation.Nullable;

public interface ILightAccess {

    default @Nullable IBlockAccess getFeaturesReadyChunk(int i, int j) { return this.c(i, j); } // Tuinity - OBFHELPER
    @Nullable IBlockAccess c(int i, int j);

    default void markLightSectionDirty(EnumSkyBlock enumskyblock, SectionPosition sectionposition) { this.a(enumskyblock, sectionposition); } // Tuinity - OBFHELPER
    default void a(EnumSkyBlock enumskyblock, SectionPosition sectionposition) {}

    IBlockAccess getWorld();
}
