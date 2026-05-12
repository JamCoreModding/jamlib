package io.github.jamalam360.jamlib.impl.network.neoforge;

import io.github.jamalam360.jamlib.api.network.NetworkPayloadType;
import io.github.jamalam360.jamlib.api.network.PayloadType;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlatformNetworkImpl {
	public static <T> void sendToServer(PayloadType<T> payloadType, NetworkPayloadType.Serializer<T> serializer, T payload) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			throw new IllegalStateException("Attempted to send a play phase packet before a level exists");
		}

		RegistryFriendlyByteBuf payloadData = new RegistryFriendlyByteBuf(Unpooled.buffer(), minecraft.level.registryAccess());
		serializer.serialize(payload, payloadData);
		JamLibPacket packet = new JamLibPacket(payloadType, payloadData);
		ClientPacketDistributor.sendToServer(packet);
	}

	public static <T> void sendToClient(ServerPlayer target, PayloadType<T> payloadType, NetworkPayloadType.Serializer<T> serializer, T payload) {
		RegistryFriendlyByteBuf payloadData = new RegistryFriendlyByteBuf(Unpooled.buffer(), target.level().registryAccess());
		serializer.serialize(payload, payloadData);
		JamLibPacket packet = new JamLibPacket(payloadType, payloadData);
		PacketDistributor.sendToPlayer(target, packet);
	}
}
