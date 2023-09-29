package net.minecraft.server;

import java.nio.file.Path;
import net.minecraft.core.IRegistry;
import net.minecraft.data.DebugReportGenerator;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.material.FluidTypes;

public class ju extends jw<FluidType> {

    public ju(DebugReportGenerator debugreportgenerator) {
        super(debugreportgenerator, IRegistry.FLUID);
    }

    @Override
    protected void b() {
        this.a(TagsFluid.WATER).a(FluidTypes.WATER, FluidTypes.FLOWING_WATER);
        this.a(TagsFluid.LAVA).a(FluidTypes.LAVA, FluidTypes.FLOWING_LAVA);
    }

    @Override
    protected Path a(MinecraftKey minecraftkey) {
        return this.b.b().resolve("data/" + minecraftkey.getNamespace() + "/tags/fluids/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "Fluid Tags";
    }
}
