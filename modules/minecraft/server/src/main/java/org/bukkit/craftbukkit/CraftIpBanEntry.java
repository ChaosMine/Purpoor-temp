package org.bukkit.craftbukkit;

import java.util.Date;

public final class CraftIpBanEntry implements org.bukkit.BanEntry {
    @Override
    public String getTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreated(Date created) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSource(String source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getExpiration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExpiration(Date expiration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReason() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReason(String reason) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException();
    }
}
