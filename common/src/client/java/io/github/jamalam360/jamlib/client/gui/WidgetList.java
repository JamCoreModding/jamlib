package io.github.jamalam360.jamlib.client.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A scrollable list of widget groups. Widget groups can have arbitrary heights.
 */
public class WidgetList extends ContainerObjectSelectionList<WidgetList.Entry> {
	public static final int PADDING = 4;

	public WidgetList(Minecraft minecraft, int width, int height, int y) {
		super(minecraft, width, height, y, 20);
		this.centerListVertically = false;
	}

	public void addWidgetGroup(List<AbstractWidget> widgets) {
		this.addEntry(new Entry(widgets));
	}

	public void updateWidgetGroup(int index, List<AbstractWidget> widgets) {
		this.children().get(index).updateChildren(widgets);
		this.swap(index, index); // Force reposition of entries
	}

	@Override
	public int getRowWidth() {
		return this.width;
	}

	@Override
	protected int scrollBarX() {
		return this.getX() + this.width - 6;
	}

	public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		private List<AbstractWidget> children;
		private List<Integer> childYs;

		private Entry(List<AbstractWidget> list) {
			this.updateChildren(list);
		}

		@Override
		public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
			for (int i = 0; i < this.children.size(); i++) {
				AbstractWidget widget = this.children.get(i);
				int relativeY = this.childYs.get(i);
				widget.setY(PADDING + this.getY() + relativeY);
				widget.render(guiGraphics, mouseX, mouseY, partialTick);
			}
		}

		public int getHeight() {
			int maxY = 0;

			for (int i = 0; i < this.children.size(); i++) {
				AbstractWidget widget = this.children.get(i);
				int relativeY = this.childYs.get(i);
				maxY = Math.max(relativeY + widget.getHeight(), maxY);
			}

			return maxY + PADDING;
		}

		private void updateChildren(List<AbstractWidget> children) {
			System.out.println("Updating children " + this.children + " with " + children);
			this.children = ImmutableList.copyOf(children);
			this.childYs = this.children.stream().map(AbstractWidget::getY).toList();
		}

		@Override
		public @NotNull List<? extends GuiEventListener> children() {
			return this.children;
		}

		@Override
		public @NotNull List<? extends NarratableEntry> narratables() {
			return this.children;
		}
	}
}
