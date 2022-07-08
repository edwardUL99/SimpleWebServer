package io.github.edwardUL99.simple.web.requests.handling.reflection;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.edwardUL99.simple.web.configuration.annotations.RequestBody;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.RequestBodyException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Provides an argument from a request body
 */
public class RequestBodyProvider implements ArgumentProvider {
    private boolean requestBodyFound;
    /**
     * For parsing request bodies
     */
    private static final Gson gson = new Gson();

    private Object[] getRequestBody(HTTPRequest request, RequestBody requestBody, Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        String name = parameter.getName();
        boolean fail = requestBody.fail();
        String contentType = request.getHeaders().get("Content-Type");

        if (contentType == null || !contentType.contains("application/json")) {
            RequestBodyException exception = new RequestBodyException("Failed to parse RequestBody for " + name +
                    " as the request does not contain JSON", null);

            if (fail)
                throw exception;
            else
                return new Object[]{null, exception};
        } else {
            try {
                return new Object[]{gson.fromJson(request.getBody(), parameterType), null};
            } catch (JsonSyntaxException ex) {
                RequestBodyException exception = new RequestBodyException("Failed to parse RequestBody for " + name, ex);
                if (fail) {
                    ex.printStackTrace();
                    throw exception;
                } else {
                    return new Object[]{null, exception};
                }
            }
        }
    }

    private int getRequestBodyExceptionPosition(Parameter[] parameters) {
        int pos;

        for (pos = 0; pos < parameters.length && parameters[pos].getType() != RequestBodyException.class; pos++);

        return (pos == parameters.length) ? -1:pos;
    }

    private void processRequestBody(HTTPRequest request, RequestBody requestBody, Parameter[] parameters, int index,
                                    List<Object> args) {
        Parameter p = parameters[index];
        Object[] parsedRequestBody = getRequestBody(request, requestBody, p);
        Object body = parsedRequestBody[0];
        RequestBodyException exception = (RequestBodyException) parsedRequestBody[1];

        if (exception != null) {
            int exceptionPos = getRequestBodyExceptionPosition(parameters);

            if (exceptionPos != -1)
                args.set(exceptionPos, exception);
        }

        args.set(index, body);
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
        Parameter parameter = parameters[position];
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);

        if (requestBody != null) {
            if (requestBodyFound)
                throw new ConfigurationException("You can only have one parameter marked with RequestBody");

            requestBodyFound = true;
            processRequestBody(request, requestBody, parameters, position, arguments);
        }
    }
}
