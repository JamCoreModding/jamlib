package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.client.config.gui.ConfigScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class EnumConfigEntry<E extends Enum<E>> extends ConfigEntry {
	@Nullable
	private EnumButton<E> button = null;

	public EnumConfigEntry(String modId, String configName, Field field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int width) {
		//noinspection unchecked
		this.button = new EnumButton<>(
				width - 188,
				0,
				150,
				20,
				CommonComponents.EMPTY.copy(),
				(Class<Enum<?>>) this.field.getType(),
				(b) -> this.setFieldValue(b.getValue())
		);
		this.button.setValue(this.getEnumValue());
		this.button.setMessage(this.getComponent());

		return List.of(this.button);
	}

	@Override
	public void onChange() {
		super.onChange();

		if (this.button != null) {
			this.button.setMessage(this.getComponent());
		}
	}

	private Component getComponent() {
		String translationKey = ConfigScreen.createTranslationKey(this.configManager.getModId(), this.configManager.getConfigName(), field.getName() + "." + this.getEnumValue().name().toLowerCase());

		if (I18n.exists(translationKey)) {
			return Component.translatable(translationKey);
		} else {
			return Component.literal(this.getEnumValue().name());
		}
	}

	private E getEnumValue() {
		//noinspection unchecked
		return (E) this.getFieldValue();
	}
}
