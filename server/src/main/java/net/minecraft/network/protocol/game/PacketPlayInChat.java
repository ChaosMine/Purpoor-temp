package net.minecraft.network.protocol.game;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

import java.io.IOException;
import java.util.concurrent.Executors;

public class PacketPlayInChat implements Packet<PacketListenerPlayIn> {
	public String a;

	public PacketPlayInChat() {}

	public PacketPlayInChat(String s) {
		this.a = s;
	}

	@Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.a = org.apache.commons.lang3.StringUtils.normalizeSpace(packetdataserializer.e(256)); // CraftBukkit - see PlayerConnection
	}

	@Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.a(this.a);
	}

	// Spigot Start
	private static final java.util.concurrent.ExecutorService executors = Executors.newFixedThreadPool(16,
		new ThreadFactoryBuilder()
			.setDaemon(true)
			.setNameFormat("Async Chat Thread - #%d")
			.build()
	);

	public void a(final PacketListenerPlayIn packetlistenerplayin) {
		if (!a.startsWith("/")) {
			executors.submit(() -> packetlistenerplayin.a(PacketPlayInChat.this));
			return;
		}
		// Spigot End
		packetlistenerplayin.a(this);
	}

	public String b() {
		return this.a;
	}
}
