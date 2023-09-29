package net.minecraft.world.entity;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.protocol.game.PacketDebug;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.tags.Tag;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.control.ControllerJump;
import net.minecraft.world.entity.ai.control.ControllerLook;
import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.entity.ai.control.EntityAIBodyControl;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.ai.sensing.EntitySenses;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.decoration.EntityLeash;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.IMonster;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.vehicle.EntityBoat;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BlockSkullAbstract;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import javax.annotation.Nullable;
import java.util.*;
// CraftBukkit end

public abstract class EntityInsentient extends EntityLiving {

    private static final DataWatcherObject<Byte> b = DataWatcher.a(EntityInsentient.class, DataWatcherRegistry.a);
    public int e;public void setAmbientSoundTime(int time) { this.e = time; } // Paper - OBFHELPER
    protected int f;
    protected ControllerLook lookController;
    protected ControllerMove moveController;
    protected ControllerJump bi; public ControllerJump getJumpController() { return bi; } // Purpur - OBFHELPER
    private final EntityAIBodyControl c;
    protected NavigationAbstract navigation;
    public PathfinderGoalSelector goalSelector;
    @Nullable public PathfinderGoalFloat goalFloat; // Paper
    public PathfinderGoalSelector targetSelector;
    private EntityLiving goalTarget;
    private final EntitySenses bo;
    private final NonNullList<ItemStack> bp; public List<ItemStack> getHandEquipment() { return this.bp; } // Purpur - OBFHELPER
    public final float[] dropChanceHand;
    private final NonNullList<ItemStack> bq; public List<ItemStack> getArmorEquipment() { return this.bq; } // Purpur - OBFHELPER
    public final float[] dropChanceArmor;
    // private boolean canPickUpLoot; // CraftBukkit - moved up to EntityLiving
    public boolean persistent;
    private final Map<PathType, Float> bt;
    public MinecraftKey lootTableKey;
    public long lootTableSeed;
    @Nullable
    public Entity leashHolder; // Paper - private -> public
    private int bx;
    @Nullable
    private NBTTagCompound by;
    private BlockPosition bz;
    private float bA;
    public int ticksSinceLastInteraction; // Purpur
    public boolean aware = true; // CraftBukkit

    protected EntityInsentient(EntityTypes<? extends EntityInsentient> entitytypes, World world) {
        super(entitytypes, world);
        this.bp = NonNullList.a(2, ItemStack.b);
        this.dropChanceHand = new float[2];
        this.bq = NonNullList.a(4, ItemStack.b);
        this.dropChanceArmor = new float[4];
        this.bt = Maps.newEnumMap(PathType.class);
        this.bz = BlockPosition.ZERO;
        this.bA = -1.0F;
        this.goalSelector = new PathfinderGoalSelector();
        this.targetSelector = new PathfinderGoalSelector();
        this.lookController = new net.pl3x.purpur.controller.ControllerLookWASD(this); // Purpur
        this.moveController = new net.pl3x.purpur.controller.ControllerMoveWASD(this); // Purpur
        this.bi = new ControllerJump(this);
        this.c = this.r();
        this.navigation = this.b(world);
        this.bo = new EntitySenses(this);
        Arrays.fill(this.dropChanceArmor, 0.085F);
        Arrays.fill(this.dropChanceHand, 0.085F);
        if (world != null && !world.isClientSide) {
            this.initPathfinder();
        }

        // CraftBukkit start - default persistance to type's persistance value
        this.persistent = !isTypeNotPersistent(0);
        // CraftBukkit end
    }

    protected void initPathfinder() {}

    public static AttributeProvider.Builder p() {
        return EntityLiving.cL().a(GenericAttributes.FOLLOW_RANGE, 16.0D).a(GenericAttributes.ATTACK_KNOCKBACK);
    }

    protected NavigationAbstract b(World world) {
        return new Navigation(this, world);
    }

    protected boolean q() {
        return false;
    }

    public float a(PathType pathtype) {
        EntityInsentient entityinsentient;

        if (this.getVehicle() instanceof EntityInsentient && ((EntityInsentient) this.getVehicle()).q()) {
            entityinsentient = (EntityInsentient) this.getVehicle();
        } else {
            entityinsentient = this;
        }

        Float ofloat = entityinsentient.bt.get(pathtype);

        return ofloat == null ? pathtype.a() : ofloat;
    }

    public void a(PathType pathtype, float f) {
        this.bt.put(pathtype, f);
    }

    public boolean b(PathType pathtype) {
        return pathtype != PathType.DANGER_FIRE && pathtype != PathType.DANGER_CACTUS && pathtype != PathType.DANGER_OTHER && pathtype != PathType.WALKABLE_DOOR;
    }

    protected EntityAIBodyControl r() {
        return new EntityAIBodyControl(this);
    }

    public ControllerLook getControllerLook() {
        return this.lookController;
    }

    // Paper start
    @Override
    public void inactiveTick() {
        super.inactiveTick();
        if (this.goalSelector.inactiveTick()) {
            this.goalSelector.doTick();
        }
        if (this.targetSelector.inactiveTick()) {
            this.targetSelector.doTick();
        }
    }
    // Paper end

    public ControllerMove getControllerMove() {
        if (this.isPassenger() && this.getVehicle() instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) this.getVehicle();

            return entityinsentient.getControllerMove();
        } else {
            return this.moveController;
        }
    }

    public ControllerJump getControllerJump() {
        return this.bi;
    }

    public NavigationAbstract getNavigation() {
        if (this.isPassenger() && this.getVehicle() instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) this.getVehicle();

            return entityinsentient.getNavigation();
        } else {
            return this.navigation;
        }
    }

    public EntitySenses getEntitySenses() {
        return this.bo;
    }

    @Nullable
    public EntityLiving getGoalTarget() {
        return this.goalTarget;
    }

    public org.bukkit.craftbukkit.entity.CraftMob getBukkitMob() { return (org.bukkit.craftbukkit.entity.CraftMob) super.getBukkitEntity(); } // Paper
    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        // CraftBukkit start - fire event
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (getGoalTarget() == entityliving) return false;
        if (fireEvent) {
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN && getGoalTarget() != null && entityliving == null) {
                reason = getGoalTarget().isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
            }
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN) {
                world.getServer().getLogger().log(java.util.logging.Level.WARNING, "Unknown target reason, please report on the issue tracker", new Exception());
            }
            CraftLivingEntity ctarget = null;
            if (entityliving != null) {
                ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
            }
            EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(this.getBukkitEntity(), ctarget, reason);
            world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }

            if (event.getTarget() != null) {
                entityliving = ((CraftLivingEntity) event.getTarget()).getHandle();
            } else {
                entityliving = null;
            }
        }
        if (entityliving instanceof EntityPlayer) this.ticksSinceLastInteraction = 0; // Purpur
        this.goalTarget = entityliving;
        return true;
        // CraftBukkit end
    }

    @Override
    public boolean a(EntityTypes<?> entitytypes) {
        return entitytypes != EntityTypes.GHAST;
    }

    public boolean a(ItemProjectileWeapon itemprojectileweapon) {
        return false;
    }

    public void blockEaten() {}

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityInsentient.b, (byte) 0);
    }

    public int getAmbientSoundInterval() { return D(); } // Paper - OBFHELPER
    public int D() {
        return 80;
    }

    public void F() {
        SoundEffect soundeffect = this.getSoundAmbient();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.dH());
        }

    }

    @Override
    public void entityBaseTick() {
        super.entityBaseTick();
        if (this.isAlive() && this.random.nextInt(1000) < this.e++) {
            this.m();
            this.F();
        }
        incrementTicksSinceLastInteraction(); // Purpur
    }

    // Purpur start
    private void incrementTicksSinceLastInteraction() {
        ++ticksSinceLastInteraction;
        if (hasPurpurRider()) {
            ticksSinceLastInteraction = 0;
            return;
        }
        if (world.purpurConfig.entityLifeSpan <= 0) {
            return; // feature disabled
        }
        if (!isTypeNotPersistent(0) || isPersistent() || isSpecialPersistence() || hasCustomName()) {
            return; // mob persistent
        }
        if (ticksSinceLastInteraction > world.purpurConfig.entityLifeSpan) {
            this.dead = true;
        }
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource.getEntity() instanceof EntityPlayer) this.ticksSinceLastInteraction = 0; // Purpur
        return super.damageEntity(damagesource, f);
    }
    // Purpur end

    @Override
    protected void c(DamageSource damagesource) {
        this.m();
        super.c(damagesource);
    }

    private void m() {
        this.e = -this.D();
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        if (this.f > 0) {
            int i = this.f;

            int j;

            for (j = 0; j < this.bq.size(); ++j) {
                if (!this.bq.get(j).isEmpty() && this.dropChanceArmor[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            for (j = 0; j < this.bp.size(); ++j) {
                if (!this.bp.get(j).isEmpty() && this.dropChanceHand[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            return i;
        } else {
            return this.f;
        }
    }

    public void doSpawnEffect() {
        if (this.world.isClientSide) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                double d3 = 10.0D;

                this.world.addParticle(Particles.POOF, this.c(1.0D) - d0 * 10.0D, this.cF() - d1 * 10.0D, this.g(1.0D) - d2 * 10.0D, d0, d1, d2);
            }
        } else {
            this.world.broadcastEntityEffect(this, (byte) 20);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClientSide) {
            this.eA();
            if (this.ticksLived % 5 == 0) {
                this.H();
            }
        }

    }

    protected void H() {
        boolean flag = !(this.getRidingPassenger() instanceof EntityInsentient);
        boolean flag1 = !(this.getVehicle() instanceof EntityBoat);

        this.goalSelector.a(PathfinderGoal.Type.MOVE, flag);
        this.goalSelector.a(PathfinderGoal.Type.JUMP, flag && flag1);
        this.goalSelector.a(PathfinderGoal.Type.LOOK, flag);
    }

    @Override
    protected float f(float f, float f1) {
        this.c.a();
        return f1;
    }

    @Nullable
    protected SoundEffect getSoundAmbient() {
        return null;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("CanPickUpLoot", this.canPickupLoot());
        nbttagcompound.setBoolean("PersistenceRequired", this.persistent);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.bq.iterator(); iterator.hasNext(); nbttaglist.add(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.save(nbttagcompound1);
            }
        }

        nbttagcompound.set("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.bp.iterator(); iterator1.hasNext(); nbttaglist1.add(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.save(nbttagcompound2);
            }
        }

        nbttagcompound.set("HandItems", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        float[] afloat = this.dropChanceArmor;
        int i = afloat.length;

        int j;

        for (j = 0; j < i; ++j) {
            float f = afloat[j];

            nbttaglist2.add(NBTTagFloat.a(f));
        }

        nbttagcompound.set("ArmorDropChances", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        float[] afloat1 = this.dropChanceHand;

        j = afloat1.length;

        for (int k = 0; k < j; ++k) {
            float f1 = afloat1[k];

            nbttaglist3.add(NBTTagFloat.a(f1));
        }

        nbttagcompound.set("HandDropChances", nbttaglist3);
        if (this.leashHolder != null) {
            nbttagcompound2 = new NBTTagCompound();
            if (this.leashHolder instanceof EntityLiving) {
                UUID uuid = this.leashHolder.getUniqueID();

                nbttagcompound2.a("UUID", uuid);
            } else if (this.leashHolder instanceof EntityHanging) {
                BlockPosition blockposition = ((EntityHanging) this.leashHolder).getBlockPosition();

                nbttagcompound2.setInt("X", blockposition.getX());
                nbttagcompound2.setInt("Y", blockposition.getY());
                nbttagcompound2.setInt("Z", blockposition.getZ());
            }

            nbttagcompound.set("Leash", nbttagcompound2);
        } else if (this.by != null) {
            nbttagcompound.set("Leash", this.by.clone());
        }

        nbttagcompound.setBoolean("LeftHanded", this.isLeftHanded());
        if (this.lootTableKey != null) {
            nbttagcompound.setString("DeathLootTable", this.lootTableKey.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("DeathLootTableSeed", this.lootTableSeed);
            }
        }

        if (this.isNoAI()) {
            nbttagcompound.setBoolean("NoAI", this.isNoAI());
        }

        nbttagcompound.setBoolean("Bukkit.Aware", this.aware); // CraftBukkit
        nbttagcompound.setInt("Purpur.ticksSinceLastInteraction", ticksSinceLastInteraction); // Purpur
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);

        // CraftBukkit start - If looting or persistence is false only use it if it was set after we started using it
        if (nbttagcompound.hasKeyOfType("CanPickUpLoot", 1)) {
            boolean data = nbttagcompound.getBoolean("CanPickUpLoot");
            if (isLevelAtLeast(nbttagcompound, 1) || data) {
                this.setCanPickupLoot(data);
            }
        }

        boolean data = nbttagcompound.getBoolean("PersistenceRequired");
        if (isLevelAtLeast(nbttagcompound, 1) || data) {
            this.persistent = data;
        }
        // CraftBukkit end
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKeyOfType("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.getList("ArmorItems", 10);

            for (i = 0; i < this.bq.size(); ++i) {
                this.bq.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("HandItems", 9)) {
            nbttaglist = nbttagcompound.getList("HandItems", 10);

            for (i = 0; i < this.bp.size(); ++i) {
                this.bp.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.getList("ArmorDropChances", 5);

            for (i = 0; i < nbttaglist.size(); ++i) {
                this.dropChanceArmor[i] = nbttaglist.i(i);
            }
        }

        if (nbttagcompound.hasKeyOfType("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.getList("HandDropChances", 5);

            for (i = 0; i < nbttaglist.size(); ++i) {
                this.dropChanceHand[i] = nbttaglist.i(i);
            }
        }

        if (nbttagcompound.hasKeyOfType("Leash", 10)) {
            this.by = nbttagcompound.getCompound("Leash");
        }

        this.setLeftHanded(nbttagcompound.getBoolean("LeftHanded"));
        if (nbttagcompound.hasKeyOfType("DeathLootTable", 8)) {
            this.lootTableKey = new MinecraftKey(nbttagcompound.getString("DeathLootTable"));
            this.lootTableSeed = nbttagcompound.getLong("DeathLootTableSeed");
        }

        this.setNoAI(nbttagcompound.getBoolean("NoAI"));
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.Aware")) {
            this.aware = nbttagcompound.getBoolean("Bukkit.Aware");
        }
        // CraftBukkit end
        // Purpur start
        if (nbttagcompound.hasKey("Purpur.ticksSinceLastInteraction")) {
            ticksSinceLastInteraction = nbttagcompound.getInt("Purpur.ticksSinceLastInteraction");
        }
        // Purpur end
    }

    @Override
    protected void a(DamageSource damagesource, boolean flag) {
        super.a(damagesource, flag);
        this.lootTableKey = null;
    }
    // CraftBukkit - start
    public MinecraftKey getLootTable() {
        return getDefaultLootTable();
    }
    // CraftBukkit - end

    @Override
    protected LootTableInfo.Builder a(boolean flag, DamageSource damagesource) {
        return super.a(flag, damagesource).a(this.lootTableSeed, this.random);
    }

    @Override
    public final MinecraftKey dp() {
        return this.lootTableKey == null ? this.getDefaultLootTable() : this.lootTableKey;
    }

    protected MinecraftKey getDefaultLootTable() {
        return super.dp();
    }

    public void setForwardSpeed(float speed) { this.t(speed); } // Purpur - OBFHELPER
    public void t(float f) {
        this.aT = f;
    }

    public void setVerticalSpeed(float speed) { this.u(speed); } // Purpur - OBFHELPER
    public void u(float f) {
        this.aS = f;
    }

    public void setStrafeSpeed(float speed) { this.v(speed); } // Purpur - OBFHELPER
    public void v(float f) {
        this.aR = f;
    }

    @Override
    public void q(float f) {
        super.q(f);
        this.t(f);
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (!this.world.isClientSide && this.canPickupLoot() && this.isAlive() && !this.killed && (this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) || world.purpurConfig.entitiesPickUpLootBypassMobGriefing)) { // Purpur
            List<EntityItem> list = this.world.a(EntityItem.class, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (!entityitem.dead && !entityitem.getItemStack().isEmpty() && !entityitem.p() && this.i(entityitem.getItemStack())) {
                    // Paper Start
                    if (!entityitem.canMobPickup) {
                        continue;
                    }
                    // Paper End
                    this.b(entityitem);
                }
            }
        }
    }

    protected void b(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();

        if (this.g(itemstack, entityitem)) { // CraftBukkit - add item
            this.a(entityitem);
            this.receive(entityitem, itemstack.getCount());
            entityitem.die();
        }

    }

    public boolean g(ItemStack itemstack) {
        // CraftBukkit start - add item
        return this.g(itemstack, null);
    }

    public boolean g(ItemStack itemstack, EntityItem entityitem) {
        // CraftBukkit end
        EnumItemSlot enumitemslot = j(itemstack);
        ItemStack itemstack1 = this.getEquipment(enumitemslot);
        boolean flag = this.a(itemstack, itemstack1);

        // CraftBukkit start
        boolean canPickup = flag && this.canPickup(itemstack);
        if (entityitem != null) {
            canPickup = !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityPickupItemEvent(this, entityitem, 0, !canPickup).isCancelled();
        }
        if (canPickup) {
            // CraftBukkit end
            double d0 = this.e(enumitemslot);

            if (!itemstack1.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
                this.forceDrops = true; // CraftBukkit
                this.a(itemstack1);
                this.forceDrops = false; // CraftBukkit
            }

            this.b(enumitemslot, itemstack);
            this.playEquipSound(itemstack);
            return true;
        } else {
            return false;
        }
    }

    protected void b(EnumItemSlot enumitemslot, ItemStack itemstack) {
        this.setSlot(enumitemslot, itemstack);
        this.d(enumitemslot);
        this.persistent = true;
    }

    public void d(EnumItemSlot enumitemslot) {
        switch (enumitemslot.a()) {
            case HAND:
                this.dropChanceHand[enumitemslot.b()] = 2.0F;
                break;
            case ARMOR:
                this.dropChanceArmor[enumitemslot.b()] = 2.0F;
        }

    }

    protected boolean a(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack1.isEmpty()) {
            return true;
        } else if (itemstack.getItem() instanceof ItemSword) {
            if (!(itemstack1.getItem() instanceof ItemSword)) {
                return true;
            } else {
                ItemSword itemsword = (ItemSword) itemstack.getItem();
                ItemSword itemsword1 = (ItemSword) itemstack1.getItem();

                return itemsword.f() != itemsword1.f() ? itemsword.f() > itemsword1.f() : this.b(itemstack, itemstack1);
            }
        } else if (itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow) {
            return this.b(itemstack, itemstack1);
        } else if (itemstack.getItem() instanceof ItemCrossbow && itemstack1.getItem() instanceof ItemCrossbow) {
            return this.b(itemstack, itemstack1);
        } else if (itemstack.getItem() instanceof ItemArmor) {
            if (EnchantmentManager.d(itemstack1)) {
                return false;
            } else if (!(itemstack1.getItem() instanceof ItemArmor)) {
                return true;
            } else {
                ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
                ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();

                return itemarmor.e() != itemarmor1.e() ? itemarmor.e() > itemarmor1.e() : (itemarmor.f() != itemarmor1.f() ? itemarmor.f() > itemarmor1.f() : this.b(itemstack, itemstack1));
            }
        } else {
            if (itemstack.getItem() instanceof ItemTool) {
                if (itemstack1.getItem() instanceof ItemBlock) {
                    return true;
                }

                if (itemstack1.getItem() instanceof ItemTool) {
                    ItemTool itemtool = (ItemTool) itemstack.getItem();
                    ItemTool itemtool1 = (ItemTool) itemstack1.getItem();

                    if (itemtool.d() != itemtool1.d()) {
                        return itemtool.d() > itemtool1.d();
                    }

                    return this.b(itemstack, itemstack1);
                }
            }

            return false;
        }
    }

    public boolean b(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getDamage() >= itemstack1.getDamage() && (!itemstack.hasTag() || itemstack1.hasTag()) ? (itemstack.hasTag() && itemstack1.hasTag() ? itemstack.getTag().getKeys().stream().anyMatch((s) -> {
            return !s.equals("Damage");
        }) && !itemstack1.getTag().getKeys().stream().anyMatch((s) -> {
            return !s.equals("Damage");
        }) : false) : true;
    }

    public boolean canPickup(ItemStack itemstack) {
        return true;
    }

    public boolean i(ItemStack itemstack) {
        return this.canPickup(itemstack);
    }

    public boolean isTypeNotPersistent(double d0) {
        return true;
    }

    public boolean isSpecialPersistence() {
        return this.isPassenger();
    }

    protected boolean L() {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.L()) {
            this.die();
        } else if (!this.isPersistent() && !this.isSpecialPersistence()) {
            // Tuinity start - optimise checkDespawn
            net.minecraft.world.level.chunk.Chunk chunk = this.getCurrentChunk();
            EntityHuman entityhuman = chunk == null || this.world.paperConfig.hardDespawnDistance >= (net.minecraft.server.level.PlayerChunkMap.GENERAL_AREA_MAP_ACCEPTABLE_SEARCH_RANGE_SQUARED) ? this.world.findNearbyPlayer(this, -1.0D, IEntitySelector.affectsSpawning) : chunk.findNearestPlayer(this.locX(), this.locY(), this.locZ(), -1.0, IEntitySelector.affectsSpawning); // Paper
            if (entityhuman == null) {
                entityhuman = ((WorldServer)this.world).playersAffectingSpawning.isEmpty() ? null : ((WorldServer)this.world).playersAffectingSpawning.get(0);
            }
            // Tuinity end - optimise checkDespawn

            if (entityhuman != null) {
                double d0 = entityhuman.h(this); // CraftBukkit - decompile error
                int i = this.getEntityType().e().f();
                int j = i * i;

                if (d0 > (double) world.paperConfig.hardDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.die();
                }

                int k = this.getEntityType().e().g();
                int l = k * k;

                if (this.ticksFarFromPlayer > 600 && this.random.nextInt(800) == 0 && d0 > world.paperConfig.softDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.die();
                } else if (d0 < world.paperConfig.softDespawnDistance) { // Paper - custom despawn distances
                    this.ticksFarFromPlayer = 0;
                }
            }

        } else {
            this.ticksFarFromPlayer = 0;
        }
    }

    @Override
    protected final void doTick() {
        ++this.ticksFarFromPlayer;
        if (!this.aware) { // Paper start - Allow nerfed mobs to jump, float and take water damage
            if (goalFloat != null) {
                if (goalFloat.validConditions()) goalFloat.update();
                this.getControllerJump().jumpIfSet();
            }

            if (isSensitiveToWater() && isInWaterOrRainOrBubble()) { // Purpur - Toggle for water sensitive mob damage
                damageEntity(DamageSource.DROWN, 1.0F);
            }
            return;
        }
        // Paper end
        this.bo.a();
        this.targetSelector.doTick();
        this.goalSelector.doTick();
        this.navigation.c();
        this.mobTick();
        this.moveController.a();
        this.lookController.a();
        this.bi.b();
        this.M();
    }

    protected void M() {
        PacketDebug.a(this.world, this, this.goalSelector);
    }

    protected void mobTick() {}

    public int getMaxHeadXRot() { return O(); } // Paper - OBFHELPER
    public int O() {
        return 40;
    }

    public int getMaxHeadYRot() { return Q(); } // Paper - OBFHELPER
    public int Q() {
        return 75;
    }

    public int getHeadRotSpeed() { return ep(); } // Paper - OBFHELPER
    public int ep() {
        return 10;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX() - this.locX();
        double d1 = entity.locZ() - this.locZ();
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = entityliving.getHeadY() - this.getHeadY();
        } else {
            d2 = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D - this.getHeadY();
        }

        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (MathHelper.d(d1, d0) * 57.2957763671875D) - 90.0F;
        float f3 = (float) (-(MathHelper.d(d2, d3) * 57.2957763671875D));

        this.pitch = this.a(this.pitch, f3, f1);
        this.yaw = this.a(this.yaw, f2, f);
    }

    private float a(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public static boolean a(EntityTypes<? extends EntityInsentient> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        BlockPosition blockposition1 = blockposition.down();

        return enummobspawn == EnumMobSpawn.SPAWNER || generatoraccess.getType(blockposition1).a(generatoraccess, blockposition1, entitytypes);
    }

    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        return true;
    }

    public boolean a(IWorldReader iworldreader) {
        return !iworldreader.containsLiquid(this.getBoundingBox()) && iworldreader.j(this);
    }

    public int getMaxSpawnGroup() {
        return 4;
    }

    public boolean c(int i) {
        return false;
    }

    @Override
    public int bP() {
        if (this.getGoalTarget() == null) {
            return 3;
        } else {
            int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);

            i -= (3 - this.world.getDifficulty().a()) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    @Override
    public Iterable<ItemStack> bn() {
        return this.bp;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.bq;
    }

    @Override
    public ItemStack getEquipment(EnumItemSlot enumitemslot) {
        switch (enumitemslot.a()) {
            case HAND:
                return this.bp.get(enumitemslot.b());
            case ARMOR:
                return this.bq.get(enumitemslot.b());
            default:
                return ItemStack.b;
        }
    }

    // Purpur start
    public List<List<ItemStack>> cloneEquipment() {
        List<List<ItemStack>> list = new ObjectArrayList<>();
        List<ItemStack> handItems = new ObjectArrayList<>();
        for (ItemStack item : this.getHandEquipment()) {
            handItems.add(item.cloneItemStack());
        }
        list.add(handItems);
        List<ItemStack> armorItems = new ObjectArrayList<>();
        for (ItemStack item : this.getArmorEquipment()) {
            armorItems.add(item.cloneItemStack());
        }
        list.add(armorItems);
        return list;
    }

    public void restoreEquipment(List<List<ItemStack>> list) {
        this.getHandEquipment().clear();
        List<ItemStack> handItems = list.get(0);
        for (int i = 0; i < handItems.size(); i++) {
            this.getHandEquipment().set(i, handItems.get(1));
        }
        this.getArmorEquipment().clear();
        List<ItemStack> armorItems = list.get(1);
        for (int i = 0; i < armorItems.size(); i++) {
            this.getArmorEquipment().set(i, armorItems.get(i));
        }
    }

    public void clearEquipment() {
        this.getHandEquipment().clear();
        this.getArmorEquipment().clear();
    }
    // Purpur end

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.a()) {
            case HAND:
                this.bp.set(enumitemslot.b(), itemstack);
                break;
            case ARMOR:
                this.bq.set(enumitemslot.b(), itemstack);
        }

    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropDeathLoot(damagesource, i, flag);
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int j = aenumitemslot.length;

        for (int k = 0; k < j; ++k) {
            EnumItemSlot enumitemslot = aenumitemslot[k];
            ItemStack itemstack = this.getEquipment(enumitemslot);
            float f = this.e(enumitemslot);
            boolean flag1 = f > 1.0F;

            if (!itemstack.isEmpty() && !EnchantmentManager.shouldNotDrop(itemstack) && (flag || flag1) && Math.max(this.random.nextFloat() - (float) i * 0.01F, 0.0F) < f) {
                if (!flag1 && itemstack.e()) {
                    itemstack.setDamage(itemstack.h() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.h() - 3, 1))));
                }

                this.a(itemstack);
                this.setSlot(enumitemslot, ItemStack.b);
            }
        }

    }

    protected float e(EnumItemSlot enumitemslot) {
        float f;

        switch (enumitemslot.a()) {
            case HAND:
                f = this.dropChanceHand[enumitemslot.b()];
                break;
            case ARMOR:
                f = this.dropChanceArmor[enumitemslot.b()];
                break;
            default:
                f = 0.0F;
        }

        return f;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyDamageScaler difficultydamagescaler) { a(difficultydamagescaler); } // Purpur - OBFHELPER
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        if (this.random.nextFloat() < 0.15F * difficultydamagescaler.d()) {
            int i = this.random.nextInt(2);
            float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            boolean flag = true;
            EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EnumItemSlot enumitemslot = aenumitemslot[k];

                if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
                    ItemStack itemstack = this.getEquipment(enumitemslot);

                    if (!flag && this.random.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.isEmpty()) {
                        Item item = a(enumitemslot, i);

                        if (item != null) {
                            this.setSlot(enumitemslot, new ItemStack(item));
                        }
                    }
                }
            }
        }

    }

    // Purpur start
    public static EnumItemSlot getSlotForDispenser(ItemStack itemstack) {
        return EnchantmentManager.getEnchantmentLevel(Enchantments.BINDING_CURSE, itemstack) > 0 ? EnumItemSlot.MAINHAND : getSlotForItemStack(itemstack);
    }
    // Purpur end

    public static EnumItemSlot getSlotForItemStack(ItemStack itemstack) { return j(itemstack); } // Purpur - OBFHELPER
    public static EnumItemSlot j(ItemStack itemstack) {
        Item item = itemstack.getItem();

        return item != Blocks.CARVED_PUMPKIN.getItem() && (!(item instanceof ItemBlock) || !(((ItemBlock) item).getBlock() instanceof BlockSkullAbstract)) ? (item instanceof ItemArmor ? ((ItemArmor) item).b() : (item == Items.ELYTRA ? EnumItemSlot.CHEST : (item == Items.SHIELD ? EnumItemSlot.OFFHAND : EnumItemSlot.MAINHAND))) : EnumItemSlot.HEAD;
    }

    @Nullable
    public static Item a(EnumItemSlot enumitemslot, int i) {
        switch (enumitemslot) {
            case HEAD:
                if (i == 0) {
                    return Items.LEATHER_HELMET;
                } else if (i == 1) {
                    return Items.GOLDEN_HELMET;
                } else if (i == 2) {
                    return Items.CHAINMAIL_HELMET;
                } else if (i == 3) {
                    return Items.IRON_HELMET;
                } else if (i == 4) {
                    return Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (i == 0) {
                    return Items.LEATHER_CHESTPLATE;
                } else if (i == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                } else if (i == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (i == 3) {
                    return Items.IRON_CHESTPLATE;
                } else if (i == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (i == 0) {
                    return Items.LEATHER_LEGGINGS;
                } else if (i == 1) {
                    return Items.GOLDEN_LEGGINGS;
                } else if (i == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (i == 3) {
                    return Items.IRON_LEGGINGS;
                } else if (i == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (i == 0) {
                    return Items.LEATHER_BOOTS;
                } else if (i == 1) {
                    return Items.GOLDEN_BOOTS;
                } else if (i == 2) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (i == 3) {
                    return Items.IRON_BOOTS;
                } else if (i == 4) {
                    return Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }

    protected void setEnchantmentBasedOnDifficulty(DifficultyDamageScaler difficultydamagescaler) { b(difficultydamagescaler); } // Purpur - OBFHELPER
    protected void b(DifficultyDamageScaler difficultydamagescaler) {
        float f = difficultydamagescaler.d();

        this.w(f);
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
                this.a(f, enumitemslot);
            }
        }

    }

    protected void w(float f) {
        if (!this.getItemInMainHand().isEmpty() && this.random.nextFloat() < 0.25F * f) {
            this.setSlot(EnumItemSlot.MAINHAND, EnchantmentManager.a(this.random, this.getItemInMainHand(), (int) (5.0F + f * (float) this.random.nextInt(18)), false));
        }

    }

    protected void a(float f, EnumItemSlot enumitemslot) {
        ItemStack itemstack = this.getEquipment(enumitemslot);

        if (!itemstack.isEmpty() && this.random.nextFloat() < 0.5F * f) {
            this.setSlot(enumitemslot, EnchantmentManager.a(this.random, itemstack, (int) (5.0F + f * (float) this.random.nextInt(18)), false));
        }

    }

    @Nullable
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).addModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        if (this.random.nextFloat() < this.world.purpurConfig.entityLeftHandedChance) { // Purpur
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }

        return groupdataentity;
    }

    public boolean er() {
        return false;
    }

    public void setPersistent() {
        this.persistent = true;
    }

    public void a(EnumItemSlot enumitemslot, float f) {
        switch (enumitemslot.a()) {
            case HAND:
                this.dropChanceHand[enumitemslot.b()] = f;
                break;
            case ARMOR:
                this.dropChanceArmor[enumitemslot.b()] = f;
        }

    }

    public boolean canPickupLoot() {
        return this.canPickUpLoot;
    }

    public void setCanPickupLoot(boolean flag) {
        this.canPickUpLoot = flag;
    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = j(itemstack);

        return this.getEquipment(enumitemslot).isEmpty() && this.canPickupLoot();
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    @Override
    public final EnumInteractionResult a(EntityHuman entityhuman, EnumHand enumhand) {
        if (!this.isAlive()) {
            return EnumInteractionResult.PASS;
        } else if (this.getLeashHolder() == entityhuman) {
            if (enumhand == EnumHand.OFF_HAND && (world.purpurConfig.villagerCanBeLeashed || world.purpurConfig.villagerTraderCanBeLeashed) && this instanceof EntityVillagerAbstract) return EnumInteractionResult.CONSUME; // Purpur
            // CraftBukkit start - fire PlayerUnleashEntityEvent
            // Paper start - drop leash variable
            PlayerUnleashEntityEvent event = CraftEventFactory.callPlayerUnleashEntityEvent(this, entityhuman, !entityhuman.abilities.canInstantlyBuild);
            if (event.isCancelled()) {
                // Paper end
                ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutAttachEntity(this, this.getLeashHolder()));
                return EnumInteractionResult.PASS;
            }
            // CraftBukkit end
            this.unleash(true, event.isDropLeash()); // Paper - drop leash variable
            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            EnumInteractionResult enuminteractionresult = this.c(entityhuman, enumhand);

            if (enuminteractionresult.a()) {
                return enuminteractionresult;
            } else {
                enuminteractionresult = this.b(entityhuman, enumhand);
                return enuminteractionresult.a() ? enuminteractionresult : super.a(entityhuman, enumhand);
            }
        }
    }

    private EnumInteractionResult c(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.LEAD && this.a(entityhuman)) {
            // CraftBukkit start - fire PlayerLeashEntityEvent
            if (CraftEventFactory.callPlayerLeashEntityEvent(this, entityhuman, entityhuman).isCancelled()) {
                ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutAttachEntity(this, this.getLeashHolder()));
                return EnumInteractionResult.PASS;
            }
            // CraftBukkit end
            this.setLeashHolder(entityhuman, true);
            itemstack.subtract(1);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            if (itemstack.getItem() == Items.NAME_TAG) {
                EnumInteractionResult enuminteractionresult = itemstack.a(entityhuman, this, enumhand);

                if (enuminteractionresult.a()) {
                    return enuminteractionresult;
                }
            }

            if (itemstack.getItem() instanceof ItemMonsterEgg) {
                if (this.world instanceof WorldServer) {
                    ItemMonsterEgg itemmonsteregg = (ItemMonsterEgg) itemstack.getItem();
                    Optional<EntityInsentient> optional = itemmonsteregg.a(entityhuman, this, (EntityTypes<? extends EntityInsentient>) this.getEntityType(), (WorldServer) this.world, this.getPositionVector(), itemstack); // CraftBukkit - decompile error

                    optional.ifPresent((entityinsentient) -> {
                        this.a(entityhuman, entityinsentient);
                    });
                    return optional.isPresent() ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS;
                } else {
                    return EnumInteractionResult.CONSUME;
                }
            } else {
                return EnumInteractionResult.PASS;
            }
        }
    }

    protected void a(EntityHuman entityhuman, EntityInsentient entityinsentient) {}

    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        return tryRide(entityhuman, enumhand); // Purpur
    }

    public boolean ev() {
        return this.a(this.getChunkCoordinates());
    }

    public boolean a(BlockPosition blockposition) {
        return this.bA == -1.0F ? true : this.bz.j(blockposition) < (double) (this.bA * this.bA);
    }

    public void a(BlockPosition blockposition, int i) {
        this.bz = blockposition;
        this.bA = (float) i;
    }

    public BlockPosition ew() {
        return this.bz;
    }

    public float ex() {
        return this.bA;
    }

    public boolean ez() {
        return this.bA != -1.0F;
    }

    // CraftBukkit start
    @Nullable
    public <T extends EntityInsentient> T a(EntityTypes<T> entitytypes, boolean flag) {
        return this.a(entitytypes, flag, EntityTransformEvent.TransformReason.UNKNOWN, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    @Nullable
    public <T extends EntityInsentient> T a(EntityTypes<T> entitytypes, boolean flag, EntityTransformEvent.TransformReason transformReason, CreatureSpawnEvent.SpawnReason spawnReason) {
        // CraftBukkit end
        if (this.dead) {
            return null;
        } else {
            T t0 = entitytypes.a(this.world); // CraftBukkit - decompile error

            t0.u(this);
            t0.setBaby(this.isBaby());
            t0.setNoAI(this.isNoAI());
            if (this.hasCustomName()) {
                t0.setCustomName(this.getCustomName());
                t0.setCustomNameVisible(this.getCustomNameVisible());
            }

            if (this.isPersistent()) {
                t0.setPersistent();
            }

            t0.setInvulnerable(this.isInvulnerable());
            if (flag) {
                t0.setCanPickupLoot(this.canPickupLoot());
                EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
                int i = aenumitemslot.length;

                for (int j = 0; j < i; ++j) {
                    EnumItemSlot enumitemslot = aenumitemslot[j];
                    ItemStack itemstack = this.getEquipment(enumitemslot);

                    if (!itemstack.isEmpty()) {
                        t0.setSlot(enumitemslot, itemstack.cloneItemStack());
                        t0.a(enumitemslot, this.e(enumitemslot));
                        itemstack.setCount(0);
                    }
                }
            }

            // CraftBukkit start
            if (CraftEventFactory.callEntityTransformEvent(this, t0, transformReason).isCancelled()) {
                return null;
            }
            this.world.addEntity(t0, spawnReason);
            // CraftBukkit end
            if (this.isPassenger()) {
                Entity entity = this.getVehicle();

                this.stopRiding();
                t0.a(entity, true);
            }

            this.die();
            return t0;
        }
    }

    protected void eA() {
        if (this.by != null) {
            this.eI();
        }

        if (this.leashHolder != null) {
            if (!this.isAlive() || !this.leashHolder.isAlive()) {
                // Paper start - drop leash variable
                EntityUnleashEvent event = new EntityUnleashEvent(this.getBukkitEntity(), (!this.isAlive()) ? UnleashReason.PLAYER_UNLEASH : UnleashReason.HOLDER_GONE, true);
                this.world.getServer().getPluginManager().callEvent(event); // CraftBukkit
                this.unleash(true, event.isDropLeash());
                // Paper end
            }

        }
    }

    public void unleash(boolean flag, boolean flag1) {
        if (this.leashHolder != null) {
            this.attachedToPlayer = false;
            if (!(this.leashHolder instanceof EntityHuman)) {
                this.leashHolder.attachedToPlayer = false;
            }

            this.leashHolder = null;
            this.by = null;
            if (!this.world.isClientSide && flag1) {
                this.forceDrops = true; // CraftBukkit
                this.a((IMaterial) Items.LEAD);
                this.forceDrops = false; // CraftBukkit
            }

            if (!this.world.isClientSide && flag && this.world instanceof WorldServer) {
                ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutAttachEntity(this, null));
            }
        }

    }

    public boolean a(EntityHuman entityhuman) {
        return !this.isLeashed() && !(this instanceof IMonster);
    }

    public boolean isLeashed() {
        return this.leashHolder != null;
    }

    @Nullable
    public Entity getLeashHolder() {
        if (this.leashHolder == null && this.bx != 0 && this.world.isClientSide) {
            this.leashHolder = this.world.getEntity(this.bx);
        }

        return this.leashHolder;
    }

    public void setLeashHolder(Entity entity, boolean flag) {
        this.leashHolder = entity;
        this.by = null;
        this.attachedToPlayer = true;
        if (!(this.leashHolder instanceof EntityHuman)) {
            this.leashHolder.attachedToPlayer = true;
        }

        if (!this.world.isClientSide && flag && this.world instanceof WorldServer) {
            ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutAttachEntity(this, this.leashHolder));
        }

        if (this.isPassenger()) {
            this.stopRiding();
        }

    }

    @Override
    public boolean a(Entity entity, boolean flag) {
        boolean flag1 = super.a(entity, flag);

        if (flag1 && this.isLeashed()) {
            // Paper start - drop leash variable
            EntityUnleashEvent event = new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.UNKNOWN, true);
            this.world.getServer().getPluginManager().callEvent(event); // CraftBukkit
            this.unleash(true, event.isDropLeash());
            // Paper end
        }

        return flag1;
    }

    private void eI() {
        if (this.by != null && this.world instanceof WorldServer) {
            if (this.by.b("UUID")) {
                UUID uuid = this.by.a("UUID");
                Entity entity = ((WorldServer) this.world).getEntity(uuid);

                if (entity != null) {
                    this.setLeashHolder(entity, true);
                    return;
                }
            } else if (this.by.hasKeyOfType("X", 99) && this.by.hasKeyOfType("Y", 99) && this.by.hasKeyOfType("Z", 99)) {
                BlockPosition blockposition = new BlockPosition(this.by.getInt("X"), this.by.getInt("Y"), this.by.getInt("Z"));

                this.setLeashHolder(EntityLeash.a(this.world, blockposition), true);
                return;
            }

            if (this.ticksLived > 100) {
                this.a((IMaterial) Items.LEAD);
                this.by = null;
            }
        }

    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        EnumItemSlot enumitemslot;

        if (i == 98) {
            enumitemslot = EnumItemSlot.MAINHAND;
        } else if (i == 99) {
            enumitemslot = EnumItemSlot.OFFHAND;
        } else if (i == 100 + EnumItemSlot.HEAD.b()) {
            enumitemslot = EnumItemSlot.HEAD;
        } else if (i == 100 + EnumItemSlot.CHEST.b()) {
            enumitemslot = EnumItemSlot.CHEST;
        } else if (i == 100 + EnumItemSlot.LEGS.b()) {
            enumitemslot = EnumItemSlot.LEGS;
        } else {
            if (i != 100 + EnumItemSlot.FEET.b()) {
                return false;
            }

            enumitemslot = EnumItemSlot.FEET;
        }

        if (!itemstack.isEmpty() && !c(enumitemslot, itemstack) && enumitemslot != EnumItemSlot.HEAD) {
            return false;
        } else {
            this.setSlot(enumitemslot, itemstack);
            return true;
        }
    }

    @Override
    public boolean cs() {
        return this.er() && super.cs();
    }

    public static boolean c(EnumItemSlot enumitemslot, ItemStack itemstack) {
        EnumItemSlot enumitemslot1 = j(itemstack);

        return enumitemslot1 == enumitemslot || enumitemslot1 == EnumItemSlot.MAINHAND && enumitemslot == EnumItemSlot.OFFHAND || enumitemslot1 == EnumItemSlot.OFFHAND && enumitemslot == EnumItemSlot.MAINHAND;
    }

    @Override
    public boolean doAITick() {
        return super.doAITick() && !this.isNoAI();
    }

    public void setNoAI(boolean flag) {
        byte b0 = this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 1) : (byte) (b0 & -2));
    }

    public void setLeftHanded(boolean flag) {
        byte b0 = this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 2) : (byte) (b0 & -3));
    }

    public void setAggressive(boolean flag) {
        byte b0 = this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 4) : (byte) (b0 & -5));
    }

    public boolean isNoAI() {
        return (this.datawatcher.get(EntityInsentient.b) & 1) != 0;
    }

    public boolean isLeftHanded() {
        return (this.datawatcher.get(EntityInsentient.b) & 2) != 0;
    }

    public boolean isAggressive() {
        return (this.datawatcher.get(EntityInsentient.b) & 4) != 0;
    }

    public void setBaby(boolean flag) {}

    @Override
    public EnumMainHand getMainHand() {
        return this.isLeftHanded() ? EnumMainHand.LEFT : EnumMainHand.RIGHT;
    }

    @Override
    public boolean c(EntityLiving entityliving) {
        return entityliving.getEntityType() == EntityTypes.PLAYER && ((EntityHuman) entityliving).abilities.isInvulnerable ? false : super.c(entityliving);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        float f = (float) this.b(GenericAttributes.ATTACK_DAMAGE);
        float f1 = (float) this.b(GenericAttributes.ATTACK_KNOCKBACK);

        if (entity instanceof EntityLiving) {
            f += EnchantmentManager.a(this.getItemInMainHand(), ((EntityLiving) entity).getMonsterType());
            f1 += (float) EnchantmentManager.b(this);
        }

        int i = EnchantmentManager.getFireAspectEnchantmentLevel(this);

        if (i > 0) {
            // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), i * 4);
            org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

            if (!combustEvent.isCancelled()) {
                entity.setOnFire(combustEvent.getDuration(), false);
            }
            // CraftBukkit end
        }

        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);

        if (flag) {
            if (f1 > 0.0F && entity instanceof EntityLiving) {
                ((EntityLiving) entity).doKnockback(f1 * 0.5F, MathHelper.sin(this.yaw * 0.017453292F), -MathHelper.cos(this.yaw * 0.017453292F), this); // Paper
                this.setMot(this.getMot().d(0.6D, 1.0D, 0.6D));
            }

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                this.a(entityhuman, this.getItemInMainHand(), entityhuman.isHandRaised() ? entityhuman.getActiveItem() : ItemStack.b);
            }

            this.a(this, entity);
            this.z(entity);
        }
        if (entity instanceof EntityPlayer) this.ticksSinceLastInteraction = 0; // Purpur
        return flag;
    }

    private void a(EntityHuman entityhuman, ItemStack itemstack, ItemStack itemstack1) {
        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentManager.getDigSpeedEnchantmentLevel(this) * 0.05F;

            if (this.random.nextFloat() < f) {
                entityhuman.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                this.world.broadcastEntityEffect(entityhuman, (byte) 30);
            }
        }

    }

    public boolean isInDaylight() { return this.eG(); } // Paper - OBFHELPER
    protected boolean eG() {
        /* // Purpur start - moved to Entity
        if (this.world.isDay() && !this.world.isClientSide) {
            float f = this.aR();
            BlockPosition blockposition = this.getVehicle() instanceof EntityBoat ? (new BlockPosition(this.locX(), (double) Math.round(this.locY()), this.locZ())).up() : new BlockPosition(this.locX(), (double) Math.round(this.locY()), this.locZ());

            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.e(blockposition)) {
                return true;
            }
        }

        return false;
        */
        return super.isInDaylight();
        // Purpur end
    }

    @Override
    protected void c(Tag<FluidType> tag) {
        if (this.getNavigation().r()) {
            super.c(tag);
        } else {
            this.setMot(this.getMot().add(0.0D, 0.3D, 0.0D));
        }

    }

    @Override
    protected void bN() {
        super.bN();
        // Paper start - drop leash variable
        EntityUnleashEvent event = new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.UNKNOWN, false);
        this.world.getServer().getPluginManager().callEvent(event); // CraftBukkit
        this.unleash(true, event.isDropLeash());
        // Paper end
    }

    // Purpur start
    public double getMaxY() {
        return world.getHeight();
    }

    public EnumInteractionResult tryRide(EntityHuman entityhuman, EnumHand enumhand) {
        if (!isRidable()) {
            return EnumInteractionResult.PASS;
        }
        if (enumhand != EnumHand.MAIN_HAND) {
            return EnumInteractionResult.PASS;
        }
        if (entityhuman.isSneaking()) {
            return EnumInteractionResult.PASS;
        }
        if (!entityhuman.getItemInHand(enumhand).isEmpty()) {
            return EnumInteractionResult.PASS;
        }
        if (!passengers.isEmpty() || entityhuman.isPassenger()) {
            return EnumInteractionResult.PASS;
        }
        if (this instanceof EntityTameableAnimal) {
            EntityTameableAnimal tameable = (EntityTameableAnimal) this;
            if (tameable.isTamed() && !tameable.isOwner(entityhuman)) {
                return EnumInteractionResult.PASS;
            }
            if (!tameable.isTamed() && !world.purpurConfig.untamedTamablesAreRidable) {
                return EnumInteractionResult.PASS;
            }
        }
        if (this instanceof EntityAgeable) {
            EntityAgeable ageable = (EntityAgeable) this;
            if (ageable.isBaby() && !world.purpurConfig.babiesAreRidable) {
                return EnumInteractionResult.PASS;
            }
        }
        if (!entityhuman.getBukkitEntity().hasPermission("allow.ride." + getEntityType().getName())) {
            entityhuman.sendMessage(net.pl3x.purpur.PurpurConfig.cannotRideMob);
            return EnumInteractionResult.PASS;
        }
        entityhuman.yaw = this.yaw;
        entityhuman.pitch = this.pitch;
        if (entityhuman.startRiding(this)) {
            return EnumInteractionResult.SUCCESS;
        } else {
            return EnumInteractionResult.PASS;
        }
    }
    // Purpur end
}
