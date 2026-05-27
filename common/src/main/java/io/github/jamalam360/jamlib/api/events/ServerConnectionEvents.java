package io.github.jamalam360.jamlib.api.events;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.server.level.ServerPlayer;

/**
 * Events for server-side logical server connections.
 */
public class ServerConnectionEvents {
	/**
	 * Called when a player has connected to a logical server.
	 */
	public static final Event<Connect> CONNECT = new Event<>();
	/**
	 * Called when a player disconnects from a logical server.
	 */
	public static final Event<Disconnect> DISCONNECT = new Event<>();

	@FunctionalInterface
	public interface Connect {
		void onConnect(ServerPlayer player);
	}

	@FunctionalInterface
	public interface Disconnect {
		void onDisconnect(ServerPlayer player);
	}
}
