package io.github.edwardUL99.simple.web.exceptions;

/**
 * Thrown when a request parameter can't be found
 */
public class ParameterNotFoundException extends RuntimeException {
    public ParameterNotFoundException(String name) {
        super("Parameter " + name + " not found in the request");
    }
}
