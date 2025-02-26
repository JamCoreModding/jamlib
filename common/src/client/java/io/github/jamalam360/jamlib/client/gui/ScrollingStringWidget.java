package io.github.jamalam360.jamlib.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class ScrollingStringWidget extends StringWidget {
	public ScrollingStringWidget(int x, int y, int width, int height, Component component, Font font) {
		super(x, y, width, height, component, font);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderScrollingString(guiGraphics, this.getFont(), 2, this.getColor());
	}
}
