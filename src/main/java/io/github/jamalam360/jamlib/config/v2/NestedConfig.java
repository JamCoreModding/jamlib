package io.github.jamalam360.jamlib.config.v2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jamalam
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NestedConfig {
    /**
     * @return The mod ID of the mod this config is for.
     */
    String value();
}
