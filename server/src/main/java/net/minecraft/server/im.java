package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.properties.IBlockState;

public interface im extends Supplier<JsonElement> {

    void a(BlockStateList<?, ?> blockstatelist);

    static im.c a() {
        return new im.c();
    }

    static im b(im... aim) {
        return new im.a(im.b.b, Arrays.asList(aim));
    }

    public static class c implements im {

        private final Map<IBlockState<?>, String> a = Maps.newHashMap();

        public c() {}

        private static <T extends Comparable<T>> String a(IBlockState<T> iblockstate, Stream<T> stream) {
            iblockstate.getClass();
            return (String) stream.map(iblockstate::a).collect(Collectors.joining("|"));
        }

        private static <T extends Comparable<T>> String c(IBlockState<T> iblockstate, T t0, T[] at) {
            return a(iblockstate, Stream.concat(Stream.of(t0), Stream.of(at)));
        }

        private <T extends Comparable<T>> void a(IBlockState<T> iblockstate, String s) {
            String s1 = (String) this.a.put(iblockstate, s);

            if (s1 != null) {
                throw new IllegalStateException("Tried to replace " + iblockstate + " value from " + s1 + " to " + s);
            }
        }

        public final <T extends Comparable<T>> im.c a(IBlockState<T> iblockstate, T t0) {
            this.a(iblockstate, iblockstate.a(t0));
            return this;
        }

        @SafeVarargs
        public final <T extends Comparable<T>> im.c a(IBlockState<T> iblockstate, T t0, T... at) {
            this.a(iblockstate, c(iblockstate, t0, at));
            return this;
        }

        public JsonElement get() {
            JsonObject jsonobject = new JsonObject();

            this.a.forEach((iblockstate, s) -> {
                jsonobject.addProperty(iblockstate.getName(), s);
            });
            return jsonobject;
        }

        @Override
        public void a(BlockStateList<?, ?> blockstatelist) {
            List<IBlockState<?>> list = (List) this.a.keySet().stream().filter((iblockstate) -> {
                return blockstatelist.a(iblockstate.getName()) != iblockstate;
            }).collect(Collectors.toList());

            if (!list.isEmpty()) {
                throw new IllegalStateException("Properties " + list + " are missing from " + blockstatelist);
            }
        }
    }

    public static class a implements im {

        private final im.b a;
        private final List<im> b;

        private a(im.b im_b, List<im> list) {
            this.a = im_b;
            this.b = list;
        }

        @Override
        public void a(BlockStateList<?, ?> blockstatelist) {
            this.b.forEach((im) -> {
                im.a(blockstatelist);
            });
        }

        public JsonElement get() {
            JsonArray jsonarray = new JsonArray();

            this.b.stream().map(Supplier::get).forEach(jsonarray::add);
            JsonObject jsonobject = new JsonObject();

            jsonobject.add(this.a.c, jsonarray);
            return jsonobject;
        }
    }

    public static enum b {

        a("AND"), b("OR");

        private final String c;

        private b(String s) {
            this.c = s;
        }
    }
}
