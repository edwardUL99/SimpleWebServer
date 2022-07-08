package io.github.edwardUL99.simple.web.requests.handling.reflection;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * A provider that provides "special" predefined arguments
 */
public class SpecialArgumentProvider implements ArgumentProvider {
    private final Object provide;

    public SpecialArgumentProvider(Object provide) {
        this.provide = provide;
    }

    /**
     * Provide arguments from the given request based on the current parameter index into the arguments list. The current parameter can be
     * indexed from the array of parameters
     *
     * @param request    the request to pull parameters  from
     * @param position   the current position of the parameter being processed
     * @param parameters the list of all the method parameters
     * @param arguments  the list of arguments to insert the argument into
     */
    @Override
    public void provide(HTTPRequest request, int position, Parameter[] parameters, List<Object> arguments) {
        if (arguments.get(position) == null)
            arguments.set(position, provide);
    }
}
