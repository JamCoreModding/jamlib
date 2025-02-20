package io.github.jamalam360.jamlib.client.config.gui;

import java.util.List;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
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

        this.renderTitle(graphics, y, 12, x + width / 2 - 10);
    }
    
    // Mainly taken from AbstractWidget
    private void renderTitle(GuiGraphics graphics, int minY, int minX, int maxX) {
        int textWidth = Minecraft.getInstance().font.width(this.title);
        int y = minY + Minecraft.getInstance().font.lineHeight / 2 + 1;
        int width = maxX - minX;
        
        if (textWidth > width) {
            int difference = textWidth - width;
            double nanos = (double)Util.getMillis() / 1000.0;
            double e = Math.max((double) difference * 0.5, 3.0);
            double f = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * nanos / e)) / 2.0 + 0.5;
            double g = Mth.lerp(f, 0.0, difference);
            graphics.enableScissor(minX, minY, maxX, minY + Minecraft.getInstance().font.lineHeight * 2);
            graphics.drawString(Minecraft.getInstance().font, this.title, minX - (int)g, y, 0xFFFFFF);
            graphics.disableScissor();
        } else {
            graphics.drawString(Minecraft.getInstance().font, this.title, minX, y, 0xFFFFFF);
        }
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
