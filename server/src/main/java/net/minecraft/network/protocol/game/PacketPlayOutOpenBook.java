package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.EnumHand;

import java.io.IOException;

public class PacketPlayOutOpenBook implements Packet<PacketListenerPlayOut> {
	private EnumHand a;

	public PacketPlayOutOpenBook() {}

	public PacketPlayOutOpenBook(EnumHand enumhand) {
		this.a = enumhand;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.readEnum(EnumHand.VALUES);
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.writeEnum(this.a);
	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}
}
