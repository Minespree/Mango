package net.minespree.mango.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @since 09/02/2018
 */
public final class Utils {
    private Utils() {
    }

    public static boolean notEqual(Object a, Object b) {
        return !Objects.equals(a, b);
    }

    public static <T> boolean equals(Class<T> type, T self, Object that, Predicate<T> test) {
        return self == that || (type.isInstance(that) && test.test(type.cast(that)));
    }

    public static <T> void ifInstance(Object generic, Class<T> specific, Consumer<T> action) {
        if (specific.isInstance(generic)) {
            action.accept((T) specific);
        }
    }
}
