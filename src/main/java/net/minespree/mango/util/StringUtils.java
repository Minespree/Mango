package net.minespree.mango.util;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @since 09/02/2018
 */
public class StringUtils {

    public static String listToCompound(Collection<String> list, String and) {
        return listToCompound(list, and, "", "");
    }

    /**
     * Converts a list of strings to a nice list as a string.
     *
     * For example: ["Hugo", "Erik", "Doshy"] -> "Hugo, Erik {and} Doshy"
     * @param list List of strings to concatenate
     * @param and String to be used for "and"
     * @param prefix Prefix to add before each element in the resulting string.
     * @param suffix Suffix to add after each element in the resulting string.
     * @return String version of the list of strings.
     */
    public static String listToCompound(Collection<?> list, String and, String prefix, String suffix) {
        StringBuilder builder = new StringBuilder();
        int i = 0;

        for (Object str : list) {
            if (i != 0) {
                if (i == list.size() - 1) {
                    builder.append(" ").append(and).append(" ");
                } else {
                    builder.append(", ");
                }
            }

            builder.append(prefix).append(str).append(suffix);
            i++;
        }

        return builder.toString();
    }

    // TODO Add Fuzzy match methods

    public static String sanitize(String string, String spaceReplace) {
        return string.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", spaceReplace);
    }

    public static String truncate(String text, int length) {
        return text.substring(0, Math.min(text.length(), length));
    }

    public static String removeEnd(String whole, String end) {
        return whole.endsWith(end) ? truncate(whole, whole.length() - end.length())
                                    : whole;
    }

    public static String substring(String text, int begin, int end) {
        return text.substring(Math.min(text.length(), begin), Math.min(text.length(), end));
    }

    public static Optional<String> nonEmpty(String string) {
        return string != null && !string.isEmpty() ? Optional.of(string) : Optional.empty();
    }

    public static String emptyIfNull(String string) {
        return string != null ? string : "";
    }

    public static boolean isBlank(String string) {
        if (string == null) {
            return true;
        }

        for (int i = 0; i < string.length(); i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static String replaceAll(Pattern pattern, String string, String replacement) {
        return pattern.matcher(string).replaceAll(replacement);
    }
}
