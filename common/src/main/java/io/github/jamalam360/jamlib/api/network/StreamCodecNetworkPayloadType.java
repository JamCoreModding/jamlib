package io.github.jamalam360.jamlib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface StreamCodecNetworkPayloadType<T> extends NetworkPayloadType<T> {
	StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec();

	@Override
	default Serializer<T> getSerializer() {
		return (object, buf) -> getStreamCodec().encode(buf, object);
	}

	@Override
	default Deserializer<T> getDeserializer() {
		return buf -> getStreamCodec().decode(buf);
	}
}
