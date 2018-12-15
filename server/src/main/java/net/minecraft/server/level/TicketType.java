package net.minecraft.server.level;

import java.util.Comparator;
import net.minecraft.core.BaseBlockPosition;
import net.minecraft.core.BlockPosition;
import net.minecraft.util.Unit;
import net.minecraft.world.level.ChunkCoordIntPair;

public class TicketType<T> {

    private final String i;
    private final Comparator<T> j;
    public long loadPeriod;
    public static final TicketType<Unit> START = a("start", (unit, unit1) -> {
        return 0;
    });
    public static final TicketType<Unit> DRAGON = a("dragon", (unit, unit1) -> {
        return 0;
    });
    public static final TicketType<ChunkCoordIntPair> PLAYER = a("player", Comparator.comparingLong(ChunkCoordIntPair::pair));
    public static final TicketType<ChunkCoordIntPair> FORCED = a("forced", Comparator.comparingLong(ChunkCoordIntPair::pair));
    public static final TicketType<ChunkCoordIntPair> LIGHT = a("light", Comparator.comparingLong(ChunkCoordIntPair::pair));
    public static final TicketType<BlockPosition> PORTAL = a("portal", BaseBlockPosition::compareTo, 300);
    public static final TicketType<Long> LOGIN = a("login", Long::compareTo, 100); // Paper
    public static final TicketType<Integer> POST_TELEPORT = a("post_teleport", Integer::compareTo, 5);
    public static final TicketType<ChunkCoordIntPair> UNKNOWN = a("unknown", Comparator.comparingLong(ChunkCoordIntPair::pair), 1);
    public static final TicketType<Unit> PLUGIN = a("plugin", (a, b) -> 0); // CraftBukkit
    public static final TicketType<org.bukkit.plugin.Plugin> PLUGIN_TICKET = a("plugin_ticket", (plugin1, plugin2) -> plugin1.getClass().getName().compareTo(plugin2.getClass().getName())); // CraftBukkit
    public static final TicketType<Long> FUTURE_AWAIT = a("future_await", Long::compareTo); // Paper
    public static final TicketType<Long> ASYNC_LOAD = a("async_load", Long::compareTo); // Paper
    public static final TicketType<ChunkCoordIntPair> PRIORITY = a("priority", Comparator.comparingLong(ChunkCoordIntPair::pair), 300); // Paper
    public static final TicketType<ChunkCoordIntPair> URGENT = a("urgent", Comparator.comparingLong(ChunkCoordIntPair::pair), 300); // Paper
    public static final TicketType<Long> DELAYED_UNLOAD = a("delayed_unload", Long::compareTo); // Tuinity - delay chunk unloads
    public static final TicketType<Long> REQUIRED_LOAD = a("required_load", Long::compareTo); // Tuinity - make sure getChunkAt does not fail
    public static final TicketType<ChunkCoordIntPair> LIGHT_UPDATE = a("light_update", Comparator.comparingLong(ChunkCoordIntPair::pair)); // Tuinity - ensure chunks stay loaded for lighting
    public static final TicketType<Long> CHUNK_RELIGHT = a("chunk_relight", Long::compareTo); // Tuinity - ensure chunk stays loaded for relighting

    // Tuinity start - delay chunk unloads
    boolean delayUnloadViable = true;
    static {
        TicketType.LIGHT.delayUnloadViable = false;
        TicketType.PLUGIN.delayUnloadViable = false;
        TicketType.PRIORITY.delayUnloadViable = false;
        TicketType.URGENT.delayUnloadViable = false;
        TicketType.DELAYED_UNLOAD.delayUnloadViable = false;
        TicketType.LIGHT_UPDATE.delayUnloadViable = false; // Tuinity - ensure chunks stay loaded for lighting
    }
    // Tuinity end - delay chunk unloads
    public static <T> TicketType<T> a(String s, Comparator<T> comparator) {
        return new TicketType<>(s, comparator, 0L);
    }

    public static <T> TicketType<T> a(String s, Comparator<T> comparator, int i) {
        return new TicketType<>(s, comparator, (long) i);
    }

    protected TicketType(String s, Comparator<T> comparator, long i) {
        this.i = s;
        this.j = comparator;
        this.loadPeriod = i;
    }

    public String toString() {
        return this.i;
    }

    public Comparator<T> a() {
        return this.j;
    }

    public long b() {
        return this.loadPeriod;
    }
}
