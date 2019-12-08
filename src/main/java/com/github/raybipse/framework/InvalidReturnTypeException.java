package com.github.raybipse.framework;

/**
 * Thrown when an implemented method returns an unexpected value. E.g., when
 * {@link com.github.raybipse.framework.Command#getPrefix() getPrefix()} returns
 * null.
 * 
 * @author RayBipse
 */
public class InvalidReturnTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidReturnTypeException(String message) {
        super(message);
    }
}