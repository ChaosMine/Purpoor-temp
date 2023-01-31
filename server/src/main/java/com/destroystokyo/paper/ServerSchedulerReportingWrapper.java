package com.destroystokyo.paper;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerSchedulerException;
import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.scheduler.CraftTask;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reporting wrapper to catch exceptions not natively
 */
public record ServerSchedulerReportingWrapper(CraftTask internalTask) implements Runnable {
	public ServerSchedulerReportingWrapper {
		checkNotNull(internalTask, "internalTask");
	}

	@Override
	public void run() {
		try {
			internalTask.run();
		} catch (RuntimeException e) {
			internalTask.getOwner().getServer().getPluginManager().callEvent(
				new ServerExceptionEvent(new ServerSchedulerException(e, internalTask))
			);
			throw e;
		} catch (Throwable t) {
			internalTask.getOwner().getServer().getPluginManager().callEvent(
				new ServerExceptionEvent(new ServerSchedulerException(t, internalTask))
			); //Do not rethrow, since it is not permitted with Runnable#run
		}
	}
}
