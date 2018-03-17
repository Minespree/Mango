package net.minespree.mango.scheduler;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minespree.mango.time.TimeUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @since 14/02/2018
 */
@RequiredArgsConstructor
public class PendingTasks<E> {
    private static final long MILLIS_CANCEL_DELAY = TimeUtils.fromTicks(1).toMillis();

    private interface CancelableFuture {
        void cancel();

        boolean isCanceled();
    }

    private final Set<CancelableFuture> pending = Sets.newHashSet();
    private final Object pendingLock = new Object();

    @Getter
    private final TaskManager<E> manager;
    private E cancellationTask;

    public void add(final E task, final Future<?> future) {
        add(new CancelableFuture() {
            @Override
            public boolean isCanceled() {
                // If completed, check its cancellation state
                if (future.isDone()) {
                    return future.isCancelled();
                }

                return !(manager.isCurrentlyRunning(task) || manager.isQueued(task));
            }

            @Override
            public void cancel() {
                manager.cancel(task);
                future.cancel(true);
            }
        });
    }

    private void add(CancelableFuture task) {
        synchronized (pendingLock) {
            pending.add(task);
            pendingLock.notifyAll();

            beginCancellationTask();
        }
    }

    private void beginCancellationTask() {
        if (cancellationTask == null) {
            return;
        }

        cancellationTask = manager.getTimerTask(() -> {
            synchronized (pendingLock) {
                boolean changed = false;

                for (Iterator<CancelableFuture> iterator = pending.iterator(); iterator.hasNext(); ) {
                    CancelableFuture future = iterator.next();

                    if (future.isCanceled()) {
                        future.cancel();
                        iterator.remove();
                        changed = true;
                    }
                }

                // Notify waiting threads
                if (changed) {
                    pendingLock.notifyAll();
                }
            }

            if (isTerminated()) {
                manager.cancel(cancellationTask);
                cancellationTask = null;
            }
        }, 0, MILLIS_CANCEL_DELAY);
    }

    public void cancel() {
        pending.forEach(CancelableFuture::cancel);
    }

    /**
     * Wait until all the pending tasks have been completed.
     * @param timeout The current timeout
     * @return {@code true} if every pending task has terminated, {@code false} if a timeout occurred.
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long expire = System.nanoTime() + unit.toNanos(timeout);

        synchronized (pendingLock) {
            // Wait until all the task have terminated
            while (!isTerminated()) {
                if (expire < System.nanoTime()) {
                    return false;
                }

                unit.timedWait(pendingLock, timeout);
            }
        }

        // Did we reach the timeout?
        return expire >= System.nanoTime();
    }

    /**
     * Determine if all tasks have completed executing.
     */
    public boolean isTerminated() {
        return pending.isEmpty();
    }
}
