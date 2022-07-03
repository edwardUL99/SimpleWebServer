package io.github.edwardUL99.simple.web.requests.response;

/**
 * Generates the raw HTTP Response
 */
public interface HTTPResponseGenerator {
    byte[] generate(HTTPResponse response);
}
