package io.github.edwardUL99.simple.web.sockets;

import io.github.edwardUL99.simple.web.exceptions.SocketException;

/**
 * Represents an object that receives a request over a socket and returns the received data
 */
public interface SocketReceiver {
    ReceivedRequest receive() throws SocketException;
}
