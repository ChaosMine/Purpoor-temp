package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.level.storage.loot.LootTableInfo;

public class CriterionTriggerTamedAnimal extends CriterionTriggerAbstract<CriterionTriggerTamedAnimal.a> {

    private static final MinecraftKey a = new MinecraftKey("tame_animal");

    public CriterionTriggerTamedAnimal() {}

    @Override
    public MinecraftKey a() {
        return CriterionTriggerTamedAnimal.a;
    }

    @Override
    public CriterionTriggerTamedAnimal.a b(JsonObject jsonobject, CriterionConditionEntity.b_ criterionconditionentity_b, LootDeserializationContext lootdeserializationcontext) {
        CriterionConditionEntity.b_ criterionconditionentity_b1 = CriterionConditionEntity.b_.a(jsonobject, "entity", lootdeserializationcontext);

        return new CriterionTriggerTamedAnimal.a(criterionconditionentity_b, criterionconditionentity_b1);
    }

    public void a(EntityPlayer entityplayer, EntityAnimal entityanimal) {
        LootTableInfo loottableinfo = CriterionConditionEntity.b(entityplayer, entityanimal);

        this.a(entityplayer, (criteriontriggertamedanimal_a) -> {
            return criteriontriggertamedanimal_a.a(loottableinfo);
        });
    }

    public static class a extends CriterionInstanceAbstract {

        private final CriterionConditionEntity.b_ a;

        public a(CriterionConditionEntity.b_ criterionconditionentity_b, CriterionConditionEntity.b_ criterionconditionentity_b1) {
            super(CriterionTriggerTamedAnimal.a, criterionconditionentity_b);
            this.a = criterionconditionentity_b1;
        }

        public static CriterionTriggerTamedAnimal.a c() {
            return new CriterionTriggerTamedAnimal.a(CriterionConditionEntity.b_.a, CriterionConditionEntity.b_.a);
        }

        public static CriterionTriggerTamedAnimal.a a(CriterionConditionEntity criterionconditionentity) {
            return new CriterionTriggerTamedAnimal.a(CriterionConditionEntity.b_.a, CriterionConditionEntity.b_.a(criterionconditionentity));
        }

        public boolean a(LootTableInfo loottableinfo) {
            return this.a.a(loottableinfo);
        }

        @Override
        public JsonObject a(LootSerializationContext lootserializationcontext) {
            JsonObject jsonobject = super.a(lootserializationcontext);

            jsonobject.add("entity", this.a.a(lootserializationcontext));
            return jsonobject;
        }
    }
}
