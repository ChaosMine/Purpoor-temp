package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryBlocks;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;

public class hx implements DebugReportProvider {

    private static final Gson b = (new GsonBuilder()).setPrettyPrinting().create();
    private final DebugReportGenerator c;

    public hx(DebugReportGenerator debugreportgenerator) {
        this.c = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) throws IOException {
        JsonObject jsonobject = new JsonObject();

        IRegistry.f.keySet().forEach((minecraftkey) -> {
            jsonobject.add(minecraftkey.toString(), a((IRegistry) IRegistry.f.get(minecraftkey)));
        });
        Path path = this.c.b().resolve("reports/registries.json");

        DebugReportProvider.a(hx.b, hashcache, jsonobject, path);
    }

    private static <T> JsonElement a(IRegistry<T> iregistry) {
        JsonObject jsonobject = new JsonObject();

        if (iregistry instanceof RegistryBlocks) {
            MinecraftKey minecraftkey = ((RegistryBlocks) iregistry).a();

            jsonobject.addProperty("default", minecraftkey.toString());
        }

        int i = IRegistry.f.a((Object) iregistry);

        jsonobject.addProperty("protocol_id", i);
        JsonObject jsonobject1 = new JsonObject();
        Iterator iterator = iregistry.keySet().iterator();

        while (iterator.hasNext()) {
            MinecraftKey minecraftkey1 = (MinecraftKey) iterator.next();
            T t0 = iregistry.get(minecraftkey1);
            int j = iregistry.a(t0);
            JsonObject jsonobject2 = new JsonObject();

            jsonobject2.addProperty("protocol_id", j);
            jsonobject1.add(minecraftkey1.toString(), jsonobject2);
        }

        jsonobject.add("entries", jsonobject1);
        return jsonobject;
    }

    @Override
    public String a() {
        return "Registry Dump";
    }
}
