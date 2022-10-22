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
     * The injector implementation
     */
    private static final ResourceInjector resourceInjector = new ResourceInjectorImpl();
    /**
     * Map of injectables in the injection
     */
    private static final Map<String, InjectionProxy> injectables = new HashMap<>();
    /**
     * Used to search for annotated classes
     */
    private static final Reflections reflection = Utils.getReflections();

    /**
     * Get the resource injector
     * @return the injector
     */
    public static ResourceInjector getResourceInjector() {
        return resourceInjector;
    }

    /**
     * Register an injectable of the given name and cls
     * @param name the name of the injectable
     * @param cls the class to try and instantiate
     */
    public static void registerInjectable(String name, Class<?> cls) {
        try {
            Constructor<?> constructor = cls.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            injectables.put(name, new InjectionProxy(instance, resourceInjector));
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("You need to have a no-arg constructor in an injectable");
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("Failed to instantiate injectable", ex);
        }
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
     * A proxy that injects resources lazily when the injectable is requested
     */
    private static class InjectionProxy {
        /**
         * The injectable object
         */
        private final Object injectable;
        /**
         * The injector to inject resources with
         */
        private final ResourceInjector resourceInjector;
        /**
         * Determines if the object has been injected
         */
        private boolean injected;

        /**
         * Instantiate the proxy
         * @param injectable the injectable object
         * @param resourceInjector the injector to inject resources with
         */
        private InjectionProxy(Object injectable, ResourceInjector resourceInjector) {
            this.injectable = injectable;
            this.resourceInjector = resourceInjector;
        }

        /**
         * Get the injectable object and inject if not already injected
         * @return the object injected with resources
         */
        private Object getInjectable() {
            if (!injected) {
                resourceInjector.inject(injectable);
                injected = true;
            }

            return injectable;
        }
    }
}
