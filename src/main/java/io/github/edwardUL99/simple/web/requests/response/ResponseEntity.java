package io.github.edwardUL99.simple.web.requests.response;

import com.google.gson.Gson;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows the definition of a response from an object as JSON
 */
public class ResponseEntity<T> implements HTTPResponse {
    /**
     * The request this response is for
     */
    private final HTTPRequest request;
    /**
     * The response entity headers
     */
    private final Map<String, String> headers;
    /**
     * The body to convert
     */
    private final T body;
    /**
     * The status of the response
     */
    private final HttpStatus status;
    /**
     * Used for parsing JSON
     */
    private static final Gson gson = new Gson();

    protected ResponseEntity(HTTPRequest request, Map<String, String> headers, T body, HttpStatus status) {
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
        return gson.toJson(body, body.getClass()).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * A builder for building a ResponseEntity
     * @param <T>
     */
    public static class Builder<T> {
        private final HTTPRequest request;
        private final Map<String, String> headers;
        private T body;
        private HttpStatus status;

        public Builder(HTTPRequest request) {
            this.request = request;
            this.headers = new HashMap<>(
                    Map.of("Content-Type", "application/json")
            );
        }

        public Builder<T> withHeader(String name, String value) {
            headers.put(name, value);

            return this;
        }

        public Builder<T> withBody(T body) {
            this.body = body;

            return this;
        }

        public Builder<T> withStatus(HttpStatus status) {
            this.status = status;

            return this;
        }

        public ResponseEntity<T> build() {
            return new ResponseEntity<>(request, headers, body, status);
        }
    }
}
