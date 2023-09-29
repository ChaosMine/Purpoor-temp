package net.minecraft.network.protocol.game;

import net.minecraft.core.IRegistry;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.Validate;

import java.io.IOException;

public class PacketPlayOutEntitySound implements Packet<PacketListenerPlayOut> {

	private SoundEffect a;
	private SoundCategory b;
	private int c;
	private float d;
	private float e;

	public PacketPlayOutEntitySound() {
	}

	public PacketPlayOutEntitySound(SoundEffect soundeffect, SoundCategory soundcategory, Entity entity, float f, float f1) {
		Validate.notNull(soundeffect, "sound");
		this.a = soundeffect;
		this.b = soundcategory;
		this.c = entity.getId();
		this.d = f;
		this.e = f1;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = IRegistry.SOUND_EVENT.fromId(packetdataserializer.i());
		this.b = packetdataserializer.readEnum(SoundCategory.VALUES);
		this.c = packetdataserializer.i();
		this.d = packetdataserializer.readFloat();
		this.e = packetdataserializer.readFloat();
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.d(IRegistry.SOUND_EVENT.a(this.a));
		packetdataserializer.writeEnum(this.b);
		packetdataserializer.d(this.c);
		packetdataserializer.writeFloat(this.d);
		packetdataserializer.writeFloat(this.e);
	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}
}
