package net.minecraft.world.entity.monster;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PathfinderGoalHasRider;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.util.PathfinderGoalUtil;
import net.minecraft.world.entity.animal.EntityChicken;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.animal.EntityTurtle;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
// CraftBukkit end

public class EntityZombie extends EntityMonster {

    private static final UUID b = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private final AttributeModifier c = new AttributeModifier(EntityZombie.b, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE); private final AttributeModifier babyModifier = this.c; // Paper - remove static - Make baby speed configurable
    private static final DataWatcherObject<Boolean> d = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Integer> bo = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.b);
    public static final DataWatcherObject<Boolean> DROWN_CONVERTING = DataWatcher.a(EntityZombie.class, DataWatcherRegistry.i);
    public static final Predicate<EnumDifficulty> getDoorBreakingPredicate() { return bq; } // Paper - OBFHELPER
    private static final Predicate<EnumDifficulty> bq = (enumdifficulty) -> {
        return enumdifficulty == EnumDifficulty.HARD;
    };
    private final PathfinderGoalBreakDoor br;
    private boolean bs;
    private int bt;
    public int drownedConversionTime;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit - add field
    // private boolean shouldBurnInDay = true; // Paper // Purpur - implemented in EntityLiving

    public EntityZombie(EntityTypes<? extends EntityZombie> entitytypes, World world) {
        super(entitytypes, world);
        this.br = new PathfinderGoalBreakDoor(this, com.google.common.base.Predicates.in(world.paperConfig.zombieBreakDoors)); // Paper
        this.setShouldBurnInDay(true); // Purpur
    }

    public EntityZombie(World world) {
        this(EntityTypes.ZOMBIE, world);
    }

    // Purpur start
    @Override
    public boolean isRidable() {
        return world.purpurConfig.zombieRidable;
    }

    @Override
    public boolean isRidableInWater() {
        return world.purpurConfig.zombieRidableInWater;
    }

    public boolean jockeyOnlyBaby() {
        return world.purpurConfig.zombieJockeyOnlyBaby;
    }

    public double jockeyChance() {
        return world.purpurConfig.zombieJockeyChance;
    }

    public boolean jockeyTryExistingChickens() {
        return world.purpurConfig.zombieJockeyTryExistingChickens;
    }

    @Override
    public void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.zombieMaxHealth);
    }

    protected void generateReinforcementsChance() {
        this.getAttributeInstance(GenericAttributes.SPAWN_REINFORCEMENTS).setValue(this.random.nextDouble() * this.world.purpurConfig.zombieSpawnReinforcements);
    }
    // Purpur end

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalHasRider(this)); // Purpur
        if (world.paperConfig.zombiesTargetTurtleEggs) this.goalSelector.a(4, new EntityZombie.a(this, 1.0D, 3)); // Paper
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(0, new PathfinderGoalHasRider(this)); // Purpur
        this.m();
    }

    protected void m() {
        this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, true, 4, this::eU));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(EntityPigZombie.class));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        // Purpur start
        if ( world.spigotConfig.zombieAggressiveTowardsVillager ) this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityVillagerAbstract>(this, EntityVillagerAbstract.class, false) { // Spigot
            @Override
            public boolean a() {
                return (world.purpurConfig.zombieAggressiveTowardsVillagerWhenLagging || !world.getMinecraftServer().server.isLagging()) && super.a();
            }
            @Override
            public boolean b() {
                return (world.purpurConfig.zombieAggressiveTowardsVillagerWhenLagging || !world.getMinecraftServer().server.isLagging()) && super.b();
            }
        });
        // Purpur end
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
        this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bo));
    }

    public static AttributeProvider.Builder eS() {
        return EntityMonster.eR().a(GenericAttributes.FOLLOW_RANGE, 35.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.23000000417232513D).a(GenericAttributes.ATTACK_DAMAGE, 3.0D).a(GenericAttributes.ARMOR, 2.0D).a(GenericAttributes.SPAWN_REINFORCEMENTS);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.getDataWatcher().register(EntityZombie.d, false);
        this.getDataWatcher().register(EntityZombie.bo, 0);
        this.getDataWatcher().register(EntityZombie.DROWN_CONVERTING, false);
    }

    public boolean isDrownConverting() {
        return this.getDataWatcher().get(EntityZombie.DROWN_CONVERTING);
    }

    public boolean canBreakDoors() { return this.eU(); } // Paper - OBFHELPER
    public boolean eU() {
        return this.bs;
    }

    public void setCanBreakDoors(boolean canBreakDoors) { this.u(canBreakDoors); } // Paper - OBFHELPER
    public void u(boolean flag) {
        if (this.eK() && PathfinderGoalUtil.a(this)) {
            if (this.bs != flag) {
                this.bs = flag;
                ((Navigation) this.getNavigation()).a(flag);
                if (flag) {
                    this.goalSelector.a(1, this.br);
                } else {
                    this.goalSelector.a(this.br);
                }
            }
        } else if (this.bs) {
            this.goalSelector.a(this.br);
            this.bs = false;
        }

    }

    protected boolean eK() {
        return true;
    }

    @Override
    public boolean isBaby() {
        return this.getDataWatcher().get(EntityZombie.d);
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        if (this.isBaby()) {
            this.f = (int) ((float) this.f * 2.5F);
        }

        return super.getExpValue(entityhuman);
    }

    @Override
    public void setBaby(boolean flag) {
        this.getDataWatcher().set(EntityZombie.d, flag);
        if (this.world != null && !this.world.isClientSide) {
            AttributeModifiable attributemodifiable = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

            attributemodifiable.removeModifier(this.babyModifier); // Paper
            if (flag) {
                attributemodifiable.b(this.babyModifier); // Paper
            }
        }

    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityZombie.d.equals(datawatcherobject)) {
            this.updateSize();
        }

        super.a(datawatcherobject);
    }

    protected boolean eN() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.world.isClientSide && this.isAlive() && !this.isNoAI()) {
            if (this.isDrownConverting()) {
                // CraftBukkit start - Use wall time instead of ticks for conversion
                int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
                this.drownedConversionTime -= elapsedTicks;
                // CraftBukkit end
                if (this.drownedConversionTime < 0) {
                    this.eP();
                }
            } else if (this.eN()) {
                if (this.a(TagsFluid.WATER)) {
                    ++this.bt;
                    if (this.bt >= 600) {
                        this.startDrownedConversion(300);
                        this.lastTick = MinecraftServer.currentTick; // Paper - Make sure this is set at start of process - GH-1887
                    }
                } else {
                    this.bt = -1;
                }
            }
        }

        super.tick();
        this.lastTick = MinecraftServer.currentTick; // CraftBukkit
    }

    @Override
    public void movementTick() {
        /* // Purpur start - implemented in EntityLiving
        if (this.isAlive()) {
            boolean flag = this.T_() && this.eG();

            if (flag) {
                ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);

                if (!itemstack.isEmpty()) {
                    if (itemstack.e()) {
                        itemstack.setDamage(itemstack.getDamage() + this.random.nextInt(2));
                        if (itemstack.getDamage() >= itemstack.h()) {
                            this.broadcastItemBreak(EnumItemSlot.HEAD);
                            this.setSlot(EnumItemSlot.HEAD, ItemStack.b);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setOnFire(8);
                }
            }
        }
        */ // Purpur end

        super.movementTick();
    }

    // Paper start
    public void stopDrowning() {
        this.drownedConversionTime = -1;
        this.getDataWatcher().set(EntityZombie.DROWN_CONVERTING, false);
    }
    // Paper end
    public void startDrownedConversion(int i) {
        this.lastTick = MinecraftServer.currentTick; // CraftBukkit
        this.drownedConversionTime = i;
        this.getDataWatcher().set(EntityZombie.DROWN_CONVERTING, true);
    }

    protected void eP() {
        this.b(EntityTypes.DROWNED);
        if (!this.isSilent()) {
            this.world.a(null, 1040, this.getChunkCoordinates(), 0);
        }

    }

    protected void b(EntityTypes<? extends EntityZombie> entitytypes) {
        EntityZombie entityzombie = this.a(entitytypes, true, EntityTransformEvent.TransformReason.DROWNED, CreatureSpawnEvent.SpawnReason.DROWNED);

        if (entityzombie != null) {
            entityzombie.y(entityzombie.world.getDamageScaler(entityzombie.getChunkCoordinates()).d());
            entityzombie.u(entityzombie.eK() && this.eU());
            // CraftBukkit start - SPIGOT-5208: End conversion to stop event spam
        } else {
            ((Zombie) getBukkitEntity()).setConversionTime(-1);
            // CraftBukkit end
        }

    }

    public boolean shouldBurnInDay() { return T_(); } // Paper - OBFHELPER
    protected boolean T_() {
        return this.shouldBurnInDay; // Paper - use api value instead
    }

    // Paper start
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        this.shouldBurnInDay = shouldBurnInDay;
    }
    // Paper end

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!super.damageEntity(damagesource, f)) {
            return false;
        } else if (!(this.world instanceof WorldServer)) {
            return false;
        } else {
            WorldServer worldserver = (WorldServer) this.world;
            EntityLiving entityliving = this.getGoalTarget();

            if (entityliving == null && damagesource.getEntity() instanceof EntityLiving) {
                entityliving = (EntityLiving) damagesource.getEntity();
            }

            if (entityliving != null && this.world.getDifficulty() == EnumDifficulty.HARD && (double) this.random.nextFloat() < this.b(GenericAttributes.SPAWN_REINFORCEMENTS) && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                int i = MathHelper.floor(this.locX());
                int j = MathHelper.floor(this.locY());
                int k = MathHelper.floor(this.locZ());
                EntityZombie entityzombie = new EntityZombie(this.world);

                for (int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    BlockPosition blockposition = new BlockPosition(i1, j1, k1);
                    EntityTypes<?> entitytypes = entityzombie.getEntityType();
                    EntityPositionTypes.Surface entitypositiontypes_surface = EntityPositionTypes.a(entitytypes);

                    if (SpawnerCreature.a(entitypositiontypes_surface, this.world, blockposition, entitytypes) && EntityPositionTypes.a(entitytypes, worldserver, EnumMobSpawn.REINFORCEMENT, blockposition, this.world.random)) {
                        entityzombie.setPosition(i1, j1, k1);
                        if (!this.world.isPlayerNearby(i1, j1, k1, 7.0D) && this.world.j(entityzombie) && this.world.getCubes(entityzombie) && !this.world.containsLiquid(entityzombie.getBoundingBox())) {
                            entityzombie.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.REINFORCEMENT_TARGET, true); // CraftBukkit
                            entityzombie.prepare(worldserver, this.world.getDamageScaler(entityzombie.getChunkCoordinates()), EnumMobSpawn.REINFORCEMENT, null, null);
                            worldserver.addAllEntities(entityzombie, CreatureSpawnEvent.SpawnReason.REINFORCEMENTS); // CraftBukkit
                            this.getAttributeInstance(GenericAttributes.SPAWN_REINFORCEMENTS).addModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, AttributeModifier.Operation.ADDITION));
                            entityzombie.getAttributeInstance(GenericAttributes.SPAWN_REINFORCEMENTS).addModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, AttributeModifier.Operation.ADDITION));
                            break;
                        }
                    }
                }
            }

            return true;
        }
    }

    @Override
    public boolean attackEntity(Entity entity) {
        boolean flag = super.attackEntity(entity);

        if (flag) {
            float f = this.world.getDamageScaler(this.getChunkCoordinates()).b();

            if (this.getItemInMainHand().isEmpty() && this.isBurning() && this.random.nextFloat() < f * 0.3F) {
                // CraftBukkit start
                EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 2 * (int) f); // PAIL: fixme
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    entity.setOnFire(event.getDuration(), false);
                }
                // CraftBukkit end
            }
        }

        return flag;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEffect getSoundStep() {
        return SoundEffects.ENTITY_ZOMBIE_STEP;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(this.getSoundStep(), 0.15F, 1.0F);
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        super.a(difficultydamagescaler);
        if (this.random.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
            int i = this.random.nextInt(3);

            if (i == 0) {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("IsBaby", this.isBaby());
        nbttagcompound.setBoolean("CanBreakDoors", this.eU());
        nbttagcompound.setInt("InWaterTime", this.isInWater() ? this.bt : -1);
        nbttagcompound.setInt("DrownedConversionTime", this.isDrownConverting() ? this.drownedConversionTime : -1);
        // nbttagcompound.setBoolean("Paper.ShouldBurnInDay", shouldBurnInDay); // Paper // Purpur - implemented in EntityLiving
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setBaby(nbttagcompound.getBoolean("IsBaby"));
        this.u(nbttagcompound.getBoolean("CanBreakDoors"));
        this.bt = nbttagcompound.getInt("InWaterTime");
        if (nbttagcompound.hasKeyOfType("DrownedConversionTime", 99) && nbttagcompound.getInt("DrownedConversionTime") > -1) {
            this.startDrownedConversion(nbttagcompound.getInt("DrownedConversionTime"));
        }
        /* // Purpur start - implemented in EntityLiving
        // Paper start
        if (nbttagcompound.hasKey("Paper.ShouldBurnInDay")) {
            shouldBurnInDay = nbttagcompound.getBoolean("Paper.ShouldBurnInDay");
        }
        // Paper end
         */ // Purpur end - implemented in EntityLiving
    }

    @Override
    public void a(WorldServer worldserver, EntityLiving entityliving) {
        super.a(worldserver, entityliving);
        // Paper start
        if (world.paperConfig.zombieVillagerInfectionChance != 0.0 && (world.paperConfig.zombieVillagerInfectionChance != -1.0 || worldserver.getDifficulty() == EnumDifficulty.NORMAL || worldserver.getDifficulty() == EnumDifficulty.HARD) && entityliving instanceof EntityVillager) {
            if (world.paperConfig.zombieVillagerInfectionChance == -1.0 && worldserver.getDifficulty() != EnumDifficulty.HARD && this.random.nextBoolean()) {
                return;
            }
            if (world.paperConfig.zombieVillagerInfectionChance != -1.0 && (this.random.nextDouble() * 100.0) > world.paperConfig.zombieVillagerInfectionChance) {
                return;
            } // Paper end

            EntityVillager entityvillager = (EntityVillager) entityliving;
            // CraftBukkit start
            EntityZombieVillager entityzombievillager = entityvillager.a(EntityTypes.ZOMBIE_VILLAGER, false, EntityTransformEvent.TransformReason.INFECTION, CreatureSpawnEvent.SpawnReason.INFECTION);
            if (entityzombievillager == null) {
                return;
            }
            // CraftBukkit end

            entityzombievillager.prepare(worldserver, worldserver.getDamageScaler(entityzombievillager.getChunkCoordinates()), EnumMobSpawn.CONVERSION, new EntityZombie.GroupDataZombie(false, true), null);
            entityzombievillager.setVillagerData(entityvillager.getVillagerData());
            entityzombievillager.a((NBTBase) entityvillager.fj().a((DynamicOps) DynamicOpsNBT.a).getValue());
            entityzombievillager.setOffers(entityvillager.getOffers().a());
            entityzombievillager.a(entityvillager.getExperience());
            if (!this.isSilent()) {
                worldserver.a(null, 1026, this.getChunkCoordinates(), 0);
            }
        }

    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public boolean canPickup(ItemStack itemstack) {
        return itemstack.getItem() == Items.EGG && this.isBaby() && this.isPassenger() ? false : super.canPickup(itemstack);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        Object object = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
        float f = difficultydamagescaler.d();

        this.setCanPickupLoot(this.world.paperConfig.zombiesAlwaysCanPickUpLoot || this.random.nextFloat() < 0.55F * f); // Paper
        if (object == null) {
            object = new EntityZombie.GroupDataZombie(a(worldaccess.getRandom()), true);
        }

        if (object instanceof EntityZombie.GroupDataZombie) {
            EntityZombie.GroupDataZombie entityzombie_groupdatazombie = (EntityZombie.GroupDataZombie) object;

            // Purpur start
            if (!jockeyOnlyBaby() || entityzombie_groupdatazombie.isBaby()) {
                this.setBaby(entityzombie_groupdatazombie.isBaby());
                if (entityzombie_groupdatazombie.b) {
                    if ((double) worldaccess.getRandom().nextFloat() < jockeyChance()) {
                        List<EntityChicken> list = jockeyTryExistingChickens() ? worldaccess.a(EntityChicken.class, this.getBoundingBox().grow(5.0D, 3.0D, 5.0D), IEntitySelector.c) : Collections.emptyList();

                        if (!list.isEmpty()) {
                            EntityChicken entitychicken = list.get(0);

                            entitychicken.setChickenJockey(true);
                            this.startRiding(entitychicken);
                        } else { // Purpur
                        EntityChicken entitychicken1 = EntityTypes.CHICKEN.a(this.world);

                        entitychicken1.setPositionRotation(this.locX(), this.locY(), this.locZ(), this.yaw, 0.0F);
                        entitychicken1.prepare(worldaccess, difficultydamagescaler, EnumMobSpawn.JOCKEY, null, null);
                        entitychicken1.setChickenJockey(true);
                        this.startRiding(entitychicken1);
                        worldaccess.addEntity(entitychicken1, CreatureSpawnEvent.SpawnReason.MOUNT); // CraftBukkit
                        } // Purpur
                    }
                }
            }

            this.u(this.eK() && this.random.nextFloat() < f * 0.1F);
            this.a(difficultydamagescaler);
            this.b(difficultydamagescaler);
        }

        if (this.getEquipment(EnumItemSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);

            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setSlot(EnumItemSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.dropChanceArmor[EnumItemSlot.HEAD.b()] = 0.0F;
            }
        }

        this.y(f);
        return (GroupDataEntity) object;
    }

    public static boolean a(Random random) {
        return random.nextFloat() < 0.05F;
    }

    protected void y(float f) {
        this.eV();
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806D, AttributeModifier.Operation.ADDITION));
        double d0 = this.random.nextDouble() * 1.5D * (double) f;

        if (d0 > 1.0D) {
            this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).addModifier(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        if (this.random.nextFloat() < f * 0.05F) {
            this.getAttributeInstance(GenericAttributes.SPAWN_REINFORCEMENTS).addModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADDITION));
            this.getAttributeInstance(GenericAttributes.MAX_HEALTH).addModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            this.u(this.eK());
        }

    }

    protected void eV() {
        generateReinforcementsChance(); // Purpur
    }

    @Override
    public double bb() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropDeathLoot(damagesource, i, flag);
        Entity entity = damagesource.getEntity();

        if (entity instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) entity;

            if (entitycreeper.canCauseHeadDrop()) {
                ItemStack itemstack = this.eM();

                if (!itemstack.isEmpty()) {
                    entitycreeper.setCausedHeadDrop();
                    this.a(itemstack);
                }
            }
        }

    }

    protected ItemStack eM() {
        return new ItemStack(Items.ZOMBIE_HEAD);
    }

    class a extends PathfinderGoalRemoveBlock {

        a(EntityCreature entitycreature, double d0, int i) {
            super(Blocks.TURTLE_EGG, entitycreature, d0, i);
        }

        @Override
        public void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
            generatoraccess.playSound(null, blockposition, SoundEffects.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5F, 0.9F + EntityZombie.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void a(World world, BlockPosition blockposition) {
            world.playSound(null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
        }

        @Override
        public double h() {
            return 1.14D;
        }
    }

    public static class GroupDataZombie implements GroupDataEntity {

        public final boolean a; public boolean isBaby() { return a; } // Purpur - OBFHELPER
        public final boolean b;

        public GroupDataZombie(boolean flag, boolean flag1) {
            this.a = flag;
            this.b = flag1;
        }
    }
}
