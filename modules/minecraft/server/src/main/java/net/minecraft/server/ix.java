package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;

public class ix {
	private final Optional<MinecraftKey> a;
	private final Set<ja> b;
	private final Optional<String> c;

	public ix(Optional<MinecraftKey> optional, Optional<String> optional1, ja... aja) {
		this.a = optional;
		this.c = optional1;
		this.b = ImmutableSet.copyOf(aja);
	}

	public MinecraftKey a(Block block, iz_ iz, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		return this.a(iw.a(block, this.c.orElse("")), iz, biconsumer);
	}

	public MinecraftKey a(Block block, String s, iz_ iz, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		return this.a(iw.a(block, s + this.c.orElse("")), iz, biconsumer);
	}

	public MinecraftKey b(Block block, String s, iz_ iz, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		return this.a(iw.a(block, s), iz, biconsumer);
	}

	public MinecraftKey a(MinecraftKey minecraftkey, iz_ iz, BiConsumer<MinecraftKey, Supplier<JsonElement>> biconsumer) {
		Map<ja, MinecraftKey> map = this.a(iz);

		biconsumer.accept(minecraftkey, () -> {
			JsonObject jsonobject = new JsonObject();

			this.a.ifPresent((minecraftkey1) -> {
				jsonobject.addProperty("parent", minecraftkey1.toString());
			});
			if (!map.isEmpty()) {
				JsonObject jsonobject1 = new JsonObject();

				map.forEach((ja, minecraftkey1) -> {
					jsonobject1.addProperty(ja.a(), minecraftkey1.toString());
				});
				jsonobject.add("textures", jsonobject1);
			}

			return jsonobject;
		});
		return minecraftkey;
	}

	private Map<ja, MinecraftKey> a(iz_ iz) {
		var stream = Streams.concat(this.b.stream(), iz.a());
		return stream.collect(ImmutableMap.toImmutableMap(Function.identity(), iz::a));
	}
}
