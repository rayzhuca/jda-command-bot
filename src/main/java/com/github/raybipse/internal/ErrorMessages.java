package com.github.raybipse.internal;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * ErrorMessages class is responsible for throwing errors inside the
 * jda-command-bot framework.
 * 
 * @author RayBipse
 */
public class ErrorMessages {

    /**
     * BotConfiguration cannot be instantized because it is a utility class.
     */
    private ErrorMessages() {
    }

    
    /**
     * Returns {@code obj} if it is not {@code null}. Otherwise, throw a {@link NullPointerException}.
     * 
     * @param <T> the type of {@code obj}
     * @param obj the obj to be checked for nullity
     * @param msg the message of the {@link NullPointerException}
     * 
     * @return {@code obj}
]     */
    public static <T> T requireNonNull(T obj, String msg) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(msg);
    }

    /**
     * Returns {@code obj} if it is not {code null}. Otherwise, throw a {@link NullPointerException}.
     * 
     * @param <T>       the type of {@code obj}
     * @param obj       the obj to be checked for nullity
     * @param paramName the name of the {@code obj}
     * 
     * @return {@code obj}
     */
    public static <T> T requireNonNullParam(T obj, String paramName) {
        if (obj != null) {
            return obj;
        }
        throw new InvalidParameterException("Parameter \"" + paramName + "\" should not be null.");
    }

    /**
     * Returns {@code obj} if it is not {code null}. Otherwise, throw a {@link NullPointerException}.
     * 
     * <pre>
     * requireNonNullReturn(method(), "method");
     * </pre>
     * 
     * @param <T>        the type of {@code obj}
     * @param obj        the obj to be checked for nullity
     * @param methodName the name of the {@code obj}
     * 
     * @return {@code obj}
     */
    public static <T> T requireNonNullReturn(T obj, String methodName) {
        return Objects.requireNonNull(obj, "Method \"" + methodName + "\" should not return null.");
    }
}