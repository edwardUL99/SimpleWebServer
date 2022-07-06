package io.github.edwardUL99.simple.web.parsing;

import io.github.edwardUL99.simple.web.exceptions.ParsingException;
import io.github.edwardUL99.simple.web.parsing.processing.PostProcessor;
import io.github.edwardUL99.simple.web.requests.DefaultHTTPRequest;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.sockets.ReceivedRequest;
import io.github.edwardUL99.simple.web.utils.Utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The default implementation of the HTTP Parser
 */
public class DefaultHttpParser implements HttpParser {
    private final List<PostProcessor> postProcessors;

    public DefaultHttpParser() {
        this(new ArrayList<>());
    }

    public DefaultHttpParser(List<PostProcessor> postProcessors) {
        this.postProcessors = new ArrayList<>(postProcessors);
    }

    @Override
    public void addPostProcessor(PostProcessor processor) {
        postProcessors.add(processor);
    }

    private Object[] parsePathInfo(String path) {
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        String[] split = path.split("\\?");

        if (split.length < 1 || split.length > 2)
            throw new ParsingException("Invalid path: " + path);

        String pathPart = split[0];
        Map<String, String> params;

        if (split.length > 1)
            params = Utils.parseParams(split[1]);
        else
            params = new HashMap<>();

        return new Object[]{pathPart, params};
    }

    // parses GET path http version [0] = RequestMethod [1] = PathInfo [2] = Version
    private Object[] parseRequestLine(String line) {
        if (!line.isEmpty()) {
            String[] split = line.split(" ");

            if (split.length != 3)
                throw new ParsingException("First line of header is invalid: " + line);

            return new Object[]{RequestMethod.valueOf(split[0]), parsePathInfo(split[1]), split[2]};
        }

        return null;
    }

    private Map<String, String> parseHeaders(String[] lines, AtomicReference<Integer> parsePosition) {
        Map<String, String> headers = new HashMap<>();
        boolean end = false;

        for (int i = parsePosition.get(); i < lines.length && !end; i++) {
            parsePosition.set(i);
            String line = lines[i];

            if (line.isEmpty()) {
                end = true;
            } else {
                int colonIndex = line.indexOf(':');

                if (colonIndex == -1)
                    throw new ParsingException("Invalid Header: " + line);

                headers.put(line.substring(0, colonIndex), line.substring(colonIndex + 2));
            }
        }

        return headers;
    }

    @SuppressWarnings("unchecked")
    private HTTPRequest parseLines(String[] lines) {
        RequestMethod requestMethod = null;
        String path = null;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        StringBuilder content = new StringBuilder();
        boolean contentFound = false;

        AtomicReference<Integer> parsePosition = new AtomicReference<>(0);

        for (int i = 0; i < lines.length; i++) {
            parsePosition.set(i);

            if (i == 0 || (requestMethod == null && path == null)) {
                Object[] requestInfo = parseRequestLine(lines[i]);

                if (requestInfo != null) {
                    requestMethod = (RequestMethod) requestInfo[0];
                    Object[] pathInfo = (Object[]) requestInfo[1];
                    path = (String) pathInfo[0];
                    params = (Map<String, String>) pathInfo[1];
                }
            } else if (!contentFound) {
                headers = parseHeaders(lines, parsePosition);
                i = parsePosition.get();
                contentFound = true;
            } else {
                content.append(lines[i]);
            }
        }

        return new DefaultHTTPRequest(requestMethod, path, params, headers, content.toString());
    }

    private HTTPRequest postProcess(HTTPRequest initialRequest) {
        HTTPRequest current = initialRequest;

        for (PostProcessor processor : postProcessors)
            current = processor.process(current);

        return current;
    }

    @Override
    public HTTPRequest parseHTTP(ReceivedRequest request) throws ParsingException {
        String body = request.getBody();
        String[] lines = body.split("\r\n");

        return postProcess
                (parseLines(lines));
    }
}
