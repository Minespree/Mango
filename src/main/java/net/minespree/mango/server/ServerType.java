package net.minespree.mango.server;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minespree.mango.players.Identity;

/**
 * @since 09/02/2018
 */
@Getter
@AllArgsConstructor
public enum ServerType {
    HUB("Hub", false),
    GAME("Game", true),
    UNKNOWN("Unknown", false, false);

    public static final String HUB_PREFIX = "hub";

    private final String displayName;
    private final boolean playing;
    private final boolean friendJoinable;

    ServerType(String displayName, boolean playing) {
        this(displayName, playing, true);
    }

    public static ServerType getType(String serverId) {
        Preconditions.checkNotNull(serverId);

        GameType type = GameType.getByServerId(serverId);

        if (type != null) {
            return GAME;
        }

        return serverId.startsWith(HUB_PREFIX) ? HUB : UNKNOWN;
    }

    public static boolean canJoin(Identity identity, String serverId) {
        Preconditions.checkNotNull(identity);

        ServerType type = getType(serverId);

        switch (type) {
            case HUB:
                return true;
            case UNKNOWN:
                return false;
            case GAME:
                GameType gameType = GameType.getByServerId(serverId);

                if (gameType == null) {
                    return false;
                }

                return gameType.canJoin(identity);
        }

        return false;
    }
}
