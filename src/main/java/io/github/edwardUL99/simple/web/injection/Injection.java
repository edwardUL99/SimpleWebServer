package io.github.edwardUL99.simple.web.injection;

import io.github.edwardUL99.simple.web.configuration.annotations.Injectable;
import io.github.edwardUL99.simple.web.utils.Utils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class providing utilities for injection
 */
public final class Injection {
    /**
     * Map of injectables in the injection
     */
    private static final Map<String, InjectionProxy> injectables = new HashMap<>();
    /**
     * Used to search for annotated classes
     */
    private static final Reflections reflection = Utils.getReflections();
    /**
     * Determines if single level injection is enabled
     */
    private static final boolean singleLevelInjection = System.getProperty("single.level.injection") != null;

    /**
     * Get the resource injector
     * @return the injector
     */
    public static ResourceInjector getResourceInjector() {
        return (singleLevelInjection) ? new SingleLevelInjector():new MultiLevelInjector();
    }

    /**
     * Get the injector to inject constructors with
     * @return constructor injector
     */
    public static ConstructorInjector getConstructorInjector() {
        return new DefaultConstructorInjector();
    }

    /**
     * Register an injectable of the given name and cls
     * @param name the name of the injectable
     * @param cls the class to try and instantiate
     */
    public static void registerInjectable(String name, Class<?> cls) {
        injectables.put(name, new InjectionProxy(cls, getConstructorInjector(), getResourceInjector()));
    }

    /**
     * Register classes annotated with Injectable annotations
     */
    public static void registerInjectables() {
        List<Class<?>> classes = new ArrayList<>(reflection.getTypesAnnotatedWith(Injectable.class));

        for (Class<?> cls : classes)
            registerInjectable(cls.getAnnotation(Injectable.class).value(), cls);
    }

    /**
     * Get the injectable registered with name
     * @param name the name of the injectable
     * @return the found injectable
     */
    public static Object getInjectable(String name) {
        InjectionProxy instance = injectables.get(name);

        if (instance == null) {
            throw new IllegalArgumentException("No injectable found with name: " + name);
        } else {
            return instance.getInjectable();
        }
    }

    /**
     * Return the first object in the injection context that can be assigned to the provided type
     * @param type the type to find the object by, can be a superclass/interface
     * @return the first found object
     */
    public static Object getInjectableByType(Class<?> type) {
        for (InjectionProxy proxy : injectables.values()) {
            if (type.isAssignableFrom(proxy.getType()))
                return proxy.getInjectable();
        }

        throw new IllegalArgumentException("No injectable of type: " + type + " found");
    }

    /**
     * A proxy that injects resources lazily when the injectable is requested
     */
    private static class InjectionProxy {
        /**
         * The type of the object
         */
        private final Class<?> type;
        /**
         * The injector to inject constructor with
         */
        private final ConstructorInjector constructorInjector;
        /**
         * The injector to inject resources with
         */
        private final ResourceInjector resourceInjector;
        /**
         * The injected instance
         */
        private Object instance;

        /**
         * Instantiate the proxy
         * @param type the type of the injectable object
         * @param constructorInjector the injector to inject the constructor with
         * @param resourceInjector the injector to inject resources with
         */
        private InjectionProxy(Class<?> type, ConstructorInjector constructorInjector, ResourceInjector resourceInjector) {
            this.type = type;
            this.constructorInjector = constructorInjector;
            this.resourceInjector = resourceInjector;
        }

        /**
         * Get the injectable object and inject if not already injected
         * @return the object injected with resources
         */
        private Object getInjectable() {
            if (instance == null) {
                instance = constructorInjector.injectConstructor(type);
                resourceInjector.inject(instance);
            }

            return instance;
        }

        /**
         * Get the type of the injectable object
         * @return the type
         */
        private Class<?> getType() {
            return type;
        }
    }
}
