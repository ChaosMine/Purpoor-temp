package net.minecraft.world.item;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.stats.StatisticList;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.InteractionResultWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityThrownExpBottle;
import net.minecraft.world.level.World;

public class ItemExpBottle extends Item {

    public ItemExpBottle(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public boolean e(ItemStack itemstack) {
        return true;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        //world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemExpBottle.RANDOM.nextFloat() * 0.4F + 0.8F));  // Paper - moved down
        if (!world.isClientSide) {
            EntityThrownExpBottle entitythrownexpbottle = new EntityThrownExpBottle(world, entityhuman);

            entitythrownexpbottle.setItem(itemstack);
            entitythrownexpbottle.a(entityhuman, entityhuman.pitch, entityhuman.yaw, -20.0F, 0.7F, 1.0F);
            // Paper start
            com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent event = new com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack), (org.bukkit.entity.Projectile) entitythrownexpbottle.getBukkitEntity());
            if (event.callEvent() && world.addEntity(entitythrownexpbottle)) {
                if (event.shouldConsume() && !entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                } else if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }

                world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (Entity.SHARED_RANDOM.nextFloat() * 0.4F + 0.8F));
                entityhuman.b(StatisticList.ITEM_USED.b(this));
            } else {
                if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }
                    return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.FAIL, itemstack);
            }
            // Paper end
        }

        /* // Paper start - moved up
        entityhuman.b(StatisticList.ITEM_USED.b(this));
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }
        */ // Paper end

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}
