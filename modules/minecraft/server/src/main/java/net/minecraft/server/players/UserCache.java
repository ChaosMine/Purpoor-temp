package net.minecraft.server.players;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class UserCache {
	private final ConcurrentMap<UUID, GameProfile> byUuid = new ConcurrentHashMap<>();
	private final LoadingCache<String, GameProfile> byName = Caffeine.newBuilder()
		.softValues()
		.maximumSize(350)
		.build(name -> new GameProfile(EntityPlayer.getOfflineUUID(name), name));

	public void saveProfile(GameProfile gameprofile) {
		a(gameprofile);
	} // Paper - OBFHELPER

	public void a(GameProfile gameprofile) {
		byUuid.put(gameprofile.getId(), gameprofile);
		byName.put(gameprofile.getName().toLowerCase(Locale.ROOT), gameprofile);
	}

	public GameProfile getProfile(String s) {
		return byName.get(s);
	}

	@Nullable
	public GameProfile getProfileIfCached(String name) {
		return byName.get(name.toLowerCase(Locale.ROOT));
	}

	@Nullable
	public GameProfile getProfile(UUID uuid) {
		return byUuid.get(uuid);
	}

	void removeUuid(UUID uuid) {
		var profile = this.byUuid.get(uuid);
		this.byName.asMap().remove(profile.getName());
		this.byUuid.remove(uuid);
	}

	void removeName(String name) {
		var uuid = EntityPlayer.getOfflineUUID(name);
		this.byName.asMap().remove(name);
		this.byUuid.remove(uuid);
	}
}
