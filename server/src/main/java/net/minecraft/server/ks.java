package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ks implements DebugReportProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson c = (new GsonBuilder()).setPrettyPrinting().create();
    private final DebugReportGenerator d;

    public ks(DebugReportGenerator debugreportgenerator) {
        this.d = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) {
        Path path = this.d.b();
        Iterator iterator = RegistryGeneration.WORLDGEN_BIOME.d().iterator();

        while (iterator.hasNext()) {
            Entry<ResourceKey<BiomeBase>, BiomeBase> entry = (Entry) iterator.next();
            Path path1 = a(path, ((ResourceKey) entry.getKey()).a());
            BiomeBase biomebase = (BiomeBase) entry.getValue();
            Function function = JsonOps.INSTANCE.withEncoder(BiomeBase.d);

            try {
                Optional<JsonElement> optional = ((DataResult) function.apply(() -> {
                    return biomebase;
                })).result();

                if (optional.isPresent()) {
                    DebugReportProvider.a(ks.c, hashcache, (JsonElement) optional.get(), path1);
                } else {
                    ks.LOGGER.error("Couldn't serialize biome {}", path1);
                }
            } catch (IOException ioexception) {
                ks.LOGGER.error("Couldn't save biome {}", path1, ioexception);
            }
        }

    }

    private static Path a(Path path, MinecraftKey minecraftkey) {
        return path.resolve("reports/biomes/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "Biomes";
    }
}
