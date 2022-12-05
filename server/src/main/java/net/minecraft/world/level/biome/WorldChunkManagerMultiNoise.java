package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.IRegistry;
import net.minecraft.data.worldgen.biome.BiomeRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorNormal;

public class WorldChunkManagerMultiNoise extends WorldChunkManager {

    private static final _a g = new _a(-7, ImmutableList.of(1.0D, 1.0D));
    public static final MapCodec<WorldChunkManagerMultiNoise> e = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(Codec.LONG.fieldOf("seed").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.r;
        }), RecordCodecBuilder.<Pair<BiomeBase._c, Supplier<BiomeBase>>>create((instance1) -> {
            return instance1.group(BiomeBase._c.a.fieldOf("parameters").forGetter(Pair::getFirst), BiomeBase.d.fieldOf("biome").forGetter(Pair::getSecond)).apply(instance1, Pair::of);
        }).listOf().fieldOf("biomes").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.p;
        }), WorldChunkManagerMultiNoise._a.a.fieldOf("temperature_noise").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.h;
        }), WorldChunkManagerMultiNoise._a.a.fieldOf("humidity_noise").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.i;
        }), WorldChunkManagerMultiNoise._a.a.fieldOf("altitude_noise").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.j;
        }), WorldChunkManagerMultiNoise._a.a.fieldOf("weirdness_noise").forGetter((worldchunkmanagermultinoise) -> {
            return worldchunkmanagermultinoise.k;
        })).apply(instance, WorldChunkManagerMultiNoise::new);
    });
    public static final Codec<WorldChunkManagerMultiNoise> f = Codec.mapEither(WorldChunkManagerMultiNoise._c.a, WorldChunkManagerMultiNoise.e).xmap((either) -> {
        return (WorldChunkManagerMultiNoise) either.map(WorldChunkManagerMultiNoise._c::d, Function.identity());
    }, (worldchunkmanagermultinoise) -> {
        return (Either) worldchunkmanagermultinoise.d().map(Either::left).orElseGet(() -> {
            return Either.right(worldchunkmanagermultinoise);
        });
    }).codec();
    private final _a h;
    private final _a i;
    private final _a j;
    private final _a k;
    private final NoiseGeneratorNormal l;
    private final NoiseGeneratorNormal m;
    private final NoiseGeneratorNormal n;
    private final NoiseGeneratorNormal o;
    private final List<Pair<BiomeBase._c, Supplier<BiomeBase>>> p;
    private final boolean q;
    private final long r;
    private final Optional<Pair<IRegistry<BiomeBase>, _b>> s;

    private WorldChunkManagerMultiNoise(long i, List<Pair<BiomeBase._c, Supplier<BiomeBase>>> list, Optional<Pair<IRegistry<BiomeBase>, _b>> optional) {
        this(i, list, WorldChunkManagerMultiNoise.g, WorldChunkManagerMultiNoise.g, WorldChunkManagerMultiNoise.g, WorldChunkManagerMultiNoise.g, optional);
    }

    private WorldChunkManagerMultiNoise(long i, List<Pair<BiomeBase._c, Supplier<BiomeBase>>> list, _a worldchunkmanagermultinoise_a, _a worldchunkmanagermultinoise_a1, _a worldchunkmanagermultinoise_a2, _a worldchunkmanagermultinoise_a3) {
        this(i, list, worldchunkmanagermultinoise_a, worldchunkmanagermultinoise_a1, worldchunkmanagermultinoise_a2, worldchunkmanagermultinoise_a3, Optional.empty());
    }

    private WorldChunkManagerMultiNoise(long i, List<Pair<BiomeBase._c, Supplier<BiomeBase>>> list, _a worldchunkmanagermultinoise_a, _a worldchunkmanagermultinoise_a1, _a worldchunkmanagermultinoise_a2, _a worldchunkmanagermultinoise_a3, Optional<Pair<IRegistry<BiomeBase>, _b>> optional) {
        super(list.stream().map(Pair::getSecond));
        this.r = i;
        this.s = optional;
        this.h = worldchunkmanagermultinoise_a;
        this.i = worldchunkmanagermultinoise_a1;
        this.j = worldchunkmanagermultinoise_a2;
        this.k = worldchunkmanagermultinoise_a3;
        this.l = NoiseGeneratorNormal.a(new SeededRandom(i), worldchunkmanagermultinoise_a.a(), worldchunkmanagermultinoise_a.b());
        this.m = NoiseGeneratorNormal.a(new SeededRandom(i + 1L), worldchunkmanagermultinoise_a1.a(), worldchunkmanagermultinoise_a1.b());
        this.n = NoiseGeneratorNormal.a(new SeededRandom(i + 2L), worldchunkmanagermultinoise_a2.a(), worldchunkmanagermultinoise_a2.b());
        this.o = NoiseGeneratorNormal.a(new SeededRandom(i + 3L), worldchunkmanagermultinoise_a3.a(), worldchunkmanagermultinoise_a3.b());
        this.p = list;
        this.q = false;
    }

    @Override
    protected Codec<? extends WorldChunkManager> a() {
        return WorldChunkManagerMultiNoise.f;
    }

    private Optional<_c> d() {
        return this.s.map((pair) -> {
            return new _c((_b) pair.getSecond(), (IRegistry) pair.getFirst(), this.r);
        });
    }

    @Override
    public BiomeBase getBiome(int i, int j, int k) {
        int l = this.q ? j : 0;
        BiomeBase._c biomebase_c = new BiomeBase._c((float) this.l.a((double) i, (double) l, (double) k), (float) this.m.a((double) i, (double) l, (double) k), (float) this.n.a((double) i, (double) l, (double) k), (float) this.o.a((double) i, (double) l, (double) k), 0.0F);

        return (BiomeBase) this.p.stream().min(Comparator.comparing((pair) -> {
            return ((BiomeBase._c) pair.getFirst()).a(biomebase_c);
        })).map(Pair::getSecond).map(Supplier::get).orElse(BiomeRegistry.b);
    }

    public boolean b(long i) {
        return this.r == i && this.s.isPresent() && Objects.equals(((Pair) this.s.get()).getSecond(), WorldChunkManagerMultiNoise._b.a);
    }

    public static class _b {

        private static final Map<MinecraftKey, _b> b = Maps.newHashMap();
        public static final _b a = new _b(new MinecraftKey("nether"), (worldchunkmanagermultinoise_b, iregistry, olong) -> {
            return new WorldChunkManagerMultiNoise(olong, ImmutableList.of(Pair.of(new BiomeBase._c(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
                return (BiomeBase) iregistry.d(Biomes.NETHER_WASTES);
            }), Pair.of(new BiomeBase._c(0.0F, -0.5F, 0.0F, 0.0F, 0.0F), () -> {
                return (BiomeBase) iregistry.d(Biomes.SOUL_SAND_VALLEY);
            }), Pair.of(new BiomeBase._c(0.4F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
                return (BiomeBase) iregistry.d(Biomes.CRIMSON_FOREST);
            }), Pair.of(new BiomeBase._c(0.0F, 0.5F, 0.0F, 0.0F, 0.375F), () -> {
                return (BiomeBase) iregistry.d(Biomes.WARPED_FOREST);
            }), Pair.of(new BiomeBase._c(-0.5F, 0.0F, 0.0F, 0.0F, 0.175F), () -> {
                return (BiomeBase) iregistry.d(Biomes.BASALT_DELTAS);
            })), Optional.of(Pair.of(iregistry, worldchunkmanagermultinoise_b)));
        });
        private final MinecraftKey c;
        private final Function3<_b, IRegistry<BiomeBase>, Long, WorldChunkManagerMultiNoise> d;

        public _b(MinecraftKey minecraftkey, Function3<_b, IRegistry<BiomeBase>, Long, WorldChunkManagerMultiNoise> function3) {
            this.c = minecraftkey;
            this.d = function3;
            WorldChunkManagerMultiNoise._b.b.put(minecraftkey, this);
        }

        public WorldChunkManagerMultiNoise a(IRegistry<BiomeBase> iregistry, long i) {
            return (WorldChunkManagerMultiNoise) this.d.apply(this, iregistry, i);
        }
    }

    static final class _c {

        public static final MapCodec<_c> a = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(MinecraftKey.a.flatXmap((minecraftkey) -> {
                return Optional.ofNullable(WorldChunkManagerMultiNoise._b.b.get(minecraftkey)).map(DataResult::success).orElseGet(() -> {
                    return DataResult.error("Unknown preset: " + minecraftkey);
                });
            }, (worldchunkmanagermultinoise_b) -> {
                return DataResult.success(worldchunkmanagermultinoise_b.c);
            }).fieldOf("preset").stable().forGetter(WorldChunkManagerMultiNoise._c::a), RegistryLookupCodec.a(IRegistry.ay).forGetter(WorldChunkManagerMultiNoise._c::b), Codec.LONG.fieldOf("seed").stable().forGetter(WorldChunkManagerMultiNoise._c::c)).apply(instance, instance.stable(_c::new));
        });
        private final _b _b;
        private final IRegistry<BiomeBase> c;
        private final long d;

        private _c(_b worldchunkmanagermultinoise_b, IRegistry<BiomeBase> iregistry, long i) {
            this._b = worldchunkmanagermultinoise_b;
            this.c = iregistry;
            this.d = i;
        }

        public _b a() {
            return this._b;
        }

        public IRegistry<BiomeBase> b() {
            return this.c;
        }

        public long c() {
            return this.d;
        }

        public WorldChunkManagerMultiNoise d() {
            return this._b.a(this.c, this.d);
        }
    }

    static class _a {

        private final int b;
        private final DoubleList c;
        public static final Codec<_a> a = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codec.INT.fieldOf("firstOctave").forGetter(WorldChunkManagerMultiNoise._a::a), Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(WorldChunkManagerMultiNoise._a::b)).apply(instance, _a::new);
        });

        public _a(int i, List<Double> list) {
            this.b = i;
            this.c = new DoubleArrayList(list);
        }

        public int a() {
            return this.b;
        }

        public DoubleList b() {
            return this.c;
        }
    }
}
