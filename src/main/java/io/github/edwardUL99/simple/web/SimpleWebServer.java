package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.initialization.WebServerInitializers;
import io.github.edwardUL99.simple.web.logging.ServerLogger;
import io.github.edwardUL99.simple.web.server.AutoConfigurationServer;
import io.github.edwardUL99.simple.web.server.Server;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main entrypoint into the server
 */
public final class SimpleWebServer {
    private static Server serverInstance;

    public static Server getServerInstance() {
        return serverInstance;
    }

    private static Configuration getConfiguration(String[] args) {
        if (args.length > 0) {
            Integer port = null;
            Path serverDirectory = null;

            List<String> argsList = new ArrayList<>(Arrays.asList(args));

            int portIndex = argsList.indexOf("-p");

            if (portIndex != -1) {
                portIndex++;

                if (portIndex > args.length) {
                    System.err.println("-p flag needs to have a port argument");
                } else {
                    port = Integer.parseInt(argsList.get(portIndex));
                }
            }

            int dirIndex = argsList.indexOf("-s");

            if (dirIndex != -1) {
                dirIndex++;

                if (dirIndex > args.length) {
                    System.err.println("-s flag needs to have a server directory path argument");
                } else {
                    serverDirectory = Path.of(argsList.get(dirIndex));
                }
            }

            return new Configuration(port, serverDirectory, "paths.json");
        } else {
            return new Configuration();
        }
    }

    public static void run(Class<?> runningClass, String[] args) {
        Configuration.setGlobalConfiguration(getConfiguration(args));
        ServerLogger log = ServerLogger.getLogger();

        WebServerInitializers.initialize();

        log.info(String.format("Starting SimpleWebServer from class %s", runningClass.getName()));

        serverInstance = new AutoConfigurationServer();
        serverInstance.listen();
        serverInstance = null;
    }
}
