package io.github.edwardUL99.simple.web.configuration.annotations;

import io.github.edwardUL99.simple.web.RegisteredHandlers;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.injection.Injection;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.handling.reflection.ReflectiveInvocationHandler;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.utils.Utils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of our annotations processor
 */
public class AnnotationsProcessorImpl implements AnnotationsProcessor {
    private final Reflections reflection = Utils.getReflections();

    private List<Class<?>> getControllers() {
        return new ArrayList<>(reflection.getTypesAnnotatedWith(RequestController.class));
    }

    private void validateMethod(Method method) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        if (!Modifier.isPublic(method.getModifiers()))
            throw new ConfigurationException("Handler methods must be public: " + methodName);

        if (!HTTPResponse.class.isAssignableFrom(method.getReturnType()))
            throw new ConfigurationException("Handler methods must return a HTTPResponse implementation: " + methodName);
    }

    private String addBasePath(String basePath, String path) {
        if (basePath != null && !basePath.isEmpty()) {
            if (!basePath.endsWith("/") && !path.startsWith("/"))
                basePath += "/";

            return basePath + path;
        }

        return path;
    }

    private void processHandlerMethods(Object instance, Class<?> controller) {
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

    private void processController(Class<?> controller) {
        try {
            Object instance = Injection.getConstructorInjector().injectConstructor(controller);
            Injection.getResourceInjector().inject(instance);
            processHandlerMethods(instance, controller);
        } catch (Exception ex) {
            throw new ConfigurationException("Failed to initialise controller: " + controller.getName(), ex);
        }
    }

    private void processAndRegister() {
        List<Class<?>> controllers = getControllers();
        controllers.forEach(this::processController);
    }

    /**
     * Process annotations and register the handlers
     *
     * @throws ConfigurationException if any annotation fails to be processed
     */
    @Override
    public void processAnnotations() throws ConfigurationException {
        processAndRegister();
    }
}
