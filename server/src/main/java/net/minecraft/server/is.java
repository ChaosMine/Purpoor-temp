package net.minecraft.server;

import com.google.gson.JsonPrimitive;
import net.minecraft.resources.MinecraftKey;

public class is {

    public static final it<a_> a = new it<>("x", (is_a) -> {
        return new JsonPrimitive(is_a.e);
    });
    public static final it<a_> b = new it<>("y", (is_a) -> {
        return new JsonPrimitive(is_a.e);
    });
    public static final it<MinecraftKey> c = new it<>("model", (minecraftkey) -> {
        return new JsonPrimitive(minecraftkey.toString());
    });
    public static final it<Boolean> d = new it<>("uvlock", JsonPrimitive::new);
    public static final it<Integer> e = new it<>("weight", JsonPrimitive::new);

    public static enum a_ {

        a(0), b(90), c(180), d(270);

        private final int e;

        private a_(int i) {
            this.e = i;
        }
    }
}
