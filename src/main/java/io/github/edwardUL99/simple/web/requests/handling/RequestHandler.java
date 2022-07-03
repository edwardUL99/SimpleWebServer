package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;

/**
 * Represents an object that can handle a request
 */
public interface RequestHandler {
    /**
     * Takes the request, processes it and returns the response
     * @param request the request to process
     * @return the response object
     * @throws RequestException if any exception occurs during request processing
     */
    HTTPResponse handleRequest(HTTPRequest request) throws RequestException;
}
