package io.github.jamalam360.jamlib.impl.network;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.PacketIdentifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record JamLibPacket(PacketIdentifier packetIdentifier,
                           RegistryFriendlyByteBuf payload) implements CustomPacketPayload {
	public static final Identifier JAMLIB_PACKET_ID = JamLib.id("packet");
	public static final CustomPacketPayload.Type<JamLibPacket> TYPE = new CustomPacketPayload.Type<>(JAMLIB_PACKET_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, JamLibPacket> CODEC = StreamCodec.of(
			(buf, packet) -> {
				buf.writeIdentifier(packet.packetIdentifier().identifier());
				int len = packet.payload().writerIndex();
				buf.writeInt(len);
				buf.writeBytes(packet.payload(), 0, len);
			},
			(buf) -> {
				Identifier payloadType = buf.readIdentifier();
				int len = buf.readInt();
				RegistryFriendlyByteBuf payload = new RegistryFriendlyByteBuf(buf.readBytes(len), buf.registryAccess());
				return new JamLibPacket(new PacketIdentifier(payloadType), payload);
			}
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
