package net.minecraft.world;

import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.util.Arrays;

import static java.util.Comparator.comparingInt;

public enum EnumDifficulty {
	PEACEFUL(0, "peaceful"),
	EASY(1, "easy"),
	NORMAL(2, "normal"),
	HARD(3, "hard"),
	;

	public static final EnumDifficulty @Unmodifiable [] VALUES = values();

	private static final EnumDifficulty[] e = Arrays.stream(VALUES)
		.sorted(comparingInt(EnumDifficulty::a))
		.toArray(EnumDifficulty[]::new);

	private final int f;
	private final String g;

	EnumDifficulty(int i, String s) {
		this.f = i;
		this.g = s;
	}

	public int getId() {
		return a();
	} // Purpur - OBFHELPER

	public int a() {
		return this.f;
	}

	public IChatBaseComponent b() {
		return new ChatMessage("options.difficulty." + this.g);
	}

	public static EnumDifficulty getById(int i) {
		return EnumDifficulty.e[i % EnumDifficulty.e.length];
	}

	@Nullable
	public static EnumDifficulty a(String s) {
		EnumDifficulty[] aenumdifficulty = values();
		int i = aenumdifficulty.length;

		for (int j = 0; j < i; ++j) {
			EnumDifficulty enumdifficulty = aenumdifficulty[j];

			if (enumdifficulty.g.equals(s)) {
				return enumdifficulty;
			}
		}

		return null;
	}

	public String c() {
		return this.g;
	}
}
