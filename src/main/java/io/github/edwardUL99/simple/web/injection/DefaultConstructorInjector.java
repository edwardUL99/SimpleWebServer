package io.github.edwardUL99.simple.web.injection;

import io.github.edwardUL99.simple.web.configuration.annotations.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * Default implementation of the constructor injector interface
 */
public class DefaultConstructorInjector implements ConstructorInjector {
    // gets the constructor annotated with Inject
    private Constructor<?> getInjectConstructor(Class<?> cls) {
        Constructor<?> injectConstructor = null;
        Constructor<?>[] constructors = cls.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            Inject inject = constructor.getAnnotation(Inject.class);

            if (inject != null) {
                if (injectConstructor != null)
                    throw new IllegalStateException("Only one constructor annotated with Inject is allowed");

                if (constructor.getParameters().length == 0)
                    throw new IllegalStateException("Constructors annotated with Inject must have at least one parameter");
                else
                    injectConstructor = constructor;
            }
        }

        return injectConstructor;
    }

    // gets the no arg constructor
    private Constructor<?> getNoArg(Class<?> cls) {
        try {
            return cls.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Without Inject annotation, you must have a no-arg constructor");
        }
    }

    private Object inject(Constructor<?> constructor) throws ReflectiveOperationException {
        Parameter[] parameters = constructor.getParameters();
        Object[] instances = new Object[parameters.length];

        for (int i = 0; i < instances.length; i++)
            instances[i] = Injection.getInjectableByType(parameters[i].getType());

        return constructor.newInstance(instances);
    }

    @Override
    public Object injectConstructor(Class<?> cls) {
        Constructor<?> inject = getInjectConstructor(cls);

        try {
            if (inject == null) {
                return getNoArg(cls).newInstance();
            } else {
                return inject(inject);
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("Failed to inject constructor", ex);
        }
    }
}
