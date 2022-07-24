package io.github.edwardUL99.simple.web.configuration;

import io.github.edwardUL99.simple.web.Constants;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Stores configuration details for the server
 */
public class Configuration {
    private Integer port;
    private Path serverDirectory;
    private String pathsFile; // path to a paths JSON registration file
    private static Configuration globalConfiguration;

    public Configuration(Integer port, Path serverDirectory, String pathsFile) {
        this.port = port;
        this.serverDirectory = serverDirectory;
        this.pathsFile = pathsFile;
        this.initialise();
    }

    public Configuration(Integer port) {
        this(port, null, "paths.json");
    }

    public Configuration() {
        this(null);
    }

    private void serverDirExists(Path serverDirectory) {
        if (!Files.isDirectory(serverDirectory))
            throw new ConfigurationException("Server directory does not exist!");
    }

    private void initialiseServerDirectory() {
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

    private void initialisePort() {
        if (this.port == null) {
            String portString = System.getenv(Constants.ENV_PORT);

            if (portString == null)
                portString = System.getProperty(Constants.PROP_PORT);

            if (portString == null)
                throw new ConfigurationException("Cannot configure server since port is not specified");

            this.port = Integer.parseInt(portString);
        }
    }

    private void initialise() {
        initialisePort();
        initialiseServerDirectory();
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

    public String getPathsFile() {
        return pathsFile;
    }

    public void setPathsFile(String pathsFile) {
        this.pathsFile = pathsFile;
    }

    public static void setGlobalConfiguration(Configuration globalConfiguration) {
        Configuration.globalConfiguration = globalConfiguration;
    }

    public static Configuration getGlobalConfiguration() {
        if (globalConfiguration == null)
            throw new ConfigurationException("Server is not configured so cannot handle requests");

        return globalConfiguration;
    }
}
