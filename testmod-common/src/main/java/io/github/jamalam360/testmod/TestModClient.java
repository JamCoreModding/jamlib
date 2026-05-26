package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.events.client.ClientPlayLifecycleEvents;

public class TestModClient {
	public static void init() {
        ClientPlayLifecycleEvents.JOIN.register(client -> TestMod.LOGGER.info("Joined server!"));
        ClientPlayLifecycleEvents.DISCONNECT.register(client -> TestMod.LOGGER.info("Left server!"));
	}
}
