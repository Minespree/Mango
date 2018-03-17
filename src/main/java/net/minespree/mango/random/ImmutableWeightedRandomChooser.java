package net.minespree.mango.random;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import net.minespree.mango.util.Collectors;

import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @since 09/02/2018
 */
public class ImmutableWeightedRandomChooser<T, N extends Number> extends WeightedRandomChooser<T, N> {
    private final NavigableMap<Double, Option> options;
    private final double totalWeight;

    public ImmutableWeightedRandomChooser(Stream<T> elements, Function<T, N> scale) {
        this(elements.collect(Collectors.mappingTo(scale)));
    }

    public ImmutableWeightedRandomChooser(Map<T, N> weights) {
        final ImmutableSortedMap.Builder<Double, Option> builder = new ImmutableSortedMap.Builder<>(Ordering.natural());
        double total = 0;

        for (Map.Entry<T, N> entry : weights.entrySet()) {
            double weight = entry.getValue().doubleValue();

            if (weight > 0) {
                total += weight;
                builder.put(total, new Option(entry.getKey(), weight));
            }
        }

        this.options = builder.build();
        this.totalWeight = total;
    }

    @Override
    public double totalWeight() {
        return totalWeight;
    }

    @Override
    public boolean isEmpty() {
        return options.isEmpty();
    }

    @Override
    protected T chooseInternal(double n) {
        final double key = n * totalWeight;

        // TODO Does random < 1 always imply key < totalWeight?
        // Rounding issues may occur
        return (key < totalWeight ? options.higherEntry(key) :
                                    options.lastEntry())
                .getValue().element;
    }
}
