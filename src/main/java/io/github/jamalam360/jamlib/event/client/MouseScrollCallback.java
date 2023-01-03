/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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