package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPosition;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;

import java.util.Random;

public class EntityZombieHusk extends EntityZombie {

    public EntityZombieHusk(EntityTypes<? extends EntityZombieHusk> entitytypes, World world) {
        super(entitytypes, world);
        this.setShouldBurnInDay(false); // Purpur
    }

    // Purpur start
    @Override
    public boolean isRidable() {
        return world.purpurConfig.huskRidable;
    }

    @Override
    public boolean isRidableInWater() {
        return world.purpurConfig.huskRidableInWater;
    }

    @Override
    public boolean jockeyOnlyBaby() {
        return world.purpurConfig.huskJockeyOnlyBaby;
    }

    @Override
    public double jockeyChance() {
        return world.purpurConfig.huskJockeyChance;
    }

    @Override
    public boolean jockeyTryExistingChickens() {
        return world.purpurConfig.huskJockeyTryExistingChickens;
    }

    @Override
    public void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.huskMaxHealth);
    }

    protected void generateReinforcementsChance() {
        this.getAttributeInstance(GenericAttributes.SPAWN_REINFORCEMENTS).setValue(this.random.nextDouble() * this.world.purpurConfig.huskSpawnReinforcements);
    }
    // Purpur end

    public static boolean a(EntityTypes<EntityZombieHusk> entitytypes, WorldAccess worldaccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return b(entitytypes, worldaccess, enummobspawn, blockposition, random) && (enummobspawn == EnumMobSpawn.SPAWNER || worldaccess.e(blockposition));
    }

    @Override
    protected boolean T_() {
        return this.shouldBurnInDay; // Purpur - moved to EntityLiving - keep methods for ABI compatibility
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_HUSK_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_HUSK_DEATH;
    }

    @Override
    protected SoundEffect getSoundStep() {
        return SoundEffects.ENTITY_HUSK_STEP;
    }

    @Override
    public boolean attackEntity(Entity entity) {
        boolean flag = super.attackEntity(entity);

        if (flag && this.getItemInMainHand().isEmpty() && entity instanceof EntityLiving) {
            float f = this.world.getDamageScaler(this.getChunkCoordinates()).b();

            ((EntityLiving) entity).addEffect(new MobEffect(MobEffects.HUNGER, 140 * (int) f), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.ATTACK); // CraftBukkit
        }

        return flag;
    }

    @Override
    protected boolean eN() {
        return true;
    }

    @Override
    protected void eP() {
        this.b(EntityTypes.ZOMBIE);
        if (!this.isSilent()) {
            this.world.a((EntityHuman) null, 1041, this.getChunkCoordinates(), 0);
        }

    }

    @Override
    protected ItemStack eM() {
        return ItemStack.b;
    }
}
