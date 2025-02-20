package io.github.jamalam360.jamlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a number (float, double, int, long) config field will force it to be within this range.
 *
 * @see RequiresRestart
 * @see MatchesRegex
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithinRange {

    double min();

    double max();
}
