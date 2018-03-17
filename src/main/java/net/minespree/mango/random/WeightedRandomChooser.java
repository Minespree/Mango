package net.minespree.mango.random;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Holds a set of weighted items which can be looked up
 * with a number in the range [0, 1)
 * @param <T> Item type
 * @param <N> Numeric weight type
 * @since 09/02/2018
 */
public abstract class WeightedRandomChooser<T, N extends Number> {

    @AllArgsConstructor
    class Option {
        protected final T element;
        protected final double weight;
    }

    public abstract double totalWeight();

    public abstract boolean isEmpty();

    protected abstract T chooseInternal(double n);

    /**
     * Choose an item in a consistent way using the given number.
     * The probability of each item being chosen is proportional
     * to its weight. Any particular number will always choose the
     * same numbers.
     * @param n Number in the range [0, 1)
     * @return An item passed to the constructor, or {@code null} if there are no items
     * @throws NoSuchElementException if this chooser is empty
     */
    public T choose(double n) {
        Preconditions.checkArgument(n >= 0);
        Preconditions.checkArgument(n < 1);

        if (isEmpty()) {
            throw new NoSuchElementException("No choices were added");
        }

        return chooseInternal(n);
    }

    /**
     * Choose an item at random using the given generator.
     * The probability of each item being chosen is proportional
     * to its weight. The choice will be constistent with regard to
     * the state of the generator.
     * @return An item passed to the constructor
     * @throws NoSuchElementException if this chooser is empty
     */
    public T choose(Random random) {
        return choose(random.nextDouble());
    }
}
