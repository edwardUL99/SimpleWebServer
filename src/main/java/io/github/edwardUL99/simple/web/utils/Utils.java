package io.github.edwardUL99.simple.web.utils;

import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.ok;

/**
 * A utilities class
 */
public final class Utils {
    public static String parseContentType(Path file) throws RequestException {
        try {
            return Files.probeContentType(file);
        } catch (IOException ex) {
            throw new RequestException("Failed to determine content type", ex);
        }
    }

    public static ResponseBuilder parseFileToResponse(HTTPRequest request, Path file) throws RequestException {
        String contentType = parseContentType(file);

        try {
            InputStream stream = new FileInputStream(file.toFile());
            byte[] body = stream.readAllBytes();

            return ok()
                    .withRequest(request)
                    .withHeadersFrom(request.getHeaders())
                    .withHeader("Content-Type", contentType)
                    .withBody(body);
        } catch (IOException ex) {
            throw new RequestException("Failed to read file", ex);
        }
    }
}
