package org.bukkit.craftbukkit.inventory;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.MerchantRecipe;
import net.minecraft.world.item.trading.MerchantRecipeList;
import net.minecraft.world.level.World;
import org.apache.commons.lang.Validate;

public class CraftMerchantCustom extends CraftMerchant {

    @Deprecated // Paper - Adventure
    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
        getMerchant().craftMerchant = this;
    }
    // Paper start
    public CraftMerchantCustom(net.kyori.adventure.text.Component title) {
        super(new MinecraftMerchant(title));
        getMerchant().craftMerchant = this;
    }
    // Paper end

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    @Override
    public MinecraftMerchant getMerchant() {
        return (MinecraftMerchant) super.getMerchant();
    }

    public static class MinecraftMerchant implements IMerchant {

        private final IChatBaseComponent title;
        private final MerchantRecipeList trades = new MerchantRecipeList();
        private EntityHuman tradingPlayer;
        private World tradingWorld;
        protected CraftMerchant craftMerchant;

        @Deprecated // Paper - Adventure
        public MinecraftMerchant(String title) {
            Validate.notNull(title, "Title cannot be null");
            this.title = new ChatComponentText(title);
        }
        // Paper start
        public MinecraftMerchant(net.kyori.adventure.text.Component title) {
            Validate.notNull(title, "Title cannot be null");
            this.title = io.papermc.paper.adventure.PaperAdventure.asVanilla(title);
        }
        // Paper end

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Override
        public void setTradingPlayer(EntityHuman entityhuman) {
            this.tradingPlayer = entityhuman;
            if (entityhuman != null) {
                this.tradingWorld = entityhuman.world;
            }
        }

        @Override
        public EntityHuman getTrader() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList getOffers() {
            return this.trades;
        }

        @Override
        public void a(MerchantRecipe merchantrecipe) {
            // Paper start
            /** Based on {@link net.minecraft.world.entity.npc.EntityVillagerAbstract#b(MerchantRecipe)} */
            if (getTrader() instanceof net.minecraft.server.level.EntityPlayer) {
                final net.minecraft.server.level.EntityPlayer trader = (net.minecraft.server.level.EntityPlayer) getTrader();
                final io.papermc.paper.event.player.PlayerPurchaseEvent event = new io.papermc.paper.event.player.PlayerPurchaseEvent(
                    trader.getBukkitEntity(),
                    merchantrecipe.asBukkit(),
                    false, // reward xp?
                    true); // should increase uses?
                event.callEvent();
                if (event.isCancelled()) {
                    return;
                }
                final org.bukkit.inventory.MerchantRecipe eventTrade = event.getTrade();
                if (event.willIncreaseTradeUses()) {
                    eventTrade.setUses(eventTrade.getUses() + 1);
                }
                if (event.isRewardingExp() && eventTrade.hasExperienceReward()) {
                    /** Based on {@link net.minecraft.world.entity.npc.EntityVillagerTrader#b(MerchantRecipe)} */
                    final int xp = 3 + net.minecraft.world.entity.Entity.SHARED_RANDOM.nextInt(4);
                    final World world = trader.getWorld();
                    world.addEntity(new net.minecraft.world.entity.EntityExperienceOrb(
                        world, trader.locX(), trader.locY() + 0.5d, trader.locZ(), xp,
                        org.bukkit.entity.ExperienceOrb.SpawnReason.VILLAGER_TRADE, trader, null));
                }
                return;
            }
            // Paper end

            // increase recipe's uses
            merchantrecipe.increaseUses();
        }

        @Override
        public void k(ItemStack itemstack) {
        }

        public IChatBaseComponent getScoreboardDisplayName() {
            return title;
        }

        @Override
        public World getWorld() {
            return this.tradingWorld;
        }

        @Override
        public int getExperience() {
            return 0; // xp
        }

        @Override
        public void setForcedExperience(int i) {
        }

        @Override
        public boolean isRegularVillager() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }

        @Override
        public SoundEffect getTradeSound() {
            return SoundEffects.ENTITY_VILLAGER_YES;
        }
    }
}
