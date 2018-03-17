package net.minespree.mango;

import net.minespree.mango.util.StringIncrementer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static net.minespree.mango.test.Assert.*;

/**
 * @since 09/02/2018
 */
public class StringIncrementerTest {
    StringIncrementer incrementer;

    @Before
    public void setUp() {
        incrementer = new StringIncrementer("-");
    }

    @Test
    public void increments() {
        assertEquals(incrementer.apply("hub"), "hub-0");
        assertEquals(incrementer.apply("hub-1"), "hub-2");
        assertThrows(NullPointerException.class, () -> {
            incrementer.apply(null);
        });

        incrementer = new StringIncrementer("x", 4);

        assertEquals(incrementer.apply("test"), "testx4");
        assertEquals(incrementer.apply("testx70"), "testx71");
    }
}
