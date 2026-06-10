package io.github.jamalam360.jamlib.impl.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.PacketKind;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class PlatformNetwork {
	@ExpectPlatform
	public static <T> void sendToServer(PacketKind<T> kind, T payload) {
		JamLib.expectPlatform();
	}

	@ExpectPlatform
	public static <T> void sendToClient(ServerPlayer target, PacketKind<T> kind, T payload) {
		JamLib.expectPlatform();
	}
}
