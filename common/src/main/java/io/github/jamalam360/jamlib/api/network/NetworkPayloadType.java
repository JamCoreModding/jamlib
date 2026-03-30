package io.github.jamalam360.jamlib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface NetworkPayloadType<T> {
	Serializer<T> getSerializer();
	Deserializer<T> getDeserializer();

	@FunctionalInterface
	interface Serializer<T> {
		void serialize(T object, RegistryFriendlyByteBuf buf);
	}

	@FunctionalInterface
	interface Deserializer<T> {
		T deserialize(RegistryFriendlyByteBuf buf);
	}
}
