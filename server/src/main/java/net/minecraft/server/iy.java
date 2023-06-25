package net.minecraft.server;

import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.resources.MinecraftKey;

public class iy {

    public static final ix a = a("cube", ja.c, ja.j, ja.k, ja.l, ja.m, ja.n, ja.o);
    public static final ix b = a("cube_directional", ja.c, ja.j, ja.k, ja.l, ja.m, ja.n, ja.o);
    public static final ix c = a("cube_all", ja.a);
    public static final ix d = a("cube_mirrored_all", "_mirrored", ja.a);
    public static final ix e = a("cube_column", ja.d, ja.i);
    public static final ix f = a("cube_column_horizontal", "_horizontal", ja.d, ja.i);
    public static final ix g = a("cube_top", ja.f, ja.i);
    public static final ix h = a("cube_bottom_top", ja.f, ja.e, ja.i);
    public static final ix i = a("orientable", ja.f, ja.g, ja.i);
    public static final ix j = a("orientable_with_bottom", ja.f, ja.e, ja.i, ja.g);
    public static final ix k = a("orientable_vertical", "_vertical", ja.g, ja.i);
    public static final ix l = a("button", ja.b);
    public static final ix m = a("button_pressed", "_pressed", ja.b);
    public static final ix n = a("button_inventory", "_inventory", ja.b);
    public static final ix o = a("door_bottom", "_bottom", ja.f, ja.e);
    public static final ix p = a("door_bottom_rh", "_bottom_hinge", ja.f, ja.e);
    public static final ix q = a("door_top", "_top", ja.f, ja.e);
    public static final ix r = a("door_top_rh", "_top_hinge", ja.f, ja.e);
    public static final ix s = a("fence_post", "_post", ja.b);
    public static final ix t = a("fence_side", "_side", ja.b);
    public static final ix u = a("fence_inventory", "_inventory", ja.b);
    public static final ix v = a("template_wall_post", "_post", ja.r);
    public static final ix w = a("template_wall_side", "_side", ja.r);
    public static final ix x = a("template_wall_side_tall", "_side_tall", ja.r);
    public static final ix y = a("wall_inventory", "_inventory", ja.r);
    public static final ix z = a("template_fence_gate", ja.b);
    public static final ix A = a("template_fence_gate_open", "_open", ja.b);
    public static final ix B = a("template_fence_gate_wall", "_wall", ja.b);
    public static final ix C = a("template_fence_gate_wall_open", "_wall_open", ja.b);
    public static final ix D = a("pressure_plate_up", ja.b);
    public static final ix E = a("pressure_plate_down", "_down", ja.b);
    public static final ix F = a(ja.c);
    public static final ix G = a("slab", ja.e, ja.f, ja.i);
    public static final ix H = a("slab_top", "_top", ja.e, ja.f, ja.i);
    public static final ix I = a("leaves", ja.a);
    public static final ix J = a("stairs", ja.e, ja.f, ja.i);
    public static final ix K = a("inner_stairs", "_inner", ja.e, ja.f, ja.i);
    public static final ix L = a("outer_stairs", "_outer", ja.e, ja.f, ja.i);
    public static final ix M = a("template_trapdoor_top", "_top", ja.b);
    public static final ix N = a("template_trapdoor_bottom", "_bottom", ja.b);
    public static final ix O = a("template_trapdoor_open", "_open", ja.b);
    public static final ix P = a("template_orientable_trapdoor_top", "_top", ja.b);
    public static final ix Q = a("template_orientable_trapdoor_bottom", "_bottom", ja.b);
    public static final ix R = a("template_orientable_trapdoor_open", "_open", ja.b);
    public static final ix S = a("cross", ja.p);
    public static final ix T = a("tinted_cross", ja.p);
    public static final ix U = a("flower_pot_cross", ja.q);
    public static final ix V = a("tinted_flower_pot_cross", ja.q);
    public static final ix W = a("rail_flat", ja.s);
    public static final ix X = a("rail_curved", "_corner", ja.s);
    public static final ix Y = a("template_rail_raised_ne", "_raised_ne", ja.s);
    public static final ix Z = a("template_rail_raised_sw", "_raised_sw", ja.s);
    public static final ix aa = a("carpet", ja.t);
    public static final ix ab = a("coral_fan", ja.x);
    public static final ix ac = a("coral_wall_fan", ja.x);
    public static final ix ad = a("template_glazed_terracotta", ja.u);
    public static final ix ae = a("template_chorus_flower", ja.b);
    public static final ix af = a("template_daylight_detector", ja.f, ja.i);
    public static final ix ag = a("template_glass_pane_noside", "_noside", ja.v);
    public static final ix ah = a("template_glass_pane_noside_alt", "_noside_alt", ja.v);
    public static final ix ai = a("template_glass_pane_post", "_post", ja.v, ja.w);
    public static final ix aj = a("template_glass_pane_side", "_side", ja.v, ja.w);
    public static final ix ak = a("template_glass_pane_side_alt", "_side_alt", ja.v, ja.w);
    public static final ix al = a("template_command_block", ja.g, ja.h, ja.i);
    public static final ix am = a("template_anvil", ja.f);
    public static final ix[] an = (ix[]) IntStream.range(0, 8).mapToObj((i) -> {
        return a("stem_growth" + i, "_stage" + i, ja.y);
    }).toArray((i) -> {
        return new ix[i];
    });
    public static final ix ao = a("stem_fruit", ja.y, ja.z);
    public static final ix ap = a("crop", ja.A);
    public static final ix aq = a("template_farmland", ja.B, ja.f);
    public static final ix ar = a("template_fire_floor", ja.C);
    public static final ix as = a("template_fire_side", ja.C);
    public static final ix at = a("template_fire_side_alt", ja.C);
    public static final ix au = a("template_fire_up", ja.C);
    public static final ix av = a("template_fire_up_alt", ja.C);
    public static final ix aw = a("template_campfire", ja.C, ja.I);
    public static final ix ax = a("template_lantern", ja.D);
    public static final ix ay = a("template_hanging_lantern", "_hanging", ja.D);
    public static final ix az = a("template_torch", ja.G);
    public static final ix aA = a("template_torch_wall", ja.G);
    public static final ix aB = a("template_piston", ja.E, ja.e, ja.i);
    public static final ix aC = a("template_piston_head", ja.E, ja.i, ja.F);
    public static final ix aD = a("template_piston_head_short", ja.E, ja.i, ja.F);
    public static final ix aE = a("template_seagrass", ja.b);
    public static final ix aF = a("template_turtle_egg", ja.a);
    public static final ix aG = a("template_two_turtle_eggs", ja.a);
    public static final ix aH = a("template_three_turtle_eggs", ja.a);
    public static final ix aI = a("template_four_turtle_eggs", ja.a);
    public static final ix aJ = a("template_single_face", ja.b);
    public static final ix aK = b("generated", ja.H);
    public static final ix aL = b("handheld", ja.H);
    public static final ix aM = b("handheld_rod", ja.H);
    public static final ix aN = b("template_shulker_box", ja.c);
    public static final ix aO = b("template_bed", ja.c);
    public static final ix aP = b("template_banner");
    public static final ix aQ = b("template_skull");

    private static ix a(ja... aja) {
        return new ix(Optional.empty(), Optional.empty(), aja);
    }

    private static ix a(String s, ja... aja) {
        return new ix(Optional.of(new MinecraftKey("minecraft", "block/" + s)), Optional.empty(), aja);
    }

    private static ix b(String s, ja... aja) {
        return new ix(Optional.of(new MinecraftKey("minecraft", "item/" + s)), Optional.empty(), aja);
    }

    private static ix a(String s, String s1, ja... aja) {
        return new ix(Optional.of(new MinecraftKey("minecraft", "block/" + s)), Optional.of(s1), aja);
    }
}
