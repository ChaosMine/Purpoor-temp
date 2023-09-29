package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPosition;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureCircleConfiguration;

import java.util.Random;

public class WorldGenFeatureCircle extends WorldGenFeatureDisk {
	public WorldGenFeatureCircle(Codec<WorldGenFeatureCircleConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean generate(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureCircleConfiguration worldgenfeaturecircleconfiguration) {
		return a(generatoraccessseed, chunkgenerator, random, blockposition, worldgenfeaturecircleconfiguration);
	}

	public boolean a(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureCircleConfiguration worldgenfeaturecircleconfiguration) {
		return generatoraccessseed.getFluid(blockposition).a(TagsFluid.WATER) && super.a(generatoraccessseed, chunkgenerator, random, blockposition, worldgenfeaturecircleconfiguration);
	}
}
