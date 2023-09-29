package org.bukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is damaged by an entity
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {
    private final Entity damager;
    private final boolean isCritical; // Purpur

    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, final double damage) {
        // Purpur start
        this(damager, damagee, cause, damage, false);
    }
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, final double damage, boolean isCritical) {
        // Purpur end
        super(damagee, cause, damage);
        this.damager = damager;
        this.isCritical = isCritical; // Purpur
    }

    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        // Purpur start
        this(damager, damagee, cause, modifiers, modifierFunctions, false);
    }
    public EntityDamageByEntityEvent(@NotNull final Entity damager, @NotNull final Entity damagee, @NotNull final DamageCause cause, @NotNull final Map<DamageModifier, Double> modifiers, @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions, boolean isCritical) {
        // Purpur end
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
        this.isCritical = isCritical; // Purpur
    }

    /**
     * Returns the entity that damaged the defender.
     *
     * @return Entity that damaged the defender.
     */
    @NotNull
    public Entity getDamager() {
        return damager;
    }

    // Purpur start

    /**
     * Whether this damage was done by a critical hit
     *
     * @return True if critical hit
     */
    public boolean isCritical() {
        return this.isCritical;
    }
    // Purpur end
}
