package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import net.minecraft.SystemUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IBlockState;

public class io implements il {

    private final Block a;
    private final List<ir> b;
    private final Set<IBlockState<?>> c = Sets.newHashSet();
    private final List<ip> d = Lists.newArrayList();

    private io(Block block, List<ir> list) {
        this.a = block;
        this.b = list;
    }

    public io a(ip ip) {
        ip.b().forEach((iblockstate) -> {
            if (this.a.getStates().a(iblockstate.getName()) != iblockstate) {
                throw new IllegalStateException("Property " + iblockstate + " is not defined for block " + this.a);
            } else if (!this.c.add(iblockstate)) {
                throw new IllegalStateException("Values of property " + iblockstate + " already defined for block " + this.a);
            }
        });
        this.d.add(ip);
        return this;
    }

    public JsonElement get() {
        Stream<Pair<iq, List<ir>>> stream = Stream.of(Pair.of(iq.a(), this.b));

        Map map;

        for (Iterator iterator = this.d.iterator(); iterator.hasNext();stream = stream.flatMap((pair) -> {
            return map.entrySet().stream().map((entry) -> {
                iq iq = ((iq) pair.getFirst()).a((iq) entry.getKey());
                List<ir> list = a((List) pair.getSecond(), (List) entry.getValue());

                return Pair.of(iq, list);
            });
        })) {
            ip ip = (ip) iterator.next();

            map = ip.a();
        }

        Map<String, JsonElement> map1 = new TreeMap();

        stream.forEach((pair) -> {
            JsonElement jsonelement = (JsonElement) map1.put(((iq) pair.getFirst()).b(), ir.a((List) pair.getSecond()));
        });
        JsonObject jsonobject = new JsonObject();

        jsonobject.add("variants", (JsonElement) SystemUtils.a((Object) (new JsonObject()), (jsonobject1) -> {
            map1.forEach(jsonobject1::add);
        }));
        return jsonobject;
    }

    private static List<ir> a(List<ir> list, List<ir> list1) {
        Builder<ir> builder = ImmutableList.builder();

        list.forEach((ir) -> {
            list1.forEach((ir1) -> {
                builder.add(ir.a(ir, ir1));
            });
        });
        return builder.build();
    }

    @Override
    public Block a() {
        return this.a;
    }

    public static io a(Block block) {
        return new io(block, ImmutableList.of(ir.a()));
    }

    public static io a(Block block, ir ir) {
        return new io(block, ImmutableList.of(ir));
    }

    public static io a(Block block, ir... air) {
        return new io(block, ImmutableList.copyOf(air));
    }
}
