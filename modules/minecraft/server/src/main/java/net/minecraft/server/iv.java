package net.minecraft.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.minecraft.resources.MinecraftKey;

public class iv implements Supplier<JsonElement> {

    private final MinecraftKey a;

    public iv(MinecraftKey minecraftkey) {
        this.a = minecraftkey;
    }

    public JsonElement get() {
        JsonObject jsonobject = new JsonObject();

        jsonobject.addProperty("parent", this.a.toString());
        return jsonobject;
    }
}
