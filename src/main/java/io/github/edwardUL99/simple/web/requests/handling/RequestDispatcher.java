package io.github.edwardUL99.simple.web.requests.handling;

import io.github.edwardUL99.simple.web.exceptions.RequestException;
import io.github.edwardUL99.simple.web.interception.InterceptedRequest;
import io.github.edwardUL99.simple.web.interception.InterceptedResponse;
import io.github.edwardUL99.simple.web.interception.WebInterceptionDispatcher;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.requests.response.ResponseBuilders;

import java.util.Objects;

/**
 * Dispatches requests to configured handlers
 */
public class RequestDispatcher {
    /**
     * The configured request handlers
     */
    private final ConfiguredHandlers handlers;
    /**
     * The singleton instance
     */
    private static RequestDispatcher INSTANCE;
    /**
     * The interception dispatcher
     */
    private static final WebInterceptionDispatcher interceptionDispatcher = WebInterceptionDispatcher.getInstance();

    public RequestDispatcher() {
        handlers = new ConfiguredHandlers();
    }

    public ConfiguredHandlers getHandlers() {
        return handlers;
    }

    private InterceptedRequest requestInterception(HTTPRequest request) {
        InterceptedRequest interceptedRequest = new InterceptedRequest(request);
        InterceptedRequest returned = interceptionDispatcher.onInboundRequest(interceptedRequest);

        return Objects.requireNonNullElse(returned, interceptedRequest);
    }

    private HTTPResponse responseInterception(HTTPResponse response) {
        InterceptedResponse intercepted = new InterceptedResponse(response);
        InterceptedResponse returnVal = interceptionDispatcher.onOutboundResponse(intercepted);

        return Objects.requireNonNullElse(intercepted.getIntercepted(), returnVal.getIntercepted());
    }

    public HTTPResponse dispatch(HTTPRequest request) throws RequestException {
        InterceptedRequest interceptedRequest = requestInterception(request);

        HTTPResponse setResponse = interceptedRequest.getInterceptedResponse();

        if (setResponse != null)
            return setResponse;

        HTTPRequest intercepted = interceptedRequest.getIntercepted();
        RequestHandler handler = handlers.getHandler(intercepted.getRequestMethod(), intercepted.getPath());

        if (handler == null) {
            return ResponseBuilders.notFound(intercepted).build();
        } else {
            HTTPResponse httpResponse = handler.handleRequest(request);

            return responseInterception(httpResponse);
        }
    }

    public static RequestDispatcher getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RequestDispatcher();

        return INSTANCE;
    }
}
