package io.github.jamalam360.jamlib.registry.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jamalam
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ContentRegistry {
    String value();
}
