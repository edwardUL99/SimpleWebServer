package io.github.edwardUL99.simple.web.initialization;

import java.util.ArrayList;
import java.util.List;

/**
 * This class reads all the registered initializers and calls initialize
 */
public class WebServerInitializers {
    private static final List<WebServerInitializer> initializers = new ArrayList<>();

    static {
        // we need to initialize resources before controllers
        initializers.add(new InjectablesInitializer());
        initializers.add(new RequestHandlersInitializer());
        initializers.add(new InterceptorsInitializer());
    }

    /**
     * Add the initializer to the class
     * @param initializer the initializer to add
     */
    public static void addInitializer(WebServerInitializer initializer) {
        initializers.add(initializer);
    }

    /**
     * Goes through the list of initializers and call initialize, analyzing the result. If the result is false, the server
     * exits, printing the stacktrace of the result throwable if it exists
     */
    public static void initialize() {
        for (WebServerInitializer initializer : initializers) {
            InitializationResult result = initializer.initialize();

            if (!result.isContinueInitialization()) {
                Throwable cause = result.getInitializationFailure();

                if (cause != null) {
                    cause.printStackTrace();
                    System.exit(2);
                } else {
                    System.exit(1);
                }
            }
        }
    }
}
