package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class BooleanConfigEntry extends ConfigEntry {
	@Nullable
	private Button button = null;

	public BooleanConfigEntry(String modId, String configName, Field field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int width) {
		this.button = Button.builder(this.getComponent(Boolean.TRUE.equals(this.getFieldValue())), button -> {
			this.setFieldValue(!(Boolean.TRUE.equals(this.getFieldValue())));
		}).pos(width - 188, 0).size(150, 20).build();

		return List.of(this.button);
	}

	@Override
	public void onChange() {
		super.onChange();

		if (this.button != null) {
			this.button.setMessage(getComponent(Boolean.TRUE.equals(this.getFieldValue())));
		}
	}

	private Component getComponent(boolean value) {
        return Component.literal(value ? "Yes" : "No").withStyle(s -> s.withColor(value ? ChatFormatting.GREEN : ChatFormatting.RED));
	}
}
