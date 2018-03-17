package net.minespree.mango.repository;

import net.minespree.mango.repository.types.Type;

/**
 * @since 03/11/2017
 */
public interface Element {
    String getId();

    Type getType();

    Object getDefaultValue();

    boolean hasDefaultValue();
}
