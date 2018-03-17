package net.minespree.mango.repository.base;

import net.minespree.mango.repository.Element;
import net.minespree.mango.repository.RepoCallback;
import net.minespree.mango.repository.RepoCallbackManager;

import java.util.List;

/**
 * @since 03/11/2017
 */
public abstract class AbstractRepoCallbackManager<T extends Element> implements RepoCallbackManager<T> {
    @Override
    public List<RepoCallback<T>> getCallbacks(T element) {
        return getCallbacks(element, false);
    }

    @Override
    public int getNumCallbacks(T element) {
        return getNumCallbacks(element, false);
    }

    @Override
    public boolean hasCallbacks(T element) {
        return hasCallbacks(element, false);
    }
}
