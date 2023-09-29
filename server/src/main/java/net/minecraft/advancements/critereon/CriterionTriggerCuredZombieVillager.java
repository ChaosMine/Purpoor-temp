package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.level.storage.loot.LootTableInfo;

public class CriterionTriggerCuredZombieVillager extends CriterionTriggerAbstract<CriterionTriggerCuredZombieVillager.a> {

    private static final MinecraftKey a = new MinecraftKey("cured_zombie_villager");

    public CriterionTriggerCuredZombieVillager() {}

    @Override
    public MinecraftKey a() {
        return CriterionTriggerCuredZombieVillager.a;
    }

    @Override
    public CriterionTriggerCuredZombieVillager.a b(JsonObject jsonobject, CriterionConditionEntity.b_ criterionconditionentity_b, LootDeserializationContext lootdeserializationcontext) {
        CriterionConditionEntity.b_ criterionconditionentity_b1 = CriterionConditionEntity.b_.a(jsonobject, "zombie", lootdeserializationcontext);
        CriterionConditionEntity.b_ criterionconditionentity_b2 = CriterionConditionEntity.b_.a(jsonobject, "villager", lootdeserializationcontext);

        return new CriterionTriggerCuredZombieVillager.a(criterionconditionentity_b, criterionconditionentity_b1, criterionconditionentity_b2);
    }

    public void a(EntityPlayer entityplayer, EntityZombie entityzombie, EntityVillager entityvillager) {
        LootTableInfo loottableinfo = CriterionConditionEntity.b(entityplayer, entityzombie);
        LootTableInfo loottableinfo1 = CriterionConditionEntity.b(entityplayer, entityvillager);

        this.a(entityplayer, (criteriontriggercuredzombievillager_a) -> {
            return criteriontriggercuredzombievillager_a.a(loottableinfo, loottableinfo1);
        });
    }

    public static class a extends CriterionInstanceAbstract {

        private final CriterionConditionEntity.b_ a;
        private final CriterionConditionEntity.b_ b;

        public a(CriterionConditionEntity.b_ criterionconditionentity_b, CriterionConditionEntity.b_ criterionconditionentity_b1, CriterionConditionEntity.b_ criterionconditionentity_b2) {
            super(CriterionTriggerCuredZombieVillager.a, criterionconditionentity_b);
            this.a = criterionconditionentity_b1;
            this.b = criterionconditionentity_b2;
        }

        public static CriterionTriggerCuredZombieVillager.a c() {
            return new CriterionTriggerCuredZombieVillager.a(CriterionConditionEntity.b_.a, CriterionConditionEntity.b_.a, CriterionConditionEntity.b_.a);
        }

        public boolean a(LootTableInfo loottableinfo, LootTableInfo loottableinfo1) {
            return !this.a.a(loottableinfo) ? false : this.b.a(loottableinfo1);
        }

        @Override
        public JsonObject a(LootSerializationContext lootserializationcontext) {
            JsonObject jsonobject = super.a(lootserializationcontext);

            jsonobject.add("zombie", this.a.a(lootserializationcontext));
            jsonobject.add("villager", this.b.a(lootserializationcontext));
            return jsonobject;
        }
    }
}
