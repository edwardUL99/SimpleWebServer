package io.github.edwardUL99.simple.web.configuration.annotations;

import io.github.edwardUL99.simple.web.RegisteredHandlers;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of our annotations processor
 */
public class AnnotationsProcessorImpl implements AnnotationsProcessor {
    private final Reflections reflection;

    public AnnotationsProcessorImpl() {
        reflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forJavaClassPath()));
    }

    private List<Class<?>> getControllers() {
        return new ArrayList<>(reflection.getTypesAnnotatedWith(RequestController.class));
    }

    private void validateMethod(Method method) {
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        if (!Modifier.isPublic(method.getModifiers()))
            throw new ConfigurationException("Handler methods must be public: " + methodName);

        if (method.getParameterCount() != 1) {
            throw new ConfigurationException("Handler methods must have 1 argument of type HTTPRequest: " + methodName);
        } else {
            Class<?> parameterType = method.getParameterTypes()[0];

            if (!HTTPRequest.class.equals(parameterType))
                throw new ConfigurationException("Handler methods must take 1 HTTPRequest parameter: " + methodName);
        }

        if (!HTTPResponse.class.isAssignableFrom(method.getReturnType()))
            throw new ConfigurationException("Handler methods must return a HTTPResponse implementation: " + methodName);
    }

    private io.github.edwardUL99.simple.web.requests.handling.RequestHandler createHandler(Object instance, Method method) {
        return r -> {
            try {
                return (HTTPResponse) method.invoke(instance, r);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Throwable cause = ex.getCause();

                if (cause instanceof RequestException)
                    throw (RequestException) cause;
                else
                    throw new RequestException("Failed to invoke request handler method", ex);
            }
        };
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
                io.github.edwardUL99.simple.web.requests.handling.RequestHandler requestHandler= createHandler(instance, method);

                RequestController annotation = controller.getAnnotation(RequestController.class);
                path = addBasePath(annotation.value(), path);

                for (RequestMethod requestMethod : requestMethods)
                    RegisteredHandlers.register(requestMethod, path, requestHandler);
            }
        }
    }

    private void processController(Class<?> controller) {
        try {
            Constructor<?> constructor = controller.getDeclaredConstructor();

            if (!Modifier.isPublic(constructor.getModifiers()))
                throw new ConfigurationException("Controllers must have a public no-arg constructor: " + controller.getName());

            Object instance = constructor.newInstance();
            processHandlerMethods(instance, controller);
        } catch (NoSuchMethodException ex) {
            throw new ConfigurationException("Controllers must have a no-arg constructor: " + controller.getName());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ex) {
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
