package net.minespree.mango.players;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minespree.mango.players.Rank;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @since 10/02/2018
 */
@Getter
@RequiredArgsConstructor
public class Identity {
    @Id
    protected final UUID uuid;

    @Indexed
    protected final String name;

    @Nonnull
    protected Rank rank;
}
