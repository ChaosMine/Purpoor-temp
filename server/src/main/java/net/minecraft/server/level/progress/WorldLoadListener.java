package net.minecraft.server.level.progress;

import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.ChunkStatus;

import javax.annotation.Nullable;

public interface WorldLoadListener {

    void a(ChunkCoordIntPair chunkcoordintpair);

    void a(ChunkCoordIntPair chunkcoordintpair, @Nullable ChunkStatus chunkstatus);

    void b();

    void setChunkRadius(int radius); // Paper - allow changing chunk radius
}
