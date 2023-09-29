package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.player.EnumChatVisibility;

import java.io.IOException;

public class PacketPlayInSettings implements Packet<PacketListenerPlayIn> {

	public String locale;
	public int viewDistance;
	private EnumChatVisibility c;
	private boolean d;
	private int e;
	private EnumMainHand f;

	public PacketPlayInSettings() {
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.locale = packetdataserializer.e(16);
		this.viewDistance = packetdataserializer.readByte();
		this.c = packetdataserializer.readEnum(EnumChatVisibility.VALUES);
		this.d = packetdataserializer.readBoolean();
		this.e = packetdataserializer.readUnsignedByte();
		this.f = packetdataserializer.readEnum(EnumMainHand.VALUES);
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.a(this.locale);
		packetdataserializer.writeByte(this.viewDistance);
		packetdataserializer.writeEnum(this.c);
		packetdataserializer.writeBoolean(this.d);
		packetdataserializer.writeByte(this.e);
		packetdataserializer.writeEnum(this.f);
	}

	public void a(PacketListenerPlayIn packetlistenerplayin) {
		packetlistenerplayin.a(this);
	}

	public EnumChatVisibility getChatVisibility() {
		return d();
	} // Paper - OBFHELPER

	public EnumChatVisibility d() {
		return this.c;
	}

	public boolean hasChatColorsEnabled() {
		return e();
	} // Paper - OBFHELPER

	public boolean e() {
		return this.d;
	}

	public int getSkinParts() {
		return f();
	} // Paper - OBFHELPER

	public int f() {
		return this.e;
	}

	public EnumMainHand getMainHand() {
		return this.f;
	}
}
