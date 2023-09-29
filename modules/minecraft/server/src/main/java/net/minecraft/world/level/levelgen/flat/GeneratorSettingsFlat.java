package net.minecraft.world.level.levelgen.flat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SystemUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.data.worldgen.BiomeDecoratorGroups;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsGeneration.a;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.levelgen.HeightMap.Type;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldGenStage.Decoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureFillConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class GeneratorSettingsFlat {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Codec<GeneratorSettingsFlat> a = RecordCodecBuilder.<GeneratorSettingsFlat>create(
		var0 -> var0.group(
			RegistryLookupCodec.a(IRegistry.ay).forGetter(var0x -> var0x.d),
			StructureSettings.a.fieldOf("structures").forGetter(GeneratorSettingsFlat::d),
			WorldGenFlatLayerInfo.a.listOf().fieldOf("layers").forGetter(GeneratorSettingsFlat::f),
			Codec.BOOL.fieldOf("lakes").orElse(false).forGetter(var0x -> var0x.k),
			Codec.BOOL.fieldOf("features").orElse(false).forGetter(var0x -> var0x.j),
			BiomeBase.d.optionalFieldOf("biome").orElseGet(Optional::empty).forGetter(var0x -> Optional.of(var0x.g))
		).apply(var0, GeneratorSettingsFlat::new)
	).stable();
	private static final Map<StructureGenerator<?>, StructureFeature<?, ?>> c = SystemUtils.a(
		Maps.newHashMap(), var0 -> {
			var0.put(StructureGenerator.MINESHAFT, StructureFeatures.b);
			var0.put(StructureGenerator.VILLAGE, StructureFeatures.t);
			var0.put(StructureGenerator.STRONGHOLD, StructureFeatures.k);
			var0.put(StructureGenerator.SWAMP_HUT, StructureFeatures.j);
			var0.put(StructureGenerator.DESERT_PYRAMID, StructureFeatures.f);
			var0.put(StructureGenerator.JUNGLE_PYRAMID, StructureFeatures.e);
			var0.put(StructureGenerator.IGLOO, StructureFeatures.g);
			var0.put(StructureGenerator.OCEAN_RUIN, StructureFeatures.m);
			var0.put(StructureGenerator.SHIPWRECK, StructureFeatures.h);
			var0.put(StructureGenerator.MONUMENT, StructureFeatures.l);
			var0.put(StructureGenerator.ENDCITY, StructureFeatures.q);
			var0.put(StructureGenerator.MANSION, StructureFeatures.d);
			var0.put(StructureGenerator.FORTRESS, StructureFeatures.o);
			var0.put(StructureGenerator.PILLAGER_OUTPOST, StructureFeatures.a);
			var0.put(StructureGenerator.RUINED_PORTAL, StructureFeatures.y);
			var0.put(StructureGenerator.BASTION_REMNANT, StructureFeatures.s);
		}
	);
	private final IRegistry<BiomeBase> d;
	private final StructureSettings e;
	private final List<WorldGenFlatLayerInfo> f = Lists.newArrayList();
	private final IBlockData[] h = new IBlockData[256];
	private Supplier<BiomeBase> g;
	private boolean i;
	private boolean j = false;
	private boolean k = false;

	public GeneratorSettingsFlat(
		IRegistry<BiomeBase> var0, StructureSettings var1, List<WorldGenFlatLayerInfo> var2, boolean var3, boolean var4, Optional<Supplier<BiomeBase>> var5
	) {
		this(var1, var0);
		if (var3) {
			this.b();
		}

		if (var4) {
			this.a();
		}

		this.f.addAll(var2);
		this.h();
		if (var5.isEmpty()) {
			LOGGER.error("Unknown biome, defaulting to plains");
			this.g = () -> var0.d(Biomes.PLAINS);
		} else {
			this.g = var5.get();
		}
	}

	public GeneratorSettingsFlat(StructureSettings var0, IRegistry<BiomeBase> var1) {
		this.d = var1;
		this.e = var0;
		this.g = () -> var1.d(Biomes.PLAINS);
	}

	public static GeneratorSettingsFlat a(IRegistry<BiomeBase> var0) {
		StructureSettings var1 = new StructureSettings(
			Optional.of(StructureSettings.c),
			Maps.newHashMap(
				ImmutableMap.of(StructureGenerator.VILLAGE, StructureSettings.b.get(StructureGenerator.VILLAGE))
			)
		);
		GeneratorSettingsFlat var2 = new GeneratorSettingsFlat(var1, var0);
		var2.g = () -> var0.d(Biomes.PLAINS);
		var2.f().add(new WorldGenFlatLayerInfo(1, Blocks.BEDROCK));
		var2.f().add(new WorldGenFlatLayerInfo(2, Blocks.DIRT));
		var2.f().add(new WorldGenFlatLayerInfo(1, Blocks.GRASS_BLOCK));
		var2.h();
		return var2;
	}

	public void a() {
		this.j = true;
	}

	public void b() {
		this.k = true;
	}

	public BiomeBase c() {
		BiomeBase var0 = this.e();
		BiomeSettingsGeneration var1 = var0.e();
		a var2 = new a().a(var1.d());
		if (this.k) {
			var2.a(Decoration.LAKES, BiomeDecoratorGroups.LAKE_WATER);
			var2.a(Decoration.LAKES, BiomeDecoratorGroups.LAKE_LAVA);
		}

		for (var var4 : this.e.a().entrySet()) {
			var2.a(var1.a(c.get(var4.getKey())));
		}

		boolean var3 = (!this.i || this.d.c(var0).equals(Optional.of(Biomes.THE_VOID))) && this.j;
		if (var3) {
			var var4 = var1.c();

			for (int var5 = 0; var5 < var4.size(); ++var5) {
				if (var5 != Decoration.UNDERGROUND_STRUCTURES.ordinal() && var5 != Decoration.SURFACE_STRUCTURES.ordinal()) {
					for (Supplier<WorldGenFeatureConfigured<?, ?>> var8 : var4.get(var5)) {
						var2.a(var5, var8);
					}
				}
			}
		}

		IBlockData[] var4 = this.g();

		for (int var5 = 0; var5 < var4.length; ++var5) {
			IBlockData var6 = var4[var5];
			if (var6 != null && !Type.MOTION_BLOCKING.e().test(var6)) {
				this.h[var5] = null;
				var2.a(Decoration.TOP_LAYER_MODIFICATION, WorldGenerator.FILL_LAYER.b(new WorldGenFeatureFillConfiguration(var5, var6)));
			}
		}

		return new BiomeBase.a()
			.a(var0.c())
			.a(var0.t())
			.a(var0.h())
			.b(var0.j())
			.c(var0.k())
			.d(var0.getHumidity())
			.a(var0.l())
			.a(var2.a())
			.a(var0.b())
			.a();
	}

	public StructureSettings d() {
		return this.e;
	}

	public BiomeBase e() {
		return this.g.get();
	}

	public List<WorldGenFlatLayerInfo> f() {
		return this.f;
	}

	public IBlockData[] g() {
		return this.h;
	}

	public void h() {
		Arrays.fill(this.h, 0, this.h.length, null);
		int var0 = 0;

		for (WorldGenFlatLayerInfo var2 : this.f) {
			var2.a(var0);
			var0 += var2.a();
		}

		this.i = true;

		for (WorldGenFlatLayerInfo var1 : this.f) {
			for (int var2 = var1.c(); var2 < var1.c() + var1.a(); ++var2) {
				IBlockData var3 = var1.b();
				if (!var3.a(Blocks.AIR)) {
					this.i = false;
					this.h[var2] = var3;
				}
			}
		}
	}
}
