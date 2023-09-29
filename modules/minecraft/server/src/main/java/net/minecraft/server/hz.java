package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.INamable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockPropertyBedPart;
import net.minecraft.world.level.block.state.properties.BlockPropertyDoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootEntryAbstract;
import net.minecraft.world.level.storage.loot.entries.LootEntryAlternatives;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootSelectorDynamic;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class hz implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {
	private static final LootItemCondition.a HAS_SILK_TOUCH = LootItemConditionMatchTool.a(CriterionConditionItem.a_.a().a(new CriterionConditionEnchantments(Enchantments.SILK_TOUCH, CriterionConditionValue.IntegerRange.b(1))));
	private static final LootItemCondition.a HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.a();
	private static final LootItemCondition.a HAS_SHEARS = LootItemConditionMatchTool.a(CriterionConditionItem.a_.a().a(Items.SHEARS));
	private static final LootItemCondition.a HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.a(HAS_SILK_TOUCH);
	private static final LootItemCondition.a HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.a();
	private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(
		Blocks.DRAGON_EGG,
		Blocks.BEACON,
		Blocks.CONDUIT,
		Blocks.SKELETON_SKULL,
		Blocks.WITHER_SKELETON_SKULL,
		Blocks.PLAYER_HEAD,
		Blocks.ZOMBIE_HEAD,
		Blocks.CREEPER_HEAD,
		Blocks.DRAGON_HEAD,
		Blocks.SHULKER_BOX,
		Blocks.BLACK_SHULKER_BOX,
		Blocks.BLUE_SHULKER_BOX,
		Blocks.BROWN_SHULKER_BOX,
		Blocks.CYAN_SHULKER_BOX,
		Blocks.GRAY_SHULKER_BOX,
		Blocks.GREEN_SHULKER_BOX,
		Blocks.LIGHT_BLUE_SHULKER_BOX,
		Blocks.LIGHT_GRAY_SHULKER_BOX,
		Blocks.LIME_SHULKER_BOX,
		Blocks.MAGENTA_SHULKER_BOX,
		Blocks.ORANGE_SHULKER_BOX,
		Blocks.PINK_SHULKER_BOX,
		Blocks.PURPLE_SHULKER_BOX,
		Blocks.RED_SHULKER_BOX,
		Blocks.WHITE_SHULKER_BOX,
		Blocks.YELLOW_SHULKER_BOX
	).map(IMaterial::getItem).collect(ImmutableSet.toImmutableSet());
	private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05f, 0.0625f, 0.083333336f, 0.1f};
	private static final float[] JUNGLE_LEAVES_SAPLING_CHANGES = new float[]{0.025f, 0.027777778f, 0.03125f, 0.041666668f, 0.1f};
	private final Map<MinecraftKey, LootTable.a> map = new HashMap<>();

	private static <T> T applyExplosionDecay(IMaterial itemLike, LootItemFunctionUser<T> functionUserBuilder) {
		if (!EXPLOSION_RESISTANT.contains(itemLike.getItem())) {
			return functionUserBuilder.b(LootItemFunctionExplosionDecay.c());
		}
		return functionUserBuilder.c();
	}

	private static <T> T applyExplosionCondition(IMaterial itemLike, LootItemConditionUser<T> conditionUserBuilder) {
		if (!EXPLOSION_RESISTANT.contains(itemLike.getItem())) {
			return conditionUserBuilder.b(LootItemConditionSurvivesExplosion.c());
		}
		return conditionUserBuilder.c();
	}

	private static LootTable.a createSingleItemTable(IMaterial itemLike) {
		return LootTable.b().a(hz.applyExplosionCondition(itemLike, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(itemLike))));
	}

	private static LootTable.a createSelfDropDispatchTable(Block block, LootItemCondition.a builder, LootEntryAbstract.a<?> builder2) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(block).b(builder).a(builder2)));
	}

	private static LootTable.a createSilkTouchDispatchTable(Block block, LootEntryAbstract.a<?> builder) {
		return hz.createSelfDropDispatchTable(block, HAS_SILK_TOUCH, builder);
	}

	private static LootTable.a createShearsDispatchTable(Block block, LootEntryAbstract.a<?> builder) {
		return hz.createSelfDropDispatchTable(block, HAS_SHEARS, builder);
	}

	private static LootTable.a createSilkTouchOrShearsDispatchTable(Block block, LootEntryAbstract.a<?> builder) {
		return hz.createSelfDropDispatchTable(block, HAS_SHEARS_OR_SILK_TOUCH, builder);
	}

	private static LootTable.a createSingleItemTableWithSilkTouch(Block block, IMaterial itemLike) {
		return hz.createSilkTouchDispatchTable(block, hz.applyExplosionCondition(block, LootItem.a(itemLike)));
	}

	private static LootTable.a createSingleItemTable(LootValue randomIntGenerator) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(hz.applyExplosionDecay(Items.CHORUS_FRUIT, LootItem.a(Items.CHORUS_FRUIT).b(LootItemFunctionSetCount.a(randomIntGenerator)))));
	}

	private static LootTable.a createSingleItemTableWithSilkTouch(Block block, IMaterial itemLike, LootValue randomIntGenerator) {
		return hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, LootItem.a(itemLike).b(LootItemFunctionSetCount.a(randomIntGenerator))));
	}

	private static LootTable.a createSilkTouchOnlyTable(IMaterial itemLike) {
		return LootTable.b().a(LootSelector.a().b(HAS_SILK_TOUCH).a(LootValueConstant.a(1)).a(LootItem.a(itemLike)));
	}

	private static LootTable.a createPotFlowerItemTable(IMaterial itemLike) {
		return LootTable.b().a(hz.applyExplosionCondition(Blocks.FLOWER_POT, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.FLOWER_POT)))).a(hz.applyExplosionCondition(itemLike, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(itemLike))));
	}

	private static LootTable.a createSlabItemTable(Block block) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(hz.applyExplosionDecay(block, LootItem.a(block).b(LootItemFunctionSetCount.a(LootValueConstant.a(2)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockStepAbstract.a, BlockPropertySlabType.DOUBLE)))))));
	}

	private static <T extends Comparable<T> & INamable> LootTable.a createSinglePropConditionTable(Block block, IBlockState<T> property, T t) {
		return LootTable.b().a(hz.applyExplosionCondition(block, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(block).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(property, t))))));
	}

	private static LootTable.a createNameableBlockEntityTable(Block block) {
		return LootTable.b().a(hz.applyExplosionCondition(block, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(block).b(LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY)))));
	}

	private static LootTable.a createShulkerBoxDrop(Block block) {
		return LootTable.b().a(hz.applyExplosionCondition(block, LootSelector.a().a(LootValueConstant.a(1)).a(((LootItem.a(block).b(LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY))).b(LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Lock", "BlockEntityTag.Lock").a("LootTable", "BlockEntityTag.LootTable").a("LootTableSeed", "BlockEntityTag.LootTableSeed"))).b(LootItemFunctionSetContents.c().a(LootSelectorDynamic.a(BlockShulkerBox.b))))));
	}

	private static LootTable.a createBannerDrop(Block block) {
		return LootTable.b().a(hz.applyExplosionCondition(block, LootSelector.a().a(LootValueConstant.a(1)).a((LootItem.a(block).b(LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY))).b(LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Patterns", "BlockEntityTag.Patterns")))));
	}

	private static LootTable.a createBeeNestDrop(Block block) {
		return LootTable.b().a(LootSelector.a().b(HAS_SILK_TOUCH).a(LootValueConstant.a(1)).a((LootItem.a(block).b(LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Bees", "BlockEntityTag.Bees"))).b(LootItemFunctionCopyState.a(block).a(BlockBeehive.a))));
	}

	private static LootTable.a createBeeHiveDrop(Block block) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a((((LootItem.a(block).b(HAS_SILK_TOUCH)).b(LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Bees", "BlockEntityTag.Bees"))).b(LootItemFunctionCopyState.a(block).a(BlockBeehive.b))).a(LootItem.a(block))));
	}

	private static LootTable.a createOreDrop(Block block, Item item) {
		return hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, LootItem.a(item).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS))));
	}

	private static LootTable.a createMushroomBlockDrop(Block block, IMaterial itemLike) {
		return hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, (LootItem.a(itemLike).b(LootItemFunctionSetCount.a(LootValueBounds.a(-6.0f, 2.0f)))).b(LootItemFunctionLimitCount.a(LootIntegerLimit.a(0)))));
	}

	private static LootTable.a createGrassDrops(Block block) {
		return hz.createShearsDispatchTable(block, hz.applyExplosionDecay(block, (LootItem.a(Items.WHEAT_SEEDS).b(LootItemConditionRandomChance.a(0.125f))).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 2))));
	}

	private static LootTable.a createStemDrops(Block var0, Item var1) {
		return LootTable.b().a(applyExplosionDecay(var0, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(var1).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.06666667F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 0)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.13333334F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 1)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.2F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 2)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.26666668F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 3)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.33333334F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 4)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.4F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 5)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.46666667F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 6)))).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.53333336F)).b(LootItemConditionBlockStateProperty.a(var0).a(CriterionTriggerProperties.a_.a().a(BlockStem.AGE, 7)))))));
	}

	private static LootTable.a createAttachedStemDrops(Block block, Item item) {
		return LootTable.b().a(hz.applyExplosionDecay(block, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(item).b(LootItemFunctionSetCount.a(LootValueBinomial.a(3, 0.53333336f))))));
	}

	private static LootTable.a createShearsOnlyDrop(IMaterial itemLike) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).b(HAS_SHEARS).a(LootItem.a(itemLike)));
	}

	private static LootTable.a createLeavesDrops(Block block, Block block2, float... fArray) {
		return hz.createSilkTouchOrShearsDispatchTable(block, (hz.applyExplosionCondition(block, LootItem.a(block2))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, fArray))).a(LootSelector.a().a(LootValueConstant.a(1)).b(HAS_NO_SHEARS_OR_SILK_TOUCH).a((hz.applyExplosionDecay(block, LootItem.a(Items.STICK).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0f, 2.0f))))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))));
	}

	private static LootTable.a createOakLeavesDrops(Block block, Block block2, float... fArray) {
		return hz.createLeavesDrops(block, block2, fArray).a(LootSelector.a().a(LootValueConstant.a(1)).b(HAS_NO_SHEARS_OR_SILK_TOUCH).a((hz.applyExplosionCondition(block, LootItem.a(Items.APPLE))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.005f, 0.0055555557f, 0.00625f, 0.008333334f, 0.025f))));
	}

	private static LootTable.a createCropDrops(Block block, Item item, Item item2, LootItemCondition.a builder) {
		return hz.applyExplosionDecay(block, LootTable.b().a(LootSelector.a().a((LootItem.a(item).b(builder)).a(LootItem.a(item2)))).a(LootSelector.a().b(builder).a(LootItem.a(item2).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286f, 3)))));
	}

	private static LootTable.a createDoublePlantShearsDrop() {
		return LootTable.b().a(LootSelector.a().b(HAS_SHEARS).a(LootItem.a(Blocks.SEAGRASS).b(LootItemFunctionSetCount.a(LootValueConstant.a(2)))));
	}

	private static LootTable.a createDoublePlantWithSeedDrops(Block block, Block block2) {
		LootEntryAlternatives.a builder = ((LootItem.a(block2).b(LootItemFunctionSetCount.a(LootValueConstant.a(2)))).b(HAS_SHEARS)).a((hz.applyExplosionCondition(block, LootItem.a(Items.WHEAT_SEEDS))).b(LootItemConditionRandomChance.a(0.125f)));
		return LootTable.b().a(LootSelector.a().a(builder).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER))).b(LootItemConditionLocationCheck.a(CriterionConditionLocation.a_.a().a(CriterionConditionBlock.a_.a().a(block).a(CriterionTriggerProperties.a_.a().a(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.UPPER).b()).b()), new BlockPosition(0, 1, 0)))).a(LootSelector.a().a(builder).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.UPPER))).b(LootItemConditionLocationCheck.a(CriterionConditionLocation.a_.a().a(CriterionConditionBlock.a_.a().a(block).a(CriterionTriggerProperties.a_.a().a(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER).b()).b()), new BlockPosition(0, -1, 0))));
	}

	public static LootTable.a noDrop() {
		return LootTable.b();
	}

	public static LootTable.a createDoorTable(Block block) {
		return hz.createSinglePropConditionTable(block, BlockDoor.HALF, BlockPropertyDoubleBlockHalf.LOWER);
	}

	@Override
	public void accept(BiConsumer<MinecraftKey, LootTable.a> biConsumer) {
		this.dropSelf(Blocks.GRANITE);
		this.dropSelf(Blocks.POLISHED_GRANITE);
		this.dropSelf(Blocks.DIORITE);
		this.dropSelf(Blocks.POLISHED_DIORITE);
		this.dropSelf(Blocks.ANDESITE);
		this.dropSelf(Blocks.POLISHED_ANDESITE);
		this.dropSelf(Blocks.DIRT);
		this.dropSelf(Blocks.COARSE_DIRT);
		this.dropSelf(Blocks.COBBLESTONE);
		this.dropSelf(Blocks.OAK_PLANKS);
		this.dropSelf(Blocks.SPRUCE_PLANKS);
		this.dropSelf(Blocks.BIRCH_PLANKS);
		this.dropSelf(Blocks.JUNGLE_PLANKS);
		this.dropSelf(Blocks.ACACIA_PLANKS);
		this.dropSelf(Blocks.DARK_OAK_PLANKS);
		this.dropSelf(Blocks.OAK_SAPLING);
		this.dropSelf(Blocks.SPRUCE_SAPLING);
		this.dropSelf(Blocks.BIRCH_SAPLING);
		this.dropSelf(Blocks.JUNGLE_SAPLING);
		this.dropSelf(Blocks.ACACIA_SAPLING);
		this.dropSelf(Blocks.DARK_OAK_SAPLING);
		this.dropSelf(Blocks.SAND);
		this.dropSelf(Blocks.RED_SAND);
		this.dropSelf(Blocks.GOLD_ORE);
		this.dropSelf(Blocks.IRON_ORE);
		this.dropSelf(Blocks.OAK_LOG);
		this.dropSelf(Blocks.SPRUCE_LOG);
		this.dropSelf(Blocks.BIRCH_LOG);
		this.dropSelf(Blocks.JUNGLE_LOG);
		this.dropSelf(Blocks.ACACIA_LOG);
		this.dropSelf(Blocks.DARK_OAK_LOG);
		this.dropSelf(Blocks.STRIPPED_SPRUCE_LOG);
		this.dropSelf(Blocks.STRIPPED_BIRCH_LOG);
		this.dropSelf(Blocks.STRIPPED_JUNGLE_LOG);
		this.dropSelf(Blocks.STRIPPED_ACACIA_LOG);
		this.dropSelf(Blocks.STRIPPED_DARK_OAK_LOG);
		this.dropSelf(Blocks.STRIPPED_OAK_LOG);
		this.dropSelf(Blocks.STRIPPED_WARPED_STEM);
		this.dropSelf(Blocks.STRIPPED_CRIMSON_STEM);
		this.dropSelf(Blocks.OAK_WOOD);
		this.dropSelf(Blocks.SPRUCE_WOOD);
		this.dropSelf(Blocks.BIRCH_WOOD);
		this.dropSelf(Blocks.JUNGLE_WOOD);
		this.dropSelf(Blocks.ACACIA_WOOD);
		this.dropSelf(Blocks.DARK_OAK_WOOD);
		this.dropSelf(Blocks.STRIPPED_OAK_WOOD);
		this.dropSelf(Blocks.STRIPPED_SPRUCE_WOOD);
		this.dropSelf(Blocks.STRIPPED_BIRCH_WOOD);
		this.dropSelf(Blocks.STRIPPED_JUNGLE_WOOD);
		this.dropSelf(Blocks.STRIPPED_ACACIA_WOOD);
		this.dropSelf(Blocks.STRIPPED_DARK_OAK_WOOD);
		this.dropSelf(Blocks.STRIPPED_CRIMSON_HYPHAE);
		this.dropSelf(Blocks.STRIPPED_WARPED_HYPHAE);
		this.dropSelf(Blocks.SPONGE);
		this.dropSelf(Blocks.WET_SPONGE);
		this.dropSelf(Blocks.LAPIS_BLOCK);
		this.dropSelf(Blocks.SANDSTONE);
		this.dropSelf(Blocks.CHISELED_SANDSTONE);
		this.dropSelf(Blocks.CUT_SANDSTONE);
		this.dropSelf(Blocks.NOTE_BLOCK);
		this.dropSelf(Blocks.POWERED_RAIL);
		this.dropSelf(Blocks.DETECTOR_RAIL);
		this.dropSelf(Blocks.STICKY_PISTON);
		this.dropSelf(Blocks.PISTON);
		this.dropSelf(Blocks.WHITE_WOOL);
		this.dropSelf(Blocks.ORANGE_WOOL);
		this.dropSelf(Blocks.MAGENTA_WOOL);
		this.dropSelf(Blocks.LIGHT_BLUE_WOOL);
		this.dropSelf(Blocks.YELLOW_WOOL);
		this.dropSelf(Blocks.LIME_WOOL);
		this.dropSelf(Blocks.PINK_WOOL);
		this.dropSelf(Blocks.GRAY_WOOL);
		this.dropSelf(Blocks.LIGHT_GRAY_WOOL);
		this.dropSelf(Blocks.CYAN_WOOL);
		this.dropSelf(Blocks.PURPLE_WOOL);
		this.dropSelf(Blocks.BLUE_WOOL);
		this.dropSelf(Blocks.BROWN_WOOL);
		this.dropSelf(Blocks.GREEN_WOOL);
		this.dropSelf(Blocks.RED_WOOL);
		this.dropSelf(Blocks.BLACK_WOOL);
		this.dropSelf(Blocks.DANDELION);
		this.dropSelf(Blocks.POPPY);
		this.dropSelf(Blocks.BLUE_ORCHID);
		this.dropSelf(Blocks.ALLIUM);
		this.dropSelf(Blocks.AZURE_BLUET);
		this.dropSelf(Blocks.RED_TULIP);
		this.dropSelf(Blocks.ORANGE_TULIP);
		this.dropSelf(Blocks.WHITE_TULIP);
		this.dropSelf(Blocks.PINK_TULIP);
		this.dropSelf(Blocks.OXEYE_DAISY);
		this.dropSelf(Blocks.CORNFLOWER);
		this.dropSelf(Blocks.WITHER_ROSE);
		this.dropSelf(Blocks.LILY_OF_THE_VALLEY);
		this.dropSelf(Blocks.BROWN_MUSHROOM);
		this.dropSelf(Blocks.RED_MUSHROOM);
		this.dropSelf(Blocks.GOLD_BLOCK);
		this.dropSelf(Blocks.IRON_BLOCK);
		this.dropSelf(Blocks.BRICKS);
		this.dropSelf(Blocks.MOSSY_COBBLESTONE);
		this.dropSelf(Blocks.OBSIDIAN);
		this.dropSelf(Blocks.CRYING_OBSIDIAN);
		this.dropSelf(Blocks.TORCH);
		this.dropSelf(Blocks.OAK_STAIRS);
		this.dropSelf(Blocks.REDSTONE_WIRE);
		this.dropSelf(Blocks.DIAMOND_BLOCK);
		this.dropSelf(Blocks.CRAFTING_TABLE);
		this.dropSelf(Blocks.OAK_SIGN);
		this.dropSelf(Blocks.SPRUCE_SIGN);
		this.dropSelf(Blocks.BIRCH_SIGN);
		this.dropSelf(Blocks.ACACIA_SIGN);
		this.dropSelf(Blocks.JUNGLE_SIGN);
		this.dropSelf(Blocks.DARK_OAK_SIGN);
		this.dropSelf(Blocks.LADDER);
		this.dropSelf(Blocks.RAIL);
		this.dropSelf(Blocks.COBBLESTONE_STAIRS);
		this.dropSelf(Blocks.LEVER);
		this.dropSelf(Blocks.STONE_PRESSURE_PLATE);
		this.dropSelf(Blocks.OAK_PRESSURE_PLATE);
		this.dropSelf(Blocks.SPRUCE_PRESSURE_PLATE);
		this.dropSelf(Blocks.BIRCH_PRESSURE_PLATE);
		this.dropSelf(Blocks.JUNGLE_PRESSURE_PLATE);
		this.dropSelf(Blocks.ACACIA_PRESSURE_PLATE);
		this.dropSelf(Blocks.DARK_OAK_PRESSURE_PLATE);
		this.dropSelf(Blocks.REDSTONE_TORCH);
		this.dropSelf(Blocks.STONE_BUTTON);
		this.dropSelf(Blocks.CACTUS);
		this.dropSelf(Blocks.SUGAR_CANE);
		this.dropSelf(Blocks.JUKEBOX);
		this.dropSelf(Blocks.OAK_FENCE);
		this.dropSelf(Blocks.PUMPKIN);
		this.dropSelf(Blocks.NETHERRACK);
		this.dropSelf(Blocks.SOUL_SAND);
		this.dropSelf(Blocks.SOUL_SOIL);
		this.dropSelf(Blocks.BASALT);
		this.dropSelf(Blocks.cP);
		this.dropSelf(Blocks.SOUL_TORCH);
		this.dropSelf(Blocks.CARVED_PUMPKIN);
		this.dropSelf(Blocks.JACK_O_LANTERN);
		this.dropSelf(Blocks.REPEATER);
		this.dropSelf(Blocks.OAK_TRAPDOOR);
		this.dropSelf(Blocks.SPRUCE_TRAPDOOR);
		this.dropSelf(Blocks.BIRCH_TRAPDOOR);
		this.dropSelf(Blocks.JUNGLE_TRAPDOOR);
		this.dropSelf(Blocks.ACACIA_TRAPDOOR);
		this.dropSelf(Blocks.DARK_OAK_TRAPDOOR);
		this.dropSelf(Blocks.STONE_BRICKS);
		this.dropSelf(Blocks.MOSSY_STONE_BRICKS);
		this.dropSelf(Blocks.CRACKED_STONE_BRICKS);
		this.dropSelf(Blocks.CHISELED_STONE_BRICKS);
		this.dropSelf(Blocks.IRON_BARS);
		this.dropSelf(Blocks.OAK_FENCE_GATE);
		this.dropSelf(Blocks.BRICK_STAIRS);
		this.dropSelf(Blocks.STONE_BRICK_STAIRS);
		this.dropSelf(Blocks.LILY_PAD);
		this.dropSelf(Blocks.NETHER_BRICKS);
		this.dropSelf(Blocks.NETHER_BRICK_FENCE);
		this.dropSelf(Blocks.NETHER_BRICK_STAIRS);
		this.dropSelf(Blocks.CAULDRON);
		this.dropSelf(Blocks.END_STONE);
		this.dropSelf(Blocks.REDSTONE_LAMP);
		this.dropSelf(Blocks.SANDSTONE_STAIRS);
		this.dropSelf(Blocks.TRIPWIRE_HOOK);
		this.dropSelf(Blocks.EMERALD_BLOCK);
		this.dropSelf(Blocks.SPRUCE_STAIRS);
		this.dropSelf(Blocks.BIRCH_STAIRS);
		this.dropSelf(Blocks.JUNGLE_STAIRS);
		this.dropSelf(Blocks.COBBLESTONE_WALL);
		this.dropSelf(Blocks.MOSSY_COBBLESTONE_WALL);
		this.dropSelf(Blocks.FLOWER_POT);
		this.dropSelf(Blocks.OAK_BUTTON);
		this.dropSelf(Blocks.SPRUCE_BUTTON);
		this.dropSelf(Blocks.BIRCH_BUTTON);
		this.dropSelf(Blocks.JUNGLE_BUTTON);
		this.dropSelf(Blocks.ACACIA_BUTTON);
		this.dropSelf(Blocks.DARK_OAK_BUTTON);
		this.dropSelf(Blocks.SKELETON_SKULL);
		this.dropSelf(Blocks.WITHER_SKELETON_SKULL);
		this.dropSelf(Blocks.ZOMBIE_HEAD);
		this.dropSelf(Blocks.CREEPER_HEAD);
		this.dropSelf(Blocks.DRAGON_HEAD);
		this.dropSelf(Blocks.ANVIL);
		this.dropSelf(Blocks.CHIPPED_ANVIL);
		this.dropSelf(Blocks.DAMAGED_ANVIL);
		this.dropSelf(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
		this.dropSelf(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
		this.dropSelf(Blocks.COMPARATOR);
		this.dropSelf(Blocks.DAYLIGHT_DETECTOR);
		this.dropSelf(Blocks.REDSTONE_BLOCK);
		this.dropSelf(Blocks.QUARTZ_BLOCK);
		this.dropSelf(Blocks.CHISELED_QUARTZ_BLOCK);
		this.dropSelf(Blocks.QUARTZ_PILLAR);
		this.dropSelf(Blocks.QUARTZ_STAIRS);
		this.dropSelf(Blocks.ACTIVATOR_RAIL);
		this.dropSelf(Blocks.WHITE_TERRACOTTA);
		this.dropSelf(Blocks.ORANGE_TERRACOTTA);
		this.dropSelf(Blocks.MAGENTA_TERRACOTTA);
		this.dropSelf(Blocks.LIGHT_BLUE_TERRACOTTA);
		this.dropSelf(Blocks.YELLOW_TERRACOTTA);
		this.dropSelf(Blocks.LIME_TERRACOTTA);
		this.dropSelf(Blocks.PINK_TERRACOTTA);
		this.dropSelf(Blocks.GRAY_TERRACOTTA);
		this.dropSelf(Blocks.LIGHT_GRAY_TERRACOTTA);
		this.dropSelf(Blocks.CYAN_TERRACOTTA);
		this.dropSelf(Blocks.PURPLE_TERRACOTTA);
		this.dropSelf(Blocks.BLUE_TERRACOTTA);
		this.dropSelf(Blocks.BROWN_TERRACOTTA);
		this.dropSelf(Blocks.GREEN_TERRACOTTA);
		this.dropSelf(Blocks.RED_TERRACOTTA);
		this.dropSelf(Blocks.BLACK_TERRACOTTA);
		this.dropSelf(Blocks.ACACIA_STAIRS);
		this.dropSelf(Blocks.DARK_OAK_STAIRS);
		this.dropSelf(Blocks.SLIME_BLOCK);
		this.dropSelf(Blocks.IRON_TRAPDOOR);
		this.dropSelf(Blocks.PRISMARINE);
		this.dropSelf(Blocks.PRISMARINE_BRICKS);
		this.dropSelf(Blocks.DARK_PRISMARINE);
		this.dropSelf(Blocks.PRISMARINE_STAIRS);
		this.dropSelf(Blocks.PRISMARINE_BRICK_STAIRS);
		this.dropSelf(Blocks.DARK_PRISMARINE_STAIRS);
		this.dropSelf(Blocks.HAY_BLOCK);
		this.dropSelf(Blocks.WHITE_CARPET);
		this.dropSelf(Blocks.ORANGE_CARPET);
		this.dropSelf(Blocks.MAGENTA_CARPET);
		this.dropSelf(Blocks.LIGHT_BLUE_CARPET);
		this.dropSelf(Blocks.YELLOW_CARPET);
		this.dropSelf(Blocks.LIME_CARPET);
		this.dropSelf(Blocks.PINK_CARPET);
		this.dropSelf(Blocks.GRAY_CARPET);
		this.dropSelf(Blocks.LIGHT_GRAY_CARPET);
		this.dropSelf(Blocks.CYAN_CARPET);
		this.dropSelf(Blocks.PURPLE_CARPET);
		this.dropSelf(Blocks.BLUE_CARPET);
		this.dropSelf(Blocks.BROWN_CARPET);
		this.dropSelf(Blocks.GREEN_CARPET);
		this.dropSelf(Blocks.RED_CARPET);
		this.dropSelf(Blocks.BLACK_CARPET);
		this.dropSelf(Blocks.TERRACOTTA);
		this.dropSelf(Blocks.COAL_BLOCK);
		this.dropSelf(Blocks.RED_SANDSTONE);
		this.dropSelf(Blocks.CHISELED_RED_SANDSTONE);
		this.dropSelf(Blocks.CUT_RED_SANDSTONE);
		this.dropSelf(Blocks.RED_SANDSTONE_STAIRS);
		this.dropSelf(Blocks.SMOOTH_STONE);
		this.dropSelf(Blocks.SMOOTH_SANDSTONE);
		this.dropSelf(Blocks.SMOOTH_QUARTZ);
		this.dropSelf(Blocks.SMOOTH_RED_SANDSTONE);
		this.dropSelf(Blocks.SPRUCE_FENCE_GATE);
		this.dropSelf(Blocks.BIRCH_FENCE_GATE);
		this.dropSelf(Blocks.JUNGLE_FENCE_GATE);
		this.dropSelf(Blocks.ACACIA_FENCE_GATE);
		this.dropSelf(Blocks.DARK_OAK_FENCE_GATE);
		this.dropSelf(Blocks.SPRUCE_FENCE);
		this.dropSelf(Blocks.BIRCH_FENCE);
		this.dropSelf(Blocks.JUNGLE_FENCE);
		this.dropSelf(Blocks.ACACIA_FENCE);
		this.dropSelf(Blocks.DARK_OAK_FENCE);
		this.dropSelf(Blocks.END_ROD);
		this.dropSelf(Blocks.PURPUR_BLOCK);
		this.dropSelf(Blocks.PURPUR_PILLAR);
		this.dropSelf(Blocks.PURPUR_STAIRS);
		this.dropSelf(Blocks.END_STONE_BRICKS);
		this.dropSelf(Blocks.MAGMA_BLOCK);
		this.dropSelf(Blocks.NETHER_WART_BLOCK);
		this.dropSelf(Blocks.RED_NETHER_BRICKS);
		this.dropSelf(Blocks.BONE_BLOCK);
		this.dropSelf(Blocks.OBSERVER);
		this.dropSelf(Blocks.TARGET);
		this.dropSelf(Blocks.WHITE_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.ORANGE_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.MAGENTA_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.YELLOW_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.LIME_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.PINK_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.GRAY_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.CYAN_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.PURPLE_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.BLUE_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.BROWN_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.GREEN_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.RED_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.BLACK_GLAZED_TERRACOTTA);
		this.dropSelf(Blocks.WHITE_CONCRETE);
		this.dropSelf(Blocks.ORANGE_CONCRETE);
		this.dropSelf(Blocks.MAGENTA_CONCRETE);
		this.dropSelf(Blocks.LIGHT_BLUE_CONCRETE);
		this.dropSelf(Blocks.YELLOW_CONCRETE);
		this.dropSelf(Blocks.LIME_CONCRETE);
		this.dropSelf(Blocks.PINK_CONCRETE);
		this.dropSelf(Blocks.GRAY_CONCRETE);
		this.dropSelf(Blocks.LIGHT_GRAY_CONCRETE);
		this.dropSelf(Blocks.CYAN_CONCRETE);
		this.dropSelf(Blocks.PURPLE_CONCRETE);
		this.dropSelf(Blocks.BLUE_CONCRETE);
		this.dropSelf(Blocks.BROWN_CONCRETE);
		this.dropSelf(Blocks.GREEN_CONCRETE);
		this.dropSelf(Blocks.RED_CONCRETE);
		this.dropSelf(Blocks.BLACK_CONCRETE);
		this.dropSelf(Blocks.WHITE_CONCRETE_POWDER);
		this.dropSelf(Blocks.ORANGE_CONCRETE_POWDER);
		this.dropSelf(Blocks.MAGENTA_CONCRETE_POWDER);
		this.dropSelf(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
		this.dropSelf(Blocks.YELLOW_CONCRETE_POWDER);
		this.dropSelf(Blocks.LIME_CONCRETE_POWDER);
		this.dropSelf(Blocks.PINK_CONCRETE_POWDER);
		this.dropSelf(Blocks.GRAY_CONCRETE_POWDER);
		this.dropSelf(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
		this.dropSelf(Blocks.CYAN_CONCRETE_POWDER);
		this.dropSelf(Blocks.PURPLE_CONCRETE_POWDER);
		this.dropSelf(Blocks.BLUE_CONCRETE_POWDER);
		this.dropSelf(Blocks.BROWN_CONCRETE_POWDER);
		this.dropSelf(Blocks.GREEN_CONCRETE_POWDER);
		this.dropSelf(Blocks.RED_CONCRETE_POWDER);
		this.dropSelf(Blocks.BLACK_CONCRETE_POWDER);
		this.dropSelf(Blocks.KELP);
		this.dropSelf(Blocks.DRIED_KELP_BLOCK);
		this.dropSelf(Blocks.DEAD_TUBE_CORAL_BLOCK);
		this.dropSelf(Blocks.DEAD_BRAIN_CORAL_BLOCK);
		this.dropSelf(Blocks.DEAD_BUBBLE_CORAL_BLOCK);
		this.dropSelf(Blocks.DEAD_FIRE_CORAL_BLOCK);
		this.dropSelf(Blocks.DEAD_HORN_CORAL_BLOCK);
		this.dropSelf(Blocks.CONDUIT);
		this.dropSelf(Blocks.DRAGON_EGG);
		this.dropSelf(Blocks.BAMBOO);
		this.dropSelf(Blocks.POLISHED_GRANITE_STAIRS);
		this.dropSelf(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
		this.dropSelf(Blocks.MOSSY_STONE_BRICK_STAIRS);
		this.dropSelf(Blocks.POLISHED_DIORITE_STAIRS);
		this.dropSelf(Blocks.MOSSY_COBBLESTONE_STAIRS);
		this.dropSelf(Blocks.END_STONE_BRICK_STAIRS);
		this.dropSelf(Blocks.STONE_STAIRS);
		this.dropSelf(Blocks.SMOOTH_SANDSTONE_STAIRS);
		this.dropSelf(Blocks.SMOOTH_QUARTZ_STAIRS);
		this.dropSelf(Blocks.GRANITE_STAIRS);
		this.dropSelf(Blocks.ANDESITE_STAIRS);
		this.dropSelf(Blocks.RED_NETHER_BRICK_STAIRS);
		this.dropSelf(Blocks.POLISHED_ANDESITE_STAIRS);
		this.dropSelf(Blocks.DIORITE_STAIRS);
		this.dropSelf(Blocks.BRICK_WALL);
		this.dropSelf(Blocks.PRISMARINE_WALL);
		this.dropSelf(Blocks.RED_SANDSTONE_WALL);
		this.dropSelf(Blocks.MOSSY_STONE_BRICK_WALL);
		this.dropSelf(Blocks.GRANITE_WALL);
		this.dropSelf(Blocks.STONE_BRICK_WALL);
		this.dropSelf(Blocks.NETHER_BRICK_WALL);
		this.dropSelf(Blocks.ANDESITE_WALL);
		this.dropSelf(Blocks.RED_NETHER_BRICK_WALL);
		this.dropSelf(Blocks.SANDSTONE_WALL);
		this.dropSelf(Blocks.END_STONE_BRICK_WALL);
		this.dropSelf(Blocks.DIORITE_WALL);
		this.dropSelf(Blocks.LOOM);
		this.dropSelf(Blocks.SCAFFOLDING);
		this.dropSelf(Blocks.HONEY_BLOCK);
		this.dropSelf(Blocks.HONEYCOMB_BLOCK);
		this.dropSelf(Blocks.RESPAWN_ANCHOR);
		this.dropSelf(Blocks.LODESTONE);
		this.dropSelf(Blocks.WARPED_STEM);
		this.dropSelf(Blocks.WARPED_HYPHAE);
		this.dropSelf(Blocks.WARPED_FUNGUS);
		this.dropSelf(Blocks.WARPED_WART_BLOCK);
		this.dropSelf(Blocks.CRIMSON_STEM);
		this.dropSelf(Blocks.CRIMSON_HYPHAE);
		this.dropSelf(Blocks.CRIMSON_FUNGUS);
		this.dropSelf(Blocks.SHROOMLIGHT);
		this.dropSelf(Blocks.CRIMSON_PLANKS);
		this.dropSelf(Blocks.WARPED_PLANKS);
		this.dropSelf(Blocks.WARPED_PRESSURE_PLATE);
		this.dropSelf(Blocks.WARPED_FENCE);
		this.dropSelf(Blocks.WARPED_TRAPDOOR);
		this.dropSelf(Blocks.WARPED_FENCE_GATE);
		this.dropSelf(Blocks.WARPED_STAIRS);
		this.dropSelf(Blocks.WARPED_BUTTON);
		this.dropSelf(Blocks.WARPED_SIGN);
		this.dropSelf(Blocks.CRIMSON_PRESSURE_PLATE);
		this.dropSelf(Blocks.CRIMSON_FENCE);
		this.dropSelf(Blocks.CRIMSON_TRAPDOOR);
		this.dropSelf(Blocks.CRIMSON_FENCE_GATE);
		this.dropSelf(Blocks.CRIMSON_STAIRS);
		this.dropSelf(Blocks.CRIMSON_BUTTON);
		this.dropSelf(Blocks.CRIMSON_SIGN);
		this.dropSelf(Blocks.NETHERITE_BLOCK);
		this.dropSelf(Blocks.ANCIENT_DEBRIS);
		this.dropSelf(Blocks.BLACKSTONE);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_BRICKS);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
		this.dropSelf(Blocks.BLACKSTONE_STAIRS);
		this.dropSelf(Blocks.BLACKSTONE_WALL);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
		this.dropSelf(Blocks.CHISELED_POLISHED_BLACKSTONE);
		this.dropSelf(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_STAIRS);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_BUTTON);
		this.dropSelf(Blocks.POLISHED_BLACKSTONE_WALL);
		this.dropSelf(Blocks.CHISELED_NETHER_BRICKS);
		this.dropSelf(Blocks.CRACKED_NETHER_BRICKS);
		this.dropSelf(Blocks.QUARTZ_BRICKS);
		this.dropSelf(Blocks.CHAIN);
		this.dropSelf(Blocks.WARPED_ROOTS);
		this.dropSelf(Blocks.CRIMSON_ROOTS);
		this.dropOther(Blocks.FARMLAND, Blocks.DIRT);
		this.dropOther(Blocks.TRIPWIRE, Items.STRING);
		this.dropOther(Blocks.GRASS_PATH, Blocks.DIRT);
		this.dropOther(Blocks.KELP_PLANT, Blocks.KELP);
		this.dropOther(Blocks.BAMBOO_SAPLING, Blocks.BAMBOO);
		this.add(Blocks.STONE, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.COBBLESTONE));
		this.add(Blocks.GRASS_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
		this.add(Blocks.PODZOL, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
		this.add(Blocks.MYCELIUM, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
		this.add(Blocks.TUBE_CORAL_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DEAD_TUBE_CORAL_BLOCK));
		this.add(Blocks.BRAIN_CORAL_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DEAD_BRAIN_CORAL_BLOCK));
		this.add(Blocks.BUBBLE_CORAL_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DEAD_BUBBLE_CORAL_BLOCK));
		this.add(Blocks.FIRE_CORAL_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DEAD_FIRE_CORAL_BLOCK));
		this.add(Blocks.HORN_CORAL_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.DEAD_HORN_CORAL_BLOCK));
		this.add(Blocks.CRIMSON_NYLIUM, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
		this.add(Blocks.WARPED_NYLIUM, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
		this.add(Blocks.BOOKSHELF, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Items.BOOK, LootValueConstant.a(3)));
		this.add(Blocks.CLAY, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Items.CLAY_BALL, LootValueConstant.a(4)));
		this.add(Blocks.ENDER_CHEST, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Blocks.OBSIDIAN, LootValueConstant.a(8)));
		this.add(Blocks.SNOW_BLOCK, (Block block) -> hz.createSingleItemTableWithSilkTouch(block, Items.SNOWBALL, LootValueConstant.a(4)));
		this.add(Blocks.CHORUS_PLANT, hz.createSingleItemTable(LootValueBounds.a(0.0f, 1.0f)));
		this.dropPottedContents(Blocks.POTTED_OAK_SAPLING);
		this.dropPottedContents(Blocks.POTTED_SPRUCE_SAPLING);
		this.dropPottedContents(Blocks.POTTED_BIRCH_SAPLING);
		this.dropPottedContents(Blocks.POTTED_JUNGLE_SAPLING);
		this.dropPottedContents(Blocks.POTTED_ACACIA_SAPLING);
		this.dropPottedContents(Blocks.POTTED_DARK_OAK_SAPLING);
		this.dropPottedContents(Blocks.POTTED_FERN);
		this.dropPottedContents(Blocks.POTTED_DANDELION);
		this.dropPottedContents(Blocks.POTTED_POPPY);
		this.dropPottedContents(Blocks.POTTED_BLUE_ORCHID);
		this.dropPottedContents(Blocks.POTTED_ALLIUM);
		this.dropPottedContents(Blocks.POTTED_AZURE_BLUET);
		this.dropPottedContents(Blocks.POTTED_RED_TULIP);
		this.dropPottedContents(Blocks.POTTED_ORANGE_TULIP);
		this.dropPottedContents(Blocks.POTTED_WHITE_TULIP);
		this.dropPottedContents(Blocks.POTTED_PINK_TULIP);
		this.dropPottedContents(Blocks.POTTED_OXEYE_DAISY);
		this.dropPottedContents(Blocks.POTTED_CORNFLOWER);
		this.dropPottedContents(Blocks.POTTED_LILY_OF_THE_VALLEY);
		this.dropPottedContents(Blocks.POTTED_WITHER_ROSE);
		this.dropPottedContents(Blocks.POTTED_RED_MUSHROOM);
		this.dropPottedContents(Blocks.POTTED_BROWN_MUSHROOM);
		this.dropPottedContents(Blocks.POTTED_DEAD_BUSH);
		this.dropPottedContents(Blocks.POTTED_CACTUS);
		this.dropPottedContents(Blocks.POTTED_BAMBOO);
		this.dropPottedContents(Blocks.POTTED_CRIMSON_FUNGUS);
		this.dropPottedContents(Blocks.POTTED_WARPED_FUNGUS);
		this.dropPottedContents(Blocks.POTTED_CRIMSON_ROOTS);
		this.dropPottedContents(Blocks.POTTED_WARPED_ROOTS);
		this.add(Blocks.ACACIA_SLAB, hz::createSlabItemTable);
		this.add(Blocks.BIRCH_SLAB, hz::createSlabItemTable);
		this.add(Blocks.BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.COBBLESTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.DARK_OAK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.DARK_PRISMARINE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.JUNGLE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.NETHER_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.OAK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.PETRIFIED_OAK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.PRISMARINE_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.PRISMARINE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.PURPUR_SLAB, hz::createSlabItemTable);
		this.add(Blocks.QUARTZ_SLAB, hz::createSlabItemTable);
		this.add(Blocks.RED_SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.CUT_RED_SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.CUT_SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SPRUCE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.STONE_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.STONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SMOOTH_STONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.POLISHED_GRANITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SMOOTH_RED_SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.MOSSY_STONE_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.POLISHED_DIORITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.MOSSY_COBBLESTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.END_STONE_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SMOOTH_SANDSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.SMOOTH_QUARTZ_SLAB, hz::createSlabItemTable);
		this.add(Blocks.GRANITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.ANDESITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.RED_NETHER_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.POLISHED_ANDESITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.DIORITE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.CRIMSON_SLAB, hz::createSlabItemTable);
		this.add(Blocks.WARPED_SLAB, hz::createSlabItemTable);
		this.add(Blocks.BLACKSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, hz::createSlabItemTable);
		this.add(Blocks.POLISHED_BLACKSTONE_SLAB, hz::createSlabItemTable);
		this.add(Blocks.ACACIA_DOOR, hz::createDoorTable);
		this.add(Blocks.BIRCH_DOOR, hz::createDoorTable);
		this.add(Blocks.DARK_OAK_DOOR, hz::createDoorTable);
		this.add(Blocks.IRON_DOOR, hz::createDoorTable);
		this.add(Blocks.JUNGLE_DOOR, hz::createDoorTable);
		this.add(Blocks.OAK_DOOR, hz::createDoorTable);
		this.add(Blocks.SPRUCE_DOOR, hz::createDoorTable);
		this.add(Blocks.WARPED_DOOR, hz::createDoorTable);
		this.add(Blocks.CRIMSON_DOOR, hz::createDoorTable);
		this.add(Blocks.BLACK_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.BLUE_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.BROWN_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.CYAN_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.GRAY_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.GREEN_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.LIGHT_BLUE_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.LIGHT_GRAY_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.LIME_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.MAGENTA_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.PURPLE_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.ORANGE_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.PINK_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.RED_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.WHITE_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.YELLOW_BED, (Block block) -> hz.createSinglePropConditionTable(block, BlockBed.PART, BlockPropertyBedPart.HEAD));
		this.add(Blocks.LILAC, (Block block) -> hz.createSinglePropConditionTable(block, BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER));
		this.add(Blocks.SUNFLOWER, (Block block) -> hz.createSinglePropConditionTable(block, BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER));
		this.add(Blocks.PEONY, (Block block) -> hz.createSinglePropConditionTable(block, BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER));
		this.add(Blocks.ROSE_BUSH, (Block block) -> hz.createSinglePropConditionTable(block, BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER));
		this.add(Blocks.TNT, LootTable.b().a(hz.applyExplosionCondition(Blocks.TNT, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.TNT).b(LootItemConditionBlockStateProperty.a(Blocks.TNT).a(CriterionTriggerProperties.a_.a().a(BlockTNT.a, false)))))));
		this.add(Blocks.COCOA, (Block block) -> LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(hz.applyExplosionDecay(block, LootItem.a(Items.COCOA_BEANS).b(LootItemFunctionSetCount.a(LootValueConstant.a(3)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockCocoa.AGE, 2))))))));
		this.add(Blocks.SEA_PICKLE, (Block block) -> LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(hz.applyExplosionDecay(Blocks.SEA_PICKLE, ((LootItem.a(block).b(LootItemFunctionSetCount.a(LootValueConstant.a(2)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSeaPickle.a, 2))))).b(LootItemFunctionSetCount.a(LootValueConstant.a(3)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSeaPickle.a, 3))))).b(LootItemFunctionSetCount.a(LootValueConstant.a(4)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSeaPickle.a, 4))))))));
		this.add(Blocks.COMPOSTER, (Block block) -> LootTable.b().a(LootSelector.a().a(hz.applyExplosionDecay(block, LootItem.a(Items.qZ)))).a(LootSelector.a().a(LootItem.a(Items.BONE_MEAL)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockComposter.a, 8)))));
		this.add(Blocks.BEACON, hz::createNameableBlockEntityTable);
		this.add(Blocks.BREWING_STAND, hz::createNameableBlockEntityTable);
		this.add(Blocks.CHEST, hz::createNameableBlockEntityTable);
		this.add(Blocks.DISPENSER, hz::createNameableBlockEntityTable);
		this.add(Blocks.DROPPER, hz::createNameableBlockEntityTable);
		this.add(Blocks.ENCHANTING_TABLE, hz::createNameableBlockEntityTable);
		this.add(Blocks.FURNACE, hz::createNameableBlockEntityTable);
		this.add(Blocks.HOPPER, hz::createNameableBlockEntityTable);
		this.add(Blocks.TRAPPED_CHEST, hz::createNameableBlockEntityTable);
		this.add(Blocks.SMOKER, hz::createNameableBlockEntityTable);
		this.add(Blocks.BLAST_FURNACE, hz::createNameableBlockEntityTable);
		this.add(Blocks.BARREL, hz::createNameableBlockEntityTable);
		this.add(Blocks.CARTOGRAPHY_TABLE, hz::createNameableBlockEntityTable);
		this.add(Blocks.FLETCHING_TABLE, hz::createNameableBlockEntityTable);
		this.add(Blocks.GRINDSTONE, hz::createNameableBlockEntityTable);
		this.add(Blocks.LECTERN, hz::createNameableBlockEntityTable);
		this.add(Blocks.SMITHING_TABLE, hz::createNameableBlockEntityTable);
		this.add(Blocks.STONECUTTER, hz::createNameableBlockEntityTable);
		this.add(Blocks.BELL, hz::createSingleItemTable);
		this.add(Blocks.LANTERN, hz::createSingleItemTable);
		this.add(Blocks.SOUL_LANTERN, hz::createSingleItemTable);
		this.add(Blocks.SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.BLACK_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.BLUE_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.BROWN_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.CYAN_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.GRAY_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.GREEN_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.LIGHT_BLUE_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.LIGHT_GRAY_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.LIME_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.MAGENTA_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.ORANGE_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.PINK_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.PURPLE_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.RED_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.WHITE_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.YELLOW_SHULKER_BOX, hz::createShulkerBoxDrop);
		this.add(Blocks.BLACK_BANNER, hz::createBannerDrop);
		this.add(Blocks.BLUE_BANNER, hz::createBannerDrop);
		this.add(Blocks.BROWN_BANNER, hz::createBannerDrop);
		this.add(Blocks.CYAN_BANNER, hz::createBannerDrop);
		this.add(Blocks.GRAY_BANNER, hz::createBannerDrop);
		this.add(Blocks.GREEN_BANNER, hz::createBannerDrop);
		this.add(Blocks.LIGHT_BLUE_BANNER, hz::createBannerDrop);
		this.add(Blocks.LIGHT_GRAY_BANNER, hz::createBannerDrop);
		this.add(Blocks.LIME_BANNER, hz::createBannerDrop);
		this.add(Blocks.MAGENTA_BANNER, hz::createBannerDrop);
		this.add(Blocks.ORANGE_BANNER, hz::createBannerDrop);
		this.add(Blocks.PINK_BANNER, hz::createBannerDrop);
		this.add(Blocks.PURPLE_BANNER, hz::createBannerDrop);
		this.add(Blocks.RED_BANNER, hz::createBannerDrop);
		this.add(Blocks.WHITE_BANNER, hz::createBannerDrop);
		this.add(Blocks.YELLOW_BANNER, hz::createBannerDrop);
		this.add(Blocks.PLAYER_HEAD, (Block block) -> LootTable.b().a(hz.applyExplosionCondition(block, LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(block).b(LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("SkullOwner", "SkullOwner"))))));
		this.add(Blocks.BEE_NEST, hz::createBeeNestDrop);
		this.add(Blocks.BEEHIVE, hz::createBeeHiveDrop);
		this.add(Blocks.BIRCH_LEAVES, (Block block) -> hz.createLeavesDrops(block, Blocks.BIRCH_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
		this.add(Blocks.ACACIA_LEAVES, (Block block) -> hz.createLeavesDrops(block, Blocks.ACACIA_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
		this.add(Blocks.JUNGLE_LEAVES, (Block block) -> hz.createLeavesDrops(block, Blocks.JUNGLE_SAPLING, JUNGLE_LEAVES_SAPLING_CHANGES));
		this.add(Blocks.SPRUCE_LEAVES, (Block block) -> hz.createLeavesDrops(block, Blocks.SPRUCE_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
		this.add(Blocks.OAK_LEAVES, (Block block) -> hz.createOakLeavesDrops(block, Blocks.OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
		this.add(Blocks.DARK_OAK_LEAVES, (Block block) -> hz.createOakLeavesDrops(block, Blocks.DARK_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
		var gga = LootItemConditionBlockStateProperty.a(Blocks.BEETROOTS).a(CriterionTriggerProperties.a_.a().a(BlockBeetroot.AGE, 3));
		this.add(Blocks.BEETROOTS, hz.createCropDrops(Blocks.BEETROOTS, Items.BEETROOT, Items.BEETROOT_SEEDS, gga));
		var ggg = LootItemConditionBlockStateProperty.a(Blocks.WHEAT).a(CriterionTriggerProperties.a_.a().a(BlockCrops.AGE, 7));
		this.add(Blocks.WHEAT, hz.createCropDrops(Blocks.WHEAT, Items.WHEAT, Items.WHEAT_SEEDS, ggg));
		var ggh = LootItemConditionBlockStateProperty.a(Blocks.CARROTS).a(CriterionTriggerProperties.a_.a().a(BlockCarrots.AGE, 7));
		this.add(Blocks.CARROTS, hz.applyExplosionDecay(Blocks.CARROTS, LootTable.b().a(LootSelector.a().a(LootItem.a(Items.CARROT))).a(LootSelector.a().b(ggh).a(LootItem.a(Items.CARROT).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286f, 3))))));
		var ggp = LootItemConditionBlockStateProperty.a(Blocks.POTATOES).a(CriterionTriggerProperties.a_.a().a(BlockPotatoes.AGE, 7));
		this.add(Blocks.POTATOES, hz.applyExplosionDecay(Blocks.POTATOES, LootTable.b().a(LootSelector.a().a(LootItem.a(Items.POTATO))).a(LootSelector.a().b(ggp).a(LootItem.a(Items.POTATO).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286f, 3)))).a(LootSelector.a().b(ggp).a(LootItem.a(Items.POISONOUS_POTATO).b(LootItemConditionRandomChance.a(0.02f))))));
		this.add(Blocks.SWEET_BERRY_BUSH, (Block block) -> hz.applyExplosionDecay(block, LootTable.b().a(LootSelector.a().b(LootItemConditionBlockStateProperty.a(Blocks.SWEET_BERRY_BUSH).a(CriterionTriggerProperties.a_.a().a(BlockSweetBerryBush.a, 3))).a(LootItem.a(Items.SWEET_BERRIES)).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 3.0f))).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS))).a(LootSelector.a().b(LootItemConditionBlockStateProperty.a(Blocks.SWEET_BERRY_BUSH).a(CriterionTriggerProperties.a_.a().a(BlockSweetBerryBush.a, 2))).a(LootItem.a(Items.SWEET_BERRIES)).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0f, 2.0f))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS)))));
		this.add(Blocks.BROWN_MUSHROOM_BLOCK, (Block block) -> hz.createMushroomBlockDrop(block, Blocks.BROWN_MUSHROOM));
		this.add(Blocks.RED_MUSHROOM_BLOCK, (Block block) -> hz.createMushroomBlockDrop(block, Blocks.RED_MUSHROOM));
		this.add(Blocks.COAL_ORE, (Block block) -> hz.createOreDrop(block, Items.COAL));
		this.add(Blocks.EMERALD_ORE, (Block block) -> hz.createOreDrop(block, Items.EMERALD));
		this.add(Blocks.NETHER_QUARTZ_ORE, (Block block) -> hz.createOreDrop(block, Items.QUARTZ));
		this.add(Blocks.DIAMOND_ORE, (Block block) -> hz.createOreDrop(block, Items.DIAMOND));
		this.add(Blocks.NETHER_GOLD_ORE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, (LootItem.a(Items.GOLD_NUGGET).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 6.0f)))).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS)))));
		this.add(Blocks.LAPIS_ORE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, (LootItem.a(Items.LAPIS_LAZULI).b(LootItemFunctionSetCount.a(LootValueBounds.a(4.0f, 9.0f)))).b(LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS)))));
		this.add(Blocks.COBWEB, (Block block) -> hz.createSilkTouchOrShearsDispatchTable(block, hz.applyExplosionCondition(block, LootItem.a(Items.STRING))));
		this.add(Blocks.DEAD_BUSH, (Block block) -> hz.createShearsDispatchTable(block, hz.applyExplosionDecay(block, LootItem.a(Items.STICK).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0f, 2.0f))))));
		this.add(Blocks.NETHER_SPROUTS, hz::createShearsOnlyDrop);
		this.add(Blocks.SEAGRASS, hz::createShearsOnlyDrop);
		this.add(Blocks.VINE, hz::createShearsOnlyDrop);
		this.add(Blocks.TALL_SEAGRASS, hz.createDoublePlantShearsDrop());
		this.add(Blocks.LARGE_FERN, (Block block) -> hz.createDoublePlantWithSeedDrops(block, Blocks.FERN));
		this.add(Blocks.TALL_GRASS, (Block block) -> hz.createDoublePlantWithSeedDrops(block, Blocks.GRASS));
		this.add(Blocks.MELON_STEM, (Block block) -> hz.createStemDrops(block, Items.MELON_SEEDS));
		this.add(Blocks.ATTACHED_MELON_STEM, (Block block) -> hz.createAttachedStemDrops(block, Items.MELON_SEEDS));
		this.add(Blocks.PUMPKIN_STEM, (Block block) -> hz.createStemDrops(block, Items.PUMPKIN_SEEDS));
		this.add(Blocks.ATTACHED_PUMPKIN_STEM, (Block block) -> hz.createAttachedStemDrops(block, Items.PUMPKIN_SEEDS));
		this.add(Blocks.CHORUS_FLOWER, (Block block) -> LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a((hz.applyExplosionCondition(block, LootItem.a(block))).b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS)))));
		this.add(Blocks.FERN, hz::createGrassDrops);
		this.add(Blocks.GRASS, hz::createGrassDrops);
		this.add(Blocks.GLOWSTONE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, ((LootItem.a(Items.GLOWSTONE_DUST).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 4.0f)))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))).b(LootItemFunctionLimitCount.a(LootIntegerLimit.a(1, 4))))));
		this.add(Blocks.MELON, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, ((LootItem.a(Items.MELON_SLICE).b(LootItemFunctionSetCount.a(LootValueBounds.a(3.0f, 7.0f)))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))).b(LootItemFunctionLimitCount.a(LootIntegerLimit.b(9))))));
		this.add(Blocks.REDSTONE_ORE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, (LootItem.a(Items.REDSTONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(4.0f, 5.0f)))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS)))));
		this.add(Blocks.SEA_LANTERN, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionDecay(block, ((LootItem.a(Items.PRISMARINE_CRYSTALS).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 3.0f)))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))).b(LootItemFunctionLimitCount.a(LootIntegerLimit.a(1, 5))))));
		this.add(Blocks.NETHER_WART, (Block block) -> LootTable.b().a(hz.applyExplosionDecay(block, LootSelector.a().a(LootValueConstant.a(1)).a((LootItem.a(Items.NETHER_WART).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 4.0f)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockNetherWart.AGE, 3))))).b(LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockNetherWart.AGE, 3))))))));
		this.add(Blocks.SNOW, (Block block) -> LootTable.b().a(LootSelector.a().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS)).a(LootEntryAlternatives.a(LootEntryAlternatives.a(LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 1))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 2)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(2))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 3)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(3))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 4)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(4))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 5)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(5))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 6)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(6))), (LootItem.a(Items.SNOWBALL).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 7)))).b(LootItemFunctionSetCount.a(LootValueConstant.a(7))), LootItem.a(Items.SNOWBALL).b(LootItemFunctionSetCount.a(LootValueConstant.a(8)))).b(HAS_NO_SILK_TOUCH), LootEntryAlternatives.a(LootItem.a(Blocks.SNOW).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 1))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(2))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 2))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(3))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 3))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(4))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 4))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(5))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 5))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(6))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 6))), LootItem.a(Blocks.SNOW).b(LootItemFunctionSetCount.a(LootValueConstant.a(7))).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a_.a().a(BlockSnow.LAYERS, 7))), LootItem.a(Blocks.SNOW_BLOCK))))));
		this.add(Blocks.GRAVEL, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionCondition(block, (LootItem.a(Items.FLINT).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.1f, 0.14285715f, 0.25f, 1.0f))).a(LootItem.a(block)))));
		this.add(Blocks.CAMPFIRE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionCondition(block, LootItem.a(Items.CHARCOAL).b(LootItemFunctionSetCount.a(LootValueConstant.a(2))))));
		this.add(Blocks.GILDED_BLACKSTONE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionCondition(block, ((LootItem.a(Items.GOLD_NUGGET).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0f, 5.0f)))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.1f, 0.14285715f, 0.25f, 1.0f))).a(LootItem.a(block)))));
		this.add(Blocks.SOUL_CAMPFIRE, (Block block) -> hz.createSilkTouchDispatchTable(block, hz.applyExplosionCondition(block, LootItem.a(Items.dm).b(LootItemFunctionSetCount.a(LootValueConstant.a(1))))));
		this.dropWhenSilkTouch(Blocks.GLASS);
		this.dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.RED_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS);
		this.dropWhenSilkTouch(Blocks.GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.RED_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS_PANE);
		this.dropWhenSilkTouch(Blocks.ICE);
		this.dropWhenSilkTouch(Blocks.PACKED_ICE);
		this.dropWhenSilkTouch(Blocks.BLUE_ICE);
		this.dropWhenSilkTouch(Blocks.TURTLE_EGG);
		this.dropWhenSilkTouch(Blocks.MUSHROOM_STEM);
		this.dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL);
		this.dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL);
		this.dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL);
		this.dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL);
		this.dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL);
		this.dropWhenSilkTouch(Blocks.TUBE_CORAL);
		this.dropWhenSilkTouch(Blocks.BRAIN_CORAL);
		this.dropWhenSilkTouch(Blocks.BUBBLE_CORAL);
		this.dropWhenSilkTouch(Blocks.FIRE_CORAL);
		this.dropWhenSilkTouch(Blocks.HORN_CORAL);
		this.dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.TUBE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.BRAIN_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.BUBBLE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.FIRE_CORAL_FAN);
		this.dropWhenSilkTouch(Blocks.HORN_CORAL_FAN);
		this.otherWhenSilkTouch(Blocks.INFESTED_STONE, Blocks.STONE);
		this.otherWhenSilkTouch(Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE);
		this.otherWhenSilkTouch(Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS);
		this.otherWhenSilkTouch(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
		this.otherWhenSilkTouch(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
		this.otherWhenSilkTouch(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS);
		this.addNetherVinesDropTable(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		this.addNetherVinesDropTable(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
		this.add(Blocks.CAKE, hz.noDrop());
		this.add(Blocks.FROSTED_ICE, hz.noDrop());
		this.add(Blocks.SPAWNER, hz.noDrop());
		this.add(Blocks.FIRE, hz.noDrop());
		this.add(Blocks.SOUL_FIRE, hz.noDrop());
		this.add(Blocks.NETHER_PORTAL, hz.noDrop());
		HashSet<MinecraftKey> puking = Sets.newHashSet();
		for (Block block2 : IRegistry.BLOCK) {
			var resourceLocation = block2.r();
			if (resourceLocation == LootTables.a || !puking.add(resourceLocation)) continue;
			LootTable.a govno = this.map.remove(resourceLocation);
			if (govno == null) {
				throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourceLocation, IRegistry.BLOCK.getKey(block2)));
			}
			biConsumer.accept(resourceLocation, govno);
		}
		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}

	private void addNetherVinesDropTable(Block block, Block block2) {
		LootTable.a builder = hz.createSilkTouchOrShearsDispatchTable(block, LootItem.a(block).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.33f, 0.55f, 0.77f, 1.0f)));
		this.add(block, builder);
		this.add(block2, builder);
	}

	public void dropPottedContents(Block block2) {
		this.add(block2, (Block block) -> hz.createPotFlowerItemTable(block).c());
	}

	public void otherWhenSilkTouch(Block block, Block block2) {
		this.add(block, hz.createSilkTouchOnlyTable(block2));
	}

	public void dropOther(Block block, IMaterial itemLike) {
		this.add(block, hz.createSingleItemTable(itemLike));
	}

	public void dropWhenSilkTouch(Block block) {
		this.otherWhenSilkTouch(block, block);
	}

	public void dropSelf(Block block) {
		this.dropOther(block, block);
	}

	private void add(Block block, Function<Block, LootTable.a> function) {
		this.add(block, function.apply(block));
	}

	private void add(Block block, LootTable.a builder) {
		this.map.put(block.r(), builder);
	}
}
