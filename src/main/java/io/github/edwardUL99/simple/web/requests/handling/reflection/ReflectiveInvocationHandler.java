package io.github.edwardUL99.simple.web.requests.handling.reflection;

import io.github.edwardUL99.simple.web.exceptions.ParameterNotFoundException;
import io.github.edwardUL99.simple.web.exceptions.RequestBodyException;
import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.handling.RequestHandler;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a handler that's invoked through reflection
 */
public class ReflectiveInvocationHandler implements RequestHandler {
    /**
     * The controller object
     */
    private final Object controller;
    /**
     * The target method
     */
    private final Method target;

    public ReflectiveInvocationHandler(Object controller, Method target) {
        this.controller = controller;
        this.target = target;
    }

    private ArgumentProvider getProvider(Parameter p, Map<Class<?>, Object> specialValues) {
        ArgumentProvider provider = ArgumentProviders.getProvider(p);

        if (provider != null) {
            return provider;
        } else {
            Class<?> type = p.getType();

            if (!specialValues.containsKey(type))
                throw new IllegalArgumentException("Invalid parameter " + p.getName());
            else
                return new SpecialArgumentProvider(specialValues.get(type));
        }
    }

    private List<Object> getArgs(HTTPRequest request) {
        Parameter[] parameters = target.getParameters();
        List<Object> args = IntStream.range(0, parameters.length).mapToObj(u -> null).collect(Collectors.toList());

        Map<Class<?>, Object> specialValues = new HashMap<>();
        specialValues.put(HTTPRequest.class, request);
        specialValues.put(RequestBodyException.class, null);

        for (int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            ArgumentProvider provider = getProvider(p, specialValues);
            provider.provide(request, i, parameters, args);
        }

        return args;
    }

    /**
     * Takes the request, processes it and returns the response
     *
     * @param request the request to process
     * @return the response object
     * @throws RequestException if any exception occurs during request processing
     */
    @Override
    public HTTPResponse handleRequest(HTTPRequest request) throws RequestException {
        try {
            List<Object> args = getArgs(request);
            Object[] argsArray = new Object[args.size()];
            argsArray = args.toArray(argsArray);

            try {
                return (HTTPResponse) target.invoke(controller, argsArray);
            } catch (InvocationTargetException | IllegalAccessException ex) {
                Throwable cause = ex.getCause();

                if (cause instanceof RequestException)
                    throw (RequestException) cause;

                throw new RequestException("Failed to invoke handler method", ex);
            }
        } catch (IllegalStateException | RequestBodyException | ParameterNotFoundException ex) {
            return ResponseBuilders.badRequest(request)
                    .build();
        }
    }
}
