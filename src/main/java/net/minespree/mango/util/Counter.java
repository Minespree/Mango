package net.minespree.mango.util;

import java.util.function.IntSupplier;

/**
 * @since 09/02/2018
 */
public class Counter implements IntSupplier {
    private int count;

    public Counter(int start) {
        count = start;
    }

    public Counter() {
        this(0);
    }

    public int next() {
        return count++;
    }

    @Override
    public int getAsInt() {
        return next();
    }
}
