package dev.kamillaova.paper.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import jdk.internal.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.invoke.MethodType.methodType;

@SuppressWarnings({"deprecation", "unchecked", "removal", "RedundantSuppression"})
public final class JavaUtil {
	public static final Unsafe UNSAFE;
	public static final MethodHandles.Lookup IMPL_LOOKUP;

	static {
		try {
			var theUnsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafeField.setAccessible(true);
			var sunUnsafe = (sun.misc.Unsafe) theUnsafeField.get(null);

			var implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
			IMPL_LOOKUP = (MethodHandles.Lookup) sunUnsafe.getObject(
				sunUnsafe.staticFieldBase(implLookupField),
				sunUnsafe.staticFieldOffset(implLookupField)
			);

			IMPL_LOOKUP.findVirtual(
				Module.class,
				"implAddOpens",
				methodType(void.class, String.class)
			).invokeExact(Object.class.getModule(), "jdk.internal.misc");

			UNSAFE = Unsafe.getUnsafe();
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	private JavaUtil() {}

	public static <T extends Throwable> RuntimeException sneaky(Throwable t) throws T {
		throw (T) t;
	}

	public static <T> T sneaky(ThrowingSupplier<T, ?> supplier) {
		try {
			return supplier.get();
		} catch (Throwable t) {
			throw sneaky(t);
		}
	}

	@SuppressWarnings("TypeParameterUnusedInFormals")
	public static <T> T cast(Object obj) {
		return (T) obj;
	}

	@SuppressWarnings("EmptyMethod")
	public static void unused(@SuppressWarnings("unused") Object unused) { }

	public static <T, R, E extends Throwable> R run(T t, ThrowingFunction<T, R, E> function) throws E {
		return function.apply(t);
	}

	public static <T> List<T> append(List<T> to, T... what) {
		var copy = new ObjectArrayList<>(to);
		copy.ensureCapacity(to.size() + what.length);
		Collections.addAll(copy, what);
		return Collections.unmodifiableList(copy);
	}

	public static <T> T allocateInstance(Class<T> clazz) {
		try {
			return (T) UNSAFE.allocateInstance(clazz);
		} catch (Throwable t) {
			throw sneaky(t);
		}
	}

	@FunctionalInterface
	public interface ThrowingSupplier<T, E extends Throwable> {
		T get() throws E;
	}

	@FunctionalInterface
	public interface ThrowingFunction<T, R, E extends Throwable> {
		R apply(T t) throws E;
	}
}
