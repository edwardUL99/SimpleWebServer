package io.github.edwardUL99.simple.web.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method parameter as a parameter that should be populated from the request
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * The name of the parameter in the request
     * @return the name, or if empty, the name of the parameter will be taken from the parameter name
     */
    String name() default "";

    /**
     * Default value if the parameter doesn't exist. If this returns an empty string, a ParameterNotFoundException is thrown
     * @return the default value
     */
    String defaultValue() default "";

    /**
     * If not required, the null value for that parameter type will be passed in
     * @return true if required, false if not
     */
    boolean required() default true;
}
