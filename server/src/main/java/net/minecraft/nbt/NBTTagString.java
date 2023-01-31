package net.minecraft.nbt;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString implements NBTBase {

    public static final NBTTagType<NBTTagString> a = new NBTTagType<>() {
        @Override
        public NBTTagString b(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
            nbtreadlimiter.a(288L);
            var s = datainput.readUTF();

            nbtreadlimiter.a(16L * s.length());
            return NBTTagString.a(s);
        }

        @Override
        public String a() {
            return "STRING";
        }

        @Override
        public String b() {
            return "TAG_String";
        }

        @Override
        public boolean c() {
            return true;
        }
    };
    private static final NBTTagString b = new NBTTagString("");
    private final String data;

    private NBTTagString(String s) {
        Objects.requireNonNull(s, "Null string not allowed");
        this.data = s;
    }

    public static NBTTagString create(String value) { return a(value); } // Paper - OBFHELPER
    public static NBTTagString a(String s) {
        return s.isEmpty() ? NBTTagString.b : new NBTTagString(s);
    }

    @Override
    public void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeUTF(this.data);
    }

    @Override
    public byte getTypeId() {
        return 8;
    }

    @Override
    public NBTTagType<NBTTagString> b() {
        return NBTTagString.a;
    }

    @Override
    public String toString() {
        return b(this.data);
    }

    @Override
    public NBTTagString clone() {
        return this;
    }

    public boolean equals(Object object) {
        return this == object ? true : object instanceof NBTTagString && Objects.equals(this.data, ((NBTTagString) object).data);
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    @Override
    public String asString() {
        return this.data;
    }

    @Override
    public IChatBaseComponent a(String s, int i) {
        var s1 = b(this.data);
        var s2 = s1.substring(0, 1);
        var ichatmutablecomponent = (new ChatComponentText(s1.substring(1, s1.length() - 1))).a(NBTTagString.e);

        return (new ChatComponentText(s2)).addSibling(ichatmutablecomponent).c(s2);
    }

    public static String b(String s) {
        var stringbuilder = dev.kamillaova.paper.pool.StringBuilderPool.take();
        stringbuilder.append(" ");
        var i = 0;

        for (var j = 0; j < s.length(); ++j) {
            var c0 = s.charAt(j);

            if (c0 == '\\') {
                stringbuilder.append('\\');
            } else if (c0 == '"' || c0 == '\'') {
                if (i == 0) {
                    i = c0 == '"' ? 39 : 34;
                }

                if (i == c0) {
                    stringbuilder.append('\\');
                }
            }

            stringbuilder.append(c0);
        }

        if (i == 0) {
            i = 34;
        }

        stringbuilder.setCharAt(0, (char) i);
        stringbuilder.append((char) i);
        return stringbuilder.toString();
    }
}
