package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureEmptyConfiguration;

import java.util.Random;

public class WorldGenFeatureEmpty extends WorldGenerator<WorldGenFeatureEmptyConfiguration> {

    public WorldGenFeatureEmpty(Codec<WorldGenFeatureEmptyConfiguration> codec) {
        super(codec);
    }

    @Override
public boolean generate(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureEmptyConfiguration worldgenfeatureemptyconfiguration) {
  return a(generatoraccessseed, chunkgenerator, random, blockposition, worldgenfeatureemptyconfiguration);
}

public boolean a(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureEmptyConfiguration worldgenfeatureemptyconfiguration) {
        return true;
    }
}
