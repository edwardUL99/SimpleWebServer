package io.github.edwardUL99.simple.web.controllers;

import io.github.edwardUL99.inject.lite.annotations.ContainerInject;
import io.github.edwardUL99.inject.lite.annotations.Inject;
import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestController;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestHandler;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;
import io.github.edwardUL99.simple.web.services.FileService;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.nio.file.Path;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.notFound;

/**
 * A controller to implement built in methods
 */
@RequestController
@ContainerInject("serverContainer")
public class DefaultController {
    private final Path httpDirectory;
    private final FileService fileService;

    @Inject
    public DefaultController(FileService fileService) {
        Configuration config = Configuration.getGlobalConfiguration();
        httpDirectory = Constants.getHttpDirectory(config.getServerDirectory());

        this.fileService = fileService;
    }

    @RequestHandler("/favicon.ico")
    public HTTPResponse favicon(HTTPRequest request) throws RequestException {
        Path favicon = fileService.getFile(httpDirectory, "favicon.ico");

        if (favicon == null) {
            return ResponseBuilders.notFound(request).build();
        } else {
            return Utils.parseFileToResponse(request, favicon)
                    .build();
        }
    }

    @RequestHandler("/static/**")
    public HTTPResponse staticFiles(HTTPRequest request) throws RequestException {
        Path file = fileService.getStaticFile(httpDirectory, request.getPath());

        if (file == null) {
            return notFound(request)
                    .build();
        } else {
            return Utils.parseFileToResponse(request, file)
                    .build();
        }
    }
}
