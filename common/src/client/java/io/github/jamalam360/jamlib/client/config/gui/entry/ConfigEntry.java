package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.config.gui.ConfigScreen;
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

public abstract class ConfigEntry {
	protected final Field field;
	protected final ConfigManager<?> configManager;
	private final Object originalValue;
	private final String translationKey;
	@Nullable
	private final List<FormattedCharSequence> tooltip;
	private boolean valid = true;
	private ImageWidget validationIcon;

	public static ConfigEntry createFromField(String modId, String configName, Field field) {
		Class<?> c = field.getType();

		if (c == boolean.class) {
			return new BooleanConfigEntry(modId, configName, field);
		} else if (c == float.class || c == double.class || c == int.class || c == long.class) {
			return new NumberConfigEntry(modId, configName, field);
		} else if (c == String.class) {
			return new StringConfigEntry(modId, configName, field);
		} else if (c.isEnum()) {
			return new EnumConfigEntry<>(modId, configName, field);
		} else if (Collection.class.isAssignableFrom(c)) {
			return new CollectionConfigEntry(modId, configName, field);
		} else {
			throw new IllegalArgumentException("Unsupported config field type " + c);
		}
	}

	public ConfigEntry(String modId, String configName, Field field) {
		this.field = field;
		this.configManager = ConfigManager.MANAGERS.get(configName);
		this.originalValue = this.getFieldValue();
		this.translationKey = ConfigScreen.createTranslationKey(modId, configName, field.getName());

		if (I18n.exists(this.translationKey + ".tooltip")) {
			this.tooltip = Minecraft.getInstance().font.split(Component.translatable(this.translationKey + ".tooltip"), 200);
		} else {
			this.tooltip = null;
		}
	}

	public List<AbstractWidget> createWidgets(int width) {
		List<AbstractWidget> widgets = new ArrayList<>();

		widgets.add(new FittingMultiLineTextWidget(12, Minecraft.getInstance().font.lineHeight / 2 + 1, width / 2 - 10, Minecraft.getInstance().font.lineHeight, Component.translatable(this.translationKey), Minecraft.getInstance().font));

		this.validationIcon = ImageWidget.sprite(20, 20, JamLib.id("validation_warning"));
		this.validationIcon.setX(width - 212);
		this.validationIcon.setY(0);
		this.validationIcon.setTooltip(Tooltip.create(Component.translatable("config.jamlib.requires_restart_tooltip")));
		this.validationIcon.visible = false;
		widgets.add(this.validationIcon);

		widgets.addAll(this.createElementWidgets(width));

		SpriteIconButton resetButton = SpriteIconButton.builder(Component.translatable("config.jamlib.reset"), (button) -> {
			this.setFieldValue(this.originalValue);

			if (widgets.get(1) instanceof EditBox box) {
				box.setValue(String.valueOf(this.originalValue));
			} else if (widgets.get(1) instanceof SliderButton slider) {
				slider.setValue((Double) this.originalValue);
			}
		}, true).sprite(JamLib.id("reset"), 16, 16).size(20, 20).build();
		resetButton.setX(width - 30);
		resetButton.setY(0);
		widgets.add(resetButton);

		return widgets;
	}

	public abstract List<AbstractWidget> createElementWidgets(int width);

	public void onChange() {
		this.validate();
	}

	private void validate() {
		Object newValue = this.getFieldValue();

		if (this.configManager.get() instanceof ConfigExtensions<?> ext) {
			@SuppressWarnings("unchecked") List<ConfigExtensions.ValidationError> errors = ((ConfigExtensions<Object>) ext).getValidationErrors((ConfigManager<Object>) this.configManager, new ConfigExtensions.FieldValidationInfo(this.field.getName(), newValue, this.originalValue, this.field));
			errors.sort((o1, o2) -> o2.type().ordinal() - o1.type().ordinal());

			if (!errors.isEmpty()) {
				this.valid = errors.getFirst().type() != ConfigExtensions.ValidationError.Type.ERROR;
				this.validationIcon.visible = true;

				((MutableSpriteImageWidget$Sprite) this.validationIcon).setSprite(errors.getFirst().type().getTexture());
				this.validationIcon.setTooltip(Tooltip.create(errors.getFirst().message()));
			} else {
				this.valid = true;
				this.validationIcon.visible = false;
			}
		}
	}

	public boolean hasChanged() {
		return this.getFieldValue().equals(this.originalValue);
	}

	public boolean isValid() {
		return this.valid;
	}

	public Component getName() {
		return Component.translatable(this.translationKey);
	}

	public @Nullable List<FormattedCharSequence> getTooltip() {
		return this.tooltip;
	}

	protected Object getFieldValue() {
		try {
			return this.field.get(this.configManager.get());
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", this.configManager.getConfigName(), e);
			return null;
		}
	}

	protected void setFieldValue(Object v) {
		Object realValue = v;

		if (v instanceof Number n) {
			Class<?> c = this.field.getType();

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

		try {
			this.field.set(this.configManager.get(), realValue);
			this.onChange();
		} catch (IllegalAccessException e) {
			JamLib.LOGGER.error("Failed to access field for config {}", this.configManager.getConfigName(), e);
		}
	}
}
