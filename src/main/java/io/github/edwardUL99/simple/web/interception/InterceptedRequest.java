package io.github.edwardUL99.simple.web.interception;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;

import java.util.Map;

/**
 * Represents a http request that's been intercepted
 */
public class InterceptedRequest implements HTTPRequest {
    private HTTPRequest intercepted;
    private HTTPResponse response;

    public InterceptedRequest(HTTPRequest intercepted) {
        this.setIntercepted(intercepted);
    }

    public void setIntercepted(HTTPRequest intercepted) {
        this.intercepted = intercepted;
    }

    public HTTPRequest getIntercepted() {
        return intercepted;
    }

    public HTTPResponse getInterceptedResponse() {
        return response;
    }

    public void setInterceptedResponse(HTTPResponse response) {
        this.response = response;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return intercepted.getRequestMethod();
    }

    @Override
    public String getPath() {
        return intercepted.getPath();
    }

    @Override
    public Map<String, String> getParams() {
        return intercepted.getParams();
    }

    @Override
    public Map<String, String> getHeaders() {
        return intercepted.getParams();
    }

    @Override
    public String getBody() {
        return intercepted.getBody();
    }
}
