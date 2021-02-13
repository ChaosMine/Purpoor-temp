package net.minecraft.world.entity.boss.enderdragon;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityPhantom;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockFireAbstract;
import net.minecraft.world.level.dimension.end.EnderDragonBattle;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityEnderCrystal extends Entity {

    private static final DataWatcherObject<Optional<BlockPosition>> c = DataWatcher.a(EntityEnderCrystal.class, DataWatcherRegistry.m);
    private static final DataWatcherObject<Boolean> d = DataWatcher.a(EntityEnderCrystal.class, DataWatcherRegistry.i);
    public int b;
    public boolean generatedByDragonFight = false; // Paper - Fix invulnerable end crystals
    // Purpur start
    private EntityPhantom targetPhantom;
    private int phantomBeamTicks = 0;
    private int phantomDamageCooldown = 0;
    private int idleCooldown = 0;
    // Purpur end

    public EntityEnderCrystal(EntityTypes<? extends EntityEnderCrystal> entitytypes, World world) {
        super(entitytypes, world);
        this.i = true;
        this.b = this.random.nextInt(100000);
    }

    public EntityEnderCrystal(World world, double d0, double d1, double d2) {
        this(EntityTypes.END_CRYSTAL, world);
        this.setPosition(d0, d1, d2);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected void initDatawatcher() {
        this.getDataWatcher().register(EntityEnderCrystal.c, Optional.empty());
        this.getDataWatcher().register(EntityEnderCrystal.d, true);
    }

    @Override
    public void tick() {
        ++this.b;
        if (this.world instanceof WorldServer) {
            BlockPosition blockposition = this.getChunkCoordinates();

            if (((WorldServer) this.world).getDragonBattle() != null && this.world.getType(blockposition).isAir()) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(this.world, blockposition, this).isCancelled()) {
                    this.world.setTypeUpdate(blockposition, BlockFireAbstract.a((IBlockAccess) this.world, blockposition));
                }
                // CraftBukkit end
            }
            // Paper start - Fix invulnerable end crystals
            if (this.world.paperConfig.fixInvulnerableEndCrystalExploit && this.generatedByDragonFight && this.isInvulnerable()) {
                if ((this.origin.getWorld() != null && !((WorldServer) this.world).uuid.equals(this.origin.getWorld().getUID()))
                    || ((WorldServer) this.world).getDragonBattle() == null
                    || ((WorldServer) this.world).getDragonBattle().respawnPhase == null
                    || ((WorldServer) this.world).getDragonBattle().respawnPhase.ordinal() > net.minecraft.world.level.dimension.end.EnumDragonRespawn.SUMMONING_DRAGON.ordinal()) {
                    this.setInvulnerable(false);
                    this.setBeamTarget(null);
                }
            }
            // Paper end
        }

        // Purpur start
        if (world.purpurConfig.phantomAttackedByCrystalRadius <= 0 || --idleCooldown > 0) {
            return; // on cooldown
        }

        if (targetPhantom == null) {
            for (EntityPhantom phantom : world.getEntitiesInAABB(EntityPhantom.class, getBoundingBox().grow(world.purpurConfig.phantomAttackedByCrystalRadius))) {
                if (phantom.hasLineOfSight(this)) {
                    attackPhantom(phantom);
                    break;
                }
            }
        } else {
            setBeamTarget(new BlockPosition(targetPhantom).add(0, -2, 0));
            if (--phantomBeamTicks > 0 && targetPhantom.isAlive()) {
                phantomDamageCooldown--;
                if (targetPhantom.hasLineOfSight(this)) {
                    if (phantomDamageCooldown <= 0) {
                        phantomDamageCooldown = 20;
                        targetPhantom.damageEntity(DamageSource.indirectMagic(this, this), world.purpurConfig.phantomAttackedByCrystalDamage);
                    }
                } else {
                    forgetPhantom(); // no longer in sight
                }
            } else {
                forgetPhantom(); // attacked long enough
            }
        }
    }

    private void attackPhantom(EntityPhantom phantom) {
        phantomDamageCooldown = 0;
        phantomBeamTicks = 60;
        targetPhantom = phantom;
    }

    private void forgetPhantom() {
        targetPhantom = null;
        setBeamTarget(null);
        phantomBeamTicks = 0;
        phantomDamageCooldown = 0;
        idleCooldown = 60;
    }

    public boolean shouldExplode() {
        return isShowingBottom() ? world.purpurConfig.baseCrystalExplode : world.purpurConfig.baselessCrystalExplode;
    }

    public float getExplosionPower() {
        return (float) (isShowingBottom() ? world.purpurConfig.baseCrystalExplosionPower : world.purpurConfig.baselessCrystalExplosionPower);
    }

    public boolean hasExplosionFire() {
        return isShowingBottom() ? world.purpurConfig.baseCrystalExplosionFire : world.purpurConfig.baselessCrystalExplosionFire;
    }

    public Explosion.Effect getExplosionEffect() {
        return isShowingBottom() ? world.purpurConfig.baseCrystalExplosionEffect : world.purpurConfig.baselessCrystalExplosionEffect;
    }
    // Purpur end

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        if (this.getBeamTarget() != null) {
            nbttagcompound.set("BeamTarget", GameProfileSerializer.a(this.getBeamTarget()));
        }

        nbttagcompound.setBoolean("ShowBottom", this.isShowingBottom());
        if (this.generatedByDragonFight) nbttagcompound.setBoolean("Paper.GeneratedByDragonFight", this.generatedByDragonFight); // Paper - Fix invulnerable end crystals
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("BeamTarget", 10)) {
            this.setBeamTarget(GameProfileSerializer.b(nbttagcompound.getCompound("BeamTarget")));
        }

        if (nbttagcompound.hasKeyOfType("ShowBottom", 1)) {
            this.setShowingBottom(nbttagcompound.getBoolean("ShowBottom"));
        }
        if (nbttagcompound.hasKeyOfType("Paper.GeneratedByDragonFight", 1)) this.generatedByDragonFight = nbttagcompound.getBoolean("Paper.GeneratedByDragonFight"); // Paper - Fix invulnerable end crystals

    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (damagesource.getEntity() instanceof EntityEnderDragon) {
            return false;
        } else {
            if (!this.dead && !this.world.isClientSide) {
                // CraftBukkit start - All non-living entities need this
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f, false)) {
                    return false;
                }
                // CraftBukkit end
                this.die();
                if (!damagesource.isExplosion()) {
                    if (shouldExplode()) { // Purpur
                    // CraftBukkit start
                    ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), getExplosionPower(), hasExplosionFire()); // Purpur
                    this.world.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        this.dead = false;
                        return false;
                    }
                    this.world.createExplosion(this, this.locX(), this.locY(), this.locZ(), event.getRadius(), event.getFire(), getExplosionEffect()); // Purpur
                    // CraftBukkit end
                    } else this.dead = false; // Purpur
                }

                this.a(damagesource);
            }

            return true;
        }
    }

    @Override
    public void killEntity() {
        this.a(DamageSource.GENERIC);
        super.killEntity();
    }

    private void a(DamageSource damagesource) {
        if (this.world instanceof WorldServer) {
            EnderDragonBattle enderdragonbattle = ((WorldServer) this.world).getDragonBattle();

            if (enderdragonbattle != null) {
                enderdragonbattle.a(this, damagesource);
            }
        }

    }

    public void setBeamTarget(@Nullable BlockPosition blockposition) {
        this.getDataWatcher().set(EntityEnderCrystal.c, Optional.ofNullable(blockposition));
    }

    @Nullable
    public BlockPosition getBeamTarget() {
        return (BlockPosition) ((Optional) this.getDataWatcher().get(EntityEnderCrystal.c)).orElse((Object) null);
    }

    public void setShowingBottom(boolean flag) {
        this.getDataWatcher().set(EntityEnderCrystal.d, flag);
    }

    public boolean isShowingBottom() {
        return (Boolean) this.getDataWatcher().get(EntityEnderCrystal.d);
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }
}
