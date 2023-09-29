package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.feature.configurations.StructureSettingsFeature;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureVillageConfiguration;

import java.util.List;

public class WorldGenFeaturePillagerOutpost extends WorldGenFeatureJigsaw {

    private static final List<BiomeSettingsMobs.c> u = ImmutableList.of(new BiomeSettingsMobs.c(EntityTypes.PILLAGER, 1, 1, 1));

    public WorldGenFeaturePillagerOutpost(Codec<WorldGenFeatureVillageConfiguration> codec) {
        super(codec, 0, true, true);
    }

    @Override
    public List<BiomeSettingsMobs.c> c() {
        return WorldGenFeaturePillagerOutpost.u;
    }

    protected boolean a(ChunkGenerator chunkgenerator, WorldChunkManager worldchunkmanager, long i, SeededRandom seededrandom, int j, int k, BiomeBase biomebase, ChunkCoordIntPair chunkcoordintpair, WorldGenFeatureVillageConfiguration worldgenfeaturevillageconfiguration) {
        int l = j >> 4;
        int i1 = k >> 4;

        seededrandom.secureseed_setSeed(Globals.worldSeed, j >> 4, k >> 4, Globals.dimension.get(), Globals.PILLAGER_OUTPOST_SALT, 0);
        seededrandom.nextInt();
        return seededrandom.nextInt(5) != 0 ? false : !this.a(chunkgenerator, i, seededrandom, j, k);
    }

    private boolean a(ChunkGenerator chunkgenerator, long i, SeededRandom seededrandom, int j, int k) {
        StructureSettingsFeature structuresettingsfeature = chunkgenerator.getSettings().a(StructureGenerator.VILLAGE);

        if (structuresettingsfeature == null) {
            return false;
        } else {
            for (int l = j - 10; l <= j + 10; ++l) {
                for (int i1 = k - 10; i1 <= k + 10; ++i1) {
                    ChunkCoordIntPair chunkcoordintpair = StructureGenerator.VILLAGE.a(structuresettingsfeature, i, seededrandom, l, i1);

                    if (l == chunkcoordintpair.x && i1 == chunkcoordintpair.z) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
