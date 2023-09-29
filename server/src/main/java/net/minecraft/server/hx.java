package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryBlocks;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;

import java.io.IOException;

public class hx implements DebugReportProvider {
	private static final Gson b = new GsonBuilder().setPrettyPrinting().create();
	private final DebugReportGenerator c;

	public hx(DebugReportGenerator var0) {
		this.c = var0;
	}

	private static <T> JsonElement a(IRegistry<T> var0) {
		JsonObject var1 = new JsonObject();
		if (var0 instanceof RegistryBlocks<?> rb) {
			MinecraftKey var2 = rb.a();
			var1.addProperty("default", var2.toString());
		}

		int var2 = ((IRegistry) IRegistry.f).a(var0);
		var1.addProperty("protocol_id", var2);
		JsonObject var3 = new JsonObject();

		for (MinecraftKey var5 : var0.keySet()) {
			T var6 = var0.get(var5);
			int var7 = var0.a(var6);
			JsonObject var8 = new JsonObject();
			var8.addProperty("protocol_id", var7);
			var3.add(var5.toString(), var8);
		}

		var1.add("entries", var3);
		return var1;
	}

	@Override
	public void a(HashCache var0) throws IOException {
		JsonObject var1 = new JsonObject();
		IRegistry.f.keySet().forEach(var1x -> var1.add(var1x.toString(), a(IRegistry.f.get(var1x))));
		java.nio.file.Path var2 = this.c.b().resolve("reports/registries.json");
		DebugReportProvider.a(b, var0, var1, var2);
	}

	@Override
	public String a() {
		return "Registry Dump";
	}
}
