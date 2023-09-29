package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPropertyJigsawOrientation;
import net.minecraft.core.EnumDirection;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemMonsterEgg;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.BlockPropertyAttachPosition;
import net.minecraft.world.level.block.state.properties.BlockPropertyBambooSize;
import net.minecraft.world.level.block.state.properties.BlockPropertyBellAttach;
import net.minecraft.world.level.block.state.properties.BlockPropertyComparatorMode;
import net.minecraft.world.level.block.state.properties.BlockPropertyDoorHinge;
import net.minecraft.world.level.block.state.properties.BlockPropertyDoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BlockPropertyHalf;
import net.minecraft.world.level.block.state.properties.BlockPropertyPistonType;
import net.minecraft.world.level.block.state.properties.BlockPropertyRedstoneSide;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.BlockPropertyStairsShape;
import net.minecraft.world.level.block.state.properties.BlockPropertyTrackPosition;
import net.minecraft.world.level.block.state.properties.BlockPropertyWallHeight;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.block.state.properties.IBlockState;

public class ii {

	private final Consumer<il> a;
	private final BiConsumer<MinecraftKey, Supplier<JsonElement>> b;
	private final Consumer<Item> c___;

	public ii(Consumer<il> consumer, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer, Consumer<Item> consumer1) {
		this.a = consumer;
		this.b = biconsumer;
		this.c___ = consumer1;
	}

	private static ip b() {
		return ip.a(BlockProperties.O).a(EnumDirection.EAST, ir.a().a(is.b, is.a_.b)).a(EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c)).a(EnumDirection.WEST, ir.a().a(is.b, is.a_.d)).a(EnumDirection.NORTH, ir.a());
	}

	private static ip c() {
		return ip.a(BlockProperties.O).a(EnumDirection.SOUTH, ir.a()).a(EnumDirection.WEST, ir.a().a(is.b, is.a_.b)).a(EnumDirection.NORTH, ir.a().a(is.b, is.a_.c)).a(EnumDirection.EAST, ir.a().a(is.b, is.a_.d));
	}

	private static ip d() {
		return ip.a(BlockProperties.O).a(EnumDirection.EAST, ir.a()).a(EnumDirection.SOUTH, ir.a().a(is.b, is.a_.b)).a(EnumDirection.WEST, ir.a().a(is.b, is.a_.c)).a(EnumDirection.NORTH, ir.a().a(is.b, is.a_.d));
	}

	private static ip e() {
		return ip.a(BlockProperties.M).a(EnumDirection.DOWN, ir.a().a(is.a, is.a_.b)).a(EnumDirection.UP, ir.a().a(is.a, is.a_.d)).a(EnumDirection.NORTH, ir.a()).a(EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c)).a(EnumDirection.WEST, ir.a().a(is.b, is.a_.d)).a(EnumDirection.EAST, ir.a().a(is.b, is.a_.b));
	}

	private static io d(Block block, MinecraftKey minecraftkey) {
		return io.a(block, a(minecraftkey));
	}

	private static ir[] a(MinecraftKey minecraftkey) {
		return new ir[]{ir.a().a(is.c, minecraftkey), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d)};
	}

	private static io e(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return io.a(block, ir.a().a(is.c, minecraftkey), ir.a().a(is.c, minecraftkey1), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c));
	}

	private static ip a(BlockStateBoolean blockstateboolean, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return ip.a(blockstateboolean).a(true, ir.a().a(is.c, minecraftkey)).a(false, ir.a().a(is.c, minecraftkey1));
	}

	private static il f(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return io.a(block).a(ip.a(BlockProperties.w).a(false, ir.a().a(is.c, minecraftkey)).a(true, ir.a().a(is.c, minecraftkey1))).a(ip.a(BlockProperties.Q, BlockProperties.O).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.EAST, ir.a().a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.WEST, ir.a().a(is.b, is.a_.d)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.NORTH, ir.a()).a(BlockPropertyAttachPosition.WALL, EnumDirection.EAST, ir.a().a(is.b, is.a_.b).a(is.a, is.a_.b).a(is.d, true)).a(BlockPropertyAttachPosition.WALL, EnumDirection.WEST, ir.a().a(is.b, is.a_.d).a(is.a, is.a_.b).a(is.d, true)).a(BlockPropertyAttachPosition.WALL, EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c).a(is.a, is.a_.b).a(is.d, true)).a(BlockPropertyAttachPosition.WALL, EnumDirection.NORTH, ir.a().a(is.a, is.a_.b).a(is.d, true)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.EAST, ir.a().a(is.b, is.a_.d).a(is.a, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.WEST, ir.a().a(is.b, is.a_.b).a(is.a, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.SOUTH, ir.a().a(is.a, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.NORTH, ir.a().a(is.b, is.a_.c).a(is.a, is.a_.c)));
	}

	private static ip.d<EnumDirection, BlockPropertyDoubleBlockHalf, BlockPropertyDoorHinge, Boolean> a(ip.d<EnumDirection, BlockPropertyDoubleBlockHalf, BlockPropertyDoorHinge, Boolean> ip_d, BlockPropertyDoubleBlockHalf blockpropertydoubleblockhalf, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return ip_d.a(EnumDirection.EAST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.SOUTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(EnumDirection.WEST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c)).a(EnumDirection.NORTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d)).a(EnumDirection.EAST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.SOUTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(EnumDirection.WEST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(EnumDirection.NORTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d)).a(EnumDirection.EAST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, true, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(EnumDirection.SOUTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, true, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(EnumDirection.WEST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, true, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d)).a(EnumDirection.NORTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.LEFT, true, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.EAST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, true, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d)).a(EnumDirection.SOUTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, true, ir.a().a(is.c, minecraftkey)).a(EnumDirection.WEST, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, true, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(EnumDirection.NORTH, blockpropertydoubleblockhalf, BlockPropertyDoorHinge.RIGHT, true, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c));
	}

	private static il b(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2, MinecraftKey minecraftkey3) {
		return io.a(block).a(a(a(ip.a(BlockProperties.O, BlockProperties.aa, BlockProperties.aH, BlockProperties.u), BlockPropertyDoubleBlockHalf.LOWER, minecraftkey, minecraftkey1), BlockPropertyDoubleBlockHalf.UPPER, minecraftkey2, minecraftkey3));
	}

	private static il g(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return in.a(block).a(ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.I, true), ir.a().a(is.c, minecraftkey1).a(is.d, true)).a(im.a().a(BlockProperties.J, true), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.K, true), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d).a(is.d, true));
	}

	private static il d(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2) {
		return in.a(block).a(im.a().a(BlockProperties.G, true), ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.T, BlockPropertyWallHeight.LOW), ir.a().a(is.c, minecraftkey1).a(is.d, true)).a(im.a().a(BlockProperties.S, BlockPropertyWallHeight.LOW), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.U, BlockPropertyWallHeight.LOW), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.V, BlockPropertyWallHeight.LOW), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.T, BlockPropertyWallHeight.TALL), ir.a().a(is.c, minecraftkey2).a(is.d, true)).a(im.a().a(BlockProperties.S, BlockPropertyWallHeight.TALL), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.U, BlockPropertyWallHeight.TALL), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.V, BlockPropertyWallHeight.TALL), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d).a(is.d, true));
	}

	private static il c(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2, MinecraftKey minecraftkey3) {
		return io.a(block, ir.a().a(is.d, true)).a(c()).a(ip.a(BlockProperties.q, BlockProperties.u).a(false, false, ir.a().a(is.c, minecraftkey1)).a(true, false, ir.a().a(is.c, minecraftkey3)).a(false, true, ir.a().a(is.c, minecraftkey)).a(true, true, ir.a().a(is.c, minecraftkey2)));
	}

	private static il e(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2) {
		return io.a(block).a(ip.a(BlockProperties.O, BlockProperties.ab, BlockProperties.aL).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.c).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.c).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.c).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.STRAIGHT, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.c).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_RIGHT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.OUTER_LEFT, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.d).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_RIGHT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.d, true)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.d, true)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.c).a(is.d, true)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.b).a(is.d, true)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, BlockPropertyStairsShape.INNER_LEFT, ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c).a(is.b, is.a_.d).a(is.d, true)));
	}

	private static il f(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2) {
		return io.a(block).a(ip.a(BlockProperties.O, BlockProperties.ab, BlockProperties.u).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.c)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.a)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.d)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.c).a(is.b, is.a_.b)));
	}

	private static il g(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2) {
		return io.a(block).a(ip.a(BlockProperties.O, BlockProperties.ab, BlockProperties.u).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, false, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, false, ir.a().a(is.c, minecraftkey)).a(EnumDirection.NORTH, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.SOUTH, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyHalf.BOTTOM, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.SOUTH, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyHalf.TOP, true, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d)));
	}

	private static io e(Block block, MinecraftKey minecraftkey) {
		return io.a(block, ir.a().a(is.c, minecraftkey));
	}

	private static ip f() {
		return ip.a(BlockProperties.F).a(EnumDirection.EnumAxis.Y, ir.a()).a(EnumDirection.EnumAxis.Z, ir.a().a(is.a, is.a_.b)).a(EnumDirection.EnumAxis.X, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.b));
	}

	private static il f(Block block, MinecraftKey minecraftkey) {
		return io.a(block, ir.a().a(is.c, minecraftkey)).a(f());
	}

	private static il h(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return io.a(block).a(ip.a(BlockProperties.F).a(EnumDirection.EnumAxis.Y, ir.a().a(is.c, minecraftkey)).a(EnumDirection.EnumAxis.Z, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.b)).a(EnumDirection.EnumAxis.X, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.b).a(is.b, is.a_.b)));
	}

	private static il i(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		return io.a(block).a(a(BlockProperties.w, minecraftkey1, minecraftkey));
	}

	private static il h(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1, MinecraftKey minecraftkey2) {
		return io.a(block).a(ip.a(BlockProperties.aK).a(BlockPropertySlabType.BOTTOM, ir.a().a(is.c, minecraftkey)).a(BlockPropertySlabType.TOP, ir.a().a(is.c, minecraftkey1)).a(BlockPropertySlabType.DOUBLE, ir.a().a(is.c, minecraftkey2)));
	}

	private static <T extends Comparable<T>> ip a(IBlockState<T> iblockstate, T t0, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		ir ira = ir.a().a(is.c, minecraftkey);
		ir ira1 = ir.a().a(is.c, minecraftkey1);

		return ip.a(iblockstate).a((comparable) -> {
			boolean flag = comparable.compareTo(t0) >= 0;

			return flag ? ira : ira1;
		});
	}

	private static List<ir> a(List<MinecraftKey> list, UnaryOperator<ir> unaryoperator) {
		return list.stream().map((minecraftkey) -> ir.a().a(is.c, minecraftkey)).map(unaryoperator).toList();
	}

	private void a(Block block) {
		this.c___.accept(block.getItem());
	}

	private void c(Block block, MinecraftKey minecraftkey) {
		this.b.accept(iw.a(block.getItem()), new iv(minecraftkey));
	}

	private void a(Item item, MinecraftKey minecraftkey) {
		this.b.accept(iw.a(item), new iv(minecraftkey));
	}

	private void a(Item item) {
		iy.aK.a(iw.a(item), iz_.b(item), this.b);
	}

	private void b(Block block) {
		Item item = block.getItem();

		if (item != Items.AIR) {
			iy.aK.a(iw.a(item), iz_.B(block), this.b);
		}

	}

	private void a(Block block, String s) {
		Item item = block.getItem();

		iy.aK.a(iw.a(item), iz_.j(iz_.a(block, s)), this.b);
	}

	private void c(Block block) {
		MinecraftKey minecraftkey = jb_.a.a(block, this.b);
		MinecraftKey minecraftkey1 = jb_.b.a(block, this.b);

		this.a.accept(e(block, minecraftkey, minecraftkey1));
	}

	private void d(Block block) {
		MinecraftKey minecraftkey = jb_.a.a(block, this.b);

		this.a.accept(d(block, minecraftkey));
	}

	private void g(Block block, MinecraftKey minecraftkey) {
		this.a.accept(f(block, minecraftkey));
	}

	private void a(Block block, jb_.a jb_a) {
		MinecraftKey minecraftkey = jb_a.a(block, this.b);

		this.a.accept(f(block, minecraftkey));
	}

	private void b(Block block, jb_.a jb_a) {
		MinecraftKey minecraftkey = jb_a.a(block, this.b);

		this.a.accept(io.a(block, ir.a().a(is.c, minecraftkey)).a(b()));
	}

	private void a(Block block, jb_.a jb_a, jb_.a jb_a1) {
		MinecraftKey minecraftkey = jb_a.a(block, this.b);
		MinecraftKey minecraftkey1 = jb_a1.a(block, this.b);

		this.a.accept(h(block, minecraftkey, minecraftkey1));
	}

	private MinecraftKey a(Block block, String s, ix ix, Function<MinecraftKey, iz_> function) {
		return ix.a(block, s, function.apply(iz_.a(block, s)), this.b);
	}

	private void e(Block block) {
		this.c(block, jb_.a);
	}

	private void c(Block block, jb_.a jb_a) {
		this.a.accept(e(block, jb_a.a(block, this.b)));
	}

	private void a(Block block, iz_ iz, ix ix) {
		MinecraftKey minecraftkey = ix.a(block, iz, this.b);

		this.a.accept(e(block, minecraftkey));
	}

	private ii.b a(Block block, jb_ jb) {
		return (new ii.b(jb.b())).a(block, jb.a());
	}

	private ii.b d(Block block, jb_.a jb_a) {
		jb_ jb = jb_a.get(block);

		return (new ii.b(jb.b())).a(block, jb.a());
	}

	private ii.b f(Block block) {
		return this.d(block, jb_.a);
	}

	private ii.b a(iz_ iz) {
		return new ii.b(iz);
	}

	private void g(Block block) {
		iz_ izx = iz_.p(block);
		MinecraftKey minecraftkey = iy.o.a(block, izx, this.b);
		MinecraftKey minecraftkey1 = iy.p.a(block, izx, this.b);
		MinecraftKey minecraftkey2 = iy.q.a(block, izx, this.b);
		MinecraftKey minecraftkey3 = iy.r.a(block, izx, this.b);

		this.a(block.getItem());
		this.a.accept(b(block, minecraftkey, minecraftkey1, minecraftkey2, minecraftkey3));
	}

	private void h(Block block) {
		iz_ iz = iz_.b(block);
		MinecraftKey minecraftkey = iy.P.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.Q.a(block, iz, this.b);
		MinecraftKey minecraftkey2 = iy.R.a(block, iz, this.b);

		this.a.accept(f(block, minecraftkey, minecraftkey1, minecraftkey2));
		this.c(block, minecraftkey1);
	}

	private void i(Block block) {
		iz_ iz = iz_.b(block);
		MinecraftKey minecraftkey = iy.M.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.N.a(block, iz, this.b);
		MinecraftKey minecraftkey2 = iy.O.a(block, iz, this.b);

		this.a.accept(g(block, minecraftkey, minecraftkey1, minecraftkey2));
		this.c(block, minecraftkey1);
	}

	private ii.d j(Block block) {
		return new ii.d(iz_.l(block));
	}

	private void k(Block block) {
		this.a(block, block);
	}

	private void a(Block block, Block block1) {
		this.a.accept(e(block, iw.a(block1)));
	}

	private void a(Block block, ii.c ii_c) {
		this.b(block);
		this.b(block, ii_c);
	}

	private void a(Block block, ii.c ii_c, iz_ iz) {
		this.b(block);
		this.b(block, ii_c, iz);
	}

	private void b(Block block, ii.c ii_c) {
		iz_ iz = iz_.c(block);

		this.b(block, ii_c, iz);
	}

	private void b(Block block, ii.c ii_c, iz_ iz) {
		MinecraftKey minecraftkey = ii_c.a().a(block, iz, this.b);

		this.a.accept(e(block, minecraftkey));
	}

	private void a(Block block, Block block1, ii.c ii_c) {
		this.a(block, ii_c);
		iz_ iz = iz_.d(block);
		MinecraftKey minecraftkey = ii_c.b().a(block1, iz, this.b);

		this.a.accept(e(block1, minecraftkey));
	}

	private void b(Block block, Block block1) {
		jb_ jb = jb_.k.get(block);
		MinecraftKey minecraftkey = jb.a(block, this.b);

		this.a.accept(e(block, minecraftkey));
		MinecraftKey minecraftkey1 = iy.ac.a(block1, jb.b(), this.b);

		this.a.accept(io.a(block1, ir.a().a(is.c, minecraftkey1)).a(b()));
		this.b(block);
	}

	private void c(Block block, Block block1) {
		this.a(block.getItem());
		iz_ iz = iz_.g(block);
		iz_ iz1 = iz_.a(block, block1);
		MinecraftKey minecraftkey = iy.ao.a(block1, iz1, this.b);

		this.a.accept(io.a(block1, ir.a().a(is.c, minecraftkey)).a(ip.a(BlockProperties.O).a(EnumDirection.WEST, ir.a()).a(EnumDirection.SOUTH, ir.a().a(is.b, is.a_.d)).a(EnumDirection.NORTH, ir.a().a(is.b, is.a_.b)).a(EnumDirection.EAST, ir.a().a(is.b, is.a_.c))));
		this.a.accept(io.a(block).a(ip.a(BlockProperties.ai).a((integer) -> {
			return ir.a().a(is.c, iy.an[integer].a(block, iz, this.b));
		})));
	}

	private void a(Block block, Block block1, Block block2, Block block3, Block block4, Block block5, Block block6, Block block7) {
		this.a(block, ii.c.b);
		this.a(block1, ii.c.b);
		this.e(block2);
		this.e(block3);
		this.b(block4, block6);
		this.b(block5, block7);
	}

	private void c(Block block, ii.c ii_c) {
		this.a(block, "_top");
		MinecraftKey minecraftkey = this.a(block, "_top", ii_c.a(), iz_::c);
		MinecraftKey minecraftkey1 = this.a(block, "_bottom", ii_c.a(), iz_::c);

		this.j(block, minecraftkey, minecraftkey1);
	}

	private void g() {
		this.a(Blocks.SUNFLOWER, "_front");
		MinecraftKey minecraftkey = iw.a(Blocks.SUNFLOWER, "_top");
		MinecraftKey minecraftkey1 = this.a(Blocks.SUNFLOWER, "_bottom", ii.c.b.a(), iz_::c);

		this.j(Blocks.SUNFLOWER, minecraftkey, minecraftkey1);
	}

	private void h() {
		MinecraftKey minecraftkey = this.a(Blocks.TALL_SEAGRASS, "_top", iy.aE, iz_::a);
		MinecraftKey minecraftkey1 = this.a(Blocks.TALL_SEAGRASS, "_bottom", iy.aE, iz_::a);

		this.j(Blocks.TALL_SEAGRASS, minecraftkey, minecraftkey1);
	}

	private void j(Block block, MinecraftKey minecraftkey, MinecraftKey minecraftkey1) {
		this.a.accept(io.a(block).a(ip.a(BlockProperties.aa).a(BlockPropertyDoubleBlockHalf.LOWER, ir.a().a(is.c, minecraftkey1)).a(BlockPropertyDoubleBlockHalf.UPPER, ir.a().a(is.c, minecraftkey))));
	}

	private void l(Block block) {
		iz_ iz = iz_.e(block);
		iz_ iz1 = iz_.e(iz_.a(block, "_corner"));
		MinecraftKey minecraftkey = iy.W.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.X.a(block, iz1, this.b);
		MinecraftKey minecraftkey2 = iy.Y.a(block, iz, this.b);
		MinecraftKey minecraftkey3 = iy.Z.a(block, iz, this.b);

		this.b(block);
		this.a.accept(io.a(block).a(ip.a(BlockProperties.ac).a(BlockPropertyTrackPosition.NORTH_SOUTH, ir.a().a(is.c, minecraftkey)).a(BlockPropertyTrackPosition.EAST_WEST, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(BlockPropertyTrackPosition.ASCENDING_EAST, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(BlockPropertyTrackPosition.ASCENDING_WEST, ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.b)).a(BlockPropertyTrackPosition.ASCENDING_NORTH, ir.a().a(is.c, minecraftkey2)).a(BlockPropertyTrackPosition.ASCENDING_SOUTH, ir.a().a(is.c, minecraftkey3)).a(BlockPropertyTrackPosition.SOUTH_EAST, ir.a().a(is.c, minecraftkey1)).a(BlockPropertyTrackPosition.SOUTH_WEST, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(BlockPropertyTrackPosition.NORTH_WEST, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(BlockPropertyTrackPosition.NORTH_EAST, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d))));
	}

	private void m(Block block) {
		MinecraftKey minecraftkey = this.a(block, "", iy.W, iz_::e);
		MinecraftKey minecraftkey1 = this.a(block, "", iy.Y, iz_::e);
		MinecraftKey minecraftkey2 = this.a(block, "", iy.Z, iz_::e);
		MinecraftKey minecraftkey3 = this.a(block, "_on", iy.W, iz_::e);
		MinecraftKey minecraftkey4 = this.a(block, "_on", iy.Y, iz_::e);
		MinecraftKey minecraftkey5 = this.a(block, "_on", iy.Z, iz_::e);
		ip ipx = ip.a(BlockProperties.w, BlockProperties.ad).a((obool, blockpropertytrackposition) -> {
			return switch (blockpropertytrackposition) {
				case NORTH_SOUTH -> ir.a().a(is.c, (obool ? minecraftkey3 : minecraftkey));
				case EAST_WEST -> ir.a().a(is.c, (obool ? minecraftkey3 : minecraftkey)).a(is.b, is.a_.b);
				case ASCENDING_EAST -> ir.a().a(is.c, (obool ? minecraftkey4 : minecraftkey1)).a(is.b, is.a_.b);
				case ASCENDING_WEST -> ir.a().a(is.c, (obool ? minecraftkey5 : minecraftkey2)).a(is.b, is.a_.b);
				case ASCENDING_NORTH -> ir.a().a(is.c, (obool ? minecraftkey4 : minecraftkey1));
				case ASCENDING_SOUTH -> ir.a().a(is.c, (obool ? minecraftkey5 : minecraftkey2));
				default -> throw new UnsupportedOperationException("Fix you generator!");
			};
		});

		this.b(block);
		this.a.accept(io.a(block).a(ipx));
	}

	private ii.a a(MinecraftKey minecraftkey, Block block) {
		return new ii.a(minecraftkey, block);
	}

	private ii.a d(Block block, Block block1) {
		return new ii.a(iw.a(block), block1);
	}

	private void a(Block block, Item item) {
		MinecraftKey minecraftkey = iy.F.a(block, iz_.a(item), this.b);

		this.a.accept(e(block, minecraftkey));
	}

	private void h(Block block, MinecraftKey minecraftkey) {
		MinecraftKey minecraftkey1 = iy.F.a(block, iz_.h(minecraftkey), this.b);

		this.a.accept(e(block, minecraftkey1));
	}

	private void e(Block block, Block block1) {
		this.c(block, jb_.a);
		MinecraftKey minecraftkey = jb_.i.get(block).a(block1, this.b);

		this.a.accept(e(block1, minecraftkey));
	}

	private void a(jb_.a jb_a, Block... ablock) {
		int i = ablock.length;

		for (int j = 0; j < i; ++j) {
			Block block = ablock[j];
			MinecraftKey minecraftkey = jb_a.a(block, this.b);

			this.a.accept(d(block, minecraftkey));
		}

	}

	private void b(jb_.a jb_a, Block... ablock) {
		int i = ablock.length;

		for (int j = 0; j < i; ++j) {
			Block block = ablock[j];
			MinecraftKey minecraftkey = jb_a.a(block, this.b);

			this.a.accept(io.a(block, ir.a().a(is.c, minecraftkey)).a(c()));
		}

	}

	private void f(Block block, Block block1) {
		this.e(block);
		iz_ iz = iz_.b(block, block1);
		MinecraftKey minecraftkey = iy.ai.a(block1, iz, this.b);
		MinecraftKey minecraftkey1 = iy.aj.a(block1, iz, this.b);
		MinecraftKey minecraftkey2 = iy.ak.a(block1, iz, this.b);
		MinecraftKey minecraftkey3 = iy.ag.a(block1, iz, this.b);
		MinecraftKey minecraftkey4 = iy.ah.a(block1, iz, this.b);
		Item item = block1.getItem();

		iy.aK.a(iw.a(item), iz_.B(block), this.b);
		this.a.accept(in.a(block1).a(ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.I, true), ir.a().a(is.c, minecraftkey1)).a(im.a().a(BlockProperties.J, true), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.K, true), ir.a().a(is.c, minecraftkey2)).a(im.a().a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.I, false), ir.a().a(is.c, minecraftkey3)).a(im.a().a(BlockProperties.J, false), ir.a().a(is.c, minecraftkey4)).a(im.a().a(BlockProperties.K, false), ir.a().a(is.c, minecraftkey4).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.d)));
	}

	private void n(Block block) {
		iz_ iz = iz_.v(block);
		MinecraftKey minecraftkey = iy.al.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = this.a(block, "_conditional", iy.al, (minecraftkey2) -> {
			return iz.c(ja.i, minecraftkey2);
		});

		this.a.accept(io.a(block).a(a(BlockProperties.c, minecraftkey1, minecraftkey)).a(e()));
	}

	private void o(Block block) {
		MinecraftKey minecraftkey = jb_.m.a(block, this.b);

		this.a.accept(e(block, minecraftkey).a(c()));
	}

	private List<ir> a(int i) {
		String s = "_age" + i;

		return IntStream.range(1, 5).mapToObj((j) -> ir.a().a(is.c, iw.a(Blocks.BAMBOO, j + s))).toList();
	}

	private void i() {
		this.a(Blocks.BAMBOO);
		this.a.accept(in.a(Blocks.BAMBOO).a(im.a().a(BlockProperties.ae, 0), this.a(0)).a(im.a().a(BlockProperties.ae, 1), this.a(1)).a(im.a().a(BlockProperties.aN, BlockPropertyBambooSize.SMALL), ir.a().a(is.c, iw.a(Blocks.BAMBOO, "_small_leaves"))).a(im.a().a(BlockProperties.aN, BlockPropertyBambooSize.LARGE), ir.a().a(is.c, iw.a(Blocks.BAMBOO, "_large_leaves"))));
	}

	private ip j() {
		return ip.a(BlockProperties.M).a(EnumDirection.DOWN, ir.a().a(is.a, is.a_.c)).a(EnumDirection.UP, ir.a()).a(EnumDirection.NORTH, ir.a().a(is.a, is.a_.b)).a(EnumDirection.SOUTH, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.c)).a(EnumDirection.WEST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.d)).a(EnumDirection.EAST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.b));
	}

	private void k() {
		MinecraftKey minecraftkey = iz_.a(Blocks.BARREL, "_top_open");

		this.a.accept(io.a(Blocks.BARREL).a(this.j()).a(ip.a(BlockProperties.u).a(false, ir.a().a(is.c, jb_.e.a(Blocks.BARREL, this.b))).a(true, ir.a().a(is.c, jb_.e.get(Blocks.BARREL).a((iz) -> {
			iz.a(ja.f, minecraftkey);
		}).a(Blocks.BARREL, "_open", this.b)))));
	}

	private void a(Block block, Function<Block, iz_> function) {
		iz_ iz = function.apply(block).b(ja.i, ja.c);
		iz_ iz1 = iz.c(ja.g, iz_.a(block, "_front_honey"));
		MinecraftKey minecraftkey = iy.j.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.j.a(block, "_honey", iz1, this.b);

		this.a.accept(io.a(block).a(b()).a(a(BlockProperties.au, 5, minecraftkey1, minecraftkey)));
	}

	private void a(Block block, IBlockState<Integer> iblockstate, int... aint) {
		if (iblockstate.getValues().size() != aint.length) {
			throw new IllegalArgumentException();
		} else {
			Int2ObjectMap<MinecraftKey> int2objectmap = new Int2ObjectOpenHashMap();
			ip ipa = ip.a(iblockstate).a((integer) -> {
				int i = aint[integer];
				MinecraftKey minecraftkey = int2objectmap.computeIfAbsent(i, (j) -> {
					return this.a(block, "_stage" + i, iy.ap, iz_::g);
				});

				return ir.a().a(is.c, minecraftkey);
			});

			this.a(block.getItem());
			this.a.accept(io.a(block).a(ipa));
		}
	}

	private void l() {
		MinecraftKey minecraftkey = iw.a(Blocks.BELL, "_floor");
		MinecraftKey minecraftkey1 = iw.a(Blocks.BELL, "_ceiling");
		MinecraftKey minecraftkey2 = iw.a(Blocks.BELL, "_wall");
		MinecraftKey minecraftkey3 = iw.a(Blocks.BELL, "_between_walls");

		this.a(Items.rj);
		this.a.accept(io.a(Blocks.BELL).a(ip.a(BlockProperties.O, BlockProperties.R).a(EnumDirection.NORTH, BlockPropertyBellAttach.FLOOR, ir.a().a(is.c, minecraftkey)).a(EnumDirection.SOUTH, BlockPropertyBellAttach.FLOOR, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyBellAttach.FLOOR, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyBellAttach.FLOOR, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyBellAttach.CEILING, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.SOUTH, BlockPropertyBellAttach.CEILING, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(EnumDirection.EAST, BlockPropertyBellAttach.CEILING, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(EnumDirection.WEST, BlockPropertyBellAttach.CEILING, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d)).a(EnumDirection.NORTH, BlockPropertyBellAttach.SINGLE_WALL, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d)).a(EnumDirection.SOUTH, BlockPropertyBellAttach.SINGLE_WALL, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(EnumDirection.EAST, BlockPropertyBellAttach.SINGLE_WALL, ir.a().a(is.c, minecraftkey2)).a(EnumDirection.WEST, BlockPropertyBellAttach.SINGLE_WALL, ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c)).a(EnumDirection.SOUTH, BlockPropertyBellAttach.DOUBLE_WALL, ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.b)).a(EnumDirection.NORTH, BlockPropertyBellAttach.DOUBLE_WALL, ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.d)).a(EnumDirection.EAST, BlockPropertyBellAttach.DOUBLE_WALL, ir.a().a(is.c, minecraftkey3)).a(EnumDirection.WEST, BlockPropertyBellAttach.DOUBLE_WALL, ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.c))));
	}

	private void m() {
		this.a.accept(io.a(Blocks.GRINDSTONE, ir.a().a(is.c, iw.a(Blocks.GRINDSTONE))).a(ip.a(BlockProperties.Q, BlockProperties.O).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.NORTH, ir.a()).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.EAST, ir.a().a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.WEST, ir.a().a(is.b, is.a_.d)).a(BlockPropertyAttachPosition.WALL, EnumDirection.NORTH, ir.a().a(is.a, is.a_.b)).a(BlockPropertyAttachPosition.WALL, EnumDirection.EAST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.WALL, EnumDirection.SOUTH, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.WALL, EnumDirection.WEST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.d)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.SOUTH, ir.a().a(is.a, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.WEST, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.NORTH, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.EAST, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.d))));
	}

	private void e(Block block, jb_.a jb_a) {
		MinecraftKey minecraftkey = jb_a.a(block, this.b);
		MinecraftKey minecraftkey1 = iz_.a(block, "_front_on");
		MinecraftKey minecraftkey2 = jb_a.get(block).a((iz) -> {
			iz.a(ja.g, minecraftkey1);
		}).a(block, "_on", this.b);

		this.a.accept(io.a(block).a(a(BlockProperties.r, minecraftkey2, minecraftkey)).a(b()));
	}

	private void a(Block... ablock) {
		MinecraftKey minecraftkey = iw.a("campfire_off");
		int i = ablock.length;

		for (int j = 0; j < i; ++j) {
			Block block = ablock[j];
			MinecraftKey minecraftkey1 = iy.aw.a(block, iz_.A(block), this.b);

			this.a(block.getItem());
			this.a.accept(io.a(block).a(a(BlockProperties.r, minecraftkey1, minecraftkey)).a(c()));
		}

	}

	private void n() {
		iz_ iz = iz_.a(iz_.C(Blocks.BOOKSHELF), iz_.C(Blocks.OAK_PLANKS));
		MinecraftKey minecraftkey = iy.e.a(Blocks.BOOKSHELF, iz, this.b);

		this.a.accept(e(Blocks.BOOKSHELF, minecraftkey));
	}

	private void o() {
		this.a(Items.REDSTONE);
		this.a.accept(in.a(Blocks.REDSTONE_WIRE).a(im.b(im.a().a(BlockProperties.X, BlockPropertyRedstoneSide.NONE).a(BlockProperties.W, BlockPropertyRedstoneSide.NONE).a(BlockProperties.Y, BlockPropertyRedstoneSide.NONE).a(BlockProperties.Z, BlockPropertyRedstoneSide.NONE), im.a().a(BlockProperties.X, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP).a(BlockProperties.W, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), im.a().a(BlockProperties.W, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP).a(BlockProperties.Y, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), im.a().a(BlockProperties.Y, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP).a(BlockProperties.Z, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), im.a().a(BlockProperties.Z, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP).a(BlockProperties.X, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP)), ir.a().a(is.c, iw.a("redstone_dust_dot"))).a(im.a().a(BlockProperties.X, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_side0"))).a(im.a().a(BlockProperties.Y, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_side_alt0"))).a(im.a().a(BlockProperties.W, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_side_alt1")).a(is.b, is.a_.d)).a(im.a().a(BlockProperties.Z, BlockPropertyRedstoneSide.SIDE, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_side1")).a(is.b, is.a_.d)).a(im.a().a(BlockProperties.X, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_up"))).a(im.a().a(BlockProperties.W, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_up")).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.Y, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_up")).a(is.b, is.a_.c)).a(im.a().a(BlockProperties.Z, BlockPropertyRedstoneSide.UP), ir.a().a(is.c, iw.a("redstone_dust_up")).a(is.b, is.a_.d)));
	}

	private void p() {
		this.a(Items.jV);
		this.a.accept(io.a(Blocks.COMPARATOR).a(c()).a(ip.a(BlockProperties.aG, BlockProperties.w).a(BlockPropertyComparatorMode.COMPARE, false, ir.a().a(is.c, iw.a(Blocks.COMPARATOR))).a(BlockPropertyComparatorMode.COMPARE, true, ir.a().a(is.c, iw.a(Blocks.COMPARATOR, "_on"))).a(BlockPropertyComparatorMode.SUBTRACT, false, ir.a().a(is.c, iw.a(Blocks.COMPARATOR, "_subtract"))).a(BlockPropertyComparatorMode.SUBTRACT, true, ir.a().a(is.c, iw.a(Blocks.COMPARATOR, "_on_subtract")))));
	}

	private void q() {
		iz_ iz = iz_.a(Blocks.SMOOTH_STONE);
		iz_ iz1 = iz_.a(iz_.a(Blocks.SMOOTH_STONE_SLAB, "_side"), iz.a(ja.f));
		MinecraftKey minecraftkey = iy.G.a(Blocks.SMOOTH_STONE_SLAB, iz1, this.b);
		MinecraftKey minecraftkey1 = iy.H.a(Blocks.SMOOTH_STONE_SLAB, iz1, this.b);
		MinecraftKey minecraftkey2 = iy.e.b(Blocks.SMOOTH_STONE_SLAB, "_double", iz1, this.b);

		this.a.accept(h(Blocks.SMOOTH_STONE_SLAB, minecraftkey, minecraftkey1, minecraftkey2));
		this.a.accept(e(Blocks.SMOOTH_STONE, iy.c.a(Blocks.SMOOTH_STONE, iz, this.b)));
	}

	private void r() {
		this.a(Items.nB);
		this.a.accept(in.a(Blocks.BREWING_STAND).a(ir.a().a(is.c, iz_.C(Blocks.BREWING_STAND))).a(im.a().a(BlockProperties.k, true), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_bottle0"))).a(im.a().a(BlockProperties.l, true), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_bottle1"))).a(im.a().a(BlockProperties.m, true), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_bottle2"))).a(im.a().a(BlockProperties.k, false), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_empty0"))).a(im.a().a(BlockProperties.l, false), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_empty1"))).a(im.a().a(BlockProperties.m, false), ir.a().a(is.c, iz_.a(Blocks.BREWING_STAND, "_empty2"))));
	}

	private void p(Block block) {
		MinecraftKey minecraftkey = iy.aJ.a(block, iz_.b(block), this.b);
		MinecraftKey minecraftkey1 = iw.a("mushroom_block_inside");

		this.a.accept(in.a(block).a(im.a().a(BlockProperties.I, true), ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.J, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.K, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.G, true), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.H, true), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.I, false), ir.a().a(is.c, minecraftkey1)).a(im.a().a(BlockProperties.J, false), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b).a(is.d, false)).a(im.a().a(BlockProperties.K, false), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c).a(is.d, false)).a(im.a().a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d).a(is.d, false)).a(im.a().a(BlockProperties.G, false), ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.d).a(is.d, false)).a(im.a().a(BlockProperties.H, false), ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.b).a(is.d, false)));
		this.c(block, jb_.a.a(block, "_inventory", this.b));
	}

	private void s() {
		this.a(Items.mN);
		this.a.accept(io.a(Blocks.CAKE).a(ip.a(BlockProperties.al).a(0, ir.a().a(is.c, iw.a(Blocks.CAKE))).a(1, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice1"))).a(2, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice2"))).a(3, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice3"))).a(4, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice4"))).a(5, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice5"))).a(6, ir.a().a(is.c, iw.a(Blocks.CAKE, "_slice6")))));
	}

	private void t() {
		iz_ iz = (new iz_()).a(ja.c, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_side3")).a(ja.o, iz_.C(Blocks.DARK_OAK_PLANKS)).a(ja.n, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_top")).a(ja.j, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_side3")).a(ja.l, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_side3")).a(ja.k, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_side1")).a(ja.m, iz_.a(Blocks.CARTOGRAPHY_TABLE, "_side2"));

		this.a.accept(e(Blocks.CARTOGRAPHY_TABLE, iy.a.a(Blocks.CARTOGRAPHY_TABLE, iz, this.b)));
	}

	private void u() {
		iz_ iz = (new iz_()).a(ja.c, iz_.a(Blocks.SMITHING_TABLE, "_front")).a(ja.o, iz_.a(Blocks.SMITHING_TABLE, "_bottom")).a(ja.n, iz_.a(Blocks.SMITHING_TABLE, "_top")).a(ja.j, iz_.a(Blocks.SMITHING_TABLE, "_front")).a(ja.k, iz_.a(Blocks.SMITHING_TABLE, "_front")).a(ja.l, iz_.a(Blocks.SMITHING_TABLE, "_side")).a(ja.m, iz_.a(Blocks.SMITHING_TABLE, "_side"));

		this.a.accept(e(Blocks.SMITHING_TABLE, iy.a.a(Blocks.SMITHING_TABLE, iz, this.b)));
	}

	private void a(Block block, Block block1, BiFunction<Block, Block, iz_> bifunction) {
		iz_ iz = bifunction.apply(block, block1);

		this.a.accept(e(block, iy.a.a(block, iz, this.b)));
	}

	private void v() {
		iz_ iz = iz_.j(Blocks.PUMPKIN);

		this.a.accept(e(Blocks.PUMPKIN, iw.a(Blocks.PUMPKIN)));
		this.a(Blocks.CARVED_PUMPKIN, iz);
		this.a(Blocks.JACK_O_LANTERN, iz);
	}

	private void a(Block block, iz_ iz) {
		MinecraftKey minecraftkey = iy.i.a(block, iz.c(ja.g, iz_.C(block)), this.b);

		this.a.accept(io.a(block, ir.a().a(is.c, minecraftkey)).a(b()));
	}

	private void w() {
		this.a(Items.nC);
		this.a.accept(io.a(Blocks.CAULDRON).a(ip.a(BlockProperties.ar).a(0, ir.a().a(is.c, iw.a(Blocks.CAULDRON))).a(1, ir.a().a(is.c, iw.a(Blocks.CAULDRON, "_level1"))).a(2, ir.a().a(is.c, iw.a(Blocks.CAULDRON, "_level2"))).a(3, ir.a().a(is.c, iw.a(Blocks.CAULDRON, "_level3")))));
	}

	private void g(Block block, Block block1) {
		iz_ iz = (new iz_()).a(ja.d, iz_.a(block1, "_top")).a(ja.i, iz_.C(block));

		this.a(block, iz, iy.e);
	}

	private void x() {
		iz_ iz = iz_.b(Blocks.CHORUS_FLOWER);
		MinecraftKey minecraftkey = iy.ae.a(Blocks.CHORUS_FLOWER, iz, this.b);
		MinecraftKey minecraftkey1 = this.a(Blocks.CHORUS_FLOWER, "_dead", iy.ae, (minecraftkey2) -> {
			return iz.c(ja.b, minecraftkey2);
		});

		this.a.accept(io.a(Blocks.CHORUS_FLOWER).a(a(BlockProperties.ah, 5, minecraftkey1, minecraftkey)));
	}

	private void q(Block block) {
		iz_ iz = (new iz_()).a(ja.f, iz_.a(Blocks.FURNACE, "_top")).a(ja.i, iz_.a(Blocks.FURNACE, "_side")).a(ja.g, iz_.a(block, "_front"));
		iz_ iz1 = (new iz_()).a(ja.i, iz_.a(Blocks.FURNACE, "_top")).a(ja.g, iz_.a(block, "_front_vertical"));
		MinecraftKey minecraftkey = iy.i.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.k.a(block, iz1, this.b);

		this.a.accept(io.a(block).a(ip.a(BlockProperties.M).a(EnumDirection.DOWN, ir.a().a(is.c, minecraftkey1).a(is.a, is.a_.c)).a(EnumDirection.UP, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.NORTH, ir.a().a(is.c, minecraftkey)).a(EnumDirection.EAST, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b)).a(EnumDirection.SOUTH, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c)).a(EnumDirection.WEST, ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d))));
	}

	private void y() {
		MinecraftKey minecraftkey = iw.a(Blocks.END_PORTAL_FRAME);
		MinecraftKey minecraftkey1 = iw.a(Blocks.END_PORTAL_FRAME, "_filled");

		this.a.accept(io.a(Blocks.END_PORTAL_FRAME).a(ip.a(BlockProperties.h).a(false, ir.a().a(is.c, minecraftkey)).a(true, ir.a().a(is.c, minecraftkey1))).a(c()));
	}

	private void z() {
		MinecraftKey minecraftkey = iw.a(Blocks.CHORUS_PLANT, "_side");
		MinecraftKey minecraftkey1 = iw.a(Blocks.CHORUS_PLANT, "_noside");
		MinecraftKey minecraftkey2 = iw.a(Blocks.CHORUS_PLANT, "_noside1");
		MinecraftKey minecraftkey3 = iw.a(Blocks.CHORUS_PLANT, "_noside2");
		MinecraftKey minecraftkey4 = iw.a(Blocks.CHORUS_PLANT, "_noside3");

		this.a.accept(in.a(Blocks.CHORUS_PLANT).a(im.a().a(BlockProperties.I, true), ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.J, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.K, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.G, true), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.H, true), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.I, false), ir.a().a(is.c, minecraftkey1).a(is.e, 2), ir.a().a(is.c, minecraftkey2), ir.a().a(is.c, minecraftkey3), ir.a().a(is.c, minecraftkey4)).a(im.a().a(BlockProperties.J, false), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey4).a(is.b, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey1).a(is.e, 2).a(is.b, is.a_.b).a(is.d, true)).a(im.a().a(BlockProperties.K, false), ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.c).a(is.d, true), ir.a().a(is.c, minecraftkey4).a(is.b, is.a_.c).a(is.d, true), ir.a().a(is.c, minecraftkey1).a(is.e, 2).a(is.b, is.a_.c).a(is.d, true), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.c).a(is.d, true)).a(im.a().a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey4).a(is.b, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey1).a(is.e, 2).a(is.b, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.G, false), ir.a().a(is.c, minecraftkey1).a(is.e, 2).a(is.a, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey4).a(is.a, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.d).a(is.d, true), ir.a().a(is.c, minecraftkey3).a(is.a, is.a_.d).a(is.d, true)).a(im.a().a(BlockProperties.H, false), ir.a().a(is.c, minecraftkey4).a(is.a, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey3).a(is.a, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey2).a(is.a, is.a_.b).a(is.d, true), ir.a().a(is.c, minecraftkey1).a(is.e, 2).a(is.a, is.a_.b).a(is.d, true)));
	}

	private void A() {
		this.a.accept(in.a(Blocks.COMPOSTER).a(ir.a().a(is.c, iz_.C(Blocks.COMPOSTER))).a(im.a().a(BlockProperties.as, 1), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents1"))).a(im.a().a(BlockProperties.as, 2), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents2"))).a(im.a().a(BlockProperties.as, 3), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents3"))).a(im.a().a(BlockProperties.as, 4), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents4"))).a(im.a().a(BlockProperties.as, 5), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents5"))).a(im.a().a(BlockProperties.as, 6), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents6"))).a(im.a().a(BlockProperties.as, 7), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents7"))).a(im.a().a(BlockProperties.as, 8), ir.a().a(is.c, iz_.a(Blocks.COMPOSTER, "_contents_ready"))));
	}

	private void r(Block block) {
		iz_ iz = (new iz_()).a(ja.e, iz_.C(Blocks.NETHERRACK)).a(ja.f, iz_.C(block)).a(ja.i, iz_.a(block, "_side"));

		this.a.accept(e(block, iy.h.a(block, iz, this.b)));
	}

	private void B() {
		MinecraftKey minecraftkey = iz_.a(Blocks.DAYLIGHT_DETECTOR, "_side");
		iz_ iz = (new iz_()).a(ja.f, iz_.a(Blocks.DAYLIGHT_DETECTOR, "_top")).a(ja.i, minecraftkey);
		iz_ iz1 = (new iz_()).a(ja.f, iz_.a(Blocks.DAYLIGHT_DETECTOR, "_inverted_top")).a(ja.i, minecraftkey);

		this.a.accept(io.a(Blocks.DAYLIGHT_DETECTOR).a(ip.a(BlockProperties.p).a(false, ir.a().a(is.c, iy.af.a(Blocks.DAYLIGHT_DETECTOR, iz, this.b))).a(true, ir.a().a(is.c, iy.af.a(iw.a(Blocks.DAYLIGHT_DETECTOR, "_inverted"), iz1, this.b)))));
	}

	private void s(Block block) {
		this.a.accept(io.a(block, ir.a().a(is.c, iw.a(block))).a(this.j()));
	}

	private void C() {
		iz_ iz = (new iz_()).a(ja.B, iz_.C(Blocks.DIRT)).a(ja.f, iz_.C(Blocks.FARMLAND));
		iz_ iz1 = (new iz_()).a(ja.B, iz_.C(Blocks.DIRT)).a(ja.f, iz_.a(Blocks.FARMLAND, "_moist"));
		MinecraftKey minecraftkey = iy.aq.a(Blocks.FARMLAND, iz, this.b);
		MinecraftKey minecraftkey1 = iy.aq.a(iz_.a(Blocks.FARMLAND, "_moist"), iz1, this.b);

		this.a.accept(io.a(Blocks.FARMLAND).a(a(BlockProperties.aw, 7, minecraftkey1, minecraftkey)));
	}

	private List<MinecraftKey> t(Block block) {
		MinecraftKey minecraftkey = iy.ar.a(iw.a(block, "_floor0"), iz_.r(block), this.b);
		MinecraftKey minecraftkey1 = iy.ar.a(iw.a(block, "_floor1"), iz_.s(block), this.b);

		return ImmutableList.of(minecraftkey, minecraftkey1);
	}

	private List<MinecraftKey> u(Block block) {
		MinecraftKey minecraftkey = iy.as.a(iw.a(block, "_side0"), iz_.r(block), this.b);
		MinecraftKey minecraftkey1 = iy.as.a(iw.a(block, "_side1"), iz_.s(block), this.b);
		MinecraftKey minecraftkey2 = iy.at.a(iw.a(block, "_side_alt0"), iz_.r(block), this.b);
		MinecraftKey minecraftkey3 = iy.at.a(iw.a(block, "_side_alt1"), iz_.s(block), this.b);

		return ImmutableList.of(minecraftkey, minecraftkey1, minecraftkey2, minecraftkey3);
	}

	private List<MinecraftKey> v(Block block) {
		MinecraftKey minecraftkey = iy.au.a(iw.a(block, "_up0"), iz_.r(block), this.b);
		MinecraftKey minecraftkey1 = iy.au.a(iw.a(block, "_up1"), iz_.s(block), this.b);
		MinecraftKey minecraftkey2 = iy.av.a(iw.a(block, "_up_alt0"), iz_.r(block), this.b);
		MinecraftKey minecraftkey3 = iy.av.a(iw.a(block, "_up_alt1"), iz_.s(block), this.b);

		return ImmutableList.of(minecraftkey, minecraftkey1, minecraftkey2, minecraftkey3);
	}

	private void D() {
		im.c im_c = im.a().a(BlockProperties.I, false).a(BlockProperties.J, false).a(BlockProperties.K, false).a(BlockProperties.L, false).a(BlockProperties.G, false);
		List<MinecraftKey> list = this.t(Blocks.FIRE);
		List<MinecraftKey> list1 = this.u(Blocks.FIRE);
		List<MinecraftKey> list2 = this.v(Blocks.FIRE);

		this.a.accept(in.a(Blocks.FIRE).a(im_c, a(list, (ir) -> {
			return ir;
		})).a(im.b(im.a().a(BlockProperties.I, true), im_c), a(list1, (ir) -> {
			return ir;
		})).a(im.b(im.a().a(BlockProperties.J, true), im_c), a(list1, (ir) -> {
			return ir.a(is.b, is.a_.b);
		})).a(im.b(im.a().a(BlockProperties.K, true), im_c), a(list1, (ir) -> {
			return ir.a(is.b, is.a_.c);
		})).a(im.b(im.a().a(BlockProperties.L, true), im_c), a(list1, (ir) -> {
			return ir.a(is.b, is.a_.d);
		})).a(im.a().a(BlockProperties.G, true), a(list2, (ir) -> {
			return ir;
		})));
	}

	private void E() {
		List<MinecraftKey> list = this.t(Blocks.SOUL_FIRE);
		List<MinecraftKey> list1 = this.u(Blocks.SOUL_FIRE);

		this.a.accept(in.a(Blocks.SOUL_FIRE).a(a(list, (ir) -> {
			return ir;
		})).a(a(list1, (ir) -> {
			return ir;
		})).a(a(list1, (ir) -> {
			return ir.a(is.b, is.a_.b);
		})).a(a(list1, (ir) -> {
			return ir.a(is.b, is.a_.c);
		})).a(a(list1, (ir) -> {
			return ir.a(is.b, is.a_.d);
		})));
	}

	private void w(Block block) {
		MinecraftKey minecraftkey = jb_.o.a(block, this.b);
		MinecraftKey minecraftkey1 = jb_.p.a(block, this.b);

		this.a(block.getItem());
		this.a.accept(io.a(block).a(a(BlockProperties.j, minecraftkey1, minecraftkey)));
	}

	private void F() {
		this.a.accept(io.a(Blocks.FROSTED_ICE).a(ip.a(BlockProperties.ag).a(0, ir.a().a(is.c, this.a(Blocks.FROSTED_ICE, "_0", iy.c, iz_::b))).a(1, ir.a().a(is.c, this.a(Blocks.FROSTED_ICE, "_1", iy.c, iz_::b))).a(2, ir.a().a(is.c, this.a(Blocks.FROSTED_ICE, "_2", iy.c, iz_::b))).a(3, ir.a().a(is.c, this.a(Blocks.FROSTED_ICE, "_3", iy.c, iz_::b)))));
	}

	private void G() {
		MinecraftKey minecraftkey = iz_.C(Blocks.DIRT);
		iz_ iz = (new iz_()).a(ja.e, minecraftkey).b(ja.e, ja.c).a(ja.f, iz_.a(Blocks.GRASS_BLOCK, "_top")).a(ja.i, iz_.a(Blocks.GRASS_BLOCK, "_snow"));
		ir ir___ = ir.a().a(is.c, iy.h.a(Blocks.GRASS_BLOCK, "_snow", iz, this.b));

		this.a(Blocks.GRASS_BLOCK, iw.a(Blocks.GRASS_BLOCK), ir___);
		MinecraftKey minecraftkey1 = jb_.e.get(Blocks.MYCELIUM).a((iz1) -> {
			iz1.a(ja.e, minecraftkey);
		}).a(Blocks.MYCELIUM, this.b);

		this.a(Blocks.MYCELIUM, minecraftkey1, ir___);
		MinecraftKey minecraftkey2 = jb_.e.get(Blocks.PODZOL).a((iz1) -> {
			iz1.a(ja.e, minecraftkey);
		}).a(Blocks.PODZOL, this.b);

		this.a(Blocks.PODZOL, minecraftkey2, ir___);
	}

	private void a(Block block, MinecraftKey minecraftkey, ir ir) {
		List<ir> list = Arrays.asList(a(minecraftkey));

		this.a.accept(io.a(block).a(ip.a(BlockProperties.z).a(true, ir).a(false, list)));
	}

	private void H() {
		this.a(Items.COCOA_BEANS);
		this.a.accept(io.a(Blocks.COCOA).a(ip.a(BlockProperties.af).a(0, ir.a().a(is.c, iw.a(Blocks.COCOA, "_stage0"))).a(1, ir.a().a(is.c, iw.a(Blocks.COCOA, "_stage1"))).a(2, ir.a().a(is.c, iw.a(Blocks.COCOA, "_stage2")))).a(c()));
	}

	private void I() {
		this.a.accept(d(Blocks.GRASS_PATH, iw.a(Blocks.GRASS_PATH)));
	}

	private void h(Block block, Block block1) {
		iz_ iz = iz_.b(block1);
		MinecraftKey minecraftkey = iy.D.a(block, iz, this.b);
		MinecraftKey minecraftkey1 = iy.E.a(block, iz, this.b);

		this.a.accept(io.a(block).a(a(BlockProperties.az, 1, minecraftkey1, minecraftkey)));
	}

	private void J() {
		MinecraftKey minecraftkey = iw.a(Blocks.HOPPER);
		MinecraftKey minecraftkey1 = iw.a(Blocks.HOPPER, "_side");

		this.a(Items.fl);
		this.a.accept(io.a(Blocks.HOPPER).a(ip.a(BlockProperties.N).a(EnumDirection.DOWN, ir.a().a(is.c, minecraftkey)).a(EnumDirection.NORTH, ir.a().a(is.c, minecraftkey1)).a(EnumDirection.EAST, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.b)).a(EnumDirection.SOUTH, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.c)).a(EnumDirection.WEST, ir.a().a(is.c, minecraftkey1).a(is.b, is.a_.d))));
	}

	private void i(Block block, Block block1) {
		MinecraftKey minecraftkey = iw.a(block);

		this.a.accept(io.a(block1, ir.a().a(is.c, minecraftkey)));
		this.c(block1, minecraftkey);
	}

	private void K() {
		MinecraftKey minecraftkey = iw.a(Blocks.IRON_BARS, "_post_ends");
		MinecraftKey minecraftkey1 = iw.a(Blocks.IRON_BARS, "_post");
		MinecraftKey minecraftkey2 = iw.a(Blocks.IRON_BARS, "_cap");
		MinecraftKey minecraftkey3 = iw.a(Blocks.IRON_BARS, "_cap_alt");
		MinecraftKey minecraftkey4 = iw.a(Blocks.IRON_BARS, "_side");
		MinecraftKey minecraftkey5 = iw.a(Blocks.IRON_BARS, "_side_alt");

		this.a.accept(in.a(Blocks.IRON_BARS).a(ir.a().a(is.c, minecraftkey)).a(im.a().a(BlockProperties.I, false).a(BlockProperties.J, false).a(BlockProperties.K, false).a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey1)).a(im.a().a(BlockProperties.I, true).a(BlockProperties.J, false).a(BlockProperties.K, false).a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey2)).a(im.a().a(BlockProperties.I, false).a(BlockProperties.J, true).a(BlockProperties.K, false).a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey2).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.I, false).a(BlockProperties.J, false).a(BlockProperties.K, true).a(BlockProperties.L, false), ir.a().a(is.c, minecraftkey3)).a(im.a().a(BlockProperties.I, false).a(BlockProperties.J, false).a(BlockProperties.K, false).a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey3).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.I, true), ir.a().a(is.c, minecraftkey4)).a(im.a().a(BlockProperties.J, true), ir.a().a(is.c, minecraftkey4).a(is.b, is.a_.b)).a(im.a().a(BlockProperties.K, true), ir.a().a(is.c, minecraftkey5)).a(im.a().a(BlockProperties.L, true), ir.a().a(is.c, minecraftkey5).a(is.b, is.a_.b)));
		this.b(Blocks.IRON_BARS);
	}

	private void x(Block block) {
		this.a.accept(io.a(block, ir.a().a(is.c, iw.a(block))).a(b()));
	}

	private void L() {
		MinecraftKey minecraftkey = iw.a(Blocks.LEVER);
		MinecraftKey minecraftkey1 = iw.a(Blocks.LEVER, "_on");

		this.b(Blocks.LEVER);
		this.a.accept(io.a(Blocks.LEVER).a(a(BlockProperties.w, minecraftkey, minecraftkey1)).a(ip.a(BlockProperties.Q, BlockProperties.O).a(BlockPropertyAttachPosition.CEILING, EnumDirection.NORTH, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.EAST, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.d)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.SOUTH, ir.a().a(is.a, is.a_.c)).a(BlockPropertyAttachPosition.CEILING, EnumDirection.WEST, ir.a().a(is.a, is.a_.c).a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.NORTH, ir.a()).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.EAST, ir.a().a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.SOUTH, ir.a().a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.FLOOR, EnumDirection.WEST, ir.a().a(is.b, is.a_.d)).a(BlockPropertyAttachPosition.WALL, EnumDirection.NORTH, ir.a().a(is.a, is.a_.b)).a(BlockPropertyAttachPosition.WALL, EnumDirection.EAST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.b)).a(BlockPropertyAttachPosition.WALL, EnumDirection.SOUTH, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.c)).a(BlockPropertyAttachPosition.WALL, EnumDirection.WEST, ir.a().a(is.a, is.a_.b).a(is.b, is.a_.d))));
	}

	private void M() {
		this.b(Blocks.LILY_PAD);
		this.a.accept(d(Blocks.LILY_PAD, iw.a(Blocks.LILY_PAD)));
	}

	private void N() {
		this.a.accept(io.a(Blocks.NETHER_PORTAL).a(ip.a(BlockProperties.E).a(EnumDirection.EnumAxis.X, ir.a().a(is.c, iw.a(Blocks.NETHER_PORTAL, "_ns"))).a(EnumDirection.EnumAxis.Z, ir.a().a(is.c, iw.a(Blocks.NETHER_PORTAL, "_ew")))));
	}

	private void O() {
		MinecraftKey minecraftkey = jb_.a.a(Blocks.NETHERRACK, this.b);

		this.a.accept(io.a(Blocks.NETHERRACK, ir.a().a(is.c, minecraftkey), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.a, is.a_.d), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.a, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.a, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.b).a(is.a, is.a_.d), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.a, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.a, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.c).a(is.a, is.a_.d), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.a, is.a_.b), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.a, is.a_.c), ir.a().a(is.c, minecraftkey).a(is.b, is.a_.d).a(is.a, is.a_.d)));
	}

	private void P() {
		MinecraftKey minecraftkey = iw.a(Blocks.OBSERVER);
		MinecraftKey minecraftkey1 = iw.a(Blocks.OBSERVER, "_on");

		this.a.accept(io.a(Blocks.OBSERVER).a(a(BlockProperties.w, minecraftkey1, minecraftkey)).a(e()));
	}

	private void Q() {
		iz_ iz = (new iz_()).a(ja.e, iz_.a(Blocks.PISTON, "_bottom")).a(ja.i, iz_.a(Blocks.PISTON, "_side"));
		MinecraftKey minecraftkey = iz_.a(Blocks.PISTON, "_top_sticky");
		MinecraftKey minecraftkey1 = iz_.a(Blocks.PISTON, "_top");
		iz_ iz1 = iz.c(ja.E, minecraftkey);
		iz_ iz2 = iz.c(ja.E, minecraftkey1);
		MinecraftKey minecraftkey2 = iw.a(Blocks.PISTON, "_base");

		this.a(Blocks.PISTON, minecraftkey2, iz2);
		this.a(Blocks.STICKY_PISTON, minecraftkey2, iz1);
		MinecraftKey minecraftkey3 = iy.h.a(Blocks.PISTON, "_inventory", iz.c(ja.f, minecraftkey1), this.b);
		MinecraftKey minecraftkey4 = iy.h.a(Blocks.STICKY_PISTON, "_inventory", iz.c(ja.f, minecraftkey), this.b);

		this.c(Blocks.PISTON, minecraftkey3);
		this.c(Blocks.STICKY_PISTON, minecraftkey4);
	}

	private void a(Block block, MinecraftKey minecraftkey, iz_ iz) {
		MinecraftKey minecraftkey1 = iy.aB.a(block, iz, this.b);

		this.a.accept(io.a(block).a(a(BlockProperties.g, minecraftkey, minecraftkey1)).a(e()));
	}

	private void R() {
		iz_ iz = (new iz_()).a(ja.F, iz_.a(Blocks.PISTON, "_top")).a(ja.i, iz_.a(Blocks.PISTON, "_side"));
		iz_ iz1 = iz.c(ja.E, iz_.a(Blocks.PISTON, "_top_sticky"));
		iz_ iz2 = iz.c(ja.E, iz_.a(Blocks.PISTON, "_top"));

		this.a.accept(io.a(Blocks.PISTON_HEAD).a(ip.a(BlockProperties.x, BlockProperties.aJ).a(false, BlockPropertyPistonType.DEFAULT, ir.a().a(is.c, iy.aC.a(Blocks.PISTON, "_head", iz2, this.b))).a(false, BlockPropertyPistonType.STICKY, ir.a().a(is.c, iy.aC.a(Blocks.PISTON, "_head_sticky", iz1, this.b))).a(true, BlockPropertyPistonType.DEFAULT, ir.a().a(is.c, iy.aD.a(Blocks.PISTON, "_head_short", iz2, this.b))).a(true, BlockPropertyPistonType.STICKY, ir.a().a(is.c, iy.aD.a(Blocks.PISTON, "_head_short_sticky", iz1, this.b)))).a(e()));
	}

	private void S() {
		MinecraftKey minecraftkey = iw.a(Blocks.SCAFFOLDING, "_stable");
		MinecraftKey minecraftkey1 = iw.a(Blocks.SCAFFOLDING, "_unstable");

		this.c(Blocks.SCAFFOLDING, minecraftkey);
		this.a.accept(io.a(Blocks.SCAFFOLDING).a(a(BlockProperties.b, minecraftkey1, minecraftkey)));
	}

	private void T() {
		MinecraftKey minecraftkey = jb_.a.a(Blocks.REDSTONE_LAMP, this.b);
		MinecraftKey minecraftkey1 = this.a(Blocks.REDSTONE_LAMP, "_on", iy.c, iz_::b);

		this.a.accept(io.a(Blocks.REDSTONE_LAMP).a(a(BlockProperties.r, minecraftkey1, minecraftkey)));
	}

	private void j(Block block, Block block1) {
		iz_ iz = iz_.u(block);

		this.a.accept(e(block, iy.az.a(block, iz, this.b)));
		this.a.accept(io.a(block1, ir.a().a(is.c, iy.aA.a(block1, iz, this.b))).a(d()));
		this.b(block);
		this.a(block1);
	}

	private void U() {
		iz_ iz = iz_.u(Blocks.REDSTONE_TORCH);
		iz_ iz1 = iz_.i(iz_.a(Blocks.REDSTONE_TORCH, "_off"));
		MinecraftKey minecraftkey = iy.az.a(Blocks.REDSTONE_TORCH, iz, this.b);
		MinecraftKey minecraftkey1 = iy.az.a(Blocks.REDSTONE_TORCH, "_off", iz1, this.b);

		this.a.accept(io.a(Blocks.REDSTONE_TORCH).a(a(BlockProperties.r, minecraftkey, minecraftkey1)));
		MinecraftKey minecraftkey2 = iy.aA.a(Blocks.REDSTONE_WALL_TORCH, iz, this.b);
		MinecraftKey minecraftkey3 = iy.aA.a(Blocks.REDSTONE_WALL_TORCH, "_off", iz1, this.b);

		this.a.accept(io.a(Blocks.REDSTONE_WALL_TORCH).a(a(BlockProperties.r, minecraftkey2, minecraftkey3)).a(d()));
		this.b(Blocks.REDSTONE_TORCH);
		this.a(Blocks.REDSTONE_WALL_TORCH);
	}

	private void V() {
		this.a(Items.jU);
		this.a.accept(io.a(Blocks.REPEATER).a(ip.a(BlockProperties.am, BlockProperties.s, BlockProperties.w).a((integer, obool, obool1) -> {
			StringBuilder stringbuilder = new StringBuilder();

			stringbuilder.append('_').append(integer).append("tick");
			if (obool1) {
				stringbuilder.append("_on");
			}

			if (obool) {
				stringbuilder.append("_locked");
			}

			return ir.a().a(is.c, iz_.a(Blocks.REPEATER, stringbuilder.toString()));
		})).a(c()));
	}

	private void W() {
		this.a(Items.aP);
		this.a.accept(io.a(Blocks.SEA_PICKLE).a(ip.a(BlockProperties.ay, BlockProperties.C).a(1, false, Arrays.asList(a(iw.a("dead_sea_pickle")))).a(2, false, Arrays.asList(a(iw.a("two_dead_sea_pickles")))).a(3, false, Arrays.asList(a(iw.a("three_dead_sea_pickles")))).a(4, false, Arrays.asList(a(iw.a("four_dead_sea_pickles")))).a(1, true, Arrays.asList(a(iw.a("sea_pickle")))).a(2, true, Arrays.asList(a(iw.a("two_sea_pickles")))).a(3, true, Arrays.asList(a(iw.a("three_sea_pickles")))).a(4, true, Arrays.asList(a(iw.a("four_sea_pickles"))))));
	}

	private void X() {
		iz_ iz = iz_.a(Blocks.SNOW);
		MinecraftKey minecraftkey = iy.c.a(Blocks.SNOW_BLOCK, iz, this.b);

		this.a.accept(io.a(Blocks.SNOW).a(ip.a(BlockProperties.aq).a((integer) -> {
			return ir.a().a(is.c, (integer < 8 ? iw.a(Blocks.SNOW, "_height" + integer * 2) : minecraftkey));
		})));
		this.c(Blocks.SNOW, iw.a(Blocks.SNOW, "_height2"));
		this.a.accept(e(Blocks.SNOW_BLOCK, minecraftkey));
	}

	private void Y() {
		this.a.accept(io.a(Blocks.STONECUTTER, ir.a().a(is.c, iw.a(Blocks.STONECUTTER))).a(b()));
	}

	private void Z() {
		MinecraftKey minecraftkey = jb_.a.a(Blocks.STRUCTURE_BLOCK, this.b);

		this.c(Blocks.STRUCTURE_BLOCK, minecraftkey);
		this.a.accept(io.a(Blocks.STRUCTURE_BLOCK).a(ip.a(BlockProperties.aM).a((blockpropertystructuremode) -> {
			return ir.a().a(is.c, this.a(Blocks.STRUCTURE_BLOCK, "_" + blockpropertystructuremode.getName(), iy.c, iz_::b));
		})));
	}

	private void aa() {
		this.a(Items.SWEET_BERRIES);
		this.a.accept(io.a(Blocks.SWEET_BERRY_BUSH).a(ip.a(BlockProperties.ag).a((integer) -> {
			return ir.a().a(is.c, this.a(Blocks.SWEET_BERRY_BUSH, "_stage" + integer, iy.S, iz_::c));
		})));
	}

	private void ab() {
		this.a(Items.STRING);
		this.a.accept(io.a(Blocks.TRIPWIRE).a(ip.a(BlockProperties.a, BlockProperties.J, BlockProperties.I, BlockProperties.K, BlockProperties.L).a(false, false, false, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ns"))).a(false, true, false, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_n")).a(is.b, is.a_.b)).a(false, false, true, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_n"))).a(false, false, false, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_n")).a(is.b, is.a_.c)).a(false, false, false, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_n")).a(is.b, is.a_.d)).a(false, true, true, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ne"))).a(false, true, false, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ne")).a(is.b, is.a_.b)).a(false, false, false, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ne")).a(is.b, is.a_.c)).a(false, false, true, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ne")).a(is.b, is.a_.d)).a(false, false, true, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ns"))).a(false, true, false, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_ns")).a(is.b, is.a_.b)).a(false, true, true, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_nse"))).a(false, true, false, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_nse")).a(is.b, is.a_.b)).a(false, false, true, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_nse")).a(is.b, is.a_.c)).a(false, true, true, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_nse")).a(is.b, is.a_.d)).a(false, true, true, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_nsew"))).a(true, false, false, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ns"))).a(true, false, true, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_n"))).a(true, false, false, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_n")).a(is.b, is.a_.c)).a(true, true, false, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_n")).a(is.b, is.a_.b)).a(true, false, false, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_n")).a(is.b, is.a_.d)).a(true, true, true, false, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ne"))).a(true, true, false, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ne")).a(is.b, is.a_.b)).a(true, false, false, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ne")).a(is.b, is.a_.c)).a(true, false, true, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ne")).a(is.b, is.a_.d)).a(true, false, true, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ns"))).a(true, true, false, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_ns")).a(is.b, is.a_.b)).a(true, true, true, true, false, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_nse"))).a(true, true, false, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_nse")).a(is.b, is.a_.b)).a(true, false, true, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_nse")).a(is.b, is.a_.c)).a(true, true, true, false, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_nse")).a(is.b, is.a_.d)).a(true, true, true, true, true, ir.a().a(is.c, iw.a(Blocks.TRIPWIRE, "_attached_nsew")))));
	}

	private void ac() {
		this.b(Blocks.TRIPWIRE_HOOK);
		this.a.accept(io.a(Blocks.TRIPWIRE_HOOK).a(ip.a(BlockProperties.a, BlockProperties.w).a((obool, obool1) -> {
			return ir.a().a(is.c, iz_.a(Blocks.TRIPWIRE_HOOK, (obool ? "_attached" : "") + (obool1 ? "_on" : "")));
		})).a(b()));
	}

	private MinecraftKey a(int i, String s, iz_ iz) {
		return switch (i) {
			case 1 -> iy.aF.a(iw.a(s + "turtle_egg"), iz, this.b);
			case 2 -> iy.aG.a(iw.a("two_" + s + "turtle_eggs"), iz, this.b);
			case 3 -> iy.aH.a(iw.a("three_" + s + "turtle_eggs"), iz, this.b);
			case 4 -> iy.aI.a(iw.a("four_" + s + "turtle_eggs"), iz, this.b);
			default -> throw new UnsupportedOperationException();
		};
	}

	private MinecraftKey a(Integer integer, Integer integer1) {
		return switch (integer1) {
			case 0 -> this.a(integer, "", iz_.b(iz_.C(Blocks.TURTLE_EGG)));
			case 1 -> this.a(integer, "slightly_cracked_", iz_.b(iz_.a(Blocks.TURTLE_EGG, "_slightly_cracked")));
			case 2 -> this.a(integer, "very_cracked_", iz_.b(iz_.a(Blocks.TURTLE_EGG, "_very_cracked")));
			default -> throw new UnsupportedOperationException();
		};
	}

	private void ad() {
		this.a(Items.iC);
		this.a.accept(io.a(Blocks.TURTLE_EGG).a(ip.a(BlockProperties.ao, BlockProperties.ap).b((integer, integer1) -> {
			return Arrays.asList(a(this.a(integer, integer1)));
		})));
	}

	private void ae() {
		this.b(Blocks.VINE);
		this.a.accept(io.a(Blocks.VINE).a(ip.a(BlockProperties.J, BlockProperties.I, BlockProperties.K, BlockProperties.G, BlockProperties.L).a(false, false, false, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1"))).a(false, false, true, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1"))).a(false, false, false, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_1")).a(is.b, is.a_.b)).a(false, true, false, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1")).a(is.b, is.a_.c)).a(true, false, false, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1")).a(is.b, is.a_.d)).a(true, true, false, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2"))).a(true, false, true, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2")).a(is.b, is.a_.b)).a(false, false, true, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2")).a(is.b, is.a_.c)).a(false, true, false, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2")).a(is.b, is.a_.d)).a(true, false, false, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2_opposite"))).a(false, true, true, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2_opposite")).a(is.b, is.a_.b)).a(true, true, true, false, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_3"))).a(true, false, true, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3")).a(is.b, is.a_.b)).a(false, true, true, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3")).a(is.b, is.a_.c)).a(true, true, false, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3")).a(is.b, is.a_.d)).a(true, true, true, false, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_4"))).a(false, false, false, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_u"))).a(false, false, true, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1u"))).a(false, false, false, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_1u")).a(is.b, is.a_.b)).a(false, true, false, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1u")).a(is.b, is.a_.c)).a(true, false, false, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_1u")).a(is.b, is.a_.d)).a(true, true, false, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u"))).a(true, false, true, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u")).a(is.b, is.a_.b)).a(false, false, true, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u")).a(is.b, is.a_.c)).a(false, true, false, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u")).a(is.b, is.a_.d)).a(true, false, false, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u_opposite"))).a(false, true, true, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_2u_opposite")).a(is.b, is.a_.b)).a(true, true, true, true, false, ir.a().a(is.c, iw.a(Blocks.VINE, "_3u"))).a(true, false, true, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3u")).a(is.b, is.a_.b)).a(false, true, true, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3u")).a(is.b, is.a_.c)).a(true, true, false, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_3u")).a(is.b, is.a_.d)).a(true, true, true, true, true, ir.a().a(is.c, iw.a(Blocks.VINE, "_4u")))));
	}

	private void af() {
		this.a.accept(e(Blocks.MAGMA_BLOCK, iy.c.a(Blocks.MAGMA_BLOCK, iz_.b(iw.a("magma")), this.b)));
	}

	private void y(Block block) {
		this.c(block, jb_.l);
		iy.aN.a(iw.a(block.getItem()), iz_.q(block), this.b);
	}

	private void b(Block block, Block block1, ii.c ii_c) {
		this.b(block, ii_c);
		this.b(block1, ii_c);
	}

	private void k(Block block, Block block1) {
		iy.aO.a(iw.a(block.getItem()), iz_.q(block1), this.b);
	}

	private void ag() {
		MinecraftKey minecraftkey = iw.a(Blocks.STONE);
		MinecraftKey minecraftkey1 = iw.a(Blocks.STONE, "_mirrored");

		this.a.accept(e(Blocks.INFESTED_STONE, minecraftkey, minecraftkey1));
		this.c(Blocks.INFESTED_STONE, minecraftkey);
	}

	private void l(Block block, Block block1) {
		this.a(block, ii.c.b);
		iz_ iz = iz_.d(iz_.a(block, "_pot"));
		MinecraftKey minecraftkey = ii.c.b.b().a(block1, iz, this.b);

		this.a.accept(e(block1, minecraftkey));
	}

	private void ah() {
		MinecraftKey minecraftkey = iz_.a(Blocks.RESPAWN_ANCHOR, "_bottom");
		MinecraftKey minecraftkey1 = iz_.a(Blocks.RESPAWN_ANCHOR, "_top_off");
		MinecraftKey minecraftkey2 = iz_.a(Blocks.RESPAWN_ANCHOR, "_top");
		MinecraftKey[] aminecraftkey = new MinecraftKey[5];

		for (int i = 0; i < 5; ++i) {
			iz_ iz = (new iz_()).a(ja.e, minecraftkey).a(ja.f, i == 0 ? minecraftkey1 : minecraftkey2).a(ja.i, iz_.a(Blocks.RESPAWN_ANCHOR, "_side" + i));

			aminecraftkey[i] = iy.h.a(Blocks.RESPAWN_ANCHOR, "_" + i, iz, this.b);
		}

		this.a.accept(io.a(Blocks.RESPAWN_ANCHOR).a(ip.a(BlockProperties.aC).a((integer) -> {
			return ir.a().a(is.c, aminecraftkey[integer]);
		})));
		this.a(Items.rN, aminecraftkey[0]);
	}

	private ir a(BlockPropertyJigsawOrientation blockpropertyjigsaworientation, ir ir) {
		return switch (blockpropertyjigsaworientation) {
			case DOWN_NORTH -> ir.a(is.a, is.a_.b);
			case DOWN_SOUTH -> ir.a(is.a, is.a_.b).a(is.b, is.a_.c);
			case DOWN_WEST -> ir.a(is.a, is.a_.b).a(is.b, is.a_.d);
			case DOWN_EAST -> ir.a(is.a, is.a_.b).a(is.b, is.a_.b);
			case UP_NORTH -> ir.a(is.a, is.a_.d).a(is.b, is.a_.c);
			case UP_SOUTH -> ir.a(is.a, is.a_.d);
			case UP_WEST -> ir.a(is.a, is.a_.d).a(is.b, is.a_.b);
			case UP_EAST -> ir.a(is.a, is.a_.d).a(is.b, is.a_.d);
			case NORTH_UP -> ir;
			case SOUTH_UP -> ir.a(is.b, is.a_.c);
			case WEST_UP -> ir.a(is.b, is.a_.d);
			case EAST_UP -> ir.a(is.b, is.a_.b);
			default ->
				throw new UnsupportedOperationException("Rotation " + blockpropertyjigsaworientation + " can't be expressed with existing x and y values");
		};
	}

	private void ai() {
		MinecraftKey minecraftkey = iz_.a(Blocks.JIGSAW, "_top");
		MinecraftKey minecraftkey1 = iz_.a(Blocks.JIGSAW, "_bottom");
		MinecraftKey minecraftkey2 = iz_.a(Blocks.JIGSAW, "_side");
		MinecraftKey minecraftkey3 = iz_.a(Blocks.JIGSAW, "_lock");
		iz_ iz = (new iz_()).a(ja.o, minecraftkey2).a(ja.m, minecraftkey2).a(ja.l, minecraftkey2).a(ja.c, minecraftkey).a(ja.j, minecraftkey).a(ja.k, minecraftkey1).a(ja.n, minecraftkey3);
		MinecraftKey minecraftkey4 = iy.b.a(Blocks.JIGSAW, iz, this.b);

		this.a.accept(io.a(Blocks.JIGSAW, ir.a().a(is.c, minecraftkey4)).a(ip.a(BlockProperties.P).a((blockpropertyjigsaworientation) -> {
			return this.a(blockpropertyjigsaworientation, ir.a());
		})));
	}

	public void a() {
		this.k(Blocks.AIR);
		this.a(Blocks.CAVE_AIR, Blocks.AIR);
		this.a(Blocks.VOID_AIR, Blocks.AIR);
		this.k(Blocks.BEACON);
		this.k(Blocks.CACTUS);
		this.a(Blocks.BUBBLE_COLUMN, Blocks.WATER);
		this.k(Blocks.DRAGON_EGG);
		this.k(Blocks.DRIED_KELP_BLOCK);
		this.k(Blocks.ENCHANTING_TABLE);
		this.k(Blocks.FLOWER_POT);
		this.a(Items.oX);
		this.k(Blocks.HONEY_BLOCK);
		this.k(Blocks.WATER);
		this.k(Blocks.LAVA);
		this.k(Blocks.SLIME_BLOCK);
		this.a(Items.dO);
		this.k(Blocks.POTTED_BAMBOO);
		this.k(Blocks.POTTED_CACTUS);
		this.a(Blocks.BARRIER, Items.fJ);
		this.a(Items.fJ);
		this.a(Blocks.STRUCTURE_VOID, Items.hn);
		this.a(Items.hn);
		this.h(Blocks.MOVING_PISTON, iz_.a(Blocks.PISTON, "_side"));
		this.c(Blocks.COAL_ORE, jb_.a);
		this.c(Blocks.COAL_BLOCK, jb_.a);
		this.c(Blocks.DIAMOND_ORE, jb_.a);
		this.c(Blocks.DIAMOND_BLOCK, jb_.a);
		this.c(Blocks.EMERALD_ORE, jb_.a);
		this.c(Blocks.EMERALD_BLOCK, jb_.a);
		this.c(Blocks.GOLD_ORE, jb_.a);
		this.c(Blocks.NETHER_GOLD_ORE, jb_.a);
		this.c(Blocks.GOLD_BLOCK, jb_.a);
		this.c(Blocks.IRON_ORE, jb_.a);
		this.c(Blocks.IRON_BLOCK, jb_.a);
		this.c(Blocks.ANCIENT_DEBRIS, jb_.c);
		this.c(Blocks.NETHERITE_BLOCK, jb_.a);
		this.c(Blocks.LAPIS_ORE, jb_.a);
		this.c(Blocks.LAPIS_BLOCK, jb_.a);
		this.c(Blocks.NETHER_QUARTZ_ORE, jb_.a);
		this.c(Blocks.REDSTONE_ORE, jb_.a);
		this.c(Blocks.REDSTONE_BLOCK, jb_.a);
		this.c(Blocks.GILDED_BLACKSTONE, jb_.a);
		this.c(Blocks.BLUE_ICE, jb_.a);
		this.c(Blocks.CHISELED_NETHER_BRICKS, jb_.a);
		this.c(Blocks.CLAY, jb_.a);
		this.c(Blocks.COARSE_DIRT, jb_.a);
		this.c(Blocks.CRACKED_NETHER_BRICKS, jb_.a);
		this.c(Blocks.CRACKED_STONE_BRICKS, jb_.a);
		this.c(Blocks.CRYING_OBSIDIAN, jb_.a);
		this.c(Blocks.END_STONE, jb_.a);
		this.c(Blocks.GLOWSTONE, jb_.a);
		this.c(Blocks.GRAVEL, jb_.a);
		this.c(Blocks.HONEYCOMB_BLOCK, jb_.a);
		this.c(Blocks.ICE, jb_.a);
		this.c(Blocks.JUKEBOX, jb_.f);
		this.c(Blocks.LODESTONE, jb_.c);
		this.c(Blocks.MELON, jb_.c);
		this.c(Blocks.NETHER_WART_BLOCK, jb_.a);
		this.c(Blocks.NOTE_BLOCK, jb_.a);
		this.c(Blocks.PACKED_ICE, jb_.a);
		this.c(Blocks.OBSIDIAN, jb_.a);
		this.c(Blocks.QUARTZ_BRICKS, jb_.a);
		this.c(Blocks.SEA_LANTERN, jb_.a);
		this.c(Blocks.SHROOMLIGHT, jb_.a);
		this.c(Blocks.SOUL_SAND, jb_.a);
		this.c(Blocks.SOUL_SOIL, jb_.a);
		this.c(Blocks.SPAWNER, jb_.a);
		this.c(Blocks.SPONGE, jb_.a);
		this.c(Blocks.SEAGRASS, jb_.q);
		this.a(Items.aO);
		this.c(Blocks.TNT, jb_.e);
		this.c(Blocks.TARGET, jb_.c);
		this.c(Blocks.WARPED_WART_BLOCK, jb_.a);
		this.c(Blocks.WET_SPONGE, jb_.a);
		this.c(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, jb_.a);
		this.c(Blocks.CHISELED_QUARTZ_BLOCK, jb_.c.a((iz) -> {
			iz.a(ja.i, iz_.C(Blocks.CHISELED_QUARTZ_BLOCK));
		}));
		this.c(Blocks.CHISELED_STONE_BRICKS, jb_.a);
		this.g(Blocks.CHISELED_SANDSTONE, Blocks.SANDSTONE);
		this.g(Blocks.CHISELED_RED_SANDSTONE, Blocks.RED_SANDSTONE);
		this.c(Blocks.CHISELED_POLISHED_BLACKSTONE, jb_.a);
		this.h(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
		this.h(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
		this.n();
		this.r();
		this.s();
		this.a(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
		this.t();
		this.w();
		this.x();
		this.z();
		this.A();
		this.B();
		this.y();
		this.s(Blocks.END_ROD);
		this.C();
		this.D();
		this.E();
		this.F();
		this.G();
		this.H();
		this.I();
		this.m();
		this.J();
		this.K();
		this.L();
		this.M();
		this.N();
		this.O();
		this.P();
		this.Q();
		this.R();
		this.S();
		this.U();
		this.T();
		this.V();
		this.W();
		this.u();
		this.X();
		this.Y();
		this.Z();
		this.aa();
		this.ab();
		this.ac();
		this.ad();
		this.ae();
		this.af();
		this.ai();
		this.x(Blocks.LADDER);
		this.b(Blocks.LADDER);
		this.x(Blocks.LECTERN);
		this.j(Blocks.TORCH, Blocks.WALL_TORCH);
		this.j(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
		this.a(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS, iz_::c);
		this.a(Blocks.FLETCHING_TABLE, Blocks.BIRCH_PLANKS, iz_::d);
		this.r(Blocks.CRIMSON_NYLIUM);
		this.r(Blocks.WARPED_NYLIUM);
		this.q(Blocks.DISPENSER);
		this.q(Blocks.DROPPER);
		this.w(Blocks.LANTERN);
		this.w(Blocks.SOUL_LANTERN);
		this.g(Blocks.CHAIN, iw.a(Blocks.CHAIN));
		this.a(Blocks.BASALT, jb_.c);
		this.a(Blocks.cP, jb_.c);
		this.a(Blocks.BONE_BLOCK, jb_.c);
		this.d(Blocks.DIRT);
		this.d(Blocks.SAND);
		this.d(Blocks.RED_SAND);
		this.c(Blocks.BEDROCK);
		this.a(Blocks.HAY_BLOCK, jb_.c, jb_.d);
		this.a(Blocks.PURPUR_PILLAR, jb_.r, jb_.s);
		this.a(Blocks.QUARTZ_PILLAR, jb_.r, jb_.s);
		this.b(Blocks.LOOM, jb_.h);
		this.v();
		this.a(Blocks.BEE_NEST, iz_::w);
		this.a(Blocks.BEEHIVE, iz_::y);
		this.a(Blocks.BEETROOTS, BlockProperties.ag, 0, 1, 2, 3);
		this.a(Blocks.CARROTS, BlockProperties.ai, 0, 0, 1, 1, 2, 2, 2, 3);
		this.a(Blocks.NETHER_WART, BlockProperties.ag, 0, 1, 1, 2);
		this.a(Blocks.POTATOES, BlockProperties.ai, 0, 0, 1, 1, 2, 2, 2, 3);
		this.a(Blocks.WHEAT, BlockProperties.ai, 0, 1, 2, 3, 4, 5, 6, 7);
		this.a(iw.a("banner"), Blocks.OAK_PLANKS).a(iy.aP, Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER).b(Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER);
		this.a(iw.a("bed"), Blocks.OAK_PLANKS).b(Blocks.WHITE_BED, Blocks.ORANGE_BED, Blocks.MAGENTA_BED, Blocks.LIGHT_BLUE_BED, Blocks.YELLOW_BED, Blocks.LIME_BED, Blocks.PINK_BED, Blocks.GRAY_BED, Blocks.LIGHT_GRAY_BED, Blocks.CYAN_BED, Blocks.PURPLE_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.GREEN_BED, Blocks.RED_BED, Blocks.BLACK_BED);
		this.k(Blocks.WHITE_BED, Blocks.WHITE_WOOL);
		this.k(Blocks.ORANGE_BED, Blocks.ORANGE_WOOL);
		this.k(Blocks.MAGENTA_BED, Blocks.MAGENTA_WOOL);
		this.k(Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL);
		this.k(Blocks.YELLOW_BED, Blocks.YELLOW_WOOL);
		this.k(Blocks.LIME_BED, Blocks.LIME_WOOL);
		this.k(Blocks.PINK_BED, Blocks.PINK_WOOL);
		this.k(Blocks.GRAY_BED, Blocks.GRAY_WOOL);
		this.k(Blocks.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL);
		this.k(Blocks.CYAN_BED, Blocks.CYAN_WOOL);
		this.k(Blocks.PURPLE_BED, Blocks.PURPLE_WOOL);
		this.k(Blocks.BLUE_BED, Blocks.BLUE_WOOL);
		this.k(Blocks.BROWN_BED, Blocks.BROWN_WOOL);
		this.k(Blocks.GREEN_BED, Blocks.GREEN_WOOL);
		this.k(Blocks.RED_BED, Blocks.RED_WOOL);
		this.k(Blocks.BLACK_BED, Blocks.BLACK_WOOL);
		this.a(iw.a("skull"), Blocks.SOUL_SAND).a(iy.aQ, Blocks.CREEPER_HEAD, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL).a(Blocks.DRAGON_HEAD).b(Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.SKELETON_WALL_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL);
		this.y(Blocks.SHULKER_BOX);
		this.y(Blocks.WHITE_SHULKER_BOX);
		this.y(Blocks.ORANGE_SHULKER_BOX);
		this.y(Blocks.MAGENTA_SHULKER_BOX);
		this.y(Blocks.LIGHT_BLUE_SHULKER_BOX);
		this.y(Blocks.YELLOW_SHULKER_BOX);
		this.y(Blocks.LIME_SHULKER_BOX);
		this.y(Blocks.PINK_SHULKER_BOX);
		this.y(Blocks.GRAY_SHULKER_BOX);
		this.y(Blocks.LIGHT_GRAY_SHULKER_BOX);
		this.y(Blocks.CYAN_SHULKER_BOX);
		this.y(Blocks.PURPLE_SHULKER_BOX);
		this.y(Blocks.BLUE_SHULKER_BOX);
		this.y(Blocks.BROWN_SHULKER_BOX);
		this.y(Blocks.GREEN_SHULKER_BOX);
		this.y(Blocks.RED_SHULKER_BOX);
		this.y(Blocks.BLACK_SHULKER_BOX);
		this.c(Blocks.CONDUIT, jb_.l);
		this.a(Blocks.CONDUIT);
		this.a(iw.a("chest"), Blocks.OAK_PLANKS).b(Blocks.CHEST, Blocks.TRAPPED_CHEST);
		this.a(iw.a("ender_chest"), Blocks.OBSIDIAN).b(Blocks.ENDER_CHEST);
		this.d(Blocks.END_PORTAL, Blocks.OBSIDIAN).a(Blocks.END_PORTAL, Blocks.END_GATEWAY);
		this.e(Blocks.WHITE_CONCRETE);
		this.e(Blocks.ORANGE_CONCRETE);
		this.e(Blocks.MAGENTA_CONCRETE);
		this.e(Blocks.LIGHT_BLUE_CONCRETE);
		this.e(Blocks.YELLOW_CONCRETE);
		this.e(Blocks.LIME_CONCRETE);
		this.e(Blocks.PINK_CONCRETE);
		this.e(Blocks.GRAY_CONCRETE);
		this.e(Blocks.LIGHT_GRAY_CONCRETE);
		this.e(Blocks.CYAN_CONCRETE);
		this.e(Blocks.PURPLE_CONCRETE);
		this.e(Blocks.BLUE_CONCRETE);
		this.e(Blocks.BROWN_CONCRETE);
		this.e(Blocks.GREEN_CONCRETE);
		this.e(Blocks.RED_CONCRETE);
		this.e(Blocks.BLACK_CONCRETE);
		this.a(jb_.a, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);
		this.e(Blocks.TERRACOTTA);
		this.e(Blocks.WHITE_TERRACOTTA);
		this.e(Blocks.ORANGE_TERRACOTTA);
		this.e(Blocks.MAGENTA_TERRACOTTA);
		this.e(Blocks.LIGHT_BLUE_TERRACOTTA);
		this.e(Blocks.YELLOW_TERRACOTTA);
		this.e(Blocks.LIME_TERRACOTTA);
		this.e(Blocks.PINK_TERRACOTTA);
		this.e(Blocks.GRAY_TERRACOTTA);
		this.e(Blocks.LIGHT_GRAY_TERRACOTTA);
		this.e(Blocks.CYAN_TERRACOTTA);
		this.e(Blocks.PURPLE_TERRACOTTA);
		this.e(Blocks.BLUE_TERRACOTTA);
		this.e(Blocks.BROWN_TERRACOTTA);
		this.e(Blocks.GREEN_TERRACOTTA);
		this.e(Blocks.RED_TERRACOTTA);
		this.e(Blocks.BLACK_TERRACOTTA);
		this.f(Blocks.GLASS, Blocks.GLASS_PANE);
		this.f(Blocks.WHITE_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS_PANE);
		this.f(Blocks.ORANGE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS_PANE);
		this.f(Blocks.MAGENTA_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS_PANE);
		this.f(Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
		this.f(Blocks.YELLOW_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS_PANE);
		this.f(Blocks.LIME_STAINED_GLASS, Blocks.LIME_STAINED_GLASS_PANE);
		this.f(Blocks.PINK_STAINED_GLASS, Blocks.PINK_STAINED_GLASS_PANE);
		this.f(Blocks.GRAY_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS_PANE);
		this.f(Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
		this.f(Blocks.CYAN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS_PANE);
		this.f(Blocks.PURPLE_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS_PANE);
		this.f(Blocks.BLUE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS_PANE);
		this.f(Blocks.BROWN_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS_PANE);
		this.f(Blocks.GREEN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS_PANE);
		this.f(Blocks.RED_STAINED_GLASS, Blocks.RED_STAINED_GLASS_PANE);
		this.f(Blocks.BLACK_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS_PANE);
		this.b(jb_.j, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA);
		this.e(Blocks.WHITE_WOOL, Blocks.WHITE_CARPET);
		this.e(Blocks.ORANGE_WOOL, Blocks.ORANGE_CARPET);
		this.e(Blocks.MAGENTA_WOOL, Blocks.MAGENTA_CARPET);
		this.e(Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_CARPET);
		this.e(Blocks.YELLOW_WOOL, Blocks.YELLOW_CARPET);
		this.e(Blocks.LIME_WOOL, Blocks.LIME_CARPET);
		this.e(Blocks.PINK_WOOL, Blocks.PINK_CARPET);
		this.e(Blocks.GRAY_WOOL, Blocks.GRAY_CARPET);
		this.e(Blocks.LIGHT_GRAY_WOOL, Blocks.LIGHT_GRAY_CARPET);
		this.e(Blocks.CYAN_WOOL, Blocks.CYAN_CARPET);
		this.e(Blocks.PURPLE_WOOL, Blocks.PURPLE_CARPET);
		this.e(Blocks.BLUE_WOOL, Blocks.BLUE_CARPET);
		this.e(Blocks.BROWN_WOOL, Blocks.BROWN_CARPET);
		this.e(Blocks.GREEN_WOOL, Blocks.GREEN_CARPET);
		this.e(Blocks.RED_WOOL, Blocks.RED_CARPET);
		this.e(Blocks.BLACK_WOOL, Blocks.BLACK_CARPET);
		this.a(Blocks.FERN, Blocks.POTTED_FERN, ii.c.a);
		this.a(Blocks.DANDELION, Blocks.POTTED_DANDELION, ii.c.b);
		this.a(Blocks.POPPY, Blocks.POTTED_POPPY, ii.c.b);
		this.a(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID, ii.c.b);
		this.a(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, ii.c.b);
		this.a(Blocks.AZURE_BLUET, Blocks.POTTED_AZURE_BLUET, ii.c.b);
		this.a(Blocks.RED_TULIP, Blocks.POTTED_RED_TULIP, ii.c.b);
		this.a(Blocks.ORANGE_TULIP, Blocks.POTTED_ORANGE_TULIP, ii.c.b);
		this.a(Blocks.WHITE_TULIP, Blocks.POTTED_WHITE_TULIP, ii.c.b);
		this.a(Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, ii.c.b);
		this.a(Blocks.OXEYE_DAISY, Blocks.POTTED_OXEYE_DAISY, ii.c.b);
		this.a(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER, ii.c.b);
		this.a(Blocks.LILY_OF_THE_VALLEY, Blocks.POTTED_LILY_OF_THE_VALLEY, ii.c.b);
		this.a(Blocks.WITHER_ROSE, Blocks.POTTED_WITHER_ROSE, ii.c.b);
		this.a(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, ii.c.b);
		this.a(Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, ii.c.b);
		this.a(Blocks.DEAD_BUSH, Blocks.POTTED_DEAD_BUSH, ii.c.b);
		this.p(Blocks.BROWN_MUSHROOM_BLOCK);
		this.p(Blocks.RED_MUSHROOM_BLOCK);
		this.p(Blocks.MUSHROOM_STEM);
		this.a(Blocks.GRASS, ii.c.a);
		this.b(Blocks.SUGAR_CANE, ii.c.a);
		this.a(Items.bD);
		this.b(Blocks.KELP, Blocks.KELP_PLANT, ii.c.a);
		this.a(Items.bE);
		this.a(Blocks.KELP_PLANT);
		this.b(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, ii.c.b);
		this.b(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, ii.c.b);
		this.a(Blocks.WEEPING_VINES, "_plant");
		this.a(Blocks.WEEPING_VINES_PLANT);
		this.a(Blocks.TWISTING_VINES, "_plant");
		this.a(Blocks.TWISTING_VINES_PLANT);
		this.a(Blocks.BAMBOO_SAPLING, ii.c.a, iz_.c(iz_.a(Blocks.BAMBOO, "_stage0")));
		this.i();
		this.a(Blocks.COBWEB, ii.c.b);
		this.c(Blocks.LILAC, ii.c.b);
		this.c(Blocks.ROSE_BUSH, ii.c.b);
		this.c(Blocks.PEONY, ii.c.b);
		this.c(Blocks.TALL_GRASS, ii.c.a);
		this.c(Blocks.LARGE_FERN, ii.c.a);
		this.g();
		this.h();
		this.a(Blocks.TUBE_CORAL, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN);
		this.a(Blocks.BRAIN_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
		this.a(Blocks.BUBBLE_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
		this.a(Blocks.FIRE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN);
		this.a(Blocks.HORN_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN);
		this.c(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);
		this.c(Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
		this.f(Blocks.ACACIA_PLANKS).a(Blocks.ACACIA_BUTTON).c(Blocks.ACACIA_FENCE).d(Blocks.ACACIA_FENCE_GATE).e(Blocks.ACACIA_PRESSURE_PLATE).a(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN).f(Blocks.ACACIA_SLAB).g(Blocks.ACACIA_STAIRS);
		this.g(Blocks.ACACIA_DOOR);
		this.h(Blocks.ACACIA_TRAPDOOR);
		this.j(Blocks.ACACIA_LOG).c(Blocks.ACACIA_LOG).a(Blocks.ACACIA_WOOD);
		this.j(Blocks.STRIPPED_ACACIA_LOG).c(Blocks.STRIPPED_ACACIA_LOG).a(Blocks.STRIPPED_ACACIA_WOOD);
		this.a(Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING, ii.c.b);
		this.c(Blocks.ACACIA_LEAVES, jb_.n);
		this.f(Blocks.BIRCH_PLANKS).a(Blocks.BIRCH_BUTTON).c(Blocks.BIRCH_FENCE).d(Blocks.BIRCH_FENCE_GATE).e(Blocks.BIRCH_PRESSURE_PLATE).a(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN).f(Blocks.BIRCH_SLAB).g(Blocks.BIRCH_STAIRS);
		this.g(Blocks.BIRCH_DOOR);
		this.h(Blocks.BIRCH_TRAPDOOR);
		this.j(Blocks.BIRCH_LOG).c(Blocks.BIRCH_LOG).a(Blocks.BIRCH_WOOD);
		this.j(Blocks.STRIPPED_BIRCH_LOG).c(Blocks.STRIPPED_BIRCH_LOG).a(Blocks.STRIPPED_BIRCH_WOOD);
		this.a(Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING, ii.c.b);
		this.c(Blocks.BIRCH_LEAVES, jb_.n);
		this.f(Blocks.OAK_PLANKS).a(Blocks.OAK_BUTTON).c(Blocks.OAK_FENCE).d(Blocks.OAK_FENCE_GATE).e(Blocks.OAK_PRESSURE_PLATE).a(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN).f(Blocks.OAK_SLAB).f(Blocks.PETRIFIED_OAK_SLAB).g(Blocks.OAK_STAIRS);
		this.g(Blocks.OAK_DOOR);
		this.i(Blocks.OAK_TRAPDOOR);
		this.j(Blocks.OAK_LOG).c(Blocks.OAK_LOG).a(Blocks.OAK_WOOD);
		this.j(Blocks.STRIPPED_OAK_LOG).c(Blocks.STRIPPED_OAK_LOG).a(Blocks.STRIPPED_OAK_WOOD);
		this.a(Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING, ii.c.b);
		this.c(Blocks.OAK_LEAVES, jb_.n);
		this.f(Blocks.SPRUCE_PLANKS).a(Blocks.SPRUCE_BUTTON).c(Blocks.SPRUCE_FENCE).d(Blocks.SPRUCE_FENCE_GATE).e(Blocks.SPRUCE_PRESSURE_PLATE).a(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN).f(Blocks.SPRUCE_SLAB).g(Blocks.SPRUCE_STAIRS);
		this.g(Blocks.SPRUCE_DOOR);
		this.h(Blocks.SPRUCE_TRAPDOOR);
		this.j(Blocks.SPRUCE_LOG).c(Blocks.SPRUCE_LOG).a(Blocks.SPRUCE_WOOD);
		this.j(Blocks.STRIPPED_SPRUCE_LOG).c(Blocks.STRIPPED_SPRUCE_LOG).a(Blocks.STRIPPED_SPRUCE_WOOD);
		this.a(Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING, ii.c.b);
		this.c(Blocks.SPRUCE_LEAVES, jb_.n);
		this.f(Blocks.DARK_OAK_PLANKS).a(Blocks.DARK_OAK_BUTTON).c(Blocks.DARK_OAK_FENCE).d(Blocks.DARK_OAK_FENCE_GATE).e(Blocks.DARK_OAK_PRESSURE_PLATE).a(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN).f(Blocks.DARK_OAK_SLAB).g(Blocks.DARK_OAK_STAIRS);
		this.g(Blocks.DARK_OAK_DOOR);
		this.i(Blocks.DARK_OAK_TRAPDOOR);
		this.j(Blocks.DARK_OAK_LOG).c(Blocks.DARK_OAK_LOG).a(Blocks.DARK_OAK_WOOD);
		this.j(Blocks.STRIPPED_DARK_OAK_LOG).c(Blocks.STRIPPED_DARK_OAK_LOG).a(Blocks.STRIPPED_DARK_OAK_WOOD);
		this.a(Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, ii.c.b);
		this.c(Blocks.DARK_OAK_LEAVES, jb_.n);
		this.f(Blocks.JUNGLE_PLANKS).a(Blocks.JUNGLE_BUTTON).c(Blocks.JUNGLE_FENCE).d(Blocks.JUNGLE_FENCE_GATE).e(Blocks.JUNGLE_PRESSURE_PLATE).a(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN).f(Blocks.JUNGLE_SLAB).g(Blocks.JUNGLE_STAIRS);
		this.g(Blocks.JUNGLE_DOOR);
		this.h(Blocks.JUNGLE_TRAPDOOR);
		this.j(Blocks.JUNGLE_LOG).c(Blocks.JUNGLE_LOG).a(Blocks.JUNGLE_WOOD);
		this.j(Blocks.STRIPPED_JUNGLE_LOG).c(Blocks.STRIPPED_JUNGLE_LOG).a(Blocks.STRIPPED_JUNGLE_WOOD);
		this.a(Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, ii.c.b);
		this.c(Blocks.JUNGLE_LEAVES, jb_.n);
		this.f(Blocks.CRIMSON_PLANKS).a(Blocks.CRIMSON_BUTTON).c(Blocks.CRIMSON_FENCE).d(Blocks.CRIMSON_FENCE_GATE).e(Blocks.CRIMSON_PRESSURE_PLATE).a(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN).f(Blocks.CRIMSON_SLAB).g(Blocks.CRIMSON_STAIRS);
		this.g(Blocks.CRIMSON_DOOR);
		this.h(Blocks.CRIMSON_TRAPDOOR);
		this.j(Blocks.CRIMSON_STEM).b(Blocks.CRIMSON_STEM).a(Blocks.CRIMSON_HYPHAE);
		this.j(Blocks.STRIPPED_CRIMSON_STEM).b(Blocks.STRIPPED_CRIMSON_STEM).a(Blocks.STRIPPED_CRIMSON_HYPHAE);
		this.a(Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, ii.c.b);
		this.l(Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS);
		this.f(Blocks.WARPED_PLANKS).a(Blocks.WARPED_BUTTON).c(Blocks.WARPED_FENCE).d(Blocks.WARPED_FENCE_GATE).e(Blocks.WARPED_PRESSURE_PLATE).a(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN).f(Blocks.WARPED_SLAB).g(Blocks.WARPED_STAIRS);
		this.g(Blocks.WARPED_DOOR);
		this.h(Blocks.WARPED_TRAPDOOR);
		this.j(Blocks.WARPED_STEM).b(Blocks.WARPED_STEM).a(Blocks.WARPED_HYPHAE);
		this.j(Blocks.STRIPPED_WARPED_STEM).b(Blocks.STRIPPED_WARPED_STEM).a(Blocks.STRIPPED_WARPED_HYPHAE);
		this.a(Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, ii.c.b);
		this.l(Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
		this.b(Blocks.NETHER_SPROUTS, ii.c.b);
		this.a(Items.bA);
		this.a(iz_.a(Blocks.STONE)).a((iz) -> {
			MinecraftKey minecraftkey = iy.c.a(Blocks.STONE, iz, this.b);
			MinecraftKey minecraftkey1 = iy.d.a(Blocks.STONE, iz, this.b);

			this.a.accept(e(Blocks.STONE, minecraftkey, minecraftkey1));
			return minecraftkey;
		}).f(Blocks.STONE_SLAB).e(Blocks.STONE_PRESSURE_PLATE).a(Blocks.STONE_BUTTON).g(Blocks.STONE_STAIRS);
		this.g(Blocks.IRON_DOOR);
		this.i(Blocks.IRON_TRAPDOOR);
		this.f(Blocks.STONE_BRICKS).b(Blocks.STONE_BRICK_WALL).g(Blocks.STONE_BRICK_STAIRS).f(Blocks.STONE_BRICK_SLAB);
		this.f(Blocks.MOSSY_STONE_BRICKS).b(Blocks.MOSSY_STONE_BRICK_WALL).g(Blocks.MOSSY_STONE_BRICK_STAIRS).f(Blocks.MOSSY_STONE_BRICK_SLAB);
		this.f(Blocks.COBBLESTONE).b(Blocks.COBBLESTONE_WALL).g(Blocks.COBBLESTONE_STAIRS).f(Blocks.COBBLESTONE_SLAB);
		this.f(Blocks.MOSSY_COBBLESTONE).b(Blocks.MOSSY_COBBLESTONE_WALL).g(Blocks.MOSSY_COBBLESTONE_STAIRS).f(Blocks.MOSSY_COBBLESTONE_SLAB);
		this.f(Blocks.PRISMARINE).b(Blocks.PRISMARINE_WALL).g(Blocks.PRISMARINE_STAIRS).f(Blocks.PRISMARINE_SLAB);
		this.f(Blocks.PRISMARINE_BRICKS).g(Blocks.PRISMARINE_BRICK_STAIRS).f(Blocks.PRISMARINE_BRICK_SLAB);
		this.f(Blocks.DARK_PRISMARINE).g(Blocks.DARK_PRISMARINE_STAIRS).f(Blocks.DARK_PRISMARINE_SLAB);
		this.d(Blocks.SANDSTONE, jb_.t).b(Blocks.SANDSTONE_WALL).g(Blocks.SANDSTONE_STAIRS).f(Blocks.SANDSTONE_SLAB);
		this.a(Blocks.SMOOTH_SANDSTONE, jb_.a(iz_.a(Blocks.SANDSTONE, "_top"))).f(Blocks.SMOOTH_SANDSTONE_SLAB).g(Blocks.SMOOTH_SANDSTONE_STAIRS);
		this.a(Blocks.CUT_SANDSTONE, jb_.c.get(Blocks.SANDSTONE).a((iz) -> {
			iz.a(ja.i, iz_.C(Blocks.CUT_SANDSTONE));
		})).f(Blocks.CUT_SANDSTONE_SLAB);
		this.d(Blocks.RED_SANDSTONE, jb_.t).b(Blocks.RED_SANDSTONE_WALL).g(Blocks.RED_SANDSTONE_STAIRS).f(Blocks.RED_SANDSTONE_SLAB);
		this.a(Blocks.SMOOTH_RED_SANDSTONE, jb_.a(iz_.a(Blocks.RED_SANDSTONE, "_top"))).f(Blocks.SMOOTH_RED_SANDSTONE_SLAB).g(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
		this.a(Blocks.CUT_RED_SANDSTONE, jb_.c.get(Blocks.RED_SANDSTONE).a((iz) -> {
			iz.a(ja.i, iz_.C(Blocks.CUT_RED_SANDSTONE));
		})).f(Blocks.CUT_RED_SANDSTONE_SLAB);
		this.f(Blocks.BRICKS).b(Blocks.BRICK_WALL).g(Blocks.BRICK_STAIRS).f(Blocks.BRICK_SLAB);
		this.f(Blocks.NETHER_BRICKS).c(Blocks.NETHER_BRICK_FENCE).b(Blocks.NETHER_BRICK_WALL).g(Blocks.NETHER_BRICK_STAIRS).f(Blocks.NETHER_BRICK_SLAB);
		this.f(Blocks.PURPUR_BLOCK).g(Blocks.PURPUR_STAIRS).f(Blocks.PURPUR_SLAB);
		this.f(Blocks.DIORITE).b(Blocks.DIORITE_WALL).g(Blocks.DIORITE_STAIRS).f(Blocks.DIORITE_SLAB);
		this.f(Blocks.POLISHED_DIORITE).g(Blocks.POLISHED_DIORITE_STAIRS).f(Blocks.POLISHED_DIORITE_SLAB);
		this.f(Blocks.GRANITE).b(Blocks.GRANITE_WALL).g(Blocks.GRANITE_STAIRS).f(Blocks.GRANITE_SLAB);
		this.f(Blocks.POLISHED_GRANITE).g(Blocks.POLISHED_GRANITE_STAIRS).f(Blocks.POLISHED_GRANITE_SLAB);
		this.f(Blocks.ANDESITE).b(Blocks.ANDESITE_WALL).g(Blocks.ANDESITE_STAIRS).f(Blocks.ANDESITE_SLAB);
		this.f(Blocks.POLISHED_ANDESITE).g(Blocks.POLISHED_ANDESITE_STAIRS).f(Blocks.POLISHED_ANDESITE_SLAB);
		this.f(Blocks.END_STONE_BRICKS).b(Blocks.END_STONE_BRICK_WALL).g(Blocks.END_STONE_BRICK_STAIRS).f(Blocks.END_STONE_BRICK_SLAB);
		this.d(Blocks.QUARTZ_BLOCK, jb_.c).g(Blocks.QUARTZ_STAIRS).f(Blocks.QUARTZ_SLAB);
		this.a(Blocks.SMOOTH_QUARTZ, jb_.a(iz_.a(Blocks.QUARTZ_BLOCK, "_bottom"))).g(Blocks.SMOOTH_QUARTZ_STAIRS).f(Blocks.SMOOTH_QUARTZ_SLAB);
		this.f(Blocks.RED_NETHER_BRICKS).f(Blocks.RED_NETHER_BRICK_SLAB).g(Blocks.RED_NETHER_BRICK_STAIRS).b(Blocks.RED_NETHER_BRICK_WALL);
		this.d(Blocks.BLACKSTONE, jb_.u).b(Blocks.BLACKSTONE_WALL).g(Blocks.BLACKSTONE_STAIRS).f(Blocks.BLACKSTONE_SLAB);
		this.f(Blocks.POLISHED_BLACKSTONE_BRICKS).b(Blocks.POLISHED_BLACKSTONE_BRICK_WALL).g(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).f(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
		this.f(Blocks.POLISHED_BLACKSTONE).b(Blocks.POLISHED_BLACKSTONE_WALL).e(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE).a(Blocks.POLISHED_BLACKSTONE_BUTTON).g(Blocks.POLISHED_BLACKSTONE_STAIRS).f(Blocks.POLISHED_BLACKSTONE_SLAB);
		this.q();
		this.l(Blocks.RAIL);
		this.m(Blocks.POWERED_RAIL);
		this.m(Blocks.DETECTOR_RAIL);
		this.m(Blocks.ACTIVATOR_RAIL);
		this.p();
		this.n(Blocks.COMMAND_BLOCK);
		this.n(Blocks.REPEATING_COMMAND_BLOCK);
		this.n(Blocks.CHAIN_COMMAND_BLOCK);
		this.o(Blocks.ANVIL);
		this.o(Blocks.CHIPPED_ANVIL);
		this.o(Blocks.DAMAGED_ANVIL);
		this.k();
		this.l();
		this.e(Blocks.FURNACE, jb_.g);
		this.e(Blocks.BLAST_FURNACE, jb_.g);
		this.e(Blocks.SMOKER, jb_.h);
		this.o();
		this.ah();
		this.i(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
		this.i(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
		this.i(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
		this.i(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
		this.ag();
		this.i(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
		ItemMonsterEgg.f().forEach((itemmonsteregg) -> {
			this.a(itemmonsteregg, iw.b("template_spawn_egg"));
		});
	}

	enum c {

		a, b;

		c() {
		}

		public ix a() {
			return this == ii.c.a ? iy.T : iy.S;
		}

		public ix b() {
			return this == ii.c.a ? iy.V : iy.U;
		}
	}

	class a {

		private final MinecraftKey b;

		public a(MinecraftKey minecraftkey, Block block) {
			this.b = iy.F.a(minecraftkey, iz_.q(block), ii.this.b);
		}

		public ii.a a(Block... ablock) {
			int i = ablock.length;

			for (int j = 0; j < i; ++j) {
				Block block = ablock[j];

				ii.this.a.accept(ii.e(block, this.b));
			}

			return this;
		}

		public ii.a b(Block... ablock) {
			int i = ablock.length;

			for (int j = 0; j < i; ++j) {
				Block block = ablock[j];

				ii.this.a(block);
			}

			return this.a(ablock);
		}

		public ii.a a(ix ix, Block... ablock) {
			int i = ablock.length;

			for (int j = 0; j < i; ++j) {
				Block block = ablock[j];

				ix.a(iw.a(block.getItem()), iz_.q(block), ii.this.b);
			}

			return this.a(ablock);
		}
	}

	class d {

		private final iz_ b;

		public d(iz_ iz) {
			this.b = iz;
		}

		public ii.d a(Block block) {
			iz_ iz = this.b.c(ja.d, this.b.a(ja.i));
			MinecraftKey minecraftkey = iy.e.a(block, iz, ii.this.b);

			ii.this.a.accept(ii.f(block, minecraftkey));
			return this;
		}

		public ii.d b(Block block) {
			MinecraftKey minecraftkey = iy.e.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.f(block, minecraftkey));
			return this;
		}

		public ii.d c(Block block) {
			MinecraftKey minecraftkey = iy.e.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.f.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.h(block, minecraftkey, minecraftkey1));
			return this;
		}
	}

	class b {

		private final iz_ b;
		@Nullable
		private MinecraftKey c;

		public b(iz_ iz) {
			this.b = iz;
		}

		public ii.b a(Block block, ix ix) {
			this.c = ix.a(block, this.b, ii.this.b);
			ii.this.a.accept(ii.e(block, this.c));
			return this;
		}

		public ii.b a(Function<iz_, MinecraftKey> function) {
			this.c = function.apply(this.b);
			return this;
		}

		public ii.b a(Block block) {
			MinecraftKey minecraftkey = iy.l.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.m.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.f(block, minecraftkey, minecraftkey1));
			MinecraftKey minecraftkey2 = iy.n.a(block, this.b, ii.this.b);

			ii.this.c(block, minecraftkey2);
			return this;
		}

		public ii.b b(Block block) {
			MinecraftKey minecraftkey = iy.v.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.w.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey2 = iy.x.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.d(block, minecraftkey, minecraftkey1, minecraftkey2));
			MinecraftKey minecraftkey3 = iy.y.a(block, this.b, ii.this.b);

			ii.this.c(block, minecraftkey3);
			return this;
		}

		public ii.b c(Block block) {
			MinecraftKey minecraftkey = iy.s.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.t.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.g(block, minecraftkey, minecraftkey1));
			MinecraftKey minecraftkey2 = iy.u.a(block, this.b, ii.this.b);

			ii.this.c(block, minecraftkey2);
			return this;
		}

		public ii.b d(Block block) {
			MinecraftKey minecraftkey = iy.A.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.z.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey2 = iy.C.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey3 = iy.B.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.c(block, minecraftkey, minecraftkey1, minecraftkey2, minecraftkey3));
			return this;
		}

		public ii.b e(Block block) {
			MinecraftKey minecraftkey = iy.D.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.E.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.i(block, minecraftkey, minecraftkey1));
			return this;
		}

		public ii.b a(Block block, Block block1) {
			MinecraftKey minecraftkey = iy.F.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.e(block, minecraftkey));
			ii.this.a.accept(ii.e(block1, minecraftkey));
			ii.this.a(block.getItem());
			ii.this.a(block1);
			return this;
		}

		public ii.b f(Block block) {
			if (this.c == null) {
				throw new IllegalStateException("Full block not generated yet");
			} else {
				MinecraftKey minecraftkey = iy.G.a(block, this.b, ii.this.b);
				MinecraftKey minecraftkey1 = iy.H.a(block, this.b, ii.this.b);

				ii.this.a.accept(ii.h(block, minecraftkey, minecraftkey1, this.c));
				return this;
			}
		}

		public ii.b g(Block block) {
			MinecraftKey minecraftkey = iy.K.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey1 = iy.J.a(block, this.b, ii.this.b);
			MinecraftKey minecraftkey2 = iy.L.a(block, this.b, ii.this.b);

			ii.this.a.accept(ii.e(block, minecraftkey, minecraftkey1, minecraftkey2));
			return this;
		}
	}
}
