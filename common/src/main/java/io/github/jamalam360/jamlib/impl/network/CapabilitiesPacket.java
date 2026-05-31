package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.PacketKind;
import io.github.jamalam360.jamlib.api.network.PacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;

public record CapabilitiesPacket(List<Identifier> capabilities) implements PacketPayload<CapabilitiesPacket> {
	private static final StreamCodec<RegistryFriendlyByteBuf, CapabilitiesPacket> STREAM_CODEC = ByteBufCodecs
			.<RegistryFriendlyByteBuf, Identifier>list(1_000)
			.apply(StreamCodec.of(
					FriendlyByteBuf::writeIdentifier,
					FriendlyByteBuf::readIdentifier
			))
			.map(CapabilitiesPacket::new, CapabilitiesPacket::capabilities);
	public static final PacketKind<CapabilitiesPacket> KIND = PacketKind.of(JamLib.id("capabilities"), STREAM_CODEC);

	@Override
	public PacketKind<CapabilitiesPacket> getKind() {
		return KIND;
	}

	@Override
	public CapabilitiesPacket getPayload() {
		return this;
	}
}
