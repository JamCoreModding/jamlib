package io.github.jamalam360.jamlib.client.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a config field will force a field to match a valid regex.
 *
 * @see WithinRange
 * @see RequiresRestart
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesRegex {

    String value();
}
