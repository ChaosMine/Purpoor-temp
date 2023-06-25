package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.storage.loot.LootCollector;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTableRegistry;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ie implements DebugReportProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson c = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final DebugReportGenerator d;
    private final List<Pair<Supplier<Consumer<BiConsumer<MinecraftKey, LootTable.a>>>, LootContextParameterSet>> e;

    public ie(DebugReportGenerator debugreportgenerator) {
        this.e = ImmutableList.of(Pair.of(ic::new, LootContextParameterSets.FISHING), Pair.of(ia::new, LootContextParameterSets.CHEST), Pair.of(ib::new, LootContextParameterSets.ENTITY), Pair.of(hz::new, LootContextParameterSets.BLOCK), Pair.of(ig::new, LootContextParameterSets.BARTER), Pair.of(id::new, LootContextParameterSets.GIFT));
        this.d = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) {
        Path path = this.d.b();
        Map<MinecraftKey, LootTable> map = Maps.newHashMap();

        this.e.forEach((pair) -> {
            ((Consumer) ((Supplier) pair.getFirst()).get()).accept((minecraftkey, loottable_a) -> {
                if (map.put(minecraftkey, loottable_a.a((LootContextParameterSet) pair.getSecond()).b()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + minecraftkey);
                }
            });
        });
        LootContextParameterSet lootcontextparameterset = LootContextParameterSets.GENERIC;
        Function function = (minecraftkey) -> {
            return null;
        };

        map.getClass();
        LootCollector lootcollector = new LootCollector(lootcontextparameterset, function, map::get);
        Set<MinecraftKey> set = Sets.difference(LootTables.a(), map.keySet());
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            MinecraftKey minecraftkey = (MinecraftKey) iterator.next();

            lootcollector.a("Missing built-in table: " + minecraftkey);
        }

        map.forEach((minecraftkey1, loottable) -> {
            LootTableRegistry.a(lootcollector, minecraftkey1, loottable);
        });
        Multimap<String, String> multimap = lootcollector.a();

        if (!multimap.isEmpty()) {
            multimap.forEach((s, s1) -> {
                ie.LOGGER.warn("Found validation problem in " + s + ": " + s1);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            map.forEach((minecraftkey1, loottable) -> {
                Path path1 = a(path, minecraftkey1);

                try {
                    DebugReportProvider.a(ie.c, hashcache, LootTableRegistry.a(loottable), path1);
                } catch (IOException ioexception) {
                    ie.LOGGER.error("Couldn't save loot table {}", path1, ioexception);
                }

            });
        }
    }

    private static Path a(Path path, MinecraftKey minecraftkey) {
        return path.resolve("data/" + minecraftkey.getNamespace() + "/loot_tables/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "LootTables";
    }
}
