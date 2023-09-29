package net.minecraft.network.protocol.login;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

import java.io.IOException;

public class PacketLoginInCustomPayload implements Packet<PacketLoginInListener> {
	private int a;

	public int getId() { return a; } // Paper - OBFHELPER

	private PacketDataSerializer b;

	public PacketDataSerializer getBuf() { return b; } // Paper - OBFHELPER

	public PacketLoginInCustomPayload() {}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.i();
		if (packetdataserializer.readBoolean()) {
			int i = packetdataserializer.readableBytes();

			if (i < 0 || i > 1048576) {
				throw new IOException("Payload may not be larger than 1048576 bytes");
			}

			this.b = new PacketDataSerializer(Unpooled.copiedBuffer(packetdataserializer.readSlice(i)));
		} else {
			this.b = null;
		}
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.d(this.a);
		if (this.b != null) {
			packetdataserializer.writeBoolean(true);
			packetdataserializer.writeBytes(this.b.slice());
		} else {
			packetdataserializer.writeBoolean(false);
		}
	}

	public void a(PacketLoginInListener packetlogininlistener) {
		packetlogininlistener.a(this);
	}
}
