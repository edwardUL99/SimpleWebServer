package io.github.edwardUL99.simple.web.requests.response;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds HTTPResponses
 */
public class ResponseBuilder {
    private byte[] body;
    private final Map<String, String> headers;
    private HTTPRequest request;
    private HttpStatus status;

    protected ResponseBuilder() {
        body = new byte[]{};
        headers = new HashMap<>();
        status = HttpStatus.OK;
    }

    public ResponseBuilder withBody(byte[] body) {
        this.body = body;

        return this;
    }

    public ResponseBuilder withHeader(String name, String value) {
        headers.put(name, value);

        return this;
    }

    public ResponseBuilder withHeadersFrom(Map<String, String> headers) {
        this.headers.putAll(headers);

        return this;
    }

    public ResponseBuilder withRequest(HTTPRequest request) {
        this.request = request;

        return this;
    }

    public ResponseBuilder withStatus(HttpStatus status) {
        this.status = status;

        return this;
    }

    public HTTPResponse build() {
        return new DefaultHTTPResponse(request, headers, body, status);
    }
}
