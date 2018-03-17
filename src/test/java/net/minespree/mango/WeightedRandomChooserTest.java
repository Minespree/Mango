package net.minespree.mango;

import com.google.common.collect.ImmutableMap;
import net.minespree.mango.random.ImmutableWeightedRandomChooser;
import net.minespree.mango.random.MutableWeightedRandomChooser;
import net.minespree.mango.random.WeightedRandomChooser;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static net.minespree.mango.test.Assert.*;

/**
 * @since 09/02/2018
 */
public class WeightedRandomChooserTest {
    static final double delta = 0.00001; // Used for floating-point comparisons
    static final long SEED = 2905696505089501646L;
    Random random;

    @Before
    public void setUp() {
        random = new Random(SEED);
    }

    /**
     * Run 10,000 trials and assert the given probability within 1/100th
     *
     * This has a small chance of failing, but since we use a fixed random seed
     * it will do the same thing every run.
     */
    <T, N extends Number> void assertProbability(WeightedRandomChooser<T, N> chooser, T choice, double probability) {
        int n = 0;

        for (int i = 0; i < 10000; i++) {
            if (choice.equals(chooser.choose(random))) {
                n++;
            }
        }

        assertEquals(probability, n / 10000D, 0.01);
    }

    void assertTotalWeight(double totalWeight, WeightedRandomChooser<?, ?> chooser) {
        assertEquals(totalWeight, chooser.totalWeight(), delta);
    }

    @Test
    public void empty() {
        ImmutableWeightedRandomChooser<?, ?> chooser = new ImmutableWeightedRandomChooser<>(ImmutableMap.of());

        assertTrue(chooser.isEmpty());
        assertTotalWeight(0, chooser);
        assertThrows(NoSuchElementException.class, () -> chooser.choose(random));
    }

    @Test
    public void immutable() {
        ImmutableWeightedRandomChooser<String, Integer> chooser = new ImmutableWeightedRandomChooser<>(ImmutableMap.of(
            "One", 1,
            "Two", 2,
            "Three", 3
        ));

        assertFalse(chooser.isEmpty());
        assertTotalWeight(6, chooser);
        assertProbability(chooser, "One", 1 / 6D);
        assertProbability(chooser, "Two", 2 / 6D);
        assertProbability(chooser, "Three", 3 / 6D);
    }

    @Test
    public void mutable() {
        MutableWeightedRandomChooser<String, Integer> chooser = new MutableWeightedRandomChooser<>();

        chooser.add("One", 1);
        assertTotalWeight(1, chooser);
        assertProbability(chooser, "One", 1);

        chooser.add("Two", 2);
        assertTotalWeight(3, chooser);
        assertProbability(chooser, "One", 1 / 3D);
        assertProbability(chooser, "Two", 2 / 3D);

        chooser.add("Three", 3);
        assertTotalWeight(6, chooser);
        assertProbability(chooser, "One", 1 / 6D);
        assertProbability(chooser, "Two", 2 / 6D);
        assertProbability(chooser, "Three", 3 / 6D);

        chooser.remove("One");
        assertTotalWeight(5, chooser);
        assertProbability(chooser, "Two", 2 / 5D);
        assertProbability(chooser, "Three", 3 / 5D);

        chooser.remove("Two");
        chooser.remove("Three");

        assertTotalWeight(0, chooser);
        assertThrows(NoSuchElementException.class, () -> chooser.choose(random));
    }
}
