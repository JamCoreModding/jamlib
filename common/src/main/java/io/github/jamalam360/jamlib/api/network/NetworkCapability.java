package io.github.jamalam360.jamlib.api.network;

/**
 * Represents the network capabilities of a server or a connected client.
 */
public interface NetworkCapability {
	/**
	 * @param type The type of payload to check for.
	 * @return {@code true} if the connection that this capability represents can handle the packet type.
	 */
	boolean canReceive(PayloadType<?> type);
}
