package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;

/**
 * Represents an object that can handle a request
 */
public interface RequestHandler {
    HTTPResponse handleRequest(HTTPRequest request) throws RequestException;
}
