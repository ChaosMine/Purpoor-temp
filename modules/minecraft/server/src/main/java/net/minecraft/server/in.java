package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;

public class in implements il {

    private final Block a;
    private final List<in.b> b = Lists.newArrayList();

    private in(Block block) {
        this.a = block;
    }

    @Override
    public Block a() {
        return this.a;
    }

    public static in a(Block block) {
        return new in(block);
    }

    public in a(List<ir> list) {
        this.b.add(new in.b(list));
        return this;
    }

    public in a(ir ir) {
        return this.a(ImmutableList.of(ir));
    }

    public in a(im im, List<ir> list) {
        this.b.add(new in.a(im, list));
        return this;
    }

    public in a(im im, ir... air) {
        return this.a(im, ImmutableList.copyOf(air));
    }

    public in a(im im, ir ir) {
        return this.a(im, ImmutableList.of(ir));
    }

    public JsonElement get() {
        BlockStateList<Block, IBlockData> var0 = this.a.getStates();
        this.b.forEach(var1x -> var1x.a(var0));
        JsonArray var1 = new JsonArray();
        this.b.stream().map(v -> v.get()).forEach(var1::add);
        JsonObject var2 = new JsonObject();
        var2.add("multipart", var1);
        return var2;
    }

    static class a extends in.b {

        private final im a;

        private a(im im, List<ir> list) {
            super(list);
            this.a = im;
        }

        @Override
        public void a(BlockStateList<?, ?> blockstatelist) {
            this.a.a(blockstatelist);
        }

        @Override
        public void a(JsonObject jsonobject) {
            jsonobject.add("when", this.a.get());
        }
    }

    static class b implements Supplier<JsonElement> {

        private final List<ir> a;

        private b(List<ir> list) {
            this.a = list;
        }

        public void a(BlockStateList<?, ?> blockstatelist) {}

        public void a(JsonObject jsonobject) {}

        public JsonElement get() {
            JsonObject jsonobject = new JsonObject();

            this.a(jsonobject);
            jsonobject.add("apply", ir.a(this.a));
            return jsonobject;
        }
    }
}
