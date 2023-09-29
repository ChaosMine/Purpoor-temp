package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EnumItemSlot;

public class EnchantmentInfiniteArrows extends Enchantment {

    public EnchantmentInfiniteArrows(Enchantment.Rarity enchantment_rarity, EnumItemSlot... aenumitemslot) {
        super(enchantment_rarity, net.pl3x.purpur.PurpurConfig.allowCrossbowInfinity ? EnchantmentSlotType.BOW_AND_CROSSBOW : EnchantmentSlotType.BOW, aenumitemslot); // Purpur
    }

    @Override
    public int a(int i) {
        return 20;
    }

    @Override
    public int b(int i) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean a(Enchantment enchantment) {
        return enchantment instanceof EnchantmentMending ? net.pl3x.purpur.PurpurConfig.allowInfinityMending : super.a(enchantment); // Purpur
    }
}
