package io.github.jamalam360.testmod.network;

import io.github.jamalam360.jamlib.api.network.PacketKind;
import io.github.jamalam360.jamlib.api.network.PacketPayload;
import io.github.jamalam360.testmod.TestMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PotatoPacket(int random) implements PacketPayload<PotatoPacket> {
	public static final StreamCodec<RegistryFriendlyByteBuf, PotatoPacket> STREAM_CODEC = StreamCodec.of(
		(buf, payload) -> buf.writeInt(payload.random()),
		(buf) -> new PotatoPacket(buf.readInt())
	);
	public static final PacketKind<PotatoPacket> KIND = PacketKind.of(TestMod.id("potato"), STREAM_CODEC);

	@Override
	public PacketKind<PotatoPacket> getKind() {
		return KIND;
	}

	@Override
	public PotatoPacket getPayload() {
		return this;
	}
}
