package net.minecraft.advancements;

import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.advancement.FrameType;
import org.jetbrains.annotations.Unmodifiable;

public enum AdvancementFrameType {

    // Purpur start
    TASK("task", 0, EnumChatFormat.GREEN, FrameType.TASK),
    CHALLENGE("challenge", 26, EnumChatFormat.DARK_PURPLE, FrameType.CHALLENGE),
    GOAL("goal", 52, EnumChatFormat.GREEN, FrameType.GOAL);
    // Purpur end

    public static final AdvancementFrameType @Unmodifiable [] VALUES = values();

    private final String d;
    private final int e;
    private final EnumChatFormat f;
    private final IChatBaseComponent g;

    // Purpur start
    public final FrameType bukkit;

    AdvancementFrameType(String s, int i, EnumChatFormat enumchatformat, FrameType bukkit) {
        this.bukkit = bukkit;
        // Purpur end
        this.d = s;
        this.e = i;
        this.f = enumchatformat;
        this.g = new ChatMessage("advancements.toast." + s);
    }

    public final String getId() { return this.a(); } // Paper - OBFHELPER
    public String a() {
        return this.d;
    }

    public static AdvancementFrameType a(String s) {
        AdvancementFrameType[] aadvancementframetype = values();
        int i = aadvancementframetype.length;

        for (int j = 0; j < i; ++j) {
            AdvancementFrameType advancementframetype = aadvancementframetype[j];

            if (advancementframetype.d.equals(s)) {
                return advancementframetype;
            }
        }

        throw new IllegalArgumentException("Unknown frame type '" + s + "'");
    }

    public EnumChatFormat c() {
        return this.f;
    }
}
