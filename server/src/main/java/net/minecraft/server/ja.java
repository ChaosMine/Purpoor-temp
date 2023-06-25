package net.minecraft.server;

import javax.annotation.Nullable;

public final class ja {

    public static final ja a = a("all");
    public static final ja b = a("texture", ja.a);
    public static final ja c = a("particle", ja.b);
    public static final ja d = a("end", ja.a);
    public static final ja e = a("bottom", ja.d);
    public static final ja f = a("top", ja.d);
    public static final ja g = a("front", ja.a);
    public static final ja h = a("back", ja.a);
    public static final ja i = a("side", ja.a);
    public static final ja j = a("north", ja.i);
    public static final ja k = a("south", ja.i);
    public static final ja l = a("east", ja.i);
    public static final ja m = a("west", ja.i);
    public static final ja n = a("up");
    public static final ja o = a("down");
    public static final ja p = a("cross");
    public static final ja q = a("plant");
    public static final ja r = a("wall", ja.a);
    public static final ja s = a("rail");
    public static final ja t = a("wool");
    public static final ja u = a("pattern");
    public static final ja v = a("pane");
    public static final ja w = a("edge");
    public static final ja x = a("fan");
    public static final ja y = a("stem");
    public static final ja z = a("upperstem");
    public static final ja A = a("crop");
    public static final ja B = a("dirt");
    public static final ja C = a("fire");
    public static final ja D = a("lantern");
    public static final ja E = a("platform");
    public static final ja F = a("unsticky");
    public static final ja G = a("torch");
    public static final ja H = a("layer0");
    public static final ja I = a("lit_log");
    private final String J;
    @Nullable
    private final ja K;

    private static ja a(String s) {
        return new ja(s, (ja) null);
    }

    private static ja a(String s, ja ja) {
        return new ja(s, ja);
    }

    private ja(String s, @Nullable ja ja) {
        this.J = s;
        this.K = ja;
    }

    public String a() {
        return this.J;
    }

    @Nullable
    public ja b() {
        return this.K;
    }

    public String toString() {
        return "#" + this.J;
    }
}
