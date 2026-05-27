package io.github.jamalam360.testmod;

import io.github.jamalam360.jamlib.api.events.core.EventResult;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.pack.PackReloadListenerRegistry;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandBuilders;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandRegistrationEvent;
import io.github.jamalam360.jamlib.client.api.events.ClientContainerRenderEvents;
import io.github.jamalam360.jamlib.client.api.events.ClientLevelTickEvents;
import io.github.jamalam360.jamlib.client.api.events.ClientMouseScrollEvents;
import io.github.jamalam360.jamlib.client.api.events.ClientConnectionEvents;
import io.github.jamalam360.jamlib.client.api.keymapping.KeyMappingRegistry;
import io.github.jamalam360.testmod.network.PotatoPacket;
import io.github.jamalam360.testmod.pack.TestReloadListener;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import org.lwjgl.glfw.GLFW;

public class TestModClient {
	public static void init() {
		Network.registerHandler(Network.Direction.CLIENT_BOUND, PotatoPacket.TYPE, (ctx, payload) -> {
			TestMod.LOGGER.info("Received potato packet with random: {}", payload.random());
		});

        ClientConnectionEvents.CONNECT.listen(client -> TestMod.LOGGER.info("[C] Joined server!"));
        ClientConnectionEvents.DISCONNECT.listen(client -> TestMod.LOGGER.info("[C] Left server!"));

		KeyMapping mapping = KeyMappingRegistry.register(new KeyMapping("test", GLFW.GLFW_KEY_M, KeyMapping.Category.GAMEPLAY));
		KeyMapping screenMapping = KeyMappingRegistry.register(new KeyMapping("screen", GLFW.GLFW_KEY_U, KeyMapping.Category.GAMEPLAY), true);
		ClientLevelTickEvents.POST_TICK.listen(ignored -> {
			while (mapping.consumeClick()) {
				TestMod.LOGGER.info("Key pressed!");
			}

			while (screenMapping.consumeClick()) {
				TestMod.LOGGER.info("Screen key pressed!");
			}
		});
        PackReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, TestMod.id("test_client_resources"), new TestReloadListener("client_resources"));

        ClientCommandRegistrationEvent.EVENT.listen((dispatcher, context) -> {
            dispatcher.register(ClientCommandBuilders.literal("helloc").executes(c -> {
                c.getSource().client$sendSuccess(Component.literal("Hello, client!"));
                return 1;
            }));
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

		if (Flags.DEBUG_CONTAINER_RENDER_EVENTS) {
			ClientContainerRenderEvents.RENDER_FOREGROUND.listen(((screen, graphics, mouseX, mouseY, delta) -> {
				graphics.centeredText(Minecraft.getInstance().font, "Test mod container render event", mouseX, mouseY, 0xFFFFFFFF);
			}));
		}
	}
}
