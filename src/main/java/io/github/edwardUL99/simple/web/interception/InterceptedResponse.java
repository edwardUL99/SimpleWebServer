package io.github.edwardUL99.simple.web.interception;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;

import java.util.Map;

/**
 * Represents a http response that's been intercepted
 */
public class InterceptedResponse implements HTTPResponse {
    private HTTPResponse intercepted;

    public InterceptedResponse(HTTPResponse intercepted) {
        this.intercepted = intercepted;
    }

    public HTTPResponse getIntercepted() {
        return intercepted;
    }

    public void setIntercepted(HTTPResponse intercepted) {
        this.intercepted = intercepted;
    }

    @Override
    public HTTPRequest getRequest() {
        return intercepted.getRequest();
    }

    @Override
    public Map<String, String> getHeaders() {
        return intercepted.getHeaders();
    }

    @Override
    public byte[] getBody() {
        return intercepted.getBody();
    }

    @Override
    public HttpStatus getStatus() {
        return intercepted.getStatus();
    }
}
