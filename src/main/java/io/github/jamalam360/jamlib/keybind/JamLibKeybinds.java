/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Jamalam
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

package io.github.jamalam360.jamlib.keybind;

import com.mojang.blaze3d.platform.InputUtil;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.GLFW;

/**
 * <p>Utility for registering keybinds, also with the ability to register keybinds
 * that call a callback when pressed and then when released (useful for hold rather than toggle actions).</p>
 */
public class JamLibKeybinds {

    private static final List<JamLibKeybindImpl> KEY_BINDS = new ArrayList<>();
    private static final Map<JamLibHoldKeybindImpl, Boolean> HOLD_KEY_BINDS = new Object2BooleanOpenHashMap<>();

    /**
     * @param keyBind An {@link JamLibKeybind} describing your keybind.
     *
     * @return An instance of the vanilla Minecraft {@link KeyBind} class, in case you need it to use in another part of the API.
     */
    public static KeyBind register(JamLibKeybind keyBind) {
        KeyBind keyBindBacker = new KeyBind(
              "key." + keyBind.modId() + "." + keyBind.name(),
              InputUtil.Type.KEYSYM,
              keyBind.keyCode(),
              "key.category." + keyBind.modId()
        );

        KEY_BINDS.add(new JamLibKeybindImpl(
              keyBindBacker,
              keyBind.wasPressedConsumer()
        ));

        KeyBindingHelper.registerKeyBinding(keyBindBacker);

        return keyBindBacker;
    }

    /**
     * This method registers a keybind that calls its callback for every tick that it is held down for.
     *
     * @param keyBind An {@link JamLibHoldKeybind} describing your keybind.
     *
     * @return An instance of the vanilla Minecraft {@link KeyBind} class, in case you need it to use in another part of the API.
     */
    public static KeyBind register(JamLibHoldKeybind keyBind) {
        KeyBind keyBindBacker = new KeyBind(
              "key." + keyBind.modId() + "." + keyBind.name(),
              InputUtil.Type.KEYSYM,
              keyBind.keyCode(),
              "key.category." + keyBind.modId()
        );

        HOLD_KEY_BINDS.put(new JamLibHoldKeybindImpl(
              keyBindBacker,
              keyBind.onPress(),
              keyBind.onRelease()
        ), false);

        KeyBindingHelper.registerKeyBinding(keyBindBacker);

        return keyBindBacker;
    }

    @ApiStatus.Internal
    public static void onEndTick(MinecraftClient client) {
        KEY_BINDS.forEach(keyBind -> {
            while (keyBind.keyBind().wasPressed()) {
                keyBind.wasPressedConsumer().accept(client);
            }
        });

        List<JamLibHoldKeybindImpl> toInvert = new ArrayList<>();

        HOLD_KEY_BINDS.forEach((keyBind, pressed) -> {
            if (keyBind.keyBind().isPressed()) {
                if (!pressed) {
                    keyBind.onPress().accept(client);
                    toInvert.add(keyBind);
                }
            } else {
                if (pressed) {
                    keyBind.onRelease().accept(client);
                    toInvert.add(keyBind);
                }
            }
        });

        toInvert.forEach(keyBind -> HOLD_KEY_BINDS.put(keyBind, !HOLD_KEY_BINDS.get(keyBind)));
    }

    /**
     * Represents a normal, 'toggle' keybind.
     *
     * @param modId              Your mods ID, used for registering the keybind.
     * @param name               The name of your keybind. Used as the {@link net.minecraft.util.Identifier}'s path.
     * @param keyCode            The default keycode for your keybind. Use the constants in {@link GLFW}.
     * @param wasPressedConsumer A callback that takes an {@link MinecraftClient}, and is called after the keybind is pressed.
     */
    public record JamLibKeybind(String modId, String name, int keyCode, Consumer<MinecraftClient> wasPressedConsumer) {
    }

    private record JamLibKeybindImpl(KeyBind keyBind, Consumer<MinecraftClient> wasPressedConsumer) {
    }

    /**
     * Represents a 'hold' keybind. This type calls a callback when first pressed, and then another when unpressed.
     *
     * @param modId     Your mods ID, used for registering the keybind.
     * @param name      The name of your keybind. Used as the {@link net.minecraft.util.Identifier}'s path.
     * @param keyCode   The default keycode for your keybind. Use the constants in {@link GLFW}.
     * @param onPress   A callback that takes an {@link MinecraftClient}, and is called when the keybind is pressed.
     * @param onRelease A callback that takes an {@link MinecraftClient}, and is called when the keybind is released after being held.
     */
    public record JamLibHoldKeybind(String modId, String name, int keyCode, Consumer<MinecraftClient> onPress, Consumer<MinecraftClient> onRelease) {
    }

    private record JamLibHoldKeybindImpl(KeyBind keyBind, Consumer<MinecraftClient> onPress, Consumer<MinecraftClient> onRelease) {
    }
}
