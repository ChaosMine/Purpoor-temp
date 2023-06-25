package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.CriterionConditionBlock;
import net.minecraft.advancements.critereon.CriterionConditionEnchantments;
import net.minecraft.advancements.critereon.CriterionConditionItem;
import net.minecraft.advancements.critereon.CriterionConditionLocation;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.advancements.critereon.CriterionTriggerProperties;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.INamable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockBed;
import net.minecraft.world.level.block.BlockBeehive;
import net.minecraft.world.level.block.BlockBeetroot;
import net.minecraft.world.level.block.BlockCarrots;
import net.minecraft.world.level.block.BlockCocoa;
import net.minecraft.world.level.block.BlockComposter;
import net.minecraft.world.level.block.BlockCrops;
import net.minecraft.world.level.block.BlockDoor;
import net.minecraft.world.level.block.BlockFlowerPot;
import net.minecraft.world.level.block.BlockNetherWart;
import net.minecraft.world.level.block.BlockPotatoes;
import net.minecraft.world.level.block.BlockSeaPickle;
import net.minecraft.world.level.block.BlockShulkerBox;
import net.minecraft.world.level.block.BlockSnow;
import net.minecraft.world.level.block.BlockStem;
import net.minecraft.world.level.block.BlockStepAbstract;
import net.minecraft.world.level.block.BlockSweetBerryBush;
import net.minecraft.world.level.block.BlockTNT;
import net.minecraft.world.level.block.BlockTallPlant;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockPropertyBedPart;
import net.minecraft.world.level.block.state.properties.BlockPropertyDoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BlockPropertySlabType;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.level.storage.loot.LootIntegerLimit;
import net.minecraft.world.level.storage.loot.LootSelector;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.LootValue;
import net.minecraft.world.level.storage.loot.LootValueBinomial;
import net.minecraft.world.level.storage.loot.LootValueBounds;
import net.minecraft.world.level.storage.loot.LootValueConstant;
import net.minecraft.world.level.storage.loot.entries.LootEntryAbstract;
import net.minecraft.world.level.storage.loot.entries.LootEntryAlternatives;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootSelectorDynamic;
import net.minecraft.world.level.storage.loot.entries.LootSelectorEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionApplyBonus;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionCopyNBT;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionCopyName;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionCopyState;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionLimitCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetContents;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionUser;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionBlockStateProperty;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionEntityProperty;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionLocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionMatchTool;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionRandomChance;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionSurvivesExplosion;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionTableBonus;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionUser;

public class hz implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {

    private static final LootItemCondition.a a = LootItemConditionMatchTool.a(CriterionConditionItem.a.a().a(new CriterionConditionEnchantments(Enchantments.SILK_TOUCH, CriterionConditionValue.IntegerRange.b(1))));
    private static final LootItemCondition.a b = hz.a.a();
    private static final LootItemCondition.a c = LootItemConditionMatchTool.a(CriterionConditionItem.a.a().a((IMaterial) Items.SHEARS));
    private static final LootItemCondition.a d = hz.c.a(hz.a);
    private static final LootItemCondition.a e = hz.d.a();
    private static final Set<Item> f = (Set) Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(IMaterial::getItem).collect(ImmutableSet.toImmutableSet());
    private static final float[] g = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
    private static final float[] h = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
    private final Map<MinecraftKey, LootTable.a> i = Maps.newHashMap();

    public hz() {}

    private static <T> T a(IMaterial imaterial, LootItemFunctionUser<T> lootitemfunctionuser) {
        return !hz.f.contains(imaterial.getItem()) ? lootitemfunctionuser.b(LootItemFunctionExplosionDecay.c()) : lootitemfunctionuser.c();
    }

    private static <T> T a(IMaterial imaterial, LootItemConditionUser<T> lootitemconditionuser) {
        return !hz.f.contains(imaterial.getItem()) ? lootitemconditionuser.b(LootItemConditionSurvivesExplosion.c()) : lootitemconditionuser.c();
    }

    private static LootTable.a a(IMaterial imaterial) {
        return LootTable.b().a((LootSelector.a) a(imaterial, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a(imaterial))));
    }

    private static LootTable.a a(Block block, LootItemCondition.a lootitemcondition_a, LootEntryAbstract.a<?> lootentryabstract_a) {
        return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) ((LootSelectorEntry.a) LootItem.a((IMaterial) block).b(lootitemcondition_a)).a(lootentryabstract_a)));
    }

    private static LootTable.a a(Block block, LootEntryAbstract.a<?> lootentryabstract_a) {
        return a(block, hz.a, lootentryabstract_a);
    }

    private static LootTable.a b(Block block, LootEntryAbstract.a<?> lootentryabstract_a) {
        return a(block, hz.c, lootentryabstract_a);
    }

    private static LootTable.a c(Block block, LootEntryAbstract.a<?> lootentryabstract_a) {
        return a(block, hz.d, lootentryabstract_a);
    }

    private static LootTable.a b(Block block, IMaterial imaterial) {
        return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a(imaterial)));
    }

    private static LootTable.a a(IMaterial imaterial, LootValue lootvalue) {
        return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) a(imaterial, (LootItemFunctionUser) LootItem.a(imaterial).b((LootItemFunction.a) LootItemFunctionSetCount.a(lootvalue)))));
    }

    private static LootTable.a a(Block block, IMaterial imaterial, LootValue lootvalue) {
        return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a(imaterial).b((LootItemFunction.a) LootItemFunctionSetCount.a(lootvalue))));
    }

    private static LootTable.a b(IMaterial imaterial) {
        return LootTable.b().a(LootSelector.a().b(hz.a).a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a(imaterial)));
    }

    private static LootTable.a c(IMaterial imaterial) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) Blocks.FLOWER_POT, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Blocks.FLOWER_POT)))).a((LootSelector.a) a(imaterial, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a(imaterial))));
    }

    private static LootTable.a e(Block block) {
        return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStepAbstract.a, (Comparable) BlockPropertySlabType.DOUBLE)))))));
    }

    private static <T extends Comparable<T> & INamable> LootTable.a a(Block block, IBlockState<T> iblockstate, T t0) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a(LootItem.a((IMaterial) block).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(iblockstate, t0))))));
    }

    private static LootTable.a f(Block block) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY)))));
    }

    private static LootTable.a g(Block block) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY)).b((LootItemFunction.a) LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Lock", "BlockEntityTag.Lock").a("LootTable", "BlockEntityTag.LootTable").a("LootTableSeed", "BlockEntityTag.LootTableSeed")).b((LootItemFunction.a) LootItemFunctionSetContents.c().a(LootSelectorDynamic.a(BlockShulkerBox.b))))));
    }

    private static LootTable.a h(Block block) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionCopyName.a(LootItemFunctionCopyName.Source.BLOCK_ENTITY)).b((LootItemFunction.a) LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Patterns", "BlockEntityTag.Patterns")))));
    }

    private static LootTable.a i(Block block) {
        return LootTable.b().a(LootSelector.a().b(hz.a).a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Bees", "BlockEntityTag.Bees")).b((LootItemFunction.a) LootItemFunctionCopyState.a(block).a(BlockBeehive.b))));
    }

    private static LootTable.a j(Block block) {
        return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) ((LootSelectorEntry.a) LootItem.a((IMaterial) block).b(hz.a)).b((LootItemFunction.a) LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("Bees", "BlockEntityTag.Bees")).b((LootItemFunction.a) LootItemFunctionCopyState.a(block).a(BlockBeehive.b)).a(LootItem.a((IMaterial) block))));
    }

    private static LootTable.a a(Block block, Item item) {
        return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) item).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS))));
    }

    private static LootTable.a c(Block block, IMaterial imaterial) {
        return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a(imaterial).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(-6.0F, 2.0F))).b((LootItemFunction.a) LootItemFunctionLimitCount.a(LootIntegerLimit.a(0)))));
    }

    private static LootTable.a k(Block block) {
        return b(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.WHEAT_SEEDS).b(LootItemConditionRandomChance.a(0.125F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 2))));
    }

    private static LootTable.a b(Block block, Item item) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemFunctionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) item).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.06666667F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 0)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.13333334F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 1)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.2F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 2)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.26666668F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 3)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.33333334F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 4)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.4F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 5)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.46666667F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 6)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.53333336F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockStem.AGE, 7)))))));
    }

    private static LootTable.a c(Block block, Item item) {
        return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemFunctionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) item).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBinomial.a(3, 0.53333336F))))));
    }

    private static LootTable.a d(IMaterial imaterial) {
        return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).b(hz.c).a((LootEntryAbstract.a) LootItem.a(imaterial)));
    }

    private static LootTable.a a(Block block, Block block1, float... afloat) {
        return c(block, ((LootSelectorEntry.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) block1))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, afloat))).a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).b(hz.e).a(((LootSelectorEntry.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.STICK).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(1.0F, 2.0F))))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
    }

    private static LootTable.a b(Block block, Block block1, float... afloat) {
        return a(block, block1, afloat).a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).b(hz.e).a(((LootSelectorEntry.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) Items.APPLE))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
    }

    private static LootTable.a a(Block block, Item item, Item item1, LootItemCondition.a lootitemcondition_a) {
        return (LootTable.a) a((IMaterial) block, (LootItemFunctionUser) LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) ((LootSelectorEntry.a) LootItem.a((IMaterial) item).b(lootitemcondition_a)).a(LootItem.a((IMaterial) item1)))).a(LootSelector.a().b(lootitemcondition_a).a((LootEntryAbstract.a) LootItem.a((IMaterial) item1).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286F, 3)))));
    }

    private static LootTable.a l(Block block) {
        return LootTable.b().a(LootSelector.a().b(hz.c).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2)))));
    }

    private static LootTable.a b(Block block, Block block1) {
        LootEntryAbstract.a<?> lootentryabstract_a = ((LootSelectorEntry.a) LootItem.a((IMaterial) block1).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2))).b(hz.c)).a(((LootSelectorEntry.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) Items.WHEAT_SEEDS))).b(LootItemConditionRandomChance.a(0.125F)));

        return LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) lootentryabstract_a).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER))).b(LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(CriterionConditionBlock.a.a().a(block).a(CriterionTriggerProperties.a.a().a(BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.UPPER).b()).b()), new BlockPosition(0, 1, 0)))).a(LootSelector.a().a((LootEntryAbstract.a) lootentryabstract_a).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.UPPER))).b(LootItemConditionLocationCheck.a(CriterionConditionLocation.a.a().a(CriterionConditionBlock.a.a().a(block).a(CriterionTriggerProperties.a.a().a(BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER).b()).b()), new BlockPosition(0, -1, 0))));
    }

    public static LootTable.a a() {
        return LootTable.b();
    }

    public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
        this.d(Blocks.GRANITE);
        this.d(Blocks.POLISHED_GRANITE);
        this.d(Blocks.DIORITE);
        this.d(Blocks.POLISHED_DIORITE);
        this.d(Blocks.ANDESITE);
        this.d(Blocks.POLISHED_ANDESITE);
        this.d(Blocks.DIRT);
        this.d(Blocks.COARSE_DIRT);
        this.d(Blocks.COBBLESTONE);
        this.d(Blocks.OAK_PLANKS);
        this.d(Blocks.SPRUCE_PLANKS);
        this.d(Blocks.BIRCH_PLANKS);
        this.d(Blocks.JUNGLE_PLANKS);
        this.d(Blocks.ACACIA_PLANKS);
        this.d(Blocks.DARK_OAK_PLANKS);
        this.d(Blocks.OAK_SAPLING);
        this.d(Blocks.SPRUCE_SAPLING);
        this.d(Blocks.BIRCH_SAPLING);
        this.d(Blocks.JUNGLE_SAPLING);
        this.d(Blocks.ACACIA_SAPLING);
        this.d(Blocks.DARK_OAK_SAPLING);
        this.d(Blocks.SAND);
        this.d(Blocks.RED_SAND);
        this.d(Blocks.GOLD_ORE);
        this.d(Blocks.IRON_ORE);
        this.d(Blocks.OAK_LOG);
        this.d(Blocks.SPRUCE_LOG);
        this.d(Blocks.BIRCH_LOG);
        this.d(Blocks.JUNGLE_LOG);
        this.d(Blocks.ACACIA_LOG);
        this.d(Blocks.DARK_OAK_LOG);
        this.d(Blocks.STRIPPED_SPRUCE_LOG);
        this.d(Blocks.STRIPPED_BIRCH_LOG);
        this.d(Blocks.STRIPPED_JUNGLE_LOG);
        this.d(Blocks.STRIPPED_ACACIA_LOG);
        this.d(Blocks.STRIPPED_DARK_OAK_LOG);
        this.d(Blocks.STRIPPED_OAK_LOG);
        this.d(Blocks.STRIPPED_WARPED_STEM);
        this.d(Blocks.STRIPPED_CRIMSON_STEM);
        this.d(Blocks.OAK_WOOD);
        this.d(Blocks.SPRUCE_WOOD);
        this.d(Blocks.BIRCH_WOOD);
        this.d(Blocks.JUNGLE_WOOD);
        this.d(Blocks.ACACIA_WOOD);
        this.d(Blocks.DARK_OAK_WOOD);
        this.d(Blocks.STRIPPED_OAK_WOOD);
        this.d(Blocks.STRIPPED_SPRUCE_WOOD);
        this.d(Blocks.STRIPPED_BIRCH_WOOD);
        this.d(Blocks.STRIPPED_JUNGLE_WOOD);
        this.d(Blocks.STRIPPED_ACACIA_WOOD);
        this.d(Blocks.STRIPPED_DARK_OAK_WOOD);
        this.d(Blocks.STRIPPED_CRIMSON_HYPHAE);
        this.d(Blocks.STRIPPED_WARPED_HYPHAE);
        this.d(Blocks.SPONGE);
        this.d(Blocks.WET_SPONGE);
        this.d(Blocks.LAPIS_BLOCK);
        this.d(Blocks.SANDSTONE);
        this.d(Blocks.CHISELED_SANDSTONE);
        this.d(Blocks.CUT_SANDSTONE);
        this.d(Blocks.NOTE_BLOCK);
        this.d(Blocks.POWERED_RAIL);
        this.d(Blocks.DETECTOR_RAIL);
        this.d(Blocks.STICKY_PISTON);
        this.d(Blocks.PISTON);
        this.d(Blocks.WHITE_WOOL);
        this.d(Blocks.ORANGE_WOOL);
        this.d(Blocks.MAGENTA_WOOL);
        this.d(Blocks.LIGHT_BLUE_WOOL);
        this.d(Blocks.YELLOW_WOOL);
        this.d(Blocks.LIME_WOOL);
        this.d(Blocks.PINK_WOOL);
        this.d(Blocks.GRAY_WOOL);
        this.d(Blocks.LIGHT_GRAY_WOOL);
        this.d(Blocks.CYAN_WOOL);
        this.d(Blocks.PURPLE_WOOL);
        this.d(Blocks.BLUE_WOOL);
        this.d(Blocks.BROWN_WOOL);
        this.d(Blocks.GREEN_WOOL);
        this.d(Blocks.RED_WOOL);
        this.d(Blocks.BLACK_WOOL);
        this.d(Blocks.DANDELION);
        this.d(Blocks.POPPY);
        this.d(Blocks.BLUE_ORCHID);
        this.d(Blocks.ALLIUM);
        this.d(Blocks.AZURE_BLUET);
        this.d(Blocks.RED_TULIP);
        this.d(Blocks.ORANGE_TULIP);
        this.d(Blocks.WHITE_TULIP);
        this.d(Blocks.PINK_TULIP);
        this.d(Blocks.OXEYE_DAISY);
        this.d(Blocks.CORNFLOWER);
        this.d(Blocks.WITHER_ROSE);
        this.d(Blocks.LILY_OF_THE_VALLEY);
        this.d(Blocks.BROWN_MUSHROOM);
        this.d(Blocks.RED_MUSHROOM);
        this.d(Blocks.GOLD_BLOCK);
        this.d(Blocks.IRON_BLOCK);
        this.d(Blocks.BRICKS);
        this.d(Blocks.MOSSY_COBBLESTONE);
        this.d(Blocks.OBSIDIAN);
        this.d(Blocks.CRYING_OBSIDIAN);
        this.d(Blocks.TORCH);
        this.d(Blocks.OAK_STAIRS);
        this.d(Blocks.REDSTONE_WIRE);
        this.d(Blocks.DIAMOND_BLOCK);
        this.d(Blocks.CRAFTING_TABLE);
        this.d(Blocks.OAK_SIGN);
        this.d(Blocks.SPRUCE_SIGN);
        this.d(Blocks.BIRCH_SIGN);
        this.d(Blocks.ACACIA_SIGN);
        this.d(Blocks.JUNGLE_SIGN);
        this.d(Blocks.DARK_OAK_SIGN);
        this.d(Blocks.LADDER);
        this.d(Blocks.RAIL);
        this.d(Blocks.COBBLESTONE_STAIRS);
        this.d(Blocks.LEVER);
        this.d(Blocks.STONE_PRESSURE_PLATE);
        this.d(Blocks.OAK_PRESSURE_PLATE);
        this.d(Blocks.SPRUCE_PRESSURE_PLATE);
        this.d(Blocks.BIRCH_PRESSURE_PLATE);
        this.d(Blocks.JUNGLE_PRESSURE_PLATE);
        this.d(Blocks.ACACIA_PRESSURE_PLATE);
        this.d(Blocks.DARK_OAK_PRESSURE_PLATE);
        this.d(Blocks.REDSTONE_TORCH);
        this.d(Blocks.STONE_BUTTON);
        this.d(Blocks.CACTUS);
        this.d(Blocks.SUGAR_CANE);
        this.d(Blocks.JUKEBOX);
        this.d(Blocks.OAK_FENCE);
        this.d(Blocks.PUMPKIN);
        this.d(Blocks.NETHERRACK);
        this.d(Blocks.SOUL_SAND);
        this.d(Blocks.SOUL_SOIL);
        this.d(Blocks.BASALT);
        this.d(Blocks.cP);
        this.d(Blocks.SOUL_TORCH);
        this.d(Blocks.CARVED_PUMPKIN);
        this.d(Blocks.JACK_O_LANTERN);
        this.d(Blocks.REPEATER);
        this.d(Blocks.OAK_TRAPDOOR);
        this.d(Blocks.SPRUCE_TRAPDOOR);
        this.d(Blocks.BIRCH_TRAPDOOR);
        this.d(Blocks.JUNGLE_TRAPDOOR);
        this.d(Blocks.ACACIA_TRAPDOOR);
        this.d(Blocks.DARK_OAK_TRAPDOOR);
        this.d(Blocks.STONE_BRICKS);
        this.d(Blocks.MOSSY_STONE_BRICKS);
        this.d(Blocks.CRACKED_STONE_BRICKS);
        this.d(Blocks.CHISELED_STONE_BRICKS);
        this.d(Blocks.IRON_BARS);
        this.d(Blocks.OAK_FENCE_GATE);
        this.d(Blocks.BRICK_STAIRS);
        this.d(Blocks.STONE_BRICK_STAIRS);
        this.d(Blocks.LILY_PAD);
        this.d(Blocks.NETHER_BRICKS);
        this.d(Blocks.NETHER_BRICK_FENCE);
        this.d(Blocks.NETHER_BRICK_STAIRS);
        this.d(Blocks.CAULDRON);
        this.d(Blocks.END_STONE);
        this.d(Blocks.REDSTONE_LAMP);
        this.d(Blocks.SANDSTONE_STAIRS);
        this.d(Blocks.TRIPWIRE_HOOK);
        this.d(Blocks.EMERALD_BLOCK);
        this.d(Blocks.SPRUCE_STAIRS);
        this.d(Blocks.BIRCH_STAIRS);
        this.d(Blocks.JUNGLE_STAIRS);
        this.d(Blocks.COBBLESTONE_WALL);
        this.d(Blocks.MOSSY_COBBLESTONE_WALL);
        this.d(Blocks.FLOWER_POT);
        this.d(Blocks.OAK_BUTTON);
        this.d(Blocks.SPRUCE_BUTTON);
        this.d(Blocks.BIRCH_BUTTON);
        this.d(Blocks.JUNGLE_BUTTON);
        this.d(Blocks.ACACIA_BUTTON);
        this.d(Blocks.DARK_OAK_BUTTON);
        this.d(Blocks.SKELETON_SKULL);
        this.d(Blocks.WITHER_SKELETON_SKULL);
        this.d(Blocks.ZOMBIE_HEAD);
        this.d(Blocks.CREEPER_HEAD);
        this.d(Blocks.DRAGON_HEAD);
        this.d(Blocks.ANVIL);
        this.d(Blocks.CHIPPED_ANVIL);
        this.d(Blocks.DAMAGED_ANVIL);
        this.d(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        this.d(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        this.d(Blocks.COMPARATOR);
        this.d(Blocks.DAYLIGHT_DETECTOR);
        this.d(Blocks.REDSTONE_BLOCK);
        this.d(Blocks.QUARTZ_BLOCK);
        this.d(Blocks.CHISELED_QUARTZ_BLOCK);
        this.d(Blocks.QUARTZ_PILLAR);
        this.d(Blocks.QUARTZ_STAIRS);
        this.d(Blocks.ACTIVATOR_RAIL);
        this.d(Blocks.WHITE_TERRACOTTA);
        this.d(Blocks.ORANGE_TERRACOTTA);
        this.d(Blocks.MAGENTA_TERRACOTTA);
        this.d(Blocks.LIGHT_BLUE_TERRACOTTA);
        this.d(Blocks.YELLOW_TERRACOTTA);
        this.d(Blocks.LIME_TERRACOTTA);
        this.d(Blocks.PINK_TERRACOTTA);
        this.d(Blocks.GRAY_TERRACOTTA);
        this.d(Blocks.LIGHT_GRAY_TERRACOTTA);
        this.d(Blocks.CYAN_TERRACOTTA);
        this.d(Blocks.PURPLE_TERRACOTTA);
        this.d(Blocks.BLUE_TERRACOTTA);
        this.d(Blocks.BROWN_TERRACOTTA);
        this.d(Blocks.GREEN_TERRACOTTA);
        this.d(Blocks.RED_TERRACOTTA);
        this.d(Blocks.BLACK_TERRACOTTA);
        this.d(Blocks.ACACIA_STAIRS);
        this.d(Blocks.DARK_OAK_STAIRS);
        this.d(Blocks.SLIME_BLOCK);
        this.d(Blocks.IRON_TRAPDOOR);
        this.d(Blocks.PRISMARINE);
        this.d(Blocks.PRISMARINE_BRICKS);
        this.d(Blocks.DARK_PRISMARINE);
        this.d(Blocks.PRISMARINE_STAIRS);
        this.d(Blocks.PRISMARINE_BRICK_STAIRS);
        this.d(Blocks.DARK_PRISMARINE_STAIRS);
        this.d(Blocks.HAY_BLOCK);
        this.d(Blocks.WHITE_CARPET);
        this.d(Blocks.ORANGE_CARPET);
        this.d(Blocks.MAGENTA_CARPET);
        this.d(Blocks.LIGHT_BLUE_CARPET);
        this.d(Blocks.YELLOW_CARPET);
        this.d(Blocks.LIME_CARPET);
        this.d(Blocks.PINK_CARPET);
        this.d(Blocks.GRAY_CARPET);
        this.d(Blocks.LIGHT_GRAY_CARPET);
        this.d(Blocks.CYAN_CARPET);
        this.d(Blocks.PURPLE_CARPET);
        this.d(Blocks.BLUE_CARPET);
        this.d(Blocks.BROWN_CARPET);
        this.d(Blocks.GREEN_CARPET);
        this.d(Blocks.RED_CARPET);
        this.d(Blocks.BLACK_CARPET);
        this.d(Blocks.TERRACOTTA);
        this.d(Blocks.COAL_BLOCK);
        this.d(Blocks.RED_SANDSTONE);
        this.d(Blocks.CHISELED_RED_SANDSTONE);
        this.d(Blocks.CUT_RED_SANDSTONE);
        this.d(Blocks.RED_SANDSTONE_STAIRS);
        this.d(Blocks.SMOOTH_STONE);
        this.d(Blocks.SMOOTH_SANDSTONE);
        this.d(Blocks.SMOOTH_QUARTZ);
        this.d(Blocks.SMOOTH_RED_SANDSTONE);
        this.d(Blocks.SPRUCE_FENCE_GATE);
        this.d(Blocks.BIRCH_FENCE_GATE);
        this.d(Blocks.JUNGLE_FENCE_GATE);
        this.d(Blocks.ACACIA_FENCE_GATE);
        this.d(Blocks.DARK_OAK_FENCE_GATE);
        this.d(Blocks.SPRUCE_FENCE);
        this.d(Blocks.BIRCH_FENCE);
        this.d(Blocks.JUNGLE_FENCE);
        this.d(Blocks.ACACIA_FENCE);
        this.d(Blocks.DARK_OAK_FENCE);
        this.d(Blocks.END_ROD);
        this.d(Blocks.PURPUR_BLOCK);
        this.d(Blocks.PURPUR_PILLAR);
        this.d(Blocks.PURPUR_STAIRS);
        this.d(Blocks.END_STONE_BRICKS);
        this.d(Blocks.MAGMA_BLOCK);
        this.d(Blocks.NETHER_WART_BLOCK);
        this.d(Blocks.RED_NETHER_BRICKS);
        this.d(Blocks.BONE_BLOCK);
        this.d(Blocks.OBSERVER);
        this.d(Blocks.TARGET);
        this.d(Blocks.WHITE_GLAZED_TERRACOTTA);
        this.d(Blocks.ORANGE_GLAZED_TERRACOTTA);
        this.d(Blocks.MAGENTA_GLAZED_TERRACOTTA);
        this.d(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        this.d(Blocks.YELLOW_GLAZED_TERRACOTTA);
        this.d(Blocks.LIME_GLAZED_TERRACOTTA);
        this.d(Blocks.PINK_GLAZED_TERRACOTTA);
        this.d(Blocks.GRAY_GLAZED_TERRACOTTA);
        this.d(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
        this.d(Blocks.CYAN_GLAZED_TERRACOTTA);
        this.d(Blocks.PURPLE_GLAZED_TERRACOTTA);
        this.d(Blocks.BLUE_GLAZED_TERRACOTTA);
        this.d(Blocks.BROWN_GLAZED_TERRACOTTA);
        this.d(Blocks.GREEN_GLAZED_TERRACOTTA);
        this.d(Blocks.RED_GLAZED_TERRACOTTA);
        this.d(Blocks.BLACK_GLAZED_TERRACOTTA);
        this.d(Blocks.WHITE_CONCRETE);
        this.d(Blocks.ORANGE_CONCRETE);
        this.d(Blocks.MAGENTA_CONCRETE);
        this.d(Blocks.LIGHT_BLUE_CONCRETE);
        this.d(Blocks.YELLOW_CONCRETE);
        this.d(Blocks.LIME_CONCRETE);
        this.d(Blocks.PINK_CONCRETE);
        this.d(Blocks.GRAY_CONCRETE);
        this.d(Blocks.LIGHT_GRAY_CONCRETE);
        this.d(Blocks.CYAN_CONCRETE);
        this.d(Blocks.PURPLE_CONCRETE);
        this.d(Blocks.BLUE_CONCRETE);
        this.d(Blocks.BROWN_CONCRETE);
        this.d(Blocks.GREEN_CONCRETE);
        this.d(Blocks.RED_CONCRETE);
        this.d(Blocks.BLACK_CONCRETE);
        this.d(Blocks.WHITE_CONCRETE_POWDER);
        this.d(Blocks.ORANGE_CONCRETE_POWDER);
        this.d(Blocks.MAGENTA_CONCRETE_POWDER);
        this.d(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        this.d(Blocks.YELLOW_CONCRETE_POWDER);
        this.d(Blocks.LIME_CONCRETE_POWDER);
        this.d(Blocks.PINK_CONCRETE_POWDER);
        this.d(Blocks.GRAY_CONCRETE_POWDER);
        this.d(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        this.d(Blocks.CYAN_CONCRETE_POWDER);
        this.d(Blocks.PURPLE_CONCRETE_POWDER);
        this.d(Blocks.BLUE_CONCRETE_POWDER);
        this.d(Blocks.BROWN_CONCRETE_POWDER);
        this.d(Blocks.GREEN_CONCRETE_POWDER);
        this.d(Blocks.RED_CONCRETE_POWDER);
        this.d(Blocks.BLACK_CONCRETE_POWDER);
        this.d(Blocks.KELP);
        this.d(Blocks.DRIED_KELP_BLOCK);
        this.d(Blocks.DEAD_TUBE_CORAL_BLOCK);
        this.d(Blocks.DEAD_BRAIN_CORAL_BLOCK);
        this.d(Blocks.DEAD_BUBBLE_CORAL_BLOCK);
        this.d(Blocks.DEAD_FIRE_CORAL_BLOCK);
        this.d(Blocks.DEAD_HORN_CORAL_BLOCK);
        this.d(Blocks.CONDUIT);
        this.d(Blocks.DRAGON_EGG);
        this.d(Blocks.BAMBOO);
        this.d(Blocks.POLISHED_GRANITE_STAIRS);
        this.d(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        this.d(Blocks.MOSSY_STONE_BRICK_STAIRS);
        this.d(Blocks.POLISHED_DIORITE_STAIRS);
        this.d(Blocks.MOSSY_COBBLESTONE_STAIRS);
        this.d(Blocks.END_STONE_BRICK_STAIRS);
        this.d(Blocks.STONE_STAIRS);
        this.d(Blocks.SMOOTH_SANDSTONE_STAIRS);
        this.d(Blocks.SMOOTH_QUARTZ_STAIRS);
        this.d(Blocks.GRANITE_STAIRS);
        this.d(Blocks.ANDESITE_STAIRS);
        this.d(Blocks.RED_NETHER_BRICK_STAIRS);
        this.d(Blocks.POLISHED_ANDESITE_STAIRS);
        this.d(Blocks.DIORITE_STAIRS);
        this.d(Blocks.BRICK_WALL);
        this.d(Blocks.PRISMARINE_WALL);
        this.d(Blocks.RED_SANDSTONE_WALL);
        this.d(Blocks.MOSSY_STONE_BRICK_WALL);
        this.d(Blocks.GRANITE_WALL);
        this.d(Blocks.STONE_BRICK_WALL);
        this.d(Blocks.NETHER_BRICK_WALL);
        this.d(Blocks.ANDESITE_WALL);
        this.d(Blocks.RED_NETHER_BRICK_WALL);
        this.d(Blocks.SANDSTONE_WALL);
        this.d(Blocks.END_STONE_BRICK_WALL);
        this.d(Blocks.DIORITE_WALL);
        this.d(Blocks.LOOM);
        this.d(Blocks.SCAFFOLDING);
        this.d(Blocks.HONEY_BLOCK);
        this.d(Blocks.HONEYCOMB_BLOCK);
        this.d(Blocks.RESPAWN_ANCHOR);
        this.d(Blocks.LODESTONE);
        this.d(Blocks.WARPED_STEM);
        this.d(Blocks.WARPED_HYPHAE);
        this.d(Blocks.WARPED_FUNGUS);
        this.d(Blocks.WARPED_WART_BLOCK);
        this.d(Blocks.CRIMSON_STEM);
        this.d(Blocks.CRIMSON_HYPHAE);
        this.d(Blocks.CRIMSON_FUNGUS);
        this.d(Blocks.SHROOMLIGHT);
        this.d(Blocks.CRIMSON_PLANKS);
        this.d(Blocks.WARPED_PLANKS);
        this.d(Blocks.WARPED_PRESSURE_PLATE);
        this.d(Blocks.WARPED_FENCE);
        this.d(Blocks.WARPED_TRAPDOOR);
        this.d(Blocks.WARPED_FENCE_GATE);
        this.d(Blocks.WARPED_STAIRS);
        this.d(Blocks.WARPED_BUTTON);
        this.d(Blocks.WARPED_SIGN);
        this.d(Blocks.CRIMSON_PRESSURE_PLATE);
        this.d(Blocks.CRIMSON_FENCE);
        this.d(Blocks.CRIMSON_TRAPDOOR);
        this.d(Blocks.CRIMSON_FENCE_GATE);
        this.d(Blocks.CRIMSON_STAIRS);
        this.d(Blocks.CRIMSON_BUTTON);
        this.d(Blocks.CRIMSON_SIGN);
        this.d(Blocks.NETHERITE_BLOCK);
        this.d(Blocks.ANCIENT_DEBRIS);
        this.d(Blocks.BLACKSTONE);
        this.d(Blocks.POLISHED_BLACKSTONE_BRICKS);
        this.d(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        this.d(Blocks.BLACKSTONE_STAIRS);
        this.d(Blocks.BLACKSTONE_WALL);
        this.d(Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        this.d(Blocks.CHISELED_POLISHED_BLACKSTONE);
        this.d(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        this.d(Blocks.POLISHED_BLACKSTONE);
        this.d(Blocks.POLISHED_BLACKSTONE_STAIRS);
        this.d(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        this.d(Blocks.POLISHED_BLACKSTONE_BUTTON);
        this.d(Blocks.POLISHED_BLACKSTONE_WALL);
        this.d(Blocks.CHISELED_NETHER_BRICKS);
        this.d(Blocks.CRACKED_NETHER_BRICKS);
        this.d(Blocks.QUARTZ_BRICKS);
        this.d(Blocks.CHAIN);
        this.d(Blocks.WARPED_ROOTS);
        this.d(Blocks.CRIMSON_ROOTS);
        this.a(Blocks.FARMLAND, (IMaterial) Blocks.DIRT);
        this.a(Blocks.TRIPWIRE, (IMaterial) Items.STRING);
        this.a(Blocks.GRASS_PATH, (IMaterial) Blocks.DIRT);
        this.a(Blocks.KELP_PLANT, (IMaterial) Blocks.KELP);
        this.a(Blocks.BAMBOO_SAPLING, (IMaterial) Blocks.BAMBOO);
        this.a(Blocks.STONE, (block) -> {
            return b(block, (IMaterial) Blocks.COBBLESTONE);
        });
        this.a(Blocks.GRASS_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DIRT);
        });
        this.a(Blocks.PODZOL, (block) -> {
            return b(block, (IMaterial) Blocks.DIRT);
        });
        this.a(Blocks.MYCELIUM, (block) -> {
            return b(block, (IMaterial) Blocks.DIRT);
        });
        this.a(Blocks.TUBE_CORAL_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DEAD_TUBE_CORAL_BLOCK);
        });
        this.a(Blocks.BRAIN_CORAL_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DEAD_BRAIN_CORAL_BLOCK);
        });
        this.a(Blocks.BUBBLE_CORAL_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DEAD_BUBBLE_CORAL_BLOCK);
        });
        this.a(Blocks.FIRE_CORAL_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DEAD_FIRE_CORAL_BLOCK);
        });
        this.a(Blocks.HORN_CORAL_BLOCK, (block) -> {
            return b(block, (IMaterial) Blocks.DEAD_HORN_CORAL_BLOCK);
        });
        this.a(Blocks.CRIMSON_NYLIUM, (block) -> {
            return b(block, (IMaterial) Blocks.NETHERRACK);
        });
        this.a(Blocks.WARPED_NYLIUM, (block) -> {
            return b(block, (IMaterial) Blocks.NETHERRACK);
        });
        this.a(Blocks.BOOKSHELF, (block) -> {
            return a(block, (IMaterial) Items.BOOK, (LootValue) LootValueConstant.a(3));
        });
        this.a(Blocks.CLAY, (block) -> {
            return a(block, (IMaterial) Items.CLAY_BALL, (LootValue) LootValueConstant.a(4));
        });
        this.a(Blocks.ENDER_CHEST, (block) -> {
            return a(block, (IMaterial) Blocks.OBSIDIAN, (LootValue) LootValueConstant.a(8));
        });
        this.a(Blocks.SNOW_BLOCK, (block) -> {
            return a(block, (IMaterial) Items.SNOWBALL, (LootValue) LootValueConstant.a(4));
        });
        this.a(Blocks.CHORUS_PLANT, a((IMaterial) Items.CHORUS_FRUIT, (LootValue) LootValueBounds.a(0.0F, 1.0F)));
        this.b(Blocks.POTTED_OAK_SAPLING);
        this.b(Blocks.POTTED_SPRUCE_SAPLING);
        this.b(Blocks.POTTED_BIRCH_SAPLING);
        this.b(Blocks.POTTED_JUNGLE_SAPLING);
        this.b(Blocks.POTTED_ACACIA_SAPLING);
        this.b(Blocks.POTTED_DARK_OAK_SAPLING);
        this.b(Blocks.POTTED_FERN);
        this.b(Blocks.POTTED_DANDELION);
        this.b(Blocks.POTTED_POPPY);
        this.b(Blocks.POTTED_BLUE_ORCHID);
        this.b(Blocks.POTTED_ALLIUM);
        this.b(Blocks.POTTED_AZURE_BLUET);
        this.b(Blocks.POTTED_RED_TULIP);
        this.b(Blocks.POTTED_ORANGE_TULIP);
        this.b(Blocks.POTTED_WHITE_TULIP);
        this.b(Blocks.POTTED_PINK_TULIP);
        this.b(Blocks.POTTED_OXEYE_DAISY);
        this.b(Blocks.POTTED_CORNFLOWER);
        this.b(Blocks.POTTED_LILY_OF_THE_VALLEY);
        this.b(Blocks.POTTED_WITHER_ROSE);
        this.b(Blocks.POTTED_RED_MUSHROOM);
        this.b(Blocks.POTTED_BROWN_MUSHROOM);
        this.b(Blocks.POTTED_DEAD_BUSH);
        this.b(Blocks.POTTED_CACTUS);
        this.b(Blocks.POTTED_BAMBOO);
        this.b(Blocks.POTTED_CRIMSON_FUNGUS);
        this.b(Blocks.POTTED_WARPED_FUNGUS);
        this.b(Blocks.POTTED_CRIMSON_ROOTS);
        this.b(Blocks.POTTED_WARPED_ROOTS);
        this.a(Blocks.ACACIA_SLAB, hz::e);
        this.a(Blocks.BIRCH_SLAB, hz::e);
        this.a(Blocks.BRICK_SLAB, hz::e);
        this.a(Blocks.COBBLESTONE_SLAB, hz::e);
        this.a(Blocks.DARK_OAK_SLAB, hz::e);
        this.a(Blocks.DARK_PRISMARINE_SLAB, hz::e);
        this.a(Blocks.JUNGLE_SLAB, hz::e);
        this.a(Blocks.NETHER_BRICK_SLAB, hz::e);
        this.a(Blocks.OAK_SLAB, hz::e);
        this.a(Blocks.PETRIFIED_OAK_SLAB, hz::e);
        this.a(Blocks.PRISMARINE_BRICK_SLAB, hz::e);
        this.a(Blocks.PRISMARINE_SLAB, hz::e);
        this.a(Blocks.PURPUR_SLAB, hz::e);
        this.a(Blocks.QUARTZ_SLAB, hz::e);
        this.a(Blocks.RED_SANDSTONE_SLAB, hz::e);
        this.a(Blocks.SANDSTONE_SLAB, hz::e);
        this.a(Blocks.CUT_RED_SANDSTONE_SLAB, hz::e);
        this.a(Blocks.CUT_SANDSTONE_SLAB, hz::e);
        this.a(Blocks.SPRUCE_SLAB, hz::e);
        this.a(Blocks.STONE_BRICK_SLAB, hz::e);
        this.a(Blocks.STONE_SLAB, hz::e);
        this.a(Blocks.SMOOTH_STONE_SLAB, hz::e);
        this.a(Blocks.POLISHED_GRANITE_SLAB, hz::e);
        this.a(Blocks.SMOOTH_RED_SANDSTONE_SLAB, hz::e);
        this.a(Blocks.MOSSY_STONE_BRICK_SLAB, hz::e);
        this.a(Blocks.POLISHED_DIORITE_SLAB, hz::e);
        this.a(Blocks.MOSSY_COBBLESTONE_SLAB, hz::e);
        this.a(Blocks.END_STONE_BRICK_SLAB, hz::e);
        this.a(Blocks.SMOOTH_SANDSTONE_SLAB, hz::e);
        this.a(Blocks.SMOOTH_QUARTZ_SLAB, hz::e);
        this.a(Blocks.GRANITE_SLAB, hz::e);
        this.a(Blocks.ANDESITE_SLAB, hz::e);
        this.a(Blocks.RED_NETHER_BRICK_SLAB, hz::e);
        this.a(Blocks.POLISHED_ANDESITE_SLAB, hz::e);
        this.a(Blocks.DIORITE_SLAB, hz::e);
        this.a(Blocks.CRIMSON_SLAB, hz::e);
        this.a(Blocks.WARPED_SLAB, hz::e);
        this.a(Blocks.BLACKSTONE_SLAB, hz::e);
        this.a(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, hz::e);
        this.a(Blocks.POLISHED_BLACKSTONE_SLAB, hz::e);
        this.a(Blocks.ACACIA_DOOR, hz::a);
        this.a(Blocks.BIRCH_DOOR, hz::a);
        this.a(Blocks.DARK_OAK_DOOR, hz::a);
        this.a(Blocks.IRON_DOOR, hz::a);
        this.a(Blocks.JUNGLE_DOOR, hz::a);
        this.a(Blocks.OAK_DOOR, hz::a);
        this.a(Blocks.SPRUCE_DOOR, hz::a);
        this.a(Blocks.WARPED_DOOR, hz::a);
        this.a(Blocks.CRIMSON_DOOR, hz::a);
        this.a(Blocks.BLACK_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.BLUE_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.BROWN_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.CYAN_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.GRAY_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.GREEN_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.LIGHT_BLUE_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.LIGHT_GRAY_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.LIME_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.MAGENTA_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.PURPLE_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.ORANGE_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.PINK_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.RED_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.WHITE_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.YELLOW_BED, (block) -> {
            return a(block, (IBlockState) BlockBed.PART, (Comparable) BlockPropertyBedPart.HEAD);
        });
        this.a(Blocks.LILAC, (block) -> {
            return a(block, (IBlockState) BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER);
        });
        this.a(Blocks.SUNFLOWER, (block) -> {
            return a(block, (IBlockState) BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER);
        });
        this.a(Blocks.PEONY, (block) -> {
            return a(block, (IBlockState) BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER);
        });
        this.a(Blocks.ROSE_BUSH, (block) -> {
            return a(block, (IBlockState) BlockTallPlant.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER);
        });
        this.a(Blocks.TNT, LootTable.b().a((LootSelector.a) a((IMaterial) Blocks.TNT, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a(LootItem.a((IMaterial) Blocks.TNT).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(Blocks.TNT).a(CriterionTriggerProperties.a.a().a(BlockTNT.a, false)))))));
        this.a(Blocks.COCOA, (block) -> {
            return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.COCOA_BEANS).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(3)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockCocoa.AGE, 2)))))));
        });
        this.a(Blocks.SEA_PICKLE, (block) -> {
            return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) a((IMaterial) Blocks.SEA_PICKLE, (LootItemFunctionUser) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSeaPickle.a, 2)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(3)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSeaPickle.a, 3)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(4)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSeaPickle.a, 4)))))));
        });
        this.a(Blocks.COMPOSTER, (block) -> {
            return LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.qZ)))).a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.BONE_MEAL)).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockComposter.a, 8))));
        });
        this.a(Blocks.BEACON, hz::f);
        this.a(Blocks.BREWING_STAND, hz::f);
        this.a(Blocks.CHEST, hz::f);
        this.a(Blocks.DISPENSER, hz::f);
        this.a(Blocks.DROPPER, hz::f);
        this.a(Blocks.ENCHANTING_TABLE, hz::f);
        this.a(Blocks.FURNACE, hz::f);
        this.a(Blocks.HOPPER, hz::f);
        this.a(Blocks.TRAPPED_CHEST, hz::f);
        this.a(Blocks.SMOKER, hz::f);
        this.a(Blocks.BLAST_FURNACE, hz::f);
        this.a(Blocks.BARREL, hz::f);
        this.a(Blocks.CARTOGRAPHY_TABLE, hz::f);
        this.a(Blocks.FLETCHING_TABLE, hz::f);
        this.a(Blocks.GRINDSTONE, hz::f);
        this.a(Blocks.LECTERN, hz::f);
        this.a(Blocks.SMITHING_TABLE, hz::f);
        this.a(Blocks.STONECUTTER, hz::f);
        this.a(Blocks.BELL, hz::a);
        this.a(Blocks.LANTERN, hz::a);
        this.a(Blocks.SOUL_LANTERN, hz::a);
        this.a(Blocks.SHULKER_BOX, hz::g);
        this.a(Blocks.BLACK_SHULKER_BOX, hz::g);
        this.a(Blocks.BLUE_SHULKER_BOX, hz::g);
        this.a(Blocks.BROWN_SHULKER_BOX, hz::g);
        this.a(Blocks.CYAN_SHULKER_BOX, hz::g);
        this.a(Blocks.GRAY_SHULKER_BOX, hz::g);
        this.a(Blocks.GREEN_SHULKER_BOX, hz::g);
        this.a(Blocks.LIGHT_BLUE_SHULKER_BOX, hz::g);
        this.a(Blocks.LIGHT_GRAY_SHULKER_BOX, hz::g);
        this.a(Blocks.LIME_SHULKER_BOX, hz::g);
        this.a(Blocks.MAGENTA_SHULKER_BOX, hz::g);
        this.a(Blocks.ORANGE_SHULKER_BOX, hz::g);
        this.a(Blocks.PINK_SHULKER_BOX, hz::g);
        this.a(Blocks.PURPLE_SHULKER_BOX, hz::g);
        this.a(Blocks.RED_SHULKER_BOX, hz::g);
        this.a(Blocks.WHITE_SHULKER_BOX, hz::g);
        this.a(Blocks.YELLOW_SHULKER_BOX, hz::g);
        this.a(Blocks.BLACK_BANNER, hz::h);
        this.a(Blocks.BLUE_BANNER, hz::h);
        this.a(Blocks.BROWN_BANNER, hz::h);
        this.a(Blocks.CYAN_BANNER, hz::h);
        this.a(Blocks.GRAY_BANNER, hz::h);
        this.a(Blocks.GREEN_BANNER, hz::h);
        this.a(Blocks.LIGHT_BLUE_BANNER, hz::h);
        this.a(Blocks.LIGHT_GRAY_BANNER, hz::h);
        this.a(Blocks.LIME_BANNER, hz::h);
        this.a(Blocks.MAGENTA_BANNER, hz::h);
        this.a(Blocks.ORANGE_BANNER, hz::h);
        this.a(Blocks.PINK_BANNER, hz::h);
        this.a(Blocks.PURPLE_BANNER, hz::h);
        this.a(Blocks.RED_BANNER, hz::h);
        this.a(Blocks.WHITE_BANNER, hz::h);
        this.a(Blocks.YELLOW_BANNER, hz::h);
        this.a(Blocks.PLAYER_HEAD, (block) -> {
            return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemConditionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) block).b((LootItemFunction.a) LootItemFunctionCopyNBT.a(LootItemFunctionCopyNBT.Source.BLOCK_ENTITY).a("SkullOwner", "SkullOwner")))));
        });
        this.a(Blocks.BEE_NEST, hz::i);
        this.a(Blocks.BEEHIVE, hz::j);
        this.a(Blocks.BIRCH_LEAVES, (block) -> {
            return a(block, Blocks.BIRCH_SAPLING, hz.g);
        });
        this.a(Blocks.ACACIA_LEAVES, (block) -> {
            return a(block, Blocks.ACACIA_SAPLING, hz.g);
        });
        this.a(Blocks.JUNGLE_LEAVES, (block) -> {
            return a(block, Blocks.JUNGLE_SAPLING, hz.h);
        });
        this.a(Blocks.SPRUCE_LEAVES, (block) -> {
            return a(block, Blocks.SPRUCE_SAPLING, hz.g);
        });
        this.a(Blocks.OAK_LEAVES, (block) -> {
            return b(block, Blocks.OAK_SAPLING, hz.g);
        });
        this.a(Blocks.DARK_OAK_LEAVES, (block) -> {
            return b(block, Blocks.DARK_OAK_SAPLING, hz.g);
        });
        LootItemConditionBlockStateProperty.a lootitemconditionblockstateproperty_a = LootItemConditionBlockStateProperty.a(Blocks.BEETROOTS).a(CriterionTriggerProperties.a.a().a(BlockBeetroot.a, 3));

        this.a(Blocks.BEETROOTS, a(Blocks.BEETROOTS, Items.BEETROOT, Items.BEETROOT_SEEDS, lootitemconditionblockstateproperty_a));
        LootItemConditionBlockStateProperty.a lootitemconditionblockstateproperty_a1 = LootItemConditionBlockStateProperty.a(Blocks.WHEAT).a(CriterionTriggerProperties.a.a().a(BlockCrops.AGE, 7));

        this.a(Blocks.WHEAT, a(Blocks.WHEAT, Items.WHEAT, Items.WHEAT_SEEDS, lootitemconditionblockstateproperty_a1));
        LootItemConditionBlockStateProperty.a lootitemconditionblockstateproperty_a2 = LootItemConditionBlockStateProperty.a(Blocks.CARROTS).a(CriterionTriggerProperties.a.a().a(BlockCarrots.AGE, 7));

        this.a(Blocks.CARROTS, (LootTable.a) a((IMaterial) Blocks.CARROTS, (LootItemFunctionUser) LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CARROT))).a(LootSelector.a().b((LootItemCondition.a) lootitemconditionblockstateproperty_a2).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.CARROT).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286F, 3))))));
        LootItemConditionBlockStateProperty.a lootitemconditionblockstateproperty_a3 = LootItemConditionBlockStateProperty.a(Blocks.POTATOES).a(CriterionTriggerProperties.a.a().a(BlockPotatoes.AGE, 7));

        this.a(Blocks.POTATOES, (LootTable.a) a((IMaterial) Blocks.POTATOES, (LootItemFunctionUser) LootTable.b().a(LootSelector.a().a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.POTATO))).a(LootSelector.a().b((LootItemCondition.a) lootitemconditionblockstateproperty_a3).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.POTATO).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.5714286F, 3)))).a(LootSelector.a().b((LootItemCondition.a) lootitemconditionblockstateproperty_a3).a(LootItem.a((IMaterial) Items.POISONOUS_POTATO).b(LootItemConditionRandomChance.a(0.02F))))));
        this.a(Blocks.SWEET_BERRY_BUSH, (block) -> {
            return (LootTable.a) a((IMaterial) block, (LootItemFunctionUser) LootTable.b().a(LootSelector.a().b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(Blocks.SWEET_BERRY_BUSH).a(CriterionTriggerProperties.a.a().a(BlockSweetBerryBush.a, 3))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.SWEET_BERRIES)).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 3.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))).a(LootSelector.a().b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(Blocks.SWEET_BERRY_BUSH).a(CriterionTriggerProperties.a.a().a(BlockSweetBerryBush.a, 2))).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.SWEET_BERRIES)).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(1.0F, 2.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))));
        });
        this.a(Blocks.BROWN_MUSHROOM_BLOCK, (block) -> {
            return c(block, (IMaterial) Blocks.BROWN_MUSHROOM);
        });
        this.a(Blocks.RED_MUSHROOM_BLOCK, (block) -> {
            return c(block, (IMaterial) Blocks.RED_MUSHROOM);
        });
        this.a(Blocks.COAL_ORE, (block) -> {
            return a(block, Items.COAL);
        });
        this.a(Blocks.EMERALD_ORE, (block) -> {
            return a(block, Items.EMERALD);
        });
        this.a(Blocks.NETHER_QUARTZ_ORE, (block) -> {
            return a(block, Items.QUARTZ);
        });
        this.a(Blocks.DIAMOND_ORE, (block) -> {
            return a(block, Items.DIAMOND);
        });
        this.a(Blocks.NETHER_GOLD_ORE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.GOLD_NUGGET).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 6.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS))));
        });
        this.a(Blocks.LAPIS_ORE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.LAPIS_LAZULI).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(4.0F, 9.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.a(Enchantments.LOOT_BONUS_BLOCKS))));
        });
        this.a(Blocks.COBWEB, (block) -> {
            return c(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) Items.STRING)));
        });
        this.a(Blocks.DEAD_BUSH, (block) -> {
            return b(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.STICK).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(0.0F, 2.0F)))));
        });
        this.a(Blocks.NETHER_SPROUTS, hz::d);
        this.a(Blocks.SEAGRASS, hz::d);
        this.a(Blocks.VINE, hz::d);
        this.a(Blocks.TALL_SEAGRASS, l(Blocks.SEAGRASS));
        this.a(Blocks.LARGE_FERN, (block) -> {
            return b(block, Blocks.FERN);
        });
        this.a(Blocks.TALL_GRASS, (block) -> {
            return b(block, Blocks.GRASS);
        });
        this.a(Blocks.MELON_STEM, (block) -> {
            return b(block, Items.MELON_SEEDS);
        });
        this.a(Blocks.ATTACHED_MELON_STEM, (block) -> {
            return c(block, Items.MELON_SEEDS);
        });
        this.a(Blocks.PUMPKIN_STEM, (block) -> {
            return b(block, Items.PUMPKIN_SEEDS);
        });
        this.a(Blocks.ATTACHED_PUMPKIN_STEM, (block) -> {
            return c(block, Items.PUMPKIN_SEEDS);
        });
        this.a(Blocks.CHORUS_FLOWER, (block) -> {
            return LootTable.b().a(LootSelector.a().a((LootValue) LootValueConstant.a(1)).a(((LootSelectorEntry.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) block))).b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS))));
        });
        this.a(Blocks.FERN, hz::k);
        this.a(Blocks.GRASS, hz::k);
        this.a(Blocks.GLOWSTONE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.GLOWSTONE_DUST).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 4.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS)).b((LootItemFunction.a) LootItemFunctionLimitCount.a(LootIntegerLimit.a(1, 4)))));
        });
        this.a(Blocks.MELON, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.MELON_SLICE).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(3.0F, 7.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS)).b((LootItemFunction.a) LootItemFunctionLimitCount.a(LootIntegerLimit.b(9)))));
        });
        this.a(Blocks.REDSTONE_ORE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.REDSTONE).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(4.0F, 5.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS))));
        });
        this.a(Blocks.SEA_LANTERN, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemFunctionUser) LootItem.a((IMaterial) Items.PRISMARINE_CRYSTALS).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 3.0F))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS)).b((LootItemFunction.a) LootItemFunctionLimitCount.a(LootIntegerLimit.a(1, 5)))));
        });
        this.a(Blocks.NETHER_WART, (block) -> {
            return LootTable.b().a((LootSelector.a) a((IMaterial) block, (LootItemFunctionUser) LootSelector.a().a((LootValue) LootValueConstant.a(1)).a((LootEntryAbstract.a) LootItem.a((IMaterial) Items.NETHER_WART).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 4.0F)).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockNetherWart.AGE, 3)))).b((LootItemFunction.a) LootItemFunctionApplyBonus.b(Enchantments.LOOT_BONUS_BLOCKS).b(LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockNetherWart.AGE, 3)))))));
        });
        this.a(Blocks.SNOW, (block) -> {
            return LootTable.b().a(LootSelector.a().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS)).a((LootEntryAbstract.a) LootEntryAlternatives.a(LootEntryAlternatives.a(LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 1))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 2)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 3)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(3))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 4)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(4))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 5)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(5))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 6)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(6))), ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 7)))).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(7))), LootItem.a((IMaterial) Items.SNOWBALL).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(8)))).b(hz.b), LootEntryAlternatives.a(LootItem.a((IMaterial) Blocks.SNOW).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 1))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 2))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(3))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 3))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(4))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 4))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(5))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 5))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(6))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 6))), LootItem.a((IMaterial) Blocks.SNOW).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(7))).b((LootItemCondition.a) LootItemConditionBlockStateProperty.a(block).a(CriterionTriggerProperties.a.a().a(BlockSnow.LAYERS, 7))), LootItem.a((IMaterial) Blocks.SNOW_BLOCK)))));
        });
        this.a(Blocks.GRAVEL, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.FLINT).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.1F, 0.14285715F, 0.25F, 1.0F))).a(LootItem.a((IMaterial) block))));
        });
        this.a(Blocks.CAMPFIRE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) Items.CHARCOAL).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(2)))));
        });
        this.a(Blocks.GILDED_BLACKSTONE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) ((LootSelectorEntry.a) LootItem.a((IMaterial) Items.GOLD_NUGGET).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueBounds.a(2.0F, 5.0F))).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.1F, 0.14285715F, 0.25F, 1.0F))).a(LootItem.a((IMaterial) block))));
        });
        this.a(Blocks.SOUL_CAMPFIRE, (block) -> {
            return a(block, (LootEntryAbstract.a) a((IMaterial) block, (LootItemConditionUser) LootItem.a((IMaterial) Items.dm).b((LootItemFunction.a) LootItemFunctionSetCount.a((LootValue) LootValueConstant.a(1)))));
        });
        this.c(Blocks.GLASS);
        this.c(Blocks.WHITE_STAINED_GLASS);
        this.c(Blocks.ORANGE_STAINED_GLASS);
        this.c(Blocks.MAGENTA_STAINED_GLASS);
        this.c(Blocks.LIGHT_BLUE_STAINED_GLASS);
        this.c(Blocks.YELLOW_STAINED_GLASS);
        this.c(Blocks.LIME_STAINED_GLASS);
        this.c(Blocks.PINK_STAINED_GLASS);
        this.c(Blocks.GRAY_STAINED_GLASS);
        this.c(Blocks.LIGHT_GRAY_STAINED_GLASS);
        this.c(Blocks.CYAN_STAINED_GLASS);
        this.c(Blocks.PURPLE_STAINED_GLASS);
        this.c(Blocks.BLUE_STAINED_GLASS);
        this.c(Blocks.BROWN_STAINED_GLASS);
        this.c(Blocks.GREEN_STAINED_GLASS);
        this.c(Blocks.RED_STAINED_GLASS);
        this.c(Blocks.BLACK_STAINED_GLASS);
        this.c(Blocks.GLASS_PANE);
        this.c(Blocks.WHITE_STAINED_GLASS_PANE);
        this.c(Blocks.ORANGE_STAINED_GLASS_PANE);
        this.c(Blocks.MAGENTA_STAINED_GLASS_PANE);
        this.c(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        this.c(Blocks.YELLOW_STAINED_GLASS_PANE);
        this.c(Blocks.LIME_STAINED_GLASS_PANE);
        this.c(Blocks.PINK_STAINED_GLASS_PANE);
        this.c(Blocks.GRAY_STAINED_GLASS_PANE);
        this.c(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        this.c(Blocks.CYAN_STAINED_GLASS_PANE);
        this.c(Blocks.PURPLE_STAINED_GLASS_PANE);
        this.c(Blocks.BLUE_STAINED_GLASS_PANE);
        this.c(Blocks.BROWN_STAINED_GLASS_PANE);
        this.c(Blocks.GREEN_STAINED_GLASS_PANE);
        this.c(Blocks.RED_STAINED_GLASS_PANE);
        this.c(Blocks.BLACK_STAINED_GLASS_PANE);
        this.c(Blocks.ICE);
        this.c(Blocks.PACKED_ICE);
        this.c(Blocks.BLUE_ICE);
        this.c(Blocks.TURTLE_EGG);
        this.c(Blocks.MUSHROOM_STEM);
        this.c(Blocks.DEAD_TUBE_CORAL);
        this.c(Blocks.DEAD_BRAIN_CORAL);
        this.c(Blocks.DEAD_BUBBLE_CORAL);
        this.c(Blocks.DEAD_FIRE_CORAL);
        this.c(Blocks.DEAD_HORN_CORAL);
        this.c(Blocks.TUBE_CORAL);
        this.c(Blocks.BRAIN_CORAL);
        this.c(Blocks.BUBBLE_CORAL);
        this.c(Blocks.FIRE_CORAL);
        this.c(Blocks.HORN_CORAL);
        this.c(Blocks.DEAD_TUBE_CORAL_FAN);
        this.c(Blocks.DEAD_BRAIN_CORAL_FAN);
        this.c(Blocks.DEAD_BUBBLE_CORAL_FAN);
        this.c(Blocks.DEAD_FIRE_CORAL_FAN);
        this.c(Blocks.DEAD_HORN_CORAL_FAN);
        this.c(Blocks.TUBE_CORAL_FAN);
        this.c(Blocks.BRAIN_CORAL_FAN);
        this.c(Blocks.BUBBLE_CORAL_FAN);
        this.c(Blocks.FIRE_CORAL_FAN);
        this.c(Blocks.HORN_CORAL_FAN);
        this.a(Blocks.INFESTED_STONE, Blocks.STONE);
        this.a(Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE);
        this.a(Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS);
        this.a(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
        this.a(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
        this.a(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS);
        this.c(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
        this.c(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
        this.a(Blocks.CAKE, a());
        this.a(Blocks.FROSTED_ICE, a());
        this.a(Blocks.SPAWNER, a());
        this.a(Blocks.FIRE, a());
        this.a(Blocks.SOUL_FIRE, a());
        this.a(Blocks.NETHER_PORTAL, a());
        Set<MinecraftKey> set = Sets.newHashSet();
        Iterator iterator = IRegistry.BLOCK.iterator();

        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();
            MinecraftKey minecraftkey = block.r();

            if (minecraftkey != LootTables.a && set.add(minecraftkey)) {
                LootTable.a loottable_a = (LootTable.a) this.i.remove(minecraftkey);

                if (loottable_a == null) {
                    throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", minecraftkey, IRegistry.BLOCK.getKey(block)));
                }

                biconsumer.accept(minecraftkey, loottable_a);
            }
        }

        if (!this.i.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.i.keySet());
        }
    }

    private void c(Block block, Block block1) {
        LootTable.a loottable_a = c(block, LootItem.a((IMaterial) block).b(LootItemConditionTableBonus.a(Enchantments.LOOT_BONUS_BLOCKS, 0.33F, 0.55F, 0.77F, 1.0F)));

        this.a(block, loottable_a);
        this.a(block1, loottable_a);
    }

    public static LootTable.a a(Block block) {
        return a(block, (IBlockState) BlockDoor.HALF, (Comparable) BlockPropertyDoubleBlockHalf.LOWER);
    }

    public void b(Block block) {
        this.a(block, (block1) -> {
            return c((IMaterial) ((BlockFlowerPot) block1).c());
        });
    }

    public void a(Block block, Block block1) {
        this.a(block, b((IMaterial) block1));
    }

    public void a(Block block, IMaterial imaterial) {
        this.a(block, a(imaterial));
    }

    public void c(Block block) {
        this.a(block, block);
    }

    public void d(Block block) {
        this.a(block, (IMaterial) block);
    }

    private void a(Block block, Function<Block, LootTable.a> function) {
        this.a(block, (LootTable.a) function.apply(block));
    }

    private void a(Block block, LootTable.a loottable_a) {
        this.i.put(block.r(), loottable_a);
    }
}
