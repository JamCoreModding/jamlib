package io.github.jamalam360.jamlib.impl.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.NetworkPayloadType;
import io.github.jamalam360.jamlib.api.network.PayloadType;
import net.minecraft.server.level.ServerPlayer;

public class PlatformNetwork {
	@ExpectPlatform
	public static <T> void sendToServer(PayloadType<T> payloadType, NetworkPayloadType.Serializer<T> serializer, T payload) {
		JamLib.expectPlatform();
	}

	@ExpectPlatform
	public static <T> void sendToClient(ServerPlayer target, PayloadType<T> payloadType, NetworkPayloadType.Serializer<T> serializer, T payload) {
		JamLib.expectPlatform();
	}
}
