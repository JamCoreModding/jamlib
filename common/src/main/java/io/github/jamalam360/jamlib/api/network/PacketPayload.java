package io.github.jamalam360.jamlib.api.network;

/**
 * A packet payload that can be sent over the network.
 */
public interface PacketPayload<T> {
	/**
	 * @return The associated packet kind of the payload.
	 */
	PacketKind<T> getKind();

	/**
	 * @return The payload.
	 */
	T getPayload();
}
