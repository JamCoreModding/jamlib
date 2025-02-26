package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldConfigField<T, V> implements ConfigField<T, V> {
	private final Field field;

	public FieldConfigField(Field field) {
		this.field = field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getValue(ConfigManager<T> manager) {
		try {
			return (V) this.field.get(manager.get());
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
			return null;
		}
	}

	@Override
	public void setValue(ConfigManager<T> manager, V value) {
		try {
			this.field.set(manager.get(), value);
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
		}
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return this.field.isAnnotationPresent(annotationClass);
	}

	@Override
	public <T1 extends Annotation> T1 getAnnotation(Class<T1> annotationClass) {
		return this.field.getAnnotation(annotationClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<V> getElementType() {
		return (Class<V>) this.field.getType();
	}

	@Override
	public String getName() {
		return this.field.getName();
	}

	@Override
	public Field getBackingField() {
		return this.field;
	}
}
