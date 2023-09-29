package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.boss.wither.EntityWither;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither, com.destroystokyo.paper.entity.CraftRangedEntity<EntityWither> { // Paper

    private BossBar bossBar;

    public CraftWither(CraftServer server, EntityWither entity) {
        super(server, entity);

        if (entity.bossBattle != null) {
            this.bossBar = new CraftBossBar(entity.bossBattle);
        }
    }

    @Override
    public EntityWither getHandle() {
        return (EntityWither) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }

    @Override
    public BossBar getBossBar() {
        return bossBar;
    }

    // Paper start
    @Override
    public boolean isCharged() {
        return getHandle().isPowered();
    }

    @Override
    public int getInvulnerableTicks() {
        return getHandle().getInvul();
    }

    @Override
    public void setInvulnerableTicks(int ticks) {
        getHandle().setInvul(ticks);
    }

    @Override
    public boolean canTravelThroughPortals() {
        return getHandle().canPortal();
    }

    @Override
    public void setCanTravelThroughPortals(boolean value) {
        getHandle().setCanTravelThroughPortals(value);
    }
    // Paper end
}
