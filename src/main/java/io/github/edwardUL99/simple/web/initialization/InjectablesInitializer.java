package io.github.edwardUL99.simple.web.initialization;

import io.github.edwardUL99.simple.web.injection.Injection;

/**
 * This initializer initialises the injectable resources. Must be executed before resources are injected
 */
public class InjectablesInitializer implements WebServerInitializer {
    @Override
    public InitializationResult initialize() {
        try {
            Injection.registerInjectables();

            return new InitializationResult(true, null);
        } catch (Exception ex) {
            return new InitializationResult(false, ex);
        }
    }
}
