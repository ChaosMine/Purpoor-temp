package net.minecraft.server.dedicated;

import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.WorldNBTStorage;

public class DedicatedPlayerList extends PlayerList {
	public DedicatedPlayerList(
		DedicatedServer dedicatedServer,
		IRegistryCustom.Dimension dimension,
		WorldNBTStorage nbtStorage
	) {
		super(dedicatedServer, dimension, nbtStorage, dedicatedServer.getDedicatedServerProperties().maxPlayers);
		this.a(dedicatedServer.getDedicatedServerProperties().viewDistance);
	}

	@Override
	public DedicatedServer getServer() {
		return (DedicatedServer) super.getServer();
	}
}
