package net.minespree.mango.connectable;

import com.google.common.base.Preconditions;
import net.minespree.mango.util.Connectable;

import java.io.IOException;

/**
 * @since 14/02/2018
 */
public class SimpleConnectable implements Connectable {
    protected boolean connected = false;

    @Override
    public void connect() throws IOException {
        Preconditions.checkState(isConnected(), "Connectable instance already connected");

        connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}
