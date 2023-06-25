package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.CriterionTriggerRecipeUnlocked;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.IMaterial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ji {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Item b;
    private final int c;
    private final List<RecipeItemStack> d = Lists.newArrayList();
    private final Advancement.SerializedAdvancement e = Advancement.SerializedAdvancement.a();
    private String f;

    public ji(IMaterial imaterial, int i) {
        this.b = imaterial.getItem();
        this.c = i;
    }

    public static ji a(IMaterial imaterial) {
        return new ji(imaterial, 1);
    }

    public static ji a(IMaterial imaterial, int i) {
        return new ji(imaterial, i);
    }

    public ji a(Tag<Item> tag) {
        return this.a(RecipeItemStack.a(tag));
    }

    public ji b(IMaterial imaterial) {
        return this.b(imaterial, 1);
    }

    public ji b(IMaterial imaterial, int i) {
        for (int j = 0; j < i; ++j) {
            this.a(RecipeItemStack.a(imaterial));
        }

        return this;
    }

    public ji a(RecipeItemStack recipeitemstack) {
        return this.a(recipeitemstack, 1);
    }

    public ji a(RecipeItemStack recipeitemstack, int i) {
        for (int j = 0; j < i; ++j) {
            this.d.add(recipeitemstack);
        }

        return this;
    }

    public ji a(String s, CriterionInstance criterioninstance) {
        this.e.a(s, criterioninstance);
        return this;
    }

    public ji a(String s) {
        this.f = s;
        return this;
    }

    public void a(Consumer<jf> consumer) {
        this.a(consumer, IRegistry.ITEM.getKey(this.b));
    }

    public void a(Consumer<jf> consumer, String s) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(this.b);

        if ((new MinecraftKey(s)).equals(minecraftkey)) {
            throw new IllegalStateException("Shapeless Recipe " + s + " should remove its 'save' argument");
        } else {
            this.a(consumer, new MinecraftKey(s));
        }
    }

    public void a(Consumer<jf> consumer, MinecraftKey minecraftkey) {
        this.a(minecraftkey);
        this.e.a(new MinecraftKey("recipes/root")).a("has_the_recipe", (CriterionInstance) CriterionTriggerRecipeUnlocked.a(minecraftkey)).a(AdvancementRewards.a.c(minecraftkey)).a(AdvancementRequirements.OR);
        consumer.accept(new ji.a(minecraftkey, this.b, this.c, this.f == null ? "" : this.f, this.d, this.e, new MinecraftKey(minecraftkey.getNamespace(), "recipes/" + this.b.q().b() + "/" + minecraftkey.getKey())));
    }

    private void a(MinecraftKey minecraftkey) {
        if (this.e.c().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + minecraftkey);
        }
    }

    public static class a implements jf {

        private final MinecraftKey a;
        private final Item b;
        private final int c;
        private final String d;
        private final List<RecipeItemStack> e;
        private final Advancement.SerializedAdvancement f;
        private final MinecraftKey g;

        public a(MinecraftKey minecraftkey, Item item, int i, String s, List<RecipeItemStack> list, Advancement.SerializedAdvancement advancement_serializedadvancement, MinecraftKey minecraftkey1) {
            this.a = minecraftkey;
            this.b = item;
            this.c = i;
            this.d = s;
            this.e = list;
            this.f = advancement_serializedadvancement;
            this.g = minecraftkey1;
        }

        @Override
        public void a(JsonObject jsonobject) {
            if (!this.d.isEmpty()) {
                jsonobject.addProperty("group", this.d);
            }

            JsonArray jsonarray = new JsonArray();
            Iterator iterator = this.e.iterator();

            while (iterator.hasNext()) {
                RecipeItemStack recipeitemstack = (RecipeItemStack) iterator.next();

                jsonarray.add(recipeitemstack.c());
            }

            jsonobject.add("ingredients", jsonarray);
            JsonObject jsonobject1 = new JsonObject();

            jsonobject1.addProperty("item", IRegistry.ITEM.getKey(this.b).toString());
            if (this.c > 1) {
                jsonobject1.addProperty("count", this.c);
            }

            jsonobject.add("result", jsonobject1);
        }

        @Override
        public RecipeSerializer<?> c() {
            return RecipeSerializer.b;
        }

        @Override
        public MinecraftKey b() {
            return this.a;
        }

        @Nullable
        @Override
        public JsonObject d() {
            return this.f.b();
        }

        @Nullable
        @Override
        public MinecraftKey e() {
            return this.g;
        }
    }
}
