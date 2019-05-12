package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStrollLand;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.animal.EntityTurtle;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;

public class EntityGiantZombie extends EntityMonster {

    public EntityGiantZombie(EntityTypes<? extends EntityGiantZombie> entitytypes, World world) {
        super(entitytypes, world);
        // Purpur start
        this.safeFallDistance = 10.0F;
        setStepHeight(world.purpurConfig.giantStepHeight);
        // Purpur end
    }

    // Purpur start
    @Override
    protected void initPathfinder() {
        if (world.purpurConfig.giantHaveAI) {
            this.goalSelector.a(0, new PathfinderGoalFloat(this));
            this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
            this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));
            this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
            this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
            if (world.purpurConfig.giantHaveHostileAI) {
                this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
                this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this).a(EntityPigZombie.class));
                this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
                this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillager.class, false));
                this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
                this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityTurtle.class, true));
            }
        }
    }

    @Override
    protected void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.giantMaxHealth);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.world.purpurConfig.giantMovementSpeed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.world.purpurConfig.giantAttackDamage);
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @javax.annotation.Nullable GroupDataEntity groupdataentity, @javax.annotation.Nullable NBTTagCompound nbttagcompound) {
        GroupDataEntity groupData = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
        if (groupData == null) {
            setEquipmentBasedOnDifficulty(difficultydamagescaler);
            setEnchantmentBasedOnDifficulty(difficultydamagescaler);
        }
        return groupData;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyDamageScaler difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        // TODO make configurable
        if (random.nextFloat() < (world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.05F)) {
            setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
    }

    @Override
    public float getJumpHeight() {
        // make giants jump as high as everything else relative to their size
        // 1.0 makes bottom of feet about as high as their waist when they jump
        return world.purpurConfig.giantJumpHeight;
    }
    // Purpur end

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 10.440001F;
    }

    public static AttributeProvider.Builder m() {
        return EntityMonster.eR().a(GenericAttributes.MAX_HEALTH, 100.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.5D).a(GenericAttributes.ATTACK_DAMAGE, 50.0D);
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return super.a(blockposition, iworldreader); // Purpur - fix light requirements for natural spawns
    }
}
