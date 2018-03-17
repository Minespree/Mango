package net.minespree.mango.util;

import com.google.common.base.Preconditions;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Appends or increments a numeric suffix to a string.
 *
 * {@code
 *  StringIncrementer incrementer = new StringIncrementer("-", 1);
 *  si.apply("hub")     // returns "hub-1"
 *  si.apply("hub-1")   // returns "hub-2"
 *  si.apply("hub-27")   // returns "hub-28"
 * }
 * @since 09/02/2018
 */
public class StringIncrementer implements UnaryOperator<String> {
    private final String delimiter;
    private final int initial;
    private final Pattern pattern;

    public StringIncrementer(String delimiter) {
        this(delimiter, 0);
    }

    public StringIncrementer(String delimiter, int initial) {
        this.delimiter = delimiter;
        this.initial = initial;
        this.pattern = Pattern.compile("^(.*)" + Pattern.quote(delimiter) + "(\\d+)$");
    }

    @Override
    public String apply(String s) {
        Preconditions.checkNotNull(s);
        final Matcher matcher = pattern.matcher(s);

        if (matcher.matches()) {
            return matcher.group(1) + delimiter + (Integer.parseInt(matcher.group(2)) + 1);
        } else {
            return s + delimiter + initial;
        }
    }
}
