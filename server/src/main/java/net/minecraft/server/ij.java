package net.minecraft.server;

import com.google.gson.JsonElement;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ij {

    private final BiConsumer<MinecraftKey, Supplier<JsonElement>> a;

    public ij(BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
        this.a = biconsumer;
    }

    private void a(Item item, ix ix) {
        ix.a(iw.a(item), iz.b(item), this.a);
    }

    private void a(Item item, String s, ix ix) {
        ix.a(iw.a(item, s), iz.j(iz.a(item, s)), this.a);
    }

    private void a(Item item, Item item1, ix ix) {
        ix.a(iw.a(item), iz.b(item1), this.a);
    }

    public void a() {
        this.a(Items.ACACIA_BOAT, iy.aK);
        this.a(Items.APPLE, iy.aK);
        this.a(Items.ARMOR_STAND, iy.aK);
        this.a(Items.ARROW, iy.aK);
        this.a(Items.BAKED_POTATO, iy.aK);
        this.a(Items.bF, iy.aL);
        this.a(Items.BEEF, iy.aK);
        this.a(Items.BEETROOT, iy.aK);
        this.a(Items.BEETROOT_SOUP, iy.aK);
        this.a(Items.BIRCH_BOAT, iy.aK);
        this.a(Items.BLACK_DYE, iy.aK);
        this.a(Items.BLAZE_POWDER, iy.aK);
        this.a(Items.BLAZE_ROD, iy.aL);
        this.a(Items.BLUE_DYE, iy.aK);
        this.a(Items.BONE_MEAL, iy.aK);
        this.a(Items.BOOK, iy.aK);
        this.a(Items.BOWL, iy.aK);
        this.a(Items.BREAD, iy.aK);
        this.a(Items.BRICK, iy.aK);
        this.a(Items.BROWN_DYE, iy.aK);
        this.a(Items.BUCKET, iy.aK);
        this.a(Items.CARROT_ON_A_STICK, iy.aM);
        this.a(Items.WARPED_FUNGUS_ON_A_STICK, iy.aM);
        this.a(Items.CHAINMAIL_BOOTS, iy.aK);
        this.a(Items.CHAINMAIL_CHESTPLATE, iy.aK);
        this.a(Items.CHAINMAIL_HELMET, iy.aK);
        this.a(Items.CHAINMAIL_LEGGINGS, iy.aK);
        this.a(Items.CHARCOAL, iy.aK);
        this.a(Items.CHEST_MINECART, iy.aK);
        this.a(Items.CHICKEN, iy.aK);
        this.a(Items.CHORUS_FRUIT, iy.aK);
        this.a(Items.CLAY_BALL, iy.aK);

        int i;

        for (i = 1; i < 64; ++i) {
            this.a(Items.CLOCK, String.format("_%02d", i), iy.aK);
        }

        this.a(Items.COAL, iy.aK);
        this.a(Items.COD_BUCKET, iy.aK);
        this.a(Items.COMMAND_BLOCK_MINECART, iy.aK);

        for (i = 0; i < 32; ++i) {
            if (i != 16) {
                this.a(Items.COMPASS, String.format("_%02d", i), iy.aK);
            }
        }

        this.a(Items.COOKED_BEEF, iy.aK);
        this.a(Items.COOKED_CHICKEN, iy.aK);
        this.a(Items.COOKED_COD, iy.aK);
        this.a(Items.COOKED_MUTTON, iy.aK);
        this.a(Items.COOKED_PORKCHOP, iy.aK);
        this.a(Items.COOKED_RABBIT, iy.aK);
        this.a(Items.COOKED_SALMON, iy.aK);
        this.a(Items.COOKIE, iy.aK);
        this.a(Items.CREEPER_BANNER_PATTERN, iy.aK);
        this.a(Items.CYAN_DYE, iy.aK);
        this.a(Items.DARK_OAK_BOAT, iy.aK);
        this.a(Items.DIAMOND, iy.aK);
        this.a(Items.DIAMOND_AXE, iy.aL);
        this.a(Items.DIAMOND_BOOTS, iy.aK);
        this.a(Items.DIAMOND_CHESTPLATE, iy.aK);
        this.a(Items.DIAMOND_HELMET, iy.aK);
        this.a(Items.DIAMOND_HOE, iy.aL);
        this.a(Items.DIAMOND_HORSE_ARMOR, iy.aK);
        this.a(Items.DIAMOND_LEGGINGS, iy.aK);
        this.a(Items.DIAMOND_PICKAXE, iy.aL);
        this.a(Items.DIAMOND_SHOVEL, iy.aL);
        this.a(Items.DIAMOND_SWORD, iy.aL);
        this.a(Items.DRAGON_BREATH, iy.aK);
        this.a(Items.DRIED_KELP, iy.aK);
        this.a(Items.EGG, iy.aK);
        this.a(Items.EMERALD, iy.aK);
        this.a(Items.ENCHANTED_BOOK, iy.aK);
        this.a(Items.ENDER_EYE, iy.aK);
        this.a(Items.ENDER_PEARL, iy.aK);
        this.a(Items.END_CRYSTAL, iy.aK);
        this.a(Items.EXPERIENCE_BOTTLE, iy.aK);
        this.a(Items.FERMENTED_SPIDER_EYE, iy.aK);
        this.a(Items.FIREWORK_ROCKET, iy.aK);
        this.a(Items.FIRE_CHARGE, iy.aK);
        this.a(Items.FLINT, iy.aK);
        this.a(Items.FLINT_AND_STEEL, iy.aK);
        this.a(Items.FLOWER_BANNER_PATTERN, iy.aK);
        this.a(Items.FURNACE_MINECART, iy.aK);
        this.a(Items.GHAST_TEAR, iy.aK);
        this.a(Items.GLASS_BOTTLE, iy.aK);
        this.a(Items.GLISTERING_MELON_SLICE, iy.aK);
        this.a(Items.GLOBE_BANNER_PATTERN, iy.aK);
        this.a(Items.GLOWSTONE_DUST, iy.aK);
        this.a(Items.GOLDEN_APPLE, iy.aK);
        this.a(Items.GOLDEN_AXE, iy.aL);
        this.a(Items.GOLDEN_BOOTS, iy.aK);
        this.a(Items.GOLDEN_CARROT, iy.aK);
        this.a(Items.GOLDEN_CHESTPLATE, iy.aK);
        this.a(Items.GOLDEN_HELMET, iy.aK);
        this.a(Items.GOLDEN_HOE, iy.aL);
        this.a(Items.GOLDEN_HORSE_ARMOR, iy.aK);
        this.a(Items.GOLDEN_LEGGINGS, iy.aK);
        this.a(Items.GOLDEN_PICKAXE, iy.aL);
        this.a(Items.GOLDEN_SHOVEL, iy.aL);
        this.a(Items.GOLDEN_SWORD, iy.aL);
        this.a(Items.GOLD_INGOT, iy.aK);
        this.a(Items.GOLD_NUGGET, iy.aK);
        this.a(Items.GRAY_DYE, iy.aK);
        this.a(Items.GREEN_DYE, iy.aK);
        this.a(Items.GUNPOWDER, iy.aK);
        this.a(Items.HEART_OF_THE_SEA, iy.aK);
        this.a(Items.HONEYCOMB, iy.aK);
        this.a(Items.HONEY_BOTTLE, iy.aK);
        this.a(Items.HOPPER_MINECART, iy.aK);
        this.a(Items.INK_SAC, iy.aK);
        this.a(Items.IRON_AXE, iy.aL);
        this.a(Items.IRON_BOOTS, iy.aK);
        this.a(Items.IRON_CHESTPLATE, iy.aK);
        this.a(Items.IRON_HELMET, iy.aK);
        this.a(Items.IRON_HOE, iy.aL);
        this.a(Items.IRON_HORSE_ARMOR, iy.aK);
        this.a(Items.IRON_INGOT, iy.aK);
        this.a(Items.IRON_LEGGINGS, iy.aK);
        this.a(Items.IRON_NUGGET, iy.aK);
        this.a(Items.IRON_PICKAXE, iy.aL);
        this.a(Items.IRON_SHOVEL, iy.aL);
        this.a(Items.IRON_SWORD, iy.aL);
        this.a(Items.ITEM_FRAME, iy.aK);
        this.a(Items.JUNGLE_BOAT, iy.aK);
        this.a(Items.KNOWLEDGE_BOOK, iy.aK);
        this.a(Items.LAPIS_LAZULI, iy.aK);
        this.a(Items.LAVA_BUCKET, iy.aK);
        this.a(Items.LEATHER, iy.aK);
        this.a(Items.LEATHER_HORSE_ARMOR, iy.aK);
        this.a(Items.LIGHT_BLUE_DYE, iy.aK);
        this.a(Items.LIGHT_GRAY_DYE, iy.aK);
        this.a(Items.LIME_DYE, iy.aK);
        this.a(Items.MAGENTA_DYE, iy.aK);
        this.a(Items.MAGMA_CREAM, iy.aK);
        this.a(Items.MAP, iy.aK);
        this.a(Items.MELON_SLICE, iy.aK);
        this.a(Items.MILK_BUCKET, iy.aK);
        this.a(Items.MINECART, iy.aK);
        this.a(Items.MOJANG_BANNER_PATTERN, iy.aK);
        this.a(Items.MUSHROOM_STEW, iy.aK);
        this.a(Items.MUSIC_DISC_11, iy.aK);
        this.a(Items.MUSIC_DISC_13, iy.aK);
        this.a(Items.MUSIC_DISC_BLOCKS, iy.aK);
        this.a(Items.MUSIC_DISC_CAT, iy.aK);
        this.a(Items.MUSIC_DISC_CHIRP, iy.aK);
        this.a(Items.MUSIC_DISC_FAR, iy.aK);
        this.a(Items.MUSIC_DISC_MALL, iy.aK);
        this.a(Items.MUSIC_DISC_MELLOHI, iy.aK);
        this.a(Items.MUSIC_DISC_PIGSTEP, iy.aK);
        this.a(Items.MUSIC_DISC_STAL, iy.aK);
        this.a(Items.MUSIC_DISC_STRAD, iy.aK);
        this.a(Items.MUSIC_DISC_WAIT, iy.aK);
        this.a(Items.MUSIC_DISC_WARD, iy.aK);
        this.a(Items.MUTTON, iy.aK);
        this.a(Items.NAME_TAG, iy.aK);
        this.a(Items.NAUTILUS_SHELL, iy.aK);
        this.a(Items.NETHERITE_AXE, iy.aL);
        this.a(Items.NETHERITE_BOOTS, iy.aK);
        this.a(Items.NETHERITE_CHESTPLATE, iy.aK);
        this.a(Items.NETHERITE_HELMET, iy.aK);
        this.a(Items.NETHERITE_HOE, iy.aL);
        this.a(Items.NETHERITE_INGOT, iy.aK);
        this.a(Items.NETHERITE_LEGGINGS, iy.aK);
        this.a(Items.NETHERITE_PICKAXE, iy.aL);
        this.a(Items.NETHERITE_SCRAP, iy.aK);
        this.a(Items.NETHERITE_SHOVEL, iy.aL);
        this.a(Items.NETHERITE_SWORD, iy.aL);
        this.a(Items.NETHER_BRICK, iy.aK);
        this.a(Items.NETHER_STAR, iy.aK);
        this.a(Items.OAK_BOAT, iy.aK);
        this.a(Items.ORANGE_DYE, iy.aK);
        this.a(Items.PAINTING, iy.aK);
        this.a(Items.PAPER, iy.aK);
        this.a(Items.PHANTOM_MEMBRANE, iy.aK);
        this.a(Items.PIGLIN_BANNER_PATTERN, iy.aK);
        this.a(Items.PINK_DYE, iy.aK);
        this.a(Items.POISONOUS_POTATO, iy.aK);
        this.a(Items.POPPED_CHORUS_FRUIT, iy.aK);
        this.a(Items.PORKCHOP, iy.aK);
        this.a(Items.PRISMARINE_CRYSTALS, iy.aK);
        this.a(Items.PRISMARINE_SHARD, iy.aK);
        this.a(Items.PUFFERFISH, iy.aK);
        this.a(Items.PUFFERFISH_BUCKET, iy.aK);
        this.a(Items.PUMPKIN_PIE, iy.aK);
        this.a(Items.PURPLE_DYE, iy.aK);
        this.a(Items.QUARTZ, iy.aK);
        this.a(Items.RABBIT, iy.aK);
        this.a(Items.RABBIT_FOOT, iy.aK);
        this.a(Items.RABBIT_HIDE, iy.aK);
        this.a(Items.RABBIT_STEW, iy.aK);
        this.a(Items.RED_DYE, iy.aK);
        this.a(Items.ROTTEN_FLESH, iy.aK);
        this.a(Items.SADDLE, iy.aK);
        this.a(Items.SALMON, iy.aK);
        this.a(Items.SALMON_BUCKET, iy.aK);
        this.a(Items.SCUTE, iy.aK);
        this.a(Items.SHEARS, iy.aK);
        this.a(Items.SHULKER_SHELL, iy.aK);
        this.a(Items.SKULL_BANNER_PATTERN, iy.aK);
        this.a(Items.SLIME_BALL, iy.aK);
        this.a(Items.SNOWBALL, iy.aK);
        this.a(Items.SPECTRAL_ARROW, iy.aK);
        this.a(Items.SPIDER_EYE, iy.aK);
        this.a(Items.SPRUCE_BOAT, iy.aK);
        this.a(Items.STICK, iy.aL);
        this.a(Items.STONE_AXE, iy.aL);
        this.a(Items.STONE_HOE, iy.aL);
        this.a(Items.STONE_PICKAXE, iy.aL);
        this.a(Items.STONE_SHOVEL, iy.aL);
        this.a(Items.STONE_SWORD, iy.aL);
        this.a(Items.SUGAR, iy.aK);
        this.a(Items.SUSPICIOUS_STEW, iy.aK);
        this.a(Items.TNT_MINECART, iy.aK);
        this.a(Items.TOTEM_OF_UNDYING, iy.aK);
        this.a(Items.TRIDENT, iy.aK);
        this.a(Items.TROPICAL_FISH, iy.aK);
        this.a(Items.TROPICAL_FISH_BUCKET, iy.aK);
        this.a(Items.TURTLE_HELMET, iy.aK);
        this.a(Items.WATER_BUCKET, iy.aK);
        this.a(Items.WHEAT, iy.aK);
        this.a(Items.WHITE_DYE, iy.aK);
        this.a(Items.WOODEN_AXE, iy.aL);
        this.a(Items.WOODEN_HOE, iy.aL);
        this.a(Items.WOODEN_PICKAXE, iy.aL);
        this.a(Items.WOODEN_SHOVEL, iy.aL);
        this.a(Items.WOODEN_SWORD, iy.aL);
        this.a(Items.WRITABLE_BOOK, iy.aK);
        this.a(Items.WRITTEN_BOOK, iy.aK);
        this.a(Items.YELLOW_DYE, iy.aK);
        this.a(Items.DEBUG_STICK, Items.STICK, iy.aL);
        this.a(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, iy.aK);
    }
}
