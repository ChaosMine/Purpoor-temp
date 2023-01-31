package net.minecraft.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.WrappedByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.papermc.paper.adventure.PaperAdventure;
import me.steinborn.krypton.mod.shared.network.util.VarIntUtil;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTReadLimiter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Unmodifiable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import javax.annotation.Nullable;
import jdk.internal.ValueBased;

@ValueBased
public class PacketDataSerializer extends WrappedByteBuf {
	public Locale adventure$locale;

	public PacketDataSerializer(ByteBuf bytebuf) {
		super(bytebuf);
	}

	public static int countBytes(int i) {
		return PacketDataSerializer.a(i);
	} // Paper - OBFHELPER

	public static int varIntLen(int i) {
		return a(i);
	} // OBFHELPER

	public static int a(int i) {
		return VarIntUtil.getVarIntLength(i);
	}

	public <T> T a(Codec<T> codec) throws IOException {
		var nbttagcompound = this.m();
		var dataresult = codec.parse(DynamicOpsNBT.a, nbttagcompound);

		if (dataresult.error().isPresent()) {
			throw new IOException("Failed to decode: " + dataresult.error().get().message() + " " + nbttagcompound);
		} else {
			return dataresult.result().orElseThrow();
		}
	}

	public <T> void a(Codec<T> codec, T t0) throws IOException {
		var dataresult = codec.encodeStart(DynamicOpsNBT.a, t0);

		if (dataresult.error().isPresent()) {
			throw new IOException("Failed to encode: " + dataresult.error().get().message() + " " + t0);
		} else {
			this.a((NBTTagCompound) dataresult.result().get());
		}
	}

	public PacketDataSerializer a(byte[] abyte) {
		this.d(abyte.length);
		buf.writeBytes(abyte);
		return this;
	}

	public byte[] a() {
		return this.b(buf.readableBytes());
	}

	public byte[] b(int i) {
		var j = this.i();

		if (j > i) {
			throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + i);
		} else {
			var abyte = new byte[j];

			buf.readBytes(abyte);
			return abyte;
		}
	}

	public PacketDataSerializer a(int[] aint) {
		this.d(aint.length);
		var i = aint.length;

		for (var j = 0; j < i; ++j) {
			var k = aint[j];

			this.d(k);
		}

		return this;
	}

	public int[] b() {
		return this.c(buf.readableBytes());
	}

	public int[] c(int i) {
		var j = this.i();

		if (j > i) {
			throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + i);
		} else {
			var aint = new int[j];

			for (var k = 0; k < aint.length; ++k) {
				aint[k] = this.readVarInt();
			}

			return aint;
		}
	}

	public PacketDataSerializer a(long[] along) {
		this.d(along.length);
		var i = along.length;

		for (var j = 0; j < i; ++j) {
			var k = along[j];

			buf.writeLong(k);
		}

		return this;
	}

	public BlockPosition e() {
		return BlockPosition.fromLong(buf.readLong());
	}

	public PacketDataSerializer a(BlockPosition blockposition) {
		buf.writeLong(blockposition.asLong());
		return this;
	}

	public IChatBaseComponent h() {
		return IChatBaseComponent.ChatSerializer.a(this.e(262144));
	}

	// Paper start
	public PacketDataSerializer writeComponent(net.kyori.adventure.text.Component component) {
		this.writeUtf(PaperAdventure.asJsonString(component, this.adventure$locale), 262144);
		return this;
	}

	@Deprecated
	public PacketDataSerializer writeComponent(net.md_5.bungee.api.chat.BaseComponent[] component) {
		return this.writeUtf(net.md_5.bungee.chat.ComponentSerializer.toString(component), 262144);
	}
	// Paper end

	public PacketDataSerializer a(IChatBaseComponent ichatbasecomponent) {
		return this.writeUtf(PaperAdventure.asJsonString(ichatbasecomponent, this.adventure$locale), 262144); // Paper
	}

	public <T extends Enum<T>> T a(Class<T> oclass) {
		return ((T[]) oclass.getEnumConstants())[this.i()]; // CraftBukkit - fix decompile error
	}

	public PacketDataSerializer a(Enum<?> oenum) {
		return this.d(oenum.ordinal());
	}

	public <T extends Enum<T>> T readEnum(T @Unmodifiable [] values) {
		return values[this.readVarInt()];
	}

	public PacketDataSerializer writeEnum(Enum<?> value) {
		this.writeVarInt(value.ordinal());
		return this;
	}

	public int readVarInt() {
		return i();
	} // Paper - OBFHELPER

	public int i() {
		return ProtocolUtils.readVarInt(buf);
	}

	public long j() {
		var i = 0L;
		var j = 0;

		byte b0;

		do {
			b0 = buf.readByte();
			i |= (long) (b0 & 127) << j++ * 7;
			if (j > 10) {
				throw new RuntimeException("VarLong too big");
			}
		} while ((b0 & 128) == 128);

		return i;
	}

	public PacketDataSerializer a(UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
		return this;
	}

	public UUID readUUID() {
		return k();
	} // Paper - OBFHELPER

	public UUID k() {
		return new UUID(buf.readLong(), buf.readLong());
	}

	public PacketDataSerializer writeVarInt(int i) {
		return d(i);
	} // OBFHELPER

	public PacketDataSerializer d(int value) {
		ProtocolUtils.writeVarInt(buf, value);
		return this;
	}

	public PacketDataSerializer writeVarInt21(int value) {
		ProtocolUtils.write21BitVarInt(buf, value);
		return this;
	}

	public PacketDataSerializer b(long i) {
		while ((i & -128L) != 0L) {
			buf.writeByte((int) (i & 127L) | 128);
			i >>>= 7;
		}

		buf.writeByte((int) i);
		return this;
	}

	public PacketDataSerializer a(@Nullable NBTTagCompound nbttagcompound) {
		if (nbttagcompound == null) {
			buf.writeByte(0);
		} else {
			try {
				NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) new ByteBufOutputStream(buf));
			} catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
				throw new EncoderException(ioexception);
			}
		}

		return this;
	}

	@Nullable
	public NBTTagCompound l() {
		return this.a(new NBTReadLimiter(50000L));
	}

	@Nullable
	public NBTTagCompound m() {
		return this.a(NBTReadLimiter.a);
	}

	@Nullable
	public NBTTagCompound a(NBTReadLimiter nbtreadlimiter) {
		var i = buf.readerIndex();
		var b0 = buf.readByte();

		if (b0 == 0) {
			return null;
		} else {
			buf.readerIndex(i);

			try {
				return NBTCompressedStreamTools.a(new ByteBufInputStream(buf), nbtreadlimiter);
			} catch (IOException ioexception) {
				throw new EncoderException(ioexception);
			}
		}
	}

	public PacketDataSerializer a(ItemStack itemstack) {
		if (itemstack.isEmpty() || itemstack.getItem() == null) { // CraftBukkit - NPE fix itemstack.getItem()
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			var item = itemstack.getItem();

			this.d(Item.getId(item));
			buf.writeByte(itemstack.getCount());
			NBTTagCompound nbttagcompound = null;

			if (item.usesDurability() || item.n()) {
				// Spigot start - filter
				itemstack = itemstack.cloneItemStack();
				//CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack)); // Paper - This is no longer needed due to NBT being supported
				// Spigot end
				nbttagcompound = itemstack.getTag();
				// Paper start
				if (nbttagcompound != null && nbttagcompound.hasKeyOfType("SkullOwner", 10)) {
					var owner = nbttagcompound.getCompound("SkullOwner");
					if (owner.hasUUID("Id")) {
						nbttagcompound.setUUID("SkullOwnerOrig", owner.getUUID("Id"));
						TileEntitySkull.sanitizeUUID(owner);
					}
				}
				// Paper end
			}

			this.a(nbttagcompound);
		}

		return this;
	}

	public ItemStack n() {
		if (!buf.readBoolean()) {
			return ItemStack.b;
		} else {
			var i = this.i();
			var b0 = buf.readByte();
			var itemstack = new ItemStack(Item.getById(i), b0);

			itemstack.setTag(this.l());
			// CraftBukkit start
			if (itemstack.getTag() != null) {
				// Paper start - Fix skulls of same owner - restore orig ID since we changed it on send to client
				if (itemstack.tag.hasKey("SkullOwnerOrig")) {
					var owner = itemstack.tag.getCompound("SkullOwner");
					if (itemstack.tag.hasKey("SkullOwnerOrig")) {
						owner.map.put("Id", itemstack.tag.map.get("SkullOwnerOrig"));
						itemstack.tag.remove("SkullOwnerOrig");
					}
				}
				// Paper end
				// CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack)); // Paper - This is no longer needed due to NBT being supported
			}
			// CraftBukkit end
			return itemstack;
		}
	}

	public String readUTF(int maxLength) {
		return this.e(maxLength);
	} // Paper - OBFHELPER

	public String e(int i) {
		var j = this.i();

		if (j > i * 4) {
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
		} else if (j < 0) {
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			var s = this.toString(buf.readerIndex(), j, StandardCharsets.UTF_8);

			buf.readerIndex(buf.readerIndex() + j);
			if (s.length() > i) {
				throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
			} else {
				return s;
			}
		}
	}

	public PacketDataSerializer a(String s) {
		return this.a(s, 32767);
	}

	public PacketDataSerializer writeUtf(String string, int maxLength) {
		return this.a(string, maxLength);
	} // Paper - OBFHELPER

	public PacketDataSerializer a(String string, int i) {
		if (string.length() > i) {
			throw new EncoderException("String too big (was " + string.length() + " characters, max " + i + ")");
		}
		var utf8Bytes = ByteBufUtil.utf8Bytes(string);
		if (utf8Bytes > i * 3) {
			throw new EncoderException("String too big (was " + utf8Bytes + " bytes encoded, max " + (i * 3) + ")");
		} else {
			ProtocolUtils.writeVarInt(buf, utf8Bytes);
			buf.writeCharSequence(string, StandardCharsets.UTF_8);
		}
		return this;
	}

	public MinecraftKey p() {
		return new MinecraftKey(this.e(32767));
	}

	public PacketDataSerializer a(MinecraftKey minecraftkey) {
		this.a(minecraftkey.toString());
		return this;
	}

	public Date q() {
		return new Date(buf.readLong());
	}

	public PacketDataSerializer a(Date date) {
		buf.writeLong(date.getTime());
		return this;
	}

	public MovingObjectPositionBlock r() {
		var blockposition = this.e();
		var enumdirection = this.readEnum(EnumDirection.VALUES);
		var f = buf.readFloat();
		var f1 = buf.readFloat();
		var f2 = buf.readFloat();
		var flag = buf.readBoolean();

		return new MovingObjectPositionBlock(new Vec3D((double) blockposition.getX() + (double) f, (double) blockposition.getY() + (double) f1, (double) blockposition.getZ() + (double) f2), enumdirection, blockposition, flag);
	}

	public void a(MovingObjectPositionBlock movingobjectpositionblock) {
		var blockposition = movingobjectpositionblock.getBlockPosition();

		this.a(blockposition);
		this.writeEnum(movingobjectpositionblock.getDirection());
		var vec3d = movingobjectpositionblock.getPos();

		buf.writeFloat((float) (vec3d.x - (double) blockposition.getX()));
		buf.writeFloat((float) (vec3d.y - (double) blockposition.getY()));
		buf.writeFloat((float) (vec3d.z - (double) blockposition.getZ()));
		buf.writeBoolean(movingobjectpositionblock.d());
	}
}
