package com.github.raybipse.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Nullable is a marker interface that signifies that the field or the method's return value can be {@code null}.
 * 
 * @author RayBipse
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
public @interface Nullable {
    
}