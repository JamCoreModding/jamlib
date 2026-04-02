package io.github.jamalam360.jamlib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * A {@link NetworkPayloadType} that uses a {@link StreamCodec} to encode and decode the payload.
 */
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
