package io.github.jamalam360.jamlib.api.network;

public interface PacketPayload<T> {
	PacketKind<T> getKind();
	T getPayload();
}
