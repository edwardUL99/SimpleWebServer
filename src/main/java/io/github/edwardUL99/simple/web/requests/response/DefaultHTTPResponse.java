package io.github.edwardUL99.simple.web.requests.response;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.util.Map;

/**
 * This object represents the default response object
 */
public class DefaultHTTPResponse implements HTTPResponse {
    private final HTTPRequest request;
    private final Map<String, String> headers;
    private final byte[] body;
    private final HttpStatus status;

    public DefaultHTTPResponse(HTTPRequest request, Map<String, String> headers, byte[] body, HttpStatus status) {
        this.request = request;
        this.headers = headers;
        this.body = body;
        this.status = status;
    }

    @Override
    public HTTPRequest getRequest() {
        return request;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
