package com.destroystokyo.paper.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * copied from https://github.com/Spottedleaf/ConcurrentUtil/blob/master/src/main/java/ca/spottedleaf/concurrentutil/lock/WeakSeqLock.java
 *
 * @author Spottedleaf
 */
public final class WeakSeqLock {

    private long lock;

    private static final VarHandle LOCK_HANDLE;

    static {
        try {
            LOCK_HANDLE = MethodHandles.lookup().findVarHandle(WeakSeqLock.class, "lock", long.class);
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    private long getLockPlain() {
        return (long) LOCK_HANDLE.get(this);
    }

    private long getLockOpaque() {
        return (long) LOCK_HANDLE.getOpaque(this);
    }

    private void setLockOpaque(final long value) {
        LOCK_HANDLE.setOpaque(this, value);
    }

    public WeakSeqLock() {
        VarHandle.storeStoreFence();
    }

    public void acquireWrite() {
        final long lock = this.getLockPlain();
        this.setLockOpaque(lock + 1);
        VarHandle.storeStoreFence();
    }

    public boolean tryAcquireWrite() {
        this.acquireWrite();
        return true;
    }

    public void releaseWrite() {
        final long lock = this.getLockPlain();
        VarHandle.storeStoreFence();
        this.setLockOpaque(lock + 1);
    }

    public void abortWrite() {
        final long lock = this.getLockPlain();
        VarHandle.storeStoreFence();
        this.setLockOpaque(lock ^ 1);
    }

    public long acquireRead() {
        long failures = 0;
        long curr;

        for (curr = this.getLockOpaque(); !this.canRead(curr); curr = this.getLockOpaque()) {
            for (int i = 0; i < failures; ++i) {
                Thread.onSpinWait();
            }

            if (++failures > 5_000) { /* TODO determine a threshold */
                Thread.yield();
            }
            /* Better waiting is beyond the scope of this lock; if it is needed the lock is being misused */
        }

        VarHandle.loadLoadFence();
        return curr;
    }

    public boolean tryReleaseRead(final long read) {
        VarHandle.loadLoadFence();
        return this.getLockOpaque() == read;
    }

    public long getSequentialCounter() {
        final long lock = this.getLockOpaque();
        VarHandle.loadLoadFence();
        return lock;
    }

    public boolean canRead(final long read) {
        return (read & 1) == 0;
    }
}
