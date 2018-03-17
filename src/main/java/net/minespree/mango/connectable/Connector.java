package net.minespree.mango.connectable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.minespree.mango.util.Connectable;
import net.minespree.mango.util.Enableable;

import java.util.Collections;
import java.util.Deque;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @since 14/02/2018
 */
@RequiredArgsConstructor
public class Connector implements Enableable {
    protected final Logger logger;
    private final Set<Connectable> registered = Collections.newSetFromMap(Maps.newIdentityHashMap());
    private final Deque<Connectable> pending = Lists.newLinkedList();
    private final Deque<Connectable> connected = Lists.newLinkedList();

    private boolean finishedConnecting;

    public void register(Connectable connectable) {
        if (registered.add(connectable)) {
            if (finishedConnecting) {
                throw new IllegalStateException("Tried to provision a " + Connectable.class.getSimpleName() + " when already connected");
            }

            pending.add(connectable);
        }
    }

    @Override
    public void enable() {
        Preconditions.checkState(!finishedConnecting, "Already connected");
        logger.fine("Connecting all services");

        for (;;) {
            final Connectable connectable = pending.poll();

            if (connectable == null) {
                break;
            }

            logger.fine("Connecting " + connectable.getClass().getName());
            connected.push(connectable);
        }

        finishedConnecting = true;
    }

    @Override
    public void disable() {
        Preconditions.checkState(finishedConnecting, "Not connected");
        logger.fine("Disconnecting all services");

        while (!connected.isEmpty()) {
            final Connectable connectable = connected.pop();

            logger.fine("Disconnecting " + connectable.getClass().getName());
        }
    }
}
