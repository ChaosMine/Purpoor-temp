package net.minecraft.server;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializerComplex;

public class jl {

    private final RecipeSerializerComplex<?> a;

    public jl(RecipeSerializerComplex<?> recipeserializercomplex) {
        this.a = recipeserializercomplex;
    }

    public static jl a(RecipeSerializerComplex<?> recipeserializercomplex) {
        return new jl(recipeserializercomplex);
    }

    public void a(Consumer<jf> consumer, final String s) {
        consumer.accept(new jf() {
            @Override
            public void a(JsonObject jsonobject) {}

            @Override
            public RecipeSerializer<?> c() {
                return jl.this.a;
            }

            @Override
            public MinecraftKey b() {
                return new MinecraftKey(s);
            }

            @Nullable
            @Override
            public JsonObject d() {
                return null;
            }

            @Override
            public MinecraftKey e() {
                return new MinecraftKey("");
            }
        });
    }
}
