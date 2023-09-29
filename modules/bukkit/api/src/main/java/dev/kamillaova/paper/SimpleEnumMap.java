package dev.kamillaova.paper;

import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class SimpleEnumMap<K extends Enum<K>, V>
	extends AbstractReference2ObjectMap<K, V>
	implements Cloneable
{
	private final K @Unmodifiable [] keys;
	private final int keysCount;

	private final V[] data;
	private int size;

	private ReferenceSet<K> keySet;

	public SimpleEnumMap(K[] keys) {
		this.keys = keys;
		this.keysCount = keys.length;
		this.data = (V[]) new Object[keys.length];
		this.size = 0;
	}

	private SimpleEnumMap(K[] keys, V[] data, int size) {
		this.keys = keys;
		this.keysCount = keys.length;
		this.data = data;
		this.size = size;
	}

	@Override
	public V get(Object rawKey) {
		if (rawKey == null) return null;
		var key = (K) rawKey;
		var ret = this.data[key.ordinal()];
		if (ret != null) return ret;
		return defRetValue;
	}

	@Override
	public V put(K key, V value) {
		var data = this.data;
		var keyOrdinal = key.ordinal();
		var oldValue = data[keyOrdinal];
		data[keyOrdinal] = value;
		if (oldValue == null && value != null) {
			this.size++;
		} else if (oldValue != null && value == null) {
			this.size--;
		}
		if (oldValue != null) return oldValue;
		return defRetValue;
	}

	@Override
	public V remove(Object rawKey) {
		return put((K) rawKey, null);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public boolean containsKey(Object rawKey) {
		return get(rawKey) != null;
	}

	@Override
	public boolean containsValue(Object rawValue) {
		var value = (V) rawValue;
		if (value == null) return false;
		var data = this.data;
		var dataSize = data.length;
		for (var i = 0; i < dataSize; i++) {
			var val = data[i];
			if (value.equals(val)) return true;
		}
		return false;
	}

	@Override
	public void clear() {
		Arrays.fill(this.data, null);
		this.size = 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		var keys = this.keys;
		var data = this.data;

		for (int i = 0, max = data.length; i < max; i++) {
			var value = data[i];
			if (value != null) {
				action.accept(keys[i], value);
			}
		}
	}

	@Override
	public ReferenceSet<K> keySet() {
		var keySet = this.keySet;
		return keySet != null
			? keySet
			: (this.keySet = new KeySet());
	}

	@Override
	public ObjectCollection<V> values() {
		throw new UnsupportedOperationException(); // TODO: IMPORTANT!! IMPLEMENT IMPORTANT!!
	}

	@Override
	public ObjectSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet() {
		throw new UnsupportedOperationException(); // TODO: IMPORTANT!! IMPLEMENT IMPORTANT!!
	}

	@Override
	public SimpleEnumMap<K, V> clone() {
		return new SimpleEnumMap<>(this.keys, this.data.clone(), this.size);
	}

	private final class KeySet extends AbstractReferenceSet<K> {
		@Override
		public ObjectIterator<K> iterator() {
			return new ObjectIterator<>() {
				private int idx;
				private int realIdx;

				@Override
				public boolean hasNext() {
					return this.idx < SimpleEnumMap.this.size; // TODO: Check - nexts != null
				}

				@Override
				public K next() {
					var keys = SimpleEnumMap.this.keys;
					var keysCount = SimpleEnumMap.this.keysCount;
					var data = SimpleEnumMap.this.data;

					K key = null;
					var realIdx = this.realIdx;

					do {
						if (data[realIdx] != null) {
							key = keys[realIdx];
						}
						realIdx++;
					} while (keysCount <= realIdx && key == null);
					this.idx++;
					this.realIdx = realIdx;
					return key;
				}
			};
		}

		@Override
		public int size() {
			return SimpleEnumMap.this.size;
		}
	}
}
