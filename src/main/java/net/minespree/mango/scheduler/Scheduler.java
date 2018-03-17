package net.minespree.mango.scheduler;

import lombok.RequiredArgsConstructor;
import net.minespree.mango.scheduler.base.AbstractListeningService;
import net.minespree.mango.scheduler.base.ListenableScheduledFuture;
import net.minespree.mango.scheduler.base.RunnableAbstractFuture;
import net.minespree.mango.scheduler.base.ScheduledExecutorService;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 14/02/2018
 */
@RequiredArgsConstructor
public class Scheduler<E> extends AbstractListeningService implements ScheduledExecutorService<E> {
    private final TaskManager<E> manager;
    private final PendingTasks<E> tasks;
    private volatile boolean shutdown;

    public Scheduler(PendingTasks<E> tasks) {
        this.tasks = checkNotNull(tasks);
        this.manager = checkNotNull(tasks.getManager());
    }

    @Override
    protected <T> RunnableAbstractFuture<T> newTaskFor(Callable<T> callable) {
        validateState();
        return new CallableTask<>(callable);
    }

    @Override
    protected <T> RunnableAbstractFuture<T> newTaskFor(Runnable runnable, T value) {
        return newTaskFor(Executors.callable(runnable, value));
    }

    @Override
    public void execute(@Nonnull Runnable command) {
        validateState();

        if (command instanceof RunnableFuture) {
            E task = manager.getTask(command);

            tasks.add(task, (Future<?>) command);
        } else {
            // Submit it first
            submit(command);
        }
    }

    @Nonnull
    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        tasks.cancel();

        // We don't support this
        return Collections.emptyList();
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    private void validateState() {
        if (shutdown) {
            throw new RejectedExecutionException("Executor service has shut down. Cannot schedule new tasks.");
        }
    }

    @Nonnull
    @Override
    public <V> ListenableScheduledFuture<V> schedule(@Nonnull Callable<V> callable, long delay, @Nonnull TimeUnit unit) {
        CallableTask<V> task = new CallableTask<>(callable);
        E externalTask = manager.getLaterTask(task, unit.toMillis(delay));

        tasks.add(externalTask, task);

        return task.getScheduledFuture(System.nanoTime() + unit.toNanos(delay), 0);
    }

    @Nonnull
    @Override
    public ListenableScheduledFuture<?> schedule(@Nonnull Runnable task, long delay, @Nonnull TimeUnit unit) {
        return schedule(Executors.callable(task), delay, unit);
    }

    @Nonnull
    @Override
    public ListenableScheduledFuture<?> scheduleAtFixedRate(@Nonnull Runnable task, long initialDelay, long period, @Nonnull TimeUnit unit) {
        return null;
    }

    @Nonnull
    @Override
    public ListenableScheduledFuture<?> scheduleWithFixedDelay(@Nonnull Runnable runnable, long initialDelay, long delay, @Nonnull TimeUnit unit) {
        // We don't currently support this
        return scheduleAtFixedRate(runnable, initialDelay, delay, unit);
    }

    @Override
    public boolean isTerminated() {
        return tasks.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @Nonnull TimeUnit unit) throws InterruptedException {
        return tasks.awaitTermination(timeout, unit);
    }
}
