package net.minecraft.sounds;

import org.jetbrains.annotations.Unmodifiable;

public enum SoundCategory {
	MASTER("master"),
	MUSIC("music"),
	RECORDS("record"),
	WEATHER("weather"),
	BLOCKS("block"),
	HOSTILE("hostile"),
	NEUTRAL("neutral"),
	PLAYERS("player"),
	AMBIENT("ambient"),
	VOICE("voice"),
	;

	public static final SoundCategory @Unmodifiable [] VALUES = values();

	private final String l;

	SoundCategory(String s) {
		this.l = s;
	}

	public String a() {
		return this.l;
	}
}
