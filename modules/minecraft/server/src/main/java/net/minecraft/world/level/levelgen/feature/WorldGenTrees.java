package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.WorldGenFoilagePlacer;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.VoxelShapeBitSet;
import net.minecraft.world.phys.shapes.VoxelShapeDiscrete;

import java.util.*;

public class WorldGenTrees extends WorldGenerator<WorldGenFeatureTreeConfiguration> {
	public WorldGenTrees(Codec<WorldGenFeatureTreeConfiguration> codec) {
		super(codec);
	}

	public static boolean c(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return e(virtuallevelreadable, blockposition) || virtuallevelreadable.a(blockposition, (iblockdata) -> {
			return iblockdata.a(TagsBlock.LOGS);
		});
	}

	private static boolean f(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return virtuallevelreadable.a(blockposition, (iblockdata) -> {
			return iblockdata.a(Blocks.VINE);
		});
	}

	private static boolean g(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return virtuallevelreadable.a(blockposition, (iblockdata) -> {
			return iblockdata.a(Blocks.WATER);
		});
	}

	public static boolean d(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return virtuallevelreadable.a(blockposition, (iblockdata) -> {
			return iblockdata.isAir() || iblockdata.a(TagsBlock.LEAVES);
		});
	}

	private static boolean h(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return virtuallevelreadable.a(blockposition, (iblockdata) -> {
			Block block = iblockdata.getBlock();

			return b(block) || block == Blocks.FARMLAND;
		});
	}

	private static boolean i(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return virtuallevelreadable.a(blockposition, (iblockdata) -> {
			Material material = iblockdata.getMaterial();

			return material == Material.REPLACEABLE_PLANT;
		});
	}

	public static void b(IWorldWriter iworldwriter, BlockPosition blockposition, IBlockData iblockdata) {
		iworldwriter.setTypeAndData(blockposition, iblockdata, 19);
	}

	public static boolean e(VirtualLevelReadable virtuallevelreadable, BlockPosition blockposition) {
		return d(virtuallevelreadable, blockposition) || i(virtuallevelreadable, blockposition) || g(virtuallevelreadable, blockposition);
	}

	private boolean a(VirtualLevelWritable virtuallevelwritable, Random random, BlockPosition blockposition, Set<BlockPosition> set, Set<BlockPosition> set1, StructureBoundingBox structureboundingbox, WorldGenFeatureTreeConfiguration worldgenfeaturetreeconfiguration) {
		int i = worldgenfeaturetreeconfiguration.g.a(random);
		int j = worldgenfeaturetreeconfiguration.f.a(random, i, worldgenfeaturetreeconfiguration);
		int k = i - j;
		int l = worldgenfeaturetreeconfiguration.f.a(random, k);
		int i1;
		BlockPosition blockposition1;

		if (!worldgenfeaturetreeconfiguration.e) {
			int j1 = virtuallevelwritable.getHighestBlockYAt(HeightMap.Type.OCEAN_FLOOR, blockposition).getY();

			i1 = virtuallevelwritable.getHighestBlockYAt(HeightMap.Type.WORLD_SURFACE, blockposition).getY();
			if (i1 - j1 > worldgenfeaturetreeconfiguration.i) {
				return false;
			}

			int k1;

			if (worldgenfeaturetreeconfiguration.l == HeightMap.Type.OCEAN_FLOOR) {
				k1 = j1;
			} else if (worldgenfeaturetreeconfiguration.l == HeightMap.Type.WORLD_SURFACE) {
				k1 = i1;
			} else {
				k1 = virtuallevelwritable.getHighestBlockYAt(worldgenfeaturetreeconfiguration.l, blockposition).getY();
			}

			blockposition1 = new BlockPosition(blockposition.getX(), k1, blockposition.getZ());
		} else {
			blockposition1 = blockposition;
		}

		if (blockposition1.getY() >= 1 && blockposition1.getY() + i + 1 <= 256) {
			if (!h(virtuallevelwritable, blockposition1.down())) {
				return false;
			} else {
				OptionalInt optionalint = worldgenfeaturetreeconfiguration.h.c();

				i1 = this.a(virtuallevelwritable, i, blockposition1, worldgenfeaturetreeconfiguration);
				if (i1 < i && (optionalint.isEmpty() || i1 < optionalint.getAsInt())) {
					return false;
				} else {
					List<WorldGenFoilagePlacer.b> list = worldgenfeaturetreeconfiguration.g.a(virtuallevelwritable, random, i1, blockposition1, set, structureboundingbox, worldgenfeaturetreeconfiguration);

					int finalI = i1;
					list.forEach((worldgenfoilageplacer_b) -> {
						worldgenfeaturetreeconfiguration.f.a(virtuallevelwritable, random, worldgenfeaturetreeconfiguration, finalI, worldgenfoilageplacer_b, j, l, set1, structureboundingbox);
					});
					return true;
				}
			}
		} else {
			return false;
		}
	}

	private int a(VirtualLevelReadable virtuallevelreadable, int i, BlockPosition blockposition, WorldGenFeatureTreeConfiguration worldgenfeaturetreeconfiguration) {
		BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

		for (int j = 0; j <= i + 1; ++j) {
			int k = worldgenfeaturetreeconfiguration.h.a(i, j);

			for (int l = -k; l <= k; ++l) {
				for (int i1 = -k; i1 <= k; ++i1) {
					blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, l, j, i1);
					if (!c(virtuallevelreadable, blockposition_mutableblockposition) || !worldgenfeaturetreeconfiguration.j && f(virtuallevelreadable, blockposition_mutableblockposition)) {
						return j - 2;
					}
				}
			}
		}

		return i;
	}

	@Override
	protected void a(IWorldWriter iworldwriter, BlockPosition blockposition, IBlockData iblockdata) {
		b(iworldwriter, blockposition, iblockdata);
	}

	@Override
	public boolean generate(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureTreeConfiguration worldGenFeatureTreeConfiguration) {
		return a(generatoraccessseed, chunkgenerator, random, blockposition, worldGenFeatureTreeConfiguration);
	}

	public final boolean a(GeneratorAccessSeed generatoraccessseed, ChunkGenerator chunkgenerator, Random random, BlockPosition blockposition, WorldGenFeatureTreeConfiguration worldgenfeaturetreeconfiguration) {
		Set<BlockPosition> set = Sets.newHashSet();
		Set<BlockPosition> set1 = Sets.newHashSet();
		Set<BlockPosition> set2 = Sets.newHashSet();
		StructureBoundingBox structureboundingbox = StructureBoundingBox.a();
		boolean flag = this.a(generatoraccessseed, random, blockposition, set, set1, structureboundingbox, worldgenfeaturetreeconfiguration);

		if (structureboundingbox.a <= structureboundingbox.d && flag && !set.isEmpty()) {
			if (!worldgenfeaturetreeconfiguration.d.isEmpty()) {
				List<BlockPosition> list = Lists.newArrayList(set);
				List<BlockPosition> list1 = Lists.newArrayList(set1);

				list.sort(Comparator.comparingInt(BaseBlockPosition::getY));
				list1.sort(Comparator.comparingInt(BaseBlockPosition::getY));
				worldgenfeaturetreeconfiguration.d.forEach((worldgenfeaturetree) -> {
					worldgenfeaturetree.a(generatoraccessseed, random, list, list1, set2, structureboundingbox);
				});
			}

			VoxelShapeDiscrete voxelshapediscrete = this.a(generatoraccessseed, structureboundingbox, set, set2);

			DefinedStructure.a(generatoraccessseed, 3, voxelshapediscrete, structureboundingbox.a, structureboundingbox.b, structureboundingbox.c);
			return true;
		} else {
			return false;
		}
	}

	private VoxelShapeDiscrete a(GeneratorAccess generatoraccess, StructureBoundingBox structureboundingbox, Set<BlockPosition> set, Set<BlockPosition> set1) {
		List<Set<BlockPosition>> list = Lists.newArrayList();
		VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(structureboundingbox.d(), structureboundingbox.e(), structureboundingbox.f());

		for (int i = 0; i < 6; ++i) {
			list.add(Sets.newHashSet());
		}

		BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

		BlockPosition blockposition;
		var iterator = Lists.newArrayList(set1).iterator();

		while (iterator.hasNext()) {
			blockposition = iterator.next();
			if (structureboundingbox.b(blockposition)) {
				voxelshapebitset.a(blockposition.getX() - structureboundingbox.a, blockposition.getY() - structureboundingbox.b, blockposition.getZ() - structureboundingbox.c, true, true);
			}
		}

		iterator = Lists.newArrayList(set).iterator();

		while (iterator.hasNext()) {
			blockposition = iterator.next();
			if (structureboundingbox.b(blockposition)) {
				voxelshapebitset.a(blockposition.getX() - structureboundingbox.a, blockposition.getY() - structureboundingbox.b, blockposition.getZ() - structureboundingbox.c, true, true);
			}

			EnumDirection[] aenumdirection = EnumDirection.values();
			int j = aenumdirection.length;

			for (int k = 0; k < j; ++k) {
				EnumDirection enumdirection = aenumdirection[k];

				blockposition_mutableblockposition.a(blockposition, enumdirection);
				if (!set.contains(blockposition_mutableblockposition)) {
					IBlockData iblockdata = generatoraccess.getType(blockposition_mutableblockposition);

					if (iblockdata.b(BlockProperties.an)) {
						list.get(0).add(blockposition_mutableblockposition.immutableCopy());
						b(generatoraccess, blockposition_mutableblockposition, iblockdata.set(BlockProperties.an, 1));
						if (structureboundingbox.b(blockposition_mutableblockposition)) {
							voxelshapebitset.a(blockposition_mutableblockposition.getX() - structureboundingbox.a, blockposition_mutableblockposition.getY() - structureboundingbox.b, blockposition_mutableblockposition.getZ() - structureboundingbox.c, true, true);
						}
					}
				}
			}
		}

		for (int l = 1; l < 6; ++l) {
			Set<BlockPosition> set2 = list.get(l - 1);
			Set<BlockPosition> set3 = list.get(l);

			for (BlockPosition blockposition1 : set2) {
				if (structureboundingbox.b(blockposition1)) {
					voxelshapebitset.a(blockposition1.getX() - structureboundingbox.a, blockposition1.getY() - structureboundingbox.b, blockposition1.getZ() - structureboundingbox.c, true, true);
				}

				EnumDirection[] aenumdirection1 = EnumDirection.values();
				int i1 = aenumdirection1.length;

				for (int j1 = 0; j1 < i1; ++j1) {
					EnumDirection enumdirection1 = aenumdirection1[j1];

					blockposition_mutableblockposition.a(blockposition1, enumdirection1);
					if (!set2.contains(blockposition_mutableblockposition) && !set3.contains(blockposition_mutableblockposition)) {
						IBlockData iblockdata1 = generatoraccess.getType(blockposition_mutableblockposition);

						if (iblockdata1.b(BlockProperties.an)) {
							int k1 = iblockdata1.get(BlockProperties.an);

							if (k1 > l + 1) {
								IBlockData iblockdata2 = iblockdata1.set(BlockProperties.an, l + 1);

								b(generatoraccess, blockposition_mutableblockposition, iblockdata2);
								if (structureboundingbox.b(blockposition_mutableblockposition)) {
									voxelshapebitset.a(blockposition_mutableblockposition.getX() - structureboundingbox.a, blockposition_mutableblockposition.getY() - structureboundingbox.b, blockposition_mutableblockposition.getZ() - structureboundingbox.c, true, true);
								}

								set3.add(blockposition_mutableblockposition.immutableCopy());
							}
						}
					}
				}
			}
		}

		return voxelshapebitset;
	}
}
