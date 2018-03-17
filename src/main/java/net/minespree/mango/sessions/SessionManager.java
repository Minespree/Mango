package net.minespree.mango.sessions;

import com.google.common.util.concurrent.ListenableFuture;
import net.minespree.mango.players.Session;

import java.util.Set;
import java.util.UUID;

/**
 * @since 10/02/2018
 */
public interface SessionManager {
    ListenableFuture<Session> start(SessionStartRequest request);

    ListenableFuture<?> finish(Session session);

    ListenableFuture<Session> online(UUID playerUuid);

    ListenableFuture<Set<Session>> friends(UUID playerUuid);

    ListenableFuture<Set<Session>> staff(boolean disguised);
}
