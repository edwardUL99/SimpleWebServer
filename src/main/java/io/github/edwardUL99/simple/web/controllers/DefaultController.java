package io.github.edwardUL99.simple.web.controllers;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestController;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestHandler;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.notFound;

/**
 * A controller to implement built in methods
 */
@RequestController
public class DefaultController {
    private final Path httpDirectory;
    private static final String STATIC = "/static";

    public DefaultController() {
        Configuration config = Configuration.getGlobalConfiguration();

        httpDirectory = Constants.getHttpDirectory(config.getServerDirectory());
    }

    @RequestHandler("/favicon.ico")
    public HTTPResponse favicon(HTTPRequest request) throws RequestException {
        Path file = httpDirectory.resolve("favicon.ico");

        if (!Files.isRegularFile(file)) {
            return ResponseBuilders.notFound(request).build();
        } else {
            return Utils.parseFileToResponse(request, file)
                    .build();
        }
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

    private Path retrieveFile(String path) {
        String filepath = normalizePath(path);

        Path filePath = httpDirectory.resolve(filepath);

        if (!Files.isRegularFile(filePath)) {
            return null;
        } else {
            return filePath;
        }
    }

    @RequestHandler("/static/**")
    public HTTPResponse staticFiles(HTTPRequest request) throws RequestException {
        Path file = retrieveFile(request.getPath());

        if (file == null) {
            return notFound(request)
                    .build();
        } else {
            return Utils.parseFileToResponse(request, file)
                    .build();
        }
    }
}
