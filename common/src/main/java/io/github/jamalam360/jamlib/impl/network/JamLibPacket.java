package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.PayloadType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record JamLibPacket(PayloadType<?> payloadType, RegistryFriendlyByteBuf payload) implements CustomPacketPayload {
	public static final Identifier JAMLIB_PACKET_ID = JamLib.id("packet");
	public static final CustomPacketPayload.Type<JamLibPacket> TYPE = new CustomPacketPayload.Type<>(JAMLIB_PACKET_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, JamLibPacket> CODEC = StreamCodec.of(
			(buf, packet) -> {
				buf.writeIdentifier(packet.payloadType().id());
				buf.writeBytes(packet.payload());
			},
			(buf) -> {
				Identifier payloadType = buf.readIdentifier();
				RegistryFriendlyByteBuf payload = new RegistryFriendlyByteBuf(buf.readBytes(buf.readableBytes()), buf.registryAccess());
				return new JamLibPacket(new PayloadType<>(payloadType), payload);
			}
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
