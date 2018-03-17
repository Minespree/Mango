package net.minespree.mango.util;

/**
 * A service that receives lifecycle callbacks.
 * @since 14/02/2018
 */
public interface Enableable {
    /**
     * Called when this service starts listening
     */
    default void enable() {}

    /**
     * Called when this service stops listening
     */
    default void disable() {}
}
