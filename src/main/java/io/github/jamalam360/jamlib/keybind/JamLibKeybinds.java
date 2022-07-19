package io.github.jamalam360.jamlib.keybind;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Jamalam
 */
public class JamLibKeybinds {
    private static final List<JamLibKeybindImpl> KEY_BINDS = new ArrayList<>();

    public static void register(JamLibKeybind keyBind) {
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
    }

    @ApiStatus.Internal
    public static void onEndTick(MinecraftClient client) {
        KEY_BINDS.forEach(keyBind -> {
            while (keyBind.keyBind().wasPressed()) {
                keyBind.wasPressedConsumer().accept(client);
            }
        });
    }

    public record JamLibKeybind(String modId, String name, int keyCode, Consumer<MinecraftClient> wasPressedConsumer) {
    }

    private record JamLibKeybindImpl(KeyBind keyBind, Consumer<MinecraftClient> wasPressedConsumer) {
    }
}
