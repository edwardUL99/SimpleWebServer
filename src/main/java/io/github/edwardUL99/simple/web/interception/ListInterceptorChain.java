package io.github.edwardUL99.simple.web.interception;

import java.util.ArrayList;
import java.util.List;

/**
 * An interceptor chain that stores interceptors in the order they are added in a list
 */
public class ListInterceptorChain<T> implements InterceptorChain<T> {
    /**
     * The list of interceptors in the chain
     */
    private final List<Interceptor<T>> interceptors;
    /**
     * The current chain index
     */
    private int index;
    /**
     * The end result
     */
    private T result;

    public ListInterceptorChain() {
        interceptors = new ArrayList<>();
    }

    /**
     * Add the provided interceptor to the chain
     *
     * @param interceptor the interceptor to add
     */
    @Override
    public void addInterceptor(Interceptor<T> interceptor) {
        interceptors.add(interceptor);
    }

    /**
     * Pass the provided context to the next interceptor chain if it exists
     *
     * @param context the context being passed down the chain
     */
    @Override
    public void next(T context) {
        if (index < interceptors.size())
            interceptors.get(index++).intercept(context, this);
        else
            result = context;
    }

    /**
     * Get the result at the end of the chain
     *
     * @return the result of interception, null if end not reached
     */
    @Override
    public T getResult() {
        return result;
    }
}
