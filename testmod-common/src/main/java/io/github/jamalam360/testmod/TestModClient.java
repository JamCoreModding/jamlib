package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.api.events.core.EventResult;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.client.api.events.ClientLevelTickEvents;
import io.github.jamalam360.jamlib.client.api.events.ClientMouseScrollEvents;
import io.github.jamalam360.jamlib.client.api.keymapping.KeyMappingRegistry;
import io.github.jamalam360.testmod.network.PotatoPacket;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class TestModClient {
	public static void init() {
		Network.registerHandler(Network.Direction.CLIENT_BOUND, PotatoPacket.TYPE, (ctx, payload) -> {
			TestMod.LOGGER.info("Received potato packet with random: {}", payload.random());
		});

		KeyMapping mapping = KeyMappingRegistry.register(new KeyMapping("test", GLFW.GLFW_KEY_M, KeyMapping.Category.GAMEPLAY));
		ClientLevelTickEvents.POST_TICK.listen(ignored -> {
			while (mapping.consumeClick()) {
				TestMod.LOGGER.info("Key pressed!");
			}
		});

		if (Flags.DEBUG_MOUSE_SCROLL_EVENTS) {
			ClientMouseScrollEvents.ALWAYS.listen((m, a) -> {
				TestMod.LOGGER.info("Always: {}", a);
				return EventResult.pass();
			});

			ClientMouseScrollEvents.OUT_OF_SCREENS.listen((m, a) -> {
				TestMod.LOGGER.info("Out of screen: {}", a);

				if (m.player.getRandom().nextBoolean()) {
					TestMod.LOGGER.info("Cancelling");
					return EventResult.cancel(null);
				} else {
					TestMod.LOGGER.info("Not cancelling");
					return EventResult.pass();
				}
			});

			ClientMouseScrollEvents.IN_SCREENS.listen((m, a) -> {
				TestMod.LOGGER.info("In screen: {}", a);

				if (m.player.getRandom().nextBoolean()) {
					TestMod.LOGGER.info("Cancelling");
					return EventResult.cancel(null);
				} else {
					TestMod.LOGGER.info("Not cancelling");
					return EventResult.pass();
				}
			});
		}
	}
}
