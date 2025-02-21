package io.github.jamalam360.jamlib.client.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WidgetList extends ContainerObjectSelectionList<WidgetList.Entry> {
	public static final int PADDING = 4;

	public WidgetList(Minecraft minecraft, int width, int height, int y) {
		super(minecraft, width, height, y, 1);
		System.out.println(height);
		this.centerListVertically = false;
		this.headerHeight = PADDING;
	}

	public void addWidgetGroup(AbstractWidget... widgets) {
		this.addEntry(new WidgetList.Entry(ImmutableList.copyOf(widgets)));
	}

	public void addWidgetGroup(List<AbstractWidget> widgets) {
		this.addEntry(new WidgetList.Entry(widgets));
	}

	@Override
	public int getRowWidth() {
		return this.width;
	}

	@Nullable
	public Entry getRealEntryAtPosition(double mouseX, double mouseY) {
		int halfRowWidth = this.getRowWidth() / 2;
		int centerX = this.getX() + this.width / 2;
		int left = centerX - halfRowWidth;
		int right = centerX + halfRowWidth;
		int m = Mth.floor(mouseY - (double) this.getY()) - this.headerHeight + (int) this.scrollAmount() - 4;

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
	public int maxScrollAmount() {
//		int height = this.headerHeight;
//
//		for (int i = 0; i < this.getItemCount(); i++) {
//			height += this.getEntry(i).getHeight() + PADDING;
//		}
//
//		return height;
		return super.maxScrollAmount();
	}

	@Override
	protected void renderListItems(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		for (int itemIdx = 0; itemIdx < this.getItemCount(); ++itemIdx) {
			int top = this.getRowTop(itemIdx);
			int bottom = this.getRowBottom(itemIdx);
			if (bottom >= this.getY() && top <= this.getBottom()) {
				this.renderItem(graphics, mouseX, mouseY, partialTick, itemIdx, this.getRowLeft(), top, this.getRowWidth(), this.getEntry(itemIdx).getHeight());
			}
		}
	}

	@Override
	protected int contentHeight() {
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

		return this.getY() - (int) this.scrollAmount() + itemsHeight + this.headerHeight;
	}

	@Override
	public int getRowBottom(int index) {
		return this.getRowTop(index) + this.getEntry(index).getHeight();
	}

	@Override
	protected double scrollRate() {
		return 10;
	}

	@Override
	protected int scrollBarX() {
		return this.getX() + this.width - 6;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.isValidClickButton(button)) {
			this.updateScrolling(mouseX, mouseY, button);
			if (this.isMouseOver(mouseX, mouseY)) {
				Entry entry = this.getRealEntryAtPosition(mouseX, mouseY);
				if (entry != null) {
					if (entry.mouseClicked(mouseX, mouseY, button)) {
						Entry focused = this.getFocused();
						if (focused != entry && focused instanceof ContainerEventHandler eventHandler) {
							eventHandler.setFocused(null);
						}

						this.setFocused(entry);
						this.setDragging(true);
						return true;
					}
				} else {
					// TODO: hmm?
//					return this.clickedHeader((int) (mouseX - (double) (this.getX() + this.width / 2 - this.getRowWidth() / 2)), (int) (mouseY - (double) this.getY()) + (int) this.scrollAmount() - 4);
				}
			}
		}
		return false;
	}

	public class Entry extends ContainerObjectSelectionList.Entry<Entry> {
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

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			for (AbstractWidget widget : this.children) {
				if (widget.isMouseOver(mouseX, mouseY) && widget.mouseClicked(mouseX, mouseY, button)) {
					return true;
				}
			}

			return false;
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
		public boolean isMouseOver(double mouseX, double mouseY) {
			return Objects.equals(WidgetList.this.getRealEntryAtPosition(mouseX, mouseY), this);
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
