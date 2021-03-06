package io.github.edwardUL99.simple.web.parsing;

import io.github.edwardUL99.simple.web.exceptions.ParsingException;
import io.github.edwardUL99.simple.web.parsing.processing.PostProcessor;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.sockets.ReceivedRequest;

public interface HttpParser {
    void addPostProcessor(PostProcessor processor);
    HTTPRequest parseHTTP(ReceivedRequest request) throws ParsingException;
}
