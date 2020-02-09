package com.github.raybipse.internal;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * ErrorMessages class is responsible for throwing errors inside the
 * java-command-bot framework.
 * 
 * @author RayBipse
 */
public class ErrorMessages {

    private ErrorMessages() {
    }

    /**
     * Returns {@code obj} if it is not null. Throws a {@link NullPointerException}
     * otherwise.
     * 
     * @param <T>       the type of {@code obj}
     * @param obj       the obj to be checked for nullity
     * @param paramName the name of the {@code obj}
     * @return {@code obj}
     */
    public static <T> T requireNonNullParam(T obj, String paramName) {
        if (obj != null) {
            return obj;
        }
        throw new InvalidParameterException("Parameter \"" + paramName + "\" should not be null.");
    }

    /**
     * Returns {@code obj} if it is not null. Throws a {@link NullPointerException}
     * otherwise.
     * 
     * <pre>
     * requireNonNullReturn(method(), "method");
     * </pre>
     * 
     * @param <T>        the type of {@code obj}
     * @param obj        the obj to be checked for nullity
     * @param methodName the name of the {@code obj}
     * @return {@code obj}
     */
    public static <T> T requireNonNullReturn(T obj, String methodName) {
        return Objects.requireNonNull(obj, "Method \"" + methodName + "\" should not return null.");
    }
}