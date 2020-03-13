package com.github.raybipse.components;

/**
 * An interface that is used to represent a callable operation.
 * 
 * @param <P> the type of the parameter
 * @param <R> the type of the return 
 */
public interface Invocable<P, R> {
    /**
     * Invokes the invocable.
     * @param parameter the parameter
     * @return the return value
     */
    R invoke(P parameter);
}