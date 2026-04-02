package io.github.jamalam360.jamlib.api.events.client;

import io.github.jamalam360.jamlib.events.Event;
import net.minecraft.client.Minecraft;

/**
 * Events for client-side player lifecycle events.
 */
public class ClientPlayLifecycleEvents {
	/**
	 * Called when the local player has joined a logical server.
	 */
	public static final Event<Join> JOIN = new Event<>();
	/**
	 * Called when the local player leaves a logical server.
	 */
	public static final Event<Leave> DISCONNECT = new Event<>();

	@FunctionalInterface
	public interface Join {
		void onJoin(Minecraft client);
	}

	@FunctionalInterface
	public interface Leave {
		void onLeave(Minecraft client);
	}
}
