package io.papermc.paper.adventure;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.PacketPlayOutBoss;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.function.Consumer;

public final class VanillaBossBarListener implements BossBar.Listener {
    private final Consumer<PacketPlayOutBoss.Action> action;

    public VanillaBossBarListener(final Consumer<PacketPlayOutBoss.Action> action) {
        this.action = action;
    }

    @Override
    public void bossBarNameChanged(final @NonNull BossBar bar, final @NonNull Component oldName, final @NonNull Component newName) {
        this.action.accept(PacketPlayOutBoss.Action.UPDATE_NAME);
    }

    @Override
    public void bossBarProgressChanged(final @NonNull BossBar bar, final float oldProgress, final float newProgress) {
        this.action.accept(PacketPlayOutBoss.Action.UPDATE_PCT);
    }

    @Override
    public void bossBarColorChanged(final @NonNull BossBar bar, final BossBar.@NonNull Color oldColor, final BossBar.@NonNull Color newColor) {
        this.action.accept(PacketPlayOutBoss.Action.UPDATE_STYLE);
    }

    @Override
    public void bossBarOverlayChanged(final @NonNull BossBar bar, final BossBar.@NonNull Overlay oldOverlay, final BossBar.@NonNull Overlay newOverlay) {
        this.action.accept(PacketPlayOutBoss.Action.UPDATE_STYLE);
    }

    @Override
    public void bossBarFlagsChanged(final @NonNull BossBar bar, final @NonNull Set<BossBar.Flag> flagsAdded, final @NonNull Set<BossBar.Flag> flagsRemoved) {
        this.action.accept(PacketPlayOutBoss.Action.UPDATE_PROPERTIES);
    }
}
