package com.github.raybipse.internal;

import java.util.Objects;

/**
 * ErrorMessages class is responsible for throwing errors inside the java-command-bot framework.
 */
public class ErrorMessages {

    private ErrorMessages() {}

    public static <T> T requireNonNullParam(T obj, String paramName) {
        return Objects.requireNonNull(obj, "Parameter \"" + paramName + "\" should not be null.");
    }

    public static <T> T requireNonNullReturn(T obj, String methodName) {
        return Objects.requireNonNull(obj, "Method \"" + methodName + "\" should not return null.");
    }
}