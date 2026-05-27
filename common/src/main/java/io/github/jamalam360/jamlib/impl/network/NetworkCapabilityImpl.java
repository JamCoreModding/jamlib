package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.api.network.NetworkCapability;
import io.github.jamalam360.jamlib.api.network.PayloadType;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.Set;

public class NetworkCapabilityImpl implements NetworkCapability {
	private final Set<Identifier> supportedTypes = new HashSet<>();

	@Override
	public boolean canReceive(PayloadType<?> type) {
		return this.supportedTypes.contains(type.id());
	}

	public void addSupportedPayloadType(Identifier identifier) {
		this.supportedTypes.add(identifier);
	}
}
