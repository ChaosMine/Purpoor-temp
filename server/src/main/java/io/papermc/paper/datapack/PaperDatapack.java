package io.papermc.paper.datapack;

import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.ResourcePackLoader;

import java.util.List;
import java.util.stream.Collectors;

public class PaperDatapack implements Datapack {
    private final String name;
    private final Compatibility compatibility;
    private final boolean enabled;

    PaperDatapack(ResourcePackLoader loader, boolean enabled) {
        this.name = loader.getName();
        this.compatibility = Compatibility.valueOf(loader.getVersion().name());
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Compatibility getCompatibility() {
        return compatibility;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) {
            return;
        }

        MinecraftServer server = MinecraftServer.getServer();
        List<String> enabledKeys = server.getResourcePackRepository().getEnabledPacks().stream().map(ResourcePackLoader::getName).collect(Collectors.toList());
        if (enabled) {
            enabledKeys.add(this.name);
        } else {
            enabledKeys.remove(this.name);
        }
        server.reloadServerResources(enabledKeys, ServerResourcesReloadedEvent.Cause.PLUGIN);
    }
}
