package net.minecraft.world;

import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;

public abstract class BossBattle {

    private final UUID h;
    public IChatBaseComponent title;
    protected float b;
    public BossBattle.BarColor color;
    public BossBattle.BarStyle style;
    protected boolean e;
    protected boolean f;
    protected boolean g;
    public net.kyori.adventure.bossbar.BossBar adventure; // Paper

    public BossBattle(UUID uuid, IChatBaseComponent ichatbasecomponent, BossBattle.BarColor bossbattle_barcolor, BossBattle.BarStyle bossbattle_barstyle) {
        this.h = uuid;
        this.title = ichatbasecomponent;
        this.color = bossbattle_barcolor;
        this.style = bossbattle_barstyle;
        this.b = 1.0F;
    }

    public UUID i() {
        return this.h;
    }

    public IChatBaseComponent j() {
        if(this.adventure != null) return PaperAdventure.asVanilla(this.adventure.name()); // Paper
        return this.title;
    }

    public void a(IChatBaseComponent ichatbasecomponent) {
        if (this.adventure != null) this.adventure.name(PaperAdventure.asAdventure(ichatbasecomponent)); // Paper
        this.title = ichatbasecomponent;
    }

    public float getProgress() {
        if (this.adventure != null) return this.adventure.progress(); // Paper
        return this.b;
    }

    public void a(float f) {
        if (this.adventure != null) this.adventure.progress(f); // Paper
        this.b = f;
    }

    public BossBattle.BarColor l() {
        if (this.adventure != null) return PaperAdventure.asVanilla(this.adventure.color()); // Paper
        return this.color;
    }

    public void a(BossBattle.BarColor bossbattle_barcolor) {
        if(this.adventure != null) this.adventure.color(PaperAdventure.asAdventure(bossbattle_barcolor)); // Paper
        this.color = bossbattle_barcolor;
    }

    public BossBattle.BarStyle m() {
        if(this.adventure != null) return PaperAdventure.asVanilla(this.adventure.overlay()); // Paper
        return this.style;
    }

    public void a(BossBattle.BarStyle bossbattle_barstyle) {
        if(this.adventure != null) this.adventure.overlay(PaperAdventure.asAdventure(bossbattle_barstyle)); // Paper
        this.style = bossbattle_barstyle;
    }

    public boolean isDarkenSky() {
        if(this.adventure != null) return this.adventure.hasFlag(net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN); // Paper
        return this.e;
    }

    public BossBattle a(boolean flag) {
        if(this.adventure != null) PaperAdventure.setFlag(this.adventure, net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN, flag); // Paper
        this.e = flag;
        return this;
    }

    public boolean isPlayMusic() {
        if(this.adventure != null) return this.adventure.hasFlag(net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC); // Paper
        return this.f;
    }

    public BossBattle b(boolean flag) {
        if(this.adventure != null) PaperAdventure.setFlag(this.adventure, net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC, flag); // Paper
        this.f = flag;
        return this;
    }

    public BossBattle c(boolean flag) {
        if(this.adventure != null) PaperAdventure.setFlag(this.adventure, net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG, flag); // Paper
        this.g = flag;
        return this;
    }

    public boolean isCreateFog() {
        if(this.adventure != null) return this.adventure.hasFlag(net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG); // Paper
        return this.g;
    }

    public static enum BarStyle {

        PROGRESS("progress"), NOTCHED_6("notched_6"), NOTCHED_10("notched_10"), NOTCHED_12("notched_12"), NOTCHED_20("notched_20");

        public static final BarStyle @Unmodifiable [] VALUES = values();

        private final String f;

        private BarStyle(String s) {
            this.f = s;
        }

        public String a() {
            return this.f;
        }

        public static BossBattle.BarStyle a(String s) {
            BossBattle.BarStyle[] abossbattle_barstyle = values();
            int i = abossbattle_barstyle.length;

            for (int j = 0; j < i; ++j) {
                BossBattle.BarStyle bossbattle_barstyle = abossbattle_barstyle[j];

                if (bossbattle_barstyle.f.equals(s)) {
                    return bossbattle_barstyle;
                }
            }

            return BossBattle.BarStyle.PROGRESS;
        }
    }

    public static enum BarColor {

        PINK("pink", EnumChatFormat.RED), BLUE("blue", EnumChatFormat.BLUE), RED("red", EnumChatFormat.DARK_RED), GREEN("green", EnumChatFormat.GREEN), YELLOW("yellow", EnumChatFormat.YELLOW), PURPLE("purple", EnumChatFormat.DARK_BLUE), WHITE("white", EnumChatFormat.WHITE);

        public static final BarColor @Unmodifiable [] VALUES = values();

        private final String h;
        private final EnumChatFormat i;

        private BarColor(String s, EnumChatFormat enumchatformat) {
            this.h = s;
            this.i = enumchatformat;
        }

        public EnumChatFormat a() {
            return this.i;
        }

        public String b() {
            return this.h;
        }

        public static BossBattle.BarColor a(String s) {
            BossBattle.BarColor[] abossbattle_barcolor = values();
            int i = abossbattle_barcolor.length;

            for (int j = 0; j < i; ++j) {
                BossBattle.BarColor bossbattle_barcolor = abossbattle_barcolor[j];

                if (bossbattle_barcolor.h.equals(s)) {
                    return bossbattle_barcolor;
                }
            }

            return BossBattle.BarColor.WHITE;
        }
    }
}
