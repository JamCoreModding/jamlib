package io.github.jamalam360.jamlib.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class SpriteButton extends Button {
	private final int textureWidth;
	private final int textureHeight;
	private ResourceLocation texture;

	public SpriteButton(int x, int y, int width, int height, MutableComponent description, ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress) {
		super(x, y, width, height, CommonComponents.EMPTY, onPress, s -> description);
		this.setTooltip(Tooltip.create(description));
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		super.renderWidget(guiGraphics, i, j, f);

		int x = this.getX() + (this.width - this.textureWidth) / 2;
		int y = this.getY() + (this.height - this.textureHeight) / 2;

		guiGraphics.blit(this.texture, x, y, this.textureWidth, this.textureHeight, 0.0F, 0.0F, this.textureWidth, this.textureHeight, this.textureWidth, this.textureHeight);
	}

	public void setTexture(ResourceLocation location) {
		this.texture = location;
	}
}
