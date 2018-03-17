package net.minespree.mango.validator;

import com.google.common.base.Preconditions;
import net.minespree.mango.util.UserUtils;

import java.util.UUID;

/**
 * @since 09/02/2018
 */
public final class Validators {
    private static final int PORT_LIMIT = 65535;

    private Validators() {}

    public static int validatePort(int port) {
        Preconditions.checkArgument(port > 1 && port < PORT_LIMIT, "Port exceeds maximum range (1, " + PORT_LIMIT + ")");
        return port;
    }

    public static String validateUsername(String username) {
        Preconditions.checkArgument(!username.isEmpty() && username.length() <= UserUtils.MAX_USERNAME_LENGTH, "The username is too long (max " + UserUtils.MAX_USERNAME_LENGTH + ")");
        Preconditions.checkArgument(UserUtils.USERNAME_REGEX.matcher(username).matches(), "The username contains invalid characters");

        return username;
    }

    /**
     * @param raw Stringified UUID with dashes
     */
    public static UUID validateUuid(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format (must be 8-4-4-4-12)", e);
        }
    }
}
