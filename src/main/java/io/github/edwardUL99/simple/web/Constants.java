package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores constants for the server
 */
public final class Constants {
    /**
     * The environment variable that stores the path to the storage location
     */
    public static final String ENV_STORAGE_DIR_LOCATION = "SERVER_DIR";
    /**
     * The system property that stores the path to the storage location
     */
    public static final String PROP_STORAGE_DIR_LOCATION = "server.dir";
    /**
     * The name of the directory that stores the files underneath the server directory
     */
    public static final String HTTP_DIR = "http";
    /**
     * Generic text for bad request message
     */
    public static final String BAD_REQUEST = "<h3>Bad Request</h3><br><p>The server could not handle the request as it was malformed</p>";
    /**
     * Generic text for not found message
     */
    public static final String NOT_FOUND = "<h3>Not Found</h3><br><p>The specified resource could not be found on the server</p>";
    /**
     * Generic text for internal server error
     */
    public static final String INTERNAL_SERVER_ERROR = "<h3>Internal Server Error</h3><br><p>An unexpected error has occurred inside the server while processing your request. Please try again later</p>";
    /**
     * Generic text for service unavailable message
     */
    public static final String SERVICE_UNAVAILABLE = "<h3>Service Unavailable</h4><br><p>The server is unavailable to handle this request right now. It may not know how to handle the request.</p>";
    /**
     * HTML content type
     */
    public static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";

    /**
     * Get the file storage location relative to the provided server directory
     * @param serverDirectory the location of the server directory
     * @return the path representing the file storage directory
     */
    public static Path getHttpDirectory(Path serverDirectory) {
        if (serverDirectory == null)
            throw new ConfigurationException("The serverDirectory is null, it may not have been configured");

        return serverDirectory.resolve(HTTP_DIR);
    }

    /**
     * Creates an error HTML page
     * @param errorText the contents of the page
     * @return the html
     */
    public static String createErrorHTML(String errorText) {
        String header = "<head><title>Server Error</title></head>";
        String footer = String.format("<br><footer>The error occurred at: %s</footer>", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String body = String.format("<body>%s%s</body>", errorText, footer);

        return String.format("<!DOCTYPE html><html>%s%s</html>", header, body);
    }
}
