package io.github.jamalam360.jamlib.api.network;

/**
 * A handler for network payloads.
 */
@FunctionalInterface
public interface NetworkPayloadHandler<T> {
    /**
	 * Handle an incoming network payload.
     * @param ctx The network context.
     * @param payload The payload.
     */
	void handle(NetworkContext ctx, T payload);
}
