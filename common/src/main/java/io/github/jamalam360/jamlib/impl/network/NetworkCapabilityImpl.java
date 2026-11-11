package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.api.network.NetworkCapability;
import io.github.jamalam360.jamlib.api.network.PacketIdentifier;
import net.minecraft.resources.Identifier;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkCapabilityImpl implements NetworkCapability {
	private final Set<Identifier> supportedTypes = ConcurrentHashMap.newKeySet();

	@Override
	public boolean canReceive(PacketIdentifier identifier) {
		return this.supportedTypes.contains(identifier.identifier());
	}

	public void addSupportedPayloadType(Identifier identifier) {
		this.supportedTypes.add(identifier);
	}

	public void clear() {
		this.supportedTypes.clear();
	}
}
