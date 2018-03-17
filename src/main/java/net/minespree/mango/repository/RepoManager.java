package net.minespree.mango.repository;

import java.util.Map;

/**
 * @since 03/11/2017
 */
public interface RepoManager<T extends Element> {
    boolean hasValue(T element);

    Object getRawValue(T element);

    Object getValue(T element);

    Object getValue(T element, Object defaultValue) throws IllegalArgumentException;

    Map<T, Object> getEntries();

    <V> V getRawValue(T element, Class<V> typeClass) throws IllegalArgumentException;

    <V> V getValue(T element, Class<V> typeClass) throws IllegalArgumentException;

    <V> V getValue(T element, Class<V> typeClass, V defaultValue) throws IllegalArgumentException;

    boolean setValue(T element, Object value);

    boolean setValue(T element, Object value, boolean notifyGlobal);

    void deleteValue(T element);

    /**
     * Get the {@link RepoCallbackManager} that will be used to send change notifications for this {@link RepoManager}
     */
    RepoCallbackManager<T> getCallbackManager();
}
