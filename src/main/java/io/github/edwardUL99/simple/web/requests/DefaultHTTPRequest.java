package io.github.edwardUL99.simple.web.requests;

import java.util.Map;

/**
 * The default HTTP request implementation
 */
public class DefaultHTTPRequest implements HTTPRequest {
    private final RequestMethod requestMethod;
    private final PathInfo pathInfo;
    private final Map<String, String> headers;
    private final String body;

    public DefaultHTTPRequest(RequestMethod requestMethod, PathInfo pathInfo, Map<String, String> headers, String body) {
        this.requestMethod = requestMethod;
        this.pathInfo = pathInfo;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public PathInfo getPathInfo() {
        return pathInfo;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getBody() {
        return body;
    }
}
