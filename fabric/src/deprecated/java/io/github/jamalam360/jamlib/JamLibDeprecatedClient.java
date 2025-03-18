package io.github.jamalam360.jamlib;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import io.github.jamalam360.jamlib.event.client.MouseScrollCallback;
import io.github.jamalam360.jamlib.keybind.JamLibKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Deprecated
public class JamLibDeprecatedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(JamLibKeybinds::onEndTick);
		ClientRawInputEvent.MOUSE_SCROLLED.register((minecraft, amount) -> {
			double mouseX = minecraft.mouseHandler.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getWidth();
			double mouseY = minecraft.mouseHandler.xpos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getHeight();

			boolean result = MouseScrollCallback.EVENT.invoker().onMouseScroll(mouseX, mouseY, amount);

			if (result) {
				return EventResult.interruptTrue();
			} else {
				return EventResult.pass();
			}
		});
	}
}
