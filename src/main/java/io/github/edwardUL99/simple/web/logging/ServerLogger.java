package io.github.edwardUL99.simple.web.logging;

import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.HttpStatus;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Logs events from the server to a logs directory
 */
public class ServerLogger {
    private final Logger log = LoggerFactory.getLogger(ServerLogger.class);
    private FileWriter writer;
    private static ServerLogger INSTANCE;

    public enum LogType {
        INFO,
        ERROR
    }

    protected ServerLogger() {
        Configuration config = Configuration.getGlobalConfiguration();

        if (config == null)
            throw new ConfigurationException("The server is not configured");

        initialiseLogs(config.getServerDirectory());
    }

    private void initialiseLogs(Path serverDirectory) {
        Path logDir = serverDirectory.resolve("logs");
        Path logFile = logDir.resolve("server.log");

        if (!Files.isDirectory(logDir)) {
            try {
                Files.createDirectories(logDir);
            } catch (IOException ex) {
                throw new ConfigurationException("Failed to initialise log directory");
            }
        }

        if (!Files.isRegularFile(logFile)) {
            try {
                Files.createFile(logFile);
            } catch (IOException ex) {
                throw new ConfigurationException("Failed to initialise log file");
            }
        }

        try {
            writer = new FileWriter(logFile.toFile(), true);
        } catch (IOException ex) {
            throw new ConfigurationException("Failed to write to log");
        }
    }

    private void writeLine(LogType type, String line) {
        try {
            if (type == LogType.INFO)
                log.info(line);
            else
                log.error(line);

            writer.write(String.format("[%s] - %s - %s\n", type.toString(), LocalDateTime.now(), line));
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void logAccess(HTTPRequest request, HTTPResponse response) {
        HttpStatus status = response.getStatus();
        int code = status.getCode();
        String message = String.format("%s [ %s ] HTTP/1.1 - %d %s",
                request.getRequestMethod(), request.getPath(), code, status.getName());

        if (code < 400) {
            writeLine(LogType.INFO, message);
        } else {
            writeLine(LogType.ERROR, message);
        }
    }

    public void throwable(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);

        String ex = stringWriter.toString();

        writeLine(LogType.ERROR, ex);
    }

    public void info(String info) {
        writeLine(LogType.INFO, info);
    }

    public void debug(String debug) {
        log.debug(debug);
    }

    public void error(String error) {
        writeLine(LogType.ERROR, error);
    }

    public static ServerLogger getLogger() {
        if (INSTANCE == null)
            INSTANCE = new ServerLogger();

        return INSTANCE;
    }
}
