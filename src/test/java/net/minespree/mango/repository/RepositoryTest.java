package net.minespree.mango.repository;

import net.minespree.mango.repository.base.SimpleRepoCallbackManager;
import net.minespree.mango.repository.base.SimpleRepoRegistry;
import net.minespree.mango.repository.types.EnumType;
import net.minespree.mango.repository.types.IntegerType;
import net.minespree.mango.repository.types.StringType;
import net.minespree.mango.repository.types.Type;
import org.junit.Before;
import org.junit.Test;

import static net.minespree.mango.test.Assert.assertThrows;
import static org.junit.Assert.*;

/**
 * @since 09/02/2018
 */
public class RepositoryTest {
    RepoManager<MockElement> manager;
    RepoCallbackManager<MockElement> callbackManager;
    RepoRegistry<MockElement> registry;

    MockElement element;

    @Before
    public void setUp() {
        registry = new SimpleRepoRegistry<>();
        callbackManager = new SimpleRepoCallbackManager<>();
        manager = new MockRepoManager(callbackManager);

        element = new MockElement("id", new StringType(), "default");
    }

    void addElement(Object value, boolean notifyGlobal) {
        manager.setValue(element, value, notifyGlobal);
    }

    void addElement(Object value) {
        addElement(value, false);
    }

    @Test
    public void types() {
        Type type = new StringType();

        assertTrue(type.isInstance(""));
        assertFalse(type.isInstance(null));

        type = new IntegerType();

        assertThrows(IllegalArgumentException.class, () -> new IntegerType().toString("blah"));
        assertTrue(type.isInstance(145));
        assertEquals("124", type.toString(124));

        type = new EnumType<MockEnumType>("Options", MockEnumType.class) {
            @Override
            public Object getDefault() {
                return MockEnumType.NONE;
            }
        };

        type.isInstance(MockEnumType.ALL);
        assertEquals(MockEnumType.NONE, type.getDefault());

        // Adding illegal type (element is StringType)
        assertThrows(IllegalArgumentException.class, () -> {
            addElement(new Object());
        });
    }

    @Test
    public void manager() {
        manager.setValue(element, "Value");
        assertEquals("Value", manager.getValue(element));

        manager.setValue(element, null);
        assertEquals("default", manager.getValue(element));

        manager.deleteValue(element);
        assertFalse(manager.hasValue(element));

        Object value = manager.getValue(element, String.class, "newDefault");
        assertEquals("newDefault", value);
    }

    @Test
    public void callbacks() {
        // TODO Setup Mockito
    }
}
