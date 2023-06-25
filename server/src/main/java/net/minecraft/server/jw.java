package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class jw<T> implements DebugReportProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson e = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DebugReportGenerator b;
    protected final IRegistry<T> c;
    private final Map<MinecraftKey, Tag.a> f = Maps.newLinkedHashMap();

    protected jw(DebugReportGenerator debugreportgenerator, IRegistry<T> iregistry) {
        this.b = debugreportgenerator;
        this.c = iregistry;
    }

    protected abstract void b();

    @Override
    public void a(HashCache hashcache) {
        this.f.clear();
        this.b();
        Tag<T> tag = TagSet.a();
        Function<MinecraftKey, Tag<T>> function = (minecraftkey) -> {
            return this.f.containsKey(minecraftkey) ? tag : null;
        };
        Function<MinecraftKey, T> function1 = (minecraftkey) -> {
            return this.c.getOptional(minecraftkey).orElse((Object) null);
        };

        this.f.forEach((minecraftkey, tag_a) -> {
            List<Tag.b> list = (List) tag_a.b(function, function1).collect(Collectors.toList());

            if (!list.isEmpty()) {
                throw new IllegalArgumentException(String.format("Couldn't define tag %s as it is missing following references: %s", minecraftkey, list.stream().map(Objects::toString).collect(Collectors.joining(","))));
            } else {
                JsonObject jsonobject = tag_a.c();
                Path path = this.a(minecraftkey);

                try {
                    String s = jw.e.toJson(jsonobject);
                    String s1 = jw.a.hashUnencodedChars(s).toString();

                    if (!Objects.equals(hashcache.a(path), s1) || !Files.exists(path, new LinkOption[0])) {
                        Files.createDirectories(path.getParent());
                        BufferedWriter bufferedwriter = Files.newBufferedWriter(path);
                        Throwable throwable = null;

                        try {
                            bufferedwriter.write(s);
                        } catch (Throwable throwable1) {
                            throwable = throwable1;
                            throw throwable1;
                        } finally {
                            if (bufferedwriter != null) {
                                if (throwable != null) {
                                    try {
                                        bufferedwriter.close();
                                    } catch (Throwable throwable2) {
                                        throwable.addSuppressed(throwable2);
                                    }
                                } else {
                                    bufferedwriter.close();
                                }
                            }

                        }
                    }

                    hashcache.a(path, s1);
                } catch (IOException ioexception) {
                    jw.LOGGER.error("Couldn't save tags to {}", path, ioexception);
                }

            }
        });
    }

    protected abstract Path a(MinecraftKey minecraftkey);

    protected jw.a<T> a(Tag.e<T> tag_e) {
        Tag.a tag_a = this.b(tag_e);

        return new jw.a<>(tag_a, this.c, "vanilla");
    }

    protected Tag.a b(Tag.e<T> tag_e) {
        return (Tag.a) this.f.computeIfAbsent(tag_e.a(), (minecraftkey) -> {
            return new Tag.a();
        });
    }

    public static class a<T> {

        private final Tag.a a;
        private final IRegistry<T> b;
        private final String c;

        private a(Tag.a tag_a, IRegistry<T> iregistry, String s) {
            this.a = tag_a;
            this.b = iregistry;
            this.c = s;
        }

        public jw.a<T> a(T t0) {
            this.a.a(this.b.getKey(t0), this.c);
            return this;
        }

        public jw.a<T> a(Tag.e<T> tag_e) {
            this.a.c(tag_e.a(), this.c);
            return this;
        }

        @SafeVarargs
        public final jw.a<T> a(T... at) {
            Stream stream = Stream.of(at);
            IRegistry iregistry = this.b;

            this.b.getClass();
            stream.map(iregistry::getKey).forEach((minecraftkey) -> {
                this.a.a(minecraftkey, this.c);
            });
            return this;
        }
    }
}
