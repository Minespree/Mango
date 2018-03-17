package net.minespree.mango.util;

import java.io.IOException;

/**
 * Service that needs to be connected and disconnected
 * along with Mango. If a new {@link Connectable} is provisioned
 * after the connection phase is complete, an {@link IllegalStateException} is thrown.
 * @since 10/02/2018
 */
public interface Connectable {
    default void connect() throws IOException {}

    default void disconnect() throws IOException {}

    boolean isConnected();
}
