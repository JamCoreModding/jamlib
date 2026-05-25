package io.github.jamalam360.jamlib.client.api.events;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Events for injecting into container rendering.
 */
public class ClientContainerRenderEvents {
	/**
	 * An event for rendering after a container's foreground is rendered.
	 */
	public static final Event<Render> RENDER_FOREGROUND = new Event<>();

	@FunctionalInterface
	public interface Render {
		void render(AbstractContainerScreen<?> screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, double delta);
	}
}
