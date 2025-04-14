package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StringConfigEntry<T> extends ConfigEntry<T, String> {
	@Nullable
	private EditBox editBox = null;

	public StringConfigEntry(String modId, String configName, ConfigField<T, String> field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int left, int width) {
		this.editBox = new EditBox(
				Minecraft.getInstance().font,
				left,
				0,
				width,
				20,
				CommonComponents.EMPTY
		);
		this.editBox.setValue(this.getFieldValue());
		this.editBox.setResponder(this::setFieldValue);

		return List.of(this.editBox);
	}

	@Override
	public void resetToDefault() {
		super.resetToDefault();

		if (this.editBox != null) {
			this.editBox.setValue(this.getFieldValue());
		}
	}
}
