// mc-dev import
package net.minecraft.world.level;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.SectionPosition;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IStructureAccess;
import net.minecraft.world.level.levelgen.GeneratorSettings;
import net.minecraft.world.level.levelgen.feature.StructureGenerator;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class StructureManager {

    private final GeneratorAccess a; public GeneratorAccess getLevel() { return a; } // Paper - OBFHELPER
    private final GeneratorSettings b;

    public StructureManager(GeneratorAccess generatoraccess, GeneratorSettings generatorsettings) {
        this.a = generatoraccess;
        this.b = generatorsettings;
    }

    public StructureManager a(RegionLimitedWorldAccess regionlimitedworldaccess) {
        if (regionlimitedworldaccess.getMinecraftWorld() != this.a) {
            throw new IllegalStateException("Using invalid feature manager (source level: " + regionlimitedworldaccess.getMinecraftWorld() + ", region: " + regionlimitedworldaccess);
        } else {
            return new StructureManager(regionlimitedworldaccess, this.b);
        }
    }

    public Stream<? extends StructureStart<?>> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator) {
        return this.a.getChunkAt(sectionposition.a(), sectionposition.c(), ChunkStatus.STRUCTURE_REFERENCES).b(structuregenerator).stream().map((olong) -> {
            return SectionPosition.a(new ChunkCoordIntPair(olong), 0);
        }).map((sectionposition1) -> {
            return this.a(sectionposition1, structuregenerator, this.a.getChunkAt(sectionposition1.a(), sectionposition1.c(), ChunkStatus.STRUCTURE_STARTS));
        }).filter((structurestart) -> {
            return structurestart != null && structurestart.e();
        });
    }

    // Paper start - remove structure streams
    public java.util.List<StructureStart<?>> getFeatureStarts(SectionPosition sectionPosition, StructureGenerator<?> structureGenerator) {
        // Tuinity start - add world parameter
        return this.getFeatureStarts(sectionPosition, structureGenerator, null);
    }
    public java.util.List<StructureStart<?>> getFeatureStarts(SectionPosition sectionPosition, StructureGenerator<?> structureGenerator, IWorldReader world) {
        // Tuinity end - add world parameter
        java.util.List<StructureStart<?>> list = new ObjectArrayList<>();
        for (Long curLong: (world == null ? getLevel() : world).getChunkAt(sectionPosition.a(), sectionPosition.c(), ChunkStatus.STRUCTURE_REFERENCES).b(structureGenerator)) { // Tuinity - fix deadlock on world gen - chunk can be unloaded while generating, so we should be using the generator's regionlimitedaccess so we always get the chunk
            SectionPosition sectionPosition1 = SectionPosition.a(new ChunkCoordIntPair(curLong), 0);
            StructureStart<?> structurestart = a(sectionPosition1, structureGenerator, getLevel().getChunkAt(sectionPosition1.a(), sectionPosition1.c(), ChunkStatus.STRUCTURE_STARTS));
            if (structurestart != null && structurestart.e()) {
                list.add(structurestart);
            }
        }
        return list;
    }
    // Paper end

    @Nullable
    public StructureStart<?> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, IStructureAccess istructureaccess) {
        return istructureaccess.a(structuregenerator);
    }

    public void a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, StructureStart<?> structurestart, IStructureAccess istructureaccess) {
        istructureaccess.a(structuregenerator, structurestart);
    }

    public void a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator, long i, IStructureAccess istructureaccess) {
        istructureaccess.a(structuregenerator, i);
    }

    public boolean a() {
        return this.b.shouldGenerateMapFeatures();
    }

    public StructureStart<?> a(BlockPosition blockposition, boolean flag, StructureGenerator<?> structuregenerator) {
        // Tuinity start - add world parameter
        return this.getStructureStarts(blockposition,flag, structuregenerator, null);
    }
    public StructureStart<?> getStructureStarts(BlockPosition blockposition, boolean flag, StructureGenerator<?> structuregenerator, IWorldReader world) {
        // Paper start - remove structure streams
        for (StructureStart<?> structurestart : getFeatureStarts(SectionPosition.a(blockposition), structuregenerator, world)) { // Tuinity end - add world parameter
            if (structurestart.c().b(blockposition)) {
                if (!flag) {
                    return structurestart;
                }
                for (StructurePiece structurepiece : structurestart.d()) {
                    if (structurepiece.g().b(blockposition)) {
                        return structurestart;
                    }
                }
            }
        }
        return StructureStart.a;
        // Paper end
    }

    // Spigot start
    public World getWorld() {
        return this.a.getMinecraftWorld();
    }
    // Spigot end
}
