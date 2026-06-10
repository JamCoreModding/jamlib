package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.api.network.PacketIdentifier;
import io.github.jamalam360.jamlib.api.network.PacketKind;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PacketKindImpl<T>(PacketIdentifier identifier, StreamCodec<RegistryFriendlyByteBuf, T> codec) implements PacketKind<T> {
	@Override
	public PacketIdentifier getIdentifier() {
		return this.identifier();
	}

	@Override
	public Serializer<T> getSerializer() {
		return (object, buf) -> this.codec().encode(buf, object);
	}

	@Override
	public Deserializer<T> getDeserializer() {
		return this.codec()::decode;
	}
}
