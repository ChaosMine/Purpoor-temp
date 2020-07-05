package net.minecraft.world.item;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityHuman;

// Paper start
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
// Paper end

public class ItemNameTag extends Item {

    public ItemNameTag(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving, EnumHand enumhand) {
        if (itemstack.hasName() && !(entityliving instanceof EntityHuman)) {
            if (!entityhuman.world.isClientSide && entityliving.isAlive()) {
                // Paper start
                PlayerNameEntityEvent event = new PlayerNameEntityEvent(((EntityPlayer) entityhuman).getBukkitEntity(), entityliving.getBukkitLivingEntity(), PaperAdventure.asAdventure(itemstack.getName()), true);
                if (!event.callEvent()) return EnumInteractionResult.PASS;
                EntityLiving newEntityLiving = ((org.bukkit.craftbukkit.entity.CraftLivingEntity) event.getEntity()).getHandle();
                newEntityLiving.setCustomName(event.getName() != null ? PaperAdventure.asVanilla(event.getName()) : null);
                if (event.isPersistent() && newEntityLiving instanceof EntityInsentient) {
                    ((EntityInsentient) newEntityLiving).setPersistent();
                }
                // Paper end
                itemstack.subtract(1);
            }

            return EnumInteractionResult.a(entityhuman.world.isClientSide);
        } else {
            return EnumInteractionResult.PASS;
        }
    }
}
