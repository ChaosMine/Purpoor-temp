package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.INamable;
import org.jetbrains.annotations.Unmodifiable;

public enum BlockPropertyStructureMode implements INamable {
    SAVE("save"),
    LOAD("load"),
    CORNER("corner"),
    DATA("data"),
    ;

    public static final BlockPropertyStructureMode @Unmodifiable [] VALUES = values();

    private final String e;

    BlockPropertyStructureMode(String s) {
        this.e = s;
    }

    @Override
    public String getName() {
        return this.e;
    }
}
