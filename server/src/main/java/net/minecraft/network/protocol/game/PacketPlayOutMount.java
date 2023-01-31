package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutMount implements Packet<PacketListenerPlayOut> {

    public int a;
    public int[] b;

    public PacketPlayOutMount() {}

    public PacketPlayOutMount(Entity entity) {
        this.a = entity.getId();
        List<Entity> list = entity.passengers; // Paper - do not create a copy of the list

        this.b = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            this.b[i] = ((Entity) list.get(i)).getId();
        }

    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.b = packetdataserializer.b();
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.a(this.b);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
