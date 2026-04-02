package io.github.jamalam360.jamlib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * A payload that can be sent over the network.
 * @param <T> The type of the payload, usually a record.
 */
public interface NetworkPayloadType<T> {
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
