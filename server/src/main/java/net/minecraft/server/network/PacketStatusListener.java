package net.minecraft.server.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.status.PacketStatusInListener;
import net.minecraft.network.protocol.status.PacketStatusInPing;
import net.minecraft.network.protocol.status.PacketStatusInStart;
import net.minecraft.network.protocol.status.PacketStatusOutPong;
import net.minecraft.server.MinecraftServer;

public class PacketStatusListener implements PacketStatusInListener {
	private static final IChatBaseComponent a = new ChatMessage("multiplayer.status.request_handled");
	private final MinecraftServer minecraftServer;
	private final NetworkManager networkManager;
	private boolean d;

	public PacketStatusListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
		this.minecraftServer = minecraftserver;
		this.networkManager = networkmanager;
	}

	@Override
	public void a(IChatBaseComponent ichatbasecomponent) {}

	@Override
	public NetworkManager a() {
		return this.networkManager;
	}

	@Override
	public void a(PacketStatusInStart packetstatusinstart) {
		if (this.d) {
			this.networkManager.close(PacketStatusListener.a);
		} else {
			this.d = true;
			if (this.minecraftServer.getServerPing().getServerData() == null)
				return; // Purpur - do not respond to pings before we know the protocol version
			com.destroystokyo.paper.network.StandardPaperServerListPingEventImpl.processRequest(this.minecraftServer, this.networkManager);
		}
	}

	@Override
	public void a(PacketStatusInPing packetstatusinping) {
		this.networkManager.sendPacket(new PacketStatusOutPong(packetstatusinping.b()));
		this.networkManager.close(PacketStatusListener.a);
	}
}
