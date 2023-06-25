package net.minecraft.server;

import com.google.gson.JsonElement;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

public interface il extends Supplier<JsonElement> {

    Block a();
}
