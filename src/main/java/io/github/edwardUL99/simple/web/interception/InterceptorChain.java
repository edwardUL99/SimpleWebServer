package io.github.edwardUL99.simple.web.interception;

/**
 * An object that represents a chain in the interception
 * @param <T> the type of the context object passed through interception
 */
public interface InterceptorChain<T> {
    /**
     * Add the provided interceptor to the chain
     * @param interceptor the interceptor to add
     */
    void addInterceptor(Interceptor<T> interceptor);

    /**
     * Pass the provided context to the next interceptor chain if it exists
     * @param context the context being passed down the chain
     */
    void next(T context);

    /**
     * Get the result at the end of the chain
     * @return the result of interception, null if end not reached
     */
    T getResult();

    /**
     * Resets the chain to the start
     */
    void reset();
}
