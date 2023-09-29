package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface IReloadListener {

    CompletableFuture<Void> a(IReloadListener.a ireloadlistener_a, IResourceManager iresourcemanager, Executor executor, Executor executor1);

    default String c() {
        return this.getClass().getSimpleName();
    }

    public interface a {

        <T> CompletableFuture<T> a(T t0);
    }
}
