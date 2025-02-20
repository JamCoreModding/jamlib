package io.github.jamalam360.jamlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a config field will warn the user that they must restart the game before the config change will take effect.
 *
 * @see WithinRange
 * @see MatchesRegex
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRestart {
}
