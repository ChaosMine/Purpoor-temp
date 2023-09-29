package net.minecraft.nbt;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.PeekingIterator;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DynamicOpsNBT implements DynamicOps<NBTBase> {
	public static final DynamicOpsNBT a = new DynamicOpsNBT();

	protected DynamicOpsNBT() {
	}

	private static NBTList<?> a(byte var0, byte var1) {
		if (a(var0, var1, (byte) 4)) {
			return new NBTTagLongArray(new long[0]);
		} else if (a(var0, var1, (byte) 1)) {
			return new NBTTagByteArray(new byte[0]);
		} else {
			return a(var0, var1, (byte) 3) ? new NBTTagIntArray(new int[0]) : new NBTTagList();
		}
	}

	private static boolean a(byte var0, byte var1, byte var2) {
		return var0 == var2 && (var1 == var2 || var1 == 0);
	}

	private static <T extends NBTBase> void a(NBTList<T> var0, NBTBase var1, NBTBase var2) {
		if (var1 instanceof NBTList<?> var3) {
			var3.forEach(var1x -> var0.add((T) var1x));
		}

		var0.add((T) var2);
	}

	private static <T extends NBTBase> void a(NBTList<T> var0, NBTBase var1, List<NBTBase> var2) {
		if (var1 instanceof NBTList<?> var3) {
			var3.forEach(var1x -> var0.add((T) var1x));
		}

		var2.forEach(var1x -> var0.add((T) var1x));
	}

	public NBTBase empty() {
		return NBTTagEnd.b;
	}

	public <U> U convertTo(DynamicOps<U> var0, NBTBase var1) {
		switch (var1.getTypeId()) {
			case 0:
				return var0.empty();
			case 1:
				return var0.createByte(((NBTNumber) var1).asByte());
			case 2:
				return var0.createShort(((NBTNumber) var1).asShort());
			case 3:
				return var0.createInt(((NBTNumber) var1).asInt());
			case 4:
				return var0.createLong(((NBTNumber) var1).asLong());
			case 5:
				return var0.createFloat(((NBTNumber) var1).asFloat());
			case 6:
				return var0.createDouble(((NBTNumber) var1).asDouble());
			case 7:
				return var0.createByteList(ByteBuffer.wrap(((NBTTagByteArray) var1).getBytes()));
			case 8:
				return var0.createString(var1.asString());
			case 9:
				return this.convertList(var0, var1);
			case 10:
				return this.convertMap(var0, var1);
			case 11:
				return var0.createIntList(Arrays.stream(((NBTTagIntArray) var1).getInts()));
			case 12:
				return var0.createLongList(Arrays.stream(((NBTTagLongArray) var1).getLongs()));
			default:
				throw new IllegalStateException("Unknown tag type: " + var1);
		}
	}

	public DataResult<Number> getNumberValue(NBTBase var0) {
		return var0 instanceof NBTNumber ? DataResult.success(((NBTNumber) var0).k()) : DataResult.error("Not a number");
	}

	public NBTBase createNumeric(Number var0) {
		return NBTTagDouble.a(var0.doubleValue());
	}

	public NBTBase createByte(byte var0) {
		return NBTTagByte.a(var0);
	}

	public NBTBase createShort(short var0) {
		return NBTTagShort.a(var0);
	}

	public NBTBase createInt(int var0) {
		return NBTTagInt.a(var0);
	}

	public NBTBase createLong(long var0) {
		return NBTTagLong.a(var0);
	}

	public NBTBase createFloat(float var0) {
		return NBTTagFloat.a(var0);
	}

	public NBTBase createDouble(double var0) {
		return NBTTagDouble.a(var0);
	}

	public NBTBase createBoolean(boolean var0) {
		return NBTTagByte.a(var0);
	}

	public DataResult<String> getStringValue(NBTBase var0) {
		return var0 instanceof NBTTagString ? DataResult.success(var0.asString()) : DataResult.error("Not a string");
	}

	public NBTBase createString(String var0) {
		return NBTTagString.a(var0);
	}

	public DataResult<NBTBase> mergeToList(NBTBase var0, NBTBase var1) {
		if (!(var0 instanceof NBTList) && !(var0 instanceof NBTTagEnd)) {
			return DataResult.error("mergeToList called with not a list: " + var0, var0);
		} else {
			NBTList<?> var2 = a(var0 instanceof NBTList ? ((NBTList) var0).d_() : 0, var1.getTypeId());
			a(var2, var0, var1);
			return DataResult.success(var2);
		}
	}

	public DataResult<NBTBase> mergeToList(NBTBase var0, List<NBTBase> var1) {
		if (!(var0 instanceof NBTList) && !(var0 instanceof NBTTagEnd)) {
			return DataResult.error("mergeToList called with not a list: " + var0, var0);
		} else {
			NBTList<?> var2 = a(var0 instanceof NBTList ? ((NBTList) var0).d_() : 0, var1.stream().findFirst().map(NBTBase::getTypeId).orElse((byte) 0));
			a(var2, var0, var1);
			return DataResult.success(var2);
		}
	}

	public DataResult<NBTBase> mergeToMap(NBTBase var0, NBTBase var1, NBTBase var2) {
		if (!(var0 instanceof NBTTagCompound) && !(var0 instanceof NBTTagEnd)) {
			return DataResult.error("mergeToMap called with not a map: " + var0, var0);
		} else if (!(var1 instanceof NBTTagString)) {
			return DataResult.error("key is not a string: " + var1, var0);
		} else {
			NBTTagCompound var3 = new NBTTagCompound();
			if (var0 instanceof NBTTagCompound var4) {
				var4.getKeys().forEach(var2x -> var3.set(var2x, var4.get(var2x)));
			}

			var3.set(var1.asString(), var2);
			return DataResult.success(var3);
		}
	}

	public DataResult<NBTBase> mergeToMap(NBTBase var0, MapLike<NBTBase> var1) {
		if (!(var0 instanceof NBTTagCompound) && !(var0 instanceof NBTTagEnd)) {
			return DataResult.error("mergeToMap called with not a map: " + var0, var0);
		} else {
			NBTTagCompound var2 = new NBTTagCompound();
			if (var0 instanceof NBTTagCompound) {
				NBTTagCompound var3 = (NBTTagCompound) var0;
				var3.getKeys().forEach(var2x -> var2.set(var2x, var3.get(var2x)));
			}

			List<NBTBase> var3 = Lists.newArrayList();
			var1.entries().forEach(var2x -> {
				NBTBase var3x = var2x.getFirst();
				if (!(var3x instanceof NBTTagString)) {
					var3.add(var3x);
				} else {
					var2.set(var3x.asString(), var2x.getSecond());
				}
			});
			return !var3.isEmpty() ? DataResult.error("some keys are not strings: " + var3, var2) : DataResult.success(var2);
		}
	}

	public DataResult<Stream<Pair<NBTBase, NBTBase>>> getMapValues(NBTBase var0) {
		if (!(var0 instanceof NBTTagCompound var1)) {
			return DataResult.error("Not a map: " + var0);
		} else {
			return DataResult.success(var1.getKeys().stream().map(var1x -> Pair.of(this.createString(var1x), var1.get(var1x))));
		}
	}

	public DataResult<Consumer<BiConsumer<NBTBase, NBTBase>>> getMapEntries(NBTBase var0) {
		if (!(var0 instanceof NBTTagCompound var1)) {
			return DataResult.error("Not a map: " + var0);
		} else {
			return DataResult.success(var1x -> var1.getKeys().forEach(var2x -> var1x.accept(this.createString(var2x), var1.get(var2x))));
		}
	}

	public DataResult<MapLike<NBTBase>> getMap(NBTBase var0) {
		if (!(var0 instanceof NBTTagCompound var1)) {
			return DataResult.error("Not a map: " + var0);
		} else {
			return DataResult.success(new MapLike<NBTBase>() {
				@Nullable
				public NBTBase get(NBTBase var0) {
					return var1.get(var0.asString());
				}

				@Nullable
				public NBTBase get(String var0) {
					return var1.get(var0);
				}

				@Override
				public Stream<Pair<NBTBase, NBTBase>> entries() {
					return var1.getKeys().stream().map(var1xx -> Pair.of(DynamicOpsNBT.this.createString(var1xx), var1.get(var1xx)));
				}

				public String toString() {
					return "MapLike[" + var1 + "]";
				}
			});
		}
	}

	public NBTBase createMap(Stream<Pair<NBTBase, NBTBase>> var0) {
		NBTTagCompound var1 = new NBTTagCompound();
		var0.forEach(var1x -> var1.set((var1x.getFirst()).asString(), var1x.getSecond()));
		return var1;
	}

	public DataResult<Stream<NBTBase>> getStream(NBTBase var0) {
		return var0 instanceof NBTList ? DataResult.success(((NBTList) var0).stream()) : DataResult.error("Not a list");
	}

	public DataResult<Consumer<Consumer<NBTBase>>> getList(NBTBase var0) {
		if (var0 instanceof NBTList<?> var1) {
			return DataResult.success(var1::forEach);
		} else {
			return DataResult.error("Not a list: " + var0);
		}
	}

	public DataResult<ByteBuffer> getByteBuffer(NBTBase var0) {
		return var0 instanceof NBTTagByteArray ? DataResult.success(ByteBuffer.wrap(((NBTTagByteArray) var0).getBytes())) : DynamicOps.super.getByteBuffer(var0);
	}

	public NBTBase createByteList(ByteBuffer var0) {
		return new NBTTagByteArray(DataFixUtils.toArray(var0));
	}

	public DataResult<IntStream> getIntStream(NBTBase var0) {
		return var0 instanceof NBTTagIntArray ? DataResult.success(Arrays.stream(((NBTTagIntArray) var0).getInts())) : DynamicOps.super.getIntStream(var0);
	}

	public NBTBase createIntList(IntStream var0) {
		return new NBTTagIntArray(var0.toArray());
	}

	public DataResult<LongStream> getLongStream(NBTBase var0) {
		return var0 instanceof NBTTagLongArray ? DataResult.success(Arrays.stream(((NBTTagLongArray) var0).getLongs())) : DynamicOps.super.getLongStream(var0);
	}

	public NBTBase createLongList(LongStream var0) {
		return new NBTTagLongArray(var0.toArray());
	}

	public NBTBase createList(Stream<NBTBase> var0) {
		PeekingIterator<NBTBase> var1 = Iterators.peekingIterator(var0.iterator());
		if (!var1.hasNext()) {
			return new NBTTagList();
		} else {
			NBTBase var2 = var1.peek();
			if (var2 instanceof NBTTagByte) {
				List<Byte> var3 = Lists.newArrayList(Iterators.transform(var1, var0x -> ((NBTTagByte) var0x).asByte()));
				return new NBTTagByteArray(var3);
			} else if (var2 instanceof NBTTagInt) {
				List<Integer> var3 = Lists.newArrayList(Iterators.transform(var1, var0x -> ((NBTTagInt) var0x).asInt()));
				return new NBTTagIntArray(var3);
			} else if (var2 instanceof NBTTagLong) {
				List<Long> var3 = Lists.newArrayList(Iterators.transform(var1, var0x -> ((NBTTagLong) var0x).asLong()));
				return new NBTTagLongArray(var3);
			} else {
				NBTTagList var3 = new NBTTagList();

				while (var1.hasNext()) {
					NBTBase var4 = var1.next();
					if (!(var4 instanceof NBTTagEnd)) {
						var3.add(var4);
					}
				}

				return var3;
			}
		}
	}

	public NBTBase remove(NBTBase var0, String var1) {
		if (var0 instanceof NBTTagCompound var2) {
			NBTTagCompound var3 = new NBTTagCompound();
			var2.getKeys().stream().filter(var1x -> !Objects.equals(var1x, var1)).forEach(var2x -> var3.set(var2x, var2.get(var2x)));
			return var3;
		} else {
			return var0;
		}
	}

	public String toString() {
		return "NBT";
	}

	@Override
	public RecordBuilder<NBTBase> mapBuilder() {
		return new DynamicOpsNBT.a();
	}

	class a extends RecordBuilder.AbstractStringBuilder<NBTBase, NBTTagCompound> {
		protected a() {
			super(DynamicOpsNBT.this);
		}

		protected NBTTagCompound initBuilder() {
			return new NBTTagCompound();
		}

		protected NBTTagCompound append(String var0, NBTBase var1, NBTTagCompound var2) {
			var2.set(var0, var1);
			return var2;
		}

		protected DataResult<NBTBase> build(NBTTagCompound var0, NBTBase var1) {
			if (var1 == null || var1 == NBTTagEnd.b) {
				return DataResult.success(var0);
			} else if (!(var1 instanceof NBTTagCompound)) {
				return DataResult.error("mergeToMap called with not a map: " + var1, var1);
			} else {
				NBTTagCompound var2 = new NBTTagCompound(Maps.newHashMap(((NBTTagCompound) var1).h()));

				for (Map.Entry<String, NBTBase> var4 : var0.h().entrySet()) {
					var2.set(var4.getKey(), var4.getValue());
				}

				return DataResult.success(var2);
			}
		}
	}
}
