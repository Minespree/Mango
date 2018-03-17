package net.minespree.mango;

import net.minespree.mango.collections.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ListUtils}
 * @since 09/02/2018
 */
public class ListUtilsTest {
    @Test
    public void simpleListUnionTest() {
        List<String> union = ListUtils.union(Arrays.asList("hello", "world"), Arrays.asList("cat"));

        assertTrue("[\"hello\", \"world\"] + [\"cat\"] did not contain all elements",
            union.containsAll(Arrays.asList("hello", "world", "cat"))
        );
    }
}
