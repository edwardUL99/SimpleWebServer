package io.github.edwardUL99.simple.web.handlers;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.PathInfo;
import io.github.edwardUL99.simple.web.requests.handling.RequestHandler;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.notFound;

/**
 * Used to implement static path retrieval
 */
public class StaticGetHandler implements RequestHandler {
    private final Path httpDirectory;
    private static final String STATIC = "/static";

    public StaticGetHandler() {
        Configuration config = Configuration.getGlobalConfiguration();

        if (config == null)
            throw new ConfigurationException("Server is not configured so cannot handle requests");

        httpDirectory = Constants.getHttpDirectory(config.getServerDirectory());
    }

    private String normalizePath(String filePath) {
        int staticIndex = filePath.indexOf(STATIC);

        if (staticIndex != -1)
            filePath = filePath.substring(staticIndex + STATIC.length());

        if (filePath.equals("/"))
            filePath = "index.html";

        if (filePath.startsWith("/"))
            filePath = filePath.substring(1);

        return filePath;
    }

    private Path retrieveFile(PathInfo pathInfo) {
        String filepath = normalizePath(pathInfo.getPath());

        Path path = httpDirectory.resolve(filepath);

        if (!Files.isRegularFile(path)) {
            return null;
        } else {
            return path;
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
            return Utils.parseFileToResponse(request, file)
                    .build();
        }
    }
}
