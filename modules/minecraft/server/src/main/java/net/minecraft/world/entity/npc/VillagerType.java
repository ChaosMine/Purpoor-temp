package net.minecraft.world.entity.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.SystemUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;

public final class VillagerType {
	public static final VillagerType DESERT = a("desert");
	public static final VillagerType JUNGLE = a("jungle");
	public static final VillagerType PLAINS = a("plains");
	public static final VillagerType SAVANNA = a("savanna");
	public static final VillagerType SNOW = a("snow");
	public static final VillagerType SWAMP = a("swamp");
	public static final VillagerType TAIGA = a("taiga");
	private static final Map<ResourceKey<BiomeBase>, VillagerType> i = SystemUtils.a(new HashMap<>(), map -> {
		map.put(Biomes.BADLANDS, VillagerType.DESERT);
		map.put(Biomes.BADLANDS_PLATEAU, VillagerType.DESERT);
		map.put(Biomes.DESERT, VillagerType.DESERT);
		map.put(Biomes.DESERT_HILLS, VillagerType.DESERT);
		map.put(Biomes.DESERT_LAKES, VillagerType.DESERT);
		map.put(Biomes.ERODED_BADLANDS, VillagerType.DESERT);
		map.put(Biomes.MODIFIED_BADLANDS_PLATEAU, VillagerType.DESERT);
		map.put(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, VillagerType.DESERT);
		map.put(Biomes.WOODED_BADLANDS_PLATEAU, VillagerType.DESERT);
		map.put(Biomes.BAMBOO_JUNGLE, VillagerType.JUNGLE);
		map.put(Biomes.BAMBOO_JUNGLE_HILLS, VillagerType.JUNGLE);
		map.put(Biomes.JUNGLE, VillagerType.JUNGLE);
		map.put(Biomes.JUNGLE_EDGE, VillagerType.JUNGLE);
		map.put(Biomes.JUNGLE_HILLS, VillagerType.JUNGLE);
		map.put(Biomes.MODIFIED_JUNGLE, VillagerType.JUNGLE);
		map.put(Biomes.MODIFIED_JUNGLE_EDGE, VillagerType.JUNGLE);
		map.put(Biomes.SAVANNA_PLATEAU, VillagerType.SAVANNA);
		map.put(Biomes.SAVANNA, VillagerType.SAVANNA);
		map.put(Biomes.SHATTERED_SAVANNA, VillagerType.SAVANNA);
		map.put(Biomes.SHATTERED_SAVANNA_PLATEAU, VillagerType.SAVANNA);
		map.put(Biomes.DEEP_FROZEN_OCEAN, VillagerType.SNOW);
		map.put(Biomes.FROZEN_OCEAN, VillagerType.SNOW);
		map.put(Biomes.FROZEN_RIVER, VillagerType.SNOW);
		map.put(Biomes.ICE_SPIKES, VillagerType.SNOW);
		map.put(Biomes.SNOWY_BEACH, VillagerType.SNOW);
		map.put(Biomes.SNOWY_MOUNTAINS, VillagerType.SNOW);
		map.put(Biomes.SNOWY_TAIGA, VillagerType.SNOW);
		map.put(Biomes.SNOWY_TAIGA_HILLS, VillagerType.SNOW);
		map.put(Biomes.SNOWY_TAIGA_MOUNTAINS, VillagerType.SNOW);
		map.put(Biomes.SNOWY_TUNDRA, VillagerType.SNOW);
		map.put(Biomes.SWAMP, VillagerType.SWAMP);
		map.put(Biomes.SWAMP_HILLS, VillagerType.SWAMP);
		map.put(Biomes.GIANT_SPRUCE_TAIGA, VillagerType.TAIGA);
		map.put(Biomes.GIANT_SPRUCE_TAIGA_HILLS, VillagerType.TAIGA);
		map.put(Biomes.GIANT_TREE_TAIGA, VillagerType.TAIGA);
		map.put(Biomes.GIANT_TREE_TAIGA_HILLS, VillagerType.TAIGA);
		map.put(Biomes.GRAVELLY_MOUNTAINS, VillagerType.TAIGA);
		map.put(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, VillagerType.TAIGA);
		map.put(Biomes.MOUNTAIN_EDGE, VillagerType.TAIGA);
		map.put(Biomes.MOUNTAINS, VillagerType.TAIGA);
		map.put(Biomes.TAIGA, VillagerType.TAIGA);
		map.put(Biomes.TAIGA_HILLS, VillagerType.TAIGA);
		map.put(Biomes.TAIGA_MOUNTAINS, VillagerType.TAIGA);
		map.put(Biomes.WOODED_MOUNTAINS, VillagerType.TAIGA);
	});
	private final String h;

	private VillagerType(String s) {
		this.h = s;
	}

	private static VillagerType a(String s) {
		return IRegistry.a(IRegistry.VILLAGER_TYPE, new MinecraftKey(s), new VillagerType(s));
	}

	public static VillagerType a(Optional<ResourceKey<BiomeBase>> biomeBaseKey) {
		return biomeBaseKey.flatMap(key -> Optional.ofNullable(VillagerType.i.get(key)))
			.orElse(VillagerType.PLAINS);
	}

	public String toString() {
		return this.h;
	}
}
