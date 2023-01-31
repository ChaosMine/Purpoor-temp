// mc-dev import
package net.minecraft.network.protocol.game;

import net.minecraft.SystemUtils;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.EnumHand;
import net.minecraft.world.phys.MovingObjectPositionBlock;

import java.io.IOException;

public class PacketPlayInUseItem implements Packet<PacketListenerPlayIn> {
	private MovingObjectPositionBlock a;
	private EnumHand b;
	public long timestamp;

	public PacketPlayInUseItem() {}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.timestamp = SystemUtils.getMonotonicMillis(); // Spigot
		this.b = packetdataserializer.readEnum(EnumHand.VALUES);
		this.a = packetdataserializer.r();
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.writeEnum(this.b);
		packetdataserializer.a(this.a);
	}

	public void a(PacketListenerPlayIn packetlistenerplayin) {
		packetlistenerplayin.a(this);
	}

	public EnumHand b() {
		return this.b;
	}

	public MovingObjectPositionBlock c() {
		return this.a;
	}
}
