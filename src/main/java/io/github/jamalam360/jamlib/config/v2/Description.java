package io.github.jamalam360.jamlib.config.v2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to add a description a config field.
 *
 * @author Jamalam
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
