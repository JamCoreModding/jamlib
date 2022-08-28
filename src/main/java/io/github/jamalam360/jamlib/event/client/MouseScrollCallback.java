package io.github.jamalam360.jamlib.event.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Called on the client-side when the player scrolls their mouse wheel, if
 * they do not have a screen currently open.
 *
 * @author Jamalam
 */

@Environment(EnvType.CLIENT)
public interface MouseScrollCallback {
    Event<MouseScrollCallback> EVENT = EventFactory.createArrayBacked(MouseScrollCallback.class, (listeners) -> (mouseX, mouseY, amount) -> {
        boolean consumed = false;

        for (MouseScrollCallback listener : listeners) {
            if (listener.onMouseScroll(mouseX, mouseY, amount)) {
                consumed = true;
                break;
            }
        }

        return consumed;
    });


    /**
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @param amount The scroll amount.
     * @return Whether the callback performed an action.
     */
    boolean onMouseScroll(double mouseX, double mouseY, double amount);
}
