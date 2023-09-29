package net.minecraft.util.datafix.schemas;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.Hook.HookFunction;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class DataConverterSchemaV99 extends Schema {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<String, String> c = DataFixUtils.make(new HashMap<>(), map -> {
		map.put("minecraft:furnace", "Furnace");
		map.put("minecraft:lit_furnace", "Furnace");
		map.put("minecraft:chest", "Chest");
		map.put("minecraft:trapped_chest", "Chest");
		map.put("minecraft:ender_chest", "EnderChest");
		map.put("minecraft:jukebox", "RecordPlayer");
		map.put("minecraft:dispenser", "Trap");
		map.put("minecraft:dropper", "Dropper");
		map.put("minecraft:sign", "Sign");
		map.put("minecraft:mob_spawner", "MobSpawner");
		map.put("minecraft:noteblock", "Music");
		map.put("minecraft:brewing_stand", "Cauldron");
		map.put("minecraft:enhanting_table", "EnchantTable");
		map.put("minecraft:command_block", "CommandBlock");
		map.put("minecraft:beacon", "Beacon");
		map.put("minecraft:skull", "Skull");
		map.put("minecraft:daylight_detector", "DLDetector");
		map.put("minecraft:hopper", "Hopper");
		map.put("minecraft:banner", "Banner");
		map.put("minecraft:flower_pot", "FlowerPot");
		map.put("minecraft:repeating_command_block", "CommandBlock");
		map.put("minecraft:chain_command_block", "CommandBlock");
		map.put("minecraft:standing_sign", "Sign");
		map.put("minecraft:wall_sign", "Sign");
		map.put("minecraft:piston_head", "Piston");
		map.put("minecraft:daylight_detector_inverted", "DLDetector");
		map.put("minecraft:unpowered_comparator", "Comparator");
		map.put("minecraft:powered_comparator", "Comparator");
		map.put("minecraft:wall_banner", "Banner");
		map.put("minecraft:standing_banner", "Banner");
		map.put("minecraft:structure_block", "Structure");
		map.put("minecraft:end_portal", "Airportal");
		map.put("minecraft:end_gateway", "EndGateway");
		map.put("minecraft:shield", "Banner");
	});
	protected static final HookFunction a = new HookFunction() {
		public <T> T apply(DynamicOps<T> dynamicops, T t0) {
			return DataConverterSchemaV99.a(new Dynamic<>(dynamicops, t0), DataConverterSchemaV99.c, "ArmorStand");
		}
	};

	public DataConverterSchemaV99(int i, Schema schema) {
		super(i, schema);
	}

	protected static TypeTemplate a(Schema schema) {
		return DSL.optionalFields("Equipment", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
	}

	protected static void a(Schema schema, Map<String, Supplier<TypeTemplate>> map, String s) {
		schema.register(map, s, () -> {
			return a(schema);
		});
	}

	protected static void b(Schema schema, Map<String, Supplier<TypeTemplate>> map, String s) {
		schema.register(map, s, () -> {
			return DSL.optionalFields("inTile", DataConverterTypes.BLOCK_NAME.in(schema));
		});
	}

	protected static void c(Schema schema, Map<String, Supplier<TypeTemplate>> map, String s) {
		schema.register(map, s, () -> {
			return DSL.optionalFields("DisplayTile", DataConverterTypes.BLOCK_NAME.in(schema));
		});
	}

	protected static void d(Schema schema, Map<String, Supplier<TypeTemplate>> map, String s) {
		schema.register(map, s, () -> {
			return DSL.optionalFields("Items", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
		});
	}

	protected static <T> T a(Dynamic<T> dynamic, Map<String, String> map, String s) {
		return dynamic.update("tag", (dynamic1) -> {
			return dynamic1.update("BlockEntityTag", (dynamic2) -> {
				String s1 = dynamic.get("id").asString("");
				String s2 = map.get(DataConverterSchemaNamed.a(s1));

				if (s2 == null) {
					DataConverterSchemaV99.LOGGER.warn("Unable to resolve BlockEntity for ItemStack: {}", s1);
					return dynamic2;
				} else {
					return dynamic2.set("id", dynamic.createString(s2));
				}
			}).update("EntityTag", (dynamic2) -> {
				String s1 = dynamic.get("id").asString("");

				return Objects.equals(DataConverterSchemaNamed.a(s1), "minecraft:armor_stand") ? dynamic2.set("id", dynamic.createString(s)) : dynamic2;
			});
		}).getValue();
	}

	public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
		Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();

		schema.register(map, "Item", (s) -> {
			return DSL.optionalFields("Item", DataConverterTypes.ITEM_STACK.in(schema));
		});
		schema.registerSimple(map, "XPOrb");
		b(schema, map, "ThrownEgg");
		schema.registerSimple(map, "LeashKnot");
		schema.registerSimple(map, "Painting");
		schema.register(map, "Arrow", (s) -> {
			return DSL.optionalFields("inTile", DataConverterTypes.BLOCK_NAME.in(schema));
		});
		schema.register(map, "TippedArrow", (s) -> {
			return DSL.optionalFields("inTile", DataConverterTypes.BLOCK_NAME.in(schema));
		});
		schema.register(map, "SpectralArrow", (s) -> {
			return DSL.optionalFields("inTile", DataConverterTypes.BLOCK_NAME.in(schema));
		});
		b(schema, map, "Snowball");
		b(schema, map, "Fireball");
		b(schema, map, "SmallFireball");
		b(schema, map, "ThrownEnderpearl");
		schema.registerSimple(map, "EyeOfEnderSignal");
		schema.register(map, "ThrownPotion", (s) -> {
			return DSL.optionalFields("inTile", DataConverterTypes.BLOCK_NAME.in(schema), "Potion", DataConverterTypes.ITEM_STACK.in(schema));
		});
		b(schema, map, "ThrownExpBottle");
		schema.register(map, "ItemFrame", (s) -> {
			return DSL.optionalFields("Item", DataConverterTypes.ITEM_STACK.in(schema));
		});
		b(schema, map, "WitherSkull");
		schema.registerSimple(map, "PrimedTnt");
		schema.register(map, "FallingSand", (s) -> {
			return DSL.optionalFields("Block", DataConverterTypes.BLOCK_NAME.in(schema), "TileEntityData", DataConverterTypes.BLOCK_ENTITY.in(schema));
		});
		schema.register(map, "FireworksRocketEntity", (s) -> {
			return DSL.optionalFields("FireworksItem", DataConverterTypes.ITEM_STACK.in(schema));
		});
		schema.registerSimple(map, "Boat");
		schema.register(map, "Minecart", () -> {
			return DSL.optionalFields("DisplayTile", DataConverterTypes.BLOCK_NAME.in(schema), "Items", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
		});
		c(schema, map, "MinecartRideable");
		schema.register(map, "MinecartChest", (s) -> {
			return DSL.optionalFields("DisplayTile", DataConverterTypes.BLOCK_NAME.in(schema), "Items", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
		});
		c(schema, map, "MinecartFurnace");
		c(schema, map, "MinecartTNT");
		schema.register(map, "MinecartSpawner", () -> {
			return DSL.optionalFields("DisplayTile", DataConverterTypes.BLOCK_NAME.in(schema), DataConverterTypes.UNTAGGED_SPAWNER.in(schema));
		});
		schema.register(map, "MinecartHopper", (s) -> {
			return DSL.optionalFields("DisplayTile", DataConverterTypes.BLOCK_NAME.in(schema), "Items", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
		});
		c(schema, map, "MinecartCommandBlock");
		a(schema, map, "ArmorStand");
		a(schema, map, "Creeper");
		a(schema, map, "Skeleton");
		a(schema, map, "Spider");
		a(schema, map, "Giant");
		a(schema, map, "Zombie");
		a(schema, map, "Slime");
		a(schema, map, "Ghast");
		a(schema, map, "PigZombie");
		schema.register(map, "Enderman", (s) -> {
			return DSL.optionalFields("carried", DataConverterTypes.BLOCK_NAME.in(schema), a(schema));
		});
		a(schema, map, "CaveSpider");
		a(schema, map, "Silverfish");
		a(schema, map, "Blaze");
		a(schema, map, "LavaSlime");
		a(schema, map, "EnderDragon");
		a(schema, map, "WitherBoss");
		a(schema, map, "Bat");
		a(schema, map, "Witch");
		a(schema, map, "Endermite");
		a(schema, map, "Guardian");
		a(schema, map, "Pig");
		a(schema, map, "Sheep");
		a(schema, map, "Cow");
		a(schema, map, "Chicken");
		a(schema, map, "Squid");
		a(schema, map, "Wolf");
		a(schema, map, "MushroomCow");
		a(schema, map, "SnowMan");
		a(schema, map, "Ozelot");
		a(schema, map, "VillagerGolem");
		schema.register(map, "EntityHorse", (s) -> {
			return DSL.optionalFields("Items", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)), "ArmorItem", DataConverterTypes.ITEM_STACK.in(schema), "SaddleItem", DataConverterTypes.ITEM_STACK.in(schema), a(schema));
		});
		a(schema, map, "Rabbit");
		schema.register(map, "Villager", (s) -> {
			return DSL.optionalFields("Inventory", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", DataConverterTypes.ITEM_STACK.in(schema), "buyB", DataConverterTypes.ITEM_STACK.in(schema), "sell", DataConverterTypes.ITEM_STACK.in(schema)))), a(schema));
		});
		schema.registerSimple(map, "EnderCrystal");
		schema.registerSimple(map, "AreaEffectCloud");
		schema.registerSimple(map, "ShulkerBullet");
		a(schema, map, "Shulker");
		return map;
	}

	public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
		Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();

		d(schema, map, "Furnace");
		d(schema, map, "Chest");
		schema.registerSimple(map, "EnderChest");
		schema.register(map, "RecordPlayer", (s) -> {
			return DSL.optionalFields("RecordItem", DataConverterTypes.ITEM_STACK.in(schema));
		});
		d(schema, map, "Trap");
		d(schema, map, "Dropper");
		schema.registerSimple(map, "Sign");
		schema.register(map, "MobSpawner", (s) -> {
			return DataConverterTypes.UNTAGGED_SPAWNER.in(schema);
		});
		schema.registerSimple(map, "Music");
		schema.registerSimple(map, "Piston");
		d(schema, map, "Cauldron");
		schema.registerSimple(map, "EnchantTable");
		schema.registerSimple(map, "Airportal");
		schema.registerSimple(map, "Control");
		schema.registerSimple(map, "Beacon");
		schema.registerSimple(map, "Skull");
		schema.registerSimple(map, "DLDetector");
		d(schema, map, "Hopper");
		schema.registerSimple(map, "Comparator");
		schema.register(map, "FlowerPot", (s) -> {
			return DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), DataConverterTypes.ITEM_NAME.in(schema)));
		});
		schema.registerSimple(map, "Banner");
		schema.registerSimple(map, "Structure");
		schema.registerSimple(map, "EndGateway");
		return map;
	}

	public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map1) {
		schema.registerType(false, DataConverterTypes.LEVEL, DSL::remainder);
		schema.registerType(false, DataConverterTypes.PLAYER, () -> {
			return DSL.optionalFields("Inventory", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)), "EnderItems", DSL.list(DataConverterTypes.ITEM_STACK.in(schema)));
		});
		schema.registerType(false, DataConverterTypes.CHUNK, () -> {
			return DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(DataConverterTypes.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DataConverterTypes.BLOCK_ENTITY.in(schema)), "TileTicks", DSL.list(DSL.fields("i", DataConverterTypes.BLOCK_NAME.in(schema)))));
		});
		schema.registerType(true, DataConverterTypes.BLOCK_ENTITY, () -> {
			return DSL.taggedChoiceLazy("id", DSL.string(), map1);
		});
		schema.registerType(true, DataConverterTypes.ENTITY_TREE, () -> {
			return DSL.optionalFields("Riding", DataConverterTypes.ENTITY_TREE.in(schema), DataConverterTypes.ENTITY.in(schema));
		});
		schema.registerType(false, DataConverterTypes.ENTITY_NAME, () -> {
			return DSL.constType(DataConverterSchemaNamed.a());
		});
		schema.registerType(true, DataConverterTypes.ENTITY, () -> {
			return DSL.taggedChoiceLazy("id", DSL.string(), map);
		});
		schema.registerType(true, DataConverterTypes.ITEM_STACK, () -> {
			return DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), DataConverterTypes.ITEM_NAME.in(schema)), "tag", DSL.optionalFields("EntityTag", DataConverterTypes.ENTITY_TREE.in(schema), "BlockEntityTag", DataConverterTypes.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(DataConverterTypes.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(DataConverterTypes.BLOCK_NAME.in(schema)))), DataConverterSchemaV99.a, HookFunction.IDENTITY);
		});
		schema.registerType(false, DataConverterTypes.OPTIONS, DSL::remainder);
		schema.registerType(false, DataConverterTypes.BLOCK_NAME, () -> {
			return DSL.or(DSL.constType(DSL.intType()), DSL.constType(DataConverterSchemaNamed.a()));
		});
		schema.registerType(false, DataConverterTypes.ITEM_NAME, () -> {
			return DSL.constType(DataConverterSchemaNamed.a());
		});
		schema.registerType(false, DataConverterTypes.STATS, DSL::remainder);
		schema.registerType(false, DataConverterTypes.SAVED_DATA, () -> {
			return DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(DataConverterTypes.STRUCTURE_FEATURE.in(schema)), "Objectives", DSL.list(DataConverterTypes.OBJECTIVE.in(schema)), "Teams", DSL.list(DataConverterTypes.TEAM.in(schema))));
		});
		schema.registerType(false, DataConverterTypes.STRUCTURE_FEATURE, DSL::remainder);
		schema.registerType(false, DataConverterTypes.OBJECTIVE, DSL::remainder);
		schema.registerType(false, DataConverterTypes.TEAM, DSL::remainder);
		schema.registerType(true, DataConverterTypes.UNTAGGED_SPAWNER, DSL::remainder);
		schema.registerType(false, DataConverterTypes.POI_CHUNK, DSL::remainder);
		schema.registerType(true, DataConverterTypes.WORLD_GEN_SETTINGS, DSL::remainder);
	}
}
