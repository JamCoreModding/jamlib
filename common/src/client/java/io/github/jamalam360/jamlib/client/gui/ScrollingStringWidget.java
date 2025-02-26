package io.github.jamalam360.jamlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

/**
 * A string widget that scrolls if the component is too long for the width
 */
public class ScrollingStringWidget extends StringWidget {
	public ScrollingStringWidget(int x, int y, int width, int height, Component component, Font font) {
		super(x, y, width, height, component, font);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderScrollingString(guiGraphics, this.getFont(), 2, this.getColor());

		if (this.isMouseOver(mouseX, mouseY) && this.getTooltip() != null) {
			guiGraphics.renderTooltip(Minecraft.getInstance().font, this.getTooltip().toCharSequence(Minecraft.getInstance()), mouseX, mouseY);
		}
	}
}
