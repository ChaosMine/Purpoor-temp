package co.aikar.util;

import com.google.common.collect.ForwardingMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Counter<T> extends ForwardingMap<T, Long> {
	private final Object2LongMap<T> counts = new Object2LongOpenHashMap<>();

	public long decrement(@Nullable T key) {
		return increment(key, -1);
	}

	public long increment(@Nullable T key) {
		return increment(key, 1);
	}

	public long decrement(@Nullable T key, long amount) {
		return increment(key, -amount);
	}

	public long increment(@Nullable T key, long amount) {
		var count = this.getCount(key);
		count += amount;
		this.counts.put(key, count);
		return count;
	}

	public long getCount(@Nullable T key) {
		return this.counts.getOrDefault(key, 0L);
	}

	@NotNull
	@Override
	protected Map<T, Long> delegate() {
		return this.counts;
	}
}
