package io.github.edwardUL99.simple.web.exceptions;

import com.google.gson.JsonSyntaxException;

/**
 * Thrown when failed to parse request body
 */
public class RequestBodyException extends RuntimeException {
    private final JsonSyntaxException syntaxException;

    public RequestBodyException(String message, JsonSyntaxException syntaxException) {
        super(message);
        this.syntaxException = syntaxException;
    }

    public JsonSyntaxException getSyntaxException() {
        return syntaxException;
    }
}
