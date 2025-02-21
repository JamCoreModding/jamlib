package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.List;

public class CollectionConfigEntry extends ConfigEntry {
	public CollectionConfigEntry(String modId, String configName, Field field) {
		super(modId, configName, field);
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int width) {
		List<AbstractWidget> buttons = List.of(
				Button.builder(Component.literal("beans"), button -> {
					System.out.println("first");
				}).pos(width - 188, 0).size(150, 20).build(),
				Button.builder(Component.literal("means heinz"), button -> {
					System.out.println("second");
				}).pos(width - 188, 20).size(150, 20).build()
		);

		return buttons;
	}

	@Override
	public void onChange() {
		super.onChange();
	}
}
