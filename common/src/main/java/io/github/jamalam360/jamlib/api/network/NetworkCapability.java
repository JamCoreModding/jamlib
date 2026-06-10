package io.github.jamalam360.jamlib.api.network;

/**
 * Represents the network capabilities of a server or a connected client.
 */
public interface NetworkCapability {
	/**
	 * @param kind The packet kind to check for.
	 * @return {@code true} if the connection that this capability represents can handle the packet kind.
	 */
	default boolean canReceive(PacketKind<?> kind) {
		return this.canReceive(kind.getIdentifier());
	}

	/**
	 * @param identifier The identifier of packet kind to check for.
	 * @return {@code true} if the connection that this capability represents can handle the packet kind.
	 */
	boolean canReceive(PacketIdentifier identifier);
}
