package io.github.edwardUL99.simple.web.requests.response;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.util.Map;

/**
 * Represents a response to a http request
 */
public interface HTTPResponse {
    HTTPRequest getRequest();
    Map<String, String> getHeaders();
    byte[] getBody();
    HttpStatus getStatus();
}
