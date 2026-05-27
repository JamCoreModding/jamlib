package io.github.jamalam360.jamlib.api.network;

public interface NetworkCapability {
	boolean canReceive(PayloadType<?> type);
}
