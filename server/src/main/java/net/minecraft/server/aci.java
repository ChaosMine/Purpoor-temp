package net.minecraft.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.IReloadListener;
import net.minecraft.server.packs.resources.IResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.GameProfilerFiller;

public interface aci extends IReloadListener {

    @Override
    default CompletableFuture<Void> a(IReloadListener.a ireloadlistener_a, IResourceManager iresourcemanager, GameProfilerFiller gameprofilerfiller, GameProfilerFiller gameprofilerfiller1, Executor executor, Executor executor1) {
        return ireloadlistener_a.a(Unit.INSTANCE).thenRunAsync(() -> {
            gameprofilerfiller1.a();
            gameprofilerfiller1.enter("listener");
            this.a(iresourcemanager);
            gameprofilerfiller1.exit();
            gameprofilerfiller1.b();
        }, executor1);
    }

    void a(IResourceManager iresourcemanager);
}
