package io.github.jamalam360.jamlib.api.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import io.github.jamalam360.jamlib.impl.network.PlatformNetwork;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * A platform-agnostic packet sending system.
 */
public class Network {
	private static final Map<PayloadType<?>, NetworkPayloadType<?>> types = new HashMap<>();
	private static final Map<PayloadType<?>, NetworkPayloadHandler<?>> clientBoundHandlers = new HashMap<>();
	private static final Map<PayloadType<?>, NetworkPayloadHandler<?>> serverBoundHandlers = new HashMap<>();

    /**
	 * Registers a handler for a payload type.
     * @param direction The direction the handler should be registered for.
     * @param id The ID of the payload the handler handles.
     * @param handler The handler.
     */
	public static <T> void registerHandler(Direction direction, PayloadType<T> id, NetworkPayloadHandler<T> handler) {
		Map<PayloadType<?>, NetworkPayloadHandler<?>> handlers = switch (direction) {
			case CLIENT_BOUND -> clientBoundHandlers;
			case SERVER_BOUND -> serverBoundHandlers;
		};

		if (handlers.containsKey(id)) {
			throw new IllegalArgumentException("A handler for the payload type with the id " + id + " is already registered");
		}

		handlers.put(id, handler);
	}

    /**
     * @param id The ID of the payload type.
     * @param type The type of the payload.
     */
	public static <T> void registerPayloadType(PayloadType<T> id, NetworkPayloadType<T> type) {
		if (types.containsKey(id)) {
			throw new IllegalArgumentException("A payload type with the id " + id + " is already registered");
		}

		types.put(id, type);
		JamLib.LOGGER.info("Registered network payload type {}", id.id());
	}

    /**
	 * Sends a payload to the server.
     * @param id The ID of the payload type.
     * @param payload The payload.
     */
	@SuppressWarnings("unchecked")
	public static <T> void sendToServer(PayloadType<T> id, T payload) {
		if (!types.containsKey(id)) {
			throw new IllegalArgumentException("No payload type with the id " + id + " is registered");
		}

		NetworkPayloadType<T> type = (NetworkPayloadType<T>) types.get(id);
		PlatformNetwork.sendToServer(id, type.getSerializer(), payload);
	}

	/**
	 * Sends a payload to the client.
	 * @param id The ID of the payload type.
	 * @param payload The payload.
	 */
	@SuppressWarnings("unchecked")
	public static <T> void sendToClient(ServerPlayer player, PayloadType<T> id, T payload) {
		if (!types.containsKey(id)) {
			throw new IllegalArgumentException("No payload type with the id " + id + " is registered");
		}

		NetworkPayloadType<T> type = (NetworkPayloadType<T>) types.get(id);
		PlatformNetwork.sendToClient(player, id, type.getSerializer(), payload);
	}

	@SuppressWarnings("unchecked")
	@ApiStatus.Internal
	public static <T> void receive(Direction direction, NetworkContext context, JamLibPacket packet) {
		Map<PayloadType<?>, NetworkPayloadHandler<?>> handlers = switch (direction) {
			case CLIENT_BOUND -> clientBoundHandlers;
			case SERVER_BOUND -> serverBoundHandlers;
		};

		if (!types.containsKey(packet.payloadType())) {
			throw new IllegalArgumentException("No payload type with the id " + packet.payloadType() + " is registered");
		}

		if (!handlers.containsKey(packet.payloadType())) {
			throw new IllegalStateException("Received a packet with the id " + packet.payloadType() + " for which no handler is registered");
		}

		NetworkPayloadType<T> type = (NetworkPayloadType<T>) types.get(packet.payloadType());
		NetworkPayloadHandler<T> handler = (NetworkPayloadHandler<T>) handlers.get(packet.payloadType());
		handler.handle(context, type.getDeserializer().deserialize(packet.payload()));
	}

	public enum Direction {
		CLIENT_BOUND,
		SERVER_BOUND
	}
}
