package net.minecraft.world.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.entity.player.AutoRecipeStackManager;
import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;

import java.util.Iterator;
// CraftBukkit end

public class ShapelessRecipes implements RecipeCrafting {

    private final MinecraftKey key;
    private final String group;
    private final ItemStack result;
    private final NonNullList<RecipeItemStack> ingredients;

    public ShapelessRecipes(MinecraftKey minecraftkey, String s, ItemStack itemstack, NonNullList<RecipeItemStack> nonnulllist) {
        this.key = minecraftkey;
        this.group = s;
        this.result = itemstack;
        this.ingredients = nonnulllist;
    }

    // CraftBukkit start
    @SuppressWarnings("unchecked")
    public org.bukkit.inventory.ShapelessRecipe toBukkitRecipe() {
        var result = CraftItemStack.asCraftMirror(this.result);
        var recipe = new CraftShapelessRecipe(result, this);
        recipe.setGroup(this.group);

        for (var list : this.ingredients) {
            recipe.addIngredient(CraftRecipe.toBukkit(list));
        }
        return recipe;
    }
    // CraftBukkit end

    @Override
    public MinecraftKey getKey() {
        return this.key;
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializer.b;
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public NonNullList<RecipeItemStack> a() {
        return this.ingredients;
    }

    public boolean a(InventoryCrafting inventorycrafting, World world) {
        // Paper start
        java.util.List<ItemStack> providedItems = new ObjectArrayList<>();
        var matchedProvided = new co.aikar.util.Counter<ItemStack>();
        var matchedIngredients = new co.aikar.util.Counter<RecipeItemStack>();
        // Paper end
        for (var j = 0; j < inventorycrafting.getSize(); ++j) {
            var itemstack = inventorycrafting.getItem(j);

            if (!itemstack.isEmpty()) {
                // Paper start
                itemstack = itemstack.cloneItemStack();
                providedItems.add(itemstack);
                for (var ingredient : ingredients) {
                    if (ingredient.test(itemstack)) {
                        matchedProvided.increment(itemstack);
                        matchedIngredients.increment(ingredient);
                    }
                }
                // Paper end
            }
        }

        // Paper start
        if (matchedProvided.isEmpty() || matchedIngredients.isEmpty()) {
            return false;
        }
        java.util.List<RecipeItemStack> ingredients = new ObjectArrayList<>(this.ingredients);
        providedItems.sort(java.util.Comparator.comparingInt((ItemStack c) -> (int) matchedProvided.getCount(c)).reversed());
        ingredients.sort(java.util.Comparator.comparingInt((RecipeItemStack c) -> (int) matchedIngredients.getCount(c)));

        PROVIDED:
        for (var provided : providedItems) {
            for (var itIngredient = ingredients.iterator(); itIngredient.hasNext(); ) {
                var ingredient = itIngredient.next();
                if (ingredient.test(provided)) {
                    itIngredient.remove();
                    continue PROVIDED;
                }
            }
            return false;
        }
        return ingredients.isEmpty();
        // Paper end
    }

    public ItemStack a(InventoryCrafting inventorycrafting) {
        return this.result.cloneItemStack();
    }

    public static class a implements RecipeSerializer<ShapelessRecipes> {

        public a() {}

        @Override
        public ShapelessRecipes a(MinecraftKey minecraftkey, JsonObject jsonobject) {
            var s = ChatDeserializer.a(jsonobject, "group", "");
            var nonnulllist = a(ChatDeserializer.u(jsonobject, "ingredients"));

            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                var itemstack = ShapedRecipes.a(ChatDeserializer.t(jsonobject, "result"));

                return new ShapelessRecipes(minecraftkey, s, itemstack, nonnulllist);
            }
        }

        private static NonNullList<RecipeItemStack> a(JsonArray jsonarray) {
            NonNullList<RecipeItemStack> nonnulllist = NonNullList.a();

            for (var i = 0; i < jsonarray.size(); ++i) {
                var recipeitemstack = RecipeItemStack.a(jsonarray.get(i));

                if (!recipeitemstack.d()) {
                    nonnulllist.add(recipeitemstack);
                }
            }

            return nonnulllist;
        }

        @Override
        public ShapelessRecipes a(MinecraftKey minecraftkey, PacketDataSerializer packetdataserializer) {
            var s = packetdataserializer.e(32767);
            var i = packetdataserializer.i();
            var nonnulllist = NonNullList.a(i, RecipeItemStack.a);

            for (var j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, RecipeItemStack.b(packetdataserializer));
            }

            var itemstack = packetdataserializer.n();

            return new ShapelessRecipes(minecraftkey, s, itemstack, nonnulllist);
        }

        public void a(PacketDataSerializer packetdataserializer, ShapelessRecipes shapelessrecipes) {
            packetdataserializer.a(shapelessrecipes.group);
            packetdataserializer.d(shapelessrecipes.ingredients.size());

            for (var recipeitemstack : shapelessrecipes.ingredients) {
                recipeitemstack.a(packetdataserializer);
            }

            packetdataserializer.a(shapelessrecipes.result);
        }
    }
}
