package io.github.jamalam360.jamlib.client.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SelectionList extends ContainerObjectSelectionList<SelectionListEntry> {
	public SelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
		super(minecraft, width, height, y, itemHeight);
		this.centerListVertically = false;
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.renderWidget(graphics, mouseX, mouseY, delta);
	}

	@Override
	protected int scrollBarX() {
		return this.width - 7;
	}

	@Override
	public int getRowWidth() {
		return 1000;
	}
}
