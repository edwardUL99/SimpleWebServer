package io.github.edwardUL99.simple.web.initialization;

import io.github.edwardUL99.simple.web.RegisteredHandlers;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.configuration.annotations.AnnotationsProcessor;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

import java.io.IOException;

/**
 * This class initializes all the request handlers for the web server
 */
public class RequestHandlersInitializer implements WebServerInitializer {
    /**
     * Perform initialization and then return the result of that initialization
     *
     * @return the result of the initialization
     */
    @Override
    public InitializationResult initialize() {
        if (Configuration.getGlobalConfiguration() == null)
            return new InitializationResult(false,
                    new ConfigurationException("The server needs to be configured before initialization"));

        try {
            RegisteredHandlers.configure(); // configure the registered handlers class so it can read from the JSON file and be available for the annotations
            AnnotationsProcessor.newInstance().processAnnotations(); // the new annotations processing method of registering handlers

            return new InitializationResult(true, null);
        } catch (IOException | ConfigurationException ex) {
            return new InitializationResult(false, ex);
        }
    }
}
