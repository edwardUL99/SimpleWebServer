package io.github.edwardUL99.simple.web.requests;

import java.util.Map;

/**
 * Represents a request sent into the server
 */
public interface HTTPRequest {
    RequestMethod getRequestMethod();
    String getPath();
    Map<String, String> getParams();
    Map<String, String> getHeaders();
    String getBody();
}
