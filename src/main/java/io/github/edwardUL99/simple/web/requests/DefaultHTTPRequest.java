package io.github.edwardUL99.simple.web.requests;

import java.util.Map;

/**
 * The default HTTP request implementation
 */
public class DefaultHTTPRequest implements HTTPRequest {
    private final RequestMethod requestMethod;
    private final String path;
    private final Map<String, String> params;
    private final Map<String, String> headers;
    private final String body;

    public DefaultHTTPRequest(RequestMethod requestMethod, String path, Map<String, String> params, Map<String, String> headers, String body) {
        this.requestMethod = requestMethod;
        this.path = path;
        this.params = params;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
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
