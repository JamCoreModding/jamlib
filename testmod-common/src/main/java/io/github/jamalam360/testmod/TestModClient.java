package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.events.client.ClientPlayLifecycleEvents;
import io.github.jamalam360.testmod.network.PotatoPacket;

public class TestModClient {
	public static void init() {
		Network.registerHandler(Network.Direction.CLIENT_BOUND, PotatoPacket.TYPE, (ctx, payload) -> {
			TestMod.LOGGER.info("Received potato packet with random: {}", payload.random());
		});

		ClientPlayLifecycleEvents.JOIN.register(client -> TestMod.LOGGER.info("Joined server!"));
        ClientPlayLifecycleEvents.DISCONNECT.register(client -> TestMod.LOGGER.info("Left server!"));
	}
}
