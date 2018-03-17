package net.minespree.mango.util;

import com.google.common.base.Preconditions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * @since 09/02/2018
 */
public final class ArrayUtils {
    private ArrayUtils() {}

    public static final int NOT_FOUND_INDEX = -1;

    public static <T> T fromEnd(T[] array, int index) {
        return array[array.length - 1 - index];
    }

    public static <T> int indexOf(T[] array, T value) {
        if (array == null) {
            return NOT_FOUND_INDEX;
        }

        if (value == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return NOT_FOUND_INDEX;
    }

    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != NOT_FOUND_INDEX;
    }

    /**
     * Creates a new array of given size, with the same component type as the given array
     */
    public static <T> T[] sameType(T[] array, int size) {
        return (T[]) Array.newInstance(array.getClass().getComponentType(), size);
    }

    public static <T> T[] copyOfRange(T[] source, int from, int to) {
        final T[] dest = sameType(source, to - from);
        System.arraycopy(source, Math.max(0, from),
                dest, Math.max(0, -from),
                Math.min(to, source.length));

        return dest;
    }

    public static <T> T[] append(T[] a, T... b) {
        if (b.length == 0) {
            return a;
        }

        if (a.length == 0) {
            return b;
        }

        T[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }

    public static <T> T getRandomElement(T[] array, Random random) {
        Preconditions.checkArgument(array.length > 0, "Array cannot be empty");

        return array[random.nextInt(array.length)];
    }
}
