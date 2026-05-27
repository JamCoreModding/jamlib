package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.PayloadType;
import io.github.jamalam360.jamlib.api.network.StreamCodecNetworkPayloadType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;

public class CapabilitiesPacket implements StreamCodecNetworkPayloadType<CapabilitiesPacket.Payload> {
	public static final CapabilitiesPacket INSTANCE = new CapabilitiesPacket();
	public static final PayloadType<Payload> TYPE = new PayloadType<>(JamLib.id("capabilities"));
	private static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = ByteBufCodecs
			.<RegistryFriendlyByteBuf, Identifier>list(1_000)
			.apply(StreamCodec.of(
					FriendlyByteBuf::writeIdentifier,
					FriendlyByteBuf::readIdentifier
			))
			.map(Payload::new, Payload::capabilities);

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, Payload> getStreamCodec() {
		return STREAM_CODEC;
	}

	public record Payload(List<Identifier> capabilities) {}
}
