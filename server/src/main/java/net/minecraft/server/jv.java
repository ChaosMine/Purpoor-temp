package net.minecraft.server;

import java.nio.file.Path;
import java.util.function.Function;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsBlock;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class jv extends jw<Item> {

    private final Function<Tag.e<Block>, Tag.a> d;

    public jv(DebugReportGenerator debugreportgenerator, js js) {
        super(debugreportgenerator, IRegistry.ITEM);
        this.d = js::b;
    }

    @Override
    protected void b() {
        this.a(TagsBlock.WOOL, TagsItem.WOOL);
        this.a(TagsBlock.PLANKS, TagsItem.PLANKS);
        this.a(TagsBlock.STONE_BRICKS, TagsItem.STONE_BRICKS);
        this.a(TagsBlock.WOODEN_BUTTONS, TagsItem.WOODEN_BUTTONS);
        this.a(TagsBlock.BUTTONS, TagsItem.BUTTONS);
        this.a(TagsBlock.CARPETS, TagsItem.CARPETS);
        this.a(TagsBlock.WOODEN_DOORS, TagsItem.WOODEN_DOORS);
        this.a(TagsBlock.WOODEN_STAIRS, TagsItem.WOODEN_STAIRS);
        this.a(TagsBlock.WOODEN_SLABS, TagsItem.WOODEN_SLABS);
        this.a(TagsBlock.WOODEN_FENCES, TagsItem.WOODEN_FENCES);
        this.a(TagsBlock.WOODEN_PRESSURE_PLATES, TagsItem.WOODEN_PRESSURE_PLATES);
        this.a(TagsBlock.DOORS, TagsItem.DOORS);
        this.a(TagsBlock.SAPLINGS, TagsItem.SAPLINGS);
        this.a(TagsBlock.OAK_LOGS, TagsItem.OAK_LOGS);
        this.a(TagsBlock.DARK_OAK_LOGS, TagsItem.DARK_OAK_LOGS);
        this.a(TagsBlock.BIRCH_LOGS, TagsItem.BIRCH_LOGS);
        this.a(TagsBlock.ACACIA_LOGS, TagsItem.ACACIA_LOGS);
        this.a(TagsBlock.SPRUCE_LOGS, TagsItem.SPRUCE_LOGS);
        this.a(TagsBlock.JUNGLE_LOGS, TagsItem.JUNGLE_LOGS);
        this.a(TagsBlock.CRIMSON_STEMS, TagsItem.CRIMSON_STEMS);
        this.a(TagsBlock.WARPED_STEMS, TagsItem.WARPED_STEMS);
        this.a(TagsBlock.LOGS_THAT_BURN, TagsItem.LOGS_THAT_BURN);
        this.a(TagsBlock.LOGS, TagsItem.LOGS);
        this.a(TagsBlock.SAND, TagsItem.SAND);
        this.a(TagsBlock.SLABS, TagsItem.SLABS);
        this.a(TagsBlock.WALLS, TagsItem.WALLS);
        this.a(TagsBlock.STAIRS, TagsItem.STAIRS);
        this.a(TagsBlock.ANVIL, TagsItem.ANVIL);
        this.a(TagsBlock.RAILS, TagsItem.RAILS);
        this.a(TagsBlock.LEAVES, TagsItem.LEAVES);
        this.a(TagsBlock.WOODEN_TRAPDOORS, TagsItem.WOODEN_TRAPDOORS);
        this.a(TagsBlock.TRAPDOORS, TagsItem.TRAPDOORS);
        this.a(TagsBlock.SMALL_FLOWERS, TagsItem.SMALL_FLOWERS);
        this.a(TagsBlock.BEDS, TagsItem.BEDS);
        this.a(TagsBlock.FENCES, TagsItem.FENCES);
        this.a(TagsBlock.TALL_FLOWERS, TagsItem.TALL_FLOWERS);
        this.a(TagsBlock.FLOWERS, TagsItem.FLOWERS);
        this.a(TagsBlock.GOLD_ORES, TagsItem.GOLD_ORES);
        this.a(TagsBlock.SOUL_FIRE_BASE_BLOCKS, TagsItem.SOUL_FIRE_BASE_BLOCKS);
        this.a(TagsItem.BANNERS).a((Object[])(Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER, Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER, Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER, Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER));
        this.a(TagsItem.BOATS).a((Object[])(Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT, Items.DARK_OAK_BOAT));
        this.a(TagsItem.FISHES).a((Object[])(Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH));
        this.a(TagsBlock.STANDING_SIGNS, TagsItem.SIGNS);
        this.a(TagsItem.CREEPER_DROP_MUSIC_DISCS).a((Object[])(Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT));
        this.a(TagsItem.MUSIC_DISCS).a(TagsItem.CREEPER_DROP_MUSIC_DISCS).a((Object)Items.MUSIC_DISC_PIGSTEP);
        this.a(TagsItem.COALS).a((Object[])(Items.COAL, Items.CHARCOAL));
        this.a(TagsItem.ARROWS).a((Object[])(Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW));
        this.a(TagsItem.LECTERN_BOOKS).a((Object[])(Items.WRITTEN_BOOK, Items.WRITABLE_BOOK));
        this.a(TagsItem.BEACON_PAYMENT_ITEMS).a((Object[])(Items.NETHERITE_INGOT, Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT));
        this.a(TagsItem.PIGLIN_REPELLENTS).a((Object)Items.dp).a((Object)Items.rl).a((Object)Items.ro);
        this.a(TagsItem.PIGLIN_LOVED).a(TagsItem.GOLD_ORES).a((Object[])(Items.bG, Items.rE, Items.fg, Items.GOLD_INGOT, Items.rj, Items.CLOCK, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE));
        this.a(TagsItem.NON_FLAMMABLE_WOOD).a((Object[])(Items.S, Items.aa, Items.aq, Items.ai, Items.R, Items.Z, Items.ap, Items.ah, Items.v, Items.w, Items.bO, Items.bP, Items.cP, Items.cQ, Items.dg, Items.dh, Items.dy, Items.dz, Items.dY, Items.dZ, Items.ex, Items.ey, Items.eZ, Items.fa, Items.jS, Items.jT, Items.CRIMSON_SIGN, Items.WARPED_SIGN));
        this.a(TagsItem.STONE_TOOL_MATERIALS).a((Object[])(Items.o, Items.rB));
        this.a(TagsItem.STONE_CRAFTING_MATERIALS).a((Object[])(Items.o, Items.rB));
    }

    protected void a(Tag.e<Block> tag_e, Tag.e<Item> tag_e1) {
        Tag.a tag_a = this.b(tag_e1);
        Tag.a tag_a1 = (Tag.a) this.d.apply(tag_e);

        tag_a1.b().forEach(tag_a::a);
    }

    @Override
    protected Path a(MinecraftKey minecraftkey) {
        return this.b.b().resolve("data/" + minecraftkey.getNamespace() + "/tags/items/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "Item Tags";
    }
}
