package dev.kamillaova.paper.pool;

import io.netty.util.internal.InternalThreadLocalMap;

public final class StringBuilderPool {
	public static StringBuilder take() {
		return InternalThreadLocalMap.get().stringBuilder();
	}
}
