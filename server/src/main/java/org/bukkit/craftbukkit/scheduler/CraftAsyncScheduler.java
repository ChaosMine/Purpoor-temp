/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.bukkit.craftbukkit.scheduler;

import com.destroystokyo.paper.ServerSchedulerReportingWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.SystemPropertyUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.*;

public class CraftAsyncScheduler extends CraftScheduler {
	private static final Logger log = LogManager.getLogger(CraftAsyncScheduler.class);
	private static final boolean DEBUG = SystemPropertyUtil.getBoolean("dev.kamillaova.purpur.CraftAsyncScheduler.debug", false);
	private static final boolean DUMP_ON_REJECT = SystemPropertyUtil.getBoolean("dev.kamillaova.purpur.CraftAsyncScheduler.dumpOnReject", true);

	private final Queue<Runnable> runningTasks = new ConcurrentLinkedQueue<>();

	private final ThreadPoolExecutor executor;

	private final Executor management = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
		.setNameFormat("Craft Async Scheduler Management Thread").build());
	private final List<CraftTask> temp = new ObjectArrayList<>();

	CraftAsyncScheduler() {
		super(true);
		executor = new ThreadPoolExecutor(
			1, 100,
			1L, TimeUnit.MINUTES,
			new SynchronousQueue<>(),
			new ThreadFactoryBuilder()
				.setThreadFactory(r -> {
					if (DEBUG) {
						new Throwable("CraftAsyncScheduler Thread create dump").printStackTrace();
					}
					var t = new FastThreadLocalThread(r);
					t.setPriority(Thread.NORM_PRIORITY + 2);
					t.setDaemon(true);
					return t;
				})
				.setNameFormat("Craft Scheduler Thread - %1$d")
				.build()
		) {
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				super.beforeExecute(t, r);
				CraftAsyncScheduler.this.runningTasks.offer(r);
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				CraftAsyncScheduler.this.runningTasks.remove(r);
			}
		};
		if (DUMP_ON_REJECT) {
			executor.setRejectedExecutionHandler((r, e) -> {
				log.error("Rejected execution of {}, dumping all tasks", r.getClass());
				Runnable runnable;
				while ((runnable = this.runningTasks.peek()) != null) {
					if (runnable instanceof ServerSchedulerReportingWrapper reportingWrapper) {
						runnable = reportingWrapper.internalTask().rTask;
					}
					log.error("  - {}", runnable.getClass());
				}
			});
		}
		executor.allowCoreThreadTimeOut(false);
		executor.prestartAllCoreThreads();
	}

	@Override
	public void cancelTask(int taskId) {
		this.management.execute(() -> this.removeTask(taskId));
	}

	private synchronized void removeTask(int taskId) {
		parsePending();
		this.pending.removeIf((task) -> {
			if (task.getTaskId() == taskId) {
				task.cancel0();
				return true;
			}
			return false;
		});
	}

	@Override
	public void mainThreadHeartbeat(int currentTick) {
		this.currentTick = currentTick;
		this.management.execute(() -> this.runTasks(currentTick));
	}

	private synchronized void runTasks(int currentTick) {
		parsePending();
		while (!this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick) {
			var task = this.pending.remove();
			if (executeTask(task)) {
				var period = task.getPeriod();
				if (period > 0) {
					task.setNextRun(currentTick + period);
					temp.add(task);
				}
			}
			parsePending();
		}
		this.pending.addAll(temp);
		temp.clear();
	}

	private boolean executeTask(CraftTask task) {
		if (isValid(task)) {
			if (DEBUG) {
				log.info("CraftAsyncScheduler executeTask: " + task.rTask.getClass());
			}
			this.runners.put(task.getTaskId(), task);
			this.executor.execute(new ServerSchedulerReportingWrapper(task));
			return true;
		}
		return false;
	}

	@Override
	public synchronized void cancelTasks(Plugin plugin) {
		parsePending();
		for (var iterator = this.pending.iterator(); iterator.hasNext(); ) {
			var task = iterator.next();
			if (task.getTaskId() != -1 && (plugin == null || task.getOwner().equals(plugin))) {
				task.cancel0();
				iterator.remove();
			}
		}
	}

	/**
	 * Checks if task is not cancelled
	 * @return true if the task is not cancelled, false otherwise
	 */
	static boolean isValid(CraftTask runningTask) {
		return runningTask.getPeriod() >= CraftTask.NO_REPEATING;
	}
}
