package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.SystemUtils;
import net.minecraft.advancements.critereon.CriterionConditionDamageSource;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionEntityFlags;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsEntity;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootSelectorEmpty;
import net.minecraft.world.level.storage.loot.entries.LootSelectorLootTable;
import net.minecraft.world.level.storage.loot.entries.LootSelectorTag;
import net.minecraft.world.level.storage.loot.functions.LootEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSetTag;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionSmelt;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ib implements Consumer<BiConsumer<MinecraftKey, LootTable.a>> {

	private static final CriterionConditionEntity.a_ a = CriterionConditionEntity.a_.a().a(CriterionConditionEntityFlags.a_.a().a(true).b());
	private static final Set<EntityTypes<?>> b = ImmutableSet.of(EntityTypes.PLAYER, EntityTypes.ARMOR_STAND, EntityTypes.IRON_GOLEM, EntityTypes.SNOW_GOLEM, EntityTypes.VILLAGER);
	private final Map<MinecraftKey, LootTable.a> c = Maps.newHashMap();

	public ib() {
	}

	private static LootTable.a a(IMaterial imaterial) {
		return LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(imaterial))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootSelectorLootTable.a(EntityTypes.SHEEP.i())));
	}

	public void accept(BiConsumer<MinecraftKey, LootTable.a> biconsumer) {
		this.a(EntityTypes.ARMOR_STAND, LootTable.b());
		this.a(EntityTypes.BAT, LootTable.b());
		this.a(EntityTypes.BEE, LootTable.b());
		this.a(EntityTypes.BLAZE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BLAZE_ROD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.CAT, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STRING).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))))));
		this.a(EntityTypes.CAVE_SPIDER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STRING).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SPIDER_EYE).b(LootItemFunctionSetCount.a(LootValueBounds.a(-1.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.CHICKEN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.FEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.CHICKEN).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.COD, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE_MEAL)).b(LootItemConditionRandomChance.a(0.05F))));
		this.a(EntityTypes.COW, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BEEF).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.CREEPER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GUNPOWDER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootSelectorTag.b(TagsItem.CREEPER_DROP_MUSIC_DISCS)).b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.KILLER, CriterionConditionEntity.a_.a().a(TagsEntity.SKELETONS)))));
		this.a(EntityTypes.DOLPHIN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))))));
		this.a(EntityTypes.DONKEY, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.DROWNED, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GOLD_INGOT)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.05F, 0.01F))));
		this.a(EntityTypes.ELDER_GUARDIAN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PRISMARINE_SHARD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD).a(3).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a)))).a(LootItem.a(Items.PRISMARINE_CRYSTALS).a(2).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootSelectorEmpty.b())).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.WET_SPONGE)).b(LootItemConditionKilledByPlayer.c())).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootSelectorLootTable.a(LootTables.aj)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.ENDER_DRAGON, LootTable.b());
		this.a(EntityTypes.ENDERMAN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ENDER_PEARL).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.ENDERMITE, LootTable.b());
		this.a(EntityTypes.EVOKER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.TOTEM_OF_UNDYING))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.EMERALD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.FOX, LootTable.b());
		this.a(EntityTypes.GHAST, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GHAST_TEAR).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GUNPOWDER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.GIANT, LootTable.b());
		this.a(EntityTypes.GUARDIAN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PRISMARINE_SHARD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD).a(2).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a)))).a(LootItem.a(Items.PRISMARINE_CRYSTALS).a(2).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootSelectorEmpty.b())).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootSelectorLootTable.a(LootTables.aj)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.HORSE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.HUSK, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.IRON_INGOT)).a(LootItem.a(Items.CARROT)).a(LootItem.a(Items.POTATO)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.RAVAGER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SADDLE).b(LootItemFunctionSetCount.a(LootValueConstant.a(1))))));
		this.a(EntityTypes.ILLUSIONER, LootTable.b());
		this.a(EntityTypes.IRON_GOLEM, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.POPPY).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.IRON_INGOT).b(LootItemFunctionSetCount.a(LootValueBounds.a(3.0F, 5.0F))))));
		this.a(EntityTypes.LLAMA, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.MAGMA_CUBE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.MAGMA_CREAM).b(LootItemFunctionSetCount.a(LootValueBounds.a(-2.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.MULE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.MOOSHROOM, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BEEF).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.OCELOT, LootTable.b());
		this.a(EntityTypes.PANDA, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.BAMBOO).b(LootItemFunctionSetCount.a(LootValueConstant.a(1))))));
		this.a(EntityTypes.PARROT, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.FEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.PHANTOM, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PHANTOM_MEMBRANE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.PIG, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PORKCHOP).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.PILLAGER, LootTable.b());
		this.a(EntityTypes.PLAYER, LootTable.b());
		this.a(EntityTypes.POLAR_BEAR, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COD).a(3).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.SALMON).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.PUFFERFISH, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PUFFERFISH).b(LootItemFunctionSetCount.a(LootValueConstant.a(1))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE_MEAL)).b(LootItemConditionRandomChance.a(0.05F))));
		this.a(EntityTypes.RABBIT, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.RABBIT_HIDE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.RABBIT).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.RABBIT_FOOT)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.1F, 0.03F))));
		this.a(EntityTypes.SALMON, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SALMON).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE_MEAL)).b(LootItemConditionRandomChance.a(0.05F))));
		this.a(EntityTypes.SHEEP, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.MUTTON).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 2.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(LootTables.af, a(Blocks.BLACK_WOOL));
		this.a(LootTables.ab, a(Blocks.BLUE_WOOL));
		this.a(LootTables.ac, a(Blocks.BROWN_WOOL));
		this.a(LootTables.Z, a(Blocks.CYAN_WOOL));
		this.a(LootTables.X, a(Blocks.GRAY_WOOL));
		this.a(LootTables.ad, a(Blocks.GREEN_WOOL));
		this.a(LootTables.T, a(Blocks.LIGHT_BLUE_WOOL));
		this.a(LootTables.Y, a(Blocks.LIGHT_GRAY_WOOL));
		this.a(LootTables.V, a(Blocks.LIME_WOOL));
		this.a(LootTables.S, a(Blocks.MAGENTA_WOOL));
		this.a(LootTables.R, a(Blocks.ORANGE_WOOL));
		this.a(LootTables.W, a(Blocks.PINK_WOOL));
		this.a(LootTables.aa, a(Blocks.PURPLE_WOOL));
		this.a(LootTables.ae, a(Blocks.RED_WOOL));
		this.a(LootTables.Q, a(Blocks.WHITE_WOOL));
		this.a(LootTables.U, a(Blocks.YELLOW_WOOL));
		this.a(EntityTypes.SHULKER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SHULKER_SHELL)).b(LootItemConditionRandomChanceWithLooting.a(0.5F, 0.0625F))));
		this.a(EntityTypes.SILVERFISH, LootTable.b());
		this.a(EntityTypes.SKELETON, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.SKELETON_HORSE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.SLIME, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SLIME_BALL).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.SNOW_GOLEM, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SNOWBALL).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 15.0F))))));
		this.a(EntityTypes.SPIDER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STRING).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.SPIDER_EYE).b(LootItemFunctionSetCount.a(LootValueBounds.a(-1.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.SQUID, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.INK_SAC).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.STRAY, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.TIPPED_ARROW).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)).a(1)).b(LootItemFunctionSetTag.a(SystemUtils.a(new NBTTagCompound(), (nbttagcompound) -> {
					nbttagcompound.setString("Potion", "minecraft:slowness");
				})))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.STRIDER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.STRING).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 5.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.TRADER_LLAMA, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.TROPICAL_FISH, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.TROPICAL_FISH).b(LootItemFunctionSetCount.a(LootValueConstant.a(1))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE_MEAL)).b(LootItemConditionRandomChance.a(0.05F))));
		this.a(EntityTypes.TURTLE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.SEAGRASS).a(3).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BOWL)).b(LootItemConditionDamageSourceProperties.a(CriterionConditionDamageSource.a_.a().h(true)))));
		this.a(EntityTypes.VEX, LootTable.b());
		this.a(EntityTypes.VILLAGER, LootTable.b());
		this.a(EntityTypes.WANDERING_TRADER, LootTable.b());
		this.a(EntityTypes.VINDICATOR, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.EMERALD).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).b(LootItemConditionKilledByPlayer.c())));
		this.a(EntityTypes.WITCH, LootTable.b().a(LootSelector.a().a(LootValueBounds.a(1.0F, 3.0F)).a(LootItem.a(Items.GLOWSTONE_DUST).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.SUGAR).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.REDSTONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.SPIDER_EYE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.GLASS_BOTTLE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.GUNPOWDER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F)))).a(LootItem.a(Items.STICK).a(2).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.WITHER, LootTable.b());
		this.a(EntityTypes.WITHER_SKELETON, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.COAL).b(LootItemFunctionSetCount.a(LootValueBounds.a(-1.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.BONE).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Blocks.WITHER_SKELETON_SKULL)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.WOLF, LootTable.b());
		this.a(EntityTypes.ZOGLIN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(1.0F, 3.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.ZOMBIE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.IRON_INGOT)).a(LootItem.a(Items.CARROT)).a(LootItem.a(Items.POTATO)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.ZOMBIE_HORSE, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.ZOMBIFIED_PIGLIN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GOLD_NUGGET).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.GOLD_INGOT)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		this.a(EntityTypes.HOGLIN, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.PORKCHOP).b(LootItemFunctionSetCount.a(LootValueBounds.a(2.0F, 4.0F))).b(LootItemFunctionSmelt.c().b(LootItemConditionEntityProperty.a(LootTableInfo.EntityTarget.THIS, ib.a))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.LEATHER).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 1.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))));
		this.a(EntityTypes.PIGLIN, LootTable.b());
		this.a(EntityTypes.PIGLIN_BRUTE, LootTable.b());
		this.a(EntityTypes.ZOMBIE_VILLAGER, LootTable.b().a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.ROTTEN_FLESH).b(LootItemFunctionSetCount.a(LootValueBounds.a(0.0F, 2.0F))).b(LootEnchantFunction.a(LootValueBounds.a(0.0F, 1.0F))))).a(LootSelector.a().a(LootValueConstant.a(1)).a(LootItem.a(Items.IRON_INGOT)).a(LootItem.a(Items.CARROT)).a(LootItem.a(Items.POTATO)).b(LootItemConditionKilledByPlayer.c()).b(LootItemConditionRandomChanceWithLooting.a(0.025F, 0.01F))));
		Set<MinecraftKey> set = Sets.newHashSet();
		Iterator iterator = IRegistry.ENTITY_TYPE.iterator();

		while (iterator.hasNext()) {
			EntityTypes<?> entitytypes = (EntityTypes) iterator.next();
			MinecraftKey minecraftkey = entitytypes.i();

			if (!ib.b.contains(entitytypes) && entitytypes.e() == EnumCreatureType.MISC) {
				if (minecraftkey != LootTables.a && this.c.remove(minecraftkey) != null) {
					throw new IllegalStateException(String.format("Weird loottable '%s' for '%s', not a LivingEntity so should not have loot", minecraftkey, IRegistry.ENTITY_TYPE.getKey(entitytypes)));
				}
			} else if (minecraftkey != LootTables.a && set.add(minecraftkey)) {
				LootTable.a loottable_a = this.c.remove(minecraftkey);

				if (loottable_a == null) {
					throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", minecraftkey, IRegistry.ENTITY_TYPE.getKey(entitytypes)));
				}

				biconsumer.accept(minecraftkey, loottable_a);
			}
		}

		this.c.forEach(biconsumer::accept);
	}

	private void a(EntityTypes<?> entitytypes, LootTable.a loottable_a) {
		this.a(entitytypes.i(), loottable_a);
	}

	private void a(MinecraftKey minecraftkey, LootTable.a loottable_a) {
		this.c.put(minecraftkey, loottable_a);
	}
}
