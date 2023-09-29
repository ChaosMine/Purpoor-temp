package net.minecraft.server.packs.repository;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.IResourcePack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcePackRepository implements AutoCloseable {

    private final Set<ResourcePackSource> a;
    private Map<String, ResourcePackLoader> b;
    private List<ResourcePackLoader> c;
    private final ResourcePackLoader.a d;

    public ResourcePackRepository(ResourcePackLoader.a resourcepackloader_a, ResourcePackSource... aresourcepacksource) {
        this.b = ImmutableMap.of();
        this.c = ImmutableList.of();
        this.d = resourcepackloader_a;
        this.a = ImmutableSet.copyOf(aresourcepacksource);
    }

    public ResourcePackRepository(ResourcePackSource... aresourcepacksource) {
        this(ResourcePackLoader::new, aresourcepacksource);
    }

    public void a() {
        List<String> list = this.c.stream().map(ResourcePackLoader::e).collect(ImmutableList.toImmutableList());

        this.close();
        this.b = this.g();
        this.c = this.b(list);
    }

    private Map<String, ResourcePackLoader> g() {
        Map<String, ResourcePackLoader> map = Maps.newTreeMap();
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            ResourcePackSource resourcepacksource = (ResourcePackSource) iterator.next();

            resourcepacksource.a((resourcepackloader) -> {
                ResourcePackLoader resourcepackloader1 = map.put(resourcepackloader.e(), resourcepackloader);
            }, this.d);
        }

        return ImmutableMap.copyOf(map);
    }

    public void a(Collection<String> collection) {
        this.c = this.b(collection);
    }

    private List<ResourcePackLoader> b(Collection<String> collection) {
        List<ResourcePackLoader> list = this.c(collection).toList();
        Iterator iterator = this.b.values().iterator();

        while (iterator.hasNext()) {
            ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();

            if (resourcepackloader.f() && !list.contains(resourcepackloader)) {
                resourcepackloader.h().a(list, resourcepackloader, Functions.identity(), false);
            }
        }

        return ImmutableList.copyOf(list);
    }

    private Stream<ResourcePackLoader> c(Collection<String> collection) {
        Stream stream = collection.stream();
        Map map = this.b;

        this.b.getClass();
        return stream.map(map::get).filter(Objects::nonNull);
    }

    public Collection<String> b() {
        return this.b.keySet();
    }

    public final Collection<ResourcePackLoader> getPacks() { return this.c(); } // Paper - OBFHELPER
    public Collection<ResourcePackLoader> c() {
        return this.b.values();
    }

    public Collection<String> d() {
        return this.c.stream().map(ResourcePackLoader::e).collect(ImmutableSet.toImmutableSet());
    }

    public final Collection<ResourcePackLoader> getEnabledPacks() { return this.e(); } // Paper - OBFHELPER
    public Collection<ResourcePackLoader> e() {
        return this.c;
    }

    @Nullable
    public ResourcePackLoader a(String s) {
        return this.b.get(s);
    }

    public void close() {
        this.b.values().forEach(ResourcePackLoader::close);
    }

    public boolean b(String s) {
        return this.b.containsKey(s);
    }

    public List<IResourcePack> f() {
        return this.c.stream().map(ResourcePackLoader::d).collect(ImmutableList.toImmutableList());
    }
}
