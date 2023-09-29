package w.asm;

import dev.kamillaova.paper.util.JavaUtil;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 * @author whilein
 */
public interface MagicAccessorBridge {
	static @NotNull MagicAccessorBridge getInstance() {
		return _Initializer.INSTANCE;
	}

	static boolean isMagicAccessorAvailable() {
		return _Initializer.INSTANCE.isAvailable();
	}

	boolean isAvailable();
	@NotNull Class<?> type();
	@NotNull String internalName();

	final class _Initializer {

		private static final MagicAccessorBridge INSTANCE;

		private static final String MAGIC_ACCESSOR_BRIDGE_NAME = "w/asm/MagicAccessorImpl";

		static {
			Class<?> magicAccessorImpl;

			try {
				magicAccessorImpl = Class.forName("jdk.internal.reflect.MagicAccessorImpl");
			} catch (final ClassNotFoundException cfe) {
				try {
					magicAccessorImpl = Class.forName("sun.reflect.MagicAccessorImpl");
				} catch (final ClassNotFoundException cfe1) {
					magicAccessorImpl = null;
				}
			}

			if (magicAccessorImpl == null) {
				INSTANCE = new StubImpl();
			} else {
				var magicAccessorImplName = Type.getInternalName(magicAccessorImpl);

				var bridgeCode = createBridgeClass(magicAccessorImplName);

				var magicAccessorBridge = JavaUtil.UNSAFE.defineClass(
					MAGIC_ACCESSOR_BRIDGE_NAME.replace('.', '/'),
					bridgeCode, 0, bridgeCode.length, null, null
				);

				INSTANCE = new DefaultImpl(magicAccessorBridge, MAGIC_ACCESSOR_BRIDGE_NAME);
			}
		}

		private static byte[] createBridgeClass(String magicAccessorImplName) {
			var cw = new ClassMaker(0);

			cw.visit(
				V1_8,
				ACC_PUBLIC | ACC_ABSTRACT,
				MAGIC_ACCESSOR_BRIDGE_NAME,
				null,
				magicAccessorImplName,
				null
			);
			cw.visitEmptyConstructor(ACC_PUBLIC).visitEnd();
			cw.visitEnd();

			return cw.toByteArray();
		}

		private static final class StubImpl implements MagicAccessorBridge {
			private UnsupportedOperationException constructException() {
				return new UnsupportedOperationException(
					"MagicAccessorImpl is not found on current JVM version (" + System.getProperty("java.version") + ")."
				);
			}

			@Override
			public boolean isAvailable() {
				return false;
			}

			@Override
			public @NotNull Class<?> type() {
				throw constructException();
			}

			@Override
			public @NotNull String internalName() {
				throw constructException();
			}
		}

		private record DefaultImpl(
			Class<?> type,
			String internalName
		) implements MagicAccessorBridge {
			@Override
			public boolean isAvailable() {
				return true;
			}
		}
	}
}
