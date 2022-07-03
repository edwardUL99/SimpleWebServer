package io.github.edwardUL99.simple.web.requests;

/**
 * Represents a HTTP Status
 */
public class HttpStatus {
    public static final HttpStatus OK = new HttpStatus(200, "OK");
    public static final HttpStatus BAD_REQUEST = new HttpStatus(400, "Bad Request");
    public static final HttpStatus NOT_FOUND = new HttpStatus(404, "Not Found");
    public static final HttpStatus INTERNAL_SERVER_ERROR = new HttpStatus(500, "Internal Server Error");
    public static final HttpStatus SERVICE_UNAVAILABLE = new HttpStatus(503, "Service Unavailable");

    private final int code;
    private final String name;

    private HttpStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
