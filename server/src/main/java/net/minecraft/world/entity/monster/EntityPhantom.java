package net.minecraft.world.entity.monster;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.particles.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.PathfinderGoalHasRider;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFlying;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.IEntitySelector;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.control.EntityAIBodyControl;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderCrystal;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.phys.Vec3D;

public class EntityPhantom extends EntityFlying implements IMonster {

    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityPhantom.class, DataWatcherRegistry.b);
    private Vec3D c; public void setHomeOffset(Vec3D offset) { this.c = offset; } public Vec3D getHomeOffset() { return this.c; } // Purpur - OBFHELPER
    private BlockPosition d; public void setHome(BlockPosition home) { this.d = home; } public BlockPosition getHome() { return this.d; } // Purpur - OBFHELPER
    private EntityPhantom.AttackPhase bo; public AttackPhase getAttackPhase() { return this.bo; } // Purpur - OBFHELPER
    private Vec3D crystalPosition; // Purpur
    private static final RecipeItemStack TORCH = RecipeItemStack.a(Items.torch(), Items.soulTorch()); // Purpur

    public EntityPhantom(EntityTypes<? extends EntityPhantom> entitytypes, World world) {
        super(entitytypes, world);
        this.c = Vec3D.ORIGIN;
        this.d = BlockPosition.ZERO;
        this.bo = EntityPhantom.AttackPhase.CIRCLE;
        this.f = 5;
        this.moveController = new EntityPhantom.g(this);
        this.lookController = new EntityPhantom.f(this);
    }

    // Purpur start
    @Override
    public boolean isRidable() {
        return world.purpurConfig.phantomRidable;
    }

    @Override
    public boolean isRidableInWater() {
        return world.purpurConfig.phantomRidableInWater;
    }

    @Override
    public double getMaxY() {
        return world.purpurConfig.phantomMaxY;
    }

    @Override
    public void g(Vec3D vec3d) {
        super.g(vec3d);
        if (hasPurpurRider() && !onGround) {
            float speed = (float) getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue();
            setSpeed(speed);
            Vec3D mot = getMot();
            move(EnumMoveType.SELF, mot.multiply(speed, speed, speed));
            setMot(mot.a(0.9D));
        }
    }

    public static AttributeProvider.Builder defaultAttributes() {
        return EntityMonster.eR().a(GenericAttributes.FLYING_SPEED, 3.0D);
    }

    @Override
    public boolean onSpacebar() {
        if (hasPurpurRider() && getPurpurRider().getBukkitEntity().hasPermission("allow.special.phantom")) {
            shoot();
        }
        return false;
    }

    public boolean shoot() {
        org.bukkit.Location loc = ((org.bukkit.entity.LivingEntity) getBukkitEntity()).getEyeLocation();
        loc.setPitch(-loc.getPitch());
        org.bukkit.util.Vector target = loc.getDirection().normalize().multiply(100).add(loc.toVector());

        net.pl3x.purpur.entity.PhantomFlames flames = new net.pl3x.purpur.entity.PhantomFlames(world, this);
        flames.canGrief = world.purpurConfig.phantomAllowGriefing;
        flames.shoot(target.getX() - locX(), target.getY() - locY(), target.getZ() - locZ(), 1.0F, 5.0F);
        world.addEntity(flames);
        return true;
    }

    @Override
    public void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.phantomMaxHealth);
    }
    // Purpur end

    @Override
    protected EntityAIBodyControl r() {
        return new EntityPhantom.d(this);
    }

    @Override
    protected void initPathfinder() {
        // Purpur start
        this.goalSelector.a(0, new PathfinderGoalHasRider(this));
        if (world.purpurConfig.phantomOrbitCrystalRadius > 0) {
            this.goalSelector.a(1, new FindCrystalGoal(this));
            this.goalSelector.a(2, new OrbitCrystalGoal(this));
        }
        this.goalSelector.a(3, new EntityPhantom.c()); // PickAttackGoal
        this.goalSelector.a(4, new EntityPhantom.i()); // SweepAttackGoal
        this.goalSelector.a(5, new EntityPhantom.e()); // OrbitPointGoal
        this.targetSelector.a(0, new PathfinderGoalHasRider(this));
        this.targetSelector.a(1, new EntityPhantom.b()); // AttackPlayer Goal
        // Purpur end
    }

    // Purpur start
    @Override
    protected LootTableInfo.Builder a(boolean wasRecentlyHit, DamageSource damagesource) { // dropLoot
        boolean dropped = false;
        if (killer == null && damagesource.getEntity() instanceof EntityEnderCrystal) {
            if (random.nextInt(5) < 1) { // 1 out of 5 chance (20%)
                dropped = dropItem(new ItemStack(Items.PHANTOM_MEMBRANE)) != null;
            }
        }
        if (!dropped) {
            return super.a(wasRecentlyHit, damagesource); // dropLoot
        }
        return new LootTableInfo.Builder((WorldServer) world);
    }

    public boolean isCirclingCrystal() {
        return crystalPosition != null;
    }
    // Purpur end

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPhantom.b, 0);
    }

    public void setSize(int i) {
        this.datawatcher.set(EntityPhantom.b, MathHelper.clamp(i, 0, 64));
    }

    private void eJ() {
        this.updateSize();
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double) (6 + this.getSize()));
    }

    public int getSize() {
        return (Integer) this.datawatcher.get(EntityPhantom.b);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.35F;
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityPhantom.b.equals(datawatcherobject)) {
            this.eJ();
        }

        super.a(datawatcherobject);
    }

    @Override
    protected boolean L() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide) {
            float f = MathHelper.cos((float) (this.getId() * 3 + this.ticksLived) * 0.13F + 3.1415927F);
            float f1 = MathHelper.cos((float) (this.getId() * 3 + this.ticksLived + 1) * 0.13F + 3.1415927F);

            if (f > 0.0F && f1 <= 0.0F) {
                this.world.a(this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
            }

            int i = this.getSize();
            float f2 = MathHelper.cos(this.yaw * 0.017453292F) * (1.3F + 0.21F * (float) i);
            float f3 = MathHelper.sin(this.yaw * 0.017453292F) * (1.3F + 0.21F * (float) i);
            float f4 = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);

            this.world.addParticle(Particles.MYCELIUM, this.locX() + (double) f2, this.locY() + (double) f4, this.locZ() + (double) f3, 0.0D, 0.0D, 0.0D);
            this.world.addParticle(Particles.MYCELIUM, this.locX() - (double) f2, this.locY() + (double) f4, this.locZ() - (double) f3, 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    public void movementTick() {
        if (this.isAlive() && !hasPurpurRider() && (((shouldBurnInDay || world.purpurConfig.phantomBurnInDaylight) && this.isInDaylight()) || (world.purpurConfig.phantomBurnInLight > 0 && world.getLightLevel(new BlockPosition(this)) >= world.purpurConfig.phantomBurnInLight))) { // Paper - Configurable Burning // Purpur
            this.setOnFire(8);
        }

        super.movementTick();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.d = this.getChunkCoordinates().up(5);
        this.setSize(0);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKey("AX")) {
            this.d = new BlockPosition(nbttagcompound.getInt("AX"), nbttagcompound.getInt("AY"), nbttagcompound.getInt("AZ"));
        }

        this.setSize(nbttagcompound.getInt("Size"));
        // Paper start
        if (nbttagcompound.hasUUID("Paper.SpawningEntity")) {
            this.spawningEntity = nbttagcompound.getUUID("Paper.SpawningEntity");
        }
        this.shouldBurnInDay = nbttagcompound.getBoolean("Paper.ShouldBurnInDay");
        // Paper end
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("AX", this.d.getX());
        nbttagcompound.setInt("AY", this.d.getY());
        nbttagcompound.setInt("AZ", this.d.getZ());
        nbttagcompound.setInt("Size", this.getSize());
        // Paper start
        if (this.spawningEntity != null) {
            nbttagcompound.setUUID("Paper.SpawningEntity", this.spawningEntity);
        }
        nbttagcompound.setBoolean("Paper.ShouldBurnInDay", shouldBurnInDay);
        // Paper end
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PHANTOM_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PHANTOM_DEATH;
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public boolean a(EntityTypes<?> entitytypes) {
        return true;
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        int i = this.getSize();
        EntitySize entitysize = super.a(entitypose);
        float f = (entitysize.width + 0.2F * (float) i) / entitysize.width;

        return entitysize.a(f);
    }

    // Paper start
    java.util.UUID spawningEntity;

    public java.util.UUID getSpawningEntity() {
        return spawningEntity;
    }
    public void setSpawningEntity(java.util.UUID entity) { this.spawningEntity = entity; }

    private boolean shouldBurnInDay = true;
    public boolean shouldBurnInDay() { return shouldBurnInDay; }
    public void setShouldBurnInDay(boolean shouldBurnInDay) { this.shouldBurnInDay = shouldBurnInDay; }
    // Paper end

    // Purpur start
    class FindCrystalGoal extends PathfinderGoal {
        private final EntityPhantom phantom;
        private EntityEnderCrystal crystal;
        private java.util.Comparator<EntityEnderCrystal> comparator;

        FindCrystalGoal(EntityPhantom phantom) {
            this.phantom = phantom;
            comparator = java.util.Comparator.comparingDouble(phantom::h);
            this.a(EnumSet.of(PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() { // shouldExecute
            double range = maxTargetRange();
            List<EntityEnderCrystal> crystals = world.getEntitiesInAABB(EntityEnderCrystal.class, phantom.getBoundingBox().grow(range));
            if (crystals.isEmpty()) {
                return false;
            }
            crystals.sort(comparator);
            crystal = crystals.get(0);
            if (phantom.getDistanceSquared(crystal) > range * range) {
                crystal = null;
                return false;
            }
            return true;
        }

        @Override
        public boolean b() { // shouldContinueExecuting
            if (crystal == null || !crystal.isAlive()) {
                return false;
            }
            double range = maxTargetRange();
            return phantom.getDistanceSquared(crystal) <= (range * range) * 2;
        }

        @Override
        public void c() { // startExecuting
            phantom.crystalPosition = new Vec3D(crystal.locX(), crystal.locY() + (phantom.getRandom().nextInt(10) + 10), crystal.locZ());
        }

        @Override
        public void d() { // resetTask
            crystal = null;
            phantom.crystalPosition = null;
            super.d();
        }

        private double maxTargetRange() {
            return phantom.world.purpurConfig.phantomOrbitCrystalRadius;
        }

        class DistanceComparator implements java.util.Comparator<Entity> {
            private final Entity entity;

            public DistanceComparator(Entity entity) {
                this.entity = entity;
            }

            public int compare(Entity entity1, Entity entity2) {
                return Double.compare(entity.getDistanceSquared(entity1), entity.getDistanceSquared(entity2));
            }
        }
    }

    class OrbitCrystalGoal extends PathfinderGoal {
        private final EntityPhantom phantom;
        private float offset;
        private float radius;
        private float verticalChange;
        private float direction;

        OrbitCrystalGoal(EntityPhantom phantom) {
            this.phantom = phantom;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() { // shouldExecute
            return phantom.isCirclingCrystal();
        }

        @Override
        public void c() { // startExecuting
            this.radius = 5.0F + phantom.random.nextFloat() * 10.0F;
            this.verticalChange = -4.0F + phantom.random.nextFloat() * 9.0F;
            this.direction = phantom.random.nextBoolean() ? 1.0F : -1.0F;
            updateOffset();
        }

        @Override
        public void e() { // tick
            if (phantom.random.nextInt(350) == 0) {
                this.verticalChange = -4.0F + phantom.random.nextFloat() * 9.0F;
            }
            if (phantom.random.nextInt(250) == 0) {
                ++this.radius;
                if (this.radius > 15.0F) {
                    this.radius = 5.0F;
                    this.direction = -this.direction;
                }
            }
            if (phantom.random.nextInt(450) == 0) {
                this.offset = phantom.random.nextFloat() * 2.0F * 3.1415927F;
                updateOffset();
            }
            if (phantom.getHomeOffset().c(phantom.locX(), phantom.locY(), phantom.locZ()) < 4.0D) {
                updateOffset();
            }
            if (phantom.getHomeOffset().y < phantom.locY() && !phantom.world.isEmpty((new BlockPosition(phantom)).down(1))) {
                this.verticalChange = Math.max(1.0F, this.verticalChange);
                updateOffset();
            }
            if (phantom.getHomeOffset().y > phantom.locY() && !phantom.world.isEmpty((new BlockPosition(phantom)).up(1))) {
                this.verticalChange = Math.min(-1.0F, this.verticalChange);
                updateOffset();
            }
        }

        private void updateOffset() {
            this.offset += this.direction * 15.0F * 0.017453292F;
            phantom.setHomeOffset(phantom.crystalPosition.add(
                    this.radius * MathHelper.cos(this.offset),
                    -4.0F + this.verticalChange,
                    this.radius * MathHelper.sin(this.offset)));
        }
    }
    // Purpur end

    class b extends PathfinderGoal {

        private final PathfinderTargetCondition b;
        private int c;

        private b() {
            this.b = (new PathfinderTargetCondition()).a(64.0D);
            this.c = 20;
        }

        @Override
        public boolean a() {
            if (getPurpurRider() != null || isCirclingCrystal()) return false; // Purpur - pathfinder does not have a flag
            if (this.c > 0) {
                --this.c;
                return false;
            } else {
                this.c = 60;
                List<EntityHuman> list = EntityPhantom.this.world.a(this.b, (EntityLiving) EntityPhantom.this, EntityPhantom.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D));
                if (world.purpurConfig.phantomIgnorePlayersWithTorch) list.removeIf(human -> TORCH.test(human.getItemInHand(EnumHand.MAIN_HAND)) || TORCH.test(human.getItemInHand(EnumHand.OFF_HAND)));// Purpur
                if (!list.isEmpty()) {
                    list.sort(Comparator.comparing(Entity::locY).reversed());
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityHuman entityhuman = (EntityHuman) iterator.next();

                        if (EntityPhantom.this.a((EntityLiving) entityhuman, PathfinderTargetCondition.a)) {
                            if (!world.paperConfig.phantomOnlyAttackInsomniacs || IEntitySelector.isInsomniac.test(entityhuman)) // Paper
                            EntityPhantom.this.setGoalTarget(entityhuman, org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true); // CraftBukkit - reason
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        @Override
        public boolean b() {
            if (getPurpurRider() != null || isCirclingCrystal()) return false; // Purpur - pathfinder does not have a flag
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            return entityliving != null ? EntityPhantom.this.a(entityliving, PathfinderTargetCondition.a) : false;
        }
    }

    class c extends PathfinderGoal {

        private int b;

        private c() {}

        @Override
        public boolean a() {
            if (getPurpurRider() != null || isCirclingCrystal()) return false; // Purpur - pathfinder does not have a flag
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            return entityliving != null ? EntityPhantom.this.a(EntityPhantom.this.getGoalTarget(), PathfinderTargetCondition.a) : false;
        }

        @Override
        public void c() {
            this.b = 10;
            EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
            this.g();
        }

        @Override
        public void d() {
            EntityPhantom.this.d = EntityPhantom.this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, EntityPhantom.this.d).up(10 + EntityPhantom.this.random.nextInt(20));
        }

        @Override
        public void e() {
            if (EntityPhantom.this.bo == EntityPhantom.AttackPhase.CIRCLE) {
                --this.b;
                if (this.b <= 0) {
                    EntityPhantom.this.bo = EntityPhantom.AttackPhase.SWOOP;
                    this.g();
                    this.b = (8 + EntityPhantom.this.random.nextInt(4)) * 20;
                    EntityPhantom.this.playSound(SoundEffects.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + EntityPhantom.this.random.nextFloat() * 0.1F);
                }
            }

        }

        private void g() {
            EntityPhantom.this.d = EntityPhantom.this.getGoalTarget().getChunkCoordinates().up(20 + EntityPhantom.this.random.nextInt(20));
            if (EntityPhantom.this.d.getY() < EntityPhantom.this.world.getSeaLevel()) {
                EntityPhantom.this.d = new BlockPosition(EntityPhantom.this.d.getX(), EntityPhantom.this.world.getSeaLevel() + 1, EntityPhantom.this.d.getZ());
            }

        }
    }

    class i extends EntityPhantom.h {

        private i() {
            super();
        }

        @Override
        public boolean a() {
            return EntityPhantom.this.getGoalTarget() != null && EntityPhantom.this.bo == EntityPhantom.AttackPhase.SWOOP;
        }

        @Override
        public boolean b() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            if (entityliving == null) {
                return false;
            } else if (!entityliving.isAlive()) {
                return false;
            } else if (entityliving instanceof EntityHuman && (((EntityHuman) entityliving).isSpectator() || ((EntityHuman) entityliving).isCreative())) {
                return false;
            // Purpur start
            } else if (world.purpurConfig.phantomBurnInLight > 0 && world.getLightLevel(new BlockPosition(EntityPhantom.this)) >= world.purpurConfig.phantomBurnInLight) {
                return false;
            } else if (world.purpurConfig.phantomIgnorePlayersWithTorch && (TORCH.test(entityliving.getItemInHand(EnumHand.MAIN_HAND)) || TORCH.test(entityliving.getItemInHand(EnumHand.OFF_HAND)))) {
                return false;
            // Purpur end
            } else if (!this.a()) {
                return false;
            } else {
                if (EntityPhantom.this.ticksLived % 20 == 0) {
                    List<EntityCat> list = EntityPhantom.this.world.a(EntityCat.class, EntityPhantom.this.getBoundingBox().g(16.0D), IEntitySelector.a);

                    if (!list.isEmpty()) {
                        Iterator iterator = list.iterator();

                        while (iterator.hasNext()) {
                            EntityCat entitycat = (EntityCat) iterator.next();

                            entitycat.eZ();
                        }

                        return false;
                    }
                }

                return true;
            }
        }

        @Override
        public void c() {}

        @Override
        public void d() {
            EntityPhantom.this.setGoalTarget((EntityLiving) null);
            EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
        }

        @Override
        public void e() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            EntityPhantom.this.c = new Vec3D(entityliving.locX(), entityliving.e(0.5D), entityliving.locZ());
            if (EntityPhantom.this.getBoundingBox().g(0.20000000298023224D).c(entityliving.getBoundingBox())) {
                EntityPhantom.this.attackEntity(entityliving);
                EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
                if (!EntityPhantom.this.isSilent()) {
                    EntityPhantom.this.world.triggerEffect(1039, EntityPhantom.this.getChunkCoordinates(), 0);
                }
            } else if (EntityPhantom.this.positionChanged || EntityPhantom.this.hurtTicks > 0) {
                EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
            }

        }
    }

    class e extends EntityPhantom.h {

        private float c;
        private float d;
        private float e;
        private float f;

        private e() {
            super();
        }

        @Override
        public boolean a() {
            return EntityPhantom.this.getGoalTarget() == null || EntityPhantom.this.bo == EntityPhantom.AttackPhase.CIRCLE;
        }

        @Override
        public void c() {
            this.d = 5.0F + EntityPhantom.this.random.nextFloat() * 10.0F;
            this.e = -4.0F + EntityPhantom.this.random.nextFloat() * 9.0F;
            this.f = EntityPhantom.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.h();
        }

        @Override
        public void e() {
            if (EntityPhantom.this.random.nextInt(350) == 0) {
                this.e = -4.0F + EntityPhantom.this.random.nextFloat() * 9.0F;
            }

            if (EntityPhantom.this.random.nextInt(250) == 0) {
                ++this.d;
                if (this.d > 15.0F) {
                    this.d = 5.0F;
                    this.f = -this.f;
                }
            }

            if (EntityPhantom.this.random.nextInt(450) == 0) {
                this.c = EntityPhantom.this.random.nextFloat() * 2.0F * 3.1415927F;
                this.h();
            }

            if (this.g()) {
                this.h();
            }

            if (EntityPhantom.this.c.y < EntityPhantom.this.locY() && !EntityPhantom.this.world.isEmpty(EntityPhantom.this.getChunkCoordinates().down(1))) {
                this.e = Math.max(1.0F, this.e);
                this.h();
            }

            if (EntityPhantom.this.c.y > EntityPhantom.this.locY() && !EntityPhantom.this.world.isEmpty(EntityPhantom.this.getChunkCoordinates().up(1))) {
                this.e = Math.min(-1.0F, this.e);
                this.h();
            }

        }

        private void h() {
            if (BlockPosition.ZERO.equals(EntityPhantom.this.d)) {
                EntityPhantom.this.d = EntityPhantom.this.getChunkCoordinates();
            }

            this.c += this.f * 15.0F * 0.017453292F;
            EntityPhantom.this.c = Vec3D.b((BaseBlockPosition) EntityPhantom.this.d).add((double) (this.d * MathHelper.cos(this.c)), (double) (-4.0F + this.e), (double) (this.d * MathHelper.sin(this.c)));
        }
    }

    abstract class h extends PathfinderGoal {

        public h() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        protected boolean g() {
            return EntityPhantom.this.c.c(EntityPhantom.this.locX(), EntityPhantom.this.locY(), EntityPhantom.this.locZ()) < 4.0D;
        }
    }

    class f extends net.pl3x.purpur.controller.ControllerLookWASD { // Purpur

        public f(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        // Purpur start
        public void tick(EntityHuman rider) {
            setYawPitch(rider.yaw, -rider.pitch * 0.75F);
        }

        @Override
        public void tick() {
            // do nothing
        }
        // Purpur end
    }

    class d extends EntityAIBodyControl {

        public d(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        public void a() {
            EntityPhantom.this.aC = EntityPhantom.this.aA;
            EntityPhantom.this.aA = EntityPhantom.this.yaw;
        }
    }

    class g extends net.pl3x.purpur.controller.ControllerMoveWASDFlying { // Purpur

        private float j = 0.1F;

        public g(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        // Purpur start
        public void tick(EntityHuman rider) {
            if (!EntityPhantom.this.onGround) {
                // phantom is always in motion when flying
                // TODO - FIX THIS
                // rider.setForward(1.0F);
            }
            super.tick(rider);
        }

        @Override
        public void tick() {
            // Purpur end
            if (EntityPhantom.this.positionChanged) {
                EntityPhantom.this.yaw += 180.0F;
                this.j = 0.1F;
            }

            float f = (float) (EntityPhantom.this.c.x - EntityPhantom.this.locX());
            float f1 = (float) (EntityPhantom.this.c.y - EntityPhantom.this.locY());
            float f2 = (float) (EntityPhantom.this.c.z - EntityPhantom.this.locZ());
            double d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d1 = 1.0D - (double) MathHelper.e(f1 * 0.7F) / d0;

            f = (float) ((double) f * d1);
            f2 = (float) ((double) f2 * d1);
            d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d2 = (double) MathHelper.c(f * f + f2 * f2 + f1 * f1);
            float f3 = EntityPhantom.this.yaw;
            float f4 = (float) MathHelper.d((double) f2, (double) f);
            float f5 = MathHelper.g(EntityPhantom.this.yaw + 90.0F);
            float f6 = MathHelper.g(f4 * 57.295776F);

            EntityPhantom.this.yaw = MathHelper.d(f5, f6, 4.0F) - 90.0F;
            EntityPhantom.this.aA = EntityPhantom.this.yaw;
            if (MathHelper.d(f3, EntityPhantom.this.yaw) < 3.0F) {
                this.j = MathHelper.c(this.j, 1.8F, 0.005F * (1.8F / this.j));
            } else {
                this.j = MathHelper.c(this.j, 0.2F, 0.025F);
            }

            float f7 = (float) (-(MathHelper.d((double) (-f1), d0) * 57.2957763671875D));

            EntityPhantom.this.pitch = f7;
            float f8 = EntityPhantom.this.yaw + 90.0F;
            double d3 = (double) (this.j * MathHelper.cos(f8 * 0.017453292F)) * Math.abs((double) f / d2);
            double d4 = (double) (this.j * MathHelper.sin(f8 * 0.017453292F)) * Math.abs((double) f2 / d2);
            double d5 = (double) (this.j * MathHelper.sin(f7 * 0.017453292F)) * Math.abs((double) f1 / d2);
            Vec3D vec3d = EntityPhantom.this.getMot();

            EntityPhantom.this.setMot(vec3d.e((new Vec3D(d3, d5, d4)).d(vec3d).a(0.2D)));
        }
    }

    static enum AttackPhase {

        CIRCLE, SWOOP;

        private AttackPhase() {}
    }
}
