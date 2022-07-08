package io.github.edwardUL99.simple.web.requests.handling.reflection;

import io.github.edwardUL99.simple.web.configuration.annotations.RequestParam;
import io.github.edwardUL99.simple.web.exceptions.ParameterNotFoundException;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A provider that provides an argument from a request parameter annotation
 */
public class RequestParamProvider implements ArgumentProvider {
    /**
     * Converters for defined parameter types
     */
    private static final Map<Class<?>, Function<String, Object>> converters = new HashMap<>();
    /**
     * Null values for types
     */
    private static final Map<Class<?>, Object> nullValues = new HashMap<>();

    static {
        putConverter(s -> s, String.class);
        putConverter(Integer::parseInt, Integer.class, int.class);
        putConverter(Long::parseLong, Long.class, long.class);
        putConverter(Double::parseDouble, Double.class, double.class);
        putConverter(Float::parseFloat, Float.class, float.class);
        putConverter(Byte::parseByte, Byte.class, byte.class);
        putConverter(Boolean::parseBoolean, Boolean.class, boolean.class);

        putNullValue(0, int.class);
        putNullValue(0L, long.class);
        putNullValue(0.0, double.class);
        putNullValue(0.0f, float.class);
        putNullValue(0, byte.class);
        putNullValue(false, boolean.class);
    }

    private static void putConverter(Function<String, Object> converter, Class<?>...classes) {
        for (Class<?> cls : classes)
            converters.put(cls, converter);
    }

    private static void putNullValue(Object nullValue, Class<?> cls) {
        nullValues.put(cls, nullValue);
    }

    private Object getArg(Parameter parameter, RequestParam requestParam, Map<String, String> params) {
        Class<?> paramType = parameter.getType();
        String name = parameter.getName();
        String definedName = requestParam.name();
        String defaultValue = requestParam.defaultValue();
        boolean required = requestParam.required();

        if (!definedName.isEmpty())
            name = definedName;

        String param = params.getOrDefault(name, (defaultValue.isEmpty()) ? null:defaultValue);

        if (required && param == null) {
            throw new ParameterNotFoundException(name);
        } else if (!required && param == null) {
            return nullValues.get(paramType);
        } else {
            Function<String, Object> converter = converters.get(paramType);

            if (converter == null) {
                throw new IllegalStateException("The server does not know how to convert parameters of type: " + paramType);
            } else {
                try {
                    return converter.apply(param);
                } catch (NumberFormatException ex) {
                    throw new IllegalStateException("Failed to parse parameter of type: " + paramType);
                }
            }
        }
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
        Map<String, String> params = request.getParams();
        Parameter parameter = parameters[position];
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);

        if (requestParam != null)
            arguments.set(position, getArg(parameter, requestParam, params));
    }
}
