package io.github.edwardUL99.simple.web.configuration.annotations;

import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

/**
 * The processor for reading annotations
 */
public interface AnnotationsProcessor {
    /**
     * Process annotations and register the handlers
     * @throws ConfigurationException if any annotation fails to be processed
     */
    void processAnnotations() throws ConfigurationException;

    /**
     * Gets a default implementation of the annotation processor used within the system
     * @return the annotation processor implementation
     */
    static AnnotationsProcessor newInstance() {
        return new AnnotationsProcessorImpl();
    }
}
