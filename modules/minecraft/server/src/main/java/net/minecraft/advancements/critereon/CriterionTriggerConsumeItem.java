package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IMaterial;

public class CriterionTriggerConsumeItem extends CriterionTriggerAbstract<CriterionTriggerConsumeItem.a> {

    private static final MinecraftKey a = new MinecraftKey("consume_item");

    public CriterionTriggerConsumeItem() {}

    @Override
    public MinecraftKey a() {
        return CriterionTriggerConsumeItem.a;
    }

    @Override
    public CriterionTriggerConsumeItem.a b(JsonObject jsonobject, CriterionConditionEntity.b_ criterionconditionentity_b, LootDeserializationContext lootdeserializationcontext) {
        return new CriterionTriggerConsumeItem.a(criterionconditionentity_b, CriterionConditionItem.a(jsonobject.get("item")));
    }

    public void a(EntityPlayer entityplayer, ItemStack itemstack) {
        this.a(entityplayer, (criteriontriggerconsumeitem_a) -> {
            return criteriontriggerconsumeitem_a.a(itemstack);
        });
    }

    public static class a extends CriterionInstanceAbstract {

        private final CriterionConditionItem a;

        public a(CriterionConditionEntity.b_ criterionconditionentity_b, CriterionConditionItem criterionconditionitem) {
            super(CriterionTriggerConsumeItem.a, criterionconditionentity_b);
            this.a = criterionconditionitem;
        }

        public static CriterionTriggerConsumeItem.a c() {
            return new CriterionTriggerConsumeItem.a(CriterionConditionEntity.b_.a, CriterionConditionItem.a);
        }

        public static CriterionTriggerConsumeItem.a a(IMaterial imaterial) {
            return new CriterionTriggerConsumeItem.a(CriterionConditionEntity.b_.a, new CriterionConditionItem(null, imaterial.getItem(), CriterionConditionValue.IntegerRange.e, CriterionConditionValue.IntegerRange.e, CriterionConditionEnchantments.b, CriterionConditionEnchantments.b, null, CriterionConditionNBT.a));
        }

        public boolean a(ItemStack itemstack) {
            return this.a.a(itemstack);
        }

        @Override
        public JsonObject a(LootSerializationContext lootserializationcontext) {
            JsonObject jsonobject = super.a(lootserializationcontext);

            jsonobject.add("item", this.a.a());
            return jsonobject;
        }
    }
}
