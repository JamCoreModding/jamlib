package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.client.config.gui.ConfigScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnumConfigEntry<T, V extends Enum<V>> extends ConfigEntry<T, V> {
	@Nullable
	private EnumButton<V> button = null;

	public EnumConfigEntry(String modId, String configName, ConfigField<T, V> field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int left, int width) {
		//noinspection unchecked
		this.button = new EnumButton<>(
				left,
				0,
				width,
				20,
				CommonComponents.EMPTY.copy(),
				(Class<Enum<?>>) this.field.getElementType(),
				(b) -> this.setFieldValue(b.getValue())
		);
		this.button.setValue(this.getFieldValue());
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
		String translationKey = ConfigScreen.createTranslationKey(this.configManager.getModId(), this.configManager.getConfigName(), field.getName() + "." + this.getFieldValue().name().toLowerCase());

		if (I18n.exists(translationKey)) {
			return Component.translatable(translationKey);
		} else {
			return Component.literal(this.getFieldValue().name());
		}
	}
}
