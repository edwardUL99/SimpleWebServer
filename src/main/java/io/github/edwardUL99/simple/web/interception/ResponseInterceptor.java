package io.github.edwardUL99.simple.web.interception;

/**
 * An interceptor that operates on an outgoing HTTPResponse. Any subclass is automatically registered by the server on
 * initialization so ensure this class is subclassed rather than implementing the interface directly
 */
public abstract class ResponseInterceptor implements Interceptor<InterceptedResponse> {

    protected ResponseInterceptor() {
    }

    /**
     * Perform the interception on the provided context. Call chain.next() to proceed with further interception and eventual
     * request processing
     *
     * @param context the context being intercepted
     * @param chain   the chain to pass the context to next
     */
    @Override
    public final void intercept(InterceptedResponse context, InterceptorChain<InterceptedResponse> chain) {
        onInboundRequest(context, chain);
    }

    /**
     * Perform the interception on the outgoing response
     * @param response the outbound response
     * @param chain the chain to pass the response to
     */
    protected abstract void onInboundRequest(InterceptedResponse response, InterceptorChain<InterceptedResponse> chain);
}
