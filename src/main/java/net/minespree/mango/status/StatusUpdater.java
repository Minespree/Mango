package net.minespree.mango.status;

import net.minespree.mango.server.Instance;

/**
 * @since 15/02/2018
 */
public interface StatusUpdater {
    void update(Instance instance);

    void onShutdown(Instance instance);
}
