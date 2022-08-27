package io.github.jamalam360.jamlib.config.v2;

import java.lang.reflect.Field;

/**
 * Represents a field in a config class.
 *
 * @author Jamalam
 */
public class ConfigEntry {
    private final Field field;
    private final Object defaultValue;
    private Object value;

    public ConfigEntry(Field field, Object value, Object defaultValue) {
        this.field = field;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public Field getField() {
        return this.field;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }
}
