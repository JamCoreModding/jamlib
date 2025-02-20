package io.github.jamalam360.jamlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applying this to a config field will cause it to not show in the {@link io.github.jamalam360.jamlib.client.config.gui.ConfigScreen}.
 *
 * @see ConfigManager
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HiddenInGui {
}
