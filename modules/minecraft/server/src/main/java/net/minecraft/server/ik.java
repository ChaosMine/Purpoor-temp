package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ik implements DebugReportProvider {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson c = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final DebugReportGenerator d;

	public ik(DebugReportGenerator debugreportgenerator) {
		this.d = debugreportgenerator;
	}

	private static Path a(Path path, Block block) {
		MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(block);

		return path.resolve("assets/" + minecraftkey.getNamespace() + "/blockstates/" + minecraftkey.getKey() + ".json");
	}

	private static Path a(Path path, MinecraftKey minecraftkey) {
		return path.resolve("assets/" + minecraftkey.getNamespace() + "/models/" + minecraftkey.getKey() + ".json");
	}

	@Override
	public void a(HashCache hashcache) {
		Path path = this.d.b();
		Map<Block, il> map = Maps.newHashMap();
		Consumer<il> consumer = (il) -> {
			Block block = il.a();
			il il1 = map.put(block, il);

			if (il1 != null) {
				throw new IllegalStateException("Duplicate blockstate definition for " + block);
			}
		};
		Map<MinecraftKey, Supplier<JsonElement>> map1 = Maps.newHashMap();
		Set<Item> set = Sets.newHashSet();
		BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer = (minecraftkey, supplier) -> {
			Supplier<JsonElement> supplier1 = map1.put(minecraftkey, supplier);

			if (supplier1 != null) {
				throw new IllegalStateException("Duplicate model definition for " + minecraftkey);
			}
		};
		Consumer<Item> consumer1 = set::add;

		(new ii(consumer, biconsumer, consumer1)).a();
		(new ij(biconsumer)).a();
		List<Block> list = IRegistry.BLOCK.g().filter((block) -> {
			return !map.containsKey(block);
		}).toList();

		if (!list.isEmpty()) {
			throw new IllegalStateException("Missing blockstate definitions for: " + list);
		} else {
			IRegistry.BLOCK.forEach((block) -> {
				Item item = Item.e.get(block);

				if (item != null) {
					if (set.contains(item)) {
						return;
					}

					MinecraftKey minecraftkey = iw.a(item);

					if (!map1.containsKey(minecraftkey)) {
						map1.put(minecraftkey, new iv(iw.a(block)));
					}
				}

			});
			this.a(hashcache, path, map, ik::a);
			this.a(hashcache, path, map1, ik::a);
		}
	}

	private <T> void a(HashCache hashcache, Path path, Map<T, ? extends Supplier<JsonElement>> map, BiFunction<Path, T, Path> bifunction) {
		map.forEach((object, supplier) -> {
			Path path1 = bifunction.apply(path, object);

			try {
				DebugReportProvider.a(ik.c, hashcache, supplier.get(), path1);
			} catch (Exception exception) {
				ik.LOGGER.error("Couldn't save {}", path1, exception);
			}

		});
	}

	@Override
	public String a() {
		return "Block State Definitions";
	}
}
