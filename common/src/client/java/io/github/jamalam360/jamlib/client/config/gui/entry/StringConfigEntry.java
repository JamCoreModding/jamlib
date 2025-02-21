package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class StringConfigEntry extends ConfigEntry {
	@Nullable
	private EditBox editBox = null;

	public StringConfigEntry(String modId, String configName, Field field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int width) {
		this.editBox = new EditBox(
				Minecraft.getInstance().font,
				width - 188,
				0,
				150,
				20,
				CommonComponents.EMPTY
		);
		this.editBox.setValue(this.getFieldValue().toString());
		this.editBox.setResponder(this::setFieldValue);


		return List.of(this.editBox);
	}

	@Override
	public void onChange() {
		super.onChange();

		if (this.editBox != null) {
			this.editBox.setMessage(Component.literal((String) this.getFieldValue()));
		}
	}
}
