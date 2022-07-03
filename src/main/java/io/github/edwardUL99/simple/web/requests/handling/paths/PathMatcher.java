package io.github.edwardUL99.simple.web.requests.handling.paths;

import io.github.azagniotov.matcher.AntPathMatcher;

/**
 * This class is used to match paths
 */
public class PathMatcher {
    private final AntPathMatcher matcher;

    public PathMatcher() {
        matcher = new AntPathMatcher.Builder().withPathSeparator('/').build();
    }

    public boolean matches(String path, String pattern) {
        return matcher.isMatch(pattern, path);
    }
}
