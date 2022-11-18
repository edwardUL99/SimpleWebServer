package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.inject.lite.annotations.Inject;
import io.github.edwardUL99.simple.web.server.Server;

public class TestClient {
    @Inject("server")
    private Server server;
    @Inject("testClass")
    private TestClass testClass;

    public void service() {
        System.out.println(testClass.value() + ". Server is: " + ((server.isStarted()) ? "Started":"Stopped"));
    }
}
