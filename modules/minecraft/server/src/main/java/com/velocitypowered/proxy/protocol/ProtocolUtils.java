/*
 * Copyright (C) 2018-2023 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.protocol;

import io.netty.buffer.ByteBuf;
import me.steinborn.krypton.mod.shared.network.util.VarIntUtil;
import me.steinborn.krypton.mod.shared.network.util.WellKnownExceptions;

/**
 * Utilities for writing and reading data in the Minecraft protocol.
 */
public final class ProtocolUtils {
	private ProtocolUtils() {}

	/**
	 * Reads a Minecraft-style VarInt from the specified {@code buf}.
	 *
	 * @param buf the buffer to read from
	 * @return the decoded VarInt
	 */
	public static int readVarInt(ByteBuf buf) {
		int read = readVarIntSafely(buf);
		if (read == Integer.MIN_VALUE) {
			throw WellKnownExceptions.BAD_VARINT_CACHED;
		}
		return read;
	}

	/**
	 * Reads a Minecraft-style VarInt from the specified {@code buf}. The difference between this
	 * method and {@link #readVarInt(ByteBuf)} is that this function returns a sentinel value if the
	 * varint is invalid.
	 *
	 * @param buf the buffer to read from
	 * @return the decoded VarInt, or {@code Integer.MIN_VALUE} if the varint is invalid
	 */
	public static int readVarIntSafely(ByteBuf buf) {
		int i = 0;
		int maxRead = Math.min(5, buf.readableBytes());
		for (int j = 0; j < maxRead; j++) {
			int k = buf.readByte();
			i |= (k & 0x7F) << j * 7;
			if ((k & 0x80) != 128) {
				return i;
			}
		}
		return Integer.MIN_VALUE;
	}

	/**
	 * Writes a Minecraft-style VarInt to the specified {@code buf}.
	 *
	 * @param buf   the buffer to read from
	 * @param value the integer to write
	 */
	public static void writeVarInt(ByteBuf buf, int value) {
		// Peel the one and two byte count cases explicitly as they are the most common VarInt sizes
		// that the proxy will write, to improve inlining.
		if ((value & (0xFFFFFFFF << 7)) == 0) {
			buf.writeByte(value);
		} else if ((value & (0xFFFFFFFF << 14)) == 0) {
			int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
			buf.writeShort(w);
		} else {
			writeVarIntFull(buf, value);
		}
	}

	private static void writeVarIntFull(ByteBuf buf, int value) {
		// See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
		if ((value & (0xFFFFFFFF << 7)) == 0) {
			buf.writeByte(value);
		} else if ((value & (0xFFFFFFFF << 14)) == 0) {
			int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
			buf.writeShort(w);
		} else if ((value & (0xFFFFFFFF << 21)) == 0) {
			int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
			buf.writeMedium(w);
		} else if ((value & (0xFFFFFFFF << 28)) == 0) {
			int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
				| ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
			buf.writeInt(w);
		} else {
			int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
				| ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
			buf.writeInt(w);
			buf.writeByte(value >>> 28);
		}
	}

	/**
	 * Writes the specified {@code value} as a 21-bit Minecraft VarInt to the specified {@code buf}.
	 * The upper 11 bits will be discarded.
	 *
	 * @param buf   the buffer to read from
	 * @param value the integer to write
	 */
	public static void write21BitVarInt(ByteBuf buf, int value) {
		// See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
		int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
		buf.writeMedium(w);
	}
}
