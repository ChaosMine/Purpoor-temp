package net.minecraft.world.entity.animal;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.particles.Particles;
import net.minecraft.server.PathfinderGoalHasRider;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemLiquidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
// CraftBukkit end

public class EntityCow extends EntityAnimal {

    public EntityCow(EntityTypes<? extends EntityCow> entitytypes, World world) {
        super(entitytypes, world);
    }

    // Purpur start
    @Override
    public boolean isRidable() {
        return world.purpurConfig.cowRidable;
    }

    @Override
    public boolean isRidableInWater() {
        return world.purpurConfig.cowRidableInWater;
    }

    @Override
    public int getPurpurBreedTime() {
        return this.world.purpurConfig.cowBreedingTicks;
    }

    @Override
    public void initAttributes() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(this.world.purpurConfig.cowMaxHealth);
    }
    // Purpur end

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(0, new PathfinderGoalHasRider(this)); // Purpur
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 2.0D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        if (world.purpurConfig.cowFeedMushrooms > 0) this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.25D, RecipeItemStack.a(Items.WHEAT, Blocks.RED_MUSHROOM.getItem(), Blocks.BROWN_MUSHROOM.getItem()), false)); else // Purpur
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.25D, RecipeItemStack.a(Items.WHEAT), false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
        this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    public static AttributeProvider.Builder eK() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_COW_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_COW_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        if (hasPurpurRider()) return EnumInteractionResult.PASS; // Purpur
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.BUCKET && !this.isBaby()) {
            // CraftBukkit start - Got milk?
            org.bukkit.event.player.PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent((WorldServer) entityhuman.world, entityhuman, this.getChunkCoordinates(), this.getChunkCoordinates(), null, itemstack, Items.MILK_BUCKET, enumhand); // Paper - add enumHand

            if (event.isCancelled()) {
                return tryRide(entityhuman, enumhand); // Purpur
            }
            // CraftBukkit end

            entityhuman.playSound(SoundEffects.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemLiquidUtil.a(itemstack, entityhuman, CraftItemStack.asNMSCopy(event.getItemStack())); // CraftBukkit

            entityhuman.a(enumhand, itemstack1);
            return EnumInteractionResult.a(this.world.isClientSide);
        // Purpur start - feed mushroom to change to mooshroom
        } else if (world.purpurConfig.cowFeedMushrooms > 0 && getEntityType() != EntityTypes.MOOSHROOM && isMushroom(itemstack)) {
            return feedMushroom(entityhuman, enumhand, itemstack);
        // Purpur end
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    // Purpur start - feed mushroom to change to mooshroom
    private int redMushroomsFed = 0;
    private int brownMushroomsFed = 0;

    private boolean isMushroom(ItemStack itemstack) {
        return itemstack.getItem() == Blocks.RED_MUSHROOM.getItem() || itemstack.getItem() == Blocks.BROWN_MUSHROOM.getItem();
    }

    private int incrementFeedCount(ItemStack itemstack) {
        if (itemstack.getItem() == Blocks.RED_MUSHROOM.getItem()) {
            return ++redMushroomsFed;
        } else {
            return ++brownMushroomsFed;
        }
    }

    private EnumInteractionResult feedMushroom(EntityHuman entityhuman, EnumHand enumhand, ItemStack itemstack) {
        world.broadcastEntityEffect(this, (byte) 18); // hearts
        playSound(SoundEffects.ENTITY_COW_MILK, 1.0F, 1.0F);
        if (incrementFeedCount(itemstack) < world.purpurConfig.cowFeedMushrooms) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
            return EnumInteractionResult.CONSUME; // require 5 mushrooms to transform (prevents mushroom duping)
        }
        EntityMushroomCow mooshroom = EntityTypes.MOOSHROOM.create(world);
        if (mooshroom == null) {
            return tryRide(entityhuman, enumhand);
        }
        if (itemstack.getItem() == Blocks.BROWN_MUSHROOM.getItem()) {
            mooshroom.setVariant(EntityMushroomCow.Type.BROWN);
        } else {
            mooshroom.setVariant(EntityMushroomCow.Type.RED);
        }
        mooshroom.setPositionRotation(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
        mooshroom.setHealth(this.getHealth());
        mooshroom.setAge(getAge());
        mooshroom.copyPositionRotation(this);
        mooshroom.setRenderYawOffset(this.getRenderYawOffset());
        mooshroom.setHeadRotation(this.getHeadRotation());
        mooshroom.lastYaw = this.lastYaw;
        mooshroom.lastPitch = this.lastPitch;
        if (this.hasCustomName()) {
            mooshroom.setCustomName(this.getCustomName());
        }
        if (CraftEventFactory.callEntityTransformEvent(this, mooshroom, org.bukkit.event.entity.EntityTransformEvent.TransformReason.INFECTION).isCancelled()) {
            return tryRide(entityhuman, enumhand);
        }
        if (!new com.destroystokyo.paper.event.entity.EntityTransformedEvent(this.getBukkitEntity(), mooshroom.getBukkitEntity(), com.destroystokyo.paper.event.entity.EntityTransformedEvent.TransformedReason.INFECTED).callEvent()) {
            return tryRide(entityhuman, enumhand);
        }
        this.world.addEntity(mooshroom);
        this.die();
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }
        for (int i = 0; i < 15; ++i) {
            ((WorldServer) world).sendParticles(((WorldServer) world).players, null, Particles.HAPPY_VILLAGER,
                    locX() + random.nextFloat(), locY() + (random.nextFloat() * 2), locZ() + random.nextFloat(), 1,
                    random.nextGaussian() * 0.05D, random.nextGaussian() * 0.05D, random.nextGaussian() * 0.05D, 0, true);
        }
        return EnumInteractionResult.SUCCESS;
    }
    // Purpur end

    @Override
    public EntityCow createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityCow) EntityTypes.COW.a((World) worldserver);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return this.isBaby() ? entitysize.height * 0.95F : 1.3F;
    }
}
