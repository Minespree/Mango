package net.minespree.mango.server;

import lombok.Value;
import net.minespree.mango.serialization.JsonUtils;

/**
 * @since 09/02/2018
 */
@Value
public class LobbyStatus {
    private GamePhase phase;
    private String mapName;
    private int onlinePlayers;
    private int maxPlayers;
    private long startTime;

    public boolean isEmpty() {
        return onlinePlayers <= 0;
    }

    public boolean isDnr(long now, long timeout) {
        if (isDnrDisabled(timeout)) {
            return false;
        }

        return now > startTime + timeout;
    }

    public boolean isDnr(long timeout) {
        return isDnr(System.currentTimeMillis(), timeout);
    }

    public static boolean isDnrDisabled(long timeout) {
        return timeout <= -1;
    }

    public static LobbyStatus fromJson(String data) {
        return JsonUtils.gson().fromJson(data, LobbyStatus.class);
    }
}
