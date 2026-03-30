package io.github.jamalam360.testmod.network;

import io.github.jamalam360.jamlib.api.network.PayloadType;
import io.github.jamalam360.jamlib.api.network.StreamCodecNetworkPayloadType;
import io.github.jamalam360.testmod.TestMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PotatoPacket implements StreamCodecNetworkPayloadType<PotatoPacket.Payload> {
	public static final PotatoPacket INSTANCE = new PotatoPacket();
	public static final PayloadType<PotatoPacket.Payload> TYPE = new PayloadType<>(TestMod.id("potato"));

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, Payload> getStreamCodec() {
		return StreamCodec.of(
				(buf, payload) -> buf.writeInt(payload.random()),
				(buf) -> new Payload(buf.readInt())
		);
	}

	public record Payload(int random) {}
}
