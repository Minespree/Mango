package net.minespree.mango.repository.types.util;

import com.google.common.base.Preconditions;

/**
 * @since 18/10/2017
 */
public class TypeUtil {
    private TypeUtil() {
    }

    public static <T> T getValue(Object value, Class<T> typeClass) throws IllegalArgumentException {
        Preconditions.checkNotNull(typeClass);

        if (value != null) {
            Preconditions.checkArgument(typeClass.isAssignableFrom(value.getClass()), "value can't be casted to %s", typeClass.getName());
            return (T) value;
        }

        return null;
    }
}
