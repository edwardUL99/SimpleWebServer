package io.github.edwardUL99.simple.web.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a candidate for injection
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
    /**
     * The name of the object to inject
     * @return resource name
     */
    String value() default "";
}
