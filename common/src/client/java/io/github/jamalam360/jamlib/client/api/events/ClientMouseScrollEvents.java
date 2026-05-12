package io.github.jamalam360.jamlib.client.api.events;

import io.github.jamalam360.jamlib.api.events.core.CancellableEvent;
import io.github.jamalam360.jamlib.api.events.core.EventResult;
import net.minecraft.client.Minecraft;

/**
 * Events for the player scrolling their mouse.
 */
public class ClientMouseScrollEvents {
	/**
	 * An event for the player scrolling their mouse within a screen.
	 */
	public static final CancellableEvent<ClientMouseScrollEvents.MouseScroll, Void> IN_SCREENS = new CancellableEvent<>();
	/**
	 * An event for the player scrolling their mouse outside a screen.
	 */
	public static final CancellableEvent<ClientMouseScrollEvents.MouseScroll, Void> OUT_OF_SCREENS = new CancellableEvent<>();
	/**
	 * An event for the player scrolling their mouse.
	 */
	public static final CancellableEvent<ClientMouseScrollEvents.MouseScroll, Void> ALWAYS = new CancellableEvent<>();


	@FunctionalInterface
	public interface MouseScroll {
		EventResult<Void> onScroll(Minecraft client, double amount);
	}
}
