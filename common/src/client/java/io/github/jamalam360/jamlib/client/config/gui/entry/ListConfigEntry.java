package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.gui.WidgetList;
import io.github.jamalam360.jamlib.config.ConfigExtensions;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ListConfigEntry<T, E> extends ConfigEntry<T, List<E>> {
	private List<ConfigEntry<T, E>> listMembers;

	public ListConfigEntry(String modId, String configName, ConfigField<T, List<E>> field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int left, int width) {
		this.createListMembers();
		List<AbstractWidget> widgets = new ArrayList<>();
		int currentY = 0;
		int bottom = 0;
		int childWidth = width - 20 - WidgetList.PADDING;

		for (int i = 0; i < this.listMembers.size(); i++) {
			ConfigEntry<T, E> entry = this.listMembers.get(i);
			List<AbstractWidget> entryWidgets = entry.createElementWidgets(left, childWidth);

			for (AbstractWidget widget : entryWidgets) {
				bottom = Math.max(widget.getBottom(), bottom);
				widget.setY(widget.getY() + currentY);
			}

			int finalI = i;
			widgets.add(Button.builder(Component.literal("-"), button -> {
				this.getFieldValue().remove(finalI);
				this.recreateWidgetsNextTick();
				this.onChange();
			}).size(20, 20).pos(left + childWidth + WidgetList.PADDING, currentY).build());
			widgets.addAll(entryWidgets);
			currentY += bottom + WidgetList.PADDING;
		}

		widgets.add(Button.builder(Component.literal("+"), button -> {
			//noinspection unchecked
			this.getFieldValue().add((E) this.getDefaultNewValue());
			this.recreateWidgetsNextTick();
			this.onChange();
		}).size(width, 20).pos(left, currentY).build());
		this.updateValidationIcon();
		return widgets;
	}

	@SuppressWarnings("unchecked")
	private void createListMembers() {
		this.listMembers = new ArrayList<>();
		List<E> list = this.getFieldValue();
		Class<E> elementType = (Class<E>) ((ParameterizedType) this.field.getBackingField().getGenericType()).getActualTypeArguments()[0];
		for (int i = 0; i < list.size(); i++) {
			if (elementType == boolean.class) {
				this.listMembers.add((ConfigEntry<T, E>) new BooleanConfigEntry<>(this.configManager.getModId(), this.configManager.getConfigName(), (ConfigField<T, Boolean>) new ListMemberConfigField<>(this.field.getBackingField(), elementType, i)));
			} else if (elementType == float.class || elementType == double.class || elementType == int.class || elementType == long.class || elementType == Float.class || elementType == Double.class || elementType == Integer.class || elementType == Long.class) {
				this.listMembers.add((ConfigEntry<T, E>) new NumberConfigEntry<>(this.configManager.getModId(), this.configManager.getConfigName(), (ConfigField<T, Number>) new ListMemberConfigField<>(this.field.getBackingField(), elementType, i)));
			} else if (elementType == String.class) {
				this.listMembers.add((ConfigEntry<T, E>) new StringConfigEntry<>(this.configManager.getModId(), this.configManager.getConfigName(), (ConfigField<T, String>) new ListMemberConfigField<>(this.field.getBackingField(), elementType, i)));
			} else if (elementType.isEnum()) {
				this.listMembers.add((ConfigEntry<T, E>) new EnumConfigEntry<>(this.configManager.getModId(), this.configManager.getConfigName(), (ConfigField<T, ? extends Enum>) new ListMemberConfigField<>(this.field.getBackingField(), elementType, i)));
			} else if (Collection.class.isAssignableFrom(elementType)) {
				throw new IllegalArgumentException("Cannot nest collections in config");
			} else {
				throw new IllegalArgumentException("Unsupported config field type " + elementType);
			}
		}
	}

	@Override
	protected void validate() {
		super.validate();
		if (this.configManager.get() instanceof ConfigExtensions<?>) {
			@SuppressWarnings("unchecked") ConfigExtensions<T> ext = (ConfigExtensions<T>) this.configManager.get();

			for (ConfigEntry<T, E> entry : this.listMembers) {
				this.errors.addAll(ext.getValidationErrors(this.configManager, new ConfigExtensions.FieldValidationInfo(entry.field.getName(), entry.getFieldValue(), entry.originalValue, entry.field.getBackingField())));
			}

			this.errors.sort((o1, o2) -> o2.type().ordinal() - o1.type().ordinal());
			this.updateValidationIcon();
		}
	}

	@Override
	public boolean isValid() {
		return super.isValid() && this.listMembers.stream().allMatch(ConfigEntry::isValid);
	}

	@Override
	protected void resetToDefault() {
		super.resetToDefault();
		this.recreateWidgetsNextTick();
	}

	@SuppressWarnings("unchecked")
	private Object getDefaultNewValue() {
		Class<E> c = (Class<E>) ((ParameterizedType) this.field.getBackingField().getGenericType()).getActualTypeArguments()[0];

		if (c == boolean.class) {
			return false;
		} else if (c == float.class || c == double.class || c == int.class || c == long.class || c == Float.class || c == Double.class || c == Integer.class || c == Long.class) {
			return 0;
		} else if (c == String.class) {
			return "";
		} else if (c.isEnum()) {
			return c.getEnumConstants()[0];
		} else if (Collection.class.isAssignableFrom(c)) {
			throw new IllegalArgumentException("Cannot nest collections in config");
		} else {
			throw new IllegalArgumentException("Unsupported config field type " + c);
		}
	}
}
