package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.requests.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Storage of request handlers per method
 */
public final class RequestHandlers {
    private static final Map<RequestMethod, RequestHandler> handlers = new HashMap<>();

    static {
        handlers.put(RequestMethod.GET, new GetRequestHandler());
    }

    public static void registerHandler(RequestMethod method, RequestHandler handler) {
        handlers.put(method, handler);
    }

    public static RequestHandler getHandler(RequestMethod method) {
        return handlers.get(method);
    }
}
