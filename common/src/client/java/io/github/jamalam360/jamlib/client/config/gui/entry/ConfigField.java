package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.config.ConfigManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface ConfigField<T, V> {
	V getValue(ConfigManager<T> manager);
	void setValue(ConfigManager<T> manager, V value);
	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);
	<A extends Annotation> A getAnnotation(Class<A> annotationClass);
	Class<V> getElementType();
	String getName();
	Field getBackingField();
}
