package net.minespree.mango.scheduler.base;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides default implementations of {@link ListeningExecutorService}
 * execution methods. This class implements the {@link #submit(Runnable)}, {@link #invokeAny(Collection)},
 * and {@link #invokeAll(Collection)} methods using a {@link ListenableFutureTask}
 * returned by {@code newTaskFor}. For example, the implementation of
 * {@link #submit(Runnable)} creates an associated
 * {@link ListenableFutureTask} that is executed and returned.
 * <p>
 * This file is a modified version of
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/AbstractExecutorService.java?revision=1.35
 * which contained the following notice:
 * <p>
 * Written by Doug Lea with assistance from members of JCP JSR-166 Expert Group and released to the
 * public domain, as explained at http://creativecommons.org/publicdomain/zero/1.0/
 * <p>
 * Rationale for copying:
 * Guava targets JDK5, whose AbstractExecutorService class lacks the newTaskFor protected
 * customization methods needed by MoreExecutors.listeningDecorator. This class is a copy of
 * AbstractExecutorService from the JSR166 CVS repository. It contains the desired methods.
 *
 * @since 14/02/2018
 */
public abstract class AbstractListeningService implements ListeningExecutorService {
    /**
     * Returns a {@link RunnableAbstractFuture} which when run will execute
     * the underlying runnable and which, as a {@link Future}, will yield
     * the given value as its result and provide for cancellation of the
     * underlying task.
     */
    protected abstract <T> RunnableAbstractFuture<T> newTaskFor(Runnable runnable, T value);

    /**
     * @see #newTaskFor(Runnable, Object)
     */
    protected abstract <T> RunnableAbstractFuture<T> newTaskFor(Callable<T> callable);

    @Nonnull
    @Override
    public ListenableFuture<?> submit(@Nonnull Runnable runnable) {
        checkNotNull(runnable);

        RunnableAbstractFuture<Void> task = newTaskFor(runnable, null);
        execute(task);

        return task;
    }

    @Nonnull
    @Override
    public <T> ListenableFuture<T> submit(@Nonnull Runnable runnable, @Nonnull T result) {
        checkNotNull(runnable);

        RunnableAbstractFuture<T> task = newTaskFor(runnable, result);
        execute(task);

        return task;
    }

    @Nonnull
    @Override
    public <T> ListenableFuture<T> submit(@Nonnull Callable<T> callable) {
        checkNotNull(callable);

        RunnableAbstractFuture<T> task = newTaskFor(callable);
        execute(task);

        return task;
    }

    private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks, boolean timed, long nanos) throws InterruptedException, TimeoutException, ExecutionException {
        checkNotNull(tasks);
        int taskCount = tasks.size();

        checkArgument(taskCount > 0);

        List<Future<T>> futures = Lists.newArrayListWithCapacity(taskCount);
        ExecutorCompletionService<T> service = new ExecutorCompletionService<>(this);

        /*
        For efficiency, especially in executors with limited
        parallelism, check to see if previously submitted tasks are
        done before submitting more of them. This interleaving
        plus the exception mechanics account for messiness of main
        loop.
         */
        try {
            // Record exceptions so that if we fail to obtain any
            // result, we can throw the last exception we got.
            ExecutionException exception = null;
            long lastTime = timed ? System.nanoTime() : 0;
            Iterator<? extends Callable<T>> iterator = tasks.iterator();

            // Start one task for sure; the rest incrementally
            futures.add(service.submit(iterator.next()));
            taskCount--;
            int active = 1;

            for (; ; ) {
                Future<T> future = service.poll();

                if (future == null) {
                    if (taskCount > 0) {
                        taskCount--;
                        futures.add(service.submit(iterator.next()));
                        active++;
                    } else if (active == 0) {
                        break;
                    } else if (timed) {
                        future = service.poll(nanos, TimeUnit.NANOSECONDS);

                        if (future == null) {
                            throw new TimeoutException();
                        }

                        long now = System.nanoTime();
                        nanos -= now - lastTime;
                        lastTime = now;
                    } else {
                        future = service.take();
                    }
                }

                if (future != null) {
                    active--;

                    try {
                        return future.get();
                    } catch (ExecutionException e) {
                        exception = e;
                    } catch (RuntimeException e) {
                        exception = new ExecutionException(e);
                    }
                }
            }

            if (exception == null) {
                exception = new ExecutionException(null);
            }

            throw exception;
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }
    }

    @Nonnull
    @Override
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        try {
            return doInvokeAny(tasks, false, 0);
        } catch (TimeoutException ignored) {
            assert false;
            return null;
        }
    }

    @Override
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks, long timeout, @Nonnull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return doInvokeAny(tasks, true, unit.toNanos(timeout));
    }

    @Nonnull
    @Override
    public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        checkNotNull(tasks);

        List<Future<T>> futures = Lists.newArrayListWithCapacity(tasks.size());
        boolean done = false;

        try {
            for (Callable<T> t : tasks) {
                RunnableAbstractFuture<T> future = newTaskFor(t);
                futures.add(future);

                execute(future);
            }

            for (Future<T> future : futures) {
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (CancellationException | ExecutionException ignored) {
                    }
                }
            }

            done = true;
            return futures;
        } finally {
            if (!done) {
                for (Future<T> future : futures) {
                    future.cancel(true);
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks, long timeout, @Nonnull TimeUnit unit) throws InterruptedException {
        checkNotNull(tasks);
        checkNotNull(unit);

        long nanos = unit.toNanos(timeout);
        List<Future<T>> futures = Lists.newArrayListWithCapacity(tasks.size());
        boolean done = false;

        try {
            for (Callable<T> t : tasks) {
                futures.add(newTaskFor(t));
            }

            long lastTime = System.nanoTime();

            for (Future<T> future : futures) {
                execute((Runnable) future);

                long now = System.nanoTime();
                nanos -= now - lastTime;
                lastTime = now;

                if (nanos <= 0) {
                    return futures;
                }
            }

            for (Future<T> future : futures) {
                if (!future.isDone()) {
                    if (nanos <= 0) {
                        return futures;
                    }

                    try {
                        future.get(nanos, TimeUnit.NANOSECONDS);
                    } catch (CancellationException | ExecutionException ignored) {
                    } catch (TimeoutException exception) {
                        return futures;
                    }

                    long now = System.nanoTime();
                    nanos -= now - lastTime;
                    lastTime = now;
                }
            }

            done = true;
            return futures;
        } finally {
            if (!done) {
                for (Future<T> future : futures) {
                    future.cancel(true);
                }
            }
        }
    }
}
