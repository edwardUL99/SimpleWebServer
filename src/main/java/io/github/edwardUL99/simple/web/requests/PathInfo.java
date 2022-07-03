package io.github.edwardUL99.simple.web.requests;

import java.util.Map;

/**
 * This class represents the path passed by the request and any parameters passed in
 */
public class PathInfo {
    private final String path;
    private final Map<String, String> params;

    public PathInfo(String path, Map<String, String> params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
