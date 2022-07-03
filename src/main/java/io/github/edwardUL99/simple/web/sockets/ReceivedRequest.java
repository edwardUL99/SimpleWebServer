package io.github.edwardUL99.simple.web.sockets;

import io.github.edwardUL99.simple.web.exceptions.SocketException;

import java.io.IOException;
import java.net.Socket;

/**
 * This class represents a request received on a socket
 */
public class ReceivedRequest {
    private final String body;
    /**
     * The socket that sent in the request
     */
    private final Socket clientSocket;

    public ReceivedRequest(String body, Socket clientSocket) {
        this.body = body;
        this.clientSocket = clientSocket;
    }

    public String getBody() {
        return body;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Closes the received request by closing the client socket underlying it
     * @throws SocketException if it fails to be closed
     */
    public void close() throws SocketException {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            throw new SocketException("Failed to close received request", ex);
        }
    }
}
