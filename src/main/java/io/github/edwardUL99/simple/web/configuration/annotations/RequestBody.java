package io.github.edwardUL99.simple.web.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the parameter should be treated as JSON pulled from the request
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
    /**
     * If the request is not JSON or fails to be parsed, a bad request is returned if this is true, otherwise,
     * null is passed in for the param
     * @return the value for fail
     */
    boolean fail() default true;
}
