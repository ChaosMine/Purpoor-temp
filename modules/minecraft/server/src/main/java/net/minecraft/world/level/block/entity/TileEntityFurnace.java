package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsItem;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ContainerUtil;
import net.minecraft.world.IWorldInventory;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.player.AutoRecipeStackManager;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.AutoRecipeOutput;
import net.minecraft.world.inventory.IContainerProperties;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.RecipeCooking;
import net.minecraft.world.item.crafting.Recipes;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockFurnace;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
// CraftBukkit end

public abstract class TileEntityFurnace extends TileEntityContainer implements IWorldInventory, RecipeHolder, AutoRecipeOutput, ITickable {

	private static final int[] g = new int[]{0};
	private static final int[] h = new int[]{2, 1};
	private static final int[] i = new int[]{1};
	protected NonNullList<ItemStack> items;
	public int burnTime;
	private int ticksForCurrentFuel;
	public double cookSpeedMultiplier = 1.0; // Paper - cook speed multiplier API
	public int cookTime;
	public int cookTimeTotal;
	protected final IContainerProperties b;
	private final Object2IntOpenHashMap<MinecraftKey> n;
	protected final Recipes<? extends RecipeCooking> c;

	protected TileEntityFurnace(TileEntityTypes<?> tileentitytypes, Recipes<? extends RecipeCooking> recipes) {
		super(tileentitytypes);
		this.items = NonNullList.a(3, ItemStack.b);
		this.b = new IContainerProperties() {
			@Override
			public int getProperty(int i) {
				return switch (i) {
					case 0 -> TileEntityFurnace.this.burnTime;
					case 1 -> TileEntityFurnace.this.ticksForCurrentFuel;
					case 2 -> TileEntityFurnace.this.cookTime;
					case 3 -> TileEntityFurnace.this.cookTimeTotal;
					default -> 0;
				};
			}

			@Override
			public void setProperty(int i, int j) {
				switch (i) {
					case 0 -> TileEntityFurnace.this.burnTime = j;
					case 1 -> TileEntityFurnace.this.ticksForCurrentFuel = j;
					case 2 -> TileEntityFurnace.this.cookTime = j;
					case 3 -> TileEntityFurnace.this.cookTimeTotal = j;
				}

			}

			@Override
			public int a() {
				return 4;
			}
		};
		this.n = new Object2IntOpenHashMap<>();
		this.c = recipes;
	}

	private static Reference2IntMap<Item> cachedBurnDurations = null; // Paper - cache burn durations

	public static Reference2IntMap<Item> getBurnDurations() {
		return f();
	} // Paper - OBFHELPER

	public static Reference2IntMap<Item> f() {
		// Paper start - cache burn durations
		if (cachedBurnDurations != null) {
			return cachedBurnDurations;
		}
		// Paper end
		Reference2IntMap<Item> map = new Reference2IntLinkedOpenHashMap<>();

		a(map, Items.LAVA_BUCKET, 20000);
		a(map, Blocks.COAL_BLOCK, 16000);
		a(map, Items.BLAZE_ROD, 2400);
		a(map, Items.COAL, 1600);
		a(map, Items.CHARCOAL, 1600);
		a(map, TagsItem.LOGS, 300);
		a(map, TagsItem.PLANKS, 300);
		a(map, TagsItem.WOODEN_STAIRS, 300);
		a(map, TagsItem.WOODEN_SLABS, 150);
		a(map, TagsItem.WOODEN_TRAPDOORS, 300);
		a(map, TagsItem.WOODEN_PRESSURE_PLATES, 300);
		a(map, Blocks.OAK_FENCE, 300);
		a(map, Blocks.BIRCH_FENCE, 300);
		a(map, Blocks.SPRUCE_FENCE, 300);
		a(map, Blocks.JUNGLE_FENCE, 300);
		a(map, Blocks.DARK_OAK_FENCE, 300);
		a(map, Blocks.ACACIA_FENCE, 300);
		a(map, Blocks.OAK_FENCE_GATE, 300);
		a(map, Blocks.BIRCH_FENCE_GATE, 300);
		a(map, Blocks.SPRUCE_FENCE_GATE, 300);
		a(map, Blocks.JUNGLE_FENCE_GATE, 300);
		a(map, Blocks.DARK_OAK_FENCE_GATE, 300);
		a(map, Blocks.ACACIA_FENCE_GATE, 300);
		a(map, Blocks.NOTE_BLOCK, 300);
		a(map, Blocks.BOOKSHELF, 300);
		a(map, Blocks.LECTERN, 300);
		a(map, Blocks.JUKEBOX, 300);
		a(map, Blocks.CHEST, 300);
		a(map, Blocks.TRAPPED_CHEST, 300);
		a(map, Blocks.CRAFTING_TABLE, 300);
		a(map, Blocks.DAYLIGHT_DETECTOR, 300);
		a(map, TagsItem.BANNERS, 300);
		a(map, Items.BOW, 300);
		a(map, Items.FISHING_ROD, 300);
		a(map, Blocks.LADDER, 300);
		a(map, TagsItem.SIGNS, 200);
		a(map, Items.WOODEN_SHOVEL, 200);
		a(map, Items.WOODEN_SWORD, 200);
		a(map, Items.WOODEN_HOE, 200);
		a(map, Items.WOODEN_AXE, 200);
		a(map, Items.WOODEN_PICKAXE, 200);
		a(map, TagsItem.WOODEN_DOORS, 200);
		a(map, TagsItem.BOATS, 1200);
		a(map, TagsItem.WOOL, 100);
		a(map, TagsItem.WOODEN_BUTTONS, 100);
		a(map, Items.STICK, 100);
		a(map, TagsItem.SAPLINGS, 100);
		a(map, Items.BOWL, 100);
		a(map, TagsItem.CARPETS, 67);
		a(map, Blocks.DRIED_KELP_BLOCK, 4001);
		a(map, Items.CROSSBOW, 300);
		a(map, Blocks.BAMBOO, 50);
		a(map, Blocks.DEAD_BUSH, 100);
		a(map, Blocks.SCAFFOLDING, 400);
		a(map, Blocks.LOOM, 300);
		a(map, Blocks.BARREL, 300);
		a(map, Blocks.CARTOGRAPHY_TABLE, 300);
		a(map, Blocks.FLETCHING_TABLE, 300);
		a(map, Blocks.SMITHING_TABLE, 300);
		a(map, Blocks.COMPOSTER, 300);
		// Paper start - cache burn durations
		cachedBurnDurations = Reference2IntMaps.unmodifiable(new Reference2IntLinkedOpenHashMap<>(map));
		return cachedBurnDurations;
		// Paper end
	}

	// CraftBukkit start - add fields and methods
	private int maxStack = MAX_STACK;
	public List<HumanEntity> transaction = new ObjectArrayList<>();

	public List<ItemStack> getContents() {
		return this.items;
	}

	public void onOpen(CraftHumanEntity who) {
		transaction.add(who);
	}

	public void onClose(CraftHumanEntity who) {
		transaction.remove(who);
	}

	public List<HumanEntity> getViewers() {
		return transaction;
	}

	@Override
	public int getMaxStackSize() {
		return maxStack;
	}

	public void setMaxStackSize(int size) {
		maxStack = size;
	}
	// CraftBukkit end

	private static boolean b(Item item) {
		return TagsItem.NON_FLAMMABLE_WOOD.isTagged(item);
	}

	private static void a(Map<Item, Integer> map, Tag<Item> tag, int i) {
		Iterator iterator = tag.getTagged().iterator();

		while (iterator.hasNext()) {
			var item = (Item) iterator.next();

			if (!b(item)) {
				map.put(item, i);
			}
		}

	}

	private static void a(Reference2IntMap<Item> map, IMaterial imaterial, int i) {
		var item = imaterial.getItem();

		if (!b(item)) {
			map.put(item, i);
		}
	}

	private boolean isBurning() {
		return this.burnTime > 0;
	}

	@Override
	public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
		super.load(iblockdata, nbttagcompound);
		this.items = NonNullList.a(this.getSize(), ItemStack.b);
		ContainerUtil.b(nbttagcompound, this.items);
		this.burnTime = nbttagcompound.getShort("BurnTime");
		this.cookTime = nbttagcompound.getShort("CookTime");
		this.cookTimeTotal = nbttagcompound.getShort("CookTimeTotal");
		this.ticksForCurrentFuel = this.fuelTime(this.items.get(1));
		var nbttagcompound1 = nbttagcompound.getCompound("RecipesUsed");

		for (var s : nbttagcompound1.getKeys()) {
			this.n.put(new MinecraftKey(s), nbttagcompound1.getInt(s));
		}

		// Paper start - cook speed API
		if (nbttagcompound.hasKey("Paper.CookSpeedMultiplier")) {
			this.cookSpeedMultiplier = nbttagcompound.getDouble("Paper.CookSpeedMultiplier");
		}
		// Paper end
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.save(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short) this.burnTime);
		nbttagcompound.setShort("CookTime", (short) this.cookTime);
		nbttagcompound.setShort("CookTimeTotal", (short) this.cookTimeTotal);
		nbttagcompound.setDouble("Paper.CookSpeedMultiplier", this.cookSpeedMultiplier); // Paper - cook speed multiplier API
		ContainerUtil.a(nbttagcompound, this.items);
		var nbttagcompound1 = new NBTTagCompound();

		this.n.forEach((minecraftkey, integer) -> {
			nbttagcompound1.setInt(minecraftkey.toString(), integer);
		});
		nbttagcompound.set("RecipesUsed", nbttagcompound1);
		return nbttagcompound;
	}

	@Override
	public void tick() {
		var flag = this.isBurning();
		var flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.world.isClientSide) {
			var itemstack = this.items.get(1);

			// Purpur start
			var infiniteFuel = false;
			if (world.purpurConfig.furnaceInfiniteFuel && !isBurning() && itemstack.isEmpty() && !items.get(0).isEmpty() && world.getTime() % 20 == 0) {
				var pos = getPosition().down();
				var iblockdata = world.getTypeIfLoaded(pos);
				if (iblockdata != null && iblockdata.equals(Blocks.LAVA)) {
					var fluid = iblockdata.getFluid();
					if (fluid != null && fluid.isSource()) {
						world.setTypeAndData(pos, Blocks.AIR.getBlockData(), 3);
						itemstack = Items.LAVA_BUCKET.createItemStack();
						infiniteFuel = true;
					}
				}
			}
			// Purpur end

			if (!this.isBurning() && (itemstack.isEmpty() || this.items.get(0).isEmpty())) {
				if (!this.isBurning() && this.cookTime > 0) {
					this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
				}
			} else {
				IRecipe irecipe = this.world.getCraftingManager().craft((Recipes<RecipeCooking>) this.c, this, this.world).orElse(null); // Eclipse fail

				if (!this.isBurning() && this.canBurn(irecipe)) {
					// CraftBukkit start
					var fuel = CraftItemStack.asCraftMirror(itemstack);

					var furnaceBurnEvent = new FurnaceBurnEvent(CraftBlock.at(this.world, this.position), fuel, fuelTime(itemstack));
					this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);

					if (furnaceBurnEvent.isCancelled()) {
						return;
					}

					this.burnTime = furnaceBurnEvent.getBurnTime();
					this.ticksForCurrentFuel = this.burnTime;
					if (this.isBurning() && furnaceBurnEvent.isBurning()) {
						// CraftBukkit end
						flag1 = true;
						if (!itemstack.isEmpty() && furnaceBurnEvent.willConsumeFuel()) { // Paper
							var item = itemstack.getItem();

							itemstack.subtract(1);
							if (itemstack.isEmpty()) {
								var item1 = item.getCraftingRemainingItem();

								this.items.set(1, item1 == null ? ItemStack.b : new ItemStack(item1));
							}
						}
					}
				}

				if (this.isBurning() && this.canBurn(irecipe)) {
					++this.cookTime;
					if (this.cookTime >= this.cookTimeTotal) { // Paper - cook speed multiplier API
						this.cookTime = 0;
						this.cookTimeTotal = this.getRecipeCookingTime();
						this.burn(irecipe);
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				this.world.setTypeAndData(this.position, this.world.getType(this.position).set(BlockFurnace.LIT, this.isBurning()), 3);
			}

			if (infiniteFuel) this.items.set(1, ItemStack.NULL_ITEM); // Purpur
		}

		if (flag1) {
			this.update();
		}

	}

	protected boolean canBurn(@Nullable IRecipe<?> irecipe) {
		if (!this.items.get(0).isEmpty() && irecipe != null) {
			var itemstack = irecipe.getResult();

			if (itemstack.isEmpty()) {
				return false;
			} else {
				var itemstack1 = this.items.get(2);

				return itemstack1.isEmpty() || (itemstack1.doMaterialsMatch(itemstack) && (itemstack1.getCount() < this.getMaxStackSize() && itemstack1.getCount() < itemstack1.getMaxStackSize() || itemstack1.getCount() < itemstack.getMaxStackSize()));
			}
		} else {
			return false;
		}
	}

	private void burn(@Nullable IRecipe<?> irecipe) {
		if (irecipe != null && this.canBurn(irecipe)) {
			var itemstack = this.items.get(0);
			var itemstack1 = irecipe.getResult();
			var itemstack2 = this.items.get(2);

			// CraftBukkit start - fire FurnaceSmeltEvent
			var source = CraftItemStack.asCraftMirror(itemstack);
			var result = CraftItemStack.asBukkitCopy(itemstack1);

			var furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), source, result, (org.bukkit.inventory.CookingRecipe<?>) irecipe.toBukkitRecipe()); // Paper
			this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

			if (furnaceSmeltEvent.isCancelled()) {
				return;
			}

			result = furnaceSmeltEvent.getResult();
			itemstack1 = CraftItemStack.asNMSCopy(result);

			if (!itemstack1.isEmpty()) {
				if (itemstack2.isEmpty()) {
					this.items.set(2, itemstack1.cloneItemStack());
				} else if (CraftItemStack.asCraftMirror(itemstack2).isSimilar(result)) {
					itemstack2.add(itemstack1.getCount());
				} else {
					return;
				}
			}

            /*
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.cloneItemStack());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.add(1);
            }
            */
			// CraftBukkit end

			if (!this.world.isClientSide) {
				this.a(irecipe);
			}

			if (itemstack.getItem() == Blocks.WET_SPONGE.getItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
				this.items.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemstack.subtract(1);
		}
	}

	protected int fuelTime(ItemStack itemstack) {
		if (itemstack.isEmpty()) {
			return 0;
		} else {
			var item = itemstack.getItem();

			return getBurnDurations().getOrDefault(item, 0); // Paper - cache burn durations
		}
	}

	// Paper begin - Expose this function so CraftFurnace can correctly scale the total cooking time to a new multiplier
	public int getRecipeCookingTime() {
		/* Scale the recipe's cooking time to the current cookSpeedMultiplier */
		var cookTime = (this.hasWorld()) ? this.world.getCraftingManager().craft((Recipes<RecipeCooking>) this.c, this, this.world).map(RecipeCooking::getCookingTime).orElse(200) : 200; // CraftBukkit - SPIGOT-4302 // Eclipse fail
		return (int) Math.ceil(cookTime / this.cookSpeedMultiplier);
	}
	// Paper end

	public static boolean isFuel(ItemStack itemstack) {
		return getBurnDurations().containsKey(itemstack.getItem()); // Paper - cache burn durations
	}

	@Override
	public int[] getSlotsForFace(EnumDirection enumdirection) {
		return enumdirection == EnumDirection.DOWN ? TileEntityFurnace.h : (enumdirection == EnumDirection.UP ? TileEntityFurnace.g : TileEntityFurnace.i);
	}

	@Override
	public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
		return this.b(i, itemstack);
	}

	@Override
	public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
		if (enumdirection == EnumDirection.DOWN && i == 1) {
			var item = itemstack.getItem();

			return item == Items.WATER_BUCKET || item == Items.BUCKET;
		}

		return true;
	}

	@Override
	public int getSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		Iterator iterator = this.items.iterator();

		ItemStack itemstack;

		do {
			if (!iterator.hasNext()) {
				return true;
			}

			itemstack = (ItemStack) iterator.next();
		} while (itemstack.isEmpty());

		return false;
	}

	@Override
	public ItemStack getItem(int i) {
		return this.items.get(i);
	}

	@Override
	public ItemStack splitStack(int i, int j) {
		return ContainerUtil.a(this.items, i, j);
	}

	@Override
	public ItemStack splitWithoutUpdate(int i) {
		return ContainerUtil.a(this.items, i);
	}

	@Override
	public void setItem(int i, ItemStack itemstack) {
		var itemstack1 = this.items.get(i);
		var flag = !itemstack.isEmpty() && itemstack.doMaterialsMatch(itemstack1) && ItemStack.equals(itemstack, itemstack1);

		this.items.set(i, itemstack);
		if (itemstack.getCount() > this.getMaxStackSize()) {
			itemstack.setCount(this.getMaxStackSize());
		}

		if (i == 0 && !flag) {
			this.cookTimeTotal = this.getRecipeCookingTime();
			this.cookTime = 0;
			this.update();
		}

	}

	@Override
	public boolean a(EntityHuman entityhuman) {
		return this.world.getTileEntity(this.position) == this && entityhuman.h((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean b(int i, ItemStack itemstack) {
		if (i == 2) {
			return false;
		} else if (i != 1) {
			return true;
		} else {
			var itemstack1 = this.items.get(1);

			return isFuel(itemstack) || itemstack.getItem() == Items.BUCKET && itemstack1.getItem() != Items.BUCKET;
		}
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public void a(@Nullable IRecipe<?> irecipe) {
		if (irecipe != null) {
			var minecraftkey = irecipe.getKey();

			this.n.addTo(minecraftkey, 1);
		}

	}

	@Nullable
	@Override
	public IRecipe<?> ak_() {
		return null;
	}

	@Override
	public void b(EntityHuman entityhuman) {
	}

	public void d(EntityHuman entityhuman, ItemStack itemstack, int amount) { // CraftBukkit
		var list = this.a(entityhuman.world, entityhuman.getPositionVector(), entityhuman, itemstack, amount); // CraftBukkit

		entityhuman.discoverRecipes(list);
		this.n.clear();
	}

	public List<IRecipe<?>> a(World world, Vec3D vec3d) {
		// CraftBukkit start
		return this.a(world, vec3d, null, null, 0);
	}

	public List<IRecipe<?>> a(World world, Vec3D vec3d, EntityHuman entityhuman, ItemStack itemstack, int amount) {
		// CraftBukkit end
		List<IRecipe<?>> list = Lists.newArrayList();

		for (var entry : this.n.object2IntEntrySet()) {
			world.getCraftingManager().getRecipe(entry.getKey()).ifPresent((irecipe) -> {
				if (!(irecipe instanceof RecipeCooking)) return; // Paper - don't process non-cooking recipes
				list.add(irecipe);
				a(world, vec3d, entry.getIntValue(), ((RecipeCooking) irecipe).getExperience(), entityhuman, itemstack, amount); // CraftBukkit
			});
		}

		return list;
	}

	private void a(World world, Vec3D vec3d, int i, float f, EntityHuman entityhuman, ItemStack itemstack, int amount) { // CraftBukkit
		var j = MathHelper.d((float) i * f);
		var f1 = MathHelper.h((float) i * f);

		if (f1 != 0.0F && Math.random() < (double) f1) {
			++j;
		}

		// CraftBukkit start - fire FurnaceExtractEvent
		if (amount != 0) {
			var event = new FurnaceExtractEvent((Player) entityhuman.getBukkitEntity(), CraftBlock.at(world, position), org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(itemstack.getItem()), amount, j);
			world.getServer().getPluginManager().callEvent(event);
			j = event.getExpToDrop();
		}
		// CraftBukkit end

		while (j > 0) {
			var k = EntityExperienceOrb.getOrbValue(j);

			j -= k;
			world.addEntity(new EntityExperienceOrb(world, vec3d.x, vec3d.y, vec3d.z, k, org.bukkit.entity.ExperienceOrb.SpawnReason.FURNACE, entityhuman)); // Paper
		}

	}

	@Override
	public void a(AutoRecipeStackManager autorecipestackmanager) {
		Iterator iterator = this.items.iterator();

		while (iterator.hasNext()) {
			var itemstack = (ItemStack) iterator.next();

			autorecipestackmanager.b(itemstack);
		}

	}
}
