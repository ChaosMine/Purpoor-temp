package net.minecraft.network.protocol.game;

import net.minecraft.SystemUtils;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.EnumHand;

import java.io.IOException;

public class PacketPlayInBlockPlace implements Packet<PacketListenerPlayIn> {
	private EnumHand a;
	public long timestamp;

	public PacketPlayInBlockPlace() {}

	public PacketPlayInBlockPlace(EnumHand enumhand) {
		this.a = enumhand;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.timestamp = SystemUtils.getMonotonicMillis();
		this.a = packetdataserializer.readEnum(EnumHand.VALUES);
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.writeEnum(this.a);
	}

	public void a(PacketListenerPlayIn packetlistenerplayin) {
		packetlistenerplayin.a(this);
	}

	public EnumHand b() {
		return this.a;
	}
}
