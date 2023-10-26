package io.github.jamalam360.jamlib.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class SelectionList extends ContainerObjectSelectionList<SelectionListEntry> {
	public SelectionList(Minecraft minecraft, int i, int j, int k, int l, int m) {
		super(minecraft, i, j, k, l, m);
		this.centerListVertically = false;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		SelectionListEntry hovered = this.getHoveredEntry(mouseX, mouseY);

		if (hovered != null) {
			if (hovered.getTooltip() != null) {
				graphics.renderTooltip(Minecraft.getInstance().font, hovered.getTooltip(), mouseX, mouseY);
			}
		}
	}

	@Nullable
	private SelectionListEntry getHoveredEntry(int mouseX, int mouseY) {
		SelectionListEntry entry = this.getEntryAtPosition(mouseX, mouseY);

		if (entry == null) {
			return null;
		}

		boolean anyWidgetsHovered = false;
		for (GuiEventListener widget : entry.children()) {
			if (widget instanceof AbstractWidget && widget.isMouseOver(mouseX, mouseY)) {
				anyWidgetsHovered = true;
				break;
			}
		}

		return anyWidgetsHovered ? null : entry;
	}

	@Override
	protected int getScrollbarPosition() {
		return this.width - 7;
	}

	@Override
	public int getRowWidth() {
		return 1000;
	}
}
