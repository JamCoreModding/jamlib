package io.github.jamalam360.jamlib.config.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class SelectionListEntry extends ContainerObjectSelectionList.Entry<SelectionListEntry> {

    private final Component title;
    private final List<FormattedCharSequence> tooltip;
    private final List<AbstractWidget> widgets;

    public SelectionListEntry(Component title, List<FormattedCharSequence> tooltip, List<AbstractWidget> widgets) {
        this.title = title;
        this.tooltip = tooltip;
        this.widgets = widgets;
    }

    @Override
    public void render(GuiGraphics graphics, int i, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        for (AbstractWidget widget : this.widgets) {
            widget.setY(y);
            widget.render(graphics, mouseX, mouseY, delta);
        }

        graphics.drawString(Minecraft.getInstance().font, this.title, 12, y + 5, 0xFFFFFF);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }

    public List<FormattedCharSequence> getTooltip() {
        return this.tooltip;
    }
}
