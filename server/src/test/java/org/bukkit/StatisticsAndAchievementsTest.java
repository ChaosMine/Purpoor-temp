package org.bukkit;

import com.google.common.collect.HashMultiset;
import net.minecraft.core.IRegistry;
import net.minecraft.stats.StatisticWrapper;
import net.minecraft.world.entity.EntityTypes;
import net.pl3x.purpur.PurpurConfig;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.entity.EntityType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StatisticsAndAchievementsTest extends AbstractTestingBase {

    static {
        PurpurConfig.disableStatistics = false;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyEntityMapping() throws Throwable {
        for (Statistic statistic : Statistic.values()) {
            if (statistic.getType() == Statistic.Type.ENTITY) {
                for (EntityType entity : EntityType.values()) {
                    if (entity.getName() != null) {
                        assertNotNull(statistic + " missing for " + entity, CraftStatistic.getEntityStatistic(statistic, entity));
                    }
                }
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void verifyStatisticMapping() throws Throwable {
        HashMultiset<Statistic> statistics = HashMultiset.create();
        for (StatisticWrapper wrapper : IRegistry.STATS) {
            for (Object child : wrapper.getRegistry()) {
                net.minecraft.stats.Statistic<?> statistic = wrapper.b(child);
                String message = String.format("org.bukkit.Statistic is missing: '%s'", statistic);

                Statistic subject = CraftStatistic.getBukkitStatistic(statistic);
                assertThat(message, subject, is(not(nullValue())));

                if (wrapper.getRegistry() == IRegistry.BLOCK || wrapper.getRegistry() == IRegistry.ITEM) {
                    assertNotNull("Material type map missing for " + wrapper.getRegistry().getKey(child), CraftStatistic.getMaterialFromStatistic(statistic));
                } else if (wrapper.getRegistry() == IRegistry.ENTITY_TYPE) {
                    assertNotNull("Entity type map missing for " + EntityTypes.getName((EntityTypes<?>) child), CraftStatistic.getEntityTypeFromStatistic((net.minecraft.stats.Statistic<EntityTypes<?>>) statistic));
                }

                statistics.add(subject);
            }
        }

        for (Statistic statistic : Statistic.values()) {
            String message = String.format("org.bukkit.Statistic.%s does not have a corresponding minecraft statistic", statistic.name());
            assertThat(message, statistics.remove(statistic, statistics.count(statistic)), is(greaterThan(0)));
        }
    }
}
