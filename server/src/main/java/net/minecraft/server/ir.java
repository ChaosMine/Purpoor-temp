package net.minecraft.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ir implements Supplier<JsonElement> {
	private final Map<it<?>, it<?>.a> a = new LinkedHashMap<>();

	public static ir a() {
		return new ir();
	}

	public static ir a(ir var0, ir var1) {
		ir var2 = new ir();
		var2.a.putAll(var0.a);
		var2.a.putAll(var1.a);
		return var2;
	}

	public static JsonElement a(List<ir> var0) {
		if (var0.size() == 1) {
			return var0.get(0).get();
		} else {
			JsonArray var1 = new JsonArray();
			var0.forEach(var1x -> var1.add(var1x.get()));
			return var1;
		}
	}

	public <T> ir a(it<T> var0, T var1) {
		it<?>.a var2 = this.a.put(var0, var0.a(var1));
		if (var2 != null) {
			throw new IllegalStateException("Replacing value of " + var2 + " with " + var1);
		} else {
			return this;
		}
	}

	public JsonElement get() {
		JsonObject var0 = new JsonObject();
		this.a.values().forEach(var1x -> var1x.a(var0));
		return var0;
	}
}
