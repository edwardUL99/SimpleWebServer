package io.github.edwardUL99.simple.web.handlers;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.handling.RequestHandler;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Used to allow browsers to retrieve the favicon
 */
public class FaviconGetHandler implements RequestHandler {
    private final Path httpDirectory;
    private static final String FAVICON = "/favicon.ico";

    public FaviconGetHandler() {
        Configuration config = Configuration.getGlobalConfiguration();

        if (config == null)
            throw new ConfigurationException("Server is not configured so cannot handle requests");

        httpDirectory = Constants.getHttpDirectory(config.getServerDirectory());
    }

    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws RequestException {
        String path = request.getPathInfo().getPath();

        if (!path.equals(FAVICON)) {
            return ResponseBuilders.badRequest(request).build();
        } else {
            path = path.substring(1);
            Path file = httpDirectory.resolve(path);

            if (!Files.isRegularFile(file)) {
                return ResponseBuilders.notFound(request).build();
            } else {
                return Utils.parseFileToResponse(request, file)
                        .build();
            }
        }
    }
}
