package io.github.edwardUL99.simple.web.interception;

/**
 * An interceptor that operates on an incoming HTTPRequest. Any subclass is automatically registered by the server on
 *  * initialization so ensure this class is subclassed rather than implementing the interface directly
 */
public abstract class RequestInterceptor implements Interceptor<InterceptedRequest> {

    protected RequestInterceptor() {
    }

    /**
     * Perform the interception on the provided context. Call chain.next() to proceed with further interception and eventual
     * request processing
     *
     * @param context the context being intercepted
     * @param chain   the chain to pass the context to next
     */
    @Override
    public final void intercept(InterceptedRequest context, InterceptorChain<InterceptedRequest> chain) {
        onInboundRequest(context, chain);
    }

    /**
     * Perform the interception on the inbound request
     * @param request the inbound request
     * @param chain the chain to pass the request to
     */
    protected abstract void onInboundRequest(InterceptedRequest request, InterceptorChain<InterceptedRequest> chain);
}
