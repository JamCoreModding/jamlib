package io.github.jamalam360.jamlib.api.events;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.server.MinecraftServer;

/**
 * Lifecycle events for the logical server.
 */
public class ServerLifecycleEvents {
	/**
	 * Called when a logical server has started and is about to start ticking.
	 */
	public static final Event<Started> STARTED = new Event<>();
	/**
	 * Called when a logical server has started shutting down.
	 */
	public static final Event<Stopping> STOPPING = new Event<>();
	/**
	 * Called when a logical server has stopped.
	 */
	public static final Event<Stopped> STOPPED = new Event<>();

	@FunctionalInterface
	public interface Started {
		void onStarted(MinecraftServer server);
	}

	@FunctionalInterface
	public interface Stopping {
		void onStopping(MinecraftServer server);
	}

	@FunctionalInterface
	public interface Stopped {
		void onStopped(MinecraftServer server);
	}
}
