package net.pl3x.purpur.entity;

import net.minecraft.core.particles.Particles;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityDolphin;
import net.minecraft.world.entity.projectile.EntityLlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileHelper;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.MovingObjectPositionEntity;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class DolphinSpit extends EntityLlamaSpit {
    public EntityLiving dolphin;
    public int ticksLived;

    public DolphinSpit(EntityTypes<? extends EntityLlamaSpit> entitytypes, World world) {
        super(entitytypes, world);
    }

    public DolphinSpit(World world, EntityDolphin dolphin) {
        this(EntityTypes.LLAMA_SPIT, world);
        setShooter(dolphin.hasPurpurRider() ? dolphin.getPurpurRider() : dolphin);
        this.dolphin = dolphin;
        this.setPosition(
                dolphin.locX() - (double) (dolphin.getWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(dolphin.getRenderYawOffset() * 0.017453292F),
                dolphin.getHeadY() - 0.10000000149011612D,
                dolphin.locZ() + (double) (dolphin.getWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(dolphin.getRenderYawOffset() * 0.017453292F));
    }

    @Override
    public boolean canSaveToDisk() {
        return false;
    }

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

        double x = this.locX() + mot.x;
        double y = this.locY() + mot.y;
        double z = this.locZ() + mot.z;
        setMot(mot.scale(0.99D));

        Vec3D motDouble = mot.scale(2.0);
        for (int i = 0; i < 5; i++) {
            ((WorldServer) world).sendParticles(null, Particles.BUBBLE,
                    locX() + random.nextFloat() / 2 - 0.25F,
                    locY() + random.nextFloat() / 2 - 0.25F,
                    locZ() + random.nextFloat() / 2 - 0.25F,
                    0, motDouble.getX(), motDouble.getY(), motDouble.getZ(), 0.1, true);
        }

        if (++ticksLived > 20) {
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
            rayTrace.getEntity().damageEntity(DamageSource.indirectMobAttack(this, (EntityLiving) shooter).setProjectile(), world.purpurConfig.dolphinSpitDamage);
        }
    }

    protected void onHit(MovingObjectPositionBlock rayTrace) {
        IBlockData iblockdata = world.getType(rayTrace.getBlockPosition());
        iblockdata.a(world, iblockdata, rayTrace, this);
        die();
    }
}
