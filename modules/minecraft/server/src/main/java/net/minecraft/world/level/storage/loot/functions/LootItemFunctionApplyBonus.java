package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootItemFunctionApplyBonus extends LootItemFunctionConditional {

    private static final Map<MinecraftKey, c_> a = Maps.newHashMap();
    private final Enchantment b;
    private final b_ d;

    private LootItemFunctionApplyBonus(LootItemCondition[] alootitemcondition, Enchantment enchantment, b_ lootitemfunctionapplybonus_b) {
        super(alootitemcondition);
        this.b = enchantment;
        this.d = lootitemfunctionapplybonus_b;
    }

    @Override
    public LootItemFunctionType b() {
        return LootItemFunctions.p;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.TOOL);
    }

    @Override
    public ItemStack a(ItemStack itemstack, LootTableInfo loottableinfo) {
        ItemStack itemstack1 = loottableinfo.getContextParameter(LootContextParameters.TOOL);

        if (itemstack1 != null) {
            int i = EnchantmentManager.getEnchantmentLevel(this.b, itemstack1);
            int j = this.d.a(loottableinfo.a(), itemstack.getCount(), i);

            itemstack.setCount(j);
        }

        return itemstack;
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment, float f, int i) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new a_(i, f));
        });
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new d_());
        });
    }

    public static LootItemFunctionConditional.a<?> b(Enchantment enchantment) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new f_(1));
        });
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment, int i) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new f_(i));
        });
    }

    static {
        LootItemFunctionApplyBonus.a.put(LootItemFunctionApplyBonus.a_.a, LootItemFunctionApplyBonus.a_::a);
        LootItemFunctionApplyBonus.a.put(LootItemFunctionApplyBonus.d_.a, LootItemFunctionApplyBonus.d_::a);
        LootItemFunctionApplyBonus.a.put(f_.a, f_::a);
    }

    public static class e_ extends LootItemFunctionConditional.c<LootItemFunctionApplyBonus> {

        public e_() {}

        public void a(JsonObject jsonobject, LootItemFunctionApplyBonus lootitemfunctionapplybonus, JsonSerializationContext jsonserializationcontext) {
            super.a(jsonobject, lootitemfunctionapplybonus, jsonserializationcontext);
            jsonobject.addProperty("enchantment", IRegistry.ENCHANTMENT.getKey(lootitemfunctionapplybonus.b).toString());
            jsonobject.addProperty("formula", lootitemfunctionapplybonus.d.a().toString());
            JsonObject jsonobject1 = new JsonObject();

            lootitemfunctionapplybonus.d.a(jsonobject1, jsonserializationcontext);
            if (jsonobject1.size() > 0) {
                jsonobject.add("parameters", jsonobject1);
            }

        }

        @Override
        public LootItemFunctionApplyBonus b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
            MinecraftKey minecraftkey = new MinecraftKey(ChatDeserializer.h(jsonobject, "enchantment"));
            Enchantment enchantment = IRegistry.ENCHANTMENT.getOptional(minecraftkey).orElseThrow(() -> {
                return new JsonParseException("Invalid enchantment id: " + minecraftkey);
            });
            MinecraftKey minecraftkey1 = new MinecraftKey(ChatDeserializer.h(jsonobject, "formula"));
            c_ lootitemfunctionapplybonus_c = LootItemFunctionApplyBonus.a.get(minecraftkey1);

            if (lootitemfunctionapplybonus_c == null) {
                throw new JsonParseException("Invalid formula id: " + minecraftkey1);
            } else {
                b_ lootitemfunctionapplybonus_b;

                if (jsonobject.has("parameters")) {
                    lootitemfunctionapplybonus_b = lootitemfunctionapplybonus_c.deserialize(ChatDeserializer.t(jsonobject, "parameters"), jsondeserializationcontext);
                } else {
                    lootitemfunctionapplybonus_b = lootitemfunctionapplybonus_c.deserialize(new JsonObject(), jsondeserializationcontext);
                }

                return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, lootitemfunctionapplybonus_b);
            }
        }
    }

    static final class d_ implements b_ {

        public static final MinecraftKey a = new MinecraftKey("ore_drops");

        private d_() {}

        @Override
        public int a(Random random, int i, int j) {
            if (j > 0) {
                int k = random.nextInt(j + 2) - 1;

                if (k < 0) {
                    k = 0;
                }

                return i * (k + 1);
            } else {
                return i;
            }
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {}

        public static b_ a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new d_();
        }

        @Override
        public MinecraftKey a() {
            return LootItemFunctionApplyBonus.d_.a;
        }
    }

    static final class f_ implements b_ {

        public static final MinecraftKey a = new MinecraftKey("uniform_bonus_count");
        private final int b;

        public f_(int i) {
            this.b = i;
        }

        @Override
        public int a(Random random, int i, int j) {
            return i + random.nextInt(this.b * j + 1);
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("bonusMultiplier", this.b);
        }

        public static b_ a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            int i = ChatDeserializer.n(jsonobject, "bonusMultiplier");

            return new f_(i);
        }

        @Override
        public MinecraftKey a() {
            return f_.a;
        }
    }

    static final class a_ implements b_ {

        public static final MinecraftKey a = new MinecraftKey("binomial_with_bonus_count");
        private final int b;
        private final float c;

        public a_(int i, float f) {
            this.b = i;
            this.c = f;
        }

        @Override
        public int a(Random random, int i, int j) {
            for (int k = 0; k < j + this.b; ++k) {
                if (random.nextFloat() < this.c) {
                    ++i;
                }
            }

            return i;
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("extra", this.b);
            jsonobject.addProperty("probability", this.c);
        }

        public static b_ a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            int i = ChatDeserializer.n(jsonobject, "extra");
            float f = ChatDeserializer.l(jsonobject, "probability");

            return new a_(i, f);
        }

        @Override
        public MinecraftKey a() {
            return LootItemFunctionApplyBonus.a_.a;
        }
    }

    interface c_ {

        b_ deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext);
    }

    interface b_ {

        int a(Random random, int i, int j);

        void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext);

        MinecraftKey a();
    }
}
