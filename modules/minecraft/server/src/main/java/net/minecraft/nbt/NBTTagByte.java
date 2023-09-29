package net.minecraft.nbt;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTNumber {
	public static final NBTTagType<NBTTagByte> a = new NBTTagType<>() {
		@Override
		public NBTTagByte b(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
			nbtreadlimiter.a(72L);
			return NBTTagByte.a(datainput.readByte());
		}

		@Override
		public String a() {
			return "BYTE";
		}

		@Override
		public String b() {
			return "TAG_Byte";
		}

		@Override
		public boolean c() {
			return true;
		}
	};
	public static final NBTTagByte b = a((byte) 0);
	public static final NBTTagByte c = a((byte) 1);
	private final byte data;

	private NBTTagByte(byte b0) {
		this.data = b0;
	}

	public static NBTTagByte a(byte b0) {
		return NBTTagByte.a_.a[128 + b0];
	}

	public static NBTTagByte a(boolean flag) {
		return flag ? NBTTagByte.c : NBTTagByte.b;
	}

	@Override
	public void write(DataOutput dataoutput) throws IOException {
		dataoutput.writeByte(this.data);
	}

	@Override
	public byte getTypeId() {
		return 1;
	}

	@Override
	public NBTTagType<NBTTagByte> b() {
		return NBTTagByte.a;
	}

	@Override
	public String toString() {
		return this.data + "b";
	}

	@Override
	public NBTTagByte clone() {
		return this;
	}

	public boolean equals(Object object) {
		return this == object || object instanceof NBTTagByte && this.data == ((NBTTagByte) object).data;
	}

	public int hashCode() {
		return this.data;
	}

	@Override
	public IChatBaseComponent a(String s, int i) {
		IChatMutableComponent ichatmutablecomponent = (new ChatComponentText("b")).a(NBTTagByte.g);

		return (new ChatComponentText(String.valueOf(this.data))).addSibling(ichatmutablecomponent).a(NBTTagByte.f);
	}

	@Override
	public long asLong() {
		return this.data;
	}

	@Override
	public int asInt() {
		return this.data;
	}

	@Override
	public short asShort() {
		return this.data;
	}

	@Override
	public byte asByte() {
		return this.data;
	}

	@Override
	public double asDouble() {
		return this.data;
	}

	@Override
	public float asFloat() {
		return this.data;
	}

	@Override
	public Number k() {
		return this.data;
	}

	static class a_ {
		private static final NBTTagByte[] a = new NBTTagByte[256];

		static {
			for (int i = 0; i < a.length; ++i) {
				a[i] = new NBTTagByte((byte) (i - 128));
			}
		}
	}
}
