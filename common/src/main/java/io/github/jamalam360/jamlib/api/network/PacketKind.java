package io.github.jamalam360.jamlib.api.network;

import io.github.jamalam360.jamlib.impl.network.PacketKindImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

/**
 * Represents the kind of packet that can be sent over the network.
 * @param <T> The type of the payload, usually a record.
 */
public interface PacketKind<T> {
	/**
	 * @param id A unique identifier for this packet kind.
	 * @param codec A stream codec to encode and decode the payload.
	 * @param <T> The payload type.
	 * @return A {@link PacketKind} implementation.
	 */
	static <T> PacketKind<T> of(Identifier id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		return new PacketKindImpl<>(new PacketIdentifier(id), codec);
	}

	/**
	 * @param id A unique identifier for this packet kind.
	 * @param codec A stream codec to encode and decode the payload.
	 * @param <T> The payload type.
	 * @return A {@link PacketKind} implementation.
	 */
	static <T> PacketKind<T> of(PacketIdentifier id, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		return new PacketKindImpl<>(id, codec);
	}

	/**
	 * @return A unique {@link PacketIdentifier} for this packet kind.
	 */
	PacketIdentifier getIdentifier();

    /**
     * @return A serializer instance for the payload type.
     */
	Serializer<T> getSerializer();

    /**
     * @return A deserializer instance for the payload type.
     */
	Deserializer<T> getDeserializer();

    /**
	 * A serializer for a payload.
     */
	@FunctionalInterface
	interface Serializer<T> {
        /**
		 * Serializes a payload to a buffer.
         * @param object The payload to serialize.
         * @param buf The buffer to write to.
         */
		void serialize(T object, RegistryFriendlyByteBuf buf);
	}

    /**
	 * A deserializer for a payload.
     */
	@FunctionalInterface
	interface Deserializer<T> {
        /**
		 * Deserializes a payload from a buffer.
         * @param buf The buffer to read from.
         * @return The deserialized payload.
         */
		T deserialize(RegistryFriendlyByteBuf buf);
	}
}
