package io.github.edwardUL99.simple.web.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a class that can be injected
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Injectable {
    /**
     * The name of the injectable
     * @return injectable name
     */
    String value();
}
