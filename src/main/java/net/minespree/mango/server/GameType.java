package net.minespree.mango.server;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minespree.mango.players.Identity;
import net.minespree.mango.players.Rank;

/**
 * @since 09/02/2018
 */
@Getter
public enum GameType {
    BLOCKWARS("blockwars", "BLOCKWARS", "BlockWars", "bw-"),
    SKYWARS("skywars", "SKYWARS", "SkyWars", "sw-"),
    THIMBLE("thimble", "THIMBLE", "Thimble", "th-"),
    CLASH("clash", "CLASH", "Clash", "cl-"),
    DOUBLE_TROUBLE("doubletrouble", "DOUBLE_TROUBLE", "Double Trouble", "dt-"),
    BATTLE_ROYALE("battleroyale", "BATTLE_ROYALE", "Battle Royale", "br-");

    /**
     * PlayPen's package name
     */
    private final String packageId;
    private final String databaseId;
    private final String displayName;
    private final String serverPrefix;

    private boolean maintenance;
    private boolean released;

    private Rank requiredRank;

    GameType(String packageId, String databaseId, String displayName, String serverPrefix, boolean maintenance, boolean released, Rank requiredRank) {
        this.packageId = packageId;
        this.databaseId = databaseId;
        this.displayName = displayName;
        this.serverPrefix = serverPrefix;
        this.maintenance = maintenance;
        this.released = released;
        this.requiredRank = requiredRank;
    }

    GameType(String packageId, String databaseId, String displayName, String serverPrefix, Rank requiredRank) {
        this(packageId, databaseId, displayName, serverPrefix, false, true, requiredRank);
    }

    GameType(String packageId, String databaseId, String displayName, String serverPrefix) {
        this(packageId, databaseId, displayName, serverPrefix, Rank.MEMBER);
    }

    public static GameType getByServerId(String serverId) {
        Preconditions.checkNotNull(serverId);

        for (GameType type : values()) {
            if (serverId.startsWith(type.getServerPrefix())) {
                return type;
            }
        }

        return null;
    }

    public static GameType getByDatabase(String databaseId) {
        Preconditions.checkNotNull(databaseId);

        for (GameType type : values()) {
            if (databaseId.equals(type.getDatabaseId())) {
                return type;
            }
        }

        return null;
    }

    public boolean canJoin(Identity identity) {
        if (identity == null || maintenance || !released) {
            return false;
        }

        return requiredRank.has(identity.getRank());
    }
}
