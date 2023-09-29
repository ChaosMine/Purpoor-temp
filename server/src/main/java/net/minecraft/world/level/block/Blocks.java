package net.minecraft.world.level.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection.EnumAxis;
import net.minecraft.core.IRegistry;
import net.minecraft.core.particles.Particles;
import net.minecraft.data.worldgen.BiomeDecoratorGroups;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.block.BlockPressurePlateBinary.EnumMobType;
import net.minecraft.world.level.block.BlockSkull.Type;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityShulkerBox;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import net.minecraft.world.level.block.grower.*;
import net.minecraft.world.level.block.piston.BlockPiston;
import net.minecraft.world.level.block.piston.BlockPistonExtension;
import net.minecraft.world.level.block.piston.BlockPistonMoving;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.BlockBase.BlockData;
import net.minecraft.world.level.block.state.BlockBase.Info;
import net.minecraft.world.level.block.state.BlockBase.e;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.BlockPropertyBedPart;
import net.minecraft.world.level.block.state.properties.BlockPropertyWood;
import net.minecraft.world.level.material.FluidTypes;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialMapColor;

import java.util.function.ToIntFunction;

public class Blocks {
	public static final Block AIR = a("air", new BlockAir(Info.a(Material.AIR).a().f().g()));
	public static final Block STONE = a("stone", new Block(Info.a(Material.STONE, MaterialMapColor.m).h().a(1.5F, 6.0F)));
	public static final Block GRANITE = a("granite", new Block(Info.a(Material.STONE, MaterialMapColor.l).h().a(1.5F, 6.0F)));
	public static final Block POLISHED_GRANITE = a("polished_granite", new Block(Info.a(Material.STONE, MaterialMapColor.l).h().a(1.5F, 6.0F)));
	public static final Block DIORITE = a("diorite", new Block(Info.a(Material.STONE, MaterialMapColor.p).h().a(1.5F, 6.0F)));
	public static final Block POLISHED_DIORITE = a("polished_diorite", new Block(Info.a(Material.STONE, MaterialMapColor.p).h().a(1.5F, 6.0F)));
	public static final Block ANDESITE = a("andesite", new Block(Info.a(Material.STONE, MaterialMapColor.m).h().a(1.5F, 6.0F)));
	public static final Block POLISHED_ANDESITE = a("polished_andesite", new Block(Info.a(Material.STONE, MaterialMapColor.m).h().a(1.5F, 6.0F)));
	public static final Block GRASS_BLOCK = a("grass_block", new BlockGrass(Info.a(Material.GRASS).d().d(0.6F).a(SoundEffectType.c)));
	public static final Block DIRT = a("dirt", new Block(Info.a(Material.EARTH, MaterialMapColor.l).d(0.5F).a(SoundEffectType.b)));
	public static final Block COARSE_DIRT = a("coarse_dirt", new Block(Info.a(Material.EARTH, MaterialMapColor.l).d(0.5F).a(SoundEffectType.b)));
	public static final Block PODZOL = a("podzol", new BlockDirtSnow(Info.a(Material.EARTH, MaterialMapColor.J).d(0.5F).a(SoundEffectType.b)));
	public static final Block COBBLESTONE = a("cobblestone", new Block(Info.a(Material.STONE).h().a(2.0F, 6.0F)));
	public static final Block OAK_PLANKS = a("oak_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.o).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block SPRUCE_PLANKS = a("spruce_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.J).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block BIRCH_PLANKS = a("birch_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.d).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block JUNGLE_PLANKS = a("jungle_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.l).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block ACACIA_PLANKS = a("acacia_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.q).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block DARK_OAK_PLANKS = a("dark_oak_planks", new Block(Info.a(Material.WOOD, MaterialMapColor.B).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block OAK_SAPLING = a(
		"oak_sapling", new BlockSapling(new WorldGenTreeProviderOak(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block SPRUCE_SAPLING = a(
		"spruce_sapling", new BlockSapling(new WorldGenTreeProviderSpruce(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block BIRCH_SAPLING = a(
		"birch_sapling", new BlockSapling(new WorldGenTreeProviderBirch(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block JUNGLE_SAPLING = a(
		"jungle_sapling", new BlockSapling(new WorldGenMegaTreeProviderJungle(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block ACACIA_SAPLING = a(
		"acacia_sapling", new BlockSapling(new WorldGenTreeProviderAcacia(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block DARK_OAK_SAPLING = a(
		"dark_oak_sapling", new BlockSapling(new WorldGenMegaTreeProviderDarkOak(), Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c))
	);
	public static final Block BEDROCK = a("bedrock", new Block(Info.a(Material.STONE).a(-1.0F, 3600000.0F).f().a(Blocks::a_IBIBBET)));
	public static final Block WATER = a("water", new BlockFluids(FluidTypes.WATER, Info.a(Material.WATER).a().d(100.0F).f()));
	public static final Block LAVA = a("lava", new BlockFluids(FluidTypes.LAVA, Info.a(Material.LAVA).a().d().d(100.0F).a(var0 -> 15).f()));
	public static final Block SAND = a("sand", new BlockSand(14406560, Info.a(Material.SAND, MaterialMapColor.d).d(0.5F).a(SoundEffectType.i)));
	public static final Block RED_SAND = a("red_sand", new BlockSand(11098145, Info.a(Material.SAND, MaterialMapColor.q).d(0.5F).a(SoundEffectType.i)));
	public static final Block GRAVEL = a("gravel", new BlockGravel(Info.a(Material.SAND, MaterialMapColor.m).d(0.6F).a(SoundEffectType.b)));
	public static final Block GOLD_ORE = a("gold_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block IRON_ORE = a("iron_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block COAL_ORE = a("coal_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block NETHER_GOLD_ORE = a(
		"nether_gold_ore", new BlockOre(Info.a(Material.STONE, MaterialMapColor.K).h().a(3.0F, 3.0F).a(SoundEffectType.T))
	);
	public static final Block OAK_LOG = a("oak_log", a(MaterialMapColor.o, MaterialMapColor.J));
	public static final Block SPRUCE_LOG = a("spruce_log", a(MaterialMapColor.J, MaterialMapColor.B));
	public static final Block BIRCH_LOG = a("birch_log", a(MaterialMapColor.d, MaterialMapColor.p));
	public static final Block JUNGLE_LOG = a("jungle_log", a(MaterialMapColor.l, MaterialMapColor.J));
	public static final Block ACACIA_LOG = a("acacia_log", a(MaterialMapColor.q, MaterialMapColor.m));
	public static final Block DARK_OAK_LOG = a("dark_oak_log", a(MaterialMapColor.B, MaterialMapColor.B));
	public static final Block STRIPPED_SPRUCE_LOG = a("stripped_spruce_log", a(MaterialMapColor.J, MaterialMapColor.J));
	public static final Block STRIPPED_BIRCH_LOG = a("stripped_birch_log", a(MaterialMapColor.d, MaterialMapColor.d));
	public static final Block STRIPPED_JUNGLE_LOG = a("stripped_jungle_log", a(MaterialMapColor.l, MaterialMapColor.l));
	public static final Block STRIPPED_ACACIA_LOG = a("stripped_acacia_log", a(MaterialMapColor.q, MaterialMapColor.q));
	public static final Block STRIPPED_DARK_OAK_LOG = a("stripped_dark_oak_log", a(MaterialMapColor.B, MaterialMapColor.B));
	public static final Block STRIPPED_OAK_LOG = a("stripped_oak_log", a(MaterialMapColor.o, MaterialMapColor.o));
	public static final Block OAK_WOOD = a("oak_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.o).d(2.0F).a(SoundEffectType.a)));
	public static final Block SPRUCE_WOOD = a("spruce_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.J).d(2.0F).a(SoundEffectType.a)));
	public static final Block BIRCH_WOOD = a("birch_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.d).d(2.0F).a(SoundEffectType.a)));
	public static final Block JUNGLE_WOOD = a("jungle_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.l).d(2.0F).a(SoundEffectType.a)));
	public static final Block ACACIA_WOOD = a("acacia_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.w).d(2.0F).a(SoundEffectType.a)));
	public static final Block DARK_OAK_WOOD = a("dark_oak_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.B).d(2.0F).a(SoundEffectType.a)));
	public static final Block STRIPPED_OAK_WOOD = a(
		"stripped_oak_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.o).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block STRIPPED_SPRUCE_WOOD = a(
		"stripped_spruce_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.J).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block STRIPPED_BIRCH_WOOD = a(
		"stripped_birch_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.d).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block STRIPPED_JUNGLE_WOOD = a(
		"stripped_jungle_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.l).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block STRIPPED_ACACIA_WOOD = a(
		"stripped_acacia_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.q).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block STRIPPED_DARK_OAK_WOOD = a(
		"stripped_dark_oak_wood", new BlockRotatable(Info.a(Material.WOOD, MaterialMapColor.B).d(2.0F).a(SoundEffectType.a))
	);
	public static final Block OAK_LEAVES = a("oak_leaves", b());
	public static final Block SPRUCE_LEAVES = a("spruce_leaves", b());
	public static final Block BIRCH_LEAVES = a("birch_leaves", b());
	public static final Block JUNGLE_LEAVES = a("jungle_leaves", b());
	public static final Block ACACIA_LEAVES = a("acacia_leaves", b());
	public static final Block DARK_OAK_LEAVES = a("dark_oak_leaves", b());
	public static final Block SPONGE = a("sponge", new BlockSponge(Info.a(Material.SPONGE).d(0.6F).a(SoundEffectType.c)));
	public static final Block WET_SPONGE = a("wet_sponge", new BlockWetSponge(Info.a(Material.SPONGE).d(0.6F).a(SoundEffectType.c)));
	public static final Block GLASS = a(
		"glass", new BlockGlass(Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b().a(Blocks::a_IBIBBET).a(Blocks::b_IBIBBET).b(Blocks::b).c(Blocks::b))
	);
	public static final Block LAPIS_ORE = a("lapis_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block LAPIS_BLOCK = a("lapis_block", new Block(Info.a(Material.ORE, MaterialMapColor.H).h().a(3.0F, 3.0F)));
	public static final Block DISPENSER = a("dispenser", new BlockDispenser(Info.a(Material.STONE).h().d(3.5F)));
	public static final Block SANDSTONE = a("sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().d(0.8F)));
	public static final Block CHISELED_SANDSTONE = a("chiseled_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().d(0.8F)));
	public static final Block CUT_SANDSTONE = a("cut_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().d(0.8F)));
	public static final Block NOTE_BLOCK = a("note_block", new BlockNote(Info.a(Material.WOOD).a(SoundEffectType.a).d(0.8F)));
	public static final Block WHITE_BED = a("white_bed", a(EnumColor.WHITE));
	public static final Block ORANGE_BED = a("orange_bed", a(EnumColor.ORANGE));
	public static final Block MAGENTA_BED = a("magenta_bed", a(EnumColor.MAGENTA));
	public static final Block LIGHT_BLUE_BED = a("light_blue_bed", a(EnumColor.LIGHT_BLUE));
	public static final Block YELLOW_BED = a("yellow_bed", a(EnumColor.YELLOW));
	public static final Block LIME_BED = a("lime_bed", a(EnumColor.LIME));
	public static final Block PINK_BED = a("pink_bed", a(EnumColor.PINK));
	public static final Block GRAY_BED = a("gray_bed", a(EnumColor.GRAY));
	public static final Block LIGHT_GRAY_BED = a("light_gray_bed", a(EnumColor.LIGHT_GRAY));
	public static final Block CYAN_BED = a("cyan_bed", a(EnumColor.CYAN));
	public static final Block PURPLE_BED = a("purple_bed", a(EnumColor.PURPLE));
	public static final Block BLUE_BED = a("blue_bed", a(EnumColor.BLUE));
	public static final Block BROWN_BED = a("brown_bed", a(EnumColor.BROWN));
	public static final Block GREEN_BED = a("green_bed", a(EnumColor.GREEN));
	public static final Block RED_BED = a("red_bed", a(EnumColor.RED));
	public static final Block BLACK_BED = a("black_bed", a(EnumColor.BLACK));
	public static final Block POWERED_RAIL = a("powered_rail", new BlockPoweredRail(Info.a(Material.ORIENTABLE).a().d(0.7F).a(SoundEffectType.f)));
	public static final Block DETECTOR_RAIL = a("detector_rail", new BlockMinecartDetector(Info.a(Material.ORIENTABLE).a().d(0.7F).a(SoundEffectType.f)));
	public static final Block STICKY_PISTON = a("sticky_piston", a(true));
	public static final Block COBWEB = a("cobweb", new BlockWeb(Info.a(Material.WEB).a().h().d(4.0F)));
	public static final Block GRASS = a("grass", new BlockLongGrass(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block FERN = a("fern", new BlockLongGrass(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block DEAD_BUSH = a("dead_bush", new BlockDeadBush(Info.a(Material.REPLACEABLE_PLANT, MaterialMapColor.o).a().c().a(SoundEffectType.c)));
	public static final Block SEAGRASS = a("seagrass", new BlockSeaGrass(Info.a(Material.REPLACEABLE_WATER_PLANT).a().c().a(SoundEffectType.o)));
	public static final Block TALL_SEAGRASS = a("tall_seagrass", new BlockTallSeaGrass(Info.a(Material.REPLACEABLE_WATER_PLANT).a().c().a(SoundEffectType.o)));
	public static final Block PISTON = a("piston", a(false));
	public static final Block PISTON_HEAD = a("piston_head", new BlockPistonExtension(Info.a(Material.PISTON).d(1.5F).f()));
	public static final Block WHITE_WOOL = a("white_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.j).d(0.8F).a(SoundEffectType.h)));
	public static final Block ORANGE_WOOL = a("orange_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.q).d(0.8F).a(SoundEffectType.h)));
	public static final Block MAGENTA_WOOL = a("magenta_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.r).d(0.8F).a(SoundEffectType.h)));
	public static final Block LIGHT_BLUE_WOOL = a("light_blue_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.s).d(0.8F).a(SoundEffectType.h)));
	public static final Block YELLOW_WOOL = a("yellow_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.t).d(0.8F).a(SoundEffectType.h)));
	public static final Block LIME_WOOL = a("lime_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.u).d(0.8F).a(SoundEffectType.h)));
	public static final Block PINK_WOOL = a("pink_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.v).d(0.8F).a(SoundEffectType.h)));
	public static final Block GRAY_WOOL = a("gray_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.w).d(0.8F).a(SoundEffectType.h)));
	public static final Block LIGHT_GRAY_WOOL = a("light_gray_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.x).d(0.8F).a(SoundEffectType.h)));
	public static final Block CYAN_WOOL = a("cyan_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.y).d(0.8F).a(SoundEffectType.h)));
	public static final Block PURPLE_WOOL = a("purple_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.z).d(0.8F).a(SoundEffectType.h)));
	public static final Block BLUE_WOOL = a("blue_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.A).d(0.8F).a(SoundEffectType.h)));
	public static final Block BROWN_WOOL = a("brown_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.B).d(0.8F).a(SoundEffectType.h)));
	public static final Block GREEN_WOOL = a("green_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.C).d(0.8F).a(SoundEffectType.h)));
	public static final Block RED_WOOL = a("red_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.D).d(0.8F).a(SoundEffectType.h)));
	public static final Block BLACK_WOOL = a("black_wool", new Block(Info.a(Material.CLOTH, MaterialMapColor.E).d(0.8F).a(SoundEffectType.h)));
	public static final Block MOVING_PISTON = a(
		"moving_piston", new BlockPistonMoving(Info.a(Material.PISTON).d(-1.0F).e().f().b().a(Blocks::b_IBIBBET).b(Blocks::b).c(Blocks::b))
	);
	public static final Block DANDELION = a("dandelion", new BlockFlowers(MobEffects.SATURATION, 7, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block POPPY = a("poppy", new BlockFlowers(MobEffects.NIGHT_VISION, 5, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block BLUE_ORCHID = a("blue_orchid", new BlockFlowers(MobEffects.SATURATION, 7, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block ALLIUM = a("allium", new BlockFlowers(MobEffects.FIRE_RESISTANCE, 4, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block AZURE_BLUET = a("azure_bluet", new BlockFlowers(MobEffects.BLINDNESS, 8, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block RED_TULIP = a("red_tulip", new BlockFlowers(MobEffects.WEAKNESS, 9, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block ORANGE_TULIP = a("orange_tulip", new BlockFlowers(MobEffects.WEAKNESS, 9, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block WHITE_TULIP = a("white_tulip", new BlockFlowers(MobEffects.WEAKNESS, 9, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block PINK_TULIP = a("pink_tulip", new BlockFlowers(MobEffects.WEAKNESS, 9, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block OXEYE_DAISY = a("oxeye_daisy", new BlockFlowers(MobEffects.REGENERATION, 8, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block CORNFLOWER = a("cornflower", new BlockFlowers(MobEffects.JUMP, 6, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block WITHER_ROSE = a("wither_rose", new BlockWitherRose(MobEffects.WITHER, Info.a(Material.PLANT).a().c().a(SoundEffectType.c)));
	public static final Block LILY_OF_THE_VALLEY = a(
		"lily_of_the_valley", new BlockFlowers(MobEffects.POISON, 12, Info.a(Material.PLANT).a().c().a(SoundEffectType.c))
	);
	public static final Block BROWN_MUSHROOM = a(
		"brown_mushroom", new BlockMushroom(Info.a(Material.PLANT, MaterialMapColor.B).a().d().c().a(SoundEffectType.c).a(var0 -> 1).d(Blocks::a))
	);
	public static final Block RED_MUSHROOM = a(
		"red_mushroom", new BlockMushroom(Info.a(Material.PLANT, MaterialMapColor.D).a().d().c().a(SoundEffectType.c).d(Blocks::a))
	);
	public static final Block GOLD_BLOCK = a("gold_block", new Block(Info.a(Material.ORE, MaterialMapColor.F).h().a(3.0F, 6.0F).a(SoundEffectType.f)));
	public static final Block IRON_BLOCK = a("iron_block", new Block(Info.a(Material.ORE, MaterialMapColor.h).h().a(5.0F, 6.0F).a(SoundEffectType.f)));
	public static final Block BRICKS = a("bricks", new Block(Info.a(Material.STONE, MaterialMapColor.D).h().a(2.0F, 6.0F)));
	public static final Block TNT = a("tnt", new BlockTNT(Info.a(Material.TNT).c().a(SoundEffectType.c)));
	public static final Block BOOKSHELF = a("bookshelf", new Block(Info.a(Material.WOOD).d(1.5F).a(SoundEffectType.a)));
	public static final Block MOSSY_COBBLESTONE = a("mossy_cobblestone", new Block(Info.a(Material.STONE).h().a(2.0F, 6.0F)));
	public static final Block OBSIDIAN = a("obsidian", new Block(Info.a(Material.STONE, MaterialMapColor.E).h().a(50.0F, 1200.0F)));
	public static final Block TORCH = a("torch", new BlockTorch(Info.a(Material.ORIENTABLE).a().c().a(var0 -> 14).a(SoundEffectType.a), Particles.FLAME));
	public static final Block WALL_TORCH = a(
		"wall_torch", new BlockTorchWall(Info.a(Material.ORIENTABLE).a().c().a(var0 -> 14).a(SoundEffectType.a).a(TORCH), Particles.FLAME)
	);
	public static final Block FIRE = a("fire", new BlockFire(Info.a(Material.FIRE, MaterialMapColor.f).a().c().a(var0 -> 15).a(SoundEffectType.h)));
	public static final Block SOUL_FIRE = a(
		"soul_fire", new BlockSoulFire(Info.a(Material.FIRE, MaterialMapColor.s).a().c().a(var0 -> 10).a(SoundEffectType.h))
	);
	public static final Block SPAWNER = a("spawner", new BlockMobSpawner(Info.a(Material.STONE).h().d(5.0F).a(SoundEffectType.f).b()));
	public static final Block OAK_STAIRS = a("oak_stairs", new BlockStairs(OAK_PLANKS.getBlockData(), Info.a((BlockBase) OAK_PLANKS)));
	public static final Block CHEST = a("chest", new BlockChest(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a), () -> TileEntityTypes.CHEST));
	public static final Block REDSTONE_WIRE = a("redstone_wire", new BlockRedstoneWire(Info.a(Material.ORIENTABLE).a().c()));
	public static final Block DIAMOND_ORE = a("diamond_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block DIAMOND_BLOCK = a("diamond_block", new Block(Info.a(Material.ORE, MaterialMapColor.G).h().a(5.0F, 6.0F).a(SoundEffectType.f)));
	public static final Block CRAFTING_TABLE = a("crafting_table", new BlockWorkbench(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block WHEAT = a("wheat", new BlockCrops(Info.a(Material.PLANT).a().d().c().a(SoundEffectType.u)));
	public static final Block FARMLAND = a("farmland", new BlockSoil(Info.a(Material.EARTH).d().d(0.6F).a(SoundEffectType.b).c(Blocks::a).b(Blocks::a)));
	public static final Block FURNACE = a("furnace", new BlockFurnaceFurace(Info.a(Material.STONE).h().d(3.5F).a(a(13))));
	public static final Block OAK_SIGN = a("oak_sign", new BlockFloorSign(Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.a));
	public static final Block SPRUCE_SIGN = a(
		"spruce_sign", new BlockFloorSign(Info.a(Material.WOOD, SPRUCE_LOG.s()).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.b)
	);
	public static final Block SPRUCE_WALL_SIGN = a(
		"spruce_wall_sign", new BlockWallSign(Info.a(Material.WOOD, SPRUCE_LOG.s()).a().d(1.0F).a(SoundEffectType.a).a(SPRUCE_SIGN), BlockPropertyWood.b)
	);
	public static final Block BIRCH_SIGN = a(
		"birch_sign", new BlockFloorSign(Info.a(Material.WOOD, MaterialMapColor.d).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.c)
	);
	public static final Block ACACIA_SIGN = a(
		"acacia_sign", new BlockFloorSign(Info.a(Material.WOOD, MaterialMapColor.q).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.d)
	);
	public static final Block JUNGLE_SIGN = a(
		"jungle_sign", new BlockFloorSign(Info.a(Material.WOOD, JUNGLE_LOG.s()).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.e)
	);
	public static final Block JUNGLE_WALL_SIGN = a(
		"jungle_wall_sign", new BlockWallSign(Info.a(Material.WOOD, JUNGLE_LOG.s()).a().d(1.0F).a(SoundEffectType.a).a(JUNGLE_SIGN), BlockPropertyWood.e)
	);
	public static final Block DARK_OAK_SIGN = a(
		"dark_oak_sign", new BlockFloorSign(Info.a(Material.WOOD, DARK_OAK_LOG.s()).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.f)
	);
	public static final Block DARK_OAK_WALL_SIGN = a(
		"dark_oak_wall_sign", new BlockWallSign(Info.a(Material.WOOD, DARK_OAK_LOG.s()).a().d(1.0F).a(SoundEffectType.a).a(DARK_OAK_SIGN), BlockPropertyWood.f)
	);
	public static final Block OAK_DOOR = a("oak_door", new BlockDoor(Info.a(Material.WOOD, OAK_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block LADDER = a("ladder", new BlockLadder(Info.a(Material.ORIENTABLE).d(0.4F).a(SoundEffectType.k).b()));
	public static final Block RAIL = a("rail", new BlockMinecartTrack(Info.a(Material.ORIENTABLE).a().d(0.7F).a(SoundEffectType.f)));
	public static final Block COBBLESTONE_STAIRS = a("cobblestone_stairs", new BlockStairs(COBBLESTONE.getBlockData(), Info.a((BlockBase) COBBLESTONE)));
	public static final Block OAK_WALL_SIGN = a(
		"oak_wall_sign", new BlockWallSign(Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(OAK_SIGN), BlockPropertyWood.a)
	);
	public static final Block BIRCH_WALL_SIGN = a(
		"birch_wall_sign", new BlockWallSign(Info.a(Material.WOOD, MaterialMapColor.d).a().d(1.0F).a(SoundEffectType.a).a(BIRCH_SIGN), BlockPropertyWood.c)
	);
	public static final Block ACACIA_WALL_SIGN = a(
		"acacia_wall_sign", new BlockWallSign(Info.a(Material.WOOD, MaterialMapColor.q).a().d(1.0F).a(SoundEffectType.a).a(ACACIA_SIGN), BlockPropertyWood.d)
	);
	public static final Block LEVER = a("lever", new BlockLever(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block STONE_PRESSURE_PLATE = a(
		"stone_pressure_plate", new BlockPressurePlateBinary(EnumMobType.MOBS, Info.a(Material.STONE).h().a().d(0.5F))
	);
	public static final Block IRON_DOOR = a("iron_door", new BlockDoor(Info.a(Material.ORE, MaterialMapColor.h).h().d(5.0F).a(SoundEffectType.f).b()));
	public static final Block OAK_PRESSURE_PLATE = a(
		"oak_pressure_plate", new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, OAK_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block SPRUCE_PRESSURE_PLATE = a(
		"spruce_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, SPRUCE_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block BIRCH_PRESSURE_PLATE = a(
		"birch_pressure_plate", new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, BIRCH_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block JUNGLE_PRESSURE_PLATE = a(
		"jungle_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, JUNGLE_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block ACACIA_PRESSURE_PLATE = a(
		"acacia_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, ACACIA_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block DARK_OAK_PRESSURE_PLATE = a(
		"dark_oak_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.WOOD, DARK_OAK_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block REDSTONE_ORE = a("redstone_ore", new BlockRedstoneOre(Info.a(Material.STONE).h().d().a(a(9)).a(3.0F, 3.0F)));
	public static final Block REDSTONE_TORCH = a("redstone_torch", new BlockRedstoneTorch(Info.a(Material.ORIENTABLE).a().c().a(a(7)).a(SoundEffectType.a)));
	public static final Block REDSTONE_WALL_TORCH = a(
		"redstone_wall_torch", new BlockRedstoneTorchWall(Info.a(Material.ORIENTABLE).a().c().a(a(7)).a(SoundEffectType.a).a(REDSTONE_TORCH))
	);
	public static final Block STONE_BUTTON = a("stone_button", new BlockStoneButton(Info.a(Material.ORIENTABLE).a().d(0.5F)));
	public static final Block SNOW = a("snow", new BlockSnow(Info.a(Material.PACKED_ICE).d().d(0.1F).h().a(SoundEffectType.j)));
	public static final Block ICE = a(
		"ice",
		new BlockIce(
			Info.a(Material.ICE)
				.a(0.98F)
				.d()
				.d(0.5F)
				.a(SoundEffectType.g)
				.b()
				.a((var0, var1x, var2, var3x) -> var3x == EntityTypes.POLAR_BEAR)
		)
	);
	public static final Block SNOW_BLOCK = a("snow_block", new Block(Info.a(Material.SNOW_BLOCK).h().d(0.2F).a(SoundEffectType.j)));
	public static final Block CACTUS = a("cactus", new BlockCactus(Info.a(Material.CACTUS).d().d(0.4F).a(SoundEffectType.h)));
	public static final Block CLAY = a("clay", new Block(Info.a(Material.CLAY).d(0.6F).a(SoundEffectType.b)));
	public static final Block SUGAR_CANE = a("sugar_cane", new BlockReed(Info.a(Material.PLANT).a().d().c().a(SoundEffectType.c)));
	public static final Block JUKEBOX = a("jukebox", new BlockJukeBox(Info.a(Material.WOOD, MaterialMapColor.l).a(2.0F, 6.0F)));
	public static final Block OAK_FENCE = a("oak_fence", new BlockFence(Info.a(Material.WOOD, OAK_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block PUMPKIN = a("pumpkin", new BlockPumpkin(Info.a(Material.PUMPKIN, MaterialMapColor.q).d(1.0F).a(SoundEffectType.a)));
	public static final Block NETHERRACK = a("netherrack", new BlockNetherrack(Info.a(Material.STONE, MaterialMapColor.K).h().d(0.4F).a(SoundEffectType.K)));
	public static final Block SOUL_SAND = a(
		"soul_sand",
		new BlockSlowSand(Info.a(Material.SAND, MaterialMapColor.B).d(0.5F).b(0.4F).a(SoundEffectType.G).a(Blocks::b_IBIBBET).a(Blocks::a_IBIBBET).c(Blocks::a).b(Blocks::a))
	);
	public static final Block SOUL_SOIL = a("soul_soil", new Block(Info.a(Material.EARTH, MaterialMapColor.B).d(0.5F).a(SoundEffectType.H)));
	public static final Block BASALT = a("basalt", new BlockRotatable(Info.a(Material.STONE, MaterialMapColor.E).h().a(1.25F, 4.2F).a(SoundEffectType.I)));
	public static final Block cP = a("polished_basalt", new BlockRotatable(Info.a(Material.STONE, MaterialMapColor.E).h().a(1.25F, 4.2F).a(SoundEffectType.I)));
	public static final Block SOUL_TORCH = a(
		"soul_torch", new BlockTorch(Info.a(Material.ORIENTABLE).a().c().a(var0 -> 10).a(SoundEffectType.a), Particles.SOUL_FIRE_FLAME)
	);
	public static final Block SOUL_WALL_TORCH = a(
		"soul_wall_torch", new BlockTorchWall(Info.a(Material.ORIENTABLE).a().c().a(var0 -> 10).a(SoundEffectType.a).a(SOUL_TORCH), Particles.SOUL_FIRE_FLAME)
	);
	public static final Block GLOWSTONE = a("glowstone", new Block(Info.a(Material.SHATTERABLE, MaterialMapColor.d).d(0.3F).a(SoundEffectType.g).a(var0 -> 15)));
	public static final Block NETHER_PORTAL = a("nether_portal", new BlockPortal(Info.a(Material.PORTAL).a().d().d(-1.0F).a(SoundEffectType.g).a(var0 -> 11)));
	public static final Block CARVED_PUMPKIN = a(
		"carved_pumpkin", new BlockPumpkinCarved(Info.a(Material.PUMPKIN, MaterialMapColor.q).d(1.0F).a(SoundEffectType.a).a(Blocks::b_IBIBBET))
	);
	public static final Block JACK_O_LANTERN = a(
		"jack_o_lantern", new BlockPumpkinCarved(Info.a(Material.PUMPKIN, MaterialMapColor.q).d(1.0F).a(SoundEffectType.a).a(var0 -> 15).a(Blocks::b_IBIBBET))
	);
	public static final Block CAKE = a("cake", new BlockCake(Info.a(Material.CAKE).d(0.5F).a(SoundEffectType.h)));
	public static final Block REPEATER = a("repeater", new BlockRepeater(Info.a(Material.ORIENTABLE).c().a(SoundEffectType.a)));
	public static final Block WHITE_STAINED_GLASS = a("white_stained_glass", b(EnumColor.WHITE));
	public static final Block ORANGE_STAINED_GLASS = a("orange_stained_glass", b(EnumColor.ORANGE));
	public static final Block MAGENTA_STAINED_GLASS = a("magenta_stained_glass", b(EnumColor.MAGENTA));
	public static final Block LIGHT_BLUE_STAINED_GLASS = a("light_blue_stained_glass", b(EnumColor.LIGHT_BLUE));
	public static final Block YELLOW_STAINED_GLASS = a("yellow_stained_glass", b(EnumColor.YELLOW));
	public static final Block LIME_STAINED_GLASS = a("lime_stained_glass", b(EnumColor.LIME));
	public static final Block PINK_STAINED_GLASS = a("pink_stained_glass", b(EnumColor.PINK));
	public static final Block GRAY_STAINED_GLASS = a("gray_stained_glass", b(EnumColor.GRAY));
	public static final Block LIGHT_GRAY_STAINED_GLASS = a("light_gray_stained_glass", b(EnumColor.LIGHT_GRAY));
	public static final Block CYAN_STAINED_GLASS = a("cyan_stained_glass", b(EnumColor.CYAN));
	public static final Block PURPLE_STAINED_GLASS = a("purple_stained_glass", b(EnumColor.PURPLE));
	public static final Block BLUE_STAINED_GLASS = a("blue_stained_glass", b(EnumColor.BLUE));
	public static final Block BROWN_STAINED_GLASS = a("brown_stained_glass", b(EnumColor.BROWN));
	public static final Block GREEN_STAINED_GLASS = a("green_stained_glass", b(EnumColor.GREEN));
	public static final Block RED_STAINED_GLASS = a("red_stained_glass", b(EnumColor.RED));
	public static final Block BLACK_STAINED_GLASS = a("black_stained_glass", b(EnumColor.BLACK));
	public static final Block OAK_TRAPDOOR = a(
		"oak_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.o).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block SPRUCE_TRAPDOOR = a(
		"spruce_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.J).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block BIRCH_TRAPDOOR = a(
		"birch_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.d).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block JUNGLE_TRAPDOOR = a(
		"jungle_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.l).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block ACACIA_TRAPDOOR = a(
		"acacia_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.q).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block DARK_OAK_TRAPDOOR = a(
		"dark_oak_trapdoor", new BlockTrapdoor(Info.a(Material.WOOD, MaterialMapColor.B).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block STONE_BRICKS = a("stone_bricks", new Block(Info.a(Material.STONE).h().a(1.5F, 6.0F)));
	public static final Block MOSSY_STONE_BRICKS = a("mossy_stone_bricks", new Block(Info.a(Material.STONE).h().a(1.5F, 6.0F)));
	public static final Block CRACKED_STONE_BRICKS = a("cracked_stone_bricks", new Block(Info.a(Material.STONE).h().a(1.5F, 6.0F)));
	public static final Block CHISELED_STONE_BRICKS = a("chiseled_stone_bricks", new Block(Info.a(Material.STONE).h().a(1.5F, 6.0F)));
	public static final Block INFESTED_STONE = a("infested_stone", new BlockMonsterEggs(STONE, Info.a(Material.CLAY).a(0.0F, 0.75F)));
	public static final Block INFESTED_COBBLESTONE = a("infested_cobblestone", new BlockMonsterEggs(COBBLESTONE, Info.a(Material.CLAY).a(0.0F, 0.75F)));
	public static final Block INFESTED_STONE_BRICKS = a("infested_stone_bricks", new BlockMonsterEggs(STONE_BRICKS, Info.a(Material.CLAY).a(0.0F, 0.75F)));
	public static final Block INFESTED_MOSSY_STONE_BRICKS = a(
		"infested_mossy_stone_bricks", new BlockMonsterEggs(MOSSY_STONE_BRICKS, Info.a(Material.CLAY).a(0.0F, 0.75F))
	);
	public static final Block INFESTED_CRACKED_STONE_BRICKS = a(
		"infested_cracked_stone_bricks", new BlockMonsterEggs(CRACKED_STONE_BRICKS, Info.a(Material.CLAY).a(0.0F, 0.75F))
	);
	public static final Block INFESTED_CHISELED_STONE_BRICKS = a(
		"infested_chiseled_stone_bricks", new BlockMonsterEggs(CHISELED_STONE_BRICKS, Info.a(Material.CLAY).a(0.0F, 0.75F))
	);
	public static final Block BROWN_MUSHROOM_BLOCK = a(
		"brown_mushroom_block", new BlockHugeMushroom(Info.a(Material.WOOD, MaterialMapColor.l).d(0.2F).a(SoundEffectType.a))
	);
	public static final Block RED_MUSHROOM_BLOCK = a(
		"red_mushroom_block", new BlockHugeMushroom(Info.a(Material.WOOD, MaterialMapColor.D).d(0.2F).a(SoundEffectType.a))
	);
	public static final Block MUSHROOM_STEM = a("mushroom_stem", new BlockHugeMushroom(Info.a(Material.WOOD, MaterialMapColor.e).d(0.2F).a(SoundEffectType.a)));
	public static final Block IRON_BARS = a("iron_bars", new BlockIronBars(Info.a(Material.ORE, MaterialMapColor.b).h().a(5.0F, 6.0F).a(SoundEffectType.f).b()));
	public static final Block CHAIN = a("chain", new BlockChain(Info.a(Material.ORE, MaterialMapColor.b).h().a(5.0F, 6.0F).a(SoundEffectType.S).b()));
	public static final Block GLASS_PANE = a("glass_pane", new BlockIronBars(Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b()));
	public static final Block MELON = a("melon", new BlockMelon(Info.a(Material.PUMPKIN, MaterialMapColor.u).d(1.0F).a(SoundEffectType.a)));
	public static final Block ATTACHED_PUMPKIN_STEM = a(
		"attached_pumpkin_stem", new BlockStemAttached((BlockStemmed) PUMPKIN, Info.a(Material.PLANT).a().c().a(SoundEffectType.a))
	);
	public static final Block ATTACHED_MELON_STEM = a(
		"attached_melon_stem", new BlockStemAttached((BlockStemmed) MELON, Info.a(Material.PLANT).a().c().a(SoundEffectType.a))
	);
	public static final Block PUMPKIN_STEM = a("pumpkin_stem", new BlockStem((BlockStemmed) PUMPKIN, Info.a(Material.PLANT).a().d().c().a(SoundEffectType.v)));
	public static final Block MELON_STEM = a("melon_stem", new BlockStem((BlockStemmed) MELON, Info.a(Material.PLANT).a().d().c().a(SoundEffectType.v)));
	public static final Block VINE = a("vine", new BlockVine(Info.a(Material.REPLACEABLE_PLANT).a().d().d(0.2F).a(SoundEffectType.w)));
	public static final Block OAK_FENCE_GATE = a("oak_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, OAK_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block BRICK_STAIRS = a("brick_stairs", new BlockStairs(BRICKS.getBlockData(), Info.a((BlockBase) BRICKS)));
	public static final Block STONE_BRICK_STAIRS = a("stone_brick_stairs", new BlockStairs(STONE_BRICKS.getBlockData(), Info.a((BlockBase) STONE_BRICKS)));
	public static final Block MYCELIUM = a("mycelium", new BlockMycel(Info.a(Material.GRASS, MaterialMapColor.z).d().d(0.6F).a(SoundEffectType.c)));
	public static final Block LILY_PAD = a("lily_pad", new BlockWaterLily(Info.a(Material.PLANT).c().a(SoundEffectType.d).b()));
	public static final Block NETHER_BRICKS = a("nether_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L)));
	public static final Block NETHER_BRICK_FENCE = a(
		"nether_brick_fence", new BlockFence(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L))
	);
	public static final Block NETHER_BRICK_STAIRS = a("nether_brick_stairs", new BlockStairs(NETHER_BRICKS.getBlockData(), Info.a((BlockBase) NETHER_BRICKS)));
	public static final Block NETHER_WART = a("nether_wart", new BlockNetherWart(Info.a(Material.PLANT, MaterialMapColor.D).a().d().a(SoundEffectType.x)));
	public static final Block ENCHANTING_TABLE = a(
		"enchanting_table", new BlockEnchantmentTable(Info.a(Material.STONE, MaterialMapColor.D).h().a(5.0F, 1200.0F))
	);
	public static final Block BREWING_STAND = a("brewing_stand", new BlockBrewingStand(Info.a(Material.ORE).h().d(0.5F).a(var0 -> 1).b()));
	public static final Block CAULDRON = a("cauldron", new BlockCauldron(Info.a(Material.ORE, MaterialMapColor.m).h().d(2.0F).b()));
	public static final Block END_PORTAL = a(
		"end_portal", new BlockEnderPortal(Info.a(Material.PORTAL, MaterialMapColor.E).a().a(var0 -> 15).a(-1.0F, 3600000.0F).f())
	);
	public static final Block END_PORTAL_FRAME = a(
		"end_portal_frame", new BlockEnderPortalFrame(Info.a(Material.STONE, MaterialMapColor.C).a(SoundEffectType.g).a(var0 -> 1).a(-1.0F, 3600000.0F).f())
	);
	public static final Block END_STONE = a("end_stone", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().a(3.0F, 9.0F)));
	public static final Block DRAGON_EGG = a("dragon_egg", new BlockDragonEgg(Info.a(Material.DRAGON_EGG, MaterialMapColor.E).a(3.0F, 9.0F).a(var0 -> 1).b()));
	public static final Block REDSTONE_LAMP = a(
		"redstone_lamp", new BlockRedstoneLamp(Info.a(Material.BUILDABLE_GLASS).a(a(15)).d(0.3F).a(SoundEffectType.g).a(Blocks::b_IBIBBET))
	);
	public static final Block COCOA = a("cocoa", new BlockCocoa(Info.a(Material.PLANT).d().a(0.2F, 3.0F).a(SoundEffectType.a).b()));
	public static final Block SANDSTONE_STAIRS = a("sandstone_stairs", new BlockStairs(SANDSTONE.getBlockData(), Info.a((BlockBase) SANDSTONE)));
	public static final Block EMERALD_ORE = a("emerald_ore", new BlockOre(Info.a(Material.STONE).h().a(3.0F, 3.0F)));
	public static final Block ENDER_CHEST = a("ender_chest", new BlockEnderChest(Info.a(Material.STONE).h().a(22.5F, 600.0F).a(var0 -> 7)));
	public static final Block TRIPWIRE_HOOK = a("tripwire_hook", new BlockTripwireHook(Info.a(Material.ORIENTABLE).a()));
	public static final Block TRIPWIRE = a("tripwire", new BlockTripwire((BlockTripwireHook) TRIPWIRE_HOOK, Info.a(Material.ORIENTABLE).a()));
	public static final Block EMERALD_BLOCK = a("emerald_block", new Block(Info.a(Material.ORE, MaterialMapColor.I).h().a(5.0F, 6.0F).a(SoundEffectType.f)));
	public static final Block SPRUCE_STAIRS = a("spruce_stairs", new BlockStairs(SPRUCE_PLANKS.getBlockData(), Info.a((BlockBase) SPRUCE_PLANKS)));
	public static final Block BIRCH_STAIRS = a("birch_stairs", new BlockStairs(BIRCH_PLANKS.getBlockData(), Info.a((BlockBase) BIRCH_PLANKS)));
	public static final Block JUNGLE_STAIRS = a("jungle_stairs", new BlockStairs(JUNGLE_PLANKS.getBlockData(), Info.a((BlockBase) JUNGLE_PLANKS)));
	public static final Block COMMAND_BLOCK = a("command_block", new BlockCommand(Info.a(Material.ORE, MaterialMapColor.B).h().a(-1.0F, 3600000.0F).f()));
	public static final Block BEACON = a("beacon", new BlockBeacon(Info.a(Material.SHATTERABLE, MaterialMapColor.G).d(3.0F).a(var0 -> 15).b().a(Blocks::b_IBIBBET)));
	public static final Block COBBLESTONE_WALL = a("cobblestone_wall", new BlockCobbleWall(Info.a((BlockBase) COBBLESTONE)));
	public static final Block MOSSY_COBBLESTONE_WALL = a("mossy_cobblestone_wall", new BlockCobbleWall(Info.a((BlockBase) COBBLESTONE)));
	public static final Block FLOWER_POT = a("flower_pot", new BlockFlowerPot(AIR, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_OAK_SAPLING = a("potted_oak_sapling", new BlockFlowerPot(OAK_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_SPRUCE_SAPLING = a("potted_spruce_sapling", new BlockFlowerPot(SPRUCE_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_BIRCH_SAPLING = a("potted_birch_sapling", new BlockFlowerPot(BIRCH_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_JUNGLE_SAPLING = a("potted_jungle_sapling", new BlockFlowerPot(JUNGLE_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_ACACIA_SAPLING = a("potted_acacia_sapling", new BlockFlowerPot(ACACIA_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_DARK_OAK_SAPLING = a("potted_dark_oak_sapling", new BlockFlowerPot(DARK_OAK_SAPLING, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_FERN = a("potted_fern", new BlockFlowerPot(FERN, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_DANDELION = a("potted_dandelion", new BlockFlowerPot(DANDELION, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_POPPY = a("potted_poppy", new BlockFlowerPot(POPPY, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_BLUE_ORCHID = a("potted_blue_orchid", new BlockFlowerPot(BLUE_ORCHID, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_ALLIUM = a("potted_allium", new BlockFlowerPot(ALLIUM, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_AZURE_BLUET = a("potted_azure_bluet", new BlockFlowerPot(AZURE_BLUET, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_RED_TULIP = a("potted_red_tulip", new BlockFlowerPot(RED_TULIP, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_ORANGE_TULIP = a("potted_orange_tulip", new BlockFlowerPot(ORANGE_TULIP, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_WHITE_TULIP = a("potted_white_tulip", new BlockFlowerPot(WHITE_TULIP, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_PINK_TULIP = a("potted_pink_tulip", new BlockFlowerPot(PINK_TULIP, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_OXEYE_DAISY = a("potted_oxeye_daisy", new BlockFlowerPot(OXEYE_DAISY, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_CORNFLOWER = a("potted_cornflower", new BlockFlowerPot(CORNFLOWER, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_LILY_OF_THE_VALLEY = a(
		"potted_lily_of_the_valley", new BlockFlowerPot(LILY_OF_THE_VALLEY, Info.a(Material.ORIENTABLE).c().b())
	);
	public static final Block POTTED_WITHER_ROSE = a("potted_wither_rose", new BlockFlowerPot(WITHER_ROSE, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_RED_MUSHROOM = a("potted_red_mushroom", new BlockFlowerPot(RED_MUSHROOM, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_BROWN_MUSHROOM = a("potted_brown_mushroom", new BlockFlowerPot(BROWN_MUSHROOM, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_DEAD_BUSH = a("potted_dead_bush", new BlockFlowerPot(DEAD_BUSH, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_CACTUS = a("potted_cactus", new BlockFlowerPot(CACTUS, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block CARROTS = a("carrots", new BlockCarrots(Info.a(Material.PLANT).a().d().c().a(SoundEffectType.u)));
	public static final Block POTATOES = a("potatoes", new BlockPotatoes(Info.a(Material.PLANT).a().d().c().a(SoundEffectType.u)));
	public static final Block OAK_BUTTON = a("oak_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block SPRUCE_BUTTON = a("spruce_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block BIRCH_BUTTON = a("birch_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block JUNGLE_BUTTON = a("jungle_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block ACACIA_BUTTON = a("acacia_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block DARK_OAK_BUTTON = a("dark_oak_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block SKELETON_SKULL = a("skeleton_skull", new BlockSkull(Type.SKELETON, Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block SKELETON_WALL_SKULL = a(
		"skeleton_wall_skull", new BlockSkullWall(Type.SKELETON, Info.a(Material.ORIENTABLE).d(1.0F).a(SKELETON_SKULL))
	);
	public static final Block WITHER_SKELETON_SKULL = a("wither_skeleton_skull", new BlockWitherSkull(Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block WITHER_SKELETON_WALL_SKULL = a(
		"wither_skeleton_wall_skull", new BlockWitherSkullWall(Info.a(Material.ORIENTABLE).d(1.0F).a(WITHER_SKELETON_SKULL))
	);
	public static final Block ZOMBIE_HEAD = a("zombie_head", new BlockSkull(Type.ZOMBIE, Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block ZOMBIE_WALL_HEAD = a("zombie_wall_head", new BlockSkullWall(Type.ZOMBIE, Info.a(Material.ORIENTABLE).d(1.0F).a(ZOMBIE_HEAD)));
	public static final Block PLAYER_HEAD = a("player_head", new BlockSkullPlayer(Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block PLAYER_WALL_HEAD = a("player_wall_head", new BlockSkullPlayerWall(Info.a(Material.ORIENTABLE).d(1.0F).a(PLAYER_HEAD)));
	public static final Block CREEPER_HEAD = a("creeper_head", new BlockSkull(Type.CREEPER, Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block CREEPER_WALL_HEAD = a("creeper_wall_head", new BlockSkullWall(Type.CREEPER, Info.a(Material.ORIENTABLE).d(1.0F).a(CREEPER_HEAD)));
	public static final Block DRAGON_HEAD = a("dragon_head", new BlockSkull(Type.DRAGON, Info.a(Material.ORIENTABLE).d(1.0F)));
	public static final Block DRAGON_WALL_HEAD = a("dragon_wall_head", new BlockSkullWall(Type.DRAGON, Info.a(Material.ORIENTABLE).d(1.0F).a(DRAGON_HEAD)));
	public static final Block ANVIL = a("anvil", new BlockAnvil(Info.a(Material.HEAVY, MaterialMapColor.h).h().a(5.0F, 1200.0F).a(SoundEffectType.l)));
	public static final Block CHIPPED_ANVIL = a(
		"chipped_anvil", new BlockAnvil(Info.a(Material.HEAVY, MaterialMapColor.h).h().a(5.0F, 1200.0F).a(SoundEffectType.l))
	);
	public static final Block DAMAGED_ANVIL = a(
		"damaged_anvil", new BlockAnvil(Info.a(Material.HEAVY, MaterialMapColor.h).h().a(5.0F, 1200.0F).a(SoundEffectType.l))
	);
	public static final Block TRAPPED_CHEST = a("trapped_chest", new BlockChestTrapped(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE = a(
		"light_weighted_pressure_plate", new BlockPressurePlateWeighted(15, Info.a(Material.ORE, MaterialMapColor.F).h().a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE = a(
		"heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(150, Info.a(Material.ORE).h().a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block COMPARATOR = a("comparator", new BlockRedstoneComparator(Info.a(Material.ORIENTABLE).c().a(SoundEffectType.a)));
	public static final Block DAYLIGHT_DETECTOR = a("daylight_detector", new BlockDaylightDetector(Info.a(Material.WOOD).d(0.2F).a(SoundEffectType.a)));
	public static final Block REDSTONE_BLOCK = a(
		"redstone_block", new BlockPowered(Info.a(Material.ORE, MaterialMapColor.f).h().a(5.0F, 6.0F).a(SoundEffectType.f).a(Blocks::b_IBIBBET))
	);
	public static final Block NETHER_QUARTZ_ORE = a(
		"nether_quartz_ore", new BlockOre(Info.a(Material.STONE, MaterialMapColor.K).h().a(3.0F, 3.0F).a(SoundEffectType.N))
	);
	public static final Block HOPPER = a("hopper", new BlockHopper(Info.a(Material.ORE, MaterialMapColor.m).h().a(3.0F, 4.8F).a(SoundEffectType.f).b()));
	public static final Block QUARTZ_BLOCK = a("quartz_block", new Block(Info.a(Material.STONE, MaterialMapColor.p).h().d(0.8F)));
	public static final Block CHISELED_QUARTZ_BLOCK = a("chiseled_quartz_block", new Block(Info.a(Material.STONE, MaterialMapColor.p).h().d(0.8F)));
	public static final Block QUARTZ_PILLAR = a("quartz_pillar", new BlockRotatable(Info.a(Material.STONE, MaterialMapColor.p).h().d(0.8F)));
	public static final Block QUARTZ_STAIRS = a("quartz_stairs", new BlockStairs(QUARTZ_BLOCK.getBlockData(), Info.a((BlockBase) QUARTZ_BLOCK)));
	public static final Block ACTIVATOR_RAIL = a("activator_rail", new BlockPoweredRail(Info.a(Material.ORIENTABLE).a().d(0.7F).a(SoundEffectType.f)));
	public static final Block DROPPER = a("dropper", new BlockDropper(Info.a(Material.STONE).h().d(3.5F)));
	public static final Block WHITE_TERRACOTTA = a("white_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.L).h().a(1.25F, 4.2F)));
	public static final Block ORANGE_TERRACOTTA = a("orange_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.M).h().a(1.25F, 4.2F)));
	public static final Block MAGENTA_TERRACOTTA = a("magenta_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.N).h().a(1.25F, 4.2F)));
	public static final Block LIGHT_BLUE_TERRACOTTA = a("light_blue_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.O).h().a(1.25F, 4.2F)));
	public static final Block YELLOW_TERRACOTTA = a("yellow_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.P).h().a(1.25F, 4.2F)));
	public static final Block LIME_TERRACOTTA = a("lime_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.Q).h().a(1.25F, 4.2F)));
	public static final Block PINK_TERRACOTTA = a("pink_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.R).h().a(1.25F, 4.2F)));
	public static final Block GRAY_TERRACOTTA = a("gray_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.S).h().a(1.25F, 4.2F)));
	public static final Block LIGHT_GRAY_TERRACOTTA = a("light_gray_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.T).h().a(1.25F, 4.2F)));
	public static final Block CYAN_TERRACOTTA = a("cyan_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.U).h().a(1.25F, 4.2F)));
	public static final Block PURPLE_TERRACOTTA = a("purple_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.V).h().a(1.25F, 4.2F)));
	public static final Block BLUE_TERRACOTTA = a("blue_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.W).h().a(1.25F, 4.2F)));
	public static final Block BROWN_TERRACOTTA = a("brown_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.X).h().a(1.25F, 4.2F)));
	public static final Block GREEN_TERRACOTTA = a("green_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.Y).h().a(1.25F, 4.2F)));
	public static final Block RED_TERRACOTTA = a("red_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.Z).h().a(1.25F, 4.2F)));
	public static final Block BLACK_TERRACOTTA = a("black_terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.aa).h().a(1.25F, 4.2F)));
	public static final Block WHITE_STAINED_GLASS_PANE = a(
		"white_stained_glass_pane", new BlockStainedGlassPane(EnumColor.WHITE, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block ORANGE_STAINED_GLASS_PANE = a(
		"orange_stained_glass_pane", new BlockStainedGlassPane(EnumColor.ORANGE, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block MAGENTA_STAINED_GLASS_PANE = a(
		"magenta_stained_glass_pane", new BlockStainedGlassPane(EnumColor.MAGENTA, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block LIGHT_BLUE_STAINED_GLASS_PANE = a(
		"light_blue_stained_glass_pane", new BlockStainedGlassPane(EnumColor.LIGHT_BLUE, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block YELLOW_STAINED_GLASS_PANE = a(
		"yellow_stained_glass_pane", new BlockStainedGlassPane(EnumColor.YELLOW, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block LIME_STAINED_GLASS_PANE = a(
		"lime_stained_glass_pane", new BlockStainedGlassPane(EnumColor.LIME, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block PINK_STAINED_GLASS_PANE = a(
		"pink_stained_glass_pane", new BlockStainedGlassPane(EnumColor.PINK, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block GRAY_STAINED_GLASS_PANE = a(
		"gray_stained_glass_pane", new BlockStainedGlassPane(EnumColor.GRAY, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block LIGHT_GRAY_STAINED_GLASS_PANE = a(
		"light_gray_stained_glass_pane", new BlockStainedGlassPane(EnumColor.LIGHT_GRAY, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block CYAN_STAINED_GLASS_PANE = a(
		"cyan_stained_glass_pane", new BlockStainedGlassPane(EnumColor.CYAN, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block PURPLE_STAINED_GLASS_PANE = a(
		"purple_stained_glass_pane", new BlockStainedGlassPane(EnumColor.PURPLE, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block BLUE_STAINED_GLASS_PANE = a(
		"blue_stained_glass_pane", new BlockStainedGlassPane(EnumColor.BLUE, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block BROWN_STAINED_GLASS_PANE = a(
		"brown_stained_glass_pane", new BlockStainedGlassPane(EnumColor.BROWN, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block GREEN_STAINED_GLASS_PANE = a(
		"green_stained_glass_pane", new BlockStainedGlassPane(EnumColor.GREEN, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block RED_STAINED_GLASS_PANE = a(
		"red_stained_glass_pane", new BlockStainedGlassPane(EnumColor.RED, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block BLACK_STAINED_GLASS_PANE = a(
		"black_stained_glass_pane", new BlockStainedGlassPane(EnumColor.BLACK, Info.a(Material.SHATTERABLE).d(0.3F).a(SoundEffectType.g).b())
	);
	public static final Block ACACIA_STAIRS = a("acacia_stairs", new BlockStairs(ACACIA_PLANKS.getBlockData(), Info.a((BlockBase) ACACIA_PLANKS)));
	public static final Block DARK_OAK_STAIRS = a("dark_oak_stairs", new BlockStairs(DARK_OAK_PLANKS.getBlockData(), Info.a((BlockBase) DARK_OAK_PLANKS)));
	public static final Block SLIME_BLOCK = a("slime_block", new BlockSlime(Info.a(Material.CLAY, MaterialMapColor.c).a(0.8F).a(SoundEffectType.m).b()));
	public static final Block BARRIER = a("barrier", new BlockBarrier(Info.a(Material.BANNER).a(-1.0F, 3600000.8F).f().b().a(Blocks::a_IBIBBET)));
	public static final Block IRON_TRAPDOOR = a("iron_trapdoor", new BlockTrapdoor(Info.a(Material.ORE).h().d(5.0F).a(SoundEffectType.f).b().a(Blocks::a_IBIBBET)));
	public static final Block PRISMARINE = a("prismarine", new Block(Info.a(Material.STONE, MaterialMapColor.y).h().a(1.5F, 6.0F)));
	public static final Block PRISMARINE_BRICKS = a("prismarine_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.G).h().a(1.5F, 6.0F)));
	public static final Block DARK_PRISMARINE = a("dark_prismarine", new Block(Info.a(Material.STONE, MaterialMapColor.G).h().a(1.5F, 6.0F)));
	public static final Block PRISMARINE_STAIRS = a("prismarine_stairs", new BlockStairs(PRISMARINE.getBlockData(), Info.a((BlockBase) PRISMARINE)));
	public static final Block PRISMARINE_BRICK_STAIRS = a(
		"prismarine_brick_stairs", new BlockStairs(PRISMARINE_BRICKS.getBlockData(), Info.a((BlockBase) PRISMARINE_BRICKS))
	);
	public static final Block DARK_PRISMARINE_STAIRS = a(
		"dark_prismarine_stairs", new BlockStairs(DARK_PRISMARINE.getBlockData(), Info.a((BlockBase) DARK_PRISMARINE))
	);
	public static final Block PRISMARINE_SLAB = a("prismarine_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.y).h().a(1.5F, 6.0F)));
	public static final Block PRISMARINE_BRICK_SLAB = a(
		"prismarine_brick_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.G).h().a(1.5F, 6.0F))
	);
	public static final Block DARK_PRISMARINE_SLAB = a(
		"dark_prismarine_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.G).h().a(1.5F, 6.0F))
	);
	public static final Block SEA_LANTERN = a(
		"sea_lantern", new Block(Info.a(Material.SHATTERABLE, MaterialMapColor.p).d(0.3F).a(SoundEffectType.g).a(var0 -> 15))
	);
	public static final Block HAY_BLOCK = a("hay_block", new BlockHay(Info.a(Material.GRASS, MaterialMapColor.t).d(0.5F).a(SoundEffectType.c)));
	public static final Block WHITE_CARPET = a(
		"white_carpet", new BlockCarpet(EnumColor.WHITE, Info.a(Material.WOOL, MaterialMapColor.j).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block ORANGE_CARPET = a(
		"orange_carpet", new BlockCarpet(EnumColor.ORANGE, Info.a(Material.WOOL, MaterialMapColor.q).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block MAGENTA_CARPET = a(
		"magenta_carpet", new BlockCarpet(EnumColor.MAGENTA, Info.a(Material.WOOL, MaterialMapColor.r).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block LIGHT_BLUE_CARPET = a(
		"light_blue_carpet", new BlockCarpet(EnumColor.LIGHT_BLUE, Info.a(Material.WOOL, MaterialMapColor.s).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block YELLOW_CARPET = a(
		"yellow_carpet", new BlockCarpet(EnumColor.YELLOW, Info.a(Material.WOOL, MaterialMapColor.t).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block LIME_CARPET = a(
		"lime_carpet", new BlockCarpet(EnumColor.LIME, Info.a(Material.WOOL, MaterialMapColor.u).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block PINK_CARPET = a(
		"pink_carpet", new BlockCarpet(EnumColor.PINK, Info.a(Material.WOOL, MaterialMapColor.v).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block GRAY_CARPET = a(
		"gray_carpet", new BlockCarpet(EnumColor.GRAY, Info.a(Material.WOOL, MaterialMapColor.w).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block LIGHT_GRAY_CARPET = a(
		"light_gray_carpet", new BlockCarpet(EnumColor.LIGHT_GRAY, Info.a(Material.WOOL, MaterialMapColor.x).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block CYAN_CARPET = a(
		"cyan_carpet", new BlockCarpet(EnumColor.CYAN, Info.a(Material.WOOL, MaterialMapColor.y).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block PURPLE_CARPET = a(
		"purple_carpet", new BlockCarpet(EnumColor.PURPLE, Info.a(Material.WOOL, MaterialMapColor.z).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block BLUE_CARPET = a(
		"blue_carpet", new BlockCarpet(EnumColor.BLUE, Info.a(Material.WOOL, MaterialMapColor.A).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block BROWN_CARPET = a(
		"brown_carpet", new BlockCarpet(EnumColor.BROWN, Info.a(Material.WOOL, MaterialMapColor.B).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block GREEN_CARPET = a(
		"green_carpet", new BlockCarpet(EnumColor.GREEN, Info.a(Material.WOOL, MaterialMapColor.C).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block RED_CARPET = a(
		"red_carpet", new BlockCarpet(EnumColor.RED, Info.a(Material.WOOL, MaterialMapColor.D).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block BLACK_CARPET = a(
		"black_carpet", new BlockCarpet(EnumColor.BLACK, Info.a(Material.WOOL, MaterialMapColor.E).d(0.1F).a(SoundEffectType.h))
	);
	public static final Block TERRACOTTA = a("terracotta", new Block(Info.a(Material.STONE, MaterialMapColor.q).h().a(1.25F, 4.2F)));
	public static final Block COAL_BLOCK = a("coal_block", new Block(Info.a(Material.STONE, MaterialMapColor.E).h().a(5.0F, 6.0F)));
	public static final Block PACKED_ICE = a("packed_ice", new Block(Info.a(Material.SNOW_LAYER).a(0.98F).d(0.5F).a(SoundEffectType.g)));
	public static final Block SUNFLOWER = a("sunflower", new BlockTallPlantFlower(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block LILAC = a("lilac", new BlockTallPlantFlower(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block ROSE_BUSH = a("rose_bush", new BlockTallPlantFlower(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block PEONY = a("peony", new BlockTallPlantFlower(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block TALL_GRASS = a("tall_grass", new BlockTallPlant(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block LARGE_FERN = a("large_fern", new BlockTallPlant(Info.a(Material.REPLACEABLE_PLANT).a().c().a(SoundEffectType.c)));
	public static final Block WHITE_BANNER = a("white_banner", new BlockBanner(EnumColor.WHITE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block ORANGE_BANNER = a("orange_banner", new BlockBanner(EnumColor.ORANGE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block MAGENTA_BANNER = a("magenta_banner", new BlockBanner(EnumColor.MAGENTA, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block LIGHT_BLUE_BANNER = a(
		"light_blue_banner", new BlockBanner(EnumColor.LIGHT_BLUE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a))
	);
	public static final Block YELLOW_BANNER = a("yellow_banner", new BlockBanner(EnumColor.YELLOW, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block LIME_BANNER = a("lime_banner", new BlockBanner(EnumColor.LIME, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block PINK_BANNER = a("pink_banner", new BlockBanner(EnumColor.PINK, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block GRAY_BANNER = a("gray_banner", new BlockBanner(EnumColor.GRAY, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block LIGHT_GRAY_BANNER = a(
		"light_gray_banner", new BlockBanner(EnumColor.LIGHT_GRAY, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a))
	);
	public static final Block CYAN_BANNER = a("cyan_banner", new BlockBanner(EnumColor.CYAN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block PURPLE_BANNER = a("purple_banner", new BlockBanner(EnumColor.PURPLE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block BLUE_BANNER = a("blue_banner", new BlockBanner(EnumColor.BLUE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block BROWN_BANNER = a("brown_banner", new BlockBanner(EnumColor.BROWN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block GREEN_BANNER = a("green_banner", new BlockBanner(EnumColor.GREEN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block RED_BANNER = a("red_banner", new BlockBanner(EnumColor.RED, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block BLACK_BANNER = a("black_banner", new BlockBanner(EnumColor.BLACK, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a)));
	public static final Block WHITE_WALL_BANNER = a(
		"white_wall_banner", new BlockBannerWall(EnumColor.WHITE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(WHITE_BANNER))
	);
	public static final Block ORANGE_WALL_BANNER = a(
		"orange_wall_banner", new BlockBannerWall(EnumColor.ORANGE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(ORANGE_BANNER))
	);
	public static final Block MAGENTA_WALL_BANNER = a(
		"magenta_wall_banner", new BlockBannerWall(EnumColor.MAGENTA, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(MAGENTA_BANNER))
	);
	public static final Block LIGHT_BLUE_WALL_BANNER = a(
		"light_blue_wall_banner", new BlockBannerWall(EnumColor.LIGHT_BLUE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(LIGHT_BLUE_BANNER))
	);
	public static final Block YELLOW_WALL_BANNER = a(
		"yellow_wall_banner", new BlockBannerWall(EnumColor.YELLOW, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(YELLOW_BANNER))
	);
	public static final Block LIME_WALL_BANNER = a(
		"lime_wall_banner", new BlockBannerWall(EnumColor.LIME, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(LIME_BANNER))
	);
	public static final Block PINK_WALL_BANNER = a(
		"pink_wall_banner", new BlockBannerWall(EnumColor.PINK, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(PINK_BANNER))
	);
	public static final Block GRAY_WALL_BANNER = a(
		"gray_wall_banner", new BlockBannerWall(EnumColor.GRAY, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(GRAY_BANNER))
	);
	public static final Block LIGHT_GRAY_WALL_BANNER = a(
		"light_gray_wall_banner", new BlockBannerWall(EnumColor.LIGHT_GRAY, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(LIGHT_GRAY_BANNER))
	);
	public static final Block CYAN_WALL_BANNER = a(
		"cyan_wall_banner", new BlockBannerWall(EnumColor.CYAN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(CYAN_BANNER))
	);
	public static final Block PURPLE_WALL_BANNER = a(
		"purple_wall_banner", new BlockBannerWall(EnumColor.PURPLE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(PURPLE_BANNER))
	);
	public static final Block BLUE_WALL_BANNER = a(
		"blue_wall_banner", new BlockBannerWall(EnumColor.BLUE, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(BLUE_BANNER))
	);
	public static final Block BROWN_WALL_BANNER = a(
		"brown_wall_banner", new BlockBannerWall(EnumColor.BROWN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(BROWN_BANNER))
	);
	public static final Block GREEN_WALL_BANNER = a(
		"green_wall_banner", new BlockBannerWall(EnumColor.GREEN, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(GREEN_BANNER))
	);
	public static final Block RED_WALL_BANNER = a(
		"red_wall_banner", new BlockBannerWall(EnumColor.RED, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(RED_BANNER))
	);
	public static final Block BLACK_WALL_BANNER = a(
		"black_wall_banner", new BlockBannerWall(EnumColor.BLACK, Info.a(Material.WOOD).a().d(1.0F).a(SoundEffectType.a).a(BLACK_BANNER))
	);
	public static final Block RED_SANDSTONE = a("red_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.q).h().d(0.8F)));
	public static final Block CHISELED_RED_SANDSTONE = a("chiseled_red_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.q).h().d(0.8F)));
	public static final Block CUT_RED_SANDSTONE = a("cut_red_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.q).h().d(0.8F)));
	public static final Block RED_SANDSTONE_STAIRS = a("red_sandstone_stairs", new BlockStairs(RED_SANDSTONE.getBlockData(), Info.a((BlockBase) RED_SANDSTONE)));
	public static final Block OAK_SLAB = a("oak_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.o).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block SPRUCE_SLAB = a(
		"spruce_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.J).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block BIRCH_SLAB = a("birch_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.d).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block JUNGLE_SLAB = a(
		"jungle_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.l).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block ACACIA_SLAB = a(
		"acacia_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.q).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block DARK_OAK_SLAB = a(
		"dark_oak_slab", new BlockStepAbstract(Info.a(Material.WOOD, MaterialMapColor.B).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block STONE_SLAB = a("stone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.m).h().a(2.0F, 6.0F)));
	public static final Block SMOOTH_STONE_SLAB = a("smooth_stone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.m).h().a(2.0F, 6.0F)));
	public static final Block SANDSTONE_SLAB = a("sandstone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.d).h().a(2.0F, 6.0F)));
	public static final Block CUT_SANDSTONE_SLAB = a("cut_sandstone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.d).h().a(2.0F, 6.0F)));
	public static final Block PETRIFIED_OAK_SLAB = a("petrified_oak_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.o).h().a(2.0F, 6.0F)));
	public static final Block COBBLESTONE_SLAB = a("cobblestone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.m).h().a(2.0F, 6.0F)));
	public static final Block BRICK_SLAB = a("brick_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.D).h().a(2.0F, 6.0F)));
	public static final Block STONE_BRICK_SLAB = a("stone_brick_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.m).h().a(2.0F, 6.0F)));
	public static final Block NETHER_BRICK_SLAB = a(
		"nether_brick_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L))
	);
	public static final Block QUARTZ_SLAB = a("quartz_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.p).h().a(2.0F, 6.0F)));
	public static final Block RED_SANDSTONE_SLAB = a("red_sandstone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.q).h().a(2.0F, 6.0F)));
	public static final Block CUT_RED_SANDSTONE_SLAB = a(
		"cut_red_sandstone_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.q).h().a(2.0F, 6.0F))
	);
	public static final Block PURPUR_SLAB = a("purpur_slab", new BlockStepAbstract(Info.a(Material.STONE, MaterialMapColor.r).h().a(2.0F, 6.0F)));
	public static final Block SMOOTH_STONE = a("smooth_stone", new Block(Info.a(Material.STONE, MaterialMapColor.m).h().a(2.0F, 6.0F)));
	public static final Block SMOOTH_SANDSTONE = a("smooth_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().a(2.0F, 6.0F)));
	public static final Block SMOOTH_QUARTZ = a("smooth_quartz", new Block(Info.a(Material.STONE, MaterialMapColor.p).h().a(2.0F, 6.0F)));
	public static final Block SMOOTH_RED_SANDSTONE = a("smooth_red_sandstone", new Block(Info.a(Material.STONE, MaterialMapColor.q).h().a(2.0F, 6.0F)));
	public static final Block SPRUCE_FENCE_GATE = a(
		"spruce_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, SPRUCE_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block BIRCH_FENCE_GATE = a(
		"birch_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, BIRCH_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block JUNGLE_FENCE_GATE = a(
		"jungle_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, JUNGLE_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block ACACIA_FENCE_GATE = a(
		"acacia_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, ACACIA_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block DARK_OAK_FENCE_GATE = a(
		"dark_oak_fence_gate", new BlockFenceGate(Info.a(Material.WOOD, DARK_OAK_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block SPRUCE_FENCE = a("spruce_fence", new BlockFence(Info.a(Material.WOOD, SPRUCE_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block BIRCH_FENCE = a("birch_fence", new BlockFence(Info.a(Material.WOOD, BIRCH_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block JUNGLE_FENCE = a("jungle_fence", new BlockFence(Info.a(Material.WOOD, JUNGLE_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block ACACIA_FENCE = a("acacia_fence", new BlockFence(Info.a(Material.WOOD, ACACIA_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a)));
	public static final Block DARK_OAK_FENCE = a(
		"dark_oak_fence", new BlockFence(Info.a(Material.WOOD, DARK_OAK_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block SPRUCE_DOOR = a("spruce_door", new BlockDoor(Info.a(Material.WOOD, SPRUCE_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block BIRCH_DOOR = a("birch_door", new BlockDoor(Info.a(Material.WOOD, BIRCH_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block JUNGLE_DOOR = a("jungle_door", new BlockDoor(Info.a(Material.WOOD, JUNGLE_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block ACACIA_DOOR = a("acacia_door", new BlockDoor(Info.a(Material.WOOD, ACACIA_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block DARK_OAK_DOOR = a("dark_oak_door", new BlockDoor(Info.a(Material.WOOD, DARK_OAK_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block END_ROD = a("end_rod", new BlockEndRod(Info.a(Material.ORIENTABLE).c().a(var0 -> 14).a(SoundEffectType.a).b()));
	public static final Block CHORUS_PLANT = a(
		"chorus_plant", new BlockChorusFruit(Info.a(Material.PLANT, MaterialMapColor.z).d(0.4F).a(SoundEffectType.a).b())
	);
	public static final Block CHORUS_FLOWER = a(
		"chorus_flower", new BlockChorusFlower((BlockChorusFruit) CHORUS_PLANT, Info.a(Material.PLANT, MaterialMapColor.z).d().d(0.4F).a(SoundEffectType.a).b())
	);
	public static final Block PURPUR_BLOCK = a("purpur_block", new Block(Info.a(Material.STONE, MaterialMapColor.r).h().a(1.5F, 6.0F)));
	public static final Block PURPUR_PILLAR = a("purpur_pillar", new BlockRotatable(Info.a(Material.STONE, MaterialMapColor.r).h().a(1.5F, 6.0F)));
	public static final Block PURPUR_STAIRS = a("purpur_stairs", new BlockStairs(PURPUR_BLOCK.getBlockData(), Info.a((BlockBase) PURPUR_BLOCK)));
	public static final Block END_STONE_BRICKS = a("end_stone_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.d).h().a(3.0F, 9.0F)));
	public static final Block BEETROOTS = a("beetroots", new BlockBeetroot(Info.a(Material.PLANT).a().d().c().a(SoundEffectType.u)));
	public static final Block GRASS_PATH = a("grass_path", new BlockGrassPath(Info.a(Material.EARTH).d(0.65F).a(SoundEffectType.c).c(Blocks::a).b(Blocks::a)));
	public static final Block END_GATEWAY = a(
		"end_gateway", new BlockEndGateway(Info.a(Material.PORTAL, MaterialMapColor.E).a().a(var0 -> 15).a(-1.0F, 3600000.0F).f())
	);
	public static final Block REPEATING_COMMAND_BLOCK = a(
		"repeating_command_block", new BlockCommand(Info.a(Material.ORE, MaterialMapColor.z).h().a(-1.0F, 3600000.0F).f())
	);
	public static final Block CHAIN_COMMAND_BLOCK = a(
		"chain_command_block", new BlockCommand(Info.a(Material.ORE, MaterialMapColor.C).h().a(-1.0F, 3600000.0F).f())
	);
	public static final Block FROSTED_ICE = a(
		"frosted_ice",
		new BlockIceFrost(
			Info.a(Material.ICE)
				.a(0.98F)
				.d()
				.d(0.5F)
				.a(SoundEffectType.g)
				.b()
				.a((var0, var1x, var2, var3x) -> var3x == EntityTypes.POLAR_BEAR)
		)
	);
	public static final Block MAGMA_BLOCK = a(
		"magma_block",
		new BlockMagma(
			Info.a(Material.STONE, MaterialMapColor.K)
				.h()
				.a(var0 -> 3)
				.d()
				.d(0.5F)
				.a((var0, var1x, var2, var3x) -> var3x.c())
				.d(Blocks::a)
				.e(Blocks::a)
		)
	);
	public static final Block NETHER_WART_BLOCK = a("nether_wart_block", new Block(Info.a(Material.GRASS, MaterialMapColor.D).d(1.0F).a(SoundEffectType.J)));
	public static final Block RED_NETHER_BRICKS = a(
		"red_nether_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L))
	);
	public static final Block BONE_BLOCK = a("bone_block", new BlockRotatable(Info.a(Material.STONE, MaterialMapColor.d).h().d(2.0F).a(SoundEffectType.O)));
	public static final Block STRUCTURE_VOID = a("structure_void", new BlockStructureVoid(Info.a(Material.STRUCTURE_VOID).a().f()));
	public static final Block OBSERVER = a("observer", new BlockObserver(Info.a(Material.STONE).d(3.0F).h().a(Blocks::b_IBIBBET)));
	public static final Block SHULKER_BOX = a("shulker_box", a(null, Info.a(Material.SHULKER_SHELL)));
	public static final Block WHITE_SHULKER_BOX = a("white_shulker_box", a(EnumColor.WHITE, Info.a(Material.SHULKER_SHELL, MaterialMapColor.j)));
	public static final Block ORANGE_SHULKER_BOX = a("orange_shulker_box", a(EnumColor.ORANGE, Info.a(Material.SHULKER_SHELL, MaterialMapColor.q)));
	public static final Block MAGENTA_SHULKER_BOX = a("magenta_shulker_box", a(EnumColor.MAGENTA, Info.a(Material.SHULKER_SHELL, MaterialMapColor.r)));
	public static final Block LIGHT_BLUE_SHULKER_BOX = a("light_blue_shulker_box", a(EnumColor.LIGHT_BLUE, Info.a(Material.SHULKER_SHELL, MaterialMapColor.s)));
	public static final Block YELLOW_SHULKER_BOX = a("yellow_shulker_box", a(EnumColor.YELLOW, Info.a(Material.SHULKER_SHELL, MaterialMapColor.t)));
	public static final Block LIME_SHULKER_BOX = a("lime_shulker_box", a(EnumColor.LIME, Info.a(Material.SHULKER_SHELL, MaterialMapColor.u)));
	public static final Block PINK_SHULKER_BOX = a("pink_shulker_box", a(EnumColor.PINK, Info.a(Material.SHULKER_SHELL, MaterialMapColor.v)));
	public static final Block GRAY_SHULKER_BOX = a("gray_shulker_box", a(EnumColor.GRAY, Info.a(Material.SHULKER_SHELL, MaterialMapColor.w)));
	public static final Block LIGHT_GRAY_SHULKER_BOX = a("light_gray_shulker_box", a(EnumColor.LIGHT_GRAY, Info.a(Material.SHULKER_SHELL, MaterialMapColor.x)));
	public static final Block CYAN_SHULKER_BOX = a("cyan_shulker_box", a(EnumColor.CYAN, Info.a(Material.SHULKER_SHELL, MaterialMapColor.y)));
	public static final Block PURPLE_SHULKER_BOX = a("purple_shulker_box", a(EnumColor.PURPLE, Info.a(Material.SHULKER_SHELL, MaterialMapColor.V)));
	public static final Block BLUE_SHULKER_BOX = a("blue_shulker_box", a(EnumColor.BLUE, Info.a(Material.SHULKER_SHELL, MaterialMapColor.A)));
	public static final Block BROWN_SHULKER_BOX = a("brown_shulker_box", a(EnumColor.BROWN, Info.a(Material.SHULKER_SHELL, MaterialMapColor.B)));
	public static final Block GREEN_SHULKER_BOX = a("green_shulker_box", a(EnumColor.GREEN, Info.a(Material.SHULKER_SHELL, MaterialMapColor.C)));
	public static final Block RED_SHULKER_BOX = a("red_shulker_box", a(EnumColor.RED, Info.a(Material.SHULKER_SHELL, MaterialMapColor.D)));
	public static final Block BLACK_SHULKER_BOX = a("black_shulker_box", a(EnumColor.BLACK, Info.a(Material.SHULKER_SHELL, MaterialMapColor.E)));
	public static final Block WHITE_GLAZED_TERRACOTTA = a(
		"white_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.WHITE).h().d(1.4F))
	);
	public static final Block ORANGE_GLAZED_TERRACOTTA = a(
		"orange_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.ORANGE).h().d(1.4F))
	);
	public static final Block MAGENTA_GLAZED_TERRACOTTA = a(
		"magenta_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.MAGENTA).h().d(1.4F))
	);
	public static final Block LIGHT_BLUE_GLAZED_TERRACOTTA = a(
		"light_blue_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.LIGHT_BLUE).h().d(1.4F))
	);
	public static final Block YELLOW_GLAZED_TERRACOTTA = a(
		"yellow_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.YELLOW).h().d(1.4F))
	);
	public static final Block LIME_GLAZED_TERRACOTTA = a(
		"lime_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.LIME).h().d(1.4F))
	);
	public static final Block PINK_GLAZED_TERRACOTTA = a(
		"pink_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.PINK).h().d(1.4F))
	);
	public static final Block GRAY_GLAZED_TERRACOTTA = a(
		"gray_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.GRAY).h().d(1.4F))
	);
	public static final Block LIGHT_GRAY_GLAZED_TERRACOTTA = a(
		"light_gray_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.LIGHT_GRAY).h().d(1.4F))
	);
	public static final Block CYAN_GLAZED_TERRACOTTA = a(
		"cyan_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.CYAN).h().d(1.4F))
	);
	public static final Block PURPLE_GLAZED_TERRACOTTA = a(
		"purple_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.PURPLE).h().d(1.4F))
	);
	public static final Block BLUE_GLAZED_TERRACOTTA = a(
		"blue_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.BLUE).h().d(1.4F))
	);
	public static final Block BROWN_GLAZED_TERRACOTTA = a(
		"brown_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.BROWN).h().d(1.4F))
	);
	public static final Block GREEN_GLAZED_TERRACOTTA = a(
		"green_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.GREEN).h().d(1.4F))
	);
	public static final Block RED_GLAZED_TERRACOTTA = a("red_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.RED).h().d(1.4F)));
	public static final Block BLACK_GLAZED_TERRACOTTA = a(
		"black_glazed_terracotta", new BlockGlazedTerracotta(Info.a(Material.STONE, EnumColor.BLACK).h().d(1.4F))
	);
	public static final Block WHITE_CONCRETE = a("white_concrete", new Block(Info.a(Material.STONE, EnumColor.WHITE).h().d(1.8F)));
	public static final Block ORANGE_CONCRETE = a("orange_concrete", new Block(Info.a(Material.STONE, EnumColor.ORANGE).h().d(1.8F)));
	public static final Block MAGENTA_CONCRETE = a("magenta_concrete", new Block(Info.a(Material.STONE, EnumColor.MAGENTA).h().d(1.8F)));
	public static final Block LIGHT_BLUE_CONCRETE = a("light_blue_concrete", new Block(Info.a(Material.STONE, EnumColor.LIGHT_BLUE).h().d(1.8F)));
	public static final Block YELLOW_CONCRETE = a("yellow_concrete", new Block(Info.a(Material.STONE, EnumColor.YELLOW).h().d(1.8F)));
	public static final Block LIME_CONCRETE = a("lime_concrete", new Block(Info.a(Material.STONE, EnumColor.LIME).h().d(1.8F)));
	public static final Block PINK_CONCRETE = a("pink_concrete", new Block(Info.a(Material.STONE, EnumColor.PINK).h().d(1.8F)));
	public static final Block GRAY_CONCRETE = a("gray_concrete", new Block(Info.a(Material.STONE, EnumColor.GRAY).h().d(1.8F)));
	public static final Block LIGHT_GRAY_CONCRETE = a("light_gray_concrete", new Block(Info.a(Material.STONE, EnumColor.LIGHT_GRAY).h().d(1.8F)));
	public static final Block CYAN_CONCRETE = a("cyan_concrete", new Block(Info.a(Material.STONE, EnumColor.CYAN).h().d(1.8F)));
	public static final Block PURPLE_CONCRETE = a("purple_concrete", new Block(Info.a(Material.STONE, EnumColor.PURPLE).h().d(1.8F)));
	public static final Block BLUE_CONCRETE = a("blue_concrete", new Block(Info.a(Material.STONE, EnumColor.BLUE).h().d(1.8F)));
	public static final Block BROWN_CONCRETE = a("brown_concrete", new Block(Info.a(Material.STONE, EnumColor.BROWN).h().d(1.8F)));
	public static final Block GREEN_CONCRETE = a("green_concrete", new Block(Info.a(Material.STONE, EnumColor.GREEN).h().d(1.8F)));
	public static final Block RED_CONCRETE = a("red_concrete", new Block(Info.a(Material.STONE, EnumColor.RED).h().d(1.8F)));
	public static final Block BLACK_CONCRETE = a("black_concrete", new Block(Info.a(Material.STONE, EnumColor.BLACK).h().d(1.8F)));
	public static final Block WHITE_CONCRETE_POWDER = a(
		"white_concrete_powder", new BlockConcretePowder(WHITE_CONCRETE, Info.a(Material.SAND, EnumColor.WHITE).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block ORANGE_CONCRETE_POWDER = a(
		"orange_concrete_powder", new BlockConcretePowder(ORANGE_CONCRETE, Info.a(Material.SAND, EnumColor.ORANGE).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block MAGENTA_CONCRETE_POWDER = a(
		"magenta_concrete_powder", new BlockConcretePowder(MAGENTA_CONCRETE, Info.a(Material.SAND, EnumColor.MAGENTA).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block LIGHT_BLUE_CONCRETE_POWDER = a(
		"light_blue_concrete_powder", new BlockConcretePowder(LIGHT_BLUE_CONCRETE, Info.a(Material.SAND, EnumColor.LIGHT_BLUE).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block YELLOW_CONCRETE_POWDER = a(
		"yellow_concrete_powder", new BlockConcretePowder(YELLOW_CONCRETE, Info.a(Material.SAND, EnumColor.YELLOW).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block LIME_CONCRETE_POWDER = a(
		"lime_concrete_powder", new BlockConcretePowder(LIME_CONCRETE, Info.a(Material.SAND, EnumColor.LIME).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block PINK_CONCRETE_POWDER = a(
		"pink_concrete_powder", new BlockConcretePowder(PINK_CONCRETE, Info.a(Material.SAND, EnumColor.PINK).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block GRAY_CONCRETE_POWDER = a(
		"gray_concrete_powder", new BlockConcretePowder(GRAY_CONCRETE, Info.a(Material.SAND, EnumColor.GRAY).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block LIGHT_GRAY_CONCRETE_POWDER = a(
		"light_gray_concrete_powder", new BlockConcretePowder(LIGHT_GRAY_CONCRETE, Info.a(Material.SAND, EnumColor.LIGHT_GRAY).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block CYAN_CONCRETE_POWDER = a(
		"cyan_concrete_powder", new BlockConcretePowder(CYAN_CONCRETE, Info.a(Material.SAND, EnumColor.CYAN).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block PURPLE_CONCRETE_POWDER = a(
		"purple_concrete_powder", new BlockConcretePowder(PURPLE_CONCRETE, Info.a(Material.SAND, EnumColor.PURPLE).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block BLUE_CONCRETE_POWDER = a(
		"blue_concrete_powder", new BlockConcretePowder(BLUE_CONCRETE, Info.a(Material.SAND, EnumColor.BLUE).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block BROWN_CONCRETE_POWDER = a(
		"brown_concrete_powder", new BlockConcretePowder(BROWN_CONCRETE, Info.a(Material.SAND, EnumColor.BROWN).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block GREEN_CONCRETE_POWDER = a(
		"green_concrete_powder", new BlockConcretePowder(GREEN_CONCRETE, Info.a(Material.SAND, EnumColor.GREEN).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block RED_CONCRETE_POWDER = a(
		"red_concrete_powder", new BlockConcretePowder(RED_CONCRETE, Info.a(Material.SAND, EnumColor.RED).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block BLACK_CONCRETE_POWDER = a(
		"black_concrete_powder", new BlockConcretePowder(BLACK_CONCRETE, Info.a(Material.SAND, EnumColor.BLACK).d(0.5F).a(SoundEffectType.i))
	);
	public static final Block KELP = a("kelp", new BlockKelp(Info.a(Material.WATER_PLANT).a().d().c().a(SoundEffectType.o)));
	public static final Block KELP_PLANT = a("kelp_plant", new BlockKelpPlant(Info.a(Material.WATER_PLANT).a().c().a(SoundEffectType.o)));
	public static final Block DRIED_KELP_BLOCK = a("dried_kelp_block", new Block(Info.a(Material.GRASS, MaterialMapColor.C).a(0.5F, 2.5F).a(SoundEffectType.c)));
	public static final Block TURTLE_EGG = a(
		"turtle_egg", new BlockTurtleEgg(Info.a(Material.DRAGON_EGG, MaterialMapColor.d).d(0.5F).a(SoundEffectType.f).d().b())
	);
	public static final Block DEAD_TUBE_CORAL_BLOCK = a("dead_tube_coral_block", new Block(Info.a(Material.STONE, MaterialMapColor.w).h().a(1.5F, 6.0F)));
	public static final Block DEAD_BRAIN_CORAL_BLOCK = a("dead_brain_coral_block", new Block(Info.a(Material.STONE, MaterialMapColor.w).h().a(1.5F, 6.0F)));
	public static final Block DEAD_BUBBLE_CORAL_BLOCK = a("dead_bubble_coral_block", new Block(Info.a(Material.STONE, MaterialMapColor.w).h().a(1.5F, 6.0F)));
	public static final Block DEAD_FIRE_CORAL_BLOCK = a("dead_fire_coral_block", new Block(Info.a(Material.STONE, MaterialMapColor.w).h().a(1.5F, 6.0F)));
	public static final Block DEAD_HORN_CORAL_BLOCK = a("dead_horn_coral_block", new Block(Info.a(Material.STONE, MaterialMapColor.w).h().a(1.5F, 6.0F)));
	public static final Block TUBE_CORAL_BLOCK = a(
		"tube_coral_block", new BlockCoral(DEAD_TUBE_CORAL_BLOCK, Info.a(Material.STONE, MaterialMapColor.A).h().a(1.5F, 6.0F).a(SoundEffectType.p))
	);
	public static final Block BRAIN_CORAL_BLOCK = a(
		"brain_coral_block", new BlockCoral(DEAD_BRAIN_CORAL_BLOCK, Info.a(Material.STONE, MaterialMapColor.v).h().a(1.5F, 6.0F).a(SoundEffectType.p))
	);
	public static final Block BUBBLE_CORAL_BLOCK = a(
		"bubble_coral_block", new BlockCoral(DEAD_BUBBLE_CORAL_BLOCK, Info.a(Material.STONE, MaterialMapColor.z).h().a(1.5F, 6.0F).a(SoundEffectType.p))
	);
	public static final Block FIRE_CORAL_BLOCK = a(
		"fire_coral_block", new BlockCoral(DEAD_FIRE_CORAL_BLOCK, Info.a(Material.STONE, MaterialMapColor.D).h().a(1.5F, 6.0F).a(SoundEffectType.p))
	);
	public static final Block HORN_CORAL_BLOCK = a(
		"horn_coral_block", new BlockCoral(DEAD_HORN_CORAL_BLOCK, Info.a(Material.STONE, MaterialMapColor.t).h().a(1.5F, 6.0F).a(SoundEffectType.p))
	);
	public static final Block DEAD_TUBE_CORAL = a("dead_tube_coral", new BlockCoralDead(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_BRAIN_CORAL = a("dead_brain_coral", new BlockCoralDead(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_BUBBLE_CORAL = a("dead_bubble_coral", new BlockCoralDead(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_FIRE_CORAL = a("dead_fire_coral", new BlockCoralDead(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_HORN_CORAL = a("dead_horn_coral", new BlockCoralDead(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block TUBE_CORAL = a(
		"tube_coral", new BlockCoralPlant(DEAD_TUBE_CORAL, Info.a(Material.WATER_PLANT, MaterialMapColor.A).a().c().a(SoundEffectType.o))
	);
	public static final Block BRAIN_CORAL = a(
		"brain_coral", new BlockCoralPlant(DEAD_BRAIN_CORAL, Info.a(Material.WATER_PLANT, MaterialMapColor.v).a().c().a(SoundEffectType.o))
	);
	public static final Block BUBBLE_CORAL = a(
		"bubble_coral", new BlockCoralPlant(DEAD_BUBBLE_CORAL, Info.a(Material.WATER_PLANT, MaterialMapColor.z).a().c().a(SoundEffectType.o))
	);
	public static final Block FIRE_CORAL = a(
		"fire_coral", new BlockCoralPlant(DEAD_FIRE_CORAL, Info.a(Material.WATER_PLANT, MaterialMapColor.D).a().c().a(SoundEffectType.o))
	);
	public static final Block HORN_CORAL = a(
		"horn_coral", new BlockCoralPlant(DEAD_HORN_CORAL, Info.a(Material.WATER_PLANT, MaterialMapColor.t).a().c().a(SoundEffectType.o))
	);
	public static final Block DEAD_TUBE_CORAL_FAN = a("dead_tube_coral_fan", new BlockCoralFanAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_BRAIN_CORAL_FAN = a(
		"dead_brain_coral_fan", new BlockCoralFanAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c())
	);
	public static final Block DEAD_BUBBLE_CORAL_FAN = a(
		"dead_bubble_coral_fan", new BlockCoralFanAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c())
	);
	public static final Block DEAD_FIRE_CORAL_FAN = a("dead_fire_coral_fan", new BlockCoralFanAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block DEAD_HORN_CORAL_FAN = a("dead_horn_coral_fan", new BlockCoralFanAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c()));
	public static final Block TUBE_CORAL_FAN = a(
		"tube_coral_fan", new BlockCoralFan(DEAD_TUBE_CORAL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.A).a().c().a(SoundEffectType.o))
	);
	public static final Block BRAIN_CORAL_FAN = a(
		"brain_coral_fan", new BlockCoralFan(DEAD_BRAIN_CORAL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.v).a().c().a(SoundEffectType.o))
	);
	public static final Block BUBBLE_CORAL_FAN = a(
		"bubble_coral_fan", new BlockCoralFan(DEAD_BUBBLE_CORAL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.z).a().c().a(SoundEffectType.o))
	);
	public static final Block FIRE_CORAL_FAN = a(
		"fire_coral_fan", new BlockCoralFan(DEAD_FIRE_CORAL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.D).a().c().a(SoundEffectType.o))
	);
	public static final Block HORN_CORAL_FAN = a(
		"horn_coral_fan", new BlockCoralFan(DEAD_HORN_CORAL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.t).a().c().a(SoundEffectType.o))
	);
	public static final Block DEAD_TUBE_CORAL_WALL_FAN = a(
		"dead_tube_coral_wall_fan", new BlockCoralFanWallAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c().a(DEAD_TUBE_CORAL_FAN))
	);
	public static final Block TUBE_CORAL_WALL_FAN = a(
		"tube_coral_wall_fan",
		new BlockCoralFanWall(DEAD_TUBE_CORAL_WALL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.A).a().c().a(SoundEffectType.o).a(TUBE_CORAL_FAN))
	);
	public static final Block DEAD_BRAIN_CORAL_WALL_FAN = a(
		"dead_brain_coral_wall_fan", new BlockCoralFanWallAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c().a(DEAD_BRAIN_CORAL_FAN))
	);
	public static final Block BRAIN_CORAL_WALL_FAN = a(
		"brain_coral_wall_fan",
		new BlockCoralFanWall(DEAD_BRAIN_CORAL_WALL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.v).a().c().a(SoundEffectType.o).a(BRAIN_CORAL_FAN))
	);
	public static final Block DEAD_BUBBLE_CORAL_WALL_FAN = a(
		"dead_bubble_coral_wall_fan", new BlockCoralFanWallAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c().a(DEAD_BUBBLE_CORAL_FAN))
	);
	public static final Block BUBBLE_CORAL_WALL_FAN = a(
		"bubble_coral_wall_fan",
		new BlockCoralFanWall(DEAD_BUBBLE_CORAL_WALL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.z).a().c().a(SoundEffectType.o).a(BUBBLE_CORAL_FAN))
	);
	public static final Block DEAD_FIRE_CORAL_WALL_FAN = a(
		"dead_fire_coral_wall_fan", new BlockCoralFanWallAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c().a(DEAD_FIRE_CORAL_FAN))
	);
	public static final Block FIRE_CORAL_WALL_FAN = a(
		"fire_coral_wall_fan",
		new BlockCoralFanWall(DEAD_FIRE_CORAL_WALL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.D).a().c().a(SoundEffectType.o).a(FIRE_CORAL_FAN))
	);
	public static final Block DEAD_HORN_CORAL_WALL_FAN = a(
		"dead_horn_coral_wall_fan", new BlockCoralFanWallAbstract(Info.a(Material.STONE, MaterialMapColor.w).h().a().c().a(DEAD_HORN_CORAL_FAN))
	);
	public static final Block HORN_CORAL_WALL_FAN = a(
		"horn_coral_wall_fan",
		new BlockCoralFanWall(DEAD_HORN_CORAL_WALL_FAN, Info.a(Material.WATER_PLANT, MaterialMapColor.t).a().c().a(SoundEffectType.o).a(HORN_CORAL_FAN))
	);
	public static final Block SEA_PICKLE = a(
		"sea_pickle",
		new BlockSeaPickle(
			Info.a(Material.WATER_PLANT, MaterialMapColor.C)
				.a(var0 -> BlockSeaPickle.h(var0) ? 0 : 3 + 3 * var0.get(BlockSeaPickle.a))
				.a(SoundEffectType.m)
				.b()
		)
	);
	public static final Block BLUE_ICE = a("blue_ice", new BlockHalfTransparent(Info.a(Material.SNOW_LAYER).d(2.8F).a(0.989F).a(SoundEffectType.g)));
	public static final Block CONDUIT = a("conduit", new BlockConduit(Info.a(Material.SHATTERABLE, MaterialMapColor.G).d(3.0F).a(var0 -> 15).b()));
	public static final Block BAMBOO_SAPLING = a(
		"bamboo_sapling", new BlockBambooSapling(Info.a(Material.BAMBOO_SAPLING).d().c().a().d(1.0F).a(SoundEffectType.r))
	);
	public static final Block BAMBOO = a("bamboo", new BlockBamboo(Info.a(Material.BAMBOO, MaterialMapColor.i).d().c().d(1.0F).a(SoundEffectType.q).b()));
	public static final Block POTTED_BAMBOO = a("potted_bamboo", new BlockFlowerPot(BAMBOO, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block VOID_AIR = a("void_air", new BlockAir(Info.a(Material.AIR).a().f().g()));
	public static final Block CAVE_AIR = a("cave_air", new BlockAir(Info.a(Material.AIR).a().f().g()));
	public static final Block BUBBLE_COLUMN = a("bubble_column", new BlockBubbleColumn(Info.a(Material.BUBBLE_COLUMN).a().f()));
	public static final Block POLISHED_GRANITE_STAIRS = a(
		"polished_granite_stairs", new BlockStairs(POLISHED_GRANITE.getBlockData(), Info.a((BlockBase) POLISHED_GRANITE))
	);
	public static final Block SMOOTH_RED_SANDSTONE_STAIRS = a(
		"smooth_red_sandstone_stairs", new BlockStairs(SMOOTH_RED_SANDSTONE.getBlockData(), Info.a((BlockBase) SMOOTH_RED_SANDSTONE))
	);
	public static final Block MOSSY_STONE_BRICK_STAIRS = a(
		"mossy_stone_brick_stairs", new BlockStairs(MOSSY_STONE_BRICKS.getBlockData(), Info.a((BlockBase) MOSSY_STONE_BRICKS))
	);
	public static final Block POLISHED_DIORITE_STAIRS = a(
		"polished_diorite_stairs", new BlockStairs(POLISHED_DIORITE.getBlockData(), Info.a((BlockBase) POLISHED_DIORITE))
	);
	public static final Block MOSSY_COBBLESTONE_STAIRS = a(
		"mossy_cobblestone_stairs", new BlockStairs(MOSSY_COBBLESTONE.getBlockData(), Info.a((BlockBase) MOSSY_COBBLESTONE))
	);
	public static final Block END_STONE_BRICK_STAIRS = a(
		"end_stone_brick_stairs", new BlockStairs(END_STONE_BRICKS.getBlockData(), Info.a((BlockBase) END_STONE_BRICKS))
	);
	public static final Block STONE_STAIRS = a("stone_stairs", new BlockStairs(STONE.getBlockData(), Info.a((BlockBase) STONE)));
	public static final Block SMOOTH_SANDSTONE_STAIRS = a(
		"smooth_sandstone_stairs", new BlockStairs(SMOOTH_SANDSTONE.getBlockData(), Info.a((BlockBase) SMOOTH_SANDSTONE))
	);
	public static final Block SMOOTH_QUARTZ_STAIRS = a("smooth_quartz_stairs", new BlockStairs(SMOOTH_QUARTZ.getBlockData(), Info.a((BlockBase) SMOOTH_QUARTZ)));
	public static final Block GRANITE_STAIRS = a("granite_stairs", new BlockStairs(GRANITE.getBlockData(), Info.a((BlockBase) GRANITE)));
	public static final Block ANDESITE_STAIRS = a("andesite_stairs", new BlockStairs(ANDESITE.getBlockData(), Info.a((BlockBase) ANDESITE)));
	public static final Block RED_NETHER_BRICK_STAIRS = a(
		"red_nether_brick_stairs", new BlockStairs(RED_NETHER_BRICKS.getBlockData(), Info.a((BlockBase) RED_NETHER_BRICKS))
	);
	public static final Block POLISHED_ANDESITE_STAIRS = a(
		"polished_andesite_stairs", new BlockStairs(POLISHED_ANDESITE.getBlockData(), Info.a((BlockBase) POLISHED_ANDESITE))
	);
	public static final Block DIORITE_STAIRS = a("diorite_stairs", new BlockStairs(DIORITE.getBlockData(), Info.a((BlockBase) DIORITE)));
	public static final Block POLISHED_GRANITE_SLAB = a("polished_granite_slab", new BlockStepAbstract(Info.a((BlockBase) POLISHED_GRANITE)));
	public static final Block SMOOTH_RED_SANDSTONE_SLAB = a("smooth_red_sandstone_slab", new BlockStepAbstract(Info.a((BlockBase) SMOOTH_RED_SANDSTONE)));
	public static final Block MOSSY_STONE_BRICK_SLAB = a("mossy_stone_brick_slab", new BlockStepAbstract(Info.a((BlockBase) MOSSY_STONE_BRICKS)));
	public static final Block POLISHED_DIORITE_SLAB = a("polished_diorite_slab", new BlockStepAbstract(Info.a((BlockBase) POLISHED_DIORITE)));
	public static final Block MOSSY_COBBLESTONE_SLAB = a("mossy_cobblestone_slab", new BlockStepAbstract(Info.a((BlockBase) MOSSY_COBBLESTONE)));
	public static final Block END_STONE_BRICK_SLAB = a("end_stone_brick_slab", new BlockStepAbstract(Info.a((BlockBase) END_STONE_BRICKS)));
	public static final Block SMOOTH_SANDSTONE_SLAB = a("smooth_sandstone_slab", new BlockStepAbstract(Info.a((BlockBase) SMOOTH_SANDSTONE)));
	public static final Block SMOOTH_QUARTZ_SLAB = a("smooth_quartz_slab", new BlockStepAbstract(Info.a((BlockBase) SMOOTH_QUARTZ)));
	public static final Block GRANITE_SLAB = a("granite_slab", new BlockStepAbstract(Info.a((BlockBase) GRANITE)));
	public static final Block ANDESITE_SLAB = a("andesite_slab", new BlockStepAbstract(Info.a((BlockBase) ANDESITE)));
	public static final Block RED_NETHER_BRICK_SLAB = a("red_nether_brick_slab", new BlockStepAbstract(Info.a((BlockBase) RED_NETHER_BRICKS)));
	public static final Block POLISHED_ANDESITE_SLAB = a("polished_andesite_slab", new BlockStepAbstract(Info.a((BlockBase) POLISHED_ANDESITE)));
	public static final Block DIORITE_SLAB = a("diorite_slab", new BlockStepAbstract(Info.a((BlockBase) DIORITE)));
	public static final Block BRICK_WALL = a("brick_wall", new BlockCobbleWall(Info.a((BlockBase) BRICKS)));
	public static final Block PRISMARINE_WALL = a("prismarine_wall", new BlockCobbleWall(Info.a((BlockBase) PRISMARINE)));
	public static final Block RED_SANDSTONE_WALL = a("red_sandstone_wall", new BlockCobbleWall(Info.a((BlockBase) RED_SANDSTONE)));
	public static final Block MOSSY_STONE_BRICK_WALL = a("mossy_stone_brick_wall", new BlockCobbleWall(Info.a((BlockBase) MOSSY_STONE_BRICKS)));
	public static final Block GRANITE_WALL = a("granite_wall", new BlockCobbleWall(Info.a((BlockBase) GRANITE)));
	public static final Block STONE_BRICK_WALL = a("stone_brick_wall", new BlockCobbleWall(Info.a((BlockBase) STONE_BRICKS)));
	public static final Block NETHER_BRICK_WALL = a("nether_brick_wall", new BlockCobbleWall(Info.a((BlockBase) NETHER_BRICKS)));
	public static final Block ANDESITE_WALL = a("andesite_wall", new BlockCobbleWall(Info.a((BlockBase) ANDESITE)));
	public static final Block RED_NETHER_BRICK_WALL = a("red_nether_brick_wall", new BlockCobbleWall(Info.a((BlockBase) RED_NETHER_BRICKS)));
	public static final Block SANDSTONE_WALL = a("sandstone_wall", new BlockCobbleWall(Info.a((BlockBase) SANDSTONE)));
	public static final Block END_STONE_BRICK_WALL = a("end_stone_brick_wall", new BlockCobbleWall(Info.a((BlockBase) END_STONE_BRICKS)));
	public static final Block DIORITE_WALL = a("diorite_wall", new BlockCobbleWall(Info.a((BlockBase) DIORITE)));
	public static final Block SCAFFOLDING = a("scaffolding", new BlockScaffolding(Info.a(Material.ORIENTABLE, MaterialMapColor.d).a().a(SoundEffectType.s).e()));
	public static final Block LOOM = a("loom", new BlockLoom(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block BARREL = a("barrel", new BlockBarrel(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block SMOKER = a("smoker", new BlockSmoker(Info.a(Material.STONE).h().d(3.5F).a(a(13))));
	public static final Block BLAST_FURNACE = a("blast_furnace", new BlockBlastFurnace(Info.a(Material.STONE).h().d(3.5F).a(a(13))));
	public static final Block CARTOGRAPHY_TABLE = a("cartography_table", new BlockCartographyTable(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block FLETCHING_TABLE = a("fletching_table", new BlockFletchingTable(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block GRINDSTONE = a(
		"grindstone", new BlockGrindstone(Info.a(Material.HEAVY, MaterialMapColor.h).h().a(2.0F, 6.0F).a(SoundEffectType.e))
	);
	public static final Block LECTERN = a("lectern", new BlockLectern(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block SMITHING_TABLE = a("smithing_table", new BlockSmithingTable(Info.a(Material.WOOD).d(2.5F).a(SoundEffectType.a)));
	public static final Block STONECUTTER = a("stonecutter", new BlockStonecutter(Info.a(Material.STONE).h().d(3.5F)));
	public static final Block BELL = a("bell", new BlockBell(Info.a(Material.ORE, MaterialMapColor.F).h().d(5.0F).a(SoundEffectType.l)));
	public static final Block LANTERN = a("lantern", new BlockLantern(Info.a(Material.ORE).h().d(3.5F).a(SoundEffectType.y).a(var0 -> 15).b()));
	public static final Block SOUL_LANTERN = a("soul_lantern", new BlockLantern(Info.a(Material.ORE).h().d(3.5F).a(SoundEffectType.y).a(var0 -> 10).b()));
	public static final Block CAMPFIRE = a(
		"campfire", new BlockCampfire(true, 1, Info.a(Material.WOOD, MaterialMapColor.J).d(2.0F).a(SoundEffectType.a).a(a(15)).b())
	);
	public static final Block SOUL_CAMPFIRE = a(
		"soul_campfire", new BlockCampfire(false, 2, Info.a(Material.WOOD, MaterialMapColor.J).d(2.0F).a(SoundEffectType.a).a(a(10)).b())
	);
	public static final Block SWEET_BERRY_BUSH = a("sweet_berry_bush", new BlockSweetBerryBush(Info.a(Material.PLANT).d().a().a(SoundEffectType.t)));
	public static final Block WARPED_STEM = a("warped_stem", a(MaterialMapColor.af));
	public static final Block STRIPPED_WARPED_STEM = a("stripped_warped_stem", a(MaterialMapColor.af));
	public static final Block WARPED_HYPHAE = a(
		"warped_hyphae", new BlockRotatable(Info.a(Material.NETHER_WOOD, MaterialMapColor.ag).d(2.0F).a(SoundEffectType.z))
	);
	public static final Block STRIPPED_WARPED_HYPHAE = a(
		"stripped_warped_hyphae", new BlockRotatable(Info.a(Material.NETHER_WOOD, MaterialMapColor.ag).d(2.0F).a(SoundEffectType.z))
	);
	public static final Block WARPED_NYLIUM = a(
		"warped_nylium", new BlockNylium(Info.a(Material.STONE, MaterialMapColor.ae).h().d(0.4F).a(SoundEffectType.A).d())
	);
	public static final Block WARPED_FUNGUS = a(
		"warped_fungus",
		new BlockFungi(Info.a(Material.PLANT, MaterialMapColor.y).c().a().a(SoundEffectType.B), () -> BiomeDecoratorGroups.WARPED_FUNGI_PLANTED)
	);
	public static final Block WARPED_WART_BLOCK = a("warped_wart_block", new Block(Info.a(Material.GRASS, MaterialMapColor.ah).d(1.0F).a(SoundEffectType.J)));
	public static final Block WARPED_ROOTS = a("warped_roots", new BlockRoots(Info.a(Material.h, MaterialMapColor.y).a().c().a(SoundEffectType.C)));
	public static final Block NETHER_SPROUTS = a("nether_sprouts", new BlockNetherSprouts(Info.a(Material.h, MaterialMapColor.y).a().c().a(SoundEffectType.M)));
	public static final Block CRIMSON_STEM = a("crimson_stem", a(MaterialMapColor.ac));
	public static final Block STRIPPED_CRIMSON_STEM = a("stripped_crimson_stem", a(MaterialMapColor.ac));
	public static final Block CRIMSON_HYPHAE = a(
		"crimson_hyphae", new BlockRotatable(Info.a(Material.NETHER_WOOD, MaterialMapColor.ad).d(2.0F).a(SoundEffectType.z))
	);
	public static final Block STRIPPED_CRIMSON_HYPHAE = a(
		"stripped_crimson_hyphae", new BlockRotatable(Info.a(Material.NETHER_WOOD, MaterialMapColor.ad).d(2.0F).a(SoundEffectType.z))
	);
	public static final Block CRIMSON_NYLIUM = a(
		"crimson_nylium", new BlockNylium(Info.a(Material.STONE, MaterialMapColor.ab).h().d(0.4F).a(SoundEffectType.A).d())
	);
	public static final Block CRIMSON_FUNGUS = a(
		"crimson_fungus",
		new BlockFungi(Info.a(Material.PLANT, MaterialMapColor.K).c().a().a(SoundEffectType.B), () -> BiomeDecoratorGroups.CRIMSON_FUNGI_PLANTED)
	);
	public static final Block SHROOMLIGHT = a("shroomlight", new Block(Info.a(Material.GRASS, MaterialMapColor.D).d(1.0F).a(SoundEffectType.D).a(var0 -> 15)));
	public static final Block WEEPING_VINES = a(
		"weeping_vines", new BlockWeepingVines(Info.a(Material.PLANT, MaterialMapColor.K).d().a().c().a(SoundEffectType.E))
	);
	public static final Block WEEPING_VINES_PLANT = a(
		"weeping_vines_plant", new BlockWeepingVinesPlant(Info.a(Material.PLANT, MaterialMapColor.K).a().c().a(SoundEffectType.E))
	);
	public static final Block TWISTING_VINES = a(
		"twisting_vines", new BlockTwistingVines(Info.a(Material.PLANT, MaterialMapColor.y).d().a().c().a(SoundEffectType.E))
	);
	public static final Block TWISTING_VINES_PLANT = a(
		"twisting_vines_plant", new BlockTwistingVinesPlant(Info.a(Material.PLANT, MaterialMapColor.y).a().c().a(SoundEffectType.E))
	);
	public static final Block CRIMSON_ROOTS = a("crimson_roots", new BlockRoots(Info.a(Material.h, MaterialMapColor.K).a().c().a(SoundEffectType.C)));
	public static final Block CRIMSON_PLANKS = a(
		"crimson_planks", new Block(Info.a(Material.NETHER_WOOD, MaterialMapColor.ac).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block WARPED_PLANKS = a(
		"warped_planks", new Block(Info.a(Material.NETHER_WOOD, MaterialMapColor.af).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block CRIMSON_SLAB = a(
		"crimson_slab", new BlockStepAbstract(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block WARPED_SLAB = a(
		"warped_slab", new BlockStepAbstract(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block CRIMSON_PRESSURE_PLATE = a(
		"crimson_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block WARPED_PRESSURE_PLATE = a(
		"warped_pressure_plate",
		new BlockPressurePlateBinary(EnumMobType.EVERYTHING, Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a().d(0.5F).a(SoundEffectType.a))
	);
	public static final Block CRIMSON_FENCE = a(
		"crimson_fence", new BlockFence(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block WARPED_FENCE = a(
		"warped_fence", new BlockFence(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block CRIMSON_TRAPDOOR = a(
		"crimson_trapdoor", new BlockTrapdoor(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block WARPED_TRAPDOOR = a(
		"warped_trapdoor", new BlockTrapdoor(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b().a(Blocks::a_IBIBBET))
	);
	public static final Block CRIMSON_FENCE_GATE = a(
		"crimson_fence_gate", new BlockFenceGate(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block WARPED_FENCE_GATE = a(
		"warped_fence_gate", new BlockFenceGate(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a(2.0F, 3.0F).a(SoundEffectType.a))
	);
	public static final Block CRIMSON_STAIRS = a("crimson_stairs", new BlockStairs(CRIMSON_PLANKS.getBlockData(), Info.a((BlockBase) CRIMSON_PLANKS)));
	public static final Block WARPED_STAIRS = a("warped_stairs", new BlockStairs(WARPED_PLANKS.getBlockData(), Info.a((BlockBase) WARPED_PLANKS)));
	public static final Block CRIMSON_BUTTON = a("crimson_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block WARPED_BUTTON = a("warped_button", new BlockWoodButton(Info.a(Material.ORIENTABLE).a().d(0.5F).a(SoundEffectType.a)));
	public static final Block CRIMSON_DOOR = a("crimson_door", new BlockDoor(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block WARPED_DOOR = a("warped_door", new BlockDoor(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).d(3.0F).a(SoundEffectType.a).b()));
	public static final Block CRIMSON_SIGN = a(
		"crimson_sign", new BlockFloorSign(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.g)
	);
	public static final Block CRIMSON_WALL_SIGN = a(
		"crimson_wall_sign",
		new BlockWallSign(Info.a(Material.NETHER_WOOD, CRIMSON_PLANKS.s()).a().d(1.0F).a(SoundEffectType.a).a(CRIMSON_SIGN), BlockPropertyWood.g)
	);
	public static final Block WARPED_SIGN = a(
		"warped_sign", new BlockFloorSign(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a().d(1.0F).a(SoundEffectType.a), BlockPropertyWood.h)
	);
	public static final Block WARPED_WALL_SIGN = a(
		"warped_wall_sign",
		new BlockWallSign(Info.a(Material.NETHER_WOOD, WARPED_PLANKS.s()).a().d(1.0F).a(SoundEffectType.a).a(WARPED_SIGN), BlockPropertyWood.h)
	);
	public static final Block STRUCTURE_BLOCK = a("structure_block", new BlockStructure(Info.a(Material.ORE, MaterialMapColor.x).h().a(-1.0F, 3600000.0F).f()));
	public static final Block JIGSAW = a("jigsaw", new BlockJigsaw(Info.a(Material.ORE, MaterialMapColor.x).h().a(-1.0F, 3600000.0F).f()));
	public static final Block COMPOSTER = a("composter", new BlockComposter(Info.a(Material.WOOD).d(0.6F).a(SoundEffectType.a)));
	public static final Block TARGET = a("target", new BlockTarget(Info.a(Material.GRASS, MaterialMapColor.p).d(0.5F).a(SoundEffectType.c)));
	public static final Block BEE_NEST = a("bee_nest", new BlockBeehive(Info.a(Material.WOOD, MaterialMapColor.t).d(0.3F).a(SoundEffectType.a)));
	public static final Block BEEHIVE = a("beehive", new BlockBeehive(Info.a(Material.WOOD).d(0.6F).a(SoundEffectType.a)));
	public static final Block HONEY_BLOCK = a("honey_block", new BlockHoney(Info.a(Material.CLAY, MaterialMapColor.q).b(0.4F).c(0.5F).b().a(SoundEffectType.n)));
	public static final Block HONEYCOMB_BLOCK = a("honeycomb_block", new Block(Info.a(Material.CLAY, MaterialMapColor.q).d(0.6F).a(SoundEffectType.p)));
	public static final Block NETHERITE_BLOCK = a(
		"netherite_block", new Block(Info.a(Material.ORE, MaterialMapColor.E).h().a(50.0F, 1200.0F).a(SoundEffectType.P))
	);
	public static final Block ANCIENT_DEBRIS = a(
		"ancient_debris", new Block(Info.a(Material.ORE, MaterialMapColor.E).h().a(30.0F, 1200.0F).a(SoundEffectType.Q))
	);
	public static final Block CRYING_OBSIDIAN = a(
		"crying_obsidian", new BlockCryingObsidian(Info.a(Material.STONE, MaterialMapColor.E).h().a(50.0F, 1200.0F).a(var0 -> 10))
	);
	public static final Block RESPAWN_ANCHOR = a(
		"respawn_anchor", new BlockRespawnAnchor(Info.a(Material.STONE, MaterialMapColor.E).h().a(50.0F, 1200.0F).a(var0 -> BlockRespawnAnchor.a(var0, 15)))
	);
	public static final Block POTTED_CRIMSON_FUNGUS = a("potted_crimson_fungus", new BlockFlowerPot(CRIMSON_FUNGUS, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_WARPED_FUNGUS = a("potted_warped_fungus", new BlockFlowerPot(WARPED_FUNGUS, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_CRIMSON_ROOTS = a("potted_crimson_roots", new BlockFlowerPot(CRIMSON_ROOTS, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block POTTED_WARPED_ROOTS = a("potted_warped_roots", new BlockFlowerPot(WARPED_ROOTS, Info.a(Material.ORIENTABLE).c().b()));
	public static final Block LODESTONE = a("lodestone", new Block(Info.a(Material.HEAVY).h().d(3.5F).a(SoundEffectType.R)));
	public static final Block BLACKSTONE = a("blackstone", new Block(Info.a(Material.STONE, MaterialMapColor.E).h().a(1.5F, 6.0F)));
	public static final Block BLACKSTONE_STAIRS = a("blackstone_stairs", new BlockStairs(BLACKSTONE.getBlockData(), Info.a((BlockBase) BLACKSTONE)));
	public static final Block BLACKSTONE_WALL = a("blackstone_wall", new BlockCobbleWall(Info.a((BlockBase) BLACKSTONE)));
	public static final Block BLACKSTONE_SLAB = a("blackstone_slab", new BlockStepAbstract(Info.a((BlockBase) BLACKSTONE).a(2.0F, 6.0F)));
	public static final Block POLISHED_BLACKSTONE = a("polished_blackstone", new Block(Info.a((BlockBase) BLACKSTONE).a(2.0F, 6.0F)));
	public static final Block POLISHED_BLACKSTONE_BRICKS = a("polished_blackstone_bricks", new Block(Info.a((BlockBase) POLISHED_BLACKSTONE).a(1.5F, 6.0F)));
	public static final Block CRACKED_POLISHED_BLACKSTONE_BRICKS = a(
		"cracked_polished_blackstone_bricks", new Block(Info.a((BlockBase) POLISHED_BLACKSTONE_BRICKS))
	);
	public static final Block POLISHED_BLACKSTONE_BRICK_SLAB = a(
		"polished_blackstone_brick_slab", new BlockStepAbstract(Info.a((BlockBase) POLISHED_BLACKSTONE_BRICKS).a(2.0F, 6.0F))
	);
	public static final Block POLISHED_BLACKSTONE_BRICK_STAIRS = a(
		"polished_blackstone_brick_stairs", new BlockStairs(POLISHED_BLACKSTONE_BRICKS.getBlockData(), Info.a((BlockBase) POLISHED_BLACKSTONE_BRICKS))
	);
	public static final Block POLISHED_BLACKSTONE_BRICK_WALL = a(
		"polished_blackstone_brick_wall", new BlockCobbleWall(Info.a((BlockBase) POLISHED_BLACKSTONE_BRICKS))
	);
	public static final Block CHISELED_POLISHED_BLACKSTONE = a("chiseled_polished_blackstone", new Block(Info.a((BlockBase) POLISHED_BLACKSTONE).a(1.5F, 6.0F)));
	public static final Block POLISHED_BLACKSTONE_STAIRS = a(
		"polished_blackstone_stairs", new BlockStairs(POLISHED_BLACKSTONE.getBlockData(), Info.a((BlockBase) POLISHED_BLACKSTONE))
	);
	public static final Block POLISHED_BLACKSTONE_SLAB = a("polished_blackstone_slab", new BlockStepAbstract(Info.a((BlockBase) POLISHED_BLACKSTONE)));
	public static final Block POLISHED_BLACKSTONE_WALL = a("polished_blackstone_wall", new BlockCobbleWall(Info.a((BlockBase) POLISHED_BLACKSTONE)));
	public static final Block GILDED_BLACKSTONE = a("gilded_blackstone", new Block(Info.a((BlockBase) BLACKSTONE).a(SoundEffectType.U)));
	public static final Block POLISHED_BLACKSTONE_PRESSURE_PLATE = a(
		"polished_blackstone_pressure_plate", new BlockPressurePlateBinary(EnumMobType.MOBS, Info.a(Material.STONE, MaterialMapColor.E).h().a().d(0.5F))
	);
	public static final Block POLISHED_BLACKSTONE_BUTTON = a("polished_blackstone_button", new BlockStoneButton(Info.a(Material.ORIENTABLE).a().d(0.5F)));
	public static final Block CHISELED_NETHER_BRICKS = a(
		"chiseled_nether_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L))
	);
	public static final Block CRACKED_NETHER_BRICKS = a(
		"cracked_nether_bricks", new Block(Info.a(Material.STONE, MaterialMapColor.K).h().a(2.0F, 6.0F).a(SoundEffectType.L))
	);
	public static final Block QUARTZ_BRICKS = a("quartz_bricks", new Block(Info.a((BlockBase) QUARTZ_BLOCK)));

	static {
		for (Block var1 : IRegistry.BLOCK) {
			for (IBlockData var3 : var1.getStates().a()) {
				Block.REGISTRY_ID.b(var3);
			}

			var1.r();
		}
	}

	private static ToIntFunction<IBlockData> a(int var0) {
		return var1 -> var1.get(BlockProperties.r) ? var0 : 0;
	}

	private static Boolean a_IBIBBET(IBlockData var0, IBlockAccess var1, BlockPosition var2, EntityTypes<?> var3) {
		return false;
	}

	private static Boolean b_IBIBBET(IBlockData var0, IBlockAccess var1, BlockPosition var2, EntityTypes<?> var3) {
		return true;
	}

	private static Boolean c(IBlockData var0, IBlockAccess var1, BlockPosition var2, EntityTypes<?> var3) {
		return var3 == EntityTypes.OCELOT || var3 == EntityTypes.PARROT;
	}

	private static BlockBed a(EnumColor var0) {
		return new BlockBed(
			var0,
			Info.a(Material.CLOTH, var1 -> var1.get(BlockBed.PART) == BlockPropertyBedPart.FOOT ? var0.f() : MaterialMapColor.e)
				.a(SoundEffectType.a)
				.d(0.2F)
				.b()
		);
	}

	private static BlockRotatable a(MaterialMapColor var0, MaterialMapColor var1) {
		return new BlockRotatable(Info.a(Material.WOOD, var2 -> var2.get(BlockRotatable.AXIS) == EnumAxis.Y ? var0 : var1).d(2.0F).a(SoundEffectType.a));
	}

	private static Block a(MaterialMapColor var0) {
		return new BlockRotatable(Info.a(Material.NETHER_WOOD, var1 -> var0).d(2.0F).a(SoundEffectType.z));
	}

	private static boolean a(IBlockData var0, IBlockAccess var1, BlockPosition var2) {
		return true;
	}

	private static boolean b(IBlockData var0, IBlockAccess var1, BlockPosition var2) {
		return false;
	}

	private static BlockStainedGlass b(EnumColor var0) {
		return new BlockStainedGlass(
			var0, Info.a(Material.SHATTERABLE, var0).d(0.3F).a(SoundEffectType.g).b().a(Blocks::a_IBIBBET).a(Blocks::b_IBIBBET).b(Blocks::b).c(Blocks::b)
		);
	}

	private static BlockLeaves b() {
		return new BlockLeaves(Info.a(Material.LEAVES).d(0.2F).d().a(SoundEffectType.c).b().a(Blocks::c).b(Blocks::b).c(Blocks::b));
	}

	private static BlockShulkerBox a(EnumColor var0, Info var1) {
		e var2 = (var0x, var1x, var2x) -> {
			TileEntity var3 = var1x.getTileEntity(var2x);
			if (!(var3 instanceof TileEntityShulkerBox var4)) {
				return true;
			} else {
				return var4.l();
			}
		};
		return new BlockShulkerBox(var0, var1.d(2.0F).e().b().b(var2).c(var2));
	}

	private static BlockPiston a(boolean var0) {
		e var1 = (var0x, var1x, var2) -> !var0x.get(BlockPiston.EXTENDED);
		return new BlockPiston(var0, Info.a(Material.PISTON).d(1.5F).a(Blocks::b_IBIBBET).b(var1).c(var1));
	}

	private static Block a(String var0, Block var1) {
		return IRegistry.a(IRegistry.BLOCK, var0, var1);
	}

	public static void a() {
		Block.REGISTRY_ID.forEach(BlockData::a);
	}
}
