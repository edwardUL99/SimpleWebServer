package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.server.AutoConfigurationServer;
import io.github.edwardUL99.simple.web.server.Server;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Configuration.setGlobalConfiguration(
                new Configuration(8080,
                        Path.of(System.getenv("HOME")).resolve("test_server")));

        Server server = new AutoConfigurationServer();
        server.listen();
    }
}
