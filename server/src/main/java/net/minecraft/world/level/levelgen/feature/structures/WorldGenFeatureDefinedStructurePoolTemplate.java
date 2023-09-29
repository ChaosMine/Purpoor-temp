package net.minecraft.world.level.levelgen.feature.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.INamable;
import net.minecraft.world.level.block.EnumBlockRotation;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureProcessorGravity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WorldGenFeatureDefinedStructurePoolTemplate {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Codec<WorldGenFeatureDefinedStructurePoolTemplate> a = RecordCodecBuilder.create(var02 -> var02.group(
		MinecraftKey.a.fieldOf("name").forGetter(WorldGenFeatureDefinedStructurePoolTemplate::b),
		MinecraftKey.a.fieldOf("fallback").forGetter(WorldGenFeatureDefinedStructurePoolTemplate::a),
		Codec.mapPair(
			WorldGenFeatureDefinedStructurePoolStructure.e.fieldOf("element"),
			Codec.INT.fieldOf("weight")
		).codec().listOf().promotePartial(SystemUtils.a("Pool element: ", LOGGER::error)).fieldOf("elements").forGetter(var0 -> var0.e)
	).apply(var02, WorldGenFeatureDefinedStructurePoolTemplate::new));
	public static final Codec<Supplier<WorldGenFeatureDefinedStructurePoolTemplate>> b = RegistryFileCodec.a(IRegistry.ax, a);
	private final MinecraftKey d;
	private final List<Pair<WorldGenFeatureDefinedStructurePoolStructure, Integer>> e;
	private final List<WorldGenFeatureDefinedStructurePoolStructure> f;
	private final MinecraftKey g;
	private int h = Integer.MIN_VALUE;

	public WorldGenFeatureDefinedStructurePoolTemplate(MinecraftKey var0, MinecraftKey var1, List<Pair<WorldGenFeatureDefinedStructurePoolStructure, Integer>> var2) {
		this.d = var0;
		this.e = var2;
		this.f = Lists.newArrayList();
		for (Pair<WorldGenFeatureDefinedStructurePoolStructure, Integer> var4 : var2) {
			WorldGenFeatureDefinedStructurePoolStructure var5 = var4.getFirst();
			for (int var6 = 0; var6 < var4.getSecond(); ++var6) {
				this.f.add(var5);
			}
		}
		this.g = var1;
	}

	public WorldGenFeatureDefinedStructurePoolTemplate(MinecraftKey var0, MinecraftKey var1, List<Pair<Function<Matching, ? extends WorldGenFeatureDefinedStructurePoolStructure>, Integer>> var2, Matching var3) {
		this.d = var0;
		this.e = Lists.newArrayList();
		this.f = Lists.newArrayList();
		for (Pair<Function<Matching, ? extends WorldGenFeatureDefinedStructurePoolStructure>, Integer> var5 : var2) {
			WorldGenFeatureDefinedStructurePoolStructure var6 = var5.getFirst().apply(var3);
			this.e.add(Pair.of(var6, var5.getSecond()));
			for (int var7 = 0; var7 < var5.getSecond(); ++var7) {
				this.f.add(var6);
			}
		}
		this.g = var1;
	}

	public int a(DefinedStructureManager var0) {
		if (this.h == Integer.MIN_VALUE) {
			this.h = this.f.stream().mapToInt(var1 -> var1.a(var0, BlockPosition.ZERO, EnumBlockRotation.NONE).e()).max().orElse(0);
		}
		return this.h;
	}

	public MinecraftKey a() {
		return this.g;
	}

	public WorldGenFeatureDefinedStructurePoolStructure a(Random var0) {
		return this.f.get(var0.nextInt(this.f.size()));
	}

	public List<WorldGenFeatureDefinedStructurePoolStructure> b(Random var0) {
		return ImmutableList.copyOf(ObjectArrays.shuffle(this.f.toArray(new WorldGenFeatureDefinedStructurePoolStructure[0]), var0));
	}

	public MinecraftKey b() {
		return this.d;
	}

	public int c() {
		return this.f.size();
	}

	public enum Matching implements INamable {
		TERRAIN_MATCHING("terrain_matching", ImmutableList.of(new DefinedStructureProcessorGravity(HeightMap.Type.WORLD_SURFACE_WG, -1))),
		RIGID("rigid", ImmutableList.of());

		public static final Codec<Matching> c;
		private static final Map<String, Matching> d;

		static {
			c = INamable.a(Matching::values, Matching::a);
			d = Arrays.stream(Matching.values()).collect(Collectors.toMap(Matching::b, var0 -> var0));
		}

		private final String e;
		private final ImmutableList<DefinedStructureProcessor> f;

		Matching(String var2, ImmutableList<DefinedStructureProcessor> var3) {
			this.e = var2;
			this.f = var3;
		}

		public static Matching a(String var0) {
			return d.get(var0);
		}

		public String b() {
			return this.e;
		}

		public ImmutableList<DefinedStructureProcessor> c() {
			return this.f;
		}

		@Override
		public String getName() {
			return this.e;
		}
	}
}
