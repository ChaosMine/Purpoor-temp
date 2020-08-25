package net.minecraft.world.item;

import net.minecraft.network.protocol.game.PacketPlayOutSetCooldown;
import net.minecraft.server.level.EntityPlayer;

import io.papermc.paper.event.player.PlayerItemCooldownEvent; // Paper

public class ItemCooldownPlayer extends ItemCooldown {

    private final EntityPlayer a; public EntityPlayer getEntityPlayer() { return a; } // Paper - OBFHELPER

    public ItemCooldownPlayer(EntityPlayer entityplayer) {
        this.a = entityplayer;
    }

    // Paper start
    @Override
    public void setCooldown(Item item, int ticks) {
        PlayerItemCooldownEvent event = new PlayerItemCooldownEvent(getEntityPlayer().getBukkitEntity(), org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(item), ticks);
        if (event.callEvent()) {
            super.setCooldown(item, event.getCooldown());
        }
    }
    // Paper end

    @Override
    protected void b(Item item, int i) {
        super.b(item, i);
        this.a.playerConnection.sendPacket(new PacketPlayOutSetCooldown(item, i));
    }

    @Override
    protected void c(Item item) {
        super.c(item);
        this.a.playerConnection.sendPacket(new PacketPlayOutSetCooldown(item, 0));
    }
}
