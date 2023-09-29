package net.minecraft.world.entity.item;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.stats.StatisticList;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.util.MathHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityItem extends Entity {

    public static final DataWatcherObject<ItemStack> ITEM = DataWatcher.a(EntityItem.class, DataWatcherRegistry.g);
    public int age;
    public int pickupDelay;
    public int f;
    public UUID thrower;
    public UUID owner;
    public final float b;
    public int lastTick = MinecraftServer.currentTick - 1; // CraftBukkit
    public boolean canMobPickup = true; // Paper
    // Purpur start
    public boolean immuneToCactus = false;
    public boolean immuneToExplosion = false;
    public boolean immuneToFire = false;
    public boolean immuneToLightning = false;
    // Purpur end

    public EntityItem(EntityTypes<? extends EntityItem> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 5;
        this.b = (float) (Math.random() * 3.141592653589793D * 2.0D);
    }

    public EntityItem(World world, double d0, double d1, double d2) {
        this(EntityTypes.ITEM, world);
        this.setPosition(d0, d1, d2);
        this.yaw = this.random.nextFloat() * 360.0F;
        this.setMot(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
    }

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2);
        this.setItemStack(itemstack);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected void initDatawatcher() {
        this.getDataWatcher().register(EntityItem.ITEM, ItemStack.b);
    }

    @Override
    public void tick() {
        if (this.getItemStack().isEmpty()) {
            this.die();
        } else {
            super.tick();
            // CraftBukkit start - Use wall time for pickup and despawn timers
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            if (this.pickupDelay != 32767) this.pickupDelay -= elapsedTicks;
            this.pickupDelay = Math.max(0, this.pickupDelay); // Paper - don't go below 0
            if (this.age != -32768) this.age += elapsedTicks;
            this.lastTick = MinecraftServer.currentTick;
            // CraftBukkit end

            this.lastX = this.locX();
            this.lastY = this.locY();
            this.lastZ = this.locZ();
            Vec3D vec3d = this.getMot();
            float f = this.getHeadHeight() - 0.11111111F;

            if (this.isInWater() && this.b(TagsFluid.WATER) > (double) f) {
                this.u();
            } else if (this.aQ() && this.b(TagsFluid.LAVA) > (double) f) {
                this.v();
            } else if (!this.isNoGravity()) {
                this.setMot(this.getMot().add(0.0D, -0.04D, 0.0D));
            }

            if (this.world.isClientSide) {
                this.noclip = false;
            } else {
                this.noclip = !this.world.getCubes(this);
                if (this.noclip) {
                    this.l(this.locX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.locZ());
                }
            }

            if (!this.onGround || c(this.getMot()) > 9.999999747378752E-6D || this.ticksLived % 4 == 0) { // Paper - Ensure checking item movement is always offset from Spigot's entity activation range check
                this.move(EnumMoveType.SELF, this.getMot());
                float f1 = 0.98F;

                if (this.onGround) {
                    f1 = this.world.getType(new BlockPosition(this.locX(), this.locY() - 1.0D, this.locZ())).getBlock().getFrictionFactor() * 0.98F;
                }

                this.setMot(this.getMot().d(f1, 0.98D, f1));
                if (this.onGround) {
                    Vec3D vec3d1 = this.getMot();

                    if (vec3d1.y < 0.0D) {
                        this.setMot(vec3d1.d(1.0D, -0.5D, 1.0D));
                    }
                }
            }

            boolean flag = MathHelper.floor(this.lastX) != MathHelper.floor(this.locX()) || MathHelper.floor(this.lastY) != MathHelper.floor(this.locY()) || MathHelper.floor(this.lastZ) != MathHelper.floor(this.locZ());
            int i = flag ? 2 : 40;

            if (this.ticksLived % i == 0) {
                if (this.world.getFluid(this.getChunkCoordinates()).a(TagsFluid.LAVA) && !this.isFireProof()) {
                    this.playSound(SoundEffects.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
                }

                if (!this.world.isClientSide && this.z()) {
                    this.mergeNearby();
                }
            }

            /* CraftBukkit start - moved up
            if (this.age != -32768) {
                ++this.age;
            }
            // CraftBukkit end */

            this.impulse |= this.aK();
            if (!this.world.isClientSide) {
                double d0 = this.getMot().d(vec3d).g();

                if (d0 > 0.01D) {
                    this.impulse = true;
                }
            }

            if (!this.world.isClientSide && this.age >= this.getDespawnRate()) { // Spigot // Paper
                // CraftBukkit start - fire ItemDespawnEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                    this.age = 0;
                    return;
                }
                // CraftBukkit end
                this.die();
            }

        }
    }

    // Spigot start - copied from above
    @Override
    public void inactiveTick() {
        // CraftBukkit start - Use wall time for pickup and despawn timers
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        if (this.pickupDelay != 32767) this.pickupDelay -= elapsedTicks;
        this.pickupDelay = Math.max(0, this.pickupDelay); // Paper - don't go below 0
        if (this.age != -32768) this.age += elapsedTicks;
        this.lastTick = MinecraftServer.currentTick;
        // CraftBukkit end

        if (!this.world.isClientSide && this.age >= this.getDespawnRate()) { // Spigot // Paper
            // CraftBukkit start - fire ItemDespawnEvent
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                this.age = 0;
                return;
            }
            // CraftBukkit end
            this.die();
        }
    }
    // Spigot end

    private void u() {
        Vec3D vec3d = this.getMot();

        this.setMot(vec3d.x * 0.9900000095367432D, vec3d.y + (double) (vec3d.y < 0.05999999865889549D ? 5.0E-4F : 0.0F), vec3d.z * 0.9900000095367432D);
    }

    private void v() {
        Vec3D vec3d = this.getMot();

        this.setMot(vec3d.x * 0.949999988079071D, vec3d.y + (double) (vec3d.y < 0.05999999865889549D ? 5.0E-4F : 0.0F), vec3d.z * 0.949999988079071D);
    }

    private void mergeNearby() {
        if (this.z()) {
            // Paper start - avoid item merge if stack size above max stack size
            ItemStack stack = getItemStack();
            if (stack.getCount() >= stack.getMaxStackSize()) return;
            // Paper end
            // Spigot start
            double radius = world.spigotConfig.itemMerge;
            List<EntityItem> list = this.world.a(EntityItem.class, this.getBoundingBox().grow(radius, radius, radius), (entityitem) -> {
                // Spigot end
                return entityitem != this && entityitem.z();
            });
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (entityitem.z()) {
                    // Paper Start - Fix items merging through walls
                        if (this.world.paperConfig.fixItemsMergingThroughWalls) {
                            net.minecraft.world.level.RayTrace rayTrace = new net.minecraft.world.level.RayTrace(this.getPositionVector(), entityitem.getPositionVector(),
                                net.minecraft.world.level.RayTrace.BlockCollisionOption.COLLIDER, net.minecraft.world.level.RayTrace.FluidCollisionOption.NONE, this);
                            net.minecraft.world.phys.MovingObjectPositionBlock rayTraceResult = world.rayTrace(rayTrace);
                            if (rayTraceResult.getType() == net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType.BLOCK) continue;
                        }
                    // Paper End
                    this.a(entityitem);
                    if (this.dead) {
                        break;
                    }
                }
            }

        }
    }

    private boolean z() {
        ItemStack itemstack = this.getItemStack();

        return this.isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && itemstack.getCount() < itemstack.getMaxStackSize();
    }

    private void a(EntityItem entityitem) {
        ItemStack itemstack = this.getItemStack();
        ItemStack itemstack1 = entityitem.getItemStack();

        if (Objects.equals(this.getOwner(), entityitem.getOwner()) && a(itemstack, itemstack1)) {
            if (true || itemstack1.getCount() < itemstack.getCount()) { // Spigot
                a(this, itemstack, entityitem, itemstack1);
            } else {
                a(entityitem, itemstack1, this, itemstack);
            }

        }
    }

    public static boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() != itemstack.getItem() ? false : (itemstack1.getCount() + itemstack.getCount() > itemstack1.getMaxStackSize() ? false : (itemstack1.hasTag() ^ itemstack.hasTag() ? false : !itemstack1.hasTag() || itemstack1.getTag().equals(itemstack.getTag())));
    }

    public static ItemStack a(ItemStack itemstack, ItemStack itemstack1, int i) {
        int j = Math.min(Math.min(itemstack.getMaxStackSize(), i) - itemstack.getCount(), itemstack1.getCount());
        ItemStack itemstack2 = itemstack.cloneItemStack();

        itemstack2.add(j);
        itemstack1.subtract(j);
        return itemstack2;
    }

    private static void a(EntityItem entityitem, ItemStack itemstack, ItemStack itemstack1) {
        ItemStack itemstack2 = a(itemstack, itemstack1, 64);

        if (!itemstack2.isEmpty()) entityitem.setItemStack(itemstack2); // CraftBukkit - don't set empty stacks
    }

    private static void a(EntityItem entityitem, ItemStack itemstack, EntityItem entityitem1, ItemStack itemstack1) {
        if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemMergeEvent(entityitem1, entityitem).isCancelled()) return; // CraftBukkit
        a(entityitem, itemstack, itemstack1);
        entityitem.pickupDelay = Math.max(entityitem.pickupDelay, entityitem1.pickupDelay);
        entityitem.age = Math.min(entityitem.age, entityitem1.age);
        if (itemstack1.isEmpty()) {
            entityitem1.die();
        }

    }

    @Override
    public boolean isFireProof() {
        return this.getItemStack().getItem().u() || super.isFireProof();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (!this.getItemStack().isEmpty() && this.getItemStack().getItem() == Items.NETHER_STAR && damagesource.isExplosion()) {
            return false;
        } else if (!this.getItemStack().getItem().a(damagesource)) {
            return false;
        // Purpur start
        } else if (
                (immuneToCactus && damagesource == DamageSource.CACTUS) ||
                (immuneToFire && (damagesource.isFire() || damagesource == DamageSource.FIRE)) ||
                (immuneToLightning && damagesource == DamageSource.LIGHTNING) ||
                (immuneToExplosion && damagesource.isExplosion())
        ) {
            respawnOnClient();
            return false;
        // Purpur end
        } else {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                return false;
            }
            // CraftBukkit end
            this.velocityChanged();
            this.f = (int) ((float) this.f - f);
            if (this.f <= 0) {
                this.die();
            }

            return false;
        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.f);
        nbttagcompound.setShort("Age", (short) this.age);
        nbttagcompound.setShort("PickupDelay", (short) this.pickupDelay);
        if (this.getThrower() != null) {
            nbttagcompound.a("Thrower", this.getThrower());
        }

        if (this.getOwner() != null) {
            nbttagcompound.a("Owner", this.getOwner());
        }

        if (!this.getItemStack().isEmpty()) {
            nbttagcompound.set("Item", this.getItemStack().save(new NBTTagCompound()));
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.f = nbttagcompound.getShort("Health");
        this.age = nbttagcompound.getShort("Age");
        if (nbttagcompound.hasKey("PickupDelay")) {
            this.pickupDelay = nbttagcompound.getShort("PickupDelay");
        }

        if (nbttagcompound.b("Owner")) {
            this.owner = nbttagcompound.a("Owner");
        }

        if (nbttagcompound.b("Thrower")) {
            this.thrower = nbttagcompound.a("Thrower");
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");

        this.setItemStack(ItemStack.a(nbttagcompound1));
        if (this.getItemStack().isEmpty()) {
            this.die();
        }

    }

    @Override
    public void pickup(EntityHuman entityhuman) {
        if (!this.world.isClientSide) {
            ItemStack itemstack = this.getItemStack();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            // CraftBukkit start - fire PlayerPickupItemEvent
            int canHold = entityhuman.inventory.canHold(itemstack);
            int remaining = i - canHold;
            boolean flyAtPlayer = false; // Paper

            // Paper start
            if (this.pickupDelay <= 0) {
                PlayerAttemptPickupItemEvent attemptEvent = new PlayerAttemptPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                this.world.getServer().getPluginManager().callEvent(attemptEvent);

                flyAtPlayer = attemptEvent.getFlyAtPlayer();
                if (attemptEvent.isCancelled()) {
                    if (flyAtPlayer) {
                        entityhuman.receive(this, i);
                    }

                    return;
                }
            }
            // Paper end

            if (this.pickupDelay <= 0 && canHold > 0) {
                itemstack.setCount(canHold);
                // Call legacy event
                PlayerPickupItemEvent playerEvent = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                playerEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.world.getServer().getPluginManager().callEvent(playerEvent);
                flyAtPlayer = playerEvent.getFlyAtPlayer(); // Paper
                if (playerEvent.isCancelled()) {
                    itemstack.setCount(i); // SPIGOT-5294 - restore count
                    // Paper Start
                    if (flyAtPlayer) {
                        entityhuman.receive(this, i);
                    }
                    // Paper End
                    return;
                }

                // Call newer event afterwards
                EntityPickupItemEvent entityEvent = new EntityPickupItemEvent(entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                entityEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.world.getServer().getPluginManager().callEvent(entityEvent);
                if (entityEvent.isCancelled()) {
                    itemstack.setCount(i); // SPIGOT-5294 - restore count
                    return;
                }

                // Update the ItemStack if it was changed in the event
                ItemStack current = this.getItemStack();
                if (!itemstack.equals(current)) {
                    itemstack = current;
                } else {
                    itemstack.setCount(canHold + remaining); // = i
                }

                // Possibly < 0; fix here so we do not have to modify code below
                this.pickupDelay = 0;
            } else if (this.pickupDelay == 0) {
                // ensure that the code below isn't triggered if canHold says we can't pick the items up
                this.pickupDelay = -1;
            }
            // CraftBukkit end

            if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(entityhuman.getUniqueID())) && entityhuman.inventory.pickup(itemstack)) {
                // Paper Start
                if (flyAtPlayer) {
                    entityhuman.receive(this, i);
                }
                // Paper End
                if (itemstack.isEmpty()) {
                    this.die();
                    itemstack.setCount(i);
                }

                entityhuman.a(StatisticList.ITEM_PICKED_UP.b(item), i);
                entityhuman.a(this);
            }

        }
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        IChatBaseComponent ichatbasecomponent = this.getCustomName();

        return ichatbasecomponent != null ? ichatbasecomponent : new ChatMessage(this.getItemStack().j());
    }

    @Override
    public boolean bL() {
        return false;
    }

    @Nullable
    @Override
    public Entity b(WorldServer worldserver) {
        Entity entity = super.b(worldserver);

        if (!this.world.isClientSide && entity instanceof EntityItem) {
            ((EntityItem) entity).mergeNearby();
        }

        return entity;
    }

    public ItemStack getItemStack() {
        return this.getDataWatcher().get(EntityItem.ITEM);
    }

    public void setItemStack(ItemStack itemstack) {
        com.google.common.base.Preconditions.checkArgument(!itemstack.isEmpty(), "Cannot drop air"); // CraftBukkit
        this.getDataWatcher().set(EntityItem.ITEM, itemstack);
        this.getDataWatcher().markDirty(EntityItem.ITEM); // CraftBukkit - SPIGOT-4591, must mark dirty
        // Purpur start
        if (world.purpurConfig.itemImmuneToCactus.contains(itemstack.getItem())) immuneToCactus = true;
        if (world.purpurConfig.itemImmuneToExplosion.contains(itemstack.getItem())) immuneToExplosion = true;
        if (world.purpurConfig.itemImmuneToFire.contains(itemstack.getItem())) immuneToFire = true;
        if (world.purpurConfig.itemImmuneToLightning.contains(itemstack.getItem())) immuneToLightning = true;
        // Purpur end
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
        if (EntityItem.ITEM.equals(datawatcherobject)) {
            this.getItemStack().a(this);
        }

    }

    @Nullable
    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable UUID uuid) {
        this.owner = uuid;
    }

    @Nullable
    public UUID getThrower() {
        return this.thrower;
    }

    public void setThrower(@Nullable UUID uuid) {
        this.thrower = uuid;
    }

    public void defaultPickupDelay() {
        this.pickupDelay = 10;
    }

    public void n() {
        this.pickupDelay = 0;
    }

    public void o() {
        this.pickupDelay = 32767;
    }

    public void setPickupDelay(int i) {
        this.pickupDelay = i;
    }

    public boolean p() {
        return this.pickupDelay > 0;
    }

    public void r() {
        this.age = -6000;
    }

    public void s() {
        this.o();
        this.age = this.getDespawnRate() - 1; // Spigot // Paper
    }

    // Paper start
    public int getDespawnRate(){
        Material material = this.getItemStack().getBukkitStack().getType();
        return world.paperConfig.altItemDespawnRateMap.getOrDefault(material, world.spigotConfig.itemDespawnRate);
    }
    // Paper end

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }

    // Paper start - fix MC-4
    public void setPositionRaw(double x, double y, double z) {
        if (com.destroystokyo.paper.PaperConfig.fixEntityPositionDesync) { // Tuinity - revert // Purpur - no
            // encode/decode from PacketPlayOutEntity
            x = MathHelper.floorLong(x * 4096.0D) * (1 / 4096.0D);
            y = MathHelper.floorLong(y * 4096.0D) * (1 / 4096.0D);
            z = MathHelper.floorLong(z * 4096.0D) * (1 / 4096.0D);
        }
        super.setPositionRaw(x, y, z);
    }
    // Paper end - fix MC-4

    // Purpur start
    public void respawnOnClient() {
        Packet<?> spawnPacket = new PacketPlayOutSpawnEntity(this);
        Packet<?> metadataPacket = new net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata(getId(), getDataWatcher(), true);
        for (net.minecraft.server.level.EntityPlayer entityplayer : this.tracker.trackerEntry.trackedPlayers) {
            entityplayer.playerConnection.sendPacket(spawnPacket);
            entityplayer.playerConnection.sendPacket(metadataPacket);
        }
    }
    // Purpur end
}
