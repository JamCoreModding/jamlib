package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ListMemberConfigField<T, V> implements ConfigField<T, V> {
	private final Field listField;
	private final Class<V> elementClass;
	private final int index;

	public ListMemberConfigField(Field listField, Class<V> elementClass, int index) {
		this.listField = listField;
		this.elementClass = elementClass;
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getValue(ConfigManager<T> manager) {
		try {
			List<V> list = (List<V>) this.listField.get(manager.get());
			return this.index >= 0 && this.index < list.size() ? list.get(this.index) : null;
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(ConfigManager<T> manager, V value) {
		try {
			List<V> list = (List<V>) this.listField.get(manager.get());
			list.set(this.index, value);
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
		}
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return this.listField.isAnnotationPresent(annotationClass);
	}

	@Override
	public <T1 extends Annotation> T1 getAnnotation(Class<T1> annotationClass) {
		return this.listField.getAnnotation(annotationClass);
	}

	@Override
	public Class<V> getElementType() {
		return this.elementClass;
	}

	@Override
	public String getName() {
		return this.listField.getName() + "." + this.index;
	}

	@Override
	public Field getBackingField() {
		return this.listField;
	}
}
