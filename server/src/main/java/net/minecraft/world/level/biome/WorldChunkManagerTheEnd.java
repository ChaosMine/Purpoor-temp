package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kamillaova.paper.util.NettyUtil;
import io.netty.util.concurrent.FastThreadLocal;
import it.unimi.dsi.fastutil.HashCommon;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.util.MathHelper;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.synth.NoiseGenerator3Handler;

import java.util.List;
import java.util.Map;

import static dev.kamillaova.paper.util.NettyUtil.fastThreadLocal;

public class WorldChunkManagerTheEnd extends WorldChunkManager {

    public static final Codec<WorldChunkManagerTheEnd> e = RecordCodecBuilder.create((instance) -> {
        return instance.group(RegistryLookupCodec.a(IRegistry.ay).forGetter((worldchunkmanagertheend) -> {
            return worldchunkmanagertheend.g;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((worldchunkmanagertheend) -> {
            return worldchunkmanagertheend.h;
        })).apply(instance, instance.stable(WorldChunkManagerTheEnd::new));
    });
    private final NoiseGenerator3Handler f;
    private final IRegistry<BiomeBase> g;
    private final long h;
    private final BiomeBase i;
    private final BiomeBase j;
    private final BiomeBase k;
    private final BiomeBase l;
    private final BiomeBase m;
    // Paper start
    private static final class NoiseCache {
        public long[] keys = new long[8192];
        public float[] values = new float[8192];
        public NoiseCache() {
            java.util.Arrays.fill(keys, Long.MIN_VALUE);
        }
    }
    private static final FastThreadLocal<Map<NoiseGenerator3Handler, NoiseCache>> noiseCache = fastThreadLocal(java.util.WeakHashMap::new);
    // Paper end

    public WorldChunkManagerTheEnd(IRegistry<BiomeBase> iregistry, long i) {
        this(iregistry, i, (BiomeBase) iregistry.d(Biomes.THE_END), (BiomeBase) iregistry.d(Biomes.END_HIGHLANDS), (BiomeBase) iregistry.d(Biomes.END_MIDLANDS), (BiomeBase) iregistry.d(Biomes.SMALL_END_ISLANDS), (BiomeBase) iregistry.d(Biomes.END_BARRENS));
    }

    private WorldChunkManagerTheEnd(IRegistry<BiomeBase> iregistry, long i, BiomeBase biomebase, BiomeBase biomebase1, BiomeBase biomebase2, BiomeBase biomebase3, BiomeBase biomebase4) {
        super((List) ImmutableList.of(biomebase, biomebase1, biomebase2, biomebase3, biomebase4));
        this.g = iregistry;
        this.h = i;
        this.i = biomebase;
        this.j = biomebase1;
        this.k = biomebase2;
        this.l = biomebase3;
        this.m = biomebase4;
        SeededRandom seededrandom = new SeededRandom(i);
        seededrandom.secureseed_setSeed(Globals.worldSeed, 0, 0, Globals.dimension.get(), Globals.END_BIOME_NOISE_SALT, 0);
        seededrandom.a(17292);
        this.f = new NoiseGenerator3Handler(seededrandom);
    }

    @Override
    protected Codec<? extends WorldChunkManager> a() {
        return WorldChunkManagerTheEnd.e;
    }

    @Override
    public BiomeBase getBiome(int i, int j, int k) {
        int l = i >> 2;
        int i1 = k >> 2;

        if ((long) l * (long) l + (long) i1 * (long) i1 <= 4096L) {
            return this.i;
        } else {
            float f = a(this.f, l * 2 + 1, i1 * 2 + 1);

            return f > 40.0F ? this.j : (f >= 0.0F ? this.k : (f < -20.0F ? this.l : this.m));
        }
    }

    public boolean b(long i) {
        return this.h == i;
    }

    public static float a(NoiseGenerator3Handler noisegenerator3handler, int i, int j) {
        int k = i / 2;
        int l = j / 2;
        int i1 = i % 2;
        int j1 = j % 2;
        // Paper start - cast ints to long to avoid integer overflow
        float f = 100.0F - MathHelper.sqrt((long) i * (long) i + (long) j * (long) j) * 8.0F;
        // Paper end

        f = MathHelper.a(f, -100.0F, 80.0F);

        NoiseCache cache = noiseCache.get().computeIfAbsent(noisegenerator3handler, m -> new NoiseCache()); // Paper
        for (int k1 = -12; k1 <= 12; ++k1) {
            for (int l1 = -12; l1 <= 12; ++l1) {
                long i2 = (long) (k + k1);
                long j2 = (long) (l + l1);

                // Paper start - Significantly improve end generation performance by using a noise cache
                long key = ChunkCoordIntPair.pair((int) i2, (int) j2);
                int index = (int) HashCommon.mix(key) & 8191;
                float f1 = Float.MIN_VALUE;
                if (cache.keys[index] == key) {
                    f1 = cache.values[index];
                } else {
                    if (i2 * i2 + j2 * j2 > 4096L && noisegenerator3handler.a((double) i2, (double) j2) < -0.8999999761581421D) {
                        f1 = (MathHelper.e((float) i2) * 3439.0F + MathHelper.e((float) j2) * 147.0F) % 13.0F + 9.0F;
                    }
                    cache.keys[index] = key;
                    cache.values[index] = f1;
                }
                if (f1 != Float.MIN_VALUE) {
                    // Paper end
                    float f2 = (float) (i1 - k1 * 2);
                    float f3 = (float) (j1 - l1 * 2);
                    float f4 = 100.0F - MathHelper.c(f2 * f2 + f3 * f3) * f1;

                    f4 = MathHelper.a(f4, -100.0F, 80.0F);
                    f = Math.max(f, f4);
                }
            }
        }

        return f;
    }
}
