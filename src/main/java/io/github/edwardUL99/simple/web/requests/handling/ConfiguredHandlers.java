package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.handling.paths.PathMatcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers handlers for different request methods and paths
 */
public class ConfiguredHandlers {
    private final PathMatcher matcher;
    private final Map<RequestMethod, Map<String, RequestHandler>> handlers;

    public ConfiguredHandlers() {
        matcher = new PathMatcher();
        handlers = new LinkedHashMap<>();
    }

    public void register(RequestMethod requestMethod, String path, RequestHandler handler) {
        Map<String, RequestHandler> handlers = this.handlers.computeIfAbsent(requestMethod, k -> new LinkedHashMap<>());
        handlers.put(path, handler);
    }

    public RequestHandler getHandler(RequestMethod requestMethod, String path) {
        Map<String, RequestHandler> handlers = this.handlers.get(requestMethod);

        if (handlers != null) {
            for (Map.Entry<String, RequestHandler> e : handlers.entrySet()) {
                String registeredPath = e.getKey();

                if (matcher.matches(path, registeredPath))
                    return e.getValue();
            }
        }

        return null;
    }
}
