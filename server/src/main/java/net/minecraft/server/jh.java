package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

public class jh {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Item b;
    private final int c;
    private final List<String> d = Lists.newArrayList();
    private final Map<Character, RecipeItemStack> e = Maps.newLinkedHashMap();
    private final Advancement.SerializedAdvancement f = Advancement.SerializedAdvancement.a();
    private String g;

    public jh(IMaterial imaterial, int i) {
        this.b = imaterial.getItem();
        this.c = i;
    }

    public static jh a(IMaterial imaterial) {
        return a(imaterial, 1);
    }

    public static jh a(IMaterial imaterial, int i) {
        return new jh(imaterial, i);
    }

    public jh a(Character character, Tag<Item> tag) {
        return this.a(character, RecipeItemStack.a(tag));
    }

    public jh a(Character character, IMaterial imaterial) {
        return this.a(character, RecipeItemStack.a(imaterial));
    }

    public jh a(Character character, RecipeItemStack recipeitemstack) {
        if (this.e.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        } else if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.e.put(character, recipeitemstack);
            return this;
        }
    }

    public jh a(String s) {
        if (!this.d.isEmpty() && s.length() != this.d.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.d.add(s);
            return this;
        }
    }

    public jh a(String s, CriterionInstance criterioninstance) {
        this.f.a(s, criterioninstance);
        return this;
    }

    public jh b(String s) {
        this.g = s;
        return this;
    }

    public void a(Consumer<jf> consumer) {
        this.a(consumer, IRegistry.ITEM.getKey(this.b));
    }

    public void a(Consumer<jf> consumer, String s) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(this.b);

        if ((new MinecraftKey(s)).equals(minecraftkey)) {
            throw new IllegalStateException("Shaped Recipe " + s + " should remove its 'save' argument");
        } else {
            this.a(consumer, new MinecraftKey(s));
        }
    }

    public void a(Consumer<jf> consumer, MinecraftKey minecraftkey) {
        this.a(minecraftkey);
        this.f.a(new MinecraftKey("recipes/root")).a("has_the_recipe", CriterionTriggerRecipeUnlocked.a(minecraftkey)).a(AdvancementRewards.a_.c(minecraftkey)).a(AdvancementRequirements.OR);
        consumer.accept(new jh.a(minecraftkey, this.b, this.c, this.g == null ? "" : this.g, this.d, this.e, this.f, new MinecraftKey(minecraftkey.getNamespace(), "recipes/" + this.b.q().b() + "/" + minecraftkey.getKey())));
    }

    private void a(MinecraftKey minecraftkey) {
        if (this.d.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + minecraftkey + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.e.keySet());

            set.remove(' ');
            Iterator iterator = this.d.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                for (int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);

                    if (!this.e.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + minecraftkey + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + minecraftkey);
            } else if (this.d.size() == 1 && this.d.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + minecraftkey + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.f.c().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + minecraftkey);
            }
        }
    }

    class a implements jf {

        private final MinecraftKey b;
        private final Item c;
        private final int d;
        private final String e;
        private final List<String> f;
        private final Map<Character, RecipeItemStack> g;
        private final Advancement.SerializedAdvancement h;
        private final MinecraftKey i;

        public a(MinecraftKey minecraftkey, Item item, int i, String s, List list, Map map, Advancement.SerializedAdvancement advancement_serializedadvancement, MinecraftKey minecraftkey1) {
            this.b = minecraftkey;
            this.c = item;
            this.d = i;
            this.e = s;
            this.f = list;
            this.g = map;
            this.h = advancement_serializedadvancement;
            this.i = minecraftkey1;
        }

        @Override
        public void a(JsonObject jsonobject) {
            if (!this.e.isEmpty()) {
                jsonobject.addProperty("group", this.e);
            }

            JsonArray jsonarray = new JsonArray();
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                jsonarray.add(s);
            }

            jsonobject.add("pattern", jsonarray);
            JsonObject jsonobject1 = new JsonObject();
            Iterator iterator1 = this.g.entrySet().iterator();

            while (iterator1.hasNext()) {
                Entry<Character, RecipeItemStack> entry = (Entry) iterator1.next();

                jsonobject1.add(String.valueOf(entry.getKey()), entry.getValue().c());
            }

            jsonobject.add("key", jsonobject1);
            JsonObject jsonobject2 = new JsonObject();

            jsonobject2.addProperty("item", IRegistry.ITEM.getKey(this.c).toString());
            if (this.d > 1) {
                jsonobject2.addProperty("count", this.d);
            }

            jsonobject.add("result", jsonobject2);
        }

        @Override
        public RecipeSerializer<?> c() {
            return RecipeSerializer.a;
        }

        @Override
        public MinecraftKey b() {
            return this.b;
        }

        @Nullable
        @Override
        public JsonObject d() {
            return this.h.b();
        }

        @Nullable
        @Override
        public MinecraftKey e() {
            return this.i;
        }
    }
}
