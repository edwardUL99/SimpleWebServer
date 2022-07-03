package io.github.edwardUL99.simple.web.exceptions;

/**
 * An exception that can be thrown by anywhere in the server to force the server to shutdown
 */
public class FatalTerminationException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public FatalTerminationException(String message) {
        super(message);
    }
}
