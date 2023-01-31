package net.minecraft.server.dedicated;

import joptsimple.OptionSet;
import net.minecraft.core.IRegistryCustom;

import java.io.File;
import java.nio.file.Path;
import java.util.function.UnaryOperator;
// CraftBukkit end

public class DedicatedServerSettings {

    private final Path path;
    private DedicatedServerProperties properties;

    // CraftBukkit start
    public DedicatedServerSettings(IRegistryCustom iregistrycustom, OptionSet optionset) {
        this.path = ((File) optionset.valueOf("config")).toPath();
        this.properties = DedicatedServerProperties.load(iregistrycustom, path, optionset);
        // CraftBukkit end
    }

    public DedicatedServerProperties getProperties() {
        return this.properties;
    }

    public void save() {
        this.properties.savePropertiesFile(this.path);
    }

    public DedicatedServerSettings setProperty(UnaryOperator<DedicatedServerProperties> unaryoperator) {
        (this.properties = (DedicatedServerProperties) unaryoperator.apply(this.properties)).savePropertiesFile(this.path);
        return this;
    }
}
