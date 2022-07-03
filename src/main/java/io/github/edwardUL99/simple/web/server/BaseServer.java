package io.github.edwardUL99.simple.web.server;

import io.github.edwardUL99.simple.web.parsing.HttpParser;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponseGenerator;
import io.github.edwardUL99.simple.web.sockets.SocketReceiver;

/**
 * A base server to listen for requests
 */
public abstract class BaseServer implements Server {
    protected SocketReceiver receiver;
    protected HttpParser parser;
    protected HTTPResponseGenerator generator;

    protected BaseServer(SocketReceiver receiver, HttpParser parser, HTTPResponseGenerator generator) {
        this.receiver = receiver;
        this.parser = parser;
        this.generator = generator;
    }
}
