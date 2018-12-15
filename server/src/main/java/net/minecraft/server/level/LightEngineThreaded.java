package net.minecraft.server.level;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap; // Paper
// Tuinity start
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
// Tuinity end
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.SectionPosition;
import net.minecraft.util.thread.Mailbox;
import net.minecraft.util.thread.ThreadedMailbox;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ILightAccess;
import net.minecraft.world.level.chunk.NibbleArray;
import net.minecraft.world.level.lighting.LightEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LightEngineThreaded extends LightEngine implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ThreadedMailbox<Runnable> b; private ThreadedMailbox<Runnable> getExecutor() { return this.b; } // Tuinity - OBFHELPER
    // Paper start
    private static final int MAX_PRIORITIES = PlayerChunkMap.GOLDEN_TICKET + 2;

    private boolean isChunkLightStatus(long pair) {
        if (true) return true; // Tuinity - viewing ticket levels async can result in the viewing of transient levels, and LIGHT ticket isn't guaranteed to exist for all loading chunks thanks to really dumb unloading behaviors with the chunk system
        PlayerChunk playerChunk = playerChunkMap.getVisibleChunk(pair);
        if (playerChunk == null) {
            return false;
        }
        ChunkStatus status = PlayerChunk.getChunkStatus(playerChunk.getTicketLevel());
        return status != null && status.isAtLeastStatus(ChunkStatus.LIGHT);
    }

    static class ChunkLightQueue {
        public boolean shouldFastUpdate;
        java.util.ArrayDeque<Runnable> pre = new java.util.ArrayDeque<Runnable>();
        java.util.ArrayDeque<Runnable> post = new java.util.ArrayDeque<Runnable>();

        ChunkLightQueue(long chunk) {}
    }

    static class PendingLightTask {
        long chunkId;
        IntSupplier priority;
        Runnable pre;
        Runnable post;
        boolean fastUpdate;

        public PendingLightTask(long chunkId, IntSupplier priority, Runnable pre, Runnable post, boolean fastUpdate) {
            this.chunkId = chunkId;
            this.priority = priority;
            this.pre = pre;
            this.post = post;
            this.fastUpdate = fastUpdate;
        }
    }


    // Retain the chunks priority level for queued light tasks
    class LightQueue {
        private int size = 0;
        private final Long2ObjectLinkedOpenHashMap<ChunkLightQueue>[] buckets = new Long2ObjectLinkedOpenHashMap[MAX_PRIORITIES];
        private final java.util.concurrent.ConcurrentLinkedQueue<PendingLightTask> pendingTasks = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final java.util.concurrent.ConcurrentLinkedQueue<Runnable> priorityChanges = new java.util.concurrent.ConcurrentLinkedQueue<>();

        private LightQueue() {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new Long2ObjectLinkedOpenHashMap<>();
            }
        }

        public void changePriority(long pair, int currentPriority, int priority) {
            this.priorityChanges.add(() -> {
                ChunkLightQueue remove = this.buckets[currentPriority].remove(pair);
                if (remove != null) {
                    ChunkLightQueue existing = this.buckets[Math.max(1, priority)].put(pair, remove);
                    if (existing != null) {
                        remove.pre.addAll(existing.pre);
                        remove.post.addAll(existing.post);
                    }
                }
            });
        }

        public final void addChunk(long chunkId, IntSupplier priority, Runnable pre, Runnable post) {
            pendingTasks.add(new PendingLightTask(chunkId, priority, pre, post, true));
            queueUpdate();
        }

        public final void add(long chunkId, IntSupplier priority, LightEngineThreaded.Update type, Runnable run) {
            pendingTasks.add(new PendingLightTask(chunkId, priority, type == Update.PRE_UPDATE ? run : null, type == Update.POST_UPDATE ? run : null, false));
        }
        public final void add(PendingLightTask update) {
            int priority = update.priority.getAsInt();
            ChunkLightQueue lightQueue = this.buckets[priority].computeIfAbsent(update.chunkId, ChunkLightQueue::new);

            if (update.pre != null) {
                this.size++;
                lightQueue.pre.add(update.pre);
            }
            if (update.post != null) {
                this.size++;
                lightQueue.post.add(update.post);
            }
            if (update.fastUpdate) {
                lightQueue.shouldFastUpdate = true;
            }
        }

        public final boolean isEmpty() {
            return this.size == 0 && this.pendingTasks.isEmpty();
        }

        public final int size() {
            return this.size;
        }

        public boolean poll(java.util.List<Runnable> pre, java.util.List<Runnable> post) {
            PendingLightTask pending;
            while ((pending = pendingTasks.poll()) != null) {
                add(pending);
            }
            Runnable run;
            while ((run = priorityChanges.poll()) != null) {
                run.run();
            }
            boolean hasWork = false;
            Long2ObjectLinkedOpenHashMap<ChunkLightQueue>[] buckets = this.buckets;
            int priority = 0;
            while (priority < MAX_PRIORITIES && !isEmpty()) {
                Long2ObjectLinkedOpenHashMap<ChunkLightQueue> bucket = buckets[priority];
                if (bucket.isEmpty()) {
                    priority++;
                    if (hasWork) {
                        return true;
                    } else {
                        continue;
                    }
                }
                ChunkLightQueue queue = bucket.removeFirst();
                this.size -= queue.pre.size() + queue.post.size();
                pre.addAll(queue.pre);
                post.addAll(queue.post);
                queue.pre.clear();
                queue.post.clear();
                hasWork = true;
                if (queue.shouldFastUpdate) {
                    return true;
                }
            }
            return hasWork;
        }
    }

    final LightQueue queue = new LightQueue();
    // Paper end
    private final PlayerChunkMap d; private final PlayerChunkMap playerChunkMap; // Paper
    private final Mailbox<ChunkTaskQueueSorter.a<Runnable>> e;
    private volatile int f = 5;
    private final AtomicBoolean g = new AtomicBoolean();

    // Tuinity start - replace light engine impl
    protected final com.tuinity.tuinity.chunk.light.StarLightInterface theLightEngine;
    public final boolean hasBlockLight;
    public final boolean hasSkyLight;
    // Tuinity end - replace light engine impl

    public LightEngineThreaded(ILightAccess ilightaccess, PlayerChunkMap playerchunkmap, boolean flag, ThreadedMailbox<Runnable> threadedmailbox, Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailbox) {
        super(ilightaccess, true, flag);
        this.d = playerchunkmap; this.playerChunkMap = d; // Paper
        this.e = mailbox;
        this.b = threadedmailbox;
        // Tuinity start - replace light engine impl
        this.hasBlockLight = true;
        this.hasSkyLight = flag;
        this.theLightEngine = com.tuinity.tuinity.config.TuinityConfig.useNewLightEngine ? new com.tuinity.tuinity.chunk.light.StarLightInterface(ilightaccess, flag, true) : null;
        // Tuinity end - replace light engine impl
    }

    // Tuinity start - replace light engine impl
    protected final IChunkAccess getChunk(final int chunkX, final int chunkZ) {
        final WorldServer world = (WorldServer)this.theLightEngine.getWorld();
        return world.getChunkProvider().getChunkAtImmediately(chunkX, chunkZ);
    }

    protected long relightCounter;

    public int relight(java.util.Set<ChunkCoordIntPair> chunks_param,
                        java.util.function.Consumer<ChunkCoordIntPair> chunkLightCallback,
                        java.util.function.IntConsumer onComplete) {
        if (!org.bukkit.Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Must only be called on the main thread");
        }

        java.util.Set<ChunkCoordIntPair> chunks = new java.util.LinkedHashSet<>(chunks_param);
        // add tickets
        java.util.Map<ChunkCoordIntPair, Long> ticketIds = new java.util.HashMap<>();
        int totalChunks = 0;
        for (java.util.Iterator<ChunkCoordIntPair> iterator = chunks.iterator(); iterator.hasNext();) {
            final ChunkCoordIntPair chunkPos = iterator.next();

            final IChunkAccess chunk = this.theLightEngine.getWorld().getChunkProvider().getChunkAtImmediately(chunkPos.x, chunkPos.z);
            if (chunk == null || !chunk.isLit() || !chunk.getChunkStatus().isAtLeastStatus(ChunkStatus.LIGHT)) {
                // cannot relight this chunk
                iterator.remove();
                continue;
            }

            final Long id = Long.valueOf(this.relightCounter++);

            this.theLightEngine.getWorld().getChunkProvider().addTicketAtLevel(TicketType.CHUNK_RELIGHT, chunkPos, net.minecraft.server.MCUtil.getTicketLevelFor(ChunkStatus.LIGHT), id);
            ticketIds.put(chunkPos, id);

            ++totalChunks;
        }

        this.getExecutor().queue(() -> {
            this.theLightEngine.relightChunks(chunks, (ChunkCoordIntPair chunkPos) -> {
                chunkLightCallback.accept(chunkPos);
                ((java.util.concurrent.Executor)this.theLightEngine.getWorld().getChunkProvider().serverThreadQueue).execute(() -> {
                    this.theLightEngine.getWorld().getChunkProvider().playerChunkMap.getUpdatingChunk(chunkPos.pair()).sendPacketToTrackedPlayers(new net.minecraft.network.protocol.game.PacketPlayOutLightUpdate(chunkPos, this, true), false);
                    this.theLightEngine.getWorld().getChunkProvider().removeTicketAtLevel(TicketType.CHUNK_RELIGHT, chunkPos, net.minecraft.server.MCUtil.getTicketLevelFor(ChunkStatus.LIGHT), ticketIds.get(chunkPos));
                });
            }, onComplete);
        });
        this.queueUpdate();

        return totalChunks;
    }

    protected final Long2IntOpenHashMap holdingChunks = new Long2IntOpenHashMap();
    protected final LongArrayList postWorkTicketRelease = new LongArrayList();

    private void addLightWorkTicket(int chunkX, int chunkZ) {
        final long coordinate = net.minecraft.server.MCUtil.getCoordinateKey(chunkX, chunkZ);
        final int current = this.holdingChunks.addTo(coordinate, 1);
        if (current == 0) {
            final ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(coordinate);
            this.theLightEngine.getWorld().getChunkProvider().addTicketAtLevel(TicketType.LIGHT_UPDATE, chunkPos,
                    net.minecraft.server.MCUtil.getTicketLevelFor(ChunkStatus.LIGHT), chunkPos);
        }
    }

    protected final void releaseLightWorkChunk(int chunkX, int chunkZ) {
        final long coordinate = net.minecraft.server.MCUtil.getCoordinateKey(chunkX, chunkZ);
        final int current = this.holdingChunks.get(coordinate);
        if (current == 1) {
            this.holdingChunks.remove(coordinate);
            final ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(coordinate);
            this.theLightEngine.getWorld().getChunkProvider().removeTicketAtLevel(TicketType.LIGHT_UPDATE, chunkPos,
                    net.minecraft.server.MCUtil.getTicketLevelFor(ChunkStatus.LIGHT), chunkPos);
        } else {
            this.holdingChunks.put(coordinate, current - 1);
        }
    }

    protected final CompletableFuture<IChunkAccess> acquireLightWorkChunk(int chunkX, int chunkZ) {
        ChunkProviderServer chunkProvider = this.theLightEngine.getWorld().getChunkProvider();
        PlayerChunkMap chunkMap = chunkProvider.playerChunkMap;
        int targetLevel = net.minecraft.server.MCUtil.getTicketLevelFor(ChunkStatus.LIGHT);

        this.addLightWorkTicket(chunkX, chunkZ);

        PlayerChunk playerChunk = chunkMap.getUpdatingChunk(net.minecraft.server.MCUtil.getCoordinateKey(chunkX, chunkZ));
        ChunkStatus holderStatus;
        if (playerChunk == null || playerChunk.getTicketLevel() > targetLevel ||
                (holderStatus = playerChunk.getChunkHolderStatus()) == null ||
                !holderStatus.isAtLeastStatus(ChunkStatus.LIGHT)) {
            CompletableFuture<IChunkAccess> ret = new CompletableFuture<>();

            chunkProvider.getChunkAtAsynchronously(chunkX, chunkZ, ChunkStatus.LIGHT, true, false, ret::complete);

            return ret;
        }

        return CompletableFuture.completedFuture(playerChunk.getAvailableChunkNow());
    }

    // note: task is discarded if the chunk is not at light status or if the chunk is not lit
    protected final void scheduleLightWorkTask(int chunkX, int chunkZ, LightEngineThreaded.Update type, Runnable task) {
        IChunkAccess current = this.getChunk(chunkX, chunkZ);

        if (current == null || !current.getChunkStatus().isAtLeastStatus(ChunkStatus.LIGHT)) {
            return;
        }

        if (current.getChunkStatus() != ChunkStatus.FULL) {
            // do not keep chunk loaded, we are probably in a gen thread
            // if we proceed to add a ticket the chunk will be loaded, which is not what we want (avoid cascading gen)
            this.scheduleTask(chunkX, chunkZ, type, task);
            return;
        }

        if (!org.bukkit.Bukkit.isPrimaryThread()) {
            this.playerChunkMap.mainInvokingExecutor.execute(() -> {
                this.scheduleLightWorkTask(chunkX, chunkZ, type, task);
            });
            return;
        }

        this.acquireLightWorkChunk(chunkX, chunkZ).whenCompleteAsync((chunk, throwable) -> {
            if (throwable != null) {
                LOGGER.fatal("Failed to load light chunk for light work", throwable);
                this.releaseLightWorkChunk(chunkX, chunkZ);
            } else {
                this.scheduleTask(chunkX, chunkZ, type, () -> {
                    try {
                        task.run();
                    } finally {
                        this.postWorkTicketRelease.add(net.minecraft.server.MCUtil.getCoordinateKey(chunkX, chunkZ));
                    }
                });
            }
        }, this.playerChunkMap.mainInvokingExecutor);
    }

    // override things from superclass

    @Override
    public boolean a() {
        return this.theLightEngine != null ? false : super.a();
    }

    @Override
    public net.minecraft.world.level.lighting.LightEngineLayerEventListener a(EnumSkyBlock var0) {
        if (this.theLightEngine == null) {
            return super.a(var0);
        }
        if (var0 == EnumSkyBlock.BLOCK) {
            return this.theLightEngine.getBlockReader();
        } else {
            return this.theLightEngine.getSkyReader();
        }
    }

    @Override
    public int b(BlockPosition var0, int var1) {
        if (this.theLightEngine == null) {
            return super.b(var0, var1);
        }
        int var2 = this.theLightEngine.getSkyReader().b(var0) - var1;
        int var3 = this.theLightEngine.getBlockReader().b(var0);
        return Math.max(var3, var2);
    }
    // Tuinity end - replace light engine impl

    public void close() {}

    @Override
    public int a(int i, boolean flag, boolean flag1) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition, int i) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.immutableCopy();

        // Tuinity start - replace light engine impl
        if (this.theLightEngine != null) {
            this.scheduleLightWorkTask(blockposition1.getX() >> 4, blockposition1.getZ() >> 4, LightEngineThreaded.Update.POST_UPDATE, () -> {
                this.theLightEngine.blockChange(blockposition1);
            });
            return;
        }
        // Tuinity start - replace light engine impl

        this.a(blockposition.getX() >> 4, blockposition.getZ() >> 4, LightEngineThreaded.Update.POST_UPDATE, SystemUtils.a(() -> {
            super.a(blockposition1);
        }, () -> {
            return "checkBlock " + blockposition1;
        }));
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair) {
        // Tuinity start - replace light impl
        if (this.theLightEngine != null) {
            return;
        }
        // Tuinity end - replace light impl
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, false);
            super.a(chunkcoordintpair, false);

            int i;

            for (i = -1; i < 17; ++i) {
                super.a(EnumSkyBlock.BLOCK, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
                super.a(EnumSkyBlock.SKY, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
            }

            for (i = 0; i < 16; ++i) {
                super.a(SectionPosition.a(chunkcoordintpair, i), true);
            }

        }, () -> {
            return "updateChunkStatus " + chunkcoordintpair + " " + true;
        }));
    }

    @Override
    public void a(SectionPosition sectionposition, boolean flag) {
        // Tuinity start - replace light engine impl
        if (this.theLightEngine != null) {
            this.scheduleLightWorkTask(sectionposition.getX(), sectionposition.getZ(), LightEngineThreaded.Update.POST_UPDATE, () -> {
                this.theLightEngine.sectionChange(sectionposition, flag);
            });
            return;
        }
        // Tuinity start - replace light engine impl
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(sectionposition, flag);
        }, () -> {
            return "updateSectionStatus " + sectionposition + " " + flag;
        }));
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        // Tuinity start - replace light impl
        if (this.theLightEngine != null) {
            return;
        }
        // Tuinity end - replace light impl
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(chunkcoordintpair, flag);
        }, () -> {
            return "enableLight " + chunkcoordintpair + " " + flag;
        }));
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, SectionPosition sectionposition, @Nullable NibbleArray nibblearray, boolean flag) {
        // Tuinity start - replace light impl
        if (this.theLightEngine != null) {
            return;
        }
        // Tuinity end - replace light impl
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(enumskyblock, sectionposition, nibblearray, flag);
        }, () -> {
            return "queueData " + sectionposition;
        }));
    }

    private void scheduleTask(int x, int z, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) { this.a(x, z, lightenginethreaded_update, runnable); } // Tuinity - OBFHELPER
    private void a(int i, int j, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        this.a(i, j, this.d.c(ChunkCoordIntPair.pair(i, j)), lightenginethreaded_update, runnable);
    }

    private void a(int i, int j, IntSupplier intsupplier, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        // Paper start - replace method
        this.queue.add(ChunkCoordIntPair.pair(i, j), intsupplier, lightenginethreaded_update, runnable);
        // Paper end
    }

    @Override
    public void b(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        // Tuinity start - replace light impl
        if (this.theLightEngine != null) {
            return;
        }
        // Tuinity end - replace light impl
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, flag);
        }, () -> {
            return "retainData " + chunkcoordintpair;
        }));
    }

    public CompletableFuture<IChunkAccess> a(IChunkAccess ichunkaccess, boolean flag) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();

        // Tuinity start - replace light engine
        if (this.theLightEngine != null) {
            // make the completion of this future only depend on pre-update execution
            // post-update is used for block updates, so by using pre here we prioritise chunk lighting
            return CompletableFuture.supplyAsync(() -> {
                Boolean[] emptySections = com.tuinity.tuinity.chunk.light.StarLightEngine.getEmptySectionsForChunk(ichunkaccess);
                if (!flag) {
                    ichunkaccess.setLit(false);
                    this.theLightEngine.lightChunk(ichunkaccess, emptySections);
                    ichunkaccess.setLit(true);
                } else {
                    this.theLightEngine.forceLoadInChunk(ichunkaccess, emptySections);
                    this.theLightEngine.checkChunkEdges(chunkcoordintpair.x, chunkcoordintpair.z);
                }

                // safe to move the release light to here, as all the work required is done now
                this.playerChunkMap.removeLightTicket(chunkcoordintpair); // copied from below
                return ichunkaccess;
            }, (runnable) -> {
                LightEngineThreaded.this.scheduleTask(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.PRE_UPDATE, runnable);
                LightEngineThreaded.this.queueUpdate();
            }).whenComplete((IChunkAccess chunk, Throwable throwable) -> {
                if (throwable != null && !(throwable instanceof ThreadDeath)) {
                    LOGGER.fatal("Failed to light chunk " + ichunkaccess.getPos().toString() + " in world '" + this.theLightEngine.getWorld().getWorld().getName() + "'", throwable);
                }
            });
        }
        // Tuinity end - replace light engine

        // Paper start
        //ichunkaccess.b(false); // Don't need to disable this
        long pair = chunkcoordintpair.pair();
        CompletableFuture<IChunkAccess> future = new CompletableFuture<>();
        IntSupplier prioritySupplier = playerChunkMap.getPrioritySupplier(pair);
        boolean[] skippedPre = {false};
        this.queue.addChunk(pair, prioritySupplier, SystemUtils.a(() -> {
            if (!isChunkLightStatus(pair)) {
                future.complete(ichunkaccess);
                skippedPre[0] = true;
                return;
            }
            // Paper end
            ChunkSection[] achunksection = ichunkaccess.getSections();

            for (int i = 0; i < 16; ++i) {
                ChunkSection chunksection = achunksection[i];

                if (!ChunkSection.a(chunksection)) {
                    super.a(SectionPosition.a(chunkcoordintpair, i), false);
                }
            }

            super.a(chunkcoordintpair, true);
            if (!flag) {
                ichunkaccess.m().forEach((blockposition) -> {
                    super.a(blockposition, ichunkaccess.g(blockposition));
                });
            }

            // this.d.c(chunkcoordintpair); // Paper - move into post task below
        }, () -> {
            return "lightChunk " + chunkcoordintpair + " " + flag;
            // Paper start  - merge the 2 together
        }), () -> {
            this.d.c(chunkcoordintpair); // Paper - release light tickets as post task to ensure they stay loaded until fully done
            if (skippedPre[0]) return; // Paper - future's already complete
            ichunkaccess.b(true);
            super.b(chunkcoordintpair, false);
            // Paper start
            future.complete(ichunkaccess);
        });
        return future;
        // Paper end
    }

    public void queueUpdate() {
        if ((!this.queue.isEmpty() || (this.theLightEngine == null && super.a())) && this.g.compareAndSet(false, true)) { // Paper // Tuinity - replace light impl
            this.b.a((() -> { // Paper - decompile error
                this.b();
                this.g.set(false);
                queueUpdate(); // Paper - if we still have work to do, do it!
            }));
        }

    }

    // Paper start - replace impl
    private final java.util.List<Runnable> pre = new java.util.ArrayList<>();
    private final java.util.List<Runnable> post = new java.util.ArrayList<>();
    private void b() {
        //final long start = System.nanoTime(); // TODO remove debug
        if (queue.poll(pre, post)) {
            pre.forEach(Runnable::run);
            pre.clear();
            if (this.theLightEngine == null) super.a(Integer.MAX_VALUE, true, true); // Tuinity - replace light impl
            post.forEach(Runnable::run);
            post.clear();
        } else {
            // might have level updates to go still
            if (this.theLightEngine == null) super.a(Integer.MAX_VALUE, true, true); // Tuinity - replace light impl
        }
        // Tuinity start - replace light impl
        if (this.theLightEngine != null) {
            this.theLightEngine.propagateChanges();
            if (!this.postWorkTicketRelease.isEmpty()) {
                LongArrayList copy = this.postWorkTicketRelease.clone();
                this.postWorkTicketRelease.clear();
                this.playerChunkMap.mainInvokingExecutor.execute(() -> {
                    LongIterator iterator = copy.iterator();
                    while (iterator.hasNext()) {
                        long coordinate = iterator.nextLong();
                        this.releaseLightWorkChunk(net.minecraft.server.MCUtil.getCoordinateX(coordinate), net.minecraft.server.MCUtil.getCoordinateZ(coordinate));
                    }
                });
            }
        }
        // Tuinity end - replace light impl
        // Paper end
        //final long end = System.nanoTime(); // TODO remove debug
        //System.out.println("Block updates took " + (end - start) * 1.0e-6 + "ms"); // TODO remove debug
    }

    public void a(int i) {
        this.f = i;
    }

    static enum Update {

        PRE_UPDATE, POST_UPDATE;

        private Update() {}
    }
}
