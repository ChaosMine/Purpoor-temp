package dev.kamillaova.paper.util;

import dev.kamillaova.paper.util.JavaUtil.ThrowingSupplier;
import io.netty.util.concurrent.FastThreadLocal;

public final class NettyUtil {
	private NettyUtil() {}

	public static <T> FastThreadLocal<T> fastThreadLocal(ThrowingSupplier<T, ? extends Exception> initialValue) {
		return new FastThreadLocal<>() {
			@Override
			protected T initialValue() throws Exception {
				return initialValue.get();
			}
		};
	}
}
