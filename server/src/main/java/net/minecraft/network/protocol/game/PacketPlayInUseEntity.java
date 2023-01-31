package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.io.IOException;

public class PacketPlayInUseEntity implements Packet<PacketListenerPlayIn> {
	public int a;

	public int getEntityId() {
		return this.a;
	} // Paper - add accessor

	public PacketPlayInUseEntity.EnumEntityUseAction action;
	public Vec3D c;
	public EnumHand d;
	public boolean e;

	public PacketPlayInUseEntity() {
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = packetdataserializer.i();
		this.action = packetdataserializer.readEnum(EnumEntityUseAction.VALUES);
		if (this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
			this.c = new Vec3D(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
		}

		if (this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT || this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
			this.d = packetdataserializer.readEnum(EnumHand.VALUES);
		}

		this.e = packetdataserializer.readBoolean();
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.d(this.a);
		packetdataserializer.writeEnum(this.action);
		if (this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
			packetdataserializer.writeFloat((float) this.c.x);
			packetdataserializer.writeFloat((float) this.c.y);
			packetdataserializer.writeFloat((float) this.c.z);
		}

		if (this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT || this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
			packetdataserializer.writeEnum(this.d);
		}

		packetdataserializer.writeBoolean(this.e);
	}

	public void a(PacketListenerPlayIn packetlistenerplayin) {
		packetlistenerplayin.a(this);
	}

	@Nullable
	public Entity a(World world) {
		return world.getEntity(this.a);
	}

	public PacketPlayInUseEntity.EnumEntityUseAction b() {
		return this.action;
	}

	@Nullable
	public EnumHand c() {
		return this.d;
	}

	public Vec3D d() {
		return this.c;
	}

	public boolean e() {
		return this.e;
	}

	public enum EnumEntityUseAction {

		INTERACT, ATTACK, INTERACT_AT;

		public static final EnumEntityUseAction @Unmodifiable [] VALUES = values();

		EnumEntityUseAction() {
		}
	}
}
