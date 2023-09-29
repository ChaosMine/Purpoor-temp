package org.bukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.List; // Paper
import java.util.Map;
import java.util.Set; // Paper
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UndefinedNullability;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
// Purpur start
import com.google.common.collect.Multimap;
import java.util.Collection;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import com.destroystokyo.paper.Namespaced;
// Purpur end

/**
 * Represents a stack of items.
 * <p>
 * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain <i>items</i>. Do not
 * use this class to encapsulate Materials for which {@link Material#isItem()}
 * returns false.</b>
 */
public class ItemStack implements Cloneable, ConfigurationSerializable, net.kyori.adventure.text.event.HoverEventSource<net.kyori.adventure.text.event.HoverEvent.ShowItem> { // Paper
    private Material type = Material.AIR;
    private int amount = 0;
    private MaterialData data = null;
    private ItemMeta meta;

    @Utility
    protected ItemStack() {}

    /**
     * Defaults stack size to 1, with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     */
    public ItemStack(@NotNull final Material type) {
        this(type, 1);
    }

    /**
     * An item stack with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     * @param amount stack size
     */
    public ItemStack(@NotNull final Material type, final int amount) {
        this(type, amount, (short) 0);
    }

    /**
     * An item stack with the specified damage / durability
     *
     * @param type item material
     * @param amount stack size
     * @param damage durability / damage
     * @deprecated see {@link #setDurability(short)}
     */
    public ItemStack(@NotNull final Material type, final int amount, final short damage) {
        this(type, amount, damage, null);
    }

    /**
     * @param type the type
     * @param amount the amount in the stack
     * @param damage the damage value of the item
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    @Deprecated
    public ItemStack(@NotNull final Material type, final int amount, final short damage, @Nullable final Byte data) {
        Validate.notNull(type, "Material cannot be null");
        this.type = type;
        this.amount = amount;
        if (damage != 0) {
            setDurability(damage);
        }
        if (data != null) {
            createData(data);
        }
    }

    /**
     * Creates a new item stack derived from the specified stack
     *
     * @param stack the stack to copy
     * @throws IllegalArgumentException if the specified stack is null or
     *     returns an item meta not created by the item factory
     */
    public ItemStack(@NotNull final ItemStack stack) throws IllegalArgumentException {
        Validate.notNull(stack, "Cannot copy null stack");
        this.type = stack.getType();
        this.amount = stack.getAmount();
        if (this.type.isLegacy()) {
            this.data = stack.getData();
        }
        if (stack.hasItemMeta()) {
            setItemMeta0(stack.getItemMeta(), type);
        }
    }

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    @Utility
    @NotNull
    public Material getType() {
        return type;
    }

    /**
     * Sets the type of this item
     * <p>
     * Note that in doing so you will reset the MaterialData for this stack.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type New type to set the items in this stack to
     */
    @Utility
    public void setType(@NotNull Material type) {
        Validate.notNull(type, "Material cannot be null");
        this.type = type;
        if (this.meta != null) {
            this.meta = Bukkit.getItemFactory().asMetaFor(meta, type);
        }
        if (type.isLegacy()) {
            createData((byte) 0);
        } else {
            this.data = null;
        }
    }

    /**
     * Gets the amount of items in this stack
     *
     * @return Amount of items in this stack
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount New amount of items in this stack
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the MaterialData for this stack of items
     *
     * @return MaterialData for this item
     */
    @Nullable
    public MaterialData getData() {
        Material mat = Bukkit.getUnsafe().toLegacy(getType());
        if (data == null && mat != null && mat.getData() != null) {
            data = mat.getNewData((byte) this.getDurability());
        }

        return data;
    }

    /**
     * Sets the MaterialData for this stack of items
     *
     * @param data New MaterialData for this item
     */
    public void setData(@Nullable MaterialData data) {
        if (data == null) {
            this.data = data;
        } else {
            Material mat = Bukkit.getUnsafe().toLegacy(getType());

            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = data;
            } else {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    /**
     * Sets the durability of this item
     *
     * @param durability Durability of this item
     * @deprecated durability is now part of ItemMeta. To avoid confusion and
     * misuse, {@link #getItemMeta()}, {@link #setItemMeta(ItemMeta)} and
     * {@link Damageable#setDamage(int)} should be used instead. This is because
     * any call to this method will be overwritten by subsequent setting of
     * ItemMeta which was created before this call.
     */
    @Deprecated
    public void setDurability(final short durability) {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            setItemMeta(meta);
        }
    }

    /**
     * Gets the durability of this item
     *
     * @return Durability of this item
     * @deprecated see {@link #setDurability(short)}
     */
    @Deprecated
    public short getDurability() {
        ItemMeta meta = getItemMeta();
        return (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();
    }

    /**
     * Get the maximum stacksize for the material hold in this ItemStack.
     * (Returns -1 if it has no idea)
     *
     * @return The maximum you can stack this material to.
     */
    @Utility
    public int getMaxStackSize() {
        Material material = getType();
        if (material != null) {
            return material.getMaxStackSize();
        }
        return -1;
    }

    private void createData(final byte data) {
        this.data = type.getNewData(data);
    }

    @Override
    @Utility
    public String toString() {
        StringBuilder toString = new StringBuilder("ItemStack{").append(getType().name()).append(" x ").append(getAmount());
        if (hasItemMeta()) {
            toString.append(", ").append(getItemMeta());
        }
        return toString.append('}').toString();
    }

    @Override
    @Utility
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemStack)) {
            return false;
        }

        ItemStack stack = (ItemStack) obj;
        return getAmount() == stack.getAmount() && isSimilar(stack);
    }

    /**
     * This method is the same as equals, but does not consider stack size
     * (amount).
     *
     * @param stack the item stack to compare to
     * @return true if the two stacks are equal, ignoring the amount
     */
    @Utility
    public boolean isSimilar(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        Material comparisonType = (this.type.isLegacy()) ? Bukkit.getUnsafe().fromLegacy(this.getData(), true) : this.type; // This may be called from legacy item stacks, try to get the right material
        return comparisonType == stack.getType() && getDurability() == stack.getDurability() && hasItemMeta() == stack.hasItemMeta() && (hasItemMeta() ? Bukkit.getItemFactory().equals(getItemMeta(), stack.getItemMeta()) : true);
    }

    @NotNull
    @Override
    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack) super.clone();

            if (this.meta != null) {
                itemStack.meta = this.meta.clone();
            }

            if (this.data != null) {
                itemStack.data = this.data.clone();
            }

            return itemStack;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    @Utility
    public int hashCode() {
        int hash = 1;

        hash = hash * 31 + getType().hashCode();
        hash = hash * 31 + getAmount();
        hash = hash * 31 + (getDurability() & 0xffff);
        hash = hash * 31 + (hasItemMeta() ? (meta == null ? getItemMeta().hashCode() : meta.hashCode()) : 0);

        return hash;
    }

    /**
     * Checks if this ItemStack contains the given {@link Enchantment}
     *
     * @param ench Enchantment to test
     * @return True if this has the given enchantment
     */
    public boolean containsEnchantment(@NotNull Enchantment ench) {
        return meta == null ? false : meta.hasEnchant(ench);
    }

    /**
     * Gets the level of the specified enchantment on this item stack
     *
     * @param ench Enchantment to check
     * @return Level of the enchantment, or 0
     */
    public int getEnchantmentLevel(@NotNull Enchantment ench) {
        return meta == null ? 0 : meta.getEnchantLevel(ench);
    }

    /**
     * Gets a map containing all enchantments and their levels on this item.
     *
     * @return Map of enchantments.
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return meta == null ? ImmutableMap.<Enchantment, Integer>of() : meta.getEnchants();
    }

    /**
     * Adds the specified enchantments to this item stack.
     * <p>
     * This method is the same as calling {@link
     * #addEnchantment(org.bukkit.enchantments.Enchantment, int)} for each
     * element of the map.
     *
     * @param enchantments Enchantments to add
     * @throws IllegalArgumentException if the specified enchantments is null
     * @throws IllegalArgumentException if any specific enchantment or level
     *     is null. <b>Warning</b>: Some enchantments may be added before this
     *     exception is thrown.
     */
    @Utility
    public void addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "Enchantments cannot be null");
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     * @throws IllegalArgumentException if enchantment null, or enchantment is
     *     not applicable
     */
    @Utility
    public void addEnchantment(@NotNull Enchantment ench, int level) {
        Validate.notNull(ench, "Enchantment cannot be null");
        if ((level < ench.getStartLevel()) || (level > ench.getMaxLevel())) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel() + ")");
        } else if (!ench.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }

        addUnsafeEnchantment(ench, level);
    }

    /**
     * Adds the specified enchantments to this item stack in an unsafe manner.
     * <p>
     * This method is the same as calling {@link
     * #addUnsafeEnchantment(org.bukkit.enchantments.Enchantment, int)} for
     * each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    @Utility
    public void addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     * <p>
     * This method is unsafe and will ignore level restrictions or item type.
     * Use at your own discretion.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     */
    public void addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        ItemMeta itemMeta = (meta == null ? meta = Bukkit.getItemFactory().getItemMeta(type) : meta);
        if (itemMeta != null) {
            itemMeta.addEnchant(ench, level, true);
        }
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this
     * ItemStack
     *
     * @param ench Enchantment to remove
     * @return Previous level, or 0
     */
    public int removeEnchantment(@NotNull Enchantment ench) {
        int level = getEnchantmentLevel(ench);
        if (level == 0 || meta == null) {
            return level;
        }
        meta.removeEnchant(ench);
        return level;
    }

    @Override
    @NotNull
    @Utility
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("v", Bukkit.getUnsafe().getDataVersion()); // Include version to indicate we are using modern material names (or LEGACY prefix)
        result.put("type", getType().name());

        if (getAmount() != 1) {
            result.put("amount", getAmount());
        }

        ItemMeta meta = getItemMeta();
        if (!Bukkit.getItemFactory().equals(meta, null)) {
            result.put("meta", meta);
        }

        return result;
    }

    /**
     * Required method for configuration serialization
     *
     * @param args map to deserialize
     * @return deserialized item stack
     * @see ConfigurationSerializable
     */
    @NotNull
    public static ItemStack deserialize(@NotNull Map<String, Object> args) {
        int version = (args.containsKey("v")) ? ((Number) args.get("v")).intValue() : -1;
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        Material type;
        if (version < 0) {
            type = Material.getMaterial(Material.LEGACY_PREFIX + args.get("type"));

            byte dataVal = (type != null && type.getMaxDurability() == 0) ? (byte) damage : 0; // Actually durable items get a 0 passed into conversion
            type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, dataVal), true);

            // We've converted now so the data val isn't a thing and can be reset
            if (dataVal != 0) {
                damage = 0;
            }
        } else {
            type = Bukkit.getUnsafe().getMaterial((String) args.get("type"), version);
        }

        if (args.containsKey("amount")) {
            amount = ((Number) args.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (args.containsKey("enchantments")) { // Backward compatiblity, @deprecated
            Object raw = args.get("enchantments");

            if (raw instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) raw;

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                ((ItemMeta) raw).setVersion(version);
                result.setItemMeta((ItemMeta) raw);
            }
        }

        if (version < 0) {
            // Set damage again incase meta overwrote it
            if (args.containsKey("damage")) {
                result.setDurability(damage);
            }
        }

        return result.ensureServerConversions(); // Paper
    }

    // Paper start
    /**
     * Edits the {@link ItemMeta} of this stack.
     * <p>
     * The {@link java.util.function.Consumer} must only interact
     * with this stack's {@link ItemMeta} through the provided {@link ItemMeta} instance.
     * Calling this method or any other meta-related method of the {@link ItemStack} class
     * (such as {@link #getItemMeta()}, {@link #addItemFlags(ItemFlag...)}, {@link #lore()}, etc.)
     * from inside the consumer is disallowed and will produce undefined results or exceptions.
     * </p>
     *
     * @param consumer the meta consumer
     * @return {@code true} if the edit was successful, {@code false} otherwise
     */
    public boolean editMeta(final @NotNull java.util.function.Consumer<? super ItemMeta> consumer) {
        final ItemMeta meta = this.getItemMeta();
        if (meta != null) {
            consumer.accept(meta);
            this.setItemMeta(meta);
            return true;
        }
        return false;
    }
    // Paper end

    /**
     * Get a copy of this ItemStack's {@link ItemMeta}.
     *
     * @return a copy of the current ItemStack's ItemData
     */
    @UndefinedNullability // Paper
    public ItemMeta getItemMeta() {
        return this.meta == null ? Bukkit.getItemFactory().getItemMeta(this.type) : this.meta.clone();
    }

    /**
     * Checks to see if any meta data has been defined.
     *
     * @return Returns true if some meta data has been set for this item
     */
    public boolean hasItemMeta() {
        return !Bukkit.getItemFactory().equals(meta, null);
    }

    /**
     * Set the ItemMeta of this ItemStack.
     *
     * @param itemMeta new ItemMeta, or null to indicate meta data be cleared.
     * @return True if successfully applied ItemMeta, see {@link
     *     ItemFactory#isApplicable(ItemMeta, ItemStack)}
     * @throws IllegalArgumentException if the item meta was not created by
     *     the {@link ItemFactory}
     */
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        return setItemMeta0(itemMeta, type);
    }

    /*
     * Cannot be overridden, so it's safe for constructor call
     */
    private boolean setItemMeta0(@Nullable ItemMeta itemMeta, @NotNull Material material) {
        if (itemMeta == null) {
            this.meta = null;
            return true;
        }
        if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            return false;
        }
        this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);

        Material newType = Bukkit.getItemFactory().updateMaterial(meta, material);
        if (this.type != newType) {
            this.type = newType;
        }

        if (this.meta == itemMeta) {
            this.meta = itemMeta.clone();
        }

        return true;
    }

    // Paper start
    @NotNull
    @Override
    public net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowItem> asHoverEvent(final @NotNull java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowItem> op) {
        return org.bukkit.Bukkit.getServer().getItemFactory().asHoverEvent(this, op);
    }

    /**
     * Get the formatted display name of the {@link ItemStack}.
     *
     * @return display name of the {@link ItemStack}
     */
    public @NotNull net.kyori.adventure.text.Component displayName() {
        return Bukkit.getServer().getItemFactory().displayName(this);
    }

    /**
     * Minecraft updates are converting simple item stacks into more complex NBT oriented Item Stacks.
     *
     * Use this method to to ensure any desired data conversions are processed.
     * The input itemstack will not be the same as the returned itemstack.
     *
     * @return A potentially Data Converted ItemStack
     */
    @NotNull
    public ItemStack ensureServerConversions() {
        return Bukkit.getServer().getItemFactory().ensureServerConversions(this);
    }

    /**
     * Deserializes this itemstack from raw NBT bytes. NBT is safer for data migrations as it will
     * use the built in data converter instead of bukkits dangerous serialization system.
     *
     * This expects that the DataVersion was stored on the root of the Compound, as saved from
     * the {@link #serializeAsBytes()} API returned.
     * @param bytes bytes representing an item in NBT
     * @return ItemStack migrated to this version of Minecraft if needed.
     */
    @NotNull
    public static ItemStack deserializeBytes(@NotNull byte[] bytes) {
        return org.bukkit.Bukkit.getUnsafe().deserializeItem(bytes);
    }

    /**
     * Serializes this itemstack to raw bytes in NBT. NBT is safer for data migrations as it will
     * use the built in data converter instead of bukkits dangerous serialization system.
     * @return bytes representing this item in NBT.
     */
    @NotNull
    public byte[] serializeAsBytes() {
        return org.bukkit.Bukkit.getUnsafe().serializeItem(this);
    }

    /**
     * Gets the Display name as seen in the Client.
     * Currently the server only supports the English language. To override this,
     * You must replace the language file embedded in the server jar.
     *
     * @return Display name of Item
     */
    @Nullable
    public String getI18NDisplayName() {
        return Bukkit.getServer().getItemFactory().getI18NDisplayName(this);
    }

    public int getMaxItemUseDuration() {
        if (type == null || type == Material.AIR || !type.isItem()) {
            return 0;
        }
        // Requires access to NMS
        return ensureServerConversions().getMaxItemUseDuration();
    }

    /**
     * Clones the itemstack and returns it a single quantity.
     * @return The new itemstack with 1 quantity
     */
    @NotNull
    public ItemStack asOne() {
        return asQuantity(1);
    }

    /**
     * Clones the itemstack and returns it as the specified quantity
     * @param qty The quantity of the cloned item
     * @return The new itemstack with specified quantity
     */
    @NotNull
    public ItemStack asQuantity(int qty) {
        ItemStack clone = clone();
        clone.setAmount(qty);
        return clone;
    }

    /**
     * Adds 1 to this itemstack. Will not go over the items max stack size.
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack add() {
        return add(1);
    }

    /**
     * Adds quantity to this itemstack. Will not go over the items max stack size.
     *
     * @param qty The amount to add
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack add(int qty) {
        setAmount(Math.min(getMaxStackSize(), getAmount() + qty));
        return this;
    }

    /**
     * Subtracts 1 to this itemstack.  Going to 0 or less will invalidate the item.
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack subtract() {
        return subtract(1);
    }

    /**
     * Subtracts quantity to this itemstack. Going to 0 or less will invalidate the item.
     *
     * @param qty The amount to add
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack subtract(int qty) {
        setAmount(Math.max(0, getAmount() - qty));
        return this;
    }

    /**
     * If the item has lore, returns it, else it will return null
     * @return The lore, or null
     * @deprecated in favor of {@link #lore()}
     */
    @Deprecated
    public @Nullable List<String> getLore() {
        if (!hasItemMeta()) {
            return null;
        }
        ItemMeta itemMeta = getItemMeta();
        if (!itemMeta.hasLore()) {
            return null;
        }
        return itemMeta.getLore();
    }

    /**
     * If the item has lore, returns it, else it will return null
     * @return The lore, or null
     */
    public @Nullable List<net.kyori.adventure.text.Component> lore() {
        if (!this.hasItemMeta()) {
            return null;
        }
        final ItemMeta itemMeta = getItemMeta();
        if (!itemMeta.hasLore()) {
            return null;
        }
        return itemMeta.lore();
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     * @deprecated in favour of {@link #lore(List)}
     */
    @Deprecated
    public void setLore(@Nullable List<String> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot set lore on " + getType());
        }
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    public void lore(@Nullable List<net.kyori.adventure.text.Component> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot set lore on " + getType());
        }
        itemMeta.lore(lore);
        this.setItemMeta(itemMeta);
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hideflags which shouldn't be rendered
     */
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot add flags on " + getType());
        }
        itemMeta.addItemFlags(itemFlags);
        setItemMeta(itemMeta);
    }

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hideflags which should be removed
     */
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot remove flags on " + getType());
        }
        itemMeta.removeItemFlags(itemFlags);
        setItemMeta(itemMeta);
    }

    /**
     * Get current set itemFlags. The collection returned is unmodifiable.
     *
     * @return A set of all itemFlags set
     */
    @NotNull
    public Set<ItemFlag> getItemFlags() {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return java.util.Collections.emptySet();
        }
        return itemMeta.getItemFlags();
    }

    /**
     * Check if the specified flag is present on this item.
     *
     * @param flag the flag to check
     * @return if it is present
     */
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        ItemMeta itemMeta = getItemMeta();
        return itemMeta != null && itemMeta.hasItemFlag(flag);
    }

    /**
     * Gets the translation key for this itemstack.
     * This is not the same as getting the translation key
     * for the material of this itemstack.
     *
     * @return the translation key
     */
    @NotNull
    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    /**
     * Gets the item rarity of the itemstack. The rarity can change based on enchantements.
     *
     * @return the itemstack rarity
     */
    @NotNull
    public io.papermc.paper.inventory.ItemRarity getRarity() {
        return Bukkit.getUnsafe().getItemStackRarity(this);
    }

    /**
     * Checks if an itemstack can repair this itemstack.
     * Returns false if {@code this} or {@code repairMaterial}'s type is not an item ({@link Material#isItem()}).
     *
     * @param repairMaterial the repair material
     * @return true if it is repairable by, false if not
     */
    public boolean isRepairableBy(@NotNull ItemStack repairMaterial) {
        return Bukkit.getUnsafe().isValidRepairItemStack(this, repairMaterial);
    }

    /**
     * Checks if this itemstack can repair another.
     * Returns false if {@code this} or {@code toBeRepaired}'s type is not an item ({@link Material#isItem()}).
     *
     * @param toBeRepaired the itemstack to be repaired
     * @return true if it can repair, false if not
     */
    public boolean canRepair(@NotNull ItemStack toBeRepaired) {
        return Bukkit.getUnsafe().isValidRepairItemStack(toBeRepaired, this);
    }
    // Paper end

    // Purpur start
    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @return the display name that is set
     */
    @NotNull
    public String getDisplayName() {
        return getItemMeta().getDisplayName();
    }

    /**
     * Sets the display name.
     *
     * @param name the name to set
     */
    public void setDisplayName(@Nullable String name) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(name);
        setItemMeta(itemMeta);
    }

    /**
     * Checks for existence of a display name.
     *
     * @return true if this has a display name
     */
    public boolean hasDisplayName() {
        return hasItemMeta() && getItemMeta().hasDisplayName();
    }

    /**
     * Gets the localized display name that is set.
     * <p>
     * Plugins should check that hasLocalizedName() returns <code>true</code>
     * before calling this method.
     *
     * @return the localized name that is set
     */
    @NotNull
    public String getLocalizedName() {
        return getItemMeta().getLocalizedName();
    }

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     */
    public void setLocalizedName(@Nullable String name) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLocalizedName(name);
        setItemMeta(itemMeta);
    }

    /**
     * Checks for existence of a localized name.
     *
     * @return true if this has a localized name
     */
    public boolean hasLocalizedName() {
        return hasItemMeta() && getItemMeta().hasLocalizedName();
    }

    /**
     * Checks for existence of lore.
     *
     * @return true if this has lore
     */
    public boolean hasLore() {
        return hasItemMeta() && getItemMeta().hasLore();
    }

    /**
     * Checks for existence of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return true if this enchantment exists for this meta
     */
    public boolean hasEnchant(@NotNull Enchantment ench) {
        return hasItemMeta() && getItemMeta().hasEnchant(ench);
    }

    /**
     * Checks for the level of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return The level that the specified enchantment has, or 0 if none
     */
    public int getEnchantLevel(@NotNull Enchantment ench) {
        return getItemMeta().getEnchantLevel(ench);
    }

    /**
     * Returns a copy the enchantments in this ItemMeta. <br>
     * Returns an empty map if none.
     *
     * @return An immutable copy of the enchantments
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchants() {
        return getItemMeta().getEnchants();
    }

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param ench Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     */
    public boolean addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.addEnchant(ench, level, ignoreLevelRestriction);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param ench Enchantment to remove
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     */
    public boolean removeEnchant(@NotNull Enchantment ench) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.removeEnchant(ench);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Checks for the existence of any enchantments.
     *
     * @return true if an enchantment exists on this meta
     */
    public boolean hasEnchants() {
        return hasItemMeta() && getItemMeta().hasEnchants();
    }

    /**
     * Checks if the specified enchantment conflicts with any enchantments in
     * this ItemMeta.
     *
     * @param ench enchantment to test
     * @return true if the enchantment conflicts, false otherwise
     */
    public boolean hasConflictingEnchant(@NotNull Enchantment ench) {
        return hasItemMeta() && getItemMeta().hasConflictingEnchant(ench);
    }

    /**
     * Sets the custom model data.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     *
     * @param data the data to set, or null to clear
     */
    public void setCustomModelData(@Nullable Integer data) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setCustomModelData(data);
        setItemMeta(itemMeta);
    }

    /**
     * Gets the custom model data that is set.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     * <p>
     * Plugins should check that hasCustomModelData() returns <code>true</code>
     * before calling this method.
     *
     * @return the localized name that is set
     */
    public int getCustomModelData() {
        return getItemMeta().getCustomModelData();
    }

    /**
     * Checks for existence of custom model data.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     *
     * @return true if this has custom model data
     */
    public boolean hasCustomModelData() {
        return hasItemMeta() && getItemMeta().hasCustomModelData();
    }

    /**
     * Returns whether the item has block data currently attached to it.
     *
     * @return whether block data is already attached
     */
    public boolean hasBlockData() {
        return hasItemMeta() && ((BlockDataMeta) getItemMeta()).hasBlockData();
    }

    /**
     * Returns the currently attached block data for this item or creates a new
     * one if one doesn't exist.
     *
     * The state is a copy, it must be set back (or to another item) with
     * {@link #setBlockData(BlockData)}
     *
     * @param material the material we wish to get this data in the context of
     * @return the attached data or new data
     */
    @NotNull
    public BlockData getBlockData(@NotNull Material material) {
        return ((BlockDataMeta) getItemMeta()).getBlockData(material);
    }

    /**
     * Attaches a copy of the passed block data to the item.
     *
     * @param blockData the block data to attach to the block.
     * @throws IllegalArgumentException if the blockData is null or invalid for
     * this item.
     */
    public void setBlockData(@NotNull BlockData blockData) {
        ItemMeta itemMeta = getItemMeta();
        ((BlockDataMeta) itemMeta).setBlockData(blockData);
        setItemMeta(itemMeta);
    }

    /**
     * Gets the repair penalty
     *
     * @return the repair penalty
     */
    public int getRepairCost() {
        return ((Repairable) getItemMeta()).getRepairCost();
    }

    /**
     * Sets the repair penalty
     *
     * @param cost repair penalty
     */
    public void setRepairCost(int cost) {
        ItemMeta itemMeta = getItemMeta();
        ((Repairable) itemMeta).setRepairCost(cost);
        setItemMeta(itemMeta);
    }

    /**
     * Checks to see if this has a repair penalty
     *
     * @return true if this has a repair penalty
     */
    public boolean hasRepairCost() {
        return hasItemMeta() && ((Repairable) getItemMeta()).hasRepairCost();
    }

    /**
     * Return if the unbreakable tag is true. An unbreakable item will not lose
     * durability.
     *
     * @return true if the unbreakable tag is true
     */
    public boolean isUnbreakable() {
        return hasItemMeta() && getItemMeta().isUnbreakable();
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     */
    public void setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        setItemMeta(itemMeta);
    }

    /**
     * Checks for the existence of any AttributeModifiers.
     *
     * @return true if any AttributeModifiers exist
     */
    public boolean hasAttributeModifiers() {
        return hasItemMeta() && getItemMeta().hasAttributeModifiers();
    }

    /**
     * Return an immutable copy of all Attributes and
     * their modifiers in this ItemMeta.<br>
     * Returns null if none exist.
     *
     * @return an immutable {@link Multimap} of Attributes
     *         and their AttributeModifiers, or null if none exist
     */
    @Nullable
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return getItemMeta().getAttributeModifiers();
    }

    /**
     * Return an immutable copy of all {@link Attribute}s and their
     * {@link AttributeModifier}s for a given {@link EquipmentSlot}.<br>
     * Any {@link AttributeModifier} that does have have a given
     * {@link EquipmentSlot} will be returned. This is because
     * AttributeModifiers without a slot are active in any slot.<br>
     * If there are no attributes set for the given slot, an empty map
     * will be returned.
     *
     * @param slot the {@link EquipmentSlot} to check
     * @return the immutable {@link Multimap} with the
     *         respective Attributes and modifiers, or an empty map
     *         if no attributes are set.
     */
    @NotNull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable EquipmentSlot slot) {
        return getItemMeta().getAttributeModifiers(slot);
    }

    /**
     * Return an immutable copy of all {@link AttributeModifier}s
     * for a given {@link Attribute}
     *
     * @param attribute the {@link Attribute}
     * @return an immutable collection of {@link AttributeModifier}s
     *          or null if no AttributeModifiers exist for the Attribute.
     * @throws NullPointerException if Attribute is null
     */
    @Nullable
    public Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        return getItemMeta().getAttributeModifiers(attribute);
    }

    /**
     * Add an Attribute and it's Modifier.
     * AttributeModifiers can now support {@link EquipmentSlot}s.
     * If not set, the {@link AttributeModifier} will be active in ALL slots.
     * <br>
     * Two {@link AttributeModifier}s that have the same {@link java.util.UUID}
     * cannot exist on the same Attribute.
     *
     * @param attribute the {@link Attribute} to modify
     * @param modifier the {@link AttributeModifier} specifying the modification
     * @return true if the Attribute and AttributeModifier were
     *         successfully added
     * @throws NullPointerException if Attribute is null
     * @throws NullPointerException if AttributeModifier is null
     * @throws IllegalArgumentException if AttributeModifier already exists
     */
    public boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.addAttributeModifier(attribute, modifier);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Set all {@link Attribute}s and their {@link AttributeModifier}s.
     * To clear all currently set Attributes and AttributeModifiers use
     * null or an empty Multimap.
     * If not null nor empty, this will filter all entries that are not-null
     * and add them to the ItemStack.
     *
     * @param attributeModifiers the new Multimap containing the Attributes
     *                           and their AttributeModifiers
     */
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setAttributeModifiers(attributeModifiers);
        setItemMeta(itemMeta);
    }

    /**
     * Remove all {@link AttributeModifier}s associated with the given
     * {@link Attribute}.
     * This will return false if nothing was removed.
     *
     * @param attribute attribute to remove
     * @return  true if all modifiers were removed from a given
     *                  Attribute. Returns false if no attributes were
     *                  removed.
     * @throws NullPointerException if Attribute is null
     */
    public boolean removeAttributeModifier(@NotNull Attribute attribute) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.removeAttributeModifier(attribute);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Remove all {@link Attribute}s and {@link AttributeModifier}s for a
     * given {@link EquipmentSlot}.<br>
     * If the given {@link EquipmentSlot} is null, this will remove all
     * {@link AttributeModifier}s that do not have an EquipmentSlot set.
     *
     * @param slot the {@link EquipmentSlot} to clear all Attributes and
     *             their modifiers for
     * @return true if all modifiers were removed that match the given
     *         EquipmentSlot.
     */
    public boolean removeAttributeModifier(@Nullable EquipmentSlot slot) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.removeAttributeModifier(slot);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Remove a specific {@link Attribute} and {@link AttributeModifier}.
     * AttributeModifiers are matched according to their {@link java.util.UUID}.
     *
     * @param attribute the {@link Attribute} to remove
     * @param modifier the {@link AttributeModifier} to remove
     * @return if any attribute modifiers were remove
     *
     * @throws NullPointerException if the Attribute is null
     * @throws NullPointerException if the AttributeModifier is null
     *
     * @see AttributeModifier#getUniqueId()
     */
    public boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        ItemMeta itemMeta = getItemMeta();
        boolean result = itemMeta.removeAttributeModifier(attribute, modifier);
        setItemMeta(itemMeta);
        return result;
    }

    /**
     * Returns a custom tag container capable of storing tags on the object.
     *
     * Note that the tags stored on this container are all stored under their
     * own custom namespace therefore modifying default tags using this
     * {@link PersistentDataHolder} is impossible.
     *
     * @return the persistent metadata container
     */
    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        return getItemMeta().getPersistentDataContainer();
    }

    /**
     * Checks to see if this item has damage
     *
     * @return true if this has damage
     */
    public boolean hasDamage() {
        return hasItemMeta() && ((Damageable) getItemMeta()).hasDamage();
    }

    /**
     * Gets the damage
     *
     * @return the damage
     */
    public int getDamage() {
        return ((Damageable) getItemMeta()).getDamage();
    }

    /**
     * Sets the damage
     *
     * @param damage item damage
     */
    public void setDamage(int damage) {
        ItemMeta itemMeta = getItemMeta();
        ((Damageable) itemMeta).setDamage(damage);
        setItemMeta(itemMeta);
    }

    /**
     * Gets the collection of namespaced keys that the item can destroy in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @return Set of {@link com.destroystokyo.paper.Namespaced}
     */
    @NotNull
    public Set<Namespaced> getDestroyableKeys() {
        return getItemMeta().getDestroyableKeys();
    }

    /**
     * Sets the collection of namespaced keys that the item can destroy in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canDestroy Collection of {@link com.destroystokyo.paper.Namespaced}
     */
    public void setDestroyableKeys(@NotNull Collection<Namespaced> canDestroy) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDestroyableKeys(canDestroy);
        setItemMeta(itemMeta);
    }

    /**
     * Gets the collection of namespaced keys that the item can be placed on in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @return Set of {@link com.destroystokyo.paper.Namespaced}
     */
    @NotNull
    public Set<Namespaced> getPlaceableKeys() {
        return getItemMeta().getPlaceableKeys();
    }

    /**
     * Sets the set of namespaced keys that the item can be placed on in {@link org.bukkit.GameMode#ADVENTURE}
     *
     * @param canPlaceOn Collection of {@link com.destroystokyo.paper.Namespaced}
     */
    @NotNull
    public void setPlaceableKeys(@NotNull Collection<Namespaced> canPlaceOn) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setPlaceableKeys(canPlaceOn);
        setItemMeta(itemMeta);
    }

    /**
     * Checks for the existence of any keys that the item can be placed on
     *
     * @return true if this item has placeable keys
     */
    public boolean hasPlaceableKeys() {
        return hasItemMeta() && getItemMeta().hasPlaceableKeys();
    }

    /**
     * Checks for the existence of any keys that the item can destroy
     *
     * @return true if this item has destroyable keys
     */
    public boolean hasDestroyableKeys() {
        return hasItemMeta() && getItemMeta().hasDestroyableKeys();
    }

    /**
     * Repairs this item by 1 durability
     */
    public void repair() {
        repair(1);
    }

    /**
     * Damages this item by 1 durability
     *
     * @return True if damage broke the item
     */
    public boolean damage() {
        return damage(1);
    }

    /**
     * Repairs this item's durability by amount
     *
     * @param amount Amount of durability to repair
     */
    public void repair(int amount) {
        damage(-amount);
    }

    /**
     * Damages this item's durability by amount
     *
     * @param amount Amount of durability to damage
     * @return True if damage broke the item
     */
    public boolean damage(int amount) {
        return damage(amount, false);
    }

    /**
     * Damages this item's durability by amount
     *
     * @param amount Amount of durability to damage
     * @param ignoreUnbreaking Ignores unbreaking enchantment
     * @return True if damage broke the item
     */
    public boolean damage(int amount, boolean ignoreUnbreaking) {
        Damageable damageable = (Damageable) getItemMeta();
        if (amount > 0) {
            int unbreaking = getEnchantLevel(Enchantment.DURABILITY);
            int reduce = 0;
            for (int i = 0; unbreaking > 0 && i < amount; ++i) {
                if (reduceDamage(java.util.concurrent.ThreadLocalRandom.current(), unbreaking)) {
                    ++reduce;
                }
            }
            amount -= reduce;
            if (amount <= 0) {
                return isBroke(damageable.getDamage());
            }
        }
        int damage = damageable.getDamage() + amount;
        damageable.setDamage(damage);
        setItemMeta((ItemMeta) damageable);
        return isBroke(damage);
    }

    public boolean isBroke(int damage) {
        if (damage > getType().getMaxDurability()) {
            if (getAmount() > 0) {
                // ensure it "breaks"
                setAmount(0);
            }
            return true;
        }
        return false;
    }

    private boolean reduceDamage(java.util.Random random, int unbreaking) {
        if (getType().isArmor()) {
            return random.nextFloat() < 0.6F;
        }
        return random.nextInt(unbreaking + 1) > 0;
    }
    // Purpur end
}
