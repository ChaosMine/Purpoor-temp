package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;

public class PacketPlayOutWorldBorder implements Packet<PacketListenerPlayOut> {
	private PacketPlayOutWorldBorder.EnumWorldBorderAction a;
	private int b;
	private double c;
	private double d;
	private double e;
	private double f;
	private long g;
	private int h;
	private int i;

	public PacketPlayOutWorldBorder() {}

	public PacketPlayOutWorldBorder(WorldBorder worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction packetplayoutworldborder_enumworldborderaction) {
		this.a = packetplayoutworldborder_enumworldborderaction;
		this.c = worldborder.getCenterX() * worldborder.world.getDimensionManager().getCoordinateScale();
		this.d = worldborder.getCenterZ() * worldborder.world.getDimensionManager().getCoordinateScale();
		this.f = worldborder.getSize();
		this.e = worldborder.k();
		this.g = worldborder.j();
		this.b = worldborder.m();
		this.i = worldborder.getWarningDistance();
		this.h = worldborder.getWarningTime();
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.readEnum(EnumWorldBorderAction.VALUES);
		switch (this.a) {
			case SET_SIZE -> this.e = packetdataserializer.readDouble();
			case LERP_SIZE -> {
				this.f = packetdataserializer.readDouble();
				this.e = packetdataserializer.readDouble();
				this.g = packetdataserializer.j();
			}
			case SET_CENTER -> {
				this.c = packetdataserializer.readDouble();
				this.d = packetdataserializer.readDouble();
			}
			case SET_WARNING_BLOCKS -> this.i = packetdataserializer.i();
			case SET_WARNING_TIME -> this.h = packetdataserializer.i();
			case INITIALIZE -> {
				this.c = packetdataserializer.readDouble();
				this.d = packetdataserializer.readDouble();
				this.f = packetdataserializer.readDouble();
				this.e = packetdataserializer.readDouble();
				this.g = packetdataserializer.j();
				this.b = packetdataserializer.i();
				this.i = packetdataserializer.i();
				this.h = packetdataserializer.i();
			}
		}
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.writeEnum(this.a);
		switch (this.a) {
			case SET_SIZE:
				packetdataserializer.writeDouble(this.e);
				break;
			case LERP_SIZE:
				packetdataserializer.writeDouble(this.f);
				packetdataserializer.writeDouble(this.e);
				packetdataserializer.b(this.g);
				break;
			case SET_CENTER:
				packetdataserializer.writeDouble(this.c);
				packetdataserializer.writeDouble(this.d);
				break;
			case SET_WARNING_BLOCKS:
				packetdataserializer.d(this.i);
				break;
			case SET_WARNING_TIME:
				packetdataserializer.d(this.h);
				break;
			case INITIALIZE:
				packetdataserializer.writeDouble(this.c);
				packetdataserializer.writeDouble(this.d);
				packetdataserializer.writeDouble(this.f);
				packetdataserializer.writeDouble(this.e);
				packetdataserializer.b(this.g);
				packetdataserializer.d(this.b);
				packetdataserializer.d(this.i);
				packetdataserializer.d(this.h);
		}
	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}

	public enum EnumWorldBorderAction {

		SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS;

		public static final EnumWorldBorderAction @Unmodifiable [] VALUES = values();

		EnumWorldBorderAction() {
		}
	}
}
