package net.minecraft.world.level.chunk;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketDataSerializer;

public interface DataPalette<T> {

    default int getOrCreateIdFor(T object) { return this.a(object); } // Paper - OBFHELPER
    int a(T t0);

    boolean a(Predicate<T> predicate);

    @Nullable default T getObject(int dataBits) { return this.a(dataBits); } // Paper - OBFHELPER
    @Nullable
    T a(int i);

    void b(PacketDataSerializer packetdataserializer);

    int a();

    void a(NBTTagList nbttaglist);
}
