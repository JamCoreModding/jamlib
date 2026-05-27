package io.github.jamalam360.jamlib.client.api.network;

import io.github.jamalam360.jamlib.api.events.core.Event;

/**
 * Events related to client networking.
 */
public class ClientNetworkEvents {
	/**
	 * An event called when the client receives capabilities from the logical server.
	 */
	public static final Event<ServerCapabilitiesHandshakeCompleted> SERVER_CAPABILITIES_HANDSHAKE_COMPLETED = new Event<>();

	public interface ServerCapabilitiesHandshakeCompleted {
		void onServerCapabilitiesHandshakeCompleted();
	}
}
