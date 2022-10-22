package io.github.edwardUL99.simple.web.injection;

/**
 * This interface defines an object that can inject objects into fields annotated with @Resource
 */
public interface ResourceInjector {
    /**
     * Inject any @Resource annotated field of the provided object
     * @param obj the object to inject
     */
    void inject(Object obj);
}
