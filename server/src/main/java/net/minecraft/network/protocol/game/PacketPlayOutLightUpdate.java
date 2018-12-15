package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelFuture; // Paper

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.SectionPosition;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MCUtil;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.chunk.NibbleArray;
import net.minecraft.world.level.lighting.LightEngine;

public class PacketPlayOutLightUpdate implements Packet<PacketListenerPlayOut> {

    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private List<byte[]> g;
    private List<byte[]> h;
    private boolean i;

    // Paper start
    java.lang.Runnable cleaner1;
    java.lang.Runnable cleaner2;
    java.util.concurrent.atomic.AtomicInteger remainingSends = new java.util.concurrent.atomic.AtomicInteger(0);

    @Override
    public void onPacketDispatch(EntityPlayer player) {
        if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine) remainingSends.incrementAndGet();
    }

    @Override
    public void onPacketDispatchFinish(EntityPlayer player, ChannelFuture future) {
        if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine && remainingSends.decrementAndGet() <= 0) {
            // incase of any race conditions, schedule this delayed
            MCUtil.scheduleTask(5, () -> {
                if (remainingSends.get() == 0) {
                    cleaner1.run();
                    cleaner2.run();
                }
            }, "Light Packet Release");
        }
    }

    @Override
    public boolean hasFinishListener() {
        return !com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine; // Tuinity - replace light impl
    }

    // Paper end
    public PacketPlayOutLightUpdate() {}

    public PacketPlayOutLightUpdate(ChunkCoordIntPair chunkcoordintpair, LightEngine lightengine, boolean flag) {
        this.a = chunkcoordintpair.x;
        this.b = chunkcoordintpair.z;
        this.i = flag;
        this.g = Lists.newArrayList();if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine) cleaner1 = MCUtil.registerListCleaner(this, this.g, NibbleArray::releaseBytes); // Paper // Tuinity - purge cleaner usage
        this.h = Lists.newArrayList();if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine) cleaner2 = MCUtil.registerListCleaner(this, this.h, NibbleArray::releaseBytes); // Paper // Tuinity - purge cleaner usage

        for (int i = 0; i < 18; ++i) {
            NibbleArray nibblearray = lightengine.a(EnumSkyBlock.SKY).a(SectionPosition.a(chunkcoordintpair, -1 + i));
            NibbleArray nibblearray1 = lightengine.a(EnumSkyBlock.BLOCK).a(SectionPosition.a(chunkcoordintpair, -1 + i));

            if (nibblearray != null) {
                if (nibblearray.c()) {
                    this.e |= 1 << i;
                } else {
                    this.c |= 1 << i;
                    this.g.add(com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine ? nibblearray.asBytes() : nibblearray.getCloneIfSet()); // Paper // Tuinity - don't clone again
                }
            }

            if (nibblearray1 != null) {
                if (nibblearray1.c()) {
                    this.f |= 1 << i;
                } else {
                    this.d |= 1 << i;
                    this.h.add(com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine ? nibblearray1.asBytes() : nibblearray1.getCloneIfSet()); // Paper // Tuinity - don't clone again
                }
            }
        }

    }

    public PacketPlayOutLightUpdate(ChunkCoordIntPair chunkcoordintpair, LightEngine lightengine, int i, int j, boolean flag) {
        this.a = chunkcoordintpair.x;
        this.b = chunkcoordintpair.z;
        this.i = flag;
        this.c = i;
        this.d = j;
        this.g = Lists.newArrayList();if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine) cleaner1 = MCUtil.registerListCleaner(this, this.g, NibbleArray::releaseBytes); // Paper // Tuinity - purge cleaner usage
        this.h = Lists.newArrayList();if (!com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine) cleaner2 = MCUtil.registerListCleaner(this, this.h, NibbleArray::releaseBytes); // Paper // Tuinity - purge cleaner usage

        for (int k = 0; k < 18; ++k) {
            NibbleArray nibblearray;

            if ((this.c & 1 << k) != 0) {
                nibblearray = lightengine.a(EnumSkyBlock.SKY).a(SectionPosition.a(chunkcoordintpair, -1 + k));
                if (nibblearray != null && !nibblearray.c()) {
                    this.g.add(com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine ? nibblearray.asBytes() : nibblearray.getCloneIfSet()); // Paper // Tuinity - don't clone again
                } else {
                    this.c &= ~(1 << k);
                    if (nibblearray != null) {
                        this.e |= 1 << k;
                    }
                }
            }

            if ((this.d & 1 << k) != 0) {
                nibblearray = lightengine.a(EnumSkyBlock.BLOCK).a(SectionPosition.a(chunkcoordintpair, -1 + k));
                if (nibblearray != null && !nibblearray.c()) {
                    this.h.add(com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine ? nibblearray.asBytes() : nibblearray.getCloneIfSet()); // Paper // Tuinity - don't clone again
                } else {
                    this.d &= ~(1 << k);
                    if (nibblearray != null) {
                        this.f |= 1 << k;
                    }
                }
            }
        }

    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.b = packetdataserializer.i();
        this.i = packetdataserializer.readBoolean();
        this.c = packetdataserializer.i();
        this.d = packetdataserializer.i();
        this.e = packetdataserializer.i();
        this.f = packetdataserializer.i();
        this.g = Lists.newArrayList();

        int i;

        for (i = 0; i < 18; ++i) {
            if ((this.c & 1 << i) != 0) {
                this.g.add(packetdataserializer.b(2048));
            }
        }

        this.h = Lists.newArrayList();

        for (i = 0; i < 18; ++i) {
            if ((this.d & 1 << i) != 0) {
                this.h.add(packetdataserializer.b(2048));
            }
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.d(this.b);
        packetdataserializer.writeBoolean(this.i);
        packetdataserializer.d(this.c);
        packetdataserializer.d(this.d);
        packetdataserializer.d(this.e);
        packetdataserializer.d(this.f);
        Iterator iterator = this.g.iterator();

        byte[] abyte;

        while (iterator.hasNext()) {
            abyte = (byte[]) iterator.next();
            packetdataserializer.a(abyte);
        }

        iterator = this.h.iterator();

        while (iterator.hasNext()) {
            abyte = (byte[]) iterator.next();
            packetdataserializer.a(abyte);
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
