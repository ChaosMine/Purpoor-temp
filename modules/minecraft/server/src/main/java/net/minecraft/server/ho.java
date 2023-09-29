package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ho implements DebugReportProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson c = (new GsonBuilder()).setPrettyPrinting().create();
    private final DebugReportGenerator d;
    private final List<Consumer<Consumer<Advancement>>> e = ImmutableList.of(new ht(), new hq(), new hp(), new hr(), new hs());

    public ho(DebugReportGenerator debugreportgenerator) {
        this.d = debugreportgenerator;
    }

    @Override
    public void a(HashCache hashcache) throws IOException {
        Path path = this.d.b();
        Set<MinecraftKey> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getName())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getName());
            } else {
                Path path1 = a(path, advancement);

                try {
                    DebugReportProvider.a(ho.c, hashcache, advancement.a().b(), path1);
                } catch (IOException ioexception) {
                    ho.LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }

            }
        };
        Iterator iterator = this.e.iterator();

        while (iterator.hasNext()) {
            Consumer<Consumer<Advancement>> consumer1 = (Consumer) iterator.next();

            consumer1.accept(consumer);
        }

    }

    private static Path a(Path path, Advancement advancement) {
        return path.resolve("data/" + advancement.getName().getNamespace() + "/advancements/" + advancement.getName().getKey() + ".json");
    }

    @Override
    public String a() {
        return "Advancements";
    }
}
