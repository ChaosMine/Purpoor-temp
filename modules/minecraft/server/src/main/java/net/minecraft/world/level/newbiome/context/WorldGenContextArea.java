package net.minecraft.world.level.newbiome.context;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.earthcomputer.secureseed.Globals;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorPerlin;
import net.minecraft.world.level.newbiome.area.AreaLazy;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer8;

public class WorldGenContextArea implements AreaContextTransformed<AreaLazy> {

    private final Long2IntLinkedOpenHashMap a;
    private final int b;
    private final NoiseGeneratorPerlin c;
    private final long d;
    private long e;

    private final SeededRandom rand = new SeededRandom(0);
    private long salt;

    public WorldGenContextArea(int i, long j, long k) {
        this.d = b(j, k);
        var rand = new SeededRandom(0);
        rand.secureseed_setSeed(Globals.worldSeed, 0, 0, Globals.dimension.get(), Globals.BIOME_NOISE_SALT, 0);
        this.c = new NoiseGeneratorPerlin(rand);
        this.a = new Long2IntLinkedOpenHashMap(16, 0.25F);
        this.a.defaultReturnValue(Integer.MIN_VALUE);
        this.b = i;
        this.salt = k;
    }

    @Override
    public AreaLazy a(AreaTransformer8 areatransformer8) {
        return new AreaLazy(this.a, this.b, areatransformer8);
    }

    public AreaLazy a(AreaTransformer8 areatransformer8, AreaLazy arealazy) {
        return new AreaLazy(this.a, Math.min(1024, arealazy.a() * 4), areatransformer8);
    }

    public AreaLazy a(AreaTransformer8 areatransformer8, AreaLazy arealazy, AreaLazy arealazy1) {
        return new AreaLazy(this.a, Math.min(1024, Math.max(arealazy.a(), arealazy1.a()) * 4), areatransformer8);
    }

    @Override
    public void a(long i, long j) {
        rand.secureseed_setSeed(Globals.worldSeed, (int) i, (int) j, Globals.dimension.get(), Globals.BIOME_SALT, salt);
    }

    @Override
    public int a(int i) {
        return rand.nextInt(i);
    }

    @Override
    public NoiseGeneratorPerlin b() {
        return this.c;
    }

    private static long b(long i, long j) {
        long k = LinearCongruentialGenerator.a(j, j);

        k = LinearCongruentialGenerator.a(k, j);
        k = LinearCongruentialGenerator.a(k, j);
        long l = LinearCongruentialGenerator.a(i, k);

        l = LinearCongruentialGenerator.a(l, k);
        l = LinearCongruentialGenerator.a(l, k);
        return l;
    }
}
