package net.minespree.mango.repository.types.util;

/**
 * @since 20/10/2017
 */
public interface Toggleable {
    Object getNextState(Object previous) throws IllegalArgumentException;
}
