package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.api.network.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkImpl {
	private static final Map<PacketIdentifier, PacketKind<?>> packetKinds = new HashMap<>();
	private static final Map<PacketIdentifier, NetworkPayloadHandler<?>> clientBoundHandlers = new HashMap<>();
	private static final Map<PacketIdentifier, NetworkPayloadHandler<?>> serverBoundHandlers = new HashMap<>();

	public static <T> void registerHandler(PacketDirection direction, PacketKind<T> kind, NetworkPayloadHandler<T> handler) {
		Map<PacketIdentifier, NetworkPayloadHandler<?>> handlers = switch (direction) {
			case CLIENTBOUND -> clientBoundHandlers;
			case SERVERBOUND -> serverBoundHandlers;
		};

		if (handlers.containsKey(kind.getIdentifier())) {
			throw new IllegalArgumentException("A handler for the payload kind with the id " + kind.getIdentifier() + " is already registered");
		}

		packetKinds.put(kind.getIdentifier(), kind);
		handlers.put(kind.getIdentifier(), handler);
	}

	public static <T> void sendToServer(PacketPayload<T> payload) {
		PlatformNetwork.sendToServer(payload.getKind(), payload.getPayload());
	}

	public static <T> void sendToClient(ServerPlayer player, PacketPayload<T> payload) {
		PlatformNetwork.sendToClient(player, payload.getKind(), payload.getPayload());
	}

	@SuppressWarnings("unchecked")
	@ApiStatus.Internal
	public static <T> void receive(PacketDirection direction, NetworkContext context, JamLibPacket packet) {
		Map<PacketIdentifier, NetworkPayloadHandler<?>> handlers = switch (direction) {
			case CLIENTBOUND -> clientBoundHandlers;
			case SERVERBOUND -> serverBoundHandlers;
		};

		if (!packetKinds.containsKey(packet.packetIdentifier())) {
			throw new IllegalArgumentException("No payload kind with the id " + packet.packetIdentifier() + " (" + direction.name() + ") is registered");
		}

		if (!handlers.containsKey(packet.packetIdentifier())) {
			throw new IllegalStateException("Received a packet with the id " + packet.packetIdentifier() + " (" + direction.name() + ") for which no handler is registered");
		}

		PacketKind<T> kind = (PacketKind<T>) packetKinds.get(packet.packetIdentifier());
		NetworkPayloadHandler<T> handler = (NetworkPayloadHandler<T>) handlers.get(packet.packetIdentifier());
		handler.handle(context, kind.getDeserializer().deserialize(packet.payload()));
	}

	@ApiStatus.Internal
	public static List<Identifier> getRegisteredHandlerTypes(PacketDirection direction) {
		return (direction == PacketDirection.CLIENTBOUND ? clientBoundHandlers : serverBoundHandlers).keySet().stream().map(PacketIdentifier::identifier).toList();
	}
}
