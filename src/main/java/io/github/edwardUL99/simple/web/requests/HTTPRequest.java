package io.github.edwardUL99.simple.web.requests;

import java.util.Map;

/**
 * Represents a request sent into the server
 */
public interface HTTPRequest {
    RequestMethod getRequestMethod();
    PathInfo getPathInfo();
    Map<String, String> getHeaders();
    String getBody();
}
