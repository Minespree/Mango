package net.minespree.mango.random;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @since 09/02/2018
 */
public class MutableWeightedRandomChooser<T, N extends Number> extends WeightedRandomChooser<T, N> {
    private final Map<T, Option> options = Maps.newHashMap();
    private double totalWeight = 0;

    public MutableWeightedRandomChooser() {}

    public MutableWeightedRandomChooser(Map<T, N> weights) {
        addAll(weights);
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
        n *= totalWeight;
        Option result = null;

        for (Option option : options.values()) {
            if (n < option.weight) {
                result = option;
                break;
            } else {
                n -= option.weight;
            }
        }

        // NPE should be impossible
        return result.element;
    }

    public void add(T choice, N weight) {
        final double doubleWeight = weight.doubleValue();

        if (doubleWeight > 0) {
            totalWeight += doubleWeight;
            options.put(choice, new Option(choice, doubleWeight));
        }
    }

    public void remove(T choice) {
        final Option c = options.remove(choice);

        if (c != null) {
            totalWeight -= c.weight;
        }
    }

    public void addAll(Map<T, N> weightedChoices) {
        weightedChoices.forEach(this::add);
    }

    public void removeAll(Iterable<T> choices) {
        choices.forEach(this::remove);
    }
}
