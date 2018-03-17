package net.minespree.mango.repository;

import java.util.Collection;

/**
 * @since 03/11/2017
 */
public interface RepoRegistry<T extends Element> {
    T get(String id);

    T find(String id) throws IllegalArgumentException;

    Collection<T> getElements();

    boolean isRegistered(T element);

    void register(T element);

    void registerAll(Collection<T> elements);

    boolean unregister(T element);
}
