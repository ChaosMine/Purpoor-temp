package org.bukkit;

import com.google.common.collect.ImmutableMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.logging.Logger;

public final class TestServer implements InvocationHandler {
	private interface MethodHandler {
		Object handle(TestServer server, Object[] args);
	}

	private static final Map<Method, MethodHandler> methods;

	static {
		try {
			ImmutableMap.Builder<Method, MethodHandler> methodMap = ImmutableMap.builder();
			methodMap.put(
				Server.class.getMethod("isPrimaryThread"),
        (server, args) -> Thread.currentThread().equals(server.creatingThread)
      );
			// Paper start
			methodMap.put(
				Server.class.getMethod("getTag", String.class, NamespacedKey.class, Class.class),
        (server, args) -> new com.destroystokyo.paper.MaterialSetTag()
      );
			// Paper end
			methodMap.put(
				Server.class.getMethod("getPluginManager"),
        (server, args) -> server.pluginManager
      );
			methodMap.put(
				Server.class.getMethod("getLogger"),
				new MethodHandler() {
					final Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());

					@Override
					public Object handle(TestServer server, Object[] args) {
						return logger;
					}
				}
			);
      methodMap.put(
        Server.class.getMethod("getSlf4jLogger"),
        new MethodHandler() {
          final org.slf4j.Logger logger = LoggerFactory.getLogger(TestServer.class.getCanonicalName());

          @Override
          public Object handle(TestServer server, Object[] args) {
            return logger;
          }
        }
      );
			methodMap.put(
				Server.class.getMethod("getName"),
        (server, args) -> TestServer.class.getSimpleName()
      );
			methodMap.put(
				Server.class.getMethod("getVersion"),
        (server, args) -> "Version_" + TestServer.class.getPackage().getImplementationVersion()
      );
			methodMap.put(
				Server.class.getMethod("getBukkitVersion"),
        (server, args) -> "BukkitVersion_" + TestServer.class.getPackage().getImplementationVersion()
      );
			methods = methodMap.build();

			TestServer server = new TestServer();
			Server instance = Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(server);
			Bukkit.setServer(instance);
			server.pluginManager = new SimplePluginManager(instance, new SimpleCommandMap(instance));
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	private final Thread creatingThread = Thread.currentThread();
	private PluginManager pluginManager;

	private TestServer() {
	}

  public static Server getInstance() {
		return Bukkit.getServer();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		MethodHandler handler = methods.get(method);
		if (handler != null) {
			return handler.handle(this, args);
		}
		throw new UnsupportedOperationException(String.valueOf(method));
	}
}
