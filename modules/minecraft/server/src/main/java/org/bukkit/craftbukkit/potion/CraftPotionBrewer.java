package org.bukkit.craftbukkit.potion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.PotionRegistry;
import org.bukkit.potion.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CraftPotionBrewer implements PotionBrewer {
    private static final Map<Integer, Collection<PotionEffect>> cache = Maps.newHashMap(); // Paper

    @Override
    public Collection<PotionEffect> getEffects(PotionType damage, boolean upgraded, boolean extended) {
        // Paper start
        int key = damage.ordinal() << 2;
        key |= (upgraded ? 1 : 0) << 1;
        key |= extended ? 1 : 0;

        if (cache.containsKey(key))
            return cache.get(key);
        // Paper end

        List<MobEffect> mcEffects = PotionRegistry.a(CraftPotionUtil.fromBukkit(new PotionData(damage, extended, upgraded))).a();

        ImmutableList.Builder<PotionEffect> builder = new ImmutableList.Builder<PotionEffect>();
        for (MobEffect effect : mcEffects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }

        cache.put(key, builder.build()); // Paper

        return cache.get(key); // Paper
    }

    @Override
    public Collection<PotionEffect> getEffectsFromDamage(int damage) {
        return new ObjectArrayList<>();
    }

    @Override
    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : (int) (duration * potion.getDurationModifier()), amplifier);
    }
}
