package io.papermc.paper.datapack;

import net.minecraft.server.packs.repository.ResourcePackLoader;
import net.minecraft.server.packs.repository.ResourcePackRepository;

import java.util.Collection;
import java.util.stream.Collectors;

public class PaperDatapackManager implements DatapackManager {
    private final ResourcePackRepository repository;

    public PaperDatapackManager(ResourcePackRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Datapack> getPacks() {
        Collection<ResourcePackLoader> enabledPacks = repository.getEnabledPacks();
        return repository.getPacks().stream().map(loader -> new PaperDatapack(loader, enabledPacks.contains(loader))).collect(Collectors.toList());
    }

    @Override
    public Collection<Datapack> getEnabledPacks() {
        return repository.getEnabledPacks().stream().map(loader -> new PaperDatapack(loader, true)).collect(Collectors.toList());
    }
}
