package org.bukkit.support;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Versioning;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.logging.Logger;

public final class DummyServer implements InvocationHandler {
	private static final HashMap<Method, MethodHandler> methods = new HashMap<>();

	static {
		try {
			methods.put(
				Server.class.getMethod("getItemFactory"),
				(server, args) -> CraftItemFactory.instance()
			);
			methods.put(
				Server.class.getMethod("getName"),
				(server, args) -> DummyServer.class.getName()
			);
			methods.put(
				Server.class.getMethod("getVersion"),
				(server, args) -> DummyServer.class.getPackage().getImplementationVersion()
			);
			methods.put(
				Server.class.getMethod("getBukkitVersion"),
				(server, args) -> Versioning.getBukkitVersion()
			);
			methods.put(
				Server.class.getMethod("getLogger"),
				new MethodHandler() {
					final Logger logger = Logger.getLogger(DummyServer.class.getCanonicalName());

					@Override
					public Object handle(DummyServer server, Object[] args) {
						return logger;
					}
				}
			);
			methods.put(
				Server.class.getMethod("getUnsafe"),
				(server, args) -> CraftMagicNumbers.INSTANCE
			);
			methods.put(
				Server.class.getMethod("createBlockData", Material.class),
				(server, args) -> CraftBlockData.newData((Material) args[0], null)
			);
			methods.put(Server.class.getMethod("getLootTable", NamespacedKey.class),
				(server, args) -> {
					NamespacedKey key = (NamespacedKey) args[0];
					return new CraftLootTable(key, AbstractTestingBase.LOOT_TABLE_REGISTRY.getLootTable(CraftNamespacedKey.toMinecraft(key)));
				}
			);
			Bukkit.setServer(Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(new DummyServer()));
		} catch (Throwable t) {
			throw new Error(t);
		}
	}

	private DummyServer() {}

	public static void init() {}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		MethodHandler handler = methods.get(method);
		if (handler != null) {
			return handler.handle(this, args);
		}
		throw new UnsupportedOperationException(String.valueOf(method));
	}

	private interface MethodHandler {
		Object handle(DummyServer server, Object[] args);
	}
}
