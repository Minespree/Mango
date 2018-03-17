package net.minespree.mango.players;

import net.minespree.mango.players.Identity;
import org.mongodb.morphia.annotations.Entity;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

/**
 * @since 10/02/2018
 */
@Entity(value = "players", noClassnameStored = true)
public class Session extends Identity {
    protected String packageId;
    protected String serverId;

    @Nullable
    protected String nickname;

    protected String ip;

    protected Instant start;
    @Nullable
    protected Instant end;

    public Session(UUID uuid, String name, Rank rank) {
        super(uuid, name, rank);
    }
}
