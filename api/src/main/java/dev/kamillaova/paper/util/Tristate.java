package dev.kamillaova.paper.util;

import org.jetbrains.annotations.Unmodifiable;

public enum Tristate {
	FALSE(false),
	TRUE(true),
	UNSET(false),
	;

	private static final Tristate @Unmodifiable [] VALUES = values();

	private final boolean booleanValue;

	public static Tristate of(Boolean val) {
		return val == null ? UNSET : val ? TRUE : FALSE;
	}

	public static Tristate of(boolean val) {
		return val ? TRUE : FALSE;
	}

	Tristate(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public boolean isFalse() {
		return this == FALSE;
	}

	public boolean isTrue() {
		return this == TRUE;
	}

	public boolean isUnset() {
		return this == UNSET;
	}

	public boolean asBoolean() {
		return this.booleanValue;
	}
}
