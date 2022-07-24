package io.github.edwardUL99.simple.web.interception;

import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;

import java.util.Objects;

/**
 * A dispatcher for dispatching to web request and responses
 */
public class WebInterceptionDispatcher {
    private final InterceptorChain<InterceptedRequest> requestChain;
    private final InterceptorChain<InterceptedResponse> responseChain;
    private static final WebInterceptionDispatcher INSTANCE = new WebInterceptionDispatcher();

    private WebInterceptionDispatcher() {
        requestChain = new ListInterceptorChain<>();
        responseChain = new ListInterceptorChain<>();
    }

    /**
     * Add the request interceptor to the dispatcher
     * @param interceptor the interceptor to add
     */
    public void addRequestInterceptor(RequestInterceptor interceptor) {
        requestChain.addInterceptor(interceptor);
    }

    /**
     * Add the response interceptor to the dispatcher
     * @param interceptor the interceptor to add
     */
    public void addResponseInterceptor(ResponseInterceptor interceptor) {
        responseChain.addInterceptor(interceptor);
    }

    /**
     * Endpoint to call on an inbound request
     * @param request the request to intercept
     * @return the result of interception
     */
    public InterceptedRequest onInboundRequest(InterceptedRequest request) {
        requestChain.next(request);
        InterceptedRequest intercepted = requestChain.getResult();

        if (intercepted == null) {
            if (request.getInterceptedResponse() == null)
                request.setInterceptedResponse(ResponseBuilders.serviceUnavailable(request.getIntercepted()).build());

            intercepted = request;
        }

        requestChain.reset();

        return intercepted;
    }

    /**
     * Endpoint to call on an outbound response
     * @param response the response to intercept
     * @return the result of interception
     */
    public InterceptedResponse onOutboundResponse(InterceptedResponse response) {
        responseChain.next(response);
        InterceptedResponse intercepted = responseChain.getResult();

        responseChain.reset();

        return Objects.requireNonNullElse(intercepted, response);
    }

    /**
     * Get the instance of the dispatcher
     * @return the dispatcher for web interception
     */
    public static WebInterceptionDispatcher getInstance() {
        return INSTANCE;
    }
}
