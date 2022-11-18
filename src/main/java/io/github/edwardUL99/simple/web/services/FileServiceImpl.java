package io.github.edwardUL99.simple.web.services;

import io.github.edwardUL99.inject.lite.annotations.Injectable;

import java.nio.file.Files;
import java.nio.file.Path;

@Injectable("fileServiceImpl")
public class FileServiceImpl implements FileService {
    private static final String STATIC = "/static";

    @Override
    public Path getFile(Path baseDirectory, String path) {
        Path file = baseDirectory.resolve(path);

        return (Files.isRegularFile(file)) ? file : null;
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

    private Path retrieveFile(Path baseDirectory, String path) {
        String filepath = normalizePath(path);

        Path filePath = baseDirectory.resolve(filepath);

        if (!Files.isRegularFile(filePath)) {
            return null;
        } else {
            return filePath;
        }
    }

    @Override
    public Path getStaticFile(Path baseDirectory, String path) {
        return retrieveFile(baseDirectory, path);
    }
}
