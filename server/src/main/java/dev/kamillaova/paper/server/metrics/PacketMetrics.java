package dev.kamillaova.paper.server.metrics;

import io.netty.util.concurrent.FastThreadLocalThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public final class PacketMetrics {
	private static final Logger LOGGER = LogManager.getLogger(PacketMetrics.class);

	private static final LongAdder rx = new LongAdder();
	private static final LongAdder tx = new LongAdder();

	static {
		Executors.newSingleThreadScheduledExecutor(runnable -> {
			var thread = new FastThreadLocalThread(runnable);
			thread.setDaemon(true);
			thread.setPriority(Thread.MIN_PRIORITY + 1);
			return thread;
		}).scheduleWithFixedDelay(() -> {
			var rxCount = PacketMetrics.rx.sumThenReset();
			var txCount = PacketMetrics.tx.sumThenReset();

			LOGGER.info("RX: {} p/s, TX: {} p/s", rxCount, txCount);
		}, 0, 1, TimeUnit.SECONDS);
	}

	public static void receive() {
		rx.increment();
	}

	public static void transmit() {
		tx.increment();
	}
}
