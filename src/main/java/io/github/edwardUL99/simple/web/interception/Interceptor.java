package io.github.edwardUL99.simple.web.interception;

/**
 * An interface for an interceptor that can operate on any context object specified by the T type parameter
 * @param <T> the type of the context passed in
 */
public interface Interceptor<T> {
    /**
     * Perform the interception on the provided context. Call chain.next() to proceed with further interception and eventual
     * request processing
     * @param context the context being intercepted
     * @param chain the chain to pass the context to next
     */
    void intercept(T context, InterceptorChain<T> chain);
}
