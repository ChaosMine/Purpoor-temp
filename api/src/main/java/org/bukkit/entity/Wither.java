package org.bukkit.entity;

import com.destroystokyo.paper.entity.RangedEntity;

/**
 * Represents a Wither boss
 */
public interface Wither extends Monster, Boss, RangedEntity { // Paper
    // Paper start
    /**
     * @return whether the wither is charged
     */
    boolean isCharged();

    /**
     * @return ticks the wither is invulnerable for
     */
    int getInvulnerableTicks();

    /**
     * Sets for how long in the future, the wither should be invulnerable.
     *
     * @param ticks ticks the wither is invulnerable for
     */
    void setInvulnerableTicks(int ticks);

    /**
     * @return whether the wither can travel through portals
     */
    boolean canTravelThroughPortals();

    /**
     * Sets whether the wither can travel through portals.
     *
     * @param value whether the wither can travel through portals
     */
    void setCanTravelThroughPortals(boolean value);
    // Paper end
}
