package net.minecraft.nbt;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTNumber {
	private final int data;	public static final NBTTagType<NBTTagInt> a = new NBTTagType<>() {
		@Override
		public NBTTagInt b(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
			nbtreadlimiter.a(96L);
			return NBTTagInt.a(datainput.readInt());
		}

		@Override
		public String a() {
			return "INT";
		}

		@Override
		public String b() {
			return "TAG_Int";
		}

		@Override
		public boolean c() {
			return true;
		}
	};
	private NBTTagInt(int i) {
		this.data = i;
	}

	public static NBTTagInt a(int i) {
		return i >= -128 && i <= 1024 ? NBTTagInt.a_.a[i + 128] : new NBTTagInt(i);
	}

	@Override
	public void write(DataOutput dataoutput) throws IOException {
		dataoutput.writeInt(this.data);
	}

	@Override
	public byte getTypeId() {
		return 3;
	}

	@Override
	public NBTTagType<NBTTagInt> b() {
		return NBTTagInt.a;
	}

	@Override
	public String toString() {
		return String.valueOf(this.data);
	}

	@Override
	public NBTTagInt clone() {
		return this;
	}

	public boolean equals(Object object) {
		return this == object || object instanceof NBTTagInt && this.data == ((NBTTagInt) object).data;
	}

	public int hashCode() {
		return this.data;
	}

	@Override
	public IChatBaseComponent a(String s, int i) {
		return (new ChatComponentText(String.valueOf(this.data))).a(NBTTagInt.f);
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
		return (short) (this.data & '\uffff');
	}

	@Override
	public byte asByte() {
		return (byte) (this.data & 255);
	}

	@Override
	public double asDouble() {
		return this.data;
	}

	@Override
	public float asFloat() {
		return (float) this.data;
	}

	@Override
	public Number k() {
		return this.data;
	}

	static class a_ {
		static final NBTTagInt[] a = new NBTTagInt[1153];

		static {
			for (int i = 0; i < a.length; ++i) {
				a[i] = new NBTTagInt(-128 + i);
			}

		}
	}
}
