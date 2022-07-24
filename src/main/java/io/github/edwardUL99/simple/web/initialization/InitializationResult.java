package io.github.edwardUL99.simple.web.initialization;

/**
 * This class represents a result of a web server initializer. It determines if the server should quit or continue initialization
 */
public class InitializationResult {
    /**
     * If true, initialization should continue
     */
    private final boolean continueInitialization;
    /**
     * A throwable that may have caused the initialization failure
     */
    private final Throwable initializationFailure;

    /**
     * The result of the initialization step
     * @param continueInitialization true to continue initialization, false to stop and quit the server
     * @param initializationFailure if caused by an exception, pass it into this argument
     */
    public InitializationResult(boolean continueInitialization, Throwable initializationFailure) {
        this.continueInitialization = continueInitialization;
        this.initializationFailure = initializationFailure;
    }

    /**
     * Determines if initialization should be continued or not
     * @return true to continue initialization, false to stop and quit the server
     */
    public boolean isContinueInitialization() {
        return continueInitialization;
    }

    /**
     * Gets the throwable that caused the initialization failure, if any
     * @return throwable that caused initialization to fail
     */
    public Throwable getInitializationFailure() {
        return initializationFailure;
    }
}
