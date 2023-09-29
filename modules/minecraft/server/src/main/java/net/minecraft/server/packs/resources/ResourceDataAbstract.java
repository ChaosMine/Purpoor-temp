package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class ResourceDataAbstract<T> implements IReloadListener {

    public ResourceDataAbstract() {}

    @Override
    public final CompletableFuture<Void> a(IReloadListener.a ireloadlistener_a, IResourceManager iresourcemanager, Executor executor, Executor executor1) {
        CompletableFuture<T> completablefuture = CompletableFuture.supplyAsync(() -> {
            return this.b(iresourcemanager);
        }, executor);

        return completablefuture.thenCompose(ireloadlistener_a::a).thenAcceptAsync((object) -> {
            this.a(object, iresourcemanager);
        }, executor1);
    }

    protected abstract T b(IResourceManager iresourcemanager);

    protected abstract void a(T t0, IResourceManager iresourcemanager);
}
