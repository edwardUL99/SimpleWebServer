package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.inject.lite.Injection;
import io.github.edwardUL99.inject.lite.annotations.Inject;
import io.github.edwardUL99.inject.lite.container.Container;
import io.github.edwardUL99.inject.lite.container.Containers;
import io.github.edwardUL99.inject.lite.injector.Injector;
import io.github.edwardUL99.inject.lite.testing.MockDependency;
import io.github.edwardUL99.inject.lite.testing.TestInject;
import io.github.edwardUL99.inject.lite.testing.junit.TestInjectionExtension;
import io.github.edwardUL99.simple.web.server.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(TestInjectionExtension.class)
public class TestCase {
    @MockDependency("server")
    private Server server;

    @TestInject
    private TestClient client;

    @Inject("testClass")
    private TestClass testClass;

    @Test
    public void test() {
        when(server.isStarted())
            .thenReturn(true);
        client.service();
        System.out.println(Injector.get());
        System.out.println(Injection.newInjector());
        verify(server).isStarted();

        when(server.isStarted())
            .thenReturn(false);
        client.service();

        assertNotNull(testClass.value());
        System.out.println(testClass);
        System.out.println(client);

        Containers.executeSingleContainer(Container.builder().withExecutionUnit(container -> {
            System.out.println(Injector.get());

            container.asyncExecutor().schedule(() -> {
                System.out.println(Injector.get());
                container.setKeepAlive(false);
            });

            container.setKeepAlive(true);
        }));
    }

    @Test
    public void test1() {
        System.out.println(testClass);
        System.out.println(client);
    }
}
