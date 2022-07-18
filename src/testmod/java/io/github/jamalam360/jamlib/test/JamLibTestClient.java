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

package io.github.jamalam360.jamlib.test;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import net.minecraft.text.Text;

import java.util.Random;

/**
 * @author Jamalam360
 */
public class JamLibTestClient implements ClientModInitializer {
    private static final KeyBind NETWORK_KEYBIND = KeyBindingHelper.registerKeyBinding(
            new KeyBind(
                    "key.jamlib.network",
                    InputUtil.Type.KEYSYM,
                    InputUtil.KEY_K_CODE,
                    "key.category.jamlib"
            )
    );
    public static final Random RANDOM = new Random();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (NETWORK_KEYBIND.wasPressed()) {
                JamLibTestNetwork.NETWORK_KEYBIND_PRESS.send((buf) -> buf.writeInt(RANDOM.nextInt(100)));
            }
        });

        JamLibTestNetwork.NETWORK_KEYBIND_PRESS_RESPONSE.registerHandler(((client, handler, buf, responseSender) -> client.player.sendMessage(Text.literal("Response: " + buf.readInt()), false)));
    }
}
