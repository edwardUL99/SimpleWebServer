package io.github.edwardUL99.simple.web.requests.response;

import io.github.edwardUL99.simple.web.requests.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Default generator for http response
 */
public class DefaultResponseGenerator implements HTTPResponseGenerator {
    private static final String CRLF = "\r\n";

    private String generateHeaders(Map<String, String> headers) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> e : headers.entrySet())
            builder.append(String.format("%s: %s%s", e.getKey(), e.getValue(), CRLF));

        builder.append(CRLF);

        return builder.toString();
    }

    private byte[] mergeHeaderBody(byte[] header, byte[] body) {
        byte[] full = new byte[header.length + body.length];

        int j = 0;

        for (int i = 0; i < full.length; i++) {
            if (i < header.length) {
                full[i] =  header[i];
            } else {
                full[i] = body[j++];
            }
        }

        return full;
    }

    @Override
    public byte[] generate(HTTPResponse response) {
        HttpStatus status = response.getStatus();
        String headerLine = String.format("HTTP/1.1 %d %s%s", status.getCode(), status.getName(), CRLF);
        String headers = generateHeaders(response.getHeaders());

        byte[] headerBytes = String.format("%s%s", headerLine, headers).getBytes(StandardCharsets.UTF_8);
        byte[] body = response.getBody();

        return mergeHeaderBody(headerBytes, body);
    }
}
