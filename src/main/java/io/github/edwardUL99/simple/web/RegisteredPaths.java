package io.github.edwardUL99.simple.web;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.edwardUL99.simple.web.handlers.FaviconGetHandler;
import io.github.edwardUL99.simple.web.handlers.StaticGetHandler;
import io.github.edwardUL99.simple.web.requests.RequestMethod;
import io.github.edwardUL99.simple.web.requests.handling.ConfiguredHandlers;
import io.github.edwardUL99.simple.web.requests.handling.RequestDispatcher;
import io.github.edwardUL99.simple.web.requests.handling.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;

/**
 * A utility class which is loaded by {@link io.github.edwardUL99.simple.web.configuration.Configuration} to register
 * all paths inside the static block
 */
public class RegisteredPaths {
    /**
     * The handlers to register the paths to
     */
    private static final ConfiguredHandlers HANDLERS = RequestDispatcher.getInstance().getHandlers();
    private static final Logger log = LoggerFactory.getLogger(RegisteredPaths.class);
    private static final Gson gson = new Gson();

    static {
        configure(); // configures from paths.json on classpath

        register(RequestMethod.GET, "/static/**", new StaticGetHandler()); // for static file path handling
        register(RequestMethod.GET, "/favicon.ico", new FaviconGetHandler()); // for browsers retrieving favicons
    }

    private static void register(RequestMethod method, String path, RequestHandler handler) {
        HANDLERS.register(method, path, handler);
        log.info("Handler {} registered for path {} on method {}", handler.getClass().getName(), path, method);
    }

    private static void instantiate(String path, String className, RequestMethod method) {
        try {
            Class<?> cls = Class.forName(className);
            Constructor<?> constructor = cls.getDeclaredConstructor();
            register(method, path, (RequestHandler) constructor.newInstance());
        } catch (ClassNotFoundException ex) {
            log.error("Can't find handler " + className, ex);
        } catch (NoSuchMethodException ex) {
            log.error("Can't instantiate handler " + className + " as it does not have a no-arg constructor", ex);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            log.error("Failed to instantiate handler " + className, ex);
        }
    }

    private static void configure() {
        URL url = RegisteredPaths.class.getClassLoader().getResource("paths.json");

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
                ex.printStackTrace();
                log.error("Failed to read configuration from paths.json with error: " + ex);
            }
        } else {
            log.info("No paths.json found on class path");
        }
    }
}
