package io.github.jamalam360.jamlib.client.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a number (float, double, int, long) config field will render a slider instead of a text box. The field must also have the {@link WithinRange}
 * annotation.
 *
 * @see RequiresRestart
 * @see MatchesRegex
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Slider {
}
