package net.minespree.mango.collections;

import java.util.function.ObjIntConsumer;

/**
 * @since 09/02/2018
 */
public class IterableUtils {
    public static <E> void forEachIndexed(Iterable<E> iterable, ObjIntConsumer<E> consumer) {
        int i = 0;

        for (E e : iterable) {
            consumer.accept(e, i++);
        }
    }
}
