package io.github.edwardUL99.simple.web;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.edwardUL99.inject.lite.exceptions.InjectionException;
import io.github.edwardUL99.inject.lite.injector.Injector;
import io.github.edwardUL99.simple.web.configuration.Configuration;
import io.github.edwardUL99.simple.web.logging.ServerLogger;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.handling.ConfiguredHandlers;
import io.github.edwardUL99.simple.web.requests.handling.RequestDispatcher;
import io.github.edwardUL99.simple.web.requests.handling.RequestHandler;
import io.github.edwardUL99.simple.web.server.Server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * A utility class which is loaded by {@link io.github.edwardUL99.simple.web.configuration.Configuration} to register
 * all paths inside the static block
 */
public class RegisteredHandlers {
    /**
     * The handlers to register the paths to
     */
    private static final ConfiguredHandlers HANDLERS = RequestDispatcher.getInstance().getHandlers();
    private static final ServerLogger log = ServerLogger.getLogger();
    private static final Gson gson = new Gson();
    private static final Injector injector = Injector.get();

    private static boolean isServerStarted() {
        Server server = SimpleWebServer.getServerInstance();

        return server != null && server.isStarted();
    }

    public static void register(RequestMethod method, String path, RequestHandler handler) {
        if (isServerStarted())
            throw new IllegalStateException("Cannot register a handler after the server has been started");

        HANDLERS.register(method, path, handler);
        log.debug(String.format("Handler %s registered for path %s on method %s", handler.getClass().getName(), path, method));
    }

    private static void instantiate(String path, String className, RequestMethod method) {
        try {
            Class<?> cls = Class.forName(className);
            register(method, path, (RequestHandler) injector.inject(cls));
        } catch (ClassNotFoundException ex) {
            log.error("Can't find handler " + className);
            log.throwable(ex);
        } catch (InjectionException ex) {
            log.throwable(ex);
        }
    }

    public static void configure() throws IOException {
        URL url = RegisteredHandlers.class.getClassLoader()
                .getResource(Configuration.getGlobalConfiguration().getPathsFile());

        if (url != null) {
            try {
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonObject json = gson.fromJson(reader, JsonObject.class).getAsJsonObject("paths");

                if (json == null) {
                    log.error("Invalid paths.json file so ignoring...");
                } else {
                    for (Map.Entry<String, JsonElement> e : json.entrySet()) {
                        String key = e.getKey();
                        RequestMethod method = RequestMethod.valueOf(key);

                        for (Map.Entry<String, JsonElement> e1 : e.getValue().getAsJsonObject().entrySet()) {
                            String path = e1.getKey();
                            String className = e1.getValue().getAsString();

                            instantiate(path, className, method);
                        }
                    }
                }
            } catch (IOException ex) {
                log.error("Failed to read configuration from paths.json with error");
                log.throwable(ex);

                throw ex;
            }
        } else {
            log.info("No paths.json found on class path");
        }
    }
}
