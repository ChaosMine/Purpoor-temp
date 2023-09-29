package net.minecraft.stats;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.EntityHuman;
import net.pl3x.purpur.PurpurConfig;

public class StatisticManager {

    protected final Object2IntMap<Statistic<?>> a;

    public StatisticManager() {
        if (PurpurConfig.disableStatistics) {
            a = null;
        } else {
            this.a = Object2IntMaps.synchronize(new Object2IntOpenHashMap<>());
            this.a.defaultReturnValue(0);
        }
    }

    public void b(EntityHuman entityhuman, Statistic<?> statistic, int i) {
        if (!PurpurConfig.disableStatistics) {
            int j = (int) Math.min((long) this.getStatisticValue(statistic) + (long) i, 2147483647L);

            // CraftBukkit start - fire Statistic events
            org.bukkit.event.Cancellable cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, this.getStatisticValue(statistic), j);
            if (cancellable != null && cancellable.isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.setStatistic(entityhuman, statistic, j);
        }
    }

    public void setStatistic(EntityHuman entityhuman, Statistic<?> statistic, int i) {
        if (!PurpurConfig.disableStatistics) {
            this.a.put(statistic, i);
        }
    }

    public int getStatisticValue(Statistic<?> statistic) {
        if (PurpurConfig.disableStatistics) {
            return 0;
        } else {
            return this.a.getInt(statistic);
        }
    }
}
