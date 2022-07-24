package io.github.edwardUL99.simple.web.initialization;

import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.interception.RequestInterceptor;
import io.github.edwardUL99.simple.web.interception.ResponseInterceptor;
import io.github.edwardUL99.simple.web.interception.WebInterceptionDispatcher;
import io.github.edwardUL99.simple.web.utils.Utils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Initializes all interceptors found on the classpath
 */
public class InterceptorsInitializer implements WebServerInitializer {
    private static final Reflections reflections = Utils.getReflections();
    private static WebInterceptionDispatcher dispatcher = WebInterceptionDispatcher.getInstance();

    private Object construct(Class<?> subClass) {
        try {
            Constructor<?> constructor = subClass.getDeclaredConstructor();

            return constructor.newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new ConfigurationException("Interceptors must have a single-arg constructor", ex);
        }
    }

    /**
     * Initialize the interceptors which subclass the provided type. Only type allowed is either
     * RequestInterceptor or ResponseInterceptor
     * @param type the parent interceptor type
     */
    private void initializeInterceptors(Class<?> type) {
        if (!List.of(RequestInterceptor.class, ResponseInterceptor.class).contains(type))
            throw new IllegalArgumentException("Invalid type " + type);

        for (Class<?> subClass: reflections.getSubTypesOf(type)) {
            Object constructed = construct(subClass);

            if (constructed instanceof RequestInterceptor)
                dispatcher.addRequestInterceptor((RequestInterceptor) constructed);
            else
                dispatcher.addResponseInterceptor((ResponseInterceptor) constructed);
        }
    }

    /**
     * Perform initialization and then return the result of that initialization
     *
     * @return the result of the initialization
     */
    @Override
    public InitializationResult initialize() {
        try {
            initializeInterceptors(RequestInterceptor.class);
            initializeInterceptors(ResponseInterceptor.class);

            return new InitializationResult(true, null);
        } catch (ConfigurationException ex) {
            return new InitializationResult(false, ex);
        }
    }
}
