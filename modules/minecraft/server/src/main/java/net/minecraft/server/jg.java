package net.minecraft.server;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class jg implements DebugReportProvider {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson c = (new GsonBuilder()).setPrettyPrinting().create();
	private final DebugReportGenerator d;

	public jg(DebugReportGenerator debugreportgenerator) {
		this.d = debugreportgenerator;
	}

	private static void a(HashCache hashcache, JsonObject jsonobject, Path path) {
		try {
			String s = jg.c.toJson(jsonobject);
			String s1 = jg.a.hashUnencodedChars(s).toString();

			if (!Objects.equals(hashcache.a(path), s1) || !Files.exists(path)) {
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

			if (!Objects.equals(hashcache.a(path), s1) || !Files.exists(path)) {
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
		a(consumer, Blocks.ACACIA_PLANKS, TagsItem.ACACIA_LOGS);
		b(consumer, Blocks.BIRCH_PLANKS, TagsItem.BIRCH_LOGS);
		b(consumer, Blocks.CRIMSON_PLANKS, TagsItem.CRIMSON_STEMS);
		a(consumer, Blocks.DARK_OAK_PLANKS, TagsItem.DARK_OAK_LOGS);
		b(consumer, Blocks.JUNGLE_PLANKS, TagsItem.JUNGLE_LOGS);
		b(consumer, Blocks.OAK_PLANKS, TagsItem.OAK_LOGS);
		b(consumer, Blocks.SPRUCE_PLANKS, TagsItem.SPRUCE_LOGS);
		b(consumer, Blocks.WARPED_PLANKS, TagsItem.WARPED_STEMS);
		a(consumer, Blocks.ACACIA_WOOD, Blocks.ACACIA_LOG);
		a(consumer, Blocks.BIRCH_WOOD, Blocks.BIRCH_LOG);
		a(consumer, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_LOG);
		a(consumer, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_LOG);
		a(consumer, Blocks.OAK_WOOD, Blocks.OAK_LOG);
		a(consumer, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_LOG);
		a(consumer, Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_STEM);
		a(consumer, Blocks.WARPED_HYPHAE, Blocks.WARPED_STEM);
		a(consumer, Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_ACACIA_LOG);
		a(consumer, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_BIRCH_LOG);
		a(consumer, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_LOG);
		a(consumer, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_LOG);
		a(consumer, Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_OAK_LOG);
		a(consumer, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_LOG);
		a(consumer, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM);
		a(consumer, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM);
		b(consumer, Items.ACACIA_BOAT, Blocks.ACACIA_PLANKS);
		b(consumer, Items.BIRCH_BOAT, Blocks.BIRCH_PLANKS);
		b(consumer, Items.DARK_OAK_BOAT, Blocks.DARK_OAK_PLANKS);
		b(consumer, Items.JUNGLE_BOAT, Blocks.JUNGLE_PLANKS);
		b(consumer, Items.OAK_BOAT, Blocks.OAK_PLANKS);
		b(consumer, Items.SPRUCE_BOAT, Blocks.SPRUCE_PLANKS);
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
		jh.a(Blocks.ACTIVATOR_RAIL, 6).a('#', Blocks.REDSTONE_TORCH).a('S', Items.STICK).a('X', Items.IRON_INGOT).a("XSX").a("X#X").a("XSX").a("has_rail", a(Blocks.RAIL)).a(consumer);
		ji.a(Blocks.ANDESITE, 2).b(Blocks.DIORITE).b(Blocks.COBBLESTONE).a("has_stone", a(Blocks.DIORITE)).a(consumer);
		jh.a(Blocks.ANVIL).a('I', Blocks.IRON_BLOCK).a('i', Items.IRON_INGOT).a("III").a(" i ").a("iii").a("has_iron_block", a(Blocks.IRON_BLOCK)).a(consumer);
		jh.a(Items.ARMOR_STAND).a('/', Items.STICK).a('_', Blocks.SMOOTH_STONE_SLAB).a("///").a(" / ").a("/_/").a("has_stone_slab", a(Blocks.SMOOTH_STONE_SLAB)).a(consumer);
		jh.a(Items.ARROW, 4).a('#', Items.STICK).a('X', Items.FLINT).a('Y', Items.FEATHER).a("X").a("#").a("Y").a("has_feather", a(Items.FEATHER)).a("has_flint", a(Items.FLINT)).a(consumer);
		jh.a(Blocks.BARREL, 1).a('P', TagsItem.PLANKS).a('S', TagsItem.WOODEN_SLABS).a("PSP").a("P P").a("PSP").a("has_planks", a(TagsItem.PLANKS)).a("has_wood_slab", a(TagsItem.WOODEN_SLABS)).a(consumer);
		jh.a(Blocks.BEACON).a('S', Items.NETHER_STAR).a('G', Blocks.GLASS).a('O', Blocks.OBSIDIAN).a("GGG").a("GSG").a("OOO").a("has_nether_star", a(Items.NETHER_STAR)).a(consumer);
		jh.a(Blocks.BEEHIVE).a('P', TagsItem.PLANKS).a('H', Items.HONEYCOMB).a("PPP").a("HHH").a("PPP").a("has_honeycomb", a(Items.HONEYCOMB)).a(consumer);
		ji.a(Items.BEETROOT_SOUP).b(Items.BOWL).b(Items.BEETROOT, 6).a("has_beetroot", a(Items.BEETROOT)).a(consumer);
		ji.a(Items.BLACK_DYE).b(Items.INK_SAC).a("black_dye").a("has_ink_sac", a(Items.INK_SAC)).a(consumer);
		ji.a(Items.BLACK_DYE).b(Blocks.WITHER_ROSE).a("black_dye").a("has_black_flower", a(Blocks.WITHER_ROSE)).a(consumer, "black_dye_from_wither_rose");
		ji.a(Items.BLAZE_POWDER, 2).b(Items.BLAZE_ROD).a("has_blaze_rod", a(Items.BLAZE_ROD)).a(consumer);
		ji.a(Items.BLUE_DYE).b(Items.LAPIS_LAZULI).a("blue_dye").a("has_lapis_lazuli", a(Items.LAPIS_LAZULI)).a(consumer);
		ji.a(Items.BLUE_DYE).b(Blocks.CORNFLOWER).a("blue_dye").a("has_blue_flower", a(Blocks.CORNFLOWER)).a(consumer, "blue_dye_from_cornflower");
		jh.a(Blocks.BLUE_ICE).a('#', Blocks.PACKED_ICE).a("###").a("###").a("###").a("has_packed_ice", a(Blocks.PACKED_ICE)).a(consumer);
		jh.a(Blocks.BONE_BLOCK).a('X', Items.BONE_MEAL).a("XXX").a("XXX").a("XXX").a("has_bonemeal", a(Items.BONE_MEAL)).a(consumer);
		ji.a(Items.BONE_MEAL, 3).b(Items.BONE).a("bonemeal").a("has_bone", a(Items.BONE)).a(consumer);
		ji.a(Items.BONE_MEAL, 9).b(Blocks.BONE_BLOCK).a("bonemeal").a("has_bone_block", a(Blocks.BONE_BLOCK)).a(consumer, "bone_meal_from_bone_block");
		ji.a(Items.BOOK).b(Items.PAPER, 3).b(Items.LEATHER).a("has_paper", a(Items.PAPER)).a(consumer);
		jh.a(Blocks.BOOKSHELF).a('#', TagsItem.PLANKS).a('X', Items.BOOK).a("###").a("XXX").a("###").a("has_book", a(Items.BOOK)).a(consumer);
		jh.a(Items.BOW).a('#', Items.STICK).a('X', Items.STRING).a(" #X").a("# X").a(" #X").a("has_string", a(Items.STRING)).a(consumer);
		jh.a(Items.BOWL, 4).a('#', TagsItem.PLANKS).a("# #").a(" # ").a("has_brown_mushroom", a(Blocks.BROWN_MUSHROOM)).a("has_red_mushroom", a(Blocks.RED_MUSHROOM)).a("has_mushroom_stew", a(Items.MUSHROOM_STEW)).a(consumer);
		jh.a(Items.BREAD).a('#', Items.WHEAT).a("###").a("has_wheat", a(Items.WHEAT)).a(consumer);
		jh.a(Blocks.BREWING_STAND).a('B', Items.BLAZE_ROD).a('#', TagsItem.STONE_CRAFTING_MATERIALS).a(" B ").a("###").a("has_blaze_rod", a(Items.BLAZE_ROD)).a(consumer);
		jh.a(Blocks.BRICKS).a('#', Items.BRICK).a("##").a("##").a("has_brick", a(Items.BRICK)).a(consumer);
		jh.a(Blocks.BRICK_SLAB, 6).a('#', Blocks.BRICKS).a("###").a("has_brick_block", a(Blocks.BRICKS)).a(consumer);
		jh.a(Blocks.BRICK_STAIRS, 4).a('#', Blocks.BRICKS).a("#  ").a("## ").a("###").a("has_brick_block", a(Blocks.BRICKS)).a(consumer);
		ji.a(Items.BROWN_DYE).b(Items.COCOA_BEANS).a("brown_dye").a("has_cocoa_beans", a(Items.COCOA_BEANS)).a(consumer);
		jh.a(Items.BUCKET).a('#', Items.IRON_INGOT).a("# #").a(" # ").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.CAKE).a('A', Items.MILK_BUCKET).a('B', Items.SUGAR).a('C', Items.WHEAT).a('E', Items.EGG).a("AAA").a("BEB").a("CCC").a("has_egg", a(Items.EGG)).a(consumer);
		jh.a(Blocks.CAMPFIRE).a('L', TagsItem.LOGS).a('S', Items.STICK).a('C', TagsItem.COALS).a(" S ").a("SCS").a("LLL").a("has_stick", a(Items.STICK)).a("has_coal", a(TagsItem.COALS)).a(consumer);
		jh.a(Items.CARROT_ON_A_STICK).a('#', Items.FISHING_ROD).a('X', Items.CARROT).a("# ").a(" X").a("has_carrot", a(Items.CARROT)).a(consumer);
		jh.a(Items.WARPED_FUNGUS_ON_A_STICK).a('#', Items.FISHING_ROD).a('X', Items.bx).a("# ").a(" X").a("has_warped_fungus", a(Items.bx)).a(consumer);
		jh.a(Blocks.CAULDRON).a('#', Items.IRON_INGOT).a("# #").a("# #").a("###").a("has_water_bucket", a(Items.WATER_BUCKET)).a(consumer);
		jh.a(Blocks.COMPOSTER).a('#', TagsItem.WOODEN_SLABS).a("# #").a("# #").a("###").a("has_wood_slab", a(TagsItem.WOODEN_SLABS)).a(consumer);
		jh.a(Blocks.CHEST).a('#', TagsItem.PLANKS).a("###").a("# #").a("###").a("has_lots_of_items", new CriterionTriggerInventoryChanged.a(CriterionConditionEntity.b_.a, CriterionConditionValue.IntegerRange.b(10), CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, new CriterionConditionItem[0])).a(consumer);
		jh.a(Items.CHEST_MINECART).a('A', Blocks.CHEST).a('B', Items.MINECART).a("A").a("B").a("has_minecart", a(Items.MINECART)).a(consumer);
		jh.a(Blocks.CHISELED_NETHER_BRICKS).a('#', Blocks.NETHER_BRICK_SLAB).a("#").a("#").a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.CHISELED_QUARTZ_BLOCK).a('#', Blocks.QUARTZ_SLAB).a("#").a("#").a("has_chiseled_quartz_block", a(Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", a(Blocks.QUARTZ_PILLAR)).a(consumer);
		jh.a(Blocks.CHISELED_STONE_BRICKS).a('#', Blocks.STONE_BRICK_SLAB).a("#").a("#").a("has_stone_bricks", a(TagsItem.STONE_BRICKS)).a(consumer);
		jh.a(Blocks.CLAY).a('#', Items.CLAY_BALL).a("##").a("##").a("has_clay_ball", a(Items.CLAY_BALL)).a(consumer);
		jh.a(Items.CLOCK).a('#', Items.GOLD_INGOT).a('X', Items.REDSTONE).a(" # ").a("#X#").a(" # ").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		ji.a(Items.COAL, 9).b(Blocks.COAL_BLOCK).a("has_coal_block", a(Blocks.COAL_BLOCK)).a(consumer);
		jh.a(Blocks.COAL_BLOCK).a('#', Items.COAL).a("###").a("###").a("###").a("has_coal", a(Items.COAL)).a(consumer);
		jh.a(Blocks.COARSE_DIRT, 4).a('D', Blocks.DIRT).a('G', Blocks.GRAVEL).a("DG").a("GD").a("has_gravel", a(Blocks.GRAVEL)).a(consumer);
		jh.a(Blocks.COBBLESTONE_SLAB, 6).a('#', Blocks.COBBLESTONE).a("###").a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer);
		jh.a(Blocks.COBBLESTONE_WALL, 6).a('#', Blocks.COBBLESTONE).a("###").a("###").a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer);
		jh.a(Blocks.COMPARATOR).a('#', Blocks.REDSTONE_TORCH).a('X', Items.QUARTZ).a('I', Blocks.STONE).a(" # ").a("#X#").a("III").a("has_quartz", a(Items.QUARTZ)).a(consumer);
		jh.a(Items.COMPASS).a('#', Items.IRON_INGOT).a('X', Items.REDSTONE).a(" # ").a("#X#").a(" # ").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		jh.a(Items.COOKIE, 8).a('#', Items.WHEAT).a('X', Items.COCOA_BEANS).a("#X#").a("has_cocoa", a(Items.COCOA_BEANS)).a(consumer);
		jh.a(Blocks.CRAFTING_TABLE).a('#', TagsItem.PLANKS).a("##").a("##").a("has_planks", a(TagsItem.PLANKS)).a(consumer);
		jh.a(Items.CROSSBOW).a('~', Items.STRING).a('#', Items.STICK).a('&', Items.IRON_INGOT).a('$', Blocks.TRIPWIRE_HOOK).a("#&#").a("~$~").a(" # ").a("has_string", a(Items.STRING)).a("has_stick", a(Items.STICK)).a("has_iron_ingot", a(Items.IRON_INGOT)).a("has_tripwire_hook", a(Blocks.TRIPWIRE_HOOK)).a(consumer);
		jh.a(Blocks.LOOM).a('#', TagsItem.PLANKS).a('@', Items.STRING).a("@@").a("##").a("has_string", a(Items.STRING)).a(consumer);
		jh.a(Blocks.CHISELED_RED_SANDSTONE).a('#', Blocks.RED_SANDSTONE_SLAB).a("#").a("#").a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", a(Blocks.CHISELED_RED_SANDSTONE)).a("has_cut_red_sandstone", a(Blocks.CUT_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.CHISELED_SANDSTONE).a('#', Blocks.SANDSTONE_SLAB).a("#").a("#").a("has_stone_slab", a(Blocks.SANDSTONE_SLAB)).a(consumer);
		ji.a(Items.CYAN_DYE, 2).b(Items.BLUE_DYE).b(Items.GREEN_DYE).a("has_green_dye", a(Items.GREEN_DYE)).a("has_blue_dye", a(Items.BLUE_DYE)).a(consumer);
		jh.a(Blocks.DARK_PRISMARINE).a('S', Items.PRISMARINE_SHARD).a('I', Items.BLACK_DYE).a("SSS").a("SIS").a("SSS").a("has_prismarine_shard", a(Items.PRISMARINE_SHARD)).a(consumer);
		jh.a(Blocks.PRISMARINE_STAIRS, 4).a('#', Blocks.PRISMARINE).a("#  ").a("## ").a("###").a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer);
		jh.a(Blocks.PRISMARINE_BRICK_STAIRS, 4).a('#', Blocks.PRISMARINE_BRICKS).a("#  ").a("## ").a("###").a("has_prismarine_bricks", a(Blocks.PRISMARINE_BRICKS)).a(consumer);
		jh.a(Blocks.DARK_PRISMARINE_STAIRS, 4).a('#', Blocks.DARK_PRISMARINE).a("#  ").a("## ").a("###").a("has_dark_prismarine", a(Blocks.DARK_PRISMARINE)).a(consumer);
		jh.a(Blocks.DAYLIGHT_DETECTOR).a('Q', Items.QUARTZ).a('G', Blocks.GLASS).a('W', RecipeItemStack.a(TagsItem.WOODEN_SLABS)).a("GGG").a("QQQ").a("WWW").a("has_quartz", a(Items.QUARTZ)).a(consumer);
		jh.a(Blocks.DETECTOR_RAIL, 6).a('R', Items.REDSTONE).a('#', Blocks.STONE_PRESSURE_PLATE).a('X', Items.IRON_INGOT).a("X X").a("X#X").a("XRX").a("has_rail", a(Blocks.RAIL)).a(consumer);
		ji.a(Items.DIAMOND, 9).b(Blocks.DIAMOND_BLOCK).a("has_diamond_block", a(Blocks.DIAMOND_BLOCK)).a(consumer);
		jh.a(Items.DIAMOND_AXE).a('#', Items.STICK).a('X', Items.DIAMOND).a("XX").a("X#").a(" #").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Blocks.DIAMOND_BLOCK).a('#', Items.DIAMOND).a("###").a("###").a("###").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_BOOTS).a('X', Items.DIAMOND).a("X X").a("X X").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_CHESTPLATE).a('X', Items.DIAMOND).a("X X").a("XXX").a("XXX").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_HELMET).a('X', Items.DIAMOND).a("XXX").a("X X").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_HOE).a('#', Items.STICK).a('X', Items.DIAMOND).a("XX").a(" #").a(" #").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_LEGGINGS).a('X', Items.DIAMOND).a("XXX").a("X X").a("X X").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_PICKAXE).a('#', Items.STICK).a('X', Items.DIAMOND).a("XXX").a(" # ").a(" # ").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_SHOVEL).a('#', Items.STICK).a('X', Items.DIAMOND).a("X").a("#").a("#").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Items.DIAMOND_SWORD).a('#', Items.STICK).a('X', Items.DIAMOND).a("X").a("X").a("#").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Blocks.DIORITE, 2).a('Q', Items.QUARTZ).a('C', Blocks.COBBLESTONE).a("CQ").a("QC").a("has_quartz", a(Items.QUARTZ)).a(consumer);
		jh.a(Blocks.DISPENSER).a('R', Items.REDSTONE).a('#', Blocks.COBBLESTONE).a('X', Items.BOW).a("###").a("#X#").a("#R#").a("has_bow", a(Items.BOW)).a(consumer);
		jh.a(Blocks.DROPPER).a('R', Items.REDSTONE).a('#', Blocks.COBBLESTONE).a("###").a("# #").a("#R#").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		ji.a(Items.EMERALD, 9).b(Blocks.EMERALD_BLOCK).a("has_emerald_block", a(Blocks.EMERALD_BLOCK)).a(consumer);
		jh.a(Blocks.EMERALD_BLOCK).a('#', Items.EMERALD).a("###").a("###").a("###").a("has_emerald", a(Items.EMERALD)).a(consumer);
		jh.a(Blocks.ENCHANTING_TABLE).a('B', Items.BOOK).a('#', Blocks.OBSIDIAN).a('D', Items.DIAMOND).a(" B ").a("D#D").a("###").a("has_obsidian", a(Blocks.OBSIDIAN)).a(consumer);
		jh.a(Blocks.ENDER_CHEST).a('#', Blocks.OBSIDIAN).a('E', Items.ENDER_EYE).a("###").a("#E#").a("###").a("has_ender_eye", a(Items.ENDER_EYE)).a(consumer);
		ji.a(Items.ENDER_EYE).b(Items.ENDER_PEARL).b(Items.BLAZE_POWDER).a("has_blaze_powder", a(Items.BLAZE_POWDER)).a(consumer);
		jh.a(Blocks.END_STONE_BRICKS, 4).a('#', Blocks.END_STONE).a("##").a("##").a("has_end_stone", a(Blocks.END_STONE)).a(consumer);
		jh.a(Items.END_CRYSTAL).a('T', Items.GHAST_TEAR).a('E', Items.ENDER_EYE).a('G', Blocks.GLASS).a("GGG").a("GEG").a("GTG").a("has_ender_eye", a(Items.ENDER_EYE)).a(consumer);
		jh.a(Blocks.END_ROD, 4).a('#', Items.POPPED_CHORUS_FRUIT).a('/', Items.BLAZE_ROD).a("/").a("#").a("has_chorus_fruit_popped", a(Items.POPPED_CHORUS_FRUIT)).a(consumer);
		ji.a(Items.FERMENTED_SPIDER_EYE).b(Items.SPIDER_EYE).b(Blocks.BROWN_MUSHROOM).b(Items.SUGAR).a("has_spider_eye", a(Items.SPIDER_EYE)).a(consumer);
		ji.a(Items.FIRE_CHARGE, 3).b(Items.GUNPOWDER).b(Items.BLAZE_POWDER).a(RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a("has_blaze_powder", a(Items.BLAZE_POWDER)).a(consumer);
		jh.a(Items.FISHING_ROD).a('#', Items.STICK).a('X', Items.STRING).a("  #").a(" #X").a("# X").a("has_string", a(Items.STRING)).a(consumer);
		ji.a(Items.FLINT_AND_STEEL).b(Items.IRON_INGOT).b(Items.FLINT).a("has_flint", a(Items.FLINT)).a("has_obsidian", a(Blocks.OBSIDIAN)).a(consumer);
		jh.a(Blocks.FLOWER_POT).a('#', Items.BRICK).a("# #").a(" # ").a("has_brick", a(Items.BRICK)).a(consumer);
		jh.a(Blocks.FURNACE).a('#', TagsItem.STONE_CRAFTING_MATERIALS).a("###").a("# #").a("###").a("has_cobblestone", a(TagsItem.STONE_CRAFTING_MATERIALS)).a(consumer);
		jh.a(Items.FURNACE_MINECART).a('A', Blocks.FURNACE).a('B', Items.MINECART).a("A").a("B").a("has_minecart", a(Items.MINECART)).a(consumer);
		jh.a(Items.GLASS_BOTTLE, 3).a('#', Blocks.GLASS).a("# #").a(" # ").a("has_glass", a(Blocks.GLASS)).a(consumer);
		jh.a(Blocks.GLASS_PANE, 16).a('#', Blocks.GLASS).a("###").a("###").a("has_glass", a(Blocks.GLASS)).a(consumer);
		jh.a(Blocks.GLOWSTONE).a('#', Items.GLOWSTONE_DUST).a("##").a("##").a("has_glowstone_dust", a(Items.GLOWSTONE_DUST)).a(consumer);
		jh.a(Items.GOLDEN_APPLE).a('#', Items.GOLD_INGOT).a('X', Items.APPLE).a("###").a("#X#").a("###").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_AXE).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("XX").a("X#").a(" #").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_BOOTS).a('X', Items.GOLD_INGOT).a("X X").a("X X").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_CARROT).a('#', Items.GOLD_NUGGET).a('X', Items.CARROT).a("###").a("#X#").a("###").a("has_gold_nugget", a(Items.GOLD_NUGGET)).a(consumer);
		jh.a(Items.GOLDEN_CHESTPLATE).a('X', Items.GOLD_INGOT).a("X X").a("XXX").a("XXX").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_HELMET).a('X', Items.GOLD_INGOT).a("XXX").a("X X").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_HOE).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("XX").a(" #").a(" #").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_LEGGINGS).a('X', Items.GOLD_INGOT).a("XXX").a("X X").a("X X").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_PICKAXE).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("XXX").a(" # ").a(" # ").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Blocks.POWERED_RAIL, 6).a('R', Items.REDSTONE).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("X X").a("X#X").a("XRX").a("has_rail", a(Blocks.RAIL)).a(consumer);
		jh.a(Items.GOLDEN_SHOVEL).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("X").a("#").a("#").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Items.GOLDEN_SWORD).a('#', Items.STICK).a('X', Items.GOLD_INGOT).a("X").a("X").a("#").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		jh.a(Blocks.GOLD_BLOCK).a('#', Items.GOLD_INGOT).a("###").a("###").a("###").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		ji.a(Items.GOLD_INGOT, 9).b(Blocks.GOLD_BLOCK).a("gold_ingot").a("has_gold_block", a(Blocks.GOLD_BLOCK)).a(consumer, "gold_ingot_from_gold_block");
		jh.a(Items.GOLD_INGOT).a('#', Items.GOLD_NUGGET).a("###").a("###").a("###").b("gold_ingot").a("has_gold_nugget", a(Items.GOLD_NUGGET)).a(consumer, "gold_ingot_from_nuggets");
		ji.a(Items.GOLD_NUGGET, 9).b(Items.GOLD_INGOT).a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		ji.a(Blocks.GRANITE).b(Blocks.DIORITE).b(Items.QUARTZ).a("has_quartz", a(Items.QUARTZ)).a(consumer);
		ji.a(Items.GRAY_DYE, 2).b(Items.BLACK_DYE).b(Items.WHITE_DYE).a("has_white_dye", a(Items.WHITE_DYE)).a("has_black_dye", a(Items.BLACK_DYE)).a(consumer);
		jh.a(Blocks.HAY_BLOCK).a('#', Items.WHEAT).a("###").a("###").a("###").a("has_wheat", a(Items.WHEAT)).a(consumer);
		jh.a(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).a('#', Items.IRON_INGOT).a("##").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		ji.a(Items.HONEY_BOTTLE, 4).b(Items.ru).b(Items.GLASS_BOTTLE, 4).a("has_honey_block", a(Blocks.HONEY_BLOCK)).a(consumer);
		jh.a(Blocks.HONEY_BLOCK, 1).a('S', Items.HONEY_BOTTLE).a("SS").a("SS").a("has_honey_bottle", a(Items.HONEY_BOTTLE)).a(consumer);
		jh.a(Blocks.HONEYCOMB_BLOCK).a('H', Items.HONEYCOMB).a("HH").a("HH").a("has_honeycomb", a(Items.HONEYCOMB)).a(consumer);
		jh.a(Blocks.HOPPER).a('C', Blocks.CHEST).a('I', Items.IRON_INGOT).a("I I").a("ICI").a(" I ").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.HOPPER_MINECART).a('A', Blocks.HOPPER).a('B', Items.MINECART).a("A").a("B").a("has_minecart", a(Items.MINECART)).a(consumer);
		jh.a(Items.IRON_AXE).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("XX").a("X#").a(" #").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.IRON_BARS, 16).a('#', Items.IRON_INGOT).a("###").a("###").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.IRON_BLOCK).a('#', Items.IRON_INGOT).a("###").a("###").a("###").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_BOOTS).a('X', Items.IRON_INGOT).a("X X").a("X X").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_CHESTPLATE).a('X', Items.IRON_INGOT).a("X X").a("XXX").a("XXX").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.IRON_DOOR, 3).a('#', Items.IRON_INGOT).a("##").a("##").a("##").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_HELMET).a('X', Items.IRON_INGOT).a("XXX").a("X X").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_HOE).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("XX").a(" #").a(" #").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		ji.a(Items.IRON_INGOT, 9).b(Blocks.IRON_BLOCK).a("iron_ingot").a("has_iron_block", a(Blocks.IRON_BLOCK)).a(consumer, "iron_ingot_from_iron_block");
		jh.a(Items.IRON_INGOT).a('#', Items.IRON_NUGGET).a("###").a("###").a("###").b("iron_ingot").a("has_iron_nugget", a(Items.IRON_NUGGET)).a(consumer, "iron_ingot_from_nuggets");
		jh.a(Items.IRON_LEGGINGS).a('X', Items.IRON_INGOT).a("XXX").a("X X").a("X X").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		ji.a(Items.IRON_NUGGET, 9).b(Items.IRON_INGOT).a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_PICKAXE).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("XXX").a(" # ").a(" # ").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_SHOVEL).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("X").a("#").a("#").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.IRON_SWORD).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("X").a("X").a("#").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.IRON_TRAPDOOR).a('#', Items.IRON_INGOT).a("##").a("##").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.ITEM_FRAME).a('#', Items.STICK).a('X', Items.LEATHER).a("###").a("#X#").a("###").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Blocks.JUKEBOX).a('#', TagsItem.PLANKS).a('X', Items.DIAMOND).a("###").a("#X#").a("###").a("has_diamond", a(Items.DIAMOND)).a(consumer);
		jh.a(Blocks.LADDER, 3).a('#', Items.STICK).a("# #").a("###").a("# #").a("has_stick", a(Items.STICK)).a(consumer);
		jh.a(Blocks.LAPIS_BLOCK).a('#', Items.LAPIS_LAZULI).a("###").a("###").a("###").a("has_lapis", a(Items.LAPIS_LAZULI)).a(consumer);
		ji.a(Items.LAPIS_LAZULI, 9).b(Blocks.LAPIS_BLOCK).a("has_lapis_block", a(Blocks.LAPIS_BLOCK)).a(consumer);
		jh.a(Items.LEAD, 2).a('~', Items.STRING).a('O', Items.SLIME_BALL).a("~~ ").a("~O ").a("  ~").a("has_slime_ball", a(Items.SLIME_BALL)).a(consumer);
		jh.a(Items.LEATHER).a('#', Items.RABBIT_HIDE).a("##").a("##").a("has_rabbit_hide", a(Items.RABBIT_HIDE)).a(consumer);
		jh.a(Items.LEATHER_BOOTS).a('X', Items.LEATHER).a("X X").a("X X").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Items.LEATHER_CHESTPLATE).a('X', Items.LEATHER).a("X X").a("XXX").a("XXX").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Items.LEATHER_HELMET).a('X', Items.LEATHER).a("XXX").a("X X").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Items.LEATHER_LEGGINGS).a('X', Items.LEATHER).a("XXX").a("X X").a("X X").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Items.LEATHER_HORSE_ARMOR).a('X', Items.LEATHER).a("X X").a("XXX").a("X X").a("has_leather", a(Items.LEATHER)).a(consumer);
		jh.a(Blocks.LECTERN).a('S', TagsItem.WOODEN_SLABS).a('B', Blocks.BOOKSHELF).a("SSS").a(" B ").a(" S ").a("has_book", a(Items.BOOK)).a(consumer);
		jh.a(Blocks.LEVER).a('#', Blocks.COBBLESTONE).a('X', Items.STICK).a("X").a("#").a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer);
		ji.a(Items.LIGHT_BLUE_DYE).b(Blocks.BLUE_ORCHID).a("light_blue_dye").a("has_red_flower", a(Blocks.BLUE_ORCHID)).a(consumer, "light_blue_dye_from_blue_orchid");
		ji.a(Items.LIGHT_BLUE_DYE, 2).b(Items.BLUE_DYE).b(Items.WHITE_DYE).a("light_blue_dye").a("has_blue_dye", a(Items.BLUE_DYE)).a("has_white_dye", a(Items.WHITE_DYE)).a(consumer, "light_blue_dye_from_blue_white_dye");
		ji.a(Items.LIGHT_GRAY_DYE).b(Blocks.AZURE_BLUET).a("light_gray_dye").a("has_red_flower", a(Blocks.AZURE_BLUET)).a(consumer, "light_gray_dye_from_azure_bluet");
		ji.a(Items.LIGHT_GRAY_DYE, 2).b(Items.GRAY_DYE).b(Items.WHITE_DYE).a("light_gray_dye").a("has_gray_dye", a(Items.GRAY_DYE)).a("has_white_dye", a(Items.WHITE_DYE)).a(consumer, "light_gray_dye_from_gray_white_dye");
		ji.a(Items.LIGHT_GRAY_DYE, 3).b(Items.BLACK_DYE).b(Items.WHITE_DYE, 2).a("light_gray_dye").a("has_white_dye", a(Items.WHITE_DYE)).a("has_black_dye", a(Items.BLACK_DYE)).a(consumer, "light_gray_dye_from_black_white_dye");
		ji.a(Items.LIGHT_GRAY_DYE).b(Blocks.OXEYE_DAISY).a("light_gray_dye").a("has_red_flower", a(Blocks.OXEYE_DAISY)).a(consumer, "light_gray_dye_from_oxeye_daisy");
		ji.a(Items.LIGHT_GRAY_DYE).b(Blocks.WHITE_TULIP).a("light_gray_dye").a("has_red_flower", a(Blocks.WHITE_TULIP)).a(consumer, "light_gray_dye_from_white_tulip");
		jh.a(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE).a('#', Items.GOLD_INGOT).a("##").a("has_gold_ingot", a(Items.GOLD_INGOT)).a(consumer);
		ji.a(Items.LIME_DYE, 2).b(Items.GREEN_DYE).b(Items.WHITE_DYE).a("has_green_dye", a(Items.GREEN_DYE)).a("has_white_dye", a(Items.WHITE_DYE)).a(consumer);
		jh.a(Blocks.JACK_O_LANTERN).a('A', Blocks.CARVED_PUMPKIN).a('B', Blocks.TORCH).a("A").a("B").a("has_carved_pumpkin", a(Blocks.CARVED_PUMPKIN)).a(consumer);
		ji.a(Items.MAGENTA_DYE).b(Blocks.ALLIUM).a("magenta_dye").a("has_red_flower", a(Blocks.ALLIUM)).a(consumer, "magenta_dye_from_allium");
		ji.a(Items.MAGENTA_DYE, 4).b(Items.BLUE_DYE).b(Items.RED_DYE, 2).b(Items.WHITE_DYE).a("magenta_dye").a("has_blue_dye", a(Items.BLUE_DYE)).a("has_rose_red", a(Items.RED_DYE)).a("has_white_dye", a(Items.WHITE_DYE)).a(consumer, "magenta_dye_from_blue_red_white_dye");
		ji.a(Items.MAGENTA_DYE, 3).b(Items.BLUE_DYE).b(Items.RED_DYE).b(Items.PINK_DYE).a("magenta_dye").a("has_pink_dye", a(Items.PINK_DYE)).a("has_blue_dye", a(Items.BLUE_DYE)).a("has_red_dye", a(Items.RED_DYE)).a(consumer, "magenta_dye_from_blue_red_pink");
		ji.a(Items.MAGENTA_DYE, 2).b(Blocks.LILAC).a("magenta_dye").a("has_double_plant", a(Blocks.LILAC)).a(consumer, "magenta_dye_from_lilac");
		ji.a(Items.MAGENTA_DYE, 2).b(Items.PURPLE_DYE).b(Items.PINK_DYE).a("magenta_dye").a("has_pink_dye", a(Items.PINK_DYE)).a("has_purple_dye", a(Items.PURPLE_DYE)).a(consumer, "magenta_dye_from_purple_and_pink");
		jh.a(Blocks.MAGMA_BLOCK).a('#', Items.MAGMA_CREAM).a("##").a("##").a("has_magma_cream", a(Items.MAGMA_CREAM)).a(consumer);
		ji.a(Items.MAGMA_CREAM).b(Items.BLAZE_POWDER).b(Items.SLIME_BALL).a("has_blaze_powder", a(Items.BLAZE_POWDER)).a(consumer);
		jh.a(Items.MAP).a('#', Items.PAPER).a('X', Items.COMPASS).a("###").a("#X#").a("###").a("has_compass", a(Items.COMPASS)).a(consumer);
		jh.a(Blocks.MELON).a('M', Items.MELON_SLICE).a("MMM").a("MMM").a("MMM").a("has_melon", a(Items.MELON_SLICE)).a(consumer);
		ji.a(Items.MELON_SEEDS).b(Items.MELON_SLICE).a("has_melon", a(Items.MELON_SLICE)).a(consumer);
		jh.a(Items.MINECART).a('#', Items.IRON_INGOT).a("# #").a("###").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		ji.a(Blocks.MOSSY_COBBLESTONE).b(Blocks.COBBLESTONE).b(Blocks.VINE).a("has_vine", a(Blocks.VINE)).a(consumer);
		jh.a(Blocks.MOSSY_COBBLESTONE_WALL, 6).a('#', Blocks.MOSSY_COBBLESTONE).a("###").a("###").a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer);
		ji.a(Blocks.MOSSY_STONE_BRICKS).b(Blocks.STONE_BRICKS).b(Blocks.VINE).a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer);
		ji.a(Items.MUSHROOM_STEW).b(Blocks.BROWN_MUSHROOM).b(Blocks.RED_MUSHROOM).b(Items.BOWL).a("has_mushroom_stew", a(Items.MUSHROOM_STEW)).a("has_bowl", a(Items.BOWL)).a("has_brown_mushroom", a(Blocks.BROWN_MUSHROOM)).a("has_red_mushroom", a(Blocks.RED_MUSHROOM)).a(consumer);
		jh.a(Blocks.NETHER_BRICKS).a('N', Items.NETHER_BRICK).a("NN").a("NN").a("has_netherbrick", a(Items.NETHER_BRICK)).a(consumer);
		jh.a(Blocks.NETHER_BRICK_FENCE, 6).a('#', Blocks.NETHER_BRICKS).a('-', Items.NETHER_BRICK).a("#-#").a("#-#").a("has_nether_brick", a(Blocks.NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.NETHER_BRICK_SLAB, 6).a('#', Blocks.NETHER_BRICKS).a("###").a("has_nether_brick", a(Blocks.NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.NETHER_BRICK_STAIRS, 4).a('#', Blocks.NETHER_BRICKS).a("#  ").a("## ").a("###").a("has_nether_brick", a(Blocks.NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.NETHER_WART_BLOCK).a('#', Items.NETHER_WART).a("###").a("###").a("###").a("has_nether_wart", a(Items.NETHER_WART)).a(consumer);
		jh.a(Blocks.NOTE_BLOCK).a('#', TagsItem.PLANKS).a('X', Items.REDSTONE).a("###").a("#X#").a("###").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		jh.a(Blocks.OBSERVER).a('Q', Items.QUARTZ).a('R', Items.REDSTONE).a('#', Blocks.COBBLESTONE).a("###").a("RRQ").a("###").a("has_quartz", a(Items.QUARTZ)).a(consumer);
		ji.a(Items.ORANGE_DYE).b(Blocks.ORANGE_TULIP).a("orange_dye").a("has_red_flower", a(Blocks.ORANGE_TULIP)).a(consumer, "orange_dye_from_orange_tulip");
		ji.a(Items.ORANGE_DYE, 2).b(Items.RED_DYE).b(Items.YELLOW_DYE).a("orange_dye").a("has_red_dye", a(Items.RED_DYE)).a("has_yellow_dye", a(Items.YELLOW_DYE)).a(consumer, "orange_dye_from_red_yellow");
		jh.a(Items.PAINTING).a('#', Items.STICK).a('X', RecipeItemStack.a(TagsItem.WOOL)).a("###").a("#X#").a("###").a("has_wool", a(TagsItem.WOOL)).a(consumer);
		jh.a(Items.PAPER, 3).a('#', Blocks.SUGAR_CANE).a("###").a("has_reeds", a(Blocks.SUGAR_CANE)).a(consumer);
		jh.a(Blocks.QUARTZ_PILLAR, 2).a('#', Blocks.QUARTZ_BLOCK).a("#").a("#").a("has_chiseled_quartz_block", a(Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", a(Blocks.QUARTZ_PILLAR)).a(consumer);
		ji.a(Blocks.PACKED_ICE).b(Blocks.ICE, 9).a("has_ice", a(Blocks.ICE)).a(consumer);
		ji.a(Items.PINK_DYE, 2).b(Blocks.PEONY).a("pink_dye").a("has_double_plant", a(Blocks.PEONY)).a(consumer, "pink_dye_from_peony");
		ji.a(Items.PINK_DYE).b(Blocks.PINK_TULIP).a("pink_dye").a("has_red_flower", a(Blocks.PINK_TULIP)).a(consumer, "pink_dye_from_pink_tulip");
		ji.a(Items.PINK_DYE, 2).b(Items.RED_DYE).b(Items.WHITE_DYE).a("pink_dye").a("has_white_dye", a(Items.WHITE_DYE)).a("has_red_dye", a(Items.RED_DYE)).a(consumer, "pink_dye_from_red_white_dye");
		jh.a(Blocks.PISTON).a('R', Items.REDSTONE).a('#', Blocks.COBBLESTONE).a('T', TagsItem.PLANKS).a('X', Items.IRON_INGOT).a("TTT").a("#X#").a("#R#").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		jh.a(Blocks.cP, 4).a('S', Blocks.BASALT).a("SS").a("SS").a("has_basalt", a(Blocks.BASALT)).a(consumer);
		jh.a(Blocks.POLISHED_GRANITE, 4).a('S', Blocks.GRANITE).a("SS").a("SS").a("has_stone", a(Blocks.GRANITE)).a(consumer);
		jh.a(Blocks.POLISHED_DIORITE, 4).a('S', Blocks.DIORITE).a("SS").a("SS").a("has_stone", a(Blocks.DIORITE)).a(consumer);
		jh.a(Blocks.POLISHED_ANDESITE, 4).a('S', Blocks.ANDESITE).a("SS").a("SS").a("has_stone", a(Blocks.ANDESITE)).a(consumer);
		jh.a(Blocks.PRISMARINE).a('S', Items.PRISMARINE_SHARD).a("SS").a("SS").a("has_prismarine_shard", a(Items.PRISMARINE_SHARD)).a(consumer);
		jh.a(Blocks.PRISMARINE_BRICKS).a('S', Items.PRISMARINE_SHARD).a("SSS").a("SSS").a("SSS").a("has_prismarine_shard", a(Items.PRISMARINE_SHARD)).a(consumer);
		jh.a(Blocks.PRISMARINE_SLAB, 6).a('#', Blocks.PRISMARINE).a("###").a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer);
		jh.a(Blocks.PRISMARINE_BRICK_SLAB, 6).a('#', Blocks.PRISMARINE_BRICKS).a("###").a("has_prismarine_bricks", a(Blocks.PRISMARINE_BRICKS)).a(consumer);
		jh.a(Blocks.DARK_PRISMARINE_SLAB, 6).a('#', Blocks.DARK_PRISMARINE).a("###").a("has_dark_prismarine", a(Blocks.DARK_PRISMARINE)).a(consumer);
		ji.a(Items.PUMPKIN_PIE).b(Blocks.PUMPKIN).b(Items.SUGAR).b(Items.EGG).a("has_carved_pumpkin", a(Blocks.CARVED_PUMPKIN)).a("has_pumpkin", a(Blocks.PUMPKIN)).a(consumer);
		ji.a(Items.PUMPKIN_SEEDS, 4).b(Blocks.PUMPKIN).a("has_pumpkin", a(Blocks.PUMPKIN)).a(consumer);
		ji.a(Items.PURPLE_DYE, 2).b(Items.BLUE_DYE).b(Items.RED_DYE).a("has_blue_dye", a(Items.BLUE_DYE)).a("has_red_dye", a(Items.RED_DYE)).a(consumer);
		jh.a(Blocks.SHULKER_BOX).a('#', Blocks.CHEST).a('-', Items.SHULKER_SHELL).a("-").a("#").a("-").a("has_shulker_shell", a(Items.SHULKER_SHELL)).a(consumer);
		jh.a(Blocks.PURPUR_BLOCK, 4).a('F', Items.POPPED_CHORUS_FRUIT).a("FF").a("FF").a("has_chorus_fruit_popped", a(Items.POPPED_CHORUS_FRUIT)).a(consumer);
		jh.a(Blocks.PURPUR_PILLAR).a('#', Blocks.PURPUR_SLAB).a("#").a("#").a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer);
		jh.a(Blocks.PURPUR_SLAB, 6).a('#', RecipeItemStack.a(Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR)).a("###").a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer);
		jh.a(Blocks.PURPUR_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR)).a("#  ").a("## ").a("###").a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer);
		jh.a(Blocks.QUARTZ_BLOCK).a('#', Items.QUARTZ).a("##").a("##").a("has_quartz", a(Items.QUARTZ)).a(consumer);
		jh.a(Blocks.QUARTZ_BRICKS, 4).a('#', Blocks.QUARTZ_BLOCK).a("##").a("##").a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer);
		jh.a(Blocks.QUARTZ_SLAB, 6).a('#', RecipeItemStack.a(Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR)).a("###").a("has_chiseled_quartz_block", a(Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", a(Blocks.QUARTZ_PILLAR)).a(consumer);
		jh.a(Blocks.QUARTZ_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR)).a("#  ").a("## ").a("###").a("has_chiseled_quartz_block", a(Blocks.CHISELED_QUARTZ_BLOCK)).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a("has_quartz_pillar", a(Blocks.QUARTZ_PILLAR)).a(consumer);
		ji.a(Items.RABBIT_STEW).b(Items.BAKED_POTATO).b(Items.COOKED_RABBIT).b(Items.BOWL).b(Items.CARROT).b(Blocks.BROWN_MUSHROOM).a("rabbit_stew").a("has_cooked_rabbit", a(Items.COOKED_RABBIT)).a(consumer, "rabbit_stew_from_brown_mushroom");
		ji.a(Items.RABBIT_STEW).b(Items.BAKED_POTATO).b(Items.COOKED_RABBIT).b(Items.BOWL).b(Items.CARROT).b(Blocks.RED_MUSHROOM).a("rabbit_stew").a("has_cooked_rabbit", a(Items.COOKED_RABBIT)).a(consumer, "rabbit_stew_from_red_mushroom");
		jh.a(Blocks.RAIL, 16).a('#', Items.STICK).a('X', Items.IRON_INGOT).a("X X").a("X#X").a("X X").a("has_minecart", a(Items.MINECART)).a(consumer);
		ji.a(Items.REDSTONE, 9).b(Blocks.REDSTONE_BLOCK).a("has_redstone_block", a(Blocks.REDSTONE_BLOCK)).a(consumer);
		jh.a(Blocks.REDSTONE_BLOCK).a('#', Items.REDSTONE).a("###").a("###").a("###").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		jh.a(Blocks.REDSTONE_LAMP).a('R', Items.REDSTONE).a('G', Blocks.GLOWSTONE).a(" R ").a("RGR").a(" R ").a("has_glowstone", a(Blocks.GLOWSTONE)).a(consumer);
		jh.a(Blocks.REDSTONE_TORCH).a('#', Items.STICK).a('X', Items.REDSTONE).a("X").a("#").a("has_redstone", a(Items.REDSTONE)).a(consumer);
		ji.a(Items.RED_DYE).b(Items.BEETROOT).a("red_dye").a("has_beetroot", a(Items.BEETROOT)).a(consumer, "red_dye_from_beetroot");
		ji.a(Items.RED_DYE).b(Blocks.POPPY).a("red_dye").a("has_red_flower", a(Blocks.POPPY)).a(consumer, "red_dye_from_poppy");
		ji.a(Items.RED_DYE, 2).b(Blocks.ROSE_BUSH).a("red_dye").a("has_double_plant", a(Blocks.ROSE_BUSH)).a(consumer, "red_dye_from_rose_bush");
		ji.a(Items.RED_DYE).b(Blocks.RED_TULIP).a("red_dye").a("has_red_flower", a(Blocks.RED_TULIP)).a(consumer, "red_dye_from_tulip");
		jh.a(Blocks.RED_NETHER_BRICKS).a('W', Items.NETHER_WART).a('N', Items.NETHER_BRICK).a("NW").a("WN").a("has_nether_wart", a(Items.NETHER_WART)).a(consumer);
		jh.a(Blocks.RED_SANDSTONE).a('#', Blocks.RED_SAND).a("##").a("##").a("has_sand", a(Blocks.RED_SAND)).a(consumer);
		jh.a(Blocks.RED_SANDSTONE_SLAB, 6).a('#', RecipeItemStack.a(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE)).a("###").a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", a(Blocks.CHISELED_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.CUT_RED_SANDSTONE_SLAB, 6).a('#', Blocks.CUT_RED_SANDSTONE).a("###").a("has_cut_red_sandstone", a(Blocks.CUT_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.RED_SANDSTONE_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE)).a("#  ").a("## ").a("###").a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a("has_chiseled_red_sandstone", a(Blocks.CHISELED_RED_SANDSTONE)).a("has_cut_red_sandstone", a(Blocks.CUT_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.REPEATER).a('#', Blocks.REDSTONE_TORCH).a('X', Items.REDSTONE).a('I', Blocks.STONE).a("#X#").a("III").a("has_redstone_torch", a(Blocks.REDSTONE_TORCH)).a(consumer);
		jh.a(Blocks.SANDSTONE).a('#', Blocks.SAND).a("##").a("##").a("has_sand", a(Blocks.SAND)).a(consumer);
		jh.a(Blocks.SANDSTONE_SLAB, 6).a('#', RecipeItemStack.a(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE)).a("###").a("has_sandstone", a(Blocks.SANDSTONE)).a("has_chiseled_sandstone", a(Blocks.CHISELED_SANDSTONE)).a(consumer);
		jh.a(Blocks.CUT_SANDSTONE_SLAB, 6).a('#', Blocks.CUT_SANDSTONE).a("###").a("has_cut_sandstone", a(Blocks.CUT_SANDSTONE)).a(consumer);
		jh.a(Blocks.SANDSTONE_STAIRS, 4).a('#', RecipeItemStack.a(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE)).a("#  ").a("## ").a("###").a("has_sandstone", a(Blocks.SANDSTONE)).a("has_chiseled_sandstone", a(Blocks.CHISELED_SANDSTONE)).a("has_cut_sandstone", a(Blocks.CUT_SANDSTONE)).a(consumer);
		jh.a(Blocks.SEA_LANTERN).a('S', Items.PRISMARINE_SHARD).a('C', Items.PRISMARINE_CRYSTALS).a("SCS").a("CCC").a("SCS").a("has_prismarine_crystals", a(Items.PRISMARINE_CRYSTALS)).a(consumer);
		jh.a(Items.SHEARS).a('#', Items.IRON_INGOT).a(" #").a("# ").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Items.SHIELD).a('W', TagsItem.PLANKS).a('o', Items.IRON_INGOT).a("WoW").a("WWW").a(" W ").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.SLIME_BLOCK).a('#', Items.SLIME_BALL).a("###").a("###").a("###").a("has_slime_ball", a(Items.SLIME_BALL)).a(consumer);
		ji.a(Items.SLIME_BALL, 9).b(Blocks.SLIME_BLOCK).a("has_slime", a(Blocks.SLIME_BLOCK)).a(consumer);
		jh.a(Blocks.CUT_RED_SANDSTONE, 4).a('#', Blocks.RED_SANDSTONE).a("##").a("##").a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.CUT_SANDSTONE, 4).a('#', Blocks.SANDSTONE).a("##").a("##").a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer);
		jh.a(Blocks.SNOW_BLOCK).a('#', Items.SNOWBALL).a("##").a("##").a("has_snowball", a(Items.SNOWBALL)).a(consumer);
		jh.a(Blocks.SNOW, 6).a('#', Blocks.SNOW_BLOCK).a("###").a("has_snowball", a(Items.SNOWBALL)).a(consumer);
		jh.a(Blocks.SOUL_CAMPFIRE).a('L', TagsItem.LOGS).a('S', Items.STICK).a('#', TagsItem.SOUL_FIRE_BASE_BLOCKS).a(" S ").a("S#S").a("LLL").a("has_stick", a(Items.STICK)).a("has_soul_sand", a(TagsItem.SOUL_FIRE_BASE_BLOCKS)).a(consumer);
		jh.a(Items.GLISTERING_MELON_SLICE).a('#', Items.GOLD_NUGGET).a('X', Items.MELON_SLICE).a("###").a("#X#").a("###").a("has_melon", a(Items.MELON_SLICE)).a(consumer);
		jh.a(Items.SPECTRAL_ARROW, 2).a('#', Items.GLOWSTONE_DUST).a('X', Items.ARROW).a(" # ").a("#X#").a(" # ").a("has_glowstone_dust", a(Items.GLOWSTONE_DUST)).a(consumer);
		jh.a(Items.STICK, 4).a('#', TagsItem.PLANKS).a("#").a("#").b("sticks").a("has_planks", a(TagsItem.PLANKS)).a(consumer);
		jh.a(Items.STICK, 1).a('#', Blocks.BAMBOO).a("#").a("#").b("sticks").a("has_bamboo", a(Blocks.BAMBOO)).a(consumer, "stick_from_bamboo_item");
		jh.a(Blocks.STICKY_PISTON).a('P', Blocks.PISTON).a('S', Items.SLIME_BALL).a("S").a("P").a("has_slime_ball", a(Items.SLIME_BALL)).a(consumer);
		jh.a(Blocks.STONE_BRICKS, 4).a('#', Blocks.STONE).a("##").a("##").a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Items.STONE_AXE).a('#', Items.STICK).a('X', TagsItem.STONE_TOOL_MATERIALS).a("XX").a("X#").a(" #").a("has_cobblestone", a(TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
		jh.a(Blocks.STONE_BRICK_SLAB, 6).a('#', Blocks.STONE_BRICKS).a("###").a("has_stone_bricks", a(TagsItem.STONE_BRICKS)).a(consumer);
		jh.a(Blocks.STONE_BRICK_STAIRS, 4).a('#', Blocks.STONE_BRICKS).a("#  ").a("## ").a("###").a("has_stone_bricks", a(TagsItem.STONE_BRICKS)).a(consumer);
		ji.a(Blocks.STONE_BUTTON).b(Blocks.STONE).a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Items.STONE_HOE).a('#', Items.STICK).a('X', TagsItem.STONE_TOOL_MATERIALS).a("XX").a(" #").a(" #").a("has_cobblestone", a(TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
		jh.a(Items.STONE_PICKAXE).a('#', Items.STICK).a('X', TagsItem.STONE_TOOL_MATERIALS).a("XXX").a(" # ").a(" # ").a("has_cobblestone", a(TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
		jh.a(Blocks.STONE_PRESSURE_PLATE).a('#', Blocks.STONE).a("##").a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Items.STONE_SHOVEL).a('#', Items.STICK).a('X', TagsItem.STONE_TOOL_MATERIALS).a("X").a("#").a("#").a("has_cobblestone", a(TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
		jh.a(Blocks.STONE_SLAB, 6).a('#', Blocks.STONE).a("###").a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Blocks.SMOOTH_STONE_SLAB, 6).a('#', Blocks.SMOOTH_STONE).a("###").a("has_smooth_stone", a(Blocks.SMOOTH_STONE)).a(consumer);
		jh.a(Blocks.COBBLESTONE_STAIRS, 4).a('#', Blocks.COBBLESTONE).a("#  ").a("## ").a("###").a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer);
		jh.a(Items.STONE_SWORD).a('#', Items.STICK).a('X', TagsItem.STONE_TOOL_MATERIALS).a("X").a("X").a("#").a("has_cobblestone", a(TagsItem.STONE_TOOL_MATERIALS)).a(consumer);
		jh.a(Blocks.WHITE_WOOL).a('#', Items.STRING).a("##").a("##").a("has_string", a(Items.STRING)).a(consumer, "white_wool_from_string");
		ji.a(Items.SUGAR).b(Blocks.SUGAR_CANE).a("sugar").a("has_reeds", a(Blocks.SUGAR_CANE)).a(consumer, "sugar_from_sugar_cane");
		ji.a(Items.SUGAR, 3).b(Items.HONEY_BOTTLE).a("sugar").a("has_honey_bottle", a(Items.HONEY_BOTTLE)).a(consumer, "sugar_from_honey_bottle");
		jh.a(Blocks.TARGET).a('H', Items.fL).a('R', Items.REDSTONE).a(" R ").a("RHR").a(" R ").a("has_redstone", a(Items.REDSTONE)).a("has_hay_block", a(Blocks.HAY_BLOCK)).a(consumer);
		jh.a(Blocks.TNT).a('#', RecipeItemStack.a(Blocks.SAND, Blocks.RED_SAND)).a('X', Items.GUNPOWDER).a("X#X").a("#X#").a("X#X").a("has_gunpowder", a(Items.GUNPOWDER)).a(consumer);
		jh.a(Items.TNT_MINECART).a('A', Blocks.TNT).a('B', Items.MINECART).a("A").a("B").a("has_minecart", a(Items.MINECART)).a(consumer);
		jh.a(Blocks.TORCH, 4).a('#', Items.STICK).a('X', RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a("X").a("#").a("has_stone_pickaxe", a(Items.STONE_PICKAXE)).a(consumer);
		jh.a(Blocks.SOUL_TORCH, 4).a('X', RecipeItemStack.a(Items.COAL, Items.CHARCOAL)).a('#', Items.STICK).a('S', TagsItem.SOUL_FIRE_BASE_BLOCKS).a("X").a("#").a("S").a("has_soul_sand", a(TagsItem.SOUL_FIRE_BASE_BLOCKS)).a(consumer);
		jh.a(Blocks.LANTERN).a('#', Items.cp).a('X', Items.IRON_NUGGET).a("XXX").a("X#X").a("XXX").a("has_iron_nugget", a(Items.IRON_NUGGET)).a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.SOUL_LANTERN).a('#', Items.dp).a('X', Items.IRON_NUGGET).a("XXX").a("X#X").a("XXX").a("has_soul_torch", a(Items.dp)).a(consumer);
		ji.a(Blocks.TRAPPED_CHEST).b(Blocks.CHEST).b(Blocks.TRIPWIRE_HOOK).a("has_tripwire_hook", a(Blocks.TRIPWIRE_HOOK)).a(consumer);
		jh.a(Blocks.TRIPWIRE_HOOK, 2).a('#', TagsItem.PLANKS).a('S', Items.STICK).a('I', Items.IRON_INGOT).a("I").a("S").a("#").a("has_string", a(Items.STRING)).a(consumer);
		jh.a(Items.TURTLE_HELMET).a('X', Items.SCUTE).a("XXX").a("X X").a("has_scute", a(Items.SCUTE)).a(consumer);
		ji.a(Items.WHEAT, 9).b(Blocks.HAY_BLOCK).a("has_hay_block", a(Blocks.HAY_BLOCK)).a(consumer);
		ji.a(Items.WHITE_DYE).b(Items.BONE_MEAL).a("white_dye").a("has_bone_meal", a(Items.BONE_MEAL)).a(consumer);
		ji.a(Items.WHITE_DYE).b(Blocks.LILY_OF_THE_VALLEY).a("white_dye").a("has_white_flower", a(Blocks.LILY_OF_THE_VALLEY)).a(consumer, "white_dye_from_lily_of_the_valley");
		jh.a(Items.WOODEN_AXE).a('#', Items.STICK).a('X', TagsItem.PLANKS).a("XX").a("X#").a(" #").a("has_stick", a(Items.STICK)).a(consumer);
		jh.a(Items.WOODEN_HOE).a('#', Items.STICK).a('X', TagsItem.PLANKS).a("XX").a(" #").a(" #").a("has_stick", a(Items.STICK)).a(consumer);
		jh.a(Items.WOODEN_PICKAXE).a('#', Items.STICK).a('X', TagsItem.PLANKS).a("XXX").a(" # ").a(" # ").a("has_stick", a(Items.STICK)).a(consumer);
		jh.a(Items.WOODEN_SHOVEL).a('#', Items.STICK).a('X', TagsItem.PLANKS).a("X").a("#").a("#").a("has_stick", a(Items.STICK)).a(consumer);
		jh.a(Items.WOODEN_SWORD).a('#', Items.STICK).a('X', TagsItem.PLANKS).a("X").a("X").a("#").a("has_stick", a(Items.STICK)).a(consumer);
		ji.a(Items.WRITABLE_BOOK).b(Items.BOOK).b(Items.INK_SAC).b(Items.FEATHER).a("has_book", a(Items.BOOK)).a(consumer);
		ji.a(Items.YELLOW_DYE).b(Blocks.DANDELION).a("yellow_dye").a("has_yellow_flower", a(Blocks.DANDELION)).a(consumer, "yellow_dye_from_dandelion");
		ji.a(Items.YELLOW_DYE, 2).b(Blocks.SUNFLOWER).a("yellow_dye").a("has_double_plant", a(Blocks.SUNFLOWER)).a(consumer, "yellow_dye_from_sunflower");
		ji.a(Items.DRIED_KELP, 9).b(Blocks.DRIED_KELP_BLOCK).a("has_dried_kelp_block", a(Blocks.DRIED_KELP_BLOCK)).a(consumer);
		ji.a(Blocks.DRIED_KELP_BLOCK).b(Items.DRIED_KELP, 9).a("has_dried_kelp", a(Items.DRIED_KELP)).a(consumer);
		jh.a(Blocks.CONDUIT).a('#', Items.NAUTILUS_SHELL).a('X', Items.HEART_OF_THE_SEA).a("###").a("#X#").a("###").a("has_nautilus_core", a(Items.HEART_OF_THE_SEA)).a("has_nautilus_shell", a(Items.NAUTILUS_SHELL)).a(consumer);
		jh.a(Blocks.POLISHED_GRANITE_STAIRS, 4).a('#', Blocks.POLISHED_GRANITE).a("#  ").a("## ").a("###").a("has_polished_granite", a(Blocks.POLISHED_GRANITE)).a(consumer);
		jh.a(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, 4).a('#', Blocks.SMOOTH_RED_SANDSTONE).a("#  ").a("## ").a("###").a("has_smooth_red_sandstone", a(Blocks.SMOOTH_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.MOSSY_STONE_BRICK_STAIRS, 4).a('#', Blocks.MOSSY_STONE_BRICKS).a("#  ").a("## ").a("###").a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.POLISHED_DIORITE_STAIRS, 4).a('#', Blocks.POLISHED_DIORITE).a("#  ").a("## ").a("###").a("has_polished_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer);
		jh.a(Blocks.MOSSY_COBBLESTONE_STAIRS, 4).a('#', Blocks.MOSSY_COBBLESTONE).a("#  ").a("## ").a("###").a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer);
		jh.a(Blocks.END_STONE_BRICK_STAIRS, 4).a('#', Blocks.END_STONE_BRICKS).a("#  ").a("## ").a("###").a("has_end_stone_bricks", a(Blocks.END_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.STONE_STAIRS, 4).a('#', Blocks.STONE).a("#  ").a("## ").a("###").a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Blocks.SMOOTH_SANDSTONE_STAIRS, 4).a('#', Blocks.SMOOTH_SANDSTONE).a("#  ").a("## ").a("###").a("has_smooth_sandstone", a(Blocks.SMOOTH_SANDSTONE)).a(consumer);
		jh.a(Blocks.SMOOTH_QUARTZ_STAIRS, 4).a('#', Blocks.SMOOTH_QUARTZ).a("#  ").a("## ").a("###").a("has_smooth_quartz", a(Blocks.SMOOTH_QUARTZ)).a(consumer);
		jh.a(Blocks.GRANITE_STAIRS, 4).a('#', Blocks.GRANITE).a("#  ").a("## ").a("###").a("has_granite", a(Blocks.GRANITE)).a(consumer);
		jh.a(Blocks.ANDESITE_STAIRS, 4).a('#', Blocks.ANDESITE).a("#  ").a("## ").a("###").a("has_andesite", a(Blocks.ANDESITE)).a(consumer);
		jh.a(Blocks.RED_NETHER_BRICK_STAIRS, 4).a('#', Blocks.RED_NETHER_BRICKS).a("#  ").a("## ").a("###").a("has_red_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.POLISHED_ANDESITE_STAIRS, 4).a('#', Blocks.POLISHED_ANDESITE).a("#  ").a("## ").a("###").a("has_polished_andesite", a(Blocks.POLISHED_ANDESITE)).a(consumer);
		jh.a(Blocks.DIORITE_STAIRS, 4).a('#', Blocks.DIORITE).a("#  ").a("## ").a("###").a("has_diorite", a(Blocks.DIORITE)).a(consumer);
		jh.a(Blocks.POLISHED_GRANITE_SLAB, 6).a('#', Blocks.POLISHED_GRANITE).a("###").a("has_polished_granite", a(Blocks.POLISHED_GRANITE)).a(consumer);
		jh.a(Blocks.SMOOTH_RED_SANDSTONE_SLAB, 6).a('#', Blocks.SMOOTH_RED_SANDSTONE).a("###").a("has_smooth_red_sandstone", a(Blocks.SMOOTH_RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.MOSSY_STONE_BRICK_SLAB, 6).a('#', Blocks.MOSSY_STONE_BRICKS).a("###").a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.POLISHED_DIORITE_SLAB, 6).a('#', Blocks.POLISHED_DIORITE).a("###").a("has_polished_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer);
		jh.a(Blocks.MOSSY_COBBLESTONE_SLAB, 6).a('#', Blocks.MOSSY_COBBLESTONE).a("###").a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer);
		jh.a(Blocks.END_STONE_BRICK_SLAB, 6).a('#', Blocks.END_STONE_BRICKS).a("###").a("has_end_stone_bricks", a(Blocks.END_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.SMOOTH_SANDSTONE_SLAB, 6).a('#', Blocks.SMOOTH_SANDSTONE).a("###").a("has_smooth_sandstone", a(Blocks.SMOOTH_SANDSTONE)).a(consumer);
		jh.a(Blocks.SMOOTH_QUARTZ_SLAB, 6).a('#', Blocks.SMOOTH_QUARTZ).a("###").a("has_smooth_quartz", a(Blocks.SMOOTH_QUARTZ)).a(consumer);
		jh.a(Blocks.GRANITE_SLAB, 6).a('#', Blocks.GRANITE).a("###").a("has_granite", a(Blocks.GRANITE)).a(consumer);
		jh.a(Blocks.ANDESITE_SLAB, 6).a('#', Blocks.ANDESITE).a("###").a("has_andesite", a(Blocks.ANDESITE)).a(consumer);
		jh.a(Blocks.RED_NETHER_BRICK_SLAB, 6).a('#', Blocks.RED_NETHER_BRICKS).a("###").a("has_red_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.POLISHED_ANDESITE_SLAB, 6).a('#', Blocks.POLISHED_ANDESITE).a("###").a("has_polished_andesite", a(Blocks.POLISHED_ANDESITE)).a(consumer);
		jh.a(Blocks.DIORITE_SLAB, 6).a('#', Blocks.DIORITE).a("###").a("has_diorite", a(Blocks.DIORITE)).a(consumer);
		jh.a(Blocks.BRICK_WALL, 6).a('#', Blocks.BRICKS).a("###").a("###").a("has_bricks", a(Blocks.BRICKS)).a(consumer);
		jh.a(Blocks.PRISMARINE_WALL, 6).a('#', Blocks.PRISMARINE).a("###").a("###").a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer);
		jh.a(Blocks.RED_SANDSTONE_WALL, 6).a('#', Blocks.RED_SANDSTONE).a("###").a("###").a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer);
		jh.a(Blocks.MOSSY_STONE_BRICK_WALL, 6).a('#', Blocks.MOSSY_STONE_BRICKS).a("###").a("###").a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.GRANITE_WALL, 6).a('#', Blocks.GRANITE).a("###").a("###").a("has_granite", a(Blocks.GRANITE)).a(consumer);
		jh.a(Blocks.STONE_BRICK_WALL, 6).a('#', Blocks.STONE_BRICKS).a("###").a("###").a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer);
		jh.a(Blocks.NETHER_BRICK_WALL, 6).a('#', Blocks.NETHER_BRICKS).a("###").a("###").a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.ANDESITE_WALL, 6).a('#', Blocks.ANDESITE).a("###").a("###").a("has_andesite", a(Blocks.ANDESITE)).a(consumer);
		jh.a(Blocks.RED_NETHER_BRICK_WALL, 6).a('#', Blocks.RED_NETHER_BRICKS).a("###").a("###").a("has_red_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer);
		jh.a(Blocks.SANDSTONE_WALL, 6).a('#', Blocks.SANDSTONE).a("###").a("###").a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer);
		jh.a(Blocks.END_STONE_BRICK_WALL, 6).a('#', Blocks.END_STONE_BRICKS).a("###").a("###").a("has_end_stone_bricks", a(Blocks.END_STONE_BRICKS)).a(consumer);
		jh.a(Blocks.DIORITE_WALL, 6).a('#', Blocks.DIORITE).a("###").a("###").a("has_diorite", a(Blocks.DIORITE)).a(consumer);
		ji.a(Items.CREEPER_BANNER_PATTERN).b(Items.PAPER).b(Items.CREEPER_HEAD).a("has_creeper_head", a(Items.CREEPER_HEAD)).a(consumer);
		ji.a(Items.SKULL_BANNER_PATTERN).b(Items.PAPER).b(Items.WITHER_SKELETON_SKULL).a("has_wither_skeleton_skull", a(Items.WITHER_SKELETON_SKULL)).a(consumer);
		ji.a(Items.FLOWER_BANNER_PATTERN).b(Items.PAPER).b(Blocks.OXEYE_DAISY).a("has_oxeye_daisy", a(Blocks.OXEYE_DAISY)).a(consumer);
		ji.a(Items.MOJANG_BANNER_PATTERN).b(Items.PAPER).b(Items.ENCHANTED_GOLDEN_APPLE).a("has_enchanted_golden_apple", a(Items.ENCHANTED_GOLDEN_APPLE)).a(consumer);
		jh.a(Blocks.SCAFFOLDING, 6).a('~', Items.STRING).a('I', Blocks.BAMBOO).a("I~I").a("I I").a("I I").a("has_bamboo", a(Blocks.BAMBOO)).a(consumer);
		jh.a(Blocks.GRINDSTONE).a('I', Items.STICK).a('-', Blocks.STONE_SLAB).a('#', TagsItem.PLANKS).a("I-I").a("# #").a("has_stone_slab", a(Blocks.STONE_SLAB)).a(consumer);
		jh.a(Blocks.BLAST_FURNACE).a('#', Blocks.SMOOTH_STONE).a('X', Blocks.FURNACE).a('I', Items.IRON_INGOT).a("III").a("IXI").a("###").a("has_smooth_stone", a(Blocks.SMOOTH_STONE)).a(consumer);
		jh.a(Blocks.SMOKER).a('#', TagsItem.LOGS).a('X', Blocks.FURNACE).a(" # ").a("#X#").a(" # ").a("has_furnace", a(Blocks.FURNACE)).a(consumer);
		jh.a(Blocks.CARTOGRAPHY_TABLE).a('#', TagsItem.PLANKS).a('@', Items.PAPER).a("@@").a("##").a("##").a("has_paper", a(Items.PAPER)).a(consumer);
		jh.a(Blocks.SMITHING_TABLE).a('#', TagsItem.PLANKS).a('@', Items.IRON_INGOT).a("@@").a("##").a("##").a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
		jh.a(Blocks.FLETCHING_TABLE).a('#', TagsItem.PLANKS).a('@', Items.FLINT).a("@@").a("##").a("##").a("has_flint", a(Items.FLINT)).a(consumer);
		jh.a(Blocks.STONECUTTER).a('I', Items.IRON_INGOT).a('#', Blocks.STONE).a(" I ").a("###").a("has_stone", a(Blocks.STONE)).a(consumer);
		jh.a(Blocks.LODESTONE).a('S', Items.dJ).a('#', Items.NETHERITE_INGOT).a("SSS").a("S#S").a("SSS").a("has_netherite_ingot", a(Items.NETHERITE_INGOT)).a(consumer);
		jh.a(Blocks.NETHERITE_BLOCK).a('#', Items.NETHERITE_INGOT).a("###").a("###").a("###").a("has_netherite_ingot", a(Items.NETHERITE_INGOT)).a(consumer);
		ji.a(Items.NETHERITE_INGOT, 9).b(Blocks.NETHERITE_BLOCK).a("netherite_ingot").a("has_netherite_block", a(Blocks.NETHERITE_BLOCK)).a(consumer, "netherite_ingot_from_netherite_block");
		ji.a(Items.NETHERITE_INGOT).b(Items.NETHERITE_SCRAP, 4).b(Items.GOLD_INGOT, 4).a("netherite_ingot").a("has_netherite_scrap", a(Items.NETHERITE_SCRAP)).a(consumer);
		jh.a(Blocks.RESPAWN_ANCHOR).a('O', Blocks.CRYING_OBSIDIAN).a('G', Blocks.GLOWSTONE).a("OOO").a("GGG").a("OOO").a("has_obsidian", a(Blocks.CRYING_OBSIDIAN)).a(consumer);
		jh.a(Blocks.BLACKSTONE_STAIRS, 4).a('#', Blocks.BLACKSTONE).a("#  ").a("## ").a("###").a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_STAIRS, 4).a('#', Blocks.POLISHED_BLACKSTONE).a("#  ").a("## ").a("###").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, 4).a('#', Blocks.POLISHED_BLACKSTONE_BRICKS).a("#  ").a("## ").a("###").a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
		jh.a(Blocks.BLACKSTONE_SLAB, 6).a('#', Blocks.BLACKSTONE).a("###").a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_SLAB, 6).a('#', Blocks.POLISHED_BLACKSTONE).a("###").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 6).a('#', Blocks.POLISHED_BLACKSTONE_BRICKS).a("###").a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE, 4).a('S', Blocks.BLACKSTONE).a("SS").a("SS").a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_BRICKS, 4).a('#', Blocks.POLISHED_BLACKSTONE).a("##").a("##").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.CHISELED_POLISHED_BLACKSTONE).a('#', Blocks.POLISHED_BLACKSTONE_SLAB).a("#").a("#").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.BLACKSTONE_WALL, 6).a('#', Blocks.BLACKSTONE).a("###").a("###").a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_WALL, 6).a('#', Blocks.POLISHED_BLACKSTONE).a("###").a("###").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_BRICK_WALL, 6).a('#', Blocks.POLISHED_BLACKSTONE_BRICKS).a("###").a("###").a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
		ji.a(Blocks.POLISHED_BLACKSTONE_BUTTON).b(Blocks.POLISHED_BLACKSTONE).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE).a('#', Blocks.POLISHED_BLACKSTONE).a("##").a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer);
		jh.a(Blocks.CHAIN).a('I', Items.IRON_INGOT).a('N', Items.IRON_NUGGET).a("N").a("I").a("N").a("has_iron_nugget", a(Items.IRON_NUGGET)).a("has_iron_ingot", a(Items.IRON_INGOT)).a(consumer);
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
		jj.c(RecipeItemStack.a(Items.POTATO), Items.BAKED_POTATO, 0.35F, 200).a("has_potato", a(Items.POTATO)).a(consumer);
		jj.c(RecipeItemStack.a(Items.CLAY_BALL), Items.BRICK, 0.3F, 200).a("has_clay_ball", a(Items.CLAY_BALL)).a(consumer);
		jj.c(RecipeItemStack.a(TagsItem.LOGS_THAT_BURN), Items.CHARCOAL, 0.15F, 200).a("has_log", a(TagsItem.LOGS_THAT_BURN)).a(consumer);
		jj.c(RecipeItemStack.a(Items.CHORUS_FRUIT), Items.POPPED_CHORUS_FRUIT, 0.1F, 200).a("has_chorus_fruit", a(Items.CHORUS_FRUIT)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.COAL_ORE.getItem()), Items.COAL, 0.1F, 200).a("has_coal_ore", a(Blocks.COAL_ORE)).a(consumer, "coal_from_smelting");
		jj.c(RecipeItemStack.a(Items.BEEF), Items.COOKED_BEEF, 0.35F, 200).a("has_beef", a(Items.BEEF)).a(consumer);
		jj.c(RecipeItemStack.a(Items.CHICKEN), Items.COOKED_CHICKEN, 0.35F, 200).a("has_chicken", a(Items.CHICKEN)).a(consumer);
		jj.c(RecipeItemStack.a(Items.COD), Items.COOKED_COD, 0.35F, 200).a("has_cod", a(Items.COD)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.KELP), Items.DRIED_KELP, 0.1F, 200).a("has_kelp", a(Blocks.KELP)).a(consumer, "dried_kelp_from_smelting");
		jj.c(RecipeItemStack.a(Items.SALMON), Items.COOKED_SALMON, 0.35F, 200).a("has_salmon", a(Items.SALMON)).a(consumer);
		jj.c(RecipeItemStack.a(Items.MUTTON), Items.COOKED_MUTTON, 0.35F, 200).a("has_mutton", a(Items.MUTTON)).a(consumer);
		jj.c(RecipeItemStack.a(Items.PORKCHOP), Items.COOKED_PORKCHOP, 0.35F, 200).a("has_porkchop", a(Items.PORKCHOP)).a(consumer);
		jj.c(RecipeItemStack.a(Items.RABBIT), Items.COOKED_RABBIT, 0.35F, 200).a("has_rabbit", a(Items.RABBIT)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.DIAMOND_ORE.getItem()), Items.DIAMOND, 1.0F, 200).a("has_diamond_ore", a(Blocks.DIAMOND_ORE)).a(consumer, "diamond_from_smelting");
		jj.c(RecipeItemStack.a(Blocks.LAPIS_ORE.getItem()), Items.LAPIS_LAZULI, 0.2F, 200).a("has_lapis_ore", a(Blocks.LAPIS_ORE)).a(consumer, "lapis_from_smelting");
		jj.c(RecipeItemStack.a(Blocks.EMERALD_ORE.getItem()), Items.EMERALD, 1.0F, 200).a("has_emerald_ore", a(Blocks.EMERALD_ORE)).a(consumer, "emerald_from_smelting");
		jj.c(RecipeItemStack.a(TagsItem.SAND), Blocks.GLASS.getItem(), 0.1F, 200).a("has_sand", a(TagsItem.SAND)).a(consumer);
		jj.c(RecipeItemStack.a(TagsItem.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 200).a("has_gold_ore", a(TagsItem.GOLD_ORES)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.SEA_PICKLE.getItem()), Items.LIME_DYE, 0.1F, 200).a("has_sea_pickle", a(Blocks.SEA_PICKLE)).a(consumer, "lime_dye_from_smelting");
		jj.c(RecipeItemStack.a(Blocks.CACTUS.getItem()), Items.GREEN_DYE, 1.0F, 200).a("has_cactus", a(Blocks.CACTUS)).a(consumer);
		jj.c(RecipeItemStack.a(Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR), Items.GOLD_NUGGET, 0.1F, 200).a("has_golden_pickaxe", a(Items.GOLDEN_PICKAXE)).a("has_golden_shovel", a(Items.GOLDEN_SHOVEL)).a("has_golden_axe", a(Items.GOLDEN_AXE)).a("has_golden_hoe", a(Items.GOLDEN_HOE)).a("has_golden_sword", a(Items.GOLDEN_SWORD)).a("has_golden_helmet", a(Items.GOLDEN_HELMET)).a("has_golden_chestplate", a(Items.GOLDEN_CHESTPLATE)).a("has_golden_leggings", a(Items.GOLDEN_LEGGINGS)).a("has_golden_boots", a(Items.GOLDEN_BOOTS)).a("has_golden_horse_armor", a(Items.GOLDEN_HORSE_ARMOR)).a(consumer, "gold_nugget_from_smelting");
		jj.c(RecipeItemStack.a(Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS), Items.IRON_NUGGET, 0.1F, 200).a("has_iron_pickaxe", a(Items.IRON_PICKAXE)).a("has_iron_shovel", a(Items.IRON_SHOVEL)).a("has_iron_axe", a(Items.IRON_AXE)).a("has_iron_hoe", a(Items.IRON_HOE)).a("has_iron_sword", a(Items.IRON_SWORD)).a("has_iron_helmet", a(Items.IRON_HELMET)).a("has_iron_chestplate", a(Items.IRON_CHESTPLATE)).a("has_iron_leggings", a(Items.IRON_LEGGINGS)).a("has_iron_boots", a(Items.IRON_BOOTS)).a("has_iron_horse_armor", a(Items.IRON_HORSE_ARMOR)).a("has_chainmail_helmet", a(Items.CHAINMAIL_HELMET)).a("has_chainmail_chestplate", a(Items.CHAINMAIL_CHESTPLATE)).a("has_chainmail_leggings", a(Items.CHAINMAIL_LEGGINGS)).a("has_chainmail_boots", a(Items.CHAINMAIL_BOOTS)).a(consumer, "iron_nugget_from_smelting");
		jj.c(RecipeItemStack.a(Blocks.IRON_ORE.getItem()), Items.IRON_INGOT, 0.7F, 200).a("has_iron_ore", a(Blocks.IRON_ORE.getItem())).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.CLAY), Blocks.TERRACOTTA.getItem(), 0.35F, 200).a("has_clay_block", a(Blocks.CLAY)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.NETHERRACK), Items.NETHER_BRICK, 0.1F, 200).a("has_netherrack", a(Blocks.NETHERRACK)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.NETHER_QUARTZ_ORE), Items.QUARTZ, 0.2F, 200).a("has_nether_quartz_ore", a(Blocks.NETHER_QUARTZ_ORE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.REDSTONE_ORE), Items.REDSTONE, 0.7F, 200).a("has_redstone_ore", a(Blocks.REDSTONE_ORE)).a(consumer, "redstone_from_smelting");
		jj.c(RecipeItemStack.a(Blocks.WET_SPONGE), Blocks.SPONGE.getItem(), 0.15F, 200).a("has_wet_sponge", a(Blocks.WET_SPONGE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.STONE.getItem(), 0.1F, 200).a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.STONE), Blocks.SMOOTH_STONE.getItem(), 0.1F, 200).a("has_stone", a(Blocks.STONE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SMOOTH_SANDSTONE.getItem(), 0.1F, 200).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.SMOOTH_RED_SANDSTONE.getItem(), 0.1F, 200).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.SMOOTH_QUARTZ.getItem(), 0.1F, 200).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.CRACKED_STONE_BRICKS.getItem(), 0.1F, 200).a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.BLACK_TERRACOTTA), Blocks.BLACK_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_black_terracotta", a(Blocks.BLACK_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.BLUE_TERRACOTTA), Blocks.BLUE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_blue_terracotta", a(Blocks.BLUE_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.BROWN_TERRACOTTA), Blocks.BROWN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_brown_terracotta", a(Blocks.BROWN_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.CYAN_TERRACOTTA), Blocks.CYAN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_cyan_terracotta", a(Blocks.CYAN_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.GRAY_TERRACOTTA), Blocks.GRAY_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_gray_terracotta", a(Blocks.GRAY_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.GREEN_TERRACOTTA), Blocks.GREEN_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_green_terracotta", a(Blocks.GREEN_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.LIGHT_BLUE_TERRACOTTA), Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_light_blue_terracotta", a(Blocks.LIGHT_BLUE_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.LIGHT_GRAY_TERRACOTTA), Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_light_gray_terracotta", a(Blocks.LIGHT_GRAY_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.LIME_TERRACOTTA), Blocks.LIME_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_lime_terracotta", a(Blocks.LIME_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.MAGENTA_TERRACOTTA), Blocks.MAGENTA_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_magenta_terracotta", a(Blocks.MAGENTA_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.ORANGE_TERRACOTTA), Blocks.ORANGE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_orange_terracotta", a(Blocks.ORANGE_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.PINK_TERRACOTTA), Blocks.PINK_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_pink_terracotta", a(Blocks.PINK_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.PURPLE_TERRACOTTA), Blocks.PURPLE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_purple_terracotta", a(Blocks.PURPLE_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.RED_TERRACOTTA), Blocks.RED_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_red_terracotta", a(Blocks.RED_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.WHITE_TERRACOTTA), Blocks.WHITE_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_white_terracotta", a(Blocks.WHITE_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.YELLOW_TERRACOTTA), Blocks.YELLOW_GLAZED_TERRACOTTA.getItem(), 0.1F, 200).a("has_yellow_terracotta", a(Blocks.YELLOW_TERRACOTTA)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.ANCIENT_DEBRIS), Items.NETHERITE_SCRAP, 2.0F, 200).a("has_ancient_debris", a(Blocks.ANCIENT_DEBRIS)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getItem(), 0.1F, 200).a("has_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer);
		jj.c(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.CRACKED_NETHER_BRICKS.getItem(), 0.1F, 200).a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer);
		jj.b(RecipeItemStack.a(Blocks.IRON_ORE.getItem()), Items.IRON_INGOT, 0.7F, 100).a("has_iron_ore", a(Blocks.IRON_ORE.getItem())).a(consumer, "iron_ingot_from_blasting");
		jj.b(RecipeItemStack.a(TagsItem.GOLD_ORES), Items.GOLD_INGOT, 1.0F, 100).a("has_gold_ore", a(TagsItem.GOLD_ORES)).a(consumer, "gold_ingot_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.DIAMOND_ORE.getItem()), Items.DIAMOND, 1.0F, 100).a("has_diamond_ore", a(Blocks.DIAMOND_ORE)).a(consumer, "diamond_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.LAPIS_ORE.getItem()), Items.LAPIS_LAZULI, 0.2F, 100).a("has_lapis_ore", a(Blocks.LAPIS_ORE)).a(consumer, "lapis_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.REDSTONE_ORE), Items.REDSTONE, 0.7F, 100).a("has_redstone_ore", a(Blocks.REDSTONE_ORE)).a(consumer, "redstone_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.COAL_ORE.getItem()), Items.COAL, 0.1F, 100).a("has_coal_ore", a(Blocks.COAL_ORE)).a(consumer, "coal_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.EMERALD_ORE.getItem()), Items.EMERALD, 1.0F, 100).a("has_emerald_ore", a(Blocks.EMERALD_ORE)).a(consumer, "emerald_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.NETHER_QUARTZ_ORE), Items.QUARTZ, 0.2F, 100).a("has_nether_quartz_ore", a(Blocks.NETHER_QUARTZ_ORE)).a(consumer, "quartz_from_blasting");
		jj.b(RecipeItemStack.a(Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR), Items.GOLD_NUGGET, 0.1F, 100).a("has_golden_pickaxe", a(Items.GOLDEN_PICKAXE)).a("has_golden_shovel", a(Items.GOLDEN_SHOVEL)).a("has_golden_axe", a(Items.GOLDEN_AXE)).a("has_golden_hoe", a(Items.GOLDEN_HOE)).a("has_golden_sword", a(Items.GOLDEN_SWORD)).a("has_golden_helmet", a(Items.GOLDEN_HELMET)).a("has_golden_chestplate", a(Items.GOLDEN_CHESTPLATE)).a("has_golden_leggings", a(Items.GOLDEN_LEGGINGS)).a("has_golden_boots", a(Items.GOLDEN_BOOTS)).a("has_golden_horse_armor", a(Items.GOLDEN_HORSE_ARMOR)).a(consumer, "gold_nugget_from_blasting");
		jj.b(RecipeItemStack.a(Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS), Items.IRON_NUGGET, 0.1F, 100).a("has_iron_pickaxe", a(Items.IRON_PICKAXE)).a("has_iron_shovel", a(Items.IRON_SHOVEL)).a("has_iron_axe", a(Items.IRON_AXE)).a("has_iron_hoe", a(Items.IRON_HOE)).a("has_iron_sword", a(Items.IRON_SWORD)).a("has_iron_helmet", a(Items.IRON_HELMET)).a("has_iron_chestplate", a(Items.IRON_CHESTPLATE)).a("has_iron_leggings", a(Items.IRON_LEGGINGS)).a("has_iron_boots", a(Items.IRON_BOOTS)).a("has_iron_horse_armor", a(Items.IRON_HORSE_ARMOR)).a("has_chainmail_helmet", a(Items.CHAINMAIL_HELMET)).a("has_chainmail_chestplate", a(Items.CHAINMAIL_CHESTPLATE)).a("has_chainmail_leggings", a(Items.CHAINMAIL_LEGGINGS)).a("has_chainmail_boots", a(Items.CHAINMAIL_BOOTS)).a(consumer, "iron_nugget_from_blasting");
		jj.b(RecipeItemStack.a(Blocks.ANCIENT_DEBRIS), Items.NETHERITE_SCRAP, 2.0F, 100).a("has_ancient_debris", a(Blocks.ANCIENT_DEBRIS)).a(consumer, "netherite_scrap_from_blasting");
		a(consumer, "smoking", RecipeSerializer.r, 100);
		a(consumer, "campfire_cooking", RecipeSerializer.s, 600);
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_SLAB, 2).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_slab_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_STAIRS).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_stairs_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_BRICKS).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_bricks_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_BRICK_SLAB, 2).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_brick_slab_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_BRICK_STAIRS).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_brick_stairs_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.CHISELED_STONE_BRICKS).a("has_stone", a(Blocks.STONE)).a(consumer, "chiseled_stone_bricks_stone_from_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE), Blocks.STONE_BRICK_WALL).a("has_stone", a(Blocks.STONE)).a(consumer, "stone_brick_walls_from_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.CUT_SANDSTONE).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "cut_sandstone_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SANDSTONE_SLAB, 2).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "sandstone_slab_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.CUT_SANDSTONE_SLAB, 2).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "cut_sandstone_slab_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.CUT_SANDSTONE), Blocks.CUT_SANDSTONE_SLAB, 2).a("has_cut_sandstone", a(Blocks.SANDSTONE)).a(consumer, "cut_sandstone_slab_from_cut_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SANDSTONE_STAIRS).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "sandstone_stairs_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.SANDSTONE_WALL).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "sandstone_wall_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SANDSTONE), Blocks.CHISELED_SANDSTONE).a("has_sandstone", a(Blocks.SANDSTONE)).a(consumer, "chiseled_sandstone_from_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.CUT_RED_SANDSTONE).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.RED_SANDSTONE_SLAB, 2).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_slab_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.CUT_RED_SANDSTONE_SLAB, 2).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_slab_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.CUT_RED_SANDSTONE), Blocks.CUT_RED_SANDSTONE_SLAB, 2).a("has_cut_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "cut_red_sandstone_slab_from_cut_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.RED_SANDSTONE_STAIRS).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_stairs_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.RED_SANDSTONE_WALL).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "red_sandstone_wall_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_SANDSTONE), Blocks.CHISELED_RED_SANDSTONE).a("has_red_sandstone", a(Blocks.RED_SANDSTONE)).a(consumer, "chiseled_red_sandstone_from_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.QUARTZ_SLAB, 2).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_slab_from_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.QUARTZ_STAIRS).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_stairs_from_quartz_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.QUARTZ_PILLAR).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_pillar_from_quartz_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.CHISELED_QUARTZ_BLOCK).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer, "chiseled_quartz_block_from_quartz_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.QUARTZ_BLOCK), Blocks.QUARTZ_BRICKS).a("has_quartz_block", a(Blocks.QUARTZ_BLOCK)).a(consumer, "quartz_bricks_from_quartz_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.COBBLESTONE_STAIRS).a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer, "cobblestone_stairs_from_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.COBBLESTONE_SLAB, 2).a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer, "cobblestone_slab_from_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.COBBLESTONE), Blocks.COBBLESTONE_WALL).a("has_cobblestone", a(Blocks.COBBLESTONE)).a(consumer, "cobblestone_wall_from_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.STONE_BRICK_SLAB, 2).a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer, "stone_brick_slab_from_stone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.STONE_BRICK_STAIRS).a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer, "stone_brick_stairs_from_stone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.STONE_BRICK_WALL).a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer, "stone_brick_wall_from_stone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.STONE_BRICKS), Blocks.CHISELED_STONE_BRICKS).a("has_stone_bricks", a(Blocks.STONE_BRICKS)).a(consumer, "chiseled_stone_bricks_from_stone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BRICKS), Blocks.BRICK_SLAB, 2).a("has_bricks", a(Blocks.BRICKS)).a(consumer, "brick_slab_from_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BRICKS), Blocks.BRICK_STAIRS).a("has_bricks", a(Blocks.BRICKS)).a(consumer, "brick_stairs_from_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BRICKS), Blocks.BRICK_WALL).a("has_bricks", a(Blocks.BRICKS)).a(consumer, "brick_wall_from_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.NETHER_BRICK_SLAB, 2).a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_slab_from_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.NETHER_BRICK_STAIRS).a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_stairs_from_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.NETHER_BRICK_WALL).a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer, "nether_brick_wall_from_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.NETHER_BRICKS), Blocks.CHISELED_NETHER_BRICKS).a("has_nether_bricks", a(Blocks.NETHER_BRICKS)).a(consumer, "chiseled_nether_bricks_from_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), Blocks.RED_NETHER_BRICK_SLAB, 2).a("has_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_slab_from_red_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), Blocks.RED_NETHER_BRICK_STAIRS).a("has_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_stairs_from_red_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.RED_NETHER_BRICKS), Blocks.RED_NETHER_BRICK_WALL).a("has_nether_bricks", a(Blocks.RED_NETHER_BRICKS)).a(consumer, "red_nether_brick_wall_from_red_nether_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), Blocks.PURPUR_SLAB, 2).a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer, "purpur_slab_from_purpur_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), Blocks.PURPUR_STAIRS).a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer, "purpur_stairs_from_purpur_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PURPUR_BLOCK), Blocks.PURPUR_PILLAR).a("has_purpur_block", a(Blocks.PURPUR_BLOCK)).a(consumer, "purpur_pillar_from_purpur_block_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PRISMARINE), Blocks.PRISMARINE_SLAB, 2).a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer, "prismarine_slab_from_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PRISMARINE), Blocks.PRISMARINE_STAIRS).a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer, "prismarine_stairs_from_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PRISMARINE), Blocks.PRISMARINE_WALL).a("has_prismarine", a(Blocks.PRISMARINE)).a(consumer, "prismarine_wall_from_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PRISMARINE_BRICKS), Blocks.PRISMARINE_BRICK_SLAB, 2).a("has_prismarine_brick", a(Blocks.PRISMARINE_BRICKS)).a(consumer, "prismarine_brick_slab_from_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.PRISMARINE_BRICKS), Blocks.PRISMARINE_BRICK_STAIRS).a("has_prismarine_brick", a(Blocks.PRISMARINE_BRICKS)).a(consumer, "prismarine_brick_stairs_from_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DARK_PRISMARINE), Blocks.DARK_PRISMARINE_SLAB, 2).a("has_dark_prismarine", a(Blocks.DARK_PRISMARINE)).a(consumer, "dark_prismarine_slab_from_dark_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DARK_PRISMARINE), Blocks.DARK_PRISMARINE_STAIRS).a("has_dark_prismarine", a(Blocks.DARK_PRISMARINE)).a(consumer, "dark_prismarine_stairs_from_dark_prismarine_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.ANDESITE_SLAB, 2).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "andesite_slab_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.ANDESITE_STAIRS).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "andesite_stairs_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.ANDESITE_WALL).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "andesite_wall_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.POLISHED_ANDESITE).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "polished_andesite_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.POLISHED_ANDESITE_SLAB, 2).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "polished_andesite_slab_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.ANDESITE), Blocks.POLISHED_ANDESITE_STAIRS).a("has_andesite", a(Blocks.ANDESITE)).a(consumer, "polished_andesite_stairs_from_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_ANDESITE), Blocks.POLISHED_ANDESITE_SLAB, 2).a("has_polished_andesite", a(Blocks.POLISHED_ANDESITE)).a(consumer, "polished_andesite_slab_from_polished_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_ANDESITE), Blocks.POLISHED_ANDESITE_STAIRS).a("has_polished_andesite", a(Blocks.POLISHED_ANDESITE)).a(consumer, "polished_andesite_stairs_from_polished_andesite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BASALT), Blocks.cP).a("has_basalt", a(Blocks.BASALT)).a(consumer, "polished_basalt_from_basalt_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.GRANITE_SLAB, 2).a("has_granite", a(Blocks.GRANITE)).a(consumer, "granite_slab_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.GRANITE_STAIRS).a("has_granite", a(Blocks.GRANITE)).a(consumer, "granite_stairs_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.GRANITE_WALL).a("has_granite", a(Blocks.GRANITE)).a(consumer, "granite_wall_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.POLISHED_GRANITE).a("has_granite", a(Blocks.GRANITE)).a(consumer, "polished_granite_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.POLISHED_GRANITE_SLAB, 2).a("has_granite", a(Blocks.GRANITE)).a(consumer, "polished_granite_slab_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.GRANITE), Blocks.POLISHED_GRANITE_STAIRS).a("has_granite", a(Blocks.GRANITE)).a(consumer, "polished_granite_stairs_from_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_GRANITE), Blocks.POLISHED_GRANITE_SLAB, 2).a("has_polished_granite", a(Blocks.POLISHED_GRANITE)).a(consumer, "polished_granite_slab_from_polished_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_GRANITE), Blocks.POLISHED_GRANITE_STAIRS).a("has_polished_granite", a(Blocks.POLISHED_GRANITE)).a(consumer, "polished_granite_stairs_from_polished_granite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.DIORITE_SLAB, 2).a("has_diorite", a(Blocks.DIORITE)).a(consumer, "diorite_slab_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.DIORITE_STAIRS).a("has_diorite", a(Blocks.DIORITE)).a(consumer, "diorite_stairs_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.DIORITE_WALL).a("has_diorite", a(Blocks.DIORITE)).a(consumer, "diorite_wall_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.POLISHED_DIORITE).a("has_diorite", a(Blocks.DIORITE)).a(consumer, "polished_diorite_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.POLISHED_DIORITE_SLAB, 2).a("has_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_slab_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.DIORITE), Blocks.POLISHED_DIORITE_STAIRS).a("has_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_stairs_from_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_DIORITE), Blocks.POLISHED_DIORITE_SLAB, 2).a("has_polished_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_slab_from_polished_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_DIORITE), Blocks.POLISHED_DIORITE_STAIRS).a("has_polished_diorite", a(Blocks.POLISHED_DIORITE)).a(consumer, "polished_diorite_stairs_from_polished_diorite_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), Blocks.MOSSY_STONE_BRICK_SLAB, 2).a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_slab_from_mossy_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), Blocks.MOSSY_STONE_BRICK_STAIRS).a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_stairs_from_mossy_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_STONE_BRICKS), Blocks.MOSSY_STONE_BRICK_WALL).a("has_mossy_stone_bricks", a(Blocks.MOSSY_STONE_BRICKS)).a(consumer, "mossy_stone_brick_wall_from_mossy_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), Blocks.MOSSY_COBBLESTONE_SLAB, 2).a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_slab_from_mossy_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), Blocks.MOSSY_COBBLESTONE_STAIRS).a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_stairs_from_mossy_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.MOSSY_COBBLESTONE), Blocks.MOSSY_COBBLESTONE_WALL).a("has_mossy_cobblestone", a(Blocks.MOSSY_COBBLESTONE)).a(consumer, "mossy_cobblestone_wall_from_mossy_cobblestone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_SANDSTONE), Blocks.SMOOTH_SANDSTONE_SLAB, 2).a("has_smooth_sandstone", a(Blocks.SMOOTH_SANDSTONE)).a(consumer, "smooth_sandstone_slab_from_smooth_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_SANDSTONE), Blocks.SMOOTH_SANDSTONE_STAIRS).a("has_mossy_cobblestone", a(Blocks.SMOOTH_SANDSTONE)).a(consumer, "smooth_sandstone_stairs_from_smooth_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_RED_SANDSTONE), Blocks.SMOOTH_RED_SANDSTONE_SLAB, 2).a("has_smooth_red_sandstone", a(Blocks.SMOOTH_RED_SANDSTONE)).a(consumer, "smooth_red_sandstone_slab_from_smooth_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_RED_SANDSTONE), Blocks.SMOOTH_RED_SANDSTONE_STAIRS).a("has_smooth_red_sandstone", a(Blocks.SMOOTH_RED_SANDSTONE)).a(consumer, "smooth_red_sandstone_stairs_from_smooth_red_sandstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_QUARTZ), Blocks.SMOOTH_QUARTZ_SLAB, 2).a("has_smooth_quartz", a(Blocks.SMOOTH_QUARTZ)).a(consumer, "smooth_quartz_slab_from_smooth_quartz_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_QUARTZ), Blocks.SMOOTH_QUARTZ_STAIRS).a("has_smooth_quartz", a(Blocks.SMOOTH_QUARTZ)).a(consumer, "smooth_quartz_stairs_from_smooth_quartz_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), Blocks.END_STONE_BRICK_SLAB, 2).a("has_end_stone_brick", a(Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_slab_from_end_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), Blocks.END_STONE_BRICK_STAIRS).a("has_end_stone_brick", a(Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_stairs_from_end_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE_BRICKS), Blocks.END_STONE_BRICK_WALL).a("has_end_stone_brick", a(Blocks.END_STONE_BRICKS)).a(consumer, "end_stone_brick_wall_from_end_stone_brick_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE), Blocks.END_STONE_BRICKS).a("has_end_stone", a(Blocks.END_STONE)).a(consumer, "end_stone_bricks_from_end_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE), Blocks.END_STONE_BRICK_SLAB, 2).a("has_end_stone", a(Blocks.END_STONE)).a(consumer, "end_stone_brick_slab_from_end_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE), Blocks.END_STONE_BRICK_STAIRS).a("has_end_stone", a(Blocks.END_STONE)).a(consumer, "end_stone_brick_stairs_from_end_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.END_STONE), Blocks.END_STONE_BRICK_WALL).a("has_end_stone", a(Blocks.END_STONE)).a(consumer, "end_stone_brick_wall_from_end_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.SMOOTH_STONE), Blocks.SMOOTH_STONE_SLAB, 2).a("has_smooth_stone", a(Blocks.SMOOTH_STONE)).a(consumer, "smooth_stone_slab_from_smooth_stone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.BLACKSTONE_SLAB, 2).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "blackstone_slab_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.BLACKSTONE_STAIRS).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "blackstone_stairs_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.BLACKSTONE_WALL).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "blackstone_wall_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_WALL).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_wall_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_SLAB, 2).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_slab_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_STAIRS).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_stairs_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.CHISELED_POLISHED_BLACKSTONE).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "chiseled_polished_blackstone_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICKS).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_bricks_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_slab_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_stairs_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_blackstone", a(Blocks.BLACKSTONE)).a(consumer, "polished_blackstone_brick_wall_from_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_SLAB, 2).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_slab_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_STAIRS).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_stairs_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICKS).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_bricks_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_WALL).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_wall_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_slab_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_stairs_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "polished_blackstone_brick_wall_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE), Blocks.CHISELED_POLISHED_BLACKSTONE).a("has_polished_blackstone", a(Blocks.POLISHED_BLACKSTONE)).a(consumer, "chiseled_polished_blackstone_from_polished_blackstone_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 2).a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_slab_from_polished_blackstone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS).a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_stairs_from_polished_blackstone_bricks_stonecutting");
		jk.a(RecipeItemStack.a(Blocks.POLISHED_BLACKSTONE_BRICKS), Blocks.POLISHED_BLACKSTONE_BRICK_WALL).a("has_polished_blackstone_bricks", a(Blocks.POLISHED_BLACKSTONE_BRICKS)).a(consumer, "polished_blackstone_brick_wall_from_polished_blackstone_bricks_stonecutting");
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
		jm.a(RecipeItemStack.a(item), RecipeItemStack.a(Items.NETHERITE_INGOT), item1).a("has_netherite_ingot", a(Items.NETHERITE_INGOT)).a(consumer, IRegistry.ITEM.getKey(item1.getItem()).getKey() + "_smithing");
	}

	private static void a(Consumer<jf> consumer, IMaterial imaterial, Tag<Item> tag) {
		ji.a(imaterial, 4).a(tag).a("planks").a("has_log", a(tag)).a(consumer);
	}

	private static void b(Consumer<jf> consumer, IMaterial imaterial, Tag<Item> tag) {
		ji.a(imaterial, 4).a(tag).a("planks").a("has_logs", a(tag)).a(consumer);
	}

	private static void a(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 3).a('#', imaterial1).a("##").a("##").b("bark").a("has_log", a(imaterial1)).a(consumer);
	}

	private static void b(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial).a('#', imaterial1).a("# #").a("###").b("boat").a("in_water", a(Blocks.WATER)).a(consumer);
	}

	private static void c(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		ji.a(imaterial).b(imaterial1).a("wooden_button").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void d(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 3).a('#', imaterial1).a("##").a("##").a("##").b("wooden_door").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void e(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 3).a('#', Items.STICK).a('W', imaterial1).a("W#W").a("W#W").b("wooden_fence").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void f(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial).a('#', Items.STICK).a('W', imaterial1).a("#W#").a("#W#").b("wooden_fence_gate").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void g(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial).a('#', imaterial1).a("##").b("wooden_pressure_plate").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void h(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 6).a('#', imaterial1).a("###").b("wooden_slab").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void i(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 4).a('#', imaterial1).a("#  ").a("## ").a("###").b("wooden_stairs").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void j(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 2).a('#', imaterial1).a("###").a("###").b("wooden_trapdoor").a("has_planks", a(imaterial1)).a(consumer);
	}

	private static void k(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial, 3).b("sign").a('#', imaterial1).a('X', Items.STICK).a("###").a("###").a(" X ").a("has_" + s, a(imaterial1)).a(consumer);
	}

	private static void l(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		ji.a(imaterial).b(imaterial1).b(Blocks.WHITE_WOOL).a("wool").a("has_white_wool", a(Blocks.WHITE_WOOL)).a(consumer);
	}

	private static void m(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial, 3).a('#', imaterial1).a("##").b("carpet").a("has_" + s, a(imaterial1)).a(consumer);
	}

	private static void n(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();
		String s1 = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial, 8).a('#', Blocks.WHITE_CARPET).a('$', imaterial1).a("###").a("#$#").a("###").b("carpet").a("has_white_carpet", a(Blocks.WHITE_CARPET)).a("has_" + s1, a(imaterial1)).a(consumer, s + "_from_white_carpet");
	}

	private static void o(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial).a('#', imaterial1).a('X', TagsItem.PLANKS).a("###").a("XXX").b("bed").a("has_" + s, a(imaterial1)).a(consumer);
	}

	private static void p(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();

		ji.a(imaterial).b(Items.WHITE_BED).b(imaterial1).a("dyed_bed").a("has_bed", a(Items.WHITE_BED)).a(consumer, s + "_from_white_bed");
	}

	private static void q(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial).a('#', imaterial1).a('|', Items.STICK).a("###").a("###").a(" | ").b("banner").a("has_" + s, a(imaterial1)).a(consumer);
	}

	private static void r(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 8).a('#', Blocks.GLASS).a('X', imaterial1).a("###").a("#X#").a("###").b("stained_glass").a("has_glass", a(Blocks.GLASS)).a(consumer);
	}

	private static void s(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 16).a('#', imaterial1).a("###").a("###").b("stained_glass_pane").a("has_glass", a(imaterial1)).a(consumer);
	}

	private static void t(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		String s = IRegistry.ITEM.getKey(imaterial.getItem()).getKey();
		String s1 = IRegistry.ITEM.getKey(imaterial1.getItem()).getKey();

		jh.a(imaterial, 8).a('#', Blocks.GLASS_PANE).a('$', imaterial1).a("###").a("#$#").a("###").b("stained_glass_pane").a("has_glass_pane", a(Blocks.GLASS_PANE)).a("has_" + s1, a(imaterial1)).a(consumer, s + "_from_glass_pane");
	}

	private static void u(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		jh.a(imaterial, 8).a('#', Blocks.TERRACOTTA).a('X', imaterial1).a("###").a("#X#").a("###").b("stained_terracotta").a("has_terracotta", a(Blocks.TERRACOTTA)).a(consumer);
	}

	private static void v(Consumer<jf> consumer, IMaterial imaterial, IMaterial imaterial1) {
		ji.a(imaterial, 8).b(imaterial1).b(Blocks.SAND, 4).b(Blocks.GRAVEL, 4).a("concrete_powder").a("has_sand", a(Blocks.SAND)).a("has_gravel", a(Blocks.GRAVEL)).a(consumer);
	}

	private static void a(Consumer<jf> consumer, String s, RecipeSerializerCooking<?> recipeserializercooking, int i) {
		jj.a(RecipeItemStack.a(Items.BEEF), Items.COOKED_BEEF, 0.35F, i, recipeserializercooking).a("has_beef", a(Items.BEEF)).a(consumer, "cooked_beef_from_" + s);
		jj.a(RecipeItemStack.a(Items.CHICKEN), Items.COOKED_CHICKEN, 0.35F, i, recipeserializercooking).a("has_chicken", a(Items.CHICKEN)).a(consumer, "cooked_chicken_from_" + s);
		jj.a(RecipeItemStack.a(Items.COD), Items.COOKED_COD, 0.35F, i, recipeserializercooking).a("has_cod", a(Items.COD)).a(consumer, "cooked_cod_from_" + s);
		jj.a(RecipeItemStack.a(Blocks.KELP), Items.DRIED_KELP, 0.1F, i, recipeserializercooking).a("has_kelp", a(Blocks.KELP)).a(consumer, "dried_kelp_from_" + s);
		jj.a(RecipeItemStack.a(Items.SALMON), Items.COOKED_SALMON, 0.35F, i, recipeserializercooking).a("has_salmon", a(Items.SALMON)).a(consumer, "cooked_salmon_from_" + s);
		jj.a(RecipeItemStack.a(Items.MUTTON), Items.COOKED_MUTTON, 0.35F, i, recipeserializercooking).a("has_mutton", a(Items.MUTTON)).a(consumer, "cooked_mutton_from_" + s);
		jj.a(RecipeItemStack.a(Items.PORKCHOP), Items.COOKED_PORKCHOP, 0.35F, i, recipeserializercooking).a("has_porkchop", a(Items.PORKCHOP)).a(consumer, "cooked_porkchop_from_" + s);
		jj.a(RecipeItemStack.a(Items.POTATO), Items.BAKED_POTATO, 0.35F, i, recipeserializercooking).a("has_potato", a(Items.POTATO)).a(consumer, "baked_potato_from_" + s);
		jj.a(RecipeItemStack.a(Items.RABBIT), Items.COOKED_RABBIT, 0.35F, i, recipeserializercooking).a("has_rabbit", a(Items.RABBIT)).a(consumer, "cooked_rabbit_from_" + s);
	}

	private static CriterionTriggerEnterBlock.a a(Block block) {
		return new CriterionTriggerEnterBlock.a(CriterionConditionEntity.b_.a, block, CriterionTriggerProperties.a);
	}

	private static CriterionTriggerInventoryChanged.a a(IMaterial imaterial) {
		return a(CriterionConditionItem.a_.a().a(imaterial).b());
	}

	private static CriterionTriggerInventoryChanged.a a(Tag<Item> tag) {
		return a(CriterionConditionItem.a_.a().a(tag).b());
	}

	private static CriterionTriggerInventoryChanged.a a(CriterionConditionItem... acriterionconditionitem) {
		return new CriterionTriggerInventoryChanged.a(CriterionConditionEntity.b_.a, CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, acriterionconditionitem);
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
		b(hashcache, Advancement.SerializedAdvancement.a().a("impossible", new CriterionTriggerImpossible.a()).b(), path.resolve("data/minecraft/advancements/recipes/root.json"));
	}

	@Override
	public String a() {
		return "Recipes";
	}
}
