package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.PathInfo;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilder;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.notFound;
import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.ok;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implements handler for GET requests
 */
public class GetRequestHandler implements RequestHandler {
    private final Path httpDirectory;

    public GetRequestHandler() {
        Configuration config = Configuration.getGlobalConfiguration();

        if (config == null)
            throw new ConfigurationException("Server is not configured so cannot handle requests");

        httpDirectory = Constants.getHttpDirectory(config.getServerDirectory());
    }

    private Path retrieveFile(PathInfo pathInfo) {
        String filepath = pathInfo.getPath();

        if (filepath.equals("/"))
            filepath = "index.html";

        if (filepath.startsWith("/"))
            filepath = filepath.substring(1);

        Path path = httpDirectory.resolve(filepath);

        if (!Files.isRegularFile(path)) {
            return null;
        } else {
            return path;
        }
    }

    private String parseContentType(Path file) throws RequestException {
        try {
            return Files.probeContentType(file);
        } catch (IOException ex) {
            throw new RequestException("Failed to determine content type", ex);
        }
    }

    private ResponseBuilder parseFileResponse(HTTPRequest request, Path file) throws RequestException {
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

    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws RequestException {
        PathInfo pathInfo = request.getPathInfo();
        Path file = retrieveFile(pathInfo);

        if (file == null) {
            return notFound(request)
                    .build();
        } else {
            return parseFileResponse(request, file)
                    .build();
        }
    }
}
