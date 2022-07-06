package io.github.edwardUL99.simple.web.parsing.processing;

import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Parses form data into the requests parameters
 */
public class FormDataProcessor implements PostProcessor {
    /**
     * The header for the form data
     */
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    private Map<String, String> parseBody(String body) {
        body = URLDecoder.decode(body, StandardCharsets.UTF_8);

        return Utils.parseParams(body);
    }

    /**
     * Process the given request and return the processed request
     *
     * @param request the request to process
     * @return the processed request
     */
    @Override
    public HTTPRequest process(HTTPRequest request) {
        Map<String, String> headers = request.getHeaders();

        if (headers.getOrDefault("Content-Type", "").equals(FORM_URL_ENCODED)) {
            String body = request.getBody();

            if (body != null) {
                Map<String, String> params = request.getParams();
                params.putAll(parseBody(body));
            }
        }

        return request;
    }
}
