package net.minecraft.server;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface jf {

    void a(JsonObject jsonobject);

    default JsonObject a() {
        JsonObject jsonobject = new JsonObject();

        jsonobject.addProperty("type", IRegistry.RECIPE_SERIALIZER.getKey(this.c()).toString());
        this.a(jsonobject);
        return jsonobject;
    }

    MinecraftKey b();

    RecipeSerializer<?> c();

    @Nullable
    JsonObject d();

    @Nullable
    MinecraftKey e();
}
