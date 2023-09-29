package net.minecraft.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Function;

public class it<T> {

    private final String a;
    private final Function<T, JsonElement> b;

    public it(String s, Function<T, JsonElement> function) {
        this.a = s;
        this.b = function;
    }

    public it<T>.a a(T t0) {
        return new it.a(t0);
    }

    public String toString() {
        return this.a;
    }

    public class a {

        private final T b;

        public a(Object object) {
            this.b = (T) object;
        }

        public void a(JsonObject jsonobject) {
            jsonobject.add(it.this.a, it.this.b.apply(this.b));
        }

        public String toString() {
            return it.this.a + "=" + this.b;
        }
    }
}
