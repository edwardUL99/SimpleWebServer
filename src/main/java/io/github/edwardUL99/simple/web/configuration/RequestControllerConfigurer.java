package io.github.edwardUL99.simple.web.configuration;

import io.github.edwardUL99.inject.lite.annotations.processing.AnnotationProcessor;
import io.github.edwardUL99.inject.lite.annotations.processing.CustomInjectableProcessor;
import io.github.edwardUL99.simple.web.RegisteredHandlers;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestController;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestHandler;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.handling.reflection.ReflectiveInvocationHandler;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility class for configuring a request controller
 */
public final class RequestControllerConfigurer {
    /**
     * The processor to configure request controllers as custom injectables
     */
    public static final AnnotationProcessor<RequestController> requestControllerProcessor;

    static {
        requestControllerProcessor = new CustomInjectableProcessor<>(
                a -> a.getType().getName(),
                RequestControllerConfigurer::configureController
        );
    }

    private static void validateMethod(Method method) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        if (!Modifier.isPublic(method.getModifiers()))
            throw new ConfigurationException("Handler methods must be public: " + methodName);

        if (!HTTPResponse.class.isAssignableFrom(method.getReturnType()))
            throw new ConfigurationException("Handler methods must return a HTTPResponse implementation: " + methodName);
    }

    private static String addBasePath(String basePath, String path) {
        if (basePath != null && !basePath.isEmpty()) {
            if (!basePath.endsWith("/") && !path.startsWith("/"))
                basePath += "/";

            return basePath + path;
        }

        return path;
    }

    private static void processHandlerMethods(Object instance, Class<?> controller) {
        for (Method method : controller.getMethods()) {
            RequestHandler handler = method.getAnnotation(RequestHandler.class);

            if (handler != null) {
                validateMethod(method);

                String path = handler.value();
                RequestMethod[] requestMethods = handler.methods();

                RequestController annotation = controller.getAnnotation(RequestController.class);
                path = addBasePath(annotation.value(), path);

                for (RequestMethod requestMethod : requestMethods)
                    RegisteredHandlers.register(requestMethod, path,
                            new ReflectiveInvocationHandler(instance, method));
            }
        }
    }

    /**
     * Configure the provided controller and handler methods
     * @param controller the controller object
     */
    public static void configureController(Object controller) {
        processHandlerMethods(controller, controller.getClass());
    }
}
