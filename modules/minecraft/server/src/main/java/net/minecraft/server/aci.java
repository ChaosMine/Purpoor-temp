package net.minecraft.server;

import net.minecraft.server.packs.resources.IReloadListener;
import net.minecraft.server.packs.resources.IResourceManager;
import net.minecraft.util.Unit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface aci extends IReloadListener {
	@Override
	default CompletableFuture<Void> a(IReloadListener.a ira, IResourceManager iresourcemanager, Executor executor, Executor executor1) {
		return ira.a(Unit.INSTANCE).thenRunAsync(() -> this.a(iresourcemanager), executor1);
	}

	void a(IResourceManager iresourcemanager);
}
