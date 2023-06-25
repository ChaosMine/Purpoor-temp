package org.bukkit;

import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * This interface provides value conversions that may be specific to a
 * runtime, or have arbitrary meaning (read: magic values).
 * <p>
 * Their existence and behavior is not guaranteed across future versions. They
 * may be poorly named, throw exceptions, have misleading parameters, or any
 * other bad programming practice.
 */
public interface UnsafeValues {
	// Paper start
	net.kyori.adventure.text.flattener.ComponentFlattener componentFlattener();

	net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer plainComponentSerializer();

	net.kyori.adventure.text.serializer.gson.GsonComponentSerializer gsonComponentSerializer();

	net.kyori.adventure.text.serializer.gson.GsonComponentSerializer colorDownsamplingGsonComponentSerializer();

	net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer legacyComponentSerializer();
	// Paper end

	Material toLegacy(Material material);

	Material fromLegacy(Material material);

	Material fromLegacy(MaterialData material);

	Material fromLegacy(MaterialData material, boolean itemPriority);

	BlockData fromLegacy(Material material, byte data);

	Material getMaterial(String material, int version);

	int getDataVersion();

	ItemStack modifyItemStack(ItemStack stack, String arguments);

	void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException;

	/**
	 * Load an advancement represented by the specified string into the server.
	 * The advancement format is governed by Minecraft and has no specified
	 * layout.
	 * <br>
	 * It is currently a JSON object, as described by the Minecraft Wiki:
	 * http://minecraft.gamepedia.com/Advancements
	 * <br>
	 * Loaded advancements will be stored and persisted across server restarts
	 * and reloads.
	 * <br>
	 * Callers should be prepared for {@link Exception} to be thrown.
	 *
	 * @param key         the unique advancement key
	 * @param advancement representation of the advancement
	 * @return the loaded advancement or null if an error occurred
	 */
	Advancement loadAdvancement(NamespacedKey key, String advancement);

	/**
	 * Delete an advancement which was loaded and saved by
	 * {@link #loadAdvancement(org.bukkit.NamespacedKey, java.lang.String)}.
	 * <br>
	 * This method will only remove advancement from persistent storage. It
	 * should be accompanied by a call to {@link Server#reloadData()} in order
	 * to fully remove it from the running instance.
	 *
	 * @param key the unique advancement key
	 * @return true if a file matching this key was found and deleted
	 */
	boolean removeAdvancement(NamespacedKey key);

	// Paper start

	/**
	 * Called once by the version command on first use, then cached.
	 *
	 * @return Paper's VersionFetcher
	 */
	default com.destroystokyo.paper.util.VersionFetcher getVersionFetcher() {
		return new com.destroystokyo.paper.util.VersionFetcher.DummyVersionFetcher();
	}

	boolean isSupportedApiVersion(String apiVersion);

	static boolean isLegacyPlugin(org.bukkit.plugin.Plugin plugin) {
		return !Bukkit.getUnsafe().isSupportedApiVersion(plugin.getDescription().getAPIVersion());
	}

	byte[] serializeItem(ItemStack item);

	ItemStack deserializeItem(byte[] data);

	/**
	 * Return the translation key for the Material, so the client can translate it into the active
	 * locale when using a {@link net.kyori.adventure.text.TranslatableComponent}.
	 *
	 * @param mat Material to check
	 * @return the translation key
	 */
	String getTranslationKey(Material mat);

	/**
	 * Return the translation key for the Block, so the client can translate it into the active
	 * locale when using a {@link net.kyori.adventure.text.TranslatableComponent}.
	 *
	 * @param block Block to check
	 * @return the translation key
	 */
	String getTranslationKey(org.bukkit.block.Block block);

	/**
	 * Return the translation key for the EntityType, so the client can translate it into the active
	 * locale when using a {@link net.kyori.adventure.text.TranslatableComponent}.<br>
	 * This is <code>null</code>, when the EntityType isn't known to NMS (custom entities)
	 *
	 * @param type EntityType to check
	 * @return the translation key
	 */
	String getTranslationKey(org.bukkit.entity.EntityType type);

	/**
	 * Return the translation key for the ItemStack, so the client can translate it into the active
	 * locale when using a {@link net.kyori.adventure.text.TranslatableComponent}.<br>
	 *
	 * @return the translation key
	 */
	String getTranslationKey(ItemStack itemStack);

	/**
	 * Creates and returns the next EntityId available.
	 * <p>
	 * Use this when sending custom packets, so that there are no collisions on the client or server.
	 *
	 * @return the next available entity id
	 */
  int nextEntityId();

	/**
	 * Gets the item rarity of a material. The material <b>MUST</b> be an item.
	 * Use {@link Material#isItem()} before this.
	 *
	 * @param material the material to get the rarity of
	 * @return the item rarity
	 */
  io.papermc.paper.inventory.ItemRarity getItemRarity(Material material);

	/**
	 * Gets the item rarity of the itemstack. The rarity can change based on enchantements.
	 *
	 * @param itemStack the itemstack to get the rarity of
	 * @return the itemstack rarity
	 */
  io.papermc.paper.inventory.ItemRarity getItemStackRarity(ItemStack itemStack);

	/**
	 * Checks if an itemstack can be repaired with another itemstack.
	 * Returns false if either argument's type is not an item ({@link Material#isItem()}).
	 *
	 * @param itemToBeRepaired the itemstack to be repaired
	 * @param repairMaterial   the repair material
	 * @return true if valid repair, false if not
	 */
  boolean isValidRepairItemStack(@org.jetbrains.annotations.NotNull ItemStack itemToBeRepaired, @org.jetbrains.annotations.NotNull ItemStack repairMaterial);

	/**
	 * Returns an immutable multimap of attributes for the material and slot.
	 * {@link Material#isItem()} must be true for this material.
	 *
	 * @param material      the material
	 * @param equipmentSlot the slot to get the attributes for
	 * @return an immutable multimap of attributes
	 * @throws IllegalArgumentException if {@link Material#isItem()} is false
	 */
	@org.jetbrains.annotations.NotNull com.google.common.collect.Multimap<org.bukkit.attribute.Attribute, org.bukkit.attribute.AttributeModifier> getItemAttributes(@org.jetbrains.annotations.NotNull Material material, @org.jetbrains.annotations.NotNull org.bukkit.inventory.EquipmentSlot equipmentSlot);

	/**
	 * Returns the server's protocol version.
	 *
	 * @return the server's protocol version
	 */
	int getProtocolVersion();
	// Paper end

	// Purpur start

	/**
	 * Serialize entity to byte array
	 *
	 * @param entity entity to serialize
	 * @return serialized entity
	 */
	byte[] serializeEntity(org.bukkit.entity.Entity entity);

	/**
	 * Deserialize an entity from byte array
	 * <p>
	 * The entity is not automatically spawned in the world. You will have to spawn
	 * the entity yourself with {@link org.bukkit.entity.Entity#spawnAt(Location)} or
	 * {@link org.bukkit.entity.Entity#spawnAt(Location, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason)}
	 *
	 * @param data  serialized entity
	 * @param world world entity belongs in
	 * @return deserialized entity
	 */
	org.bukkit.entity.Entity deserializeEntity(byte[] data, org.bukkit.World world);
	// Purpur end
}
