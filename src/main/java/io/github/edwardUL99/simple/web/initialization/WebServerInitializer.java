package io.github.edwardUL99.simple.web.initialization;

/**
 * This interface represents an object that can initialize the server
 */
public interface WebServerInitializer {
    /**
     * Perform initialization and then return the result of that initialization
     * @return the result of the initialization
     */
    InitializationResult initialize();
}
