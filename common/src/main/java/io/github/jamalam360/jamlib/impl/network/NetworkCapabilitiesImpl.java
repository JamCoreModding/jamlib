package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.api.network.NetworkEvents;
import io.github.jamalam360.jamlib.api.network.PacketDirection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkCapabilitiesImpl {
	private static final Map<ServerPlayer, NetworkCapabilityImpl> PLAYER_CAPABILITIES = new HashMap<>();
	// Only used client-side
	private static final NetworkCapabilityImpl SERVER_CAPABILITY = new NetworkCapabilityImpl();

	public static NetworkCapabilityImpl getServerCapability() {
		return SERVER_CAPABILITY;
	}

	public static NetworkCapabilityImpl getPlayerCapability(ServerPlayer player) {
		if (!PLAYER_CAPABILITIES.containsKey(player)) {
			JamLib.LOGGER.warn("Player {} does not have a network capability", player.getName().getString());
			return null;
		}

		return PLAYER_CAPABILITIES.get(player);
	}

	public static NetworkCapabilityImpl getOrCreatePlayerCapability(ServerPlayer player) {
		return PLAYER_CAPABILITIES.computeIfAbsent(player, _ -> new NetworkCapabilityImpl());
	}

	public static void onPlayerJoin(ServerPlayer player) {
		List<Identifier> capabilities = NetworkImpl.getRegisteredHandlerTypes(PacketDirection.SERVERBOUND);

		if (PlatformNetwork.canClientReceiveJamLibPackets(player)) {
			JamLib.LOGGER.info("Sending {} network {} to {}", capabilities.size(), capabilities.size() > 1 ? "capabilities" : "capability", player.getName().getString());
			Network.sendToClient(player, new CapabilitiesPacket(capabilities));
		} else {
			JamLib.LOGGER.info("Not sending network capabilities to {} as JamLib is not installed", player.getName().getString());
		}
	}

	public static void handleCapabilities(NetworkContext context, CapabilitiesPacket payload) {
		NetworkCapabilityImpl capabilities = getOrCreatePlayerCapability((ServerPlayer) context.getPlayer());
		payload.capabilities().forEach(capabilities::addSupportedPayloadType);
		JamLib.LOGGER.info("Received {} {} from {}", payload.capabilities().size(), payload.capabilities().size() > 1 ? "capabilities" : "capability", context.getPlayer().getName().getString());
		NetworkEvents.CLIENT_CAPABILITIES_HANDSHAKE_COMPLETED.invoke(l -> l.onClientCapabilitiesHandshakeCompleted((ServerPlayer) context.getPlayer()));
	}
}
