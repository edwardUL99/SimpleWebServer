package io.github.edwardUL99.simple.web.server;

import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.exceptions.ConfigurationException;
import io.github.edwardUL99.simple.web.exceptions.FatalTerminationException;
import io.github.edwardUL99.simple.web.exceptions.ParsingException;
import io.github.edwardUL99.simple.web.exceptions.SocketException;
import io.github.edwardUL99.simple.web.logging.ServerLogger;
import io.github.edwardUL99.simple.web.parsing.DefaultHttpParser;
import io.github.edwardUL99.simple.web.requests.HTTPRequest;
import io.github.edwardUL99.simple.web.requests.handling.RequestDispatcher;
import io.github.edwardUL99.simple.web.requests.response.DefaultResponseGenerator;
import io.github.edwardUL99.simple.web.requests.response.HTTPResponse;
import io.github.edwardUL99.simple.web.sockets.DefaultSocketReceiver;
import io.github.edwardUL99.simple.web.sockets.ReceivedRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static io.github.edwardUL99.simple.web.requests.response.ResponseBuilders.*;

/**
 * A server that automatically configures itself
 */
public class AutoConfigurationServer extends BaseServer {
    private final RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
    private final ServerLogger serverLog = ServerLogger.getLogger();

    public AutoConfigurationServer() {
        super(null, null, null);
        this.initialise();
    }

    private void initialise() {
        Configuration config = Configuration.getGlobalConfiguration();

        if (config == null)
            throw new ConfigurationException("Server is not configured");

        int port = config.getPort();

        receiver = new DefaultSocketReceiver(port);
        parser = new DefaultHttpParser();
        generator = new DefaultResponseGenerator();

        logStart(port);
    }

    private void writeResponse(HTTPResponse response, Socket client) {
        try {
            byte[] html = generator.generate(response);
            OutputStream out = client.getOutputStream();
            out.write(html);
            out.write(new byte[]{'\r', '\n'});
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            serverLog.throwable(ex);
        }
    }

    private void log(HTTPRequest request, HTTPResponse response) {
        serverLog.logAccess(request, response);
    }

    private void logStart(int port) {
        String hostname;

        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            hostname = "<unknown>";
        }

        serverLog.info(String.format("Server started on host %s and port %d", hostname, port));
    }

    private void processRequest(ReceivedRequest received) {
        Socket client = received.getClientSocket();

        try {
            HTTPRequest request = parser.parseHTTP(received);

            try {
                HTTPResponse response = requestDispatcher.dispatch(request);

                writeResponse(response, client);
                log(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                writeResponse(internalServerError(request).build(), client);
                serverLog.throwable(ex);
            }
        } catch (ParsingException ex) {
            writeResponse(badRequest(null).build(), client);
            serverLog.throwable(ex);
        }
    }

    private void run() {
        boolean run = true;

        while (run) {
            try {
                ReceivedRequest received = receiver.receive();

                processRequest(received);
            } catch (SocketException ex) {
                ex.printStackTrace();
                run = !(ex.getCause() instanceof BindException);
                serverLog.throwable(ex);
            } catch (FatalTerminationException ex) {
                ex.printStackTrace();
                run = false;
                serverLog.throwable(ex);
            }
        }
    }

    @Override
    public void listen() {
        run();
    }
}
