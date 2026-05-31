package io.github.jamalam360.jamlib.api.network;

import io.github.jamalam360.jamlib.impl.network.NetworkCapabilitiesImpl;
import io.github.jamalam360.jamlib.impl.network.NetworkImpl;
import net.minecraft.server.level.ServerPlayer;

/**
 * Networking system.
 */
public class Network {
    /**
	 * Registers a handler for a packet kind.
     * @param direction The direction the handler should be registered for.
     * @param kind The kind of packet that the handler handles.
     * @param handler The handler.
     */
	public static <T> void registerHandler(PacketDirection direction, PacketKind<T> kind, NetworkPayloadHandler<T> handler) {
		NetworkImpl.registerHandler(direction, kind, handler);
	}

	/**
	 * Sends a packet to the server.
     * @param payload The payload.
     */
	public static <T> void sendToServer(PacketPayload<T> payload) {
		NetworkImpl.sendToServer(payload);
	}

	/**
	 * Sends a payload to the client.
	 * @param payload The payload.
	 */
	public static <T> void sendToClient(ServerPlayer player, PacketPayload<T> payload) {
		NetworkImpl.sendToClient(player, payload);
	}

	/**
	 * Gets the network capability of the logical server that the client is currently connected to.
	 * Should only be called client-side.
	 * @return The server's network capability.
	 */
	public static NetworkCapability getServerCapability() {
		return NetworkCapabilitiesImpl.getServerCapability();
	}

	/**
	 * Gets the network capability of a player connected to the server.
	 * Should only be called server-side.
	 * @param player The player to get the capability of.
	 * @return The player's network capability.
	 */
	public static NetworkCapability getPlayerCapability(ServerPlayer player) {
		return NetworkCapabilitiesImpl.getPlayerCapability(player);
	}
}
