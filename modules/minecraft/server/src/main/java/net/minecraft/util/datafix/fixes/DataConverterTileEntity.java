package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;

import java.util.Map;

public class DataConverterTileEntity extends DataFix {
	private static final Map<String, String> a = ImmutableMap.<String, String>builder()
		.put("Airportal", "minecraft:end_portal")
		.put("Banner", "minecraft:banner")
		.put("Beacon", "minecraft:beacon")
		.put("Cauldron", "minecraft:brewing_stand")
		.put("Chest", "minecraft:chest")
		.put("Comparator", "minecraft:comparator")
		.put("Control", "minecraft:command_block")
		.put("DLDetector", "minecraft:daylight_detector")
		.put("Dropper", "minecraft:dropper")
		.put("EnchantTable", "minecraft:enchanting_table")
		.put("EndGateway", "minecraft:end_gateway")
		.put("EnderChest", "minecraft:ender_chest")
		.put("FlowerPot", "minecraft:flower_pot")
		.put("Furnace", "minecraft:furnace")
		.put("Hopper", "minecraft:hopper")
		.put("MobSpawner", "minecraft:mob_spawner")
		.put("Music", "minecraft:noteblock")
		.put("Piston", "minecraft:piston")
		.put("RecordPlayer", "minecraft:jukebox")
		.put("Sign", "minecraft:sign")
		.put("Skull", "minecraft:skull")
		.put("Structure", "minecraft:structure_block")
		.put("Trap", "minecraft:dispenser")
		.build();

	public DataConverterTileEntity(Schema schema, boolean flag) {
		super(schema, flag);
	}

	@SuppressWarnings("unchecked")
	public TypeRewriteRule makeRule() {
		Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
		Type<?> type1 = this.getOutputSchema().getType(DataConverterTypes.ITEM_STACK);
		var taggedchoicetype =  (TaggedChoiceType<String>) this.getInputSchema().findChoiceType(DataConverterTypes.BLOCK_ENTITY);
		var taggedchoicetype1 = (TaggedChoiceType<String>) this.getOutputSchema().findChoiceType(DataConverterTypes.BLOCK_ENTITY);

		return TypeRewriteRule.seq(this.convertUnchecked("item stack block entity name hook converter", type, type1), this.fixTypeEverywhere("BlockEntityIdFix", taggedchoicetype, taggedchoicetype1, (dynamicops) -> {
			return (pair) -> {
				return pair.mapFirst((s) -> {
					return DataConverterTileEntity.a.getOrDefault(s, s);
				});
			};
		}));
	}
}
