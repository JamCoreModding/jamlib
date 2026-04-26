package io.github.jamalam360.jamlib.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a config field will cause it to not show in the config screen.
 *
 * @see ConfigManager
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HiddenInGui {
}
