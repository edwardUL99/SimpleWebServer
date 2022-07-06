package io.github.edwardUL99.simple.web.configuration.annotations;

import io.github.edwardUL99.simple.web.requests.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a request handler
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RequestHandler {
    /**
     * The path to register the controller method for
     * @return the path to register the handler for
     */
    String value();

    /**
     * Register a list of methods that the handler can handle
     * @return the array of allowed methods
     */
    RequestMethod[] methods() default RequestMethod.GET;
}
