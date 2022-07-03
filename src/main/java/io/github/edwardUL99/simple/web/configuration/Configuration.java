package io.github.edwardUL99.simple.web.configuration;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Stores configuration details for the server
 */
public class Configuration {
    private int port;
    private Path serverDirectory;
    private static Configuration globalConfiguration;

    public Configuration(int port, Path serverDirectory) {
        this.port = port;
        this.serverDirectory = serverDirectory;
        this.initialise();
    }

    public Configuration(int port) {
        this(port, null);
    }

    public Configuration() {
        this(8080);
    }

    private void serverDirExists(Path serverDirectory) {
        if (!Files.isDirectory(serverDirectory))
            throw new ConfigurationException("Server directory does not exist!");
    }

    private void initialise() {
        if (this.serverDirectory == null) {
            String path = System.getenv(Constants.ENV_STORAGE_DIR_LOCATION);

            if (path == null)
                path = System.getProperty(Constants.PROP_STORAGE_DIR_LOCATION);

            if (path == null)
                throw new ConfigurationException("Cannot configure server since it does not know where to retrieve files.");

            this.serverDirectory = Path.of(path);
        }

        serverDirExists(serverDirectory);

        if (!Files.isDirectory(Constants.getHttpDirectory(serverDirectory)))
            throw new ConfigurationException("The directory http should exist to store all server files in the server directory");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Path getServerDirectory() {
        return serverDirectory;
    }

    public void setServerDirectory(Path serverDirectory) {
        this.serverDirectory = serverDirectory;
    }

    public static void setGlobalConfiguration(Configuration globalConfiguration) {
        Configuration.globalConfiguration = globalConfiguration;
    }

    public static Configuration getGlobalConfiguration() {
        return globalConfiguration;
    }
}
