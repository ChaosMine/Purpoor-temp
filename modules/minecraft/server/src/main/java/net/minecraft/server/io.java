package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.SystemUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IBlockState;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class io implements il {

    private final Block a;
    private final List<ir> b;
    private final Set<IBlockState<?>> c = Sets.newHashSet();
    private final List<ip> d = Lists.newArrayList();

    private io(Block var0, List<ir> var1) {
        this.a = var0;
        this.b = var1;
    }

    public io a(ip var02) {
        var02.b().forEach(var0 -> {
            if (this.a.getStates().a(var0.getName()) != var0) {
                throw new IllegalStateException("Property " + var0 + " is not defined for block " + this.a);
            }
            if (!this.c.add(var0)) {
                throw new IllegalStateException("Values of property " + var0 + " already defined for block " + this.a);
            }
        });
        this.d.add(var02);
        return this;
    }

    @Override
    public JsonElement get() {
        Stream<Pair<iq, List<ir>>> var0 = Stream.of(Pair.of(iq.a(), this.b));
        for (ip ip2 : this.d) {
            Map<iq, List<ir>> var3 = ip2.a();
            var0 = var0.flatMap(var12 -> var3.entrySet().stream().map(var1 -> {
                iq var2 = var12.getFirst().a(var1.getKey());
                List<ir> var555 = io.a(var12.getSecond(), var1.getValue());
                return Pair.of(var2, var555);
            }));
        }
        var var13 = new TreeMap<String, JsonElement>();
        var0.forEach(var1 -> var13.put(var1.getFirst().b(), ir.a(var1.getSecond())));
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("variants", SystemUtils.a(new JsonObject(), var1 -> var13.forEach(var1::add)));
        return jsonObject;
    }

    private static List<ir> a(List<ir> list, List<ir> list1) {
        var builder = ImmutableList.<ir>builder();

        list.forEach((ir) -> list1.forEach((ir1) -> {
            builder.add(ir.a(ir, ir1));
        }));
        return builder.build();
    }

    @Override
    public Block a() {
        return this.a;
    }

    public static io a(Block var0) {
        return new io(var0, ImmutableList.of(ir.a()));
    }

    public static io a(Block var0, ir var1) {
        return new io(var0, ImmutableList.of(var1));
    }

    public static io a(Block var0, ir ... var1) {
        return new io(var0, ImmutableList.copyOf(var1));
    }
}
