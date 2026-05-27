package io.github.jamalam360.jamlib.api.network;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.server.level.ServerPlayer;

/**
 * Events related to networking.
 */
public class NetworkEvents {
	/**
	 * An event called when the server receives capabilities from a client.
	 */
	public static final Event<ClientCapabilitiesHandshakeCompleted> CLIENT_CAPABILITIES_HANDSHAKE_COMPLETED = new Event<>();

	public interface ClientCapabilitiesHandshakeCompleted {
		void onClientCapabilitiesHandshakeCompleted(ServerPlayer player);
	}
}
