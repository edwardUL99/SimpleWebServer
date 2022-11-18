package io.github.edwardUL99.simple.web;

import io.github.edwardUL99.inject.lite.annotations.ContainerInject;
import io.github.edwardUL99.inject.lite.annotations.Injectable;

@ContainerInject("container")
@Injectable("testClass")
public class TestClass implements TestInterface {
    public String value() {
        return "Hello World";
    }
}
