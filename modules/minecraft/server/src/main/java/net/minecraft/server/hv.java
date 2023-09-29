package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.SystemUtils;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.data.DebugReportProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.IBlockState;

import java.io.IOException;

public class hv implements DebugReportProvider {
	private static final Gson b = new GsonBuilder().setPrettyPrinting().create();
	private final DebugReportGenerator c;

	public hv(DebugReportGenerator var0) {
		this.c = var0;
	}

	@Override
	public void a(HashCache var0) throws IOException {
		JsonObject var1 = new JsonObject();

		for(Block var3 : IRegistry.BLOCK) {
			MinecraftKey var4 = IRegistry.BLOCK.getKey(var3);
			JsonObject var5 = new JsonObject();
			BlockStateList<Block, IBlockData> var6 = var3.getStates();
			if (!var6.d().isEmpty()) {
				JsonObject var7 = new JsonObject();

				for(IBlockState<?> var9 : var6.d()) {
					JsonArray var10 = new JsonArray();

					for(Comparable<?> var12 : var9.getValues()) {
						var10.add(SystemUtils.a((IBlockState) var9, (Comparable) var12));
					}

					var7.add(var9.getName(), var10);
				}

				var5.add("properties", var7);
			}

			JsonArray var7 = new JsonArray();

			for(IBlockData var9 : var6.a()) {
				JsonObject var10 = new JsonObject();
				JsonObject var11 = new JsonObject();

				for(IBlockState<?> var13 : var6.d()) {
					var11.addProperty(var13.getName(), SystemUtils.a((IBlockState) var13, (Comparable) var9.get(var13)));
				}

				if (var11.size() > 0) {
					var10.add("properties", var11);
				}

				var10.addProperty("id", Block.getCombinedId(var9));
				if (var9 == var3.getBlockData()) {
					var10.addProperty("default", true);
				}

				var7.add(var10);
			}

			var5.add("states", var7);
			var1.add(var4.toString(), var5);
		}

		java.nio.file.Path var2 = this.c.b().resolve("reports/blocks.json");
		DebugReportProvider.a(b, var0, var1, var2);
	}

	@Override
	public String a() {
		return "Block List";
	}
}