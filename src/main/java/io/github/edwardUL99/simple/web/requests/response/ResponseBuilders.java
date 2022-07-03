package io.github.edwardUL99.simple.web.requests.response;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.nio.charset.StandardCharsets;

/**
 * A utility class for working with ResponseBuilders
 */
public final class ResponseBuilders {
    public static ResponseBuilder newBuilder() {
        return new ResponseBuilder();
    }

    public static ResponseBuilder ok() {
        return newBuilder()
                .withStatus(HttpStatus.OK);
    }

    private static ResponseBuilder error(HTTPRequest request, HttpStatus status, String error) {
        String errorHtml = Constants.createErrorHTML(error);

        return newBuilder()
                .withRequest(request)
                .withStatus(status)
                .withHeader("Content-Type", Constants.HTML_CONTENT_TYPE)
                .withBody(errorHtml.getBytes(StandardCharsets.UTF_8));
    }

    public static ResponseBuilder badRequest(HTTPRequest request) {
        return error(request, HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST);
    }

    public static ResponseBuilder notFound(HTTPRequest request) {
        return error(request, HttpStatus.NOT_FOUND, Constants.NOT_FOUND);
    }

    public static ResponseBuilder internalServerError(HTTPRequest request) {
        return error(request, HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
    }

    public static ResponseBuilder serviceUnavailable(HTTPRequest request) {
        return error(request, HttpStatus.SERVICE_UNAVAILABLE, Constants.SERVICE_UNAVAILABLE);
    }
}
