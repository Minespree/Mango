package net.minespree.mango.server;

import net.minespree.mango.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @since 14/02/2018
 * @param <T> External task class e.g. Bukkit -> BukkitTask
 */
public interface Instance<T> {
    @Nonnull String getServerId();

    @Nonnull ServerType getServerType();

    @Nonnull GameType getGameType();

    @Nonnull
    default String getServerGroup() {
        return getGameType().getPackageId();
    }

    @Nonnull LobbyStatus getCurrentStatus();

    Scheduler<T> getSyncScheduler();

    Scheduler<T> getAsyncScheduler();
}
