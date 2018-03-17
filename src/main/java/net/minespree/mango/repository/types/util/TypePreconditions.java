package net.minespree.mango.repository.types.util;

import com.google.common.base.Preconditions;
import net.minespree.mango.repository.types.Type;

/**
 * @since 20/10/2017
 */
public class TypePreconditions {
    public static void checkInstance(Type type, Object obj) throws IllegalArgumentException {
        Preconditions.checkNotNull(obj);
        Preconditions.checkArgument(type.isInstance(obj), "object is not an instance of " + type.getName());
    }
}
