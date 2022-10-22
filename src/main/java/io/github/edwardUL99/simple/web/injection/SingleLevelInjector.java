package io.github.edwardUL99.simple.web.injection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a resource injector which only injects fields in the class only and not the inheritance hierarchy
 */
public class SingleLevelInjector extends BaseResourceInjector {
    @Override
    protected List<Field> getFields(Class<?> cls) {
        return Arrays.asList(cls.getDeclaredFields());
    }
}
