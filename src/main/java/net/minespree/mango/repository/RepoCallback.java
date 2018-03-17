package net.minespree.mango.repository;

/**
 * @since 03/11/2017
 */
public interface RepoCallback<T extends Element> {
    void notifyChange(RepoManager manager, T element, Object oldValue, Object newValue);
}
