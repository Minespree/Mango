package net.minespree.mango.repository;

import lombok.Value;
import net.minespree.mango.repository.types.Type;

/**
 * @since 09/02/2018
 */
@Value
public class MockElement implements Element {
    private String id;
    private Type type;
    private Object defaultValue;

    @Override
    public boolean hasDefaultValue() {
        return defaultValue != null;
    }
}
