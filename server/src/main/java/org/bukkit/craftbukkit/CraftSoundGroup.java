package org.bukkit.craftbukkit;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.block.SoundEffectType;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;

import java.util.HashMap;

public class CraftSoundGroup implements SoundGroup {

    private final net.minecraft.world.level.block.SoundEffectType handle;
    private static final Object2ObjectOpenHashMap<SoundEffectType, CraftSoundGroup> SOUND_GROUPS = new Object2ObjectOpenHashMap<>();

    public static SoundGroup getSoundGroup(SoundEffectType soundEffectType) {
        return SOUND_GROUPS.computeIfAbsent(soundEffectType, CraftSoundGroup::new);
    }

    private CraftSoundGroup(net.minecraft.world.level.block.SoundEffectType soundEffectType) {
        this.handle = soundEffectType;
    }

    public net.minecraft.world.level.block.SoundEffectType getHandle() {
        return handle;
    }

    @Override
    public float getVolume() {
        return getHandle().getVolume();
    }

    @Override
    public float getPitch() {
        return getHandle().getPitch();
    }

    @Override
    public Sound getBreakSound() {
        return CraftSound.getBukkit(getHandle().breakSound);
    }

    @Override
    public Sound getStepSound() {
        return CraftSound.getBukkit(getHandle().getStepSound());
    }

    @Override
    public Sound getPlaceSound() {
        return CraftSound.getBukkit(getHandle().getPlaceSound());
    }

    @Override
    public Sound getHitSound() {
        return CraftSound.getBukkit(getHandle().hitSound);
    }

    @Override
    public Sound getFallSound() {
        return CraftSound.getBukkit(getHandle().getFallSound());
    }
}
