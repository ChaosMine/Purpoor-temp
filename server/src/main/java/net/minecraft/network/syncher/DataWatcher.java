package net.minecraft.network.syncher;

import com.google.common.collect.Lists;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DataWatcher {
	private static final Reference2IntOpenHashMap<Class<? extends Entity>> b = new Reference2IntOpenHashMap<>();

	public boolean registrationLocked;

	private final Entity entity;
	private final Int2ObjectOpenHashMap<Item<?>> entries = new Int2ObjectOpenHashMap<>();
	private boolean f = true;
	private boolean g;

	public DataWatcher(Entity entity) {
		this.entity = entity;
	}

	public static <T> DataWatcherObject<T> a(Class<? extends Entity> oclass, DataWatcherSerializer<T> datawatcherserializer) {
		int i;

		if (DataWatcher.b.containsKey(oclass)) {
			i = DataWatcher.b.getInt(oclass) + 1;
		} else {
			var j = 0;
			Class<?> oclass2 = oclass;

			while (oclass2 != Entity.class) {
				oclass2 = oclass2.getSuperclass();
				if (DataWatcher.b.containsKey(oclass2)) {
					j = DataWatcher.b.getInt(oclass2) + 1;
					break;
				}
			}

			i = j;
		}

		if (i > 254) {
			throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
		} else {
			DataWatcher.b.put(oclass, i);
			return datawatcherserializer.a(i);
		}
	}

	public <T> void register(DataWatcherObject<T> datawatcherobject, T t0) {
		if (this.registrationLocked) {
			throw new IllegalStateException("Registering datawatcher object after entity initialization"); // Spigot
		}
		var i = datawatcherobject.a();

		if (i > 254) {
			throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
		} else if (this.entries.containsKey(i)) {
			throw new IllegalArgumentException("Duplicate id value for " + i + "!");
		} else if (DataWatcherRegistry.b(datawatcherobject.b()) < 0) {
			throw new IllegalArgumentException("Unregistered serializer " + datawatcherobject.b() + " for " + i + "!");
		} else {
			this.registerObject(datawatcherobject, t0);
		}
	}

	private <T> void registerObject(DataWatcherObject<T> datawatcherobject, T t0) {
		var datawatcher_item = new Item<>(datawatcherobject, t0);

		this.entries.put(datawatcherobject.a(), datawatcher_item);
		this.f = false;
	}

	@SuppressWarnings("unchecked")
	private <T> Item<T> b(DataWatcherObject<T> datawatcherobject) {
		return (Item<T>) this.entries.get(datawatcherobject.a());
	}

	public <T> T get(DataWatcherObject<T> datawatcherobject) {
		return this.b(datawatcherobject).b();
	}

	public <T> void set(DataWatcherObject<T> datawatcherobject, T t0) {
		var datawatcher_item = this.b(datawatcherobject);

		if (!Objects.equals(t0, datawatcher_item.b())) {
			datawatcher_item.a(t0);
			this.entity.a(datawatcherobject);
			datawatcher_item.a(true);
			this.g = true;
		}

	}

	public <T> void markDirty(DataWatcherObject<T> datawatcherobject) {
		this.b(datawatcherobject).a(true);
		this.g = true;
	}

	public boolean a() {
		return this.g;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void a(List<Item<?>> list, PacketDataSerializer packetdataserializer) throws IOException {
		if (list != null) {
			var i = 0;

			for (var j = list.size(); i < j; ++i) {
				a(packetdataserializer, (Item) list.get(i));
			}
		}

		packetdataserializer.writeByte(255);
	}

	@Nullable
	public List<Item<?>> b() {
		List<Item<?>> list = null;

		if (this.g) {
			for (var item : this.entries.values()) {
				if (item.c()) {
					item.a(false);
					if (list == null) {
						list = Lists.newArrayList();
					}

					list.add(item.d());
				}
			}
		}

		this.g = false;
		return list;
	}

	@Nullable
	public List<Item<?>> c() {
		List<Item<?>> list = null;

		Item<?> datawatcher_item;

		for (Iterator<Item<?>> iterator = this.entries.values().iterator(); iterator.hasNext(); list.add(datawatcher_item.d())) {
			datawatcher_item = iterator.next();
			if (list == null) {
				list = Lists.newArrayList();
			}
		}

		return list;
	}

	private static <T> void a(PacketDataSerializer packetdataserializer, Item<T> datawatcher_item) {
		var datawatcherobject = datawatcher_item.a();
		var i = DataWatcherRegistry.b(datawatcherobject.b());

		if (i < 0) {
			throw new EncoderException("Unknown serializer type " + datawatcherobject.b());
		} else {
			packetdataserializer.writeByte(datawatcherobject.a());
			packetdataserializer.d(i);
			datawatcherobject.b().a(packetdataserializer, datawatcher_item.b());
		}
	}

	@Nullable
	public static List<Item<?>> a(PacketDataSerializer packetdataserializer) throws IOException {
		ArrayList<Item<?>> arraylist = null;

		short short0;

		while ((short0 = packetdataserializer.readUnsignedByte()) != 255) {
			if (arraylist == null) {
				arraylist = new ArrayList<>();
			}

			var i = packetdataserializer.i();
			var datawatcherserializer = DataWatcherRegistry.a(i);

			if (datawatcherserializer == null) {
				throw new DecoderException("Unknown serializer type " + i);
			}

			arraylist.add(a(packetdataserializer, short0, datawatcherserializer));
		}

		return arraylist;
	}

	private static <T> Item<T> a(PacketDataSerializer packetdataserializer, int i, DataWatcherSerializer<T> datawatcherserializer) {
		return new Item<>(datawatcherserializer.a(i), datawatcherserializer.a(packetdataserializer));
	}

	public boolean d() {
		return this.f;
	}

	public void e() {
		this.g = false;

		for (var item : this.entries.values()) {
			item.a(false);
		}
	}

	public static class Item<T> {
		private final DataWatcherObject<T> a;
		private T b;
		private boolean c;

		public Item(DataWatcherObject<T> datawatcherobject, T t0) {
			this.a = datawatcherobject;
			this.b = t0;
			this.c = true;
		}

		public DataWatcherObject<T> a() {
			return this.a;
		}

		public void a(T t0) {
			this.b = t0;
		}

		public T b() {
			return this.b;
		}

		public boolean c() {
			return this.c;
		}

		public void a(boolean flag) {
			this.c = flag;
		}

		public Item<T> d() {
			return new Item<>(this.a, this.a.b().a(this.b));
		}
	}
}
