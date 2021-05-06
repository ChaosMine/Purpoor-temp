package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.npc.EntityVillagerTrader;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, EntityVillagerTrader entity) {
        super(server, entity);
    }

    @Override
    public EntityVillagerTrader getHandle() {
        return (EntityVillagerTrader) entity;
    }

    @Override
    public String toString() {
        return "CraftWanderingTrader";
    }

    @Override
    public EntityType getType() {
        return EntityType.WANDERING_TRADER;
    }

    @Override
    public int getDespawnDelay() {
        return getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        getHandle().setDespawnDelay(despawnDelay);
    }

    // Paper start - Add more WanderingTrader API
    @Override
    public void setCanDrinkPotion(boolean bool) {
        getHandle().canDrinkPotion = bool;
    }

    @Override
    public boolean canDrinkPotion() {
        return getHandle().canDrinkPotion;
    }

    @Override
    public void setCanDrinkMilk(boolean bool) {
        getHandle().canDrinkMilk = bool;
    }

    @Override
    public boolean canDrinkMilk() {
        return getHandle().canDrinkMilk;
    }
    // Paper end
}
