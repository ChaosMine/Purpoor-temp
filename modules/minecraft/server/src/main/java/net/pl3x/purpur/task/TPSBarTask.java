package net.pl3x.purpur.task;

import io.netty.util.concurrent.FastThreadLocalThread;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.Player;

import java.util.concurrent.locks.LockSupport;

public class TPSBarTask implements Runnable {
	private static final TPSBarTask instance = new TPSBarTask();
	private static Thread thread;

	private final NamespacedKey key;
	private final CraftBossBar bossbar;

	private boolean running = true;

	private TPSBarTask() {
		this.key = new NamespacedKey("purpur", "tpsbar");
		var bossbar = Bukkit.getBossBar(key);
		if (bossbar == null) {
			bossbar = Bukkit.createBossBar(key, "", BarColor.RED, BarStyle.SEGMENTED_20);
		}
		bossbar.setVisible(true);
		bossbar.setProgress(1.0D);
		this.bossbar = (CraftBossBar) bossbar;
	}

	@Override
	@SuppressWarnings("BusyWait")
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			if (bossbar.getHandle().getPlayers().isEmpty()) {
				LockSupport.park("Waiting players");
			}

			if (!running) return;

			double tps = MinecraftServer.SERVER.tps5s.getAverage();

			bossbar.setVisible(true);
			bossbar.setProgress(Math.max(Math.min(tps / 20.0D, 1.0D), 0.0D));

			String tpsColor;
			if (tps >= 18) {
				tpsColor = "§2";
				bossbar.setColor(BarColor.GREEN);
			} else if (tps >= 15) {
				tpsColor = "§e";
				bossbar.setColor(BarColor.YELLOW);
			} else {
				tpsColor = "§4";
				bossbar.setColor(BarColor.RED);
			}

			double mspt = MinecraftServer.SERVER.tickTimes5s.getAverage();
			String msptColor;
			if (mspt < 40) {
				msptColor = "§2";
			} else if (mspt < 50) {
				msptColor = "§e";
			} else {
				msptColor = "§4";
			}

			bossbar.setTitle("§eTPS§3: " + tpsColor + String.format("%.2f", tps) + " §eMSPT§3: " + msptColor + String.format("%.3f", mspt));
			try {
				Thread.sleep(300L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	public static void removePlayer(Player player) {
		instance.bossbar.removePlayer(player);
	}

	public static void togglePlayer(Player player) {
		BossBar bossbar = instance.bossbar;
		if (bossbar.getPlayers().contains(player)) {
			bossbar.removePlayer(player);
		} else {
			bossbar.addPlayer(player);
		}
		LockSupport.unpark(thread);
	}

	public static void start() {
		thread = new FastThreadLocalThread(instance, "TPS Bar thread");
		thread.setDaemon(true);
		thread.start();
	}

	public static void stop() {
		BossBar bossbar = instance.bossbar;
		bossbar.setVisible(false);
		bossbar.removeAll();
		Bukkit.removeBossBar(instance.key);

		instance.running = false;
		thread.interrupt();
		LockSupport.unpark(thread);
	}
}
