package net.minespree.mango.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * Connects the Mango scheduler with an external one such
 * as the Bukkit, Bungee or PlayPen schedulers.
 *
 * @since 14/02/2018
 */
public interface TaskManager<E> {
    E getTask(Runnable command);

    /**
     * @param delay in {@link TimeUnit#MILLISECONDS}
     */
    E getLaterTask(Runnable task, long delay);

    /**
     * @param initialDelay in {@link TimeUnit#MILLISECONDS}
     * @param interval in {@link TimeUnit#MILLISECONDS}
     */
    E getTimerTask(Runnable task, long initialDelay, long interval);

    void cancel(E task);

    boolean isCurrentlyRunning(E task);

    boolean isQueued(E task);
}
