package net.minespree.mango.scheduler;

import net.minespree.mango.scheduler.base.ListenableScheduledFuture;
import net.minespree.mango.scheduler.base.RunnableAbstractFuture;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @since 14/02/2018
 */
public class CallableTask<T> extends RunnableAbstractFuture<T> {
    protected final Callable<T> compute;

    public CallableTask(Callable<T> compute) {
        this.compute = checkNotNull(compute, "Compute cannot be null");
    }

    public ListenableScheduledFuture<T> getScheduledFuture(final long startTime, final long nextDelay) {
        return new ListenableScheduledFuture<T>() {
            @Override
            public T get() throws InterruptedException, ExecutionException {
                return CallableTask.this.get();
            }

            @Override
            public T get(long timeout, @Nonnull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return CallableTask.this.get(timeout, unit);
            }

            @Override
            public long getDelay(@Nonnull TimeUnit unit) {
                long current = System.nanoTime();

                if (current < startTime || !isPeriodic()) {
                    return unit.convert(startTime - current, TimeUnit.NANOSECONDS);
                } else {
                    return unit.convert(((current - startTime) % nextDelay), TimeUnit.NANOSECONDS);
                }
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return CallableTask.this.cancel(mayInterruptIfRunning);
            }

            @Override
            public void addListener(@Nonnull Runnable runnable, @Nonnull Executor executor) {
                CallableTask.this.addListener(runnable, executor);
            }

            @Override
            public int compareTo(@Nonnull Delayed o) {
                return Long.compare(
                        getDelay(TimeUnit.NANOSECONDS),
                        o.getDelay(TimeUnit.NANOSECONDS)
                );
            }

            @Override
            public boolean isCancelled() {
                return CallableTask.this.isCancelled();
            }

            @Override
            public boolean isDone() {
                return CallableTask.this.isDone();
            }

            public boolean isPeriodic() {
                return nextDelay > 0;
            }

            @Override
            public void run() {
                compute();
            }
        };
    }

    /**
     * Invoked by the thread responsible for computing this future.
     */
    protected void compute() {
        try {
            if (!isCancelled()) {
                set(compute.call());
            }
        } catch (Throwable e) {
            setException(e);
        }
    }

    @Override
    public void run() {
        compute();
    }
}
