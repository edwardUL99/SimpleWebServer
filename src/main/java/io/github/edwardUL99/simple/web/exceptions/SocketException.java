package io.github.edwardUL99.simple.web.exceptions;

import java.io.IOException;

/**
 * An exception that occurs at the socket level
 */
public class SocketException extends IOException {
    /**
     * Constructs an {@code IOException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     * @since 1.6
     */
    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
