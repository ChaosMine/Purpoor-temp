package org.spigotmc;

import net.minecraft.SystemUtils;

public class TickLimiter {

    private final int maxTime;
    private long startTime;

    public TickLimiter(int maxtime) {
        this.maxTime = maxtime;
    }

    public void initTick() {
        startTime = SystemUtils.getMonotonicMillis();
    }

    public boolean shouldContinue() {
        long remaining = SystemUtils.getMonotonicMillis() - startTime;
        return remaining < maxTime;
    }
}
