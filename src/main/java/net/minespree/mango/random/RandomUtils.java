package net.minespree.mango.random;

import com.google.common.collect.Iterables;

import java.util.Random;

/**
 * @since 09/02/2018
 */
public class RandomUtils {
    public static int safeNextInt(Random random, int i) {
        return i <= 0 ? 0 : random.nextInt(i);
    }

    public static <T> T element(Random random, Iterable<? extends T> iterable) {
        return Iterables.get(iterable, safeNextInt(random, Iterables.size(iterable)));
    }
}
