package org.bukkit.craftbukkit;

import java.util.Date;
import java.util.Set;

@Deprecated(forRemoval = true)
public class CraftIpBanList implements org.bukkit.BanList {
	@Override
	public org.bukkit.BanEntry getBanEntry(String target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<org.bukkit.BanEntry> getBanEntries() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isBanned(String target) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void pardon(String target) {
		throw new UnsupportedOperationException();
	}
}
