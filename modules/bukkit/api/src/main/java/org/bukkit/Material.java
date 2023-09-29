package org.bukkit;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An enum of all material IDs accepted by the official server and client
 */
@SuppressWarnings({"DeprecatedIsStillUsed", "deprecation"}) // Paper
public enum Material implements Keyed {
	//<editor-fold desc="Materials" defaultstate="collapsed">
	AIR(9648, 0),
	STONE(22948),
	GRANITE(21091),
	POLISHED_GRANITE(5477),
	DIORITE(24688),
	POLISHED_DIORITE(31615),
	ANDESITE(25975),
	POLISHED_ANDESITE(8335),
	/**
	 * BlockData: {@link Snowable}
	 */
	GRASS_BLOCK(28346, Snowable.class),
	DIRT(10580),
	COARSE_DIRT(15411),
	/**
	 * BlockData: {@link Snowable}
	 */
	PODZOL(24068, Snowable.class),
	CRIMSON_NYLIUM(18139),
	WARPED_NYLIUM(26396),
	COBBLESTONE(32147),
	OAK_PLANKS(14905),
	SPRUCE_PLANKS(14593),
	BIRCH_PLANKS(29322),
	JUNGLE_PLANKS(26445),
	ACACIA_PLANKS(31312),
	DARK_OAK_PLANKS(20869),
	CRIMSON_PLANKS(18812),
	WARPED_PLANKS(16045),
	/**
	 * BlockData: {@link Sapling}
	 */
	OAK_SAPLING(9636, Sapling.class),
	/**
	 * BlockData: {@link Sapling}
	 */
	SPRUCE_SAPLING(19874, Sapling.class),
	/**
	 * BlockData: {@link Sapling}
	 */
	BIRCH_SAPLING(31533, Sapling.class),
	/**
	 * BlockData: {@link Sapling}
	 */
	JUNGLE_SAPLING(17951, Sapling.class),
	/**
	 * BlockData: {@link Sapling}
	 */
	ACACIA_SAPLING(20806, Sapling.class),
	/**
	 * BlockData: {@link Sapling}
	 */
	DARK_OAK_SAPLING(14933, Sapling.class),
	BEDROCK(23130),
	SAND(11542),
	RED_SAND(16279),
	GRAVEL(7804),
	GOLD_ORE(32625),
	IRON_ORE(19834),
	COAL_ORE(30965),
	NETHER_GOLD_ORE(4185),
	/**
	 * BlockData: {@link Orientable}
	 */
	OAK_LOG(26723, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	SPRUCE_LOG(9726, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	BIRCH_LOG(26727, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	JUNGLE_LOG(20721, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	ACACIA_LOG(8385, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	DARK_OAK_LOG(14831, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	CRIMSON_STEM(27920, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	WARPED_STEM(28920, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_OAK_LOG(20523, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_SPRUCE_LOG(6140, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_BIRCH_LOG(8838, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_JUNGLE_LOG(15476, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_ACACIA_LOG(18167, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_DARK_OAK_LOG(6492, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_CRIMSON_STEM(16882, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_WARPED_STEM(15627, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_OAK_WOOD(31455, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_SPRUCE_WOOD(6467, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_BIRCH_WOOD(22350, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_JUNGLE_WOOD(30315, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_ACACIA_WOOD(27193, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_DARK_OAK_WOOD(16000, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_CRIMSON_HYPHAE(27488, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	STRIPPED_WARPED_HYPHAE(7422, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	OAK_WOOD(7378, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	SPRUCE_WOOD(32328, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	BIRCH_WOOD(20913, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	JUNGLE_WOOD(10341, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	ACACIA_WOOD(9541, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	DARK_OAK_WOOD(16995, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	CRIMSON_HYPHAE(6550, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	WARPED_HYPHAE(18439, Orientable.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	OAK_LEAVES(4385, Leaves.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	SPRUCE_LEAVES(20039, Leaves.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	BIRCH_LEAVES(12601, Leaves.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	JUNGLE_LEAVES(5133, Leaves.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	ACACIA_LEAVES(16606, Leaves.class),
	/**
	 * BlockData: {@link Leaves}
	 */
	DARK_OAK_LEAVES(22254, Leaves.class),
	SPONGE(15860),
	WET_SPONGE(9043),
	GLASS(6195),
	LAPIS_ORE(22934),
	LAPIS_BLOCK(14485),
	/**
	 * BlockData: {@link Dispenser}
	 */
	DISPENSER(20871, Dispenser.class),
	SANDSTONE(13141),
	CHISELED_SANDSTONE(31763),
	CUT_SANDSTONE(6118),
	/**
	 * BlockData: {@link NoteBlock}
	 */
	NOTE_BLOCK(20979, NoteBlock.class),
	/**
	 * BlockData: {@link RedstoneRail}
	 */
	POWERED_RAIL(11064, RedstoneRail.class),
	/**
	 * BlockData: {@link RedstoneRail}
	 */
	DETECTOR_RAIL(13475, RedstoneRail.class),
	/**
	 * BlockData: {@link Piston}
	 */
	STICKY_PISTON(18127, Piston.class),
	COBWEB(9469),
	GRASS(6155),
	FERN(15794),
	DEAD_BUSH(22888),
	SEAGRASS(23942),
	/**
	 * BlockData: {@link SeaPickle}
	 */
	SEA_PICKLE(19562, SeaPickle.class),
	/**
	 * BlockData: {@link Piston}
	 */
	PISTON(21130, Piston.class),
	WHITE_WOOL(8624),
	ORANGE_WOOL(23957),
	MAGENTA_WOOL(11853),
	LIGHT_BLUE_WOOL(21073),
	YELLOW_WOOL(29507),
	LIME_WOOL(10443),
	PINK_WOOL(7611),
	GRAY_WOOL(27209),
	LIGHT_GRAY_WOOL(22936),
	CYAN_WOOL(12221),
	PURPLE_WOOL(11922),
	BLUE_WOOL(15738),
	BROWN_WOOL(32638),
	GREEN_WOOL(25085),
	RED_WOOL(11621),
	BLACK_WOOL(16693),
	DANDELION(30558),
	POPPY(12851),
	BLUE_ORCHID(13432),
	ALLIUM(6871),
	AZURE_BLUET(17608),
	RED_TULIP(16781),
	ORANGE_TULIP(26038),
	WHITE_TULIP(31495),
	PINK_TULIP(27319),
	OXEYE_DAISY(11709),
	CORNFLOWER(15405),
	LILY_OF_THE_VALLEY(7185),
	WITHER_ROSE(8619),
	BROWN_MUSHROOM(9665),
	RED_MUSHROOM(19728),
	CRIMSON_FUNGUS(26268),
	WARPED_FUNGUS(19799),
	CRIMSON_ROOTS(14064),
	WARPED_ROOTS(13932),
	NETHER_SPROUTS(10431),
	/**
	 * BlockData: {@link Ageable}
	 */
	WEEPING_VINES(29267, Ageable.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	TWISTING_VINES(27283, Ageable.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	SUGAR_CANE(7726, Ageable.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	KELP(21916, Ageable.class),
	/**
	 * BlockData: {@link Bamboo}
	 */
	BAMBOO(18728, Bamboo.class),
	GOLD_BLOCK(27392),
	IRON_BLOCK(24754),
	/**
	 * BlockData: {@link Slab}
	 */
	OAK_SLAB(12002, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SPRUCE_SLAB(28798, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	BIRCH_SLAB(13807, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	JUNGLE_SLAB(19117, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	ACACIA_SLAB(23730, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	DARK_OAK_SLAB(28852, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	CRIMSON_SLAB(4691, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	WARPED_SLAB(27150, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	STONE_SLAB(19838, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SMOOTH_STONE_SLAB(24129, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SANDSTONE_SLAB(29830, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	CUT_SANDSTONE_SLAB(30944, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	PETRIFIED_OAK_SLAB(18658, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	COBBLESTONE_SLAB(6340, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	BRICK_SLAB(26333, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	STONE_BRICK_SLAB(19676, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	NETHER_BRICK_SLAB(26586, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	QUARTZ_SLAB(4423, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	RED_SANDSTONE_SLAB(17550, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	CUT_RED_SANDSTONE_SLAB(7220, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	PURPUR_SLAB(11487, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	PRISMARINE_SLAB(31323, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	PRISMARINE_BRICK_SLAB(25624, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	DARK_PRISMARINE_SLAB(7577, Slab.class),
	SMOOTH_QUARTZ(14415),
	SMOOTH_RED_SANDSTONE(25180),
	SMOOTH_SANDSTONE(30039),
	SMOOTH_STONE(21910),
	BRICKS(14165),
	/**
	 * BlockData: {@link TNT}
	 */
	TNT(7896, TNT.class),
	BOOKSHELF(10069),
	MOSSY_COBBLESTONE(21900),
	OBSIDIAN(32723),
	TORCH(6063),
	/**
	 * BlockData: {@link Directional}
	 */
	END_ROD(24832, Directional.class),
	/**
	 * BlockData: {@link MultipleFacing}
	 */
	CHORUS_PLANT(28243, MultipleFacing.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	CHORUS_FLOWER(28542, Ageable.class),
	PURPUR_BLOCK(7538),
	/**
	 * BlockData: {@link Orientable}
	 */
	PURPUR_PILLAR(26718, Orientable.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	PURPUR_STAIRS(8921, Stairs.class),
	SPAWNER(7018),
	/**
	 * BlockData: {@link Stairs}
	 */
	OAK_STAIRS(5449, Stairs.class),
	/**
	 * BlockData: {@link Chest}
	 */
	CHEST(22969, Chest.class),
	DIAMOND_ORE(9292),
	DIAMOND_BLOCK(5944),
	CRAFTING_TABLE(20706),
	/**
	 * BlockData: {@link Farmland}
	 */
	FARMLAND(31166, Farmland.class),
	/**
	 * BlockData: {@link Furnace}
	 */
	FURNACE(8133, Furnace.class),
	/**
	 * BlockData: {@link Ladder}
	 */
	LADDER(23599, Ladder.class),
	/**
	 * BlockData: {@link Rail}
	 */
	RAIL(13285, Rail.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	COBBLESTONE_STAIRS(24715, Stairs.class),
	/**
	 * BlockData: {@link Switch}
	 */
	LEVER(15319, Switch.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	STONE_PRESSURE_PLATE(22591, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	OAK_PRESSURE_PLATE(20108, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	SPRUCE_PRESSURE_PLATE(15932, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	BIRCH_PRESSURE_PLATE(9664, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	JUNGLE_PRESSURE_PLATE(11376, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	ACACIA_PRESSURE_PLATE(17586, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	DARK_OAK_PRESSURE_PLATE(31375, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	CRIMSON_PRESSURE_PLATE(18316, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	WARPED_PRESSURE_PLATE(29516, Powerable.class),
	/**
	 * BlockData: {@link Powerable}
	 */
	POLISHED_BLACKSTONE_PRESSURE_PLATE(32340, Powerable.class),
	/**
	 * BlockData: {@link Lightable}
	 */
	REDSTONE_ORE(10887, Lightable.class),
	/**
	 * BlockData: {@link Lightable}
	 */
	REDSTONE_TORCH(22547, Lightable.class),
	/**
	 * BlockData: {@link Snow}
	 */
	SNOW(14146, Snow.class),
	ICE(30428),
	SNOW_BLOCK(19913),
	/**
	 * BlockData: {@link Ageable}
	 */
	CACTUS(12191, Ageable.class),
	CLAY(27880),
	/**
	 * BlockData: {@link Jukebox}
	 */
	JUKEBOX(19264, Jukebox.class),
	/**
	 * BlockData: {@link Fence}
	 */
	OAK_FENCE(6442, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	SPRUCE_FENCE(25416, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	BIRCH_FENCE(17347, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	JUNGLE_FENCE(14358, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	ACACIA_FENCE(4569, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	DARK_OAK_FENCE(21767, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	CRIMSON_FENCE(21075, Fence.class),
	/**
	 * BlockData: {@link Fence}
	 */
	WARPED_FENCE(18438, Fence.class),
	PUMPKIN(19170),
	/**
	 * BlockData: {@link Directional}
	 */
	CARVED_PUMPKIN(25833, Directional.class),
	NETHERRACK(23425),
	SOUL_SAND(16841),
	SOUL_SOIL(31140),
	/**
	 * BlockData: {@link Orientable}
	 */
	BASALT(28478, Orientable.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	POLISHED_BASALT(11659, Orientable.class),
	/**
	 * BlockData: {@link Lightable}
	 */
	SOUL_TORCH(14292, Lightable.class),
	GLOWSTONE(32713),
	/**
	 * BlockData: {@link Directional}
	 */
	JACK_O_LANTERN(13758, Directional.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	OAK_TRAPDOOR(16927, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	SPRUCE_TRAPDOOR(10289, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	BIRCH_TRAPDOOR(32585, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	JUNGLE_TRAPDOOR(8626, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	ACACIA_TRAPDOOR(18343, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	DARK_OAK_TRAPDOOR(10355, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	CRIMSON_TRAPDOOR(25056, TrapDoor.class),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	WARPED_TRAPDOOR(7708, TrapDoor.class),
	INFESTED_STONE(18440),
	INFESTED_COBBLESTONE(4348),
	INFESTED_STONE_BRICKS(19749),
	INFESTED_MOSSY_STONE_BRICKS(9850),
	INFESTED_CRACKED_STONE_BRICKS(7476),
	INFESTED_CHISELED_STONE_BRICKS(4728),
	STONE_BRICKS(6962),
	MOSSY_STONE_BRICKS(16415),
	CRACKED_STONE_BRICKS(27869),
	CHISELED_STONE_BRICKS(9087),
	/**
	 * BlockData: {@link MultipleFacing}
	 */
	BROWN_MUSHROOM_BLOCK(6291, MultipleFacing.class),
	/**
	 * BlockData: {@link MultipleFacing}
	 */
	RED_MUSHROOM_BLOCK(20766, MultipleFacing.class),
	/**
	 * BlockData: {@link MultipleFacing}
	 */
	MUSHROOM_STEM(16543, MultipleFacing.class),
	/**
	 * BlockData: {@link Fence}
	 */
	IRON_BARS(9378, Fence.class),
	/**
	 * BlockData: {@link Chain}
	 */
	CHAIN(28265, Chain.class),
	/**
	 * BlockData: {@link Fence}
	 */
	GLASS_PANE(5709, Fence.class),
	MELON(25172),
	/**
	 * BlockData: {@link MultipleFacing}
	 */
	VINE(14564, MultipleFacing.class),
	/**
	 * BlockData: {@link Gate}
	 */
	OAK_FENCE_GATE(16689, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	SPRUCE_FENCE_GATE(26423, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	BIRCH_FENCE_GATE(6322, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	JUNGLE_FENCE_GATE(21360, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	ACACIA_FENCE_GATE(14145, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	DARK_OAK_FENCE_GATE(10679, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	CRIMSON_FENCE_GATE(15602, Gate.class),
	/**
	 * BlockData: {@link Gate}
	 */
	WARPED_FENCE_GATE(11115, Gate.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	BRICK_STAIRS(21534, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	STONE_BRICK_STAIRS(27032, Stairs.class),
	/**
	 * BlockData: {@link Snowable}
	 */
	MYCELIUM(9913, Snowable.class),
	LILY_PAD(19271),
	NETHER_BRICKS(27802),
	CRACKED_NETHER_BRICKS(10888),
	CHISELED_NETHER_BRICKS(21613),
	/**
	 * BlockData: {@link Fence}
	 */
	NETHER_BRICK_FENCE(5286, Fence.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	NETHER_BRICK_STAIRS(12085, Stairs.class),
	ENCHANTING_TABLE(16255),
	/**
	 * BlockData: {@link EndPortalFrame}
	 */
	END_PORTAL_FRAME(15480, EndPortalFrame.class),
	END_STONE(29686),
	END_STONE_BRICKS(20314),
	DRAGON_EGG(29946),
	/**
	 * BlockData: {@link Lightable}
	 */
	REDSTONE_LAMP(8217, Lightable.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	SANDSTONE_STAIRS(18474, Stairs.class),
	EMERALD_ORE(16630),
	/**
	 * BlockData: {@link EnderChest}
	 */
	ENDER_CHEST(32349, EnderChest.class),
	/**
	 * BlockData: {@link TripwireHook}
	 */
	TRIPWIRE_HOOK(8130, TripwireHook.class),
	EMERALD_BLOCK(9914),
	/**
	 * BlockData: {@link Stairs}
	 */
	SPRUCE_STAIRS(11192, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	BIRCH_STAIRS(7657, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	JUNGLE_STAIRS(20636, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	CRIMSON_STAIRS(32442, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	WARPED_STAIRS(17721, Stairs.class),
	/**
	 * BlockData: {@link CommandBlock}
	 */
	COMMAND_BLOCK(4355, CommandBlock.class),
	BEACON(6608),
	/**
	 * BlockData: {@link Wall}
	 */
	COBBLESTONE_WALL(12616, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	MOSSY_COBBLESTONE_WALL(11536, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	BRICK_WALL(18995, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	PRISMARINE_WALL(18184, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	RED_SANDSTONE_WALL(4753, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	MOSSY_STONE_BRICK_WALL(18259, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	GRANITE_WALL(23279, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	STONE_BRICK_WALL(29073, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	NETHER_BRICK_WALL(10398, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	ANDESITE_WALL(14938, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	RED_NETHER_BRICK_WALL(4580, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	SANDSTONE_WALL(18470, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	END_STONE_BRICK_WALL(27225, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	DIORITE_WALL(17412, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	BLACKSTONE_WALL(17327, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	POLISHED_BLACKSTONE_WALL(15119, Wall.class),
	/**
	 * BlockData: {@link Wall}
	 */
	POLISHED_BLACKSTONE_BRICK_WALL(9540, Wall.class),
	/**
	 * BlockData: {@link Switch}
	 */
	STONE_BUTTON(12279, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	OAK_BUTTON(13510, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	SPRUCE_BUTTON(23281, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	BIRCH_BUTTON(26934, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	JUNGLE_BUTTON(25317, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	ACACIA_BUTTON(13993, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	DARK_OAK_BUTTON(6214, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	CRIMSON_BUTTON(26799, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	WARPED_BUTTON(25264, Switch.class),
	/**
	 * BlockData: {@link Switch}
	 */
	POLISHED_BLACKSTONE_BUTTON(20760, Switch.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ANVIL(18718, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	CHIPPED_ANVIL(10623, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	DAMAGED_ANVIL(10274, Directional.class),
	/**
	 * BlockData: {@link Chest}
	 */
	TRAPPED_CHEST(18970, Chest.class),
	/**
	 * BlockData: {@link AnaloguePowerable}
	 */
	LIGHT_WEIGHTED_PRESSURE_PLATE(14875, AnaloguePowerable.class),
	/**
	 * BlockData: {@link AnaloguePowerable}
	 */
	HEAVY_WEIGHTED_PRESSURE_PLATE(16970, AnaloguePowerable.class),
	/**
	 * BlockData: {@link DaylightDetector}
	 */
	DAYLIGHT_DETECTOR(8864, DaylightDetector.class),
	REDSTONE_BLOCK(19496),
	NETHER_QUARTZ_ORE(4807),
	/**
	 * BlockData: {@link Hopper}
	 */
	HOPPER(31974, Hopper.class),
	CHISELED_QUARTZ_BLOCK(30964),
	QUARTZ_BLOCK(11987),
	QUARTZ_BRICKS(23358),
	/**
	 * BlockData: {@link Orientable}
	 */
	QUARTZ_PILLAR(16452, Orientable.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	QUARTZ_STAIRS(24079, Stairs.class),
	/**
	 * BlockData: {@link RedstoneRail}
	 */
	ACTIVATOR_RAIL(5834, RedstoneRail.class),
	/**
	 * BlockData: {@link Dispenser}
	 */
	DROPPER(31273, Dispenser.class),
	WHITE_TERRACOTTA(20975),
	ORANGE_TERRACOTTA(18684),
	MAGENTA_TERRACOTTA(25900),
	LIGHT_BLUE_TERRACOTTA(31779),
	YELLOW_TERRACOTTA(32129),
	LIME_TERRACOTTA(24013),
	PINK_TERRACOTTA(23727),
	GRAY_TERRACOTTA(18004),
	LIGHT_GRAY_TERRACOTTA(26388),
	CYAN_TERRACOTTA(25940),
	PURPLE_TERRACOTTA(10387),
	BLUE_TERRACOTTA(5236),
	BROWN_TERRACOTTA(23664),
	GREEN_TERRACOTTA(4105),
	RED_TERRACOTTA(5086),
	BLACK_TERRACOTTA(26691),
	BARRIER(26453),
	/**
	 * BlockData: {@link TrapDoor}
	 */
	IRON_TRAPDOOR(17095, TrapDoor.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	HAY_BLOCK(17461, Orientable.class),
	WHITE_CARPET(15117),
	ORANGE_CARPET(24752),
	MAGENTA_CARPET(6180),
	LIGHT_BLUE_CARPET(21194),
	YELLOW_CARPET(18149),
	LIME_CARPET(15443),
	PINK_CARPET(27381),
	GRAY_CARPET(26991),
	LIGHT_GRAY_CARPET(11317),
	CYAN_CARPET(9742),
	PURPLE_CARPET(5574),
	BLUE_CARPET(13292),
	BROWN_CARPET(23352),
	GREEN_CARPET(7780),
	RED_CARPET(5424),
	BLACK_CARPET(6056),
	TERRACOTTA(16544),
	COAL_BLOCK(27968),
	PACKED_ICE(28993),
	/**
	 * BlockData: {@link Stairs}
	 */
	ACACIA_STAIRS(17453, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	DARK_OAK_STAIRS(22921, Stairs.class),
	SLIME_BLOCK(31892),
	GRASS_PATH(8604),
	/**
	 * BlockData: {@link Bisected}
	 */
	SUNFLOWER(7408, Bisected.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	LILAC(22837, Bisected.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	ROSE_BUSH(6080, Bisected.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	PEONY(21155, Bisected.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	TALL_GRASS(21559, Bisected.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	LARGE_FERN(30177, Bisected.class),
	WHITE_STAINED_GLASS(31190),
	ORANGE_STAINED_GLASS(25142),
	MAGENTA_STAINED_GLASS(26814),
	LIGHT_BLUE_STAINED_GLASS(17162),
	YELLOW_STAINED_GLASS(12182),
	LIME_STAINED_GLASS(24266),
	PINK_STAINED_GLASS(16164),
	GRAY_STAINED_GLASS(29979),
	LIGHT_GRAY_STAINED_GLASS(5843),
	CYAN_STAINED_GLASS(30604),
	PURPLE_STAINED_GLASS(21845),
	BLUE_STAINED_GLASS(7107),
	BROWN_STAINED_GLASS(20945),
	GREEN_STAINED_GLASS(22503),
	RED_STAINED_GLASS(9717),
	BLACK_STAINED_GLASS(13941),
	/**
	 * BlockData: {@link GlassPane}
	 */
	WHITE_STAINED_GLASS_PANE(10557, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	ORANGE_STAINED_GLASS_PANE(21089, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	MAGENTA_STAINED_GLASS_PANE(14082, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	LIGHT_BLUE_STAINED_GLASS_PANE(18721, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	YELLOW_STAINED_GLASS_PANE(20298, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	LIME_STAINED_GLASS_PANE(10610, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	PINK_STAINED_GLASS_PANE(24637, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	GRAY_STAINED_GLASS_PANE(25272, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	LIGHT_GRAY_STAINED_GLASS_PANE(19008, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	CYAN_STAINED_GLASS_PANE(11784, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	PURPLE_STAINED_GLASS_PANE(10948, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	BLUE_STAINED_GLASS_PANE(28484, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	BROWN_STAINED_GLASS_PANE(17557, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	GREEN_STAINED_GLASS_PANE(4767, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	RED_STAINED_GLASS_PANE(8630, GlassPane.class),
	/**
	 * BlockData: {@link GlassPane}
	 */
	BLACK_STAINED_GLASS_PANE(13201, GlassPane.class),
	PRISMARINE(7539),
	PRISMARINE_BRICKS(29118),
	DARK_PRISMARINE(19940),
	/**
	 * BlockData: {@link Stairs}
	 */
	PRISMARINE_STAIRS(19217, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	PRISMARINE_BRICK_STAIRS(15445, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	DARK_PRISMARINE_STAIRS(26511, Stairs.class),
	SEA_LANTERN(20780),
	RED_SANDSTONE(9092),
	CHISELED_RED_SANDSTONE(15529),
	CUT_RED_SANDSTONE(26842),
	/**
	 * BlockData: {@link Stairs}
	 */
	RED_SANDSTONE_STAIRS(25466, Stairs.class),
	/**
	 * BlockData: {@link CommandBlock}
	 */
	REPEATING_COMMAND_BLOCK(12405, CommandBlock.class),
	/**
	 * BlockData: {@link CommandBlock}
	 */
	CHAIN_COMMAND_BLOCK(26798, CommandBlock.class),
	MAGMA_BLOCK(25927),
	NETHER_WART_BLOCK(15486),
	WARPED_WART_BLOCK(15463),
	RED_NETHER_BRICKS(18056),
	/**
	 * BlockData: {@link Orientable}
	 */
	BONE_BLOCK(17312, Orientable.class),
	STRUCTURE_VOID(30806),
	/**
	 * BlockData: {@link Observer}
	 */
	OBSERVER(10726, Observer.class),
	/**
	 * BlockData: {@link Directional}
	 */
	SHULKER_BOX(7776, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	WHITE_SHULKER_BOX(31750, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ORANGE_SHULKER_BOX(21673, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	MAGENTA_SHULKER_BOX(21566, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_BLUE_SHULKER_BOX(18226, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	YELLOW_SHULKER_BOX(28700, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIME_SHULKER_BOX(28360, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PINK_SHULKER_BOX(24968, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GRAY_SHULKER_BOX(12754, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_GRAY_SHULKER_BOX(21345, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	CYAN_SHULKER_BOX(28123, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PURPLE_SHULKER_BOX(10373, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLUE_SHULKER_BOX(11476, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BROWN_SHULKER_BOX(24230, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GREEN_SHULKER_BOX(9377, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	RED_SHULKER_BOX(32448, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLACK_SHULKER_BOX(24076, 1, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	WHITE_GLAZED_TERRACOTTA(11326, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ORANGE_GLAZED_TERRACOTTA(27451, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	MAGENTA_GLAZED_TERRACOTTA(8067, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_BLUE_GLAZED_TERRACOTTA(4336, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	YELLOW_GLAZED_TERRACOTTA(10914, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIME_GLAZED_TERRACOTTA(13861, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PINK_GLAZED_TERRACOTTA(10260, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GRAY_GLAZED_TERRACOTTA(6256, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_GRAY_GLAZED_TERRACOTTA(10707, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	CYAN_GLAZED_TERRACOTTA(9550, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PURPLE_GLAZED_TERRACOTTA(4818, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLUE_GLAZED_TERRACOTTA(23823, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BROWN_GLAZED_TERRACOTTA(5655, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GREEN_GLAZED_TERRACOTTA(6958, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	RED_GLAZED_TERRACOTTA(24989, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLACK_GLAZED_TERRACOTTA(29678, Directional.class),
	WHITE_CONCRETE(6281),
	ORANGE_CONCRETE(19914),
	MAGENTA_CONCRETE(20591),
	LIGHT_BLUE_CONCRETE(29481),
	YELLOW_CONCRETE(15722),
	LIME_CONCRETE(5863),
	PINK_CONCRETE(5227),
	GRAY_CONCRETE(13959),
	LIGHT_GRAY_CONCRETE(14453),
	CYAN_CONCRETE(26522),
	PURPLE_CONCRETE(20623),
	BLUE_CONCRETE(18756),
	BROWN_CONCRETE(19006),
	GREEN_CONCRETE(17949),
	RED_CONCRETE(8032),
	BLACK_CONCRETE(13338),
	WHITE_CONCRETE_POWDER(10363),
	ORANGE_CONCRETE_POWDER(30159),
	MAGENTA_CONCRETE_POWDER(8272),
	LIGHT_BLUE_CONCRETE_POWDER(31206),
	YELLOW_CONCRETE_POWDER(10655),
	LIME_CONCRETE_POWDER(28859),
	PINK_CONCRETE_POWDER(6421),
	GRAY_CONCRETE_POWDER(13031),
	LIGHT_GRAY_CONCRETE_POWDER(21589),
	CYAN_CONCRETE_POWDER(15734),
	PURPLE_CONCRETE_POWDER(26808),
	BLUE_CONCRETE_POWDER(17773),
	BROWN_CONCRETE_POWDER(21485),
	GREEN_CONCRETE_POWDER(6904),
	RED_CONCRETE_POWDER(13286),
	BLACK_CONCRETE_POWDER(16150),
	/**
	 * BlockData: {@link TurtleEgg}
	 */
	TURTLE_EGG(32101, TurtleEgg.class),
	DEAD_TUBE_CORAL_BLOCK(28350),
	DEAD_BRAIN_CORAL_BLOCK(12979),
	DEAD_BUBBLE_CORAL_BLOCK(28220),
	DEAD_FIRE_CORAL_BLOCK(5307),
	DEAD_HORN_CORAL_BLOCK(15103),
	TUBE_CORAL_BLOCK(23723),
	BRAIN_CORAL_BLOCK(30618),
	BUBBLE_CORAL_BLOCK(15437),
	FIRE_CORAL_BLOCK(12119),
	HORN_CORAL_BLOCK(19958),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	TUBE_CORAL(23048, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	BRAIN_CORAL(31316, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	BUBBLE_CORAL(12464, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	FIRE_CORAL(29151, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	HORN_CORAL(19511, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_BRAIN_CORAL(9116, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_BUBBLE_CORAL(30583, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_FIRE_CORAL(8365, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_HORN_CORAL(5755, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_TUBE_CORAL(18028, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	TUBE_CORAL_FAN(19929, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	BRAIN_CORAL_FAN(13849, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	BUBBLE_CORAL_FAN(10795, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	FIRE_CORAL_FAN(11112, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	HORN_CORAL_FAN(13610, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_TUBE_CORAL_FAN(17628, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_BRAIN_CORAL_FAN(26150, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_BUBBLE_CORAL_FAN(17322, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_FIRE_CORAL_FAN(27073, Waterlogged.class),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	DEAD_HORN_CORAL_FAN(11387, Waterlogged.class),
	BLUE_ICE(22449),
	/**
	 * BlockData: {@link Waterlogged}
	 */
	CONDUIT(5148, Waterlogged.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	POLISHED_GRANITE_STAIRS(29588, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	SMOOTH_RED_SANDSTONE_STAIRS(17561, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	MOSSY_STONE_BRICK_STAIRS(27578, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	POLISHED_DIORITE_STAIRS(4625, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	MOSSY_COBBLESTONE_STAIRS(29210, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	END_STONE_BRICK_STAIRS(28831, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	STONE_STAIRS(23784, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	SMOOTH_SANDSTONE_STAIRS(21183, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	SMOOTH_QUARTZ_STAIRS(19560, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	GRANITE_STAIRS(21840, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	ANDESITE_STAIRS(17747, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	RED_NETHER_BRICK_STAIRS(26374, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	POLISHED_ANDESITE_STAIRS(7573, Stairs.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	DIORITE_STAIRS(13134, Stairs.class),
	/**
	 * BlockData: {@link Slab}
	 */
	POLISHED_GRANITE_SLAB(4521, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SMOOTH_RED_SANDSTONE_SLAB(16304, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	MOSSY_STONE_BRICK_SLAB(14002, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	POLISHED_DIORITE_SLAB(18303, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	MOSSY_COBBLESTONE_SLAB(12139, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	END_STONE_BRICK_SLAB(23239, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SMOOTH_SANDSTONE_SLAB(9030, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	SMOOTH_QUARTZ_SLAB(26543, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	GRANITE_SLAB(10901, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	ANDESITE_SLAB(32124, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	RED_NETHER_BRICK_SLAB(12462, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	POLISHED_ANDESITE_SLAB(24573, Slab.class),
	/**
	 * BlockData: {@link Slab}
	 */
	DIORITE_SLAB(10715, Slab.class),
	/**
	 * BlockData: {@link Scaffolding}
	 */
	SCAFFOLDING(15757, Scaffolding.class),
	/**
	 * BlockData: {@link Door}
	 */
	IRON_DOOR(4788, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	OAK_DOOR(20341, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	SPRUCE_DOOR(10642, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	BIRCH_DOOR(14759, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	JUNGLE_DOOR(28163, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	ACACIA_DOOR(23797, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	DARK_OAK_DOOR(10669, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	CRIMSON_DOOR(19544, Door.class),
	/**
	 * BlockData: {@link Door}
	 */
	WARPED_DOOR(15062, Door.class),
	/**
	 * BlockData: {@link Repeater}
	 */
	REPEATER(28823, Repeater.class),
	/**
	 * BlockData: {@link Comparator}
	 */
	COMPARATOR(18911, Comparator.class),
	/**
	 * BlockData: {@link StructureBlock}
	 */
	STRUCTURE_BLOCK(26831, StructureBlock.class),
	/**
	 * BlockData: {@link Jigsaw}
	 */
	JIGSAW(17398, Jigsaw.class),
	TURTLE_HELMET(30120, 1, 275),
	SCUTE(11914),
	FLINT_AND_STEEL(28620, 1, 64),
	APPLE(7720),
	BOW(8745, 1, 384),
	ARROW(31091),
	COAL(29067),
	CHARCOAL(5390),
	DIAMOND(20865),
	IRON_INGOT(24895),
	GOLD_INGOT(28927),
	NETHERITE_INGOT(32457),
	NETHERITE_SCRAP(29331),
	WOODEN_SWORD(7175, 1, 59),
	WOODEN_SHOVEL(28432, 1, 59),
	WOODEN_PICKAXE(12792, 1, 59),
	WOODEN_AXE(6292, 1, 59),
	WOODEN_HOE(16043, 1, 59),
	STONE_SWORD(25084, 1, 131),
	STONE_SHOVEL(9520, 1, 131),
	STONE_PICKAXE(14611, 1, 131),
	STONE_AXE(6338, 1, 131),
	STONE_HOE(22855, 1, 131),
	GOLDEN_SWORD(10505, 1, 32),
	GOLDEN_SHOVEL(15597, 1, 32),
	GOLDEN_PICKAXE(25898, 1, 32),
	GOLDEN_AXE(4878, 1, 32),
	GOLDEN_HOE(19337, 1, 32),
	IRON_SWORD(10904, 1, 250),
	IRON_SHOVEL(30045, 1, 250),
	IRON_PICKAXE(8842, 1, 250),
	IRON_AXE(15894, 1, 250),
	IRON_HOE(11339, 1, 250),
	DIAMOND_SWORD(27707, 1, 1561),
	DIAMOND_SHOVEL(25415, 1, 1561),
	DIAMOND_PICKAXE(24291, 1, 1561),
	DIAMOND_AXE(27277, 1, 1561),
	DIAMOND_HOE(24050, 1, 1561),
	NETHERITE_SWORD(23871, 1, 2031),
	NETHERITE_SHOVEL(29728, 1, 2031),
	NETHERITE_PICKAXE(9930, 1, 2031),
	NETHERITE_AXE(29533, 1, 2031),
	NETHERITE_HOE(27385, 1, 2031),
	STICK(9773),
	BOWL(32661),
	MUSHROOM_STEW(16336, 1),
	STRING(12806),
	FEATHER(30548),
	GUNPOWDER(29974),
	WHEAT_SEEDS(28742),
	/**
	 * BlockData: {@link Ageable}
	 */
	WHEAT(27709, Ageable.class),
	BREAD(32049),
	LEATHER_HELMET(11624, 1, 55),
	LEATHER_CHESTPLATE(29275, 1, 80),
	LEATHER_LEGGINGS(28210, 1, 75),
	LEATHER_BOOTS(15282, 1, 65),
	CHAINMAIL_HELMET(26114, 1, 165),
	CHAINMAIL_CHESTPLATE(23602, 1, 240),
	CHAINMAIL_LEGGINGS(19087, 1, 225),
	CHAINMAIL_BOOTS(17953, 1, 195),
	IRON_HELMET(12025, 1, 165),
	IRON_CHESTPLATE(28112, 1, 240),
	IRON_LEGGINGS(18951, 1, 225),
	IRON_BOOTS(8531, 1, 195),
	DIAMOND_HELMET(10755, 1, 363),
	DIAMOND_CHESTPLATE(32099, 1, 528),
	DIAMOND_LEGGINGS(11202, 1, 495),
	DIAMOND_BOOTS(16522, 1, 429),
	GOLDEN_HELMET(7945, 1, 77),
	GOLDEN_CHESTPLATE(4507, 1, 112),
	GOLDEN_LEGGINGS(21002, 1, 105),
	GOLDEN_BOOTS(7859, 1, 91),
	NETHERITE_HELMET(15907, 1, 407),
	NETHERITE_CHESTPLATE(6106, 1, 592),
	NETHERITE_LEGGINGS(25605, 1, 555),
	NETHERITE_BOOTS(8923, 1, 481),
	FLINT(23596),
	PORKCHOP(30896),
	COOKED_PORKCHOP(27231),
	PAINTING(23945),
	GOLDEN_APPLE(27732),
	ENCHANTED_GOLDEN_APPLE(8280),
	/**
	 * BlockData: {@link Sign}
	 */
	OAK_SIGN(8192, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	SPRUCE_SIGN(21502, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	BIRCH_SIGN(11351, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	JUNGLE_SIGN(24717, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	ACACIA_SIGN(29808, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	DARK_OAK_SIGN(15127, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	CRIMSON_SIGN(12162, 16, Sign.class),
	/**
	 * BlockData: {@link Sign}
	 */
	WARPED_SIGN(10407, 16, Sign.class),
	BUCKET(15215, 16),
	WATER_BUCKET(8802, 1),
	LAVA_BUCKET(9228, 1),
	MINECART(14352, 1),
	SADDLE(30206, 1),
	REDSTONE(11233),
	SNOWBALL(19487, 16),
	OAK_BOAT(17570, 1),
	LEATHER(16414),
	MILK_BUCKET(9680, 1),
	PUFFERFISH_BUCKET(8861, 1),
	SALMON_BUCKET(31427, 1),
	COD_BUCKET(28601, 1),
	TROPICAL_FISH_BUCKET(29995, 1),
	BRICK(6820),
	CLAY_BALL(24603),
	DRIED_KELP_BLOCK(12966),
	PAPER(9923),
	BOOK(23097),
	SLIME_BALL(5242),
	CHEST_MINECART(4497, 1),
	FURNACE_MINECART(14196, 1),
	EGG(21603, 16),
	COMPASS(24139),
	FISHING_ROD(4167, 1, 64),
	CLOCK(14980),
	GLOWSTONE_DUST(6665),
	COD(24691),
	SALMON(18516),
	TROPICAL_FISH(24879),
	PUFFERFISH(8115),
	COOKED_COD(9681),
	COOKED_SALMON(5615),
	INK_SAC(7184),
	COCOA_BEANS(30186),
	LAPIS_LAZULI(11075),
	WHITE_DYE(10758),
	ORANGE_DYE(13866),
	MAGENTA_DYE(11788),
	LIGHT_BLUE_DYE(28738),
	YELLOW_DYE(5952),
	LIME_DYE(6147),
	PINK_DYE(31151),
	GRAY_DYE(9184),
	LIGHT_GRAY_DYE(27643),
	CYAN_DYE(8043),
	PURPLE_DYE(6347),
	BLUE_DYE(11588),
	BROWN_DYE(7648),
	GREEN_DYE(23215),
	RED_DYE(5728),
	BLACK_DYE(6202),
	BONE_MEAL(32458),
	BONE(5686),
	SUGAR(30638),
	/**
	 * BlockData: {@link Cake}
	 */
	CAKE(27048, 1, Cake.class),
	/**
	 * BlockData: {@link Bed}
	 */
	WHITE_BED(8185, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	ORANGE_BED(11194, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	MAGENTA_BED(20061, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	LIGHT_BLUE_BED(20957, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	YELLOW_BED(30410, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	LIME_BED(27860, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	PINK_BED(13795, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	GRAY_BED(15745, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	LIGHT_GRAY_BED(5090, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	CYAN_BED(16746, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	PURPLE_BED(29755, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	BLUE_BED(12714, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	BROWN_BED(26672, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	GREEN_BED(13797, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	RED_BED(30910, 1, Bed.class),
	/**
	 * BlockData: {@link Bed}
	 */
	BLACK_BED(20490, 1, Bed.class),
	COOKIE(27431),
	FILLED_MAP(23504),
	SHEARS(27971, 1, 238),
	MELON_SLICE(5347),
	DRIED_KELP(21042),
	PUMPKIN_SEEDS(28985),
	MELON_SEEDS(18340),
	BEEF(4803),
	COOKED_BEEF(21595),
	CHICKEN(17281),
	COOKED_CHICKEN(16984),
	ROTTEN_FLESH(21591),
	ENDER_PEARL(5259, 16),
	BLAZE_ROD(8289),
	GHAST_TEAR(18222),
	GOLD_NUGGET(28814),
	/**
	 * BlockData: {@link Ageable}
	 */
	NETHER_WART(29227, Ageable.class),
	POTION(24020, 1),
	GLASS_BOTTLE(6116),
	SPIDER_EYE(9318),
	FERMENTED_SPIDER_EYE(19386),
	BLAZE_POWDER(18941),
	MAGMA_CREAM(25097),
	/**
	 * BlockData: {@link BrewingStand}
	 */
	BREWING_STAND(14539, BrewingStand.class),
	/**
	 * BlockData: {@link Levelled}
	 */
	CAULDRON(26531, Levelled.class),
	ENDER_EYE(24860),
	GLISTERING_MELON_SLICE(20158),
	BAT_SPAWN_EGG(14607),
	BEE_SPAWN_EGG(22924),
	BLAZE_SPAWN_EGG(4759),
	CAT_SPAWN_EGG(29583),
	CAVE_SPIDER_SPAWN_EGG(23341),
	CHICKEN_SPAWN_EGG(5462),
	COD_SPAWN_EGG(27248),
	COW_SPAWN_EGG(14761),
	CREEPER_SPAWN_EGG(9653),
	DOLPHIN_SPAWN_EGG(20787),
	DONKEY_SPAWN_EGG(14513),
	DROWNED_SPAWN_EGG(19368),
	ELDER_GUARDIAN_SPAWN_EGG(11418),
	ENDERMAN_SPAWN_EGG(29488),
	ENDERMITE_SPAWN_EGG(16617),
	EVOKER_SPAWN_EGG(21271),
	FOX_SPAWN_EGG(22376),
	GHAST_SPAWN_EGG(9970),
	GUARDIAN_SPAWN_EGG(20113),
	HOGLIN_SPAWN_EGG(14088),
	HORSE_SPAWN_EGG(25981),
	HUSK_SPAWN_EGG(20178),
	LLAMA_SPAWN_EGG(23640),
	MAGMA_CUBE_SPAWN_EGG(26638),
	MOOSHROOM_SPAWN_EGG(22125),
	MULE_SPAWN_EGG(11229),
	OCELOT_SPAWN_EGG(30080),
	PANDA_SPAWN_EGG(23759),
	PARROT_SPAWN_EGG(23614),
	PHANTOM_SPAWN_EGG(24648),
	PIG_SPAWN_EGG(22584),
	PIGLIN_SPAWN_EGG(16193),
	PIGLIN_BRUTE_SPAWN_EGG(30230),
	PILLAGER_SPAWN_EGG(28659),
	POLAR_BEAR_SPAWN_EGG(17015),
	PUFFERFISH_SPAWN_EGG(24570),
	RABBIT_SPAWN_EGG(26496),
	RAVAGER_SPAWN_EGG(8726),
	SALMON_SPAWN_EGG(18739),
	SHEEP_SPAWN_EGG(24488),
	SHULKER_SPAWN_EGG(31848),
	SILVERFISH_SPAWN_EGG(14537),
	SKELETON_SPAWN_EGG(15261),
	SKELETON_HORSE_SPAWN_EGG(21356),
	SLIME_SPAWN_EGG(17196),
	SPIDER_SPAWN_EGG(14984),
	SQUID_SPAWN_EGG(10682),
	STRAY_SPAWN_EGG(30153),
	STRIDER_SPAWN_EGG(6203),
	TRADER_LLAMA_SPAWN_EGG(8439),
	TROPICAL_FISH_SPAWN_EGG(19713),
	TURTLE_SPAWN_EGG(17324),
	VEX_SPAWN_EGG(27751),
	VILLAGER_SPAWN_EGG(30348),
	VINDICATOR_SPAWN_EGG(25324),
	WANDERING_TRADER_SPAWN_EGG(17904),
	WITCH_SPAWN_EGG(11837),
	WITHER_SKELETON_SPAWN_EGG(10073),
	WOLF_SPAWN_EGG(21692),
	ZOGLIN_SPAWN_EGG(7442),
	ZOMBIE_SPAWN_EGG(5814),
	ZOMBIE_HORSE_SPAWN_EGG(4275),
	ZOMBIE_VILLAGER_SPAWN_EGG(10311),
	ZOMBIFIED_PIGLIN_SPAWN_EGG(6626),
	EXPERIENCE_BOTTLE(12858),
	FIRE_CHARGE(4842),
	WRITABLE_BOOK(13393, 1),
	WRITTEN_BOOK(24164, 16),
	EMERALD(5654),
	ITEM_FRAME(27318),
	FLOWER_POT(30567),
	CARROT(22824),
	POTATO(21088),
	BAKED_POTATO(14624),
	POISONOUS_POTATO(32640),
	MAP(21655),
	GOLDEN_CARROT(5300),
	/**
	 * BlockData: {@link Rotatable}
	 */
	SKELETON_SKULL(13270, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	WITHER_SKELETON_SKULL(31487, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	PLAYER_HEAD(21174, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	ZOMBIE_HEAD(9304, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	CREEPER_HEAD(29146, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	DRAGON_HEAD(20084, Rotatable.class),
	CARROT_ON_A_STICK(27809, 1, 25),
	WARPED_FUNGUS_ON_A_STICK(11706, 1, 100),
	NETHER_STAR(12469),
	PUMPKIN_PIE(28725),
	FIREWORK_ROCKET(23841),
	FIREWORK_STAR(12190),
	ENCHANTED_BOOK(11741, 1),
	NETHER_BRICK(19996),
	QUARTZ(23608),
	TNT_MINECART(4277, 1),
	HOPPER_MINECART(19024, 1),
	PRISMARINE_SHARD(10993),
	PRISMARINE_CRYSTALS(31546),
	RABBIT(23068),
	COOKED_RABBIT(4454),
	RABBIT_STEW(10611, 1),
	RABBIT_FOOT(13864),
	RABBIT_HIDE(12467),
	ARMOR_STAND(12852, 16),
	IRON_HORSE_ARMOR(30108, 1),
	GOLDEN_HORSE_ARMOR(7996, 1),
	DIAMOND_HORSE_ARMOR(10321, 1),
	LEATHER_HORSE_ARMOR(30667, 1),
	LEAD(29539),
	NAME_TAG(30731),
	COMMAND_BLOCK_MINECART(7992, 1),
	MUTTON(4792),
	COOKED_MUTTON(31447),
	/**
	 * BlockData: {@link Rotatable}
	 */
	WHITE_BANNER(17562, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	ORANGE_BANNER(4839, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	MAGENTA_BANNER(15591, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	LIGHT_BLUE_BANNER(18060, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	YELLOW_BANNER(30382, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	LIME_BANNER(18887, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	PINK_BANNER(19439, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	GRAY_BANNER(12053, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	LIGHT_GRAY_BANNER(11417, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	CYAN_BANNER(9839, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	PURPLE_BANNER(29027, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	BLUE_BANNER(18481, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	BROWN_BANNER(11481, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	GREEN_BANNER(10698, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	RED_BANNER(26961, 16, Rotatable.class),
	/**
	 * BlockData: {@link Rotatable}
	 */
	BLACK_BANNER(9365, 16, Rotatable.class),
	END_CRYSTAL(19090),
	CHORUS_FRUIT(7652),
	POPPED_CHORUS_FRUIT(27844),
	BEETROOT(23305),
	BEETROOT_SEEDS(21282),
	BEETROOT_SOUP(16036, 1),
	DRAGON_BREATH(20154),
	SPLASH_POTION(30248, 1),
	SPECTRAL_ARROW(4568),
	TIPPED_ARROW(25164),
	LINGERING_POTION(25857, 1),
	SHIELD(29943, 1, 336),
	ELYTRA(23829, 1, 432),
	SPRUCE_BOAT(9606, 1),
	BIRCH_BOAT(28104, 1),
	JUNGLE_BOAT(4495, 1),
	ACACIA_BOAT(27326, 1),
	DARK_OAK_BOAT(28618, 1),
	TOTEM_OF_UNDYING(10139, 1),
	SHULKER_SHELL(27848),
	IRON_NUGGET(13715),
	KNOWLEDGE_BOOK(12646, 1),
	DEBUG_STICK(24562, 1),
	MUSIC_DISC_13(16359, 1),
	MUSIC_DISC_CAT(16246, 1),
	MUSIC_DISC_BLOCKS(26667, 1),
	MUSIC_DISC_CHIRP(19436, 1),
	MUSIC_DISC_FAR(13823, 1),
	MUSIC_DISC_MALL(11517, 1),
	MUSIC_DISC_MELLOHI(26117, 1),
	MUSIC_DISC_STAL(14989, 1),
	MUSIC_DISC_STRAD(16785, 1),
	MUSIC_DISC_WARD(24026, 1),
	MUSIC_DISC_11(27426, 1),
	MUSIC_DISC_WAIT(26499, 1),
	MUSIC_DISC_PIGSTEP(21323, 1),
	TRIDENT(7534, 1, 250),
	PHANTOM_MEMBRANE(18398),
	NAUTILUS_SHELL(19989),
	HEART_OF_THE_SEA(11807),
	CROSSBOW(4340, 1, 326),
	SUSPICIOUS_STEW(8173, 1),
	/**
	 * BlockData: {@link Directional}
	 */
	LOOM(14276, Directional.class),
	FLOWER_BANNER_PATTERN(5762, 1),
	CREEPER_BANNER_PATTERN(15774, 1),
	SKULL_BANNER_PATTERN(7680, 1),
	MOJANG_BANNER_PATTERN(11903, 1),
	GLOBE_BANNER_PATTERN(27753, 1),
	PIGLIN_BANNER_PATTERN(22028, 1),
	/**
	 * BlockData: {@link Levelled}
	 */
	COMPOSTER(31247, Levelled.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BARREL(22396, Directional.class),
	/**
	 * BlockData: {@link Furnace}
	 */
	SMOKER(24781, Furnace.class),
	/**
	 * BlockData: {@link Furnace}
	 */
	BLAST_FURNACE(31157, Furnace.class),
	CARTOGRAPHY_TABLE(28529),
	FLETCHING_TABLE(30838),
	/**
	 * BlockData: {@link Grindstone}
	 */
	GRINDSTONE(26260, Grindstone.class),
	/**
	 * BlockData: {@link Lectern}
	 */
	LECTERN(23490, Lectern.class),
	SMITHING_TABLE(9082),
	/**
	 * BlockData: {@link Directional}
	 */
	STONECUTTER(25170, Directional.class),
	/**
	 * BlockData: {@link Bell}
	 */
	BELL(20000, Bell.class),
	/**
	 * BlockData: {@link Lantern}
	 */
	LANTERN(5992, Lantern.class),
	/**
	 * BlockData: {@link Lantern}
	 */
	SOUL_LANTERN(27778, Lantern.class),
	SWEET_BERRIES(19747),
	/**
	 * BlockData: {@link Campfire}
	 */
	CAMPFIRE(8488, Campfire.class),
	/**
	 * BlockData: {@link Campfire}
	 */
	SOUL_CAMPFIRE(4238, Campfire.class),
	SHROOMLIGHT(20424),
	HONEYCOMB(9482),
	/**
	 * BlockData: {@link Beehive}
	 */
	BEE_NEST(8825, Beehive.class),
	/**
	 * BlockData: {@link Beehive}
	 */
	BEEHIVE(11830, Beehive.class),
	HONEY_BOTTLE(22927, 16),
	HONEY_BLOCK(30615),
	HONEYCOMB_BLOCK(28780),
	LODESTONE(23127),
	NETHERITE_BLOCK(6527),
	ANCIENT_DEBRIS(18198),
	/**
	 * BlockData: {@link AnaloguePowerable}
	 */
	TARGET(22637, AnaloguePowerable.class),
	CRYING_OBSIDIAN(31545),
	BLACKSTONE(7354),
	/**
	 * BlockData: {@link Slab}
	 */
	BLACKSTONE_SLAB(11948, Slab.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	BLACKSTONE_STAIRS(14646, Stairs.class),
	GILDED_BLACKSTONE(8498),
	POLISHED_BLACKSTONE(18144),
	/**
	 * BlockData: {@link Slab}
	 */
	POLISHED_BLACKSTONE_SLAB(23430, Slab.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	POLISHED_BLACKSTONE_STAIRS(8653, Stairs.class),
	CHISELED_POLISHED_BLACKSTONE(21942),
	POLISHED_BLACKSTONE_BRICKS(19844),
	/**
	 * BlockData: {@link Slab}
	 */
	POLISHED_BLACKSTONE_BRICK_SLAB(12219, Slab.class),
	/**
	 * BlockData: {@link Stairs}
	 */
	POLISHED_BLACKSTONE_BRICK_STAIRS(17983, Stairs.class),
	CRACKED_POLISHED_BLACKSTONE_BRICKS(16846),
	/**
	 * BlockData: {@link RespawnAnchor}
	 */
	RESPAWN_ANCHOR(4099, RespawnAnchor.class),
	/**
	 * BlockData: {@link Levelled}
	 */
	WATER(24998, Levelled.class),
	/**
	 * BlockData: {@link Levelled}
	 */
	LAVA(8415, Levelled.class),
	/**
	 * BlockData: {@link Bisected}
	 */
	TALL_SEAGRASS(27189, Bisected.class),
	/**
	 * BlockData: {@link PistonHead}
	 */
	PISTON_HEAD(30226, PistonHead.class),
	/**
	 * BlockData: {@link TechnicalPiston}
	 */
	MOVING_PISTON(13831, TechnicalPiston.class),
	/**
	 * BlockData: {@link Directional}
	 */
	WALL_TORCH(25890, Directional.class),
	/**
	 * BlockData: {@link Fire}
	 */
	FIRE(16396, Fire.class),
	/**
	 * BlockData: {@link Fire}
	 */
	SOUL_FIRE(30163, Fire.class),
	/**
	 * BlockData: {@link RedstoneWire}
	 */
	REDSTONE_WIRE(25984, RedstoneWire.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	OAK_WALL_SIGN(12984, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	SPRUCE_WALL_SIGN(7352, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	BIRCH_WALL_SIGN(9887, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	ACACIA_WALL_SIGN(20316, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	JUNGLE_WALL_SIGN(29629, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	DARK_OAK_WALL_SIGN(9508, 16, WallSign.class),
	/**
	 * BlockData: {@link RedstoneWallTorch}
	 */
	REDSTONE_WALL_TORCH(7595, RedstoneWallTorch.class),
	/**
	 * BlockData: {@link Directional}
	 */
	SOUL_WALL_TORCH(27500, Directional.class),
	/**
	 * BlockData: {@link Orientable}
	 */
	NETHER_PORTAL(19469, Orientable.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ATTACHED_PUMPKIN_STEM(12724, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ATTACHED_MELON_STEM(30882, Directional.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	PUMPKIN_STEM(19021, Ageable.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	MELON_STEM(8247, Ageable.class),
	END_PORTAL(16782),
	/**
	 * BlockData: {@link Cocoa}
	 */
	COCOA(29709, Cocoa.class),
	/**
	 * BlockData: {@link Tripwire}
	 */
	TRIPWIRE(8810, Tripwire.class),
	POTTED_OAK_SAPLING(11905),
	POTTED_SPRUCE_SAPLING(29498),
	POTTED_BIRCH_SAPLING(32484),
	POTTED_JUNGLE_SAPLING(7525),
	POTTED_ACACIA_SAPLING(14096),
	POTTED_DARK_OAK_SAPLING(6486),
	POTTED_FERN(23315),
	POTTED_DANDELION(9727),
	POTTED_POPPY(7457),
	POTTED_BLUE_ORCHID(6599),
	POTTED_ALLIUM(13184),
	POTTED_AZURE_BLUET(8754),
	POTTED_RED_TULIP(28594),
	POTTED_ORANGE_TULIP(28807),
	POTTED_WHITE_TULIP(24330),
	POTTED_PINK_TULIP(10089),
	POTTED_OXEYE_DAISY(19707),
	POTTED_CORNFLOWER(28917),
	POTTED_LILY_OF_THE_VALLEY(9364),
	POTTED_WITHER_ROSE(26876),
	POTTED_RED_MUSHROOM(22881),
	POTTED_BROWN_MUSHROOM(14481),
	POTTED_DEAD_BUSH(13020),
	POTTED_CACTUS(8777),
	/**
	 * BlockData: {@link Ageable}
	 */
	CARROTS(17258, Ageable.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	POTATOES(10879, Ageable.class),
	/**
	 * BlockData: {@link Directional}
	 */
	SKELETON_WALL_SKULL(31650, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	WITHER_SKELETON_WALL_SKULL(9326, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ZOMBIE_WALL_HEAD(16296, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PLAYER_WALL_HEAD(13164, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	CREEPER_WALL_HEAD(30123, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	DRAGON_WALL_HEAD(19818, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	WHITE_WALL_BANNER(15967, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	ORANGE_WALL_BANNER(9936, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	MAGENTA_WALL_BANNER(23291, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_BLUE_WALL_BANNER(12011, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	YELLOW_WALL_BANNER(32004, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIME_WALL_BANNER(21422, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PINK_WALL_BANNER(9421, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GRAY_WALL_BANNER(24275, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	LIGHT_GRAY_WALL_BANNER(31088, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	CYAN_WALL_BANNER(10889, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	PURPLE_WALL_BANNER(14298, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLUE_WALL_BANNER(17757, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BROWN_WALL_BANNER(14731, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	GREEN_WALL_BANNER(15046, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	RED_WALL_BANNER(4378, Directional.class),
	/**
	 * BlockData: {@link Directional}
	 */
	BLACK_WALL_BANNER(4919, Directional.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	BEETROOTS(22075, Ageable.class),
	END_GATEWAY(26605),
	/**
	 * BlockData: {@link Ageable}
	 */
	FROSTED_ICE(21814, Ageable.class),
	KELP_PLANT(29697),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	DEAD_TUBE_CORAL_WALL_FAN(5128, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	DEAD_BRAIN_CORAL_WALL_FAN(23718, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	DEAD_BUBBLE_CORAL_WALL_FAN(18453, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	DEAD_FIRE_CORAL_WALL_FAN(23375, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	DEAD_HORN_CORAL_WALL_FAN(27550, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	TUBE_CORAL_WALL_FAN(25282, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	BRAIN_CORAL_WALL_FAN(22685, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	BUBBLE_CORAL_WALL_FAN(20382, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	FIRE_CORAL_WALL_FAN(20100, CoralWallFan.class),
	/**
	 * BlockData: {@link CoralWallFan}
	 */
	HORN_CORAL_WALL_FAN(28883, CoralWallFan.class),
	BAMBOO_SAPLING(8478),
	POTTED_BAMBOO(22542),
	VOID_AIR(13668),
	CAVE_AIR(17422),
	/**
	 * BlockData: {@link BubbleColumn}
	 */
	BUBBLE_COLUMN(31612, BubbleColumn.class),
	/**
	 * BlockData: {@link Ageable}
	 */
	SWEET_BERRY_BUSH(11958, Ageable.class),
	WEEPING_VINES_PLANT(19437),
	TWISTING_VINES_PLANT(25338),
	/**
	 * BlockData: {@link WallSign}
	 */
	CRIMSON_WALL_SIGN(19242, 16, WallSign.class),
	/**
	 * BlockData: {@link WallSign}
	 */
	WARPED_WALL_SIGN(13534, 16, WallSign.class),
	POTTED_CRIMSON_FUNGUS(5548),
	POTTED_WARPED_FUNGUS(30800),
	POTTED_CRIMSON_ROOTS(13852),
	POTTED_WARPED_ROOTS(6403),
	// ----- Legacy Separator -----
	@Deprecated
	LEGACY_AIR(0, 0),
	@Deprecated
	LEGACY_STONE(1),
	@Deprecated
	LEGACY_GRASS(2),
	@Deprecated
	LEGACY_DIRT(3),
	@Deprecated
	LEGACY_COBBLESTONE(4),
	@Deprecated
	LEGACY_WOOD(5, org.bukkit.material.Wood.class),
	@Deprecated
	LEGACY_SAPLING(6, org.bukkit.material.Sapling.class),
	@Deprecated
	LEGACY_BEDROCK(7),
	@Deprecated
	LEGACY_WATER(8, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_STATIONARY_WATER(9, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_LAVA(10, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_STATIONARY_LAVA(11, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_SAND(12),
	@Deprecated
	LEGACY_GRAVEL(13),
	@Deprecated
	LEGACY_GOLD_ORE(14),
	@Deprecated
	LEGACY_IRON_ORE(15),
	@Deprecated
	LEGACY_COAL_ORE(16),
	@Deprecated
	LEGACY_LOG(17, org.bukkit.material.Tree.class),
	@Deprecated
	LEGACY_LEAVES(18, org.bukkit.material.Leaves.class),
	@Deprecated
	LEGACY_SPONGE(19),
	@Deprecated
	LEGACY_GLASS(20),
	@Deprecated
	LEGACY_LAPIS_ORE(21),
	@Deprecated
	LEGACY_LAPIS_BLOCK(22),
	@Deprecated
	LEGACY_DISPENSER(23, org.bukkit.material.Dispenser.class),
	@Deprecated
	LEGACY_SANDSTONE(24, org.bukkit.material.Sandstone.class),
	@Deprecated
	LEGACY_NOTE_BLOCK(25),
	@Deprecated
	LEGACY_BED_BLOCK(26, org.bukkit.material.Bed.class),
	@Deprecated
	LEGACY_POWERED_RAIL(27, org.bukkit.material.PoweredRail.class),
	@Deprecated
	LEGACY_DETECTOR_RAIL(28, org.bukkit.material.DetectorRail.class),
	@Deprecated
	LEGACY_PISTON_STICKY_BASE(29, org.bukkit.material.PistonBaseMaterial.class),
	@Deprecated
	LEGACY_WEB(30),
	@Deprecated
	LEGACY_LONG_GRASS(31, org.bukkit.material.LongGrass.class),
	@Deprecated
	LEGACY_DEAD_BUSH(32),
	@Deprecated
	LEGACY_PISTON_BASE(33, org.bukkit.material.PistonBaseMaterial.class),
	@Deprecated
	LEGACY_PISTON_EXTENSION(34, org.bukkit.material.PistonExtensionMaterial.class),
	@Deprecated
	LEGACY_WOOL(35, org.bukkit.material.Wool.class),
	@Deprecated
	LEGACY_PISTON_MOVING_PIECE(36),
	@Deprecated
	LEGACY_YELLOW_FLOWER(37),
	@Deprecated
	LEGACY_RED_ROSE(38),
	@Deprecated
	LEGACY_BROWN_MUSHROOM(39),
	@Deprecated
	LEGACY_RED_MUSHROOM(40),
	@Deprecated
	LEGACY_GOLD_BLOCK(41),
	@Deprecated
	LEGACY_IRON_BLOCK(42),
	@Deprecated
	LEGACY_DOUBLE_STEP(43, org.bukkit.material.Step.class),
	@Deprecated
	LEGACY_STEP(44, org.bukkit.material.Step.class),
	@Deprecated
	LEGACY_BRICK(45),
	@Deprecated
	LEGACY_TNT(46),
	@Deprecated
	LEGACY_BOOKSHELF(47),
	@Deprecated
	LEGACY_MOSSY_COBBLESTONE(48),
	@Deprecated
	LEGACY_OBSIDIAN(49),
	@Deprecated
	LEGACY_TORCH(50, org.bukkit.material.Torch.class),
	@Deprecated
	LEGACY_FIRE(51),
	@Deprecated
	LEGACY_MOB_SPAWNER(52),
	@Deprecated
	LEGACY_WOOD_STAIRS(53, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_CHEST(54, org.bukkit.material.Chest.class),
	@Deprecated
	LEGACY_REDSTONE_WIRE(55, org.bukkit.material.RedstoneWire.class),
	@Deprecated
	LEGACY_DIAMOND_ORE(56),
	@Deprecated
	LEGACY_DIAMOND_BLOCK(57),
	@Deprecated
	LEGACY_WORKBENCH(58),
	@Deprecated
	LEGACY_CROPS(59, org.bukkit.material.Crops.class),
	@Deprecated
	LEGACY_SOIL(60, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_FURNACE(61, org.bukkit.material.Furnace.class),
	@Deprecated
	LEGACY_BURNING_FURNACE(62, org.bukkit.material.Furnace.class),
	@Deprecated
	LEGACY_SIGN_POST(63, 64, org.bukkit.material.Sign.class),
	@Deprecated
	LEGACY_WOODEN_DOOR(64, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_LADDER(65, org.bukkit.material.Ladder.class),
	@Deprecated
	LEGACY_RAILS(66, org.bukkit.material.Rails.class),
	@Deprecated
	LEGACY_COBBLESTONE_STAIRS(67, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_WALL_SIGN(68, 64, org.bukkit.material.Sign.class),
	@Deprecated
	LEGACY_LEVER(69, org.bukkit.material.Lever.class),
	@Deprecated
	LEGACY_STONE_PLATE(70, org.bukkit.material.PressurePlate.class),
	@Deprecated
	LEGACY_IRON_DOOR_BLOCK(71, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_WOOD_PLATE(72, org.bukkit.material.PressurePlate.class),
	@Deprecated
	LEGACY_REDSTONE_ORE(73),
	@Deprecated
	LEGACY_GLOWING_REDSTONE_ORE(74),
	@Deprecated
	LEGACY_REDSTONE_TORCH_OFF(75, org.bukkit.material.RedstoneTorch.class),
	@Deprecated
	LEGACY_REDSTONE_TORCH_ON(76, org.bukkit.material.RedstoneTorch.class),
	@Deprecated
	LEGACY_STONE_BUTTON(77, org.bukkit.material.Button.class),
	@Deprecated
	LEGACY_SNOW(78),
	@Deprecated
	LEGACY_ICE(79),
	@Deprecated
	LEGACY_SNOW_BLOCK(80),
	@Deprecated
	LEGACY_CACTUS(81, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_CLAY(82),
	@Deprecated
	LEGACY_SUGAR_CANE_BLOCK(83, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_JUKEBOX(84),
	@Deprecated
	LEGACY_FENCE(85),
	@Deprecated
	LEGACY_PUMPKIN(86, org.bukkit.material.Pumpkin.class),
	@Deprecated
	LEGACY_NETHERRACK(87),
	@Deprecated
	LEGACY_SOUL_SAND(88),
	@Deprecated
	LEGACY_GLOWSTONE(89),
	@Deprecated
	LEGACY_PORTAL(90),
	@Deprecated
	LEGACY_JACK_O_LANTERN(91, org.bukkit.material.Pumpkin.class),
	@Deprecated
	LEGACY_CAKE_BLOCK(92, 64, org.bukkit.material.Cake.class),
	@Deprecated
	LEGACY_DIODE_BLOCK_OFF(93, org.bukkit.material.Diode.class),
	@Deprecated
	LEGACY_DIODE_BLOCK_ON(94, org.bukkit.material.Diode.class),
	@Deprecated
	LEGACY_STAINED_GLASS(95),
	@Deprecated
	LEGACY_TRAP_DOOR(96, org.bukkit.material.TrapDoor.class),
	@Deprecated
	LEGACY_MONSTER_EGGS(97, org.bukkit.material.MonsterEggs.class),
	@Deprecated
	LEGACY_SMOOTH_BRICK(98, org.bukkit.material.SmoothBrick.class),
	@Deprecated
	LEGACY_HUGE_MUSHROOM_1(99, org.bukkit.material.Mushroom.class),
	@Deprecated
	LEGACY_HUGE_MUSHROOM_2(100, org.bukkit.material.Mushroom.class),
	@Deprecated
	LEGACY_IRON_FENCE(101),
	@Deprecated
	LEGACY_THIN_GLASS(102),
	@Deprecated
	LEGACY_MELON_BLOCK(103),
	@Deprecated
	LEGACY_PUMPKIN_STEM(104, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_MELON_STEM(105, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_VINE(106, org.bukkit.material.Vine.class),
	@Deprecated
	LEGACY_FENCE_GATE(107, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_BRICK_STAIRS(108, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_SMOOTH_STAIRS(109, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_MYCEL(110),
	@Deprecated
	LEGACY_WATER_LILY(111),
	@Deprecated
	LEGACY_NETHER_BRICK(112),
	@Deprecated
	LEGACY_NETHER_FENCE(113),
	@Deprecated
	LEGACY_NETHER_BRICK_STAIRS(114, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_NETHER_WARTS(115, org.bukkit.material.NetherWarts.class),
	@Deprecated
	LEGACY_ENCHANTMENT_TABLE(116),
	@Deprecated
	LEGACY_BREWING_STAND(117, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_CAULDRON(118, org.bukkit.material.Cauldron.class),
	@Deprecated
	LEGACY_ENDER_PORTAL(119),
	@Deprecated
	LEGACY_ENDER_PORTAL_FRAME(120),
	@Deprecated
	LEGACY_ENDER_STONE(121),
	@Deprecated
	LEGACY_DRAGON_EGG(122),
	@Deprecated
	LEGACY_REDSTONE_LAMP_OFF(123),
	@Deprecated
	LEGACY_REDSTONE_LAMP_ON(124),
	@Deprecated
	LEGACY_WOOD_DOUBLE_STEP(125, org.bukkit.material.Wood.class),
	@Deprecated
	LEGACY_WOOD_STEP(126, org.bukkit.material.WoodenStep.class),
	@Deprecated
	LEGACY_COCOA(127, org.bukkit.material.CocoaPlant.class),
	@Deprecated
	LEGACY_SANDSTONE_STAIRS(128, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_EMERALD_ORE(129),
	@Deprecated
	LEGACY_ENDER_CHEST(130, org.bukkit.material.EnderChest.class),
	@Deprecated
	LEGACY_TRIPWIRE_HOOK(131, org.bukkit.material.TripwireHook.class),
	@Deprecated
	LEGACY_TRIPWIRE(132, org.bukkit.material.Tripwire.class),
	@Deprecated
	LEGACY_EMERALD_BLOCK(133),
	@Deprecated
	LEGACY_SPRUCE_WOOD_STAIRS(134, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_BIRCH_WOOD_STAIRS(135, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_JUNGLE_WOOD_STAIRS(136, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_COMMAND(137, org.bukkit.material.Command.class),
	@Deprecated
	LEGACY_BEACON(138),
	@Deprecated
	LEGACY_COBBLE_WALL(139),
	@Deprecated
	LEGACY_FLOWER_POT(140, org.bukkit.material.FlowerPot.class),
	@Deprecated
	LEGACY_CARROT(141, org.bukkit.material.Crops.class),
	@Deprecated
	LEGACY_POTATO(142, org.bukkit.material.Crops.class),
	@Deprecated
	LEGACY_WOOD_BUTTON(143, org.bukkit.material.Button.class),
	@Deprecated
	LEGACY_SKULL(144, org.bukkit.material.Skull.class),
	@Deprecated
	LEGACY_ANVIL(145),
	@Deprecated
	LEGACY_TRAPPED_CHEST(146, org.bukkit.material.Chest.class),
	@Deprecated
	LEGACY_GOLD_PLATE(147),
	@Deprecated
	LEGACY_IRON_PLATE(148),
	@Deprecated
	LEGACY_REDSTONE_COMPARATOR_OFF(149, org.bukkit.material.Comparator.class),
	@Deprecated
	LEGACY_REDSTONE_COMPARATOR_ON(150, org.bukkit.material.Comparator.class),
	@Deprecated
	LEGACY_DAYLIGHT_DETECTOR(151),
	@Deprecated
	LEGACY_REDSTONE_BLOCK(152),
	@Deprecated
	LEGACY_QUARTZ_ORE(153),
	@Deprecated
	LEGACY_HOPPER(154, org.bukkit.material.Hopper.class),
	@Deprecated
	LEGACY_QUARTZ_BLOCK(155),
	@Deprecated
	LEGACY_QUARTZ_STAIRS(156, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_ACTIVATOR_RAIL(157, org.bukkit.material.PoweredRail.class),
	@Deprecated
	LEGACY_DROPPER(158, org.bukkit.material.Dispenser.class),
	@Deprecated
	LEGACY_STAINED_CLAY(159),
	@Deprecated
	LEGACY_STAINED_GLASS_PANE(160),
	@Deprecated
	LEGACY_LEAVES_2(161, org.bukkit.material.Leaves.class),
	@Deprecated
	LEGACY_LOG_2(162, org.bukkit.material.Tree.class),
	@Deprecated
	LEGACY_ACACIA_STAIRS(163, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_DARK_OAK_STAIRS(164, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_SLIME_BLOCK(165),
	@Deprecated
	LEGACY_BARRIER(166),
	@Deprecated
	LEGACY_IRON_TRAPDOOR(167, org.bukkit.material.TrapDoor.class),
	@Deprecated
	LEGACY_PRISMARINE(168),
	@Deprecated
	LEGACY_SEA_LANTERN(169),
	@Deprecated
	LEGACY_HAY_BLOCK(170),
	@Deprecated
	LEGACY_CARPET(171),
	@Deprecated
	LEGACY_HARD_CLAY(172),
	@Deprecated
	LEGACY_COAL_BLOCK(173),
	@Deprecated
	LEGACY_PACKED_ICE(174),
	@Deprecated
	LEGACY_DOUBLE_PLANT(175),
	@Deprecated
	LEGACY_STANDING_BANNER(176, org.bukkit.material.Banner.class),
	@Deprecated
	LEGACY_WALL_BANNER(177, org.bukkit.material.Banner.class),
	@Deprecated
	LEGACY_DAYLIGHT_DETECTOR_INVERTED(178),
	@Deprecated
	LEGACY_RED_SANDSTONE(179),
	@Deprecated
	LEGACY_RED_SANDSTONE_STAIRS(180, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_DOUBLE_STONE_SLAB2(181),
	@Deprecated
	LEGACY_STONE_SLAB2(182),
	@Deprecated
	LEGACY_SPRUCE_FENCE_GATE(183, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_BIRCH_FENCE_GATE(184, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_JUNGLE_FENCE_GATE(185, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_DARK_OAK_FENCE_GATE(186, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_ACACIA_FENCE_GATE(187, org.bukkit.material.Gate.class),
	@Deprecated
	LEGACY_SPRUCE_FENCE(188),
	@Deprecated
	LEGACY_BIRCH_FENCE(189),
	@Deprecated
	LEGACY_JUNGLE_FENCE(190),
	@Deprecated
	LEGACY_DARK_OAK_FENCE(191),
	@Deprecated
	LEGACY_ACACIA_FENCE(192),
	@Deprecated
	LEGACY_SPRUCE_DOOR(193, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_BIRCH_DOOR(194, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_JUNGLE_DOOR(195, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_ACACIA_DOOR(196, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_DARK_OAK_DOOR(197, org.bukkit.material.Door.class),
	@Deprecated
	LEGACY_END_ROD(198),
	@Deprecated
	LEGACY_CHORUS_PLANT(199),
	@Deprecated
	LEGACY_CHORUS_FLOWER(200),
	@Deprecated
	LEGACY_PURPUR_BLOCK(201),
	@Deprecated
	LEGACY_PURPUR_PILLAR(202),
	@Deprecated
	LEGACY_PURPUR_STAIRS(203, org.bukkit.material.Stairs.class),
	@Deprecated
	LEGACY_PURPUR_DOUBLE_SLAB(204),
	@Deprecated
	LEGACY_PURPUR_SLAB(205),
	@Deprecated
	LEGACY_END_BRICKS(206),
	@Deprecated
	LEGACY_BEETROOT_BLOCK(207, org.bukkit.material.Crops.class),
	@Deprecated
	LEGACY_GRASS_PATH(208),
	@Deprecated
	LEGACY_END_GATEWAY(209),
	@Deprecated
	LEGACY_COMMAND_REPEATING(210, org.bukkit.material.Command.class),
	@Deprecated
	LEGACY_COMMAND_CHAIN(211, org.bukkit.material.Command.class),
	@Deprecated
	LEGACY_FROSTED_ICE(212),
	@Deprecated
	LEGACY_MAGMA(213),
	@Deprecated
	LEGACY_NETHER_WART_BLOCK(214),
	@Deprecated
	LEGACY_RED_NETHER_BRICK(215),
	@Deprecated
	LEGACY_BONE_BLOCK(216),
	@Deprecated
	LEGACY_STRUCTURE_VOID(217),
	@Deprecated
	LEGACY_OBSERVER(218, org.bukkit.material.Observer.class),
	@Deprecated
	LEGACY_WHITE_SHULKER_BOX(219, 1),
	@Deprecated
	LEGACY_ORANGE_SHULKER_BOX(220, 1),
	@Deprecated
	LEGACY_MAGENTA_SHULKER_BOX(221, 1),
	@Deprecated
	LEGACY_LIGHT_BLUE_SHULKER_BOX(222, 1),
	@Deprecated
	LEGACY_YELLOW_SHULKER_BOX(223, 1),
	@Deprecated
	LEGACY_LIME_SHULKER_BOX(224, 1),
	@Deprecated
	LEGACY_PINK_SHULKER_BOX(225, 1),
	@Deprecated
	LEGACY_GRAY_SHULKER_BOX(226, 1),
	@Deprecated
	LEGACY_SILVER_SHULKER_BOX(227, 1),
	@Deprecated
	LEGACY_CYAN_SHULKER_BOX(228, 1),
	@Deprecated
	LEGACY_PURPLE_SHULKER_BOX(229, 1),
	@Deprecated
	LEGACY_BLUE_SHULKER_BOX(230, 1),
	@Deprecated
	LEGACY_BROWN_SHULKER_BOX(231, 1),
	@Deprecated
	LEGACY_GREEN_SHULKER_BOX(232, 1),
	@Deprecated
	LEGACY_RED_SHULKER_BOX(233, 1),
	@Deprecated
	LEGACY_BLACK_SHULKER_BOX(234, 1),
	@Deprecated
	LEGACY_WHITE_GLAZED_TERRACOTTA(235),
	@Deprecated
	LEGACY_ORANGE_GLAZED_TERRACOTTA(236),
	@Deprecated
	LEGACY_MAGENTA_GLAZED_TERRACOTTA(237),
	@Deprecated
	LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA(238),
	@Deprecated
	LEGACY_YELLOW_GLAZED_TERRACOTTA(239),
	@Deprecated
	LEGACY_LIME_GLAZED_TERRACOTTA(240),
	@Deprecated
	LEGACY_PINK_GLAZED_TERRACOTTA(241),
	@Deprecated
	LEGACY_GRAY_GLAZED_TERRACOTTA(242),
	@Deprecated
	LEGACY_SILVER_GLAZED_TERRACOTTA(243),
	@Deprecated
	LEGACY_CYAN_GLAZED_TERRACOTTA(244),
	@Deprecated
	LEGACY_PURPLE_GLAZED_TERRACOTTA(245),
	@Deprecated
	LEGACY_BLUE_GLAZED_TERRACOTTA(246),
	@Deprecated
	LEGACY_BROWN_GLAZED_TERRACOTTA(247),
	@Deprecated
	LEGACY_GREEN_GLAZED_TERRACOTTA(248),
	@Deprecated
	LEGACY_RED_GLAZED_TERRACOTTA(249),
	@Deprecated
	LEGACY_BLACK_GLAZED_TERRACOTTA(250),
	@Deprecated
	LEGACY_CONCRETE(251),
	@Deprecated
	LEGACY_CONCRETE_POWDER(252),
	@Deprecated
	LEGACY_STRUCTURE_BLOCK(255),
	// ----- Item Separator -----
	@Deprecated
	LEGACY_IRON_SPADE(256, 1, 250),
	@Deprecated
	LEGACY_IRON_PICKAXE(257, 1, 250),
	@Deprecated
	LEGACY_IRON_AXE(258, 1, 250),
	@Deprecated
	LEGACY_FLINT_AND_STEEL(259, 1, 64),
	@Deprecated
	LEGACY_APPLE(260),
	@Deprecated
	LEGACY_BOW(261, 1, 384),
	@Deprecated
	LEGACY_ARROW(262),
	@Deprecated
	LEGACY_COAL(263, org.bukkit.material.Coal.class),
	@Deprecated
	LEGACY_DIAMOND(264),
	@Deprecated
	LEGACY_IRON_INGOT(265),
	@Deprecated
	LEGACY_GOLD_INGOT(266),
	@Deprecated
	LEGACY_IRON_SWORD(267, 1, 250),
	@Deprecated
	LEGACY_WOOD_SWORD(268, 1, 59),
	@Deprecated
	LEGACY_WOOD_SPADE(269, 1, 59),
	@Deprecated
	LEGACY_WOOD_PICKAXE(270, 1, 59),
	@Deprecated
	LEGACY_WOOD_AXE(271, 1, 59),
	@Deprecated
	LEGACY_STONE_SWORD(272, 1, 131),
	@Deprecated
	LEGACY_STONE_SPADE(273, 1, 131),
	@Deprecated
	LEGACY_STONE_PICKAXE(274, 1, 131),
	@Deprecated
	LEGACY_STONE_AXE(275, 1, 131),
	@Deprecated
	LEGACY_DIAMOND_SWORD(276, 1, 1561),
	@Deprecated
	LEGACY_DIAMOND_SPADE(277, 1, 1561),
	@Deprecated
	LEGACY_DIAMOND_PICKAXE(278, 1, 1561),
	@Deprecated
	LEGACY_DIAMOND_AXE(279, 1, 1561),
	@Deprecated
	LEGACY_STICK(280),
	@Deprecated
	LEGACY_BOWL(281),
	@Deprecated
	LEGACY_MUSHROOM_SOUP(282, 1),
	@Deprecated
	LEGACY_GOLD_SWORD(283, 1, 32),
	@Deprecated
	LEGACY_GOLD_SPADE(284, 1, 32),
	@Deprecated
	LEGACY_GOLD_PICKAXE(285, 1, 32),
	@Deprecated
	LEGACY_GOLD_AXE(286, 1, 32),
	@Deprecated
	LEGACY_STRING(287),
	@Deprecated
	LEGACY_FEATHER(288),
	@Deprecated
	LEGACY_SULPHUR(289),
	@Deprecated
	LEGACY_WOOD_HOE(290, 1, 59),
	@Deprecated
	LEGACY_STONE_HOE(291, 1, 131),
	@Deprecated
	LEGACY_IRON_HOE(292, 1, 250),
	@Deprecated
	LEGACY_DIAMOND_HOE(293, 1, 1561),
	@Deprecated
	LEGACY_GOLD_HOE(294, 1, 32),
	@Deprecated
	LEGACY_SEEDS(295),
	@Deprecated
	LEGACY_WHEAT(296),
	@Deprecated
	LEGACY_BREAD(297),
	@Deprecated
	LEGACY_LEATHER_HELMET(298, 1, 55),
	@Deprecated
	LEGACY_LEATHER_CHESTPLATE(299, 1, 80),
	@Deprecated
	LEGACY_LEATHER_LEGGINGS(300, 1, 75),
	@Deprecated
	LEGACY_LEATHER_BOOTS(301, 1, 65),
	@Deprecated
	LEGACY_CHAINMAIL_HELMET(302, 1, 165),
	@Deprecated
	LEGACY_CHAINMAIL_CHESTPLATE(303, 1, 240),
	@Deprecated
	LEGACY_CHAINMAIL_LEGGINGS(304, 1, 225),
	@Deprecated
	LEGACY_CHAINMAIL_BOOTS(305, 1, 195),
	@Deprecated
	LEGACY_IRON_HELMET(306, 1, 165),
	@Deprecated
	LEGACY_IRON_CHESTPLATE(307, 1, 240),
	@Deprecated
	LEGACY_IRON_LEGGINGS(308, 1, 225),
	@Deprecated
	LEGACY_IRON_BOOTS(309, 1, 195),
	@Deprecated
	LEGACY_DIAMOND_HELMET(310, 1, 363),
	@Deprecated
	LEGACY_DIAMOND_CHESTPLATE(311, 1, 528),
	@Deprecated
	LEGACY_DIAMOND_LEGGINGS(312, 1, 495),
	@Deprecated
	LEGACY_DIAMOND_BOOTS(313, 1, 429),
	@Deprecated
	LEGACY_GOLD_HELMET(314, 1, 77),
	@Deprecated
	LEGACY_GOLD_CHESTPLATE(315, 1, 112),
	@Deprecated
	LEGACY_GOLD_LEGGINGS(316, 1, 105),
	@Deprecated
	LEGACY_GOLD_BOOTS(317, 1, 91),
	@Deprecated
	LEGACY_FLINT(318),
	@Deprecated
	LEGACY_PORK(319),
	@Deprecated
	LEGACY_GRILLED_PORK(320),
	@Deprecated
	LEGACY_PAINTING(321),
	@Deprecated
	LEGACY_GOLDEN_APPLE(322),
	@Deprecated
	LEGACY_SIGN(323, 16),
	@Deprecated
	LEGACY_WOOD_DOOR(324, 64),
	@Deprecated
	LEGACY_BUCKET(325, 16),
	@Deprecated
	LEGACY_WATER_BUCKET(326, 1),
	@Deprecated
	LEGACY_LAVA_BUCKET(327, 1),
	@Deprecated
	LEGACY_MINECART(328, 1),
	@Deprecated
	LEGACY_SADDLE(329, 1),
	@Deprecated
	LEGACY_IRON_DOOR(330, 64),
	@Deprecated
	LEGACY_REDSTONE(331),
	@Deprecated
	LEGACY_SNOW_BALL(332, 16),
	@Deprecated
	LEGACY_BOAT(333, 1),
	@Deprecated
	LEGACY_LEATHER(334),
	@Deprecated
	LEGACY_MILK_BUCKET(335, 1),
	@Deprecated
	LEGACY_CLAY_BRICK(336),
	@Deprecated
	LEGACY_CLAY_BALL(337),
	@Deprecated
	LEGACY_SUGAR_CANE(338),
	@Deprecated
	LEGACY_PAPER(339),
	@Deprecated
	LEGACY_BOOK(340),
	@Deprecated
	LEGACY_SLIME_BALL(341),
	@Deprecated
	LEGACY_STORAGE_MINECART(342, 1),
	@Deprecated
	LEGACY_POWERED_MINECART(343, 1),
	@Deprecated
	LEGACY_EGG(344, 16),
	@Deprecated
	LEGACY_COMPASS(345),
	@Deprecated
	LEGACY_FISHING_ROD(346, 1, 64),
	@Deprecated
	LEGACY_WATCH(347),
	@Deprecated
	LEGACY_GLOWSTONE_DUST(348),
	@Deprecated
	LEGACY_RAW_FISH(349),
	@Deprecated
	LEGACY_COOKED_FISH(350),
	@Deprecated
	LEGACY_INK_SACK(351, org.bukkit.material.Dye.class),
	@Deprecated
	LEGACY_BONE(352),
	@Deprecated
	LEGACY_SUGAR(353),
	@Deprecated
	LEGACY_CAKE(354, 1),
	@Deprecated
	LEGACY_BED(355, 1),
	@Deprecated
	LEGACY_DIODE(356),
	@Deprecated
	LEGACY_COOKIE(357),
	/**
	 * @see org.bukkit.map.MapView
	 */
	@Deprecated
	LEGACY_MAP(358, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_SHEARS(359, 1, 238),
	@Deprecated
	LEGACY_MELON(360),
	@Deprecated
	LEGACY_PUMPKIN_SEEDS(361),
	@Deprecated
	LEGACY_MELON_SEEDS(362),
	@Deprecated
	LEGACY_RAW_BEEF(363),
	@Deprecated
	LEGACY_COOKED_BEEF(364),
	@Deprecated
	LEGACY_RAW_CHICKEN(365),
	@Deprecated
	LEGACY_COOKED_CHICKEN(366),
	@Deprecated
	LEGACY_ROTTEN_FLESH(367),
	@Deprecated
	LEGACY_ENDER_PEARL(368, 16),
	@Deprecated
	LEGACY_BLAZE_ROD(369),
	@Deprecated
	LEGACY_GHAST_TEAR(370),
	@Deprecated
	LEGACY_GOLD_NUGGET(371),
	@Deprecated
	LEGACY_NETHER_STALK(372),
	@Deprecated
	LEGACY_POTION(373, 1, org.bukkit.material.MaterialData.class),
	@Deprecated
	LEGACY_GLASS_BOTTLE(374),
	@Deprecated
	LEGACY_SPIDER_EYE(375),
	@Deprecated
	LEGACY_FERMENTED_SPIDER_EYE(376),
	@Deprecated
	LEGACY_BLAZE_POWDER(377),
	@Deprecated
	LEGACY_MAGMA_CREAM(378),
	@Deprecated
	LEGACY_BREWING_STAND_ITEM(379),
	@Deprecated
	LEGACY_CAULDRON_ITEM(380),
	@Deprecated
	LEGACY_EYE_OF_ENDER(381),
	@Deprecated
	LEGACY_SPECKLED_MELON(382),
	@Deprecated
	LEGACY_MONSTER_EGG(383, 64, org.bukkit.material.SpawnEgg.class),
	@Deprecated
	LEGACY_EXP_BOTTLE(384, 64),
	@Deprecated
	LEGACY_FIREBALL(385, 64),
	@Deprecated
	LEGACY_BOOK_AND_QUILL(386, 1),
	@Deprecated
	LEGACY_WRITTEN_BOOK(387, 16),
	@Deprecated
	LEGACY_EMERALD(388, 64),
	@Deprecated
	LEGACY_ITEM_FRAME(389),
	@Deprecated
	LEGACY_FLOWER_POT_ITEM(390),
	@Deprecated
	LEGACY_CARROT_ITEM(391),
	@Deprecated
	LEGACY_POTATO_ITEM(392),
	@Deprecated
	LEGACY_BAKED_POTATO(393),
	@Deprecated
	LEGACY_POISONOUS_POTATO(394),
	@Deprecated
	LEGACY_EMPTY_MAP(395),
	@Deprecated
	LEGACY_GOLDEN_CARROT(396),
	@Deprecated
	LEGACY_SKULL_ITEM(397),
	@Deprecated
	LEGACY_CARROT_STICK(398, 1, 25),
	@Deprecated
	LEGACY_NETHER_STAR(399),
	@Deprecated
	LEGACY_PUMPKIN_PIE(400),
	@Deprecated
	LEGACY_FIREWORK(401),
	@Deprecated
	LEGACY_FIREWORK_CHARGE(402),
	@Deprecated
	LEGACY_ENCHANTED_BOOK(403, 1),
	@Deprecated
	LEGACY_REDSTONE_COMPARATOR(404),
	@Deprecated
	LEGACY_NETHER_BRICK_ITEM(405),
	@Deprecated
	LEGACY_QUARTZ(406),
	@Deprecated
	LEGACY_EXPLOSIVE_MINECART(407, 1),
	@Deprecated
	LEGACY_HOPPER_MINECART(408, 1),
	@Deprecated
	LEGACY_PRISMARINE_SHARD(409),
	@Deprecated
	LEGACY_PRISMARINE_CRYSTALS(410),
	@Deprecated
	LEGACY_RABBIT(411),
	@Deprecated
	LEGACY_COOKED_RABBIT(412),
	@Deprecated
	LEGACY_RABBIT_STEW(413, 1),
	@Deprecated
	LEGACY_RABBIT_FOOT(414),
	@Deprecated
	LEGACY_RABBIT_HIDE(415),
	@Deprecated
	LEGACY_ARMOR_STAND(416, 16),
	@Deprecated
	LEGACY_IRON_BARDING(417, 1),
	@Deprecated
	LEGACY_GOLD_BARDING(418, 1),
	@Deprecated
	LEGACY_DIAMOND_BARDING(419, 1),
	@Deprecated
	LEGACY_LEASH(420),
	@Deprecated
	LEGACY_NAME_TAG(421),
	@Deprecated
	LEGACY_COMMAND_MINECART(422, 1),
	@Deprecated
	LEGACY_MUTTON(423),
	@Deprecated
	LEGACY_COOKED_MUTTON(424),
	@Deprecated
	LEGACY_BANNER(425, 16),
	@Deprecated
	LEGACY_END_CRYSTAL(426),
	@Deprecated
	LEGACY_SPRUCE_DOOR_ITEM(427),
	@Deprecated
	LEGACY_BIRCH_DOOR_ITEM(428),
	@Deprecated
	LEGACY_JUNGLE_DOOR_ITEM(429),
	@Deprecated
	LEGACY_ACACIA_DOOR_ITEM(430),
	@Deprecated
	LEGACY_DARK_OAK_DOOR_ITEM(431),
	@Deprecated
	LEGACY_CHORUS_FRUIT(432),
	@Deprecated
	LEGACY_CHORUS_FRUIT_POPPED(433),
	@Deprecated
	LEGACY_BEETROOT(434),
	@Deprecated
	LEGACY_BEETROOT_SEEDS(435),
	@Deprecated
	LEGACY_BEETROOT_SOUP(436, 1),
	@Deprecated
	LEGACY_DRAGONS_BREATH(437),
	@Deprecated
	LEGACY_SPLASH_POTION(438, 1),
	@Deprecated
	LEGACY_SPECTRAL_ARROW(439),
	@Deprecated
	LEGACY_TIPPED_ARROW(440),
	@Deprecated
	LEGACY_LINGERING_POTION(441, 1),
	@Deprecated
	LEGACY_SHIELD(442, 1, 336),
	@Deprecated
	LEGACY_ELYTRA(443, 1, 431),
	@Deprecated
	LEGACY_BOAT_SPRUCE(444, 1),
	@Deprecated
	LEGACY_BOAT_BIRCH(445, 1),
	@Deprecated
	LEGACY_BOAT_JUNGLE(446, 1),
	@Deprecated
	LEGACY_BOAT_ACACIA(447, 1),
	@Deprecated
	LEGACY_BOAT_DARK_OAK(448, 1),
	@Deprecated
	LEGACY_TOTEM(449, 1),
	@Deprecated
	LEGACY_SHULKER_SHELL(450),
	@Deprecated
	LEGACY_IRON_NUGGET(452),
	@Deprecated
	LEGACY_KNOWLEDGE_BOOK(453, 1),
	@Deprecated
	LEGACY_GOLD_RECORD(2256, 1),
	@Deprecated
	LEGACY_GREEN_RECORD(2257, 1),
	@Deprecated
	LEGACY_RECORD_3(2258, 1),
	@Deprecated
	LEGACY_RECORD_4(2259, 1),
	@Deprecated
	LEGACY_RECORD_5(2260, 1),
	@Deprecated
	LEGACY_RECORD_6(2261, 1),
	@Deprecated
	LEGACY_RECORD_7(2262, 1),
	@Deprecated
	LEGACY_RECORD_8(2263, 1),
	@Deprecated
	LEGACY_RECORD_9(2264, 1),
	@Deprecated
	LEGACY_RECORD_10(2265, 1),
	@Deprecated
	LEGACY_RECORD_11(2266, 1),
	@Deprecated
	LEGACY_RECORD_12(2267, 1),
	;
	//</editor-fold>

	public static final Material @Unmodifiable [] VALUES = values();

	@Deprecated
	public static final String LEGACY_PREFIX = "LEGACY_";

	private final int id;
	private final Constructor<? extends MaterialData> ctor;
	private static final Map<String, Material> BY_NAME = new Object2ReferenceOpenHashMap<>();
	private final int maxStack;
	private final short durability;
	public final Class<?> data;
	private final boolean legacy;
	private final NamespacedKey key;
	private boolean isBlock; // Paper

	Material(final int id) {
		this(id, 64);
	}

	Material(final int id, final int stack) {
		this(id, stack, MaterialData.class);
	}

	Material(final int id, final int stack, final int durability) {
		this(id, stack, durability, MaterialData.class);
	}

	Material(final int id, final Class<?> data) {
		this(id, 64, data);
	}

	Material(final int id, final int stack, final Class<?> data) {
		this(id, stack, 0, data);
	}

	@SuppressWarnings("unchecked")
	Material(final int id, final int stack, final int durability, final Class<?> data) {
		this.id = id;
		this.durability = (short) durability;
		this.maxStack = stack;
		this.data = data;
		this.legacy = this.name().startsWith(LEGACY_PREFIX);
		this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
		// try to cache the constructor for this material
		try {
			if (MaterialData.class.isAssignableFrom(data)) {
				this.ctor = (Constructor<? extends MaterialData>) data.getConstructor(Material.class, byte.class);
			} else {
				this.ctor = null;
			}
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new AssertionError(ex);
		}
	}

	// Paper start

	/**
	 * @return If the type is either AIR, CAVE_AIR or VOID_AIR
	 */
	public boolean isEmpty() {
		return switch (this) {
			case AIR, CAVE_AIR, VOID_AIR -> true;
			default -> false;
		};
	}

	/**
	 * Return the translation key for the Material, so the client can translate it into the active
	 * locale when using a TranslatableComponent.
	 *
	 * @return the translation key
	 */
	@NotNull
	public String getTranslationKey() {
		return Bukkit.getUnsafe().getTranslationKey(this);
	}

	/**
	 * Returns the item rarity for the item. The Material <b>MUST</b> be an Item not a block.
	 * Use {@link #isItem()} before this.
	 *
	 * @return the item rarity
	 */
	@NotNull
	public io.papermc.paper.inventory.ItemRarity getItemRarity() {
		return Bukkit.getUnsafe().getItemRarity(this);
	}

	/**
	 * Returns an immutable multimap of attributes for the slot.
	 * {@link #isItem()} must be true for this material.
	 *
	 * @param equipmentSlot the slot to get the attributes for
	 * @return an immutable multimap of attributes
	 * @throws IllegalArgumentException if {@link #isItem()} is false
	 */
	@NotNull
	public Multimap<Attribute, AttributeModifier> getItemAttributes(@NotNull EquipmentSlot equipmentSlot) {
		return Bukkit.getUnsafe().getItemAttributes(this, equipmentSlot);
	}
	// Paper end

	/**
	 * Do not use for any reason.
	 *
	 * @return ID of this material
	 * @deprecated Magic value
	 */
	@Deprecated
	public int getId() {
		Validate.isTrue(legacy, "Cannot get ID of Modern Material");
		return id;
	}

	/**
	 * Do not use for any reason.
	 *
	 * @return legacy status
	 */
	@Deprecated
	public boolean isLegacy() {
		return legacy;
	}

	@NotNull
	@Override
	public NamespacedKey getKey() {
		Validate.isTrue(!legacy, "Cannot get key of Legacy Material");
		return key;
	}

	/**
	 * Gets the maximum amount of this material that can be held in a stack
	 *
	 * @return Maximum stack size for this material
	 */
	public int getMaxStackSize() {
		return maxStack;
	}

	/**
	 * Gets the maximum durability of this material
	 *
	 * @return Maximum durability for this material
	 */
	public short getMaxDurability() {
		return durability;
	}

	/**
	 * Creates a new {@link BlockData} instance for this Material, with all
	 * properties initialized to unspecified defaults.
	 *
	 * @return new data instance
	 */
	@NotNull
	public BlockData createBlockData() {
		return Bukkit.createBlockData(this);
	}

	/**
	 * Creates a new {@link BlockData} instance for this Material, with
	 * all properties initialized to unspecified defaults.
	 *
	 * @param consumer consumer to run on new instance before returning
	 * @return new data instance
	 */
	@NotNull
	public BlockData createBlockData(@Nullable Consumer<BlockData> consumer) {
		return Bukkit.createBlockData(this, consumer);
	}

	/**
	 * Creates a new {@link BlockData} instance for this Material, with all
	 * properties initialized to unspecified defaults, except for those provided
	 * in data.
	 *
	 * @param data data string
	 * @return new data instance
	 * @throws IllegalArgumentException if the specified data is not valid
	 */
	@NotNull
	public BlockData createBlockData(@Nullable String data) throws IllegalArgumentException {
		return Bukkit.createBlockData(this, data);
	}

	/**
	 * Gets the MaterialData class associated with this Material
	 *
	 * @return MaterialData associated with this Material
	 */
	@NotNull
	public Class<? extends MaterialData> getData() {
		Validate.isTrue(legacy, "Cannot get data class of Modern Material");
		return ctor.getDeclaringClass();
	}

	/**
	 * Constructs a new MaterialData relevant for this Material, with the
	 * given initial data
	 *
	 * @param raw Initial data to construct the MaterialData with
	 * @return New MaterialData with the given data
	 * @deprecated Magic value
	 */
	@Deprecated
	@NotNull
	public MaterialData getNewData(final byte raw) {
		Validate.isTrue(legacy, "Cannot get new data of Modern Material");
		try {
			return ctor.newInstance(this, raw);
		} catch (InstantiationException ex) {
			final Throwable t = ex.getCause();
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			} else if (t instanceof Error) {
				throw (Error) t;
			}
			throw new AssertionError(t);
		} catch (Throwable t) {
			throw new AssertionError(t);
		}
	}

	/**
	 * Checks if this Material is a placable block
	 *
	 * @return true if this material is a block
	 */
	public boolean isBlock() {
		// Paper start - cache isBlock
		return this.isBlock;
	}

	private boolean isBlock0() {
		// Paper end
		return switch (this) {
			case ACACIA_BUTTON,
				ACACIA_DOOR,
				ACACIA_FENCE,
				ACACIA_FENCE_GATE,
				ACACIA_LEAVES,
				ACACIA_LOG,
				ACACIA_PLANKS,
				ACACIA_PRESSURE_PLATE,
				ACACIA_SAPLING,
				ACACIA_SIGN,
				ACACIA_SLAB,
				ACACIA_STAIRS,
				ACACIA_TRAPDOOR,
				ACACIA_WALL_SIGN,
				ACACIA_WOOD,
				ACTIVATOR_RAIL,
				AIR,
				ALLIUM,
				ANCIENT_DEBRIS,
				ANDESITE,
				ANDESITE_SLAB,
				ANDESITE_STAIRS,
				ANDESITE_WALL,
				ANVIL,
				ATTACHED_MELON_STEM,
				ATTACHED_PUMPKIN_STEM,
				AZURE_BLUET,
				BAMBOO,
				BAMBOO_SAPLING,
				BARREL,
				BARRIER,
				BASALT,
				BEACON,
				BEDROCK,
				BEEHIVE,
				BEETROOTS,
				BEE_NEST,
				BELL,
				BIRCH_BUTTON,
				BIRCH_DOOR,
				BIRCH_FENCE,
				BIRCH_FENCE_GATE,
				BIRCH_LEAVES,
				BIRCH_LOG,
				BIRCH_PLANKS,
				BIRCH_PRESSURE_PLATE,
				BIRCH_SAPLING,
				BIRCH_SIGN,
				BIRCH_SLAB,
				BIRCH_STAIRS,
				BIRCH_TRAPDOOR,
				BIRCH_WALL_SIGN,
				BIRCH_WOOD,
				BLACKSTONE,
				BLACKSTONE_SLAB,
				BLACKSTONE_STAIRS,
				BLACKSTONE_WALL, BLACK_BANNER,
				BLACK_BED,
				BLACK_CARPET,
				BLACK_CONCRETE,
				BLACK_CONCRETE_POWDER,
				BLACK_GLAZED_TERRACOTTA,
				BLACK_SHULKER_BOX,
				BLACK_STAINED_GLASS,
				BLACK_STAINED_GLASS_PANE,
				BLACK_TERRACOTTA,
				BLACK_WALL_BANNER,
				BLACK_WOOL,
				BLAST_FURNACE,
				BLUE_BANNER,
				BLUE_BED,
				BLUE_CARPET,
				BLUE_CONCRETE,
				BLUE_CONCRETE_POWDER,
				BLUE_GLAZED_TERRACOTTA,
				BLUE_ICE,
				BLUE_ORCHID,
				BLUE_SHULKER_BOX,
				BLUE_STAINED_GLASS,
				BLUE_STAINED_GLASS_PANE,
				BLUE_TERRACOTTA,
				BLUE_WALL_BANNER,
				BLUE_WOOL,
				BONE_BLOCK,
				BOOKSHELF,
				BRAIN_CORAL,
				BRAIN_CORAL_BLOCK,
				BRAIN_CORAL_FAN,
				BRAIN_CORAL_WALL_FAN,
				BREWING_STAND,
				BRICKS,
				BRICK_SLAB,
				BRICK_STAIRS,
				BRICK_WALL,
				BROWN_BANNER,
				BROWN_BED,
				BROWN_CARPET,
				BROWN_CONCRETE,
				BROWN_CONCRETE_POWDER,
				BROWN_GLAZED_TERRACOTTA,
				BROWN_MUSHROOM,
				BROWN_MUSHROOM_BLOCK,
				BROWN_SHULKER_BOX,
				BROWN_STAINED_GLASS,
				BROWN_STAINED_GLASS_PANE,
				BROWN_TERRACOTTA,
				BROWN_WALL_BANNER,
				BROWN_WOOL,
				BUBBLE_COLUMN,
				BUBBLE_CORAL,
				BUBBLE_CORAL_BLOCK,
				BUBBLE_CORAL_FAN,
				BUBBLE_CORAL_WALL_FAN,
				CACTUS,
				CAKE,
				CAMPFIRE,
				CARROTS,
				CARTOGRAPHY_TABLE,
				CARVED_PUMPKIN,
				CAULDRON,
				CAVE_AIR,
				CHAIN,
				CHAIN_COMMAND_BLOCK,
				CHEST,
				CHIPPED_ANVIL,
				CHISELED_NETHER_BRICKS,
				CHISELED_POLISHED_BLACKSTONE,
				CHISELED_QUARTZ_BLOCK,
				CHISELED_RED_SANDSTONE,
				CHISELED_SANDSTONE,
				CHISELED_STONE_BRICKS,
				CHORUS_FLOWER,
				CHORUS_PLANT,
				CLAY,
				COAL_BLOCK,
				COAL_ORE,
				COARSE_DIRT,
				COBBLESTONE,
				COBBLESTONE_SLAB,
				COBBLESTONE_STAIRS,
				COBBLESTONE_WALL,
				COBWEB,
				COCOA,
				COMMAND_BLOCK,
				COMPARATOR,
				COMPOSTER,
				CONDUIT,
				CORNFLOWER,
				CRACKED_NETHER_BRICKS,
				CRACKED_POLISHED_BLACKSTONE_BRICKS,
				CRACKED_STONE_BRICKS,
				CRAFTING_TABLE,
				CREEPER_HEAD,
				CREEPER_WALL_HEAD,
				CRIMSON_BUTTON,
				CRIMSON_DOOR,
				CRIMSON_FENCE,
				CRIMSON_FENCE_GATE,
				CRIMSON_FUNGUS,
				CRIMSON_HYPHAE,
				CRIMSON_NYLIUM,
				CRIMSON_PLANKS,
				CRIMSON_PRESSURE_PLATE,
				CRIMSON_ROOTS,
				CRIMSON_SIGN,
				CRIMSON_SLAB,
				CRIMSON_STAIRS,
				CRIMSON_STEM,
				CRIMSON_TRAPDOOR,
				CRIMSON_WALL_SIGN,
				CRYING_OBSIDIAN,
				CUT_RED_SANDSTONE,
				CUT_RED_SANDSTONE_SLAB,
				CUT_SANDSTONE,
				CUT_SANDSTONE_SLAB,
				CYAN_BANNER,
				CYAN_BED,
				CYAN_CARPET,
				CYAN_CONCRETE,
				CYAN_CONCRETE_POWDER,
				CYAN_GLAZED_TERRACOTTA,
				CYAN_SHULKER_BOX,
				CYAN_STAINED_GLASS,
				CYAN_STAINED_GLASS_PANE,
				CYAN_TERRACOTTA,
				CYAN_WALL_BANNER,
				CYAN_WOOL,
				DAMAGED_ANVIL,
				DANDELION,
				DARK_OAK_BUTTON,
				DARK_OAK_DOOR,
				DARK_OAK_FENCE,
				DARK_OAK_FENCE_GATE,
				DARK_OAK_LEAVES,
				DARK_OAK_LOG,
				DARK_OAK_PLANKS,
				DARK_OAK_PRESSURE_PLATE,
				DARK_OAK_SAPLING,
				DARK_OAK_SIGN,
				DARK_OAK_SLAB,
				DARK_OAK_STAIRS,
				DARK_OAK_TRAPDOOR,
				DARK_OAK_WALL_SIGN,
				DARK_OAK_WOOD,
				DARK_PRISMARINE,
				DARK_PRISMARINE_SLAB,
				DARK_PRISMARINE_STAIRS,
				DAYLIGHT_DETECTOR,
				DEAD_BRAIN_CORAL,
				DEAD_BRAIN_CORAL_BLOCK,
				DEAD_BRAIN_CORAL_FAN,
				DEAD_BRAIN_CORAL_WALL_FAN,
				DEAD_BUBBLE_CORAL,
				DEAD_BUBBLE_CORAL_BLOCK,
				DEAD_BUBBLE_CORAL_FAN,
				DEAD_BUBBLE_CORAL_WALL_FAN,
				DEAD_BUSH,
				DEAD_FIRE_CORAL,
				DEAD_FIRE_CORAL_BLOCK,
				DEAD_FIRE_CORAL_FAN,
				DEAD_FIRE_CORAL_WALL_FAN,
				DEAD_HORN_CORAL,
				DEAD_HORN_CORAL_BLOCK,
				DEAD_HORN_CORAL_FAN,
				DEAD_HORN_CORAL_WALL_FAN,
				DEAD_TUBE_CORAL,
				DEAD_TUBE_CORAL_BLOCK,
				DEAD_TUBE_CORAL_FAN,
				DEAD_TUBE_CORAL_WALL_FAN,
				DETECTOR_RAIL,
				DIAMOND_BLOCK,
				DIAMOND_ORE,
				DIORITE,
				DIORITE_SLAB,
				DIORITE_STAIRS,
				DIORITE_WALL,
				DIRT,
				DISPENSER,
				DRAGON_EGG,
				DRAGON_HEAD,
				DRAGON_WALL_HEAD,
				DRIED_KELP_BLOCK,
				DROPPER,
				EMERALD_BLOCK,
				EMERALD_ORE,
				ENCHANTING_TABLE,
				ENDER_CHEST,
				END_GATEWAY,
				END_PORTAL,
				END_PORTAL_FRAME,
				END_ROD,
				END_STONE,
				END_STONE_BRICKS,
				END_STONE_BRICK_SLAB,
				END_STONE_BRICK_STAIRS,
				END_STONE_BRICK_WALL,
				FARMLAND,
				FERN,
				FIRE,
				FIRE_CORAL,
				FIRE_CORAL_BLOCK,
				FIRE_CORAL_FAN,
				FIRE_CORAL_WALL_FAN,
				FLETCHING_TABLE,
				FLOWER_POT,
				FROSTED_ICE,
				FURNACE,
				GILDED_BLACKSTONE,
				GLASS,
				GLASS_PANE,
				GLOWSTONE,
				GOLD_BLOCK,
				GOLD_ORE,
				GRANITE,
				GRANITE_SLAB,
				GRANITE_STAIRS,
				GRANITE_WALL,
				GRASS,
				GRASS_BLOCK,
				GRASS_PATH,
				GRAVEL,
				GRAY_BANNER,
				GRAY_BED,
				GRAY_CARPET,
				GRAY_CONCRETE,
				GRAY_CONCRETE_POWDER,
				GRAY_GLAZED_TERRACOTTA,
				GRAY_SHULKER_BOX,
				GRAY_STAINED_GLASS,
				GRAY_STAINED_GLASS_PANE,
				GRAY_TERRACOTTA,
				GRAY_WALL_BANNER,
				GRAY_WOOL,
				GREEN_BANNER,
				GREEN_BED,
				GREEN_CARPET,
				GREEN_CONCRETE,
				GREEN_CONCRETE_POWDER,
				GREEN_GLAZED_TERRACOTTA,
				GREEN_SHULKER_BOX,
				GREEN_STAINED_GLASS,
				GREEN_STAINED_GLASS_PANE,
				GREEN_TERRACOTTA,
				GREEN_WALL_BANNER,
				GREEN_WOOL,
				GRINDSTONE,
				HAY_BLOCK,
				HEAVY_WEIGHTED_PRESSURE_PLATE,
				HONEYCOMB_BLOCK,
				HONEY_BLOCK,
				HOPPER,
				HORN_CORAL,
				HORN_CORAL_BLOCK,
				HORN_CORAL_FAN,
				HORN_CORAL_WALL_FAN,
				ICE,
				INFESTED_CHISELED_STONE_BRICKS,
				INFESTED_COBBLESTONE,
				INFESTED_CRACKED_STONE_BRICKS,
				INFESTED_MOSSY_STONE_BRICKS,
				INFESTED_STONE,
				INFESTED_STONE_BRICKS,
				IRON_BARS,
				IRON_BLOCK,
				IRON_DOOR,
				IRON_ORE,
				IRON_TRAPDOOR,
				JACK_O_LANTERN,
				JIGSAW,
				JUKEBOX,
				JUNGLE_BUTTON,
				JUNGLE_DOOR,
				JUNGLE_FENCE,
				JUNGLE_FENCE_GATE,
				JUNGLE_LEAVES,
				JUNGLE_LOG,
				JUNGLE_PLANKS,
				JUNGLE_PRESSURE_PLATE,
				JUNGLE_SAPLING,
				JUNGLE_SIGN,
				JUNGLE_SLAB,
				JUNGLE_STAIRS,
				JUNGLE_TRAPDOOR,
				JUNGLE_WALL_SIGN,
				JUNGLE_WOOD,
				KELP,
				KELP_PLANT,
				LADDER,
				LANTERN,
				LAPIS_BLOCK,
				LAPIS_ORE,
				LARGE_FERN,
				LAVA,
				LECTERN,
				LEVER,
				LIGHT_BLUE_BANNER,
				LIGHT_BLUE_BED,
				LIGHT_BLUE_CARPET,
				LIGHT_BLUE_CONCRETE,
				LIGHT_BLUE_CONCRETE_POWDER,
				LIGHT_BLUE_GLAZED_TERRACOTTA,
				LIGHT_BLUE_SHULKER_BOX,
				LIGHT_BLUE_STAINED_GLASS,
				LIGHT_BLUE_STAINED_GLASS_PANE,
				LIGHT_BLUE_TERRACOTTA,
				LIGHT_BLUE_WALL_BANNER,
				LIGHT_BLUE_WOOL,
				LIGHT_GRAY_BANNER,
				LIGHT_GRAY_BED,
				LIGHT_GRAY_CARPET,
				LIGHT_GRAY_CONCRETE,
				LIGHT_GRAY_CONCRETE_POWDER,
				LIGHT_GRAY_GLAZED_TERRACOTTA,
				LIGHT_GRAY_SHULKER_BOX,
				LIGHT_GRAY_STAINED_GLASS,
				LIGHT_GRAY_STAINED_GLASS_PANE,
				LIGHT_GRAY_TERRACOTTA,
				LIGHT_GRAY_WALL_BANNER,
				LIGHT_GRAY_WOOL,
				LIGHT_WEIGHTED_PRESSURE_PLATE,
				LILAC,
				LILY_OF_THE_VALLEY,
				LILY_PAD,
				LIME_BANNER,
				LIME_BED,
				LIME_CARPET,
				LIME_CONCRETE,
				LIME_CONCRETE_POWDER,
				LIME_GLAZED_TERRACOTTA,
				LIME_SHULKER_BOX,
				LIME_STAINED_GLASS,
				LIME_STAINED_GLASS_PANE,
				LIME_TERRACOTTA,
				LIME_WALL_BANNER,
				LIME_WOOL,
				LODESTONE,
				LOOM,
				MAGENTA_BANNER,
				MAGENTA_BED,
				MAGENTA_CARPET,
				MAGENTA_CONCRETE,
				MAGENTA_CONCRETE_POWDER,
				MAGENTA_GLAZED_TERRACOTTA,
				MAGENTA_SHULKER_BOX,
				MAGENTA_STAINED_GLASS,
				MAGENTA_STAINED_GLASS_PANE,
				MAGENTA_TERRACOTTA,
				MAGENTA_WALL_BANNER,
				MAGENTA_WOOL,
				MAGMA_BLOCK,
				MELON,
				MELON_STEM,
				MOSSY_COBBLESTONE,
				MOSSY_COBBLESTONE_SLAB,
				MOSSY_COBBLESTONE_STAIRS,
				MOSSY_COBBLESTONE_WALL,
				MOSSY_STONE_BRICKS,
				MOSSY_STONE_BRICK_SLAB,
				MOSSY_STONE_BRICK_STAIRS,
				MOSSY_STONE_BRICK_WALL,
				MOVING_PISTON,
				MUSHROOM_STEM,
				MYCELIUM,
				NETHERITE_BLOCK,
				NETHERRACK,
				NETHER_BRICKS,
				NETHER_BRICK_FENCE,
				NETHER_BRICK_SLAB,
				NETHER_BRICK_STAIRS,
				NETHER_BRICK_WALL,
				NETHER_GOLD_ORE,
				NETHER_PORTAL,
				NETHER_QUARTZ_ORE,
				NETHER_SPROUTS,
				NETHER_WART,
				NETHER_WART_BLOCK,
				NOTE_BLOCK,
				OAK_BUTTON,
				OAK_DOOR,
				OAK_FENCE,
				OAK_FENCE_GATE,
				OAK_LEAVES,
				OAK_LOG,
				OAK_PLANKS,
				OAK_PRESSURE_PLATE,
				OAK_SAPLING,
				OAK_SIGN,
				OAK_SLAB,
				OAK_STAIRS,
				OAK_TRAPDOOR,
				OAK_WALL_SIGN,
				OAK_WOOD,
				OBSERVER,
				OBSIDIAN,
				ORANGE_BANNER,
				ORANGE_BED,
				ORANGE_CARPET,
				ORANGE_CONCRETE,
				ORANGE_CONCRETE_POWDER,
				ORANGE_GLAZED_TERRACOTTA,
				ORANGE_SHULKER_BOX,
				ORANGE_STAINED_GLASS,
				ORANGE_STAINED_GLASS_PANE,
				ORANGE_TERRACOTTA,
				ORANGE_TULIP,
				ORANGE_WALL_BANNER,
				ORANGE_WOOL,
				OXEYE_DAISY,
				PACKED_ICE,
				PEONY,
				PETRIFIED_OAK_SLAB,
				PINK_BANNER,
				PINK_BED,
				PINK_CARPET,
				PINK_CONCRETE,
				PINK_CONCRETE_POWDER,
				PINK_GLAZED_TERRACOTTA,
				PINK_SHULKER_BOX,
				PINK_STAINED_GLASS,
				PINK_STAINED_GLASS_PANE,
				PINK_TERRACOTTA,
				PINK_TULIP,
				PINK_WALL_BANNER,
				PINK_WOOL,
				PISTON,
				PISTON_HEAD,
				PLAYER_HEAD,
				PLAYER_WALL_HEAD,
				PODZOL,
				POLISHED_ANDESITE,
				POLISHED_ANDESITE_SLAB,
				POLISHED_ANDESITE_STAIRS,
				POLISHED_BASALT,
				POLISHED_BLACKSTONE,
				POLISHED_BLACKSTONE_BRICKS,
				POLISHED_BLACKSTONE_BRICK_SLAB,
				POLISHED_BLACKSTONE_BRICK_STAIRS,
				POLISHED_BLACKSTONE_BRICK_WALL,
				POLISHED_BLACKSTONE_BUTTON,
				POLISHED_BLACKSTONE_PRESSURE_PLATE,
				POLISHED_BLACKSTONE_SLAB,
				POLISHED_BLACKSTONE_STAIRS,
				POLISHED_BLACKSTONE_WALL,
				POLISHED_DIORITE,
				POLISHED_DIORITE_SLAB,
				POLISHED_DIORITE_STAIRS,
				POLISHED_GRANITE,
				POLISHED_GRANITE_SLAB,
				POLISHED_GRANITE_STAIRS,
				POPPY,
				POTATOES,
				POTTED_ACACIA_SAPLING,
				POTTED_ALLIUM,
				POTTED_AZURE_BLUET,
				POTTED_BAMBOO,
				POTTED_BIRCH_SAPLING,
				POTTED_BLUE_ORCHID,
				POTTED_BROWN_MUSHROOM,
				POTTED_CACTUS,
				POTTED_CORNFLOWER,
				POTTED_CRIMSON_FUNGUS,
				POTTED_CRIMSON_ROOTS,
				POTTED_DANDELION,
				POTTED_DARK_OAK_SAPLING,
				POTTED_DEAD_BUSH,
				POTTED_FERN,
				POTTED_JUNGLE_SAPLING,
				POTTED_LILY_OF_THE_VALLEY,
				POTTED_OAK_SAPLING,
				POTTED_ORANGE_TULIP,
				POTTED_OXEYE_DAISY,
				POTTED_PINK_TULIP,
				POTTED_POPPY,
				POTTED_RED_MUSHROOM,
				POTTED_RED_TULIP,
				POTTED_SPRUCE_SAPLING,
				POTTED_WARPED_FUNGUS,
				POTTED_WARPED_ROOTS,
				POTTED_WHITE_TULIP,
				POTTED_WITHER_ROSE,
				POWERED_RAIL,
				PRISMARINE,
				PRISMARINE_BRICKS,
				PRISMARINE_BRICK_SLAB,
				PRISMARINE_BRICK_STAIRS,
				PRISMARINE_SLAB,
				PRISMARINE_STAIRS,
				PRISMARINE_WALL,
				PUMPKIN,
				PUMPKIN_STEM,
				PURPLE_BANNER,
				PURPLE_BED,
				PURPLE_CARPET,
				PURPLE_CONCRETE,
				PURPLE_CONCRETE_POWDER,
				PURPLE_GLAZED_TERRACOTTA,
				PURPLE_SHULKER_BOX,
				PURPLE_STAINED_GLASS,
				PURPLE_STAINED_GLASS_PANE,
				PURPLE_TERRACOTTA,
				PURPLE_WALL_BANNER,
				PURPLE_WOOL,
				PURPUR_BLOCK,
				PURPUR_PILLAR,
				PURPUR_SLAB,
				PURPUR_STAIRS,
				QUARTZ_BLOCK,
				QUARTZ_BRICKS,
				QUARTZ_PILLAR,
				QUARTZ_SLAB,
				QUARTZ_STAIRS,
				RAIL,
				REDSTONE_BLOCK,
				REDSTONE_LAMP,
				REDSTONE_ORE,
				REDSTONE_TORCH,
				REDSTONE_WALL_TORCH,
				REDSTONE_WIRE,
				RED_BANNER,
				RED_BED,
				RED_CARPET,
				RED_CONCRETE,
				RED_CONCRETE_POWDER,
				RED_GLAZED_TERRACOTTA,
				RED_MUSHROOM,
				RED_MUSHROOM_BLOCK,
				RED_NETHER_BRICKS,
				RED_NETHER_BRICK_SLAB,
				RED_NETHER_BRICK_STAIRS,
				RED_NETHER_BRICK_WALL,
				RED_SAND,
				RED_SANDSTONE,
				RED_SANDSTONE_SLAB,
				RED_SANDSTONE_STAIRS,
				RED_SANDSTONE_WALL,
				RED_SHULKER_BOX,
				RED_STAINED_GLASS,
				RED_STAINED_GLASS_PANE,
				RED_TERRACOTTA,
				RED_TULIP,
				RED_WALL_BANNER,
				RED_WOOL,
				REPEATER,
				REPEATING_COMMAND_BLOCK,
				RESPAWN_ANCHOR,
				ROSE_BUSH,
				SAND,
				SANDSTONE,
				SANDSTONE_SLAB,
				SANDSTONE_STAIRS,
				SANDSTONE_WALL,
				SCAFFOLDING,
				SEAGRASS,
				SEA_LANTERN,
				SEA_PICKLE,
				SHROOMLIGHT,
				SHULKER_BOX,
				SKELETON_SKULL,
				SKELETON_WALL_SKULL,
				SLIME_BLOCK,
				SMITHING_TABLE,
				SMOKER,
				SMOOTH_QUARTZ,
				SMOOTH_QUARTZ_SLAB,
				SMOOTH_QUARTZ_STAIRS,
				SMOOTH_RED_SANDSTONE,
				SMOOTH_RED_SANDSTONE_SLAB,
				SMOOTH_RED_SANDSTONE_STAIRS,
				SMOOTH_SANDSTONE,
				SMOOTH_SANDSTONE_SLAB,
				SMOOTH_SANDSTONE_STAIRS,
				SMOOTH_STONE,
				SMOOTH_STONE_SLAB,
				SNOW,
				SNOW_BLOCK,
				SOUL_CAMPFIRE,
				SOUL_FIRE,
				SOUL_LANTERN,
				SOUL_SAND,
				SOUL_SOIL,
				SOUL_TORCH,
				SOUL_WALL_TORCH,
				SPAWNER,
				SPONGE,
				SPRUCE_BUTTON,
				SPRUCE_DOOR,
				SPRUCE_FENCE,
				SPRUCE_FENCE_GATE,
				SPRUCE_LEAVES,
				SPRUCE_LOG,
				SPRUCE_PLANKS,
				SPRUCE_PRESSURE_PLATE,
				SPRUCE_SAPLING,
				SPRUCE_SIGN,
				SPRUCE_SLAB,
				SPRUCE_STAIRS,
				SPRUCE_TRAPDOOR,
				SPRUCE_WALL_SIGN,
				SPRUCE_WOOD,
				STICKY_PISTON,
				STONE,
				STONECUTTER,
				STONE_BRICKS,
				STONE_BRICK_SLAB,
				STONE_BRICK_STAIRS,
				STONE_BRICK_WALL,
				STONE_BUTTON,
				STONE_PRESSURE_PLATE,
				STONE_SLAB,
				STONE_STAIRS,
				STRIPPED_ACACIA_LOG,
				STRIPPED_ACACIA_WOOD,
				STRIPPED_BIRCH_LOG,
				STRIPPED_BIRCH_WOOD,
				STRIPPED_CRIMSON_HYPHAE,
				STRIPPED_CRIMSON_STEM,
				STRIPPED_DARK_OAK_LOG,
				STRIPPED_DARK_OAK_WOOD,
				STRIPPED_JUNGLE_LOG,
				STRIPPED_JUNGLE_WOOD,
				STRIPPED_OAK_LOG,
				STRIPPED_OAK_WOOD,
				STRIPPED_SPRUCE_LOG,
				STRIPPED_SPRUCE_WOOD,
				STRIPPED_WARPED_HYPHAE,
				STRIPPED_WARPED_STEM,
				STRUCTURE_BLOCK,
				STRUCTURE_VOID,
				SUGAR_CANE,
				SUNFLOWER,
				SWEET_BERRY_BUSH,
				TALL_GRASS,
				TALL_SEAGRASS,
				TARGET,
				TERRACOTTA,
				TNT,
				TORCH,
				TRAPPED_CHEST,
				TRIPWIRE,
				TRIPWIRE_HOOK,
				TUBE_CORAL,
				TUBE_CORAL_BLOCK,
				TUBE_CORAL_FAN,
				TUBE_CORAL_WALL_FAN,
				TURTLE_EGG,
				TWISTING_VINES,
				TWISTING_VINES_PLANT,
				VINE,
				VOID_AIR,
				WALL_TORCH,
				WARPED_BUTTON,
				WARPED_DOOR,
				WARPED_FENCE,
				WARPED_FENCE_GATE,
				WARPED_FUNGUS,
				WARPED_HYPHAE,
				WARPED_NYLIUM,
				WARPED_PLANKS,
				WARPED_PRESSURE_PLATE,
				WARPED_ROOTS,
				WARPED_SIGN,
				WARPED_SLAB,
				WARPED_STAIRS,
				WARPED_STEM,
				WARPED_TRAPDOOR,
				WARPED_WALL_SIGN,
				WARPED_WART_BLOCK,
				WATER,
				WEEPING_VINES,
				WEEPING_VINES_PLANT,
				WET_SPONGE,
				WHEAT,
				WHITE_BANNER,
				WHITE_BED,
				WHITE_CARPET,
				WHITE_CONCRETE,
				WHITE_CONCRETE_POWDER,
				WHITE_GLAZED_TERRACOTTA,
				WHITE_SHULKER_BOX,
				WHITE_STAINED_GLASS,
				WHITE_STAINED_GLASS_PANE,
				WHITE_TERRACOTTA,
				WHITE_TULIP,
				WHITE_WALL_BANNER,
				WHITE_WOOL,
				WITHER_ROSE,
				WITHER_SKELETON_SKULL,
				WITHER_SKELETON_WALL_SKULL,
				YELLOW_BANNER,
				YELLOW_BED,
				YELLOW_CARPET,
				YELLOW_CONCRETE,
				YELLOW_CONCRETE_POWDER,
				YELLOW_GLAZED_TERRACOTTA,
				YELLOW_SHULKER_BOX,
				YELLOW_STAINED_GLASS,
				YELLOW_STAINED_GLASS_PANE,
				YELLOW_TERRACOTTA,
				YELLOW_WALL_BANNER,
				YELLOW_WOOL,
				ZOMBIE_HEAD,
				ZOMBIE_WALL_HEAD -> true;
			default -> 0 <= id && id < 256;
		};
	}

	/**
	 * Checks if this Material is edible.
	 *
	 * @return true if this Material is edible.
	 */
	public boolean isEdible() {
		return switch (this) {
			case APPLE,
				BAKED_POTATO,
				BEEF,
				BEETROOT,
				BEETROOT_SOUP,
				BREAD,
				CARROT,
				CHICKEN,
				CHORUS_FRUIT,
				COD,
				COOKED_BEEF,
				COOKED_CHICKEN,
				COOKED_COD,
				COOKED_MUTTON,
				COOKED_PORKCHOP,
				COOKED_RABBIT,
				COOKED_SALMON,
				COOKIE,
				DRIED_KELP,
				ENCHANTED_GOLDEN_APPLE,
				GOLDEN_APPLE,
				GOLDEN_CARROT,
				HONEY_BOTTLE,
				MELON_SLICE,
				MUSHROOM_STEW,
				MUTTON,
				POISONOUS_POTATO,
				PORKCHOP,
				POTATO,
				PUFFERFISH,
				PUMPKIN_PIE,
				RABBIT,
				RABBIT_STEW,
				ROTTEN_FLESH,
				SALMON,
				SPIDER_EYE,
				SUSPICIOUS_STEW,
				SWEET_BERRIES,
				TROPICAL_FISH,
				LEGACY_BREAD,
				LEGACY_CARROT_ITEM,
				LEGACY_BAKED_POTATO,
				LEGACY_POTATO_ITEM,
				LEGACY_POISONOUS_POTATO,
				LEGACY_GOLDEN_CARROT,
				LEGACY_PUMPKIN_PIE,
				LEGACY_COOKIE,
				LEGACY_MELON,
				LEGACY_MUSHROOM_SOUP,
				LEGACY_RAW_CHICKEN,
				LEGACY_COOKED_CHICKEN,
				LEGACY_RAW_BEEF,
				LEGACY_COOKED_BEEF,
				LEGACY_RAW_FISH,
				LEGACY_COOKED_FISH,
				LEGACY_PORK,
				LEGACY_GRILLED_PORK,
				LEGACY_APPLE,
				LEGACY_GOLDEN_APPLE,
				LEGACY_ROTTEN_FLESH,
				LEGACY_SPIDER_EYE,
				LEGACY_RABBIT,
				LEGACY_COOKED_RABBIT,
				LEGACY_RABBIT_STEW,
				LEGACY_MUTTON,
				LEGACY_COOKED_MUTTON,
				LEGACY_BEETROOT,
				LEGACY_CHORUS_FRUIT,
				LEGACY_BEETROOT_SOUP -> true;
			default -> false;
		};
	}

	/**
	 * Attempts to get the Material with the given name.
	 * <p>
	 * This is a normal lookup, names must be the precise name they are given
	 * in the enum.
	 *
	 * @param name Name of the material to get
	 * @return Material if found, or null
	 */
	@Nullable
	public static Material getMaterial(@NotNull final String name) {
		return getMaterial(name, false);
	}

	/**
	 * Attempts to get the Material with the given name.
	 * <p>
	 * This is a normal lookup, names must be the precise name they are given in
	 * the enum (but optionally including the LEGACY_PREFIX if legacyName is
	 * true).
	 * <p>
	 * If legacyName is true, then the lookup will be against legacy materials,
	 * but the returned Material will be a modern material (ie this method is
	 * useful for updating stored data).
	 *
	 * @param name       Name of the material to get
	 * @param legacyName whether this is a legacy name lookup
	 * @return Material if found, or null
	 */
	@Nullable
	public static Material getMaterial(@NotNull String name, boolean legacyName) {
		if (legacyName) {
			if (!name.startsWith(LEGACY_PREFIX)) {
				name = LEGACY_PREFIX + name;
			}

			Material match = BY_NAME.get(name);
			return Bukkit.getUnsafe().fromLegacy(match);
		}

		return BY_NAME.get(name);
	}

	/**
	 * Attempts to match the Material with the given name.
	 * <p>
	 * This is a match lookup; names will be stripped of the "minecraft:"
	 * namespace, converted to uppercase, then stripped of special characters in
	 * an attempt to format it like the enum.
	 *
	 * @param name Name of the material to get
	 * @return Material if found, or null
	 */
	@Nullable
	public static Material matchMaterial(@NotNull final String name) {
		return matchMaterial(name, false);
	}

	/**
	 * Attempts to match the Material with the given name.
	 * <p>
	 * This is a match lookup; names will be stripped of the "minecraft:"
	 * namespace, converted to uppercase, then stripped of special characters in
	 * an attempt to format it like the enum.
	 *
	 * @param name       Name of the material to get
	 * @param legacyName whether this is a legacy name (see
	 *                   {@link #getMaterial(java.lang.String, boolean)}
	 * @return Material if found, or null
	 */
	@Nullable
	public static Material matchMaterial(@NotNull final String name, boolean legacyName) {
		Validate.notNull(name, "Name cannot be null");

		String filtered = name;
		if (filtered.startsWith(NamespacedKey.MINECRAFT + ":")) {
			filtered = filtered.substring((NamespacedKey.MINECRAFT + ":").length());
		}

		filtered = filtered.toUpperCase(java.util.Locale.ENGLISH);

		filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
		return getMaterial(filtered, legacyName);
	}

	static {
		for (Material material : values()) {
			BY_NAME.put(material.name(), material);
			material.isBlock = material.isBlock0(); // Paper
		}
	}

	/**
	 * @return True if this material represents a playable music disk.
	 */
	public boolean isRecord() {
		return switch (this) {
			case MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_BLOCKS, MUSIC_DISC_CAT, MUSIC_DISC_CHIRP, MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI, MUSIC_DISC_PIGSTEP, MUSIC_DISC_STAL, MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD ->
				true;
			default -> id >= LEGACY_GOLD_RECORD.id && id <= LEGACY_RECORD_12.id;
		};
	}

	/**
	 * Check if the material is a block and solid (can be built upon)
	 *
	 * @return True if this material is a block and solid
	 */
	public boolean isSolid() {
		if (!isBlock() || id == 0) {
			return false;
		}
		return switch (this) {
			case ACACIA_DOOR,
				ACACIA_FENCE,
				ACACIA_FENCE_GATE,
				ACACIA_LEAVES,
				ACACIA_LOG,
				ACACIA_PLANKS,
				ACACIA_PRESSURE_PLATE,
				ACACIA_SIGN,
				ACACIA_SLAB,
				ACACIA_STAIRS,
				ACACIA_TRAPDOOR,
				ACACIA_WALL_SIGN,
				ACACIA_WOOD,
				ANCIENT_DEBRIS,
				ANDESITE,
				ANDESITE_SLAB,
				ANDESITE_STAIRS,
				ANDESITE_WALL,
				ANVIL,
				BAMBOO,
				BARREL,
				BARRIER,
				BASALT,
				BEACON,
				BEDROCK,
				BEEHIVE,
				BEE_NEST,
				BELL,
				BIRCH_DOOR,
				BIRCH_FENCE,
				BIRCH_FENCE_GATE,
				BIRCH_LEAVES,
				BIRCH_LOG,
				BIRCH_PLANKS,
				BIRCH_PRESSURE_PLATE,
				BIRCH_SIGN,
				BIRCH_SLAB,
				BIRCH_STAIRS,
				BIRCH_TRAPDOOR,
				BIRCH_WALL_SIGN,
				BIRCH_WOOD,
				BLACKSTONE,
				BLACKSTONE_SLAB,
				BLACKSTONE_STAIRS,
				BLACKSTONE_WALL,
				BLACK_BANNER,
				BLACK_BED,
				BLACK_CONCRETE,
				BLACK_CONCRETE_POWDER,
				BLACK_GLAZED_TERRACOTTA,
				BLACK_SHULKER_BOX,
				BLACK_STAINED_GLASS,
				BLACK_STAINED_GLASS_PANE,
				BLACK_TERRACOTTA,
				BLACK_WALL_BANNER,
				BLACK_WOOL,
				BLAST_FURNACE,
				BLUE_BANNER,
				BLUE_BED,
				BLUE_CONCRETE,
				BLUE_CONCRETE_POWDER,
				BLUE_GLAZED_TERRACOTTA,
				BLUE_ICE,
				BLUE_SHULKER_BOX,
				BLUE_STAINED_GLASS,
				BLUE_STAINED_GLASS_PANE,
				BLUE_TERRACOTTA,
				BLUE_WALL_BANNER,
				BLUE_WOOL,
				BONE_BLOCK,
				BOOKSHELF,
				BRAIN_CORAL_BLOCK,
				BREWING_STAND,
				BRICKS,
				BRICK_SLAB,
				BRICK_STAIRS,
				BRICK_WALL,
				BROWN_BANNER,
				BROWN_BED,
				BROWN_CONCRETE,
				BROWN_CONCRETE_POWDER,
				BROWN_GLAZED_TERRACOTTA,
				BROWN_MUSHROOM_BLOCK,
				BROWN_SHULKER_BOX,
				BROWN_STAINED_GLASS,
				BROWN_STAINED_GLASS_PANE,
				BROWN_TERRACOTTA,
				BROWN_WALL_BANNER,
				BROWN_WOOL,
				BUBBLE_CORAL_BLOCK,
				CACTUS,
				CAKE,
				CAMPFIRE,
				CARTOGRAPHY_TABLE,
				CARVED_PUMPKIN,
				CAULDRON,
				CHAIN,
				CHAIN_COMMAND_BLOCK,
				CHEST,
				CHIPPED_ANVIL,
				CHISELED_NETHER_BRICKS,
				CHISELED_POLISHED_BLACKSTONE,
				CHISELED_QUARTZ_BLOCK,
				CHISELED_RED_SANDSTONE,
				CHISELED_SANDSTONE,
				CHISELED_STONE_BRICKS,
				CLAY,
				COAL_BLOCK,
				COAL_ORE,
				COARSE_DIRT,
				COBBLESTONE,
				COBBLESTONE_SLAB,
				COBBLESTONE_STAIRS,
				COBBLESTONE_WALL,
				COMMAND_BLOCK,
				COMPOSTER,
				CONDUIT,
				CRACKED_NETHER_BRICKS,
				CRACKED_POLISHED_BLACKSTONE_BRICKS,
				CRACKED_STONE_BRICKS,
				CRAFTING_TABLE,
				CRIMSON_DOOR,
				CRIMSON_FENCE,
				CRIMSON_FENCE_GATE,
				CRIMSON_HYPHAE,
				CRIMSON_NYLIUM,
				CRIMSON_PLANKS,
				CRIMSON_PRESSURE_PLATE,
				CRIMSON_SIGN,
				CRIMSON_SLAB,
				CRIMSON_STAIRS,
				CRIMSON_STEM,
				CRIMSON_TRAPDOOR,
				CRIMSON_WALL_SIGN,
				CRYING_OBSIDIAN,
				CUT_RED_SANDSTONE,
				CUT_RED_SANDSTONE_SLAB,
				CUT_SANDSTONE,
				CUT_SANDSTONE_SLAB,
				CYAN_BANNER,
				CYAN_BED,
				CYAN_CONCRETE,
				CYAN_CONCRETE_POWDER,
				CYAN_GLAZED_TERRACOTTA,
				CYAN_SHULKER_BOX,
				CYAN_STAINED_GLASS,
				CYAN_STAINED_GLASS_PANE,
				CYAN_TERRACOTTA,
				CYAN_WALL_BANNER,
				CYAN_WOOL,
				DAMAGED_ANVIL,
				DARK_OAK_DOOR,
				DARK_OAK_FENCE,
				DARK_OAK_FENCE_GATE,
				DARK_OAK_LEAVES,
				DARK_OAK_LOG,
				DARK_OAK_PLANKS,
				DARK_OAK_PRESSURE_PLATE,
				DARK_OAK_SIGN,
				DARK_OAK_SLAB,
				DARK_OAK_STAIRS,
				DARK_OAK_TRAPDOOR,
				DARK_OAK_WALL_SIGN,
				DARK_OAK_WOOD,
				DARK_PRISMARINE,
				DARK_PRISMARINE_SLAB,
				DARK_PRISMARINE_STAIRS,
				DAYLIGHT_DETECTOR,
				DEAD_BRAIN_CORAL,
				DEAD_BRAIN_CORAL_BLOCK,
				DEAD_BRAIN_CORAL_FAN,
				DEAD_BRAIN_CORAL_WALL_FAN,
				DEAD_BUBBLE_CORAL,
				DEAD_BUBBLE_CORAL_BLOCK,
				DEAD_BUBBLE_CORAL_FAN,
				DEAD_BUBBLE_CORAL_WALL_FAN,
				DEAD_FIRE_CORAL,
				DEAD_FIRE_CORAL_BLOCK,
				DEAD_FIRE_CORAL_FAN,
				DEAD_FIRE_CORAL_WALL_FAN,
				DEAD_HORN_CORAL,
				DEAD_HORN_CORAL_BLOCK,
				DEAD_HORN_CORAL_FAN,
				DEAD_HORN_CORAL_WALL_FAN,
				DEAD_TUBE_CORAL,
				DEAD_TUBE_CORAL_BLOCK,
				DEAD_TUBE_CORAL_FAN,
				DEAD_TUBE_CORAL_WALL_FAN,
				DIAMOND_BLOCK,
				DIAMOND_ORE,
				DIORITE,
				DIORITE_SLAB,
				DIORITE_STAIRS,
				DIORITE_WALL,
				DIRT,
				DISPENSER,
				DRAGON_EGG,
				DRIED_KELP_BLOCK,
				DROPPER,
				EMERALD_BLOCK,
				EMERALD_ORE,
				ENCHANTING_TABLE,
				ENDER_CHEST,
				END_PORTAL_FRAME,
				END_STONE,
				END_STONE_BRICKS,
				END_STONE_BRICK_SLAB,
				END_STONE_BRICK_STAIRS,
				END_STONE_BRICK_WALL,
				FARMLAND,
				FIRE_CORAL_BLOCK,
				FLETCHING_TABLE,
				FROSTED_ICE,
				FURNACE,
				GILDED_BLACKSTONE,
				GLASS,
				GLASS_PANE,
				GLOWSTONE,
				GOLD_BLOCK,
				GOLD_ORE,
				GRANITE,
				GRANITE_SLAB,
				GRANITE_STAIRS,
				GRANITE_WALL,
				GRASS_BLOCK,
				GRASS_PATH,
				GRAVEL,
				GRAY_BANNER,
				GRAY_BED,
				GRAY_CONCRETE,
				GRAY_CONCRETE_POWDER,
				GRAY_GLAZED_TERRACOTTA,
				GRAY_SHULKER_BOX,
				GRAY_STAINED_GLASS,
				GRAY_STAINED_GLASS_PANE,
				GRAY_TERRACOTTA,
				GRAY_WALL_BANNER,
				GRAY_WOOL,
				GREEN_BANNER,
				GREEN_BED,
				GREEN_CONCRETE,
				GREEN_CONCRETE_POWDER,
				GREEN_GLAZED_TERRACOTTA,
				GREEN_SHULKER_BOX,
				GREEN_STAINED_GLASS,
				GREEN_STAINED_GLASS_PANE,
				GREEN_TERRACOTTA,
				GREEN_WALL_BANNER,
				GREEN_WOOL,
				GRINDSTONE,
				HAY_BLOCK,
				HEAVY_WEIGHTED_PRESSURE_PLATE,
				HONEYCOMB_BLOCK,
				HONEY_BLOCK,
				HOPPER,
				HORN_CORAL_BLOCK,
				ICE,
				INFESTED_CHISELED_STONE_BRICKS,
				INFESTED_COBBLESTONE,
				INFESTED_CRACKED_STONE_BRICKS,
				INFESTED_MOSSY_STONE_BRICKS,
				INFESTED_STONE,
				INFESTED_STONE_BRICKS,
				IRON_BARS,
				IRON_BLOCK,
				IRON_DOOR,
				IRON_ORE,
				IRON_TRAPDOOR,
				JACK_O_LANTERN,
				JIGSAW,
				JUKEBOX,
				JUNGLE_DOOR,
				JUNGLE_FENCE,
				JUNGLE_FENCE_GATE,
				JUNGLE_LEAVES,
				JUNGLE_LOG,
				JUNGLE_PLANKS,
				JUNGLE_PRESSURE_PLATE,
				JUNGLE_SIGN,
				JUNGLE_SLAB,
				JUNGLE_STAIRS,
				JUNGLE_TRAPDOOR,
				JUNGLE_WALL_SIGN,
				JUNGLE_WOOD,
				LANTERN,
				LAPIS_BLOCK,
				LAPIS_ORE,
				LECTERN,
				LIGHT_BLUE_BANNER,
				LIGHT_BLUE_BED,
				LIGHT_BLUE_CONCRETE,
				LIGHT_BLUE_CONCRETE_POWDER,
				LIGHT_BLUE_GLAZED_TERRACOTTA,
				LIGHT_BLUE_SHULKER_BOX,
				LIGHT_BLUE_STAINED_GLASS,
				LIGHT_BLUE_STAINED_GLASS_PANE,
				LIGHT_BLUE_TERRACOTTA,
				LIGHT_BLUE_WALL_BANNER,
				LIGHT_BLUE_WOOL,
				LIGHT_GRAY_BANNER,
				LIGHT_GRAY_BED,
				LIGHT_GRAY_CONCRETE,
				LIGHT_GRAY_CONCRETE_POWDER,
				LIGHT_GRAY_GLAZED_TERRACOTTA,
				LIGHT_GRAY_SHULKER_BOX,
				LIGHT_GRAY_STAINED_GLASS,
				LIGHT_GRAY_STAINED_GLASS_PANE,
				LIGHT_GRAY_TERRACOTTA,
				LIGHT_GRAY_WALL_BANNER,
				LIGHT_GRAY_WOOL,
				LIGHT_WEIGHTED_PRESSURE_PLATE,
				LIME_BANNER,
				LIME_BED,
				LIME_CONCRETE,
				LIME_CONCRETE_POWDER,
				LIME_GLAZED_TERRACOTTA,
				LIME_SHULKER_BOX,
				LIME_STAINED_GLASS,
				LIME_STAINED_GLASS_PANE,
				LIME_TERRACOTTA,
				LIME_WALL_BANNER,
				LIME_WOOL,
				LODESTONE,
				LOOM,
				MAGENTA_BANNER,
				MAGENTA_BED,
				MAGENTA_CONCRETE,
				MAGENTA_CONCRETE_POWDER,
				MAGENTA_GLAZED_TERRACOTTA,
				MAGENTA_SHULKER_BOX,
				MAGENTA_STAINED_GLASS,
				MAGENTA_STAINED_GLASS_PANE,
				MAGENTA_TERRACOTTA,
				MAGENTA_WALL_BANNER,
				MAGENTA_WOOL,
				MAGMA_BLOCK,
				MELON,
				MOSSY_COBBLESTONE,
				MOSSY_COBBLESTONE_SLAB,
				MOSSY_COBBLESTONE_STAIRS,
				MOSSY_COBBLESTONE_WALL,
				MOSSY_STONE_BRICKS,
				MOSSY_STONE_BRICK_SLAB,
				MOSSY_STONE_BRICK_STAIRS,
				MOSSY_STONE_BRICK_WALL,
				MOVING_PISTON,
				MUSHROOM_STEM,
				MYCELIUM,
				NETHERITE_BLOCK,
				NETHERRACK,
				NETHER_BRICKS,
				NETHER_BRICK_FENCE,
				NETHER_BRICK_SLAB,
				NETHER_BRICK_STAIRS,
				NETHER_BRICK_WALL,
				NETHER_GOLD_ORE,
				NETHER_QUARTZ_ORE,
				NETHER_WART_BLOCK,
				NOTE_BLOCK,
				OAK_DOOR,
				OAK_FENCE,
				OAK_FENCE_GATE,
				OAK_LEAVES,
				OAK_LOG,
				OAK_PLANKS,
				OAK_PRESSURE_PLATE,
				OAK_SIGN,
				OAK_SLAB,
				OAK_STAIRS,
				OAK_TRAPDOOR,
				OAK_WALL_SIGN,
				OAK_WOOD,
				OBSERVER,
				OBSIDIAN,
				ORANGE_BANNER,
				ORANGE_BED,
				ORANGE_CONCRETE,
				ORANGE_CONCRETE_POWDER,
				ORANGE_GLAZED_TERRACOTTA,
				ORANGE_SHULKER_BOX,
				ORANGE_STAINED_GLASS,
				ORANGE_STAINED_GLASS_PANE,
				ORANGE_TERRACOTTA,
				ORANGE_WALL_BANNER,
				ORANGE_WOOL,
				PACKED_ICE,
				PETRIFIED_OAK_SLAB,
				PINK_BANNER,
				PINK_BED,
				PINK_CONCRETE,
				PINK_CONCRETE_POWDER,
				PINK_GLAZED_TERRACOTTA,
				PINK_SHULKER_BOX,
				PINK_STAINED_GLASS,
				PINK_STAINED_GLASS_PANE,
				PINK_TERRACOTTA,
				PINK_WALL_BANNER,
				PINK_WOOL,
				PISTON,
				PISTON_HEAD,
				PODZOL,
				POLISHED_ANDESITE,
				POLISHED_ANDESITE_SLAB,
				POLISHED_ANDESITE_STAIRS,
				POLISHED_BASALT,
				POLISHED_BLACKSTONE,
				POLISHED_BLACKSTONE_BRICKS,
				POLISHED_BLACKSTONE_BRICK_SLAB,
				POLISHED_BLACKSTONE_BRICK_STAIRS,
				POLISHED_BLACKSTONE_BRICK_WALL,
				POLISHED_BLACKSTONE_PRESSURE_PLATE,
				POLISHED_BLACKSTONE_SLAB,
				POLISHED_BLACKSTONE_STAIRS,
				POLISHED_BLACKSTONE_WALL,
				POLISHED_DIORITE,
				POLISHED_DIORITE_SLAB,
				POLISHED_DIORITE_STAIRS,
				POLISHED_GRANITE,
				POLISHED_GRANITE_SLAB,
				POLISHED_GRANITE_STAIRS,
				PRISMARINE,
				PRISMARINE_BRICKS,
				PRISMARINE_BRICK_SLAB,
				PRISMARINE_BRICK_STAIRS,
				PRISMARINE_SLAB,
				PRISMARINE_STAIRS,
				PRISMARINE_WALL,
				PUMPKIN,
				PURPLE_BANNER,
				PURPLE_BED,
				PURPLE_CONCRETE,
				PURPLE_CONCRETE_POWDER,
				PURPLE_GLAZED_TERRACOTTA,
				PURPLE_SHULKER_BOX,
				PURPLE_STAINED_GLASS,
				PURPLE_STAINED_GLASS_PANE,
				PURPLE_TERRACOTTA,
				PURPLE_WALL_BANNER,
				PURPLE_WOOL,
				PURPUR_BLOCK,
				PURPUR_PILLAR,
				PURPUR_SLAB,
				PURPUR_STAIRS,
				QUARTZ_BLOCK,
				QUARTZ_BRICKS,
				QUARTZ_PILLAR,
				QUARTZ_SLAB,
				QUARTZ_STAIRS,
				REDSTONE_BLOCK,
				REDSTONE_LAMP,
				REDSTONE_ORE,
				RED_BANNER,
				RED_BED,
				RED_CONCRETE,
				RED_CONCRETE_POWDER,
				RED_GLAZED_TERRACOTTA,
				RED_MUSHROOM_BLOCK,
				RED_NETHER_BRICKS,
				RED_NETHER_BRICK_SLAB,
				RED_NETHER_BRICK_STAIRS,
				RED_NETHER_BRICK_WALL,
				RED_SAND,
				RED_SANDSTONE,
				RED_SANDSTONE_SLAB,
				RED_SANDSTONE_STAIRS,
				RED_SANDSTONE_WALL,
				RED_SHULKER_BOX,
				RED_STAINED_GLASS,
				RED_STAINED_GLASS_PANE,
				RED_TERRACOTTA,
				RED_WALL_BANNER,
				RED_WOOL,
				REPEATING_COMMAND_BLOCK,
				RESPAWN_ANCHOR,
				SAND,
				SANDSTONE,
				SANDSTONE_SLAB,
				SANDSTONE_STAIRS,
				SANDSTONE_WALL,
				SEA_LANTERN,
				SHROOMLIGHT,
				SHULKER_BOX,
				SLIME_BLOCK,
				SMITHING_TABLE,
				SMOKER,
				SMOOTH_QUARTZ,
				SMOOTH_QUARTZ_SLAB,
				SMOOTH_QUARTZ_STAIRS,
				SMOOTH_RED_SANDSTONE,
				SMOOTH_RED_SANDSTONE_SLAB,
				SMOOTH_RED_SANDSTONE_STAIRS,
				SMOOTH_SANDSTONE,
				SMOOTH_SANDSTONE_SLAB,
				SMOOTH_SANDSTONE_STAIRS,
				SMOOTH_STONE,
				SMOOTH_STONE_SLAB,
				SNOW_BLOCK,
				SOUL_CAMPFIRE,
				SOUL_LANTERN,
				SOUL_SAND,
				SOUL_SOIL,
				SPAWNER,
				SPONGE,
				SPRUCE_DOOR,
				SPRUCE_FENCE,
				SPRUCE_FENCE_GATE,
				SPRUCE_LEAVES,
				SPRUCE_LOG,
				SPRUCE_PLANKS,
				SPRUCE_PRESSURE_PLATE,
				SPRUCE_SIGN,
				SPRUCE_SLAB,
				SPRUCE_STAIRS,
				SPRUCE_TRAPDOOR,
				SPRUCE_WALL_SIGN,
				SPRUCE_WOOD,
				STICKY_PISTON,
				STONE,
				STONECUTTER,
				STONE_BRICKS,
				STONE_BRICK_SLAB,
				STONE_BRICK_STAIRS,
				STONE_BRICK_WALL,
				STONE_PRESSURE_PLATE,
				STONE_SLAB,
				STONE_STAIRS,
				STRIPPED_ACACIA_LOG,
				STRIPPED_ACACIA_WOOD,
				STRIPPED_BIRCH_LOG,
				STRIPPED_BIRCH_WOOD,
				STRIPPED_CRIMSON_HYPHAE,
				STRIPPED_CRIMSON_STEM,
				STRIPPED_DARK_OAK_LOG,
				STRIPPED_DARK_OAK_WOOD,
				STRIPPED_JUNGLE_LOG,
				STRIPPED_JUNGLE_WOOD,
				STRIPPED_OAK_LOG,
				STRIPPED_OAK_WOOD,
				STRIPPED_SPRUCE_LOG,
				STRIPPED_SPRUCE_WOOD,
				STRIPPED_WARPED_HYPHAE,
				STRIPPED_WARPED_STEM,
				STRUCTURE_BLOCK,
				TARGET,
				TERRACOTTA,
				TNT,
				TRAPPED_CHEST,
				TUBE_CORAL_BLOCK,
				TURTLE_EGG,
				WARPED_DOOR,
				WARPED_FENCE,
				WARPED_FENCE_GATE,
				WARPED_HYPHAE,
				WARPED_NYLIUM,
				WARPED_PLANKS,
				WARPED_PRESSURE_PLATE,
				WARPED_SIGN,
				WARPED_SLAB,
				WARPED_STAIRS,
				WARPED_STEM,
				WARPED_TRAPDOOR,
				WARPED_WALL_SIGN,
				WARPED_WART_BLOCK,
				WET_SPONGE,
				WHITE_BANNER,
				WHITE_BED,
				WHITE_CONCRETE,
				WHITE_CONCRETE_POWDER,
				WHITE_GLAZED_TERRACOTTA,
				WHITE_SHULKER_BOX,
				WHITE_STAINED_GLASS,
				WHITE_STAINED_GLASS_PANE,
				WHITE_TERRACOTTA,
				WHITE_WALL_BANNER,
				WHITE_WOOL,
				YELLOW_BANNER,
				YELLOW_BED,
				YELLOW_CONCRETE,
				YELLOW_CONCRETE_POWDER,
				YELLOW_GLAZED_TERRACOTTA,
				YELLOW_SHULKER_BOX,
				YELLOW_STAINED_GLASS,
				YELLOW_STAINED_GLASS_PANE,
				YELLOW_TERRACOTTA,
				YELLOW_WALL_BANNER,
				YELLOW_WOOL,
				LEGACY_STONE,
				LEGACY_GRASS,
				LEGACY_DIRT,
				LEGACY_COBBLESTONE,
				LEGACY_WOOD,
				LEGACY_BEDROCK,
				LEGACY_SAND,
				LEGACY_GRAVEL,
				LEGACY_GOLD_ORE,
				LEGACY_IRON_ORE,
				LEGACY_COAL_ORE,
				LEGACY_LOG,
				LEGACY_LEAVES,
				LEGACY_SPONGE,
				LEGACY_GLASS,
				LEGACY_LAPIS_ORE,
				LEGACY_LAPIS_BLOCK,
				LEGACY_DISPENSER,
				LEGACY_SANDSTONE,
				LEGACY_NOTE_BLOCK,
				LEGACY_BED_BLOCK,
				LEGACY_PISTON_STICKY_BASE,
				LEGACY_PISTON_BASE,
				LEGACY_PISTON_EXTENSION,
				LEGACY_WOOL,
				LEGACY_PISTON_MOVING_PIECE,
				LEGACY_GOLD_BLOCK,
				LEGACY_IRON_BLOCK,
				LEGACY_DOUBLE_STEP,
				LEGACY_STEP,
				LEGACY_BRICK,
				LEGACY_TNT,
				LEGACY_BOOKSHELF,
				LEGACY_MOSSY_COBBLESTONE,
				LEGACY_OBSIDIAN,
				LEGACY_MOB_SPAWNER,
				LEGACY_WOOD_STAIRS,
				LEGACY_CHEST,
				LEGACY_DIAMOND_ORE,
				LEGACY_DIAMOND_BLOCK,
				LEGACY_WORKBENCH,
				LEGACY_SOIL,
				LEGACY_FURNACE,
				LEGACY_BURNING_FURNACE,
				LEGACY_SIGN_POST,
				LEGACY_WOODEN_DOOR,
				LEGACY_COBBLESTONE_STAIRS,
				LEGACY_WALL_SIGN,
				LEGACY_STONE_PLATE,
				LEGACY_IRON_DOOR_BLOCK,
				LEGACY_WOOD_PLATE,
				LEGACY_REDSTONE_ORE,
				LEGACY_GLOWING_REDSTONE_ORE,
				LEGACY_ICE,
				LEGACY_SNOW_BLOCK,
				LEGACY_CACTUS,
				LEGACY_CLAY,
				LEGACY_JUKEBOX,
				LEGACY_FENCE,
				LEGACY_PUMPKIN,
				LEGACY_NETHERRACK,
				LEGACY_SOUL_SAND,
				LEGACY_GLOWSTONE,
				LEGACY_JACK_O_LANTERN,
				LEGACY_CAKE_BLOCK,
				LEGACY_STAINED_GLASS,
				LEGACY_TRAP_DOOR,
				LEGACY_MONSTER_EGGS,
				LEGACY_SMOOTH_BRICK,
				LEGACY_HUGE_MUSHROOM_1,
				LEGACY_HUGE_MUSHROOM_2,
				LEGACY_IRON_FENCE,
				LEGACY_THIN_GLASS,
				LEGACY_MELON_BLOCK,
				LEGACY_FENCE_GATE,
				LEGACY_BRICK_STAIRS,
				LEGACY_SMOOTH_STAIRS,
				LEGACY_MYCEL,
				LEGACY_NETHER_BRICK,
				LEGACY_NETHER_FENCE,
				LEGACY_NETHER_BRICK_STAIRS,
				LEGACY_ENCHANTMENT_TABLE,
				LEGACY_BREWING_STAND,
				LEGACY_CAULDRON,
				LEGACY_ENDER_PORTAL_FRAME,
				LEGACY_ENDER_STONE,
				LEGACY_DRAGON_EGG,
				LEGACY_REDSTONE_LAMP_OFF,
				LEGACY_REDSTONE_LAMP_ON,
				LEGACY_WOOD_DOUBLE_STEP,
				LEGACY_WOOD_STEP,
				LEGACY_SANDSTONE_STAIRS,
				LEGACY_EMERALD_ORE,
				LEGACY_ENDER_CHEST,
				LEGACY_EMERALD_BLOCK,
				LEGACY_SPRUCE_WOOD_STAIRS,
				LEGACY_BIRCH_WOOD_STAIRS,
				LEGACY_JUNGLE_WOOD_STAIRS,
				LEGACY_COMMAND,
				LEGACY_BEACON,
				LEGACY_COBBLE_WALL,
				LEGACY_ANVIL,
				LEGACY_TRAPPED_CHEST,
				LEGACY_GOLD_PLATE,
				LEGACY_IRON_PLATE,
				LEGACY_DAYLIGHT_DETECTOR,
				LEGACY_REDSTONE_BLOCK,
				LEGACY_QUARTZ_ORE,
				LEGACY_HOPPER,
				LEGACY_QUARTZ_BLOCK,
				LEGACY_QUARTZ_STAIRS,
				LEGACY_DROPPER,
				LEGACY_STAINED_CLAY,
				LEGACY_HAY_BLOCK,
				LEGACY_HARD_CLAY,
				LEGACY_COAL_BLOCK,
				LEGACY_STAINED_GLASS_PANE,
				LEGACY_LEAVES_2,
				LEGACY_LOG_2,
				LEGACY_ACACIA_STAIRS,
				LEGACY_DARK_OAK_STAIRS,
				LEGACY_PACKED_ICE,
				LEGACY_RED_SANDSTONE,
				LEGACY_SLIME_BLOCK,
				LEGACY_BARRIER,
				LEGACY_IRON_TRAPDOOR,
				LEGACY_PRISMARINE,
				LEGACY_SEA_LANTERN,
				LEGACY_DOUBLE_STONE_SLAB2,
				LEGACY_RED_SANDSTONE_STAIRS,
				LEGACY_STONE_SLAB2,
				LEGACY_SPRUCE_FENCE_GATE,
				LEGACY_BIRCH_FENCE_GATE,
				LEGACY_JUNGLE_FENCE_GATE,
				LEGACY_DARK_OAK_FENCE_GATE,
				LEGACY_ACACIA_FENCE_GATE,
				LEGACY_SPRUCE_FENCE,
				LEGACY_BIRCH_FENCE,
				LEGACY_JUNGLE_FENCE,
				LEGACY_DARK_OAK_FENCE,
				LEGACY_ACACIA_FENCE,
				LEGACY_STANDING_BANNER,
				LEGACY_WALL_BANNER,
				LEGACY_DAYLIGHT_DETECTOR_INVERTED,
				LEGACY_SPRUCE_DOOR,
				LEGACY_BIRCH_DOOR,
				LEGACY_JUNGLE_DOOR,
				LEGACY_ACACIA_DOOR,
				LEGACY_DARK_OAK_DOOR,
				LEGACY_PURPUR_BLOCK,
				LEGACY_PURPUR_PILLAR,
				LEGACY_PURPUR_STAIRS,
				LEGACY_PURPUR_DOUBLE_SLAB,
				LEGACY_PURPUR_SLAB,
				LEGACY_END_BRICKS,
				LEGACY_GRASS_PATH,
				LEGACY_STRUCTURE_BLOCK,
				LEGACY_COMMAND_REPEATING,
				LEGACY_COMMAND_CHAIN,
				LEGACY_FROSTED_ICE,
				LEGACY_MAGMA,
				LEGACY_NETHER_WART_BLOCK,
				LEGACY_RED_NETHER_BRICK,
				LEGACY_BONE_BLOCK,
				LEGACY_OBSERVER,
				LEGACY_WHITE_SHULKER_BOX,
				LEGACY_ORANGE_SHULKER_BOX,
				LEGACY_MAGENTA_SHULKER_BOX,
				LEGACY_LIGHT_BLUE_SHULKER_BOX,
				LEGACY_YELLOW_SHULKER_BOX,
				LEGACY_LIME_SHULKER_BOX,
				LEGACY_PINK_SHULKER_BOX,
				LEGACY_GRAY_SHULKER_BOX,
				LEGACY_SILVER_SHULKER_BOX,
				LEGACY_CYAN_SHULKER_BOX,
				LEGACY_PURPLE_SHULKER_BOX,
				LEGACY_BLUE_SHULKER_BOX,
				LEGACY_BROWN_SHULKER_BOX,
				LEGACY_GREEN_SHULKER_BOX,
				LEGACY_RED_SHULKER_BOX,
				LEGACY_BLACK_SHULKER_BOX,
				LEGACY_WHITE_GLAZED_TERRACOTTA,
				LEGACY_ORANGE_GLAZED_TERRACOTTA,
				LEGACY_MAGENTA_GLAZED_TERRACOTTA,
				LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA,
				LEGACY_YELLOW_GLAZED_TERRACOTTA,
				LEGACY_LIME_GLAZED_TERRACOTTA,
				LEGACY_PINK_GLAZED_TERRACOTTA,
				LEGACY_GRAY_GLAZED_TERRACOTTA,
				LEGACY_SILVER_GLAZED_TERRACOTTA,
				LEGACY_CYAN_GLAZED_TERRACOTTA,
				LEGACY_PURPLE_GLAZED_TERRACOTTA,
				LEGACY_BLUE_GLAZED_TERRACOTTA,
				LEGACY_BROWN_GLAZED_TERRACOTTA,
				LEGACY_GREEN_GLAZED_TERRACOTTA,
				LEGACY_RED_GLAZED_TERRACOTTA,
				LEGACY_BLACK_GLAZED_TERRACOTTA,
				LEGACY_CONCRETE,
				LEGACY_CONCRETE_POWDER -> true;
			default -> false;
		};
	}

	/**
	 * Check if the material is an air block.
	 *
	 * @return True if this material is an air block.
	 */
	public boolean isAir() {
		return switch (this) {
			case AIR, CAVE_AIR, VOID_AIR, LEGACY_AIR -> true;
			default -> false;
		};
	}

	/**
	 * Check if the material is a block and does not block any light
	 *
	 * @return True if this material is a block and does not block any light
	 * @deprecated currently does not have an implementation which is well
	 * linked to the underlying server. Contributions welcome.
	 */
	@Deprecated
	public boolean isTransparent() {
		if (!isBlock()) {
			return false;
		}
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isTransparent">
			// ----- Legacy Separator -----
			case ACACIA_BUTTON, ACACIA_SAPLING, ACTIVATOR_RAIL, AIR, ALLIUM, ATTACHED_MELON_STEM, ATTACHED_PUMPKIN_STEM, AZURE_BLUET, BARRIER, BEETROOTS, BIRCH_BUTTON, BIRCH_SAPLING, BLACK_CARPET, BLUE_CARPET, BLUE_ORCHID, BROWN_CARPET, BROWN_MUSHROOM, CARROTS, CAVE_AIR, CHORUS_FLOWER, CHORUS_PLANT, COCOA, COMPARATOR, CREEPER_HEAD, CREEPER_WALL_HEAD, CYAN_CARPET, DANDELION, DARK_OAK_BUTTON, DARK_OAK_SAPLING, DEAD_BUSH, DETECTOR_RAIL, DRAGON_HEAD, DRAGON_WALL_HEAD, END_GATEWAY, END_PORTAL, END_ROD, FERN, FIRE, FLOWER_POT, GRASS, GRAY_CARPET, GREEN_CARPET, JUNGLE_BUTTON, JUNGLE_SAPLING, LADDER, LARGE_FERN, LEVER, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, LILAC, LILY_PAD, LIME_CARPET, MAGENTA_CARPET, MELON_STEM, NETHER_PORTAL, NETHER_WART, OAK_BUTTON, OAK_SAPLING, ORANGE_CARPET, ORANGE_TULIP, OXEYE_DAISY, PEONY, PINK_CARPET, PINK_TULIP, PLAYER_HEAD, PLAYER_WALL_HEAD, POPPY, POTATOES, POTTED_ACACIA_SAPLING, POTTED_ALLIUM, POTTED_AZURE_BLUET, POTTED_BIRCH_SAPLING, POTTED_BLUE_ORCHID, POTTED_BROWN_MUSHROOM, POTTED_CACTUS, POTTED_DANDELION, POTTED_DARK_OAK_SAPLING, POTTED_DEAD_BUSH, POTTED_FERN, POTTED_JUNGLE_SAPLING, POTTED_OAK_SAPLING, POTTED_ORANGE_TULIP, POTTED_OXEYE_DAISY, POTTED_PINK_TULIP, POTTED_POPPY, POTTED_RED_MUSHROOM, POTTED_RED_TULIP, POTTED_SPRUCE_SAPLING, POTTED_WHITE_TULIP, POWERED_RAIL, PUMPKIN_STEM, PURPLE_CARPET, RAIL, REDSTONE_TORCH, REDSTONE_WALL_TORCH, REDSTONE_WIRE, RED_CARPET, RED_MUSHROOM, RED_TULIP, REPEATER, ROSE_BUSH, SKELETON_SKULL, SKELETON_WALL_SKULL, SNOW, SPRUCE_BUTTON, SPRUCE_SAPLING, STONE_BUTTON, STRUCTURE_VOID, SUGAR_CANE, SUNFLOWER, TALL_GRASS, TORCH, TRIPWIRE, TRIPWIRE_HOOK, VINE, VOID_AIR, WALL_TORCH, WHEAT, WHITE_CARPET, WHITE_TULIP, WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL, YELLOW_CARPET, ZOMBIE_HEAD, ZOMBIE_WALL_HEAD, LEGACY_AIR, LEGACY_SAPLING, LEGACY_POWERED_RAIL, LEGACY_DETECTOR_RAIL, LEGACY_LONG_GRASS, LEGACY_DEAD_BUSH, LEGACY_YELLOW_FLOWER, LEGACY_RED_ROSE, LEGACY_BROWN_MUSHROOM, LEGACY_RED_MUSHROOM, LEGACY_TORCH, LEGACY_FIRE, LEGACY_REDSTONE_WIRE, LEGACY_CROPS, LEGACY_LADDER, LEGACY_RAILS, LEGACY_LEVER, LEGACY_REDSTONE_TORCH_OFF, LEGACY_REDSTONE_TORCH_ON, LEGACY_STONE_BUTTON, LEGACY_SNOW, LEGACY_SUGAR_CANE_BLOCK, LEGACY_PORTAL, LEGACY_DIODE_BLOCK_OFF, LEGACY_DIODE_BLOCK_ON, LEGACY_PUMPKIN_STEM, LEGACY_MELON_STEM, LEGACY_VINE, LEGACY_WATER_LILY, LEGACY_NETHER_WARTS, LEGACY_ENDER_PORTAL, LEGACY_COCOA, LEGACY_TRIPWIRE_HOOK, LEGACY_TRIPWIRE, LEGACY_FLOWER_POT, LEGACY_CARROT, LEGACY_POTATO, LEGACY_WOOD_BUTTON, LEGACY_SKULL, LEGACY_REDSTONE_COMPARATOR_OFF, LEGACY_REDSTONE_COMPARATOR_ON, LEGACY_ACTIVATOR_RAIL, LEGACY_CARPET, LEGACY_DOUBLE_PLANT, LEGACY_END_ROD, LEGACY_CHORUS_PLANT, LEGACY_CHORUS_FLOWER, LEGACY_BEETROOT_BLOCK, LEGACY_END_GATEWAY, LEGACY_STRUCTURE_VOID ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Check if the material is a block and can catch fire
	 *
	 * @return True if this material is a block and can catch fire
	 */
	public boolean isFlammable() {
		if (!isBlock()) {
			return false;
		}
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isFlammable">
			// ----- Legacy Separator -----
			case ACACIA_DOOR, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_LEAVES, ACACIA_LOG, ACACIA_PLANKS, ACACIA_PRESSURE_PLATE, ACACIA_SIGN, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_TRAPDOOR, ACACIA_WALL_SIGN, ACACIA_WOOD, BAMBOO, BAMBOO_SAPLING, BARREL, BEEHIVE, BEE_NEST, BIRCH_DOOR, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_LEAVES, BIRCH_LOG, BIRCH_PLANKS, BIRCH_PRESSURE_PLATE, BIRCH_SIGN, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_TRAPDOOR, BIRCH_WALL_SIGN, BIRCH_WOOD, BLACK_BANNER, BLACK_BED, BLACK_CARPET, BLACK_WALL_BANNER, BLACK_WOOL, BLUE_BANNER, BLUE_BED, BLUE_CARPET, BLUE_WALL_BANNER, BLUE_WOOL, BOOKSHELF, BROWN_BANNER, BROWN_BED, BROWN_CARPET, BROWN_MUSHROOM_BLOCK, BROWN_WALL_BANNER, BROWN_WOOL, CAMPFIRE, CARTOGRAPHY_TABLE, CHEST, COMPOSTER, CRAFTING_TABLE, CYAN_BANNER, CYAN_BED, CYAN_CARPET, CYAN_WALL_BANNER, CYAN_WOOL, DARK_OAK_DOOR, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_LEAVES, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_PRESSURE_PLATE, DARK_OAK_SIGN, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_TRAPDOOR, DARK_OAK_WALL_SIGN, DARK_OAK_WOOD, DAYLIGHT_DETECTOR, DEAD_BUSH, FERN, FLETCHING_TABLE, GRASS, GRAY_BANNER, GRAY_BED, GRAY_CARPET, GRAY_WALL_BANNER, GRAY_WOOL, GREEN_BANNER, GREEN_BED, GREEN_CARPET, GREEN_WALL_BANNER, GREEN_WOOL, JUKEBOX, JUNGLE_DOOR, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_LEAVES, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_PRESSURE_PLATE, JUNGLE_SIGN, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_TRAPDOOR, JUNGLE_WALL_SIGN, JUNGLE_WOOD, LARGE_FERN, LECTERN, LIGHT_BLUE_BANNER, LIGHT_BLUE_BED, LIGHT_BLUE_CARPET, LIGHT_BLUE_WALL_BANNER, LIGHT_BLUE_WOOL, LIGHT_GRAY_BANNER, LIGHT_GRAY_BED, LIGHT_GRAY_CARPET, LIGHT_GRAY_WALL_BANNER, LIGHT_GRAY_WOOL, LILAC, LIME_BANNER, LIME_BED, LIME_CARPET, LIME_WALL_BANNER, LIME_WOOL, LOOM, MAGENTA_BANNER, MAGENTA_BED, MAGENTA_CARPET, MAGENTA_WALL_BANNER, MAGENTA_WOOL, MUSHROOM_STEM, NOTE_BLOCK, OAK_DOOR, OAK_FENCE, OAK_FENCE_GATE, OAK_LEAVES, OAK_LOG, OAK_PLANKS, OAK_PRESSURE_PLATE, OAK_SIGN, OAK_SLAB, OAK_STAIRS, OAK_TRAPDOOR, OAK_WALL_SIGN, OAK_WOOD, ORANGE_BANNER, ORANGE_BED, ORANGE_CARPET, ORANGE_WALL_BANNER, ORANGE_WOOL, PEONY, PINK_BANNER, PINK_BED, PINK_CARPET, PINK_WALL_BANNER, PINK_WOOL, PURPLE_BANNER, PURPLE_BED, PURPLE_CARPET, PURPLE_WALL_BANNER, PURPLE_WOOL, RED_BANNER, RED_BED, RED_CARPET, RED_MUSHROOM_BLOCK, RED_WALL_BANNER, RED_WOOL, ROSE_BUSH, SMITHING_TABLE, SOUL_CAMPFIRE, SPRUCE_DOOR, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_LEAVES, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_PRESSURE_PLATE, SPRUCE_SIGN, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_TRAPDOOR, SPRUCE_WALL_SIGN, SPRUCE_WOOD, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, SUNFLOWER, TALL_GRASS, TNT, TRAPPED_CHEST, VINE, WHITE_BANNER, WHITE_BED, WHITE_CARPET, WHITE_WALL_BANNER, WHITE_WOOL, YELLOW_BANNER, YELLOW_BED, YELLOW_CARPET, YELLOW_WALL_BANNER, YELLOW_WOOL, LEGACY_WOOD, LEGACY_LOG, LEGACY_LEAVES, LEGACY_NOTE_BLOCK, LEGACY_BED_BLOCK, LEGACY_LONG_GRASS, LEGACY_DEAD_BUSH, LEGACY_WOOL, LEGACY_TNT, LEGACY_BOOKSHELF, LEGACY_WOOD_STAIRS, LEGACY_CHEST, LEGACY_WORKBENCH, LEGACY_SIGN_POST, LEGACY_WOODEN_DOOR, LEGACY_WALL_SIGN, LEGACY_WOOD_PLATE, LEGACY_JUKEBOX, LEGACY_FENCE, LEGACY_TRAP_DOOR, LEGACY_HUGE_MUSHROOM_1, LEGACY_HUGE_MUSHROOM_2, LEGACY_VINE, LEGACY_FENCE_GATE, LEGACY_WOOD_DOUBLE_STEP, LEGACY_WOOD_STEP, LEGACY_SPRUCE_WOOD_STAIRS, LEGACY_BIRCH_WOOD_STAIRS, LEGACY_JUNGLE_WOOD_STAIRS, LEGACY_TRAPPED_CHEST, LEGACY_DAYLIGHT_DETECTOR, LEGACY_CARPET, LEGACY_LEAVES_2, LEGACY_LOG_2, LEGACY_ACACIA_STAIRS, LEGACY_DARK_OAK_STAIRS, LEGACY_DOUBLE_PLANT, LEGACY_SPRUCE_FENCE_GATE, LEGACY_BIRCH_FENCE_GATE, LEGACY_JUNGLE_FENCE_GATE, LEGACY_DARK_OAK_FENCE_GATE, LEGACY_ACACIA_FENCE_GATE, LEGACY_SPRUCE_FENCE, LEGACY_BIRCH_FENCE, LEGACY_JUNGLE_FENCE, LEGACY_DARK_OAK_FENCE, LEGACY_ACACIA_FENCE, LEGACY_STANDING_BANNER, LEGACY_WALL_BANNER, LEGACY_DAYLIGHT_DETECTOR_INVERTED, LEGACY_SPRUCE_DOOR, LEGACY_BIRCH_DOOR, LEGACY_JUNGLE_DOOR, LEGACY_ACACIA_DOOR, LEGACY_DARK_OAK_DOOR ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Check if the material is a block and can burn away
	 *
	 * @return True if this material is a block and can burn away
	 */
	public boolean isBurnable() {
		if (!isBlock()) {
			return false;
		}
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isBurnable">
			// ----- Legacy Separator -----
			case ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_LEAVES, ACACIA_LOG, ACACIA_PLANKS, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_WOOD, ALLIUM, AZURE_BLUET, BAMBOO, BEEHIVE, BEE_NEST, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_LEAVES, BIRCH_LOG, BIRCH_PLANKS, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_WOOD, BLACK_CARPET, BLACK_WOOL, BLUE_CARPET, BLUE_ORCHID, BLUE_WOOL, BOOKSHELF, BROWN_CARPET, BROWN_WOOL, COAL_BLOCK, COMPOSTER, CORNFLOWER, CYAN_CARPET, CYAN_WOOL, DANDELION, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_LEAVES, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_WOOD, DEAD_BUSH, DRIED_KELP_BLOCK, FERN, GRASS, GRAY_CARPET, GRAY_WOOL, GREEN_CARPET, GREEN_WOOL, HAY_BLOCK, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_LEAVES, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_WOOD, LARGE_FERN, LECTERN, LIGHT_BLUE_CARPET, LIGHT_BLUE_WOOL, LIGHT_GRAY_CARPET, LIGHT_GRAY_WOOL, LILAC, LILY_OF_THE_VALLEY, LIME_CARPET, LIME_WOOL, MAGENTA_CARPET, MAGENTA_WOOL, OAK_FENCE, OAK_FENCE_GATE, OAK_LEAVES, OAK_LOG, OAK_PLANKS, OAK_SLAB, OAK_STAIRS, OAK_WOOD, ORANGE_CARPET, ORANGE_TULIP, ORANGE_WOOL, OXEYE_DAISY, PEONY, PINK_CARPET, PINK_TULIP, PINK_WOOL, POPPY, PURPLE_CARPET, PURPLE_WOOL, RED_CARPET, RED_TULIP, RED_WOOL, ROSE_BUSH, SCAFFOLDING, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_LEAVES, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_WOOD, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, SUNFLOWER, SWEET_BERRY_BUSH, TALL_GRASS, TARGET, TNT, VINE, WHITE_CARPET, WHITE_TULIP, WHITE_WOOL, WITHER_ROSE, YELLOW_CARPET, YELLOW_WOOL, LEGACY_WOOD, LEGACY_LOG, LEGACY_LEAVES, LEGACY_LONG_GRASS, LEGACY_WOOL, LEGACY_YELLOW_FLOWER, LEGACY_RED_ROSE, LEGACY_TNT, LEGACY_BOOKSHELF, LEGACY_WOOD_STAIRS, LEGACY_FENCE, LEGACY_VINE, LEGACY_WOOD_DOUBLE_STEP, LEGACY_WOOD_STEP, LEGACY_SPRUCE_WOOD_STAIRS, LEGACY_BIRCH_WOOD_STAIRS, LEGACY_JUNGLE_WOOD_STAIRS, LEGACY_HAY_BLOCK, LEGACY_COAL_BLOCK, LEGACY_LEAVES_2, LEGACY_LOG_2, LEGACY_CARPET, LEGACY_DOUBLE_PLANT, LEGACY_DEAD_BUSH, LEGACY_FENCE_GATE, LEGACY_SPRUCE_FENCE_GATE, LEGACY_BIRCH_FENCE_GATE, LEGACY_JUNGLE_FENCE_GATE, LEGACY_DARK_OAK_FENCE_GATE, LEGACY_ACACIA_FENCE_GATE, LEGACY_SPRUCE_FENCE, LEGACY_BIRCH_FENCE, LEGACY_JUNGLE_FENCE, LEGACY_DARK_OAK_FENCE, LEGACY_ACACIA_FENCE, LEGACY_ACACIA_STAIRS, LEGACY_DARK_OAK_STAIRS ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Checks if this Material can be used as fuel in a Furnace
	 *
	 * @return true if this Material can be used as fuel.
	 */
	public boolean isFuel() {
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isFuel">
			// ----- Legacy Separator -----
			case ACACIA_BOAT, ACACIA_BUTTON, ACACIA_DOOR, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_LOG, ACACIA_PLANKS, ACACIA_PRESSURE_PLATE, ACACIA_SAPLING, ACACIA_SIGN, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_TRAPDOOR, ACACIA_WOOD, BAMBOO, BARREL, BIRCH_BOAT, BIRCH_BUTTON, BIRCH_DOOR, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_LOG, BIRCH_PLANKS, BIRCH_PRESSURE_PLATE, BIRCH_SAPLING, BIRCH_SIGN, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_TRAPDOOR, BIRCH_WOOD, BLACK_BANNER, BLACK_CARPET, BLACK_WOOL, BLAZE_ROD, BLUE_BANNER, BLUE_CARPET, BLUE_WOOL, BOOKSHELF, BOW, BOWL, BROWN_BANNER, BROWN_CARPET, BROWN_WOOL, CARTOGRAPHY_TABLE, CHARCOAL, CHEST, COAL, COAL_BLOCK, COMPOSTER, CRAFTING_TABLE, CROSSBOW, CYAN_BANNER, CYAN_CARPET, CYAN_WOOL, DARK_OAK_BOAT, DARK_OAK_BUTTON, DARK_OAK_DOOR, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_PRESSURE_PLATE, DARK_OAK_SAPLING, DARK_OAK_SIGN, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_TRAPDOOR, DARK_OAK_WOOD, DAYLIGHT_DETECTOR, DEAD_BUSH, DRIED_KELP_BLOCK, FISHING_ROD, FLETCHING_TABLE, GRAY_BANNER, GRAY_CARPET, GRAY_WOOL, GREEN_BANNER, GREEN_CARPET, GREEN_WOOL, JUKEBOX, JUNGLE_BOAT, JUNGLE_BUTTON, JUNGLE_DOOR, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_PRESSURE_PLATE, JUNGLE_SAPLING, JUNGLE_SIGN, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_TRAPDOOR, JUNGLE_WOOD, LADDER, LAVA_BUCKET, LECTERN, LIGHT_BLUE_BANNER, LIGHT_BLUE_CARPET, LIGHT_BLUE_WOOL, LIGHT_GRAY_BANNER, LIGHT_GRAY_CARPET, LIGHT_GRAY_WOOL, LIME_BANNER, LIME_CARPET, LIME_WOOL, LOOM, MAGENTA_BANNER, MAGENTA_CARPET, MAGENTA_WOOL, NOTE_BLOCK, OAK_BOAT, OAK_BUTTON, OAK_DOOR, OAK_FENCE, OAK_FENCE_GATE, OAK_LOG, OAK_PLANKS, OAK_PRESSURE_PLATE, OAK_SAPLING, OAK_SIGN, OAK_SLAB, OAK_STAIRS, OAK_TRAPDOOR, OAK_WOOD, ORANGE_BANNER, ORANGE_CARPET, ORANGE_WOOL, PINK_BANNER, PINK_CARPET, PINK_WOOL, PURPLE_BANNER, PURPLE_CARPET, PURPLE_WOOL, RED_BANNER, RED_CARPET, RED_WOOL, SCAFFOLDING, SMITHING_TABLE, SPRUCE_BOAT, SPRUCE_BUTTON, SPRUCE_DOOR, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_PRESSURE_PLATE, SPRUCE_SAPLING, SPRUCE_SIGN, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_TRAPDOOR, SPRUCE_WOOD, STICK, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, TRAPPED_CHEST, WHITE_BANNER, WHITE_CARPET, WHITE_WOOL, WOODEN_AXE, WOODEN_HOE, WOODEN_PICKAXE, WOODEN_SHOVEL, WOODEN_SWORD, YELLOW_BANNER, YELLOW_CARPET, YELLOW_WOOL, LEGACY_LAVA_BUCKET, LEGACY_COAL_BLOCK, LEGACY_BLAZE_ROD, LEGACY_COAL, LEGACY_BOAT, LEGACY_BOAT_ACACIA, LEGACY_BOAT_BIRCH, LEGACY_BOAT_DARK_OAK, LEGACY_BOAT_JUNGLE, LEGACY_BOAT_SPRUCE, LEGACY_LOG, LEGACY_LOG_2, LEGACY_WOOD, LEGACY_WOOD_PLATE, LEGACY_FENCE, LEGACY_ACACIA_FENCE, LEGACY_BIRCH_FENCE, LEGACY_DARK_OAK_FENCE, LEGACY_JUNGLE_FENCE, LEGACY_SPRUCE_FENCE, LEGACY_FENCE_GATE, LEGACY_ACACIA_FENCE_GATE, LEGACY_BIRCH_FENCE_GATE, LEGACY_DARK_OAK_FENCE_GATE, LEGACY_JUNGLE_FENCE_GATE, LEGACY_SPRUCE_FENCE_GATE, LEGACY_WOOD_STAIRS, LEGACY_ACACIA_STAIRS, LEGACY_BIRCH_WOOD_STAIRS, LEGACY_DARK_OAK_STAIRS, LEGACY_JUNGLE_WOOD_STAIRS, LEGACY_SPRUCE_WOOD_STAIRS, LEGACY_TRAP_DOOR, LEGACY_WORKBENCH, LEGACY_BOOKSHELF, LEGACY_CHEST, LEGACY_TRAPPED_CHEST, LEGACY_DAYLIGHT_DETECTOR, LEGACY_JUKEBOX, LEGACY_NOTE_BLOCK, LEGACY_BANNER, LEGACY_FISHING_ROD, LEGACY_LADDER, LEGACY_WOOD_SWORD, LEGACY_WOOD_PICKAXE, LEGACY_WOOD_AXE, LEGACY_WOOD_SPADE, LEGACY_WOOD_HOE, LEGACY_BOW, LEGACY_SIGN, LEGACY_WOOD_DOOR, LEGACY_ACACIA_DOOR_ITEM, LEGACY_BIRCH_DOOR_ITEM, LEGACY_DARK_OAK_DOOR_ITEM, LEGACY_JUNGLE_DOOR_ITEM, LEGACY_SPRUCE_DOOR_ITEM, LEGACY_WOOD_STEP, LEGACY_SAPLING, LEGACY_STICK, LEGACY_WOOD_BUTTON, LEGACY_WOOL, LEGACY_CARPET, LEGACY_BOWL ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Check if the material is a block and completely blocks vision
	 *
	 * @return True if this material is a block and completely blocks vision
	 */
	public boolean isOccluding() {
		if (!isBlock()) {
			return false;
		}
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isOccluding">
			// ----- Legacy Separator -----
			case ACACIA_LOG, ACACIA_PLANKS, ACACIA_WOOD, ANCIENT_DEBRIS, ANDESITE, BARREL, BARRIER, BASALT, BEDROCK, BEEHIVE, BEE_NEST, BIRCH_LOG, BIRCH_PLANKS, BIRCH_WOOD, BLACKSTONE, BLACK_CONCRETE, BLACK_CONCRETE_POWDER, BLACK_GLAZED_TERRACOTTA, BLACK_SHULKER_BOX, BLACK_TERRACOTTA, BLACK_WOOL, BLAST_FURNACE, BLUE_CONCRETE, BLUE_CONCRETE_POWDER, BLUE_GLAZED_TERRACOTTA, BLUE_ICE, BLUE_SHULKER_BOX, BLUE_TERRACOTTA, BLUE_WOOL, BONE_BLOCK, BOOKSHELF, BRAIN_CORAL_BLOCK, BRICKS, BROWN_CONCRETE, BROWN_CONCRETE_POWDER, BROWN_GLAZED_TERRACOTTA, BROWN_MUSHROOM_BLOCK, BROWN_SHULKER_BOX, BROWN_TERRACOTTA, BROWN_WOOL, BUBBLE_CORAL_BLOCK, CARTOGRAPHY_TABLE, CARVED_PUMPKIN, CHAIN_COMMAND_BLOCK, CHISELED_NETHER_BRICKS, CHISELED_POLISHED_BLACKSTONE, CHISELED_QUARTZ_BLOCK, CHISELED_RED_SANDSTONE, CHISELED_SANDSTONE, CHISELED_STONE_BRICKS, CLAY, COAL_BLOCK, COAL_ORE, COARSE_DIRT, COBBLESTONE, COMMAND_BLOCK, CRACKED_NETHER_BRICKS, CRACKED_POLISHED_BLACKSTONE_BRICKS, CRACKED_STONE_BRICKS, CRAFTING_TABLE, CRIMSON_HYPHAE, CRIMSON_NYLIUM, CRIMSON_PLANKS, CRIMSON_STEM, CRYING_OBSIDIAN, CUT_RED_SANDSTONE, CUT_SANDSTONE, CYAN_CONCRETE, CYAN_CONCRETE_POWDER, CYAN_GLAZED_TERRACOTTA, CYAN_SHULKER_BOX, CYAN_TERRACOTTA, CYAN_WOOL, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_WOOD, DARK_PRISMARINE, DEAD_BRAIN_CORAL_BLOCK, DEAD_BUBBLE_CORAL_BLOCK, DEAD_FIRE_CORAL_BLOCK, DEAD_HORN_CORAL_BLOCK, DEAD_TUBE_CORAL_BLOCK, DIAMOND_BLOCK, DIAMOND_ORE, DIORITE, DIRT, DISPENSER, DRIED_KELP_BLOCK, DROPPER, EMERALD_BLOCK, EMERALD_ORE, END_STONE, END_STONE_BRICKS, FIRE_CORAL_BLOCK, FLETCHING_TABLE, FURNACE, GILDED_BLACKSTONE, GOLD_BLOCK, GOLD_ORE, GRANITE, GRASS_BLOCK, GRAVEL, GRAY_CONCRETE, GRAY_CONCRETE_POWDER, GRAY_GLAZED_TERRACOTTA, GRAY_SHULKER_BOX, GRAY_TERRACOTTA, GRAY_WOOL, GREEN_CONCRETE, GREEN_CONCRETE_POWDER, GREEN_GLAZED_TERRACOTTA, GREEN_SHULKER_BOX, GREEN_TERRACOTTA, GREEN_WOOL, HAY_BLOCK, HONEYCOMB_BLOCK, HORN_CORAL_BLOCK, INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_CRACKED_STONE_BRICKS, INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE, INFESTED_STONE_BRICKS, IRON_BLOCK, IRON_ORE, JACK_O_LANTERN, JIGSAW, JUKEBOX, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_WOOD, LAPIS_BLOCK, LAPIS_ORE, LIGHT_BLUE_CONCRETE, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_BLUE_SHULKER_BOX, LIGHT_BLUE_TERRACOTTA, LIGHT_BLUE_WOOL, LIGHT_GRAY_CONCRETE, LIGHT_GRAY_CONCRETE_POWDER, LIGHT_GRAY_GLAZED_TERRACOTTA, LIGHT_GRAY_SHULKER_BOX, LIGHT_GRAY_TERRACOTTA, LIGHT_GRAY_WOOL, LIME_CONCRETE, LIME_CONCRETE_POWDER, LIME_GLAZED_TERRACOTTA, LIME_SHULKER_BOX, LIME_TERRACOTTA, LIME_WOOL, LODESTONE, LOOM, MAGENTA_CONCRETE, MAGENTA_CONCRETE_POWDER, MAGENTA_GLAZED_TERRACOTTA, MAGENTA_SHULKER_BOX, MAGENTA_TERRACOTTA, MAGENTA_WOOL, MAGMA_BLOCK, MELON, MOSSY_COBBLESTONE, MOSSY_STONE_BRICKS, MUSHROOM_STEM, MYCELIUM, NETHERITE_BLOCK, NETHERRACK, NETHER_BRICKS, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE, NETHER_WART_BLOCK, NOTE_BLOCK, OAK_LOG, OAK_PLANKS, OAK_WOOD, OBSIDIAN, ORANGE_CONCRETE, ORANGE_CONCRETE_POWDER, ORANGE_GLAZED_TERRACOTTA, ORANGE_SHULKER_BOX, ORANGE_TERRACOTTA, ORANGE_WOOL, PACKED_ICE, PINK_CONCRETE, PINK_CONCRETE_POWDER, PINK_GLAZED_TERRACOTTA, PINK_SHULKER_BOX, PINK_TERRACOTTA, PINK_WOOL, PODZOL, POLISHED_ANDESITE, POLISHED_BASALT, POLISHED_BLACKSTONE, POLISHED_BLACKSTONE_BRICKS, POLISHED_DIORITE, POLISHED_GRANITE, PRISMARINE, PRISMARINE_BRICKS, PUMPKIN, PURPLE_CONCRETE, PURPLE_CONCRETE_POWDER, PURPLE_GLAZED_TERRACOTTA, PURPLE_SHULKER_BOX, PURPLE_TERRACOTTA, PURPLE_WOOL, PURPUR_BLOCK, PURPUR_PILLAR, QUARTZ_BLOCK, QUARTZ_BRICKS, QUARTZ_PILLAR, REDSTONE_LAMP, REDSTONE_ORE, RED_CONCRETE, RED_CONCRETE_POWDER, RED_GLAZED_TERRACOTTA, RED_MUSHROOM_BLOCK, RED_NETHER_BRICKS, RED_SAND, RED_SANDSTONE, RED_SHULKER_BOX, RED_TERRACOTTA, RED_WOOL, REPEATING_COMMAND_BLOCK, RESPAWN_ANCHOR, SAND, SANDSTONE, SHROOMLIGHT, SHULKER_BOX, SLIME_BLOCK, SMITHING_TABLE, SMOKER, SMOOTH_QUARTZ, SMOOTH_RED_SANDSTONE, SMOOTH_SANDSTONE, SMOOTH_STONE, SNOW_BLOCK, SOUL_SAND, SOUL_SOIL, SPAWNER, SPONGE, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_WOOD, STONE, STONE_BRICKS, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_CRIMSON_HYPHAE, STRIPPED_CRIMSON_STEM, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, STRIPPED_WARPED_HYPHAE, STRIPPED_WARPED_STEM, STRUCTURE_BLOCK, TARGET, TERRACOTTA, TUBE_CORAL_BLOCK, WARPED_HYPHAE, WARPED_NYLIUM, WARPED_PLANKS, WARPED_STEM, WARPED_WART_BLOCK, WET_SPONGE, WHITE_CONCRETE, WHITE_CONCRETE_POWDER, WHITE_GLAZED_TERRACOTTA, WHITE_SHULKER_BOX, WHITE_TERRACOTTA, WHITE_WOOL, YELLOW_CONCRETE, YELLOW_CONCRETE_POWDER, YELLOW_GLAZED_TERRACOTTA, YELLOW_SHULKER_BOX, YELLOW_TERRACOTTA, YELLOW_WOOL, LEGACY_STONE, LEGACY_GRASS, LEGACY_DIRT, LEGACY_COBBLESTONE, LEGACY_WOOD, LEGACY_BEDROCK, LEGACY_SAND, LEGACY_GRAVEL, LEGACY_GOLD_ORE, LEGACY_IRON_ORE, LEGACY_COAL_ORE, LEGACY_LOG, LEGACY_SPONGE, LEGACY_LAPIS_ORE, LEGACY_LAPIS_BLOCK, LEGACY_DISPENSER, LEGACY_SANDSTONE, LEGACY_NOTE_BLOCK, LEGACY_WOOL, LEGACY_GOLD_BLOCK, LEGACY_IRON_BLOCK, LEGACY_DOUBLE_STEP, LEGACY_BRICK, LEGACY_BOOKSHELF, LEGACY_MOSSY_COBBLESTONE, LEGACY_OBSIDIAN, LEGACY_MOB_SPAWNER, LEGACY_DIAMOND_ORE, LEGACY_DIAMOND_BLOCK, LEGACY_WORKBENCH, LEGACY_FURNACE, LEGACY_BURNING_FURNACE, LEGACY_REDSTONE_ORE, LEGACY_GLOWING_REDSTONE_ORE, LEGACY_SNOW_BLOCK, LEGACY_CLAY, LEGACY_JUKEBOX, LEGACY_PUMPKIN, LEGACY_NETHERRACK, LEGACY_SOUL_SAND, LEGACY_JACK_O_LANTERN, LEGACY_MONSTER_EGGS, LEGACY_SMOOTH_BRICK, LEGACY_HUGE_MUSHROOM_1, LEGACY_HUGE_MUSHROOM_2, LEGACY_MELON_BLOCK, LEGACY_MYCEL, LEGACY_NETHER_BRICK, LEGACY_ENDER_STONE, LEGACY_REDSTONE_LAMP_OFF, LEGACY_REDSTONE_LAMP_ON, LEGACY_WOOD_DOUBLE_STEP, LEGACY_EMERALD_ORE, LEGACY_EMERALD_BLOCK, LEGACY_COMMAND, LEGACY_QUARTZ_ORE, LEGACY_QUARTZ_BLOCK, LEGACY_DROPPER, LEGACY_STAINED_CLAY, LEGACY_HAY_BLOCK, LEGACY_HARD_CLAY, LEGACY_COAL_BLOCK, LEGACY_LOG_2, LEGACY_PACKED_ICE, LEGACY_SLIME_BLOCK, LEGACY_BARRIER, LEGACY_PRISMARINE, LEGACY_RED_SANDSTONE, LEGACY_DOUBLE_STONE_SLAB2, LEGACY_PURPUR_BLOCK, LEGACY_PURPUR_PILLAR, LEGACY_PURPUR_DOUBLE_SLAB, LEGACY_END_BRICKS, LEGACY_STRUCTURE_BLOCK, LEGACY_COMMAND_REPEATING, LEGACY_COMMAND_CHAIN, LEGACY_MAGMA, LEGACY_NETHER_WART_BLOCK, LEGACY_RED_NETHER_BRICK, LEGACY_BONE_BLOCK, LEGACY_WHITE_GLAZED_TERRACOTTA, LEGACY_ORANGE_GLAZED_TERRACOTTA, LEGACY_MAGENTA_GLAZED_TERRACOTTA, LEGACY_LIGHT_BLUE_GLAZED_TERRACOTTA, LEGACY_YELLOW_GLAZED_TERRACOTTA, LEGACY_LIME_GLAZED_TERRACOTTA, LEGACY_PINK_GLAZED_TERRACOTTA, LEGACY_GRAY_GLAZED_TERRACOTTA, LEGACY_SILVER_GLAZED_TERRACOTTA, LEGACY_CYAN_GLAZED_TERRACOTTA, LEGACY_PURPLE_GLAZED_TERRACOTTA, LEGACY_BLUE_GLAZED_TERRACOTTA, LEGACY_BROWN_GLAZED_TERRACOTTA, LEGACY_GREEN_GLAZED_TERRACOTTA, LEGACY_RED_GLAZED_TERRACOTTA, LEGACY_BLACK_GLAZED_TERRACOTTA, LEGACY_CONCRETE, LEGACY_CONCRETE_POWDER ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * @return True if this material is affected by gravity.
	 */
	public boolean hasGravity() {
		if (!isBlock()) {
			return false;
		}
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="hasGravity">
			// ----- Legacy Separator -----
			case ANVIL, BLACK_CONCRETE_POWDER, BLUE_CONCRETE_POWDER, BROWN_CONCRETE_POWDER, CHIPPED_ANVIL, CYAN_CONCRETE_POWDER, DAMAGED_ANVIL, DRAGON_EGG, GRAVEL, GRAY_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, LIME_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, ORANGE_CONCRETE_POWDER, PINK_CONCRETE_POWDER, PURPLE_CONCRETE_POWDER, RED_CONCRETE_POWDER, RED_SAND, SAND, WHITE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER, LEGACY_SAND, LEGACY_GRAVEL, LEGACY_ANVIL, LEGACY_CONCRETE_POWDER ->
				//</editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Checks if this Material is an obtainable item.
	 *
	 * @return true if this material is an item
	 */
	public boolean isItem() {
		return switch (this) {
			//<editor-fold defaultstate="collapsed" desc="isItem">
			// ----- Legacy Separator -----
			case ACACIA_WALL_SIGN, ATTACHED_MELON_STEM, ATTACHED_PUMPKIN_STEM, BAMBOO_SAPLING, BEETROOTS, BIRCH_WALL_SIGN, BLACK_WALL_BANNER, BLUE_WALL_BANNER, BRAIN_CORAL_WALL_FAN, BROWN_WALL_BANNER, BUBBLE_COLUMN, BUBBLE_CORAL_WALL_FAN, CARROTS, CAVE_AIR, COCOA, CREEPER_WALL_HEAD, CRIMSON_WALL_SIGN, CYAN_WALL_BANNER, DARK_OAK_WALL_SIGN, DEAD_BRAIN_CORAL_WALL_FAN, DEAD_BUBBLE_CORAL_WALL_FAN, DEAD_FIRE_CORAL_WALL_FAN, DEAD_HORN_CORAL_WALL_FAN, DEAD_TUBE_CORAL_WALL_FAN, DRAGON_WALL_HEAD, END_GATEWAY, END_PORTAL, FIRE, FIRE_CORAL_WALL_FAN, FROSTED_ICE, GRAY_WALL_BANNER, GREEN_WALL_BANNER, HORN_CORAL_WALL_FAN, JUNGLE_WALL_SIGN, KELP_PLANT, LAVA, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_WALL_BANNER, MAGENTA_WALL_BANNER, MELON_STEM, MOVING_PISTON, NETHER_PORTAL, OAK_WALL_SIGN, ORANGE_WALL_BANNER, PINK_WALL_BANNER, PISTON_HEAD, PLAYER_WALL_HEAD, POTATOES, POTTED_ACACIA_SAPLING, POTTED_ALLIUM, POTTED_AZURE_BLUET, POTTED_BAMBOO, POTTED_BIRCH_SAPLING, POTTED_BLUE_ORCHID, POTTED_BROWN_MUSHROOM, POTTED_CACTUS, POTTED_CORNFLOWER, POTTED_CRIMSON_FUNGUS, POTTED_CRIMSON_ROOTS, POTTED_DANDELION, POTTED_DARK_OAK_SAPLING, POTTED_DEAD_BUSH, POTTED_FERN, POTTED_JUNGLE_SAPLING, POTTED_LILY_OF_THE_VALLEY, POTTED_OAK_SAPLING, POTTED_ORANGE_TULIP, POTTED_OXEYE_DAISY, POTTED_PINK_TULIP, POTTED_POPPY, POTTED_RED_MUSHROOM, POTTED_RED_TULIP, POTTED_SPRUCE_SAPLING, POTTED_WARPED_FUNGUS, POTTED_WARPED_ROOTS, POTTED_WHITE_TULIP, POTTED_WITHER_ROSE, PUMPKIN_STEM, PURPLE_WALL_BANNER, REDSTONE_WALL_TORCH, REDSTONE_WIRE, RED_WALL_BANNER, SKELETON_WALL_SKULL, SOUL_FIRE, SOUL_WALL_TORCH, SPRUCE_WALL_SIGN, SWEET_BERRY_BUSH, TALL_SEAGRASS, TRIPWIRE, TUBE_CORAL_WALL_FAN, TWISTING_VINES_PLANT, VOID_AIR, WALL_TORCH, WARPED_WALL_SIGN, WATER, WEEPING_VINES_PLANT, WHITE_WALL_BANNER, WITHER_SKELETON_WALL_SKULL, YELLOW_WALL_BANNER, ZOMBIE_WALL_HEAD, LEGACY_ACACIA_DOOR, LEGACY_BED_BLOCK, LEGACY_BEETROOT_BLOCK, LEGACY_BIRCH_DOOR, LEGACY_BREWING_STAND, LEGACY_BURNING_FURNACE, LEGACY_CAKE_BLOCK, LEGACY_CARROT, LEGACY_CAULDRON, LEGACY_COCOA, LEGACY_CROPS, LEGACY_DARK_OAK_DOOR, LEGACY_DAYLIGHT_DETECTOR_INVERTED, LEGACY_DIODE_BLOCK_OFF, LEGACY_DIODE_BLOCK_ON, LEGACY_DOUBLE_STEP, LEGACY_DOUBLE_STONE_SLAB2, LEGACY_ENDER_PORTAL, LEGACY_END_GATEWAY, LEGACY_FIRE, LEGACY_FLOWER_POT, LEGACY_FROSTED_ICE, LEGACY_GLOWING_REDSTONE_ORE, LEGACY_IRON_DOOR_BLOCK, LEGACY_JUNGLE_DOOR, LEGACY_LAVA, LEGACY_MELON_STEM, LEGACY_NETHER_WARTS, LEGACY_PISTON_EXTENSION, LEGACY_PISTON_MOVING_PIECE, LEGACY_PORTAL, LEGACY_POTATO, LEGACY_PUMPKIN_STEM, LEGACY_PURPUR_DOUBLE_SLAB, LEGACY_REDSTONE_COMPARATOR_OFF, LEGACY_REDSTONE_COMPARATOR_ON, LEGACY_REDSTONE_LAMP_ON, LEGACY_REDSTONE_TORCH_OFF, LEGACY_REDSTONE_WIRE, LEGACY_SIGN_POST, LEGACY_SKULL, LEGACY_SPRUCE_DOOR, LEGACY_STANDING_BANNER, LEGACY_STATIONARY_LAVA, LEGACY_STATIONARY_WATER, LEGACY_SUGAR_CANE_BLOCK, LEGACY_TRIPWIRE, LEGACY_WALL_BANNER, LEGACY_WALL_SIGN, LEGACY_WATER, LEGACY_WOODEN_DOOR, LEGACY_WOOD_DOUBLE_STEP ->
				//</editor-fold>
				false;
			default -> true;
		};
	}

	/**
	 * Checks if this Material can be interacted with.
	 * <p>
	 * Interactable materials include those with functionality when they are
	 * interacted with by a player such as chests, furnaces, etc.
	 * <p>
	 * Some blocks such as piston heads and stairs are considered interactable
	 * though may not perform any additional functionality.
	 * <p>
	 * Note that the interactability of some materials may be dependant on their
	 * state as well. This method will return true if there is at least one
	 * state in which additional interact handling is performed for the
	 * material.
	 *
	 * @return true if this material can be interacted with.
	 */
	public boolean isInteractable() {
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="isInteractable">
			case ACACIA_BUTTON, ACACIA_DOOR, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_SIGN, ACACIA_STAIRS, ACACIA_TRAPDOOR, ACACIA_WALL_SIGN, ANDESITE_STAIRS, ANVIL, BARREL, BEACON, BEEHIVE, BEE_NEST, BELL, BIRCH_BUTTON, BIRCH_DOOR, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_SIGN, BIRCH_STAIRS, BIRCH_TRAPDOOR, BIRCH_WALL_SIGN, BLACKSTONE_STAIRS, BLACK_BED, BLACK_SHULKER_BOX, BLAST_FURNACE, BLUE_BED, BLUE_SHULKER_BOX, BREWING_STAND, BRICK_STAIRS, BROWN_BED, BROWN_SHULKER_BOX, CAKE, CAMPFIRE, CARTOGRAPHY_TABLE, CAULDRON, CHAIN_COMMAND_BLOCK, CHEST, CHIPPED_ANVIL, COBBLESTONE_STAIRS, COMMAND_BLOCK, COMPARATOR, COMPOSTER, CRAFTING_TABLE, CRIMSON_BUTTON, CRIMSON_DOOR, CRIMSON_FENCE, CRIMSON_FENCE_GATE, CRIMSON_SIGN, CRIMSON_STAIRS, CRIMSON_TRAPDOOR, CRIMSON_WALL_SIGN, CYAN_BED, CYAN_SHULKER_BOX, DAMAGED_ANVIL, DARK_OAK_BUTTON, DARK_OAK_DOOR, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_SIGN, DARK_OAK_STAIRS, DARK_OAK_TRAPDOOR, DARK_OAK_WALL_SIGN, DARK_PRISMARINE_STAIRS, DAYLIGHT_DETECTOR, DIORITE_STAIRS, DISPENSER, DRAGON_EGG, DROPPER, ENCHANTING_TABLE, ENDER_CHEST, END_STONE_BRICK_STAIRS, FLETCHING_TABLE, FLOWER_POT, FURNACE, GRANITE_STAIRS, GRAY_BED, GRAY_SHULKER_BOX, GREEN_BED, GREEN_SHULKER_BOX, GRINDSTONE, HOPPER, IRON_DOOR, IRON_TRAPDOOR, JIGSAW, JUKEBOX, JUNGLE_BUTTON, JUNGLE_DOOR, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_SIGN, JUNGLE_STAIRS, JUNGLE_TRAPDOOR, JUNGLE_WALL_SIGN, LECTERN, LEVER, LIGHT_BLUE_BED, LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_BED, LIGHT_GRAY_SHULKER_BOX, LIME_BED, LIME_SHULKER_BOX, LOOM, MAGENTA_BED, MAGENTA_SHULKER_BOX, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, MOVING_PISTON, NETHER_BRICK_FENCE, NETHER_BRICK_STAIRS, NOTE_BLOCK, OAK_BUTTON, OAK_DOOR, OAK_FENCE, OAK_FENCE_GATE, OAK_SIGN, OAK_STAIRS, OAK_TRAPDOOR, OAK_WALL_SIGN, ORANGE_BED, ORANGE_SHULKER_BOX, PINK_BED, PINK_SHULKER_BOX, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_BUTTON, POLISHED_BLACKSTONE_STAIRS, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE_STAIRS, POTTED_ACACIA_SAPLING, POTTED_ALLIUM, POTTED_AZURE_BLUET, POTTED_BAMBOO, POTTED_BIRCH_SAPLING, POTTED_BLUE_ORCHID, POTTED_BROWN_MUSHROOM, POTTED_CACTUS, POTTED_CORNFLOWER, POTTED_CRIMSON_FUNGUS, POTTED_CRIMSON_ROOTS, POTTED_DANDELION, POTTED_DARK_OAK_SAPLING, POTTED_DEAD_BUSH, POTTED_FERN, POTTED_JUNGLE_SAPLING, POTTED_LILY_OF_THE_VALLEY, POTTED_OAK_SAPLING, POTTED_ORANGE_TULIP, POTTED_OXEYE_DAISY, POTTED_PINK_TULIP, POTTED_POPPY, POTTED_RED_MUSHROOM, POTTED_RED_TULIP, POTTED_SPRUCE_SAPLING, POTTED_WARPED_FUNGUS, POTTED_WARPED_ROOTS, POTTED_WHITE_TULIP, POTTED_WITHER_ROSE, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS, PUMPKIN, PURPLE_BED, PURPLE_SHULKER_BOX, PURPUR_STAIRS, QUARTZ_STAIRS, REDSTONE_ORE, REDSTONE_WIRE, RED_BED, RED_NETHER_BRICK_STAIRS, RED_SANDSTONE_STAIRS, RED_SHULKER_BOX, REPEATER, REPEATING_COMMAND_BLOCK, RESPAWN_ANCHOR, SANDSTONE_STAIRS, SHULKER_BOX, SMITHING_TABLE, SMOKER, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_STAIRS, SOUL_CAMPFIRE, SPRUCE_BUTTON, SPRUCE_DOOR, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_SIGN, SPRUCE_STAIRS, SPRUCE_TRAPDOOR, SPRUCE_WALL_SIGN, STONECUTTER, STONE_BRICK_STAIRS, STONE_BUTTON, STONE_STAIRS, STRUCTURE_BLOCK, SWEET_BERRY_BUSH, TNT, TRAPPED_CHEST, WARPED_BUTTON, WARPED_DOOR, WARPED_FENCE, WARPED_FENCE_GATE, WARPED_SIGN, WARPED_STAIRS, WARPED_TRAPDOOR, WARPED_WALL_SIGN, WHITE_BED, WHITE_SHULKER_BOX, YELLOW_BED, YELLOW_SHULKER_BOX ->
				// </editor-fold>
				true;
			default -> false;
		};
	}

	/**
	 * Obtains the block's hardness level (also known as "strength").
	 * <br>
	 * This number is used to calculate the time required to break each block.
	 * <br>
	 * Only available when {@link #isBlock()} is true.
	 *
	 * @return the hardness of that material.
	 */
	public float getHardness() {
		Validate.isTrue(isBlock(), "The Material is not a block!");
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="getBlockHardness">
			case BARRIER, BEDROCK, CHAIN_COMMAND_BLOCK, COMMAND_BLOCK, END_GATEWAY, END_PORTAL, END_PORTAL_FRAME, JIGSAW, MOVING_PISTON, NETHER_PORTAL, REPEATING_COMMAND_BLOCK, STRUCTURE_BLOCK ->
				-1.0F;
			case BLACK_CARPET, BLUE_CARPET, BROWN_CARPET, CYAN_CARPET, GRAY_CARPET, GREEN_CARPET, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, LIME_CARPET, MAGENTA_CARPET, ORANGE_CARPET, PINK_CARPET, PURPLE_CARPET, RED_CARPET, SNOW, WHITE_CARPET, YELLOW_CARPET ->
				0.1F;
			case ACACIA_LEAVES, BIRCH_LEAVES, BLACK_BED, BLUE_BED, BROWN_BED, BROWN_MUSHROOM_BLOCK, COCOA, CYAN_BED, DARK_OAK_LEAVES, DAYLIGHT_DETECTOR, GRAY_BED, GREEN_BED, JUNGLE_LEAVES, LIGHT_BLUE_BED, LIGHT_GRAY_BED, LIME_BED, MAGENTA_BED, MUSHROOM_STEM, OAK_LEAVES, ORANGE_BED, PINK_BED, PURPLE_BED, RED_BED, RED_MUSHROOM_BLOCK, SNOW_BLOCK, SPRUCE_LEAVES, VINE, WHITE_BED, YELLOW_BED ->
				0.2F;
			case BEE_NEST, BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, BLUE_STAINED_GLASS, BLUE_STAINED_GLASS_PANE, BROWN_STAINED_GLASS, BROWN_STAINED_GLASS_PANE, CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE, GLASS, GLASS_PANE, GLOWSTONE, GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS, GREEN_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE, LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS, ORANGE_STAINED_GLASS_PANE, PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS, PURPLE_STAINED_GLASS_PANE, REDSTONE_LAMP, RED_STAINED_GLASS, RED_STAINED_GLASS_PANE, SEA_LANTERN, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS, YELLOW_STAINED_GLASS_PANE ->
				0.3F;
			case CACTUS, CHORUS_FLOWER, CHORUS_PLANT, CRIMSON_NYLIUM, LADDER, NETHERRACK, WARPED_NYLIUM -> 0.4F;
			case ACACIA_BUTTON, ACACIA_PRESSURE_PLATE, BIRCH_BUTTON, BIRCH_PRESSURE_PLATE, BLACK_CONCRETE_POWDER, BLUE_CONCRETE_POWDER, BREWING_STAND, BROWN_CONCRETE_POWDER, CAKE, COARSE_DIRT, CRIMSON_BUTTON, CRIMSON_PRESSURE_PLATE, CYAN_CONCRETE_POWDER, DARK_OAK_BUTTON, DARK_OAK_PRESSURE_PLATE, DIRT, DRIED_KELP_BLOCK, FROSTED_ICE, GRAY_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, HAY_BLOCK, HEAVY_WEIGHTED_PRESSURE_PLATE, ICE, JUNGLE_BUTTON, JUNGLE_PRESSURE_PLATE, LEVER, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, LIGHT_WEIGHTED_PRESSURE_PLATE, LIME_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, MAGMA_BLOCK, OAK_BUTTON, OAK_PRESSURE_PLATE, ORANGE_CONCRETE_POWDER, PACKED_ICE, PINK_CONCRETE_POWDER, PODZOL, POLISHED_BLACKSTONE_BUTTON, POLISHED_BLACKSTONE_PRESSURE_PLATE, PURPLE_CONCRETE_POWDER, RED_CONCRETE_POWDER, RED_SAND, SAND, SOUL_SAND, SOUL_SOIL, SPRUCE_BUTTON, SPRUCE_PRESSURE_PLATE, STONE_BUTTON, STONE_PRESSURE_PLATE, TARGET, TURTLE_EGG, WARPED_BUTTON, WARPED_PRESSURE_PLATE, WHITE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER ->
				0.5F;
			case BEEHIVE, CLAY, COMPOSTER, FARMLAND, GRASS_BLOCK, GRAVEL, HONEYCOMB_BLOCK, MYCELIUM, SPONGE, WET_SPONGE ->
				0.6F;
			case GRASS_PATH -> 0.65F;
			case ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL, RAIL -> 0.7F;
			case BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CHISELED_QUARTZ_BLOCK, CHISELED_RED_SANDSTONE, CHISELED_SANDSTONE, CUT_RED_SANDSTONE, CUT_SANDSTONE, CYAN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, LIME_WOOL, MAGENTA_WOOL, NOTE_BLOCK, ORANGE_WOOL, PINK_WOOL, PURPLE_WOOL, QUARTZ_BLOCK, QUARTZ_BRICKS, QUARTZ_PILLAR, QUARTZ_STAIRS, RED_SANDSTONE, RED_SANDSTONE_STAIRS, RED_SANDSTONE_WALL, RED_WOOL, SANDSTONE, SANDSTONE_STAIRS, SANDSTONE_WALL, WHITE_WOOL, YELLOW_WOOL ->
				0.8F;
			case ACACIA_SIGN, ACACIA_WALL_SIGN, BAMBOO, BAMBOO_SAPLING, BIRCH_SIGN, BIRCH_WALL_SIGN, BLACK_BANNER, BLACK_WALL_BANNER, BLUE_BANNER, BLUE_WALL_BANNER, BROWN_BANNER, BROWN_WALL_BANNER, CARVED_PUMPKIN, CREEPER_HEAD, CREEPER_WALL_HEAD, CRIMSON_SIGN, CRIMSON_WALL_SIGN, CYAN_BANNER, CYAN_WALL_BANNER, DARK_OAK_SIGN, DARK_OAK_WALL_SIGN, DRAGON_HEAD, DRAGON_WALL_HEAD, GRAY_BANNER, GRAY_WALL_BANNER, GREEN_BANNER, GREEN_WALL_BANNER, JACK_O_LANTERN, JUNGLE_SIGN, JUNGLE_WALL_SIGN, LIGHT_BLUE_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_BANNER, LIME_WALL_BANNER, MAGENTA_BANNER, MAGENTA_WALL_BANNER, MELON, NETHER_WART_BLOCK, OAK_SIGN, OAK_WALL_SIGN, ORANGE_BANNER, ORANGE_WALL_BANNER, PINK_BANNER, PINK_WALL_BANNER, PLAYER_HEAD, PLAYER_WALL_HEAD, PUMPKIN, PURPLE_BANNER, PURPLE_WALL_BANNER, RED_BANNER, RED_WALL_BANNER, SHROOMLIGHT, SKELETON_SKULL, SKELETON_WALL_SKULL, SPRUCE_SIGN, SPRUCE_WALL_SIGN, WARPED_SIGN, WARPED_WALL_SIGN, WARPED_WART_BLOCK, WHITE_BANNER, WHITE_WALL_BANNER, WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL, YELLOW_BANNER, YELLOW_WALL_BANNER, ZOMBIE_HEAD, ZOMBIE_WALL_HEAD ->
				1.0F;
			case BASALT, BLACK_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, CYAN_TERRACOTTA, GRAY_TERRACOTTA, GREEN_TERRACOTTA, LIGHT_BLUE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, LIME_TERRACOTTA, MAGENTA_TERRACOTTA, ORANGE_TERRACOTTA, PINK_TERRACOTTA, POLISHED_BASALT, PURPLE_TERRACOTTA, RED_TERRACOTTA, TERRACOTTA, WHITE_TERRACOTTA, YELLOW_TERRACOTTA ->
				1.25F;
			case BLACK_GLAZED_TERRACOTTA, BLUE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA, RED_GLAZED_TERRACOTTA, WHITE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA ->
				1.4F;
			case ANDESITE, ANDESITE_SLAB, ANDESITE_STAIRS, ANDESITE_WALL, BLACKSTONE, BLACKSTONE_STAIRS, BLACKSTONE_WALL, BOOKSHELF, BRAIN_CORAL_BLOCK, BUBBLE_CORAL_BLOCK, CHISELED_POLISHED_BLACKSTONE, CHISELED_STONE_BRICKS, CRACKED_POLISHED_BLACKSTONE_BRICKS, CRACKED_STONE_BRICKS, DARK_PRISMARINE, DARK_PRISMARINE_SLAB, DARK_PRISMARINE_STAIRS, DEAD_BRAIN_CORAL_BLOCK, DEAD_BUBBLE_CORAL_BLOCK, DEAD_FIRE_CORAL_BLOCK, DEAD_HORN_CORAL_BLOCK, DEAD_TUBE_CORAL_BLOCK, DIORITE, DIORITE_SLAB, DIORITE_STAIRS, DIORITE_WALL, FIRE_CORAL_BLOCK, GILDED_BLACKSTONE, GRANITE, GRANITE_SLAB, GRANITE_STAIRS, GRANITE_WALL, HORN_CORAL_BLOCK, MOSSY_STONE_BRICKS, MOSSY_STONE_BRICK_SLAB, MOSSY_STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_WALL, PISTON, PISTON_HEAD, POLISHED_ANDESITE, POLISHED_ANDESITE_SLAB, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICKS, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_BRICK_WALL, POLISHED_DIORITE, POLISHED_DIORITE_SLAB, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE, POLISHED_GRANITE_SLAB, POLISHED_GRANITE_STAIRS, PRISMARINE, PRISMARINE_BRICKS, PRISMARINE_BRICK_SLAB, PRISMARINE_BRICK_STAIRS, PRISMARINE_SLAB, PRISMARINE_STAIRS, PRISMARINE_WALL, PURPUR_BLOCK, PURPUR_PILLAR, PURPUR_STAIRS, STICKY_PISTON, STONE, STONE_BRICKS, STONE_BRICK_STAIRS, STONE_BRICK_WALL, STONE_STAIRS, TUBE_CORAL_BLOCK ->
				1.5F;
			case BLACK_CONCRETE, BLUE_CONCRETE, BROWN_CONCRETE, CYAN_CONCRETE, GRAY_CONCRETE, GREEN_CONCRETE, LIGHT_BLUE_CONCRETE, LIGHT_GRAY_CONCRETE, LIME_CONCRETE, MAGENTA_CONCRETE, ORANGE_CONCRETE, PINK_CONCRETE, PURPLE_CONCRETE, RED_CONCRETE, WHITE_CONCRETE, YELLOW_CONCRETE ->
				1.8F;
			case ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_LOG, ACACIA_PLANKS, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_WOOD, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_LOG, BIRCH_PLANKS, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_WOOD, BLACKSTONE_SLAB, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BONE_BLOCK, BRICKS, BRICK_SLAB, BRICK_STAIRS, BRICK_WALL, BROWN_SHULKER_BOX, CAMPFIRE, CAULDRON, CHISELED_NETHER_BRICKS, COBBLESTONE, COBBLESTONE_SLAB, COBBLESTONE_STAIRS, COBBLESTONE_WALL, CRACKED_NETHER_BRICKS, CRIMSON_FENCE, CRIMSON_FENCE_GATE, CRIMSON_HYPHAE, CRIMSON_PLANKS, CRIMSON_SLAB, CRIMSON_STAIRS, CRIMSON_STEM, CUT_RED_SANDSTONE_SLAB, CUT_SANDSTONE_SLAB, CYAN_SHULKER_BOX, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_WOOD, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, GRINDSTONE, JUKEBOX, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_LOG, JUNGLE_PLANKS, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_WOOD, LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_SLAB, MOSSY_COBBLESTONE_STAIRS, MOSSY_COBBLESTONE_WALL, NETHER_BRICKS, NETHER_BRICK_FENCE, NETHER_BRICK_SLAB, NETHER_BRICK_STAIRS, NETHER_BRICK_WALL, OAK_FENCE, OAK_FENCE_GATE, OAK_LOG, OAK_PLANKS, OAK_SLAB, OAK_STAIRS, OAK_WOOD, ORANGE_SHULKER_BOX, PETRIFIED_OAK_SLAB, PINK_SHULKER_BOX, POLISHED_BLACKSTONE, POLISHED_BLACKSTONE_BRICK_SLAB, POLISHED_BLACKSTONE_SLAB, POLISHED_BLACKSTONE_STAIRS, POLISHED_BLACKSTONE_WALL, PURPLE_SHULKER_BOX, PURPUR_SLAB, QUARTZ_SLAB, RED_NETHER_BRICKS, RED_NETHER_BRICK_SLAB, RED_NETHER_BRICK_STAIRS, RED_NETHER_BRICK_WALL, RED_SANDSTONE_SLAB, RED_SHULKER_BOX, SANDSTONE_SLAB, SHULKER_BOX, SMOOTH_QUARTZ, SMOOTH_QUARTZ_SLAB, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE, SMOOTH_RED_SANDSTONE_SLAB, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE, SMOOTH_SANDSTONE_SLAB, SMOOTH_SANDSTONE_STAIRS, SMOOTH_STONE, SMOOTH_STONE_SLAB, SOUL_CAMPFIRE, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_LOG, SPRUCE_PLANKS, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_WOOD, STONE_BRICK_SLAB, STONE_SLAB, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_CRIMSON_HYPHAE, STRIPPED_CRIMSON_STEM, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, STRIPPED_WARPED_HYPHAE, STRIPPED_WARPED_STEM, WARPED_FENCE, WARPED_FENCE_GATE, WARPED_HYPHAE, WARPED_PLANKS, WARPED_SLAB, WARPED_STAIRS, WARPED_STEM, WHITE_SHULKER_BOX, YELLOW_SHULKER_BOX ->
				2.0F;
			case BARREL, CARTOGRAPHY_TABLE, CHEST, CRAFTING_TABLE, FLETCHING_TABLE, LECTERN, LOOM, SMITHING_TABLE, TRAPPED_CHEST ->
				2.5F;
			case BLUE_ICE -> 2.8F;
			case ACACIA_DOOR, ACACIA_TRAPDOOR, BEACON, BIRCH_DOOR, BIRCH_TRAPDOOR, COAL_ORE, CONDUIT, CRIMSON_DOOR, CRIMSON_TRAPDOOR, DARK_OAK_DOOR, DARK_OAK_TRAPDOOR, DIAMOND_ORE, DRAGON_EGG, EMERALD_ORE, END_STONE, END_STONE_BRICKS, END_STONE_BRICK_SLAB, END_STONE_BRICK_STAIRS, END_STONE_BRICK_WALL, GOLD_BLOCK, GOLD_ORE, HOPPER, IRON_ORE, JUNGLE_DOOR, JUNGLE_TRAPDOOR, LAPIS_BLOCK, LAPIS_ORE, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE, OAK_DOOR, OAK_TRAPDOOR, OBSERVER, REDSTONE_ORE, SPRUCE_DOOR, SPRUCE_TRAPDOOR, WARPED_DOOR, WARPED_TRAPDOOR ->
				3.0F;
			case BLAST_FURNACE, DISPENSER, DROPPER, FURNACE, LANTERN, LODESTONE, SMOKER, SOUL_LANTERN, STONECUTTER -> 3.5F;
			case COBWEB -> 4.0F;
			case ANVIL, BELL, CHAIN, CHIPPED_ANVIL, COAL_BLOCK, DAMAGED_ANVIL, DIAMOND_BLOCK, EMERALD_BLOCK, ENCHANTING_TABLE, IRON_BARS, IRON_BLOCK, IRON_DOOR, IRON_TRAPDOOR, REDSTONE_BLOCK, SPAWNER ->
				5.0F;
			case ENDER_CHEST -> 22.5F;
			case ANCIENT_DEBRIS -> 30.0F;
			case CRYING_OBSIDIAN, NETHERITE_BLOCK, OBSIDIAN, RESPAWN_ANCHOR -> 50.0F;
			case LAVA, WATER -> 100.0F;
			default -> 0F;
			// </editor-fold>
		};
	}

	/**
	 * Obtains the blast resistance value (also known as block "durability").
	 * <br>
	 * This value is used in explosions to calculate whether a block should be
	 * broken or not.
	 * <br>
	 * Only available when {@link #isBlock()} is true.
	 *
	 * @return the blast resistance of that material.
	 */
	public float getBlastResistance() {
		Validate.isTrue(isBlock(), "The Material is not a block!");
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="getBlastResistance">
			case BLACK_CARPET, BLUE_CARPET, BROWN_CARPET, CYAN_CARPET, GRAY_CARPET, GREEN_CARPET, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, LIME_CARPET, MAGENTA_CARPET, ORANGE_CARPET, PINK_CARPET, PURPLE_CARPET, RED_CARPET, SNOW, WHITE_CARPET, YELLOW_CARPET ->
				0.1F;
			case ACACIA_LEAVES, BIRCH_LEAVES, BLACK_BED, BLUE_BED, BROWN_BED, BROWN_MUSHROOM_BLOCK, CYAN_BED, DARK_OAK_LEAVES, DAYLIGHT_DETECTOR, GRAY_BED, GREEN_BED, JUNGLE_LEAVES, LIGHT_BLUE_BED, LIGHT_GRAY_BED, LIME_BED, MAGENTA_BED, MUSHROOM_STEM, OAK_LEAVES, ORANGE_BED, PINK_BED, PURPLE_BED, RED_BED, RED_MUSHROOM_BLOCK, SNOW_BLOCK, SPRUCE_LEAVES, VINE, WHITE_BED, YELLOW_BED ->
				0.2F;
			case BEE_NEST, BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, BLUE_STAINED_GLASS, BLUE_STAINED_GLASS_PANE, BROWN_STAINED_GLASS, BROWN_STAINED_GLASS_PANE, CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE, GLASS, GLASS_PANE, GLOWSTONE, GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS, GREEN_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE, LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS, ORANGE_STAINED_GLASS_PANE, PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS, PURPLE_STAINED_GLASS_PANE, REDSTONE_LAMP, RED_STAINED_GLASS, RED_STAINED_GLASS_PANE, SEA_LANTERN, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS, YELLOW_STAINED_GLASS_PANE ->
				0.3F;
			case CACTUS, CHORUS_FLOWER, CHORUS_PLANT, CRIMSON_NYLIUM, LADDER, NETHERRACK, WARPED_NYLIUM -> 0.4F;
			case ACACIA_BUTTON, ACACIA_PRESSURE_PLATE, BIRCH_BUTTON, BIRCH_PRESSURE_PLATE, BLACK_CONCRETE_POWDER, BLUE_CONCRETE_POWDER, BREWING_STAND, BROWN_CONCRETE_POWDER, CAKE, COARSE_DIRT, CRIMSON_BUTTON, CRIMSON_PRESSURE_PLATE, CYAN_CONCRETE_POWDER, DARK_OAK_BUTTON, DARK_OAK_PRESSURE_PLATE, DIRT, FROSTED_ICE, GRAY_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, HAY_BLOCK, HEAVY_WEIGHTED_PRESSURE_PLATE, ICE, JUNGLE_BUTTON, JUNGLE_PRESSURE_PLATE, LEVER, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, LIGHT_WEIGHTED_PRESSURE_PLATE, LIME_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, MAGMA_BLOCK, OAK_BUTTON, OAK_PRESSURE_PLATE, ORANGE_CONCRETE_POWDER, PACKED_ICE, PINK_CONCRETE_POWDER, PODZOL, POLISHED_BLACKSTONE_BUTTON, POLISHED_BLACKSTONE_PRESSURE_PLATE, PURPLE_CONCRETE_POWDER, RED_CONCRETE_POWDER, RED_SAND, SAND, SOUL_SAND, SOUL_SOIL, SPRUCE_BUTTON, SPRUCE_PRESSURE_PLATE, STONE_BUTTON, STONE_PRESSURE_PLATE, TARGET, TURTLE_EGG, WARPED_BUTTON, WARPED_PRESSURE_PLATE, WHITE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER ->
				0.5F;
			case BEEHIVE, CLAY, COMPOSTER, FARMLAND, GRASS_BLOCK, GRAVEL, HONEYCOMB_BLOCK, MYCELIUM, SPONGE, WET_SPONGE ->
				0.6F;
			case GRASS_PATH -> 0.65F;
			case ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL, RAIL -> 0.7F;
			case INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_CRACKED_STONE_BRICKS, INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE, INFESTED_STONE_BRICKS ->
				0.75F;
			case BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CHISELED_QUARTZ_BLOCK, CHISELED_RED_SANDSTONE, CHISELED_SANDSTONE, CUT_RED_SANDSTONE, CUT_SANDSTONE, CYAN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, LIME_WOOL, MAGENTA_WOOL, NOTE_BLOCK, ORANGE_WOOL, PINK_WOOL, PURPLE_WOOL, QUARTZ_BLOCK, QUARTZ_BRICKS, QUARTZ_PILLAR, QUARTZ_STAIRS, RED_SANDSTONE, RED_SANDSTONE_STAIRS, RED_SANDSTONE_WALL, RED_WOOL, SANDSTONE, SANDSTONE_STAIRS, SANDSTONE_WALL, WHITE_WOOL, YELLOW_WOOL ->
				0.8F;
			case ACACIA_SIGN, ACACIA_WALL_SIGN, BAMBOO, BAMBOO_SAPLING, BIRCH_SIGN, BIRCH_WALL_SIGN, BLACK_BANNER, BLACK_WALL_BANNER, BLUE_BANNER, BLUE_WALL_BANNER, BROWN_BANNER, BROWN_WALL_BANNER, CARVED_PUMPKIN, CREEPER_HEAD, CREEPER_WALL_HEAD, CRIMSON_SIGN, CRIMSON_WALL_SIGN, CYAN_BANNER, CYAN_WALL_BANNER, DARK_OAK_SIGN, DARK_OAK_WALL_SIGN, DRAGON_HEAD, DRAGON_WALL_HEAD, GRAY_BANNER, GRAY_WALL_BANNER, GREEN_BANNER, GREEN_WALL_BANNER, JACK_O_LANTERN, JUNGLE_SIGN, JUNGLE_WALL_SIGN, LIGHT_BLUE_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_BANNER, LIME_WALL_BANNER, MAGENTA_BANNER, MAGENTA_WALL_BANNER, MELON, NETHER_WART_BLOCK, OAK_SIGN, OAK_WALL_SIGN, ORANGE_BANNER, ORANGE_WALL_BANNER, PINK_BANNER, PINK_WALL_BANNER, PLAYER_HEAD, PLAYER_WALL_HEAD, PUMPKIN, PURPLE_BANNER, PURPLE_WALL_BANNER, RED_BANNER, RED_WALL_BANNER, SHROOMLIGHT, SKELETON_SKULL, SKELETON_WALL_SKULL, SPRUCE_SIGN, SPRUCE_WALL_SIGN, WARPED_SIGN, WARPED_WALL_SIGN, WARPED_WART_BLOCK, WHITE_BANNER, WHITE_WALL_BANNER, WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL, YELLOW_BANNER, YELLOW_WALL_BANNER, ZOMBIE_HEAD, ZOMBIE_WALL_HEAD ->
				1.0F;
			case BLACK_GLAZED_TERRACOTTA, BLUE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA, RED_GLAZED_TERRACOTTA, WHITE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA ->
				1.4F;
			case BOOKSHELF, PISTON, PISTON_HEAD, STICKY_PISTON -> 1.5F;
			case BLACK_CONCRETE, BLUE_CONCRETE, BROWN_CONCRETE, CYAN_CONCRETE, GRAY_CONCRETE, GREEN_CONCRETE, LIGHT_BLUE_CONCRETE, LIGHT_GRAY_CONCRETE, LIME_CONCRETE, MAGENTA_CONCRETE, ORANGE_CONCRETE, PINK_CONCRETE, PURPLE_CONCRETE, RED_CONCRETE, WHITE_CONCRETE, YELLOW_CONCRETE ->
				1.8F;
			case ACACIA_LOG, ACACIA_WOOD, BIRCH_LOG, BIRCH_WOOD, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BONE_BLOCK, BROWN_SHULKER_BOX, CAMPFIRE, CAULDRON, CRIMSON_HYPHAE, CRIMSON_STEM, CYAN_SHULKER_BOX, DARK_OAK_LOG, DARK_OAK_WOOD, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, JUNGLE_LOG, JUNGLE_WOOD, LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, OAK_LOG, OAK_WOOD, ORANGE_SHULKER_BOX, PINK_SHULKER_BOX, PURPLE_SHULKER_BOX, RED_SHULKER_BOX, SHULKER_BOX, SOUL_CAMPFIRE, SPRUCE_LOG, SPRUCE_WOOD, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_CRIMSON_HYPHAE, STRIPPED_CRIMSON_STEM, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, STRIPPED_WARPED_HYPHAE, STRIPPED_WARPED_STEM, WARPED_HYPHAE, WARPED_STEM, WHITE_SHULKER_BOX, YELLOW_SHULKER_BOX ->
				2.0F;
			case BARREL, CARTOGRAPHY_TABLE, CHEST, CRAFTING_TABLE, DRIED_KELP_BLOCK, FLETCHING_TABLE, LECTERN, LOOM, SMITHING_TABLE, TRAPPED_CHEST ->
				2.5F;
			case BLUE_ICE -> 2.8F;
			case ACACIA_DOOR, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_PLANKS, ACACIA_SLAB, ACACIA_STAIRS, ACACIA_TRAPDOOR, BEACON, BIRCH_DOOR, BIRCH_FENCE, BIRCH_FENCE_GATE, BIRCH_PLANKS, BIRCH_SLAB, BIRCH_STAIRS, BIRCH_TRAPDOOR, COAL_ORE, COCOA, CONDUIT, CRIMSON_DOOR, CRIMSON_FENCE, CRIMSON_FENCE_GATE, CRIMSON_PLANKS, CRIMSON_SLAB, CRIMSON_STAIRS, CRIMSON_TRAPDOOR, DARK_OAK_DOOR, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_PLANKS, DARK_OAK_SLAB, DARK_OAK_STAIRS, DARK_OAK_TRAPDOOR, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, IRON_ORE, JUNGLE_DOOR, JUNGLE_FENCE, JUNGLE_FENCE_GATE, JUNGLE_PLANKS, JUNGLE_SLAB, JUNGLE_STAIRS, JUNGLE_TRAPDOOR, LAPIS_BLOCK, LAPIS_ORE, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE, OAK_DOOR, OAK_FENCE, OAK_FENCE_GATE, OAK_PLANKS, OAK_SLAB, OAK_STAIRS, OAK_TRAPDOOR, OBSERVER, REDSTONE_ORE, SPRUCE_DOOR, SPRUCE_FENCE, SPRUCE_FENCE_GATE, SPRUCE_PLANKS, SPRUCE_SLAB, SPRUCE_STAIRS, SPRUCE_TRAPDOOR, WARPED_DOOR, WARPED_FENCE, WARPED_FENCE_GATE, WARPED_PLANKS, WARPED_SLAB, WARPED_STAIRS, WARPED_TRAPDOOR ->
				3.0F;
			case BLAST_FURNACE, DISPENSER, DROPPER, FURNACE, LANTERN, LODESTONE, SMOKER, SOUL_LANTERN, STONECUTTER -> 3.5F;
			case COBWEB -> 4.0F;
			case BASALT, BLACK_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, CYAN_TERRACOTTA, GRAY_TERRACOTTA, GREEN_TERRACOTTA, LIGHT_BLUE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, LIME_TERRACOTTA, MAGENTA_TERRACOTTA, ORANGE_TERRACOTTA, PINK_TERRACOTTA, POLISHED_BASALT, PURPLE_TERRACOTTA, RED_TERRACOTTA, TERRACOTTA, WHITE_TERRACOTTA, YELLOW_TERRACOTTA ->
				4.2F;
			case HOPPER -> 4.8F;
			case BELL, IRON_DOOR, IRON_TRAPDOOR, SPAWNER -> 5.0F;
			case ANDESITE, ANDESITE_SLAB, ANDESITE_STAIRS, ANDESITE_WALL, BLACKSTONE, BLACKSTONE_SLAB, BLACKSTONE_STAIRS, BLACKSTONE_WALL, BRAIN_CORAL_BLOCK, BRICKS, BRICK_SLAB, BRICK_STAIRS, BRICK_WALL, BUBBLE_CORAL_BLOCK, CHAIN, CHISELED_NETHER_BRICKS, CHISELED_POLISHED_BLACKSTONE, CHISELED_STONE_BRICKS, COAL_BLOCK, COBBLESTONE, COBBLESTONE_SLAB, COBBLESTONE_STAIRS, COBBLESTONE_WALL, CRACKED_NETHER_BRICKS, CRACKED_POLISHED_BLACKSTONE_BRICKS, CRACKED_STONE_BRICKS, CUT_RED_SANDSTONE_SLAB, CUT_SANDSTONE_SLAB, DARK_PRISMARINE, DARK_PRISMARINE_SLAB, DARK_PRISMARINE_STAIRS, DEAD_BRAIN_CORAL_BLOCK, DEAD_BUBBLE_CORAL_BLOCK, DEAD_FIRE_CORAL_BLOCK, DEAD_HORN_CORAL_BLOCK, DEAD_TUBE_CORAL_BLOCK, DIAMOND_BLOCK, DIORITE, DIORITE_SLAB, DIORITE_STAIRS, DIORITE_WALL, EMERALD_BLOCK, FIRE_CORAL_BLOCK, GILDED_BLACKSTONE, GOLD_BLOCK, GRANITE, GRANITE_SLAB, GRANITE_STAIRS, GRANITE_WALL, GRINDSTONE, HORN_CORAL_BLOCK, IRON_BARS, IRON_BLOCK, JUKEBOX, MOSSY_COBBLESTONE, MOSSY_COBBLESTONE_SLAB, MOSSY_COBBLESTONE_STAIRS, MOSSY_COBBLESTONE_WALL, MOSSY_STONE_BRICKS, MOSSY_STONE_BRICK_SLAB, MOSSY_STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_WALL, NETHER_BRICKS, NETHER_BRICK_FENCE, NETHER_BRICK_SLAB, NETHER_BRICK_STAIRS, NETHER_BRICK_WALL, PETRIFIED_OAK_SLAB, POLISHED_ANDESITE, POLISHED_ANDESITE_SLAB, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE, POLISHED_BLACKSTONE_BRICKS, POLISHED_BLACKSTONE_BRICK_SLAB, POLISHED_BLACKSTONE_BRICK_STAIRS, POLISHED_BLACKSTONE_BRICK_WALL, POLISHED_BLACKSTONE_SLAB, POLISHED_BLACKSTONE_STAIRS, POLISHED_BLACKSTONE_WALL, POLISHED_DIORITE, POLISHED_DIORITE_SLAB, POLISHED_DIORITE_STAIRS, POLISHED_GRANITE, POLISHED_GRANITE_SLAB, POLISHED_GRANITE_STAIRS, PRISMARINE, PRISMARINE_BRICKS, PRISMARINE_BRICK_SLAB, PRISMARINE_BRICK_STAIRS, PRISMARINE_SLAB, PRISMARINE_STAIRS, PRISMARINE_WALL, PURPUR_BLOCK, PURPUR_PILLAR, PURPUR_SLAB, PURPUR_STAIRS, QUARTZ_SLAB, REDSTONE_BLOCK, RED_NETHER_BRICKS, RED_NETHER_BRICK_SLAB, RED_NETHER_BRICK_STAIRS, RED_NETHER_BRICK_WALL, RED_SANDSTONE_SLAB, SANDSTONE_SLAB, SMOOTH_QUARTZ, SMOOTH_QUARTZ_SLAB, SMOOTH_QUARTZ_STAIRS, SMOOTH_RED_SANDSTONE, SMOOTH_RED_SANDSTONE_SLAB, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE, SMOOTH_SANDSTONE_SLAB, SMOOTH_SANDSTONE_STAIRS, SMOOTH_STONE, SMOOTH_STONE_SLAB, STONE, STONE_BRICKS, STONE_BRICK_SLAB, STONE_BRICK_STAIRS, STONE_BRICK_WALL, STONE_SLAB, STONE_STAIRS, TUBE_CORAL_BLOCK ->
				6.0F;
			case DRAGON_EGG, END_STONE, END_STONE_BRICKS, END_STONE_BRICK_SLAB, END_STONE_BRICK_STAIRS, END_STONE_BRICK_WALL ->
				9.0F;
			case LAVA, WATER -> 100.0F;
			case ENDER_CHEST -> 600.0F;
			case ANCIENT_DEBRIS, ANVIL, CHIPPED_ANVIL, CRYING_OBSIDIAN, DAMAGED_ANVIL, ENCHANTING_TABLE, NETHERITE_BLOCK, OBSIDIAN, RESPAWN_ANCHOR ->
				1200.0F;
			case BEDROCK, CHAIN_COMMAND_BLOCK, COMMAND_BLOCK, END_GATEWAY, END_PORTAL, END_PORTAL_FRAME, JIGSAW, REPEATING_COMMAND_BLOCK, STRUCTURE_BLOCK ->
				3600000.0F;
			case BARRIER -> 3600000.8F;
			default -> 0;
			// </editor-fold>
		};
	}

	/**
	 * Determines the remaining item in a crafting grid after crafting with this
	 * ingredient.
	 * <br>
	 * Only available when {@link #isItem()} is true.
	 *
	 * @return the item left behind when crafting, or null if nothing is.
	 */
	@Nullable
	public Material getCraftingRemainingItem() {
		Validate.isTrue(isItem(), "The Material is not an item!");
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="getCraftingRemainingItem">
			case WATER_BUCKET, LAVA_BUCKET, MILK_BUCKET -> BUCKET;
			case DRAGON_BREATH, HONEY_BOTTLE -> GLASS_BOTTLE;
			default -> null;
			// </editor-fold>
		};
	}

	/**
	 * Get the best suitable slot for this Material.
	 * <p>
	 * For most items this will be {@link EquipmentSlot#HAND}.
	 *
	 * @return the best EquipmentSlot for this Material
	 */
	@NotNull
	public EquipmentSlot getEquipmentSlot() {
		Validate.isTrue(isItem(), "The Material is not an item!");
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="getEquipmentSlot">
			case CARVED_PUMPKIN, CHAINMAIL_HELMET, CREEPER_HEAD, DIAMOND_HELMET, DRAGON_HEAD, GOLDEN_HELMET, IRON_HELMET, LEATHER_HELMET, NETHERITE_HELMET, PLAYER_HEAD, SKELETON_SKULL, TURTLE_HELMET, WITHER_SKELETON_SKULL, ZOMBIE_HEAD ->
				EquipmentSlot.HEAD;
			case CHAINMAIL_CHESTPLATE, DIAMOND_CHESTPLATE, ELYTRA, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE, NETHERITE_CHESTPLATE ->
				EquipmentSlot.CHEST;
			case CHAINMAIL_LEGGINGS, DIAMOND_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, LEATHER_LEGGINGS, NETHERITE_LEGGINGS ->
				EquipmentSlot.LEGS;
			case CHAINMAIL_BOOTS, DIAMOND_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS, NETHERITE_BOOTS ->
				EquipmentSlot.FEET;
			case SHIELD -> EquipmentSlot.OFF_HAND;
			default -> EquipmentSlot.HAND;
			// </editor-fold>
		};
	}

	// Purpur start
	public boolean isArmor() {
		return switch (this) {
			// <editor-fold defaultstate="collapsed" desc="isarmor">
			case LEATHER_BOOTS,
				LEATHER_CHESTPLATE,
				LEATHER_HELMET,
				LEATHER_LEGGINGS,
				CHAINMAIL_BOOTS,
				CHAINMAIL_CHESTPLATE,
				CHAINMAIL_HELMET,
				CHAINMAIL_LEGGINGS,
				IRON_BOOTS,
				IRON_CHESTPLATE,
				IRON_HELMET,
				IRON_LEGGINGS,
				GOLDEN_BOOTS,
				GOLDEN_CHESTPLATE,
				GOLDEN_HELMET,
				GOLDEN_LEGGINGS,
				DIAMOND_BOOTS,
				DIAMOND_CHESTPLATE,
				DIAMOND_HELMET,
				DIAMOND_LEGGINGS,
				NETHERITE_BOOTS,
				NETHERITE_CHESTPLATE,
				NETHERITE_HELMET,
				NETHERITE_LEGGINGS,
				TURTLE_HELMET -> true;
			default -> false;
		};
	}
	// Purpur end
}
