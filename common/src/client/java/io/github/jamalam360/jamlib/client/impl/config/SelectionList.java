package io.github.jamalam360.jamlib.client.impl.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SelectionList extends ContainerObjectSelectionList<SelectionListEntry> {
	public SelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
		super(minecraft, width, height, y, itemHeight);
		this.centerListVertically = false;
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
