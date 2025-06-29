package io.github.jamalam360.jamlib.client.gui;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * A string widget that scrolls if the component is too long for the width
 */
public class ScrollingStringWidget extends StringWidget {
	public ScrollingStringWidget(int x, int y, int width, int height, Component component, Font font) {
		super(x, y, width, height, component, font);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderLeftAlignedScrollingString(guiGraphics, this.getFont(), this.getMessage(), this.getX(), this.getWidth(), this.getY(), this.getColor());
//		if (this.isMouseOver(mouseX, mouseY) && this.tooltip != null) {
//			guiGraphics.renderTooltip(Minecraft.getInstance().font, this.getTooltip().toCharSequence(Minecraft.getInstance()), mouseX, mouseY);
//		}
	}

	private static void renderLeftAlignedScrollingString(GuiGraphics graphics, Font font, Component text, int minX, int width, int y, int color) {
		int messageWidth = font.width(text);

		if (messageWidth > width) {
			int overflowWidth = messageWidth - width;
			double d = (double) Util.getMillis() / (double)1000.0F;
			double e = Math.max((double)overflowWidth * (double)0.5F, 3.0F);
			double f = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d / e)) / (double)2.0F + (double)0.5F;
			double g = Mth.lerp(f, 0.0F, overflowWidth);
			graphics.enableScissor(minX, y, minX + width, y + font.lineHeight);
			graphics.drawString(font, text, minX - (int)g, y, color);
			graphics.disableScissor();
		} else {
			graphics.drawString(font, text, minX, y, color);
		}

	}
}
