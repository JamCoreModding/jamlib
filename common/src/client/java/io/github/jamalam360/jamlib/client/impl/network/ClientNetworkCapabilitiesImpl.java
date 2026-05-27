package io.github.jamalam360.jamlib.client.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.client.api.network.ClientNetworkEvents;
import io.github.jamalam360.jamlib.impl.network.CapabilitiesPacket;
import io.github.jamalam360.jamlib.impl.network.NetworkCapabilitiesImpl;
import io.github.jamalam360.jamlib.impl.network.NetworkCapabilityImpl;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ClientNetworkCapabilitiesImpl {
	public static void handleServerCapabilities(NetworkContext ctx, CapabilitiesPacket.Payload payload) {
		NetworkCapabilityImpl capability = NetworkCapabilitiesImpl.getOrCreateServerCapability();
		payload.capabilities().forEach(capability::addSupportedPayloadType);
		List<Identifier> clientCapabilities = Network.getRegisteredHandlerTypes(Network.Direction.CLIENT_BOUND);
		Network.sendToServer(CapabilitiesPacket.TYPE, new CapabilitiesPacket.Payload(clientCapabilities));
		JamLib.LOGGER.info("Received {} network capabilities from server, responded with {}", payload.capabilities().size(), clientCapabilities.size());
		ClientNetworkEvents.SERVER_CAPABILITIES_HANDSHAKE_COMPLETED.invoke(ClientNetworkEvents.ServerCapabilitiesHandshakeCompleted::onServerCapabilitiesHandshakeCompleted);
	}
}
