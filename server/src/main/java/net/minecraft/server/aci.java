package net.minecraft.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.IReloadListener;
import net.minecraft.server.packs.resources.IResourceManager;
import net.minecraft.util.Unit;

public interface aci extends IReloadListener {

    @Override
    default CompletableFuture<Void> a(IReloadListener.a ireloadlistener_a, IResourceManager iresourcemanager, Executor executor, Executor executor1) {
        return ireloadlistener_a.a(Unit.INSTANCE).thenRunAsync(() -> {
            this.a(iresourcemanager);
        }, executor1);
    }

    void a(IResourceManager iresourcemanager);
}
