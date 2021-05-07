package net.minecraft.world.item.enchantment;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.EnumChatFormat;
import net.minecraft.SystemUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.item.ItemStack;

public abstract class Enchantment {

    private final EnumItemSlot[] a; public final EnumItemSlot[] getSlots() { return this.a; } // Paper - OBFHELPER
    private final Enchantment.Rarity d;
    public final EnchantmentSlotType itemTarget;
    @Nullable
    protected String c;

    protected Enchantment(Enchantment.Rarity enchantment_rarity, EnchantmentSlotType enchantmentslottype, EnumItemSlot[] aenumitemslot) {
        this.d = enchantment_rarity;
        this.itemTarget = enchantmentslottype;
        this.a = aenumitemslot;
    }

    public Map<EnumItemSlot, ItemStack> a(EntityLiving entityliving) {
        Map<EnumItemSlot, ItemStack> map = Maps.newEnumMap(EnumItemSlot.class);
        EnumItemSlot[] aenumitemslot = this.a;
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack = entityliving.getEquipment(enumitemslot);

            if (!itemstack.isEmpty()) {
                map.put(enumitemslot, itemstack);
            }
        }

        return map;
    }

    public Enchantment.Rarity getRarity() { return d(); } // Paper - OBFHELPER
    public Enchantment.Rarity d() {
        return this.d;
    }

    public int getStartLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int a(int i) {
        return 1 + i * 10;
    }

    public int b(int i) {
        return this.a(i) + 5;
    }

    public int a(int i, DamageSource damagesource) {
        return 0;
    }

    public float getDamageIncrease(int level, EnumMonsterType enumMonsterType) { return a(level, enumMonsterType); } // Paper - OBFHELPER
    public float a(int i, EnumMonsterType enummonstertype) {
        return 0.0F;
    }

    public final boolean isCompatible(Enchantment enchantment) {
        return this.a(enchantment) && enchantment.a(this);
    }

    protected boolean a(Enchantment enchantment) {
        return this != enchantment;
    }

    protected String f() {
        if (this.c == null) {
            this.c = SystemUtils.a("enchantment", IRegistry.ENCHANTMENT.getKey(this));
        }

        return this.c;
    }

    public String g() {
        return this.f();
    }

    public final IChatBaseComponent getTranslationComponentForLevel(int level) { return this.d(level); } // Paper - OBFHELPER
    public IChatBaseComponent d(int i) {
        ChatMessage chatmessage = new ChatMessage(this.g());

        if (this.c()) {
            chatmessage.a(EnumChatFormat.RED);
        } else {
            chatmessage.a(EnumChatFormat.GRAY);
        }

        if (i != 1 || this.getMaxLevel() != 1) {
            chatmessage.c(" ").addSibling(new ChatMessage("enchantment.level." + i));
        }

        return chatmessage;
    }

    public boolean canEnchant(ItemStack itemstack) {
        return this.itemTarget.canEnchant(itemstack.getItem());
    }

    public void a(EntityLiving entityliving, Entity entity, int i) {}

    public void b(EntityLiving entityliving, Entity entity, int i) {}

    public boolean isTreasure() {
        return false;
    }

    public boolean isCursed() { return c(); } // Paper - OBFHELPER
    public boolean c() {
        return false;
    }

    public boolean isTradeable() { return h(); } // Paper - OBFHELPER
    public boolean h() {
        return true;
    }

    public boolean isDiscoverable() { return i(); } // Paper - OBFHELPER
    public boolean i() {
        return true;
    }

    public static enum Rarity {

        COMMON(10), UNCOMMON(5), RARE(2), VERY_RARE(1);

        private final int e;

        private Rarity(int i) {
            this.e = i;
        }

        public int a() {
            return this.e;
        }
    }
}
