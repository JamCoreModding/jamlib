package io.github.jamalam360.jamlib.client.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.api.network.PacketDirection;
import io.github.jamalam360.jamlib.client.api.network.ClientNetworkEvents;
import io.github.jamalam360.jamlib.impl.network.CapabilitiesPacket;
import io.github.jamalam360.jamlib.impl.network.NetworkCapabilitiesImpl;
import io.github.jamalam360.jamlib.impl.network.NetworkCapabilityImpl;
import io.github.jamalam360.jamlib.impl.network.NetworkImpl;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ClientNetworkCapabilitiesImpl {
	public static void handleServerCapabilities(NetworkContext ctx, CapabilitiesPacket payload) {
		NetworkCapabilityImpl capability = NetworkCapabilitiesImpl.getOrCreateServerCapability();
		payload.capabilities().forEach(capability::addSupportedPayloadType);
		List<Identifier> clientCapabilities = NetworkImpl.getRegisteredHandlerTypes(PacketDirection.CLIENTBOUND);
		Network.sendToServer(new CapabilitiesPacket(clientCapabilities));
		JamLib.LOGGER.info("Received {} network {} from server (responded with {})", payload.capabilities().size(), payload.capabilities().size() > 1 ? "capabilities" : "capability", clientCapabilities.size());
		ClientNetworkEvents.SERVER_CAPABILITIES_HANDSHAKE_COMPLETED.invoke(ClientNetworkEvents.ServerCapabilitiesHandshakeCompleted::onServerCapabilitiesHandshakeCompleted);
	}
}
