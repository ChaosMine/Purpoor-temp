package net.minecraft.data.worldgen;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.IRegistry;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.IntSpread;
import net.minecraft.world.level.block.BlockHugeMushroom;
import net.minecraft.world.level.block.BlockSweetBerryBush;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.WorldGenStage;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureHugeFungiConfiguration;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.blockplacers.WorldGenBlockPlacerColumn;
import net.minecraft.world.level.levelgen.feature.blockplacers.WorldGenBlockPlacerDoublePlant;
import net.minecraft.world.level.levelgen.feature.blockplacers.WorldGenBlockPlacerSimple;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeThreeLayers;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeTwoLayers;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidTypes;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class BiomeDecoratorGroups {
	public static final WorldGenFeatureConfigured<?, ?> END_SPIKE = a(
		"end_spike", WorldGenerator.END_SPIKE.b(new WorldGenFeatureEndSpikeConfiguration(false, List.of(), null))
	);
	public static final WorldGenFeatureConfigured<?, ?> END_GATEWAY = a(
		"end_gateway",
		WorldGenerator.END_GATEWAY.b(WorldGenEndGatewayConfiguration.a(WorldServer.a, true)).a(WorldGenDecorator.x.b(WorldGenFeatureDecoratorConfiguration.b))
	);
	public static final WorldGenFeatureConfigured<?, ?> END_GATEWAY_DELAYED = a(
		"end_gateway_delayed", WorldGenerator.END_GATEWAY.b(WorldGenEndGatewayConfiguration.b())
	);
	public static final WorldGenFeatureConfigured<?, ?> CHORUS_PLANT = a(
		"chorus_plant", WorldGenerator.CHORUS_PLANT.b(WorldGenFeatureConfiguration.k).a(BiomeDecoratorGroups.b.l).c(4)
	);
	public static final WorldGenFeatureConfigured<?, ?> END_ISLAND = a("end_island", WorldGenerator.END_ISLAND.b(WorldGenFeatureConfiguration.k));
	public static final WorldGenFeatureConfigured<?, ?> END_ISLAND_DECORATED = a(
		"end_island_decorated", END_ISLAND.a(WorldGenDecorator.A.b(WorldGenFeatureDecoratorConfiguration.b))
	);
	public static final WorldGenFeatureConfigured<?, ?> DELTA = a(
		"delta",
		WorldGenerator.DELTA_FEATURE
			.b(new WorldGenFeatureDeltaConfiguration(BiomeDecoratorGroups.c.ac, BiomeDecoratorGroups.c.av, IntSpread.a(3, 4), IntSpread.a(0, 2)))
			.a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(40)))
	);
	public static final WorldGenFeatureConfigured<?, ?> SMALL_BASALT_COLUMNS = a(
		"small_basalt_columns",
		WorldGenerator.BASALT_COLUMNS
			.b(new WorldGenFeatureBasaltColumnsConfiguration(IntSpread.a(1), IntSpread.a(1, 3)))
			.a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(4)))
	);
	public static final WorldGenFeatureConfigured<?, ?> LARGE_BASALT_COLUMNS = a(
		"large_basalt_columns",
		WorldGenerator.BASALT_COLUMNS
			.b(new WorldGenFeatureBasaltColumnsConfiguration(IntSpread.a(2, 1), IntSpread.a(5, 5)))
			.a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(2)))
	);
	public static final WorldGenFeatureConfigured<?, ?> BASALT_BLOBS = a(
		"basalt_blobs",
		WorldGenerator.NETHERRACK_REPLACE_BLOBS
			.b(new WorldGenFeatureRadiusConfiguration(BiomeDecoratorGroups.c.N, BiomeDecoratorGroups.c.aB, IntSpread.a(3, 4)))
			.d(128)
			.a()
			.b(75)
	);
	public static final WorldGenFeatureConfigured<?, ?> BLACKSTONE_BLOBS = a(
		"blackstone_blobs",
		WorldGenerator.NETHERRACK_REPLACE_BLOBS
			.b(new WorldGenFeatureRadiusConfiguration(BiomeDecoratorGroups.c.N, BiomeDecoratorGroups.c.az, IntSpread.a(3, 4)))
			.d(128)
			.a()
			.b(25)
	);
	public static final WorldGenFeatureConfigured<?, ?> GLOWSTONE_EXTRA = a(
		"glowstone_extra",
		WorldGenerator.GLOWSTONE_BLOB.b(WorldGenFeatureConfiguration.k).a(WorldGenDecorator.w.b(new WorldGenDecoratorFrequencyConfiguration(10)))
	);
	public static final WorldGenFeatureConfigured<?, ?> GLOWSTONE = a(
		"glowstone", WorldGenerator.GLOWSTONE_BLOB.b(WorldGenFeatureConfiguration.k).d(128).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> CRIMSON_FOREST_VEGETATION = a(
		"crimson_forest_vegetation",
		WorldGenerator.NETHER_FOREST_VEGETATION.b(BiomeDecoratorGroups.a.k).a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(6)))
	);
	public static final WorldGenFeatureConfigured<?, ?> WARPED_FOREST_VEGETATION = a(
		"warped_forest_vegetation",
		WorldGenerator.NETHER_FOREST_VEGETATION.b(BiomeDecoratorGroups.a.l).a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(5)))
	);
	public static final WorldGenFeatureConfigured<?, ?> NETHER_SPROUTS = a(
		"nether_sprouts",
		WorldGenerator.NETHER_FOREST_VEGETATION.b(BiomeDecoratorGroups.a.m).a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(4)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TWISTING_VINES = a(
		"twisting_vines", WorldGenerator.TWISTING_VINES.b(WorldGenFeatureConfiguration.k).d(128).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> WEEPING_VINES = a(
		"weeping_vines", WorldGenerator.WEEPING_VINES.b(WorldGenFeatureConfiguration.k).d(128).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> BASALT_PILLAR = a(
		"basalt_pillar", WorldGenerator.BASALT_PILLAR.b(WorldGenFeatureConfiguration.k).d(128).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_COLD = a(
		"seagrass_cold", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.3F)).b(32).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_DEEP_COLD = a(
		"seagrass_deep_cold", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.8F)).b(40).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_NORMAL = a(
		"seagrass_normal", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.3F)).b(48).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_RIVER = a(
		"seagrass_river", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.4F)).b(48).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_DEEP = a(
		"seagrass_deep", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.8F)).b(48).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_SWAMP = a(
		"seagrass_swamp", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.6F)).b(64).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_WARM = a(
		"seagrass_warm", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.3F)).b(80).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_DEEP_WARM = a(
		"seagrass_deep_warm", WorldGenerator.SEAGRASS.b(new WorldGenFeatureConfigurationChance(0.8F)).b(80).a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEA_PICKLE = a(
		"sea_pickle", WorldGenerator.SEA_PICKLE.b(new WorldGenDecoratorFrequencyConfiguration(20)).a(BiomeDecoratorGroups.b.n).a(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> ICE_SPIKE = a(
		"ice_spike", WorldGenerator.ICE_SPIKE.b(WorldGenFeatureConfiguration.k).a(BiomeDecoratorGroups.b.l).b(3)
	);
	public static final WorldGenFeatureConfigured<?, ?> ICE_PATCH = a(
		"ice_patch",
		WorldGenerator.ICE_PATCH
			.b(
				new WorldGenFeatureCircleConfiguration(
					BiomeDecoratorGroups.c.B,
					IntSpread.a(2, 1),
					1,
					List.of(
						BiomeDecoratorGroups.c.ad,
						BiomeDecoratorGroups.c.t,
						BiomeDecoratorGroups.c.c,
						BiomeDecoratorGroups.c.d,
						BiomeDecoratorGroups.c.e,
						BiomeDecoratorGroups.c.f,
						BiomeDecoratorGroups.c.g
					)
				)
			)
			.a(BiomeDecoratorGroups.b.l)
			.b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> FOREST_ROCK = a(
		"forest_rock", WorldGenerator.FOREST_ROCK.b(new WorldGenFeatureLakeConfiguration(BiomeDecoratorGroups.c.at)).a(BiomeDecoratorGroups.b.l).c(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> SEAGRASS_SIMPLE = a(
		"seagrass_simple",
		WorldGenerator.SIMPLE_BLOCK
			.b(
				new WorldGenFeatureBlockConfiguration(
					BiomeDecoratorGroups.c.au,
					List.of(BiomeDecoratorGroups.c.ao),
					List.of(BiomeDecoratorGroups.c.ab),
					List.of(BiomeDecoratorGroups.c.ab)
				)
			)
			.a(WorldGenDecorator.q.b(new WorldGenDecoratorCarveMaskConfiguration(WorldGenStage.Features.LIQUID, 0.1F)))
	);
	public static final WorldGenFeatureConfigured<?, ?> ICEBERG_PACKED = a(
		"iceberg_packed",
		WorldGenerator.ICEBERG
			.b(new WorldGenFeatureLakeConfiguration(BiomeDecoratorGroups.c.B))
			.a(WorldGenDecorator.z.b(WorldGenFeatureEmptyConfiguration2.c))
			.a(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> ICEBERG_BLUE = a(
		"iceberg_blue",
		WorldGenerator.ICEBERG
			.b(new WorldGenFeatureLakeConfiguration(BiomeDecoratorGroups.c.C))
			.a(WorldGenDecorator.z.b(WorldGenFeatureEmptyConfiguration2.c))
			.a(200)
	);
	public static final WorldGenFeatureConfigured<?, ?> KELP_COLD = a(
		"kelp_cold",
		WorldGenerator.KELP
			.b(WorldGenFeatureConfiguration.k)
			.a(BiomeDecoratorGroups.b.f)
			.a()
			.a(WorldGenDecorator.e.b(new WorldGenDecoratorNoiseConfiguration(120, 80.0, 0.0)))
	);
	public static final WorldGenFeatureConfigured<?, ?> KELP_WARM = a(
		"kelp_warm",
		WorldGenerator.KELP
			.b(WorldGenFeatureConfiguration.k)
			.a(BiomeDecoratorGroups.b.f)
			.a()
			.a(WorldGenDecorator.e.b(new WorldGenDecoratorNoiseConfiguration(80, 80.0, 0.0)))
	);
	public static final WorldGenFeatureConfigured<?, ?> BLUE_ICE = a(
		"blue_ice",
		WorldGenerator.BLUE_ICE
			.b(WorldGenFeatureConfiguration.k)
			.a(WorldGenDecorator.l.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(30, 32, 64)))
			.a()
			.c(19)
	);
	public static final WorldGenFeatureConfigured<?, ?> BAMBOO_LIGHT = a(
		"bamboo_light", WorldGenerator.BAMBOO.b(new WorldGenFeatureConfigurationChance(0.0F)).a(BiomeDecoratorGroups.b.m).b(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> BAMBOO = a(
		"bamboo",
		WorldGenerator.BAMBOO
			.b(new WorldGenFeatureConfigurationChance(0.2F))
			.a(BiomeDecoratorGroups.b.g)
			.a()
			.a(WorldGenDecorator.e.b(new WorldGenDecoratorNoiseConfiguration(160, 80.0, 0.3)))
	);
	public static final WorldGenFeatureConfigured<?, ?> VINES = a("vines", WorldGenerator.VINES.b(WorldGenFeatureConfiguration.k).a().b(50));
	public static final WorldGenFeatureConfigured<?, ?> LAKE_WATER = a(
		"lake_water",
		WorldGenerator.LAKE
			.b(new WorldGenFeatureLakeConfiguration(BiomeDecoratorGroups.c.ab))
			.a(WorldGenDecorator.v.b(new WorldGenDecoratorDungeonConfiguration(4)))
	);
	public static final WorldGenFeatureConfigured<?, ?> LAKE_LAVA = a(
		"lake_lava",
		WorldGenerator.LAKE
			.b(new WorldGenFeatureLakeConfiguration(BiomeDecoratorGroups.c.ac))
			.a(WorldGenDecorator.u.b(new WorldGenDecoratorDungeonConfiguration(80)))
	);
	public static final WorldGenFeatureConfigured<?, ?> DISK_CLAY = a(
		"disk_clay",
		WorldGenerator.DISK
			.b(
				new WorldGenFeatureCircleConfiguration(
					BiomeDecoratorGroups.c.as, IntSpread.a(2, 1), 1, List.of(BiomeDecoratorGroups.c.ad, BiomeDecoratorGroups.c.as)
				)
			)
			.a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> DISK_GRAVEL = a(
		"disk_gravel",
		WorldGenerator.DISK
			.b(
				new WorldGenFeatureCircleConfiguration(
					BiomeDecoratorGroups.c.ae, IntSpread.a(2, 3), 2, List.of(BiomeDecoratorGroups.c.ad, BiomeDecoratorGroups.c.t)
				)
			)
			.a(BiomeDecoratorGroups.b.n)
	);
	public static final WorldGenFeatureConfigured<?, ?> DISK_SAND = a(
		"disk_sand",
		WorldGenerator.DISK
			.b(
				new WorldGenFeatureCircleConfiguration(
					BiomeDecoratorGroups.c.ar, IntSpread.a(2, 4), 2, List.of(BiomeDecoratorGroups.c.ad, BiomeDecoratorGroups.c.t)
				)
			)
			.a(BiomeDecoratorGroups.b.n)
			.b(3)
	);
	public static final WorldGenFeatureConfigured<?, ?> FREEZE_TOP_LAYER = a(
		"freeze_top_layer", WorldGenerator.FREEZE_TOP_LAYER.b(WorldGenFeatureConfiguration.k)
	);
	public static final WorldGenFeatureConfigured<?, ?> BONUS_CHEST = a("bonus_chest", WorldGenerator.BONUS_CHEST.b(WorldGenFeatureConfiguration.k));
	public static final WorldGenFeatureConfigured<?, ?> VOID_START_PLATFORM = a(
		"void_start_platform", WorldGenerator.VOID_START_PLATFORM.b(WorldGenFeatureConfiguration.k)
	);
	public static final WorldGenFeatureConfigured<?, ?> MONSTER_ROOM = a(
		"monster_room", WorldGenerator.MONSTER_ROOM.b(WorldGenFeatureConfiguration.k).d(256).a().b(8)
	);
	public static final WorldGenFeatureConfigured<?, ?> DESERT_WELL = a(
		"desert_well", WorldGenerator.DESERT_WELL.b(WorldGenFeatureConfiguration.k).a(BiomeDecoratorGroups.b.l).a(1000)
	);
	public static final WorldGenFeatureConfigured<?, ?> FOSSIL = a("fossil", WorldGenerator.FOSSIL.b(WorldGenFeatureConfiguration.k).a(64));
	public static final WorldGenFeatureConfigured<?, ?> SPRING_LAVA_DOUBLE = a(
		"spring_lava_double",
		WorldGenerator.SPRING_FEATURE
			.b(BiomeDecoratorGroups.a.i)
			.a(WorldGenDecorator.n.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(8, 16, 256)))
			.a()
			.b(40)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_LAVA = a(
		"spring_lava",
		WorldGenerator.SPRING_FEATURE
			.b(BiomeDecoratorGroups.a.i)
			.a(WorldGenDecorator.n.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(8, 16, 256)))
			.a()
			.b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_DELTA = a(
		"spring_delta",
		WorldGenerator.SPRING_FEATURE
			.b(
				new WorldGenFeatureHellFlowingLavaConfiguration(
					BiomeDecoratorGroups.c.aa,
					true,
					4,
					1,
					ImmutableSet.of(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.GRAVEL, Blocks.MAGMA_BLOCK, Blocks.BLACKSTONE)
				)
			)
			.a(BiomeDecoratorGroups.b.j)
			.a()
			.b(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_CLOSED = a(
		"spring_closed", WorldGenerator.SPRING_FEATURE.b(BiomeDecoratorGroups.a.j).a(BiomeDecoratorGroups.b.i).a().b(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_CLOSED_DOUBLE = a(
		"spring_closed_double", WorldGenerator.SPRING_FEATURE.b(BiomeDecoratorGroups.a.j).a(BiomeDecoratorGroups.b.i).a().b(32)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_OPEN = a(
		"spring_open",
		WorldGenerator.SPRING_FEATURE
			.b(new WorldGenFeatureHellFlowingLavaConfiguration(BiomeDecoratorGroups.c.aa, false, 4, 1, ImmutableSet.of(Blocks.NETHERRACK)))
			.a(BiomeDecoratorGroups.b.j)
			.a()
			.b(8)
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRING_WATER = a(
		"spring_water",
		WorldGenerator.SPRING_FEATURE
			.b(
				new WorldGenFeatureHellFlowingLavaConfiguration(
					BiomeDecoratorGroups.c.Z, true, 4, 1, ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE)
				)
			)
			.a(WorldGenDecorator.m.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(8, 8, 256)))
			.a()
			.b(50)
	);
	public static final WorldGenFeatureConfigured<?, ?> PILE_HAY = a(
		"pile_hay", WorldGenerator.BLOCK_PILE.b(new WorldGenFeatureBlockPileConfiguration(new WorldGenFeatureStateProviderRotatedBlock(Blocks.HAY_BLOCK)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PILE_MELON = a(
		"pile_melon", WorldGenerator.BLOCK_PILE.b(new WorldGenFeatureBlockPileConfiguration(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.I)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PILE_SNOW = a(
		"pile_snow", WorldGenerator.BLOCK_PILE.b(new WorldGenFeatureBlockPileConfiguration(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.R)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PILE_ICE = a(
		"pile_ice",
		WorldGenerator.BLOCK_PILE
			.b(
				new WorldGenFeatureBlockPileConfiguration(
					new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.C, 1).a(BiomeDecoratorGroups.c.B, 5)
				)
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> PILE_PUMPKIN = a(
		"pile_pumpkin",
		WorldGenerator.BLOCK_PILE
			.b(
				new WorldGenFeatureBlockPileConfiguration(
					new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.J, 19).a(BiomeDecoratorGroups.c.S, 1)
				)
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_FIRE = a(
		"patch_fire",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.L), WorldGenBlockPlacerSimple.c)
					.a(64)
					.a(ImmutableSet.of(BiomeDecoratorGroups.c.N.getBlock()))
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.d)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SOUL_FIRE = a(
		"patch_soul_fire",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.M), new WorldGenBlockPlacerSimple())
					.a(64)
					.a(ImmutableSet.of(BiomeDecoratorGroups.c.O.getBlock()))
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.d)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_BROWN_MUSHROOM = a(
		"patch_brown_mushroom",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.z), WorldGenBlockPlacerSimple.c)
					.a(64)
					.b()
					.d()
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_RED_MUSHROOM = a(
		"patch_red_mushroom",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.A), WorldGenBlockPlacerSimple.c)
					.a(64)
					.b()
					.d()
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_CRIMSON_ROOTS = a(
		"patch_crimson_roots",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.P), new WorldGenBlockPlacerSimple())
					.a(64)
					.b()
					.d()
			)
			.d(128)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SUNFLOWER = a(
		"patch_sunflower",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.T), new WorldGenBlockPlacerDoublePlant()
				)
					.a(64)
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
			.b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_PUMPKIN = a(
		"patch_pumpkin",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.J), WorldGenBlockPlacerSimple.c)
					.a(64)
					.a(ImmutableSet.of(BiomeDecoratorGroups.c.t.getBlock()))
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.m)
			.a(32)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_TAIGA_GRASS = a("patch_taiga_grass", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.b));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_BERRY_BUSH = a("patch_berry_bush", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.f));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_PLAIN = a(
		"patch_grass_plain",
		WorldGenerator.RANDOM_PATCH
			.b(BiomeDecoratorGroups.a.a)
			.a(BiomeDecoratorGroups.b.m)
			.a(WorldGenDecorator.d.b(new WorldGenFeatureDecoratorNoiseConfiguration(-0.8, 5, 10)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_FOREST = a(
		"patch_grass_forest", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.a).a(BiomeDecoratorGroups.b.m).b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_BADLANDS = a(
		"patch_grass_badlands", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.a).a(BiomeDecoratorGroups.b.m)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_SAVANNA = a(
		"patch_grass_savanna", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.a).a(BiomeDecoratorGroups.b.m).b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_NORMAL = a(
		"patch_grass_normal", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.a).a(BiomeDecoratorGroups.b.m).b(5)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_TAIGA_2 = a(
		"patch_grass_taiga_2", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.b).a(BiomeDecoratorGroups.b.m)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_TAIGA = a(
		"patch_grass_taiga", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.b).a(BiomeDecoratorGroups.b.m).b(7)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_GRASS_JUNGLE = a(
		"patch_grass_jungle", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.c).a(BiomeDecoratorGroups.b.m).b(25)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_DEAD_BUSH_2 = a(
		"patch_dead_bush_2", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.e).a(BiomeDecoratorGroups.b.m).b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_DEAD_BUSH = a(
		"patch_dead_bush", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.e).a(BiomeDecoratorGroups.b.m)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_DEAD_BUSH_BADLANDS = a(
		"patch_dead_bush_badlands", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.e).a(BiomeDecoratorGroups.b.m).b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_MELON = a(
		"patch_melon",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.I), WorldGenBlockPlacerSimple.c)
					.a(64)
					.a(ImmutableSet.of(BiomeDecoratorGroups.c.t.getBlock()))
					.a()
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.m)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_BERRY_SPARSE = a("patch_berry_sparse", PATCH_BERRY_BUSH.a(BiomeDecoratorGroups.b.m));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_BERRY_DECORATED = a("patch_berry_decorated", PATCH_BERRY_BUSH.a(BiomeDecoratorGroups.b.m).a(12));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_WATERLILLY = a(
		"patch_waterlilly",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.Q), WorldGenBlockPlacerSimple.c)
					.a(10)
					.d()
			)
			.a(BiomeDecoratorGroups.b.m)
			.b(4)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_TALL_GRASS_2 = a(
		"patch_tall_grass_2",
		WorldGenerator.RANDOM_PATCH
			.b(BiomeDecoratorGroups.a.g)
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.e)
			.a()
			.a(WorldGenDecorator.d.b(new WorldGenFeatureDecoratorNoiseConfiguration(-0.8, 0, 7)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_TALL_GRASS = a(
		"patch_tall_grass", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.g).a(BiomeDecoratorGroups.b.k).a(BiomeDecoratorGroups.b.l).b(7)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_LARGE_FERN = a(
		"patch_large_fern",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.u), new WorldGenBlockPlacerDoublePlant()
				)
					.a(64)
					.b()
					.d()
			)
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
			.b(7)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_CACTUS = a(
		"patch_cactus",
		WorldGenerator.RANDOM_PATCH
			.b(
				new WorldGenFeatureRandomPatchConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.U), new WorldGenBlockPlacerColumn(1, 2)
				)
					.a(10)
					.b()
					.d()
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_CACTUS_DESERT = a("patch_cactus_desert", PATCH_CACTUS.a(BiomeDecoratorGroups.b.m).b(10));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_CACTUS_DECORATED = a("patch_cactus_decorated", PATCH_CACTUS.a(BiomeDecoratorGroups.b.m).b(5));
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SUGAR_CANE_SWAMP = a(
		"patch_sugar_cane_swamp", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.h).a(BiomeDecoratorGroups.b.m).b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SUGAR_CANE_DESERT = a(
		"patch_sugar_cane_desert", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.h).a(BiomeDecoratorGroups.b.m).b(60)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SUGAR_CANE_BADLANDS = a(
		"patch_sugar_cane_badlands", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.h).a(BiomeDecoratorGroups.b.m).b(13)
	);
	public static final WorldGenFeatureConfigured<?, ?> PATCH_SUGAR_CANE = a(
		"patch_sugar_cane", WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.h).a(BiomeDecoratorGroups.b.m).b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> BROWN_MUSHROOM_NETHER = a("brown_mushroom_nether", PATCH_BROWN_MUSHROOM.d(128).a(2));
	public static final WorldGenFeatureConfigured<?, ?> RED_MUSHROOM_NETHER = a("red_mushroom_nether", PATCH_RED_MUSHROOM.d(128).a(2));
	public static final WorldGenFeatureConfigured<?, ?> BROWN_MUSHROOM_NORMAL = a(
		"brown_mushroom_normal", PATCH_BROWN_MUSHROOM.a(BiomeDecoratorGroups.b.m).a(4)
	);
	public static final WorldGenFeatureConfigured<?, ?> RED_MUSHROOM_NORMAL = a("red_mushroom_normal", PATCH_RED_MUSHROOM.a(BiomeDecoratorGroups.b.m).a(8));
	public static final WorldGenFeatureConfigured<?, ?> BROWN_MUSHROOM_TAIGA = a("brown_mushroom_taiga", PATCH_BROWN_MUSHROOM.a(4).a(BiomeDecoratorGroups.b.l));
	public static final WorldGenFeatureConfigured<?, ?> BROWN_MUSHROOM_GIANT = a("brown_mushroom_giant", BROWN_MUSHROOM_TAIGA.b(3));
	public static final WorldGenFeatureConfigured<?, ?> BROWN_MUSHROOM_SWAMP = a("brown_mushroom_swamp", BROWN_MUSHROOM_TAIGA.b(8));
	public static final WorldGenFeatureConfigured<?, ?> RED_MUSHROOM_TAIGA = a("red_mushroom_taiga", PATCH_RED_MUSHROOM.a(8).a(BiomeDecoratorGroups.b.m));
	public static final WorldGenFeatureConfigured<?, ?> RED_MUSHROOM_GIANT = a("red_mushroom_giant", RED_MUSHROOM_TAIGA.b(3));
	public static final WorldGenFeatureConfigured<?, ?> RED_MUSHROOM_SWAMP = a("red_mushroom_swamp", RED_MUSHROOM_TAIGA.b(8));
	public static final WorldGenFeatureConfigured<?, ?> ORE_MAGMA = a(
		"ore_magma",
		WorldGenerator.ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.av, 33))
			.a(WorldGenDecorator.s.b(WorldGenFeatureEmptyConfiguration2.c))
			.a()
			.b(4)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_SOUL_SAND = a(
		"ore_soul_sand", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.aw, 12)).d(32).a().b(12)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GOLD_DELTAS = a(
		"ore_gold_deltas",
		WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.ax, 10)).a(BiomeDecoratorGroups.b.i).a().b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_QUARTZ_DELTAS = a(
		"ore_quartz_deltas",
		WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.ay, 14)).a(BiomeDecoratorGroups.b.i).a().b(32)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GOLD_NETHER = a(
		"ore_gold_nether",
		WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.ax, 10)).a(BiomeDecoratorGroups.b.i).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_QUARTZ_NETHER = a(
		"ore_quartz_nether",
		WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.ay, 14)).a(BiomeDecoratorGroups.b.i).a().b(16)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GRAVEL_NETHER = a(
		"ore_gravel_nether",
		WorldGenerator.ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.ae, 33))
			.a(WorldGenDecorator.l.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(5, 0, 37)))
			.a()
			.b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_BLACKSTONE = a(
		"ore_blackstone",
		WorldGenerator.ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHERRACK, BiomeDecoratorGroups.c.az, 33))
			.a(WorldGenDecorator.l.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(5, 10, 37)))
			.a()
			.b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_DIRT = a(
		"ore_dirt", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ad, 33)).d(256).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GRAVEL = a(
		"ore_gravel", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ae, 33)).d(256).a().b(8)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GRANITE = a(
		"ore_granite", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.af, 33)).d(80).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_DIORITE = a(
		"ore_diorite", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ag, 33)).d(80).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_ANDESITE = a(
		"ore_andesite", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ah, 33)).d(80).a().b(10)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_COAL = a(
		"ore_coal", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ai, 17)).d(128).a().b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_IRON = a(
		"ore_iron", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.aj, 9)).d(64).a().b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GOLD_EXTRA = a(
		"ore_gold_extra",
		WorldGenerator.ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ak, 9))
			.a(WorldGenDecorator.l.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(32, 32, 80)))
			.a()
			.b(20)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_GOLD = a(
		"ore_gold", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.ak, 9)).d(32).a().b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_REDSTONE = a(
		"ore_redstone", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.al, 8)).d(16).a().b(8)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_DIAMOND = a(
		"ore_diamond", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.am, 8)).d(16).a()
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_LAPIS = a(
		"ore_lapis",
		WorldGenerator.ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.an, 7))
			.a(WorldGenDecorator.o.b(new WorldGenDecoratorHeightAverageConfiguration(16, 16)))
			.a()
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_INFESTED = a(
		"ore_infested", WorldGenerator.ORE.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NATURAL_STONE, BiomeDecoratorGroups.c.aq, 9)).d(64).a().b(7)
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_EMERALD = a(
		"ore_emerald",
		WorldGenerator.EMERALD_ORE
			.b(new WorldGenFeatureReplaceBlockConfiguration(BiomeDecoratorGroups.c.ao, BiomeDecoratorGroups.c.ap))
			.a(WorldGenDecorator.t.b(WorldGenFeatureDecoratorConfiguration.b))
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_DEBRIS_LARGE = a(
		"ore_debris_large",
		WorldGenerator.NO_SURFACE_ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHER_ORE_REPLACEABLES, BiomeDecoratorGroups.c.aA, 3))
			.a(WorldGenDecorator.o.b(new WorldGenDecoratorHeightAverageConfiguration(16, 8)))
			.a()
	);
	public static final WorldGenFeatureConfigured<?, ?> ORE_DEBRIS_SMALL = a(
		"ore_debris_small",
		WorldGenerator.NO_SURFACE_ORE
			.b(new WorldGenFeatureOreConfiguration(WorldGenFeatureOreConfiguration.Target.NETHER_ORE_REPLACEABLES, BiomeDecoratorGroups.c.aA, 2))
			.a(WorldGenDecorator.l.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(8, 16, 128)))
			.a()
	);
	public static final WorldGenFeatureConfigured<?, ?> CRIMSON_FUNGI = a(
		"crimson_fungi",
		WorldGenerator.HUGE_FUNGUS.b(WorldGenFeatureHugeFungiConfiguration.c).a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(8)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureHugeFungiConfiguration, ?> CRIMSON_FUNGI_PLANTED = a(
		"crimson_fungi_planted", WorldGenerator.HUGE_FUNGUS.b(WorldGenFeatureHugeFungiConfiguration.CRIMSON_FUNGUS)
	);
	public static final WorldGenFeatureConfigured<?, ?> WARPED_FUNGI = a(
		"warped_fungi",
		WorldGenerator.HUGE_FUNGUS.b(WorldGenFeatureHugeFungiConfiguration.e).a(WorldGenDecorator.C.b(new WorldGenDecoratorFrequencyConfiguration(8)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureHugeFungiConfiguration, ?> WARPED_FUNGI_PLANTED = a(
		"warped_fungi_planted", WorldGenerator.HUGE_FUNGUS.b(WorldGenFeatureHugeFungiConfiguration.WARPED_FUNGUS)
	);
	public static final WorldGenFeatureConfigured<?, ?> HUGE_BROWN_MUSHROOM = a(
		"huge_brown_mushroom",
		WorldGenerator.HUGE_BROWN_MUSHROOM
			.b(
				new WorldGenFeatureMushroomConfiguration(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.X), new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.Y), 3
				)
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> HUGE_RED_MUSHROOM = a(
		"huge_red_mushroom",
		WorldGenerator.HUGE_RED_MUSHROOM
			.b(
				new WorldGenFeatureMushroomConfiguration(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.W), new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.Y), 2
				)
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> OAK = a(
		"oak",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.h),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.i),
					new WorldGenFoilagePlacerBlob(IntSpread.a(2), IntSpread.a(0), 3),
					new TrunkPlacerStraight(4, 2, 0),
					new FeatureSizeTwoLayers(1, 0, 1)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> DARK_OAK = a(
		"dark_oak",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.r),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.s),
					new WorldGenFoilagePlacerDarkOak(IntSpread.a(0), IntSpread.a(0)),
					new TrunkPlacerDarkOak(6, 2, 1),
					new FeatureSizeThreeLayers(1, 1, 0, 1, 2, OptionalInt.empty())
				)
					.a(Integer.MAX_VALUE)
					.a(HeightMap.Type.MOTION_BLOCKING)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> BIRCH = a(
		"birch",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.p),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.q),
					new WorldGenFoilagePlacerBlob(IntSpread.a(2), IntSpread.a(0), 3),
					new TrunkPlacerStraight(5, 2, 0),
					new FeatureSizeTwoLayers(1, 0, 1)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> ACACIA = a(
		"acacia",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.n),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.o),
					new WorldGenFoilagePlacerAcacia(IntSpread.a(2), IntSpread.a(0)),
					new TrunkPlacerForking(5, 2, 2),
					new FeatureSizeTwoLayers(1, 0, 2)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> SPRUCE = a(
		"spruce",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.l),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.m),
					new WorldGenFoilagePlacerSpruce(IntSpread.a(2, 1), IntSpread.a(0, 2), IntSpread.a(1, 1)),
					new TrunkPlacerStraight(5, 2, 1),
					new FeatureSizeTwoLayers(2, 0, 2)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> PINE = a(
		"pine",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.l),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.m),
					new WorldGenFoilagePlacerPine(IntSpread.a(1), IntSpread.a(1), IntSpread.a(3, 1)),
					new TrunkPlacerStraight(6, 4, 0),
					new FeatureSizeTwoLayers(2, 0, 2)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> JUNGLE_TREE = a(
		"jungle_tree",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.j),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.k),
					new WorldGenFoilagePlacerBlob(IntSpread.a(2), IntSpread.a(0), 3),
					new TrunkPlacerStraight(4, 8, 0),
					new FeatureSizeTwoLayers(1, 0, 1)
				)
					.a(List.of(new WorldGenFeatureTreeCocoa(0.2F), WorldGenFeatureTreeVineTrunk.b, WorldGenFeatureTreeVineLeaves.b))
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> FANCY_OAK = a(
		"fancy_oak",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.h),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.i),
					new WorldGenFoilagePlacerFancy(IntSpread.a(2), IntSpread.a(4), 4),
					new TrunkPlacerFancy(3, 11, 0),
					new FeatureSizeTwoLayers(0, 0, 0, OptionalInt.of(4))
				)
					.a()
					.a(HeightMap.Type.MOTION_BLOCKING)
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> JUNGLE_TREE_NO_VINE = a(
		"jungle_tree_no_vine",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.j),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.k),
					new WorldGenFoilagePlacerBlob(IntSpread.a(2), IntSpread.a(0), 3),
					new TrunkPlacerStraight(4, 8, 0),
					new FeatureSizeTwoLayers(1, 0, 1)
				)
					.a()
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> MEGA_JUNGLE_TREE = a(
		"mega_jungle_tree",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.j),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.k),
					new WorldGenFoilagePlacerJungle(IntSpread.a(2), IntSpread.a(0), 2),
					new TrunkPlacerMegaJungle(10, 2, 19),
					new FeatureSizeTwoLayers(1, 1, 2)
				)
					.a(List.of(WorldGenFeatureTreeVineTrunk.b, WorldGenFeatureTreeVineLeaves.b))
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> MEGA_SPRUCE = a(
		"mega_spruce",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.l),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.m),
					new WorldGenFoilagePlacerMegaPine(IntSpread.a(0), IntSpread.a(0), IntSpread.a(13, 4)),
					new TrunkPlacerGiant(13, 2, 14),
					new FeatureSizeTwoLayers(1, 1, 2)
				)
					.a(List.of(new WorldGenFeatureTreeAlterGround(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.c))))
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> MEGA_PINE = a(
		"mega_pine",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.l),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.m),
					new WorldGenFoilagePlacerMegaPine(IntSpread.a(0), IntSpread.a(0), IntSpread.a(3, 4)),
					new TrunkPlacerGiant(13, 2, 14),
					new FeatureSizeTwoLayers(1, 1, 2)
				)
					.a(List.of(new WorldGenFeatureTreeAlterGround(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.c))))
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> SUPER_BIRCH_BEES_0002 = a(
		"super_birch_bees_0002",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.p),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.q),
					new WorldGenFoilagePlacerBlob(IntSpread.a(2), IntSpread.a(0), 3),
					new TrunkPlacerStraight(5, 2, 6),
					new FeatureSizeTwoLayers(1, 0, 1)
				)
					.a()
					.a(List.of(BiomeDecoratorGroups.b.a))
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<?, ?> SWAMP_TREE = a(
		"swamp_tree",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.h),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.i),
					new WorldGenFoilagePlacerBlob(IntSpread.a(3), IntSpread.a(0), 3),
					new TrunkPlacerStraight(5, 3, 0),
					new FeatureSizeTwoLayers(1, 0, 1)
				).a(1)
					.a(List.of(WorldGenFeatureTreeVineLeaves.b))
					.b()
			)
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(2, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> JUNGLE_BUSH = a(
		"jungle_bush",
		WorldGenerator.TREE
			.b(
				new WorldGenFeatureTreeConfiguration.a(
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.j),
					new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.i),
					new WorldGenFoilagePlacerBush(IntSpread.a(2), IntSpread.a(1), 2),
					new TrunkPlacerStraight(1, 0, 0),
					new FeatureSizeTwoLayers(0, 0, 0)
				)
					.a(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES)
					.b()
			)
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> OAK_BEES_0002 = a(
		"oak_bees_0002", WorldGenerator.TREE.b(OAK.c().a(List.of(BiomeDecoratorGroups.b.a)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> OAK_BEES_002 = a(
		"oak_bees_002", WorldGenerator.TREE.b(OAK.c().a(List.of(BiomeDecoratorGroups.b.b)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> OAK_BEES_005 = a(
		"oak_bees_005", WorldGenerator.TREE.b(OAK.c().a(List.of(BiomeDecoratorGroups.b.c)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> BIRCH_BEES_0002 = a(
		"birch_bees_0002", WorldGenerator.TREE.b(BIRCH.c().a(List.of(BiomeDecoratorGroups.b.a)))
	);
	public static final WorldGenFeatureConfigured<?, ?> BIRCH_TALL = a(
		"birch_tall",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(SUPER_BIRCH_BEES_0002.a(0.5F)), BIRCH_BEES_0002))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_BIRCH = a(
		"trees_birch",
		BIRCH_BEES_0002.a(BiomeDecoratorGroups.b.l).a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> BIRCH_BEES_002 = a(
		"birch_bees_002", WorldGenerator.TREE.b(BIRCH.c().a(List.of(BiomeDecoratorGroups.b.b)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> BIRCH_BEES_005 = a(
		"birch_bees_005", WorldGenerator.TREE.b(BIRCH.c().a(List.of(BiomeDecoratorGroups.b.c)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> FANCY_OAK_BEES_0002 = a(
		"fancy_oak_bees_0002", WorldGenerator.TREE.b(FANCY_OAK.c().a(List.of(BiomeDecoratorGroups.b.a)))
	);
	public static final WorldGenFeatureConfigured<?, ?> BIRCH_OTHER = a(
		"birch_other",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(BIRCH_BEES_0002.a(0.2F), FANCY_OAK_BEES_0002.a(0.1F)), OAK_BEES_0002))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> FANCY_OAK_BEES_002 = a(
		"fancy_oak_bees_002", WorldGenerator.TREE.b(FANCY_OAK.c().a(List.of(BiomeDecoratorGroups.b.b)))
	);
	public static final WorldGenFeatureConfigured<?, ?> FOREST_FLOWER_TREES = a(
		"forest_flower_trees",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(BIRCH_BEES_002.a(0.2F), FANCY_OAK_BEES_002.a(0.1F)), OAK_BEES_002))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(6, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> FANCY_OAK_BEES_005 = a(
		"fancy_oak_bees_005", WorldGenerator.TREE.b(FANCY_OAK.c().a(List.of(BiomeDecoratorGroups.b.c)))
	);
	public static final WorldGenFeatureConfigured<?, ?> PLAIN_VEGETATION = a(
		"plain_vegetation",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(FANCY_OAK_BEES_005.a(0.33333334F)), OAK_BEES_005))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(0, 0.05F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> OAK_BADLANDS = a(
		"oak_badlands", OAK.a(BiomeDecoratorGroups.b.l).a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(5, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> SPRUCE_SNOWY = a(
		"spruce_snowy", SPRUCE.a(BiomeDecoratorGroups.b.l).a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(0, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_WARM = a(
		"flower_warm", WorldGenerator.FLOWER.b(BiomeDecoratorGroups.a.d).a(BiomeDecoratorGroups.b.k).a(BiomeDecoratorGroups.b.l).b(4)
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_DEFAULT = a(
		"flower_default", WorldGenerator.FLOWER.b(BiomeDecoratorGroups.a.d).a(BiomeDecoratorGroups.b.k).a(BiomeDecoratorGroups.b.l).b(2)
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_FOREST = a(
		"flower_forest",
		WorldGenerator.FLOWER
			.b(new WorldGenFeatureRandomPatchConfiguration.a(WorldGenFeatureStateProviderForestFlower.c, WorldGenBlockPlacerSimple.c).a(64).d())
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
			.b(100)
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_SWAMP = a(
		"flower_swamp",
		WorldGenerator.FLOWER.b(
				new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.E), WorldGenBlockPlacerSimple.c)
					.a(64)
					.d()
			)
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_PLAIN = a(
		"flower_plain",
		WorldGenerator.FLOWER
			.b(new WorldGenFeatureRandomPatchConfiguration.a(WorldGenFeatureStateProviderPlainFlower.c, WorldGenBlockPlacerSimple.c).a(64).d())
	);
	public static final WorldGenFeatureConfigured<?, ?> FLOWER_PLAIN_DECORATED = a(
		"flower_plain_decorated",
		FLOWER_PLAIN.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.e)
			.a()
			.a(WorldGenDecorator.d.b(new WorldGenFeatureDecoratorNoiseConfiguration(-0.8, 15, 4)))
	);
	public static final WorldGenFeatureConfigured<?, ?> DARK_FOREST_VEGETATION_BROWN = a(
		"dark_forest_vegetation_brown",
		WorldGenerator.RANDOM_SELECTOR.b(
				new WorldGenFeatureRandomChoiceConfiguration(
					List.of(HUGE_BROWN_MUSHROOM.a(0.025F), HUGE_RED_MUSHROOM.a(0.05F), DARK_OAK.a(0.6666667F), BIRCH.a(0.2F), FANCY_OAK.a(0.1F)), OAK
				)
			)
			.a(WorldGenDecorator.y.b(WorldGenFeatureDecoratorConfiguration.b))
	);
	public static final WorldGenFeatureConfigured<?, ?> DARK_FOREST_VEGETATION_RED = a(
		"dark_forest_vegetation_red",
		WorldGenerator.RANDOM_SELECTOR.b(
				new WorldGenFeatureRandomChoiceConfiguration(
					List.of(HUGE_RED_MUSHROOM.a(0.025F), HUGE_BROWN_MUSHROOM.a(0.05F), DARK_OAK.a(0.6666667F), BIRCH.a(0.2F), FANCY_OAK.a(0.1F)), OAK
				)
			)
			.a(WorldGenDecorator.y.b(WorldGenFeatureDecoratorConfiguration.b))
	);
	public static final WorldGenFeatureConfigured<?, ?> WARM_OCEAN_VEGETATION = a(
		"warm_ocean_vegetation",
		WorldGenerator.SIMPLE_RANDOM_SELECTOR.b(
				new WorldGenFeatureRandom2(List.of(
					() -> WorldGenerator.CORAL_TREE.b(WorldGenFeatureConfiguration.k),
					() -> WorldGenerator.CORAL_CLAW.b(WorldGenFeatureConfiguration.k),
					() -> WorldGenerator.CORAL_MUSHROOM.b(WorldGenFeatureConfiguration.k)
				))
			)
			.a(BiomeDecoratorGroups.b.f)
			.a()
			.a(WorldGenDecorator.e.b(new WorldGenDecoratorNoiseConfiguration(20, 400.0, 0.0)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TAIGA_VEGETATION = a(
		"taiga_vegetation",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(PINE.a(0.33333334F)), SPRUCE))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_SHATTERED_SAVANNA = a(
		"trees_shattered_savanna",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(ACACIA.a(0.8F)), OAK))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(2, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_SAVANNA = a(
		"trees_savanna",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(ACACIA.a(0.8F)), OAK))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(1, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_MOUNTAIN_EDGE = a(
		"trees_mountain_edge",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(SPRUCE.a(0.666F), FANCY_OAK.a(0.1F)), OAK))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(3, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_MOUNTAIN = a(
		"trees_mountain",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(SPRUCE.a(0.666F), FANCY_OAK.a(0.1F)), OAK))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(0, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_WATER = a(
		"trees_water",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(FANCY_OAK.a(0.1F)), OAK))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(0, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_JUNGLE_EDGE = a(
		"trees_jungle_edge",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(FANCY_OAK.a(0.1F), JUNGLE_BUSH.a(0.5F)), JUNGLE_TREE))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(2, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_GIANT_SPRUCE = a(
		"trees_giant_spruce",
		WorldGenerator.RANDOM_SELECTOR
			.b(new WorldGenFeatureRandomChoiceConfiguration(List.of(MEGA_SPRUCE.a(0.33333334F), PINE.a(0.33333334F)), SPRUCE))
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_GIANT = a(
		"trees_giant",
		WorldGenerator.RANDOM_SELECTOR
			.b(
				new WorldGenFeatureRandomChoiceConfiguration(
					List.of(MEGA_SPRUCE.a(0.025641026F), MEGA_PINE.a(0.30769232F), PINE.a(0.33333334F)), SPRUCE
				)
			)
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(10, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> TREES_JUNGLE = a(
		"trees_jungle",
		WorldGenerator.RANDOM_SELECTOR
			.b(
				new WorldGenFeatureRandomChoiceConfiguration(
					List.of(FANCY_OAK.a(0.1F), JUNGLE_BUSH.a(0.5F), MEGA_JUNGLE_TREE.a(0.33333334F)), JUNGLE_TREE
				)
			)
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(50, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> BAMBOO_VEGETATION = a(
		"bamboo_vegetation",
		WorldGenerator.RANDOM_SELECTOR
			.b(
				new WorldGenFeatureRandomChoiceConfiguration(
					List.of(FANCY_OAK.a(0.05F), JUNGLE_BUSH.a(0.15F), MEGA_JUNGLE_TREE.a(0.7F)),
					WorldGenerator.RANDOM_PATCH.b(BiomeDecoratorGroups.a.c)
				)
			)
			.a(BiomeDecoratorGroups.b.l)
			.a(WorldGenDecorator.f.b(new WorldGenDecoratorFrequencyExtraChanceConfiguration(30, 0.1F, 1)))
	);
	public static final WorldGenFeatureConfigured<?, ?> MUSHROOM_FIELD_VEGETATION = a(
		"mushroom_field_vegetation",
		WorldGenerator.RANDOM_BOOLEAN_SELECTOR
			.b(new WorldGenFeatureChoiceConfiguration(() -> HUGE_RED_MUSHROOM, () -> HUGE_BROWN_MUSHROOM))
			.a(BiomeDecoratorGroups.b.l)
	);
	private static final List<Supplier<WorldGenFeatureConfigured<?, ?>>> cJ = List.of(
		() -> WorldGenerator.RANDOM_PATCH.b(
			new WorldGenFeatureRandomPatchConfiguration.a(
				new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.w), new WorldGenBlockPlacerDoublePlant()
			)
				.a(64)
				.b()
				.d()
		),
		() -> WorldGenerator.RANDOM_PATCH.b(
			new WorldGenFeatureRandomPatchConfiguration.a(
				new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.x), new WorldGenBlockPlacerDoublePlant()
			)
				.a(64)
				.b()
				.d()
		),
		() -> WorldGenerator.RANDOM_PATCH.b(
			new WorldGenFeatureRandomPatchConfiguration.a(
				new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.y), new WorldGenBlockPlacerDoublePlant()
			)
				.a(64)
				.b()
				.d()
		),
		() -> WorldGenerator.NO_BONEMEAL_FLOWER.b(
			new WorldGenFeatureRandomPatchConfiguration.a(new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.D), WorldGenBlockPlacerSimple.c)
				.a(64)
				.d()
		)
	);
	public static final WorldGenFeatureConfigured<?, ?> FOREST_FLOWER_VEGETATION_COMMON = a(
		"forest_flower_vegetation_common",
		WorldGenerator.SIMPLE_RANDOM_SELECTOR
			.b(new WorldGenFeatureRandom2(cJ))
			.a(IntSpread.a(-1, 4))
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
			.b(5)
	);
	public static final WorldGenFeatureConfigured<?, ?> FOREST_FLOWER_VEGETATION = a(
		"forest_flower_vegetation",
		WorldGenerator.SIMPLE_RANDOM_SELECTOR
			.b(new WorldGenFeatureRandom2(cJ))
			.a(IntSpread.a(-3, 4))
			.a(BiomeDecoratorGroups.b.k)
			.a(BiomeDecoratorGroups.b.l)
			.b(5)
	);

	private static <FC extends WorldGenFeatureConfiguration> WorldGenFeatureConfigured<FC, ?> a(String var0, WorldGenFeatureConfigured<FC, ?> var1) {
		return IRegistry.a(RegistryGeneration.e, var0, var1);
	}

	public static final class a {
		public static final WorldGenFeatureRandomPatchConfiguration a = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.a), WorldGenBlockPlacerSimple.c
		)
			.a(32)
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration b = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.a, 1).a(BiomeDecoratorGroups.c.b, 4), WorldGenBlockPlacerSimple.c
		)
			.a(32)
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration c = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.a, 3).a(BiomeDecoratorGroups.c.b, 1), WorldGenBlockPlacerSimple.c
		)
			.b(ImmutableSet.of(BiomeDecoratorGroups.c.c))
			.a(32)
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration d = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.F, 2).a(BiomeDecoratorGroups.c.G, 1), WorldGenBlockPlacerSimple.c
		)
			.a(64)
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration e = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.H), WorldGenBlockPlacerSimple.c
		)
			.a(4)
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration f = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.K), WorldGenBlockPlacerSimple.c
		)
			.a(64)
			.a(ImmutableSet.of(BiomeDecoratorGroups.c.t.getBlock()))
			.b()
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration g = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.v), new WorldGenBlockPlacerDoublePlant()
		)
			.a(64)
			.b()
			.d();
		public static final WorldGenFeatureRandomPatchConfiguration h = new WorldGenFeatureRandomPatchConfiguration.a(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.V), new WorldGenBlockPlacerColumn(2, 2)
		)
			.a(20)
			.b(4)
			.c(0)
			.d(4)
			.b()
			.c()
			.d();
		public static final WorldGenFeatureHellFlowingLavaConfiguration i = new WorldGenFeatureHellFlowingLavaConfiguration(
			BiomeDecoratorGroups.c.aa, true, 4, 1, ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE)
		);
		public static final WorldGenFeatureHellFlowingLavaConfiguration j = new WorldGenFeatureHellFlowingLavaConfiguration(
			BiomeDecoratorGroups.c.aa, false, 5, 0, ImmutableSet.of(Blocks.NETHERRACK)
		);
		public static final WorldGenFeatureBlockPileConfiguration k = new WorldGenFeatureBlockPileConfiguration(
			new WorldGenFeatureStateProviderWeighted().a(BiomeDecoratorGroups.c.P, 87).a(BiomeDecoratorGroups.c.aC, 11).a(BiomeDecoratorGroups.c.aD, 1)
		);
		public static final WorldGenFeatureBlockPileConfiguration l = new WorldGenFeatureBlockPileConfiguration(
			new WorldGenFeatureStateProviderWeighted()
				.a(BiomeDecoratorGroups.c.aE, 85)
				.a(BiomeDecoratorGroups.c.P, 1)
				.a(BiomeDecoratorGroups.c.aD, 13)
				.a(BiomeDecoratorGroups.c.aC, 1)
		);
		public static final WorldGenFeatureBlockPileConfiguration m = new WorldGenFeatureBlockPileConfiguration(
			new WorldGenFeatureStateProviderSimpl(BiomeDecoratorGroups.c.aF)
		);
	}

	public static final class b {
		public static final WorldGenFeatureTreeBeehive a = new WorldGenFeatureTreeBeehive(0.002F);
		public static final WorldGenFeatureTreeBeehive b = new WorldGenFeatureTreeBeehive(0.02F);
		public static final WorldGenFeatureTreeBeehive c = new WorldGenFeatureTreeBeehive(0.05F);
		public static final WorldGenDecoratorConfigured<WorldGenDecoratorFrequencyConfiguration> d = WorldGenDecorator.r
			.b(new WorldGenDecoratorFrequencyConfiguration(10));
		public static final WorldGenDecoratorConfigured<WorldGenFeatureEmptyConfiguration2> e = WorldGenDecorator.h.b(WorldGenFeatureDecoratorConfiguration.b);
		public static final WorldGenDecoratorConfigured<WorldGenFeatureEmptyConfiguration2> f = WorldGenDecorator.j.b(WorldGenFeatureDecoratorConfiguration.b);
		public static final WorldGenDecoratorConfigured<WorldGenFeatureEmptyConfiguration2> g = WorldGenDecorator.k.b(WorldGenFeatureDecoratorConfiguration.b);
		public static final WorldGenDecoratorConfigured<WorldGenFeatureEmptyConfiguration2> h = WorldGenDecorator.i.b(WorldGenFeatureDecoratorConfiguration.b);
		public static final WorldGenDecoratorConfigured<WorldGenFeatureChanceDecoratorRangeConfiguration> i = WorldGenDecorator.l
			.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(10, 20, 128));
		public static final WorldGenDecoratorConfigured<WorldGenFeatureChanceDecoratorRangeConfiguration> j = WorldGenDecorator.l
			.b(new WorldGenFeatureChanceDecoratorRangeConfiguration(4, 8, 128));
		public static final WorldGenDecoratorConfigured<?> k = WorldGenDecorator.p.b(WorldGenFeatureEmptyConfiguration2.c);
		public static final WorldGenDecoratorConfigured<?> l = e.a();
		public static final WorldGenDecoratorConfigured<?> m = h.a();
		public static final WorldGenDecoratorConfigured<?> n = f.a();
	}

	public static final class c {
		private static final IBlockData a = Blocks.GRASS.getBlockData();
		private static final IBlockData b = Blocks.FERN.getBlockData();
		private static final IBlockData c = Blocks.PODZOL.getBlockData();
		private static final IBlockData d = Blocks.COARSE_DIRT.getBlockData();
		private static final IBlockData e = Blocks.MYCELIUM.getBlockData();
		private static final IBlockData f = Blocks.SNOW_BLOCK.getBlockData();
		private static final IBlockData g = Blocks.ICE.getBlockData();
		private static final IBlockData h = Blocks.OAK_LOG.getBlockData();
		private static final IBlockData i = Blocks.OAK_LEAVES.getBlockData();
		private static final IBlockData j = Blocks.JUNGLE_LOG.getBlockData();
		private static final IBlockData k = Blocks.JUNGLE_LEAVES.getBlockData();
		private static final IBlockData l = Blocks.SPRUCE_LOG.getBlockData();
		private static final IBlockData m = Blocks.SPRUCE_LEAVES.getBlockData();
		private static final IBlockData n = Blocks.ACACIA_LOG.getBlockData();
		private static final IBlockData o = Blocks.ACACIA_LEAVES.getBlockData();
		private static final IBlockData p = Blocks.BIRCH_LOG.getBlockData();
		private static final IBlockData q = Blocks.BIRCH_LEAVES.getBlockData();
		private static final IBlockData r = Blocks.DARK_OAK_LOG.getBlockData();
		private static final IBlockData s = Blocks.DARK_OAK_LEAVES.getBlockData();
		private static final IBlockData t = Blocks.GRASS_BLOCK.getBlockData();
		private static final IBlockData u = Blocks.LARGE_FERN.getBlockData();
		private static final IBlockData v = Blocks.TALL_GRASS.getBlockData();
		private static final IBlockData w = Blocks.LILAC.getBlockData();
		private static final IBlockData x = Blocks.ROSE_BUSH.getBlockData();
		private static final IBlockData y = Blocks.PEONY.getBlockData();
		private static final IBlockData z = Blocks.BROWN_MUSHROOM.getBlockData();
		private static final IBlockData A = Blocks.RED_MUSHROOM.getBlockData();
		private static final IBlockData B = Blocks.PACKED_ICE.getBlockData();
		private static final IBlockData C = Blocks.BLUE_ICE.getBlockData();
		private static final IBlockData D = Blocks.LILY_OF_THE_VALLEY.getBlockData();
		private static final IBlockData E = Blocks.BLUE_ORCHID.getBlockData();
		private static final IBlockData F = Blocks.POPPY.getBlockData();
		private static final IBlockData G = Blocks.DANDELION.getBlockData();
		private static final IBlockData H = Blocks.DEAD_BUSH.getBlockData();
		private static final IBlockData I = Blocks.MELON.getBlockData();
		private static final IBlockData J = Blocks.PUMPKIN.getBlockData();
		private static final IBlockData K = Blocks.SWEET_BERRY_BUSH.getBlockData().set(BlockSweetBerryBush.a, 3);
		private static final IBlockData L = Blocks.FIRE.getBlockData();
		private static final IBlockData M = Blocks.SOUL_FIRE.getBlockData();
		private static final IBlockData N = Blocks.NETHERRACK.getBlockData();
		private static final IBlockData O = Blocks.SOUL_SOIL.getBlockData();
		private static final IBlockData P = Blocks.CRIMSON_ROOTS.getBlockData();
		private static final IBlockData Q = Blocks.LILY_PAD.getBlockData();
		private static final IBlockData R = Blocks.SNOW.getBlockData();
		private static final IBlockData S = Blocks.JACK_O_LANTERN.getBlockData();
		private static final IBlockData T = Blocks.SUNFLOWER.getBlockData();
		private static final IBlockData U = Blocks.CACTUS.getBlockData();
		private static final IBlockData V = Blocks.SUGAR_CANE.getBlockData();
		private static final IBlockData W = Blocks.RED_MUSHROOM_BLOCK.getBlockData().set(BlockHugeMushroom.f, false);
		private static final IBlockData X = Blocks.BROWN_MUSHROOM_BLOCK
			.getBlockData()
			.set(BlockHugeMushroom.e, true)
			.set(BlockHugeMushroom.f, false);
		private static final IBlockData Y = Blocks.MUSHROOM_STEM
			.getBlockData()
			.set(BlockHugeMushroom.e, false)
			.set(BlockHugeMushroom.f, false);
		private static final Fluid Z = FluidTypes.WATER.h();
		private static final Fluid aa = FluidTypes.LAVA.h();
		private static final IBlockData ab = Blocks.WATER.getBlockData();
		private static final IBlockData ac = Blocks.LAVA.getBlockData();
		private static final IBlockData ad = Blocks.DIRT.getBlockData();
		private static final IBlockData ae = Blocks.GRAVEL.getBlockData();
		private static final IBlockData af = Blocks.GRANITE.getBlockData();
		private static final IBlockData ag = Blocks.DIORITE.getBlockData();
		private static final IBlockData ah = Blocks.ANDESITE.getBlockData();
		private static final IBlockData ai = Blocks.COAL_ORE.getBlockData();
		private static final IBlockData aj = Blocks.IRON_ORE.getBlockData();
		private static final IBlockData ak = Blocks.GOLD_ORE.getBlockData();
		private static final IBlockData al = Blocks.REDSTONE_ORE.getBlockData();
		private static final IBlockData am = Blocks.DIAMOND_ORE.getBlockData();
		private static final IBlockData an = Blocks.LAPIS_ORE.getBlockData();
		private static final IBlockData ao = Blocks.STONE.getBlockData();
		private static final IBlockData ap = Blocks.EMERALD_ORE.getBlockData();
		private static final IBlockData aq = Blocks.INFESTED_STONE.getBlockData();
		private static final IBlockData ar = Blocks.SAND.getBlockData();
		private static final IBlockData as = Blocks.CLAY.getBlockData();
		private static final IBlockData at = Blocks.MOSSY_COBBLESTONE.getBlockData();
		private static final IBlockData au = Blocks.SEAGRASS.getBlockData();
		private static final IBlockData av = Blocks.MAGMA_BLOCK.getBlockData();
		private static final IBlockData aw = Blocks.SOUL_SAND.getBlockData();
		private static final IBlockData ax = Blocks.NETHER_GOLD_ORE.getBlockData();
		private static final IBlockData ay = Blocks.NETHER_QUARTZ_ORE.getBlockData();
		private static final IBlockData az = Blocks.BLACKSTONE.getBlockData();
		private static final IBlockData aA = Blocks.ANCIENT_DEBRIS.getBlockData();
		private static final IBlockData aB = Blocks.BASALT.getBlockData();
		private static final IBlockData aC = Blocks.CRIMSON_FUNGUS.getBlockData();
		private static final IBlockData aD = Blocks.WARPED_FUNGUS.getBlockData();
		private static final IBlockData aE = Blocks.WARPED_ROOTS.getBlockData();
		private static final IBlockData aF = Blocks.NETHER_SPROUTS.getBlockData();
	}
}
