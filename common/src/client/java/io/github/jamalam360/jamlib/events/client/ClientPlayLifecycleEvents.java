package io.github.jamalam360.jamlib.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.Minecraft;

/**
 * Events for client-side player lifecycle events.
 */
public class ClientPlayLifecycleEvents {
	/**
	 * Called when the local player has joined a logical server.
	 */
	public static final Event<Join> JOIN = EventFactory.createLoop(Join.class);
	/**
	 * Called when the local player leaves a logical server.
	 */
	public static final Event<Leave> DISCONNECT = EventFactory.createLoop(Leave.class);

	@FunctionalInterface
	public interface Join {
		void onJoin(Minecraft client);
	}

	@FunctionalInterface
	public interface Leave {
		void onLeave(Minecraft client);
	}
}
