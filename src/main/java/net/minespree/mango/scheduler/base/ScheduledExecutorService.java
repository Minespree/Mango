package net.minespree.mango.scheduler.base;

import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents a listening scheduler service that returns {@link ListenableScheduledFuture}
 * instead of {@link ScheduledFuture}
 *
 * @since 14/02/2018
 */
public interface ScheduledExecutorService<E> extends java.util.concurrent.ScheduledExecutorService, ListeningExecutorService {
    @Nonnull
    @Override
    ListenableScheduledFuture<?> schedule(@Nonnull Runnable task, long delay, @Nonnull TimeUnit unit);

    @Nonnull
    @Override
    <V> ListenableScheduledFuture<V> schedule(@Nonnull Callable<V> task, long delay, @Nonnull TimeUnit unit);

    @Nonnull
    @Override
    ListenableScheduledFuture<?> scheduleAtFixedRate(@Nonnull Runnable task, long initialDelay, long period, @Nonnull TimeUnit unit);

    /**
     * @deprecated This is not supported by the underlying Bukkit, Bungee and PlayPen schedulers.
     */
    @Nonnull
    @Override
    @Deprecated
    ListenableScheduledFuture<?> scheduleWithFixedDelay(@Nonnull Runnable runnable, long initialDelay, long delay, @Nonnull TimeUnit unit);

}
