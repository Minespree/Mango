package net.minespree.mango.repository.types;

/**
 * @since 18/10/2017
 */
public class TypeParseException extends Exception {
    public TypeParseException() {
    }

    public TypeParseException(String message) {
        super(message);
    }

    public TypeParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeParseException(Throwable cause) {
        super(cause);
    }
}
