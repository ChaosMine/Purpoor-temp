package net.pl3x.purpur.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.scheduler.MinecraftInternalPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSBarTask extends BukkitRunnable {
    private static TPSBarTask instance;
    private final NamespacedKey key;
    private BossBar bossbar;

    private TPSBarTask() {
        this.key = new NamespacedKey("purpur", "tpsbar");
    }

    private static TPSBarTask instance() {
        if (instance == null) {
            instance = new TPSBarTask();
        }
        return instance;
    }

    private BossBar bossbar() {
        if (bossbar == null) {
            bossbar = Bukkit.getBossBar(key);
            if (bossbar == null) {
                bossbar = Bukkit.createBossBar(key, "TPS: 20.0", BarColor.RED, BarStyle.SEGMENTED_20);
            }
            bossbar.setVisible(true);
            bossbar.setProgress(1.0D);
        }
        return bossbar;
    }

    @Override
    public void run() {
        BossBar bossbar = bossbar();
        if (bossbar.getPlayers().isEmpty()) {
            return;
        }

        double tps = Bukkit.getTPS()[0];
        if (tps > 20.0D) {
            tps = 20.0D;
        } else if (tps < 0.0D) {
            tps = 0.0D;
        }

        bossbar.setVisible(true);
        bossbar.setProgress(Math.max(Math.min(tps / 20.0D, 1.0D), 0.0D));

        String tpsColor;
        if (tps >= 18) {
            tpsColor = "&2";
            bossbar.setColor(BarColor.GREEN);
        } else if (tps >= 15) {
            tpsColor = "&e";
            bossbar.setColor(BarColor.YELLOW);
        } else {
            tpsColor = "&4";
            bossbar.setColor(BarColor.RED);
        }

        double mspt = Bukkit.getAverageTickTime();
        String msptColor;
        if (mspt < 40) {
            msptColor = "&2";
        } else if (mspt < 50) {
            msptColor = "&e";
        } else {
            msptColor = "&4";
        }

        bossbar.setTitle(ChatColor.translateAlternateColorCodes('&', "&eTPS&3: " + tpsColor + String.format("%.2f", tps) + " &eMSPT&3: " + msptColor + String.format("%.3f", mspt)));
    }

    @Override
    public void cancel() {
        super.cancel();
        BossBar bossbar = bossbar();
        bossbar.setVisible(false);
        bossbar.removeAll();
        Bukkit.removeBossBar(key);
    }

    public static void removePlayer(Player player) {
        instance().bossbar().removePlayer(player);
    }

    public static void togglePlayer(Player player) {
        BossBar bossbar = instance().bossbar();
        if (bossbar.getPlayers().contains(player)) {
            bossbar.removePlayer(player);
        } else {
            bossbar.addPlayer(player);
            instance.run();
        }
    }

    public static void start() {
        stop();
        instance().runTaskTimerAsynchronously(new MinecraftInternalPlugin(), 20L, 20L);
    }

    public static void stop() {
        if (instance != null) {
            instance.cancel();
        }
    }
}
