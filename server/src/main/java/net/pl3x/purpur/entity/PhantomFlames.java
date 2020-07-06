package net.pl3x.purpur.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityPhantom;
import net.minecraft.world.entity.projectile.EntityLlamaSpit;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.util.MathHelper;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.MovingObjectPositionEntity;
import net.minecraft.core.particles.Particles;
import net.minecraft.world.entity.projectile.ProjectileHelper;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.level.World;
import net.minecraft.server.level.WorldServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class PhantomFlames extends EntityLlamaSpit {
    public EntityPhantom phantom;
    public int ticksLived;
    public boolean canGrief = false;

    public PhantomFlames(EntityTypes<? extends EntityLlamaSpit> entitytypes, World world) {
        super(entitytypes, world);
    }

    public PhantomFlames(World world, EntityPhantom phantom) {
        this(EntityTypes.LLAMA_SPIT, world);
        setShooter(phantom.hasPurpurRider() ? phantom.getPurpurRider() : phantom);
        this.phantom = phantom;
        this.setPosition(
                phantom.locX() - (double) (phantom.getWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(phantom.getRenderYawOffset() * 0.017453292F),
                phantom.getHeadY() - 0.10000000149011612D,
                phantom.locZ() + (double) (phantom.getWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(phantom.getRenderYawOffset() * 0.017453292F));
    }

    @Override
    public boolean canSaveToDisk() {
        return false;
    }

    @Override
    public void tick() {
        if (dead || !valid) {
            return;
        }

        if (!leftOwner()) {
            setLeftOwner(checkIfLeftOwner());
        }

        setFlag(6, isGlowing());
        entityBaseTick();

        Vec3D mot = getMot();

        MovingObjectPosition hitResult = ProjectileHelper.getHitResult(this, this::hitPredicate);
        if (hitResult != null) {
            onHit(hitResult);
        }

        if (dead) {
            return;
        }

        double x = this.locX() + mot.x;
        double y = this.locY() + mot.y;
        double z = this.locZ() + mot.z;
        setMot(mot.scale(0.99D));

        Vec3D motDouble = mot.scale(2.0);
        for (int i = 0; i < 5; i++) {
            ((WorldServer) world).sendParticles(null, Particles.FLAME,
                    locX() + random.nextFloat() / 2 - 0.25F,
                    locY() + random.nextFloat() / 2 - 0.25F,
                    locZ() + random.nextFloat() / 2 - 0.25F,
                    0, motDouble.getX(), motDouble.getY(), motDouble.getZ(), 0.1, true);
        }

        if (world.a(getBoundingBox()).noneMatch(BlockBase.BlockData::isAir)) {
            die();
        } else if (isInWaterOrBubbleColumn()) {
            die();
        } else if (++ticksLived > 20) {
            die();
        } else {
            setMot(mot.scale(0.99D));
            if (!isNoGravity()) {
                setMot(getMot().add(0.0D, -0.06D, 0.0D));
            }
            setPosition(x, y, z);
        }
    }

    @Override
    public void shoot(double x, double y, double z, float speed, float inaccuracy) {
        setMot(new Vec3D(x, y, z).normalize().add(
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
                .scale(speed));
    }

    public void onHit(MovingObjectPosition rayTrace) {
        CraftEventFactory.callProjectileHitEvent(this, rayTrace);
        MovingObjectPosition.EnumMovingObjectType type = rayTrace.getType();
        if (type == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
            onHit((MovingObjectPositionEntity) rayTrace);
        } else if (type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            onHit((MovingObjectPositionBlock) rayTrace);
        }
    }

    protected void onHit(MovingObjectPositionEntity rayTrace) {
        Entity shooter = getShooter();
        if (shooter instanceof EntityLiving) {
            Entity target = rayTrace.getEntity();
            if (canGrief || (target instanceof EntityLiving && !(target instanceof EntityArmorStand))) {
                target.damageEntity(DamageSource.indirectMobAttack(this, (EntityLiving) shooter).setProjectile(), world.purpurConfig.phantomFlameDamage);
                if (world.purpurConfig.phantomFlameFireTime > 0) {
                    target.setOnFire(world.purpurConfig.phantomFlameFireTime);
                }
            }
        }
        die();
    }

    protected void onHit(MovingObjectPositionBlock rayTrace) {
        if (canGrief) {
            IBlockData iblockdata = world.getType(rayTrace.getBlockPosition());
            iblockdata.a(world, iblockdata, rayTrace, this);
        }
        die();
    }
}
