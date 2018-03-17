package net.minespree.mango.util;

import java.util.regex.Pattern;

/**
 * @since 09/02/2018
 */
public class UserUtils {
    private static final String ALLOWED_CHARS_REGEX = "[^A-Za-z0-9_]";
    public static final Pattern USERNAME_REGEX = Pattern.compile(ALLOWED_CHARS_REGEX + "{1,16}");
    public static final int MAX_USERNAME_LENGTH = 16;

    public static String sanitizeUsername(String username) {
        return StringUtils.truncate(
            username.replaceAll(ALLOWED_CHARS_REGEX, ""),
            MAX_USERNAME_LENGTH
        );
    }
}
