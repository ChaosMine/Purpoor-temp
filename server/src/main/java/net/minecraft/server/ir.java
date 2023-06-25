package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ir implements Supplier<JsonElement> {

    private final Map<it<?>, it<?>.a> a = Maps.newLinkedHashMap();

    public ir() {}

    public <T> ir a(it<T> it, T t0) {
        it<?>.a it_a = (it.a) this.a.put(it, it.a(t0));

        if (it_a != null) {
            throw new IllegalStateException("Replacing value of " + it_a + " with " + t0);
        } else {
            return this;
        }
    }

    public static ir a() {
        return new ir();
    }

    public static ir a(ir ir, ir ir1) {
        ir ir2 = new ir();

        ir2.a.putAll(ir.a);
        ir2.a.putAll(ir1.a);
        return ir2;
    }

    public JsonElement get() {
        JsonObject jsonobject = new JsonObject();

        this.a.values().forEach((it_a) -> {
            it_a.a(jsonobject);
        });
        return jsonobject;
    }

    public static JsonElement a(List<ir> list) {
        if (list.size() == 1) {
            return ((ir) list.get(0)).get();
        } else {
            JsonArray jsonarray = new JsonArray();

            list.forEach((ir) -> {
                jsonarray.add(ir.get());
            });
            return jsonarray;
        }
    }
}
