package io.github.edwardUL99.simple.web.requests.handling.reflection;

import io.github.edwardUL99.simple.web.configuration.annotations.RequestBody;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestParam;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides a query mechanism to get argument providers
 */
public final class ArgumentProviders {
    private static final Map<Class<? extends Annotation>, Supplier<ArgumentProvider>> annotationProviders = new HashMap<>();

    static {
        ArgumentProvider requestParamProvider = new RequestParamProvider();
        ArgumentProvider requestBodyProvider = new RequestBodyProvider();

        annotationProviders.put(RequestParam.class, () -> requestParamProvider);
        annotationProviders.put(RequestBody.class, () -> requestBodyProvider);
    }

    public static ArgumentProvider getProvider(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();

        if (annotations.length > 1)
            throw new ConfigurationException("The server does not know how to parse multiple annotations on parameters");

        if (annotations.length == 1) {
            Supplier<ArgumentProvider> supplier = annotationProviders.get(annotations[0].annotationType());

            return (supplier == null) ? null : supplier.get();
        } else {
            return null;
        }
    }
}
