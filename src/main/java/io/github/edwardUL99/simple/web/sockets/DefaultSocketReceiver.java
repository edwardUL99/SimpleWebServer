package io.github.edwardUL99.simple.web.sockets;

import io.github.edwardUL99.simple.web.exceptions.SocketException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The default socket receiver for receiving information over a socket that simply
 * just parses the body and returns a ReceivedRequest with the body and the client socket without any additional processing/checks
 */
public class DefaultSocketReceiver extends BaseSocketReceiver {
    public DefaultSocketReceiver(int port) {
        super(port);
    }

    private String readRequest(Socket socket) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (!in.ready()) {}

        int ch;
        while (in.ready() && (ch = in.read()) != -1)
            stringBuilder.append((char)ch);

        return stringBuilder.toString();
    }

    @Override
    protected ReceivedRequest doReceive(Socket clientSocket) throws SocketException {
        try {
            return new ReceivedRequest(readRequest(clientSocket), clientSocket);
        } catch (IOException ex) {
            throw new SocketException("Failed to read request from client socket", ex);
        }
    }
}
