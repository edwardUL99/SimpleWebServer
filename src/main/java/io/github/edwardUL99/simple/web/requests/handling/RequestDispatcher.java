package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;

/**
 * Dispatches requests to configured handlers
 */
public class RequestDispatcher {
    /**
     * The configured request handlers
     */
    private final ConfiguredHandlers handlers;
    /**
     * The singleton instance
     */
    private static RequestDispatcher INSTANCE;

    public RequestDispatcher() {
        handlers = new ConfiguredHandlers();
    }

    public ConfiguredHandlers getHandlers() {
        return handlers;
    }

    public HTTPResponse dispatch(HTTPRequest request) throws RequestException {
        RequestHandler handler = handlers.getHandler(request.getRequestMethod(), request.getPathInfo().getPath());

        return (handler == null) ? ResponseBuilders.notFound(request).build() : handler.handleRequest(request);
    }

    public static RequestDispatcher getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RequestDispatcher();

        return INSTANCE;
    }
}
