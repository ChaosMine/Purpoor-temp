package net.minecraft.world.level.saveddata.maps;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.Unmodifiable;

public class MapIcon {

    private final MapIcon.Type type;
    private byte x;
    private byte y;
    private byte rotation;
    private final IChatBaseComponent name;

    public MapIcon(MapIcon.Type mapicon_type, byte b0, byte b1, byte b2, @Nullable IChatBaseComponent ichatbasecomponent) {
        this.type = mapicon_type;
        this.x = b0;
        this.y = b1;
        this.rotation = b2;
        this.name = ichatbasecomponent;
    }

    public MapIcon.Type getType() {
        return this.type;
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getRotation() {
        return this.rotation;
    }

    @Nullable
    public IChatBaseComponent getName() {
        return this.name;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MapIcon)) {
            return false;
        } else {
            MapIcon mapicon = (MapIcon) object;

            return this.type != mapicon.type ? false : (this.rotation != mapicon.rotation ? false : (this.x != mapicon.x ? false : (this.y != mapicon.y ? false : Objects.equals(this.name, mapicon.name))));
        }
    }

    public int hashCode() {
        byte b0 = this.type.a();
        int i = 31 * b0 + this.x;

        i = 31 * i + this.y;
        i = 31 * i + this.rotation;
        i = 31 * i + Objects.hashCode(this.name);
        return i;
    }

    public enum Type {
        PLAYER(),
        FRAME(),
        RED_MARKER(),
        BLUE_MARKER(),
        TARGET_X(),
        TARGET_POINT(),
        PLAYER_OFF_MAP(),
        PLAYER_OFF_LIMITS(),
        MANSION(5393476),
        MONUMENT(3830373),
        BANNER_WHITE(),
        BANNER_ORANGE(),
        BANNER_MAGENTA(),
        BANNER_LIGHT_BLUE(),
        BANNER_YELLOW(),
        BANNER_LIME(),
        BANNER_PINK(),
        BANNER_GRAY(),
        BANNER_LIGHT_GRAY(),
        BANNER_CYAN(),
        BANNER_PURPLE(),
        BANNER_BLUE(),
        BANNER_BROWN(),
        BANNER_GREEN(),
        BANNER_RED(),
        BANNER_BLACK(),
        RED_X(),
        ;

        public static final Type @Unmodifiable [] VALUES = values();

        private final byte B;
        private final int D;

        Type() {
            this(-1);
        }

        Type(int i) {
            this.B = (byte) this.ordinal();
            this.D = i;
        }

        public byte a() {
            return this.B;
        }

        public boolean c() {
            return this.D >= 0;
        }

        public int d() {
            return this.D;
        }

        public static MapIcon.Type a(byte b0) {
            return values()[MathHelper.clamp(b0, 0, values().length - 1)];
        }
    }
}
