package net.minecraft.server;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionTriggerRecipeUnlocked;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class jm {

    private final RecipeItemStack a;
    private final RecipeItemStack b;
    private final Item c;
    private final Advancement.SerializedAdvancement d = Advancement.SerializedAdvancement.a();
    private final RecipeSerializer<?> e;

    public jm(RecipeSerializer<?> recipeserializer, RecipeItemStack recipeitemstack, RecipeItemStack recipeitemstack1, Item item) {
        this.e = recipeserializer;
        this.a = recipeitemstack;
        this.b = recipeitemstack1;
        this.c = item;
    }

    public static jm a(RecipeItemStack recipeitemstack, RecipeItemStack recipeitemstack1, Item item) {
        return new jm(RecipeSerializer.u, recipeitemstack, recipeitemstack1, item);
    }

    public jm a(String s, CriterionInstance criterioninstance) {
        this.d.a(s, criterioninstance);
        return this;
    }

    public void a(Consumer<jf> consumer, String s) {
        this.a(consumer, new MinecraftKey(s));
    }

    public void a(Consumer<jf> consumer, MinecraftKey minecraftkey) {
        this.a(minecraftkey);
        this.d.a(new MinecraftKey("recipes/root")).a("has_the_recipe", CriterionTriggerRecipeUnlocked.a(minecraftkey)).a(AdvancementRewards.a_.c(minecraftkey)).a(AdvancementRequirements.OR);
        consumer.accept(new jm.a(minecraftkey, this.e, this.a, this.b, this.c, this.d, new MinecraftKey(minecraftkey.getNamespace(), "recipes/" + this.c.q().b() + "/" + minecraftkey.getKey())));
    }

    private void a(MinecraftKey minecraftkey) {
        if (this.d.c().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + minecraftkey);
        }
    }

    public static class a implements jf {

        private final MinecraftKey a;
        private final RecipeItemStack b;
        private final RecipeItemStack c;
        private final Item d;
        private final Advancement.SerializedAdvancement e;
        private final MinecraftKey f;
        private final RecipeSerializer<?> g;

        public a(MinecraftKey minecraftkey, RecipeSerializer<?> recipeserializer, RecipeItemStack recipeitemstack, RecipeItemStack recipeitemstack1, Item item, Advancement.SerializedAdvancement advancement_serializedadvancement, MinecraftKey minecraftkey1) {
            this.a = minecraftkey;
            this.g = recipeserializer;
            this.b = recipeitemstack;
            this.c = recipeitemstack1;
            this.d = item;
            this.e = advancement_serializedadvancement;
            this.f = minecraftkey1;
        }

        @Override
        public void a(JsonObject jsonobject) {
            jsonobject.add("base", this.b.c());
            jsonobject.add("addition", this.c.c());
            JsonObject jsonobject1 = new JsonObject();

            jsonobject1.addProperty("item", IRegistry.ITEM.getKey(this.d).toString());
            jsonobject.add("result", jsonobject1);
        }

        @Override
        public MinecraftKey b() {
            return this.a;
        }

        @Override
        public RecipeSerializer<?> c() {
            return this.g;
        }

        @Nullable
        @Override
        public JsonObject d() {
            return this.e.b();
        }

        @Nullable
        @Override
        public MinecraftKey e() {
            return this.f;
        }
    }
}
