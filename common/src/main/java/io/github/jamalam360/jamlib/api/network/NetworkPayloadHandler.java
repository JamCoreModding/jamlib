package io.github.jamalam360.jamlib.api.network;

@FunctionalInterface
public interface NetworkPayloadHandler<T> {
	void handle(NetworkContext ctx, T payload);
}
