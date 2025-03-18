package io.github.jamalam360.jamlib.client.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A scrollable list of widget groups. Widget groups can have arbitrary heights.
 */
public class WidgetList extends ContainerObjectSelectionList<WidgetList.Entry> {
	public static final int PADDING = 4;

	public WidgetList(Minecraft minecraft, int width, int height) {
		super(minecraft, width, height, 32, height - 32, 1);
		this.centerListVertically = false;
		this.headerHeight = PADDING;
	}

	public void addWidgetGroup(List<AbstractWidget> widgets) {
		this.addEntry(new Entry(widgets));
	}

	public void updateWidgetGroup(int index, List<AbstractWidget> widgets) {
		this.children().set(index, new Entry(widgets));
	}

	@Override
	public int getRowWidth() {
		return this.width;
	}

	@Nullable
	public Entry getRealEntryAtPosition(double mouseX, double mouseY) {
		int halfRowWidth = this.getRowWidth() / 2;
		int centerX = this.x0 + this.width / 2;
		int left = centerX - halfRowWidth;
		int right = centerX + halfRowWidth;
		int m = Mth.floor(mouseY - (double) this.y0) - this.headerHeight + (int) this.getScrollAmount() - 4;

		if (mouseX < left || mouseX > right || m < 0) {
			return null;
		}

		int height = 0;

		for (int idx = 0; idx < this.getItemCount(); idx++) {
			Entry entry = this.getEntry(idx);
			height += entry.getHeight() + PADDING;
			if (m < height) {
				return entry;
			}
		}

		return null;
	}

	@Override
	protected int getMaxPosition() {
		int itemsHeight = 0;

		for (int i = 0; i < this.getItemCount(); i++) {
			itemsHeight += this.getEntry(i).getHeight() + PADDING;
		}

		return itemsHeight + this.headerHeight;
	}

	@Override
	public int getRowTop(int index) {
		int itemsHeight = 0;

		for (int i = 0; i < index; i++) {
			itemsHeight += this.getEntry(i).getHeight() + PADDING;
		}

		return this.y0 - (int) this.getScrollAmount() + itemsHeight + this.headerHeight;
	}

	@Override
	public int getRowBottom(int index) {
		return this.getRowTop(index) + this.getEntry(index).getHeight();
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
		this.setScrollAmount(this.getScrollAmount() - scrollY * 10);
		return true;
	}

	@Override
	protected int getScrollbarPosition() {
		return this.x0 + this.width - 6;
	}

	public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		private final List<AbstractWidget> children;
		private final List<Integer> childYs;

		private Entry(List<AbstractWidget> list) {
			this.children = ImmutableList.copyOf(list);
			this.childYs = this.children.stream().map(AbstractWidget::getY).toList();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
			for (int i = 0; i < this.children.size(); i++) {
				AbstractWidget widget = this.children.get(i);
				int relativeY = this.childYs.get(i);
				widget.setY(top + relativeY);
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

			return maxY;
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
