package io.github.edwardUL99.simple.web.services;

import java.nio.file.Path;

public interface FileService {
    Path getFile(Path baseDirectory, String path);

    Path getStaticFile(Path baseDirectory, String path);
}
