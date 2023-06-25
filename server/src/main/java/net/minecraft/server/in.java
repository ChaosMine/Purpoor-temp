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
        return this.a((List) ImmutableList.of(ir));
    }

    public in a(im im, List<ir> list) {
        this.b.add(new in.a(im, list));
        return this;
    }

    public in a(im im, ir... air) {
        return this.a(im, (List) ImmutableList.copyOf(air));
    }

    public in a(im im, ir ir) {
        return this.a(im, (List) ImmutableList.of(ir));
    }

    public JsonElement get() {
        BlockStateList<Block, IBlockData> blockstatelist = this.a.getStates();

        this.b.forEach((in_b) -> {
            in_b.a(blockstatelist);
        });
        JsonArray jsonarray = new JsonArray();

        this.b.stream().map(in.b::get).forEach(jsonarray::add);
        JsonObject jsonobject = new JsonObject();

        jsonobject.add("multipart", jsonarray);
        return jsonobject;
    }

    static class a extends in.b {

        private final im a;

        private a(im im, List<ir> list) {
            super(list, null);
            this.a = im;
        }

        @Override
        public void a(BlockStateList<?, ?> blockstatelist) {
            this.a.a(blockstatelist);
        }

        @Override
        public void a(JsonObject jsonobject) {
            jsonobject.add("when", (JsonElement) this.a.get());
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
