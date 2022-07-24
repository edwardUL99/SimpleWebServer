package io.github.edwardUL99.simple.web.utils;

import io.github.edwardUL99.simple.web.exceptions.ParsingException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilder;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.ok;

/**
 * A utilities class
 */
public final class Utils {
    /**
     * A reflections object to use for reflections
     */
    private static Reflections reflections;

    /**
     * Parse the content type of the given file
     * @param file the file to parse
     * @return the content type
     * @throws RequestException if the content type fails to be determined
     */
    public static String parseContentType(Path file) throws RequestException {
        try {
            return Files.probeContentType(file);
        } catch (IOException ex) {
            throw new RequestException("Failed to determine content type", ex);
        }
    }

    /**
     * Parse the specified file into a response to the given request
     * @param request the request to respond to
     * @param file the file to send in the response
     * @return the builder which will eventually build the response
     * @throws RequestException if it fails to be converted to a response
     */
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

    /**
     * Parse & separate params of key=value pairs
     * @param paramsToParse the parameters to parse
     * @return the parsed parameters
     */
    public static Map<String, String> parseParams(String paramsToParse) {
        Map<String, String> params = new HashMap<>();
        String[] paramsSplit = paramsToParse.split("&");

        for (String paramPair : paramsSplit) {
            if (!paramPair.contains("="))
                throw new ParsingException("Invalid Query Parameters");

            String[] paramSplit = paramPair.split("=");
            params.put(paramSplit[0], paramSplit[1]);
        }

        return params;
    }

    /**
     * Get a shared single instance reflections object that scans the Java classpath
     * @return the shared reflections instance
     */
    public static Reflections getReflections() {
        if (reflections == null)
            reflections = new Reflections(new ConfigurationBuilder()
                    .addUrls(ClasspathHelper.forJavaClassPath()));

        return reflections;
    }
}
