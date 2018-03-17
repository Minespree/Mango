package net.minespree.mango.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @since 09/02/2018
 */
@Getter
@RequiredArgsConstructor
public enum GamePhase {
    /**
     * The server is starting up, no players
     * can be accepted at this time.
     */
    STARTING(false, false),
    /**
     * The server is selecting and initializing a map.
     * No players can be accepted at this time.
     */
    SETUP(false, false),
    /**
     * The server is awaiting new players.
     * Lobbies also use this state.
     */
    WAITING(true, false),
    /**
     * The game on this server is now in progress
     */
    PLAYING(false),
    /**
     * The game on this server has finished, we will rotate
     * to the setup phase once all players have been kicked.
     */
    ENDING(false);

    GamePhase(boolean joinable) {
        this(joinable, true);
    }

    private final boolean joinable;
    private final boolean active;
}
