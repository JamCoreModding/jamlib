package io.github.jamalam360.jamlib.client.api.events;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.client.Minecraft;

/**
 * Events for client-side logical server connections.
 */
public class ClientConnectionEvents {
	/**
	 * Called when the local player has connected to a logical server.
	 */
	public static final Event<Connect> CONNECT = new Event<>();
	/**
	 * Called when the local player disconnects from a logical server.
	 */
	public static final Event<Disconnect> DISCONNECT = new Event<>();

	@FunctionalInterface
	public interface Connect {
		void onConnect(Minecraft client);
	}

	@FunctionalInterface
	public interface Disconnect {
		void onDisconnect(Minecraft client);
	}
}
