package io.github.edwardUL99.simple.web.parsing;

import io.github.edwardUL99.simple.web.exceptions.ParsingException;
import io.github.edwardUL99.simple.web.requests.DefaultHTTPRequest;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.PathInfo;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.sockets.ReceivedRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The default implementation of the HTTP Parser
 */
public class DefaultHttpParser implements HttpParser {
    private PathInfo parsePathInfo(String path) {
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        String[] split = path.split("\\?");

        if (split.length < 1 || split.length > 2)
            throw new ParsingException("Invalid path: " + path);

        String pathPart = split[0];
        Map<String, String> params = new HashMap<>();

        if (split.length > 1) {
            String[] paramsSplit = split[1].split("&");

            for (String paramPair : paramsSplit) {
                if (!paramPair.contains("="))
                    throw new ParsingException("Invalid Query Parameters");

                String[] paramSplit = paramPair.split("=");
                params.put(paramSplit[0], paramSplit[1]);
            }
        }

        return new PathInfo(pathPart, params);
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

    private HTTPRequest parseLines(String[] lines) {
        RequestMethod requestMethod = null;
        PathInfo pathInfo = null;
        Map<String, String> headers = new HashMap<>();
        StringBuilder content = new StringBuilder();
        boolean contentFound = false;

        AtomicReference<Integer> parsePosition = new AtomicReference<>(0);

        for (int i = 0; i < lines.length; i++) {
            parsePosition.set(i);

            if (i == 0 || (requestMethod == null && pathInfo == null)) {
                Object[] requestInfo = parseRequestLine(lines[i]);

                if (requestInfo != null) {
                    requestMethod = (RequestMethod) requestInfo[0];
                    pathInfo = (PathInfo) requestInfo[1];
                }
            } else if (!contentFound) {
                headers = parseHeaders(lines, parsePosition);
                i = parsePosition.get();
                contentFound = true;
            } else {
                content.append(lines[i]);
            }
        }

        return new DefaultHTTPRequest(requestMethod, pathInfo, headers, content.toString());
    }

    @Override
    public HTTPRequest parseHTTP(ReceivedRequest request) throws ParsingException {
        String body = request.getBody();
        String[] lines = body.split("\r\n");

        return parseLines(lines);
    }
}
