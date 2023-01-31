package io.papermc.paper.entity;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.EnumMonsterType;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.EntityCategory;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class EntityCategoryTest {

    @Test
    public void test() throws IllegalAccessException {

        Map<EnumMonsterType, String> enumMonsterTypeFieldMap = Maps.newHashMap();
        for (Field field : EnumMonsterType.class.getDeclaredFields()) {
            if (field.getType() == EnumMonsterType.class) {
                enumMonsterTypeFieldMap.put( (EnumMonsterType) field.get(null), field.getName());
            }
        }

        for (EntityCategory entityCategory : EntityCategory.values()) {
            enumMonsterTypeFieldMap.remove(CraftLivingEntity.fromBukkitEntityCategory(entityCategory));
        }
        assertTrue(EnumMonsterType.class.getName() + " instance(s): " + Joiner.on(", ").join(enumMonsterTypeFieldMap.values()) + " do not have bukkit equivalents", enumMonsterTypeFieldMap.size() == 0);
    }
}
