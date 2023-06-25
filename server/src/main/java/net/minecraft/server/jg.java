package net.minecraft.server;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionConditionEntity;
import net.minecraft.advancements.critereon.CriterionConditionItem;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.advancements.critereon.CriterionTriggerEnterBlock;
import net.minecraft.advancements.critereon.CriterionTriggerImpossible;
import net.minecraft.advancements.critereon.CriterionTriggerInventoryChanged;
import net.minecraft.advancements.critereon.CriterionTriggerProperties;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializerCooking;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class jg implements DebugReportProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson c = (new GsonBuilder()).setPrettyPrinting().create();
    private final DebugReportGenerator d;

    public jg(DebugReportGenerator debugreportgenerator) {
        this.d = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) throws IOException {
        Path path = this.d.b();
        Set<MinecraftKey> set = Sets.newHashSet();

        a((jf) -> {
            if (!set.add(jf.b())) {
                throw new IllegalStateException("Duplicate recipe " + jf.b());
            } else {
                a(hashcache, jf.a(), path.resolve("data/" + jf.b().getNamespace() + "/recipes/" + jf.b().getKey() + ".json"));
                JsonObject jsonobject = jf.d();

                if (jsonobject != null) {
                    b(hashcache, jsonobject, path.resolve("data/" + jf.b().getNamespace() + "/advancements/" + jf.e().getKey() + ".json"));
                }

            }
        });
        b(hashcache, Advancement.SerializedAdvancement.a().a("impossible", (CriterionInstance) (new CriterionTriggerImpossible.a())).b(), path.resolve("data/minecraft/advancements/recipes/root.json"));
    }

    private static void a(HashCache hashcache, JsonObject jsonobject, Path path) {
        try {
            String s = jg.c.toJson(jsonobject);
            String s1 = jg.a.hashUnencodedChars(s).toString();

            if (!Objects.equals(hashcache.a(path), s1) || !Files.exists(path, new LinkOption[0])) {
                Files.createDirectories(path.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(path);
                Throwable throwable = null;

                try {
                    bufferedwriter.write(s);
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (bufferedwriter != null) {
                        if (throwable != null) {
                            try {
                                bufferedwriter.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            bufferedwriter.close();
                        }
                    }

                }
            }

            hashcache.a(path, s1);
        } catch (IOException ioexception) {
            jg.LOGGER.error("Couldn't save recipe {}", path, ioexception);
        }

    }

    private static void b(HashCache hashcache, JsonObject jsonobject, Path path) {
        try {
            String s = jg.c.toJson(jsonobject);
            String s1 = jg.a.hashUnencodedChars(s).toString();

            if (!Objects.equals(hashcache.a(path), s1) || !Files.exists(path, new LinkOption[0])) {
                Files.createDirectories(path.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(path);
                Throwable throwable = null;

                try {
                    bufferedwriter.write(s);
                } catch (Throwable throwable1) {
                    throwable = throwable1;
                    throw throwable1;
                } finally {
                    if (bufferedwriter != null) {
                        if (throwable != null) {
                            try {
                                bufferedwriter.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        } else {
                            bufferedwriter.close();
                        }
                    }

                }
            }

            hashcache.a(path, s1);
        } catch (IOException ioexception) {
            jg.LOGGER.error("Couldn't save recipe advancement {}", path, ioexception);
        }

    }

    private static void a(Consumer<jf> consumer) {
        a(consumer, (IMaterial) Blocks.ACACIA_PLANKS, (Tag) TagsItem.ACACIA_LOGS);
        b(consumer, (IMaterial) Blocks.BIRCH_PLANKS, (Tag) TagsItem.BIRCH_LOGS);
        b(consumer, (IMaterial) Blocks.CRIMSON_PLANKS, (Tag) TagsItem.CRIMSON_STEMS);
        a(consumer, (IMaterial) Blocks.DARK_OAK_PLANKS, (Tag) TagsItem.DARK_OAK_LOGS);
        b(consumer, (IMaterial) Blocks.JUNGLE_PLANKS, (Tag) TagsItem.JUNGLE_LOGS);
        b(consumer, (IMaterial) Blocks.OAK_PLANKS, (Tag) TagsItem.OAK_LOGS);
        b(consumer, (IMaterial) Blocks.SPRUCE_PLANKS, (Tag) TagsItem.SPRUCE_LOGS);
        b(consumer, (IMaterial) Blocks.WARPED_PLANKS, (Tag) TagsItem.WARPED_STEMS);
        a(consumer, (IMaterial) Blocks.ACACIA_WOOD, (IMaterial) Blocks.ACACIA_LOG);
        a(consumer, (IMaterial) Blocks.BIRCH_WOOD, (IMaterial) Blocks.BIRCH_LOG);
        a(consumer, (IMaterial) Blocks.DARK_OAK_WOOD, (IMaterial) Blocks.DARK_OAK_LOG);
        a(consumer, (IMaterial) Blocks.JUNGLE_WOOD, (IMaterial) Blocks.JUNGLE_LOG);
        a(consumer, (IMaterial) Blocks.OAK_WOOD, (IMaterial) Blocks.OAK_LOG);
        a(consumer, (IMaterial) Blocks.SPRUCE_WOOD, (IMaterial) Blocks.SPRUCE_LOG);
        a(consumer, (IMaterial) Blocks.CRIMSON_HYPHAE, (IMaterial) Blocks.CRIMSON_STEM);
        a(consumer, (IMaterial) Blocks.WARPED_HYPHAE, (IMaterial) Blocks.WARPED_STEM);
        a(consumer, (IMaterial) Blocks.STRIPPED_ACACIA_WOOD, (IMaterial) Blocks.STRIPPED_ACACIA_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_BIRCH_WOOD, (IMaterial) Blocks.STRIPPED_BIRCH_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_DARK_OAK_WOOD, (IMaterial) Blocks.STRIPPED_DARK_OAK_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_JUNGLE_WOOD, (IMaterial) Blocks.STRIPPED_JUNGLE_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_OAK_WOOD, (IMaterial) Blocks.STRIPPED_OAK_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_SPRUCE_WOOD, (IMaterial) Blocks.STRIPPED_SPRUCE_LOG);
        a(consumer, (IMaterial) Blocks.STRIPPED_CRIMSON_HYPHAE, (IMaterial) Blocks.STRIPPED_CRIMSON_STEM);
        a(consumer, (IMaterial) Blocks.STRIPPED_WARPED_HYPHAE, (IMaterial) Blocks.STRIPPED_WARPED_STEM);
        b(consumer, (IMaterial) Items.ACACIA_BOAT, (IMaterial) Blocks.ACACIA_PLANKS);
        b(consumer, (IMaterial) Items.BIRCH_BOAT, (IMaterial) Blocks.BIRCH_PLANKS);
        b(consumer, (IMaterial) Items.DARK_OAK_BOAT, (IMaterial) Blocks.DARK_OAK_PLANKS);
        b(consumer, (IMaterial) Items.JUNGLE_BOAT, (IMaterial) Blocks.JUNGLE_PLANKS);
        b(consumer, (IMaterial) Items.OAK_BOAT, (IMaterial) Blocks.OAK_PLANKS);
        b(consumer, (IMaterial) Items.SPRUCE_BOAT, (IMaterial) Blocks.SPRUCE_PLANKS);
        c(consumer, Blocks.ACACIA_BUTTON, Blocks.ACACIA_PLANKS);
        d(consumer, Blocks.ACACIA_DOOR, Blocks.ACACIA_PLANKS);
        e(consumer, Blocks.ACACIA_FENCE, Blocks.ACACIA_PLANKS);
        f(consumer, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PLANKS);
        g(consumer, Blocks.ACACIA_PRESSURE_PLATE, Blocks.ACACIA_PLANKS);
        h(consumer, Blocks.ACACIA_SLAB, Blocks.ACACIA_PLANKS);
        i(consumer, Blocks.ACACIA_STAIRS, Blocks.ACACIA_PLANKS);
        j(consumer, Blocks.ACACIA_TRAPDOOR, Blocks.ACACIA_PLANKS);
        k(consumer, Blocks.ACACIA_SIGN, Blocks.ACACIA_PLANKS);
        c(consumer, Blocks.BIRCH_BUTTON, Blocks.BIRCH_PLANKS);
        d(consumer, Blocks.BIRCH_DOOR, Blocks.BIRCH_PLANKS);
        e(consumer, Blocks.BIRCH_FENCE, Blocks.BIRCH_PLANKS);
        f(consumer, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PLANKS);
        g(consumer, Blocks.BIRCH_PRESSURE_PLATE, Blocks.BIRCH_PLANKS);
        h(consumer, Blocks.BIRCH_SLAB, Blocks.BIRCH_PLANKS);
        i(consumer, Blocks.BIRCH_STAIRS, Blocks.BIRCH_PLANKS);
        j(consumer, Blocks.BIRCH_TRAPDOOR, Blocks.BIRCH_PLANKS);
        k(consumer, Blocks.BIRCH_SIGN, Blocks.BIRCH_PLANKS);
        c(consumer, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_PLANKS);
        d(consumer, Blocks.CRIMSON_DOOR, Blocks.CRIMSON_PLANKS);
        e(consumer, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_PLANKS);
        f(consumer, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_PLANKS);
        g(consumer, Blocks.CRIMSON_PRESSURE_PLATE, Blocks.CRIMSON_PLANKS);
        h(consumer, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_PLANKS);
        i(consumer, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_PLANKS);
        j(consumer, Blocks.CRIMSON_TRAPDOOR, Blocks.CRIMSON_PLANKS);
        k(consumer, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_PLANKS);
        c(consumer, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_PLANKS);
        d(consumer, Blocks.DARK_OAK_DOOR, Blocks.DARK_OAK_PLANKS);
        e(consumer, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_PLANKS);
        f(consumer, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PLANKS);
        g(consumer, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.DARK_OAK_PLANKS);
        h(consumer, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_PLANKS);
        i(consumer, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_PLANKS);
        j(consumer, Blocks.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_PLANKS);
        k(consumer, Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_PLANKS);
        c(consumer, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_PLANKS);
        d(consumer, Blocks.JUNGLE_DOOR, Blocks.JUNGLE_PLANKS);
        e(consumer, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_PLANKS);
        f(consumer, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PLANKS);
        g(consumer, Blocks.JUNGLE_PRESSURE_PLATE, Blocks.JUNGLE_PLANKS);
        h(consumer, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_PLANKS);
        i(consumer, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_PLANKS);
        j(consumer, Blocks.JUNGLE_TRAPDOOR, Blocks.JUNGLE_PLANKS);
        k(consumer, Blocks.JUNGLE_SIGN, Blocks.JUNGLE_PLANKS);
        c(consumer, Blocks.OAK_BUTTON, Blocks.OAK_PLANKS);
        d(consumer, Blocks.OAK_DOOR, Blocks.OAK_PLANKS);
        e(consumer, Blocks.OAK_FENCE, Blocks.OAK_PLANKS);
        f(consumer, Blocks.OAK_FENCE_GATE, Blocks.OAK_PLANKS);
        g(consumer, Blocks.OAK_PRESSURE_PLATE, Blocks.OAK_PLANKS);
        h(consumer, Blocks.OAK_SLAB, Blocks.OAK_PLANKS);
        i(consumer, Blocks.OAK_STAIRS, Blocks.OAK_PLANKS);
        j(consumer, Blocks.OAK_TRAPDOOR, Blocks.OAK_PLANKS);
        k(consumer, Blocks.OAK_SIGN, Blocks.OAK_PLANKS);
        c(consumer, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_PLANKS);
        d(consumer, Blocks.SPRUCE_DOOR, Blocks.SPRUCE_PLANKS);
        e(consumer, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_PLANKS);
        f(consumer, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PLANKS);
        g(consumer, Blocks.SPRUCE_PRESSURE_PLATE, Blocks.SPRUCE_PLANKS);
        h(consumer, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_PLANKS);
        i(consumer, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_PLANKS);
        j(consumer, Blocks.SPRUCE_TRAPDOOR, Blocks.SPRUCE_PLANKS);
        k(consumer, Blocks.SPRUCE_SIGN, Blocks.SPRUCE_PLANKS);
        c(consumer, Blocks.WARPED_BUTTON, Blocks.WARPED_PLANKS);
        d(consumer, Blocks.WARPED_DOOR, Blocks.WARPED_PLANKS);
        e(consumer, Blocks.WARPED_FENCE, Blocks.WARPED_PLANKS);
        f(consumer, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_PLANKS);
        g(consumer, Blocks.WARPED_PRESSURE_PLATE, Blocks.WARPED_PLANKS);
        h(consumer, Blocks.WARPED_SLAB, Blocks.WARPED_PLANKS);
        i(consumer, Blocks.WARPED_STAIRS, Blocks.WARPED_PLANKS);
        j(consumer, Blocks.WARPED_TRAPDOOR, Blocks.WARPED_PLANKS);
        k(consumer, Blocks.WARPED_SIGN, Blocks.WARPED_PLANKS);
        l(consumer, Blocks.BLACK_WOOL, Items.BLACK_DYE);
        m(consumer, Blocks.BLACK_CARPET, Blocks.BLACK_WOOL);
        n(consumer, Blocks.BLACK_CARPET, Items.BLACK_DYE);
        o(consumer, Items.BLACK_BED, Blocks.BLACK_WOOL);
        p(consumer, Items.BLACK_BED, Items.BLACK_DYE);
        q(consumer, Items.BLACK_BANNER, Blocks.BLACK_WOOL);
        l(consumer, Blocks.BLUE_WOOL, Items.BLUE_DYE);
        m(consumer, Blocks.BLUE_CARPET, Blocks.BLUE_WOOL);
        n(consumer, Blocks.BLUE_CARPET, Items.BLUE_DYE);
        o(consumer, Items.BLUE_BED, Blocks.BLUE_WOOL);
        p(consumer, Items.BLUE_BED, Items.BLUE_DYE);
        q(consumer, Items.BLUE_BANNER, Blocks.BLUE_WOOL);
        l(consumer, Blocks.BROWN_WOOL, Items.BROWN_DYE);
        m(consumer, Blocks.BROWN_CARPET, Blocks.BROWN_WOOL);
        n(consumer, Blocks.BROWN_CARPET, Items.BROWN_DYE);
        o(consumer, Items.BROWN_BED, Blocks.BROWN_WOOL);
        p(consumer, Items.BROWN_BED, Items.BROWN_DYE);
        q(consumer, Items.BROWN_BANNER, Blocks.BROWN_WOOL);
        l(consumer, Blocks.CYAN_WOOL, Items.CYAN_DYE);
        m(consumer, Blocks.CYAN_CARPET, Blocks.CYAN_WOOL);
        n(consumer, Blocks.CYAN_CARPET, Items.CYAN_DYE);
        o(consumer, Items.CYAN_BED, Blocks.CYAN_WOOL);
        p(consumer, Items.CYAN_BED, Items.CYAN_DYE);
        q(consumer, Items.CYAN_BANNER, Blocks.CYAN_WOOL);
        l(consumer, Blocks.GRAY_WOOL, Items.GRAY_DYE);
        m(consumer, Blocks.GRAY_CARPET, Blocks.GRAY_WOOL);
        n(consumer, Blocks.GRAY_CARPET, Items.GRAY_DYE);
        o(consumer, Items.GRAY_BED, Blocks.GRAY_WOOL);
        p(consumer, Items.GRAY_BED, Items.GRAY_DYE);
        q(consumer, Items.GRAY_BANNER, Blocks.GRAY_WOOL);
        l(consumer, Blocks.GREEN_WOOL, Items.GREEN_DYE);
        m(consumer, Blocks.GREEN_CARPET, Blocks.GREEN_WOOL);
        n(consumer, Blocks.GREEN_CARPET, Items.GREEN_DYE);
        o(consumer, Items.GREEN_BED, Blocks.GREEN_WOOL);
        p(consumer, Items.GREEN_BED, Items.GREEN_DYE);
        q(consumer, Items.GREEN_BANNER, Blocks.GREEN_WOOL);
        l(consumer, Blocks.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_DYE);
        m(consumer, Blocks.LIGHT_BLUE_CARPET, Blocks.LIGHT_BLUE_WOOL);
        n(consumer, Blocks.LIGHT_BLUE_CARPET, Items.LIGHT_BLUE_DYE);
        o(consumer, Items.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL);
        p(consumer, Items.LIGHT_BLUE_BED, Items.LIGHT_BLUE_DYE);
        q(consumer, Items.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WOOL);
        l(consumer, Blocks.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_DYE);
        m(consumer, Blocks.LIGHT_GRAY_CARPET, Blocks.LIGHT_GRAY_WOOL);
        n(consumer, Blocks.LIGHT_GRAY_CARPET, Items.LIGHT_GRAY_DYE);
        o(consumer, Items.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL);
        p(consumer, Items.LIGHT_GRAY_BED, Items.LIGHT_GRAY_DYE);
        q(consumer, Items.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WOOL);
        l(consumer, Blocks.LIME_WOOL, Items.LIME_DYE);
        m(consumer, Blocks.LIME_CARPET, Blocks.LIME_WOOL);
        n(consumer, Blocks.LIME_CARPET, Items.LIME_DYE);
        o(consumer, Items.LIME_BED, Blocks.LIME_WOOL);
        p(consumer, Items.LIME_BED, Items.LIME_DYE);
        q(consumer, Items.LIME_BANNER, Blocks.LIME_WOOL);
        l(consumer, Blocks.MAGENTA_WOOL, Items.MAGENTA_DYE);
        m(consumer, Blocks.MAGENTA_CARPET, Blocks.MAGENTA_WOOL);
        n(consumer, Blocks.MAGENTA_CARPET, Items.MAGENTA_DYE);
        o(consumer, Items.MAGENTA_BED, Blocks.MAGENTA_WOOL);
        p(consumer, Items.MAGENTA_BED, Items.MAGENTA_DYE);
        q(consumer, Items.MAGENTA_BANNER, Blocks.MAGENTA_WOOL);
        l(consumer, Blocks.ORANGE_WOOL, Items.ORANGE_DYE);
        m(consumer, Blocks.ORANGE_CARPET, Blocks.ORANGE_WOOL);
        n(consumer, Blocks.ORANGE_CARPET, Items.ORANGE_DYE);
        o(consumer, Items.ORANGE_BED, Blocks.ORANGE_WOOL);
        p(consumer, Items.ORANGE_BED, Items.ORANGE_DYE);
        q(consumer, Items.ORANGE_BANNER, Blocks.ORANGE_WOOL);
        l(consumer, Blocks.PINK_WOOL, Items.PINK_DYE);
        m(consumer, Blocks.PINK_CARPET, Blocks.PINK_WOOL);
        n(consumer, Blocks.PINK_CARPET, Items.PINK_DYE);
        o(consumer, Items.PINK_BED, Blocks.PINK_WOOL);
        p(consumer, Items.PINK_BED, Items.PINK_DYE);
        q(consumer, Items.PINK_BANNER, Blocks.PINK_WOOL);
        l(consumer, Blocks.PURPLE_WOOL, Items.PURPLE_DYE);
        m(consumer, Blocks.PURPLE_CARPET, Blocks.PURPLE_WOOL);
        n(consumer, Blocks.PURPLE_CARPET, Items.PURPLE_DYE);
        o(consumer, Items.PURPLE_BED, Blocks.PURPLE_WOOL);
        p(consumer, Items.PURPLE_BED, Items.PURPLE_DYE);
        q(consumer, Items.PURPLE_BANNER, Blocks.PURPLE_WOOL);
        l(consumer, Blocks.RED_WOOL, Items.RED_DYE);
        m(consumer, Blocks.RED_CARPET, Blocks.RED_WOOL);
        n(consumer, Blocks.RED_CARPET, Items.RED_DYE);
        o(consumer, Items.RED_BED, Blocks.RED_WOOL);
        p(consumer, Items.RED_BED, Items.RED_DYE);
        q(consumer, Items.RED_BANNER, Blocks.RED_WOOL);
        m(consumer, Blocks.WHITE_CARPET, Blocks.WHITE_WOOL);
        o(consumer, Items.WHITE_BED, Blocks.WHITE_WOOL);
        q(consumer, Items.WHITE_BANNER, Blocks.WHITE_WOOL);
        l(consumer, Blocks.YELLOW_WOOL, Items.YELLOW_DYE);
        m(consumer, Blocks.YELLOW_CARPET, Blocks.YELLOW_WOOL);
        n(consumer, Blocks.YELLOW_CARPET, Items.YELLOW_DYE);
        o(consumer, Items.YELLOW_BED, Blocks.YELLOW_WOOL);
        p(consumer, Items.YELLOW_BED, Items.YELLOW_DYE);
        q(consumer, Items.YELLOW_BANNER, Blocks.YELLOW_WOOL);
        r(consumer, Blocks.BLACK_STAINED_GLASS, Items.BLACK_DYE);
        s(consumer, Blocks.BLACK_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS);
        t(consumer, Blocks.BLACK_STAINED_GLASS_PANE, Items.BLACK_DYE);
        r(consumer, Blocks.BLUE_STAINED_GLASS, Items.BLUE_DYE);
        s(consumer, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS);
        t(consumer, Blocks.BLUE_STAINED_GLASS_PANE, Items.BLUE_DYE);
        r(consumer, Blocks.BROWN_STAINED_GLASS, Items.BROWN_DYE);
        s(consumer, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS);
        t(consumer, Blocks.BROWN_STAINED_GLASS_PANE, Items.BROWN_DYE);
        r(consumer, Blocks.CYAN_STAINED_GLASS, Items.CYAN_DYE);
        s(consumer, Blocks.CYAN_STAINED_GLASS_PANE, Blocks.CYAN_STAINED_GLASS);
        t(consumer, Blocks.CYAN_STAINED_GLASS_PANE, Items.CYAN_DYE);
        r(consumer, Blocks.GRAY_STAINED_GLASS, Items.GRAY_DYE);
        s(consumer, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS);
        t(consumer, Blocks.GRAY_STAINED_GLASS_PANE, Items.GRAY_DYE);
        r(consumer, Blocks.GREEN_STAINED_GLASS, Items.GREEN_DYE);
        s(consumer, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS);
        t(consumer, Blocks.GREEN_STAINED_GLASS_PANE, Items.GREEN_DYE);
        r(consumer, Blocks.LIGHT_BLUE_STAINED_GLASS, Items.LIGHT_BLUE_DYE);
        s(consumer, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS);
        t(consumer, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Items.LIGHT_BLUE_DYE);
        r(consumer, Blocks.LIGHT_GRAY_STAINED_GLASS, Items.LIGHT_GRAY_DYE);
        s(consumer, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS);
        t(consumer, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Items.LIGHT_GRAY_DYE);
        r(consumer, Blocks.LIME_STAINED_GLASS, Items.LIME_DYE);
        s(consumer, Blocks.LIME_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS);
        t(consumer, Blocks.LIME_STAINED_GLASS_PANE, Items.LIME_DYE);
        r(consumer, Blocks.MAGENTA_STAINED_GLASS, Items.MAGENTA_DYE);
        s(consumer, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS);
        t(consumer, Blocks.MAGENTA_STAINED_GLASS_PANE, Items.MAGENTA_DYE);
        r(consumer, Blocks.ORANGE_STAINED_GLASS, Items.ORANGE_DYE);
        s(consumer, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS);
        t(consumer, Blocks.ORANGE_STAINED_GLASS_PANE, Items.ORANGE_DYE);
        r(consumer, Blocks.PINK_STAINED_GLASS, Items.PINK_DYE);
        s(consumer, Blocks.PINK_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS);
        t(consumer, Blocks.PINK_STAINED_GLASS_PANE, Items.PINK_DYE);
        r(consumer, Blocks.PURPLE_STAINED_GLASS, Items.PURPLE_DYE);
        s(consumer, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS);
        t(consumer, Blocks.PURPLE_STAINED_GLASS_PANE, Items.PURPLE_DYE);
        r(consumer, Blocks.RED_STAINED_GLASS, Items.RED_DYE);
        s(consumer, Blocks.RED_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS);
        t(consumer, Blocks.RED_STAINED_GLASS_PANE, Items.RED_DYE);
        r(consumer, Blocks.WHITE_STAINED_GLASS, Items.WHITE_DYE);
        s(consumer, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.WHITE_STAINED_GLASS);
        t(consumer, Blocks.WHITE_STAINED_GLASS_PANE, Items.WHITE_DYE);
        r(consumer, Blocks.YELLOW_STAINED_GLASS, Items.YELLOW_DYE);
        s(consumer, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.YELLOW_STAINED_GLASS);
        t(consumer, Blocks.YELLOW_STAINED_GLASS_PANE, Items.YELLOW_DYE);
        u(consumer, Blocks.BLACK_TERRACOTTA, Items.BLACK_DYE);
        u(consumer, Blocks.BLUE_TERRACOTTA, Items.BLUE_DYE);
        u(consumer, Blocks.BROWN_TERRACOTTA, Items.BROWN_DYE);
        u(consumer, Blocks.CYAN_TERRACOTTA, Items.CYAN_DYE);
        u(consumer, Blocks.GRAY_TERRACOTTA, Items.GRAY_DYE);
        u(consumer, Blocks.GREEN_TERRACOTTA, Items.GREEN_DYE);
        u(consumer, Blocks.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_BLUE_DYE);
        u(consumer, Blocks.LIGHT_GRAY_TERRACOTTA, Items.LIGHT_GRAY_DYE);
        u(consumer, Blocks.LIME_TERRACOTTA, Items.LIME_DYE);
        u(consumer, Blocks.MAGENTA_TERRACOTTA, Items.MAGENTA_DYE);
        u(consumer, Blocks.ORANGE_TERRACOTTA, Items.ORANGE_DYE);
        u(consumer, Blocks.PINK_TERRACOTTA, Items.PINK_DYE);
        u(consumer, Blocks.PURPLE_TERRACOTTA, Items.PURPLE_DYE);
        u(consumer, Blocks.RED_TERRACOTTA, Items.RED_DYE);
        u(consumer, Blocks.WHITE_TERRACOTTA, Items.WHITE_DYE);
        u(consumer, Blocks.YELLOW_TERRACOTTA, Items.YELLOW_DYE);
        v(consumer, Blocks.BLACK_CONCRETE_POWDER, Items.BLACK_DYE);
        v(consumer, Blocks.BLUE_CONCRETE_POWDER, Items.BLUE_DYE);
        v(consumer, Blocks.BROWN_CONCRETE_POWDER, Items.BROWN_DYE);
        v(consumer, Blocks.CYAN_CONCRETE_POWDER, Items.CYAN_DYE);
        v(consumer, Blocks.GRAY_CONCRETE_POWDER, Items.GRAY_DYE);
        v(consumer, Blocks.GREEN_CONCRETE_POWDER, Items.GREEN_DYE);
        v(consumer, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_DYE);
        v(consumer, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_DYE);
        v(consumer, Blocks.LIME_CONCRETE_POWDER, Items.LIME_DYE);
        v(consumer, Blocks.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_DYE);
        v(consumer, Blocks.ORANGE_CONCRETE_POWDER, Items.ORANGE_DYE);
        v(consumer, Blocks.PINK_CONCRETE_POWDER, Items.PINK_DYE);
        v(consumer, Blocks.PURPLE_CONCRETE_POWDER, Items.PURPLE_DYE);
        v(consumer, Blocks.RED_CONCRETE_POWDER, Items.RED_DYE);
        v(consumer, Blocks.WHITE_CONCRETE_POWDER, Items.WHITE_DYE);
        v(consumer, Blocks.YELLOW_CONCRETE_POWDER, Items.YELLOW_DYE);
        jh.a(Blocks.ACTIVATOR_RAIL, 6).a((Character) '#', (IMaterial) Blocks.REDSTONE_TORCH).a((Character) 'S', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XSX").a("X#X").a("XSX").a("has_rail", (CriterionInstance) a((IMaterial) Blocks.RAIL)).a(consumer);
        ji.a((IMaterial) Blocks.ANDESITE, 2).b(Blocks.DIORITE).b(Blocks.COBBLESTONE).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer);
        jh.a((IMaterial) Blocks.ANVIL).a((Character) 'I', (IMaterial) Blocks.IRON_BLOCK).a((Character) 'i', (IMaterial) Items.IRON_INGOT).a("III").a(" i ").a("iii").a("has_iron_block", (CriterionInstance) a((IMaterial) Blocks.IRON_BLOCK)).a(consumer);
        jh.a((IMaterial) Items.ARMOR_STAND).a((Character) '/', (IMaterial) Items.STICK).a((Character) '_', (IMaterial) Blocks.SMOOTH_STONE_SLAB).a("///").a(" / ").a("/_/").a("has_stone_slab", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_STONE_SLAB)).a(consumer);
        jh.a(Items.ARROW, 4).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.FLINT).a((Character) 'Y', (IMaterial) Items.FEATHER).a("X").a("#").a("Y").a("has_feather", (CriterionInstance) a((IMaterial) Items.FEATHER)).a("has_flint", (CriterionInstance) a((IMaterial) Items.FLINT)).a(consumer);
        jh.a(Blocks.BARREL, 1).a((Character) 'P', (Tag) TagsItem.PLANKS).a((Character) 'S', (Tag) TagsItem.WOODEN_SLABS).a("PSP").a("P P").a("PSP").a("has_planks", (CriterionInstance) a((Tag) TagsItem.PLANKS)).a("has_wood_slab", (CriterionInstance) a((Tag) TagsItem.WOODEN_SLABS)).a(consumer);
        jh.a((IMaterial) Blocks.BEACON).a((Character) 'S', (IMaterial) Items.NETHER_STAR).a((Character) 'G', (IMaterial) Blocks.GLASS).a((Character) 'O', (IMaterial) Blocks.OBSIDIAN).a("GGG").a("GSG").a("OOO").a("has_nether_star", (CriterionInstance) a((IMaterial) Items.NETHER_STAR)).a(consumer);
        jh.a((IMaterial) Blocks.BEEHIVE).a((Character) 'P', (Tag) TagsItem.PLANKS).a((Character) 'H', (IMaterial) Items.HONEYCOMB).a("PPP").a("HHH").a("PPP").a("has_honeycomb", (CriterionInstance) a((IMaterial) Items.HONEYCOMB)).a(consumer);
        ji.a((IMaterial) Items.BEETROOT_SOUP).b(Items.BOWL).b(Items.BEETROOT, 6).a("has_beetroot", (CriterionInstance) a((IMaterial) Items.BEETROOT)).a(consumer);
        ji.a((IMaterial) Items.BLACK_DYE).b(Items.INK_SAC).a("black_dye").a("has_ink_sac", (CriterionInstance) a((IMaterial) Items.INK_SAC)).a(consumer);
        ji.a((IMaterial) Items.BLACK_DYE).b(Blocks.WITHER_ROSE).a("black_dye").a("has_black_flower", (CriterionInstance) a((IMaterial) Blocks.WITHER_ROSE)).a(consumer, "black_dye_from_wither_rose");
        ji.a((IMaterial) Items.BLAZE_POWDER, 2).b(Items.BLAZE_ROD).a("has_blaze_rod", (CriterionInstance) a((IMaterial) Items.BLAZE_ROD)).a(consumer);
        ji.a((IMaterial) Items.BLUE_DYE).b(Items.LAPIS_LAZULI).a("blue_dye").a("has_lapis_lazuli", (CriterionInstance) a((IMaterial) Items.LAPIS_LAZULI)).a(consumer);
        ji.a((IMaterial) Items.BLUE_DYE).b(Blocks.CORNFLOWER).a("blue_dye").a("has_blue_flower", (CriterionInstance) a((IMaterial) Blocks.CORNFLOWER)).a(consumer, "blue_dye_from_cornflower");
        jh.a((IMaterial) Blocks.BLUE_ICE).a((Character) '#', (IMaterial) Blocks.PACKED_ICE).a("###").a("###").a("###").a("has_packed_ice", (CriterionInstance) a((IMaterial) Blocks.PACKED_ICE)).a(consumer);
        jh.a((IMaterial) Blocks.BONE_BLOCK).a((Character) 'X', (IMaterial) Items.BONE_MEAL).a("XXX").a("XXX").a("XXX").a("has_bonemeal", (CriterionInstance) a((IMaterial) Items.BONE_MEAL)).a(consumer);
        ji.a((IMaterial) Items.BONE_MEAL, 3).b(Items.BONE).a("bonemeal").a("has_bone", (CriterionInstance) a((IMaterial) Items.BONE)).a(consumer);
        ji.a((IMaterial) Items.BONE_MEAL, 9).b(Blocks.BONE_BLOCK).a("bonemeal").a("has_bone_block", (CriterionInstance) a((IMaterial) Blocks.BONE_BLOCK)).a(consumer, "bone_meal_from_bone_block");
        ji.a((IMaterial) Items.BOOK).b(Items.PAPER, 3).b(Items.LEATHER).a("has_paper", (CriterionInstance) a((IMaterial) Items.PAPER)).a(consumer);
        jh.a((IMaterial) Blocks.BOOKSHELF).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) 'X', (IMaterial) Items.BOOK).a("###").a("XXX").a("###").a("has_book", (CriterionInstance) a((IMaterial) Items.BOOK)).a(consumer);
        jh.a((IMaterial) Items.BOW).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.STRING).a(" #X").a("# X").a(" #X").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a(consumer);
        jh.a(Items.BOWL, 4).a((Character) '#', (Tag) TagsItem.PLANKS).a("# #").a(" # ").a("has_brown_mushroom", (CriterionInstance) a((IMaterial) Blocks.BROWN_MUSHROOM)).a("has_red_mushroom", (CriterionInstance) a((IMaterial) Blocks.RED_MUSHROOM)).a("has_mushroom_stew", (CriterionInstance) a((IMaterial) Items.MUSHROOM_STEW)).a(consumer);
        jh.a((IMaterial) Items.BREAD).a((Character) '#', (IMaterial) Items.WHEAT).a("###").a("has_wheat", (CriterionInstance) a((IMaterial) Items.WHEAT)).a(consumer);
        jh.a((IMaterial) Blocks.BREWING_STAND).a((Character) 'B', (IMaterial) Items.BLAZE_ROD).a((Character) '#', (Tag) TagsItem.STONE_CRAFTING_MATERIALS).a(" B ").a("###").a("has_blaze_rod", (CriterionInstance) a((IMaterial) Items.BLAZE_ROD)).a(consumer);
        jh.a((IMaterial) Blocks.BRICKS).a((Character) '#', (IMaterial) Items.BRICK).a("##").a("##").a("has_brick", (CriterionInstance) a((IMaterial) Items.BRICK)).a(consumer);
        jh.a(Blocks.BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.BRICKS).a("###").a("has_brick_block", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer);
        jh.a(Blocks.BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.BRICKS).a("#  ").a("## ").a("###").a("has_brick_block", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer);
        ji.a((IMaterial) Items.BROWN_DYE).b(Items.COCOA_BEANS).a("brown_dye").a("has_cocoa_beans", (CriterionInstance) a((IMaterial) Items.COCOA_BEANS)).a(consumer);
        jh.a((IMaterial) Items.BUCKET).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("# #").a(" # ").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.CAKE).a((Character) 'A', (IMaterial) Items.MILK_BUCKET).a((Character) 'B', (IMaterial) Items.SUGAR).a((Character) 'C', (IMaterial) Items.WHEAT).a((Character) 'E', (IMaterial) Items.EGG).a("AAA").a("BEB").a("CCC").a("has_egg", (CriterionInstance) a((IMaterial) Items.EGG)).a(consumer);
        jh.a((IMaterial) Blocks.CAMPFIRE).a((Character) 'L', (Tag) TagsItem.LOGS).a((Character) 'S', (IMaterial) Items.STICK).a((Character) 'C', (Tag) TagsItem.COALS).a(" S ").a("SCS").a("LLL").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a("has_coal", (CriterionInstance) a((Tag) TagsItem.COALS)).a(consumer);
        jh.a((IMaterial) Items.CARROT_ON_A_STICK).a((Character) '#', (IMaterial) Items.FISHING_ROD).a((Character) 'X', (IMaterial) Items.CARROT).a("# ").a(" X").a("has_carrot", (CriterionInstance) a((IMaterial) Items.CARROT)).a(consumer);
        jh.a((IMaterial) Items.WARPED_FUNGUS_ON_A_STICK).a((Character) '#', (IMaterial) Items.FISHING_ROD).a((Character) 'X', (IMaterial) Items.bx).a("# ").a(" X").a("has_warped_fungus", (CriterionInstance) a((IMaterial) Items.bx)).a(consumer);
        jh.a((IMaterial) Blocks.CAULDRON).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("# #").a("# #").a("###").a("has_water_bucket", (CriterionInstance) a((IMaterial) Items.WATER_BUCKET)).a(consumer);
        jh.a((IMaterial) Blocks.COMPOSTER).a((Character) '#', (Tag) TagsItem.WOODEN_SLABS).a("# #").a("# #").a("###").a("has_wood_slab", (CriterionInstance) a((Tag) TagsItem.WOODEN_SLABS)).a(consumer);
        jh.a((IMaterial) Blocks.CHEST).a((Character) '#', (Tag) TagsItem.PLANKS).a("###").a("# #").a("###").a("has_lots_of_items", (CriterionInstance) (new CriterionTriggerInventoryChanged.a(CriterionConditionEntity.b.a, CriterionConditionValue.IntegerRange.b(10), CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, new CriterionConditionItem[0]))).a(consumer);
        jh.a((IMaterial) Items.CHEST_MINECART).a((Character) 'A', (IMaterial) Blocks.CHEST).a((Character) 'B', (IMaterial) Items.MINECART).a("A").a("B").a("has_minecart", (CriterionInstance) a((IMaterial) Items.MINECART)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_NETHER_BRICKS).a((Character) '#', (IMaterial) Blocks.NETHER_BRICK_SLAB).a("#").a("#").a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_QUARTZ_BLOCK).a((Character) '#', (IMaterial) Blocks.QUARTZ_SLAB).a("#").a("#").a("has_chiseled_quartz_block", (CriterionInstance) a((IMaterial) Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_PILLAR)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_STONE_BRICKS).a((Character) '#', (IMaterial) Blocks.STONE_BRICK_SLAB).a("#").a("#").a("has_stone_bricks", (CriterionInstance) a((Tag) TagsItem.STONE_BRICKS)).a(consumer);
        jh.a((IMaterial) Blocks.CLAY).a((Character) '#', (IMaterial) Items.CLAY_BALL).a("##").a("##").a("has_clay_ball", (CriterionInstance) a((IMaterial) Items.CLAY_BALL)).a(consumer);
        jh.a((IMaterial) Items.CLOCK).a((Character) '#', (IMaterial) Items.GOLD_INGOT).a((Character) 'X', (IMaterial) Items.REDSTONE).a(" # ").a("#X#").a(" # ").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        ji.a((IMaterial) Items.COAL, 9).b(Blocks.COAL_BLOCK).a("has_coal_block", (CriterionInstance) a((IMaterial) Blocks.COAL_BLOCK)).a(consumer);
        jh.a((IMaterial) Blocks.COAL_BLOCK).a((Character) '#', (IMaterial) Items.COAL).a("###").a("###").a("###").a("has_coal", (CriterionInstance) a((IMaterial) Items.COAL)).a(consumer);
        jh.a(Blocks.COARSE_DIRT, 4).a((Character) 'D', (IMaterial) Blocks.DIRT).a((Character) 'G', (IMaterial) Blocks.GRAVEL).a("DG").a("GD").a("has_gravel", (CriterionInstance) a((IMaterial) Blocks.GRAVEL)).a(consumer);
        jh.a(Blocks.COBBLESTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a("###").a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer);
        jh.a(Blocks.COBBLESTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a("###").a("###").a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer);
        jh.a((IMaterial) Blocks.COMPARATOR).a((Character) '#', (IMaterial) Blocks.REDSTONE_TORCH).a((Character) 'X', (IMaterial) Items.QUARTZ).a((Character) 'I', (IMaterial) Blocks.STONE).a(" # ").a("#X#").a("III").a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        jh.a((IMaterial) Items.COMPASS).a((Character) '#', (IMaterial) Items.IRON_INGOT).a((Character) 'X', (IMaterial) Items.REDSTONE).a(" # ").a("#X#").a(" # ").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        jh.a(Items.COOKIE, 8).a((Character) '#', (IMaterial) Items.WHEAT).a((Character) 'X', (IMaterial) Items.COCOA_BEANS).a("#X#").a("has_cocoa", (CriterionInstance) a((IMaterial) Items.COCOA_BEANS)).a(consumer);
        jh.a((IMaterial) Blocks.CRAFTING_TABLE).a((Character) '#', (Tag) TagsItem.PLANKS).a("##").a("##").a("has_planks", (CriterionInstance) a((Tag) TagsItem.PLANKS)).a(consumer);
        jh.a((IMaterial) Items.CROSSBOW).a((Character) '~', (IMaterial) Items.STRING).a((Character) '#', (IMaterial) Items.STICK).a((Character) '&', (IMaterial) Items.IRON_INGOT).a((Character) '$', (IMaterial) Blocks.TRIPWIRE_HOOK).a("#&#").a("~$~").a(" # ").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a("has_tripwire_hook", (CriterionInstance) a((IMaterial) Blocks.TRIPWIRE_HOOK)).a(consumer);
        jh.a((IMaterial) Blocks.LOOM).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) '@', (IMaterial) Items.STRING).a("@@").a("##").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_RED_SANDSTONE).a((Character) '#', (IMaterial) Blocks.RED_SANDSTONE_SLAB).a("#").a("#").a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CHISELED_RED_SANDSTONE)).a("has_cut_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CUT_RED_SANDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_SANDSTONE).a((Character) '#', (IMaterial) Blocks.SANDSTONE_SLAB).a("#").a("#").a("has_stone_slab", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE_SLAB)).a(consumer);
        ji.a((IMaterial) Items.CYAN_DYE, 2).b(Items.BLUE_DYE).b(Items.GREEN_DYE).a("has_green_dye", (CriterionInstance) a((IMaterial) Items.GREEN_DYE)).a("has_blue_dye", (CriterionInstance) a((IMaterial) Items.BLUE_DYE)).a(consumer);
        jh.a((IMaterial) Blocks.DARK_PRISMARINE).a((Character) 'S', (IMaterial) Items.PRISMARINE_SHARD).a((Character) 'I', (IMaterial) Items.BLACK_DYE).a("SSS").a("SIS").a("SSS").a("has_prismarine_shard", (CriterionInstance) a((IMaterial) Items.PRISMARINE_SHARD)).a(consumer);
        jh.a(Blocks.PRISMARINE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.PRISMARINE).a("#  ").a("## ").a("###").a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer);
        jh.a(Blocks.PRISMARINE_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.PRISMARINE_BRICKS).a("#  ").a("## ").a("###").a("has_prismarine_bricks", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE_BRICKS)).a(consumer);
        jh.a(Blocks.DARK_PRISMARINE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.DARK_PRISMARINE).a("#  ").a("## ").a("###").a("has_dark_prismarine", (CriterionInstance) a((IMaterial) Blocks.DARK_PRISMARINE)).a(consumer);
        jh.a((IMaterial) Blocks.DAYLIGHT_DETECTOR).a((Character) 'Q', (IMaterial) Items.QUARTZ).a((Character) 'G', (IMaterial) Blocks.GLASS).a('W', RecipeItemStack.a((Tag) TagsItem.WOODEN_SLABS)).a("GGG").a("QQQ").a("WWW").a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        jh.a(Blocks.DETECTOR_RAIL, 6).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Blocks.STONE_PRESSURE_PLATE).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X X").a("X#X").a("XRX").a("has_rail", (CriterionInstance) a((IMaterial) Blocks.RAIL)).a(consumer);
        ji.a((IMaterial) Items.DIAMOND, 9).b(Blocks.DIAMOND_BLOCK).a("has_diamond_block", (CriterionInstance) a((IMaterial) Blocks.DIAMOND_BLOCK)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_AXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.DIAMOND).a("XX").a("X#").a(" #").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Blocks.DIAMOND_BLOCK).a((Character) '#', (IMaterial) Items.DIAMOND).a("###").a("###").a("###").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_BOOTS).a((Character) 'X', (IMaterial) Items.DIAMOND).a("X X").a("X X").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_CHESTPLATE).a((Character) 'X', (IMaterial) Items.DIAMOND).a("X X").a("XXX").a("XXX").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_HELMET).a((Character) 'X', (IMaterial) Items.DIAMOND).a("XXX").a("X X").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_HOE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.DIAMOND).a("XX").a(" #").a(" #").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_LEGGINGS).a((Character) 'X', (IMaterial) Items.DIAMOND).a("XXX").a("X X").a("X X").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_PICKAXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.DIAMOND).a("XXX").a(" # ").a(" # ").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_SHOVEL).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.DIAMOND).a("X").a("#").a("#").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a((IMaterial) Items.DIAMOND_SWORD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.DIAMOND).a("X").a("X").a("#").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a(Blocks.DIORITE, 2).a((Character) 'Q', (IMaterial) Items.QUARTZ).a((Character) 'C', (IMaterial) Blocks.COBBLESTONE).a("CQ").a("QC").a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        jh.a((IMaterial) Blocks.DISPENSER).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a((Character) 'X', (IMaterial) Items.BOW).a("###").a("#X#").a("#R#").a("has_bow", (CriterionInstance) a((IMaterial) Items.BOW)).a(consumer);
        jh.a((IMaterial) Blocks.DROPPER).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a("###").a("# #").a("#R#").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        ji.a((IMaterial) Items.EMERALD, 9).b(Blocks.EMERALD_BLOCK).a("has_emerald_block", (CriterionInstance) a((IMaterial) Blocks.EMERALD_BLOCK)).a(consumer);
        jh.a((IMaterial) Blocks.EMERALD_BLOCK).a((Character) '#', (IMaterial) Items.EMERALD).a("###").a("###").a("###").a("has_emerald", (CriterionInstance) a((IMaterial) Items.EMERALD)).a(consumer);
        jh.a((IMaterial) Blocks.ENCHANTING_TABLE).a((Character) 'B', (IMaterial) Items.BOOK).a((Character) '#', (IMaterial) Blocks.OBSIDIAN).a((Character) 'D', (IMaterial) Items.DIAMOND).a(" B ").a("D#D").a("###").a("has_obsidian", (CriterionInstance) a((IMaterial) Blocks.OBSIDIAN)).a(consumer);
        jh.a((IMaterial) Blocks.ENDER_CHEST).a((Character) '#', (IMaterial) Blocks.OBSIDIAN).a((Character) 'E', (IMaterial) Items.ENDER_EYE).a("###").a("#E#").a("###").a("has_ender_eye", (CriterionInstance) a((IMaterial) Items.ENDER_EYE)).a(consumer);
        ji.a((IMaterial) Items.ENDER_EYE).b(Items.ENDER_PEARL).b(Items.BLAZE_POWDER).a("has_blaze_powder", (CriterionInstance) a((IMaterial) Items.BLAZE_POWDER)).a(consumer);
        jh.a(Blocks.END_STONE_BRICKS, 4).a((Character) '#', (IMaterial) Blocks.END_STONE).a("##").a("##").a("has_end_stone", (CriterionInstance) a((IMaterial) Blocks.END_STONE)).a(consumer);
        jh.a((IMaterial) Items.END_CRYSTAL).a((Character) 'T', (IMaterial) Items.GHAST_TEAR).a((Character) 'E', (IMaterial) Items.ENDER_EYE).a((Character) 'G', (IMaterial) Blocks.GLASS).a("GGG").a("GEG").a("GTG").a("has_ender_eye", (CriterionInstance) a((IMaterial) Items.ENDER_EYE)).a(consumer);
        jh.a(Blocks.END_ROD, 4).a((Character) '#', (IMaterial) Items.POPPED_CHORUS_FRUIT).a((Character) '/', (IMaterial) Items.BLAZE_ROD).a("/").a("#").a("has_chorus_fruit_popped", (CriterionInstance) a((IMaterial) Items.POPPED_CHORUS_FRUIT)).a(consumer);
        ji.a((IMaterial) Items.FERMENTED_SPIDER_EYE).b(Items.SPIDER_EYE).b(Blocks.BROWN_MUSHROOM).b(Items.SUGAR).a("has_spider_eye", (CriterionInstance) a((IMaterial) Items.SPIDER_EYE)).a(consumer);
        ji.a((IMaterial) Items.FIRE_CHARGE, 3).b(Items.GUNPOWDER).b(Items.BLAZE_POWDER).a(RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a("has_blaze_powder", (CriterionInstance) a((IMaterial) Items.BLAZE_POWDER)).a(consumer);
        jh.a((IMaterial) Items.FISHING_ROD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.STRING).a("  #").a(" #X").a("# X").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a(consumer);
        ji.a((IMaterial) Items.FLINT_AND_STEEL).b(Items.IRON_INGOT).b(Items.FLINT).a("has_flint", (CriterionInstance) a((IMaterial) Items.FLINT)).a("has_obsidian", (CriterionInstance) a((IMaterial) Blocks.OBSIDIAN)).a(consumer);
        jh.a((IMaterial) Blocks.FLOWER_POT).a((Character) '#', (IMaterial) Items.BRICK).a("# #").a(" # ").a("has_brick", (CriterionInstance) a((IMaterial) Items.BRICK)).a(consumer);
        jh.a((IMaterial) Blocks.FURNACE).a((Character) '#', (Tag) TagsItem.STONE_CRAFTING_MATERIALS).a("###").a("# #").a("###").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_CRAFTING_MATERIALS)).a(consumer);
        jh.a((IMaterial) Items.FURNACE_MINECART).a((Character) 'A', (IMaterial) Blocks.FURNACE).a((Character) 'B', (IMaterial) Items.MINECART).a("A").a("B").a("has_minecart", (CriterionInstance) a((IMaterial) Items.MINECART)).a(consumer);
        jh.a(Items.GLASS_BOTTLE, 3).a((Character) '#', (IMaterial) Blocks.GLASS).a("# #").a(" # ").a("has_glass", (CriterionInstance) a((IMaterial) Blocks.GLASS)).a(consumer);
        jh.a(Blocks.GLASS_PANE, 16).a((Character) '#', (IMaterial) Blocks.GLASS).a("###").a("###").a("has_glass", (CriterionInstance) a((IMaterial) Blocks.GLASS)).a(consumer);
        jh.a((IMaterial) Blocks.GLOWSTONE).a((Character) '#', (IMaterial) Items.GLOWSTONE_DUST).a("##").a("##").a("has_glowstone_dust", (CriterionInstance) a((IMaterial) Items.GLOWSTONE_DUST)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_APPLE).a((Character) '#', (IMaterial) Items.GOLD_INGOT).a((Character) 'X', (IMaterial) Items.APPLE).a("###").a("#X#").a("###").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_AXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("XX").a("X#").a(" #").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_BOOTS).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("X X").a("X X").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_CARROT).a((Character) '#', (IMaterial) Items.GOLD_NUGGET).a((Character) 'X', (IMaterial) Items.CARROT).a("###").a("#X#").a("###").a("has_gold_nugget", (CriterionInstance) a((IMaterial) Items.GOLD_NUGGET)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_CHESTPLATE).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("X X").a("XXX").a("XXX").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_HELMET).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("XXX").a("X X").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_HOE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("XX").a(" #").a(" #").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_LEGGINGS).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("XXX").a("X X").a("X X").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_PICKAXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("XXX").a(" # ").a(" # ").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a(Blocks.POWERED_RAIL, 6).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("X X").a("X#X").a("XRX").a("has_rail", (CriterionInstance) a((IMaterial) Blocks.RAIL)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_SHOVEL).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("X").a("#").a("#").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Items.GOLDEN_SWORD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.GOLD_INGOT).a("X").a("X").a("#").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.GOLD_BLOCK).a((Character) '#', (IMaterial) Items.GOLD_INGOT).a("###").a("###").a("###").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        ji.a((IMaterial) Items.GOLD_INGOT, 9).b(Blocks.GOLD_BLOCK).a("gold_ingot").a("has_gold_block", (CriterionInstance) a((IMaterial) Blocks.GOLD_BLOCK)).a(consumer, "gold_ingot_from_gold_block");
        jh.a((IMaterial) Items.GOLD_INGOT).a((Character) '#', (IMaterial) Items.GOLD_NUGGET).a("###").a("###").a("###").b("gold_ingot").a("has_gold_nugget", (CriterionInstance) a((IMaterial) Items.GOLD_NUGGET)).a(consumer, "gold_ingot_from_nuggets");
        ji.a((IMaterial) Items.GOLD_NUGGET, 9).b(Items.GOLD_INGOT).a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        ji.a((IMaterial) Blocks.GRANITE).b(Blocks.DIORITE).b(Items.QUARTZ).a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        ji.a((IMaterial) Items.GRAY_DYE, 2).b(Items.BLACK_DYE).b(Items.WHITE_DYE).a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a("has_black_dye", (CriterionInstance) a((IMaterial) Items.BLACK_DYE)).a(consumer);
        jh.a((IMaterial) Blocks.HAY_BLOCK).a((Character) '#', (IMaterial) Items.WHEAT).a("###").a("###").a("###").a("has_wheat", (CriterionInstance) a((IMaterial) Items.WHEAT)).a(consumer);
        jh.a((IMaterial) Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("##").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        ji.a((IMaterial) Items.HONEY_BOTTLE, 4).b(Items.ru).b(Items.GLASS_BOTTLE, 4).a("has_honey_block", (CriterionInstance) a((IMaterial) Blocks.HONEY_BLOCK)).a(consumer);
        jh.a(Blocks.HONEY_BLOCK, 1).a((Character) 'S', (IMaterial) Items.HONEY_BOTTLE).a("SS").a("SS").a("has_honey_bottle", (CriterionInstance) a((IMaterial) Items.HONEY_BOTTLE)).a(consumer);
        jh.a((IMaterial) Blocks.HONEYCOMB_BLOCK).a((Character) 'H', (IMaterial) Items.HONEYCOMB).a("HH").a("HH").a("has_honeycomb", (CriterionInstance) a((IMaterial) Items.HONEYCOMB)).a(consumer);
        jh.a((IMaterial) Blocks.HOPPER).a((Character) 'C', (IMaterial) Blocks.CHEST).a((Character) 'I', (IMaterial) Items.IRON_INGOT).a("I I").a("ICI").a(" I ").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.HOPPER_MINECART).a((Character) 'A', (IMaterial) Blocks.HOPPER).a((Character) 'B', (IMaterial) Items.MINECART).a("A").a("B").a("has_minecart", (CriterionInstance) a((IMaterial) Items.MINECART)).a(consumer);
        jh.a((IMaterial) Items.IRON_AXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XX").a("X#").a(" #").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a(Blocks.IRON_BARS, 16).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("###").a("###").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.IRON_BLOCK).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("###").a("###").a("###").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_BOOTS).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X X").a("X X").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_CHESTPLATE).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X X").a("XXX").a("XXX").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a(Blocks.IRON_DOOR, 3).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("##").a("##").a("##").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_HELMET).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XXX").a("X X").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_HOE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XX").a(" #").a(" #").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        ji.a((IMaterial) Items.IRON_INGOT, 9).b(Blocks.IRON_BLOCK).a("iron_ingot").a("has_iron_block", (CriterionInstance) a((IMaterial) Blocks.IRON_BLOCK)).a(consumer, "iron_ingot_from_iron_block");
        jh.a((IMaterial) Items.IRON_INGOT).a((Character) '#', (IMaterial) Items.IRON_NUGGET).a("###").a("###").a("###").b("iron_ingot").a("has_iron_nugget", (CriterionInstance) a((IMaterial) Items.IRON_NUGGET)).a(consumer, "iron_ingot_from_nuggets");
        jh.a((IMaterial) Items.IRON_LEGGINGS).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XXX").a("X X").a("X X").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        ji.a((IMaterial) Items.IRON_NUGGET, 9).b(Items.IRON_INGOT).a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_PICKAXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("XXX").a(" # ").a(" # ").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_SHOVEL).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X").a("#").a("#").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.IRON_SWORD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X").a("X").a("#").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.IRON_TRAPDOOR).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("##").a("##").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.ITEM_FRAME).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.LEATHER).a("###").a("#X#").a("###").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Blocks.JUKEBOX).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) 'X', (IMaterial) Items.DIAMOND).a("###").a("#X#").a("###").a("has_diamond", (CriterionInstance) a((IMaterial) Items.DIAMOND)).a(consumer);
        jh.a(Blocks.LADDER, 3).a((Character) '#', (IMaterial) Items.STICK).a("# #").a("###").a("# #").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        jh.a((IMaterial) Blocks.LAPIS_BLOCK).a((Character) '#', (IMaterial) Items.LAPIS_LAZULI).a("###").a("###").a("###").a("has_lapis", (CriterionInstance) a((IMaterial) Items.LAPIS_LAZULI)).a(consumer);
        ji.a((IMaterial) Items.LAPIS_LAZULI, 9).b(Blocks.LAPIS_BLOCK).a("has_lapis_block", (CriterionInstance) a((IMaterial) Blocks.LAPIS_BLOCK)).a(consumer);
        jh.a(Items.LEAD, 2).a((Character) '~', (IMaterial) Items.STRING).a((Character) 'O', (IMaterial) Items.SLIME_BALL).a("~~ ").a("~O ").a("  ~").a("has_slime_ball", (CriterionInstance) a((IMaterial) Items.SLIME_BALL)).a(consumer);
        jh.a((IMaterial) Items.LEATHER).a((Character) '#', (IMaterial) Items.RABBIT_HIDE).a("##").a("##").a("has_rabbit_hide", (CriterionInstance) a((IMaterial) Items.RABBIT_HIDE)).a(consumer);
        jh.a((IMaterial) Items.LEATHER_BOOTS).a((Character) 'X', (IMaterial) Items.LEATHER).a("X X").a("X X").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Items.LEATHER_CHESTPLATE).a((Character) 'X', (IMaterial) Items.LEATHER).a("X X").a("XXX").a("XXX").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Items.LEATHER_HELMET).a((Character) 'X', (IMaterial) Items.LEATHER).a("XXX").a("X X").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Items.LEATHER_LEGGINGS).a((Character) 'X', (IMaterial) Items.LEATHER).a("XXX").a("X X").a("X X").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Items.LEATHER_HORSE_ARMOR).a((Character) 'X', (IMaterial) Items.LEATHER).a("X X").a("XXX").a("X X").a("has_leather", (CriterionInstance) a((IMaterial) Items.LEATHER)).a(consumer);
        jh.a((IMaterial) Blocks.LECTERN).a((Character) 'S', (Tag) TagsItem.WOODEN_SLABS).a((Character) 'B', (IMaterial) Blocks.BOOKSHELF).a("SSS").a(" B ").a(" S ").a("has_book", (CriterionInstance) a((IMaterial) Items.BOOK)).a(consumer);
        jh.a((IMaterial) Blocks.LEVER).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a((Character) 'X', (IMaterial) Items.STICK).a("X").a("#").a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer);
        ji.a((IMaterial) Items.LIGHT_BLUE_DYE).b(Blocks.BLUE_ORCHID).a("light_blue_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.BLUE_ORCHID)).a(consumer, "light_blue_dye_from_blue_orchid");
        ji.a((IMaterial) Items.LIGHT_BLUE_DYE, 2).b(Items.BLUE_DYE).b(Items.WHITE_DYE).a("light_blue_dye").a("has_blue_dye", (CriterionInstance) a((IMaterial) Items.BLUE_DYE)).a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a(consumer, "light_blue_dye_from_blue_white_dye");
        ji.a((IMaterial) Items.LIGHT_GRAY_DYE).b(Blocks.AZURE_BLUET).a("light_gray_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.AZURE_BLUET)).a(consumer, "light_gray_dye_from_azure_bluet");
        ji.a((IMaterial) Items.LIGHT_GRAY_DYE, 2).b(Items.GRAY_DYE).b(Items.WHITE_DYE).a("light_gray_dye").a("has_gray_dye", (CriterionInstance) a((IMaterial) Items.GRAY_DYE)).a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a(consumer, "light_gray_dye_from_gray_white_dye");
        ji.a((IMaterial) Items.LIGHT_GRAY_DYE, 3).b(Items.BLACK_DYE).b(Items.WHITE_DYE, 2).a("light_gray_dye").a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a("has_black_dye", (CriterionInstance) a((IMaterial) Items.BLACK_DYE)).a(consumer, "light_gray_dye_from_black_white_dye");
        ji.a((IMaterial) Items.LIGHT_GRAY_DYE).b(Blocks.OXEYE_DAISY).a("light_gray_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.OXEYE_DAISY)).a(consumer, "light_gray_dye_from_oxeye_daisy");
        ji.a((IMaterial) Items.LIGHT_GRAY_DYE).b(Blocks.WHITE_TULIP).a("light_gray_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.WHITE_TULIP)).a(consumer, "light_gray_dye_from_white_tulip");
        jh.a((IMaterial) Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE).a((Character) '#', (IMaterial) Items.GOLD_INGOT).a("##").a("has_gold_ingot", (CriterionInstance) a((IMaterial) Items.GOLD_INGOT)).a(consumer);
        ji.a((IMaterial) Items.LIME_DYE, 2).b(Items.GREEN_DYE).b(Items.WHITE_DYE).a("has_green_dye", (CriterionInstance) a((IMaterial) Items.GREEN_DYE)).a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a(consumer);
        jh.a((IMaterial) Blocks.JACK_O_LANTERN).a((Character) 'A', (IMaterial) Blocks.CARVED_PUMPKIN).a((Character) 'B', (IMaterial) Blocks.TORCH).a("A").a("B").a("has_carved_pumpkin", (CriterionInstance) a((IMaterial) Blocks.CARVED_PUMPKIN)).a(consumer);
        ji.a((IMaterial) Items.MAGENTA_DYE).b(Blocks.ALLIUM).a("magenta_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.ALLIUM)).a(consumer, "magenta_dye_from_allium");
        ji.a((IMaterial) Items.MAGENTA_DYE, 4).b(Items.BLUE_DYE).b(Items.RED_DYE, 2).b(Items.WHITE_DYE).a("magenta_dye").a("has_blue_dye", (CriterionInstance) a((IMaterial) Items.BLUE_DYE)).a("has_rose_red", (CriterionInstance) a((IMaterial) Items.RED_DYE)).a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a(consumer, "magenta_dye_from_blue_red_white_dye");
        ji.a((IMaterial) Items.MAGENTA_DYE, 3).b(Items.BLUE_DYE).b(Items.RED_DYE).b(Items.PINK_DYE).a("magenta_dye").a("has_pink_dye", (CriterionInstance) a((IMaterial) Items.PINK_DYE)).a("has_blue_dye", (CriterionInstance) a((IMaterial) Items.BLUE_DYE)).a("has_red_dye", (CriterionInstance) a((IMaterial) Items.RED_DYE)).a(consumer, "magenta_dye_from_blue_red_pink");
        ji.a((IMaterial) Items.MAGENTA_DYE, 2).b(Blocks.LILAC).a("magenta_dye").a("has_double_plant", (CriterionInstance) a((IMaterial) Blocks.LILAC)).a(consumer, "magenta_dye_from_lilac");
        ji.a((IMaterial) Items.MAGENTA_DYE, 2).b(Items.PURPLE_DYE).b(Items.PINK_DYE).a("magenta_dye").a("has_pink_dye", (CriterionInstance) a((IMaterial) Items.PINK_DYE)).a("has_purple_dye", (CriterionInstance) a((IMaterial) Items.PURPLE_DYE)).a(consumer, "magenta_dye_from_purple_and_pink");
        jh.a((IMaterial) Blocks.MAGMA_BLOCK).a((Character) '#', (IMaterial) Items.MAGMA_CREAM).a("##").a("##").a("has_magma_cream", (CriterionInstance) a((IMaterial) Items.MAGMA_CREAM)).a(consumer);
        ji.a((IMaterial) Items.MAGMA_CREAM).b(Items.BLAZE_POWDER).b(Items.SLIME_BALL).a("has_blaze_powder", (CriterionInstance) a((IMaterial) Items.BLAZE_POWDER)).a(consumer);
        jh.a((IMaterial) Items.MAP).a((Character) '#', (IMaterial) Items.PAPER).a((Character) 'X', (IMaterial) Items.COMPASS).a("###").a("#X#").a("###").a("has_compass", (CriterionInstance) a((IMaterial) Items.COMPASS)).a(consumer);
        jh.a((IMaterial) Blocks.MELON).a((Character) 'M', (IMaterial) Items.MELON_SLICE).a("MMM").a("MMM").a("MMM").a("has_melon", (CriterionInstance) a((IMaterial) Items.MELON_SLICE)).a(consumer);
        ji.a((IMaterial) Items.MELON_SEEDS).b(Items.MELON_SLICE).a("has_melon", (CriterionInstance) a((IMaterial) Items.MELON_SLICE)).a(consumer);
        jh.a((IMaterial) Items.MINECART).a((Character) '#', (IMaterial) Items.IRON_INGOT).a("# #").a("###").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        ji.a((IMaterial) Blocks.MOSSY_COBBLESTONE).b(Blocks.COBBLESTONE).b(Blocks.VINE).a("has_vine", (CriterionInstance) a((IMaterial) Blocks.VINE)).a(consumer);
        jh.a(Blocks.MOSSY_COBBLESTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.MOSSY_COBBLESTONE).a("###").a("###").a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer);
        ji.a((IMaterial) Blocks.MOSSY_STONE_BRICKS).b(Blocks.STONE_BRICKS).b(Blocks.VINE).a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer);
        ji.a((IMaterial) Items.MUSHROOM_STEW).b(Blocks.BROWN_MUSHROOM).b(Blocks.RED_MUSHROOM).b(Items.BOWL).a("has_mushroom_stew", (CriterionInstance) a((IMaterial) Items.MUSHROOM_STEW)).a("has_bowl", (CriterionInstance) a((IMaterial) Items.BOWL)).a("has_brown_mushroom", (CriterionInstance) a((IMaterial) Blocks.BROWN_MUSHROOM)).a("has_red_mushroom", (CriterionInstance) a((IMaterial) Blocks.RED_MUSHROOM)).a(consumer);
        jh.a((IMaterial) Blocks.NETHER_BRICKS).a((Character) 'N', (IMaterial) Items.NETHER_BRICK).a("NN").a("NN").a("has_netherbrick", (CriterionInstance) a((IMaterial) Items.NETHER_BRICK)).a(consumer);
        jh.a(Blocks.NETHER_BRICK_FENCE, 6).a((Character) '#', (IMaterial) Blocks.NETHER_BRICKS).a((Character) '-', (IMaterial) Items.NETHER_BRICK).a("#-#").a("#-#").a("has_nether_brick", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.NETHER_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.NETHER_BRICKS).a("###").a("has_nether_brick", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.NETHER_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.NETHER_BRICKS).a("#  ").a("## ").a("###").a("has_nether_brick", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jh.a((IMaterial) Blocks.NETHER_WART_BLOCK).a((Character) '#', (IMaterial) Items.NETHER_WART).a("###").a("###").a("###").a("has_nether_wart", (CriterionInstance) a((IMaterial) Items.NETHER_WART)).a(consumer);
        jh.a((IMaterial) Blocks.NOTE_BLOCK).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) 'X', (IMaterial) Items.REDSTONE).a("###").a("#X#").a("###").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.OBSERVER).a((Character) 'Q', (IMaterial) Items.QUARTZ).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a("###").a("RRQ").a("###").a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        ji.a((IMaterial) Items.ORANGE_DYE).b(Blocks.ORANGE_TULIP).a("orange_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.ORANGE_TULIP)).a(consumer, "orange_dye_from_orange_tulip");
        ji.a((IMaterial) Items.ORANGE_DYE, 2).b(Items.RED_DYE).b(Items.YELLOW_DYE).a("orange_dye").a("has_red_dye", (CriterionInstance) a((IMaterial) Items.RED_DYE)).a("has_yellow_dye", (CriterionInstance) a((IMaterial) Items.YELLOW_DYE)).a(consumer, "orange_dye_from_red_yellow");
        jh.a((IMaterial) Items.PAINTING).a((Character) '#', (IMaterial) Items.STICK).a('X', RecipeItemStack.a((Tag) TagsItem.WOOL)).a("###").a("#X#").a("###").a("has_wool", (CriterionInstance) a((Tag) TagsItem.WOOL)).a(consumer);
        jh.a(Items.PAPER, 3).a((Character) '#', (IMaterial) Blocks.SUGAR_CANE).a("###").a("has_reeds", (CriterionInstance) a((IMaterial) Blocks.SUGAR_CANE)).a(consumer);
        jh.a(Blocks.QUARTZ_PILLAR, 2).a((Character) '#', (IMaterial) Blocks.QUARTZ_BLOCK).a("#").a("#").a("has_chiseled_quartz_block", (CriterionInstance) a((IMaterial) Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_PILLAR)).a(consumer);
        ji.a((IMaterial) Blocks.PACKED_ICE).b(Blocks.ICE, 9).a("has_ice", (CriterionInstance) a((IMaterial) Blocks.ICE)).a(consumer);
        ji.a((IMaterial) Items.PINK_DYE, 2).b(Blocks.PEONY).a("pink_dye").a("has_double_plant", (CriterionInstance) a((IMaterial) Blocks.PEONY)).a(consumer, "pink_dye_from_peony");
        ji.a((IMaterial) Items.PINK_DYE).b(Blocks.PINK_TULIP).a("pink_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.PINK_TULIP)).a(consumer, "pink_dye_from_pink_tulip");
        ji.a((IMaterial) Items.PINK_DYE, 2).b(Items.RED_DYE).b(Items.WHITE_DYE).a("pink_dye").a("has_white_dye", (CriterionInstance) a((IMaterial) Items.WHITE_DYE)).a("has_red_dye", (CriterionInstance) a((IMaterial) Items.RED_DYE)).a(consumer, "pink_dye_from_red_white_dye");
        jh.a((IMaterial) Blocks.PISTON).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a((Character) 'T', (Tag) TagsItem.PLANKS).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("TTT").a("#X#").a("#R#").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        jh.a(Blocks.cP, 4).a((Character) 'S', (IMaterial) Blocks.BASALT).a("SS").a("SS").a("has_basalt", (CriterionInstance) a((IMaterial) Blocks.BASALT)).a(consumer);
        jh.a(Blocks.POLISHED_GRANITE, 4).a((Character) 'S', (IMaterial) Blocks.GRANITE).a("SS").a("SS").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer);
        jh.a(Blocks.POLISHED_DIORITE, 4).a((Character) 'S', (IMaterial) Blocks.DIORITE).a("SS").a("SS").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer);
        jh.a(Blocks.POLISHED_ANDESITE, 4).a((Character) 'S', (IMaterial) Blocks.ANDESITE).a("SS").a("SS").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer);
        jh.a((IMaterial) Blocks.PRISMARINE).a((Character) 'S', (IMaterial) Items.PRISMARINE_SHARD).a("SS").a("SS").a("has_prismarine_shard", (CriterionInstance) a((IMaterial) Items.PRISMARINE_SHARD)).a(consumer);
        jh.a((IMaterial) Blocks.PRISMARINE_BRICKS).a((Character) 'S', (IMaterial) Items.PRISMARINE_SHARD).a("SSS").a("SSS").a("SSS").a("has_prismarine_shard", (CriterionInstance) a((IMaterial) Items.PRISMARINE_SHARD)).a(consumer);
        jh.a(Blocks.PRISMARINE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.PRISMARINE).a("###").a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer);
        jh.a(Blocks.PRISMARINE_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.PRISMARINE_BRICKS).a("###").a("has_prismarine_bricks", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE_BRICKS)).a(consumer);
        jh.a(Blocks.DARK_PRISMARINE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.DARK_PRISMARINE).a("###").a("has_dark_prismarine", (CriterionInstance) a((IMaterial) Blocks.DARK_PRISMARINE)).a(consumer);
        ji.a((IMaterial) Items.PUMPKIN_PIE).b(Blocks.PUMPKIN).b(Items.SUGAR).b(Items.EGG).a("has_carved_pumpkin", (CriterionInstance) a((IMaterial) Blocks.CARVED_PUMPKIN)).a("has_pumpkin", (CriterionInstance) a((IMaterial) Blocks.PUMPKIN)).a(consumer);
        ji.a((IMaterial) Items.PUMPKIN_SEEDS, 4).b(Blocks.PUMPKIN).a("has_pumpkin", (CriterionInstance) a((IMaterial) Blocks.PUMPKIN)).a(consumer);
        ji.a((IMaterial) Items.PURPLE_DYE, 2).b(Items.BLUE_DYE).b(Items.RED_DYE).a("has_blue_dye", (CriterionInstance) a((IMaterial) Items.BLUE_DYE)).a("has_red_dye", (CriterionInstance) a((IMaterial) Items.RED_DYE)).a(consumer);
        jh.a((IMaterial) Blocks.SHULKER_BOX).a((Character) '#', (IMaterial) Blocks.CHEST).a((Character) '-', (IMaterial) Items.SHULKER_SHELL).a("-").a("#").a("-").a("has_shulker_shell", (CriterionInstance) a((IMaterial) Items.SHULKER_SHELL)).a(consumer);
        jh.a(Blocks.PURPUR_BLOCK, 4).a((Character) 'F', (IMaterial) Items.POPPED_CHORUS_FRUIT).a("FF").a("FF").a("has_chorus_fruit_popped", (CriterionInstance) a((IMaterial) Items.POPPED_CHORUS_FRUIT)).a(consumer);
        jh.a((IMaterial) Blocks.PURPUR_PILLAR).a((Character) '#', (IMaterial) Blocks.PURPUR_SLAB).a("#").a("#").a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer);
        jh.a(Blocks.PURPUR_SLAB, 6).a('#', RecipeItemStack.a(Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR)).a("###").a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer);
        jh.a(Blocks.PURPUR_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR)).a("#  ").a("## ").a("###").a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer);
        jh.a((IMaterial) Blocks.QUARTZ_BLOCK).a((Character) '#', (IMaterial) Items.QUARTZ).a("##").a("##").a("has_quartz", (CriterionInstance) a((IMaterial) Items.QUARTZ)).a(consumer);
        jh.a(Blocks.QUARTZ_BRICKS, 4).a((Character) '#', (IMaterial) Blocks.QUARTZ_BLOCK).a("##").a("##").a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer);
        jh.a(Blocks.QUARTZ_SLAB, 6).a('#', RecipeItemStack.a(Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR)).a("###").a("has_chiseled_quartz_block", (CriterionInstance) a((IMaterial) Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_PILLAR)).a(consumer);
        jh.a(Blocks.QUARTZ_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR)).a("#  ").a("## ").a("###").a("has_chiseled_quartz_block", (CriterionInstance) a((IMaterial) Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_PILLAR)).a(consumer);
        ji.a((IMaterial) Items.RABBIT_STEW).b(Items.BAKED_POTATO).b(Items.COOKED_RABBIT).b(Items.BOWL).b(Items.CARROT).b(Blocks.BROWN_MUSHROOM).a("rabbit_stew").a("has_cooked_rabbit", (CriterionInstance) a((IMaterial) Items.COOKED_RABBIT)).a(consumer, "rabbit_stew_from_brown_mushroom");
        ji.a((IMaterial) Items.RABBIT_STEW).b(Items.BAKED_POTATO).b(Items.COOKED_RABBIT).b(Items.BOWL).b(Items.CARROT).b(Blocks.RED_MUSHROOM).a("rabbit_stew").a("has_cooked_rabbit", (CriterionInstance) a((IMaterial) Items.COOKED_RABBIT)).a(consumer, "rabbit_stew_from_red_mushroom");
        jh.a(Blocks.RAIL, 16).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.IRON_INGOT).a("X X").a("X#X").a("X X").a("has_minecart", (CriterionInstance) a((IMaterial) Items.MINECART)).a(consumer);
        ji.a((IMaterial) Items.REDSTONE, 9).b(Blocks.REDSTONE_BLOCK).a("has_redstone_block", (CriterionInstance) a((IMaterial) Blocks.REDSTONE_BLOCK)).a(consumer);
        jh.a((IMaterial) Blocks.REDSTONE_BLOCK).a((Character) '#', (IMaterial) Items.REDSTONE).a("###").a("###").a("###").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.REDSTONE_LAMP).a((Character) 'R', (IMaterial) Items.REDSTONE).a((Character) 'G', (IMaterial) Blocks.GLOWSTONE).a(" R ").a("RGR").a(" R ").a("has_glowstone", (CriterionInstance) a((IMaterial) Blocks.GLOWSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.REDSTONE_TORCH).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (IMaterial) Items.REDSTONE).a("X").a("#").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a(consumer);
        ji.a((IMaterial) Items.RED_DYE).b(Items.BEETROOT).a("red_dye").a("has_beetroot", (CriterionInstance) a((IMaterial) Items.BEETROOT)).a(consumer, "red_dye_from_beetroot");
        ji.a((IMaterial) Items.RED_DYE).b(Blocks.POPPY).a("red_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.POPPY)).a(consumer, "red_dye_from_poppy");
        ji.a((IMaterial) Items.RED_DYE, 2).b(Blocks.ROSE_BUSH).a("red_dye").a("has_double_plant", (CriterionInstance) a((IMaterial) Blocks.ROSE_BUSH)).a(consumer, "red_dye_from_rose_bush");
        ji.a((IMaterial) Items.RED_DYE).b(Blocks.RED_TULIP).a("red_dye").a("has_red_flower", (CriterionInstance) a((IMaterial) Blocks.RED_TULIP)).a(consumer, "red_dye_from_tulip");
        jh.a((IMaterial) Blocks.RED_NETHER_BRICKS).a((Character) 'W', (IMaterial) Items.NETHER_WART).a((Character) 'N', (IMaterial) Items.NETHER_BRICK).a("NW").a("WN").a("has_nether_wart", (CriterionInstance) a((IMaterial) Items.NETHER_WART)).a(consumer);
        jh.a((IMaterial) Blocks.RED_SANDSTONE).a((Character) '#', (IMaterial) Blocks.RED_SAND).a("##").a("##").a("has_sand", (CriterionInstance) a((IMaterial) Blocks.RED_SAND)).a(consumer);
        jh.a(Blocks.RED_SANDSTONE_SLAB, 6).a('#', RecipeItemStack.a(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE)).a("###").a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CHISELED_RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.CUT_RED_SANDSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.CUT_RED_SANDSTONE).a("###").a("has_cut_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CUT_RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.RED_SANDSTONE_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE)).a("#  ").a("## ").a("###").a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CHISELED_RED_SANDSTONE)).a("has_cut_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.CUT_RED_SANDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.REPEATER).a((Character) '#', (IMaterial) Blocks.REDSTONE_TORCH).a((Character) 'X', (IMaterial) Items.REDSTONE).a((Character) 'I', (IMaterial) Blocks.STONE).a("#X#").a("III").a("has_redstone_torch", (CriterionInstance) a((IMaterial) Blocks.REDSTONE_TORCH)).a(consumer);
        jh.a((IMaterial) Blocks.SANDSTONE).a((Character) '#', (IMaterial) Blocks.SAND).a("##").a("##").a("has_sand", (CriterionInstance) a((IMaterial) Blocks.SAND)).a(consumer);
        jh.a(Blocks.SANDSTONE_SLAB, 6).a('#', RecipeItemStack.a(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE)).a("###").a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a("has_chiseled_sandstone", (CriterionInstance) a((IMaterial) Blocks.CHISELED_SANDSTONE)).a(consumer);
        jh.a(Blocks.CUT_SANDSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.CUT_SANDSTONE).a("###").a("has_cut_sandstone", (CriterionInstance) a((IMaterial) Blocks.CUT_SANDSTONE)).a(consumer);
        jh.a(Blocks.SANDSTONE_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE)).a("#  ").a("## ").a("###").a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a("has_chiseled_sandstone", (CriterionInstance) a((IMaterial) Blocks.CHISELED_SANDSTONE)).a("has_cut_sandstone", (CriterionInstance) a((IMaterial) Blocks.CUT_SANDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.SEA_LANTERN).a((Character) 'S', (IMaterial) Items.PRISMARINE_SHARD).a((Character) 'C', (IMaterial) Items.PRISMARINE_CRYSTALS).a("SCS").a("CCC").a("SCS").a("has_prismarine_crystals", (CriterionInstance) a((IMaterial) Items.PRISMARINE_CRYSTALS)).a(consumer);
        jh.a((IMaterial) Items.SHEARS).a((Character) '#', (IMaterial) Items.IRON_INGOT).a(" #").a("# ").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Items.SHIELD).a((Character) 'W', (Tag) TagsItem.PLANKS).a((Character) 'o', (IMaterial) Items.IRON_INGOT).a("WoW").a("WWW").a(" W ").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.SLIME_BLOCK).a((Character) '#', (IMaterial) Items.SLIME_BALL).a("###").a("###").a("###").a("has_slime_ball", (CriterionInstance) a((IMaterial) Items.SLIME_BALL)).a(consumer);
        ji.a((IMaterial) Items.SLIME_BALL, 9).b(Blocks.SLIME_BLOCK).a("has_slime", (CriterionInstance) a((IMaterial) Blocks.SLIME_BLOCK)).a(consumer);
        jh.a(Blocks.CUT_RED_SANDSTONE, 4).a((Character) '#', (IMaterial) Blocks.RED_SANDSTONE).a("##").a("##").a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.CUT_SANDSTONE, 4).a((Character) '#', (IMaterial) Blocks.SANDSTONE).a("##").a("##").a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.SNOW_BLOCK).a((Character) '#', (IMaterial) Items.SNOWBALL).a("##").a("##").a("has_snowball", (CriterionInstance) a((IMaterial) Items.SNOWBALL)).a(consumer);
        jh.a(Blocks.SNOW, 6).a((Character) '#', (IMaterial) Blocks.SNOW_BLOCK).a("###").a("has_snowball", (CriterionInstance) a((IMaterial) Items.SNOWBALL)).a(consumer);
        jh.a((IMaterial) Blocks.SOUL_CAMPFIRE).a((Character) 'L', (Tag) TagsItem.LOGS).a((Character) 'S', (IMaterial) Items.STICK).a((Character) '#', (Tag) TagsItem.SOUL_FIRE_BASE_BLOCKS).a(" S ").a("S#S").a("LLL").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a("has_soul_sand", (CriterionInstance) a((Tag) TagsItem.SOUL_FIRE_BASE_BLOCKS)).a(consumer);
        jh.a((IMaterial) Items.GLISTERING_MELON_SLICE).a((Character) '#', (IMaterial) Items.GOLD_NUGGET).a((Character) 'X', (IMaterial) Items.MELON_SLICE).a("###").a("#X#").a("###").a("has_melon", (CriterionInstance) a((IMaterial) Items.MELON_SLICE)).a(consumer);
        jh.a(Items.SPECTRAL_ARROW, 2).a((Character) '#', (IMaterial) Items.GLOWSTONE_DUST).a((Character) 'X', (IMaterial) Items.ARROW).a(" # ").a("#X#").a(" # ").a("has_glowstone_dust", (CriterionInstance) a((IMaterial) Items.GLOWSTONE_DUST)).a(consumer);
        jh.a(Items.STICK, 4).a((Character) '#', (Tag) TagsItem.PLANKS).a("#").a("#").b("sticks").a("has_planks", (CriterionInstance) a((Tag) TagsItem.PLANKS)).a(consumer);
        jh.a(Items.STICK, 1).a((Character) '#', (IMaterial) Blocks.BAMBOO).a("#").a("#").b("sticks").a("has_bamboo", (CriterionInstance) a((IMaterial) Blocks.BAMBOO)).a(consumer, "stick_from_bamboo_item");
        jh.a((IMaterial) Blocks.STICKY_PISTON).a((Character) 'P', (IMaterial) Blocks.PISTON).a((Character) 'S', (IMaterial) Items.SLIME_BALL).a("S").a("P").a("has_slime_ball", (CriterionInstance) a((IMaterial) Items.SLIME_BALL)).a(consumer);
        jh.a(Blocks.STONE_BRICKS, 4).a((Character) '#', (IMaterial) Blocks.STONE).a("##").a("##").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a((IMaterial) Items.STONE_AXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.STONE_TOOL_MATERIALS).a("XX").a("X#").a(" #").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
        jh.a(Blocks.STONE_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.STONE_BRICKS).a("###").a("has_stone_bricks", (CriterionInstance) a((Tag) TagsItem.STONE_BRICKS)).a(consumer);
        jh.a(Blocks.STONE_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.STONE_BRICKS).a("#  ").a("## ").a("###").a("has_stone_bricks", (CriterionInstance) a((Tag) TagsItem.STONE_BRICKS)).a(consumer);
        ji.a((IMaterial) Blocks.STONE_BUTTON).b(Blocks.STONE).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a((IMaterial) Items.STONE_HOE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.STONE_TOOL_MATERIALS).a("XX").a(" #").a(" #").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
        jh.a((IMaterial) Items.STONE_PICKAXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.STONE_TOOL_MATERIALS).a("XXX").a(" # ").a(" # ").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
        jh.a((IMaterial) Blocks.STONE_PRESSURE_PLATE).a((Character) '#', (IMaterial) Blocks.STONE).a("##").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a((IMaterial) Items.STONE_SHOVEL).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.STONE_TOOL_MATERIALS).a("X").a("#").a("#").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
        jh.a(Blocks.STONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.STONE).a("###").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a(Blocks.SMOOTH_STONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.SMOOTH_STONE).a("###").a("has_smooth_stone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_STONE)).a(consumer);
        jh.a(Blocks.COBBLESTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.COBBLESTONE).a("#  ").a("## ").a("###").a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer);
        jh.a((IMaterial) Items.STONE_SWORD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.STONE_TOOL_MATERIALS).a("X").a("X").a("#").a("has_cobblestone", (CriterionInstance) a((Tag) TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
        jh.a((IMaterial) Blocks.WHITE_WOOL).a((Character) '#', (IMaterial) Items.STRING).a("##").a("##").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a(consumer, "white_wool_from_string");
        ji.a((IMaterial) Items.SUGAR).b(Blocks.SUGAR_CANE).a("sugar").a("has_reeds", (CriterionInstance) a((IMaterial) Blocks.SUGAR_CANE)).a(consumer, "sugar_from_sugar_cane");
        ji.a((IMaterial) Items.SUGAR, 3).b(Items.HONEY_BOTTLE).a("sugar").a("has_honey_bottle", (CriterionInstance) a((IMaterial) Items.HONEY_BOTTLE)).a(consumer, "sugar_from_honey_bottle");
        jh.a((IMaterial) Blocks.TARGET).a((Character) 'H', (IMaterial) Items.fL).a((Character) 'R', (IMaterial) Items.REDSTONE).a(" R ").a("RHR").a(" R ").a("has_redstone", (CriterionInstance) a((IMaterial) Items.REDSTONE)).a("has_hay_block", (CriterionInstance) a((IMaterial) Blocks.HAY_BLOCK)).a(consumer);
        jh.a((IMaterial) Blocks.TNT).a('#', RecipeItemStack.a(Blocks.SAND, Blocks.RED_SAND)).a((Character) 'X', (IMaterial) Items.GUNPOWDER).a("X#X").a("#X#").a("X#X").a("has_gunpowder", (CriterionInstance) a((IMaterial) Items.GUNPOWDER)).a(consumer);
        jh.a((IMaterial) Items.TNT_MINECART).a((Character) 'A', (IMaterial) Blocks.TNT).a((Character) 'B', (IMaterial) Items.MINECART).a("A").a("B").a("has_minecart", (CriterionInstance) a((IMaterial) Items.MINECART)).a(consumer);
        jh.a(Blocks.TORCH, 4).a((Character) '#', (IMaterial) Items.STICK).a('X', RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a("X").a("#").a("has_stone_pickaxe", (CriterionInstance) a((IMaterial) Items.STONE_PICKAXE)).a(consumer);
        jh.a(Blocks.SOUL_TORCH, 4).a('X', RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'S', (Tag) TagsItem.SOUL_FIRE_BASE_BLOCKS).a("X").a("#").a("S").a("has_soul_sand", (CriterionInstance) a((Tag) TagsItem.SOUL_FIRE_BASE_BLOCKS)).a(consumer);
        jh.a((IMaterial) Blocks.LANTERN).a((Character) '#', (IMaterial) Items.cp).a((Character) 'X', (IMaterial) Items.IRON_NUGGET).a("XXX").a("X#X").a("XXX").a("has_iron_nugget", (CriterionInstance) a((IMaterial) Items.IRON_NUGGET)).a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.SOUL_LANTERN).a((Character) '#', (IMaterial) Items.dp).a((Character) 'X', (IMaterial) Items.IRON_NUGGET).a("XXX").a("X#X").a("XXX").a("has_soul_torch", (CriterionInstance) a((IMaterial) Items.dp)).a(consumer);
        ji.a((IMaterial) Blocks.TRAPPED_CHEST).b(Blocks.CHEST).b(Blocks.TRIPWIRE_HOOK).a("has_tripwire_hook", (CriterionInstance) a((IMaterial) Blocks.TRIPWIRE_HOOK)).a(consumer);
        jh.a(Blocks.TRIPWIRE_HOOK, 2).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) 'S', (IMaterial) Items.STICK).a((Character) 'I', (IMaterial) Items.IRON_INGOT).a("I").a("S").a("#").a("has_string", (CriterionInstance) a((IMaterial) Items.STRING)).a(consumer);
        jh.a((IMaterial) Items.TURTLE_HELMET).a((Character) 'X', (IMaterial) Items.SCUTE).a("XXX").a("X X").a("has_scute", (CriterionInstance) a((IMaterial) Items.SCUTE)).a(consumer);
        ji.a((IMaterial) Items.WHEAT, 9).b(Blocks.HAY_BLOCK).a("has_hay_block", (CriterionInstance) a((IMaterial) Blocks.HAY_BLOCK)).a(consumer);
        ji.a((IMaterial) Items.WHITE_DYE).b(Items.BONE_MEAL).a("white_dye").a("has_bone_meal", (CriterionInstance) a((IMaterial) Items.BONE_MEAL)).a(consumer);
        ji.a((IMaterial) Items.WHITE_DYE).b(Blocks.LILY_OF_THE_VALLEY).a("white_dye").a("has_white_flower", (CriterionInstance) a((IMaterial) Blocks.LILY_OF_THE_VALLEY)).a(consumer, "white_dye_from_lily_of_the_valley");
        jh.a((IMaterial) Items.WOODEN_AXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.PLANKS).a("XX").a("X#").a(" #").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        jh.a((IMaterial) Items.WOODEN_HOE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.PLANKS).a("XX").a(" #").a(" #").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        jh.a((IMaterial) Items.WOODEN_PICKAXE).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.PLANKS).a("XXX").a(" # ").a(" # ").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        jh.a((IMaterial) Items.WOODEN_SHOVEL).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.PLANKS).a("X").a("#").a("#").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        jh.a((IMaterial) Items.WOODEN_SWORD).a((Character) '#', (IMaterial) Items.STICK).a((Character) 'X', (Tag) TagsItem.PLANKS).a("X").a("X").a("#").a("has_stick", (CriterionInstance) a((IMaterial) Items.STICK)).a(consumer);
        ji.a((IMaterial) Items.WRITABLE_BOOK).b(Items.BOOK).b(Items.INK_SAC).b(Items.FEATHER).a("has_book", (CriterionInstance) a((IMaterial) Items.BOOK)).a(consumer);
        ji.a((IMaterial) Items.YELLOW_DYE).b(Blocks.DANDELION).a("yellow_dye").a("has_yellow_flower", (CriterionInstance) a((IMaterial) Blocks.DANDELION)).a(consumer, "yellow_dye_from_dandelion");
        ji.a((IMaterial) Items.YELLOW_DYE, 2).b(Blocks.SUNFLOWER).a("yellow_dye").a("has_double_plant", (CriterionInstance) a((IMaterial) Blocks.SUNFLOWER)).a(consumer, "yellow_dye_from_sunflower");
        ji.a((IMaterial) Items.DRIED_KELP, 9).b(Blocks.DRIED_KELP_BLOCK).a("has_dried_kelp_block", (CriterionInstance) a((IMaterial) Blocks.DRIED_KELP_BLOCK)).a(consumer);
        ji.a((IMaterial) Blocks.DRIED_KELP_BLOCK).b(Items.DRIED_KELP, 9).a("has_dried_kelp", (CriterionInstance) a((IMaterial) Items.DRIED_KELP)).a(consumer);
        jh.a((IMaterial) Blocks.CONDUIT).a((Character) '#', (IMaterial) Items.NAUTILUS_SHELL).a((Character) 'X', (IMaterial) Items.HEART_OF_THE_SEA).a("###").a("#X#").a("###").a("has_nautilus_core", (CriterionInstance) a((IMaterial) Items.HEART_OF_THE_SEA)).a("has_nautilus_shell", (CriterionInstance) a((IMaterial) Items.NAUTILUS_SHELL)).a(consumer);
        jh.a(Blocks.POLISHED_GRANITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_GRANITE).a("#  ").a("## ").a("###").a("has_polished_granite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_GRANITE)).a(consumer);
        jh.a(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.SMOOTH_RED_SANDSTONE).a("#  ").a("## ").a("###").a("has_smooth_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.MOSSY_STONE_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.MOSSY_STONE_BRICKS).a("#  ").a("## ").a("###").a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.POLISHED_DIORITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_DIORITE).a("#  ").a("## ").a("###").a("has_polished_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer);
        jh.a(Blocks.MOSSY_COBBLESTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.MOSSY_COBBLESTONE).a("#  ").a("## ").a("###").a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer);
        jh.a(Blocks.END_STONE_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.END_STONE_BRICKS).a("#  ").a("## ").a("###").a("has_end_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.STONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.STONE).a("#  ").a("## ").a("###").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a(Blocks.SMOOTH_SANDSTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.SMOOTH_SANDSTONE).a("#  ").a("## ").a("###").a("has_smooth_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_SANDSTONE)).a(consumer);
        jh.a(Blocks.SMOOTH_QUARTZ_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.SMOOTH_QUARTZ).a("#  ").a("## ").a("###").a("has_smooth_quartz", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_QUARTZ)).a(consumer);
        jh.a(Blocks.GRANITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.GRANITE).a("#  ").a("## ").a("###").a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer);
        jh.a(Blocks.ANDESITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.ANDESITE).a("#  ").a("## ").a("###").a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer);
        jh.a(Blocks.RED_NETHER_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.RED_NETHER_BRICKS).a("#  ").a("## ").a("###").a("has_red_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.POLISHED_ANDESITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_ANDESITE).a("#  ").a("## ").a("###").a("has_polished_andesite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_ANDESITE)).a(consumer);
        jh.a(Blocks.DIORITE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.DIORITE).a("#  ").a("## ").a("###").a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer);
        jh.a(Blocks.POLISHED_GRANITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_GRANITE).a("###").a("has_polished_granite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_GRANITE)).a(consumer);
        jh.a(Blocks.SMOOTH_RED_SANDSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.SMOOTH_RED_SANDSTONE).a("###").a("has_smooth_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.MOSSY_STONE_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.MOSSY_STONE_BRICKS).a("###").a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.POLISHED_DIORITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_DIORITE).a("###").a("has_polished_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer);
        jh.a(Blocks.MOSSY_COBBLESTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.MOSSY_COBBLESTONE).a("###").a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer);
        jh.a(Blocks.END_STONE_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.END_STONE_BRICKS).a("###").a("has_end_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.SMOOTH_SANDSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.SMOOTH_SANDSTONE).a("###").a("has_smooth_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_SANDSTONE)).a(consumer);
        jh.a(Blocks.SMOOTH_QUARTZ_SLAB, 6).a((Character) '#', (IMaterial) Blocks.SMOOTH_QUARTZ).a("###").a("has_smooth_quartz", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_QUARTZ)).a(consumer);
        jh.a(Blocks.GRANITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.GRANITE).a("###").a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer);
        jh.a(Blocks.ANDESITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.ANDESITE).a("###").a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer);
        jh.a(Blocks.RED_NETHER_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.RED_NETHER_BRICKS).a("###").a("has_red_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.POLISHED_ANDESITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_ANDESITE).a("###").a("has_polished_andesite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_ANDESITE)).a(consumer);
        jh.a(Blocks.DIORITE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.DIORITE).a("###").a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer);
        jh.a(Blocks.BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.BRICKS).a("###").a("###").a("has_bricks", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer);
        jh.a(Blocks.PRISMARINE_WALL, 6).a((Character) '#', (IMaterial) Blocks.PRISMARINE).a("###").a("###").a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer);
        jh.a(Blocks.RED_SANDSTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.RED_SANDSTONE).a("###").a("###").a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer);
        jh.a(Blocks.MOSSY_STONE_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.MOSSY_STONE_BRICKS).a("###").a("###").a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.GRANITE_WALL, 6).a((Character) '#', (IMaterial) Blocks.GRANITE).a("###").a("###").a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer);
        jh.a(Blocks.STONE_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.STONE_BRICKS).a("###").a("###").a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer);
        jh.a(Blocks.NETHER_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.NETHER_BRICKS).a("###").a("###").a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.ANDESITE_WALL, 6).a((Character) '#', (IMaterial) Blocks.ANDESITE).a("###").a("###").a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer);
        jh.a(Blocks.RED_NETHER_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.RED_NETHER_BRICKS).a("###").a("###").a("has_red_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer);
        jh.a(Blocks.SANDSTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.SANDSTONE).a("###").a("###").a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer);
        jh.a(Blocks.END_STONE_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.END_STONE_BRICKS).a("###").a("###").a("has_end_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer);
        jh.a(Blocks.DIORITE_WALL, 6).a((Character) '#', (IMaterial) Blocks.DIORITE).a("###").a("###").a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer);
        ji.a((IMaterial) Items.CREEPER_BANNER_PATTERN).b(Items.PAPER).b(Items.CREEPER_HEAD).a("has_creeper_head", (CriterionInstance) a((IMaterial) Items.CREEPER_HEAD)).a(consumer);
        ji.a((IMaterial) Items.SKULL_BANNER_PATTERN).b(Items.PAPER).b(Items.WITHER_SKELETON_SKULL).a("has_wither_skeleton_skull", (CriterionInstance) a((IMaterial) Items.WITHER_SKELETON_SKULL)).a(consumer);
        ji.a((IMaterial) Items.FLOWER_BANNER_PATTERN).b(Items.PAPER).b(Blocks.OXEYE_DAISY).a("has_oxeye_daisy", (CriterionInstance) a((IMaterial) Blocks.OXEYE_DAISY)).a(consumer);
        ji.a((IMaterial) Items.MOJANG_BANNER_PATTERN).b(Items.PAPER).b(Items.ENCHANTED_GOLDEN_APPLE).a("has_enchanted_golden_apple", (CriterionInstance) a((IMaterial) Items.ENCHANTED_GOLDEN_APPLE)).a(consumer);
        jh.a(Blocks.SCAFFOLDING, 6).a((Character) '~', (IMaterial) Items.STRING).a((Character) 'I', (IMaterial) Blocks.BAMBOO).a("I~I").a("I I").a("I I").a("has_bamboo", (CriterionInstance) a((IMaterial) Blocks.BAMBOO)).a(consumer);
        jh.a((IMaterial) Blocks.GRINDSTONE).a((Character) 'I', (IMaterial) Items.STICK).a((Character) '-', (IMaterial) Blocks.STONE_SLAB).a((Character) '#', (Tag) TagsItem.PLANKS).a("I-I").a("# #").a("has_stone_slab", (CriterionInstance) a((IMaterial) Blocks.STONE_SLAB)).a(consumer);
        jh.a((IMaterial) Blocks.BLAST_FURNACE).a((Character) '#', (IMaterial) Blocks.SMOOTH_STONE).a((Character) 'X', (IMaterial) Blocks.FURNACE).a((Character) 'I', (IMaterial) Items.IRON_INGOT).a("III").a("IXI").a("###").a("has_smooth_stone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_STONE)).a(consumer);
        jh.a((IMaterial) Blocks.SMOKER).a((Character) '#', (Tag) TagsItem.LOGS).a((Character) 'X', (IMaterial) Blocks.FURNACE).a(" # ").a("#X#").a(" # ").a("has_furnace", (CriterionInstance) a((IMaterial) Blocks.FURNACE)).a(consumer);
        jh.a((IMaterial) Blocks.CARTOGRAPHY_TABLE).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) '@', (IMaterial) Items.PAPER).a("@@").a("##").a("##").a("has_paper", (CriterionInstance) a((IMaterial) Items.PAPER)).a(consumer);
        jh.a((IMaterial) Blocks.SMITHING_TABLE).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) '@', (IMaterial) Items.IRON_INGOT).a("@@").a("##").a("##").a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.FLETCHING_TABLE).a((Character) '#', (Tag) TagsItem.PLANKS).a((Character) '@', (IMaterial) Items.FLINT).a("@@").a("##").a("##").a("has_flint", (CriterionInstance) a((IMaterial) Items.FLINT)).a(consumer);
        jh.a((IMaterial) Blocks.STONECUTTER).a((Character) 'I', (IMaterial) Items.IRON_INGOT).a((Character) '#', (IMaterial) Blocks.STONE).a(" I ").a("###").a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jh.a((IMaterial) Blocks.LODESTONE).a((Character) 'S', (IMaterial) Items.dJ).a((Character) '#', (IMaterial) Items.NETHERITE_INGOT).a("SSS").a("S#S").a("SSS").a("has_netherite_ingot", (CriterionInstance) a((IMaterial) Items.NETHERITE_INGOT)).a(consumer);
        jh.a((IMaterial) Blocks.NETHERITE_BLOCK).a((Character) '#', (IMaterial) Items.NETHERITE_INGOT).a("###").a("###").a("###").a("has_netherite_ingot", (CriterionInstance) a((IMaterial) Items.NETHERITE_INGOT)).a(consumer);
        ji.a((IMaterial) Items.NETHERITE_INGOT, 9).b(Blocks.NETHERITE_BLOCK).a("netherite_ingot").a("has_netherite_block", (CriterionInstance) a((IMaterial) Blocks.NETHERITE_BLOCK)).a(consumer, "netherite_ingot_from_netherite_block");
        ji.a((IMaterial) Items.NETHERITE_INGOT).b(Items.NETHERITE_SCRAP, 4).b(Items.GOLD_INGOT, 4).a("netherite_ingot").a("has_netherite_scrap", (CriterionInstance) a((IMaterial) Items.NETHERITE_SCRAP)).a(consumer);
        jh.a((IMaterial) Blocks.RESPAWN_ANCHOR).a((Character) 'O', (IMaterial) Blocks.CRYING_OBSIDIAN).a((Character) 'G', (IMaterial) Blocks.GLOWSTONE).a("OOO").a("GGG").a("OOO").a("has_obsidian", (CriterionInstance) a((IMaterial) Blocks.CRYING_OBSIDIAN)).a(consumer);
        jh.a(Blocks.BLACKSTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.BLACKSTONE).a("#  ").a("## ").a("###").a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE).a("#  ").a("## ").a("###").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS).a("#  ").a("## ").a("###").a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
        jh.a(Blocks.BLACKSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.BLACKSTONE).a("###").a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_SLAB, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE).a("###").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS).a("###").a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE, 4).a((Character) 'S', (IMaterial) Blocks.BLACKSTONE).a("SS").a("SS").a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_BRICKS, 4).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE).a("##").a("##").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.CHISELED_POLISHED_BLACKSTONE).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE_SLAB).a("#").a("#").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a(Blocks.BLACKSTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.BLACKSTONE).a("###").a("###").a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_WALL, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE).a("###").a("###").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_WALL, 6).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS).a("###").a("###").a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
        ji.a((IMaterial) Blocks.POLISHED_BLACKSTONE_BUTTON).b(Blocks.POLISHED_BLACKSTONE).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE).a((Character) '#', (IMaterial) Blocks.POLISHED_BLACKSTONE).a("##").a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer);
        jh.a((IMaterial) Blocks.CHAIN).a((Character) 'I', (IMaterial) Items.IRON_INGOT).a((Character) 'N', (IMaterial) Items.IRON_NUGGET).a("N").a("I").a("N").a("has_iron_nugget", (CriterionInstance) a((IMaterial) Items.IRON_NUGGET)).a("has_iron_ingot", (CriterionInstance) a((IMaterial) Items.IRON_INGOT)).a(consumer);
        jl.a(RecipeSerializer.c).a(consumer, "armor_dye");
        jl.a(RecipeSerializer.k).a(consumer, "banner_duplicate");
        jl.a(RecipeSerializer.d).a(consumer, "book_cloning");
        jl.a(RecipeSerializer.g).a(consumer, "firework_rocket");
        jl.a(RecipeSerializer.h).a(consumer, "firework_star");
        jl.a(RecipeSerializer.i).a(consumer, "firework_star_fade");
        jl.a(RecipeSerializer.e).a(consumer, "map_cloning");
        jl.a(RecipeSerializer.f).a(consumer, "map_extending");
        jl.a(RecipeSerializer.o).a(consumer, "repair_item");
        jl.a(RecipeSerializer.l).a(consumer, "shield_decoration");
        jl.a(RecipeSerializer.m).a(consumer, "shulker_box_coloring");
        jl.a(RecipeSerializer.j).a(consumer, "tipped_arrow");
        jl.a(RecipeSerializer.n).a(consumer, "suspicious_stew");
        jj.c(RecipeItemStack.a(Items.POTATO), Items.BAKED_POTATO, 0.35F, 200).a("has_potato", (CriterionInstance) a((IMaterial) Items.POTATO)).a(consumer);
        jj.c(RecipeItemStack.a(Items.CLAY_BALL), Items.BRICK, 0.3F, 200).a("has_clay_ball", (CriterionInstance) a((IMaterial) Items.CLAY_BALL)).a(consumer);
        jj.c(RecipeItemStack.a((Tag) TagsItem.LOGS_THAT_BURN), Items.CHARCOAL, 0.15F, 200).a("has_log", (CriterionInstance) a((Tag) TagsItem.LOGS_THAT_BURN)).a(consumer);
        jj.c(RecipeItemStack.a(Items.CHORUS_FRUIT), Items.POPPED_CHORUS_FRUIT, 0.1F, 200).a("has_chorus_fruit", (CriterionInstance) a((IMaterial) Items.CHORUS_FRUIT)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.COAL_ORE.getItem()), Items.COAL, 0.1F, 200).a("has_coal_ore", (CriterionInstance) a((IMaterial) Blocks.COAL_ORE)).a(consumer, "coal_from_smelting");
        jj.c(RecipeItemStack.a(Items.BEEF), Items.COOKED_BEEF, 0.35F, 200).a("has_beef", (CriterionInstance) a((IMaterial) Items.BEEF)).a(consumer);
        jj.c(RecipeItemStack.a(Items.CHICKEN), Items.COOKED_CHICKEN, 0.35F, 200).a("has_chicken", (CriterionInstance) a((IMaterial) Items.CHICKEN)).a(consumer);
        jj.c(RecipeItemStack.a(Items.COD), Items.COOKED_COD, 0.35F, 200).a("has_cod", (CriterionInstance) a((IMaterial) Items.COD)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.KELP), Items.DRIED_KELP, 0.1F, 200).a("has_kelp", (CriterionInstance) a((IMaterial) Blocks.KELP)).a(consumer, "dried_kelp_from_smelting");
        jj.c(RecipeItemStack.a(Items.SALMON), Items.COOKED_SALMON, 0.35F, 200).a("has_salmon", (CriterionInstance) a((IMaterial) Items.SALMON)).a(consumer);
        jj.c(RecipeItemStack.a(Items.MUTTON), Items.COOKED_MUTTON, 0.35F, 200).a("has_mutton", (CriterionInstance) a((IMaterial) Items.MUTTON)).a(consumer);
        jj.c(RecipeItemStack.a(Items.PORKCHOP), Items.COOKED_PORKCHOP, 0.35F, 200).a("has_porkchop", (CriterionInstance) a((IMaterial) Items.PORKCHOP)).a(consumer);
        jj.c(RecipeItemStack.a(Items.RABBIT), Items.COOKED_RABBIT, 0.35F, 200).a("has_rabbit", (CriterionInstance) a((IMaterial) Items.RABBIT)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.DIAMOND_ORE.getItem()), Items.DIAMOND, 1.0F, 200).a("has_diamond_ore", (CriterionInstance) a((IMaterial) Blocks.DIAMOND_ORE)).a(consumer, "diamond_from_smelting");
        jj.c(RecipeItemStack.a(Blocks.LAPIS_ORE.getItem()), Items.LAPIS_LAZULI, 0.2F, 200).a("has_lapis_ore", (CriterionInstance) a((IMaterial) Blocks.LAPIS_ORE)).a(consumer, "lapis_from_smelting");
        jj.c(RecipeItemStack.a(Blocks.EMERALD_ORE.getItem()), Items.EMERALD, 1.0F, 200).a("has_emerald_ore", (CriterionInstance) a((IMaterial) Blocks.EMERALD_ORE)).a(consumer, "emerald_from_smelting");
        jj.c(RecipeItemStack.a((Tag) TagsItem.SAND), Blocks.GLASS.getItem(), 0.1F, 200).a("has_sand", (CriterionInstance) a((Tag) TagsItem.SAND)).a(consumer);
        jj.c(RecipeItemStack.a((Tag) TagsItem.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 200).a("has_gold_ore", (CriterionInstance) a((Tag) TagsItem.GOLD_ORES)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.SEA_PICKLE.getItem()), Items.LIME_DYE, 0.1F, 200).a("has_sea_pickle", (CriterionInstance) a((IMaterial) Blocks.SEA_PICKLE)).a(consumer, "lime_dye_from_smelting");
        jj.c(RecipeItemStack.a(Blocks.CACTUS.getItem()), Items.GREEN_DYE, 1.0F, 200).a("has_cactus", (CriterionInstance) a((IMaterial) Blocks.CACTUS)).a(consumer);
        jj.c(RecipeItemStack.a(Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR), Items.GOLD_NUGGET, 0.1F, 200).a("has_golden_pickaxe", (CriterionInstance) a((IMaterial) Items.GOLDEN_PICKAXE)).a("has_golden_shovel", (CriterionInstance) a((IMaterial) Items.GOLDEN_SHOVEL)).a("has_golden_axe", (CriterionInstance) a((IMaterial) Items.GOLDEN_AXE)).a("has_golden_hoe", (CriterionInstance) a((IMaterial) Items.GOLDEN_HOE)).a("has_golden_sword", (CriterionInstance) a((IMaterial) Items.GOLDEN_SWORD)).a("has_golden_helmet", (CriterionInstance) a((IMaterial) Items.GOLDEN_HELMET)).a("has_golden_chestplate", (CriterionInstance) a((IMaterial) Items.GOLDEN_CHESTPLATE)).a("has_golden_leggings", (CriterionInstance) a((IMaterial) Items.GOLDEN_LEGGINGS)).a("has_golden_boots", (CriterionInstance) a((IMaterial) Items.GOLDEN_BOOTS)).a("has_golden_horse_armor", (CriterionInstance) a((IMaterial) Items.GOLDEN_HORSE_ARMOR)).a(consumer, "gold_nugget_from_smelting");
        jj.c(RecipeItemStack.a(Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS), Items.IRON_NUGGET, 0.1F, 200).a("has_iron_pickaxe", (CriterionInstance) a((IMaterial) Items.IRON_PICKAXE)).a("has_iron_shovel", (CriterionInstance) a((IMaterial) Items.IRON_SHOVEL)).a("has_iron_axe", (CriterionInstance) a((IMaterial) Items.IRON_AXE)).a("has_iron_hoe", (CriterionInstance) a((IMaterial) Items.IRON_HOE)).a("has_iron_sword", (CriterionInstance) a((IMaterial) Items.IRON_SWORD)).a("has_iron_helmet", (CriterionInstance) a((IMaterial) Items.IRON_HELMET)).a("has_iron_chestplate", (CriterionInstance) a((IMaterial) Items.IRON_CHESTPLATE)).a("has_iron_leggings", (CriterionInstance) a((IMaterial) Items.IRON_LEGGINGS)).a("has_iron_boots", (CriterionInstance) a((IMaterial) Items.IRON_BOOTS)).a("has_iron_horse_armor", (CriterionInstance) a((IMaterial) Items.IRON_HORSE_ARMOR)).a("has_chainmail_helmet", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_HELMET)).a("has_chainmail_chestplate", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_CHESTPLATE)).a("has_chainmail_leggings", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_LEGGINGS)).a("has_chainmail_boots", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_BOOTS)).a(consumer, "iron_nugget_from_smelting");
        jj.c(RecipeItemStack.a(Blocks.IRON_ORE.getItem()), Items.IRON_INGOT, 0.7F, 200).a("has_iron_ore", (CriterionInstance) a((IMaterial) Blocks.IRON_ORE.getItem())).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.CLAY), Blocks.TERRACOTTA.getItem(), 0.35F, 200).a("has_clay_block", (CriterionInstance) a((IMaterial) Blocks.CLAY)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.NETHERRACK), Items.NETHER_BRICK, 0.1F, 200).a("has_netherrack", (CriterionInstance) a((IMaterial) Blocks.NETHERRACK)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.NETHER_QUARTZ_ORE), Items.QUARTZ, 0.2F, 200).a("has_nether_quartz_ore", (CriterionInstance) a((IMaterial) Blocks.NETHER_QUARTZ_ORE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.REDSTONE_ORE), Items.REDSTONE, 0.7F, 200).a("has_redstone_ore", (CriterionInstance) a((IMaterial) Blocks.REDSTONE_ORE)).a(consumer, "redstone_from_smelting");
        jj.c(RecipeItemStack.a(Blocks.WET_SPONGE), Blocks.SPONGE.getItem(), 0.15F, 200).a("has_wet_sponge", (CriterionInstance) a((IMaterial) Blocks.WET_SPONGE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.STONE.getItem(), 0.1F, 200).a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.STONE), Blocks.SMOOTH_STONE.getItem(), 0.1F, 200).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SMOOTH_SANDSTONE.getItem(), 0.1F, 200).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.SMOOTH_RED_SANDSTONE.getItem(), 0.1F, 200).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.SMOOTH_QUARTZ.getItem(), 0.1F, 200).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.CRACKED_STONE_BRICKS.getItem(), 0.1F, 200).a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.BLACK_TERRACOTTA), Blocks.BLACK_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_black_terracotta", (CriterionInstance) a((IMaterial) Blocks.BLACK_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.BLUE_TERRACOTTA), Blocks.BLUE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_blue_terracotta", (CriterionInstance) a((IMaterial) Blocks.BLUE_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.BROWN_TERRACOTTA), Blocks.BROWN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_brown_terracotta", (CriterionInstance) a((IMaterial) Blocks.BROWN_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.CYAN_TERRACOTTA), Blocks.CYAN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_cyan_terracotta", (CriterionInstance) a((IMaterial) Blocks.CYAN_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.GRAY_TERRACOTTA), Blocks.GRAY_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_gray_terracotta", (CriterionInstance) a((IMaterial) Blocks.GRAY_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.GREEN_TERRACOTTA), Blocks.GREEN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_green_terracotta", (CriterionInstance) a((IMaterial) Blocks.GREEN_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.LIGHT_BLUE_TERRACOTTA), Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_light_blue_terracotta", (CriterionInstance) a((IMaterial) Blocks.LIGHT_BLUE_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.LIGHT_GRAY_TERRACOTTA), Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_light_gray_terracotta", (CriterionInstance) a((IMaterial) Blocks.LIGHT_GRAY_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.LIME_TERRACOTTA), Blocks.LIME_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_lime_terracotta", (CriterionInstance) a((IMaterial) Blocks.LIME_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.MAGENTA_TERRACOTTA), Blocks.MAGENTA_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_magenta_terracotta", (CriterionInstance) a((IMaterial) Blocks.MAGENTA_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.ORANGE_TERRACOTTA), Blocks.ORANGE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_orange_terracotta", (CriterionInstance) a((IMaterial) Blocks.ORANGE_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.PINK_TERRACOTTA), Blocks.PINK_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_pink_terracotta", (CriterionInstance) a((IMaterial) Blocks.PINK_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.PURPLE_TERRACOTTA), Blocks.PURPLE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_purple_terracotta", (CriterionInstance) a((IMaterial) Blocks.PURPLE_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.RED_TERRACOTTA), Blocks.RED_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_red_terracotta", (CriterionInstance) a((IMaterial) Blocks.RED_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.WHITE_TERRACOTTA), Blocks.WHITE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_white_terracotta", (CriterionInstance) a((IMaterial) Blocks.WHITE_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.YELLOW_TERRACOTTA), Blocks.YELLOW_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_yellow_terracotta", (CriterionInstance) a((IMaterial) Blocks.YELLOW_TERRACOTTA)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.ANCIENT_DEBRIS), Items.NETHERITE_SCRAP, 2.0F, 200).a("has_ancient_debris", (CriterionInstance) a((IMaterial) Blocks.ANCIENT_DEBRIS)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getItem(), 0.1F, 200).a("has_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
        jj.c(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.CRACKED_NETHER_BRICKS.getItem(), 0.1F, 200).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer);
        jj.b(RecipeItemStack.a(Blocks.IRON_ORE.getItem()), Items.IRON_INGOT, 0.7F, 100).a("has_iron_ore", (CriterionInstance) a((IMaterial) Blocks.IRON_ORE.getItem())).a(consumer, "iron_ingot_from_blasting");
        jj.b(RecipeItemStack.a((Tag) TagsItem.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 100).a("has_gold_ore", (CriterionInstance) a((Tag) TagsItem.GOLD_ORES)).a(consumer, "gold_ingot_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.DIAMOND_ORE.getItem()), Items.DIAMOND, 1.0F, 100).a("has_diamond_ore", (CriterionInstance) a((IMaterial) Blocks.DIAMOND_ORE)).a(consumer, "diamond_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.LAPIS_ORE.getItem()), Items.LAPIS_LAZULI, 0.2F, 100).a("has_lapis_ore", (CriterionInstance) a((IMaterial) Blocks.LAPIS_ORE)).a(consumer, "lapis_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.REDSTONE_ORE), Items.REDSTONE, 0.7F, 100).a("has_redstone_ore", (CriterionInstance) a((IMaterial) Blocks.REDSTONE_ORE)).a(consumer, "redstone_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.COAL_ORE.getItem()), Items.COAL, 0.1F, 100).a("has_coal_ore", (CriterionInstance) a((IMaterial) Blocks.COAL_ORE)).a(consumer, "coal_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.EMERALD_ORE.getItem()), Items.EMERALD, 1.0F, 100).a("has_emerald_ore", (CriterionInstance) a((IMaterial) Blocks.EMERALD_ORE)).a(consumer, "emerald_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.NETHER_QUARTZ_ORE), Items.QUARTZ, 0.2F, 100).a("has_nether_quartz_ore", (CriterionInstance) a((IMaterial) Blocks.NETHER_QUARTZ_ORE)).a(consumer, "quartz_from_blasting");
        jj.b(RecipeItemStack.a(Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR), Items.GOLD_NUGGET, 0.1F, 100).a("has_golden_pickaxe", (CriterionInstance) a((IMaterial) Items.GOLDEN_PICKAXE)).a("has_golden_shovel", (CriterionInstance) a((IMaterial) Items.GOLDEN_SHOVEL)).a("has_golden_axe", (CriterionInstance) a((IMaterial) Items.GOLDEN_AXE)).a("has_golden_hoe", (CriterionInstance) a((IMaterial) Items.GOLDEN_HOE)).a("has_golden_sword", (CriterionInstance) a((IMaterial) Items.GOLDEN_SWORD)).a("has_golden_helmet", (CriterionInstance) a((IMaterial) Items.GOLDEN_HELMET)).a("has_golden_chestplate", (CriterionInstance) a((IMaterial) Items.GOLDEN_CHESTPLATE)).a("has_golden_leggings", (CriterionInstance) a((IMaterial) Items.GOLDEN_LEGGINGS)).a("has_golden_boots", (CriterionInstance) a((IMaterial) Items.GOLDEN_BOOTS)).a("has_golden_horse_armor", (CriterionInstance) a((IMaterial) Items.GOLDEN_HORSE_ARMOR)).a(consumer, "gold_nugget_from_blasting");
        jj.b(RecipeItemStack.a(Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS), Items.IRON_NUGGET, 0.1F, 100).a("has_iron_pickaxe", (CriterionInstance) a((IMaterial) Items.IRON_PICKAXE)).a("has_iron_shovel", (CriterionInstance) a((IMaterial) Items.IRON_SHOVEL)).a("has_iron_axe", (CriterionInstance) a((IMaterial) Items.IRON_AXE)).a("has_iron_hoe", (CriterionInstance) a((IMaterial) Items.IRON_HOE)).a("has_iron_sword", (CriterionInstance) a((IMaterial) Items.IRON_SWORD)).a("has_iron_helmet", (CriterionInstance) a((IMaterial) Items.IRON_HELMET)).a("has_iron_chestplate", (CriterionInstance) a((IMaterial) Items.IRON_CHESTPLATE)).a("has_iron_leggings", (CriterionInstance) a((IMaterial) Items.IRON_LEGGINGS)).a("has_iron_boots", (CriterionInstance) a((IMaterial) Items.IRON_BOOTS)).a("has_iron_horse_armor", (CriterionInstance) a((IMaterial) Items.IRON_HORSE_ARMOR)).a("has_chainmail_helmet", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_HELMET)).a("has_chainmail_chestplate", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_CHESTPLATE)).a("has_chainmail_leggings", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_LEGGINGS)).a("has_chainmail_boots", (CriterionInstance) a((IMaterial) Items.CHAINMAIL_BOOTS)).a(consumer, "iron_nugget_from_blasting");
        jj.b(RecipeItemStack.a(Blocks.ANCIENT_DEBRIS), Items.NETHERITE_SCRAP, 2.0F, 100).a("has_ancient_debris", (CriterionInstance) a((IMaterial) Blocks.ANCIENT_DEBRIS)).a(consumer, "netherite_scrap_from_blasting");
        a(consumer, "smoking", RecipeSerializer.r, 100);
        a(consumer, "campfire_cooking", RecipeSerializer.s, 600);
        jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_SLAB, 2).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_slab_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), (IMaterial) Blocks.STONE_STAIRS).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_stairs_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), (IMaterial) Blocks.STONE_BRICKS).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_bricks_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_BRICK_SLAB, 2).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_brick_slab_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), (IMaterial) Blocks.STONE_BRICK_STAIRS).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_brick_stairs_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), (IMaterial) Blocks.CHISELED_STONE_BRICKS).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "chiseled_stone_bricks_stone_from_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE), (IMaterial) Blocks.STONE_BRICK_WALL).a("has_stone", (CriterionInstance) a((IMaterial) Blocks.STONE)).a(consumer, "stone_brick_walls_from_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), (IMaterial) Blocks.CUT_SANDSTONE).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "cut_sandstone_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SANDSTONE_SLAB, 2).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "sandstone_slab_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.CUT_SANDSTONE_SLAB, 2).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "cut_sandstone_slab_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.CUT_SANDSTONE), Blocks.CUT_SANDSTONE_SLAB, 2).a("has_cut_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "cut_sandstone_slab_from_cut_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), (IMaterial) Blocks.SANDSTONE_STAIRS).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "sandstone_stairs_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), (IMaterial) Blocks.SANDSTONE_WALL).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "sandstone_wall_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SANDSTONE), (IMaterial) Blocks.CHISELED_SANDSTONE).a("has_sandstone", (CriterionInstance) a((IMaterial) Blocks.SANDSTONE)).a(consumer, "chiseled_sandstone_from_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), (IMaterial) Blocks.CUT_RED_SANDSTONE).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.RED_SANDSTONE_SLAB, 2).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_slab_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.CUT_RED_SANDSTONE_SLAB, 2).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_slab_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.CUT_RED_SANDSTONE), Blocks.CUT_RED_SANDSTONE_SLAB, 2).a("has_cut_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_slab_from_cut_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), (IMaterial) Blocks.RED_SANDSTONE_STAIRS).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_stairs_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), (IMaterial) Blocks.RED_SANDSTONE_WALL).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_wall_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), (IMaterial) Blocks.CHISELED_RED_SANDSTONE).a("has_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.RED_SANDSTONE)).a(consumer, "chiseled_red_sandstone_from_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.QUARTZ_SLAB, 2).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_slab_from_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), (IMaterial) Blocks.QUARTZ_STAIRS).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_stairs_from_quartz_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), (IMaterial) Blocks.QUARTZ_PILLAR).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_pillar_from_quartz_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), (IMaterial) Blocks.CHISELED_QUARTZ_BLOCK).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer, "chiseled_quartz_block_from_quartz_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), (IMaterial) Blocks.QUARTZ_BRICKS).a("has_quartz_block", (CriterionInstance) a((IMaterial) Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_bricks_from_quartz_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), (IMaterial) Blocks.COBBLESTONE_STAIRS).a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer, "cobblestone_stairs_from_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.COBBLESTONE_SLAB, 2).a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer, "cobblestone_slab_from_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), (IMaterial) Blocks.COBBLESTONE_WALL).a("has_cobblestone", (CriterionInstance) a((IMaterial) Blocks.COBBLESTONE)).a(consumer, "cobblestone_wall_from_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.STONE_BRICK_SLAB, 2).a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer, "stone_brick_slab_from_stone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), (IMaterial) Blocks.STONE_BRICK_STAIRS).a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer, "stone_brick_stairs_from_stone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), (IMaterial) Blocks.STONE_BRICK_WALL).a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer, "stone_brick_wall_from_stone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), (IMaterial) Blocks.CHISELED_STONE_BRICKS).a("has_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.STONE_BRICKS)).a(consumer, "chiseled_stone_bricks_from_stone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BRICKS), Blocks.BRICK_SLAB, 2).a("has_bricks", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer, "brick_slab_from_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BRICKS), (IMaterial) Blocks.BRICK_STAIRS).a("has_bricks", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer, "brick_stairs_from_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BRICKS), (IMaterial) Blocks.BRICK_WALL).a("has_bricks", (CriterionInstance) a((IMaterial) Blocks.BRICKS)).a(consumer, "brick_wall_from_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.NETHER_BRICK_SLAB, 2).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_slab_from_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), (IMaterial) Blocks.NETHER_BRICK_STAIRS).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_stairs_from_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), (IMaterial) Blocks.NETHER_BRICK_WALL).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_wall_from_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), (IMaterial) Blocks.CHISELED_NETHER_BRICKS).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.NETHER_BRICKS)).a(consumer, "chiseled_nether_bricks_from_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), Blocks.RED_NETHER_BRICK_SLAB, 2).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_slab_from_red_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), (IMaterial) Blocks.RED_NETHER_BRICK_STAIRS).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_stairs_from_red_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), (IMaterial) Blocks.RED_NETHER_BRICK_WALL).a("has_nether_bricks", (CriterionInstance) a((IMaterial) Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_wall_from_red_nether_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), Blocks.PURPUR_SLAB, 2).a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer, "purpur_slab_from_purpur_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), (IMaterial) Blocks.PURPUR_STAIRS).a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer, "purpur_stairs_from_purpur_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), (IMaterial) Blocks.PURPUR_PILLAR).a("has_purpur_block", (CriterionInstance) a((IMaterial) Blocks.PURPUR_BLOCK)).a(consumer, "purpur_pillar_from_purpur_block_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PRISMARINE), Blocks.PRISMARINE_SLAB, 2).a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer, "prismarine_slab_from_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PRISMARINE), (IMaterial) Blocks.PRISMARINE_STAIRS).a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer, "prismarine_stairs_from_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PRISMARINE), (IMaterial) Blocks.PRISMARINE_WALL).a("has_prismarine", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE)).a(consumer, "prismarine_wall_from_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PRISMARINE_BRICKS), Blocks.PRISMARINE_BRICK_SLAB, 2).a("has_prismarine_brick", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE_BRICKS)).a(consumer, "prismarine_brick_slab_from_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.PRISMARINE_BRICKS), (IMaterial) Blocks.PRISMARINE_BRICK_STAIRS).a("has_prismarine_brick", (CriterionInstance) a((IMaterial) Blocks.PRISMARINE_BRICKS)).a(consumer, "prismarine_brick_stairs_from_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DARK_PRISMARINE), Blocks.DARK_PRISMARINE_SLAB, 2).a("has_dark_prismarine", (CriterionInstance) a((IMaterial) Blocks.DARK_PRISMARINE)).a(consumer, "dark_prismarine_slab_from_dark_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DARK_PRISMARINE), (IMaterial) Blocks.DARK_PRISMARINE_STAIRS).a("has_dark_prismarine", (CriterionInstance) a((IMaterial) Blocks.DARK_PRISMARINE)).a(consumer, "dark_prismarine_stairs_from_dark_prismarine_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.ANDESITE_SLAB, 2).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "andesite_slab_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), (IMaterial) Blocks.ANDESITE_STAIRS).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "andesite_stairs_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), (IMaterial) Blocks.ANDESITE_WALL).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "andesite_wall_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), (IMaterial) Blocks.POLISHED_ANDESITE).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "polished_andesite_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.POLISHED_ANDESITE_SLAB, 2).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "polished_andesite_slab_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.ANDESITE), (IMaterial) Blocks.POLISHED_ANDESITE_STAIRS).a("has_andesite", (CriterionInstance) a((IMaterial) Blocks.ANDESITE)).a(consumer, "polished_andesite_stairs_from_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_ANDESITE), Blocks.POLISHED_ANDESITE_SLAB, 2).a("has_polished_andesite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_ANDESITE)).a(consumer, "polished_andesite_slab_from_polished_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_ANDESITE), (IMaterial) Blocks.POLISHED_ANDESITE_STAIRS).a("has_polished_andesite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_ANDESITE)).a(consumer, "polished_andesite_stairs_from_polished_andesite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BASALT), (IMaterial) Blocks.cP).a("has_basalt", (CriterionInstance) a((IMaterial) Blocks.BASALT)).a(consumer, "polished_basalt_from_basalt_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.GRANITE_SLAB, 2).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "granite_slab_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), (IMaterial) Blocks.GRANITE_STAIRS).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "granite_stairs_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), (IMaterial) Blocks.GRANITE_WALL).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "granite_wall_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), (IMaterial) Blocks.POLISHED_GRANITE).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "polished_granite_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.POLISHED_GRANITE_SLAB, 2).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "polished_granite_slab_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.GRANITE), (IMaterial) Blocks.POLISHED_GRANITE_STAIRS).a("has_granite", (CriterionInstance) a((IMaterial) Blocks.GRANITE)).a(consumer, "polished_granite_stairs_from_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_GRANITE), Blocks.POLISHED_GRANITE_SLAB, 2).a("has_polished_granite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_GRANITE)).a(consumer, "polished_granite_slab_from_polished_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_GRANITE), (IMaterial) Blocks.POLISHED_GRANITE_STAIRS).a("has_polished_granite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_GRANITE)).a(consumer, "polished_granite_stairs_from_polished_granite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.DIORITE_SLAB, 2).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer, "diorite_slab_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), (IMaterial) Blocks.DIORITE_STAIRS).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer, "diorite_stairs_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), (IMaterial) Blocks.DIORITE_WALL).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer, "diorite_wall_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), (IMaterial) Blocks.POLISHED_DIORITE).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.DIORITE)).a(consumer, "polished_diorite_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.POLISHED_DIORITE_SLAB, 2).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_slab_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.DIORITE), (IMaterial) Blocks.POLISHED_DIORITE_STAIRS).a("has_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_stairs_from_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_DIORITE), Blocks.POLISHED_DIORITE_SLAB, 2).a("has_polished_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_slab_from_polished_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_DIORITE), (IMaterial) Blocks.POLISHED_DIORITE_STAIRS).a("has_polished_diorite", (CriterionInstance) a((IMaterial) Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_stairs_from_polished_diorite_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), Blocks.MOSSY_STONE_BRICK_SLAB, 2).a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_slab_from_mossy_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), (IMaterial) Blocks.MOSSY_STONE_BRICK_STAIRS).a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_stairs_from_mossy_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), (IMaterial) Blocks.MOSSY_STONE_BRICK_WALL).a("has_mossy_stone_bricks", (CriterionInstance) a((IMaterial) Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_wall_from_mossy_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), Blocks.MOSSY_COBBLESTONE_SLAB, 2).a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_slab_from_mossy_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), (IMaterial) Blocks.MOSSY_COBBLESTONE_STAIRS).a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_stairs_from_mossy_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), (IMaterial) Blocks.MOSSY_COBBLESTONE_WALL).a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_wall_from_mossy_cobblestone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_SANDSTONE), Blocks.SMOOTH_SANDSTONE_SLAB, 2).a("has_smooth_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_SANDSTONE)).a(consumer, "smooth_sandstone_slab_from_smooth_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_SANDSTONE), (IMaterial) Blocks.SMOOTH_SANDSTONE_STAIRS).a("has_mossy_cobblestone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_SANDSTONE)).a(consumer, "smooth_sandstone_stairs_from_smooth_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_RED_SANDSTONE), Blocks.SMOOTH_RED_SANDSTONE_SLAB, 2).a("has_smooth_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_RED_SANDSTONE)).a(consumer, "smooth_red_sandstone_slab_from_smooth_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_RED_SANDSTONE), (IMaterial) Blocks.SMOOTH_RED_SANDSTONE_STAIRS).a("has_smooth_red_sandstone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_RED_SANDSTONE)).a(consumer, "smooth_red_sandstone_stairs_from_smooth_red_sandstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_QUARTZ), Blocks.SMOOTH_QUARTZ_SLAB, 2).a("has_smooth_quartz", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_QUARTZ)).a(consumer, "smooth_quartz_slab_from_smooth_quartz_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_QUARTZ), (IMaterial) Blocks.SMOOTH_QUARTZ_STAIRS).a("has_smooth_quartz", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_QUARTZ)).a(consumer, "smooth_quartz_stairs_from_smooth_quartz_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), Blocks.END_STONE_BRICK_SLAB, 2).a("has_end_stone_brick", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_slab_from_end_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), (IMaterial) Blocks.END_STONE_BRICK_STAIRS).a("has_end_stone_brick", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_stairs_from_end_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), (IMaterial) Blocks.END_STONE_BRICK_WALL).a("has_end_stone_brick", (CriterionInstance) a((IMaterial) Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_wall_from_end_stone_brick_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE), (IMaterial) Blocks.END_STONE_BRICKS).a("has_end_stone", (CriterionInstance) a((IMaterial) Blocks.END_STONE)).a(consumer, "end_stone_bricks_from_end_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE), Blocks.END_STONE_BRICK_SLAB, 2).a("has_end_stone", (CriterionInstance) a((IMaterial) Blocks.END_STONE)).a(consumer, "end_stone_brick_slab_from_end_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE), (IMaterial) Blocks.END_STONE_BRICK_STAIRS).a("has_end_stone", (CriterionInstance) a((IMaterial) Blocks.END_STONE)).a(consumer, "end_stone_brick_stairs_from_end_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.END_STONE), (IMaterial) Blocks.END_STONE_BRICK_WALL).a("has_end_stone", (CriterionInstance) a((IMaterial) Blocks.END_STONE)).a(consumer, "end_stone_brick_wall_from_end_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.SMOOTH_STONE), Blocks.SMOOTH_STONE_SLAB, 2).a("has_smooth_stone", (CriterionInstance) a((IMaterial) Blocks.SMOOTH_STONE)).a(consumer, "smooth_stone_slab_from_smooth_stone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.BLACKSTONE_SLAB, 2).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "blackstone_slab_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.BLACKSTONE_STAIRS).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "blackstone_stairs_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.BLACKSTONE_WALL).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "blackstone_wall_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_WALL).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_wall_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_SLAB, 2).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_slab_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_STAIRS).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_stairs_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.CHISELED_POLISHED_BLACKSTONE).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "chiseled_polished_blackstone_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_bricks_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_slab_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_stairs_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_blackstone", (CriterionInstance) a((IMaterial) Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_wall_from_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_SLAB, 2).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_slab_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_STAIRS).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_stairs_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_bricks_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_WALL).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_wall_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_slab_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_stairs_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_wall_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), (IMaterial) Blocks.CHISELED_POLISHED_BLACKSTONE).a("has_polished_blackstone", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE)).a(consumer, "chiseled_polished_blackstone_from_polished_blackstone_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_slab_from_polished_blackstone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_stairs_from_polished_blackstone_bricks_stonecutting");
        jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), (IMaterial) Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_polished_blackstone_bricks", (CriterionInstance) a((IMaterial) Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_wall_from_polished_blackstone_bricks_stonecutting");
        a(consumer, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
        a(consumer, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
        a(consumer, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
        a(consumer, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
        a(consumer, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);
        a(consumer, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
        a(consumer, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
        a(consumer, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
        a(consumer, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);
    }

    private static void a(Consumer<jf> consumer, Item item, Item item1) {
        jm.a(RecipeItemStack.a(item), RecipeItemStack.a(Items.NETHERITE_INGOT), item1).a("has_netherite_ingot", (CriterionInstance) a((IMaterial) Items.NETHERITE_INGOT)).a(consumer, IRegistry.ITEM.getKey(item1.getItem()).getKey() + "_smithing");
    }

    private static void a(Consumer<jf> consumer, IMaterial imaterial, Tag<Item> tag) {
        ji.a(imaterial, 4).a(tag).a("planks").a("has_log", (CriterionInstance) a(tag)).a(consumer);
    }

    private static void b(Consumer<jf> consumer, IMaterial imaterial, Tag<Item> tag) {
        ji.a(imaterial, 4).a(tag).a("planks").a("has_logs", (CriterionInstance) a(tag)).a(consumer);
    }

    private static void a(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 3).a('#', imaterial1).a("##").a("##").b("bark").a("has_log", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void b(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial).a('#', imaterial1).a("# #").a("###").b("boat").a("in_water", (CriterionInstance) a(Blocks.WATER)).a(consumer);
    }

    private static void c(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        ji.a(imaterial).b(imaterial1).a("wooden_button").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void d(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 3).a('#', imaterial1).a("##").a("##").a("##").b("wooden_door").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void e(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 3).a((Character) '#', (IMaterial) Items.STICK).a('W', imaterial1).a("W#W").a("W#W").b("wooden_fence").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void f(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial).a((Character) '#', (IMaterial) Items.STICK).a('W', imaterial1).a("#W#").a("#W#").b("wooden_fence_gate").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void g(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial).a('#', imaterial1).a("##").b("wooden_pressure_plate").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void h(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 6).a('#', imaterial1).a("###").b("wooden_slab").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void i(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 4).a('#', imaterial1).a("#  ").a("## ").a("###").b("wooden_stairs").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void j(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 2).a('#', imaterial1).a("###").a("###").b("wooden_trapdoor").a("has_planks", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void k(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial, 3).b("sign").a('#', imaterial1).a((Character) 'X', (IMaterial) Items.STICK).a("###").a("###").a(" X ").a("has_" + s, (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void l(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        ji.a(imaterial).b(imaterial1).b(Blocks.WHITE_WOOL).a("wool").a("has_white_wool", (CriterionInstance) a((IMaterial) Blocks.WHITE_WOOL)).a(consumer);
    }

    private static void m(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial, 3).a('#', imaterial1).a("##").b("carpet").a("has_" + s, (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void n(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();
        String s1 = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial, 8).a((Character) '#', (IMaterial) Blocks.WHITE_CARPET).a('$', imaterial1).a("###").a("#$#").a("###").b("carpet").a("has_white_carpet", (CriterionInstance) a((IMaterial) Blocks.WHITE_CARPET)).a("has_" + s1, (CriterionInstance) a(imaterial1)).a(consumer, s + "_from_white_carpet");
    }

    private static void o(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial).a('#', imaterial1).a((Character) 'X', (Tag) TagsItem.PLANKS).a("###").a("XXX").b("bed").a("has_" + s, (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void p(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();

        ji.a(imaterial).b(Items.WHITE_BED).b(imaterial1).a("dyed_bed").a("has_bed", (CriterionInstance) a((IMaterial) Items.WHITE_BED)).a(consumer, s + "_from_white_bed");
    }

    private static void q(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial).a('#', imaterial1).a((Character) '|', (IMaterial) Items.STICK).a("###").a("###").a(" | ").b("banner").a("has_" + s, (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void r(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 8).a((Character) '#', (IMaterial) Blocks.GLASS).a('X', imaterial1).a("###").a("#X#").a("###").b("stained_glass").a("has_glass", (CriterionInstance) a((IMaterial) Blocks.GLASS)).a(consumer);
    }

    private static void s(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 16).a('#', imaterial1).a("###").a("###").b("stained_glass_pane").a("has_glass", (CriterionInstance) a(imaterial1)).a(consumer);
    }

    private static void t(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();
        String s1 = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

        jh.a(imaterial, 8).a((Character) '#', (IMaterial) Blocks.GLASS_PANE).a('$', imaterial1).a("###").a("#$#").a("###").b("stained_glass_pane").a("has_glass_pane", (CriterionInstance) a((IMaterial) Blocks.GLASS_PANE)).a("has_" + s1, (CriterionInstance) a(imaterial1)).a(consumer, s + "_from_glass_pane");
    }

    private static void u(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        jh.a(imaterial, 8).a((Character) '#', (IMaterial) Blocks.TERRACOTTA).a('X', imaterial1).a("###").a("#X#").a("###").b("stained_terracotta").a("has_terracotta", (CriterionInstance) a((IMaterial) Blocks.TERRACOTTA)).a(consumer);
    }

    private static void v(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
        ji.a(imaterial, 8).b(imaterial1).b(Blocks.SAND, 4).b(Blocks.GRAVEL, 4).a("concrete_powder").a("has_sand", (CriterionInstance) a((IMaterial) Blocks.SAND)).a("has_gravel", (CriterionInstance) a((IMaterial) Blocks.GRAVEL)).a(consumer);
    }

    private static void a(Consumer<jf> consumer, String s, RecipeSerializerCooking<?> recipeserializercooking, int i) {
        jj.a(RecipeItemStack.a(Items.BEEF), Items.COOKED_BEEF, 0.35F, i, recipeserializercooking).a("has_beef", (CriterionInstance) a((IMaterial) Items.BEEF)).a(consumer, "cooked_beef_from_" + s);
        jj.a(RecipeItemStack.a(Items.CHICKEN), Items.COOKED_CHICKEN, 0.35F, i, recipeserializercooking).a("has_chicken", (CriterionInstance) a((IMaterial) Items.CHICKEN)).a(consumer, "cooked_chicken_from_" + s);
        jj.a(RecipeItemStack.a(Items.COD), Items.COOKED_COD, 0.35F, i, recipeserializercooking).a("has_cod", (CriterionInstance) a((IMaterial) Items.COD)).a(consumer, "cooked_cod_from_" + s);
        jj.a(RecipeItemStack.a(Blocks.KELP), Items.DRIED_KELP, 0.1F, i, recipeserializercooking).a("has_kelp", (CriterionInstance) a((IMaterial) Blocks.KELP)).a(consumer, "dried_kelp_from_" + s);
        jj.a(RecipeItemStack.a(Items.SALMON), Items.COOKED_SALMON, 0.35F, i, recipeserializercooking).a("has_salmon", (CriterionInstance) a((IMaterial) Items.SALMON)).a(consumer, "cooked_salmon_from_" + s);
        jj.a(RecipeItemStack.a(Items.MUTTON), Items.COOKED_MUTTON, 0.35F, i, recipeserializercooking).a("has_mutton", (CriterionInstance) a((IMaterial) Items.MUTTON)).a(consumer, "cooked_mutton_from_" + s);
        jj.a(RecipeItemStack.a(Items.PORKCHOP), Items.COOKED_PORKCHOP, 0.35F, i, recipeserializercooking).a("has_porkchop", (CriterionInstance) a((IMaterial) Items.PORKCHOP)).a(consumer, "cooked_porkchop_from_" + s);
        jj.a(RecipeItemStack.a(Items.POTATO), Items.BAKED_POTATO, 0.35F, i, recipeserializercooking).a("has_potato", (CriterionInstance) a((IMaterial) Items.POTATO)).a(consumer, "baked_potato_from_" + s);
        jj.a(RecipeItemStack.a(Items.RABBIT), Items.COOKED_RABBIT, 0.35F, i, recipeserializercooking).a("has_rabbit", (CriterionInstance) a((IMaterial) Items.RABBIT)).a(consumer, "cooked_rabbit_from_" + s);
    }

    private static CriterionTriggerEnterBlock.a a(Block block) {
        return new CriterionTriggerEnterBlock.a(CriterionConditionEntity.b.a, block, CriterionTriggerProperties.a);
    }

    private static CriterionTriggerInventoryChanged.a a(IMaterial imaterial) {
        return a(CriterionConditionItem.a.a().a(imaterial).b());
    }

    private static CriterionTriggerInventoryChanged.a a(Tag<Item> tag) {
        return a(CriterionConditionItem.a.a().a(tag).b());
    }

    private static CriterionTriggerInventoryChanged.a a(CriterionConditionItem... acriterionconditionitem) {
        return new CriterionTriggerInventoryChanged.a(CriterionConditionEntity.b.a, CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, acriterionconditionitem);
    }

    @Override
    public String a() {
        return "Recipes";
    }
}
