package net.minecraft.world.entity.monster;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.PathfinderGoalHasRider;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.entity.animal.EntityOcelot;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.Collection;
import java.util.Iterator;
// CraftBukkit end

public class EntityCreeper extends EntityMonster {

    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> POWERED = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> d = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.i); private static final DataWatcherObject<Boolean> isIgnitedDW = d; // Paper OBFHELPER
    private int bo;
    public int fuseTicks; // PAIL
    public int maxFuseTicks = 30;
    public int explosionRadius = 3;
    private int bs;
    // Purpur start
    private int spacebarCharge = 0;
    private int prevSpacebarCharge = 0;
    private int powerToggleDelay = 0;
    private boolean exploding = false;
    // Purpur end

    public EntityCreeper(EntityTypes<? extends EntityCreeper> entitytypes, World world) {
        super(entitytypes, world);
    }

    // Purpur start
    @Override
    public boolean isRidable() {
        return world.purpurConfig.creeperRidable;
    }

    @Override
    public boolean isRidableInWater() {
        return world.purpurConfig.creeperRidableInWater;
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @javax.annotation.Nullable GroupDataEntity groupdataentity, @javax.annotation.Nullable NBTTagCompound nbttagcompound) {
        double chance = worldaccess.getMinecraftWorld().purpurConfig.creeperChargedChance;
        if (chance > 0D && random.nextDouble() <= chance) {
            setPowered(true);
        }
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    protected void mobTick() {
        if (powerToggleDelay > 0) {
            powerToggleDelay--;
        }
        if (hasPurpurRider()) {
            if (getPurpurRider().getForward() != 0 || getPurpurRider().getStrafe() != 0) {
                spacebarCharge = 0;
                setIgnited(false);
            }
            if (spacebarCharge == prevSpacebarCharge) {
                spacebarCharge = 0;
            }
            prevSpacebarCharge = spacebarCharge;
        }
        super.mobTick();
    }

    @Override
    public void onMount(EntityHuman entityhuman) {
        super.onMount(entityhuman);
        setIgnited(false);
    }

    @Override
    public boolean onSpacebar() {
        if (powerToggleDelay > 0) {
            return true; // just toggled power, do not jump or ignite
        }
        spacebarCharge++;
        if (spacebarCharge > maxFuseTicks - 2) {
            spacebarCharge = 0;
            if (getPurpurRider().getBukkitEntity().hasPermission("allow.powered.creeper")) {
                powerToggleDelay = 20;
                setPowered(!isPowered());
                setIgnited(false);
                return true;
            }
        }
        if (!isIgnited()) {
            if (hasPurpurRider() && getPurpurRider().getForward() == 0 && getPurpurRider().getStrafe() == 0 &&
                    getPurpurRider().getBukkitEntity().hasPermission("allow.special.creeper")) {
                setIgnited(true);
                return true;
            }
        }
        return getForward() == 0 && getStrafe() == 0; // do not jump if standing still
    }

    @Override
    public void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.creeperMaxHealth);
    }

    protected org.bukkit.event.entity.EntityDeathEvent d(DamageSource damagesource) {
        if (!exploding && this.world.purpurConfig.creeperExplodeWhenKilled && damagesource.getEntity() instanceof EntityPlayer) {
            this.explode();
        }

        return super.d(damagesource);
    }
    // Purpur end

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalSwell(this));
        this.goalSelector.a(3, new PathfinderGoalHasRider(this)); // Purpur
        this.goalSelector.a(3, new PathfinderGoalAvoidTarget<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.a(3, new PathfinderGoalAvoidTarget<>(this, EntityCat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.8D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(0, new PathfinderGoalHasRider(this)); // Purpur
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, new Class[0]));
    }

    public static AttributeProvider.Builder m() {
        return EntityMonster.eR().a(GenericAttributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public int bP() {
        return this.getGoalTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }

    @Override
    public boolean b(float f, float f1) {
        boolean flag = super.b(f, f1);

        this.fuseTicks = (int) ((float) this.fuseTicks + f * 1.5F);
        if (this.fuseTicks > this.maxFuseTicks - 5) {
            this.fuseTicks = this.maxFuseTicks - 5;
        }

        return flag;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityCreeper.b, -1);
        this.datawatcher.register(EntityCreeper.POWERED, false);
        this.datawatcher.register(EntityCreeper.d, false);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if ((Boolean) this.datawatcher.get(EntityCreeper.POWERED)) {
            nbttagcompound.setBoolean("powered", true);
        }

        nbttagcompound.setShort("Fuse", (short) this.maxFuseTicks);
        nbttagcompound.setByte("ExplosionRadius", (byte) this.explosionRadius);
        nbttagcompound.setBoolean("ignited", this.isIgnited());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.datawatcher.set(EntityCreeper.POWERED, nbttagcompound.getBoolean("powered"));
        if (nbttagcompound.hasKeyOfType("Fuse", 99)) {
            this.maxFuseTicks = nbttagcompound.getShort("Fuse");
        }

        if (nbttagcompound.hasKeyOfType("ExplosionRadius", 99)) {
            this.explosionRadius = nbttagcompound.getByte("ExplosionRadius");
        }

        if (nbttagcompound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.bo = this.fuseTicks;
            if (this.isIgnited()) {
                this.a(1);
            }

            int i = this.eK();

            if (i > 0 && this.fuseTicks == 0) {
                this.playSound(SoundEffects.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.fuseTicks += i;
            if (this.fuseTicks < 0) {
                this.fuseTicks = 0;
            }

            if (this.fuseTicks >= this.maxFuseTicks) {
                this.fuseTicks = this.maxFuseTicks;
                this.explode();
            }
        }

        super.tick();
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_CREEPER_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_CREEPER_DEATH;
    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropDeathLoot(damagesource, i, flag);
        Entity entity = damagesource.getEntity();

        if (entity != this && entity instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) entity;

            if (entitycreeper.canCauseHeadDrop()) {
                entitycreeper.setCausedHeadDrop();
                this.a((IMaterial) Items.CREEPER_HEAD);
            }
        }

    }

    @Override
    public boolean attackEntity(Entity entity) {
        return true;
    }

    public boolean isPowered() {
        return (Boolean) this.datawatcher.get(EntityCreeper.POWERED);
    }

    public int eK() {
        return (Integer) this.datawatcher.get(EntityCreeper.b);
    }

    public void setSwellDirection(int i) { a(i); } // Purpur - OBFHELPER
    public void a(int i) {
        this.datawatcher.set(EntityCreeper.b, i);
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        super.onLightningStrike(worldserver, entitylightning);
        // CraftBukkit start
        if (CraftEventFactory.callCreeperPowerEvent(this, entitylightning, org.bukkit.event.entity.CreeperPowerEvent.PowerCause.LIGHTNING).isCancelled()) {
            return;
        }

        this.setPowered(true);
    }

    public void setPowered(boolean powered) {
        this.datawatcher.set(EntityCreeper.POWERED, powered);
    }
    // CraftBukkit end

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(entityhuman, this.locX(), this.locY(), this.locZ(), SoundEffects.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.world.isClientSide) {
                this.ignite();
                itemstack.damage(1, entityhuman, (entityhuman1) -> {
                    entityhuman1.broadcastItemBreak(enumhand);
                });
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    public void explode() {
        this.exploding = true; // Purpur
        if (!this.world.isClientSide) {
            Explosion.Effect explosion_effect = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && world.purpurConfig.creeperAllowGriefing ? Explosion.Effect.DESTROY : Explosion.Effect.NONE; // Purpur
            float f = this.isPowered() ? 2.0F : 1.0F;
            float multiplier = this.world.purpurConfig.creeperHealthRadius ? this.getHealth() / this.getMaxHealth() : 1; // Purpur

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), multiplier * (this.explosionRadius * f), false); // Purpur
            this.world.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.killed = true;
                this.world.createExplosion(this, this.locX(), this.locY(), this.locZ(), event.getRadius(), event.getFire(), explosion_effect);
                this.die();
                this.createEffectCloud();
            } else {
                fuseTicks = 0;
                this.datawatcher.set(isIgnitedDW, Boolean.valueOf(false)); // Paper
            }
            // CraftBukkit end
        }

        this.exploding = false; // Purpur
    }

    private void createEffectCloud() {
        Collection<MobEffect> collection = this.getEffects();

        if (!collection.isEmpty() && !world.paperConfig.disableCreeperLingeringEffect) { // Paper
            EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.locX(), this.locY(), this.locZ());

            entityareaeffectcloud.setSource(this); // CraftBukkit
            entityareaeffectcloud.setRadius(2.5F);
            entityareaeffectcloud.setRadiusOnUse(-0.5F);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
            entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                entityareaeffectcloud.addEffect(new MobEffect(mobeffect));
            }

            this.world.addEntity(entityareaeffectcloud, CreatureSpawnEvent.SpawnReason.EXPLOSION); // CraftBukkit
        }

    }

    public boolean isIgnited() {
        return (Boolean) this.datawatcher.get(EntityCreeper.d);
    }

    public void ignite() {
        // Paper start
        setIgnited(true);
    }

    public void setIgnited(boolean ignited) {
        if (isIgnited() != ignited) {
            com.destroystokyo.paper.event.entity.CreeperIgniteEvent event = new com.destroystokyo.paper.event.entity.CreeperIgniteEvent((org.bukkit.entity.Creeper) getBukkitEntity(), ignited);
            if (event.callEvent()) {
                this.datawatcher.set(EntityCreeper.d, event.isIgnited());
                if (!event.isIgnited()) setSwellDirection(-1); // Purpur
            }
        }
        // Paper end
    }

    public boolean canCauseHeadDrop() {
        return this.isPowered() && this.bs < 1;
    }

    public void setCausedHeadDrop() {
        ++this.bs;
    }
}
