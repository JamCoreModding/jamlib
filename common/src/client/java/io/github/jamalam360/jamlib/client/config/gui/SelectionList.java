package io.github.jamalam360.jamlib.client.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class SelectionList extends ContainerObjectSelectionList<SelectionListEntry> {
    public SelectionList(Minecraft minecraft, int width, int height, int itemHeight) {
        super(minecraft, width, height, itemHeight, height - 32, itemHeight);
        this.centerListVertically = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        SelectionListEntry hovered = this.getHoveredEntry(mouseX, mouseY);

        if (hovered != null) {
            if (hovered.getTooltip() != null) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hovered.getTooltip(), mouseX, mouseY);
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
            if (widget instanceof AbstractWidget abstractWidget && widget.isMouseOver(mouseX, mouseY)) {
                if (abstractWidget.getTooltip() != null) {
                    anyWidgetsHovered = true;
                }
                break;
            }
        }

        return anyWidgetsHovered ? null : entry;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15 + 20;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }
}
