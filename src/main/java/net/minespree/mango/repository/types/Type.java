package net.minespree.mango.repository.types;

/**
 * @since 03/11/2017
 */
public interface Type {
    String getName();

    boolean isInstance(Object obj);

    Object getDefault();

    // TODO Change by Mongo object
    Object parse(String raw) throws TypeParseException;

    String toString(Object obj) throws IllegalArgumentException;
}
