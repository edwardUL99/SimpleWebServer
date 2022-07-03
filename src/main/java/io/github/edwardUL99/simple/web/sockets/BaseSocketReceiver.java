package io.github.edwardUL99.simple.web.sockets;

import io.github.edwardUL99.simple.web.exceptions.SocketException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a base socket receiver for implementing subclasses to extend
 */
public abstract class BaseSocketReceiver implements SocketReceiver {
    protected final int port;
    protected ServerSocket serverSocket;

    protected BaseSocketReceiver(int port) {
        this.port = port;
    }

    @Override
    public final ReceivedRequest receive() throws SocketException {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ex) {
                throw new SocketException("Failed to initialise Server Socket", ex);
            }
        }

        try {
            Socket client = serverSocket.accept();

            return doReceive(client);
        } catch (IOException ex) {
            throw new SocketException("Failed to receive from socket", ex);
        }
    }

    protected abstract ReceivedRequest doReceive(Socket clientSocket) throws SocketException;
}
