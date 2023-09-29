package net.minecraft.world.level.newbiome.layer;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.SystemUtils;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.AreaLazy;
import net.minecraft.world.level.newbiome.context.AreaContextTransformed;
import net.minecraft.world.level.newbiome.context.WorldGenContextArea;

import java.util.function.LongFunction;

public class GenLayers {
	private static final Int2IntMap a = SystemUtils.a(new Int2IntOpenHashMap(), map -> {
		a(map, GenLayers.Type.BEACH, 16);
		a(map, GenLayers.Type.BEACH, 26);
		a(map, GenLayers.Type.DESERT, 2);
		a(map, GenLayers.Type.DESERT, 17);
		a(map, GenLayers.Type.DESERT, 130);
		a(map, GenLayers.Type.EXTREME_HILLS, 131);
		a(map, GenLayers.Type.EXTREME_HILLS, 162);
		a(map, GenLayers.Type.EXTREME_HILLS, 20);
		a(map, GenLayers.Type.EXTREME_HILLS, 3);
		a(map, GenLayers.Type.EXTREME_HILLS, 34);
		a(map, GenLayers.Type.FOREST, 27);
		a(map, GenLayers.Type.FOREST, 28);
		a(map, GenLayers.Type.FOREST, 29);
		a(map, GenLayers.Type.FOREST, 157);
		a(map, GenLayers.Type.FOREST, 132);
		a(map, GenLayers.Type.FOREST, 4);
		a(map, GenLayers.Type.FOREST, 155);
		a(map, GenLayers.Type.FOREST, 156);
		a(map, GenLayers.Type.FOREST, 18);
		a(map, GenLayers.Type.ICY, 140);
		a(map, GenLayers.Type.ICY, 13);
		a(map, GenLayers.Type.ICY, 12);
		a(map, GenLayers.Type.JUNGLE, 168);
		a(map, GenLayers.Type.JUNGLE, 169);
		a(map, GenLayers.Type.JUNGLE, 21);
		a(map, GenLayers.Type.JUNGLE, 23);
		a(map, GenLayers.Type.JUNGLE, 22);
		a(map, GenLayers.Type.JUNGLE, 149);
		a(map, GenLayers.Type.JUNGLE, 151);
		a(map, GenLayers.Type.MESA, 37);
		a(map, GenLayers.Type.MESA, 165);
		a(map, GenLayers.Type.MESA, 167);
		a(map, GenLayers.Type.MESA, 166);
		a(map, GenLayers.Type.BADLANDS_PLATEAU, 39);
		a(map, GenLayers.Type.BADLANDS_PLATEAU, 38);
		a(map, GenLayers.Type.MUSHROOM, 14);
		a(map, GenLayers.Type.MUSHROOM, 15);
		a(map, GenLayers.Type.NONE, 25);
		a(map, GenLayers.Type.OCEAN, 46);
		a(map, GenLayers.Type.OCEAN, 49);
		a(map, GenLayers.Type.OCEAN, 50);
		a(map, GenLayers.Type.OCEAN, 48);
		a(map, GenLayers.Type.OCEAN, 24);
		a(map, GenLayers.Type.OCEAN, 47);
		a(map, GenLayers.Type.OCEAN, 10);
		a(map, GenLayers.Type.OCEAN, 45);
		a(map, GenLayers.Type.OCEAN, 0);
		a(map, GenLayers.Type.OCEAN, 44);
		a(map, GenLayers.Type.PLAINS, 1);
		a(map, GenLayers.Type.PLAINS, 129);
		a(map, GenLayers.Type.RIVER, 11);
		a(map, GenLayers.Type.RIVER, 7);
		a(map, GenLayers.Type.SAVANNA, 35);
		a(map, GenLayers.Type.SAVANNA, 36);
		a(map, GenLayers.Type.SAVANNA, 163);
		a(map, GenLayers.Type.SAVANNA, 164);
		a(map, GenLayers.Type.SWAMP, 6);
		a(map, GenLayers.Type.SWAMP, 134);
		a(map, GenLayers.Type.TAIGA, 160);
		a(map, GenLayers.Type.TAIGA, 161);
		a(map, GenLayers.Type.TAIGA, 32);
		a(map, GenLayers.Type.TAIGA, 33);
		a(map, GenLayers.Type.TAIGA, 30);
		a(map, GenLayers.Type.TAIGA, 31);
		a(map, GenLayers.Type.TAIGA, 158);
		a(map, GenLayers.Type.TAIGA, 5);
		a(map, GenLayers.Type.TAIGA, 19);
		a(map, GenLayers.Type.TAIGA, 133);
	});

	private static <T extends Area, C extends AreaContextTransformed<T>> AreaFactory<T> a(long i, AreaFactory<T> areafactory, int j, LongFunction<C> longfunction) {
		AreaFactory<T> areafactory1 = areafactory;

		for (int k = 0; k < j; ++k) {
			areafactory1 = GenLayerZoom.NORMAL.a(longfunction.apply(i + (long) k), areafactory1);
		}

		return areafactory1;
	}

	private static <T extends Area, C extends AreaContextTransformed<T>> AreaFactory<T> a(boolean flag, int i, int j, LongFunction<C> longfunction) {
		AreaFactory<T> areafactory = LayerIsland.INSTANCE.a(longfunction.apply(1L));

		areafactory = GenLayerZoom.FUZZY.a(longfunction.apply(2000L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(1L), areafactory);
		areafactory = GenLayerZoom.NORMAL.a(longfunction.apply(2001L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(2L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(50L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(70L), areafactory);
		areafactory = GenLayerIcePlains.INSTANCE.a(longfunction.apply(2L), areafactory);
		AreaFactory<T> areafactory1 = GenLayerOceanEdge.INSTANCE.a(longfunction.apply(2L));

		areafactory1 = a(2001L, areafactory1, 6, longfunction);
		areafactory = GenLayerTopSoil.INSTANCE.a(longfunction.apply(2L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(3L), areafactory);
		areafactory = GenLayerSpecial.Special1.INSTANCE.a(longfunction.apply(2L), areafactory);
		areafactory = GenLayerSpecial.Special2.INSTANCE.a(longfunction.apply(2L), areafactory);
		areafactory = GenLayerSpecial.Special3.INSTANCE.a(longfunction.apply(3L), areafactory);
		areafactory = GenLayerZoom.NORMAL.a(longfunction.apply(2002L), areafactory);
		areafactory = GenLayerZoom.NORMAL.a(longfunction.apply(2003L), areafactory);
		areafactory = GenLayerIsland.INSTANCE.a(longfunction.apply(4L), areafactory);
		areafactory = GenLayerMushroomIsland.INSTANCE.a(longfunction.apply(5L), areafactory);
		areafactory = GenLayerDeepOcean.INSTANCE.a(longfunction.apply(4L), areafactory);
		areafactory = a(1000L, areafactory, 0, longfunction);
		AreaFactory<T> areafactory2 = a(1000L, areafactory, 0, longfunction);

		areafactory2 = GenLayerCleaner.INSTANCE.a(longfunction.apply(100L), areafactory2);
		AreaFactory<T> areafactory3 = (new GenLayerBiome(flag)).a(longfunction.apply(200L), areafactory);

		areafactory3 = GenLayerJungle.INSTANCE.a(longfunction.apply(1001L), areafactory3);
		areafactory3 = a(1000L, areafactory3, 2, longfunction);
		areafactory3 = GenLayerDesert.INSTANCE.a(longfunction.apply(1000L), areafactory3);
		AreaFactory<T> areafactory4 = a(1000L, areafactory2, 2, longfunction);

		areafactory3 = GenLayerRegionHills.INSTANCE.a(longfunction.apply(1000L), areafactory3, areafactory4);
		areafactory2 = a(1000L, areafactory2, 2, longfunction);
		areafactory2 = a(1000L, areafactory2, j, longfunction);
		areafactory2 = GenLayerRiver.INSTANCE.a(longfunction.apply(1L), areafactory2);
		areafactory2 = GenLayerSmooth.INSTANCE.a(longfunction.apply(1000L), areafactory2);
		areafactory3 = GenLayerPlains.INSTANCE.a(longfunction.apply(1001L), areafactory3);

		for (int k = 0; k < i; ++k) {
			areafactory3 = GenLayerZoom.NORMAL.a(longfunction.apply(1000 + k), areafactory3);
			if (k == 0) {
				areafactory3 = GenLayerIsland.INSTANCE.a(longfunction.apply(3L), areafactory3);
			}

			if (k == 1 || i == 1) {
				areafactory3 = GenLayerMushroomShore.INSTANCE.a(longfunction.apply(1000L), areafactory3);
			}
		}

		areafactory3 = GenLayerSmooth.INSTANCE.a(longfunction.apply(1000L), areafactory3);
		areafactory3 = GenLayerRiverMix.INSTANCE.a(longfunction.apply(100L), areafactory3, areafactory2);
		areafactory3 = GenLayerOcean.INSTANCE.a(longfunction.apply(100L), areafactory3, areafactory1);
		return areafactory3;
	}

	public static GenLayer a(long i, boolean flag, int j, int k) {
		AreaFactory<AreaLazy> areafactory = a(flag, j, k, (l) -> {
			return new WorldGenContextArea(25, i, l);
		});

		return new GenLayer(areafactory);
	}

	public static boolean a(int i, int j) {
		return i == j || GenLayers.a.get(i) == GenLayers.a.get(j);
	}

	private static void a(Int2IntOpenHashMap int2intopenhashmap, GenLayers.Type genlayers_type, int i) {
		int2intopenhashmap.put(i, genlayers_type.ordinal());
	}

	protected static boolean a(int i) {
		return i == 44 || i == 45 || i == 0 || i == 46 || i == 10 || i == 47 || i == 48 || i == 24 || i == 49 || i == 50;
	}

	protected static boolean b(int i) {
		return i == 44 || i == 45 || i == 0 || i == 46 || i == 10;
	}

	enum Type {
		NONE, TAIGA, EXTREME_HILLS, JUNGLE, MESA, BADLANDS_PLATEAU, PLAINS, SAVANNA, ICY, BEACH, FOREST, OCEAN, DESERT, RIVER, SWAMP, MUSHROOM;

		Type() {
		}
	}
}
