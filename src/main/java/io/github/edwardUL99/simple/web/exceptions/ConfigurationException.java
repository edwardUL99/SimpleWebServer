package io.github.edwardUL99.simple.web.exceptions;

/**
 * An exception that's thrown if configuration of the server fails
 */
public class ConfigurationException extends FatalTerminationException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ConfigurationException(String message) {
        super(message);
    }
}
