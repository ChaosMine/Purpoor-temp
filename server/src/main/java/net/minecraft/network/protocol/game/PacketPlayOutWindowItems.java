package net.minecraft.network.protocol.game;

import net.minecraft.core.NonNullList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PacketPlayOutWindowItems implements Packet<PacketListenerPlayOut> {

    private int a;
    private List<ItemStack> b;

    //Paper start
    @Override
    public boolean packetTooLarge(NetworkManager manager) {
        for (int i = 0 ; i < this.b.size() ; i++) {
            manager.sendPacket(new PacketPlayOutSetSlot(this.a, i, this.b.get(i)));
        }
        return true;
    }
    // Paper end
    public PacketPlayOutWindowItems() {}

    public PacketPlayOutWindowItems(int i, NonNullList<ItemStack> nonnulllist) {
        this.a = i;
        this.b = NonNullList.a(nonnulllist.size(), ItemStack.b);

        for (int j = 0; j < this.b.size(); ++j) {
            this.b.set(j, ((ItemStack) nonnulllist.get(j)).cloneItemStack());
        }

    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readUnsignedByte();
        short short0 = packetdataserializer.readShort();

        this.b = NonNullList.a(short0, ItemStack.b);

        for (int i = 0; i < short0; ++i) {
            this.b.set(i, packetdataserializer.n());
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.a);
        packetdataserializer.writeShort(this.b.size());

        for (ItemStack itemstack : this.b) {
            packetdataserializer.a(itemstack);
        }
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
