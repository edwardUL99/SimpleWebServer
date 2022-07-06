package io.github.edwardUL99.simple.web.parsing.processing;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;

/**
 * An object to process a parsed HTTP Request and return the processed request
 */
public interface PostProcessor {
    /**
     * Process the given request and return the processed request
     * @param request the request to process
     * @return the processed request
     */
    HTTPRequest process(HTTPRequest request);
}
