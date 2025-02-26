package io.github.jamalam360.jamlib.client.config.gui.entry;

import com.google.gson.Gson;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.client.gui.ScrollingStringWidget;
import io.github.jamalam360.jamlib.client.mixinsupport.MutableSpriteImageWidget$Sprite;
import io.github.jamalam360.jamlib.config.ConfigExtensions;
import io.github.jamalam360.jamlib.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ConfigEntry<T, V> {
	private static final Gson GSON = new Gson();
	protected final ConfigField<T, V> field;
	protected final ConfigManager<T> configManager;
	protected final V originalValue;
	private final String translationKey;
	@Nullable
	private final Component tooltip;
	protected ImageWidget validationIcon;
	@Nullable
	protected List<ConfigExtensions.ValidationError> errors;
	private boolean recreateWidgetsNextTick = false;

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T, V> ConfigEntry<T, V> createFromField(String modId, String configName, Field field) {
		Class<?> c = field.getType();

		if (c == boolean.class) {
			return new BooleanConfigEntry<>(modId, configName, new FieldConfigField(field));
		} else if (c == float.class || c == double.class || c == int.class || c == long.class) {
			return new NumberConfigEntry<>(modId, configName, new FieldConfigField(field));
		} else if (c == String.class) {
			return new StringConfigEntry<>(modId, configName, new FieldConfigField(field));
		} else if (c.isEnum()) {
			return new EnumConfigEntry<>(modId, configName, new FieldConfigField(field));
		} else if (Collection.class.isAssignableFrom(c)) {
			return new ListConfigEntry<>(modId, configName, new FieldConfigField(field));
		} else {
			throw new IllegalArgumentException("Unsupported config field type " + c);
		}
	}

	public ConfigEntry(String modId, String configName, ConfigField<T, V> field) {
		this.field = field;
		//noinspection unchecked
		this.configManager = (ConfigManager<T>) ConfigManager.MANAGERS.get(new ConfigManager.Key(modId, configName));
		this.originalValue = this.cloneObject(this.getFieldValue());
		this.translationKey = ConfigScreen.createTranslationKey(modId, configName, field.getName());

		if (I18n.exists(this.translationKey + ".tooltip")) {
			this.tooltip = Component.translatable(this.translationKey + ".tooltip");
		} else {
			this.tooltip = null;
		}
	}

	public List<AbstractWidget> createWidgets(int width) {
		List<AbstractWidget> widgets = new ArrayList<>();

		ScrollingStringWidget title = new ScrollingStringWidget(12, Minecraft.getInstance().font.lineHeight / 2 + 1, width / 2 - 10, Minecraft.getInstance().font.lineHeight, Component.translatable(this.translationKey), Minecraft.getInstance().font);

		if (this.tooltip != null) {
			title.setTooltip(Tooltip.create(this.tooltip));
		}

		widgets.add(title);

		this.validationIcon = ImageWidget.sprite(20, 20, JamLib.id("validation_warning"));
		this.validationIcon.setX(width - 212);
		this.validationIcon.setY(0);
		this.validationIcon.setTooltip(Tooltip.create(Component.translatable("config.jamlib.requires_restart_tooltip")));
		this.validationIcon.visible = false;
		widgets.add(this.validationIcon);

		widgets.addAll(this.createElementWidgets(width - 188, 150));

		SpriteIconButton resetButton = SpriteIconButton.builder(Component.translatable("config.jamlib.reset"), (button) -> this.setFieldValue(this.originalValue), true).sprite(JamLib.id("reset"), 16, 16).size(20, 20).build();
		resetButton.setX(width - 30);
		resetButton.setY(0);
		widgets.add(resetButton);

		return widgets;
	}

	public abstract List<AbstractWidget> createElementWidgets(int left, int width);

	public void onChange() {
		this.validate();
	}

	protected void validate() {
		V newValue = this.getFieldValue();

		if (this.configManager.get() instanceof ConfigExtensions<?>) {
			@SuppressWarnings("unchecked") ConfigExtensions<T> ext = (ConfigExtensions<T>) this.configManager.get();
			this.errors = ext.getValidationErrors(this.configManager, new ConfigExtensions.FieldValidationInfo(this.field.getName(), newValue, this.originalValue, this.field.getBackingField()));
			this.errors.sort((o1, o2) -> o2.type().ordinal() - o1.type().ordinal());
			this.updateValidationIcon();
		}
	}

	protected void updateValidationIcon() {
		if (this.validationIcon != null) {
			if (this.isValid()) {
				this.validationIcon.visible = false;
			} else {
				this.validationIcon.visible = true;
				((MutableSpriteImageWidget$Sprite) this.validationIcon).setSprite(this.errors.getFirst().type().getTexture());
				this.validationIcon.setTooltip(Tooltip.create(this.errors.getFirst().message()));
			}
		}
	}

	@Nullable
	public List<AbstractWidget> getNewWidgets(int width) {
		if (this.recreateWidgetsNextTick) {
			this.recreateWidgetsNextTick = false;
			return this.createWidgets(width);
		} else {
			return null;
		}
	}

	public void recreateWidgetsNextTick() {
		this.recreateWidgetsNextTick = true;
	}

	public boolean hasChanged() {
		return this.getFieldValue().equals(this.originalValue);
	}

	public boolean isValid() {
		return this.errors == null || this.errors.stream().noneMatch(e -> e.type() == ConfigExtensions.ValidationError.Type.ERROR);
	}

	public Component getName() {
		return Component.translatable(this.translationKey);
	}

	protected V getFieldValue() {
		return this.field.getValue(this.configManager);
	}

	protected void setFieldValue(V v) {
		Object realValue = v;

		if (v instanceof Number n) {
			Class<?> c = this.field.getElementType();

			if (c == double.class || c == Double.class) {
				realValue = n.doubleValue();
			} else if (c == float.class || c == Float.class) {
				realValue = n.floatValue();
			} else if (c == int.class || c == Integer.class) {
				realValue = n.intValue();
			} else if (c == long.class || c == Long.class) {
				realValue = n.longValue();
			}
		}

		//noinspection unchecked
		this.field.setValue(this.configManager, (V) realValue);
		this.onChange();
	}

	private V cloneObject(V object) {
		if (object == null) {
			return null;
		}

		//noinspection unchecked
		return (V) GSON.fromJson(GSON.toJson(object), object.getClass());
	}
}
